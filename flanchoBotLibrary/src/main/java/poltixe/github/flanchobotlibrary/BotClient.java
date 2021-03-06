package poltixe.github.flanchobotlibrary;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

import com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer;

import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.parser.*;

import poltixe.github.flanchobotlibrary.objects.Match;
import poltixe.github.flanchobotlibrary.objects.Player;
import poltixe.github.flanchobotlibrary.objects.Match.Slot;
import poltixe.github.flanchobotlibrary.packets.*;
import poltixe.github.flanchobotlibrary.packets.BanchoPackets.BanchoPrivileges;
import poltixe.github.flanchobotlibrary.shortcuts.*;

import org.java_websocket.client.WebSocketClient;

/**
 * A bot for oldsu!
 * 
 * @author Beyley Thomas
 * @version 1.0
 */
public abstract class BotClient {
    public Random rand = new Random();

    /**
     * The timer used to send keepalive packets to the server
     */
    Timer keepaliveTimer = new Timer();

    /**
     * The timer used to send keepalive packets to the server
     */
    Timer userStatusTimers = new Timer();

    /**
     * The socket used to connect to the server
     */
    public WebSocketClient client = null;

    /**
     * The PacketSender used for sending packets to the server
     */
    public PacketSender packetSender = null;

    /**
     * The username the bot connects with
     */
    public String username = "";
    /**
     * The password the bot connects with
     */
    public String password = "";
    /**
     * The prefix the bot looks for
     */
    public char prefix;

    /**
     * Whether or not the bot is connected
     */
    public boolean connected = false;
    /**
     * Whether or not the bot is authenticated
     */
    public boolean authenticated = false;

    /**
     * The thread that reads for packets
     */
    public Thread readForPacketsThread = new Thread();

    /**
     * Whether we are reading the header
     */
    public boolean readingHeader = true;
    /**
     * The amount of bytes read
     */
    public int readBytes = 0;
    /**
     * The last time the server was pinged
     */
    public long lastPingTime;
    /**
     * The ping timout
     */
    public int pingTimeout = 30;

    /**
     * The object that is used to print messages to console
     */
    public PrintToConsole console = null;

    /**
     * The bots user ID
     */
    public int userID = -1;

    /**
     * The bots privilegs, see BanchoPackets.BanchoPrivileges
     */
    public int privileges = -1;

    /**
     * Whether to show or hide the location data on the user panel for other users
     */
    public boolean showLocationData = false;

    /**
     * The ip to use for connections
     */
    public String ip = "";

    /**
     * The port to use for connections
     */
    public int port = 13381;

    public List<Integer> allPlayersInLobby = new ArrayList<Integer>();
    public List<Match> allMultiplayerMatches = new ArrayList<Match>();
    public List<Player> allOnlinePlayers = new ArrayList<Player>();

    Thread connectionHandler;

    /**
     * The constructor for a bot
     * 
     * @param username      The username to connect to the server with
     * @param plainPassword The password to connect to the server with in plain text
     * @param prefix        The prefix to look for
     */
    public BotClient(String username, String plainPassword, char prefix) {
        // Set the bots settings
        this.username = username;
        this.password = Hash.sha256(plainPassword);
        this.prefix = prefix;

        this.console = new PrintToConsole(username);
    }

    public boolean doReconnect = true;

    public Runnable runConnectionHandler() {
        try {
            while (true) {
                if (this.doReconnect) {
                    if (this.connected) {
                        // System.out.println("ATTEMPTED RECONNECT WHILE CONNECTED!!!");
                        // System.exit(0);

                        this.client.close();
                    }

                    this.client = new Client(new URI(this.ip), this);
                    this.client.setTcpNoDelay(true);
                    this.connect();

                    this.doReconnect = false;
                }

                Thread.sleep(10000);
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * This initializes the bot
     */
    public void initialize() throws InterruptedException, IOException, ParseException {
        this.connectionHandler = new Thread(runConnectionHandler());
        this.connectionHandler.setName("Connection handler");

        this.connectionHandler.start();

        // this.doReconnect = true;
    }

    /**
     * This reads for packets at all times
     */
    public void readPacket(ByteBuffer packet) throws InterruptedException, IOException, ParseException {
        int packetId = 0;
        boolean packetCompression;
        long packetLength;

        LittleEndianInputStream reader = new LittleEndianInputStream(new ByteArrayInputStream(packet.array()));

        packetId = BitConverter.toInt16(reader.readNBytes(2), 0);
        packetCompression = reader.readBoolean();
        packetLength = BitConverter.toUInt32(reader.readNBytes(4), 0);

        switch (packetId) {
        case BanchoPackets.loginReply:
            userID = BitConverter.toInt32(reader.readNBytes(4), 0);
            privileges = reader.readByte();

            switch (userID) {
            case -1:
                console.printError("Authentication Failed! Invalid Login!");

                Thread.sleep(1000);

                this.doReconnect = true;

                break;
            case -2:
                console.printError("Authentication Failed! Client too old!");

                this.doReconnect = true;

                break;
            case -3:
                console.printError("Your bot has been banned!");

                break;
            default:
                console.printMessage(
                        String.format("[%s:%s] Bot Authenticated and running!", String.valueOf(userID), username));

                for (BanchoPrivileges privilege : BanchoPrivileges.values()) {
                    if (privileges == privilege.privilege.privilegeValue) {
                        console.printMessage(String.format("[%s:%s] You are %s", String.valueOf(userID), username,
                                privilege.privilege.privilegeTitle));
                    }
                }

                authenticated = true;
                packetId = 0;
                packetCompression = false;
                packetLength = 0;

                allPlayersInLobby = new ArrayList<Integer>();
                allMultiplayerMatches = new ArrayList<Match>();
                allOnlinePlayers = new ArrayList<Player>();

                Thread.sleep(100);

                this.packetSender = new PacketSender(client, console);

                this.packetSender.joinLobby();

                startTimers();
                onAuthComplete();

                break;
            }

            break;
        case BanchoPackets.sendIrcMessage: {
            String sender = Uleb128.readString(reader);
            String message = Uleb128.readString(reader);
            String target = Uleb128.readString(reader);

            console.printIrcMessage(target, sender, message);

            String[] splitMessage = message.split(" ");

            if (message.charAt(0) == prefix) {
                onCommandMessage(sender, target, splitMessage[0].substring(1),
                        Arrays.copyOfRange(splitMessage, 1, splitMessage.length));

            } else {
                onMessage(sender, target, message);
            }

            break;
        }
        case BanchoPackets.lobbyJoin: {
            int userId = BitConverter.toInt32(reader.readNBytes(4), 0);

            boolean isExisting = false;
            for (Integer userIdToRemove : allPlayersInLobby) {
                if (userIdToRemove.intValue() == userId) {
                    userIdToRemove = userId;
                    isExisting = true;
                }
            }

            if (!isExisting) {
                allPlayersInLobby.add(userId);
            }

            this.console.allPlayersInLobby = this.allPlayersInLobby;

            break;
        }
        case BanchoPackets.lobbyPart: {
            int userId = BitConverter.toInt32(reader.readNBytes(4), 0);

            List<Integer> playersInLobbyCopy = new ArrayList<Integer>(allPlayersInLobby);

            for (Integer userIdToRemove : playersInLobbyCopy) {
                if (userIdToRemove.intValue() == userId)
                    allPlayersInLobby.remove(userIdToRemove);
            }

            this.console.allPlayersInLobby = this.allPlayersInLobby;

            break;
        }
        case BanchoPackets.matchNew: {
            byte matchId = reader.readByte();
            boolean inProgress = reader.readBoolean();
            byte matchType = reader.readByte();
            int mods = reader.readUnsignedShort();
            String gameName = Uleb128.readString(reader);
            String beatmapName = Uleb128.readString(reader);
            int beatmapId = BitConverter.toInt32LE(reader.readNBytes(4), 0);
            String beatmapChecksum = Uleb128.readString(reader);

            Match.Slot[] slots = new Match.Slot[8];
            for (int i = 0; i < 8; i++) {
                Match.Slot slot = new Match.Slot();
                slot.status = reader.readByte();
                slots[i] = slot;
            }

            for (Match.Slot slot : slots) {
                if (!slot.getStatuses().contains(Slot.SlotStatus.Open)
                        && !slot.getStatuses().contains(Slot.SlotStatus.Locked)) {
                    slot.userId = BitConverter.toInt32(reader.readNBytes(4), 0);
                }
            }

            Match match = new Match(matchId, inProgress, matchType, mods, gameName, beatmapName, beatmapId,
                    beatmapChecksum, slots);

            boolean isExisting = false;
            for (Match oldMatch : allMultiplayerMatches) {
                if (oldMatch.matchId == match.matchId) {
                    int oldMatchesIndex = allMultiplayerMatches.indexOf(oldMatch);
                    allMultiplayerMatches.set(oldMatchesIndex, match);
                    isExisting = true;

                    break;
                }
            }

            if (!isExisting) {
                allMultiplayerMatches.add(match);
            }

            this.console.multiplayerMatches = this.allMultiplayerMatches;

            break;
        }
        case BanchoPackets.matchDisband: {
            byte matchId = reader.readByte();

            List<Match> multiplayerMatchesCopy = new ArrayList<Match>(allMultiplayerMatches);

            for (Match match : multiplayerMatchesCopy) {
                if (match.matchId == matchId) {
                    allMultiplayerMatches.remove(match);

                    break;
                }
            }

            this.console.multiplayerMatches = this.allMultiplayerMatches;

            break;
        }
        case BanchoPackets.handleOsuUpdate: {
            Player thisPlayer = new Player();

            thisPlayer.userId = reader.readInt();
            byte clientType = reader.readByte();

            thisPlayer.status = reader.readByte();
            boolean beatmapUpdate = reader.readBoolean();

            if (beatmapUpdate) {
                thisPlayer.statusText = Uleb128.readString(reader);
                thisPlayer.mapChecksum = Uleb128.readString(reader);
                thisPlayer.enabledMods = reader.readUnsignedShort();
            }

            if (clientType > 0) {
                thisPlayer.rankedScore = BitConverter.toInt64LE(reader.readNBytes(8), 0);
                thisPlayer.accuracy = reader.readFloat();
                thisPlayer.playcount = BitConverter.toInt32(reader.readNBytes(4), 0);
                thisPlayer.totalScore = BitConverter.toInt64LE(reader.readNBytes(8), 0);
                thisPlayer.rank = BitConverter.toUInt16LE(reader.readNBytes(2), 0);
            }

            if (clientType == 2) {
                thisPlayer.username = Uleb128.readString(reader);
                thisPlayer.location = Uleb128.readString(reader);
                thisPlayer.timezone = reader.readUnsignedByte();
                thisPlayer.country = Uleb128.readString(reader);
            }

            boolean isExisting = false;
            for (Player playerToCheck : allOnlinePlayers) {
                if (playerToCheck.userId == thisPlayer.userId) {
                    if (!beatmapUpdate) {
                        Player tempPlayer = SerializationUtils.clone(playerToCheck);

                        tempPlayer.rankedScore = thisPlayer.rankedScore;
                        tempPlayer.accuracy = thisPlayer.accuracy;
                        tempPlayer.playcount = thisPlayer.playcount;
                        tempPlayer.totalScore = thisPlayer.totalScore;
                        tempPlayer.rank = thisPlayer.rank;

                        Collections.replaceAll(allOnlinePlayers, playerToCheck, tempPlayer);
                    } else {
                        Collections.replaceAll(allOnlinePlayers, playerToCheck, thisPlayer);
                    }
                    isExisting = true;

                    break;
                }
            }

            if (!isExisting)
                allOnlinePlayers.add(thisPlayer);

            this.console.allOnlinePlayers = this.allOnlinePlayers;
            this.console.isSame = false;

            break;

        }
        case BanchoPackets.handleOsuDisconnect: {
            int userId = BitConverter.toInt32(reader.readNBytes(4), 0);

            List<Player> onlinePlayersCopy = new ArrayList<Player>(allOnlinePlayers);

            for (Player playerToRemove : onlinePlayersCopy) {
                if (playerToRemove.userId == userId)
                    allOnlinePlayers.remove(playerToRemove);
            }

            this.console.allOnlinePlayers = this.allOnlinePlayers;
            this.console.isSame = false;

            break;
        }
        }

        Thread.sleep(50);
    }

    /**
     * This runs when a message is sent anywhere in game chat
     * 
     * @param sender  This is the sender of the message
     * @param target  This is the target of the message
     * @param message This is the message contents
     */
    public abstract void onMessage(String sender, String target, String message);

    /**
     * This runs when a message is sent with your bots prefix at the start
     * 
     * @param sender    The sender of the message
     * @param target    The target of the message
     * @param command   The command run
     * @param arguments The arguments of the command
     */
    public abstract void onCommandMessage(String sender, String target, String command, String[] arguments);

    /**
     * This runs when authentication is complete and the bot is connected
     */
    public abstract void onAuthComplete();

    /**
     * Runs when the bot disconnects from the server
     */
    public abstract void onBotDisconnect();

    void stoptimers() {
        try {
            keepaliveTimer.cancel();
        } catch (IllegalStateException ex) {

        }
    }

    private void startTimers() {
        keepaliveTimer = new Timer();

        // Schedule the keepalive packet
        keepaliveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (authenticated)
                    packetSender.sendHeartbeat();
            }
        }, 25000, 25000);
    }

    /**
     * Disconnects the bot from the server
     */
    public void disconnect() {
        packetSender.disconnectFromServer();

        keepaliveTimer.cancel();

        readForPacketsThread.stop();

        onBotDisconnect();
    }

    /**
     * This connects the bot to the server
     */
    public void connect() {
        try {
            this.client.connectBlocking();

            this.connected = true;

            this.console.printMessage("Authenticating...");

            this.client.send(String.format("%s\r\n%s\r\n999999|%d|%s\r\n", this.username, this.password,
                    Time.getCurrentTimezone(), this.showLocationData ? 1 : 0));

            this.lastPingTime = Time.now();
        } catch (InterruptedException ex) {
            this.console.printError("Connection to Flandrecho Failed! Reason: " + ex.getMessage());

            stoptimers();
            onBotDisconnect();

            try {
                this.client = new Client(new URI(this.ip), this);
                this.client.setTcpNoDelay(true);
                Thread.sleep(1000);
            } catch (Exception e) {
            }

            connect();
        }
    }
}
