package org.freedomfinancestack.razorpay.cas.acs.exception;

import java.util.Set;

import org.freedomfinancestack.razorpay.cas.acs.constant.RouteConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ThreeDSException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode httpStatusCode,
            WebRequest request) {
        log.error(ex.getMessage(), ex);
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }
        ThreeDSErrorResponse errorResponse =
                new ThreeDSErrorResponse(
                        httpStatusCode.value(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorCode(),
                        "Request method '" + ex.getMethod() + "' not supported",
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorComponent(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorDescription());
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            HttpHeaders headers,
            HttpStatusCode httpStatusCode,
            WebRequest request) {
        log.error(ex.getMessage(), ex);
        ThreeDSErrorResponse errorResponse =
                new ThreeDSErrorResponse(
                        httpStatusCode.value(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorCode(),
                        "Request has missing path variable",
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorComponent(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorDescription());
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode httpStatusCode,
            WebRequest request) {
        log.error(ex.getMessage(), ex);
        ThreeDSErrorResponse errorResponse =
                new ThreeDSErrorResponse(
                        httpStatusCode.value(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorCode(),
                        "Request has invalid method argument",
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorComponent(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorDescription());
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode httpStatusCode,
            WebRequest request) {
        log.error(ex.getMessage(), ex);
        ThreeDSErrorResponse errorResponse =
                new ThreeDSErrorResponse(
                        HttpStatus.OK.value(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorCode(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorComponent(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorDescription(),
                        "Request message not readable");
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.OK, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode httpStatusCode,
            WebRequest request) {
        if (isChallengeBrowserRoute(request)) {
            return handleExceptionInternal(ex, "", headers, HttpStatus.OK, request);
        }
        log.error(ex.getMessage(), ex);
        ThreeDSErrorResponse errorResponse =
                new ThreeDSErrorResponse(
                        httpStatusCode.value(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorCode(),
                        "Request parameter missing",
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorComponent(),
                        ThreeDSecureErrorCode.MESSAGE_RECEIVED_INVALID.getErrorDescription());
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.OK, request);
    }

    private boolean isChallengeBrowserRoute(WebRequest request) {
        String requestUrl = request.getDescription(false); // Get the request URL
        return requestUrl.contains(RouteConstants.CHALLENGE_BROWSER_ROUTE);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ThreeDSErrorResponse> handleThrowable(Throwable e) {
        // these case should happen ideally set alert for this
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

    private ResponseEntity<ThreeDSErrorResponse> buildResponseEntity(
            ThreeDSErrorResponse apiError) {
        return new ResponseEntity<>(apiError, HttpStatusCode.valueOf(apiError.getHttpStatus()));
    }
}
