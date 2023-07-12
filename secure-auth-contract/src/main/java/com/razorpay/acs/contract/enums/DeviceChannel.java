package com.razorpay.acs.contract.enums;

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

    public static String[] getChannelValues() {
        String[] channelValues = new String[DeviceChannel.values().length];
        int i = 0;
        for (DeviceChannel deviceChannel : DeviceChannel.values()) {
            channelValues[i] = deviceChannel.getChannel();
            i++;
        }
        return channelValues;
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
