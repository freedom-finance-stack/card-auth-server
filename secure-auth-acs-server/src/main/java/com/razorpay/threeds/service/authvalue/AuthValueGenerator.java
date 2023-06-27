package com.razorpay.threeds.service.authvalue;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.checked.ACSException;

public interface AuthValueGenerator {

  String createCAVV(Transaction transaction) throws ACSException;
}
