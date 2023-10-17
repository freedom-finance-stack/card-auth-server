package org.freedomfinancestack.razorpay.cas.contract;

import org.freedomfinancestack.extensions.validation.validator.Validatable;
import org.freedomfinancestack.razorpay.cas.contract.enums.DeviceInterface;
import org.freedomfinancestack.razorpay.cas.contract.enums.UIType;

import lombok.Data;

@Data
public class DeviceRenderOptions implements Validatable {

    private String sdkInterface;
    private String[] sdkUiType;

    public boolean isMandatoryValueAvailable() {
        if (this.sdkInterface == null) {
            return false;
        }

        if (this.sdkUiType == null) {
            return false;
        }

        return this.sdkUiType.length != 0;
    }

    public boolean isValid() {
        if (!this.isMandatoryValueAvailable()) {
            return false;
        }
        if (this.sdkInterface != null) {
            DeviceInterface deviceIntr = DeviceInterface.getDeviceInterface(sdkInterface);
            if (deviceIntr == null) {
                return false;
            }
        }

        if (this.sdkUiType != null) {
            for (String strUiType : sdkUiType) {
                UIType uiType = UIType.getUIType(strUiType);
                if (uiType == null) {
                    return false;
                }
            }
        }

        return true;
    }
}
