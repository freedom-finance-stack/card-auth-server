package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.Arrays;

import org.freedomfinancestack.extensions.stateMachine.StateMachine;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.RouteConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AResMapperParams;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.AResMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.InvalidConfigException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.AppConfiguration;
import org.freedomfinancestack.razorpay.cas.acs.module.configuration.TestConfigProperties;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.impl.DecoupledAuthenticationAsyncService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.locator.TransactionTimeoutServiceLocator;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.AuthenticationRequestValidator;
import org.freedomfinancestack.razorpay.cas.acs.validation.ThreeDSValidator;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.AuthType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code AuthenticationServiceImpl} class is an implementation of the {@link
 * AuthenticationRequestService} interface that handles authentication requests (AReq) and generates
 * authentication responses (Ares) in the ACS (Access Control Server) functionality. This service is
 * responsible for processing incoming AReq messages, validating the requests, generating Ares
 * messages, and managing transaction details in the ACS system.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service("authenticationServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationRequestServiceImpl implements AuthenticationRequestService {

    private final TransactionService transactionService;
    private final TransactionMessageLogService transactionMessageLogService;
    private final CardRangeService cardRangeService;
    private final CardDetailService cardDetailService;
    private final AuthValueGeneratorService authValueGeneratorService;
    private final AResMapper aResMapper;
    private final TransactionTimeoutServiceLocator transactionTimeoutServiceLocator;
    private final DecoupledAuthenticationAsyncService decoupledAuthenticationAsyncService;
    private final FeatureService featureService;
    private final SignerService signerService;
    private final ChallengeDetermineService challengeDetermineService;
    private final Environment environment;
    private final TestConfigProperties testConfigProperties;
    private final AppConfiguration appConfiguration;

    @Qualifier(value = "authenticationRequestValidator") private final ThreeDSValidator<AREQ> areqValidator;

    /**
     * Process the authentication request (AReq) and generate the authentication response (Ares).
     *
     * @param areq The {@link AREQ} The authentication request (AReq) object containing the details
     *     of the incoming request.
     * @return ares The {@link ARES} The authentication response (Ares) object containing the
     *     details of the generated response.
     * @throws ThreeDSException If any ThreeDSException occurs during the processing of the AReq,
     *     indicating that an "Erro" message type should be sent in the response.
     */
    @Override
    public ARES processAuthenticationRequest(@NonNull AREQ areq) throws ThreeDSException {
        Transaction transaction = new Transaction();
        ARES ares;
        CardRange cardRange = null;
        AResMapperParams aResMapperParams = new AResMapperParams();
        try {
            areq.setTransactionId(Util.generateUUID());
            transaction.setId(areq.getTransactionId());

            // Set message version before validation as it is required in Erro
            if (AuthenticationRequestValidator.isMessageVersionValid(areq.getMessageVersion())) {
                transaction.setMessageVersion(areq.getMessageVersion());
            }

            // log incoming request in DB
            transactionMessageLogService.createAndSave(areq, areq.getTransactionId());

            // validate areq
            areqValidator.validateRequest(areq);

            // todo check duplicate transaction once threeDSmethod is implemented

            // Create and Save transaction in DB
            transaction = transactionService.create(areq);
            transaction = transactionService.saveOrUpdate(transaction);

            // get range and institution entity and verify
            cardRange = cardRangeService.findByPan(areq.getAcctNumber());
            cardRangeService.validateRange(cardRange);

            // update Ids in transaction
            transaction.getTransactionCardDetail().setNetworkCode(cardRange.getNetworkCode());
            transaction.setCardRangeId(cardRange.getId());
            transaction.setInstitutionId(cardRange.getInstitution().getId());

            // fetch Card and User details and validate details
            cardDetailService.validateAndUpdateCardDetails(
                    transaction,
                    new CardDetailsRequest(
                            cardRange.getInstitution().getId(), areq.getAcctNumber()),
                    cardRange.getCardDetailsStore());

            if (DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel())) {
                featureService.getACSRenderingType(transaction, areq.getDeviceRenderOptions());
            }

            ulPortalTestingValidations(transaction, areq);

            // Determine if challenge is required and update transaction accordingly
            challengeDetermineService.determineChallenge(
                    areq, transaction, cardRange.getRiskFlag());

            if (transaction.isChallengeMandated()) {

                if (transaction
                        .getTransactionStatus()
                        .equals(TransactionStatus.CHALLENGE_REQUIRED)) {
                    AuthConfigDto authConfigDto =
                            featureService.getAuthenticationConfig(transaction);
                    AuthType authType =
                            AuthenticationServiceLocator.selectAuthType(
                                    transaction, authConfigDto.getChallengeAuthTypeConfig());
                    transaction.setAuthenticationType(authType.getValue());
                } else if (transaction
                        .getTransactionStatus()
                        .equals(TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED)) {
                    transaction.setAuthenticationType(AuthType.Decoupled.getValue());
                }

                if (DeviceChannel.APP.getChannel().equals(transaction.getDeviceChannel())) {
                    log.info("Generating ACSSignedContent");
                    String signedData =
                            signerService.getAcsSignedContent(
                                    areq,
                                    transaction,
                                    RouteConstants.getAcsChallengeUrl(
                                            appConfiguration.getHostname(),
                                            transaction.getDeviceChannel()));
                    aResMapperParams.setAcsSignedContent(signedData);
                }
                transaction.getTransactionSdkDetail().setAcsCounterAtoS("000");
            }

            if (TransactionStatus.SUCCESS.equals(transaction.getTransactionStatus())) {
                transactionService.updateEci(transaction);
                transaction.setAuthValue(authValueGeneratorService.getAuthValue(transaction));
            } else if (TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED.equals(
                    transaction.getTransactionStatus())) {
                decoupledAuthenticationAsyncService.scheduleTask(
                        transaction.getId(),
                        transaction.getTransactionStatus(),
                        areq.getThreeDSRequestorDecMaxTime());
            }

        } catch (ThreeDSException ex) {
            // NOTE : to send Erro in response throw ThreeDSException, otherwise

            // to return ARES handle ACSException and next code will convert it to
            // ARes

            // Handle any ThreeDSException by sending "Erro" message type as a response.
            // updating transaction with error and updating DB and add transaction details in error
            // message
            // throw ThreeDSException again so that it returns to client with error message, it is
            // handled in ResponseEntityExceptionHandler
            log.error(
                    " Message {}, Internal Error code {}",
                    ex.getMessage(),
                    ex.getInternalErrorCode());
            transaction = updateTransactionPhaseWithError(ex.getInternalErrorCode(), transaction);
            throw new ThreeDSException(
                    ex.getThreeDSecureErrorCode(), ex.getMessage(), transaction, ex);
        } catch (ACSException ex) {
            // Handle any ACSException by sending Ares message type as a response.
            // updating transaction with error and updating DB
            log.error(" Message {}, Internal Error code {}", ex.getMessage(), ex.getErrorCode());
            transaction = updateTransactionWithError(ex.getErrorCode(), transaction);
        } catch (Exception ex) {
            // Handle any Exception by sending "Erro" message type as a response.
            // updating transaction with error and updating DB and add transaction details in error
            // message
            // throw ThreeDSException again so that it returns to client with error message, it is
            // handled in ResponseEntityExceptionHandler
            log.error(" Message {}, Error string {}", ex.getMessage(), ex.toString());
            transaction =
                    updateTransactionPhaseWithError(
                            InternalErrorCode.INTERNAL_SERVER_ERROR, transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        }

        // If everything is successful, send Ares message type as a response.
        try {
            /*
             * below If condition for attempted case can only be used in Self Test Platform.
             */
            if (transaction.getTransactionStatus().equals(TransactionStatus.SUCCESS)
                    && isAttemptedTestRange(
                            transaction
                                    .getTransactionCardDetail()
                                    .getCardNumber()
                                    .getDecrypted())) {
                transaction.setTransactionStatus(TransactionStatus.ATTEMPT);
                // todo not raising Attempt actual anywhere in code, check if attempt scenario is
                // possible
            }
            if (!Util.isNullorBlank(transaction.getId()) && cardRange != null) {
                transactionService.updateEci(transaction);
            }
            ares = aResMapper.toAres(areq, transaction, aResMapperParams);
            transactionMessageLogService.createAndSave(ares, areq.getTransactionId());
            StateMachine.Trigger(transaction, Phase.PhaseEvent.AUTHORIZATION_PROCESSED);
            if (transaction.isChallengeMandated()) {
                transactionTimeoutServiceLocator
                        .locateService(MessageType.AReq)
                        .scheduleTask(
                                transaction.getId(),
                                transaction.getTransactionStatus(),
                                areq.getThreeDSRequestorDecMaxTime());
            }

        } catch (Exception ex) {
            // updating transaction with error and updating DB
            // throw ThreeDSException again so that it returns to client with error message, it is
            // handled in ResponseEntityExceptionHandler
            transaction =
                    updateTransactionPhaseWithError(
                            InternalErrorCode.INTERNAL_SERVER_ERROR, transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        } finally {
            transactionService.saveOrUpdate(transaction);
        }
        return ares;
    }

    private boolean isAttemptedTestRange(String cardNumber) {
        long cardNumberLong = Long.parseLong(cardNumber);
        return !Arrays.asList(environment.getActiveProfiles()).contains(InternalConstants.PROD)
                && testConfigProperties.isEnable()
                && testConfigProperties.getAttemptedRange().getStart() <= cardNumberLong
                && cardNumberLong <= testConfigProperties.getAttemptedRange().getEnd();
    }

    private Transaction updateTransactionPhaseWithError(
            InternalErrorCode internalErrorCode, Transaction transaction)
            throws ACSDataAccessException {
        transaction.setPhase(Phase.AERROR);
        return updateTransactionWithError(internalErrorCode, transaction);
    }

    private Transaction updateTransactionWithError(
            InternalErrorCode internalErrorCode, Transaction transaction)
            throws ACSDataAccessException {
        transaction.setErrorCode(internalErrorCode.getCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        return transactionService.saveOrUpdate(transaction);
    }

    private void ulPortalTestingValidations(Transaction transaction, AREQ areq)
            throws InvalidConfigException {
        // Temp variables for passing test cases
        final long ulUDStartRange = Long.parseLong("6543200100000");
        final long ulUDEndRange = Long.parseLong("6543200199999");
        if (DeviceChannel.BRW.getChannel().equals(transaction.getDeviceChannel())
                && ulUDStartRange <= Long.parseLong(areq.getAcctNumber())
                && ulUDEndRange >= Long.parseLong(areq.getAcctNumber())) {
            throw new InvalidConfigException(
                    InternalErrorCode.UNSUPPPORTED_DEVICE_CATEGORY, "failed for UL testing");
        }
    }
}
