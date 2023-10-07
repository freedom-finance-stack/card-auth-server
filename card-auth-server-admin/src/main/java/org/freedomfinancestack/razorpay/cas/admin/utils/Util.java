package org.freedomfinancestack.razorpay.cas.admin.utils;

import java.sql.Timestamp;
import java.util.UUID;

public class Util {

    public static Timestamp getCurrentTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();

        // Create a Timestamp object using the current time
        return new Timestamp(currentTimeMillis);
    }

    /**
     * Generates a random UUID (Universally Unique Identifier).
     *
     * @return the random UUID as a string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Checks if the given object is null or its string representation is blank.
     *
     * @param object the object to check
     * @return {@code true} if the object is null or its string representation is blank, {@code
     *     false} otherwise
     */
    public static boolean isNullorBlank(Object object) {
        if (null == object) {
            return true;
        } else return "".equals(object.toString());
    }
}