package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.security.SecureRandom;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.springframework.stereotype.Component;

@Component
public class RandomNumberGenerator {
    private static final int MIN_DIGIT = 1;
    private static final int MAX_DIGIT = 9;
    SecureRandom secureRandom = new SecureRandom();

    public String getIntRandomNumberInRange(int digit) {
        if (digit < MIN_DIGIT || digit > MAX_DIGIT) {
            return String.valueOf(
                    this.secureRandom.nextInt(
                            InternalConstants.OTP_START_RANGE, InternalConstants.OTP_END_RANGE));
        }
        int o = this.secureRandom.nextInt(getStartRange(digit), getEndRange(digit));
        return String.valueOf(o);
    }

    private Integer getStartRange(int digit) {
        return Integer.valueOf("1" + "0".repeat(Math.max(0, digit - 1)));
    }

    private Integer getEndRange(int digit) {
        return Integer.valueOf("9".repeat(Math.max(0, digit)));
    }
}
