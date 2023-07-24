package com.razorpay.ffs.cas.acs.exception;

import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;

import lombok.NonNull;

public class HSMConnectionException extends ThreeDSException {

    public HSMConnectionException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            @NonNull final String message) {
        super(errorCode, internalErrorCode, message);
    }
}
