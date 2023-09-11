package org.freedomfinancestack.razorpay.cas.acs.exception.threeds;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class InvalidAuthTypeException extends ThreeDSException {

    public InvalidAuthTypeException(InternalErrorCode internalErrorCode, String message) {
        super(ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, internalErrorCode, message);
    }

    public InvalidAuthTypeException(InternalErrorCode internalErrorCode) {
        super(
                ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                internalErrorCode,
                internalErrorCode.getCode() + " : " + internalErrorCode.getDefaultErrorMessage());
    }
}
