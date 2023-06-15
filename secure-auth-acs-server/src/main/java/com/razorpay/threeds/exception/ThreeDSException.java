package com.razorpay.threeds.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Optional;

abstract public class ThreeDSException extends RuntimeException {

    private final ThreeDSecureErrorCode errorCode;

    public ThreeDSException(final ThreeDSecureErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected ThreeDSException(final ThreeDSecureErrorCode errorCode, final String message,
                               final Throwable cause
    ) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ThreeDSException(final ThreeDSecureErrorCode errorCode) {
        this(errorCode, errorCode.getErrorCode());
    }

    public ThreeDSecureErrorCode getErrorCode() {
        return errorCode;
    }

    public ThreeDSErrorResponse getErrorResponse() {
        return new ThreeDSErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.getErrorCode(), errorCode.getErrorComponent(), errorCode.getErrorDescription(), this.getMessage());
    }

}
