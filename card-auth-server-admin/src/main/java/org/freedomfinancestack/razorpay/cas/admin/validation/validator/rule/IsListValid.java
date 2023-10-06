package org.freedomfinancestack.razorpay.cas.admin.validation.validator.rule;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;

public class IsListValid<T> implements Validator<List<T>> {

    private final Validator<T> validationRuleToApply;

    public IsListValid(Validator<T> validationRuleToApply) {
        this.validationRuleToApply = validationRuleToApply;
    }

    public static <T> IsListValid<T> isListValid(Validator<T> validationRuleToApply) {
        return new IsListValid<T>(validationRuleToApply);
    }

    @Override
    public void validate(List<T> values) throws RequestValidationException {
        if (Util.isNullorBlank(values)) {
            return;
        }

        for (T nextElement : values) {
            validationRuleToApply.validate(nextElement);
        }
    }
}
