package ch.epfl.sweng.hostme.call;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.zip.CRC32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CallUtils {

    /**
     * Create a mac signature for security
     *
     * @param keyString key for the secret
     * @param msg       content
     * @return mac signature
     * @throws InvalidKeyException      wrong key
     * @throws NoSuchAlgorithmException no existing algo
     */
    public static byte[] macSign(String keyString, byte[] msg) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec keySpec = new SecretKeySpec(keyString.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(keySpec);
        return mac.doFinal(msg);
    }

    /**
     * Pack the buffer
     *
     * @param packable pack the buffer
     * @return buffer in bytes
     */
    public static byte[] pack(Packable packable) {
        ByteBuf buffer = new ByteBuf();
        packable.marshal(buffer);
        return buffer.asBytes();
    }

    /**
     * encode in base 64
     *
     * @param data to be encoded
     * @return String value of the encoded data
     */
    public static String base64Encode(byte[] data) {
        byte[] encodedBytes = Base64.encodeBase64(data);
        return new String(encodedBytes);
    }

    /**
     * apply crc32 to the data
     *
     * @param data to apply crc32 on it
     * @return the crc32 data applied
     */
    public static int crc32(String data) {
        byte[] bytes = data.getBytes();
        return crc32(bytes);
    }

    /**
     * apply crc32 to the bytes array
     *
     * @param bytes to apply crc32 on it
     * @return the crc32 data applied
     */
    public static int crc32(byte[] bytes) {
        CRC32 checksum = new CRC32();
        checksum.update(bytes);
        return (int) checksum.getValue();
    }

    /**
     * get the current time stamp
     *
     * @return current time stamp
     */
    public static int getTimestamp() {
        return (int) ((new Date().getTime()) / 1000);
    }

    /**
     * get random integer
     *
     * @return random integer
     */
    public static int randomInt() {
        return new SecureRandom().nextInt();
    }

    /**
     * get if uuid is a uuid
     *
     * @param uuid check if it is a uuid
     * @return true if argument is a uuid
     */
    public static boolean isUUID(String uuid) {
        if (uuid.length() != 32) {
            return true;
        }

        return !uuid.matches("\\p{XDigit}+");
    }
}
