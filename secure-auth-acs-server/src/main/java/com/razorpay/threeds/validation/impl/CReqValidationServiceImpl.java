package com.razorpay.threeds.validation.impl;

import com.razorpay.acs.dao.contract.CREQ;
import com.razorpay.threeds.validation.ValidationService;

import org.springframework.stereotype.Service;

@Service("cReqValidationServiceImpl")
public class CReqValidationServiceImpl implements ValidationService<CREQ> {

    @Override
    public void validate(CREQ creq) {}
}
