package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import java.util.List;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class NotIn implements Validator<String> {

    private final List<String> excludedValues;

    public NotIn(List<String> excludedValues) {
        this.excludedValues = excludedValues;
    }

    public static NotIn notIn(List<String> excludedValues) {
        return new NotIn(excludedValues);
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (excludedValues.contains(value)) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "IsInRule failed");
        }
    }
}
