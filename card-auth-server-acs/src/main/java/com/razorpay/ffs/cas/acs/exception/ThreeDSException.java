package com.razorpay.ffs.cas.acs.exception;

import org.springframework.http.HttpStatus;

import com.razorpay.ffs.cas.contract.ThreeDSErrorResponse;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;
import com.razorpay.ffs.cas.dao.model.Transaction;

import lombok.Getter;

public class ThreeDSException extends Exception {

    @Getter private final ThreeDSecureErrorCode threeDSecureErrorCode;
    @Getter private InternalErrorCode internalErrorCode;
    private final ThreeDSErrorResponse threeDSErrorResponse = new ThreeDSErrorResponse(

    public ThreeDSException(
            final ThreeDSecureErrorCode threeDSecureErrorCode,
            final InternalErrorCode internalErrorCode,
            final String message) {
        super(message);
        addMetaInThreeDSecureErrorCode(this.threeDSErrorResponse, threeDSecureErrorCode, message);
        this.threeDSecureErrorCode = threeDSecureErrorCode;
        this.internalErrorCode = internalErrorCode;
    }

    public ThreeDSException(
            final ThreeDSecureErrorCode threeDSecureErrorCode,
            final InternalErrorCode internalErrorCode,
            final String message,
            final Throwable cause) {
        super(message, cause);
        addMetaInThreeDSecureErrorCode(this.threeDSErrorResponse, threeDSecureErrorCode, message);
        this.threeDSecureErrorCode = threeDSecureErrorCode;
        this.internalErrorCode = internalErrorCode;
    }

    public ThreeDSException(
            final ThreeDSecureErrorCode threeDSecureErrorCode,
            final String message,
            final Transaction transaction,
            final Throwable cause) {
        super(message, cause);
        addMetaInThreeDSecureErrorCode(this.threeDSErrorResponse, threeDSecureErrorCode, message);
        addMetaInThreeDSecureErrorCode(this.threeDSErrorResponse, transaction);
        this.threeDSecureErrorCode = threeDSecureErrorCode;
    }

    public ThreeDSErrorResponse getErrorResponse() {
        return this.threeDSErrorResponse.setHttpStatus(HttpStatus.OK.value());
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
        if (transaction.getTransactionReferenceDetail() != null) {
            threeDSErrorResponse.setThreeDSServerTransID(
                    transaction.getTransactionReferenceDetail().getThreedsServerTransactionId());
            threeDSErrorResponse.setDsTransID(
                    transaction.getTransactionReferenceDetail().getDsTransactionId());
        }
        threeDSErrorResponse.setAcsTransID(transaction.getId());
        threeDSErrorResponse.setMessageVersion(transaction.getMessageVersion());
    }
}
