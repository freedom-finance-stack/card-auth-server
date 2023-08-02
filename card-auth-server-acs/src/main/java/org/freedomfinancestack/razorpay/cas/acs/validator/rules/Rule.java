package org.freedomfinancestack.razorpay.cas.acs.validator.rules;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;

public interface Rule<T> {
    void validate(T value) throws ValidationException;

    default Rule<T> and(Rule<T> other) {
        return value -> {
            validate(value);
            other.validate(value);
        };
    }
}
