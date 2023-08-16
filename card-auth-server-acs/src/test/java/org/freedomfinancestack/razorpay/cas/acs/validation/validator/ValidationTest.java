package org.freedomfinancestack.razorpay.cas.acs.validation.validator;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationTest {

    @Test
    void testValidateWithValidValue() throws ValidationException {
        Validator<String> mockValidator =
                value -> {
                    if ("valid".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        Validation.validate("fieldName", "valid", mockValidator);
    }

    @Test
    void testValidateWithInvalidValue() {
        Validator<String> mockValidator =
                value -> {
                    if ("valid".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };
        ValidationException validationException =
                assertThrows(
                        ValidationException.class,
                        () -> Validation.validate("fieldName", "invalid", mockValidator));
        assertEquals(InternalErrorCode.INVALID_REQUEST, validationException.getInternalErrorCode());
        assertEquals("Invalid value for fieldName", validationException.getMessage());
    }

    @Test
    void testValidateWithMultipleValidators() throws ValidationException {

        Validator<String> mockValidator1 =
                value -> {
                    if ("valid".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };

        Validator<String> mockValidator2 =
                value -> {
                    if ("valid".equals(value)) {
                        // Do nothing, valid value
                    } else {
                        throw new ValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "");
                    }
                };
        Validation.validate("fieldName", "valid", mockValidator1, mockValidator2);
    }

    @Test
    void testValidateWithValidationException() {

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

        assertThrows(
                ValidationException.class,
                () -> Validation.validate("fieldName", "valid1", mockValidator1, mockValidator2));
    }
}
