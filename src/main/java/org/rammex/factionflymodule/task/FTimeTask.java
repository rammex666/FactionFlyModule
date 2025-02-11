package org.rammex.factionflymodule.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.rammex.factionflymodule.FactionFlyModule;
import org.rammex.factionflymodule.commands.FlyCommand;
import org.rammex.factionflymodule.data.DbManager;
import org.rammex.factionflymodule.util.MessagesConfigManager;

import java.util.UUID;

public class FTimeTask implements Runnable {

    @Override
    public void run() {
        for (UUID playerUUID : FlyCommand.flyingPlayers) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null && player.isOnline()) {
                int remainingTime = DbManager.getPlayerTime(player.getName());
                if (remainingTime > 0) {
                    DbManager.remove1sPlayerTime(player.getName());
                } else {
                    player.setAllowFlight(false);
                    FlyCommand.flyingPlayers.remove(playerUUID);
                    player.sendMessage(MessagesConfigManager.getMessage("messages.timeexpired"));
                }
            }
        }
    }
}
