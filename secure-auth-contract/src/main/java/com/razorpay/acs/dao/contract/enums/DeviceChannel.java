/*******************************************************************************
 * Copyright (C)  IZealiant Technologies 2016  - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 ******************************************************************************/
package com.razorpay.acs.dao.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceChannel {
  APP("01", "App-based"),
  BRW("02", "Browser"),
  TRI("03", "3DS Requestor Initiated (3RI)");

  private String channel;
  private String desc;

  public static DeviceChannel getDeviceChannel(String channel) {
    for (DeviceChannel deviceCategory : DeviceChannel.values()) {
      if (deviceCategory.getChannel().equals(channel)) {
        return deviceCategory;
      }
    }
    return null;
  }

  public static boolean contains(DeviceChannel[] channels, String channel) {
    for (DeviceChannel deviceChannel : channels) {
      if (deviceChannel.getChannel().equals(channel)) {
        return true;
      }
    }
    return false;
  }
}
