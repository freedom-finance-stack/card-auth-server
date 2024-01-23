package org.freedomfinancestack.razorpay.cas.acs.validation;

import java.util.HashMap;
import java.util.stream.Stream;

import org.freedomfinancestack.razorpay.cas.acs.data.AREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.contract.AREQ;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationRequestValidatorTest {

    @InjectMocks private AuthenticationRequestValidator authenticationRequestValidator;

    @Test
    void validateRequest_ValidData() throws ACSValidationException {
        // Arrange
        AREQ areq = AREQTestData.createSampleAREQ();

        // Act
        authenticationRequestValidator.validateRequest(areq);

        // Assert
        // No exception should be thrown for valid data
    }

    @Test
    void validateRequest_NullIncomingAReq() {
        assertThrows(
                ACSValidationException.class,
                () -> authenticationRequestValidator.validateRequest(null));
    }

    @ParameterizedTest
    @MethodSource("provideTestDataAReqValidation")
    void validateRequest_Invalid(
            String name,
            HashMap<String, Object> areqDataMap,
            ACSValidationException expectedException) {
        // Arrange
        AREQ aReq = AREQTestData.getAReq(areqDataMap);
        // Act & Assert
        ACSValidationException exception =
                assertThrows(
                        ACSValidationException.class,
                        () -> authenticationRequestValidator.validateRequest(aReq));
        assertEquals(expectedException.getMessage(), exception.getMessage());
        assertEquals(expectedException.getInternalErrorCode(), exception.getInternalErrorCode());
        assertEquals(
                expectedException.getThreeDSecureErrorCode(), exception.getThreeDSecureErrorCode());
    }

    private static Stream<Arguments> provideTestDataAReqValidation() {
        return Stream.of(
                Arguments.of(
                        "messageType incorrect",
                        new HashMap<>() {
                            {
                                put("messageType", "InvalidMessageType");
                            }
                        },
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE,
                                "Invalid value for messageType")),
                Arguments.of(
                        "messageVersion incorrect",
                        new HashMap<>() {
                            {
                                put("messageVersion", "InvalidMessageVersion");
                            }
                        },
                        new ACSValidationException(
                                ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                                "Invalid value for messageVersion")),
                Arguments.of(
                        "messageVersion null",
                        new HashMap<>() {
                            {
                                put("messageVersion", null);
                            }
                        },
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for messageVersion")),
                Arguments.of(
                        "messageVersion empty",
                        new HashMap<>() {
                            {
                                put("messageVersion", "");
                            }
                        },
                        new ACSValidationException(
                                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                                "Invalid value for messageVersion")));
    }

    @Test
    void isMessageVersionValid_ValidMessageVersion_ReturnsTrue() {
        // Given
        String validMessageVersion = "2.1.0";

        // When
        boolean result = AuthenticationRequestValidator.isMessageVersionValid(validMessageVersion);

        // Then
        assertTrue(result, "Valid message version should return true");
    }

    @Test
    void isMessageVersionValid_InvalidMessageVersion_ReturnsFalse() {
        // Given
        String invalidMessageVersion = "invalid";

        // When
        boolean result =
                AuthenticationRequestValidator.isMessageVersionValid(invalidMessageVersion);

        // Then
        assertFalse(result, "Invalid message version should return false");
    }

    @Test
    void isMessageVersionValid_NullMessageVersion_ReturnsFalse() {
        // Given
        String nullMessageVersion = null;

        // When
        boolean result = AuthenticationRequestValidator.isMessageVersionValid(nullMessageVersion);

        // Then
        assertFalse(result, "Null message version should return false");
    }

    @Test
    void isMessageVersionValid_EmptyMessageVersion_ReturnsFalse() {
        // Given
        String emptyMessageVersion = "";

        // When
        boolean result = AuthenticationRequestValidator.isMessageVersionValid(emptyMessageVersion);

        // Then
        assertFalse(result, "Empty message version should return false");
    }
}
