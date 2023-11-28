package org.freedomfinancestack.razorpay.cas.acs.exception.threeds;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSErrorResponse;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.enums.MessageType;
import org.freedomfinancestack.razorpay.cas.dao.enums.Phase;
import org.freedomfinancestack.razorpay.cas.dao.model.Transaction;
import org.springframework.http.HttpStatus;

import lombok.Getter;

// This class represents exceptions specific to the 3DS (3D-Secure) functionality.
// All exceptions related to 3DS processing should extend this class, iff we need to send Erro in
// response
public class ThreeDSException extends Exception {

    @Getter private final ThreeDSecureErrorCode threeDSecureErrorCode;
    @Getter private InternalErrorCode internalErrorCode;
    private final ThreeDSErrorResponse threeDSErrorResponse = new ThreeDSErrorResponse();

    // ThreeDSecureErrorCode has information about the error code and error component that needs to
    // be returned in erro message
    // InternalErrorCode has information about the transaction status and transaction reason that
    // needs to be returned in response
    // message will be sent as error details in response
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
            final InternalErrorCode internalErrorCode) {
        super(internalErrorCode.getDefaultErrorMessage());
        addMetaInThreeDSecureErrorCode(
                this.threeDSErrorResponse,
                threeDSecureErrorCode,
                internalErrorCode.getDefaultErrorMessage());
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

    // to add transaction details in response message, throw using this constructor
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

    public ThreeDSException(
            final ThreeDSecureErrorCode threeDSecureErrorCode,
            final String message,
            final Transaction transaction) {
        super(message);
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
        if (transaction != null) {
            if (transaction.getTransactionReferenceDetail() != null) {
                threeDSErrorResponse.setThreeDSServerTransID(
                        transaction
                                .getTransactionReferenceDetail()
                                .getThreedsServerTransactionId());
                threeDSErrorResponse.setDsTransID(
                        transaction.getTransactionReferenceDetail().getDsTransactionId());
            }
            threeDSErrorResponse.setAcsTransID(transaction.getId());
            if (!Util.isNullorBlank(transaction.getMessageVersion())) {
                threeDSErrorResponse.setMessageVersion(transaction.getMessageVersion());
            }
            if (transaction.getPhase().equals(Phase.AREQ)
                    || transaction.getPhase().equals(Phase.AERROR)) {
                threeDSErrorResponse.setErrorMessageType(MessageType.AReq.toString());
            } else if (transaction.getPhase().equals(Phase.RREQ)) {
                threeDSErrorResponse.setErrorMessageType(MessageType.RRes.toString());
            } else {
                threeDSErrorResponse.setErrorMessageType(MessageType.CReq.toString());
            }
        }
    }
}
