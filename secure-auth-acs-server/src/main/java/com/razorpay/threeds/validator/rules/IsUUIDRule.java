package com.razorpay.threeds.validator.rules;

import java.util.UUID;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;

public class IsUUIDRule implements Rule<String> {
  @Override
  public void validate(String value) throws ValidationException {
    try {
      UUID.fromString(value);
    } catch (IllegalArgumentException exception) {
      throw new ValidationException(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
    }
  }
}
