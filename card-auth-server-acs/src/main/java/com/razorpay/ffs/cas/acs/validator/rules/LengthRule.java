package com.razorpay.ffs.cas.acs.validator.rules;

import com.razorpay.ffs.cas.acs.exception.ValidationException;
import com.razorpay.ffs.cas.acs.utils.Util;
import com.razorpay.ffs.cas.acs.validator.enums.DataLengthType;
import com.razorpay.ffs.cas.contract.ThreeDSecureErrorCode;

public class LengthRule implements Rule<String> {

    private final int length;
    private final DataLengthType lengthType;

    public LengthRule(DataLengthType lengthType, int length) {
        this.length = length;
        this.lengthType = lengthType;
    }

    @Override
    public void validate(String value) throws ValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (DataLengthType.FIXED.equals(lengthType)) {
            if (length != value.length()) {
                throw new ValidationException(
                        ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, "Invalid value ");
            }
        } else if (DataLengthType.VARIABLE.equals(lengthType)) {
            if (value.length() > length) {
                throw new ValidationException(
                        ThreeDSecureErrorCode.INVALID_FORMAT_LENGTH, "Invalid value ");
            }
        }
    }
}
