package com.razorpay.threeds.exception.checked;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // todo re define error code
  DUPLICATE_TRANSACTION_REQUEST("4025", "DUPLICATE TRANSACTION REQUEST"),
  CARD_RANGE_NOT_FOUND("2009", "CARD RANGE NOT FOUND"),
  INSTITUTION_NOT_FOUND("3005", "INSTITUTION NOT FOUND"),
  INSTITUTION_INACTIVE("3006", "INSTITUTION INACTIVE"),
  CARD_RANGE_NOT_ACTIVE("2008", "CARD RANGE NOT ACTIVE"),
  RANGE_GROUP_NOT_FOUND("2010", "RANGE GROUP NOT FOUND"),
  AUTH_VALUE_GENERATOR_NOT_FOUND("2011", "AUTH VALUE GENERATOR NOT FOUND"),

  /** LUNA HSM Related Error Codes */
  HSM_CONNECTOR_REQUEST_TIMEOUT("7001", "HSM_CONNECTOR_REQUEST_TIMEOUT"),
  HSM_CONNECTOR_CONNECTION_CLOSE("7002", "HSM_CONNECTOR_CONNECTION_CLOSE"),
  HSM_CONNECTOR_GATEWAY_NOT_AVL("7003", "HSM_CONNECTOR_GATEWAY_NOT_AVL");

  private final String code;

  private final String defaultErrorMessage;

  ErrorCode(final String errorCode, final String defaultErrorMessage) {

    this.code = errorCode;
    this.defaultErrorMessage = defaultErrorMessage;
  }
}
