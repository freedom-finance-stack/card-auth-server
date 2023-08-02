package org.freedomfinancestack.razorpay.cas.acs.service.authvalue.impl;

import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ACSException;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.service.authvalue.AuthValueGenerator;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "masterCardAuthValueGeneratorImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MasterCardAuthValueGeneratorImpl implements AuthValueGenerator {

    @Override
    public String createAuthValue(Transaction transaction)
            throws ACSException, ValidationException {
        return null;
    }
}
