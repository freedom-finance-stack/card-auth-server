package com.razorpay.threeds.exception.checked;


import lombok.Getter;

@Getter
public enum ErrorCode {

    DUPLICATE_TRANSACTION_REQUEST("4025", "DUPLICATE TRANSACTION REQUEST"),
    CARD_RANGE_NOT_FOUND("2009", "CARD RANGE NOT FOUND");
    private final String code;

    private final String defaultErrorMessage;

    ErrorCode(final String errorCode,
              final String defaultErrorMessage) {

        this.code = errorCode;
        this.defaultErrorMessage = defaultErrorMessage;
    }

}