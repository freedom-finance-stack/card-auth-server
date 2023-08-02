package org.freedomfinancestack.razorpay.cas.acs.hsm;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.HSMConnectionException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface CvvHSM {

    String generateCVV(Transaction transaction, String data) throws HSMConnectionException;
}
