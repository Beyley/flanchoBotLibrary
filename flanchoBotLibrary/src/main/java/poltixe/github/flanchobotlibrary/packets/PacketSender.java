package poltixe.github.flanchobotlibrary.packets;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;

import poltixe.github.flanchobotlibrary.shortcuts.*;

/**
 * A class that has handy shortcuts for sending various packets to the server
 * 
 * @author Beyley Thomas
 */
public class PacketSender {
    /**
     * The queue used for sending packets
     */
    private ArrayBlockingQueue<byte[]> packetQueue;

    /**
     * The socket used for sending data
     */
    private Socket client;
    PrintToConsole console;

    /**
     * Constructs a new packet sender
     * 
     * @param client The client you are connected to
     */
    public PacketSender(Socket client, PrintToConsole console) {
        this.packetQueue = new ArrayBlockingQueue<byte[]>(1024);
        this.client = client;
        this.console = console;

        new Thread() {
            public void run() {
                try {
                    DataOutputStream dOut = new DataOutputStream(client.getOutputStream());

                    while (true) {
                        byte[] thisPacket = packetQueue.poll();

                        if (thisPacket != null) {
                            FileHandler.appendToPacketLog(thisPacket);

                            dOut.write(thisPacket);
                            dOut.flush();
                        }

                        Thread.sleep(50);
                    }
                } catch (IOException | InterruptedException ex) {

                }
            }
        }.start();
    }

    /**
     * Updates the status without changing the status text
     * 
     * @param newStatus The status to change to
     */
    public void updateStatus(short newStatus) {
        packetQueue.add(new SendUserStatusPacket((byte) newStatus).getPacket());
    }

    /**
     * Disconnects the bot from the server
     */
    public void disconnectFromServer() {
        packetQueue.add(new SendExitPacket().getPacket());
    }

    /**
     * Disconnects the bot from the server
     */
    public void joinLobby() {
        packetQueue.add(new SendJoinLobbyPacket().getPacket());
    }

    /**
     * Disconnects the bot from the server
     */
    public void leaveLobby() {
        packetQueue.add(new SendLeaveLobbyPacket().getPacket());
    }

    /**
     * Requests a full user status update from the server
     */
    public void requestStatusUpdate() {
        packetQueue.add(new SendRequestStatusUpdatePacket().getPacket());
    }

    /**
     * Update the status along with the status text
     * 
     * @param newStatus  The status to change to
     * @param statusText The new status text
     */
    public void updateStatus(byte newStatus, String statusText) {
        packetQueue.add(new SendUserStatusPacket(newStatus, statusText, "notreal", ModHandler.NOMOD).getPacket());
    }

    /**
     * Sends a heartbeat (keepalive) packet to the server
     */
    public void sendHeartbeat() {
        packetQueue.add(new byte[] { 3, 0, 0, 0, 0, 0, 0 });
    }

    /**
     * Sends an Irc message
     * 
     * @param username The username of the message
     * @param message  The message contents
     * @param target   The target of the message
     */
    public void sendMessage(String username, String message, String target) {
        console.printIrcMessage(target, username, message);

        packetQueue.add(new SendIrcMessagePacket(username, message, target).getPacket());
    }
}
