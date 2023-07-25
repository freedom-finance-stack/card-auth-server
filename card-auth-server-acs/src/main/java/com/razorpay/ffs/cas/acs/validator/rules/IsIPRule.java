package com.razorpay.ffs.cas.acs.validator.rules;

import com.google.common.net.InetAddresses;
import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.utils.Util;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;

public class IsIPRule implements Rule<String> {
    public IsIPRule() {}

    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (!InetAddresses.isInetAddress(value)) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid IP address");
        }
    }
}
