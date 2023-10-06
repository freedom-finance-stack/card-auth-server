package org.freedomfinancestack.razorpay.cas.admin.validation.validator.enriched;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;

public class NotIn implements Validator<String> {

    private final List<String> excludedValues;

    public NotIn(List<String> excludedValues) {
        this.excludedValues = excludedValues;
    }

    public static NotIn notIn(List<String> excludedValues) {
        return new NotIn(excludedValues);
    }

    @Override
    public void validate(String value) throws RequestValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (excludedValues.contains(value)) {
            throw new RequestValidationException(
                    InternalErrorCode.INVALID_FORMAT_VALUE, "IsInRule failed");
        }
    }
}
