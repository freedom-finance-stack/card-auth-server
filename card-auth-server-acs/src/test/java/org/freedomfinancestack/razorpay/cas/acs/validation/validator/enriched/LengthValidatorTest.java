package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import java.util.stream.Stream;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LengthValidatorTest {
    @Test
    public void testValidateValidLengthFixed() throws ValidationException {
        LengthValidator.lengthValidator(LengthValidator.DataLengthType.FIXED, 10)
                .validate("1234567890");
        LengthValidator.lengthValidator(LengthValidator.DataLengthType.FIXED, 10)
                .validate(""); // for empty string rule won't be applied
        LengthValidator.lengthValidator(LengthValidator.DataLengthType.FIXED, 10).validate(null);
        // No exception should be thrown
    }

    @Test
    public void testValidateValidLengthVariable() throws ValidationException {
        LengthValidator.lengthValidator(LengthValidator.DataLengthType.VARIABLE, 10)
                .validate("12340");
        LengthValidator.lengthValidator(LengthValidator.DataLengthType.VARIABLE, 10).validate("");
        LengthValidator.lengthValidator(LengthValidator.DataLengthType.VARIABLE, 10)
                .validate("12340123");
        LengthValidator.lengthValidator(LengthValidator.DataLengthType.FIXED, 10).validate(null);
        // No exception should be thrown
    }

    @ParameterizedTest
    @MethodSource("provideValidateInvalidLengthFixed")
    public void testValidateInValidLengthVariable(
            LengthValidator.DataLengthType dataLengthType, int length, String val) {
        ValidationException e =
                assertThrows(
                        ValidationException.class,
                        () ->
                                LengthValidator.lengthValidator(dataLengthType, length)
                                        .validate(val));
        assertEquals(ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, e.getThreeDSecureErrorCode());
    }

    public static Stream<Arguments> provideValidateInvalidLengthFixed() {
        return Stream.of(
                Arguments.of(LengthValidator.DataLengthType.FIXED, 10, "123456789"),
                Arguments.of(LengthValidator.DataLengthType.FIXED, 10, "12345678901"),
                Arguments.of(LengthValidator.DataLengthType.FIXED, 10, "123456789012"),
                Arguments.of(LengthValidator.DataLengthType.VARIABLE, 10, "1234567890123"),
                Arguments.of(LengthValidator.DataLengthType.VARIABLE, 10, "12345678901234567890"));
    }
}
