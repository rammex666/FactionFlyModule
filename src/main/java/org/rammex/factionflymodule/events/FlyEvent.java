package org.rammex.factionflymodule.events;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.rammex.factionflymodule.FactionFlyModule;

import java.util.UUID;

import static org.rammex.factionflymodule.commands.FlyCommand.flyingPlayers;

public class FlyEvent implements Listener {
    FactionFlyModule plugin;

    public FlyEvent(FactionFlyModule plugin) {
        this.plugin = plugin;
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
