package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;

public class RegexRule implements Rule<String> {

  private final java.util.regex.Pattern pattern;

  public RegexRule(String regex) {
    this.pattern = java.util.regex.Pattern.compile(regex);
  }

  @Override
  public void validate(String value) throws ValidationException {
    if (value == null || !pattern.matcher(value).matches()) {
      throw new ValidationException(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
    }
  }
}
