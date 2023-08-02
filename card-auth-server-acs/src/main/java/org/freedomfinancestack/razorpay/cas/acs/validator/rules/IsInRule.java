package org.freedomfinancestack.razorpay.cas.acs.validator.rules;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class IsInRule implements Rule<String> {

    private final String[] acceptedValues;

    public IsInRule(String[] acceptedValues) {
        this.acceptedValues = acceptedValues;
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        for (String acceptedValue : acceptedValues) {
            if (acceptedValue.equals(value)) {
                return;
            }
        }
        throw new ValidationException(
                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "IsInRule failed");
    }
}
