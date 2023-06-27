package com.razorpay.threeds.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.razorpay.acs.contract.ThreeDSErrorResponse;
import com.razorpay.acs.contract.ThreeDSecureErrorCode;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  // todo there more method needs to override if we want consistent behaviour across all the errors
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.error(ex.getMessage(), ex);
    ThreeDSErrorResponse errorResponse =
        new ThreeDSErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorCode(),
            "Request method '" + ex.getMethod() + "' not supported",
            ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorComponent(),
            ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorDescription());
    return handleExceptionInternal(ex, errorResponse, headers, status, request);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ThreeDSErrorResponse> handleThrowable(Throwable e) {
    // these case should happen ideally
    // todo we need to set alert for this
    log.error(e.getMessage(), e);
    ThreeDSErrorResponse errorResponse =
        new ThreeDSErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR.getErrorCode(),
            e.getMessage(),
            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR.getErrorComponent(),
            ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR.getErrorDescription());
    return buildResponseEntity(errorResponse);
  }

  @ExceptionHandler(ThreeDSException.class)
  public ResponseEntity<ThreeDSErrorResponse> handleACSException(ThreeDSException exception) {
    log.error(exception.getMessage(), exception);
    return buildResponseEntity(exception.getErrorResponse());
  }

  private ResponseEntity<ThreeDSErrorResponse> buildResponseEntity(ThreeDSErrorResponse apiError) {
    return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getHttpStatus()));
  }
}
