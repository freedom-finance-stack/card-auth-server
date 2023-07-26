package org.ffs.razorpay.cas.acs.service.authvalue;

import org.ffs.razorpay.cas.acs.exception.ValidationException;
import org.ffs.razorpay.cas.acs.exception.checked.ACSException;
import org.ffs.razorpay.cas.dao.model.Transaction;

public interface AuthValueGenerator {

    String createAuthValue(Transaction transaction) throws ACSException, ValidationException;
}
