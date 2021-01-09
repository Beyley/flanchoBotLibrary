package poltixe.github.flanchobotlibrary.objects;

import java.io.Serializable;

import poltixe.github.flanchobotlibrary.packets.SendUserStatusPacket.Status;

public class Player implements Serializable {
    public int userId;

    public byte status;
    public String statusText;
    public String mapChecksum;
    public int enabledMods;

    public long rankedScore;
    public float accuracy;
    public int playcount;
    public long totalScore;
    public long rank;

    public String username;
    public String location;
    public int timezone;
    public String country;

    public Player() {
    }

    public Player(int userId, byte status, String statusText, String mapChecksum, int enabledMods, long rankedScore,
            float accuracy, int playcount, long totalScore, long rank, String username, String location, int timezone,
            String country) {

        this.userId = userId;
        this.status = status;
        this.statusText = statusText;
        this.mapChecksum = mapChecksum;
        this.enabledMods = enabledMods;
        this.rankedScore = rankedScore;
        this.accuracy = accuracy;
        this.playcount = playcount;
        this.totalScore = totalScore;
        this.rank = rank;
        this.username = username;
        this.location = location;
        this.timezone = timezone;
        this.country = country;
    }

    public String getStatus() {
        for (Status status : Status.values())
            if (status.value == this.status)
                return status.name();

        return "Unknown";
    }
}
