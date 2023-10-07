package org.freedomfinancestack.razorpay.cas.admin.exception.admin;

import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.ValidationErrorCodeMapper;

public class RequestValidationException extends ACSAdminException {
    public RequestValidationException(InternalErrorCode internalErrorCode, String message) {
        super(internalErrorCode, message);
    }

    protected RequestValidationException(
            InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(internalErrorCode, message, cause);
    }

    protected RequestValidationException(InternalErrorCode internalErrorCode, Throwable cause) {
        super(internalErrorCode, cause);
    }

    protected RequestValidationException(InternalErrorCode internalErrorCode) {
        super(internalErrorCode);
    }

    public RequestValidationException(ValidationException validationException) {
        super(
                ValidationErrorCodeMapper.mapValidationToInternal(
                        validationException.getValidationErrorCode()),
                validationException.getMessage());
    }
}
