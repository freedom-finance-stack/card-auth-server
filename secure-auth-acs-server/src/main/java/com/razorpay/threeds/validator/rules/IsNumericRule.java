package com.razorpay.threeds.validator.rules;

import org.apache.commons.lang3.StringUtils;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;

public class IsNumericRule implements Rule<String> {
    @Override
    public void validate(String value) throws ValidationException {
        if (StringUtils.isBlank(value)) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
        if (!value.matches("[0-9]+")) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
