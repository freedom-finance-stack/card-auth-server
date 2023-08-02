package org.freedomfinancestack.razorpay.cas.acs.exception.threeds;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class TransactionDataNotValidException extends ThreeDSException {

    public TransactionDataNotValidException(InternalErrorCode internalErrorCode, String message) {
        super(ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID, internalErrorCode, message);
    }

    public TransactionDataNotValidException(InternalErrorCode internalErrorCode) {
        super(
                ThreeDSecureErrorCode.TRANSACTION_DATA_NOT_VALID,
                internalErrorCode,
                internalErrorCode.getCode() + " : " + internalErrorCode.getDefaultErrorMessage());
    }
}
