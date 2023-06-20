/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2016  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 ******************************************************************************/
package com.razorpay.threeds.exception;

import org.springframework.http.HttpStatus;

// Checked Exception
public class DataNotFoundException extends ThreeDSException {

  public DataNotFoundException(ThreeDSecureErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  protected DataNotFoundException(
      ThreeDSecureErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public DataNotFoundException(ThreeDSecureErrorCode errorCode) {
    super(errorCode);
  }

  @Override
  public ThreeDSErrorResponse getErrorResponse() {
    return new ThreeDSErrorResponse(
        HttpStatus.OK,
        super.getErrorCode().getErrorCode(),
        super.getMessage(),
        super.getErrorCode().getErrorComponent(),
        super.getErrorCode().getErrorDescription());
  }
}
