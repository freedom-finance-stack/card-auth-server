package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;

public class IsInRule implements Rule<String> {

  private String[] acceptedValues;

  public IsInRule(String[] acceptedValues) {
    this.acceptedValues = acceptedValues;
  }

  @Override
  public void validate(String value) throws ValidationException {
    if (value == null || value.isEmpty()) {
      return;
    }
    for (String acceptedValue : acceptedValues) {
      if (acceptedValue.equals(value)) {
        return;
      }
    }
    throw new ValidationException(
        ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, String.format("IsInRule failed"));
  }
}
