package org.freedomfinancestack.razorpay.cas.acs.exception;

import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.extensions.validation.exception.ValidationErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class ValidationErrorCodeMapper {
    private static final Map<ValidationErrorCode, ThreeDSecureErrorCode>
            VALIDATION_TO_THREEDSECURE_ERROR_MAP = new HashMap<>();

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

    public static ThreeDSecureErrorCode mapValidationToThreeDSecure(
            ValidationErrorCode validationErrorCode) {
        return VALIDATION_TO_THREEDSECURE_ERROR_MAP.get(validationErrorCode);
    }
}
