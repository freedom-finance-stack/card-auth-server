package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import java.util.Arrays;
import java.util.List;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotInTest {
    @Test
    void testValueNotInList() throws ValidationException {
        List<String> excludedValues = Arrays.asList("apple", "banana", "cherry");
        NotIn validator = NotIn.notIn(excludedValues);

        assertThrows(ValidationException.class, () -> validator.validate("apple"));
        assertThrows(ValidationException.class, () -> validator.validate("banana"));
        assertThrows(ValidationException.class, () -> validator.validate("cherry"));
        validator.validate("orange");
    }

    @Test
    void testNullValue() throws ValidationException {
        List<String> excludedValues = Arrays.asList("apple", "banana", "cherry");
        NotIn validator = NotIn.notIn(excludedValues);
        validator.validate(null);
    }

    @Test
    void testBlankValue() throws ValidationException {
        List<String> excludedValues = Arrays.asList("apple", "banana", "cherry");
        NotIn validator = NotIn.notIn(excludedValues);
        validator.validate("");
    }

    @Test
    void testValueNotInEmptyList() throws ValidationException {
        List<String> excludedValues = Arrays.asList();
        NotIn validator = NotIn.notIn(excludedValues);
        validator.validate("apple");
    }
}
