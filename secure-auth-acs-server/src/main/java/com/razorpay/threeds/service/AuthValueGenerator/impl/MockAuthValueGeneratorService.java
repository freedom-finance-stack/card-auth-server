package com.razorpay.threeds.service.AuthValueGenerator.impl;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.service.AuthValueGenerator.AuthValueGeneratorService;
import com.razorpay.threeds.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service("mockAuthValueGeneratorService")
public class MockAuthValueGeneratorService implements AuthValueGeneratorService {
    @Override
    public String generateCAVV(Transaction transaction) {
        return Util.generateUUID();
    }
}
