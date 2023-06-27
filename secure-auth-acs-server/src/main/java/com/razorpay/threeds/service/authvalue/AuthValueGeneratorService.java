package com.razorpay.threeds.service.authvalue;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.enums.Network;
import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.exception.checked.ErrorCode;
import com.razorpay.threeds.service.authvalue.impl.MasterCardAuthValueGeneratorImpl;
import com.razorpay.threeds.service.authvalue.impl.VisaAuthValueGeneratorImpl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "authValueGeneratorService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthValueGeneratorService {

  private final ApplicationContext applicationContext;

  public String getCAVV(@NonNull final Transaction transaction) throws ACSException {
    AuthValueGenerator authValueGenerator = getAuthValueGenerator(transaction);

    if (authValueGenerator == null) {
      log.error("getCAVV() Error occurred as auth value generator is empty or null");
      throw new ACSException(
          ErrorCode.AUTH_VALUE_GENERATOR_NOT_FOUND.getCode(),
          ErrorCode.AUTH_VALUE_GENERATOR_NOT_FOUND.getDefaultErrorMessage());
    }
    return authValueGenerator.createCAVV(transaction);
  }

  private AuthValueGenerator getAuthValueGenerator(@NonNull final Transaction transaction)
      throws ACSException {
    if (transaction.getTransactionCardDetail() == null
        || transaction.getTransactionCardDetail().getNetworkCode() == null) {
      log.error(
          "getAuthValueGenerator() Error occurred while fetching correct auth value generator");
      throw new ACSException(
          ErrorCode.AUTH_VALUE_GENERATOR_NOT_FOUND.getCode(),
          ErrorCode.AUTH_VALUE_GENERATOR_NOT_FOUND.getDefaultErrorMessage());
    }

    switch (Objects.requireNonNull(
        Network.getNetwork(transaction.getTransactionCardDetail().getNetworkCode().intValue()))) {
      case VISA:
        return applicationContext.getBean(VisaAuthValueGeneratorImpl.class);
      case MASTERCARD:
        return applicationContext.getBean(MasterCardAuthValueGeneratorImpl.class);
    }
    return null;
  }
}
