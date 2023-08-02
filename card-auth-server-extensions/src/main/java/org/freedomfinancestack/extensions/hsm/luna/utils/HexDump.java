package org.freedomfinancestack.extensions.hsm.luna.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

public class HexDump {

    private static String[] pseudo = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
    };

    public static String byteArrayToHexString(byte[] in) {
        int oldIndex = 0;
        int newIndex = 0;
        if ((in == null) || (in.length <= 0)) {
            return null;
        }
        StringBuffer out = new StringBuffer(in.length * 2);
        String lFormatString = new String("%1$04x:");
        String lPadding = new String(" ");
        while (oldIndex + 1 <= in.length) {
            out.append(String.format(lFormatString, new Object[] {new Integer(oldIndex)}));
            newIndex = WriteHex(in, oldIndex, out);
            out.append(lPadding);
            newIndex = WriteAscii(in, oldIndex, out);
            oldIndex += newIndex;
            out.append(String.format("%1$c", new Object[] {Integer.valueOf(10)}));
        }
        String rslt = new String(out);
        return rslt;
    }

    public static String byteArrayToHex(byte[] in) {
        return hexdump(in);
    }

    static int WriteHex(byte[] b, int index, StringBuffer out) {
        byte ch = 0;
        int offset = 0;
        while ((offset + index + 1 <= b.length) && (offset <= 15)) {
            ch = (byte) (b[(offset + index)] & 0xF0);
            ch = (byte) (ch >>> 4);
            ch = (byte) (ch & 0xF);
            out.append(pseudo[ch]);
            ch = (byte) (b[(offset + index)] & 0xF);
            out.append(pseudo[ch]);
            offset++;
            out.append(" ");
        }
        if (offset != 16) {
            for (int i = offset; i < 16; i++) {
                out.append(" ");
            }
        }
        return offset;
    }

    static int WriteAscii(byte[] b, int index, StringBuffer out) {
        int offset = 0;
        while ((offset + index + 1 <= b.length) && (offset <= 15)) {
            out.append(String.format("%1$c", new Object[] {Byte.valueOf(b[(offset + index)])}));
            offset++;
        }
        if (offset != 16) {
            for (int i = offset; i < 16; i++) {
                out.append(" ");
            }
        }
        return offset;
    }

    private static String hexOffset(int i) {
        i = i >> 4 << 4;
        int w = i > 65535 ? 8 : 4;
        try {
            return zeropad(Integer.toString(i, 16), w);
        } catch (Exception e) {
            // ulfLog.debug("hexOffset " + DHUtility.getStackTrace(e));
            return e.getMessage();
        }
    }

    public static String padleft(String s, int len, char c) throws Exception {
        s = s.trim();
        if (s.length() > len) {
            throw new Exception("invalid len " + s.length() + "/" + len);
        }
        StringBuffer d = new StringBuffer(len);
        int fill = len - s.length();
        while (fill-- > 0) {
            d.append(c);
        }
        d.append(s);
        return d.toString();
    }

    public static String zeropad(String s, int len) throws Exception {
        return padleft(s, len, '0');
    }

    public static String hexdump(byte[] b) {
        return hexdump(b, 0, b.length);
    }

    public static String hexdump(byte[] b, int offset, int len) {
        StringBuffer sb = new StringBuffer();
        StringBuffer hex = new StringBuffer();
        StringBuffer ascii = new StringBuffer();
        String sep = " ";
        String lineSep = System.getProperty("line.separator");
        for (int i = offset; i < len; i++) {
            char hi = Character.forDigit(b[i] >> 4 & 0xF, 16);
            char lo = Character.forDigit(b[i] & 0xF, 16);
            hex.append(Character.toUpperCase(hi));
            hex.append(Character.toUpperCase(lo));
            hex.append(' ');
            char c = (char) b[i];
            ascii.append((c >= ' ') && (c < '') ? c : '.');

            int j = i % 16;
            switch (j) {
                case 7:
                    hex.append(' ');
                    break;
                case 15:
                    sb.append(hexOffset(i));
                    sb.append(sep);
                    sb.append(hex.toString());
                    sb.append(' ');
                    sb.append(ascii.toString());
                    sb.append(lineSep);
                    hex = new StringBuffer();
                    ascii = new StringBuffer();
            }
        }
        if (hex.length() > 0) {
            while (hex.length() < 49) {
                hex.append(' ');
            }
            sb.append(hexOffset(b.length));
            sb.append(sep);
            sb.append(hex.toString());
            sb.append(' ');
            sb.append(ascii.toString());
            sb.append(lineSep);
        }
        return sb.toString();
    }

    // Added for Luna HSM
    private static final int ROW_BYTES = 16;
    private static final int ROW_QTR1 = 3;
    private static final int ROW_HALF = 7;
    private static final int ROW_QTR2 = 11;

    public static void dumpHexData(PrintStream out, byte[] buf, int numBytes) {
        int rows, residue, i, j;
        byte[] save_buf = new byte[ROW_BYTES + 2];
        byte[] hex_buf = new byte[4];
        byte[] idx_buf = new byte[8];
        byte[] hex_chars = new byte[20];

        hex_chars[0] = '0';
        hex_chars[1] = '1';
        hex_chars[2] = '2';
        hex_chars[3] = '3';
        hex_chars[4] = '4';
        hex_chars[5] = '5';
        hex_chars[6] = '6';
        hex_chars[7] = '7';
        hex_chars[8] = '8';
        hex_chars[9] = '9';
        hex_chars[10] = 'A';
        hex_chars[11] = 'B';
        hex_chars[12] = 'C';
        hex_chars[13] = 'D';
        hex_chars[14] = 'E';
        hex_chars[15] = 'F';

        // out.println(title + " - " + numBytes + " bytes.");
        rows = numBytes >> 4;
        residue = numBytes & 0x0000000F;
        for (i = 0; i < rows; i++) {
            int hexVal = (i * ROW_BYTES);
            idx_buf[0] = hex_chars[((hexVal >> 12) & 15)];
            idx_buf[1] = hex_chars[((hexVal >> 8) & 15)];
            idx_buf[2] = hex_chars[((hexVal >> 4) & 15)];
            idx_buf[3] = hex_chars[(hexVal & 15)];

            String idxStr = new String(idx_buf, 0, 4);
            out.print(idxStr + ": ");

            for (j = 0; j < ROW_BYTES; j++) {
                save_buf[j] = buf[(i * ROW_BYTES) + j];

                hex_buf[0] = hex_chars[(save_buf[j] >> 4) & 0x0F];
                hex_buf[1] = hex_chars[save_buf[j] & 0x0F];

                out.print((char) hex_buf[0]);
                out.print((char) hex_buf[1]);
                out.print(' ');

                if (j == ROW_QTR1 || j == ROW_HALF || j == ROW_QTR2) out.print(" ");

                if (save_buf[j] < 0x20 || save_buf[j] > 0xD9) save_buf[j] = '.';
            }

            String saveStr = new String(save_buf, 0, j);
            out.println(" | " + saveStr + " |");
        }

        if (residue > 0) {
            int hexVal = (i * ROW_BYTES);
            idx_buf[0] = hex_chars[((hexVal >> 12) & 15)];
            idx_buf[1] = hex_chars[((hexVal >> 8) & 15)];
            idx_buf[2] = hex_chars[((hexVal >> 4) & 15)];
            idx_buf[3] = hex_chars[(hexVal & 15)];

            String idxStr = new String(idx_buf, 0, 4);
            out.print(idxStr + ": ");

            for (j = 0; j < residue; j++) {
                save_buf[j] = buf[(i * ROW_BYTES) + j];

                hex_buf[0] = hex_chars[(save_buf[j] >> 4) & 0x0F];
                hex_buf[1] = hex_chars[save_buf[j] & 0x0F];

                out.print((char) hex_buf[0]);
                out.print((char) hex_buf[1]);
                out.print(' ');

                if (j == ROW_QTR1 || j == ROW_HALF || j == ROW_QTR2) out.print(" ");

                if (save_buf[j] < 0x20 || save_buf[j] > 0xD9) save_buf[j] = '.';
            }

            for (
            /* j INHERITED */ ; j < ROW_BYTES; j++) {
                save_buf[j] = ' ';
                out.print("   ");
                if (j == ROW_QTR1 || j == ROW_HALF || j == ROW_QTR2) out.print(" ");
            }

            String saveStr = new String(save_buf, 0, j);
            out.println(" | " + saveStr + " |");
        }
    }

    public static String getHexDump(byte[] byteArray) {
        String strHexDump = "";

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);

            HexDump.dumpHexData(ps, byteArray, byteArray.length);

            strHexDump = new String(baos.toByteArray(), Charset.defaultCharset());

        } catch (Exception e) {
            strHexDump = "";
        }
        return strHexDump;
    }
}
