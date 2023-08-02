package org.freedomfinancestack.extensions.hsm.luna.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HexUtil {

    public static String hexValue(byte[] b, int offset, int length) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char hi = Character.forDigit(b[(offset + i)] >> 4 & 0xF, 16);
            char lo = Character.forDigit(b[(offset + i)] & 0xF, 16);
            hex.append(Character.toUpperCase(hi));
            hex.append(Character.toUpperCase(lo));
        }
        return hex.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] byteArray = new byte[s.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            byteArray[i] = ((byte) v);
        }

        return byteArray;
    }

    public static byte[] intToByteArray(int len) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = ((byte) (len >> offset & 0xFF));
        }
        return b;
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static int byteToInt(byte[] b) {
        int val = 0;
        int i = b.length - 1;
        for (int j = 0; i >= 0; j++) {
            val += (b[i] & 0xff) << 8 * j;
            i--;
        }
        return val;
    }

    public static String hexToAscii(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n / 2);
        for (int i = 0; i < n; i += 2) {
            char a = s.charAt(i);
            char b = s.charAt(i + 1);

            if ((hexToInt(a) << 4 | hexToInt(b)) > 128) {
                int j = Integer.parseInt(s);
                sb.append((char) j);
            } else {
                sb.append((char) (hexToInt(a) << 4 | hexToInt(b)));
            }
        }
        return sb.toString();
    }

    private static int hexToInt(char ch) {
        if (('a' <= ch) && (ch <= 'f')) return ch - 'a' + 10;
        if (('A' <= ch) && (ch <= 'F')) return ch - 'A' + 10;
        if (('0' <= ch) && (ch <= '9')) return ch - '0';
        throw new IllegalArgumentException(String.valueOf(ch));
    }

    public static int hex2Decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }
}
