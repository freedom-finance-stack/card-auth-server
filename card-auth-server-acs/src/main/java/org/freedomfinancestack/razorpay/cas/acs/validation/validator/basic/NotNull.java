package org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class NotNull<T> implements Validator<T> {

    public static <T> NotNull<T> notNull() {
        return new NotNull<T>();
    }

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
