package com.razorpay.threeds.exception;

import org.springframework.http.HttpStatus;

import com.razorpay.acs.dao.model.Transaction;

public class ThreeDSException extends RuntimeException {

  private final ThreeDSecureErrorCode errorCode;
  private final ThreeDSErrorResponse threeDSErrorResponse = new ThreeDSErrorResponse();

  public ThreeDSException(final ThreeDSecureErrorCode errorCode, final String message) {
    super(message);
    addMetaInThreeDSecureErrorCode(this.threeDSErrorResponse, errorCode, message);
    this.errorCode = errorCode;
  }

  public ThreeDSException(
      final ThreeDSecureErrorCode errorCode, final String message, final Throwable cause) {
    super(message, cause);
    addMetaInThreeDSecureErrorCode(this.threeDSErrorResponse, errorCode, message);
    this.errorCode = errorCode;
  }

  public ThreeDSException(
      final ThreeDSecureErrorCode errorCode,
      final String message,
      final Transaction transaction,
      final Throwable cause) {
    super(message, cause);
    addMetaInThreeDSecureErrorCode(this.threeDSErrorResponse, errorCode, message);
    addMetaInThreeDSecureErrorCode(this.threeDSErrorResponse, transaction);
    this.errorCode = errorCode;
  }

  public ThreeDSException(final ThreeDSecureErrorCode errorCode) {
    this(errorCode, errorCode.getErrorCode());
    addMetaInThreeDSecureErrorCode(
        this.threeDSErrorResponse, errorCode, errorCode.getErrorDescription());
  }

  public ThreeDSecureErrorCode getErrorCode() {
    return errorCode;
  }

  public ThreeDSErrorResponse getErrorResponse() {
    return this.threeDSErrorResponse.setHttpStatus(HttpStatus.OK);
  }

  private void addMetaInThreeDSecureErrorCode(
      final ThreeDSErrorResponse threeDSErrorResponse,
      final ThreeDSecureErrorCode errorCode,
      final String message) {
    threeDSErrorResponse.setErrorCode(errorCode.getErrorCode());
    threeDSErrorResponse.setErrorComponent(errorCode.getErrorComponent());
    threeDSErrorResponse.setErrorDescription(errorCode.getErrorDescription());
    threeDSErrorResponse.setErrorDetail(message);
  }

  private void addMetaInThreeDSecureErrorCode(
      final ThreeDSErrorResponse threeDSErrorResponse, final Transaction transaction) {
    threeDSErrorResponse.setThreeDSServerTransID(
        transaction.getTransactionReferenceDetail().getThreedsServerTransactionId());
    threeDSErrorResponse.setDsTransID(
        transaction.getTransactionReferenceDetail().getDsTransactionId());
    threeDSErrorResponse.setAcsTransID(transaction.getId());
    threeDSErrorResponse.setMessageVersion(transaction.getMessageVersion());
  }
}
