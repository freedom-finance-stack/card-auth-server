package org.freedomfinancestack.razorpay.cas.admin.exception;

import org.freedomfinancestack.razorpay.cas.admin.dto.DefaultErrorResponse;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.ACSAdminDataAccessException;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.ACSAdminException;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.DataNotFoundException;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("An unexpected exception occurred: {}", ex.getMessage());
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ACSAdminDataAccessException.class)
    public ResponseEntity<?> handleACSAdminDataAccessException(ACSAdminDataAccessException ex) {
        return createErrorResponse(ex, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<?> handleRequestValidationException(RequestValidationException ex) {
        return createErrorResponse(ex, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(ACSAdminException.class)
    public ResponseEntity<?> handleACSAdminException(ACSAdminException ex) {
        return createErrorResponse(ex, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> handleDataNotFoundException(DataNotFoundException ex) {
        return createErrorResponse(ex, ex.getErrorCode().getHttpStatus());
    }

    private ResponseEntity<?> createErrorResponse(Exception ex, HttpStatus status) {
        if (ex instanceof ACSAdminException) {
            ACSAdminException acsAdminException = (ACSAdminException) ex;
            InternalErrorCode internalErrorCode = acsAdminException.getErrorCode();
            return new ResponseEntity<>(
                    new DefaultErrorResponse(
                            internalErrorCode.getCode(),
                            internalErrorCode.getDefaultErrorMessage(),
                            ex.getMessage(),
                            status.value()),
                    status);
        } else {
            return new ResponseEntity<>(
                    new DefaultErrorResponse(
                            "500", "INTERNAL SERVER ERROR", ex.getMessage(), status.value()),
                    status);
        }
    }
}
