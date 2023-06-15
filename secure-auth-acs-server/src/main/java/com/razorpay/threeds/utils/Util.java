package com.razorpay.threeds.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class Util {
    public static final String PADDED_SYMBOL_X = "X";
    public static final String PADDED_SYMBOL_0 = "0";
    public static final String PAD_LEFT = "LEFT";
    public static final String PAD_RIGHT = "RIGHT";

    public static boolean isNull(Object object) {
        return object == null ? true : false;
    }

    public static boolean isNotNull(Object object) {
        return object != null ? true : false;
    }

    public static boolean isNullorBlank(Object object) {
        if (null == object) {
            return true;
        } else if ("".equals(object.toString())) {
            return true;
        }

        return false;
    }

    public static boolean isValidDate(String strDate, String dateFormat) {
        try {
            TemporalAccessor ta = DateTimeFormatter.ofPattern(dateFormat).parse(strDate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean containsAllSpaces(String value) {
        return value.trim().length()==0;
    }

    public static boolean isStringContainsWhiteSpace(String value) {
        return value.contains(" ");
    }

    public static Timestamp getTimeStampFromString(String strDate, String dateFormat) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime objPurchaseDate = LocalDateTime.parse(strDate, dtf);
        return Timestamp.valueOf(objPurchaseDate);
    }


    public static String toJson(Object object) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String strJson = gson.toJson(object);
        return strJson;
    }

    public static String maskedCardNumber(String cardNumber) {
        int totalLength = cardNumber.length();
        String lastFourDigit = cardNumber.substring(totalLength - 4);
        String firstSixDigit = cardNumber.substring(0, 6);

        Integer len = totalLength - lastFourDigit.length() - firstSixDigit.length();
        String maskedDigits = PADDED_SYMBOL_X.repeat(len);

        return  firstSixDigit + maskedDigits + lastFourDigit;
    }


}
