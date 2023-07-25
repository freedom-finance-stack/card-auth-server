package com.razorpay.ffs.cas.acs.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

import static com.razorpay.ffs.cas.acs.constant.InternalConstants.PAD_LEFT;
import static com.razorpay.ffs.cas.acs.constant.InternalConstants.PAD_RIGHT;

@Slf4j
public class Util {
    public static final String PADDED_SYMBOL_X = "X";

    public static boolean isNullorBlank(Object object) {
        if (null == object) {
            return true;
        } else return "".equals(object.toString());
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

    public static String padString(
            String input, int desiredLength, char paddingChar, String padOn) {
        if (input.length() >= desiredLength) {
            return input;
        }

        StringBuilder paddedString = new StringBuilder(input);
        while (paddedString.length() < desiredLength) {
            if (PAD_LEFT.equals(padOn)) {
                paddedString.insert(0, paddingChar);
            } else if (PAD_RIGHT.equals(padOn)) {
                paddedString.append(paddingChar);
            } else {
                break;
            }
        }
        return paddedString.toString();
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
                    "getHashValue() Error Occurred while generating hash value for data: {}",
                    hashData,
                    e);
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

    public static int generateRandomNumber(int digits) {
        Random random = new Random();
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;
        return random.nextInt(max - min + 1) + min;
    }
}
