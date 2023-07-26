package com.razorpay.ffs.cas.acs.validator.rules;

import java.util.List;

import com.razorpay.ffs.cas.acs.exception.ValidationException;

public class ValidListRule<T> implements Rule<List<T>> {

    private final Rule<T> ruleToApply;

    public ValidListRule(Rule<T> ruleToApply) {
        this.ruleToApply = ruleToApply;
    }

    @Override
    public void validate(List<T> values) throws ValidationException {
        for (T nextElement : values) {
            ruleToApply.validate(nextElement);
        }
    }
}
