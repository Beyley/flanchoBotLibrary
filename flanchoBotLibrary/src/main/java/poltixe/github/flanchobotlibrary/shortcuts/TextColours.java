package poltixe.github.flanchobotlibrary.shortcuts;

import java.util.*;

/**
 * Handles all text colour related things, aka colouring the console
 * 
 * @author Beyley Thomas
 */
public class TextColours {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[38;5;0m";
    public static final String RED = "\u001B[38;5;9m";
    public static final String ORANGE = "\u001B[38;5;208m";
    public static final String GREEN = "\u001B[38;5;10m";
    public static final String YELLOW = "\u001B[38;5;11m";
    public static final String BLUE = "\u001B[38;5;20m";
    public static final String LIGHTBLUE = "\u001B[38;5;14m";
    public static final String PURPLE = "\u001B[38;5;55m";
    public static final String WHITE = "\u001B[38;5;15m";
    public static final String PINK = "\u001B[38;5;213m";

    /**
     * Rainbow colours, aka pride flag
     */
    public static final String[] RAINBOW = { RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE };
    /**
     * The trans pride flag
     */
    public static final String[] TRANS = { LIGHTBLUE, PINK, WHITE, PINK, LIGHTBLUE };
    /**
     * The lesbian pride flag
     */
    public static final String[] LESBIAN = { RED, ORANGE, WHITE, PINK, PURPLE };
    /**
     * The colours snow wanted
     */
    public static final String[] SNOW = { PURPLE, WHITE };

    /**
     * Will stretch an array of colours to the size
     * 
     * @param colours The colour array to strect
     * @param size    The size of the stretched array
     * @return The stretched array
     */
    public static String[] stretchColours(String[] colours, int size) {
        if (size % colours.length == 0) {
            List<String> newColours = new ArrayList<String>();

            int multiple = size / colours.length;

            for (Object colour : colours)
                for (int i = 1; i <= multiple; i++)
                    newColours.add((String) colour);

            colours = Arrays.copyOf(newColours.toArray(), newColours.toArray().length, String[].class);
        } else {
            List<String> newColours = new ArrayList<String>(size);

            for (int i = 1; i < size - 1; i++) {
                // How far along are we?
                double srcpos = i * (double) (colours.length - 1) / (double) (size - 1);

                // Starting source element:
                int srcel = (int) srcpos;

                // Alpha/beta fraction.
                srcpos -= srcel;

                newColours.add((String) colours[srcel]);
            }

            colours = Arrays.copyOf(newColours.toArray(), newColours.toArray().length, String[].class);
        }

        return colours;
    }
}
