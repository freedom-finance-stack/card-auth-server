package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class IsInTest {

    @Test
    public void testValidateValidIsIn() throws ValidationException {
        String[] arr = {"123", "456", "789"};
        IsIn.isIn(arr).validate("123");
        // No exception should be thrown
    }

    @Test
    public void testValidateNotValidIsIn() {
        String[] arr = {"123", "456", "789"};
        assertThrows(ValidationException.class, () -> IsIn.isIn(arr).validate("1232"));
    }

    @Test
    public void testValidateEmptyValidIsIn() throws ValidationException {
        String[] arr = {"123", "456", "789"};
        IsIn.isIn(arr).validate(null);
        // No exception should be thrown
    }
}
