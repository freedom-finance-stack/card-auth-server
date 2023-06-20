/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2016  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 ******************************************************************************/
package com.razorpay.threeds.exception.checked;

// Checked Exception
public class ACSDataAccessException extends ACSException {

  /** */
  private static final long serialVersionUID = 1L;

  public ACSDataAccessException(
      String errorCode,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(errorCode, message, cause, enableSuppression, writableStackTrace);
  }

  public ACSDataAccessException(String errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public ACSDataAccessException(String errorCode, String message) {
    super(errorCode, message);
  }

  public ACSDataAccessException(String errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
