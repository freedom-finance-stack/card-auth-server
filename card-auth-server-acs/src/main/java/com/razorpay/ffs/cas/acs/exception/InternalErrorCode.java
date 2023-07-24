package com.razorpay.ffs.cas.acs.exception;

import com.razorpay.ffs.cas.contract.enums.TransactionStatusReason;
import com.razorpay.ffs.cas.dao.enums.TransactionStatus;

import lombok.Getter;

@Getter
public enum InternalErrorCode { // todo get this file reviewed by Ashish and Piyush

    // Error code for card USER
    CARD_USER_NOT_FOUND(
            "1001",
            "CARD RANGE NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.NO_CARD_RECORD),
    CARD_USER_BLOCKED(
            "1002",
            "CARD BLOCKED",
            TransactionStatus.FAILED,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),
    CARD_USER_FETCH_EXCEPTION(
            "1003",
            "Error while fetching card user details",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.NO_CARD_RECORD),

    // Error code for Institution
    INSTITUTION_NOT_FOUND(
            "3001",
            "INSTITUTION NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.NO_CARD_RECORD),
    INSTITUTION_INACTIVE(
            "3002",
            "INSTITUTION INACTIVE",
            TransactionStatus.FAILED,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),
    INSTITUTION_FETCH_EXCEPTION(
            "3003",
            "Error while fetching institution details",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    // Error code for Card Range
    CARD_RANGE_NOT_ACTIVE(
            "3006",
            "CARD RANGE NOT ACTIVE",
            TransactionStatus.REJECTED,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),
    CARD_RANGE_NOT_FOUND(
            "3007",
            "CARD RANGE NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.NO_CARD_RECORD),
    RANGE_GROUP_NOT_FOUND(
            "3008",
            "RANGE GROUP NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.NO_CARD_RECORD),
    CARD_RANGE_FETCH_EXCEPTION(
            "3009",
            "Error while fetching card range details",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    ACS_URL_NOT_FOUND(
            "3012",
            "ACS URL NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    INVALID_NETWORK(
            "3013",
            "INVALID NETWORK",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),

    // Error code for save Transaction
    TRANSACTION_SAVE_EXCEPTION(
            "2001",
            "Error while saving transaction",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    TRANSACTION_FIND_EXCEPTION(
            "2002",
            "Error while finding transaction",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    // Error code for invalid request
    INVALID_REQUEST(
            "4001",
            "INVALID REQUEST",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),

    INTERNAL_SERVER_ERROR(
            "501",
            "INTERNAL SERVER ERROR",
            TransactionStatus.FAILED,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    /** LUNA HSM Related Error Codes */
    HSM_CONNECTOR_REQUEST_TIMEOUT(
            "7001",
            "LUNA HSM CONNECTOR REQUEST TIMEOUT",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    HSM_CONNECTOR_CONNECTION_CLOSE(
            "7002",
            "LUNA HSM CONNECTOR CONNECTION IS CLOSED",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    HSM_INTERNAL_EXCEPTION(
            "7000",
            "Internal Error occurred in HSM",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE);

    private final String code;

    private final String defaultErrorMessage;

    private TransactionStatus transactionStatus;

    private TransactionStatusReason transactionStatusReason;

    InternalErrorCode(final String errorCode, final String defaultErrorMessage) {
        this.code = errorCode;
        this.defaultErrorMessage = defaultErrorMessage;
    }

    InternalErrorCode(
            final String errorCode,
            final String defaultErrorMessage,
            final TransactionStatus transactionStatus,
            final TransactionStatusReason transactionStatusReason) {
        this.code = errorCode;
        this.defaultErrorMessage = defaultErrorMessage;
        this.transactionStatus = transactionStatus;
        this.transactionStatusReason = transactionStatusReason;
    }
}
