package org.freedomfinancestack.razorpay.cas.admin.validation.validator.enriched;

import java.util.regex.Pattern;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.freedomfinancestack.razorpay.cas.acs.validation.validator.Validator;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;

public class RegexValidator implements Validator<String> {

    private final Pattern pattern;

    public RegexValidator(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public static RegexValidator regexValidator(String rgxPattern) {
        return new RegexValidator(rgxPattern);
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (!pattern.matcher(value).matches()) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid value");
        }
    }
}
