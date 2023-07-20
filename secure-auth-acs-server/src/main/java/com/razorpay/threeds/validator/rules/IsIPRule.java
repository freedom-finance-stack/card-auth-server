package com.razorpay.threeds.validator.rules;

import com.google.common.net.InetAddresses;
import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.utils.Util;

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
