package com.razorpay.threeds.exception;

import org.springframework.http.HttpStatus;

import com.razorpay.acs.contract.ThreeDSErrorResponse;
import com.razorpay.acs.contract.ThreeDSecureErrorCode;

public class ValidationException extends ThreeDSException {

    public ValidationException(ThreeDSecureErrorCode errorCode, String message) {
        super(errorCode, InternalErrorCode.INVALID_REQUEST, message);
    }

    protected ValidationException(
            ThreeDSecureErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, InternalErrorCode.INVALID_REQUEST, message, cause);
    }

    @Override
    public ThreeDSErrorResponse getErrorResponse() {
        // todo check Status code to be sent to DS
        return new ThreeDSErrorResponse(
                HttpStatus.OK.value(),
                super.getThreeDSecureErrorCode().getErrorCode(),
                super.getMessage(),
                super.getThreeDSecureErrorCode().getErrorComponent(),
                super.getThreeDSecureErrorCode().getErrorDescription());
    }
}
