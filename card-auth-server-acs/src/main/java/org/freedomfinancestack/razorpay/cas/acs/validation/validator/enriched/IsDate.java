package org.freedomfinancestack.razorpay.cas.acs.validation.validator.enriched;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class IsDate implements Validator<String> {

    private final String acceptedFormat;

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
        if (!isValidDateFormat(value, this.acceptedFormat)) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid date format");
        }
    }

    private boolean isValidDateFormat(String value, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            Date parsedDate = sdf.parse(value);
            String formattedDate = sdf.format(parsedDate);
            // Compare the formatted date with the original value to ensure accurate validation
            return formattedDate.equals(value);
        } catch (ParseException e) {
            return false;
        }
    }
}
