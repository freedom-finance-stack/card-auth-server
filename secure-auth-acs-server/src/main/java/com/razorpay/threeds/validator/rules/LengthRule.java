package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.validator.enums.DataLengthType;

public class LengthRule implements Rule<String> {

  private final int length;
  private final DataLengthType lengthType;

  public LengthRule(DataLengthType lengthType, int length) {
    this.length = length;
    this.lengthType = lengthType;
  }

  @Override
  public void validate(String value) throws ValidationException {
    if (value == null || value.isEmpty()) {
      return;
    }
    if (DataLengthType.FIXED.equals(lengthType)) {
      if (length != value.length()) {
        throw new ValidationException(
            ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, "Invalid value ");
      }
    } else if (DataLengthType.VARIABLE.equals(lengthType)) {
      if (value.length() > length) {
        throw new ValidationException(
            ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, "Invalid value ");
      }
    }
  }
}
