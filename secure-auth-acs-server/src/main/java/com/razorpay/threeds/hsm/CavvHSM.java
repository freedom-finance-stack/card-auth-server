package com.razorpay.threeds.hsm;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.checked.ACSException;

public interface CavvHSM {

  String generateCavv(Transaction transaction, String data) throws ACSException;
}
