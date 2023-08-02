package org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic;

import java.util.UUID;
import java.util.stream.Stream;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IsUUIDTest {
    @ParameterizedTest
    @MethodSource("provideValidateValidIsUUID")
    public void testValidateValidIsNumeric(String value) throws ValidationException {
        IsUUID.isUUID().validate(value);
        // No exception should be thrown
    }

    public static Stream<String> provideValidateValidIsUUID() {
        return Stream.of(
                UUID.randomUUID().toString(), "550e8400-e29b-41d4-a716-446655440000", "", null);
    }

    @ParameterizedTest
    @MethodSource("provideValidateInValidIsUUID")
    public void testValidateInValidIsUUID(String value) {

        ValidationException exception =
                assertThrows(ValidationException.class, () -> IsUUID.isUUID().validate(value));
        assertEquals(
                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, exception.getThreeDSecureErrorCode());
        assertEquals(InternalErrorCode.INVALID_REQUEST, exception.getInternalErrorCode());
    }

    public static Stream<String> provideValidateInValidIsUUID() {
        return Stream.of("123", "550e8400 e29b 41d4 a716 446655440000", "0", "1", "abc");
    }
}
