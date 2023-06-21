package com.razorpay.acs.dao.enums;

public enum TransactionStatusReason {
  CARD_AUTHENTICATION_FAILED("01", "Card authentication failed"),
  UNKNOWN_DEVICE("02", "Unknown Device"),
  UNSUPPORTED_DEVICE("03", "Unsupported Device"),
  EXCEED_AUTHENTICATION_FREQUENCY_LIMIT("04", "Exceeds authentication frequency limit"),
  EXPIRED_CARD("05", "Expired card"),
  INVALID_CARD_NUMBER("06", "Invalid Card Number"),
  INVALID_TRANSACTION("07", "Invalid Transaction"),
  NO_CARD_RECORD("08", "No Card Record"),
  SECURITY_FAILURE("09", "Security failure"),
  STOLEN_CARD("10", "Stolen card"),
  SUSPECTED_FRAUD("11", "Suspected fraud"),
  TRANSACTION_NOT_PERMITTED("12", "Transaction not permitted to cardholder"),
  CARDHOLDER_NOT_ENROLLED_IN_SERVICE("13", "Cardholder not enrolled in service"),
  TRANSACTION_TIMEOUT("14", "Transaction timed out at the ACS"),
  LOW_CONFIDENCE("15", "Low confidence"),
  MEDIUM_CONFIDENCE("16", "Medium confidence"),
  HIGH_CONFIDENCE("17", "High confidence"),
  VERY_HIGH_CONFIDENCE("18", "Very High confidence"),
  EXCEED_MAX_CHALLANGES("19", "Exceeds ACS maximum challenges"),
  NON_PAYMENT_TRANSACTION_NOT_SUPPORTED("20", "Non-Payment transaction not supported"),
  TRI_TRANSACTION_NOT_SUPPORTED("21", "3RI transaction not supported"),
  // This is other than EMVCo
  USER_CANCELLED("22", "Cancelled by User"),
  ACS_TECHNICAL_ISSUE("22", " ACS technical Issue"),
  AUTHENTICATION_ATTEMPTED_BUT_NOT_PERFORMED_BY_CARDHOLDER(
      "26", "Authentication attempted but not performed by the cardholder");

  private String code;
  private String desc;

  private TransactionStatusReason(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  public static TransactionStatusReason getTransactionStatusReason(String type) {
    for (TransactionStatusReason statusReason : TransactionStatusReason.values()) {
      if (statusReason.getCode().equals(type)) {
        return statusReason;
      }
    }
    return null;
  }
}
