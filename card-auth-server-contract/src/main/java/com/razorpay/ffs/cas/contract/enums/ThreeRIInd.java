package com.razorpay.ffs.cas.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThreeRIInd {
    RECURRING_TRANSACTION("01", "Recurring transaction"),
    INSTALMENT_TRANSACTION("02", "Instalment transaction"),
    ADD_CARD("03", "Add card"),
    MAINTAIN_CARD_INFORMATION("04", "Maintain card information"),
    ACCOUNT_VERIFICATION("05", "Account verification"),
    SPLIT_DELAYED_SHIPMENT("06", "Split/delayed shipment"),
    TOP_UP("07", "Top-up"),
    MAIL_ORDER("08", "Mail Order"),
    TELEPHONE_ORDER("09", "Telephone Order"),
    WHITELIST_STATUS_CHECK("10", "Whitelist status check"),
    OTHER_PAYMENT("11", "Other payment");

    private final String value;
    private final String desc;

}
