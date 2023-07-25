package com.razorpay.ffs.cas.acs.validator.rules;

import java.util.List;

import com.razorpay.ffs.cas.acs.exception.ValidationException;

public class WhenRule<T> implements Rule<T> {
    private final boolean condition;
    private final List<Rule<T>> rules;
    private List<Rule<T>> elseRules;

    public WhenRule(boolean condition, List<Rule<T>> rules, List<Rule<T>> elseRules) {
        this.condition = condition;
        this.rules = rules;
        this.elseRules = elseRules;
    }

    @SafeVarargs
    public WhenRule(boolean condition, Rule<T>... rules) {
        this.condition = condition;
        this.rules = List.of(rules);
    }

    public static <T> WhenRule<T> when(boolean condition, List<Rule<T>> rules) {
        return new WhenRule<T>(condition, rules, List.of());
    }

    public WhenRule<T> elseRules(List<Rule<T>> rules) {
        return new WhenRule<T>(condition, this.rules, rules);
    }

    @Override
    public void validate(T value) throws ValidationException {
        if (condition) {
            for (Rule<T> rule : rules) {
                rule.validate(value);
            }
        } else if (elseRules != null) {
            for (Rule<T> rule : elseRules) {
                rule.validate(value);
            }
        }
    }
}
