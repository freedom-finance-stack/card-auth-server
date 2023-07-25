package com.razorpay.ffs.cas.acs.validator.rules;

import java.util.List;

import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.utils.Util;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;

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
