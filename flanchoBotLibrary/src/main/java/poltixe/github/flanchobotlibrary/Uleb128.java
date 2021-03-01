package poltixe.github.flanchobotlibrary;

import java.io.*;
import java.util.*;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Handles all Uleb128 related conversions
 */
public class Uleb128 {
    /**
     * Writes a number in Uleb128
     * 
     * @param num The number to convert to Uleb128
     * @return The bytes for that number in Uleb128
     */
    private static byte[] write_Uleb128(int num) {
        List<Byte> ret = new ArrayList<Byte>();

        if (num == 0) {
            return new byte[] { 0x00 };
        }

        int length = 0;

        while (num > 0) {
            ret.add((byte) (num & 127));
            num >>= 7;

            if (num != 0) {
                byte current = ret.get(length);
                ret.set(length, current |= 128);
            }

            length += 1;
        }

        return ArrayUtils.toPrimitive(ret.toArray(new Byte[ret.size()]));
    }

    /**
     * Writes a string in Uleb128
     * 
     * @param s The string to write in Uleb128
     * @return The bytes of that string in Uleb128
     */
    public static byte[] writeString(String s) {
        if (s.length() == 0)
            return new byte[] { 0x00 };
        List<Byte> ret = new ArrayList<Byte>();

        ret.add((byte) 11);
        Collections.addAll(ret, ArrayUtils.toObject(write_Uleb128(s.length())));
        try {
            Collections.addAll(ret, ArrayUtils.toObject(s.getBytes("utf8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ArrayUtils.toPrimitive(ret.toArray(new Byte[ret.size()]));
    }

    /**
     * Reads a Uleb128 string into a Java string
     * 
     * @param s The stream to read from
     * @return The string read
     */
    public static String readString(InputStream s) {
        try {
            byte type = (byte) s.read();

            if (type == 0) {
                return "";
            }

            int length = s.read();

            byte[] stringBytes = new byte[length];

            stringBytes = s.readNBytes(length);

            return new String(stringBytes);
        } catch (IOException e) {
        }

        return "fail";
    }
}
