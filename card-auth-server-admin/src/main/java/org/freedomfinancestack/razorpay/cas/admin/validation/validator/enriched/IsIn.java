package org.freedomfinancestack.razorpay.cas.admin.validation.validator.enriched;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;

public class IsIn<T> implements Validator<T> {

    private final T[] acceptedValues;

    public IsIn(T[] acceptedValues) {
        this.acceptedValues = acceptedValues;
    }

    public static <T> IsIn<T> isIn(T[] acceptedValues) {
        return new IsIn<>(acceptedValues);
    }

    @Override
    public void validate(T value) throws RequestValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }

        for (T acceptedValue : acceptedValues) {
            if (acceptedValue.equals(value)) {
                return;
            }
        }

        throw new RequestValidationException(
                InternalErrorCode.INVALID_FORMAT_VALUE, "IsInRule failed");
    }
}
