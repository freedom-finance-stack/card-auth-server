package org.freedomfinancestack.razorpay.cas.acs.exception;

import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.extensions.validation.exception.ValidationErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.validation.ThreeDSDataElement;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class ValidationErrorCodeMapper {
    private static final Map<ValidationErrorCode, ThreeDSecureErrorCode>
            VALIDATION_TO_THREEDSECURE_ERROR_MAP = new HashMap<>();

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
    }

    public static ThreeDSecureErrorCode mapValidationToThreeDSecure(
            ValidationErrorCode validationErrorCode, String fieldName) {
        if (FIELD_TO_THREEDSECURE_ERROR_MAP.containsKey(fieldName)) {
            return FIELD_TO_THREEDSECURE_ERROR_MAP.get(fieldName);
        }
        if (VALIDATION_TO_THREEDSECURE_ERROR_MAP.containsKey(validationErrorCode)) {
            return VALIDATION_TO_THREEDSECURE_ERROR_MAP.get(validationErrorCode);
        }
        return ThreeDSecureErrorCode.INVALID_FORMAT;
    }
}
