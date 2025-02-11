package org.rammex.factionflymodule.commands;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.rammex.factionflymodule.FactionFlyModule;
import org.rammex.factionflymodule.data.DbManager;
import org.rammex.factionflymodule.util.MessagesConfigManager;

import java.util.HashSet;
import java.util.UUID;

public class FlyCommand implements CommandExecutor, Listener {
    private final FactionFlyModule plugin;
    public static HashSet<UUID> flyingPlayers;

    public FlyCommand(FactionFlyModule plugin) {
        this.plugin = plugin;
        this.flyingPlayers = new HashSet<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player player = (Player) sender;
            if (!player.hasPermission("factionflymodule.fly")) {
                player.sendMessage(MessagesConfigManager.getMessage("messages.noPerm"));
                return true;
            }

            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            if (Board.getInstance().getFactionAt(new FLocation(player.getLocation())).equals(fPlayer.getFaction())) {
                UUID playerUUID = player.getUniqueId();
                if(flyingPlayers.contains(playerUUID)) {
                    player.setAllowFlight(false);
                    flyingPlayers.remove(playerUUID);
                    player.sendMessage(MessagesConfigManager.getMessage("messages.desactivated"));
                } else {
                    if(DbManager.getPlayerTime(player.getName()) <= 0) {
                        player.sendMessage(MessagesConfigManager.getMessage("messages.notime"));
                        return true;
                    }
                    player.setAllowFlight(true);
                    flyingPlayers.add(playerUUID);
                    player.sendMessage(MessagesConfigManager.getMessage("messages.activated"));
                }
            } else {
                player.sendMessage(MessagesConfigManager.getMessage("messages.notinclaim"));
            }
        } catch (ClassCastException ignored) {
        }
        return true;
    }


}