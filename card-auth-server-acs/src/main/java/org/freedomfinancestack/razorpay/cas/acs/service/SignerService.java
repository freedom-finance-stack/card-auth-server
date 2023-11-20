package org.freedomfinancestack.razorpay.cas.acs.service;

import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface SignerService {

    String getAcsSignedContent(AREQ areq, Transaction transaction, String acsUrl);
}
