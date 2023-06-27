package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.acs.contract.Validatable;
import com.razorpay.threeds.exception.ValidationException;

public class isValidRule<T extends Validatable> implements Rule<T> {

    @Override
    public void validate(Validatable value) throws ValidationException {
        if (!value.isValid()) {
            throw new ValidationException(ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
