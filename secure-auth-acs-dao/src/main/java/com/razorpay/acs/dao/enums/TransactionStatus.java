package com.razorpay.acs.dao.enums;

public enum TransactionStatus {
  CREATED("Y", "Created"), // todo check created should ne mapped to Y/N/U
  SUCCESS("Y", "Authentication Successful"),
  FAILED("N", "Not Authenticated"),
  UNABLE_TO_AUTHENTICATE("U", "Unable to Authentication"),
  ATTEMPT("A", "Attempt"),
  CHALLENGE_REQUIRED("C", "Challenge Required"),
  CHALLENGE_REQUIRED_DECOUPLED("D", "Challenge Required; Decoupled Authentication confirmed"),
  REJECTED("R", "Authentication Rejected"),
  INFORMATIONAL("I", "Informational Only");

  private String status;
  private String description;

  private TransactionStatus(String status, String description) {
    this.status = status;
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public String getStatus() {
    return status;
  }
}
