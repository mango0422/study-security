package mango.security.token_login.token;

import java.security.SecureRandom;

public class CustomUUID {
    private static final SecureRandom random = createSeededRandom();

    private static SecureRandom createSeededRandom() {
        long currentTimeMillis = System.currentTimeMillis();

        // 시간 기반 시드를 바이트 배열로 확장
        byte[] timeSeed = new byte[32];
        for (int i = 0; i < timeSeed.length; i++) {
            timeSeed[i] = (byte) ((currentTimeMillis >> (i % 8) * 8) & 0xFF);
        }

        return new SecureRandom(timeSeed);
    }

    public static String generateUUIDv4() {
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);

        // version 4
        randomBytes[6] &= 0x0f;
        randomBytes[6] |= 0x40;

        // variant (RFC 4122)
        randomBytes[8] &= 0x3f;
        randomBytes[8] |= 0x80;

        return bytesToHex(randomBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        return String.format(
                "%02x%02x%02x%02x-%02x%02x-%02x%02x-%02x%02x-%02x%02x%02x%02x%02x%02x",
                bytes[0], bytes[1], bytes[2], bytes[3],
                bytes[4], bytes[5],
                bytes[6], bytes[7],
                bytes[8], bytes[9],
                bytes[10], bytes[11], bytes[12], bytes[13], bytes[14], bytes[15]
        );
    }
}
