package com.razorpay.threeds.hsm.luna.domain;

import java.io.Serializable;

public class HSMTransaction implements Serializable, Cloneable {

  /** */
  private static final long serialVersionUID = 1L;

  private String data;

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "Data: " + data;
  }
}
