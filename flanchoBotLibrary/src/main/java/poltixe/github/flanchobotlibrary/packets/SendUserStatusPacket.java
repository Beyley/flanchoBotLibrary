package poltixe.github.flanchobotlibrary.packets;

import java.io.*;

import poltixe.github.flanchobotlibrary.*;

/**
 * The packet used for sending user status updates
 * 
 * @author Beyley Thomas
 */
public class SendUserStatusPacket extends Packet {
    /**
     * The new status to change to
     */
    byte newStatus;
    /**
     * Whether to update the beatmap
     */
    boolean updateBeatmap;

    /**
     * The new status text
     */
    String statusText;
    /**
     * The new map's checksum
     */
    String mapChecksum;
    /**
     * The new mods that are enabled
     */
    char currentMods;

    public static enum Status {
        /**
         * The "Idle" status
         */
        IDLE((byte) 0),
        /**
         * The "AFK" status
         */
        AFK((byte) 1),
        /**
         * The "Playing" status
         */
        PLAYING((byte) 2),
        /**
         * The "Editing" status
         */
        EDITING((byte) 3),
        /**
         * The "Modding" status
         */
        MODDING((byte) 4),
        /**
         * The "Multiplayer" status
         */
        MULTIPLAYER((byte) 5),
        /**
         * The "Watching" status
         */
        WATCHING((byte) 6),
        /**
         * The "Unknown" status
         */
        UNKNOWN((byte) 7),
        /**
         * The "Testing" status
         */
        TESTING((byte) 8),
        /**
         * The "Submititng" status
         */
        SUBMITTING((byte) 9),
        /**
         * The "Paused" status
         */
        PAUSED((byte) 10),
        /**
         * The "Lobby" status
         */
        LOBBY((byte) 11),
        /**
         * The "Multiplaying" status
         */
        MULTIPLAYING((byte) 12);

        public byte value;

        Status(byte value) {
            this.value = value;
        }
    }

    /**
     * Constructs a user status packet that does not change the map text
     * 
     * @param newStatus The new status ID
     */
    public SendUserStatusPacket(byte newStatus) {
        super(ClientPackets.sendUserStatus);

        this.newStatus = newStatus;
        this.updateBeatmap = false;
    }

    /**
     * Constructs a user packet that changes the status text
     * 
     * @param newStatus   The new status ID
     * @param statusText  The new status text
     * @param mapChecksum The map checksum the user is currently playing (Used for
     *                    spectating)
     * @param currentMods The mods the user is playing with
     */
    public SendUserStatusPacket(byte newStatus, String statusText, String mapChecksum, char currentMods) {
        super(ClientPackets.sendUserStatus);

        this.newStatus = newStatus;
        this.updateBeatmap = true;

        this.statusText = statusText;
        this.mapChecksum = mapChecksum;
        this.currentMods = currentMods;
    }

    /**
     * Constructs a byte array that is a working SendUserStatus packet
     * 
     * @return The packet contents
     */
    public byte[] getPacket() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LittleEndianOutputStream dos = new LittleEndianOutputStream(baos);
        try {
            dos.writeLittleEndianByte((byte) newStatus);
            dos.flush();
            dos.writeLittleEndianBoolean(updateBeatmap);
            dos.flush();

            if (updateBeatmap) {
                byte[] statusTextUleb = Uleb128.writeString(statusText);
                byte[] mapChecksumUleb = Uleb128.writeString(mapChecksum);

                dos.littleEndianWrite(statusTextUleb, 0, statusTextUleb.length);
                dos.flush();
                dos.littleEndianWrite(mapChecksumUleb, 0, mapChecksumUleb.length);
                dos.flush();
                dos.writeLittleEndianChar(currentMods);
                dos.flush();
            }

            return this.constructFinalPacket(baos.toByteArray());
        } catch (IOException ex) {
            return new byte[] {};
        }
    }
}
