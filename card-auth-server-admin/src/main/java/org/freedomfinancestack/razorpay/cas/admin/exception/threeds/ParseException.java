package org.freedomfinancestack.razorpay.cas.admin.exception.threeds;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

import lombok.NonNull;

/**
 * Exception thrown when parsing fails.
 *
 * @author jaydeepRadadiya
 * @version 1.0.0
 * @see ThreeDSException
 * @since 1.0.0
 */
public class ParseException extends ThreeDSException {

    public ParseException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode) {
        super(errorCode, internalErrorCode, internalErrorCode.getDefaultErrorMessage());
    }

    public ParseException(
            @NonNull final ThreeDSecureErrorCode errorCode,
            @NonNull final InternalErrorCode internalErrorCode,
            Throwable cause) {
        super(errorCode, internalErrorCode, internalErrorCode.getDefaultErrorMessage(), cause);
    }
}
