package com.razorpay.threeds.exception.checked;


import lombok.Getter;

@Getter
public enum ErrorCode {
    //todo re define error code
    DUPLICATE_TRANSACTION_REQUEST("4025", "DUPLICATE TRANSACTION REQUEST"),
    CARD_RANGE_NOT_FOUND("2009", "CARD RANGE NOT FOUND"),
    INSTITUTION_NOT_FOUND("3005", "INSTITUTION NOT FOUND"),
    INSTITUTION_INACTIVE("3006", "INSTITUTION INACTIVE"),
    CARD_RANGE_NOT_ACTIVE("2008", "CARD RANGE NOT ACTIVE"),
    RANGE_GROUP_NOT_FOUND("2010", "RANGE GROUP NOT FOUND");

    private final String code;

    private final String defaultErrorMessage;

    ErrorCode(final String errorCode,
              final String defaultErrorMessage) {

        this.code = errorCode;
        this.defaultErrorMessage = defaultErrorMessage;
    }

}