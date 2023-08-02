package org.ffs.razorpay.cas.acs.validator.rules;

import org.ffs.razorpay.cas.acs.exception.threeds.ValidationException;
import org.ffs.razorpay.cas.acs.utils.Util;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;

import com.google.common.net.InetAddresses;

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
