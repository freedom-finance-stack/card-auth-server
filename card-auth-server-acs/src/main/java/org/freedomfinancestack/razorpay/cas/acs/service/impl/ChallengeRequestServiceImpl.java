package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;

import org.freedomfinancestack.razorpay.cas.acs.dto.AuthConfigDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.ValidateChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeRequestService;
import org.freedomfinancestack.razorpay.cas.acs.service.FeatureService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageTypeService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.FeatureEntityType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @Override
    public ChallengeResponse processBrwChallengeRequest(
            @NotNull final String strCReq, final String threeDSSessionData) {
        // todo handle browser refresh, timeout and multiple request, whitelisting allowed,
        // INCounter
        // todo state management for status
        // todo check all the status and phase updated peroperly
        // todo dynamic configurable UI
        log.info("processBrowserRequest - Received CReq Request - " + strCReq);
        CREQ cReq;
        Transaction transaction;
        ChallengeResponse challengeResponse = new ChallengeResponse();
        try {
            cReq = parseEncryptedRequest(strCReq);
            transaction = transactionService.findById(cReq.getAcsTransID());
            if (null == transaction || !transaction.isChallengeMandated()) {
                throw new ThreeDSException(
                        ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED,
                        InternalErrorCode.TRANSACTION_NOT_FOUND,
                        InternalErrorCode.TRANSACTION_NOT_FOUND.getDefaultErrorMessage()
                                + "FOR CHALLENGE");
            }
            if (transaction.getTransactionStatus().equals(TransactionStatus.FAILED)
                    || transaction.getTransactionStatus().equals(TransactionStatus.REJECTED)) {
                // handle
            } else {
                // log creq
                Map<MessageType, ThreeDSObject> threeDSMessageMap =
                        transactionMessageTypeService.getTransactionMessagesByTransactionId(
                                transaction.getId());
                transactionMessageTypeService.createAndSave(cReq, cReq.getAcsTransID());
                // validate Creq
                if (Util.isNullorBlank(cReq.getChallengeCancel())) {
                    challengeRequestValidator.validateRequest(
                            cReq,
                            (AREQ) threeDSMessageMap.get(MessageType.AReq),
                            (CRES) threeDSMessageMap.get(MessageType.CRes));
                }
                transaction.setPhase(Phase.CREQ);
                transaction.setThreedsSessionData(threeDSSessionData);
                AuthConfigDto authConfigDto = getAuthConfig(transaction);
                // Todo add OTP flow
            }

        } catch (ACSDataAccessException ex) {
            // handle exception
        } catch (ThreeDSException ex) {
            challengeResponse.setError(true);
            // create error response and add to response
        }

        return null;
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