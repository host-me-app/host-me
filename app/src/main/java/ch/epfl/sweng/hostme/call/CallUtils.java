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

    public static byte[] macSign(String keyString, byte[] msg) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec keySpec = new SecretKeySpec(keyString.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(keySpec);
        return mac.doFinal(msg);
    }

    public static byte[] pack(Packable packable) {
        ByteBuf buffer = new ByteBuf();
        packable.marshal(buffer);
        return buffer.asBytes();
    }

    public static String base64Encode(byte[] data) {
        byte[] encodedBytes = Base64.encodeBase64(data);
        return new String(encodedBytes);
    }

    public static int crc32(String data) {
        byte[] bytes = data.getBytes();
        return crc32(bytes);
    }

    public static int crc32(byte[] bytes) {
        CRC32 checksum = new CRC32();
        checksum.update(bytes);
        return (int) checksum.getValue();
    }

    public static int getTimestamp() {
        return (int) ((new Date().getTime()) / 1000);
    }

    public static int randomInt() {
        return new SecureRandom().nextInt();
    }

    public static boolean isUUID(String uuid) {
        if (uuid.length() != 32) {
            return true;
        }

        return !uuid.matches("\\p{XDigit}+");
    }
}