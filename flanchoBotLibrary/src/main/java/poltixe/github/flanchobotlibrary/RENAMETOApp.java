package poltixe.github.flanchobotlibrary;

import java.io.*;

public class RENAMETOApp {
    public static void main(String[] args) {
        TestBot flanchoBot = new TestBot("", "", '@');

        flanchoBot.showLocationData = true;
        flanchoBot.ip = "";

        try {
            flanchoBot.initialize();
        } catch (InterruptedException | IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
}
