package poltixe.github.flanchobotlibrary.shortcuts;

import java.io.IOException;
import java.nio.file.*;

public class FileHandler {
    public static void appendToFile(String filename, String message) {
        try {
            Path path = Paths.get(filename + ".txt");
            Files.write(path, (message + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void appendToPacketLog(byte[] packetInfo) {
        appendToFile("packetlog", bytesToHex(packetInfo));
    }

    public static void appendToErrorLog(String error) {
        appendToFile("errorlog", error);
    }
}
