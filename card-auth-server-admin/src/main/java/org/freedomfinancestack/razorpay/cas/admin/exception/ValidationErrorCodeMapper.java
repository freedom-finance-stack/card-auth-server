package org.freedomfinancestack.razorpay.cas.admin.exception;

import java.util.HashMap;
import java.util.Map;

import org.freedomfinancestack.extensions.validation.exception.ValidationErrorCode;

public class ValidationErrorCodeMapper {

    private static final Map<ValidationErrorCode, InternalErrorCode>
            VALIDATION_TO_INTERNAL_ERROR_MAP = new HashMap<>();

    static {
        VALIDATION_TO_INTERNAL_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT, InternalErrorCode.INVALID_FORMAT);
        VALIDATION_TO_INTERNAL_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT_LENGTH, InternalErrorCode.INVALID_FORMAT_LENGTH);
        VALIDATION_TO_INTERNAL_ERROR_MAP.put(
                ValidationErrorCode.INVALID_FORMAT_VALUE, InternalErrorCode.INVALID_FORMAT_VALUE);
        VALIDATION_TO_INTERNAL_ERROR_MAP.put(
                ValidationErrorCode.REQUIRED_DATA_ELEMENT_MISSING,
                InternalErrorCode.REQUIRED_DATA_ELEMENT_MISSING);
    }

    public static InternalErrorCode mapValidationToInternal(
            ValidationErrorCode validationErrorCode) {
        return VALIDATION_TO_INTERNAL_ERROR_MAP.get(validationErrorCode);
    }
}
