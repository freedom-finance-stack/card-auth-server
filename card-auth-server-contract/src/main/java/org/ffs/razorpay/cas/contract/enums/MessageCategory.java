package org.ffs.razorpay.cas.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCategory {
    PA("01", "PAYMENT_AUTHENTICATION"),
    NPA("02", "NON_PAYMENT_AUTHENTICATION"),

    // New Message Category for Mastercard
    PVPA("85", "PV_PAYMENT_AUTHENTICATION"), // PRODUCTION_VALIDATION_PAYMENT_AUTHENTICATION
    PVNPA(
            "86",
            "PV_NON_PAYMENT_AUTHENTICATION"), // PRODUCTION_VALIDATION_NON_PAYMENT_AUTHENTICATION

    // Added for Rupay
    NW("NW", "Transaction performed at domestic merchant"),
    TW("TW", "Transaction performed at domestic merchant along with Token provisioning"),
    IT("IT", "Transaction performed at International merchant"),
    AT("AT", "Authentication Transaction Only"),
    AW("AW", "Authentication transaction for Provisioning"),
    DI("DI", "Domestic InApp transaction"),
    II("II", "International InApp transaction");

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
