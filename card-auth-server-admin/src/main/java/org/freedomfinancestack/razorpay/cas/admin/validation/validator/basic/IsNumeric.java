package org.freedomfinancestack.razorpay.cas.admin.validation.validator.basic;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IsNumeric implements Validator<String> {
    private static final IsNumeric INSTANCE = new IsNumeric();

    public static IsNumeric isNumeric() {
        return INSTANCE;
    }

    @Override
    public void validate(String value) throws RequestValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (!value.matches("[0-9]+")) {
            throw new RequestValidationException(
                    InternalErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
