package org.freedomfinancestack.razorpay.cas.acs.validator.rules;

import java.util.UUID;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class IsUUIDRule implements Rule<String> {
    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
