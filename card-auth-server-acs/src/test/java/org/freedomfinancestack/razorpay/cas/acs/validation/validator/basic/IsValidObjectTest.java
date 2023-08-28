package org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic;

import java.util.stream.Stream;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.Validatable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IsValidObjectTest {

    private static Stream<Validatable> validatableProvider() {
        // Define valid and invalid Validatable objects for testing
        return Stream.of(() -> true, null);
    }

    private static Stream<Validatable> inValidValidatableProvider() {
        // Define valid and invalid Validatable objects for testing
        return Stream.of(() -> false);
    }

    @ParameterizedTest
    @DisplayName("Validate Validatable objects")
    @MethodSource("validatableProvider")
    void testIsValidObject(Validatable validatable) throws ValidationException {
        // No exception should be thrown for valid Validatable objects
        IsValidObject.isValidObject().validate(validatable);
    }

    @ParameterizedTest
    @DisplayName("Validate invalid Validatable objects")
    @MethodSource("inValidValidatableProvider")
    void testInvalidValidatableObjects(Validatable validatable) {
        ValidationException exception =
                assertThrows(
                        ValidationException.class,
                        () -> IsValidObject.isValidObject().validate(validatable));
        assertEquals(
                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, exception.getThreeDSecureErrorCode());
        assertEquals(InternalErrorCode.INVALID_REQUEST, exception.getInternalErrorCode());
    }
}
