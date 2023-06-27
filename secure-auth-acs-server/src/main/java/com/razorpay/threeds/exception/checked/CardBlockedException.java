package com.razorpay.threeds.exception.checked;

import com.razorpay.threeds.exception.ErrorCode;

public class CardBlockedException extends ACSException {

  /** */
  private static final long serialVersionUID = 1L;

  public CardBlockedException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public CardBlockedException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
