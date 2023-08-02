package org.freedomfinancestack.razorpay.cas.acs.validation.validator.basic;

import org.freedomfinancestack.razorpay.cas.acs.exception.threeds.ValidationException;
import org.freedomfinancestack.razorpay.cas.contract.ThreeDSecureErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IsIPTest {

    @Test
    public void testValidateValidIPv4() throws ValidationException {
        String validIPv4 = "192.168.1.1";
        IsIP.isIP().validate(validIPv4);
        // No exception should be thrown
    }

    @Test
    public void testValidateValidIPv6() throws ValidationException {
        String validIPv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        IsIP.isIP().validate(validIPv6);
        // No exception should be thrown
    }

    @Test
    public void testValidateInvalidIP() {
        String invalidIP = "invalidIP";
        ValidationException exception =
                assertThrows(ValidationException.class, () -> IsIP.isIP().validate(invalidIP));
        assertEquals(
                ThreeDSecureErrorCode.INVALID_FORMAT_VALUE, exception.getThreeDSecureErrorCode());
        assertEquals("Invalid IP address", exception.getMessage());
    }

    @Test
    public void testValidateNullValue() throws ValidationException {
        String nullValue = null;
        IsIP.isIP().validate(nullValue);
        // No exception should be thrown
    }

    @Test
    public void testValidateBlankValue() throws ValidationException {
        String blankValue = "";
        IsIP.isIP().validate(blankValue);
        // No exception should be thrown
    }
}
