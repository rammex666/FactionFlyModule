package org.rammex.factionflymodule.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.rammex.factionflymodule.FactionFlyModule;

public class FactionFlyPlaceholder extends PlaceholderExpansion {

    private final FactionFlyModule plugin;

    public FactionFlyPlaceholder(FactionFlyModule plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "factionflymodule";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("flytime")) {
            return PlaceholderManager.getPlayerFormattedTime(player.getName());
        }

        return null;
    }
}