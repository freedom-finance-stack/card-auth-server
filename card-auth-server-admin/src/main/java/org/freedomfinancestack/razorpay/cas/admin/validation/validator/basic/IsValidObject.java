package org.freedomfinancestack.razorpay.cas.admin.validation.validator.basic;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.Validatable;

public class IsValidObject<T extends Validatable> implements Validator<T> {

    public static <T extends Validatable> IsValidObject<T> isValidObject() {
        return new IsValidObject<T>();
    }

    @Override
    public void validate(Validatable value) throws RequestValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (!value.isValid()) {
            throw new RequestValidationException(
                    InternalErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
