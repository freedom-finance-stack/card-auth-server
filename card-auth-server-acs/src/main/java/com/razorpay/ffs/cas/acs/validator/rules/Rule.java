package com.razorpay.ffs.cas.acs.validator.rules;

import com.razorpay.ffs.cas.acs.exception.ValidationException;

public interface Rule<T> {
    void validate(T value) throws ValidationException;

    default Rule<T> and(Rule<T> other) {
        return value -> {
            validate(value);
            other.validate(value);
        };
    }
}
