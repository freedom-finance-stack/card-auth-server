package org.freedomfinancestack.razorpay.cas.admin.validation.validator.basic;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotNull<T> implements Validator<T> {

    public static <T> NotNull<T> notNull() {
        return new NotNull<T>();
    }

    @Override
    public void validate(T value) throws RequestValidationException {
        if (null == value) {
            throw new RequestValidationException(
                    InternalErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value");
        } else if ("".equals(value.toString())) {
            throw new RequestValidationException(
                    InternalErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value");
        }
    }
}
