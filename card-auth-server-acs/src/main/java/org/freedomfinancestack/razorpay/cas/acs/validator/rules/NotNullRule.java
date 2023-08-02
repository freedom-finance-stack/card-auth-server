package org.freedomfinancestack.razorpay.cas.acs.validator.rules;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

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
