package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.AREQ;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.validator.enums.ThreeDSDataElement;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class Validation {
    @SafeVarargs
    public static <T> void validate(String fieldName, T value, Rule<T>... rules) throws ValidationException {
        for (Rule<T> rule : rules) {
            try {
                rule.validate(value);
            } catch (ValidationException e) {
                log.error("Validation failed for rule: {}, field: {}", rule.getClass().getSimpleName(), fieldName);
                throw new ValidationException(e.getThreeDSecureErrorCode(), "Invalid value for " + fieldName);
            }
        }
    }

    public static boolean validateDeviceChannelAndMessageType(ThreeDSDataElement element, AREQ areq) {
        return Arrays.stream(ThreeDSDataElement.THREEDS_COMPIND.getSupportedCategory()).anyMatch(sc -> sc.getCategory().equals(areq.getDeviceChannel()))
                && Arrays.stream(ThreeDSDataElement.THREEDS_COMPIND.getSupportedChannel()).anyMatch(sc -> sc.getChannel().equals(areq.getMessageCategory()));
    }
}
