package org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.freedomfinancestack.razorpay.cas.contract.Validatable;

public class IsValidObject<T extends Validatable> implements Validator<T> {

    public static <T extends Validatable> IsValidObject<T> isValidObject() {
        return new IsValidObject<T>();
    }

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
