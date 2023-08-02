package org.freedomfinancestack.razorpay.cas.acs.validator.rules;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.Validatable;

public class IsValidRule<T extends Validatable> implements Rule<T> {

    @Override
    public void validate(Validatable value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (!value.isValid()) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
