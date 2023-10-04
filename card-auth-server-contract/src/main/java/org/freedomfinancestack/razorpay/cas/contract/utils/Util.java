package org.freedomfinancestack.razorpay.cas.contract.utils;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Util {
    public static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_FORMAT_YYDDD = "yyDDD";

    public static boolean isValidDate(String strDate, String dateFormat) {
        try {
            TemporalAccessor ta = DateTimeFormatter.ofPattern(dateFormat).parse(strDate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
