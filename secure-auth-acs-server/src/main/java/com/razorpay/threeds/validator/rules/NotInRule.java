package com.razorpay.threeds.validator.rules;

import java.util.List;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.utils.Util;

public class NotInRule implements Rule<String> {

    private final List<String> excludedValues;

    public NotInRule(List<String> excludedValues) {
        this.excludedValues = excludedValues;
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (excludedValues.contains(value)) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "IsInRule failed");
        }
    }
}
