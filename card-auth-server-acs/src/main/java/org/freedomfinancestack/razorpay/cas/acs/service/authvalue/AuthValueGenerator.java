package org.freedomfinancestack.razorpay.cas.acs.service.authvalue;

import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;

public interface AuthValueGenerator {

    String createAuthValue(Transaction transaction) throws ACSException, ValidationException;
}
