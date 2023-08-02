package org.ffs.razorpay.cas.acs.validator.rules;

import org.ffs.razorpay.cas.acs.exception.threeds.ValidationException;
import org.ffs.razorpay.cas.contract.ThreeDSecureErrorCode;

public class MinMaxRule<T> implements Rule<T> {
    private final int min;
    private final int max;

    public MinMaxRule(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void validate(T value) throws ValidationException {

        if (value instanceof String) {
            String actualValue = (String) value;
            if ((min > 0 && actualValue.length() < min)
                    || (max > 0 && actualValue.length() > max)
                    || (min == 0 && max == 0 && actualValue.length() > 0)) {
                throw new ValidationException(
                        ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                        String.format("Invalid value "));
            }
        } else if (value instanceof Integer) {
            int actualValue = (int) value;
            if ((min > 0 && actualValue < min)
                    || (max > 0 && actualValue > max)
                    || (min == 0 && max == 0 && actualValue > 0)) {
                throw new ValidationException(
                        ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH,
                        String.format("Invalid value "));
            }
        } else {
            throw new ValidationException(
                    ThreeDSecureErrorCode.INVALID_FORMAT, String.format("Invalid value "));
        }
    }
}
