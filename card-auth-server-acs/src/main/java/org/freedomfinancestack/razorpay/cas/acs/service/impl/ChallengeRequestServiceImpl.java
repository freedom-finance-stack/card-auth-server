package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.constraints.NotNull;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CdRes;
import org.freedomfinancestack.razorpay.cas.acs.dto.ValidateChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.*;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
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

    @Override
    public CdRes processBrwChallengeRequest(
            @NotNull final String strCReq, final String threeDSSessionData)
            throws ACSDataAccessException, InvalidStateTransactionException {
        // todo handle browser refresh, timeout and multiple request from different states,
        // whitelisting allowed,
        // todo dynamic configurable UI

        log.info("processBrowserRequest - Received CReq Request - " + strCReq);
        CREQ cReq;
        AREQ aReq;
        Transaction transaction = null;
        CdRes cdRes = new CdRes();
        boolean sendRres = false;
        try {
            // 1 : parse Creq
            cReq = parseEncryptedRequest(strCReq);

            // 2 : find Transaction and previous request, response
            transaction = transactionService.findById(cReq.getAcsTransID());
            if (null == transaction || !transaction.isChallengeMandated()) {
                throw new DataNotFoundException(
                        ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED,
                        InternalErrorCode.TRANSACTION_NOT_FOUND);
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

                if (Phase.CRES.equals(transaction.getPhase())) {
                    transaction.setThreedsSessionData(threeDSSessionData);
                    AuthConfigDto authConfigDto = getAuthConfig(transaction);
                    AuthenticationService authenticationService =
                            authenticationServiceLocator.locateTransactionAuthenticationService(
                                    transaction,
                                    transaction.getTransactionPurchaseDetail().getPurchaseAmount(),
                                    authConfigDto.getChallengeAuthTypeConfig());
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
                            "Another transaction is progress or Cres not expected");
                }
            }

        } catch (ParseException ex) {
            // don't send Rres for ParseException
            generateErrorResponse(
                    cdRes,
                    ex.getThreeDSecureErrorCode(),
                    InternalErrorCode.INVALID_STATE_TRANSITION,
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
            transactionMessageTypeService.createAndSave(cdRes, transaction.getId());
            transactionService.saveOrUpdate(transaction);
        }

        // send RRes
        try {
            if (sendRres) {
                resultRequestService.sendRreq(transaction);
                StateMachine.Trigger(transaction, Phase.PhaseEvent.CREQ_RECEIVED);
            }
        } catch (ThreeDSException e) {
            generateErrorResponse(
                    cdRes,
                    e.getThreeDSecureErrorCode(),
                    e.getInternalErrorCode(),
                    transaction,
                    e.getMessage());
        } catch (InvalidStateTransactionException ex) {
            generateErrorResponse(
                    cdRes,
                    ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                    InternalErrorCode.INVALID_STATE_TRANSITION,
                    transaction,
                    ex.getMessage());
        } finally {
            // save CDres and Transaction
            transactionMessageTypeService.createAndSave(cdRes, transaction.getId());
            transactionService.saveOrUpdate(transaction);
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
        String errorRes = generateErrorResponse(error, transaction, errorDetail);
        cdRes.setEncryptedErro(Util.encodeBase64(errorRes));

        transaction.setErrorCode(internalErrorCode.getCode());
        transaction.setTransactionStatus(internalErrorCode.getTransactionStatus());
        transaction.setTransactionStatusReason(
                internalErrorCode.getTransactionStatusReason().getCode());
        StateMachine.Trigger(transaction, Phase.PhaseEvent.ERROR_OCCURRED);
    }

    public String generateErrorResponse(
            ThreeDSecureErrorCode error, Transaction transaction, String errorDetail) {
        ThreeDSErrorResponse errorObj = new ThreeDSErrorResponse();
        if (error != null) {
            errorObj.setErrorCode(error.getErrorCode());
            errorObj.setErrorComponent(error.getErrorComponent());
            errorObj.setErrorDescription(error.getErrorDescription());
            errorObj.setErrorDetail(errorDetail);
        }
        if (null != transaction) {
            errorObj.setMessageVersion(transaction.getMessageVersion());
            errorObj.setAcsTransID(transaction.getId());
            errorObj.setDsTransID(transaction.getTransactionReferenceDetail().getDsTransactionId());
            errorObj.setThreeDSServerTransID(
                    transaction.getTransactionReferenceDetail().getThreedsServerTransactionId());
        }
        return Util.toJson(errorObj);
    }

    @Override
    public ValidateChallengeResponse processBrwChallengeValidationRequest(
            @NonNull final CVReq CVReq) {
        return null;
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
