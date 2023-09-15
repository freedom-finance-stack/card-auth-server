package org.freedomfinancestack.razorpay.cas.contract;

import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;

import lombok.Data;

@Data
public class ThreeDSRequestorAuthenticationInfo implements Validatable {

    private String threeDSReqAuthMethod;

    private String threeDSReqAuthTimestamp;

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
