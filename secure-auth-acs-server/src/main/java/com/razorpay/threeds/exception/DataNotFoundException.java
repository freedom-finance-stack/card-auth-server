package com.razorpay.threeds.exception;

import org.springframework.http.HttpStatus;

import com.razorpay.acs.contract.ThreeDSErrorResponse;
import com.razorpay.acs.contract.ThreeDSecureErrorCode;

// Checked Exception
public class DataNotFoundException extends ThreeDSException {

    public DataNotFoundException(
            ThreeDSecureErrorCode errorCode, InternalErrorCode internalErrorCode) {
        super(errorCode, internalErrorCode, internalErrorCode.getCode());
    }

    public DataNotFoundException(
            ThreeDSecureErrorCode errorCode, InternalErrorCode internalErrorCode, Throwable cause) {
        super(errorCode, internalErrorCode, internalErrorCode.getCode(), cause);
    }

    @Override
    public ThreeDSErrorResponse getErrorResponse() {
        return new ThreeDSErrorResponse(
                HttpStatus.OK.value(),
                super.getThreeDSecureErrorCode().getErrorCode(),
                super.getMessage(),
                super.getThreeDSecureErrorCode().getErrorComponent(),
                super.getThreeDSecureErrorCode().getErrorDescription());
    }
}
