package org.freedomfinancestack.razorpay.cas.admin.exception.admin;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;

public class ACSAdminDataAccessException extends ACSAdminException {

    private static final long serialVersionUID = 1L;

    public ACSAdminDataAccessException(
            InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(internalErrorCode, message, cause);
    }

    public ACSAdminDataAccessException(InternalErrorCode internalErrorCode, Throwable cause) {
        super(internalErrorCode, cause);
    }

    public ACSAdminDataAccessException(InternalErrorCode internalErrorCode) {
        super(internalErrorCode);
    }

    public ACSAdminDataAccessException(InternalErrorCode internalErrorCode, String message) {
        super(internalErrorCode, message);
    }
}
