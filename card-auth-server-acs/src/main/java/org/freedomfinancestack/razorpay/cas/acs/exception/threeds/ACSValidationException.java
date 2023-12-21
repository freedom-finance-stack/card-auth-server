package org.freedomfinancestack.razorpay.cas.acs.exception.threeds;

import org.freedomfinancestack.extensions.validation.exception.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.ValidationErrorCodeMapper;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class ACSValidationException extends ThreeDSException {

    public ACSValidationException(ThreeDSecureErrorCode errorCode, String message) {
        super(errorCode, InternalErrorCode.INVALID_REQUEST, message);
    }

    public ACSValidationException(ThreeDSecureErrorCode errorCode) {
        super(errorCode, InternalErrorCode.INVALID_REQUEST);
    }

    protected ACSValidationException(
            ThreeDSecureErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, InternalErrorCode.INVALID_REQUEST, message, cause);
    }

    public ACSValidationException(ValidationException validationException) {
        super(
                ValidationErrorCodeMapper.mapValidationToThreeDSecure(
                        validationException.getValidationErrorCode(),
                        validationException.getFieldName()),
                InternalErrorCode.INVALID_REQUEST,
                validationException.getMessage());
    }
}
