package com.razorpay.ffs.cas.acs.validator.rules;

import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.utils.Util;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;

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
