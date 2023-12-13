package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.extensions.stateMachine.InvalidStateTransactionException;
import org.freedomfinancestack.extensions.stateMachine.StateMachine;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.*;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.CResMapper;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.CdResMapperImpl;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.*;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.gateway.proprietaryul.PlrqService;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.FeatureService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.freedomfinancestack.razorpay.cas.acs.service.timer.locator.TransactionTimeoutServiceLocator;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeValidationRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.decodeBase64;
import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.fromJson;

/**
 * The {@code ChallengeRequestServiceImpl} class is an implementation of the {@link
 * ChallengeRequestService}
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeRequestServiceImpl implements ChallengeRequestService {

    private final TransactionService transactionService;
    private final TransactionMessageLogService transactionMessageLogService;
    private final ChallengeRequestValidator challengeRequestValidator;
    private final FeatureService featureService;
    private final AuthenticationServiceLocator authenticationServiceLocator;
    private final CdResMapperImpl cdResMapper;
    private final ResultRequestService resultRequestService;
    private final ECommIndicatorService eCommIndicatorService;
    private final CardDetailService cardDetailService;
    private final CardRangeService cardRangeService;
    private final CResMapper cResMapper;
    private final AuthValueGeneratorService authValueGeneratorService;
    private final ChallengeValidationRequestValidator challengeValidationRequestValidator;
    private final TransactionTimeoutServiceLocator transactionTimeoutServiceLocator;
    private final PlrqService plrqService;

    @Override
    public CdRes processBrwChallengeRequest(
            @NotNull final String strCReq, final String threeDSSessionData) {
        CdRes cdReqError = new CdRes();
        try {
            return processBrwChallengeRequestHandler(strCReq, threeDSSessionData);
        } catch (Exception e) {
            log.error(
                    "Unhandled error occurred in processBrwChallengeValidationRequest {}",
                    e.getMessage(),
                    e);
            // add alert for this. Ideally this should not happen. All error handled here won't send
            // Rres and won't update transactions
            cdReqError.setError(true);
            cdReqError.setEncryptedCRes(null);
            ThreeDSErrorResponse errorObj =
                    Util.generateErrorResponse(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            null,
                            "Unexpected error while validating challenge",
                            MessageType.CReq);
            cdReqError.setEncryptedErro(Util.encodeBase64Url(errorObj));
        }
        return cdReqError;
    }

    public CdRes processBrwChallengeRequestHandler(
            @NotNull final String strCReq, final String threeDSSessionData)
            throws ACSDataAccessException,
                    InvalidStateTransactionException,
                    OperationNotSupportedException {
        // todo handle browser refresh, timeout and multiple request from different states,
        // whitelisting allowed,
        // todo dynamic configurable UI

        log.info("processBrowserRequest - Received CReq Request - " + strCReq);
        Transaction transaction = null;
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        challengeFlowDto.setCdRes(new CdRes());
        CREQ cReq;

        try {
            // parse Creq
            cReq = parseEncryptedRequest(strCReq);

            //  find Transaction and previous request, response
            transaction = fetchTransactionData(cReq.getAcsTransID());

            // set Notification url to send Cres
            challengeFlowDto
                    .getCdRes()
                    .setNotificationUrl(
                            transaction.getTransactionReferenceDetail().getNotificationUrl());

            //  log creq
            transactionMessageLogService.createAndSave(cReq, cReq.getAcsTransID());

            // validation Creq
            challengeRequestValidator.validateRequest(cReq, transaction);

            // validate threeDSSessionData
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

            // 4 flows
            // 1: if Challenge cancelled by user
            // 2: If transaction status is incorrect
            // 3: if resend challenge
            // 4: else perform send/preAuth Challenge

            if (!Util.isNullorBlank(cReq.getChallengeCancel())) {
                handleCancelChallenge(challengeFlowDto, transaction);
            } else if (Util.isChallengeCompleted(transaction)) {
                if (transaction
                        .getTransactionStatusReason()
                        .equals(TransactionStatusReason.TRANSACTION_TIMEOUT.getCode())) {
                    generateErrorResponse(
                            challengeFlowDto.getCdRes(),
                            ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT,
                            transaction,
                            "Timeout expiry reached for the transaction");
                } else {
                    generateErrorResponse(
                            challengeFlowDto.getCdRes(),
                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                            transaction,
                            "Challenge resend threshold exceeded");
                }

            } else {
                AuthConfigDto authConfigDto = featureService.getAuthenticationConfig(transaction);
                if (cReq.getResendChallenge() != null
                        && cReq.getResendChallenge().equals(InternalConstants.YES)) {
                    handleReSendChallenge(challengeFlowDto, transaction, authConfigDto);
                } else {
                    StateMachine.Trigger(transaction, Phase.PhaseEvent.CREQ_RECEIVED);
                    handleSendChallenge(challengeFlowDto, transaction, authConfigDto);
                }
                transactionTimeoutServiceLocator
                        .locateService(MessageType.CReq)
                        .scheduleTask(
                                transaction.getId(), transaction.getTransactionStatus(), null);
            }

        } catch (ParseException | TransactionDataNotValidException ex) {
            log.error("Exception occurred", ex);
            // don't send Rres for ParseException
            if (ex.getInternalErrorCode().equals(InternalErrorCode.TRANSACTION_ID_EMPTY)
                    || ex.getInternalErrorCode()
                            .equals(InternalErrorCode.CREQ_JSON_PARSING_ERROR)) {
                challengeFlowDto.getCdRes().setSendEmptyResponse(true);
            }
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (ThreeDSException ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (InvalidStateTransactionException e) {
            log.error("Exception occurred", e);
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    InternalErrorCode.INVALID_STATE_TRANSITION,
                    transaction,
                    e.getMessage());
        } catch (ACSException ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    ex.getErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (Exception ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.INTERNAL_SERVER_ERROR,
                    transaction,
                    ex.getMessage());
        } finally {
            // save CDres and Transaction
            if (transaction != null && !Util.isNullorBlank(transaction.getId())) {
                if (Util.isChallengeCompleted(transaction)) {
                    transactionTimeoutServiceLocator
                            .locateService(MessageType.CReq)
                            .cancelTask(transaction.getId());
                    challengeFlowDto.getCdRes().setChallengeCompleted(true);
                    challengeFlowDto
                            .getCdRes()
                            .setThreeDSSessionData(transaction.getThreedsSessionData());
                }
                updateEci(transaction);
                if (challengeFlowDto.isSendRreq()) {

                    try {
                        resultRequestService.handleRreq(transaction);
                    } catch (Exception ex) {
                        CRES cres = cResMapper.toCres(transaction);
                        challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64Url(cres));
                    }
                }
                transactionService.saveOrUpdate(transaction);
                transactionMessageLogService.createAndSave(
                        challengeFlowDto.getCdRes(), transaction.getId());
            }
        }
        return challengeFlowDto.getCdRes();
    }

    @Override
    public CdRes processBrwChallengeValidationRequest(@NonNull final CVReq cVReq) {
        CdRes cdReqError = new CdRes();
        try {
            return processBrwChallengeValidationReq(cVReq);
        } catch (Exception e) {
            log.error(
                    "Unhandled error occurred in processBrwChallengeValidationRequest {}",
                    e.getMessage(),
                    e);
            // add alert for this. Ideally this should not happen. All error handled here won't send
            // Rres and won't update transactions
            cdReqError.setError(true);
            cdReqError.setEncryptedCRes(null);
            ThreeDSErrorResponse errorObj =
                    Util.generateErrorResponse(
                            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                            null,
                            "Unexpected error while validating challenge",
                            MessageType.CReq);
            cdReqError.setEncryptedErro(Util.encodeBase64Url(errorObj));
        }
        return cdReqError;
    }

    private CdRes processBrwChallengeValidationReq(CVReq cvReq)
            throws ACSDataAccessException,
                    InvalidStateTransactionException,
                    OperationNotSupportedException {
        // todo combine ChallengeValidationReq and ChallengeReq
        Transaction transaction = null;
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        challengeFlowDto.setCdRes(new CdRes());

        try {
            challengeValidationRequestValidator.validateRequest(cvReq);

            // find Transaction and previous request, response
            transaction = fetchTransactionData(cvReq.getTransactionId());
            // set Notification url to send Cres
            challengeFlowDto
                    .getCdRes()
                    .setNotificationUrl(
                            transaction.getTransactionReferenceDetail().getNotificationUrl());

            // log cvreq
            transactionMessageLogService.createAndSave(cvReq, cvReq.getTransactionId());

            // 4 flows
            // 1: if Challenge cancelled by user
            // 2: If transaction status is incorrect
            // 3: if resend challenge
            // 4: else perform validation

            if (Util.isChallengeCompleted(transaction)) {
                if (transaction
                        .getTransactionStatusReason()
                        .equals(TransactionStatusReason.TRANSACTION_TIMEOUT.getCode())) {
                    generateErrorResponse(
                            challengeFlowDto.getCdRes(),
                            ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT,
                            transaction,
                            "Timeout expiry reached for the transaction");
                } else {
                    generateErrorResponse(
                            challengeFlowDto.getCdRes(),
                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                            transaction,
                            "Challenge resend threshold exceeded");
                }
            } else if (cvReq.isCancelChallenge()) {
                handleCancelChallenge(challengeFlowDto, transaction);
            } else {
                AuthConfigDto authConfigDto = featureService.getAuthenticationConfig(transaction);
                if (cvReq.isResendChallenge()) {
                    transactionTimeoutServiceLocator
                            .locateService(MessageType.CReq)
                            .scheduleTask(
                                    transaction.getId(), transaction.getTransactionStatus(), null);
                    handleReSendChallenge(challengeFlowDto, transaction, authConfigDto);
                } else {
                    handleChallengeValidation(cvReq, transaction, authConfigDto, challengeFlowDto);
                }
            }

        } catch (ParseException | TransactionDataNotValidException ex) {
            // don't send Rres for ParseException
            log.error("Exception occurred", ex);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (ThreeDSException ex) {
            challengeFlowDto.setSendRreq(true);
            log.error("Exception occurred", ex);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (InvalidStateTransactionException e) {
            log.error("Exception occurred", e);
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    InternalErrorCode.INVALID_STATE_TRANSITION,
                    transaction,
                    e.getMessage());
        } catch (ACSException ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    ex.getErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (Exception ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.INTERNAL_SERVER_ERROR,
                    transaction,
                    ex.getMessage());
        } finally {
            // save CDres and Transaction
            // this block of code should avoid throwing exception, in case of exception we won't be
            // able to send RReq to DS
            if (transaction != null) {
                if (Util.isChallengeCompleted(transaction)) {
                    log.info(
                            "Challenge is marked completed for transaction{}", transaction.getId());
                    transactionTimeoutServiceLocator
                            .locateService(MessageType.CReq)
                            .cancelTask(transaction.getId());
                    challengeFlowDto.getCdRes().setChallengeCompleted(true);
                    challengeFlowDto
                            .getCdRes()
                            .setThreeDSSessionData(transaction.getThreedsSessionData());
                }
                updateEci(transaction);
                if (challengeFlowDto.isSendRreq()) {
                    log.info("Sending Result request for transaction {}", transaction.getId());
                    // sendRreq and if it fails update response
                    try {
                        resultRequestService.handleRreq(transaction);
                    } catch (Exception ex) {
                        transaction.setTransactionStatus(TransactionStatus.FAILED);
                        CRES cres = cResMapper.toCres(transaction);
                        challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64Url(cres));
                    }
                }
                transactionService.saveOrUpdate(transaction);
                transactionMessageLogService.createAndSave(
                        challengeFlowDto.getCdRes(), transaction.getId());
            }
        }
        log.info("Sending challenge display response for transaction {}", transaction.getId());
        return challengeFlowDto.getCdRes();
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
            CVReq cvReq,
            Transaction transaction,
            AuthConfigDto authConfigDto,
            ChallengeFlowDto challengeFlowDto)
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
                                .authValue(cvReq.getAuthVal())
                                .build());

        if (authResponse.isAuthenticated()) {
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            StateMachine.Trigger(transaction, Phase.PhaseEvent.AUTH_VAL_VERIFIED);
            updateEci(transaction);
            transaction.setAuthValue(authValueGeneratorService.getAuthValue(transaction));
            CRES cres = cResMapper.toCres(transaction);
            challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64Url(cres));

            transactionMessageLogService.createAndSave(cres, transaction.getId());

            challengeFlowDto.setSendRreq(true);
        } else { // If the authentication has failed, is not completed or the Cardholder has
            // selected to cancel the authentication
            if (authConfigDto.getChallengeAttemptConfig().getAttemptThreshold()
                    > transaction.getInteractionCount()) {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.INVALID_AUTH_VAL);
                challengeFlowDto.getCdRes().setChallengeInfoText(authResponse.getDisplayMessage());
                cdResMapper.generateCDres(challengeFlowDto.getCdRes(), transaction);
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
                challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64Url(cres));
                transactionMessageLogService.createAndSave(cres, transaction.getId());
                challengeFlowDto.setSendRreq(true);
            }
        }
    }

    private void handleReSendChallenge(
            ChallengeFlowDto challengeFlowDto, Transaction transaction, AuthConfigDto authConfigDto)
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
            challengeFlowDto.setSendRreq(true);
            CRES cres = cResMapper.toCres(transaction);
            challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64Url(cres));
            transactionMessageLogService.createAndSave(cres, transaction.getId());
        } else {
            log.info(" ReSending challenge for transaction {}", transaction.getId());
            StateMachine.Trigger(transaction, Phase.PhaseEvent.RESEND_CHALLENGE);
            handleSendChallenge(challengeFlowDto, transaction, authConfigDto);
        }
    }

    private void handleSendChallenge(
            ChallengeFlowDto challengeFlowDto, Transaction transaction, AuthConfigDto authConfigDto)
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
        cdResMapper.generateCDres(challengeFlowDto.getCdRes(), transaction);
    }

    private void handleCancelChallenge(ChallengeFlowDto challengeFlowDto, Transaction transaction)
            throws InvalidStateTransactionException {
        StateMachine.Trigger(transaction, Phase.PhaseEvent.CANCEL_CHALLENGE);

        transaction.setInteractionCount(transaction.getInteractionCount() + 1);
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        transaction.setTransactionStatusReason(
                TransactionStatusReason.EXCEED_MAX_CHALLENGES.getCode());
        transaction.setErrorCode(InternalErrorCode.CANCELLED_BY_CARDHOLDER.getCode());
        transaction.setChallengeCancelInd(
                ChallengeCancelIndicator.CARDHOLDER_SELECTED_CANCEL.getIndicator());

        challengeFlowDto.setSendRreq(true);
        CRES cres = cResMapper.toCres(transaction);
        challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64Url(cres));
        log.info("challenge cancelled for transaction {}", transaction.getId());
    }

    private Transaction fetchTransactionData(String transactionId)
            throws ACSDataAccessException, ThreeDSException {
        if (Util.isNullorBlank(transactionId)) {
            throw new TransactionDataNotValidException(InternalErrorCode.TRANSACTION_ID_EMPTY);
        }
        Transaction transaction = transactionService.findById(transactionId);
        if (null == transaction || !transaction.isChallengeMandated()) {
            throw new TransactionDataNotValidException(InternalErrorCode.TRANSACTION_NOT_FOUND);
        }
        return transaction;
    }

    private CREQ parseEncryptedRequest(String strCReq) throws ParseException {
        if (Util.isNullorBlank(strCReq) || strCReq.contains(" ")) {
            throw new ParseException(
                    ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR);
        }
        CREQ creq;
        try {
            String decryptedCReq = decodeBase64(strCReq);
            creq = fromJson(decryptedCReq, CREQ.class);
        } catch (Exception e) {
            throw new ParseException(
                    ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR,
                    e);
        }
        if (null == creq) {
            throw new ParseException(
                    ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR);
        }
        return creq;
    }

    public void generateErrorResponseAndUpdateTransaction(
            CdRes cdRes,
            ThreeDSecureErrorCode error,
            InternalErrorCode internalErrorCode,
            Transaction transaction,
            String errorDetail)
            throws InvalidStateTransactionException {
        generateErrorResponse(cdRes, error, transaction, errorDetail);
        if (null != transaction) {
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

    private static void generateErrorResponse(
            CdRes cdRes, ThreeDSecureErrorCode error, Transaction transaction, String errorDetail) {
        cdRes.setError(true);
        cdRes.setEncryptedCRes(null);
        ThreeDSErrorResponse errorObj =
                Util.generateErrorResponse(error, transaction, errorDetail, MessageType.CReq);
        cdRes.setEncryptedErro(Util.encodeBase64Url(errorObj));
    }
}
