package org.freedomfinancestack.razorpay.cas.dao.enums;

import lombok.Getter;

@Getter
public enum OOBType {
    UL(1);

    private final int value;

    OOBType(int value) {
        this.value = value;
    }

    public static OOBType getOOBType(Integer value) {
        for (OOBType oobType : OOBType.values()) {
            if (oobType.getValue() == value) {
                return oobType;
            }
        }
        return null;
    }
}
