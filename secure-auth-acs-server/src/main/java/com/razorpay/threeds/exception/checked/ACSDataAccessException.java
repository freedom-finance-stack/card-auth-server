package com.razorpay.threeds.exception.checked;

import com.razorpay.threeds.exception.InternalErrorCode;

// Checked Exception
public class ACSDataAccessException extends ACSException {

    /** */
    private static final long serialVersionUID = 1L;

    public ACSDataAccessException(
            InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(internalErrorCode, message, cause);
    }

    public ACSDataAccessException(InternalErrorCode internalErrorCode, Throwable cause) {
        super(internalErrorCode, cause);
    }

    public ACSDataAccessException(InternalErrorCode internalErrorCode) {
        super(internalErrorCode);
    }

    public ACSDataAccessException(InternalErrorCode internalErrorCode, String message) {
        super(internalErrorCode, message);
    }
}
