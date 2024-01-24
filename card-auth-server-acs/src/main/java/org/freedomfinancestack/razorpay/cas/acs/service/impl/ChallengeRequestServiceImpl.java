package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import org.freedomfinancestack.extensions.stateMachine.InvalidStateTransactionException;
import org.freedomfinancestack.extensions.stateMachine.StateMachine;
import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.constant.ThreeDSConstant;
import org.freedomfinancestack.razorpay.cas.acs.dto.*;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.CResMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSDataAccessException;
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
import org.freedomfinancestack.razorpay.cas.contract.enums.*;
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
    private final CardDetailService cardDetailService;
    private final CardRangeService cardRangeService;
    private final CResMapper cResMapper;
    private final AuthValueGeneratorService authValueGeneratorService;
    private final TransactionTimeoutServiceLocator transactionTimeoutServiceLocator;
    private final ChallengeRequestParserFactory challengeRequestParserFactory;
    private final DecoupledAuthenticationService decoupledAuthenticationService;
    private final InstitutionUiService institutionUiService;

    @Override
    public ChallengeFlowDto processChallengeRequest(
            final DeviceChannel deviceChannel,
            final String strCReq,
            final String threeDSSessionData)
            throws ThreeDSException {
        if (deviceChannel.equals(DeviceChannel.APP)) {
            return processAppChallengeRequestHandler(deviceChannel, strCReq, threeDSSessionData);
        }
        return processBrwChallengeRequestHandler(deviceChannel, strCReq, threeDSSessionData);
    }

    private ChallengeFlowDto processAppChallengeRequestHandler(
            DeviceChannel deviceChannel, String strCReq, String threeDSSessionData)
            throws ThreeDSException {
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        try {
            processChallengeRequestHandler(
                    challengeFlowDto, deviceChannel, strCReq, threeDSSessionData);
            String cres;
            try {
                cres =
                        challengeRequestParserFactory
                                .getService(deviceChannel)
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

    private ChallengeFlowDto processBrwChallengeRequestHandler(
            final DeviceChannel deviceChannel,
            final String strCReq,
            final String threeDSSessionData) {
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        try {
            processChallengeRequestHandler(
                    challengeFlowDto, deviceChannel, strCReq, threeDSSessionData);
            // 3 things
            // UI
            // Final Output
            // Exception handling
        } catch (InvalidStateTransactionException e) {
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
                if (ChallengeRequestService.isChallengeCompleted(
                        challengeFlowDto.getTransaction())) {
                    challengeFlowDto.setEncryptedResponse(
                            challengeRequestParserFactory
                                    .getService(deviceChannel)
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
            final DeviceChannel deviceChannel,
            final String strCReq,
            final String threeDSSessionData)
            throws ThreeDSException, InvalidStateTransactionException {
        Transaction transaction = null;
        CREQ creq;
        InstitutionUIParams institutionUIParams = new InstitutionUIParams();
        challengeFlowDto.setInstitutionUIParams(institutionUIParams);
        try {
            creq =
                    challengeRequestParserFactory
                            .getService(deviceChannel)
                            .parseEncryptedRequest(strCReq);
            //  find Transaction and previous request, response
            transaction = transactionService.findById(creq.getAcsTransID());
            if (!transaction.isChallengeMandated()) {
                throw new TransactionDataNotValidException(InternalErrorCode.INVALID_REQUEST);
            }
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
            // isWhitelistingDataValid should be called first before whitelistingDataEntry null
            // check, as it is also performing a validation
            if (challengeRequestValidator.isWhitelistingDataValid(transaction, creq, authConfigDto)
                    && !Util.isNullorBlank(creq.getWhitelistingDataEntry())) {
                transaction
                        .getTransactionSdkDetail()
                        .setWhitelistingDataEntry(creq.getWhitelistingDataEntry());
            }

            transaction
                    .getTransactionSdkDetail()
                    .setThreeDSRequestorAppURL(creq.getThreeDSRequestorAppURL());

            // 4 flows
            // 1: if Challenge cancelled by user
            // 2: If transaction status is incorrect
            // 3: if resend challenge
            // 4: else perform send/preAuth Challenge

            if (ChallengeRequestService.isChallengeCompleted(transaction)) {
                if (transaction
                        .getTransactionStatusReason()
                        .equals(TransactionStatusReason.TRANSACTION_TIMEOUT.getCode())) {
                    if (deviceChannel.equals(DeviceChannel.BRW)
                            && transaction
                                    .getChallengeCancelInd()
                                    .equals(
                                            ChallengeCancelIndicator.TRANSACTION_TIMED_OUT
                                                    .getIndicator())) {
                        throw new ACSException(
                                InternalErrorCode.TRANSACTION_TIMED_OUT_CHALLENGE_COMPLETION,
                                "Timeout expiry reached for the transaction");
                    }
                    throw new ThreeDSException(
                            ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT,
                            InternalErrorCode.TRANSACTION_TIMED_OUT_CHALLENGE_COMPLETION,
                            "Timeout expiry reached for the transaction");
                } else {
                    throw new TransactionDataNotValidException(
                            InternalErrorCode.INVALID_REQUEST,
                            "Transaction already in completed state");
                }
            } else if (!Util.isNullorBlank(creq.getChallengeCancel())) {
                handleCancelChallenge(transaction, challengeFlowDto, creq);
            } else if (transaction.getPhase().equals(Phase.ARES)) {
                transactionTimeoutServiceLocator
                        .locateService(MessageType.CReq)
                        .scheduleTask(
                                transaction.getId(), transaction.getTransactionStatus(), null);
                StateMachine.Trigger(transaction, Phase.PhaseEvent.CREQ_RECEIVED);
                handleSendChallenge(transaction, authConfigDto, challengeFlowDto);
            } else if (!Util.isNullorBlank(creq.getChallengeHTMLDataEntry())
                    || !Util.isNullorBlank(creq.getChallengeDataEntry())
                    || !Util.isNullorBlank(creq.getOobContinue())
                    || !Util.isNullorBlank(creq.getChallengeNoEntry())) {
                if (deviceChannel.equals(DeviceChannel.BRW)
                        || transaction
                                .getTransactionSdkDetail()
                                .getAcsInterface()
                                .equals(DeviceInterface.HTML.getValue())) {
                    challengeFlowDto.setAuthValue(creq.getChallengeHTMLDataEntry());
                } else {
                    challengeFlowDto.setAuthValue(creq.getChallengeDataEntry());
                }
                handleChallengeValidation(transaction, authConfigDto, challengeFlowDto);
            } else if (!Util.isNullorBlank(creq.getResendChallenge())
                            && InternalConstants.YES.equals(creq.getResendChallenge())
                    || creq.getMessageVersion().equals(ThreeDSConstant.MESSAGE_VERSION_2_1_0)) {
                handleReSendChallenge(transaction, authConfigDto, challengeFlowDto);
            }

            // Populating Ui Params
            institutionUiService.populateUiParams(challengeFlowDto, authConfigDto);

            if (challengeFlowDto.isSendRreq()) {
                challengeFlowDto.setCres(cResMapper.toFinalCRes(transaction));
            } else {
                challengeFlowDto.setCres(
                        cResMapper.toCRes(transaction, challengeFlowDto.getInstitutionUIParams()));
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
            transactionErr.setPhase(Phase.ARES);
            updateTransactionWithError(ex.getInternalErrorCode(), transactionErr);
            throw new ThreeDSException(
                    ex.getThreeDSecureErrorCode(), ex.getMessage(), transactionErr, ex);
        } catch (ThreeDSException ex) {
            log.error("Exception occurred", ex);
            if (!ex.getThreeDSecureErrorCode()
                    .equals(ThreeDSecureErrorCode.TRANSACTION_TIMED_OUT)) {
                challengeFlowDto.setSendRreq(true);
            }
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
            updateTransactionWithError(ex.getErrorCode(), transaction);
            challengeFlowDto.setCres(cResMapper.toFinalCRes(transaction));
            if (!ex.getErrorCode()
                    .equals(InternalErrorCode.TRANSACTION_TIMED_OUT_CHALLENGE_COMPLETION)) {
                challengeFlowDto.setSendRreq(true);
            }
        } catch (Exception ex) {
            log.error("Exception occurred", ex);
            challengeFlowDto.setSendRreq(true);
            updateTransactionWithError(InternalErrorCode.INTERNAL_SERVER_ERROR, transaction);
            throw new ThreeDSException(
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, ex.getMessage(), transaction, ex);
        } finally {
            if (transaction != null) {
                if (ChallengeRequestService.isChallengeCompleted(transaction)) {
                    log.info(
                            "Challenge is marked completed for transaction{}", transaction.getId());
                    transactionTimeoutServiceLocator
                            .locateService(MessageType.CReq)
                            .cancelTask(transaction.getId());
                    challengeFlowDto.setThreeDSSessionData(transaction.getThreedsSessionData());
                }
                transactionService.updateEci(transaction);

                if (challengeFlowDto.isSendRreq()) {
                    log.info("Sending Result request for transaction {}", transaction.getId());
                    try {
                        resultRequestService.handleRreq(transaction);
                    } catch (ThreeDSException ex) {
                        if (DeviceChannel.BRW.equals(deviceChannel)) {
                            CRES cres = cResMapper.toFinalCRes(transaction);
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
                if (deviceChannel.equals(DeviceChannel.APP)) {
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
            challengeFlowDto.setSendRreq(true);
        } else {
            log.info(" ReSending challenge for transaction {}", transaction.getId());
            transaction.setInteractionCount(transaction.getInteractionCount() + 1);
            StateMachine.Trigger(transaction, Phase.PhaseEvent.RESEND_CHALLENGE);
            handleSendChallenge(transaction, authConfigDto, challengeFlowDto);
            challengeFlowDto.setCurrentFlowType(InternalConstants.RESEND);
        }
    }

    private void handleChallengeValidation(
            Transaction transaction, AuthConfigDto authConfigDto, ChallengeFlowDto challengeFlowDto)
            throws ThreeDSException, InvalidStateTransactionException, ACSException {
        transaction.setInteractionCount(transaction.getInteractionCount() + 1);
        StateMachine.Trigger(transaction, Phase.PhaseEvent.VALIDATION_REQ_RECEIVED);

        AuthResponse authResponse = null;

        AuthenticationService authenticationService =
                authenticationServiceLocator.locateTransactionAuthenticationService(
                        transaction, authConfigDto.getChallengeAuthTypeConfig());

        authResponse =
                authenticationService.authenticate(
                        AuthenticationDto.builder()
                                .authConfigDto(authConfigDto)
                                .transaction(transaction)
                                .authValue(challengeFlowDto.getAuthValue())
                                .build());

        if (authResponse != null && authResponse.isAuthenticated()) {
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            StateMachine.Trigger(transaction, Phase.PhaseEvent.AUTH_VAL_VERIFIED);
            transactionService.updateEci(transaction);
            transaction.setAuthValue(authValueGeneratorService.getAuthValue(transaction));
            challengeFlowDto.setSendRreq(true);
        } else { // If the authentication has failed, is not completed or the Cardholder has
            // selected to cancel the authentication
            if (authConfigDto.getChallengeAttemptConfig().getAttemptThreshold()
                    > transaction.getInteractionCount()) {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.INVALID_AUTH_VAL);
                challengeFlowDto.setCurrentFlowType(InternalConstants.VALIDATE_OTP);
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
    }

    private void handleCancelChallenge(
            Transaction transaction, ChallengeFlowDto challengeFlowDto, CREQ creq)
            throws InvalidStateTransactionException {
        StateMachine.Trigger(transaction, Phase.PhaseEvent.CANCEL_CHALLENGE);

        if (!creq.getMessageVersion().equals(ThreeDSConstant.MESSAGE_VERSION_2_1_0)) {
            transaction.setInteractionCount(transaction.getInteractionCount() + 1);
        }
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        transaction.setTransactionStatusReason(
                TransactionStatusReason.EXCEED_MAX_CHALLENGES.getCode());
        transaction.setErrorCode(InternalErrorCode.CANCELLED_BY_CARDHOLDER.getCode());
        transaction.setChallengeCancelInd(creq.getChallengeCancel());

        challengeFlowDto.setSendRreq(true);
        challengeFlowDto.setCurrentFlowType(InternalConstants.CANCEL);
        log.info("challenge cancelled for transaction {}", transaction.getId());
    }

    private void updateTransactionForACSException(
            InternalErrorCode internalErrorCode, Transaction transaction)
            throws ACSDataAccessException, InvalidStateTransactionException {
        transactionService.updateTransactionWithError(internalErrorCode, transaction);
        transactionService.saveOrUpdate(transaction);
    }

    private void updateTransactionWithError(
            InternalErrorCode internalErrorCode, Transaction transaction)
            throws InvalidStateTransactionException {
        transactionService.updateTransactionWithError(internalErrorCode, transaction);
        StateMachine.Trigger(transaction, Phase.PhaseEvent.ERROR_OCCURRED);
    }
}
