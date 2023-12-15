package org.freedomfinancestack.razorpay.cas.acs.service.parser;

import org.freedomfinancestack.razorpay.cas.acs.dto.ChallengeFlowDto;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface ChallengeRequestParser {
    CREQ parseEncryptedRequest(String creq) throws ParseException, TransactionDataNotValidException;

    String generateEncryptedResponse(ChallengeFlowDto challengeFlowDto, Transaction transaction)
            throws ACSException;
}
