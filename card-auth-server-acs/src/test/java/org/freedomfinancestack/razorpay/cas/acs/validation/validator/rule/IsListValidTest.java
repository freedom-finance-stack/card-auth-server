package org.freedomfinancestack.razorpay.cas.acs.validation.validator.rule;

import java.util.ArrayList;
import java.util.List;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IsListValidTest {

    @Test
    void testIsListValidWithValidValues() throws ValidationException {

        Validator<String> mockValidator =
                value -> {
                    if ("valid".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        List<String> validValues = new ArrayList<>();
        validValues.add("valid");
        validValues.add("valid");
        validValues.add("valid");

        IsListValid<String> isListValid = IsListValid.isListValid(mockValidator);

        assertDoesNotThrow(() -> isListValid.validate(validValues));
    }

    @Test
    void testIsListValidWithInvalidValues() throws ValidationException {
        Validator<String> mockValidator =
                value -> {
                    if ("valid".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        List<String> invalidValues = new ArrayList<>();
        invalidValues.add("valid");
        invalidValues.add("invalid");
        invalidValues.add("valid");

        IsListValid<String> isListValid = IsListValid.isListValid(mockValidator);

        assertThrows(ValidationException.class, () -> isListValid.validate(invalidValues));
    }
}
