package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;

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
