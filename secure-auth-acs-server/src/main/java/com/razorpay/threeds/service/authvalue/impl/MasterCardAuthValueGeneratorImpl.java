package com.razorpay.threeds.service.authvalue.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.service.authvalue.AuthValueGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "masterCardAuthValueGeneratorImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MasterCardAuthValueGeneratorImpl implements AuthValueGenerator {

  @Override
  public String createAuthValue(Transaction transaction) throws ACSException, ValidationException {
    return null;
  }
}
