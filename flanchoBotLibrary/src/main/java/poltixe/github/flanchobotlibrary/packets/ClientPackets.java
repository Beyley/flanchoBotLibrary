package poltixe.github.flanchobotlibrary.packets;

/**
 * A class containing all the packets the bot can send
 * 
 * @author Beyley Thomas
 */
public class ClientPackets {
    /**
     * The packet that sends the users status
     */
    public static final short sendUserStatus = 0;
    /**
     * The packet that sends an Irc message
     */
    public static final short sendIrcMessage = 1;
    /**
     * Disconnects the client from the server
     */
    public static final short exit = 2;
    /**
     * Requests a full status update
     */
    public static final short requestStatusUpdate = 3;
    /**
     * Tells the server that the bot has joined the lobby
     */
    public static final short spectateFrames = 19;
    /**
     * Tells the server that the bot has joined the lobby
     */
    public static final short lobbyJoin = 31;
    /**
     * Tells the server that the bot has left the lobby
     */
    public static final short lobbyPart = 30;
    /**
     * Tells the server that the bot has left the lobby
     */
    public static final short banBot = 192;
}
