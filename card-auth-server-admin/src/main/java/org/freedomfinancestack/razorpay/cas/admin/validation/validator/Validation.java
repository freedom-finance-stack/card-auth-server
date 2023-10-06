package org.freedomfinancestack.razorpay.cas.admin.validation.validator;

import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validation {
    @SafeVarargs
    public static <T> void validate(String fieldName, T value, Validator<T>... validationRules)
            throws RequestValidationException {
        for (Validator<T> validationRule : validationRules) {
            try {
                validationRule.validate(value);
            } catch (RequestValidationException e) {
                log.error(
                        "Validation failed for rule: {}, field: {}",
                        validationRule.getClass().getSimpleName(),
                        fieldName);
                throw new RequestValidationException(
                        e.getErrorCode(), "Invalid value for " + fieldName);
            }
        }
    }
}
