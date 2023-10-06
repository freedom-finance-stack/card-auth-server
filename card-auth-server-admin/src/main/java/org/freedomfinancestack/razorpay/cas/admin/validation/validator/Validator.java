package org.freedomfinancestack.razorpay.cas.admin.validation.validator;

import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;

public interface Validator<T> {
    void validate(T value) throws RequestValidationException;

    default Validator<T> and(Validator<T> other) {
        return value -> {
            validate(value);
            other.validate(value);
        };
    }
}
