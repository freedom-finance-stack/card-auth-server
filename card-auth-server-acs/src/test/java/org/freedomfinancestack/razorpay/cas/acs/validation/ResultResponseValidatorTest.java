package org.freedomfinancestack.razorpay.cas.acs.validation;

import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.extensions.validation.validator.Validation;
import org.freedomfinancestack.razorpay.cas.acs.data.RREQTestData;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ACSValidationException;
import org.freedomfinancestack.razorpay.cas.contract.RREQ;
import org.freedomfinancestack.razorpay.cas.contract.RRES;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ResultResponseValidatorTest {

    @InjectMocks
    private ResultResponseValidator resultResponseValidator;

    @Test
    void validateRequest_ValidData() throws ACSValidationException {
        // Arrange
        RRES validRRes = RREQTestData.getValidRRes();
        RREQ validRReq = RREQTestData.getValidRReq();

        // Act
        resultResponseValidator.validateRequest(validRRes, validRReq);

        // Assert
        // No exception should be thrown for valid data
    }

    @Test
    void validateRequest_NullIncomingRRes() {
        // Arrange
        RREQ validRReq = RREQTestData.getValidRReq();

        // Act & Assert
        assertThrows(ACSValidationException.class, () -> resultResponseValidator.validateRequest(null, validRReq));
    }
    @Test
    void validateRequest_NullRReq() {
        // Arrange
        RRES validRRes = RREQTestData.getValidRRes();

        // Act & Assert
        assertThrows(ACSValidationException.class, () -> resultResponseValidator.validateRequest(validRRes, null));
    }


    @ParameterizedTest
    @MethodSource("provideTestDataRResValidation")
    void validateRequest_Invalid(String name, HashMap<String, Object> rreqDataMap, HashMap<String, Object> rresDataMap, ACSValidationException expectedException)  {
        // Arrange
        RREQ validRReq = RREQTestData.getRReq(rreqDataMap);
        RRES invalidRRes = RREQTestData.getRRes(rresDataMap);
        // Act & Assert
        ACSValidationException exception =  assertThrows(ACSValidationException.class, () -> resultResponseValidator.validateRequest(invalidRRes, validRReq));
        assertEquals(expectedException.getMessage(), exception.getMessage());
        assertEquals(expectedException.getInternalErrorCode(), exception.getInternalErrorCode());
        assertEquals(expectedException.getThreeDSecureErrorCode(), exception.getThreeDSecureErrorCode());
    }


    private static Stream<Arguments> provideTestDataRResValidation() {
        return Stream.of(
                Arguments.of("messageType incorrect", new HashMap<>(){},  new HashMap<>(){{put("messageType", "InvalidMessageType");}}, new ACSValidationException(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value for messageType")),
                Arguments.of("messageType empty", new HashMap<>(){},  new HashMap<>(){{put("messageType", "");}}, new ACSValidationException(ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value for messageType")),
                Arguments.of("MessageVersion incorrect", new HashMap<>(){},  new HashMap<>(){{put("messageVersion", "3.1.1");}}, new ACSValidationException(ThreeDSecureErrorCode.MESSAGE_VERSION_NUMBER_NOT_SUPPORTED, "Invalid value for messageVersion")),
                Arguments.of("MessageVersion empty", new HashMap<>(){},  new HashMap<>(){{put("messageVersion", "");}}, new ACSValidationException(ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value for messageVersion")),
                Arguments.of("MessageVersion not matching",  new HashMap<>(){{put("messageVersion", "2.1.0");}},  new HashMap<>(){{put("messageVersion", "2.2.0");}}, new ACSValidationException(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value for messageVersion")),
                Arguments.of("ThreeDSServerTransID empty",  new HashMap<>(){},  new HashMap<>(){{put("threeDSServerTransID", "");}}, new ACSValidationException(ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value for threeDSServerTransID")),
                Arguments.of("ThreeDSServerTransID not matching",  new HashMap<>(){{put("threeDSServerTransID", "ThreeDSServerTransID1");}},  new HashMap<>(){{put("threeDSServerTransID", "ThreeDSServerTransID12");}}, new ACSValidationException(ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED, "Invalid value for threeDSServerTransID")),
                Arguments.of("acsTransID empty",  new HashMap<>(){},  new HashMap<>(){{put("acsTransID", "");}}, new ACSValidationException(ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value for acsTransID")),
                Arguments.of("acsTransID not matching",  new HashMap<>(){{put("acsTransID", "acsTransID1");}},  new HashMap<>(){{put("acsTransID", "acsTransID12");}}, new ACSValidationException(ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED, "Invalid value for acsTransID")),
                Arguments.of("dsTransID empty",  new HashMap<>(){},  new HashMap<>(){{put("dsTransID", "");}}, new ACSValidationException(ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value for dsTransID")),
                Arguments.of("dsTransID not matching",  new HashMap<>(){{put("dsTransID", "dsTransID1");}},  new HashMap<>(){{put("dsTransID", "dsTransID12");}}, new ACSValidationException(ThreeDSecureErrorCode.TRANSACTION_ID_NOT_RECOGNISED, "Invalid value for dsTransID")),
                Arguments.of("resultsStatus incorrect", new HashMap<>(){},  new HashMap<>(){{put("resultsStatus", "InvalidMessageType");}}, new ACSValidationException(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value for resultsStatus")),
                Arguments.of("resultsStatus empty", new HashMap<>(){},  new HashMap<>(){{put("resultsStatus", "");}}, new ACSValidationException(ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING, "Invalid value for resultsStatus"))
                //todo add message extension
        );
    }

}

