package org.freedomfinancestack.razorpay.cas.admin.utils;

import java.util.Random;

import org.freedomfinancestack.razorpay.cas.admin.constants.InternalConstants;

public class Util {

    public static String generateRandomStringID(Integer length) {
        //        need to handle exception handling here
        //        if (length <= 0) {
        //            throw new exception
        //        }

        long currentTimestamp = System.currentTimeMillis();
        Random random = new Random(currentTimestamp);
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(InternalConstants.CHARACTERS.length());
            char randomChar = InternalConstants.CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public static byte[] mapBase64StringToByteArray(String base64String) {
        if (base64String != null) {
            return java.util.Base64.getDecoder().decode(base64String);
        }
        return null;
    }
}
