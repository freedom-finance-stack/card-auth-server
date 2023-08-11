package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class IsIn implements Validator<String> {

    private final String[] acceptedValues;

    public IsIn(String[] acceptedValues) {
        this.acceptedValues = acceptedValues;
    }

    public static IsIn isIn(String[] acceptedValues) {
        return new IsIn(acceptedValues);
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
