package com.razorpay.ffs.cas.acs.hsm;

import com.razorpay.ffs.cas.acs.exception.HSMConnectionException;
import com.razorpay.ffs.cas.dao.model.Transaction;

public interface CvvHSM {

    String generateCVV(Transaction transaction, String data) throws HSMConnectionException;
}
