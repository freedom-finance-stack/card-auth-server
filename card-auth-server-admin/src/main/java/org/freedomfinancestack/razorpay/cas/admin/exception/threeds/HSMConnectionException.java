package org.freedomfinancestack.razorpay.cas.admin.exception.threeds;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

import lombok.NonNull;

public class HSMConnectionException extends ThreeDSException {

    public HSMConnectionException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            @NonNull final String message) {
        super(errorCode, internalErrorCode, message);
    }
}
