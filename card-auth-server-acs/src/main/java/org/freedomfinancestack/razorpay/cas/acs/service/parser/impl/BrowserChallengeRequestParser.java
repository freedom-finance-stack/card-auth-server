package org.freedomfinancestack.razorpay.cas.acs.service.parser.impl;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.service.parser.ChallengeRequestParser;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.decodeBase64;
import static org.freedomfinancestack.razorpay.cas.acs.utils.Util.fromJson;

@Service
@RequiredArgsConstructor
public class BrowserChallengeRequestParser implements ChallengeRequestParser {
    @Override
    public CREQ parseEncryptedRequest(String strCReq) throws ParseException {
        CREQ creq;
        if (Util.isNullorBlank(strCReq)) {
            throw new ParseException(
                    ThreeDSecureErrorCode.DATA_DECRYPTION_FAILURE,
                    InternalErrorCode.CREQ_JSON_PARSING_ERROR);
        }

        try {
            return Util.fromJson(strCReq, CREQ.class);
        } catch (Exception e) {
            // Do Nothing
        }

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

    @Override
    public String generateEncryptedResponse(
            ChallengeFlowDto challengeFlowDto, Transaction transaction) {
        if (challengeFlowDto.isSendEmptyResponse()) {
            return null;
        } else if (challengeFlowDto.getErrorResponse() != null) {
            return Util.encodeBase64Url(challengeFlowDto.getErrorResponse());
        }
        return Util.encodeBase64Url(challengeFlowDto.getCres());
    }
}
