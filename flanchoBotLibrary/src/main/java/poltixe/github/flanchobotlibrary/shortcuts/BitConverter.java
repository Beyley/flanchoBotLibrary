package poltixe.github.flanchobotlibrary.shortcuts;

/**
 * A class used for converting to and from objects into bytes
 */
public class BitConverter {
    /**
     * Converts an array of bytes to a short
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read short
     */
    public static short toInt16(byte[] bytes, int offset) {
        short result = (short) ((int) bytes[offset] & 0xff);
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        return (short) (result & 0xffff);
    }

    /**
     * Converts an array of bytes to a short Little Endian
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read short
     */
    public static short toInt16LE(byte[] bytes, int offset) {
        bytes = reverse(bytes);
        short result = (short) ((int) bytes[offset] & 0xff);
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        return (short) (result & 0xffff);
    }

    /**
     * Converts an array of bytes to an unsigned int16
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read uint16 as an int
     */
    public static int toUInt16(byte[] bytes, int offset) {
        int result = (int) bytes[offset + 1] & 0xff;
        result |= ((int) bytes[offset] & 0xff) << 8;
        return result & 0xffff;
    }

    /**
     * Converts an array of bytes to an unsigned int16 Little Endian
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read uint16 as an int
     */
    public static int toUInt16LE(byte[] bytes, int offset) {
        bytes = reverse(bytes);
        int result = (int) bytes[offset + 1] & 0xff;
        result |= ((int) bytes[offset] & 0xff) << 8;
        return result & 0xffff;
    }

    /**
     * Converts an array of bytes to a int32
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read int32 as an int
     */
    public static int toInt32(byte[] bytes, int offset) {
        int result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result;
    }

    /**
     * Converts an array of bytes to a int32 Little Endian
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read int32 as an int
     */
    public static int toInt32LE(byte[] bytes, int offset) {
        bytes = reverse(bytes);
        int result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result;
    }

    /**
     * Converts an array of bytes to an unsigned int32
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read uint32 as a long
     */
    public static long toUInt32(byte[] bytes, int offset) {
        long result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result & 0xFFFFFFFFL;
    }

    /**
     * Converts an array of bytes to an unsigned int32 Little Endian
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read uint32 as a long
     */
    public static long toUInt32LE(byte[] bytes, int offset) {
        bytes = reverse(bytes);
        long result = (int) bytes[offset] & 0xff;
        result |= ((int) bytes[offset + 1] & 0xff) << 8;
        result |= ((int) bytes[offset + 2] & 0xff) << 16;
        result |= ((int) bytes[offset + 3] & 0xff) << 24;
        return result & 0xFFFFFFFFL;
    }

    /**
     * Converts an array of bytes to a int64
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read int64 as an int
     */
    public static long toInt64(byte[] buffer, int offset) {
        long values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8;
            values |= (buffer[offset + i] & 0xFF);
        }
        return values;
    }

    /**
     * Converts an array of bytes to a int64 Little Endian
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read int64 as an int
     */
    public static long toInt64LE(byte[] buffer, int offset) {
        buffer = reverse(buffer);
        long values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8;
            values |= (buffer[offset + i] & 0xFF);
        }
        return values;
    }

    /**
     * Converts an array of bytes to an unsigned int64
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read uint64 as a long
     */
    public static long toUInt64(byte[] bytes, int offset) {
        long result = 0;
        for (int i = 0; i <= 56; i += 8) {
            result |= ((int) bytes[offset++] & 0xff) << i;
        }
        return result;
    }

    /**
     * Converts an array of bytes to a float
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read float
     */
    public static float toFloat(byte[] bs, int index) {
        return Float.intBitsToFloat(toInt32(bs, index));
    }

    /**
     * Converts an array of bytes to a float Little Endian
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read float
     */
    public static float toFloatLE(byte[] bs, int index) {
        return Float.intBitsToFloat(toInt32LE(bs, index));
    }

    /**
     * Converts an array of bytes to a double
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read double
     */
    public static double toDouble(byte[] arr, int offset) {
        return Double.longBitsToDouble(toUInt64(arr, offset));
    }

    /**
     * Converts an array of bytes to a boolean
     * 
     * @param bytes  The bytes to read from
     * @param offset The offset of the short
     * @return The read boolean
     */
    public static boolean toBoolean(byte[] bytes, int offset) {
        return (bytes[offset] == 0x00) ? false : true;
    }

    /**
     * Gets bytes of a short
     * 
     * @param value The short to get the bytes of
     * @return The bytes of the short
     */
    public static byte[] getBytes(short value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value & 0xff);
        bytes[1] = (byte) ((value & 0xff00) >> 8);
        return bytes;
    }

    /**
     * Gets bytes of an int
     * 
     * @param value The int to get the bytes of
     * @return The bytes of the int
     */
    public static byte[] getBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value) & 0xFF); // 最低位
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >>> 24)); // 最高位，无符号右移
        return bytes;
    }

    /**
     * Gets bytes of a long
     * 
     * @param value The long to get the bytes of
     * @return The bytes of the long
     */
    public static byte[] getBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }

    /**
     * Gets bytes of a float
     * 
     * @param value The float to get the bytes of
     * @return The bytes of the float
     */
    public static byte[] getBytes(float value) {
        return getBytes(Float.floatToIntBits(value));
    }

    /**
     * Gets bytes of a double
     * 
     * @param value The double to get the bytes of
     * @return The bytes of the double
     */
    public static byte[] getBytes(double val) {
        long value = Double.doubleToLongBits(val);
        return getBytes(value);
    }

    /**
     * Gets bytes of a boolean
     * 
     * @param value The boolean to get the bytes of
     * @return The bytes of the boolean
     */
    public static byte[] getBytes(boolean value) {
        return new byte[] { (byte) (value ? 1 : 0) };
    }

    /**
     * Converts an int to a byte
     * 
     * @param x The int
     * @return The byte
     */
    public static byte intToByte(int x) {
        return (byte) x;
    }

    /**
     * Converts a byte to an int
     * 
     * @param b The byte
     * @return The int
     */
    public static int byteToInt(byte b) {
        return b & 0xFF;
    }

    /**
     * Converts a byte array to a char
     * 
     * @param bs     The byte array
     * @param offset The offset to read from
     * @return The read char
     */
    public static char toChar(byte[] bs, int offset) {
        return (char) (((bs[offset] & 0xFF) << 8) | (bs[offset + 1] & 0xFF));
    }

    /**
     * Gets bytes of a char
     * 
     * @param value The char to get the bytes of
     * @return The bytes of the char
     */
    public static byte[] getBytes(char value) {
        byte[] b = new byte[2];
        b[0] = (byte) ((value & 0xFF00) >> 8);
        b[1] = (byte) (value & 0xFF);
        return b;
    }

    public static byte[] concat(byte[]... bs) {
        int len = 0, idx = 0;
        for (byte[] b : bs)
            len += b.length;
        byte[] buffer = new byte[len];
        for (byte[] b : bs) {
            System.arraycopy(b, 0, buffer, idx, b.length);
            idx += b.length;
        }
        return buffer;
    }

    public static byte[] reverse(byte a[]) {
        int i, k, t;
        for (i = 0; i < a.length / 2; i++) {
            t = a[i];
            a[i] = a[a.length - i - 1];
            a[a.length - i - 1] = (byte) t;
        }

        return a;
    }
}
