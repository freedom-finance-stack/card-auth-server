package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.acs.contract.Validatable;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.utils.Util;

public class IsValidRule<T extends Validatable> implements Rule<T> {

    @Override
    public void validate(Validatable value) throws ValidationException {
        if (!Util.isNullorBlank(value) && !value.isValid()) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
