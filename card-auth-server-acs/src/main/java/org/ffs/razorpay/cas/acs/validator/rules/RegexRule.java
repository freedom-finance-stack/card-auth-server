package org.ffs.razorpay.cas.acs.validator.rules;

import org.ffs.razorpay.cas.acs.exception.ValidationException;
import org.ffs.razorpay.cas.acs.utils.Util;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;

public class RegexRule implements Rule<String> {

    private final java.util.regex.Pattern pattern;

    public RegexRule(String regex) {
        this.pattern = java.util.regex.Pattern.compile(regex);
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