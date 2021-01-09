package poltixe.github.flanchobotlibrary.shortcuts;

import java.time.*;
import java.time.format.*;
import java.util.*;

import poltixe.github.flanchobotlibrary.objects.*;

/**
 * Used for printing messages to console
 * 
 * @author Beyley Thomas
 */
public class PrintToConsole {
    /**
     * The bots name
     */
    String botName;

    List<String> lastMessageBuffer = new ArrayList<String>();
    List<String> messageBuffer = new ArrayList<String>();

    List<Match> lastMultiplayerMatches = new ArrayList<Match>();
    public List<Match> multiplayerMatches = new ArrayList<Match>();

    List<Integer> lastAllPlayersInLobby = new ArrayList<Integer>();
    public List<Integer> allPlayersInLobby = new ArrayList<Integer>();

    List<Player> lastAllOnlinePlayers = new ArrayList<Player>();
    public List<Player> allOnlinePlayers = new ArrayList<Player>();

    public Player getPlayerFromId(int userId) {
        for (Player player : allOnlinePlayers) {
            if (player.userId == userId)
                return player;
        }

        return null;
    }

    Thread consoleThread;

    public boolean isSame = false;

    /**
     * Creats a new PrintToConsole object
     * 
     * @param botName The bots name
     */
    public PrintToConsole(String botName) {
        this.botName = botName;
        this.namesToConvert[0][0] = this.botName;

        for (Object[] nameToConvert : this.namesToConvert) {
            nameToConvert[1] = (String) putColoursOnString((String) nameToConvert[0], (String[]) nameToConvert[2]);
        }

        consoleThread = new Thread() {
            public void run() {
                try {
                    while (true) {
                        if (messageBuffer.equals(lastMessageBuffer) && multiplayerMatches.equals(lastMultiplayerMatches)
                                && allPlayersInLobby.equals(lastAllPlayersInLobby)
                                && allOnlinePlayers.equals(lastAllOnlinePlayers)) {
                            isSame = true;
                        } else {
                            isSame = false;
                        }

                        if (isSame) {
                            Thread.sleep(100);
                        } else {
                            System.out.print("\033[H\033[2J");
                            System.out.flush();

                            String topSeparator = "--------%s--------\n";
                            String separator = "------------------";

                            System.out.printf(topSeparator, "Recent Logs");
                            for (int i = Math.max(messageBuffer.size() - 10, 0); i < messageBuffer.size(); i++) {
                                String message = messageBuffer.get(i);
                                System.out.println(message);
                            }
                            System.out.printf(topSeparator, "Multiplayer Matches");

                            for (Match match : multiplayerMatches) {
                                System.out.printf(TextColours.GREEN
                                        + "Match ID: %d, Match Name: %s, Game Started: %s, Map Name: %s Slot Info: %s\n"
                                        + TextColours.RESET, match.matchId, match.gameName, match.inProgress,
                                        match.beatmapName, match.getSlotsAsString(allOnlinePlayers));
                            }
                            System.out.printf(topSeparator, "Players in lobby");

                            for (Integer userId : allPlayersInLobby) {
                                Player player = getPlayerFromId(userId.intValue());
                                if (player == null) {
                                    System.out.printf(TextColours.GREEN + "%s\n" + TextColours.RESET,
                                            userId.intValue());
                                } else {
                                    System.out.printf(TextColours.GREEN + "%s\n" + TextColours.RESET, player.username);
                                }
                            }
                            System.out.printf(topSeparator, "Online Players");

                            for (Player player : allOnlinePlayers) {
                                System.out.printf(TextColours.GREEN + "(#%s) %s, %s %s\n" + TextColours.RESET,
                                        player.rank, player.username, player.getStatus(), player.statusText);
                            }
                            System.out.println(separator);

                            lastMessageBuffer = List.copyOf(messageBuffer);
                            lastMultiplayerMatches = List.copyOf(multiplayerMatches);
                            lastAllPlayersInLobby = List.copyOf(allPlayersInLobby);
                            lastAllOnlinePlayers = List.copyOf(allOnlinePlayers);
                        }
                    }
                } catch (InterruptedException ex) {
                }
            }
        };

        consoleThread.start();
    }

    /**
     * A 2D array containing all the names to make colourful along with the
     * converted names
     */
    Object[][] namesToConvert = { { botName, "", TextColours.TRANS }, { "ayyEve", "", TextColours.TRANS },
            { "PoltixeTheDerg", "", TextColours.RAINBOW }, { "Eevee", "", TextColours.LESBIAN },
            { "TranzPanic", "", TextColours.SNOW } };

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static void appendToIrcLog(String message) {
        FileHandler.appendToFile("irclog", message);
    }

    /**
     * Puts colours on a string
     * 
     * @param string  The string to colourize
     * @param colours An array containing all the colours to add
     * @return The string with the colours intertwined
     */
    public String putColoursOnString(String string, String[] colours) {
        String returnString = "";

        colours = TextColours.stretchColours(colours, string.length());

        int counter = 0;
        for (char thisChar : string.toCharArray()) {
            returnString += (String) colours[counter % colours.length] + thisChar;
            counter++;
        }

        return returnString;
    }

    /**
     * Prints an IRC message to chat
     * 
     * @param target  The target of the message, usually a channel
     * @param sender  The sender of the message
     * @param message The message contents
     */
    public void printIrcMessage(String target, String sender, String message) {
        LocalDateTime now = LocalDateTime.now();

        String finalMessage = String.format(TextColours.BLUE + "[%s] IRCLog: [%s] <%s>: %s" + TextColours.RESET,
                dtf.format(now), target, sender, message);

        for (Object[] nameToConvert : this.namesToConvert) {
            finalMessage = finalMessage.replaceAll("(?i)" + (String) nameToConvert[0],
                    (String) nameToConvert[1] + TextColours.BLUE);
        }

        appendToIrcLog(finalMessage);
        messageBuffer.add(finalMessage);
    }

    public void printError(String message) {
        LocalDateTime now = LocalDateTime.now();

        String finalMessage = String.format(TextColours.RED + "[%s] [*.*] " + message + TextColours.RESET,
                dtf.format(now));

        FileHandler.appendToErrorLog(finalMessage);
        messageBuffer.add(finalMessage);
    }

    public void printMessage(String message) {
        LocalDateTime now = LocalDateTime.now();

        String finalMessage = String.format(TextColours.YELLOW + "[%s] [*.*] " + message + TextColours.RESET,
                dtf.format(now));

        messageBuffer.add(finalMessage);
    }
}
