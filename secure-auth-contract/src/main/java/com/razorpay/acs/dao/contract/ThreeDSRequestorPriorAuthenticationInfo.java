/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2017  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Author 				: Ashish Kirpan
 * Created Date 			: Nov 28, 2017
 ******************************************************************************/

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
