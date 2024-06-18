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

import java.util.HashSet;
import java.util.UUID;

public class FlyCommand implements CommandExecutor, Listener {
    private final FactionFlyModule plugin;
    private final HashSet<UUID> flyingPlayers;

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
                return true;
            }

            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            if (Board.getInstance().getFactionAt(new FLocation(player.getLocation())).equals(fPlayer.getFaction())) {
                UUID playerUUID = player.getUniqueId();
                if(flyingPlayers.contains(playerUUID)) {
                    player.setAllowFlight(false);
                    flyingPlayers.remove(playerUUID);
                    player.sendMessage("§8[§a✔§8] §7Vous avez désactivé le vol dans votre faction.");
                } else {
                    player.setAllowFlight(true);
                    flyingPlayers.add(playerUUID);
                    player.sendMessage("§8[§a✔§8] §7Vous avez activé le vol dans votre faction.");
                }
            } else {
                player.sendMessage("§8[§cX§8] §7Vous n'êtes pas dans vos claims.");
            }
        } catch (ClassCastException ignored) {
        }

        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (flyingPlayers.contains(playerUUID)) {
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            if (!Board.getInstance().getFactionAt(new FLocation(player.getLocation())).equals(fPlayer.getFaction())) {
                player.setAllowFlight(false);
                flyingPlayers.remove(playerUUID);
                player.sendMessage("§8[§cX§8] §7Vous avez quitté vos claims, le vol a été désactivé.");
            }
        }
    }
}