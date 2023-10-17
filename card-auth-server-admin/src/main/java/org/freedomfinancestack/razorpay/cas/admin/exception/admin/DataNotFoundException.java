package org.freedomfinancestack.razorpay.cas.admin.exception.admin;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;

public class DataNotFoundException extends ACSAdminException {
    protected DataNotFoundException(InternalErrorCode internalErrorCode, String message) {
        super(internalErrorCode, message);
    }

    protected DataNotFoundException(
            InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(internalErrorCode, message, cause);
    }

    protected DataNotFoundException(InternalErrorCode internalErrorCode, Throwable cause) {
        super(internalErrorCode, cause);
    }

    public DataNotFoundException(InternalErrorCode internalErrorCode) {
        super(internalErrorCode);
    }
}
