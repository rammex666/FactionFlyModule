package org.rammex.factionflymodule;

import org.bukkit.plugin.java.JavaPlugin;
import org.rammex.factionflymodule.commands.FlyCommand;

public final class FactionFlyModule extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("ffly").setExecutor(new FlyCommand(this));
        this.getServer().getPluginManager().registerEvents(new FlyCommand(this), this);

    }

    @Override
    public void onDisable() {
    }
}
