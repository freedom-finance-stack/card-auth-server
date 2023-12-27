package org.freedomfinancestack.razorpay.cas.acs.utils;

import java.text.ParseException;
import java.util.UUID;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailDto;
import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailsRequest;
import org.junit.jupiter.api.Test;

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
}
