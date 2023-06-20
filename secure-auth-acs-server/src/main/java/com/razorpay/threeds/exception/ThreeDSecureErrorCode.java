package com.razorpay.threeds.exception;

public enum ThreeDSecureErrorCode {
  // todo check error handling in 3ds doc page Page 314
  MESSAGE_RECEIVED_INVALID("101", "A", "Invalid Message Type"),
  MESSAGE_VERSION_NUMBER_NOT_SUPPORTED("102", "A", "Message version not supported"),
  SENT_MESSAGES_LIMIT_EXCEEDED(
      "103", "A", "Exceeded maximum number of PReq messages sent to the DS."),
  REQUIRED_DATA_ELEMENT_MISSING(
      "201",
      "A",
      "A message element required as defined in Table A.1 is missing from the message."),
  CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED("202", "A", "Message extension not recognised"),

  INVALID_FORMAT("203", "A", "Format of one or more elements is invalid"),
  INVALID_FORMAT_LENGTH("203", "A", "Invalid Format - Length"),
  INVALID_FORMAT_VALUE("203", "A", "Invalid Format - Value"),

  DUPLICATE_DATA_ELEMENT("204", "A", "Duplicate Data Element"),
  TRANSACTION_ID_NOT_RECOGNISED("301", "A", "Transaction not Recognised"),
  DATA_DECRYPTION_FAILURE("302", "A", "Data could not be decrypted"),
  INVALID_ENDPOINT("303", "A", "Access denied, invalid endpoint"),
  ISO_CODE_INVALID("304", "A", "ISO Code invalid"),
  TRANSACTION_DATA_NOT_VALID("305", "A", "Transaction data not valid"),
  MCC_NOT_VALID_FOR_PAYMENT_SYSTEM("306", "A", "MCC not Valid for Payment System"),
  SERIAL_NUMBER_NOT_VALID("307", "A", "Serial Number not valid"),
  TRANSACTION_TIMED_OUT("402", "A", "Transaction timed out"),
  TRANSACTION_TIMEOUT("4010", "TRANSACTION TIMEOUT"),
  TRANSIENT_SYSTEM_FAILURE("403", "A", "Transient system failure"),
  PERMANENT_SYSTEM_FAILURE("404", "A", "Permanent system failure"),
  SYSTEM_CONNECTION_FAILURE("405", "A", "System Connection failure"),
  ACS_TECHNICAL_ERROR("406", "A", "ACS Technical failure"),
  ACS_REFRESH_FAILURE("1000", "A", "Refresh Failure"),

  SESSION_EXPIRED("ACCU700", "A", "Session - Session is Expired"),
  MESSAGE_HASH_NOT_MATCHED("ACCU600", "A", "AccuRequestId - hash messages do not match");

  /*REQUIRED_ELEMENT_MISSING("03", "Required element missing"),
  DATA_ELEMENT_NOT_RECOGNISED("04","Data element not recognised"),
  INVALID_FORMAT("05","Format of one or more elements is invalid"),
  UNSUPPORTED_MESSAGE_VERSION("06","Message version not supported"),
  DUPLICATE_DATA_ELEMENT("07","Duplicate Data Element"),
  TRANSACTION_TIMEOUT("08","Transaction timed out"),
  INVALID_ENDPOINT("50","Access denied, invalid endpoint"),
  TRANSIENT_SYSTEM_FAILURE("98", "Transient system failure"),
  PERMANENT_SYSTEM_FAILURE("99","Permanent system failure")*/ ;

  private String errorCode;
  private String errorComponent;
  private String errorDescription;

  private ThreeDSecureErrorCode(String errorCode, String errorDescription) {
    this.errorCode = errorCode;
    this.errorDescription = errorDescription;
  }

  private ThreeDSecureErrorCode(String errorCode, String errorComponent, String errorDescription) {
    this.errorCode = errorCode;
    this.errorComponent = errorComponent;
    this.errorDescription = errorDescription;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public String getErrorComponent() {
    return errorComponent;
  }
}
