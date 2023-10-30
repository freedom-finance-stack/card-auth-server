package org.freedomfinancestack.razorpay.cas.acs.service.impl;

import java.util.concurrent.ThreadLocalRandom;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.springframework.stereotype.Component;

@Component
public class RandomNumberGenerator {

    public String getIntRandomNumberInRange(int digit) {
        try {
            return ThreadLocalRandom.current().nextInt(getStartRange(digit), getEndRange(digit))
                    + "";
        } catch (Exception e) {
            return ThreadLocalRandom.current()
                            .nextInt(
                                    InternalConstants.OTP_START_RANGE,
                                    InternalConstants.OTP_END_RANGE)
                    + "";
        }
    }

    private Integer getStartRange(int digit) {
        String startRange = "1";
        for (int i = 0; i < digit - 1; i++) {
            startRange += "0";
        }
        return Integer.valueOf(startRange);
    }

    private Integer getEndRange(int digit) {
        String endRange = "";
        for (int i = 0; i < digit; i++) {
            endRange += "9";
        }
        return Integer.valueOf(endRange);
    }
}
