package org.ffs.razorpay.cas.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UIType {
    TEXT("01"),
    SINGLE_SELECT("02"),
    MULTI_SELECT("03"),
    OOB("04"),
    HTML_OTHER("05");

    private final String type;

    public static UIType getUIType(String type) {
        for (UIType uiType : UIType.values()) {
            if (uiType.getType().equals(type)) {
                return uiType;
            }
        }
        return null;
    }
}
