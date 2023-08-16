package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.Map;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.dto.ValidateChallengeResponse;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSDataAccessException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.acs.service.ChallengeService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionMessageTypeService;
import org.freedomfinancestack.razorpay.cas.acs.service.TransactionService;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.ChallengeRequestValidator;
import org.freedomfinancestack.razorpay.cas.contract.*;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.decodeBase64;
import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.fromJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final TransactionService transactionService;
    private final TransactionMessageTypeService transactionMessageTypeService;
    private final ChallengeRequestValidator challengeRequestValidator;

    @Override
    public ChallengeResponse processBrwChallengeRequest(String strCReq, String threeDSSessionData) {

        log.info("processBrowserRequest - Received CReq Request - " + strCReq);
        CREQ cReq;
        Transaction transaction;
        ChallengeResponse challengeResponse = new ChallengeResponse();
        try {
            cReq = parseEncryptedRequest(strCReq);
            transaction = transactionService.findById(cReq.getAcsTransID());
            if (null == transaction) {
                throw new ThreeDSException(
                        ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED,
                        InternalErrorCode.TRANSACTION_NOT_FOUND,
                        InternalErrorCode.TRANSACTION_NOT_FOUND.getDefaultErrorMessage());
            }
            if (transaction.getTransactionStatus().equals(TransactionStatus.FAILED)
                    || transaction.getTransactionStatus().equals(TransactionStatus.REJECTED)) {
                // handle
            } else {
                // log creq
                transactionMessageTypeService.createAndSave(cReq, cReq.getAcsTransID());
                Map<MessageType, ThreeDSObject> threeDSMessageMap =
                        transactionMessageTypeService.getTransactionMessagesByTransactionId(
                                transaction.getId());
                // validate Creq
                if (Util.isNullorBlank(cReq.getChallengeCancel())) {
                    challengeRequestValidator.validateRequest(
                            cReq,
                            (AREQ) threeDSMessageMap.get(MessageType.AReq),
                            (CRES) threeDSMessageMap.get(MessageType.CRes));
                }
            }

        } catch (ACSDataAccessException ex) {
            // handle exception
        } catch (ThreeDSException ex) {
            challengeResponse.setError(true);
            // create error response and add to response
        }

        return null;
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

    @Override
    public ValidateChallengeResponse validateChallengeRequest(
            ValidateChallengeRequest validateChallengeRequest) {
        return null;
    }
}
