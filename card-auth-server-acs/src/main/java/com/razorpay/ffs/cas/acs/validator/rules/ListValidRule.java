package com.razorpay.ffs.cas.acs.validator.rules;

import java.util.List;

import com.razorpay.ffs.cas.acs.exception.ValidationException;

public class ListValidRule<T> implements Rule<T> {
    private final List<Rule<T>> rules;

    public ListValidRule(List<Rule<T>> rules) {
        this.rules = rules;
    }

    @Override
    public void validate(T value) throws ValidationException {
        for (Rule<T> rule : rules) {
            rule.validate(value);
        }
    }
}
