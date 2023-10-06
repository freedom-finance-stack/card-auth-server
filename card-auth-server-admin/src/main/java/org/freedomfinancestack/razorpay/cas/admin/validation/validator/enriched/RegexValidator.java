package org.freedomfinancestack.razorpay.cas.admin.validation.validator.enriched;

import java.util.regex.Pattern;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;

public class RegexValidator implements Validator<String> {

    private final Pattern pattern;

    public RegexValidator(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public static RegexValidator regexValidator(String rgxPattern) {
        return new RegexValidator(rgxPattern);
    }

    @Override
    public void validate(String value) throws RequestValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (!pattern.matcher(value).matches()) {
            throw new RequestValidationException(
                    InternalErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
