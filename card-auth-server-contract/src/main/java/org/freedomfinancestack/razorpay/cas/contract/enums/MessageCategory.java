package org.freedomfinancestack.razorpay.cas.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCategory {
    PA("01", "PAYMENT_AUTHENTICATION"),
    NPA("02", "NON_PAYMENT_AUTHENTICATION"),

    // todo implement New Message Category for Mastercard
    PVPA("85", "PV_PAYMENT_AUTHENTICATION"), // PRODUCTION_VALIDATION_PAYMENT_AUTHENTICATION
    PVNPA(
            "86",
            "PV_NON_PAYMENT_AUTHENTICATION"); // PRODUCTION_VALIDATION_NON_PAYMENT_AUTHENTICATION

    private final String category;
    private final String desc;

    public static String[] getCategoryValues() {
        String[] categoryValues = new String[MessageCategory.values().length];
        int i = 0;
        for (MessageCategory messageCategory : MessageCategory.values()) {
            categoryValues[i] = messageCategory.getCategory();
            i++;
        }
        return categoryValues;
    }

    public static MessageCategory getMessageCategory(String category) {
        for (MessageCategory messageCategory : MessageCategory.values()) {
            if (messageCategory.getCategory().equals(category)) {
                return messageCategory;
            }
        }
        return null;
    }

    public static boolean contains(MessageCategory[] categories, String category) {
        for (MessageCategory messageCategory : categories) {
            if (messageCategory.getCategory().equals(category)) {
                return true;
            }
        }
        return false;
    }
}
