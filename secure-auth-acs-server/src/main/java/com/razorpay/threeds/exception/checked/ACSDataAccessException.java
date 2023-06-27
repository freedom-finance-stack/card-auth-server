package com.razorpay.threeds.exception.checked;

import com.razorpay.threeds.exception.ErrorCode;

// Checked Exception
public class ACSDataAccessException extends ACSException {

  /** */
  private static final long serialVersionUID = 1L;

  public ACSDataAccessException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public ACSDataAccessException(ErrorCode errorCode, Throwable cause) {
    super(errorCode, cause);
  }

  public ACSDataAccessException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ACSDataAccessException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
