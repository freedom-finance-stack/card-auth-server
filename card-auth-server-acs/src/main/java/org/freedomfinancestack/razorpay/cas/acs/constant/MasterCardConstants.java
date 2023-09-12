package org.freedomfinancestack.razorpay.cas.acs.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MasterCardConstants {

    //IAV Calculation Constants
    public static final String HMACSHA256_ALGORITHM = "HmacSHA256";
    public static final String TLV_TAG = "C604";
    public static final String ZERO_PADDING = "000000000000000000000000000000";
}
