package org.rammex.factionflymodule.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.rammex.factionflymodule.data.DbManager;
import org.rammex.factionflymodule.util.MessagesConfigManager;

public class FlyTimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("factionflymodule.flytime")) {
            sender.sendMessage(MessagesConfigManager.getMessage("messages.noPerm"));
            return true;
        }

        if (args.length != 2) {
            if(sender instanceof Player){
                sender.sendMessage(MessagesConfigManager.getMessage("messages.flytimeusage"));
            }

            return false;
        }

        try {
            int seconds = Integer.parseInt(args[0]);
            String playerName = args[1];

            DbManager.addPlayerTime(playerName, seconds);
            if(sender instanceof Player) {
                sender.sendMessage(MessagesConfigManager.getMessage("messages.timeadded").replace("%time%", String.valueOf(seconds)).replace("%player%", playerName));
            }
        } catch (NumberFormatException e) {
            if(sender instanceof Player){
                sender.sendMessage(MessagesConfigManager.getMessage("messages.flytimeusage"));
            }
            return false;
        }

        return true;
    }
}