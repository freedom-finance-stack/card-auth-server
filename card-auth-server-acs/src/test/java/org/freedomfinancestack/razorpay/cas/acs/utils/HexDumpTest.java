package org.freedomfinancestack.razorpay.cas.acs.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HexDumpTest {
    @Test
    public void test_length_twice_input_length() {
        byte[] input = {0x12, 0x34, 0x56};
        String expected = "0000:12 34 56               \u00124V             \n";
        String result = HexDump.byteArrayToHexString(input);
        assertEquals(expected, result);
    }

    @Test
    public void test_padleft_longer_string() {
        try {
            HexDump.padleft("abcdef", 4, '0');
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("invalid len 6/4", e.getMessage());
        }

        try {
            HexDump.padleft("abcdef", 4, ' ');
            fail("Exception not thrown");
        } catch (Exception e) {
            assertEquals("invalid len 6/4", e.getMessage());
        }
    }

    @Test
    void byteArrayToHexString_inputIsEmpty() {
        byte[] input = {};
        assertNull(HexDump.byteArrayToHexString(input));
    }

    @Test
    void byteArrayToHexString_InputIsnull() {
        byte[] input = null;
        assertNull(HexDump.byteArrayToHexString(input));
    }

    @Test
    public void getHexDump() {
        byte[] byteArray = new byte[18];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) i;
        }
        String expected =
                "0000: 00 01 02 03  04 05 06 07  08 09 0A 0B  0C 0D 0E 0F  | ................ |\n"
                    + "0010: 10 11                                               | ..              "
                    + " |\n";

        String result = HexDump.getHexDump(byteArray);
        assertEquals(expected, result);
    }

    @Test
    public void getHexDump_NullPointerException() {
        byte[] byteArray = null;
        String expected = "";
        String result = HexDump.getHexDump(byteArray);
        assertEquals(expected, result);
    }

    @Test
    public void byteArrayToHex() {
        byte[] input = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 20, 30, 40, 50, 60, 70, 80, 90
        };
        String expected =
                "0000 01 02 03 04 05 06 07 08  09 0A 0B 0C 0D 0E 14 1E  ................\n"
                        + "0010 28 32 3C 46 50 5A                                 (2<FPZ\n";
        String result = HexDump.byteArrayToHex(input);
        assertEquals(expected, result);
    }

    @Test
    public void writeASCII_Exception_indexOutOfBound() {
        byte[] in = {1, 2};
        assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> HexDump.WriteAscii(in, -5, new StringBuffer()));
    }

    @Test
    public void writeASCII_Exception_buffer_null() {
        byte[] in = {1, 2};
        assertThrows(NullPointerException.class, () -> HexDump.WriteAscii(in, 1, null));
    }
}
