package com.razorpay.threeds.utils;

public class Util {


    public static boolean isNullorBlank(Object object) {
        if (null == object) {
            return true;
        } else if ("".equals(object.toString())) {
            return true;
        }

        return false;
    }
}
