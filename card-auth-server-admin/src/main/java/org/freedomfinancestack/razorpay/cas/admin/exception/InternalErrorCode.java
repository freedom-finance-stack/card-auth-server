package org.freedomfinancestack.razorpay.cas.admin.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum InternalErrorCode {
    // Common Internal Error Codes
    INTERNAL_SERVER_ERROR("500", "INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    REQUEST_VALIDATION_ERROR("400", "INVALID REQUEST", HttpStatus.BAD_REQUEST),

    DATA_NOT_FOUND("404", "DATA NOT FOUND", HttpStatus.NOT_FOUND),
    INVALID_FORMAT("400", "Format of one or more elements is invalid", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT_LENGTH("400", "Invalid Format - Length", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT_VALUE("400", "Invalid Format - Value", HttpStatus.BAD_REQUEST),
    DUPLICATE_DATA_ELEMENT("400", "Duplicate Data Element", HttpStatus.BAD_REQUEST),
    DATA_DECRYPTION_FAILURE("400", "Data could not be decrypted", HttpStatus.BAD_REQUEST),
    ISO_CODE_INVALID("400", "ISO Code invalid", HttpStatus.BAD_REQUEST),
    REQUIRED_DATA_ELEMENT_MISSING(
            "400", "A element required is missing from the request.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String defaultErrorMessage;
    private final HttpStatus httpStatus;

    InternalErrorCode(
            final String errorCode, final String defaultErrorMessage, HttpStatus httpStatus) {
        this.code = errorCode;
        this.defaultErrorMessage = defaultErrorMessage;
        this.httpStatus = httpStatus;
    }
}
