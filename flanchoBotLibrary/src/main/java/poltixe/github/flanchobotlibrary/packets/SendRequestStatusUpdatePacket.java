package poltixe.github.flanchobotlibrary.packets;

import java.io.IOException;

public class SendRequestStatusUpdatePacket extends Packet {
    /**
     * Constructs a RequestStatusUpdate packet
     * 
     */
    public SendRequestStatusUpdatePacket() {
        super(ClientPackets.requestStatusUpdate);
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
