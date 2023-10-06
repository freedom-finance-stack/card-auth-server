package org.freedomfinancestack.razorpay.cas.admin.exception.admin;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;

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
}
