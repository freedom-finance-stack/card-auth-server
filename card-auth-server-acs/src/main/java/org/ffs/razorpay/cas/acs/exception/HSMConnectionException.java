package org.ffs.razorpay.cas.acs.exception;

import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;

import lombok.NonNull;

public class HSMConnectionException extends ThreeDSException {

    public HSMConnectionException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            @NonNull final String message) {
        super(errorCode, internalErrorCode, message);
    }
}
