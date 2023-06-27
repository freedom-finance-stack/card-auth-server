package com.razorpay.threeds.exception.checked;

import com.razorpay.threeds.exception.ErrorCode;

// Checked Exception
public class ACSException extends Exception {

  /** */
  private static final long serialVersionUID = -3009383961775806469L;

  private ErrorCode errorCode;

  public ACSException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public ACSException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public ACSException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getDefaultErrorMessage(), cause);
    this.errorCode = errorCode;
  }

  public ACSException(ErrorCode errorCode) {
    super(errorCode.getDefaultErrorMessage());
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
