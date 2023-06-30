package com.razorpay.threeds.validator.rules;

import java.util.Arrays;

import com.razorpay.acs.contract.AREQ;
import com.razorpay.threeds.exception.ValidationException;
import com.razorpay.threeds.validator.enums.ThreeDSDataElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validation {
  @SafeVarargs
  public static <T> void validate(String fieldName, T value, Rule<T>... rules)
      throws ValidationException {
    for (Rule<T> rule : rules) {
      try {
        rule.validate(value);
      } catch (ValidationException e) {
        log.error(
            "Validation failed for rule: {}, field: {}",
            rule.getClass().getSimpleName(),
            fieldName);
        throw new ValidationException(
            e.getThreeDSecureErrorCode(), "Invalid value for " + fieldName);
      }
    }
  }

  public static boolean validateDeviceChannelAndMessageCategory(
      ThreeDSDataElement element, AREQ areq) {
    return validateDeviceChannel(element, areq) && validateMessageCategory(element, areq);
  }

  public static boolean validateDeviceChannel(ThreeDSDataElement element, AREQ areq) {
    return Arrays.stream(element.getSupportedChannel())
        .anyMatch(sc -> sc.getChannel().equals(areq.getDeviceChannel()));
  }

  public static boolean validateMessageCategory(ThreeDSDataElement element, AREQ areq) {
    return Arrays.stream(element.getSupportedCategory())
        .anyMatch(sc -> sc.getCategory().equals(areq.getMessageCategory()));
  }
}
