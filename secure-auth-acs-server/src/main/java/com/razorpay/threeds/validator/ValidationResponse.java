package com.razorpay.threeds.validator;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.validator.enums.ThreeDSDataElement;

import lombok.Data;

@Data
public class ValidationResponse {

  private boolean invalid = false;
  private String value;

  private ThreeDSDataElement dataElement;
  private ThreeDSecureErrorCode threeDSecureError;

  public ValidationResponse() {}

  public ValidationResponse(boolean invalid) {
    this.invalid = invalid;
  }

  public ValidationResponse(
      boolean invalid, ThreeDSecureErrorCode threeDSecureError, ThreeDSDataElement dataElement) {
    this.threeDSecureError = threeDSecureError;
    this.dataElement = dataElement;
    this.invalid = invalid;
  }

  public ValidationResponse(
      boolean invalid,
      ThreeDSecureErrorCode threeDSecureError,
      ThreeDSDataElement dataElement,
      String value) {
    this.threeDSecureError = threeDSecureError;
    this.dataElement = dataElement;
    this.invalid = invalid;
    this.value = value;
  }
}
