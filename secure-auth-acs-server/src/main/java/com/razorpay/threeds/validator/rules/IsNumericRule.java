package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.utils.Util;

public class IsNumericRule implements Rule<String> {
    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (!value.matches("[0-9]+")) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
