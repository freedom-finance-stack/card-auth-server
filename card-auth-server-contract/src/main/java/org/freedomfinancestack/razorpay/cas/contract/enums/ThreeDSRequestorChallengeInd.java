package org.freedomfinancestack.razorpay.cas.contract.enums;

import lombok.Getter;

public enum ThreeDSRequestorChallengeInd {
    NO_PREFERENCE("01", "No preference"),
    NO_CHALLENGE_REQUESTED("02", "No challenge requested"),
    CHALLENGE_REQUESTED_REQUESTER_PREFERENCE(
            "03", "Challenge requested (3DS Requestor preference)"),
    CHALLENGE_REQUESTED_MANDATE("04", "Challenge requested (Mandate)"),
    TRANSACTIONAL_RISK_ANALYSIS_IS_ALREADY_PERFORMED(
            "05", "No challenge requested (transactional risk analysis is already performed)"),
    DATA_SHARE_ONLY("06", "No challenge requested (Data share only)"),
    STRONG_CONSUMER_AUTHENTICATION_IS_ALREADY_PERFORMED(
            "07", "No challenge requested (strong consumer authentication is already performed)"),
    UTILISE_WHITELIST_EXEMPTION_IF_NO_CHALLENGE_REQUIRED(
            "08", "No challenge requested (utilise whitelist exemption if no challenge required)"),
    WHITELIST_PROMPT_REQUESTED_IF_CHALLENGE_REQUIRED(
            "09", "Challenge requested (whitelist prompt requested if challenge required)");

    @Getter private final String value;

    @Getter private final String description;

    ThreeDSRequestorChallengeInd(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ThreeDSRequestorChallengeInd getByValue(String value) {
        for (ThreeDSRequestorChallengeInd indicator : ThreeDSRequestorChallengeInd.values()) {
            if (indicator.getValue().equals(value)) {
                return indicator;
            }
        }
        return null;
    }
}
