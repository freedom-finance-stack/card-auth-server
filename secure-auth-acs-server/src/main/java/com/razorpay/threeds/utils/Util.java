package com.razorpay.threeds.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Util {
  public static final String PADDED_SYMBOL_X = "X";

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

  public static Timestamp getTimeStampFromString(String strDate, String dateFormat)
      throws ParseException {
    SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
    Date parsedDate = fmt.parse(strDate);
    return new java.sql.Timestamp(parsedDate.getTime());
  }

  public static String toJson(Object object) {
    // todo create GSON bean
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    String strJson = gson.toJson(object);
    return strJson;
  }

  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }

  public static String maskedCardNumber(String cardNumber) {
    int totalLength = cardNumber.length();
    String lastFourDigit = cardNumber.substring(totalLength - 4);
    String firstSixDigit = cardNumber.substring(0, 6);

    Integer len = totalLength - lastFourDigit.length() - firstSixDigit.length();
    String maskedDigits = PADDED_SYMBOL_X.repeat(len);

    return firstSixDigit + maskedDigits + lastFourDigit;
  }
}
