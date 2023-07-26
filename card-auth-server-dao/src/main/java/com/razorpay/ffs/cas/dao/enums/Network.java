package com.razorpay.ffs.cas.dao.enums;

import lombok.Getter;

@Getter
public enum Network {
    VISA(1, "VISA"),
    MASTERCARD(2, "MASTERCARD"),
    AMEX(3, "AMEX"),
    DISCOVER(4, "DISCOVER"),
    RUPAY(5, "RUPAY");

    private final int value;
    private final String description;

    Network(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static Network getNetwork(int id) {
        for (Network value : Network.values()) {
            if (value.getValue() == id) {
                return value;
            }
        }
        return null;
    }
}
