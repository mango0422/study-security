package mango.security.token_login.util;

import java.security.SecureRandom;

public class SecretKeyUtil {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSecretKey() {
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);
        return bytesToHex(keyBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
