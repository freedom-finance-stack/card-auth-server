package com.razorpay.acs.dao.enums;

public enum CardDetailsStore {
  ACS(CardStoreTypeConstants.ACS),
  EXTERNAL_API(CardStoreTypeConstants.EXTERNAL_API);

  CardDetailsStore(String name) {
    this.name = name;
  }

  private final String name;

  @Override
  public String toString() {
    return this.name;
  }

  public interface CardStoreTypeConstants {
    String ACS = "ACS";
    String EXTERNAL_API = "EXTERNAL_API";
  }
}
