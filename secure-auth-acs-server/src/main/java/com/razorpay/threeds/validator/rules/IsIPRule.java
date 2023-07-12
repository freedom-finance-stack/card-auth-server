package com.razorpay.threeds.validator.rules;

import com.google.common.net.InetAddresses;
import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;

public class IsIPRule implements Rule<String> {
    public IsIPRule() {}

    @Override
    public void validate(String value) throws ValidationException {
        if (value == null || value.isEmpty()) {
            return;
        }
        if (!InetAddresses.isInetAddress(value)) {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, "Invalid IP address");
        }
    }
}
