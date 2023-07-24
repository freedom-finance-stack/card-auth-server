package com.razorpay.ffs.cas.acs.service.authvalue.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.exception.checked.ACSException;
import com.razorpay.ffs.cas.acs.service.authvalue.AuthValueGenerator;
import com.razorpay.ffs.cas.dao.model.Transaction;

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
