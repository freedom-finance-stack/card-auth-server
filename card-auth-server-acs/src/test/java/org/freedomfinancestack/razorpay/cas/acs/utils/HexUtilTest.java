package org.freedomfinancestack.razorpay.cas.acs.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class HexUtilTest {

    @Test
    public void byteArrayToHexString() {
        byte[] input = {10, 20, 30, 40};
        String expected = "0A141E28";
        String actual = HexUtil.byteArrayToHexString(input);
        assertEquals(expected, actual);
    }

    @Test
    public void hexStringToByteArray() {
        // Arrange
        String hexString = "48656C6C6F20576F726C64";
        byte[] expectedByteArray = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};

        // Act
        byte[] actualByteArray = HexUtil.hexStringToByteArray(hexString);

        // Assert
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void intToByteArray() {
        int input = 12345;
        byte[] result = HexUtil.intToByteArray(input);
        assertEquals(2, result.length);
    }

    @Test
    public void getStackTrace() {
        Throwable throwable = new Throwable("Test Exception");
        String stackTrace = HexUtil.getStackTrace(throwable);
        assertNotNull(stackTrace);
        assertTrue(stackTrace.contains("Test Exception"));
    }

    @Test
    public void byteToInt() {
        byte[] b = new byte[] {(byte) 0xAB};
        int expected = 171;
        int actual = HexUtil.byteToInt(b);
        assertEquals(expected, actual);
    }

    @Test
    public void hexToAscii_Exception() {
        String input = "   48656C6C6F20576F726C64";
        assertThrows(IllegalArgumentException.class, () -> HexUtil.hexToAscii(input));
    }

    @Test
    public void hexToAscii_Success() {
        // Arrange
        String input =
                "5468697320697320616E206578616D706C6520737472696E672077697468207772617070696E672063686172616374657273";
        String expectedOutput = "This is an example string with wrapping characters";

        // Act
        String actualOutput = HexUtil.hexToAscii(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @ParameterizedTest
    @CsvSource({"A,10", "a,10", "FF,255"})
    public void hex2Decimal(String input, int output) {
        int result = HexUtil.hex2Decimal(input);
        assertEquals((output), result);
    }
}
