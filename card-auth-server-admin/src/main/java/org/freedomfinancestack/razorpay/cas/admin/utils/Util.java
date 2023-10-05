package org.freedomfinancestack.razorpay.cas.admin.utils;

import java.util.UUID;

public class Util {

    public static byte[] mapBase64StringToByteArray(String base64String) {
        if (base64String != null) {
            return java.util.Base64.getDecoder().decode(base64String);
        }
        return null;
    }

    /**
     * Generates a random UUID (Universally Unique Identifier).
     *
     * @return the random UUID as a string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
