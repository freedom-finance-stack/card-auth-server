package org.freedomfinancestack.razorpay.cas.acs.service.parser;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ParseException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.TransactionDataNotValidException;
import org.freedomfinancestack.razorpay.cas.contract.CREQ;

public interface ChallengeRequestParser {
    CREQ parseEncryptedRequest(String creq) throws ParseException, TransactionDataNotValidException;
}
