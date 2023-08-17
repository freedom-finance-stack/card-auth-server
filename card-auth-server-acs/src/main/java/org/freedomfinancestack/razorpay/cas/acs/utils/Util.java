package org.freedomfinancestack.razorpay.cas.acs.utils;

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

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * The {@code Util} class provides various utility methods used across the ACS (Access Control
 * Server) module.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author ankitchoudhary2209, jaydeepRadadiya
 */
@Slf4j
public class Util {
    public static final String PADDED_SYMBOL_X = "X";
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

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

    /**
     * Converts the string date in the specified date format to a {@link Timestamp} object.
     *
     * @param strDate the string date to be converted
     * @param dateFormat the date format of the input string date
     * @return the {@link Timestamp} object representing the converted date
     * @throws ParseException if the input string date cannot be parsed
     */
    public static Timestamp getTimeStampFromString(String strDate, String dateFormat)
            throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        Date parsedDate = fmt.parse(strDate);
        return new java.sql.Timestamp(parsedDate.getTime());
    }

    /**
     * Converts an object to its JSON representation using Gson library.
     *
     * @param object the object to be converted to JSON
     * @return the JSON representation of the object
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
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
     * Masks the card number by replacing all but the first six and last four digits with 'X'.
     *
     * @param cardNumber the card number to be masked
     * @return the masked card number
     */
    public static String maskedCardNumber(String cardNumber) {
        int totalLength = cardNumber.length();
        String lastFourDigit = cardNumber.substring(totalLength - 4);
        String firstSixDigit = cardNumber.substring(0, 6);

        int len = totalLength - lastFourDigit.length() - firstSixDigit.length();
        String maskedDigits = PADDED_SYMBOL_X.repeat(len);

        return firstSixDigit + maskedDigits + lastFourDigit;
    }

    /**
     * Pads the input string with the specified padding character to achieve the desired length.
     *
     * @param input the input string to be padded
     * @param desiredLength the desired length of the padded string
     * @param paddingChar the padding character to be used
     * @param padOn the side on which padding should be applied ("left" or "right")
     * @return the padded string
     */
    public static String padString(
            String input, int desiredLength, char paddingChar, String padOn) {
        if (input.length() >= desiredLength) {
            return input;
        }

        StringBuilder paddedString = new StringBuilder(input);
        while (paddedString.length() < desiredLength) {
            if (InternalConstants.PAD_LEFT.equals(padOn)) {
                paddedString.insert(0, paddingChar);
            } else if (InternalConstants.PAD_RIGHT.equals(padOn)) {
                paddedString.append(paddingChar);
            } else {
                break;
            }
        }
        return paddedString.toString();
    }

    /**
     * Generates the SHA-256 hash value of the input data.
     *
     * @param hashData the input data to be hashed
     * @return the SHA-256 hash value as a hexadecimal string
     */
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

    /**
     * Extracts the first 16-digit numeric value from the input string.
     *
     * @param hashedValue the input string containing the hashed value
     * @return the first 16-digit numeric value as a string
     */
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

    /**
     * Generates a random number with the specified number of digits.
     *
     * @param digits the number of digits in the random number
     * @return the generated random number
     */
    public static int generateRandomNumber(int digits) {
        Random random = new Random();
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;
        return random.nextInt(max - min + 1) + min;
    }
}