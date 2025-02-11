package org.rammex.factionflymodule.placeholder;

import org.rammex.factionflymodule.data.DbManager;

import java.util.concurrent.TimeUnit;

public class PlaceholderManager {

    public static String formatTime(int seconds) {
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    public static String getPlayerFormattedTime(String playerName) {
        int timeInSeconds = DbManager.getPlayerTime(playerName);
        return formatTime(timeInSeconds);
    }
}