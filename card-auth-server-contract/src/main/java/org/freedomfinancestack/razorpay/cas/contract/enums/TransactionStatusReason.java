package org.freedomfinancestack.razorpay.cas.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStatusReason {
    CARD_AUTHENTICATION_FAILED("01", "Card authentication failed"),
    UNKNOWN_DEVICE("02", "Unknown Device"),
    UNSUPPORTED_DEVICE("03", "Unsupported Device"),
    EXCEED_AUTHENTICATION_FREQUENCY_LIMIT("04", "Exceeds authentication frequency limit"),
    EXPIRED_CARD("05", "Expired card"),
    INVALID_CARD_NUMBER("06", "Invalid Card Number"),
    INVALID_TRANSACTION("07", "Invalid Transaction"),
    NO_CARD_RECORD("08", "Data Not Found"),
    SECURITY_FAILURE("09", "Security failure"),
    STOLEN_CARD("10", "Stolen card"),
    SUSPECTED_FRAUD("11", "Suspected fraud"),
    TRANSACTION_NOT_PERMITTED("12", "Transaction not permitted to cardholder"),
    CARDHOLDER_NOT_ENROLLED_IN_SERVICE("13", "Cardholder not enrolled in service"),
    TRANSACTION_TIMEOUT("14", "Transaction timed out at the ACS"),
    LOW_CONFIDENCE("15", "Low confidence"),
    MEDIUM_CONFIDENCE("16", "Medium confidence"),
    HIGH_CONFIDENCE("17", "High confidence"),
    VERY_HIGH_CONFIDENCE("18", "Very High confidence"),
    EXCEED_MAX_CHALLENGES("19", "Exceeds ACS maximum challenges"),
    NON_PAYMENT_TRANSACTION_NOT_SUPPORTED("20", "Non-Payment transaction not supported"),
    TRI_TRANSACTION_NOT_SUPPORTED("21", "3RI transaction not supported"),
    ACS_TECHNICAL_ISSUE("22", " ACS technical Issue"),
    DECOUPLED_AUTH_REQUIRED_BY_ACS_NOT_BY_REQUESTOR(
            "23", "Decoupled Authentication required by ACS but not requested by 3DS Requestor"),
    DECOUPLED_EXPIRY_TIME_EXCEEDED("24", "3DS Requestor Decoupled Max Expiry Time exceeded"),
    INSUFFICIENT_TIME_FOR_DECOUPLED_AUTH(
            "25",
            "Decoupled Authentication was provided insufficient time to authenticate cardholder."
                    + " ACS will not make attempt"),
    AUTH_ATTEMPTED_BUT_NOT_PERFORMED(
            "26", "Authentication attempted but not performed by the cardholder");

    private final String code;
    private final String desc;

    public static TransactionStatusReason getTransactionStatusReason(String type) {
        for (TransactionStatusReason statusReason : TransactionStatusReason.values()) {
            if (statusReason.getCode().equals(type)) {
                return statusReason;
            }
        }
        return null;
    }
}
