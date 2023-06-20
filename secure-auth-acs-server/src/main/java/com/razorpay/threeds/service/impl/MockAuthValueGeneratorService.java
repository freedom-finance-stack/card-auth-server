package com.razorpay.threeds.service.impl;

import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.service.AuthValueGeneratorService;
import com.razorpay.threeds.utils.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("mockAuthValueGeneratorService")
public class MockAuthValueGeneratorService implements AuthValueGeneratorService {
  @Override
  public String generateCAVV(Transaction transaction) {
    return Util.generateUUID();
  }
}
