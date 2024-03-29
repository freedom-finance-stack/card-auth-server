package org.freedomfinancestack.razorpay.cas.acs.utils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.freedomfinancestack.razorpay.cas.acs.constant.InternalConstants;
import org.freedomfinancestack.razorpay.cas.acs.exception.InternalErrorCode;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ImageProcessingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;

/**
 * The {@code Util} class provides various utility methods used across the ACS (Access Control
 * Server) module.
 *
 * @author ankitchoudhary2209, jaydeepRadadiya
 * @version 1.0.0
 * @since 1.0.0
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
     * Converts an Json string to given classs using Gson library.
     *
     * @param json the json string to be converted to Object
     * @param classOfT the class of T
     * @return Object of given class
     */
    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
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
        if (cardNumber == null || cardNumber.length() < 10) {
            // Handle invalid or too short card numbers
            return cardNumber;
        }
        int totalLength = cardNumber.length();
        String lastFourDigit = cardNumber.substring(totalLength - 4);
        String firstSixDigit = cardNumber.substring(0, 6);

        int len = totalLength - lastFourDigit.length() - firstSixDigit.length();
        String maskedDigits = PADDED_SYMBOL_X.repeat(len);

        return firstSixDigit + maskedDigits + lastFourDigit;
    }

    /**
     * Decode base64 data to string.
     *
     * @param base64Data string data
     * @return decoded string
     */
    public static String decodeBase64(String base64Data) throws IllegalArgumentException {
        if (base64Data == null) {
            return null;
        }
        return new String(Base64.getDecoder().decode(base64Data), StandardCharsets.UTF_8);
    }

    /**
     * Encode base64 data to string.
     *
     * @param data String data
     * @return encoded string
     */
    public static String encodeBase64(String data) {
        if (data == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Creates URL safe base64 encoded string without padding from object.
     *
     * @param data Object data
     * @return encoded string
     */
    public static String encodeBase64Url(Object data) {
        if (data == null) {
            return null;
        }
        String input = Util.toJson(data);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(input.getBytes(StandardCharsets.UTF_8));
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
     * to check string is base64 Url encoded or not, works with both padded and non-padded strings
     *
     * @param input input string. which need to check for validity
     * @return boolean set to true for valid base64url encoding
     */
    public static boolean isValidBase64Url(String input) {
        try {
            input = input.replace("=", "");
            // Decode the input to check if it's a valid Base64 encoding
            byte[] decodedBytes = Base64.getUrlDecoder().decode(input);
            // Encode the decoded bytes again and compare with the original input
            String reencoded = Base64.getUrlEncoder().withoutPadding().encodeToString(decodedBytes);
            // If the re-encoded string matches the original input, it's a valid Base64 URL encoding
            return input.equals(reencoded);
        } catch (Exception e) {
            // If decoding fails, it's not a valid Base64 encoding
            return false;
        }
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

    public static String removeBase64Padding(String base64String) {
        if (base64String != null) {
            return base64String.replaceAll("=+$", "");
        }
        return null;
    }

    public static String generateTaskIdentifier(String key, String id) {
        return key + "[" + id + "]";
    }

    public static String findFirstCommonString(List<String> str1, List<String> str2) {

        // Add all strings from str1 to the set
        Set<String> set = new HashSet<>(str1);

        // Iterate through str2 and check if any string is already in the set
        for (String s : str2) {
            if (set.contains(s)) {
                return s; // Return the first common string
            }
        }

        return null; // Return null if no common string is found
    }

    public static String formatAmount(String amount, String exponent) {
        String fractionAmount = "";
        int exponentNumber = 0;
        if (exponent != null) {
            exponentNumber = Integer.parseInt(exponent);
            int count;
            if (exponentNumber > 0) {
                if (amount.length() <= exponentNumber) {
                    count = exponentNumber - amount.length();
                    while (count >= 0) {
                        amount = InternalConstants.PADDED_SYMBOL_0 + amount;
                        count--;
                    }
                }
                fractionAmount = amount.substring(amount.length() - exponentNumber);
            } else {
                fractionAmount =
                        String.valueOf(InternalConstants.PADDED_SYMBOL_0)
                                + InternalConstants.PADDED_SYMBOL_0;
            }
        }
        return amount.substring(0, amount.length() - exponentNumber) + "." + fractionAmount;
    }

    public static String getLastFourDigit(String data) {
        if (!isNullorBlank(data)) {
            data = data.substring((data.length() - 4), data.length());
            return data;
        } else {
            return InternalConstants.LAST_FOUR_DIGIT_PLACEHOLDER;
        }
    }

    public static boolean validateIEFTRFC7571Base64UrlEncodedStringPattern(String encodedString) {
        String base64urlRegex = "^[A-Za-z0-9_.-]*$";
        Pattern base64urlPattern = Pattern.compile(base64urlRegex);

        Matcher base64urlMatcher = base64urlPattern.matcher(encodedString);

        return base64urlMatcher.matches();
    }

    public static Currency getCurrencyInstance(String numericCode) {
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        for (Currency currency : currencies) {
            if (currency.getNumericCodeAsString().equals(numericCode)) {
                return currency;
            }
        }
        throw new IllegalArgumentException(
                "Currency with numeric code " + numericCode + " not found");
    }

    public static String getBase64Image(String imgUrl) throws ImageProcessingException {
        try {
            return Base64.getEncoder().encodeToString(new URL(imgUrl).openStream().readAllBytes());
        } catch (IOException ex) {
            log.error("Error occurred while processing image: ", ex);
            throw new ImageProcessingException(
                    InternalErrorCode.IMAGE_PARSING_ERROR,
                    "Error occurred while processing image",
                    ex);
        }
    }
}
