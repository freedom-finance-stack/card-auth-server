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
import org.freedomfinancestack.razorpay.cas.acs.service.parser.ChallengeRequestParserFactory;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.locator.TransactionTimeoutServiceLocator;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceChannel;
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
public class ChallengeRequestServiceImpl implements ChallengeRequestService {

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
    private final ChallengeRequestParserFactory challengeRequestParserFactory;

    @Override
    public ChallengeFlowDto processChallengeRequest(
            final DeviceChannel flowType, final String strCReq, final String threeDSSessionData)
            throws ThreeDSException, ACSDataAccessException {
        if (flowType.equals(DeviceChannel.APP)) {
            return processAppChallengeRequestHandler(flowType, strCReq, threeDSSessionData);
        }
        return processBrwChallengeRequestHandler(flowType, strCReq, threeDSSessionData);
    }

    public ChallengeFlowDto processAppChallengeRequestHandler(
            DeviceChannel flowType, String strCReq, String threeDSSessionData)
            throws ThreeDSException, ACSDataAccessException {
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        try {
            processChallengeRequestHandler(challengeFlowDto, flowType, strCReq, threeDSSessionData);
            String cres;
            try {
                cres =
                        challengeRequestParserFactory
                                .getService(flowType)
                                .generateEncryptedResponse(
                                        challengeFlowDto, challengeFlowDto.getTransaction());
            } catch (ACSException ex) {
                updateTransactionForACSException(
                        ex.getErrorCode(), challengeFlowDto.getTransaction());
                throw new ThreeDSException(
                        ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                        ex.getMessage(),
                        challengeFlowDto.getTransaction(),
                        ex);
            }
            challengeFlowDto.setEncryptedResponse(cres);
            return challengeFlowDto;
        } catch (InvalidStateTransactionException e) { // todo handle ACSDataAccessException
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

    public ChallengeFlowDto processBrwChallengeRequestHandler(
            final DeviceChannel flowType, final String strCReq, final String threeDSSessionData) {
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        try {
            processChallengeRequestHandler(challengeFlowDto, flowType, strCReq, threeDSSessionData);
            // 3 things
            // UI
            // Final Output
            // Exception handling
        } catch (InvalidStateTransactionException | ACSDataAccessException e) {
            log.error(
                    "Invalid State Transaction occurred in processAppChallengeRequest {}",
                    e.getMessage(),
                    e);
            challengeFlowDto.setErrorResponse(
                    new ThreeDSException(
                                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                                    InternalErrorCode.INVALID_STATE_TRANSITION,
                                    "Invalid State Transaction occurred",
                                    e)
                            .getErrorResponse());
        } catch (ThreeDSException ex) {
            challengeFlowDto.setErrorResponse(ex.getErrorResponse());
        } finally {
            try {
                if (Util.isChallengeCompleted(challengeFlowDto.getTransaction())) {
                    challengeFlowDto.setEncryptedResponse(
                            challengeRequestParserFactory
                                    .getService(flowType)
                                    .generateEncryptedResponse(
                                            challengeFlowDto, challengeFlowDto.getTransaction()));
                }

            } catch (ACSException acsException) {
                // this case will never occur
                challengeFlowDto.setSendEmptyResponse(true);
            }
        }
        return challengeFlowDto;
    }

    private void processChallengeRequestHandler(
            final ChallengeFlowDto challengeFlowDto,
            final DeviceChannel flowType,
            final String strCReq,
            final String threeDSSessionData)
            throws ThreeDSException, InvalidStateTransactionException, ACSDataAccessException {
        Transaction transaction = null;
        CREQ creq;
        try {
            creq =
                    challengeRequestParserFactory
                            .getService(flowType)
                            .parseEncryptedRequest(strCReq);
            //  find Transaction and previous request, response
            transaction = fetchTransactionData(creq.getAcsTransID());
            challengeFlowDto.setTransaction(transaction);
            challengeFlowDto.setNotificationUrl(
                    transaction.getTransactionReferenceDetail().getNotificationUrl());

            //  log creq
            transactionMessageLogService.createAndSave(creq, creq.getAcsTransID());

            // Validating CREQ
            challengeRequestValidator.validateRequest(creq, transaction);
            if (!Util.isNullorBlank(threeDSSessionData)) {
                if (!Util.isValidBase64Url(threeDSSessionData)) {
                    throw new ACSValidationException(
                            ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                            " ThreeDsSessionData incorrect");
                }
                transaction.setThreedsSessionData(
                        Util.removeBase64Padding(
                                threeDSSessionData)); // removing base64 padding because Padding not
            }

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
                            InternalErrorCode.TRANSACTION_TIMED_OUT_CHALLENGE_COMPLETION,
                            "Timeout expiry reached for the transaction");
                } else {
                    throw new ThreeDSException(
                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                            InternalErrorCode.INVALID_REQUEST,
                            "Transaction already in completed state");
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
                    if (flowType.equals(DeviceChannel.BRW)
                            || transaction
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
            // don't send Rres for ParseException
            if (ex.getInternalErrorCode().equals(InternalErrorCode.TRANSACTION_ID_EMPTY)
                    || ex.getInternalErrorCode()
                            .equals(InternalErrorCode.CREQ_JSON_PARSING_ERROR)) {
                challengeFlowDto.setSendEmptyResponse(true);
            }
            Transaction transactionErr = new Transaction();
            generateDummyTransactionWithError(ex.getInternalErrorCode(), transactionErr);
            throw new ThreeDSException(
                    ex.getThreeDSecureErrorCode(), ex.getMessage(), transactionErr, ex);
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
                    challengeFlowDto.setThreeDSSessionData(transaction.getThreedsSessionData());
                }
                updateEci(transaction);

                if (challengeFlowDto.isSendRreq()) {
                    log.info("Sending Result request for transaction {}", transaction.getId());
                    try {
                        resultRequestService.handleRreq(transaction);
                    } catch (ThreeDSException ex) {
                        if (DeviceChannel.BRW.equals(flowType)) {
                            CRES cres = cResMapper.toCres(transaction);
                            challengeFlowDto.setCres(cres);
                        } else {
                            throw new ThreeDSException(
                                    ex.getThreeDSecureErrorCode(),
                                    ex.getMessage(),
                                    transaction,
                                    ex);
                        }
                    }
                }
                // TODO handle these in case above exception is thrown
                if (flowType.equals(DeviceChannel.APP)) {
                    transaction.getTransactionSdkDetail().setIncrementedAcsCounterAtoS(1);
                }
                transactionService.saveOrUpdate(transaction);
                if (challengeFlowDto.getCres() != null) {
                    transactionMessageLogService.createAndSave(
                            challengeFlowDto.getCres(), transaction.getId());
                }
            }
        }
    }

    private Transaction fetchTransactionData(String transactionId)
            throws ACSDataAccessException, TransactionDataNotValidException {
        if (transactionId == null) {
            throw new TransactionDataNotValidException(
                    ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                    InternalErrorCode.TRANSACTION_ID_NOT_RECOGNISED);
        }
        Transaction transaction = transactionService.findById(transactionId);
        if (null == transaction || !transaction.isChallengeMandated()) {
            throw new TransactionDataNotValidException(InternalErrorCode.TRANSACTION_NOT_FOUND);
        }
        return transaction;
    }

    private void handleReSendChallenge(
            Transaction transaction, AuthConfigDto authConfigDto, ChallengeFlowDto challengeFlowDto)
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
            Transaction transaction, AuthConfigDto authConfigDto, ChallengeFlowDto challengeFlowDto)
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
            Transaction transaction, AuthConfigDto authConfigDto, ChallengeFlowDto challengeFlowDto)
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
            Transaction transaction, ChallengeFlowDto challengeFlowDto, CREQ creq)
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
