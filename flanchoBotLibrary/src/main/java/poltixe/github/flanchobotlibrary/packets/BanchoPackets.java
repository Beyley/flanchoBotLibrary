package poltixe.github.flanchobotlibrary.packets;

/**
 * A class containing all the bancho packets that are relavent to a bot
 * 
 * @author Beyley Thomas
 */
public class BanchoPackets {
    /**
     * The packet sent when the server is replying to a login
     */
    public static final int loginReply = 5;
    /**
     * The packet sent when an Irc message is sent
     */
    public static final int sendIrcMessage = 7;
    /**
     * The packet sent that handles player updates
     */
    public static final int handleOsuUpdate = 12;
    /**
     * The packet sent that handles player disconnects
     */
    public static final int handleOsuDisconnect = 13;
    /**
     * The packet sent when an announcement is made
     */
    public static final int announce = 25;
    /**
     * The packet sent when you join a channel successfully
     */
    public static final int channelJoinSuccess = 65;
    /**
     * The packet sent when a new channel is available
     */
    public static final int channelAvailable = 66;
    /**
     * The packet sent when a channel is revoked
     */
    public static final int channelRevoked = 67;
    /**
     * The packet sent when you autojoined a channel? UNSURE
     */
    public static final int channelAvailableAutojoin = 68;
    /**
     * The packet sent when a new match is sent
     */
    public static final int matchNew = 28;
    /**
     * The packet sent when a match is disbanded
     */
    public static final int matchDisband = 29;
    /**
     * The packet sent when a new player joins the lobby
     */
    public static final int lobbyJoin = 35;
    /**
     * The packet sent when a player leaves the lobby
     */
    public static final int lobbyPart = 36;

    /**
     * A list of all the privileges that a user can have
     */
    public static enum BanchoPrivileges {
        /**
         * The normal privileges for most users
         */
        NORMALUSER(new Privilege((byte) 0, "a Normal user")),

        /**
         * The privileges for the Global Moderation Team, can silence players and access
         * #staff
         */
        GLOBALMODERATIONTEAM(new Privilege((byte) 1, "part of the Global Moderation Team!")),

        /**
         * The privileges for the Beatmap Approval Team, can heart and flame maps
         */
        BEATMAPAPPROVALTEAM(new Privilege((byte) 2, "part of the Beatmap Approval Team!")),

        /**
         * A developer (unsure what it does, possibly only cosmetic?)
         */
        DEVELOPER(new Privilege((byte) 3, "a developer!")),

        /**
         * A huge cuuuutie <3
         */
        EEVEE(new Privilege((byte) 4, "Eevee! <3"));

        public static class Privilege {
            public byte privilegeValue;
            public String privilegeTitle;

            Privilege(byte privilegeValue, String privilegeTitle) {
                this.privilegeValue = privilegeValue;
                this.privilegeTitle = privilegeTitle;
            }
        }

        public Privilege privilege;

        private BanchoPrivileges(Privilege privilege) {
            this.privilege = privilege;
        }
    }
}
