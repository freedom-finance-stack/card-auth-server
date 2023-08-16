package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegexValidatorTest {
    @Test
    void testValidValue() throws ValidationException {
        String regexPattern =
                "\\d{3}-\\d{2}-\\d{4}"; // Example pattern for a social security number
        RegexValidator validator = RegexValidator.regexValidator(regexPattern);

        validator.validate("123-45-6789");
    }

    @Test
    void testInvalidValue() {
        String regexPattern =
                "\\d{3}-\\d{2}-\\d{4}"; // Example pattern for a social security number
        RegexValidator validator = RegexValidator.regexValidator(regexPattern);

        assertThrows(
                ValidationException.class, () -> validator.validate("123456789")); // Invalid format
        assertThrows(
                ValidationException.class,
                () -> validator.validate("123-456-789")); // Invalid format
        assertThrows(
                ValidationException.class,
                () -> validator.validate("abc-def-ghij")); // Invalid format
    }

    @Test
    void testNullValue() throws ValidationException {
        String regexPattern =
                "\\d{3}-\\d{2}-\\d{4}"; // Example pattern for a social security number
        RegexValidator validator = RegexValidator.regexValidator(regexPattern);

        validator.validate(null);
    }

    @Test
    void testBlankValue() throws ValidationException {
        String regexPattern =
                "\\d{3}-\\d{2}-\\d{4}"; // Example pattern for a social security number
        RegexValidator validator = RegexValidator.regexValidator(regexPattern);

        validator.validate("");
    }
}
