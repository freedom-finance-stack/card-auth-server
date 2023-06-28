package com.razorpay.threeds.exception.checked;

import com.razorpay.threeds.exception.InternalErrorCode;

// Checked Exception
public class ACSException extends Exception {

  /** */
  private static final long serialVersionUID = -3009383961775806469L;

  private InternalErrorCode internalErrorCode;

  public ACSException(InternalErrorCode internalErrorCode, String message, Throwable cause) {
    super(message, cause);
    this.internalErrorCode = internalErrorCode;
  }

  public ACSException(InternalErrorCode internalErrorCode, String message) {
    super(message);
    this.internalErrorCode = internalErrorCode;
  }

  public ACSException(InternalErrorCode internalErrorCode, Throwable cause) {
    super(internalErrorCode.getDefaultErrorMessage(), cause);
    this.internalErrorCode = internalErrorCode;
  }

  public ACSException(InternalErrorCode internalErrorCode) {
    super(internalErrorCode.getDefaultErrorMessage());
    this.internalErrorCode = internalErrorCode;
  }

  public InternalErrorCode getErrorCode() {
    return internalErrorCode;
  }
}
