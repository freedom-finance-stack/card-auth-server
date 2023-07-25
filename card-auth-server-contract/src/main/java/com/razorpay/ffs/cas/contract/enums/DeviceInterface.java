package com.razorpay.ffs.cas.contract.enums;

public enum DeviceInterface {
    NATIVE("01"),
    HTML("02"),
    BOTH("03"); // Removed from new specification

    private String value;

    private DeviceInterface(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DeviceInterface getDeviceInterface(String value) {
        for (DeviceInterface deviceInterface : DeviceInterface.values()) {
            if (deviceInterface.getValue().equals(value)) {
                return deviceInterface;
            }
        }
        return null;
    }
}
