package ch.epfl.sweng.hostme.call;

import static ch.epfl.sweng.hostme.call.CallUtils.crc32;

import java.io.ByteArrayOutputStream;
import java.util.TreeMap;


public class AccessToken {
    private static final String VER = "006";
    public String appId;
    public String appCertificate;
    public String channelName;
    public String uid;
    public byte[] signature;
    public byte[] messageRawContent;
    public int crcChannelName;
    public int crcUid;
    public PrivilegeMessage message;

    public AccessToken(String appId, String appCertificate, String channelName, String uid) {
        this.appId = appId;
        this.appCertificate = appCertificate;
        this.channelName = channelName;
        this.uid = uid;
        this.crcChannelName = 0;
        this.crcUid = 0;
        this.message = new PrivilegeMessage();
    }

    public static String getVersion() {
        return VER;
    }

    public static byte[] generateSignature(String appCertificate,
                                           String appID, String channelName, String uid, byte[] message) throws Exception {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(appID.getBytes());
            stream.write(channelName.getBytes());
            stream.write(uid.getBytes());
            stream.write(message);
        } catch (Exception ignored) {
        }
        return CallUtils.macSign(appCertificate, stream.toByteArray());
    }

    public String build() throws Exception {
        if (CallUtils.isUUID(appId)) {
            return "";
        }

        if (CallUtils.isUUID(appCertificate)) {
            return "";
        }

        messageRawContent = CallUtils.pack(message);
        signature = generateSignature(appCertificate,
                appId, channelName, uid, messageRawContent);
        crcChannelName = crc32(channelName);
        crcUid = crc32(uid);

        PackContent packContent = new PackContent(signature, crcChannelName, crcUid, messageRawContent);
        byte[] content = CallUtils.pack(packContent);
        return getVersion() + this.appId + CallUtils.base64Encode(content);
    }

    public void addPrivilege(Privileges privilege, int expireTimestamp) {
        message.messages.put(privilege.intValue, expireTimestamp);
    }

    public enum Privileges {
        kJoinChannel(1),
        kPublishAudioStream(2),
        kPublishVideoStream(3),
        kPublishDataStream(4);

        public short intValue;

        Privileges(int value) {
            intValue = (short) value;
        }
    }

    public static class PrivilegeMessage implements Packable {
        public int salt;
        public int ts;
        public TreeMap<Short, Integer> messages;

        public PrivilegeMessage() {
            salt = CallUtils.randomInt();
            ts = CallUtils.getTimestamp() + 24 * 3600;
            messages = new TreeMap<>();
        }

        @Override
        public void marshal(ByteBuf out) {
            out.put(salt).put(ts).putIntMap(messages);
        }
    }

    public static class PackContent implements Packable {
        public byte[] signature;
        public int crcChannelName;
        public int crcUid;
        public byte[] rawMessage;

        public PackContent(byte[] signature, int crcChannelName, int crcUid, byte[] rawMessage) {
            this.signature = signature;
            this.crcChannelName = crcChannelName;
            this.crcUid = crcUid;
            this.rawMessage = rawMessage;
        }

        @Override
        public void marshal(ByteBuf out) {
            out.put(signature).put(crcChannelName).put(crcUid).put(rawMessage);
        }
    }
}
