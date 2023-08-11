package org.freedomfinancestack.razorpay.cas.acs.validation.validator;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;

public interface Validator<T> {
    void validate(T value) throws ValidationException;

    default Validator<T> and(Validator<T> other) {
        return value -> {
            validate(value);
            other.validate(value);
        };
    }
}
