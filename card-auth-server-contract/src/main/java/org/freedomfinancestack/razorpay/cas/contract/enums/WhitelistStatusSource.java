package org.freedomfinancestack.razorpay.cas.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WhitelistStatusSource {
    THREEDS_SERVER("01"),
    DS("02"),
    ACS("03");

    private final String value;

    public static WhitelistStatusSource getWhitelistStatusSource(String value) {
        for (WhitelistStatusSource whitelistStatusSource : WhitelistStatusSource.values()) {
            if (whitelistStatusSource.getValue().equals(value)) {
                return whitelistStatusSource;
            }
        }
        return null;
    }
}
