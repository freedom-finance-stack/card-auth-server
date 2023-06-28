package com.razorpay.threeds.hsm;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.checked.ACSException;

public interface CvvHSM {

  String generateCVV(Transaction transaction, String data) throws ACSException;
}
