package com.razorpay.threeds.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class ThreeDSException extends RuntimeException {

    private final ThreeDSecureErrorCode errorCode;

    public ThreeDSException(final String details,
                         final ThreeDSecureErrorCode errorCode) {
        super(details);
        this.errorCode = errorCode;
    }
    protected ThreeDSException(final String details,
                            final Throwable cause,
                            final ThreeDSecureErrorCode errorCode) {
        super(details, cause);
        this.errorCode = errorCode;
    }
    public ThreeDSException(final ThreeDSecureErrorCode errorCode) {
        this( errorCode.getErrorCode() , errorCode);
    }
    public ThreeDSecureErrorCode getErrorCode() {
        return errorCode;
    }
    public ThreeDSErrorResponse getErrorResponse() {
        return new ThreeDSErrorResponse( HttpStatus.INTERNAL_SERVER_ERROR,  errorCode.getErrorCode(), this.getMessage(), errorCode.getErrorComponent(), errorCode.getErrorDescription());
    }

}
