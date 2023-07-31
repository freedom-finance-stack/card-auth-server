package org.ffs.razorpay.cas.acs.exception.threeds;

import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;

public class ValidationException extends ThreeDSException {

    public ValidationException(ThreeDSecureErrorCode errorCode, String message) {
        super(errorCode, InternalErrorCode.INVALID_REQUEST, message);
    }

    protected ValidationException(
            ThreeDSecureErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, InternalErrorCode.INVALID_REQUEST, message, cause);
    }
}
