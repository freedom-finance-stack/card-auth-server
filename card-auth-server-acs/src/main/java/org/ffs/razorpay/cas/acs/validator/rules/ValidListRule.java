package org.ffs.razorpay.cas.acs.validator.rules;

import java.util.List;

import org.ffs.razorpay.cas.acs.exception.threeds.ValidationException;

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
