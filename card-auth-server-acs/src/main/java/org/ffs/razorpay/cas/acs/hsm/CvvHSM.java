package org.ffs.razorpay.cas.acs.hsm;

import org.ffs.razorpay.cas.acs.exception.HSMConnectionException;
import org.ffs.razorpay.cas.dao.model.Transaction;

public interface CvvHSM {

    String generateCVV(Transaction transaction, String data) throws HSMConnectionException;
}
