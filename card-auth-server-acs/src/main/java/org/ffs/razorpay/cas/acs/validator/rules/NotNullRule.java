package org.ffs.razorpay.cas.acs.validator.rules;

import org.ffs.razorpay.cas.acs.exception.ValidationException;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;

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
