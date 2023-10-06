package org.freedomfinancestack.razorpay.cas.admin.validation.validator.basic;

import java.util.UUID;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IsUUID implements Validator<String> {
    private static final IsUUID INSTANCE = new IsUUID();

    public static IsUUID isUUID() {
        return INSTANCE;
    }

    @Override
    public void validate(String value) throws RequestValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw new RequestValidationException(
                    InternalErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
