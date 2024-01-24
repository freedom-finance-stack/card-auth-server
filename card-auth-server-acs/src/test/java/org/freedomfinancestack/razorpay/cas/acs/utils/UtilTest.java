package org.freedomfinancestack.razorpay.cas.acs.utils;

import java.text.ParseException;
import java.util.*;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.freedomfinancestack.razorpay.cas.acs.exception.acs.ImageProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    public void testIsNullOrBlankTrue() {
        // write junit test cases for isNullorBlank method
        assertTrue(Util.isNullorBlank(null));
        assertTrue(Util.isNullorBlank(""));
    }

    @Test
    public void testIsNullOrBlankFalse() {
        assertFalse(Util.isNullorBlank("test"));
        assertFalse(Util.isNullorBlank(new CardDetailDto()));
    }

    @Test
    public void testGenerateUUID() {
        assertTrue(Util.generateUUID().length() > 0);
    }

    @Test
    public void testGetTimeStampFromString() throws Exception {
        assertEquals(
                "2023-12-12 12:01:01.0",
                Util.getTimeStampFromString("20231212120101", "yyyyMMddHHmmss").toString());
        assertEquals(
                "2012-12-12 00:00:00.0",
                Util.getTimeStampFromString("12.12.2012", "dd.MM.yyyy").toString());
    }

    @Test
    public void testGetTimeStampFromStringFail() throws Exception {
        assertThrows(
                ParseException.class,
                () -> Util.getTimeStampFromString("12122023", "yyyyMMddHHmmss"));
        assertThrows(
                ParseException.class, () -> Util.getTimeStampFromString("121223", "dd.MM.yyyy"));
    }

    @Test
    public void testFromJson_Success() {
        String json = "{\"institutionId\": \"I1\", \"cardNumber\": \"112444\"}";
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("I1", "112444");
        CardDetailsRequest result = Util.fromJson(json, CardDetailsRequest.class);
        assertEquals(cardDetailsRequest.getInstitutionId(), result.getInstitutionId());
        assertEquals(cardDetailsRequest.getCardNumber(), result.getCardNumber());
    }

    @Test
    public void testFromJson_EmptyString() {
        String json = "";
        CardDetailsRequest result = Util.fromJson(json, CardDetailsRequest.class);
        assertNull(result);
    }

    @Test
    public void testFromJson_NullString() {
        String json = null;
        CardDetailsRequest result = Util.fromJson(json, CardDetailsRequest.class);
        assertNull(result);
    }

    @Test
    public void testFromJson_EmptyClass() {
        String json = "{}";
        CardDetailsRequest result = Util.fromJson(json, CardDetailsRequest.class);
        assertNotNull(result);
        assertNull(result.getInstitutionId());
    }

    @Test
    void toJson_shouldReturnJsonRepresentationOfObject() {
        // Arrange
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("I1", "112444");

        // Act
        String json = Util.toJson(cardDetailsRequest);

        // Assert
        assertEquals("{\"institutionId\":\"I1\",\"cardNumber\":\"112444\"}", json);
    }

    @Test
    void generateUUID_shouldReturnRandomUUIDString() {
        // Act
        String uuid = Util.generateUUID();

        // Assert
        assertNotNull(uuid);
        try {
            UUID.fromString(uuid); // Check if it's a valid UUID
        } catch (IllegalArgumentException e) {
            fail("Not a valid UUID");
        }
    }

    @Test
    void maskedCardNumber_shouldReturnMaskedCardNumber() {
        // Arrange
        String cardNumber = "1234567890123456";

        // Act
        String maskedCard = Util.maskedCardNumber(cardNumber);

        // Assert
        assertNotNull(maskedCard);
        assertEquals("123456XXXXXX3456", maskedCard);
    }

    @Test
    void maskedCardNumber_emptyString() {
        // Arrange
        String cardNumber = "";

        // Act
        String maskedCard = Util.maskedCardNumber(cardNumber);

        // Assert
        assertNotNull(maskedCard);
        assertEquals("", maskedCard);
    }

    @Test
    void maskedCardNumber_lessthan10Digits() {
        // Arrange
        String cardNumber = "111111111";

        // Act
        String maskedCard = Util.maskedCardNumber(cardNumber);

        // Assert
        assertNotNull(maskedCard);
        assertEquals("111111111", maskedCard);
    }

    @Test
    void decodeBase64_shouldReturnDecodedString() {
        // Arrange
        String base64Data = "SGVsbG8gd29ybGQh"; // "Hello world!" in base64

        // Act
        String decodedString = Util.decodeBase64(base64Data);

        // Assert
        assertEquals("Hello world!", decodedString);
    }

    @Test
    void decodeBase64_emptyString() {
        // Arrange
        String base64Data = ""; // "Hello world!" in base64

        // Act
        String decodedString = Util.decodeBase64(base64Data);

        // Assert
        assertEquals("", decodedString);
    }

    @Test
    void decodeBase64_nullString() {
        // Arrange
        String base64Data = null;

        // Act
        String decodedString = Util.decodeBase64(base64Data);

        // Assert
        assertNull(decodedString);
    }

    @Test
    void encodeBase64_shouldReturnEncodedString() {
        // Arrange
        String data = "";
        // Act
        String encodedString = Util.encodeBase64(data);

        // Assert
        assertEquals("", encodedString); // "Hello world!" in base64
    }

    @Test
    void encodeBase64_shouldReturnNullEncodedString() {
        // Arrange
        String data = null;
        // Act
        String encodedString = Util.encodeBase64(data);

        // Assert
        assertEquals(null, encodedString); // "Hello world!" in base64
    }

    @Test
    void encodeBase64UrlObject_shouldReturnEncodedString() {
        // Arrange
        CardDetailsRequest cardDetailsRequest = new CardDetailsRequest("I1", "112444");
        // Act
        String encodedString = Util.encodeBase64Url(cardDetailsRequest);

        // Assert
        assertEquals(
                "eyJpbnN0aXR1dGlvbklkIjoiSTEiLCJjYXJkTnVtYmVyIjoiMTEyNDQ0In0",
                encodedString); // Replace with the expected base64 encoding of your JSON
    }

    @Test
    void encodeBase64UrlObject_shouldReturnNUllString() {
        // Act
        String encodedString = Util.encodeBase64Url(null);
        // Assert
        assertEquals(null, encodedString);
    }

    // Returns the input string if its length is greater than or equal to the desired length.
    @Test
    public void getHashValuetest_returns_input_string_if_length_greater_than_desired_length() {
        String input = "abcdef";
        int desiredLength = 4;
        char paddingChar = 'X';
        String padOn = "LEFT";

        String result = Util.padString(input, desiredLength, paddingChar, padOn);

        assertEquals(input, result);
    }

    //    @Test
    @ParameterizedTest
    @CsvSource({"LEFT", "RIGHT"})
    public void
            getHashValuetest_pads_input_string_with_padding_character_on_left_if_padOn_is_PAD_LEFT_and_input_length_less_than_desired_length(
                    String padOn) {
        String input = "abc";
        int desiredLength = 6;
        char paddingChar = 'X';

        String result = Util.padString(input, desiredLength, paddingChar, padOn);

        if (padOn.equals("LEFT")) {
            assertEquals("XXXabc", result);
        } else {
            assertEquals("abcXXX", result);
        }
    }

    @Test
    public void getHashValue_test_returns_hash_value() {
        String hashData = "test";
        String expectedHashValue =
                "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";

        String actualHashValue = Util.getHashValue(hashData);

        assertEquals(expectedHashValue, actualHashValue);
    }

    @Test
    public void getHashValue_null() {
        String hashData = null;
        String expectedHashValue = null;

        String actualHashValue = Util.getHashValue(hashData);
        assertEquals(expectedHashValue, actualHashValue);
    }

    @Test
    public void get16DigitNumericValue_Success() {
        String hashedValue = "abc123def456ghi789123abc456abc789";
        String expected = "1234567891234567";
        String actual = Util.get16DigitNumericValue(hashedValue);
        assertEquals(expected, actual);
    }

    @Test
    public void get16DigitNumericValue_Exception() {
        String hashedValue = "abc123def456ghi789";
        String actual = Util.get16DigitNumericValue(hashedValue);
        assertNull(Util.get16DigitNumericValue(hashedValue));
    }

    @Test
    public void get16DigitNumericValue_Exception_outofRegexPattern() {
        String hashedValue = "abc123def456ghi789!!";
        String actual = Util.get16DigitNumericValue(hashedValue);
        assertNull(Util.get16DigitNumericValue(hashedValue));
    }

    @Test
    public void get16DigitNumericValue_Exception_noNumeric() {
        String hashedValue = "abcddefghijkdl";
        String actual = Util.get16DigitNumericValue(hashedValue);
        assertNull(Util.get16DigitNumericValue(hashedValue));
    }

    @ParameterizedTest
    @ValueSource(strings = {"SGVsbG8gd29ybGQ", "SGVsbG8gd29ybGQ=", "SGVsbG8gd29ybGQ===", ""})
    public void isValidBase64Url_without_padding(String input) {
        boolean result = Util.isValidBase64Url(input);
        assertTrue(result);
    }

    @Test
    public void isValidBase64Url_null_input() {
        String input = null;
        boolean result = Util.isValidBase64Url(input);
        assertFalse(result);
    }

    @Test
    public void isValidBase64Url_invalid_characters() {
        String input = "SGVsbG8gd29ybGQ!";
        boolean result = Util.isValidBase64Url(input);
        assertFalse(result);
    }

    @Test
    public void generates_random_number_with_specified_digits() {
        int result = Util.generateRandomNumber(5);
        assertTrue(result >= 10000 && result <= 99999);
    }

    // Returns the input string without padding if it is not null and contains padding characters at
    // the end
    @Test
    public void removeBase64Padding_withPaddingAtEnd() {
        String base64String = "SGVsbG8gd29ybGQ=";
        String expected = "SGVsbG8gd29ybGQ";

        String result = Util.removeBase64Padding(base64String);

        assertEquals(expected, result);
    }

    // Returns null if the input string is null
    @Test
    public void removeBase64Padding_withNullInput() {
        String base64String = null;

        String result = Util.removeBase64Padding(base64String);

        assertNull(result);
    }

    // Returns the input string without padding if it does not contain padding characters at the end
    @Test
    public void removeBase64Padding_withoutPaddingAtEnd() {
        String base64String = "SGVsbG8gd29ybGQ";
        String expected = "SGVsbG8gd29ybGQ";

        String result = Util.removeBase64Padding(base64String);

        assertEquals(expected, result);
    }

    // Returns an empty string if the input string is an empty string
    @Test
    public void removeBase64Padding_withEmptyString() {
        String base64String = "";
        String expected = "";

        String result = Util.removeBase64Padding(base64String);

        assertEquals(expected, result);
    }

    // Returns an empty string if the input string contains only padding characters
    @Test
    public void removeBase64Padding_withOnlyPaddingCharacters() {
        String base64String = "==";
        String expected = "";

        String result = Util.removeBase64Padding(base64String);

        assertEquals(expected, result);
    }

    // Returns an empty string if the input string contains padding characters at the beginning
    @Test
    public void removeBase64Padding_withPaddingAtBeginning() {
        String base64String = "==SGVsbG8gd29ybGQ";
        String expected = "==SGVsbG8gd29ybGQ";

        String result = Util.removeBase64Padding(base64String);

        assertEquals(expected, result);
    }

    @Test
    public void removeBase64Padding_null() {
        String base64String = null;

        String result = Util.removeBase64Padding(base64String);

        assertNull(result);
    }

    @Test
    public void concatenated_string_with_key_and_id() {
        String key = "key";
        String id = "id";
        String expectedResult = "key[id]";

        String result = Util.generateTaskIdentifier(key, id);

        assertEquals(expectedResult, result);
    }

    @Test
    public void firstCommonString() {
        List<String> str1 = Arrays.asList("apple", "banana", "orange");
        List<String> str2 = Arrays.asList("banana", "grape", "kiwi");
        String result = Util.findFirstCommonString(str1, str2);
        assertEquals("banana", result);
    }

    @Test
    public void findFirstCommonString_ReturnsNull() {
        List<String> str1 = Arrays.asList("apple", "banana", "orange");
        List<String> str2 = Arrays.asList("grape", "kiwi", "mango");
        String result = Util.findFirstCommonString(str1, str2);
        assertNull(result);
    }

    @Test
    public void formatAmount_success() {
        String result = Util.formatAmount("123456", "2");
        assertEquals("1234.56", result);
    }

    @Test
    public void formatAmount_EdgeCase() {
        String result = Util.formatAmount("1", "2");
        assertEquals("0.01", result);
    }

    @Test
    public void getLastFourDigit() {
        String input = "1234567890";
        String expectedOutput = "7890";

        String actualOutput = Util.getLastFourDigit(input);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void getLastFourDigit_Blank() {
        String input = "";
        String expectedOutput = "XXXX";

        String actualOutput = Util.getLastFourDigit(input);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void getLastFourDigit_Exception_OutofBoundException() {
        String input = "he";
        assertThrows(StringIndexOutOfBoundsException.class, () -> Util.getLastFourDigit(input));
    }

    @ParameterizedTest
    @CsvSource({"ABC", "_.-", "abc123_.-", "ABC123_.-", "abc123_.-"})
    public void validateIEFTRFC7571Base64UrlEncodedStringPattern(String encodedString) {
        boolean result = Util.validateIEFTRFC7571Base64UrlEncodedStringPattern(encodedString);
        assertTrue(result);
    }

    @Test
    public void validateIEFTRFC7571Base64UrlEncodedStringPattern_Nullcase() {
        assertThrows(
                NullPointerException.class,
                () -> Util.validateIEFTRFC7571Base64UrlEncodedStringPattern(null));
    }

    @Test
    public void getBase64Image() throws ImageProcessingException {
        // Arrange
        String imgUrl = "http://tinyurl.com/photo5566";
        // Act
        String base64Image = Util.getBase64Image(imgUrl);
        // Assert
        assertNotNull(base64Image);
        assertEquals(
                "PCFET0NUWVBFIGh0bWw+CjxodG1sPgogICAgPGhlYWQ+CiAgICAgICAgPG1ldGEgY2hhcnNldD0iVVRGLTgiIC8+CiAgICAgICAgPG1ldGEgaHR0cC1lcXVpdj0icmVmcmVzaCIgY29udGVudD0iMDt1cmw9J2h0dHBzOi8vd3d3Lmdvb2dsZS5jb20vdXJsP3NhPWkmYW1wO3VybD1odHRwcyUzQSUyRiUyRnd3dy5wZXhlbHMuY29tJTJGc2VhcmNoJTJGYmVhdXRpZnVsJTJGJmFtcDtwc2lnPUFPdlZhdzBsZjRKb0RLUnVkQkVZRWFmT29QMFUmYW1wO3VzdD0xNzA1NzI4Mjg1MTY4MDAwJmFtcDtzb3VyY2U9aW1hZ2VzJmFtcDtjZD12ZmUmYW1wO3ZlZD0wQ0JNUWpSeHFGd29UQ01pcDdKWGI2SU1ERlFBQUFBQWRBQUFBQUJBRSciIC8+CgogICAgICAgIDx0aXRsZT5SZWRpcmVjdGluZyB0byBodHRwczovL3d3dy5nb29nbGUuY29tL3VybD9zYT1pJmFtcDt1cmw9aHR0cHMlM0ElMkYlMkZ3d3cucGV4ZWxzLmNvbSUyRnNlYXJjaCUyRmJlYXV0aWZ1bCUyRiZhbXA7cHNpZz1BT3ZWYXcwbGY0Sm9ES1J1ZEJFWUVhZk9vUDBVJmFtcDt1c3Q9MTcwNTcyODI4NTE2ODAwMCZhbXA7c291cmNlPWltYWdlcyZhbXA7Y2Q9dmZlJmFtcDt2ZWQ9MENCTVFqUnhxRndvVENNaXA3SlhiNklNREZRQUFBQUFkQUFBQUFCQUU8L3RpdGxlPgogICAgPC9oZWFkPgogICAgPGJvZHk+CiAgICAgICAgUmVkaXJlY3RpbmcgdG8gPGEgaHJlZj0iaHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS91cmw/c2E9aSZhbXA7dXJsPWh0dHBzJTNBJTJGJTJGd3d3LnBleGVscy5jb20lMkZzZWFyY2glMkZiZWF1dGlmdWwlMkYmYW1wO3BzaWc9QU92VmF3MGxmNEpvREtSdWRCRVlFYWZPb1AwVSZhbXA7dXN0PTE3MDU3MjgyODUxNjgwMDAmYW1wO3NvdXJjZT1pbWFnZXMmYW1wO2NkPXZmZSZhbXA7dmVkPTBDQk1RalJ4cUZ3b1RDTWlwN0pYYjZJTURGUUFBQUFBZEFBQUFBQkFFIj5odHRwczovL3d3dy5nb29nbGUuY29tL3VybD9zYT1pJmFtcDt1cmw9aHR0cHMlM0ElMkYlMkZ3d3cucGV4ZWxzLmNvbSUyRnNlYXJjaCUyRmJlYXV0aWZ1bCUyRiZhbXA7cHNpZz1BT3ZWYXcwbGY0Sm9ES1J1ZEJFWUVhZk9vUDBVJmFtcDt1c3Q9MTcwNTcyODI4NTE2ODAwMCZhbXA7c291cmNlPWltYWdlcyZhbXA7Y2Q9dmZlJmFtcDt2ZWQ9MENCTVFqUnhxRndvVENNaXA3SlhiNklNREZRQUFBQUFkQUFBQUFCQUU8L2E+LgogICAgPC9ib2R5Pgo8L2h0bWw+",
                base64Image);
    }

    @Test
    void getBase64Image_Exception() throws ImageProcessingException {
        // Arrange
        String imgUrl = "https://example.com/image.jpg";

        // Act and Assert
        ImageProcessingException imageProcessingException =
                assertThrows(ImageProcessingException.class, () -> Util.getBase64Image(imgUrl));
        assertEquals(
                "Error occurred while processing image", imageProcessingException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"840,USD", "978, EUR"})
    public void getCurrencyInstance_Success(String numericCode, String currencyCode) {
        Currency expectedCurrency = Currency.getInstance(currencyCode);
        Currency actualCurrency = Util.getCurrencyInstance(numericCode);
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void getCurrencyInstance_Exception() {
        String numericCode = "123";
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Util.getCurrencyInstance(numericCode);
                });
    }
}
