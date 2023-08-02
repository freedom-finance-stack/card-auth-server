package org.freedomfinancestack.razorpay.cas.acs.validator.rules;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;

public class WhenRule<T> implements Rule<T> {
    private final boolean condition;
    private final Rule<T> ifRule;
    private Rule<T> elseRule;

    public WhenRule(boolean condition, Rule<T> rule, Rule<T> elseRule) {
        this.condition = condition;
        this.ifRule = rule;
        this.elseRule = elseRule;
    }

    public WhenRule(boolean condition, Rule<T> rule) {
        this.condition = condition;
        this.ifRule = rule;
    }

    public static <T> WhenRule<T> when(boolean condition, Rule<T> rules) {
        return new WhenRule<T>(condition, rules, null);
    }

    public WhenRule<T> elseRules(Rule<T> rule) {
        return new WhenRule<T>(condition, this.ifRule, rule);
    }

    @Override
    public void validate(T value) throws ValidationException {
        if (condition) {
            this.ifRule.validate(value);
        } else if (this.elseRule != null) {
            this.elseRule.validate(value);
        }
    }
}
