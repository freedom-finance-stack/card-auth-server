package com.razorpay.acs.dao.enums;


public enum TransactionStatus {
    SUCCESS,
    FAILED,
    UNABLE_TO_AUTHENTICATE,
    ATTEMPT,
    CHALLENGE_REQUIRED,
    CHALLENGE_REQUIRED_DECOUPLED,
    REJECTED,
    INFORMATIONAL
}
