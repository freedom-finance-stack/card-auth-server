//package com.razorpay.threeds.exception;
//
//public enum ErrorCode {
//
//    INVALID_HTTP_METHOD(4051, "INVALID HTTP METHOD"),
//    ENTITY_NOT_FOUND(4041, "NO ENTITY FOUND"),
//    INVALID_URL(4061, "INVALID URL"),
//    INTERNAL_SERVER_EXCEPTION(5001, "INTERNAL SERVER EXCEPTION"),
//    INVALID_CREDENTIALS(4011, "INVALID CREDENTIALS"),
//    INVALID_USERID(4012, "INVALID USERID");
//
//    private final int code;
//
//    private final String defaultErrorMessage;
//
//    ErrorCode(final int errorCode,
//              final String defaultErrorMessage) {
//
//        this.code = errorCode;
//        this.defaultErrorMessage = defaultErrorMessage;
//    }
//
//    public String getDefaultErrorMessage() {
//        return defaultErrorMessage;
//    }
//
//    public int getCode() {
//        return code;
//    }
//}