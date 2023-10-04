package org.freedomfinancestack.razorpay.cas.dao.enums;

import lombok.Getter;

@Getter
public enum Network {
    VISA(1, "VISA"),
    MASTERCARD(2, "MASTERCARD"),
    AMEX(3, "AMEX"),
    DISCOVER(4, "DISCOVER"),
    RUPAY(5, "RUPAY");

    private final byte value;
    private final String name;

    Network(int value, String name) {
        this.value = (byte) value;
        this.name = name;
    }

    public static Network getNetwork(byte id) {
        for (Network value : Network.values()) {
            if (value.getValue() == id) {
                return value;
            }
        }
        return null;
    }
}
