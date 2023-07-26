package org.ffs.razorpay.cas.acs.exception;

import org.ffs.razorpay.cas.contract.ThreeDSErrorResponse;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.springframework.http.HttpStatus;

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
