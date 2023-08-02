package org.freedomfinancestack.razorpay.cas.contract;

import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        } else {
            return EMVCOConstant.threeDSReqAuthMethodList.contains(this.threeDSReqAuthMethod);
        }
    }
}
