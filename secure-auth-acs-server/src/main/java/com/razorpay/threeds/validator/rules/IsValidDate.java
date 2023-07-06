package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class IsValidDate implements Rule<String> {

  private String acceptedFormat;

  public IsValidDate(String acceptedFormat) {
    this.acceptedFormat = acceptedFormat;
  }

  @Override
  public void validate(String value) throws ValidationException {
    SimpleDateFormat sdf = new SimpleDateFormat(acceptedFormat);
    sdf.setLenient(false);
    try {
      sdf.parse(value);
    } catch (ParseException ex) {
      throw new ValidationException(
          ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid date format");
    }
  }
}
