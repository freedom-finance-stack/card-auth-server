package com.razorpay.ffs.cas.acs.service.authvalue;

import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSException;
import com.razorpay.ffs.cas.dao.model.Transaction;

public interface AuthValueGenerator {

    String createAuthValue(Transaction transaction) throws ACSException, ValidationException;
}
