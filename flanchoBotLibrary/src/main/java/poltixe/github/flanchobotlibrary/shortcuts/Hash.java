package poltixe.github.flanchobotlibrary.shortcuts;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Handles hashing
 */
public class Hash {
    /**
     * Gets the Sha256 of a string
     * 
     * @param s The string
     * @return The Sha256 as a string
     */
    public static String sha256(String s) {
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(s.getBytes("utf8"));
            toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }
}
