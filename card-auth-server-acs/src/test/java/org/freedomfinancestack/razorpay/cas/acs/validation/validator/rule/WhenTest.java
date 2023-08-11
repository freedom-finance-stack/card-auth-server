package org.freedomfinancestack.razorpay.cas.acs.validation.validator.rule;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WhenTest {

    @Test
    void testWhenConditionIsTrue() throws ValidationException {
        Validator<String> mockValidator =
                value -> {
                    if ("valid".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        When<String> when = When.when(true, mockValidator);

        when.validate("valid"); // Should pass without throwing an exception
        assertThrows(ValidationException.class, () -> when.validate("invalid"));
    }

    @Test
    void testWhenConditionIsFalse() throws ValidationException {
        Validator<String> mockValidator =
                value -> {
                    if ("valid".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        When<String> when = When.when(false, mockValidator);
        when.validate("valid");
        when.validate("InValid");
        // No exception should be thrown
    }

    @Test
    void testWhenWithElseRulesFalse() throws ValidationException {
        Validator<String> mockValidator1 =
                value -> {
                    if ("valid1".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        Validator<String> mockValidator2 =
                value -> {
                    if ("valid2".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        When<String> when = When.when(false, mockValidator1).elseRules(mockValidator2);

        when.validate("valid2"); // Should pass without throwing an exception
        assertThrows(ValidationException.class, () -> when.validate("valid1"));
        assertThrows(ValidationException.class, () -> when.validate("invalid"));
    }

    @Test
    void testWhenWithElseRulesTrue() throws ValidationException {
        Validator<String> mockValidator1 =
                value -> {
                    if ("valid1".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        Validator<String> mockValidator2 =
                value -> {
                    if ("valid2".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        When<String> when = When.when(true, mockValidator1).elseRules(mockValidator2);

        when.validate("valid1"); // Should pass without throwing an exception
        assertThrows(ValidationException.class, () -> when.validate("valid2"));
        assertThrows(ValidationException.class, () -> when.validate("invalid"));
    }
}
