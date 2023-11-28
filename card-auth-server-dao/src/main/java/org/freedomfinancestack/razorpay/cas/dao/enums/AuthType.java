package org.freedomfinancestack.razorpay.cas.dao.enums;

import lombok.Getter;

@Getter
public enum AuthType {
    OTP(2),
    PASSWORD(1),
    NetBankingOOB(3),
    Decoupled(4),
    UNKNOWN(5);

    private final int value;

    AuthType(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static AuthType getAuthType(Integer value) {
        for (AuthType authType : AuthType.values()) {
            if (authType.getValue().equals(value)) {
                return authType;
            }
        }
        return null;
    }
}
