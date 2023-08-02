package org.ffs.razorpay.cas.acs.validator.rules;

import org.ffs.razorpay.cas.acs.exception.threeds.ValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validation {
    @SafeVarargs
    public static <T> void validate(String fieldName, T value, Rule<T>... rules)
            throws ValidationException {
        for (Rule<T> rule : rules) {
            try {
                rule.validate(value);
            } catch (ValidationException e) {
                log.error(
                        "Validation failed for rule: {}, field: {}",
                        rule.getClass().getSimpleName(),
                        fieldName);
                throw new ValidationException(
                        e.getThreeDSecureErrorCode(), "Invalid value for " + fieldName);
            }
        }
    }
}
