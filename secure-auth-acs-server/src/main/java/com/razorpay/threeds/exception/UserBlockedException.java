package com.razorpay.threeds.exception;

import org.springframework.http.HttpStatus;

public class UserBlockedException extends ThreeDSException {

    public UserBlockedException(ThreeDSecureErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    protected UserBlockedException(ThreeDSecureErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public UserBlockedException(ThreeDSecureErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public ThreeDSErrorResponse getErrorResponse() {
        return new ThreeDSErrorResponse(HttpStatus.OK, super.getErrorCode().getErrorCode(), super.getMessage(), super.getErrorCode().getErrorComponent(), super.getErrorCode().getErrorDescription());
    }
}
