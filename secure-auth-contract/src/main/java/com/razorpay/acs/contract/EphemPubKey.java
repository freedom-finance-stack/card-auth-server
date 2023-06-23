package com.razorpay.acs.contract;

import lombok.Data;

@Data
public class EphemPubKey {

  private String alg;
  private String kid;
  private String use;
  private String kty;
  private String crv;
  private String x;
  private String y;

  //	@Override
  //	public String toString() {
  //		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
  //		String strPubKey = gson.toJson(this);
  //		return strPubKey;
  //	}

  public String toJSONString() {
    return toString();
  }

  public boolean isMandatoryValueAvailable() {
    if (this.kty == null) {
      return false;
    }

    if (this.crv == null) {
      return false;
    }

    if (this.x == null) {
      return false;
    }

    if (this.y == null) {
      return false;
    }

    return true;
  }
}
