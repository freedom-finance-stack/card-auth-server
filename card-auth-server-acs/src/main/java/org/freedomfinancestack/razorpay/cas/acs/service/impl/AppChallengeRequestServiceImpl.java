package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.extensions.stateMachine.InvalidStateTransactionException;
import org.freedomfinancestack.extensions.stateMachine.StateMachine;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.*;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.CResMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.locator.TransactionTimeoutServiceLocator;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.ChallengeCancelIndicator;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppChallengeRequestServiceImpl implements AppChallengeRequestService {

    private final TransactionService transactionService;
    private final TransactionMessageLogService transactionMessageLogService;
    private final ChallengeRequestValidator challengeRequestValidator;
    private final FeatureService featureService;
    private final AuthenticationServiceLocator authenticationServiceLocator;
    private final ResultRequestService resultRequestService;
    private final ECommIndicatorService eCommIndicatorService;
    private final CardDetailService cardDetailService;
    private final CardRangeService cardRangeService;
    private final CResMapper cResMapper;
    private final AuthValueGeneratorService authValueGeneratorService;
    private final TransactionTimeoutServiceLocator transactionTimeoutServiceLocator;
    private final AppUIGenerator appUIGenerator;
    private final SignerService signerService;

    @Override
    public String processAppChallengeRequest(String strCReq)
            throws ThreeDSException, ACSDataAccessException {
        try {
            return processAppChallengeRequestHandler(strCReq);
        } catch (InvalidStateTransactionException e) {
            log.error(
                    "Invalid State Transaction occurred in processAppChallengeRequest {}",
                    e.getMessage(),
                    e);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    InternalErrorCode.INVALID_STATE_TRANSITION,
                    "Invalid State Transaction occurred",
                    e);
        }
    }

    private String processAppChallengeRequestHandler(String strCReq)
            throws ThreeDSException, ACSDataAccessException, InvalidStateTransactionException {
        CREQ creq;
        Transaction transaction = null;
        AppChallengeFlowDto challengeFlowDto = new AppChallengeFlowDto();
        String cres;
        try {
            // Decrypting CREQ

            creq = signerService.parseEncryptedRequest(strCReq);

            //  find Transaction and previous request, response
            transaction = fetchTransactionData(creq.getAcsTransID());

            //  log creq
            transactionMessageLogService.createAndSave(creq, creq.getAcsTransID());

            // Validating CREQ
            challengeRequestValidator.validateRequest(creq, transaction);

            AuthConfigDto authConfigDto = featureService.getAuthenticationConfig(transaction);

            // Validating this outside, as authConfigDto is needed to validate this
            if (challengeRequestValidator.isWhitelistingDataValid(
                    transaction, creq, authConfigDto)) {
                transaction
                        .getTransactionSdkDetail()
                        .setWhitelistingDataEntry(creq.getWhitelistingDataEntry());
            }

            // Generating App Ui Params
            appUIGenerator.generateAppUIParams(challengeFlowDto, transaction, authConfigDto);

            // 4 flows
            // 1: if Challenge cancelled by user
            // 2: If transaction status is incorrect
            // 3: if resend challenge
            // 4: else perform send/preAuth Challenge

            if (Util.isChallengeCompleted(transaction)) {
                if (transaction
                        .getTransactionStatusReason()
                        .equals(TransactionStatusReason.TRANSACTION_TIMEOUT.getCode())) {
                    throw new ThreeDSException(
                            ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT,
                            "Timeout expiry reached for the transaction",
                            transaction);
                } else {
                    throw new ThreeDSException(
                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                            "Challenge resend threshold exceeded",
                            transaction);
                }
            } else if (!Util.isNullorBlank(creq.getChallengeCancel())) {
                handleCancelChallenge(transaction, challengeFlowDto, creq);
            } else {
                if (creq.getResendChallenge() != null
                        && InternalConstants.YES.equals(creq.getResendChallenge())) {
                    handleReSendChallenge(transaction, authConfigDto, challengeFlowDto);
                } else if (transaction.getPhase().equals(Phase.ARES)) {
                    StateMachine.Trigger(transaction, Phase.PhaseEvent.CREQ_RECEIVED);
                    handleSendChallenge(transaction, authConfigDto, challengeFlowDto);
                } else {
                    if (transaction
                            .getTransactionSdkDetail()
                            .getAcsInterface()
                            .equals(DeviceInterface.HTML.getValue())) {
                        challengeFlowDto.setAuthValue(creq.getChallengeHTMLDataEntry());
                    } else {
                        challengeFlowDto.setAuthValue(creq.getChallengeDataEntry());
                    }
                    handleChallengeValidation(transaction, authConfigDto, challengeFlowDto);
                }
            }

        } catch (ParseException | TransactionDataNotValidException ex) {
            log.error("Exception occurred", ex);
            Transaction transactionErr = new Transaction();
            generateDummyTransactionWithError(ex.getInternalErrorCode(), transactionErr);
            throw new ThreeDSException(
                    ex.getThreeDSecureErrorCode(), ex.getMessage(), transaction, ex);
        } catch (ThreeDSException ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            updateTransactionWithError(ex.getInternalErrorCode(), transaction);
            throw new ThreeDSException(
                    ex.getThreeDSecureErrorCode(), ex.getMessage(), transaction, ex);
        } catch (InvalidStateTransactionException ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            updateTransactionWithError(InternalErrorCode.INVALID_STATE_TRANSITION, transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    ex.getMessage(),
                    transaction,
                    ex);
        } catch (ACSException ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            updateTransactionWithError(ex.getErrorCode(), transaction);
            challengeFlowDto.setCres(cResMapper.toCres(transaction));
        } catch (Exception ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            updateTransactionWithError(InternalErrorCode.INTERNAL_SERVER_ERROR, transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        } finally {
            if (transaction != null) {
                if (Util.isChallengeCompleted(transaction)) {
                    log.info(
                            "Challenge is marked completed for transaction{}", transaction.getId());
                    transactionTimeoutServiceLocator
                            .locateService(MessageType.CReq)
                            .cancelTask(transaction.getId());
                }
                updateEci(transaction);

                if (challengeFlowDto.isSendRreq()) {

                    log.info("Sending Result request for transaction {}", transaction.getId());
                    // sendRreq and if it fails update response
                    try {
                        resultRequestService.handleRreq(transaction);
                    } catch (ThreeDSException ex) {
                        throw new ThreeDSException(
                                ex.getThreeDSecureErrorCode(), ex.getMessage(), transaction, ex);
                    }
                }
                // method to increase a string counter
                // TODO handle these in case above exception is thrown
                transaction.getTransactionSdkDetail().setIncrementedAcsCounterAtoS(1);
                transactionService.saveOrUpdate(transaction);
                if (challengeFlowDto.getCres() != null) {
                    transactionMessageLogService.createAndSave(
                            challengeFlowDto.getCres(), transaction.getId());
                }
            }
        }

        try {
            // Encrypting CRES
            cres = signerService.generateEncryptedResponse(transaction, challengeFlowDto.getCres());
        } catch (ACSException ex) {
            updateTransactionForACSException(ex.getErrorCode(), transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        }

        // encrypt response
        return cres;
    }

    private Transaction fetchTransactionData(String transactionId)
            throws ACSDataAccessException,
                    TransactionDataNotValidException,
                    ACSValidationException {
        if (transactionId == null) {
            throw new ACSValidationException(
                    ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                    "ACS TRANSACTION ID NOT FOUND");
        }
        Transaction transaction = transactionService.findById(transactionId);
        if (null == transaction || !transaction.isChallengeMandated()) {
            throw new TransactionDataNotValidException(InternalErrorCode.TRANSACTION_NOT_FOUND);
        }
        return transaction;
    }

    private void handleReSendChallenge(
            Transaction transaction,
            AuthConfigDto authConfigDto,
            AppChallengeFlowDto challengeFlowDto)
            throws InvalidStateTransactionException, ThreeDSException {
        transaction.setResendCount(transaction.getResendCount() + 1);
        if (transaction.getResendCount()
                > authConfigDto.getChallengeAttemptConfig().getResendThreshold()) {
            log.info(" ReSend threshold exceeded for transaction {}", transaction.getId());
            StateMachine.Trigger(transaction, Phase.PhaseEvent.AUTH_ATTEMPT_EXHAUSTED);
            transaction.setErrorCode(
                    InternalErrorCode.CHALLENGE_RESEND_THRESHOLD_EXCEEDED.getCode());
            transaction.setTransactionStatus(
                    InternalErrorCode.CHALLENGE_RESEND_THRESHOLD_EXCEEDED.getTransactionStatus());
            transaction.setTransactionStatusReason(
                    InternalErrorCode.CHALLENGE_RESEND_THRESHOLD_EXCEEDED
                            .getTransactionStatusReason()
                            .getCode());
            CRES cres =
                    cResMapper.toAppCres(transaction, challengeFlowDto.getInstitutionUIParams());
            challengeFlowDto.setCres(cres);
            transactionMessageLogService.createAndSave(cres, transaction.getId());
            challengeFlowDto.setSendRreq(true);
        } else {
            log.info(" ReSending challenge for transaction {}", transaction.getId());
            transaction.setInteractionCount(transaction.getInteractionCount() + 1);
            StateMachine.Trigger(transaction, Phase.PhaseEvent.RESEND_CHALLENGE);
            handleSendChallenge(transaction, authConfigDto, challengeFlowDto);
        }
    }

    private void updateEci(Transaction transaction) {
        if (!Util.isNullorBlank(transaction.getId())) {
            GenerateECIRequest generateECIRequest =
                    new GenerateECIRequest(
                            transaction.getTransactionStatus(),
                            transaction.getTransactionCardDetail().getNetworkCode(),
                            transaction.getMessageCategory());
            generateECIRequest.setThreeRIInd(transaction.getThreeRIInd());
            String eci = eCommIndicatorService.generateECI(generateECIRequest);
            transaction.setEci(eci);
        }
    }

    private void handleChallengeValidation(
            Transaction transaction,
            AuthConfigDto authConfigDto,
            AppChallengeFlowDto challengeFlowDto)
            throws ThreeDSException, InvalidStateTransactionException, ACSException {
        transaction.setInteractionCount(transaction.getInteractionCount() + 1);
        StateMachine.Trigger(transaction, Phase.PhaseEvent.VALIDATION_REQ_RECEIVED);

        AuthenticationService authenticationService =
                authenticationServiceLocator.locateTransactionAuthenticationService(
                        transaction, authConfigDto.getChallengeAuthTypeConfig());
        AuthResponse authResponse =
                authenticationService.authenticate(
                        AuthenticationDto.builder()
                                .authConfigDto(authConfigDto)
                                .transaction(transaction)
                                .authValue(challengeFlowDto.getAuthValue())
                                .build());

        if (authResponse.isAuthenticated()) {
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            StateMachine.Trigger(transaction, Phase.PhaseEvent.AUTH_VAL_VERIFIED);
            updateEci(transaction);
            transaction.setAuthValue(authValueGeneratorService.getAuthValue(transaction));
            CRES cres = cResMapper.toCres(transaction);
            challengeFlowDto.setCres(cres);

            transactionMessageLogService.createAndSave(cres, transaction.getId());

            challengeFlowDto.setSendRreq(true);
        } else { // If the authentication has failed, is not completed or the Cardholder has
            // selected to cancel the authentication
            if (authConfigDto.getChallengeAttemptConfig().getAttemptThreshold()
                    > transaction.getInteractionCount()) {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.INVALID_AUTH_VAL);
                CRES cres =
                        cResMapper.toAppCres(
                                transaction, challengeFlowDto.getInstitutionUIParams());
                if (transaction
                        .getTransactionSdkDetail()
                        .getAcsInterface()
                        .equals(DeviceInterface.NATIVE.getValue())) {
                    cres.setChallengeInfoText(authResponse.getDisplayMessage());
                }
                challengeFlowDto.setCres(cres);
            } else {
                transaction.setTransactionStatus(
                        InternalErrorCode.EXCEED_MAX_ALLOWED_ATTEMPTS.getTransactionStatus());
                transaction.setTransactionStatusReason(
                        InternalErrorCode.EXCEED_MAX_ALLOWED_ATTEMPTS
                                .getTransactionStatusReason()
                                .getCode());
                transaction.setErrorCode(InternalErrorCode.EXCEED_MAX_ALLOWED_ATTEMPTS.getCode());
                StateMachine.Trigger(transaction, Phase.PhaseEvent.AUTH_ATTEMPT_EXHAUSTED);
                CardRange cardRange =
                        cardRangeService.findByPan(
                                transaction.getTransactionCardDetail().getCardNumber());
                if (authConfigDto.getChallengeAttemptConfig().isBlockOnExceedAttempt()) {
                    cardDetailService.blockCard(
                            new CardDetailsRequest(
                                    transaction.getInstitutionId(),
                                    transaction.getTransactionCardDetail().getCardNumber()),
                            cardRange.getCardDetailsStore());
                }
                CRES cres = cResMapper.toCres(transaction);
                challengeFlowDto.setCres(cres);
                transactionMessageLogService.createAndSave(cres, transaction.getId());
                challengeFlowDto.setSendRreq(true);
            }
        }
    }

    private void handleSendChallenge(
            Transaction transaction,
            AuthConfigDto authConfigDto,
            AppChallengeFlowDto challengeFlowDto)
            throws ThreeDSException, InvalidStateTransactionException {
        AuthenticationService authenticationService =
                authenticationServiceLocator.locateTransactionAuthenticationService(
                        transaction, authConfigDto.getChallengeAuthTypeConfig());
        authenticationService.preAuthenticate(
                AuthenticationDto.builder()
                        .authConfigDto(authConfigDto)
                        .transaction(transaction)
                        .build());
        log.info("Sent challenge for transaction {}", transaction.getId());
        StateMachine.Trigger(transaction, Phase.PhaseEvent.SEND_AUTH_VAL);
        CRES cres = cResMapper.toAppCres(transaction, challengeFlowDto.getInstitutionUIParams());
        challengeFlowDto.setCres(cres);
    }

    private void handleCancelChallenge(
            Transaction transaction, AppChallengeFlowDto challengeFlowDto, CREQ creq)
            throws InvalidStateTransactionException {
        StateMachine.Trigger(transaction, Phase.PhaseEvent.CANCEL_CHALLENGE);

        transaction.setInteractionCount(transaction.getInteractionCount() + 1);
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        transaction.setTransactionStatusReason(
                TransactionStatusReason.EXCEED_MAX_CHALLENGES.getCode());
        transaction.setErrorCode(InternalErrorCode.CANCELLED_BY_CARDHOLDER.getCode());
        transaction.setChallengeCancelInd(creq.getChallengeCancel());

        challengeFlowDto.setSendRreq(true);
        CRES cres = cResMapper.toCres(transaction);
        challengeFlowDto.setCres(cres);
        log.info("challenge cancelled for transaction {}", transaction.getId());
    }

    private void updateTransactionWithError(
            InternalErrorCode internalErrorCode, Transaction transaction)
            throws InvalidStateTransactionException {
        if (transaction != null) {
            if (Util.isNullorBlank(transaction.getChallengeCancelInd())) {
                transaction.setChallengeCancelInd(
                        ChallengeCancelIndicator.TRANSACTION_ERROR.getIndicator());
            }
            transaction.setErrorCode(internalErrorCode.getCode());
            transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
            transaction.setTransactionStatusReason(
                    internalErrorCode.getTransactionStatusReason().getCode());
            StateMachine.Trigger(transaction, Phase.PhaseEvent.ERROR_OCCURRED);
        }
    }

    private void generateDummyTransactionWithError(
            InternalErrorCode internalErrorCode, Transaction transaction) {
        transaction.setErrorCode(internalErrorCode.getCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        transaction.setPhase(Phase.ERROR);
    }

    private void updateTransactionForACSException(
            InternalErrorCode internalErrorCode, Transaction transaction)
            throws ACSDataAccessException {
        transaction.setErrorCode(internalErrorCode.getCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        transactionService.saveOrUpdate(transaction);
    }
}
