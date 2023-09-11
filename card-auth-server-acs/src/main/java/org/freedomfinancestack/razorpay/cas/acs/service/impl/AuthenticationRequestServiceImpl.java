package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.AResMapperParams;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.GenerateECIRequest;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.AResMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.AuthenticationRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.CardRangeService;
import org.freedomfinancestack.razorpay.cas.acs.service.ECommIndicatorService;
import org.freedomfinancestack.razorpay.cas.acs.service.InstitutionAcsUrlService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageTypeService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ThreeDSValidator;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ARES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.RiskFlag;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionAcsUrl;
import org.freedomfinancestack.razorpay.cas.dao.model.InstitutionAcsUrlPK;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.StateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final TransactionMessageTypeService transactionMessageTypeService;
    private final CardRangeService cardRangeService;
    private final CardDetailService cardDetailService;
    private final AuthValueGeneratorService authValueGeneratorService;
    private final ECommIndicatorService eCommIndicatorService;
    private final AResMapper aResMapper;
    private final InstitutionAcsUrlService institutionAcsUrlService;

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
     * @throws ACSDataAccessException If any ACSDataAccessException occurs during the processing of
     *     the AReq, indicating that an "Erro" message type should be sent in the response.
     */
    @Override
    public ARES processAuthenticationRequest(@NonNull AREQ areq)
            throws ThreeDSException, ACSDataAccessException {
        Transaction transaction = new Transaction();
        InstitutionAcsUrl acsUrl = null;
        ARES ares;
        CardRange cardRange = null;
        try {
            areq.setTransactionId(Util.generateUUID());
            transaction.setId(areq.getTransactionId());
            // log incoming request in DB
            transactionMessageTypeService.createAndSave(areq, areq.getTransactionId());
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

            // get acs url
            acsUrl =
                    institutionAcsUrlService.findById(
                            new InstitutionAcsUrlPK(
                                    cardRange.getInstitution().getId(),
                                    areq.getDeviceChannel(),
                                    cardRange.getNetworkCode()));

            // fetch Card and User details and validate details
            cardDetailService.validateAndUpdateCardDetails(
                    transaction,
                    new CardDetailsRequest(
                            cardRange.getInstitution().getId(), areq.getAcctNumber()),
                    cardRange.getCardDetailsStore());

            // Determine if challenge is required and update transaction accordingly
            determineChallenge(areq, transaction, cardRange);
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
            transaction =
                    updateTransactionPhaseWithError(
                            ex.getThreeDSecureErrorCode(), ex.getInternalErrorCode(), transaction);
            throw new ThreeDSException(
                    ex.getThreeDSecureErrorCode(), ex.getMessage(), transaction, ex);
        } catch (ACSException ex) {
            // Handle any ACSException by sending Ares message type as a response.
            // updating transaction with error and updating DB
            log.error(" Message {}, Internal Error code {}", ex.getMessage(), ex.getErrorCode());
            transaction = updateTransactionForACSException(ex.getErrorCode(), transaction);
        } catch (Exception ex) {
            // Handle any Exception by sending "Erro" message type as a response.
            // updating transaction with error and updating DB and add transaction details in error
            // message
            // throw ThreeDSException again so that it returns to client with error message, it is
            // handled in ResponseEntityExceptionHandler
            log.error(" Message {}, Error string {}", ex.getMessage(), ex.toString());
            transaction =
                    updateTransactionPhaseWithError(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            InternalErrorCode.INTERNAL_SERVER_ERROR,
                            transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        }

        // If everything is successful, send Ares message type as a response.
        try {
            String eci =
                    eCommIndicatorService.generateECI(
                            new GenerateECIRequest(
                                            transaction.getTransactionStatus(),
                                            cardRange.getNetworkCode(),
                                            transaction.getMessageCategory())
                                    .setThreeRIInd(areq.getThreeRIInd()));
            transaction.setEci(eci);

            AResMapperParams aResMapperParams = AResMapperParams.builder().build();
            ares = aResMapper.toAres(areq, transaction, acsUrl, aResMapperParams);

            transactionMessageTypeService.createAndSave(ares, areq.getTransactionId());
            StateMachine.Trigger(transaction, Phase.PhaseEvent.AUTHORIZATION_PROCESSED);
        } catch (Exception ex) {
            // updating transaction with error and updating DB
            // throw ThreeDSException again so that it returns to client with error message, it is
            // handled in ResponseEntityExceptionHandler
            transaction =
                    updateTransactionPhaseWithError(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            InternalErrorCode.INTERNAL_SERVER_ERROR,
                            transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        } finally {
            transactionService.saveOrUpdate(transaction);
        }
        return ares;
    }

    private void determineChallenge(AREQ areq, Transaction transaction, CardRange cardRange)
            throws ACSException, ThreeDSException {
        if (isChallengeRequired(cardRange.getRiskFlag(), transaction)) {
            transaction.setChallengeMandated(true);
            // todo add timer logic for challenge
            transaction.setTransactionStatus(TransactionStatus.CHALLENGE_REQUIRED);
        } else {
            transaction.setChallengeMandated(false);
            String eci =
                    eCommIndicatorService.generateECI(
                            new GenerateECIRequest(
                                            transaction.getTransactionStatus(),
                                            cardRange.getNetworkCode(),
                                            transaction.getMessageCategory())
                                    .setThreeRIInd(areq.getThreeRIInd()));
            transaction.setEci(eci);
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            String authValue = authValueGeneratorService.getAuthValue(transaction);
            transaction.setAuthValue(authValue);
        }
    }

    private boolean isChallengeRequired(RiskFlag riskFlag, Transaction transaction) {
        // todo honor ThreeDSRequestorChallsengeInd once RBA is implemented
        if (riskFlag.equals(RiskFlag.NO_CHALLENGE)) {
            return false;
        } else if (riskFlag.equals(RiskFlag.CHALLENGE)) {
            return true;
        } else { // RBA
            throw new UnsupportedOperationException("RBA is not supported yet");
        }
    }

    private Transaction updateTransactionPhaseWithError(
            ThreeDSecureErrorCode threeDSecureErrorCode,
            InternalErrorCode internalErrorCode,
            Transaction transaction)
            throws ACSDataAccessException {
        transaction.setErrorCode(internalErrorCode.getCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setPhase(Phase.AERROR);
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        return transactionService.saveOrUpdate(transaction);
    }

    private Transaction updateTransactionForACSException(
            InternalErrorCode internalErrorCode, Transaction transaction)
            throws ACSDataAccessException {
        transaction.setErrorCode(internalErrorCode.getCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        return transactionService.saveOrUpdate(transaction);
    }
}
