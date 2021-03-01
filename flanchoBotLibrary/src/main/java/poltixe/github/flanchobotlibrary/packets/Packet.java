package poltixe.github.flanchobotlibrary.packets;

import java.io.*;

import poltixe.github.flanchobotlibrary.LittleEndianOutputStream;

/**
 * The base packet for other packets to extend off of
 * 
 * @author Beyley Thomas
 */
public class Packet {
    /**
     * The packets id
     */
    short packetId;
    /**
     * Whether or not the packet is compressed
     */
    boolean compression;
    /**
     * The length of the packet
     */
    int length;

    /**
     * The constructor for a packet
     * 
     * @param packetId The packet id
     */
    public Packet(short packetId) {
        this.packetId = packetId;
        this.compression = false;
    }

    /**
     * Constructs a final packet that is compatible with the server
     * 
     * @param data The data inside of the packet
     * @return The bytes of the finished packet
     * @throws IOException
     */
    protected byte[] constructFinalPacket(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        LittleEndianOutputStream dos = new LittleEndianOutputStream(baos);

        // Write the packet id
        dos.writeLittleEndianShort(packetId);
        dos.flush();
        // Write whether or not to enable compression
        dos.writeLittleEndianBoolean(compression);
        dos.flush();
        // Write the packet length
        dos.writeLittleEndianInt(data.length);
        dos.flush();

        dos.write(data);
        dos.flush();

        return baos.toByteArray();
    }
}
