package poltixe.github.flanchobotlibrary.objects;

import java.util.*;

public class Match {
    public byte matchId;
    public boolean inProgress;
    public byte matchType;
    public int mods;
    public String gameName;
    public String beatmapName;
    public int beatmapId;
    public String beatmapChecksum;

    public Slot[] slots;

    public static class Slot {
        public static enum SlotStatus {
            /**
             * The status for an open slot
             */
            Open(1),
            /**
             * The status for a locked slot
             */
            Locked(2),
            /**
             * The status for a player that is not ready
             */
            NotReady(4),
            /**
             * The status for a player that is ready
             */
            Ready(8),
            /**
             * The status for a player that does not have the map
             */
            NoMap(16),
            /**
             * The status for a player who is currently ingame
             */
            Playing(32),
            /**
             * The status for
             */
            Complete(64),
            /**
              * 
              */
            CompHasPlayer(124);

            int value;

            SlotStatus(int status) {
                this.value = status;
            }
        }

        public byte status;
        public int userId = -1;

        public Slot() {
        }

        public List<SlotStatus> getStatuses() {
            List<SlotStatus> statuses = new ArrayList<SlotStatus>();

            for (SlotStatus statusToFind : SlotStatus.values()) {
                if ((this.status & statusToFind.value) == statusToFind.value) {
                    statuses.add(statusToFind);
                }
            }

            return statuses;
        }
    }

    public Match(byte matchId, boolean inProgress, byte matchType, int mods, String gameName, String beatmapName,
            int beatmapId, String beatmapChecksum, Slot[] slots) {
        this.matchId = matchId;
        this.inProgress = inProgress;
        this.matchType = matchType;
        this.mods = mods;
        this.gameName = gameName;
        this.beatmapName = beatmapName;
        this.beatmapId = beatmapId;
        this.beatmapChecksum = beatmapChecksum;

        this.slots = slots;
    }

    public Player getPlayerFromId(int userId, List<Player> allOnlinePlayers) {
        for (Player player : allOnlinePlayers) {
            if (player.userId == userId)
                return player;
        }

        return null;
    }

    public String getSlotsAsString(List<Player> allOnlinePlayers) {
        String finalString = "";

        for (Slot slot : slots) {
            Player player = getPlayerFromId(slot.userId, allOnlinePlayers);

            if (player == null) {
                finalString += slot.userId + " : ";
            } else {
                finalString += player.username + " : ";
            }

            for (Slot.SlotStatus status : slot.getStatuses()) {
                finalString += status + ", ";
            }
        }

        return finalString;
    }
}
