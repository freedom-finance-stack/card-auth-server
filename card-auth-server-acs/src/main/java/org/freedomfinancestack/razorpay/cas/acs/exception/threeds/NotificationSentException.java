package org.freedomfinancestack.razorpay.cas.acs.exception.threeds;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

import lombok.NonNull;

public class NotificationSentException extends ThreeDSException {

    public NotificationSentException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            @NonNull final String message) {
        super(errorCode, internalErrorCode, message);
    }

    public NotificationSentException(
            @NonNull final ThreeDSecureErrorCode threeDSecureErrorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            @NonNull final Throwable cause) {

        super(
                threeDSecureErrorCode,
                internalErrorCode,
                internalErrorCode.getDefaultErrorMessage(),
                cause);
    }
}
