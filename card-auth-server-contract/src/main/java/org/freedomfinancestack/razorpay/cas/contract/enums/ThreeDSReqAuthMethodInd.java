package org.freedomfinancestack.razorpay.cas.contract.enums;

import lombok.Getter;

public enum ThreeDSReqAuthMethodInd {
    VERIFIED("01", "Verified"),
    FAILED("02", "Failed"),
    NOT_PERFORMED("03", "Not Performed"),
    ;

    @Getter private final String value;
    @Getter private final String description;

    ThreeDSReqAuthMethodInd(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ThreeDSReqAuthMethodInd getByValue(String value) {
        for (ThreeDSReqAuthMethodInd indicator : ThreeDSReqAuthMethodInd.values()) {
            if (indicator.getValue().equals(value)) {
                return indicator;
            }
        }
        return null;
    }
}
