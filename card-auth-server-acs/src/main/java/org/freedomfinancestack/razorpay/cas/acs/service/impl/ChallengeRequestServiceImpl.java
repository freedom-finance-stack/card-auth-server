package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.*;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.*;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
import org.freedomfinancestack.razorpay.cas.acs.service.cardDetail.CardDetailService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Network;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
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

    @Override
    public CdRes processBrwChallengeRequest(
            @NotNull final String strCReq, final String threeDSSessionData)
            throws ACSDataAccessException, InvalidStateTransactionException {
        // todo handle browser refresh, timeout and multiple request from different states,
        // whitelisting allowed,
        // todo dynamic configurable UI

        log.info("processBrowserRequest - Received CReq Request - " + strCReq);
        CREQ cReq;
        AREQ aReq = null;
        Transaction transaction = null;
        CdRes cdRes = new CdRes();
        boolean sendRres = false;
        try {
            // 1 : parse Creq
            cReq = parseEncryptedRequest(strCReq);

            // 2 : find Transaction and previous request, response
            transaction = transactionService.findById(cReq.getAcsTransID());
            if (null == transaction || !transaction.isChallengeMandated()) {
                throw new TransactionDataNotValidException(InternalErrorCode.TRANSACTION_NOT_FOUND);
            }
            // todo removing this fetch and use transaction data
            Map<MessageType, ThreeDSObject> threeDSMessageMap =
                    transactionMessageTypeService.getTransactionMessagesByTransactionId(
                            transaction.getId());
            aReq = (AREQ) threeDSMessageMap.get(MessageType.AReq);
            cdRes.setNotificationUrl(aReq.getNotificationURL());

            // 3: log creq
            transactionMessageTypeService.createAndSave(cReq, cReq.getAcsTransID());

            // 4:  State transition
            if (YES.equals(cReq.getResendChallenge())) {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.RESEND_CHALLENGE);
            } else {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.CREQ_RECEIVED);
            }

            if (!transaction.getTransactionStatus().equals(TransactionStatus.CHALLENGE_REQUIRED)) {
                // 5: invalid transaction state
                if (transaction.getTransactionStatus().equals(TransactionStatus.SUCCESS)) {
                    //                    throw new ValidationException(
                    //                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    //                            "Transaction already completed");

                } else {
                    throw new ValidationException(
                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                            "Transaction already failed");
                    // failed / UA/ TimeOut (Doc mentions about this case)
                }
            } else {
                // 6: validate Creq
                if (Util.isNullorBlank(cReq.getChallengeCancel())) {
                    challengeRequestValidator.validateRequest(
                            cReq, aReq, (CRES) threeDSMessageMap.get(MessageType.CRes));
                } else {
                    StateMachine.Trigger(transaction, Phase.PhaseEvent.CANCEL_CHALLENGE);
                    transaction.setChallengeCancelInd(cReq.getChallengeCancel());
                    transaction.setThreedsSessionData(threeDSSessionData);
                    transaction.setTransactionStatus(
                            InternalErrorCode.CANCELLED_BY_CARDHOLDER.getTransactionStatus());
                    transaction.setTransactionStatusReason(
                            InternalErrorCode.CANCELLED_BY_CARDHOLDER
                                    .getTransactionStatusReason()
                                    .getCode());
                    transaction.setErrorCode(InternalErrorCode.CANCELLED_BY_CARDHOLDER.getCode());
                    // return or throw exception
                }

                if (Phase.CREQ.equals(transaction.getPhase())) {
                    transaction.setThreedsSessionData(threeDSSessionData);
                    AuthConfigDto authConfigDto = getAuthConfig(transaction);
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
                    generateCDres(cdRes, transaction);
                } else {
                    throw new ValidationException(
                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                            "Another transaction is progress or Creq not expected");
                }
            }

        } catch (ParseException | TransactionDataNotValidException ex) {
            // don't send Rres for ParseException
            generateErrorResponse(
                    cdRes,
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (ThreeDSException ex) {
            sendRres = true;
            generateErrorResponse(
                    cdRes,
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (InvalidStateTransactionException e) {
            sendRres = true;
            generateErrorResponse(
                    cdRes,
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    InternalErrorCode.INVALID_STATE_TRANSITION,
                    transaction,
                    e.getMessage());
        } catch (ACSException ex) {
            sendRres = true;
            generateErrorResponse(
                    cdRes,
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    ex.getErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (Exception ex) {
            sendRres = true;
            generateErrorResponse(
                    cdRes,
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.INTERNAL_SERVER_ERROR,
                    transaction,
                    ex.getMessage());
        } finally {
            // save CDres and Transaction
            if (transaction != null) {
                GenerateECIRequest generateECIRequest =
                        new GenerateECIRequest(
                                transaction.getTransactionStatus(),
                                transaction.getTransactionCardDetail().getNetworkCode(),
                                transaction.getMessageCategory());
                if (aReq != null) {
                    generateECIRequest.setThreeRIInd(aReq.getThreeRIInd());
                }
                String eci = eCommIndicatorService.generateECI(generateECIRequest);
                transaction.setEci(eci);
                if (sendRres) {
                    resultRequestService.processRreq(transaction);
                }
                transactionService.saveOrUpdate(transaction);
                transactionMessageTypeService.createAndSave(cdRes, transaction.getId());
            }
        }

        return cdRes;
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

    public void generateErrorResponse(
            CdRes cdRes,
            ThreeDSecureErrorCode error,
            InternalErrorCode internalErrorCode,
            Transaction transaction,
            String errorDetail)
            throws InvalidStateTransactionException {
        cdRes.setError(true);
        ThreeDSErrorResponse errorObj = Util.generateErrorResponse(error, transaction, errorDetail);
        String errorRes = Util.toJson(errorObj);
        cdRes.setEncryptedErro(Util.encodeBase64(errorRes));
        if (null != transaction) {
            transaction.setErrorCode(internalErrorCode.getCode());
            transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
            transaction.setTransactionStatusReason(
                    internalErrorCode.getTransactionStatusReason().getCode());
            StateMachine.Trigger(transaction, Phase.PhaseEvent.ERROR_OCCURRED);
        }
    }

    @Override
    public CdRes processBrwChallengeValidationRequest(@NonNull final CVReq CVReq) {
        try {
            processBrwChallengeValidationReq(CVReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private CdRes processBrwChallengeValidationReq(CVReq cvReq) throws ACSDataAccessException {
        TransactionDto transactionDto = new TransactionDto();
        ChallengeFlowDto challengeFlowDto = new ChallengeFlowDto();
        challengeFlowDto.setCdRes(new CdRes());

        try {
            // find Transaction and previous request, response
            fetchTransactionData(cvReq.getTransactionId(), transactionDto);
            // set Notification url to send Cres
            challengeFlowDto
                    .getCdRes()
                    .setNotificationUrl(transactionDto.getAreq().getNotificationURL());

            Transaction transaction = transactionDto.getTransaction();
            // log cvreq
            transactionMessageTypeService.createAndSave(cvReq, cvReq.getTransactionId());

            // 4 flows
            // 1: if Challenge cancel by user
            // 2: If transaction status is incorrect
            // 3: if resend challenge
            // 4: else perform validation

            if (cvReq.isCancelChallenge()) {}

            if (!transaction.getTransactionStatus().equals(TransactionStatus.CHALLENGE_REQUIRED)) {}

            // State transition
            AuthConfigDto authConfigDto = getAuthConfig(transaction);
            if (cvReq.isResendChallenge()) {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.RESEND_CHALLENGE);
                handleSendChallenge(challengeFlowDto, transaction, authConfigDto);
            } else {
                StateMachine.Trigger(transaction, Phase.PhaseEvent.VALIDATION_REQ_RECEIVED);
                handleChallengeValidation(challengeFlowDto, transaction, authConfigDto);
            }

        } catch (ParseException | TransactionDataNotValidException ex) {
            // don't send Rres for ParseException
            generateErrorResponse(
                    cdRes,
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (ThreeDSException ex) {
            sendRres = true;
            generateErrorResponse(
                    cdRes,
                    ex.getThreeDSecureErrorCode(),
                    ex.getInternalErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (InvalidStateTransactionException e) {
            sendRres = true;
            generateErrorResponse(
                    cdRes,
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    InternalErrorCode.INVALID_STATE_TRANSITION,
                    transaction,
                    e.getMessage());
        } catch (ACSException ex) {
            sendRres = true;
            generateErrorResponse(
                    cdRes,
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    ex.getErrorCode(),
                    transaction,
                    ex.getMessage());
        } catch (Exception ex) {
            sendRres = true;
            generateErrorResponse(
                    cdRes,
                    ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                    InternalErrorCode.INTERNAL_SERVER_ERROR,
                    transaction,
                    ex.getMessage());
        } finally {
            // save CDres and Transaction
            // this block of code should avoid throwing exception, in case of exception we won't be
            // able to send RReq to DS
            GenerateECIRequest generateECIRequest =
                    new GenerateECIRequest(
                            transactionDto.getTransaction().getTransactionStatus(),
                            transactionDto
                                    .getTransaction()
                                    .getTransactionCardDetail()
                                    .getNetworkCode(),
                            transactionDto.getTransaction().getMessageCategory());
            if (transactionDto.getAreq() != null) {
                generateECIRequest.setThreeRIInd(transactionDto.getAreq().getThreeRIInd());
            }
            String eci = eCommIndicatorService.generateECI(generateECIRequest);
            transactionDto.getTransaction().setEci(eci);
            transactionService.saveOrUpdate(transactionDto.getTransaction());
            transactionMessageTypeService.createAndSave(
                    challengeFlowDto.getCdRes(), transactionDto.getTransaction().getId());
        }
        // rreq is handled differently for validation flow as we need to handle error
        if (challengeFlowDto.isSendRreq()) {
            resultRequestService.processRreq(transactionDto.getTransaction());
        }

        return challengeFlowDto.getCdRes();
    }

    private void handleChallengeValidation(
            CVReq cvReq,
            Transaction transaction,
            AuthConfigDto authConfigDto,
            ChallengeFlowDto challengeFlowDto)
            throws ThreeDSException, InvalidStateTransactionException {
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
        } else if (authConfigDto.getChallengeAttemptConfig().getAttemptThreshold()
                > transaction.getInteractionCount()) {
            StateMachine.Trigger(transaction, Phase.PhaseEvent.INVALID_AUTH_VAL);
            // show incorrect OTP on UI
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
            if (authConfigDto.getChallengeAttemptConfig().isBlockOnExceedAttempt()) {
                userDetailService.blockUser(transaction.getTransactionCardDetail().getCardNumber());
            }
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
}
