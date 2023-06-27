package com.razorpay.threeds.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

import static com.razorpay.threeds.constant.InternalConstants.PADDED_SYMBOL_X;
import static com.razorpay.threeds.constant.InternalConstants.PAD_LEFT;

@Slf4j
public class Util {

  public static boolean isNull(Object object) {
    return object == null;
  }

  public static boolean isNotNull(Object object) {
    return object != null;
  }

  public static boolean isNullorBlank(Object object) {
    if (isNull(object)) {
      return true;
    } else return "".equals(object.toString());
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
    return value.trim().length() == 0;
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

  public static String extendString(String text, String symbol, Integer len, String padOn) {
    if (PAD_LEFT.equals(padOn)) {
      StringBuilder stringBuilder = new StringBuilder();
      int paddingCharLength = len - text.length();
      stringBuilder.append(String.valueOf(symbol).repeat(Math.max(0, paddingCharLength)));
      stringBuilder.append(text);
      text = stringBuilder.toString();
    } else {
      StringBuilder textBuilder = new StringBuilder(text);
      while (textBuilder.length() < len) {
        textBuilder.append(symbol);
      }
      text = textBuilder.toString();
    }

    return text;
  }

  public static String getHashValue(String hashData) {
    StringBuilder hexString = null;
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(hashData.getBytes(StandardCharsets.UTF_8));
      hexString = new StringBuilder();

      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
    } catch (Exception e) {
      log.error(
          "getHashValue() Error Occurred while generating hash value for data: {}", hashData, e);
    }
    return hexString != null ? hexString.toString() : null;
  }

  public static String get16DigitNumericValue(String hashedValue) {

    String sixteenDigitNumericValue = null;
    StringBuilder stringBuilder;
    try {
      stringBuilder = new StringBuilder();
      Pattern p = Pattern.compile("[0-9]");
      Matcher m = p.matcher(hashedValue);
      while (m.find()) {
        stringBuilder.append(m.group());
      }
      sixteenDigitNumericValue = stringBuilder.substring(0, 16);
    } catch (Exception e) {
      log.error(
          "getHashValue() Error Occurred while converting 16 digit value for data: {}",
          hashedValue,
          e);
    }
    return sixteenDigitNumericValue;
  }

  public static long generateRandomNumber() {
    Random random = new Random();
    long min = 1000L; // Minimum value (inclusive)
    long max = 9999L; // Maximum value (inclusive)
    return min + (long) (random.nextDouble() * (max - min + 1));
  }
}
