package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class IsDate implements Validator<String> {

    private String acceptedFormat;

    public IsDate(String acceptedFormat) {
        this.acceptedFormat = acceptedFormat;
    }

    public static IsDate isDate(String acceptedFormat) {
        return new IsDate(acceptedFormat);
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(acceptedFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(value);
        } catch (ParseException ex) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid date format");
        }
    }
}
