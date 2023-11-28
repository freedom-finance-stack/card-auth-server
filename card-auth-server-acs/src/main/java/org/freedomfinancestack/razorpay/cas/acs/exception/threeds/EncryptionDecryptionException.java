package org.freedomfinancestack.razorpay.cas.acs.exception.threeds;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

import lombok.NonNull;

public class EncryptionDecryptionException extends ThreeDSException {

    public EncryptionDecryptionException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode) {
        super(errorCode, internalErrorCode, internalErrorCode.getDefaultErrorMessage());
    }

    public EncryptionDecryptionException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            Throwable cause) {
        super(errorCode, internalErrorCode, internalErrorCode.getDefaultErrorMessage(), cause);
    }

    public EncryptionDecryptionException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            @NonNull final String message) {
        super(errorCode, internalErrorCode, message);
    }

    public EncryptionDecryptionException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            @NonNull final String message,
            Throwable cause) {
        super(errorCode, internalErrorCode, message, cause);
    }
}
