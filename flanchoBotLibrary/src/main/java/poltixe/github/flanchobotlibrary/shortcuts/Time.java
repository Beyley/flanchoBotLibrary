package poltixe.github.flanchobotlibrary.shortcuts;

import java.lang.management.ManagementFactory;
import java.time.*;
import java.util.*;

/**
 * Used for many time related things
 */
public class Time {
    /**
     * Gets the current time as a long
     * 
     * @return The current time
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * Gets the timezone as an int (ex. -8)
     * 
     * @return The timezone
     */
    public static int getCurrentTimezone() {
        return TimeZone.getTimeZone(ZoneId.systemDefault()).getOffset(Instant.now().toEpochMilli()) / 1000 / 60 / 60;
    }

    /**
     * Gets the uptime of the JVM
     * 
     * @return The uptime
     */
    public static long getJVMUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }
}
