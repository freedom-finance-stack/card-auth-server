package com.razorpay.acs.dao.contract;

import com.razorpay.acs.dao.contract.constants.EMVCOConstant;

import lombok.Data;

@Data
public class ThreeDSRequestorPriorAuthenticationInfo {

  private String threeDSReqPriorAuthData;
  private String threeDSReqPriorAuthMethod;
  private String threeDSReqPriorAuthTimestamp;
  private String threeDSReqPriorRef;

  public boolean isMandatoryValueAvailable() {
    if (this.threeDSReqPriorAuthMethod == null) {
      return false;
    }

    if (this.threeDSReqPriorAuthData == null) {
      return false;
    }

    return true;
  }

  public boolean isValid() {
    if (!EMVCOConstant.threeDSReqPriorAuthMethodList.contains(this.threeDSReqPriorAuthMethod)) {
      return false;
    }
    return true;
  }
}
