package com.razorpay.acs.dao.enums;


public enum Phase {
    AREQ,
    ARES,
    CREQ,
    RETRY_CREQ,
    CRES,
    RREQ,
    REDIRECT,
    RESEND_OTP,
    AUTH_INITIATE,
    GENERATE_OTP,
    AUTH_RESULT,
    SEAMLESS_GENERATE_OTP,
    VERIFY_OTP,
    RRES,
    ERROR
}