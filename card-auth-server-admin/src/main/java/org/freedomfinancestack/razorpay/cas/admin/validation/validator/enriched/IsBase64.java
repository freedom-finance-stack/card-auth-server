package org.freedomfinancestack.razorpay.cas.admin.validation.validator.enriched;

import java.util.Base64;

import org.freedomfinancestack.razorpay.cas.admin.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.admin.exception.admin.RequestValidationException;
import org.freedomfinancestack.razorpay.cas.admin.utils.Util;
import org.freedomfinancestack.razorpay.cas.admin.validation.validator.Validator;

public class IsBase64 implements Validator<String> {

    public static IsBase64 isBase64() {
        return new IsBase64();
    }

    @Override
    public void validate(String value) throws RequestValidationException {
        if (Util.isNullorBlank(value)) {
            return;
        }
        if (!isValidBase64(value)) {
            throw new RequestValidationException(
                    InternalErrorCode.INVALID_FORMAT_VALUE, "Invalid Base64 format");
        }
    }

    private boolean isValidBase64(String value) {
        try {
            // Decoding the string to check if it's a valid Base64 encoding
            byte[] decodedBytes = Base64.getDecoder().decode(value);

            // Encoding the decoded bytes back to a string to check if the original string is valid
            // Base64
            String reencodedStr = Base64.getEncoder().encodeToString(decodedBytes);

            // If the reencoded string is equal to the original string, it's a valid Base64 encoding
            return value.equals(reencodedStr);

        } catch (IllegalArgumentException e) {
            // If an exception is thrown, it's not a valid Base64 encoding
            return false;
        }
    }
}
