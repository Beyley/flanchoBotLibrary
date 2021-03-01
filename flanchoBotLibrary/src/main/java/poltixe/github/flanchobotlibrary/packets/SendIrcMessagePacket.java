package poltixe.github.flanchobotlibrary.packets;

import java.io.*;

import poltixe.github.flanchobotlibrary.*;

/**
 * The packet used for sending an IRC message in chat
 * 
 * @author Beyley Thomas
 */
public class SendIrcMessagePacket extends Packet {
    /**
     * The sender of the message (in this cas always the bot name)
     */
    String sender;
    /**
     * The message contents
     */
    String message;
    /**
     * The target of the message
     */
    String target;

    /**
     * Constructs a SendIrcMessage packet
     * 
     * @param sender  The sender of the message
     * @param message The message contents
     * @param target  The target of the message
     */
    public SendIrcMessagePacket(String sender, String message, String target) {
        super(ClientPackets.sendIrcMessage);

        this.sender = sender;
        this.message = message;
        this.target = target;
    }

    /**
     * Constructs a byte array that is a working SendIrcMessage packet
     * 
     * @return The packet contents
     */
    public byte[] getPacket() {
        byte[] senderName = Uleb128.writeString(sender);
        byte[] ulebTarget = Uleb128.writeString(target);
        byte[] ulebMessage = Uleb128.writeString(message);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LittleEndianOutputStream dos = new LittleEndianOutputStream(baos);

        try {
            dos.writeLittleEndianByte((byte) 0);
            dos.flush();
            dos.littleEndianWrite(ulebMessage, 0, ulebMessage.length);
            dos.flush();
            dos.littleEndianWrite(ulebTarget, 0, ulebTarget.length);
            dos.flush();
            dos.littleEndianWrite(senderName, 0, senderName.length);
            dos.flush();

            return this.constructFinalPacket(baos.toByteArray());
        } catch (IOException ex) {
            return new byte[] {};
        }
    }
}
