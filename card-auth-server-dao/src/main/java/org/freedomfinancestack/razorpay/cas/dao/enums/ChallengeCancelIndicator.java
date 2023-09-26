package org.freedomfinancestack.razorpay.cas.dao.enums;

import lombok.Getter;

@Getter
public enum ChallengeCancelIndicator {
    CARDHOLDER_SELECTED_CANCEL("01", "Cardholder selected Cancel"),
    REQUESTER_CANCELLED_AUTHENTICATION("02", "3DS Requester cancelled Authentication"),
    TRANSACTION_ABANDONED("03", "Transaction Abandoned"),
    TRANSACTION_TIMED_OUT("04", "Transaction Timed Out at ACS other timeouts"),
    TRANSACTION_TIMED_OUT_AT_ACS_FIRST_CREQ_NOT_RECEIVED_BY_ACS(
            "05", "Transaction Timed Out at ACS First CReq not received by ACS"),
    TRANSACTION_ERROR("06", "Transaction Error"),
    UNKNOWN("07", "Unknown"),
    TRANSACTION_TIMED_OUT_AT_SDK("08", "Transaction Timed Out at SDK");

    private final String indicator;
    private final String desc;

    ChallengeCancelIndicator(String indicator, String desc) {
        this.indicator = indicator;
        this.desc = desc;
    }

    public static ChallengeCancelIndicator get(String indicator) {
        for (ChallengeCancelIndicator challengeIndicator : ChallengeCancelIndicator.values()) {
            if (challengeIndicator.getIndicator().equals(indicator)) {
                return challengeIndicator;
            }
        }
        return null;
    }
}
