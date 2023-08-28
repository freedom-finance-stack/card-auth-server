package org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic;

import java.util.stream.Stream;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsNumericTest {

    @ParameterizedTest
    @MethodSource("provideValidateValidIsNumeric")
    public void testValidateValidIsNumeric(String value) throws ValidationException {
        IsNumeric.isNumeric().validate(value);
        // No exception should be thrown
    }

    public static Stream<String> provideValidateValidIsNumeric() {
        return Stream.of("123", "0", "1", "", null);
    }

    @Test
    public void testValidateInvalidIsNumeric() throws ValidationException {
        String value = "abc123";
        try {
            IsNumeric.isNumeric().validate(value);
        } catch (ValidationException e) {
            assertEquals(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, e.getThreeDSecureErrorCode());
            assertEquals(InternalErrorCode.INVALID_REQUEST, e.getInternalErrorCode());
        }
    }
}
