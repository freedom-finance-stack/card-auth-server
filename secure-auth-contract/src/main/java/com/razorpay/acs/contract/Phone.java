package com.razorpay.acs.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Phone {

  @JsonProperty("cc")
  private String cc;

  @JsonProperty("subscriber")
  private String subscriber;

  public boolean isValid() {

    if (this.cc != null) {
      if (this.cc.length() < 1 && this.cc.length() > 3) {
        return false;
      }
    }

    if (this.subscriber != null) {
      if (this.subscriber.length() > 15) {
        return false;
      }
    }

    return true;
  }
}
