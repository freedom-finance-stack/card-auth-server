package org.freedomfinancestack.razorpay.cas.acs.exception;

import org.freedomfinancestack.razorpay.cas.contract.enums.TransactionStatusReason;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;

import lombok.Getter;

@Getter
public enum InternalErrorCode {

    // config
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

    INSTITUTION_NOT_FOUND(
            "1003",
            "INSTITUTION NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.NO_CARD_RECORD),

    INSTITUTION_INACTIVE(
            "1004",
            "INSTITUTION INACTIVE",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),

    INSTITUTION_FETCH_EXCEPTION(
            "1005",
            "Error while fetching institution details",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    CARD_RANGE_NOT_ACTIVE(
            "1006",
            "CARD RANGE NOT ACTIVE",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),

    CARD_RANGE_NOT_FOUND(
            "1007",
            "CARD RANGE NOT FOUND",
            TransactionStatus.FAILED,
            TransactionStatusReason.NO_CARD_RECORD),

    RANGE_GROUP_NOT_FOUND(
            "1008",
            "RANGE GROUP NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.NO_CARD_RECORD),

    CARD_RANGE_FETCH_EXCEPTION(
            "1009",
            "Error while fetching card range details",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    ACS_URL_NOT_FOUND(
            "1010",
            "ACS URL NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    INVALID_NETWORK(
            "1011",
            "INVALID NETWORK",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.TRANSACTION_NOT_PERMITTED),

    AUTH_CONFIG_NOT_PRESENT(
            "1012",
            "AUTH CONFIG NOT PRESENT",
            TransactionStatus.FAILED,
            TransactionStatusReason.NO_CARD_RECORD),

    NO_CHANNEL_FOUND_FOR_OTP(
            "1013",
            "NO CHANNEL FOUND FOR OTP",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.CARDHOLDER_NOT_ENROLLED_IN_SERVICE),




    // Error code for App based flow
    RENDERING_TYPE_NOT_FOUND(
            "2000",
            "RENDERING TYPE NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.UNSUPPORTED_DEVICE),
    UNSUPPPORTED_DEVICE_CATEGORY(
            "2001",
            "UNSUPPORTED DEVICE CATEGORY",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.UNSUPPORTED_DEVICE),
    SIGNER_DETAIL_NOT_FOUND(
            "2002",
            "SIGNER DETAIL NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    INSTITUTION_UI_CONFIG_NOT_FOUND(
            "2003",
            "INSTITUTION UI CONFIG NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    UNSUPPORTED_UI_TYPE(
            "2004",
            "UNSUPPORTED UI TYPE",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.UNSUPPORTED_DEVICE),
    DISPLAY_PAGE_PARSING_EXCEPTION(
            "2005",
            "DISPLAY PAGE PARSING EXCEPTION",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.UNSUPPORTED_DEVICE),
    DISPLAY_PAGE_NOT_FOUND(
            "2006",
            "DISPLAY PAGE NOT FOUND",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.UNSUPPORTED_DEVICE),
    UNSUPPORTED_DEVICE_INTERFACE(
            "2007",
            "UNSUPPORTED DEVICE INTERFACE",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.UNSUPPORTED_DEVICE),
    SIGNER_SERVICE_ALGORITHM_EXCEPTION(
            "2008",
            "SIGNER SERVICE ALGORITHM EXCEPTION",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    SIGNER_SERVICE_JOSE_EXCEPTION(
            "2009",
            "SIGNER SERVICE JOSE EXCEPTION",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    SIGNER_SERVICE_ENCRYPTION_EXCEPTION(
            "2010",
            "SIGNER SERVICE ENCRYPTION EXCEPTION",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    SIGNER_SERVICE_CERTIFICATE_EXCEPTION(
            "2011",
            "SIGNER SERVICE CERTIFICATE EXCEPTION",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    SIGNER_SERVICE_KEY_STORE_EXCEPTION(
            "2012",
            "SIGNER SERVICE KEY STORE EXCEPTION",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    // Error code for  Transaction
    TRANSACTION_SAVE_EXCEPTION(
            "3001",
            "Error while saving transaction",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    TRANSACTION_FIND_EXCEPTION(
            "3002",
            "Error while finding transaction",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    TRANSACTION_ID_NOT_RECOGNISED(
            "3003",
            "Transaction not Recognised",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),
    TRANSACTION_ID_EMPTY(
            "3004",
            "Transaction not Recognised",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),
    TRANSACTION_NOT_FOUND(
            "3005",
            "TRANSACTION ID NOT FOUND",
            TransactionStatus.FAILED,
            TransactionStatusReason.INVALID_TRANSACTION),

    // Flow

    INVALID_STATE_TRANSITION(
            "4001",
            "invalid state transition",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),

    EXCEED_MAX_ALLOWED_ATTEMPTS(
            "4002",
            "attempts exceeded",
            TransactionStatus.FAILED,
            TransactionStatusReason.EXCEED_MAX_CHALLENGES),

    CHALLENGE_RESEND_THRESHOLD_EXCEEDED(
            "4003",
            "challenge resend threshold exceeded",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.EXCEED_MAX_CHALLENGES),

    UNABLE_TO_SEND_OTP(
            "4004",
            "UNABLE TO SEND OTP",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    CHALLENGE_FLOW_INTERNAL_SERVER_ERROR(
            "4005",
            "UNEXPECTED ERROR OCCURRED",
            TransactionStatus.FAILED,
            TransactionStatusReason.INVALID_TRANSACTION),

    INTERNAL_SERVER_ERROR(
            "4006",
            "INTERNAL SERVER ERROR",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

   // IO connection
    HSM_CONNECTOR_REQUEST_TIMEOUT(
            "5001",
            "LUNA HSM CONNECTOR REQUEST TIMEOUT",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),
    HSM_CONNECTOR_CONNECTION_CLOSE(
            "5002",
            "LUNA HSM CONNECTOR CONNECTION IS CLOSED",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    HSM_INTERNAL_EXCEPTION(
            "5003",
            "Internal Error occurred in HSM",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    CONNECTION_TO_DS_FAILED(
            "5004",
            "can't connect to DS to send RReq",
            TransactionStatus.FAILED,
            TransactionStatusReason.TRANSACTION_TIMEOUT),

  // parsing  and validation
    CREQ_JSON_PARSING_ERROR(
            "6000",
            "Can't parse Creq message",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),

    SESSION_DATA_PARSING_ERROR(
            "6001",
            "threeDSSessionData is invalid",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),

    INVALID_REQUEST(
            "6002",
            "INVALID REQUEST",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.INVALID_TRANSACTION),

    CRES_ENCRYPTION_ERROR(
            "6003",
            "Can't encrypt CRES response",
            TransactionStatus.FAILED,
            TransactionStatusReason.ACS_TECHNICAL_ISSUE),

    INVALID_RRES(
            "6004",
            "invalid RRes received from DS",
            TransactionStatus.FAILED,
            TransactionStatusReason.INVALID_TRANSACTION),



    // timeout
    TRANSACTION_TIMED_OUT_WAITING_FOR_CREQ(
            "7000",
            "transaction timed out waiting for CREQ",
            TransactionStatus.FAILED,
            TransactionStatusReason.TRANSACTION_TIMEOUT),
    TRANSACTION_TIMED_OUT_CHALLENGE_COMPLETION(
            "7001",
            "transaction timed out for challenge completion",
            TransactionStatus.FAILED,
            TransactionStatusReason.TRANSACTION_TIMEOUT),
    TRANSACTION_TIMED_OUT_DECOUPLED_AUTH(
            "7002",
            "transaction timed out waiting for decoupled auth to complete",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.DECOUPLED_EXPIRY_TIME_EXCEEDED),

    TRANSACTION_TIMED_OUT_DS_RESPONSE(
            "7003",
            "Transaction Timed out waiting for DS Response",
            TransactionStatus.UNABLE_TO_AUTHENTICATE,
            TransactionStatusReason.TRANSACTION_TIMEOUT),

    // test
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
