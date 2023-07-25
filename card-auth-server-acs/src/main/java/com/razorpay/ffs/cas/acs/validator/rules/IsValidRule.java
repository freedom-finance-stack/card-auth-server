package com.razorpay.ffs.cas.acs.validator.rules;

import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.utils.Util;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;
import com.razorpay.ffs.cas.contract.Validatable;

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
