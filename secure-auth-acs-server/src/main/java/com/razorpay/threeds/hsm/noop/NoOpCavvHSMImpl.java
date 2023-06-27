package com.razorpay.threeds.hsm.noop;

import org.springframework.stereotype.Service;

import com.razorpay.acs.dao.model.Transaction;
import com.razorpay.threeds.exception.checked.ACSException;
import com.razorpay.threeds.hsm.CavvHSM;

import lombok.NonNull;

@Service("noOpCavvHSM")
public class NoOpCavvHSMImpl implements CavvHSM {

  public static final String NO_OP_CVV_OUTPUT = "123";

  @Override
  public String generateCavv(@NonNull final Transaction transaction, @NonNull final String data)
      throws ACSException {
    return NO_OP_CVV_OUTPUT;
  }
}
