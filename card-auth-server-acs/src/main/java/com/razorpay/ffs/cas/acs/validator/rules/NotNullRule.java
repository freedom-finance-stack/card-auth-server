package com.razorpay.ffs.cas.acs.validator.rules;

import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;

public class NotNullRule<T> implements Rule<T> {

    @Override
    public void validate(T value) throws ValidationException {
        if (null == value) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value");
        } else if ("".equals(value.toString())) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value");
        }
    }
}
