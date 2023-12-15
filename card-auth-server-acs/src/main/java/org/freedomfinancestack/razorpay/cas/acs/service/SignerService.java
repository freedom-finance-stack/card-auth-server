package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.SignerServiceException;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.CRES;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface SignerService {

    String generateEncryptedResponse(Transaction transaction, CRES cres) throws ACSException;

    String getAcsSignedContent(AREQ areq, Transaction transaction, String acsUrl)
            throws SignerServiceException;
}
