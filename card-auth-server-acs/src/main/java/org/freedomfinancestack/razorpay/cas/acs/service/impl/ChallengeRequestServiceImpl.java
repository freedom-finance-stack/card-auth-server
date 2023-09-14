package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.*;
import org.freedomfinancestack.razorpay.cas.acs.dto.mapper.CResMapper;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.*;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGeneratorService;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeValidationRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.*;
import org.freedomfinancestack.razorpay.cas.dao.model.CardRange;
import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.model.TransactionCardHolderDetail;
import org.freedomfinancestack.razorpay.cas.dao.repository.InstitutionRepository;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.InvalidStateTransactionException;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants.YES;
import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.decodeBase64;
import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.fromJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeRequestServiceImpl implements ChallengeRequestService {

    private final TransactionService transactionService;
    private final TransactionMessageTypeService transactionMessageTypeService;
    private final ChallengeRequestValidator challengeRequestValidator;
    private final FeatureService featureService;
    private final AuthenticationServiceLocator authenticationServiceLocator;
    private final InstitutionRepository institutionRepository;
    private final ResultRequestService resultRequestService;
    private final ECommIndicatorService eCommIndicatorService;
    private final CardDetailService cardDetailService;
    private final CardRangeService cardRangeService;
    private final CResMapper cResMapper;
    private final AuthValueGeneratorService authValueGeneratorService;
    private final ChallengeValidationRequestValidator challengeValidationRequestValidator;

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
                            "Unexpected error while validating challenge");
            cdReqError.setEncryptedErro(Util.encodeBase64(errorObj));
        }
        return cdReqError;
    }

    public CdRes processBrwChallengeRequestHandler(
            @NotNull final String strCReq, final String threeDSSessionData)
            throws ACSDataAccessException, InvalidStateTransactionException {
        // todo handle browser refresh, timeout and multiple request from different states,
        // whitelisting allowed,
        // todo dynamic configurable UI

        log.info("processBrowserRequest - Received CReq Request - " + strCReq);
        TransactionDto transactionDto = new TransactionDto();
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        challengeFlowDto.setCdRes(new CdRes());
        CREQ cReq;

        try {
            Transaction transaction = transactionDto.getTransaction();
            // parse Creq
            cReq = parseEncryptedRequest(strCReq);

            //  find Transaction and previous request, response
            fetchTransactionData(cReq.getAcsTransID(), transactionDto);

            // set Notification url to send Cres
            challengeFlowDto
                    .getCdRes()
                    .setNotificationUrl(transactionDto.getAreq().getNotificationURL());

            //  log creq
            transactionMessageTypeService.createAndSave(cReq, cReq.getAcsTransID());

            // validation Creq
            challengeRequestValidator.validateRequest(
                    cReq, transactionDto.getAreq(), transactionDto.getCres());

            // 4 flows
            // 1: if Challenge cancelled by user
            // 2: If transaction status is incorrect
            // 3: if resend challenge
            // 4: else perform send/preAuth Challenge

            if (!Util.isNullorBlank(cReq.getChallengeCancel())) {
                handleCancelChallenge(challengeFlowDto, transaction);
            } else if (Util.isChallengeCompleted(transaction)) {
                generateErrorResponse(
                        challengeFlowDto.getCdRes(),
                        ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                        transaction,
                        "Challenge resend threshold exceeded");
            } else {
                AuthConfigDto authConfigDto = getAuthConfig(transaction);
                if (cReq.getResendChallenge().equals(YES)) {
                    handleReSendChallenge(challengeFlowDto, transaction, authConfigDto);
                } else {
                    handleSendChallenge(challengeFlowDto, transaction, authConfigDto);
                }
            }

        } catch (ParseException | TransactionDataNotValidException ex) {
            // don't send Rres for ParseException
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transactionDto.getTransaction(),
                    ex.getMessage());
        } catch (ThreeDSException ex) {
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transactionDto.getTransaction(),
                    ex.getMessage());
        } catch (InvalidStateTransactionException e) {
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    InternalErrorCode.INVALID_STATE_TRANSITION,
                    transactionDto.getTransaction(),
                    e.getMessage());
        } catch (ACSException ex) {
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    ex.getErrorCode(),
                    transactionDto.getTransaction(),
                    ex.getMessage());
        } catch (Exception ex) {
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.INTERNAL_SERVER_ERROR,
                    transactionDto.getTransaction(),
                    ex.getMessage());
        } finally {
            // save CDres and Transaction
            if (transactionDto.getTransaction() != null) {
                if (Util.isChallengeCompleted(transactionDto.getTransaction())) {
                    challengeFlowDto.getCdRes().setChallengeCompleted(true);
                    challengeFlowDto.getCdRes().setThreeDSSessionData(threeDSSessionData);
                }
                updateEci(transactionDto.getTransaction(), transactionDto.getAreq());
                if (challengeFlowDto.isSendRreq()) {
                    // sendRreq and if it fails update response
                    resultRequestService.processRreq(transactionDto.getTransaction());
                }
                transactionService.saveOrUpdate(transactionDto.getTransaction());
                transactionMessageTypeService.createAndSave(
                        challengeFlowDto.getCdRes(), transactionDto.getTransaction().getId());
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
                            "Unexpected error while validating challenge");
            cdReqError.setEncryptedErro(Util.encodeBase64(errorObj));
        }
        return cdReqError;
    }

    private CdRes processBrwChallengeValidationReq(CVReq cvReq)
            throws ACSDataAccessException, InvalidStateTransactionException {
        TransactionDto transactionDto = new TransactionDto();
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        challengeFlowDto.setCdRes(new CdRes());
        String threeDsSessionData = "";

        try {
            challengeValidationRequestValidator.validateRequest(cvReq);

            // find Transaction and previous request, response
            fetchTransactionData(cvReq.getTransactionId(), transactionDto);
            // set Notification url to send Cres
            challengeFlowDto
                    .getCdRes()
                    .setNotificationUrl(transactionDto.getAreq().getNotificationURL());

            Transaction transaction = transactionDto.getTransaction();
            threeDsSessionData = transaction.getThreedsSessionData();
            // log cvreq
            transactionMessageTypeService.createAndSave(cvReq, cvReq.getTransactionId());

            // 4 flows
            // 1: if Challenge cancelled by user
            // 2: If transaction status is incorrect
            // 3: if resend challenge
            // 4: else perform validation

            if (cvReq.isCancelChallenge()) {
                handleCancelChallenge(challengeFlowDto, transaction);
            } else if (Util.isChallengeCompleted(transaction)) {
                // todo handle timeout case
                generateErrorResponse(
                        challengeFlowDto.getCdRes(),
                        ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                        transaction,
                        "Challenge resend threshold exceeded");
            } else {
                AuthConfigDto authConfigDto = getAuthConfig(transaction);
                if (cvReq.isResendChallenge()) {
                    handleReSendChallenge(challengeFlowDto, transaction, authConfigDto);
                } else {
                    handleChallengeValidation(cvReq, transaction, authConfigDto, challengeFlowDto);
                }
            }

        } catch (ParseException | TransactionDataNotValidException ex) {
            // don't send Rres for ParseException
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transactionDto.getTransaction(),
                    ex.getMessage());
        } catch (ThreeDSException ex) {
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transactionDto.getTransaction(),
                    ex.getMessage());
        } catch (InvalidStateTransactionException e) {
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    InternalErrorCode.INVALID_STATE_TRANSITION,
                    transactionDto.getTransaction(),
                    e.getMessage());
        } catch (ACSException ex) {
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    ex.getErrorCode(),
                    transactionDto.getTransaction(),
                    ex.getMessage());
        } catch (Exception ex) {
            challengeFlowDto.setSendRreq(true);
            generateErrorResponseAndUpdateTransaction(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.INTERNAL_SERVER_ERROR,
                    transactionDto.getTransaction(),
                    ex.getMessage());
        } finally {
            // save CDres and Transaction
            // this block of code should avoid throwing exception, in case of exception we won't be
            // able to send RReq to DS
            if (transactionDto.getTransaction() != null) {
                if (Util.isChallengeCompleted(transactionDto.getTransaction())) {
                    challengeFlowDto.getCdRes().setChallengeCompleted(true);
                    challengeFlowDto.getCdRes().setThreeDSSessionData(threeDsSessionData);
                }
                updateEci(transactionDto.getTransaction(), transactionDto.getAreq());
                if (challengeFlowDto.isSendRreq()) {
                    // sendRreq and if it fails update response
                    if (!resultRequestService.processRreq(transactionDto.getTransaction())) {
                        generateErrorResponse(
                                challengeFlowDto.getCdRes(),
                                ThreeDSecureErrorCode.SENT_MESSAGES_LIMIT_EXCEEDED,
                                transactionDto.getTransaction(),
                                "Couldn't communicate to DS");
                    }
                }
                transactionService.saveOrUpdate(transactionDto.getTransaction());
                transactionMessageTypeService.createAndSave(
                        challengeFlowDto.getCdRes(), transactionDto.getTransaction().getId());
            }
        }
        return challengeFlowDto.getCdRes();
    }

    private void updateEci(Transaction transaction, AREQ areq) {
        GenerateECIRequest generateECIRequest =
                new GenerateECIRequest(
                        transaction.getTransactionStatus(),
                        transaction.getTransactionCardDetail().getNetworkCode(),
                        transaction.getMessageCategory());
        if (areq != null) {
            generateECIRequest.setThreeRIInd(areq.getThreeRIInd());
        }
        String eci = eCommIndicatorService.generateECI(generateECIRequest);
        transaction.setEci(eci);
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
            transaction.setAuthValue(authValueGeneratorService.getAuthValue(transaction));

            CRES cres = cResMapper.toCres(transaction);
            challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64(cres));

            transactionMessageTypeService.createAndSave(cres, transaction.getId());

            challengeFlowDto.setSendRreq(true);
        } else { // If the authentication has failed, is not completed or the Cardholder has
            // selected to cancel the authentication
            if (authConfigDto.getChallengeAttemptConfig().getAttemptThreshold()
                    > transaction.getInteractionCount()) {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.INVALID_AUTH_VAL);
                challengeFlowDto.getCdRes().setChallengeInfoText(authResponse.getDisplayMessage());
                generateCDres(challengeFlowDto.getCdRes(), transaction);
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
                challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64(cres));
                transactionMessageTypeService.createAndSave(cres, transaction.getId());
                challengeFlowDto.setSendRreq(true);
            }
        }
    }

    private void handleReSendChallenge(
            ChallengeFlowDto challengeFlowDto, Transaction transaction, AuthConfigDto authConfigDto)
            throws InvalidStateTransactionException, ThreeDSException {
        transaction.setResendCount(transaction.getResendCount() + 1);
        if (transaction.getResendCount() - 1
                > authConfigDto.getChallengeAttemptConfig().getResendThreshold()) {
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
            generateErrorResponse(
                    challengeFlowDto.getCdRes(),
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    transaction,
                    "Challenge resend threshold exceeded");
        } else {
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
        StateMachine.Trigger(transaction, Phase.PhaseEvent.SEND_AUTH_VAL);
        //  generateOTP page
        generateCDres(challengeFlowDto.getCdRes(), transaction);
    }

    private void handleCancelChallenge(ChallengeFlowDto challengeFlowDto, Transaction transaction)
            throws InvalidStateTransactionException {
        StateMachine.Trigger(transaction, Phase.PhaseEvent.CANCEL_CHALLENGE);

        transaction.setInteractionCount(transaction.getInteractionCount() + 1);
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        transaction.setTransactionStatusReason(
                TransactionStatusReason.EXCEED_MAX_CHALLANGES.getCode());
        transaction.setErrorCode(InternalErrorCode.CANCELLED_BY_CARDHOLDER.getCode());
        transaction.setChallengeCancelInd(
                ChallengeCancelIndicator.CARDHOLDER_SELECTED_CANCEL.getIndicator());

        challengeFlowDto.setSendRreq(true);
        CRES cres = cResMapper.toCres(transaction);
        challengeFlowDto.getCdRes().setEncryptedCRes(Util.encodeBase64(cres));
    }

    private void fetchTransactionData(String transactionId, TransactionDto transactionDto)
            throws ACSDataAccessException, ThreeDSException {
        Transaction transaction = transactionService.findById(transactionId);
        if (null == transaction || !transaction.isChallengeMandated()) {
            throw new TransactionDataNotValidException(InternalErrorCode.TRANSACTION_NOT_FOUND);
        }

        Map<MessageType, ThreeDSObject> threeDSMessageMap =
                transactionMessageTypeService.getTransactionMessagesByTransactionId(
                        transaction.getId());
        if (null != threeDSMessageMap && !threeDSMessageMap.isEmpty()) {
            if (threeDSMessageMap.containsKey(MessageType.AReq)) {
                transactionDto.setAreq((AREQ) threeDSMessageMap.get(MessageType.AReq));
            }
            if (threeDSMessageMap.containsKey(MessageType.ARes)) {
                transactionDto.setAres((ARES) threeDSMessageMap.get(MessageType.ARes));
            }
            if (threeDSMessageMap.containsKey(MessageType.CReq)) {
                transactionDto.setCreq((CREQ) threeDSMessageMap.get(MessageType.CReq));
            }
            if (threeDSMessageMap.containsKey(MessageType.CRes)) {
                transactionDto.setCres((CRES) threeDSMessageMap.get(MessageType.CRes));
            }
        }
    }

    private AuthConfigDto getAuthConfig(Transaction transaction) throws ACSDataAccessException {
        Map<FeatureEntityType, String> entityIdsByType = new HashMap<>();
        entityIdsByType.put(FeatureEntityType.INSTITUTION, transaction.getInstitutionId());
        entityIdsByType.put(FeatureEntityType.CARD_RANGE, transaction.getCardRangeId());
        return featureService.getAuthenticationConfig(entityIdsByType);
    }

    private CREQ parseEncryptedRequest(String strCReq) throws ParseException {
        if (null == strCReq || strCReq.contains(" ")) {
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

    private void generateCDres(CdRes cdRes, Transaction transaction) throws DataNotFoundException {
        cdRes.setTransactionId(transaction.getId());
        Optional<Institution> institution =
                institutionRepository.findById(transaction.getInstitutionId());
        if (institution.isPresent()) {
            cdRes.setInstitutionName(institution.get().getName());
        } else {
            log.error("Institution not found for transaction: " + transaction.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.INSTITUTION_NOT_FOUND);
        }
        Network network =
                Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode());
        cdRes.setSchemaName(network.getName());
        cdRes.setJsEnableIndicator(
                transaction.getTransactionBrowserDetail().getJavascriptEnabled());
        StringBuilder challengeText = new StringBuilder();
        TransactionCardHolderDetail transactionCardHolderDetail =
                transaction.getTransactionCardHolderDetail();
        boolean isContactInfoAvailable = false;
        if (!Util.isNullorBlank(transactionCardHolderDetail.getMobileNumber())) {
            isContactInfoAvailable = true;
            challengeText.append(
                    String.format(
                            InternalConstants.CHALLENGE_INFORMATION_MOBILE_TEXT,
                            transactionCardHolderDetail.getMobileNumber()));
        }
        if (!Util.isNullorBlank(transactionCardHolderDetail.getEmailId())) {
            if (isContactInfoAvailable) {
                challengeText.append(InternalConstants.AND);
            }
            isContactInfoAvailable = true;
            challengeText.append(
                    String.format(
                            InternalConstants.CHALLENGE_INFORMATION_EMAIL_TEXT,
                            transactionCardHolderDetail.getEmailId()));
        }
        if (!isContactInfoAvailable) {
            log.error(
                    "No contact information found for card user for transaction id "
                            + transaction.getId());
            throw new DataNotFoundException(
                    ThreeDSecureErrorCode.TRANSIENT_SYSTEM_FAILURE,
                    InternalErrorCode.NO_CHANNEL_FOUND_FOR_OTP);
        }
        cdRes.setChallengeText(InternalConstants.CHALLENGE_INFORMATION_TEXT + challengeText);
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
        ThreeDSErrorResponse errorObj = Util.generateErrorResponse(error, transaction, errorDetail);
        cdRes.setEncryptedErro(Util.encodeBase64(errorObj));
    }
}
