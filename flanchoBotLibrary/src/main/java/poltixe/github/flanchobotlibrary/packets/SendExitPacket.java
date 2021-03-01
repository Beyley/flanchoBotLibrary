package poltixe.github.flanchobotlibrary.packets;

import java.io.*;

/**
 * The packet used for sending an IRC message in chat
 * 
 * @author Beyley Thomas
 */
public class SendExitPacket extends Packet {
    /**
     * Constructs a SendIrcMessage packet
     * 
     * @param sender  The sender of the message
     * @param message The message contents
     * @param target  The target of the message
     */
    public SendExitPacket() {
        super(ClientPackets.exit);
    }

    /**
     * Constructs a byte array that is a working SendIrcMessage packet
     * 
     * @return The packet contents
     */
    public byte[] getPacket() {
        try {
            return this.constructFinalPacket(new byte[] {});
        } catch (IOException ex) {
            return new byte[] {};
        }
    }
}
