package com.razorpay.threeds.exception;

import org.springframework.http.HttpStatus;

// Checked Exception
public class DataNotFoundException extends ThreeDSException {

  public DataNotFoundException(ThreeDSecureErrorCode errorCode, ErrorCode internalErrorCode) {
    super(errorCode, internalErrorCode, internalErrorCode.getCode());
  }

  public DataNotFoundException(
      ThreeDSecureErrorCode errorCode, ErrorCode internalErrorCode, Throwable cause) {
    super(errorCode, internalErrorCode, internalErrorCode.getCode(), cause);
  }

  @Override
  public ThreeDSErrorResponse getErrorResponse() {
    return new ThreeDSErrorResponse(
        HttpStatus.OK,
        super.getThreeDSecureErrorCode().getErrorCode(),
        super.getMessage(),
        super.getThreeDSecureErrorCode().getErrorComponent(),
        super.getThreeDSecureErrorCode().getErrorDescription());
  }
}
