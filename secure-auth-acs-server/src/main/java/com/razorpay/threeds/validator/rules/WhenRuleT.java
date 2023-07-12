package com.razorpay.threeds.validator.rules;

import com.razorpay.threeds.exception.ValidationException;

public class WhenRuleT<T> implements Rule<T> {
    private final boolean condition;
    private final Rule<T> rule;
    private Rule<T> elseRule;

    public WhenRuleT(boolean condition, Rule<T> rules, Rule<T> elseRules) {
        this.condition = condition;
        this.rule = rules;
        this.elseRule = elseRules;
    }

    public WhenRuleT(boolean condition, Rule<T> rules) {
        this.condition = condition;
        this.rule = rules;
    }

    public static <T> WhenRuleT<T> when(boolean condition, Rule<T> rules) {
        return new WhenRuleT<T>(condition, rules, null);
    }

    public WhenRuleT<T> elseRules(Rule<T> elseRule) {
        return new WhenRuleT<T>(condition, this.rule, elseRule);
    }

    @Override
    public void validate(T value) throws ValidationException {
        if (condition) {
            rule.validate(value);
        } else if (elseRule != null) {
            elseRule.validate(value);
        }
    }
}
