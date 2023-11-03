package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.security.SecureRandom;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.springframework.stereotype.Component;

@Component
public class RandomNumberGenerator {
    SecureRandom secureRandom = new SecureRandom();

    public String getIntRandomNumberInRange(int digit) {
        try {
            return String.valueOf(
                    this.secureRandom.nextInt(getStartRange(digit), getEndRange(digit)));

        } catch (Exception e) {
            return String.valueOf(
                    this.secureRandom.nextInt(
                            InternalConstants.OTP_START_RANGE, InternalConstants.OTP_END_RANGE));
        }
    }

    private Integer getStartRange(int digit) {
        return Integer.valueOf("1" + "0".repeat(Math.max(0, digit - 1)));
    }

    private Integer getEndRange(int digit) {
        return Integer.valueOf("9".repeat(Math.max(0, digit)));
    }
}
