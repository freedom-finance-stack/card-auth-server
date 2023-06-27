package com.razorpay.threeds.validator.rules;

import com.razorpay.acs.contract.ThreeDSecureErrorCode;
import com.razorpay.threeds.exception.ValidationException;

public class MinMaxRule<T> implements Rule<T> {
    private int min;
    private int max;

    public MinMaxRule(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void validate(T value) throws ValidationException {

        if (value instanceof String) {
            String actualValue = (String) value;
            if ((min > 0 && actualValue.length() < min) || (max > 0 && actualValue.length() > max) || (min == 0 && max == 0 && actualValue.length() > 0)) {
                throw new ValidationException(ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                        String.format("Invalid value "));
            }
        } else if (value instanceof Integer) {
            int actualValue = (int) value;
            if ((min > 0 && actualValue < min) || (max > 0 && actualValue > max) || (min == 0 && max == 0 && actualValue > 0)) {
                throw new ValidationException(ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                        String.format("Invalid value "));
            }
        } else {
            throw new ValidationException(ThreeDSecureErrorCode.INVALID_FORMAT,
                    String.format("Invalid value "));
        }
    }

}
