package com.razorpay.threeds.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends ThreeDSException {

    public ValidationException(ThreeDSecureErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    protected ValidationException(ThreeDSecureErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public ValidationException(ThreeDSecureErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public ThreeDSErrorResponse getErrorResponse() {
        //todo check Status code to be sent to DS
        return new ThreeDSErrorResponse(HttpStatus.OK, super.getErrorCode().getErrorCode(), super.getMessage(), super.getErrorCode().getErrorComponent(), super.getErrorCode().getErrorDescription());
    }
}
