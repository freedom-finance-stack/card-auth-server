package org.freedomfinancestack.razorpay.cas.admin.exception.threeds;

import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class OperationNotSupportedException extends ThreeDSException {

    public OperationNotSupportedException(InternalErrorCode internalErrorCode, String message) {
        super(ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR, internalErrorCode, message);
    }

    public OperationNotSupportedException(InternalErrorCode internalErrorCode) {
        super(
                ThreeDSecureErrorCode.ACS_TECHNICAL_ERROR,
                internalErrorCode,
                internalErrorCode.getCode() + " : " + internalErrorCode.getDefaultErrorMessage());
    }
}
