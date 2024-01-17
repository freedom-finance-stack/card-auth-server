package org.freedomfinancestack.razorpay.cas.acs.exception.acs;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;

public class ImageProcessingException extends ACSException {
    private static final long serialVersionUID = 1L;

    public ImageProcessingException(
            InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(internalErrorCode, message, cause);
    }

    public ImageProcessingException(InternalErrorCode internalErrorCode, String message) {
        super(internalErrorCode, message);
    }
}
