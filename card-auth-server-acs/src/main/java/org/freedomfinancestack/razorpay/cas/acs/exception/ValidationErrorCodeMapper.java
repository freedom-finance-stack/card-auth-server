package org.freedomfinancestack.razorpay.cas.acs.exception;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.extensions.validation.exception.ValidationErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.validation.ThreeDSDataElement;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class ValidationErrorCodeMapper {
    private static final Map<ValidationErrorCode, ThreeDSecureErrorCode>
            VALIDATION_TO_THREEDSECURE_ERROR_MAP = new EnumMap<>(ValidationErrorCode.class);

    private static final Map<String, ThreeDSecureErrorCode> FIELD_TO_THREEDSECURE_ERROR_MAP =
            new HashMap<>();

    static {
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT, ThreeDSecureErrorCode.INVALID_FORMAT);
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT_LENGTH,
                ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH);
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT_VALUE,
                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE);
        VALIDATION_TO_THREEDSECURE_ERROR_MAP.put(
                ValidationErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                ThreeDSecureErrorCode.REQUIRED_DATA_ELEMENT_MISSING);
    }

    static {
        FIELD_TO_THREEDSECURE_ERROR_MAP.put(
                ThreeDSDataElement.MESSAGE_VERSION.getFieldName(),
                ThreeDSecureErrorCode.MESSAGE_VERSION_NUMBER_NOT_SUPPORTED);
        FIELD_TO_THREEDSECURE_ERROR_MAP.put(
                ThreeDSDataElement.MESSAGE_EXTENSION_CRITICAL_INDICATOR.getFieldName(),
                ThreeDSecureErrorCode.CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED);
    }

    public static ThreeDSecureErrorCode mapValidationToThreeDSecure(
            ValidationErrorCode validationErrorCode, String fieldName) {
        if (FIELD_TO_THREEDSECURE_ERROR_MAP.containsKey(fieldName)) {
            return FIELD_TO_THREEDSECURE_ERROR_MAP.get(fieldName);
        }
        return VALIDATION_TO_THREEDSECURE_ERROR_MAP.getOrDefault(
                validationErrorCode, ThreeDSecureErrorCode.INVALID_FORMAT);
    }
}
