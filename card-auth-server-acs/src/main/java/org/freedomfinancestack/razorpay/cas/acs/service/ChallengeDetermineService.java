package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.dao.enums.RiskFlag;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface ChallengeDetermineService {
    void determineChallenge(AREQ objAReq, Transaction transaction, RiskFlag riskFlag)
            throws ACSException;
}
