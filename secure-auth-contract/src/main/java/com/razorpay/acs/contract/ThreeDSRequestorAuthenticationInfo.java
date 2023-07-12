package com.razorpay.acs.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.razorpay.acs.contract.constants.EMVCOConstant;

import lombok.Data;

@Data
public class ThreeDSRequestorAuthenticationInfo implements Validatable {

    @JsonProperty("threeDSReqAuthMethod")
    private String threeDSReqAuthMethod;

    @JsonProperty("threeDSReqAuthTimestamp")
    private String threeDSReqAuthTimestamp;

    @JsonProperty("threeDSReqAuthData")
    private String threeDSReqAuthData;

    public boolean isValid() {
        if (this.threeDSReqAuthMethod == null) {
            return false;
        } else if (this.threeDSReqAuthMethod.length() > 2) {
            return false;
        } else if (!EMVCOConstant.threeDSReqAuthMethodList.contains(this.threeDSReqAuthMethod)) {
            return false;
        }
        return true;
    }
}
