package ch.epfl.sweng.hostme.call;

public class RtcTokenBuilder {

    /**
     * build a token with the ID of a user
     * @param appId application id
     * @param appCertificate application certificate
     * @param channelName name of the channel
     * @param uid user id
     * @param role role
     * @param privilegeTs the privilege
     * @return the token
     */
    public String buildTokenWithUid(String appId, String appCertificate,
                                    String channelName, int uid, Role role, int privilegeTs) {
        String account = uid == 0 ? "" : String.valueOf(uid);
        return buildTokenWithUserAccount(appId, appCertificate, channelName,
                account, role, privilegeTs);
    }

    /**
     * build token with user account
     * @param appId application id
     * @param appCertificate application certificate
     * @param channelName name of the channel
     * @param account user account
     * @param role role
     * @param privilegeTs the privilege
     * @return the token
     */
    public String buildTokenWithUserAccount(String appId, String appCertificate,
                                            String channelName, String account, Role role, int privilegeTs) {
        AccessToken builder = new AccessToken(appId, appCertificate, channelName, account);
        builder.addPrivilege(AccessToken.Privileges.kJoinChannel, privilegeTs);
        if (role == Role.Role_Publisher || role == Role.Role_Subscriber || role == Role.Role_Admin) {
            builder.addPrivilege(AccessToken.Privileges.kPublishAudioStream, privilegeTs);
            builder.addPrivilege(AccessToken.Privileges.kPublishVideoStream, privilegeTs);
            builder.addPrivilege(AccessToken.Privileges.kPublishDataStream, privilegeTs);
        }

        try {
            return builder.build();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * enumeration of roles
     */
    public enum Role {
        Role_Publisher(1),
        Role_Subscriber(2),
        Role_Admin(101);

        public int initValue;

        Role(int initValue) {
            this.initValue = initValue;
        }
    }
}
