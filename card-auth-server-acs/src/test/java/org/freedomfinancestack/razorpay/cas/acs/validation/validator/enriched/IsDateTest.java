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

public class IsDateTest {
    @ParameterizedTest
    @MethodSource("provideValidateValidIsDate")
    public void testValidateValidIsDate(String format, String date) throws ValidationException {
        IsDate.isDate(format).validate(date);
    }

    public static Stream<Arguments> provideValidateValidIsDate() {
        return Stream.of(
                Arguments.of("yyyy-MM-dd", "2020-01-01"),
                Arguments.of("yyMM", "2012"),
                Arguments.of("dd/MM/yyyy", "01/01/2020"));
    }

    @ParameterizedTest
    @MethodSource("provideValidateInvalidIsDate")
    public void testValidateInvalidIsDate(String format, String date) {
        ValidationException e =
                assertThrows(ValidationException.class, () -> IsDate.isDate(format).validate(date));
        assertEquals(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, e.getThreeDSecureErrorCode());
    }

    public static Stream<Arguments> provideValidateInvalidIsDate() {
        return Stream.of(
                Arguments.of("yyyy-MM-dd", "2020-01-01 12:12:12"),
                Arguments.of("yyMM", "2012-01"),
                Arguments.of("dd/MM/yyyy", "01/01/2020 12:12:12"),
                Arguments.of("", "01/01/2020 12:12:12"));
    }

    @Test
    public void testValidateEmptyIsDate() throws ValidationException {
        IsDate.isDate("yyMM").validate(null);
    }
}
