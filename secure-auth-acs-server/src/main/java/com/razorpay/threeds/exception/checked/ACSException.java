/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2016  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 ******************************************************************************/
package com.razorpay.threeds.exception.checked;

// Checked Exception
public class ACSException extends Exception {

  /** */
  private static final long serialVersionUID = -3009383961775806469L;

  private String errorCode;
  private String statusReason;

  public ACSException(
      String errorCode,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.errorCode = errorCode;
  }

  public ACSException(String errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public ACSException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public ACSException(String errorCode, String statusReason, String message) {
    super(message);
    this.errorCode = errorCode;
    this.statusReason = statusReason;
  }

  public ACSException(String errorCode, Throwable cause) {
    super(cause);
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getStatusReason() {
    return statusReason;
  }
}
