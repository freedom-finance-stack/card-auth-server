package com.razorpay.acs.dao.contract;

import com.razorpay.acs.dao.contract.enums.DeviceInterface;
import com.razorpay.acs.dao.contract.enums.UIType;

import lombok.Data;

@Data
public class DeviceRenderOptions {

  private String sdkInterface;
  private String[] sdkUiType;

  public boolean isMandatoryValueAvailable() {
    if (this.sdkInterface == null) {
      return false;
    }

    if (this.sdkUiType == null) {
      return false;
    }

    if (this.sdkUiType != null && this.sdkUiType.length == 0) {
      return false;
    }

    return true;
  }

  public boolean isValid() {
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
