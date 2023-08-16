package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MinMaxValidatorTest {
    @Test
    void testStringMinLengthValidation() throws ValidationException {
        MinMaxValidator<String> validator = MinMaxValidator.minMaxValidator(2, 5);

        assertThrows(ValidationException.class, () -> validator.validate("1"));
        assertThrows(ValidationException.class, () -> validator.validate("A"));
        assertThrows(ValidationException.class, () -> validator.validate(""));
        validator.validate("12");
        validator.validate("12345");
    }

    @Test
    void testStringMaxLengthValidation() throws ValidationException {
        MinMaxValidator<String> validator = MinMaxValidator.minMaxValidator(2, 5);

        validator.validate("12");
        validator.validate("12345");
        assertThrows(ValidationException.class, () -> validator.validate("123456"));
        assertThrows(ValidationException.class, () -> validator.validate("1234567"));
    }

    @Test
    void testIntegerMinValidation() throws ValidationException {
        MinMaxValidator<Integer> validator = MinMaxValidator.minMaxValidator(10, 100);

        assertThrows(ValidationException.class, () -> validator.validate(5));
        assertThrows(ValidationException.class, () -> validator.validate(101));
        validator.validate(10);
        validator.validate(100);
    }

    @Test
    void testIntegerMaxValidation() throws ValidationException {
        MinMaxValidator<Integer> validator = MinMaxValidator.minMaxValidator(10, 100);

        validator.validate(10);
        validator.validate(100);
        assertThrows(ValidationException.class, () -> validator.validate(9));
        assertThrows(ValidationException.class, () -> validator.validate(101));
    }
}
