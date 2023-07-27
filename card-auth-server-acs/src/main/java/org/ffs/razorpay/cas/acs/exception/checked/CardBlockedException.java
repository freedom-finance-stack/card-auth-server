package org.ffs.razorpay.cas.acs.exception.checked;

import org.ffs.razorpay.cas.acs.exception.InternalErrorCode;

public class CardBlockedException extends ACSException {

    /** */
    private static final long serialVersionUID = 1L;

    public CardBlockedException(
            InternalErrorCode internalErrorCode, String message, Throwable cause) {
        super(internalErrorCode, message, cause);
    }

    public CardBlockedException(InternalErrorCode internalErrorCode, String message) {
        super(internalErrorCode, message);
    }
}
