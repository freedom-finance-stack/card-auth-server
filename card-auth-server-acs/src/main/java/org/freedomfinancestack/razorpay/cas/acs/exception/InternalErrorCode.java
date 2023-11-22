package org.freedomfinancestack.razorpay.cas.acs.exception;

import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;

import lombok.Getter;

@Getter
public enum
        InternalErrorCode { // todo get this file reviewed by Ashish and Piyush and revisit error
    // codes

    CARD_USER_NOT_FOUND(
            "1001",
            "CARD NOT FOUND",
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
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),
    INSTITUTION_FETCH_EXCEPTION(
            "3003",
            "Error while fetching institution details",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    CARD_RANGE_NOT_ACTIVE(
            "3006",
            "CARD RANGE NOT ACTIVE",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),

    CARD_RANGE_NOT_FOUND(
            "3007",
            "CARD RANGE NOT FOUND",
            TransactionStatus.FAILED,
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

    // Error code for App based flow
    RENDERING_TYPE_NOT_FOUND(
            "3014",
            "RENDERING TYPE NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    UNSUPPPORTED_DEVICE_CATEGORY(
            "3015",
            "UNSUPPORTED DEVICE CATEGORY",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.UNSUPPORTED_DEVICE),
    SIGNER_DETAIL_NOT_FOUND(
            "3016",
            "SIGNER DETAIL NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    //    SIGNER_DETAIL_NOT_FOUND(
    //            "3017",
    //            "SIGNER DETAIL NOT FOUND",
    //            TransactionStatus.UNABLE_TO_AUTHENTICATE,
    //            TransactionStatusReason.ACS_TECHNICAL_ISSUE
    //    ),

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
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
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
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    CREQ_JSON_PARSING_ERROR(
            "8001",
            "Can't parse Creq message",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),

    SESSION_DATA_PARSING_ERROR(
            "8001",
            "threeDSSessionData is invalid",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),

    TRANSACTION_NOT_FOUND(
            "8002",
            "TRANSACTION ID NOT FOUND",
            TransactionStatus.FAILED,
            TransactionStatusReason.INVALID_TRANSACTION),
    INVALID_CONFIG(
            "8003",
            "CONFIG ERROR IN ACS",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    CANCELLED_BY_CARDHOLDER(
            "8004",
            "CANCELED BY CARD HOLDER",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.LOW_CONFIDENCE),
    NO_CHANNEL_FOUND_FOR_OTP(
            "8005",
            "NO CHANNEL FOUND FOR OTP",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.CARDHOLDER_NOT_ENROLLED_IN_SERVICE),
    INVALID_STATE_TRANSITION(
            "8006",
            "invalid state transition",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),
    EXCEED_MAX_ALLOWED_ATTEMPTS(
            "8007",
            "attempts exceeded",
            TransactionStatus.FAILED,
            TransactionStatusReason.EXCEED_MAX_CHALLANGES),
    INVALID_RRES(
            "8008",
            "invalid RRes received from DS",
            TransactionStatus.FAILED,
            TransactionStatusReason.INVALID_TRANSACTION),
    CONNECTION_TO_DS_FAILED(
            "8009",
            "can't connect to DS to send RReq",
            TransactionStatus.FAILED,
            TransactionStatusReason.TRANSACTION_TIMEOUT),
    CHALLENGE_RESEND_THRESHOLD_EXCEEDED(
            "8010",
            "challenge resend threshold exceeded",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.EXCEED_MAX_CHALLANGES),
    TRANSACTION_TIMED_OUT_WAITING_FOR_CREQ(
            "8011",
            "transaction timed out waiting for CREQ",
            TransactionStatus.FAILED,
            TransactionStatusReason.TRANSACTION_TIMEOUT),
    TRANSACTION_TIMED_OUT_CHALLENGE_COMPLETION(
            "8013",
            "transaction timed out for challenge completion",
            TransactionStatus.FAILED,
            TransactionStatusReason.TRANSACTION_TIMEOUT),
    UNABLE_TO_SEND_OTP(
            "8015",
            "UNABLE TO SEND OTP",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    CHALLENGE_FLOW_INTERNAL_SERVER_ERROR(
            "8016",
            "UNEXPECTED ERROR OCCURRED",
            TransactionStatus.FAILED,
            TransactionStatusReason.INVALID_TRANSACTION),

    TEST_TRANSACTION_UA(
            "9001",
            "CARD NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.NO_CARD_RECORD),

    TEST_TRANSACTION_REJECTED(
            "9002",
            "CARD NOT FOUND",
            TransactionStatus.REJECTED,
            TransactionStatusReason.NO_CARD_RECORD),

    TEST_TRANSACTION_ATTEMPTED(
            "9003",
            "CARD NOT FOUND",
            TransactionStatus.ATTEMPT,
            TransactionStatusReason.NO_CARD_RECORD),

    TEST_TRANSACTION_FAILED(
            "9004",
            "CARD NOT FOUND",
            TransactionStatus.FAILED,
            TransactionStatusReason.NO_CARD_RECORD),
    ;

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
