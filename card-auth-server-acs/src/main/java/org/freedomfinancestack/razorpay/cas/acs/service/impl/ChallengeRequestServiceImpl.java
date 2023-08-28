package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.AuthenticationDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.ValidateChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.service.*;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.InvalidStateTransactionException;
import org.freedomfinancestack.razorpay.cas.dao.statemachine.StateMachine;
import org.springframework.stereotype.Service;

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

    @Override
    public ChallengeResponse processBrwChallengeRequest(
            @NotNull final String strCReq, final String threeDSSessionData) {
        // todo handle browser refresh, timeout and multiple request from different states,
        // whitelisting allowed,
        // todo dynamic configurable UI

        log.info("processBrowserRequest - Received CReq Request - " + strCReq);
        CREQ cReq;
        AREQ aReq;
        Transaction transaction;
        ChallengeResponse challengeResponse = new ChallengeResponse();

        try {
            // 1 : parse Creq
            cReq = parseEncryptedRequest(strCReq);

            // 2 : find Transaction and previous request, response
            transaction = transactionService.findById(cReq.getAcsTransID());
            if (null == transaction || !transaction.isChallengeMandated()) {
                throw new ThreeDSException(
                        ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED,
                        InternalErrorCode.TRANSACTION_NOT_FOUND,
                        InternalErrorCode.TRANSACTION_NOT_FOUND.getDefaultErrorMessage()
                                + "FOR CHALLENGE");
            }
            Map<MessageType, ThreeDSObject> threeDSMessageMap =
                    transactionMessageTypeService.getTransactionMessagesByTransactionId(
                            transaction.getId());
            aReq = (AREQ) threeDSMessageMap.get(MessageType.AReq);
            challengeResponse.setNotificationUrl(aReq.getNotificationURL());

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
                    throw new ValidationException(
                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                            "Transaction already completed");
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
                } else if (Util.isNullorBlank(cReq.getChallengeCancel())) {
                    StateMachine.Trigger(transaction, Phase.PhaseEvent.CANCEL_CHALLENGE);
                    transaction.setThreedsSessionData(threeDSSessionData);
                    transaction.setTransactionStatus(
                            InternalErrorCode.CANCELLED_BY_CARDHOLDER.getTransactionStatus());
                    transaction.setTransactionStatusReason(
                            InternalErrorCode.CANCELLED_BY_CARDHOLDER
                                    .getTransactionStatusReason()
                                    .getCode());
                    transaction.setErrorCode(InternalErrorCode.CANCELLED_BY_CARDHOLDER.getCode());
                }
                // else if (Phase.CDRES.equals(transaction.getPhase()) ||
                // Phase.CVREQ.equals(transaction.getPhase()) ||) {
                //                    // handle multiple Cres
                //              }
                else {
                    throw new ValidationException(
                            ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                            "Another transaction is progress or Cres not expected");
                }

                // If Phase CDRES : generate OTP page return
            }

        }  catch (ThreeDSException ex) {
            challengeResponse.setError(true);
            // create error response and add to response
            //       / Error
        } catch (InvalidStateTransactionException e) {
            throw new RuntimeException(e);
        } catch (ACSException e) {
           //  Generate CRes
            throw new RuntimeException(e);
        } finally {
            // If Phase RRes or IsError set   :
            //    send RRes
        }
        return null;
    }

    @Override
    public ValidateChallengeResponse validateChallengeRequest(
            @NotNull final ValidateChallengeRequest validateChallengeRequest) {
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
                    ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR);
        }
        CREQ creq;
        try {
            String decryptedCReq = decodeBase64(strCReq);
            creq = fromJson(decryptedCReq, CREQ.class);
        } catch (Exception e) {
            throw new ParseException(
                    ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR,
                    e);
        }
        if (null == creq) {
            throw new ParseException(
                    ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR);
        }
        return creq;
    }
}
