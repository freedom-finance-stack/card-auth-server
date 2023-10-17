package org.freedomfinancestack.razorpay.cas.admin.dto;

public class DefaultErrorResponse {
    private final String errorCode;
    private final String errorDescription;
    private final String exceptionMessage;
    private final int httpStatusCode;

    public DefaultErrorResponse(
            String errorCode,
            String errorDescription,
            String exceptionMessage,
            int httpStatusCode) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.exceptionMessage = exceptionMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
