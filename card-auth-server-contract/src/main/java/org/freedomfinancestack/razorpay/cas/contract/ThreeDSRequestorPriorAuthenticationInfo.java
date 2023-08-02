package org.freedomfinancestack.razorpay.cas.contract;

import org.freedomfinancestack.razorpay.cas.contract.constants.EMVCOConstant;

import lombok.Data;

@Data
public class ThreeDSRequestorPriorAuthenticationInfo implements Validatable {

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
