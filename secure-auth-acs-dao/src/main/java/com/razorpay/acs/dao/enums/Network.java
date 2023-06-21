package com.razorpay.acs.dao.enums;

public enum Network {
  VISA("01", "VISA"),
  MASTERCARD("02", "MASTERCARD"),
  AMEX("03", "AMEX"),
  DISCOVER("04", "DISCOVER"),
  RUPAY("05", "RUPAY");

  private String value;
  private String description;

  private Network(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }

  public static Network getNetwork(String id) {
    for (Network value : Network.values()) {
      if (value.getValue().equals(id)) {
        return value;
      }
    }
    return null;
  }
}
