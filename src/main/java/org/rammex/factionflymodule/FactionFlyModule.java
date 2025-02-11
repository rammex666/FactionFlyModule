package org.rammex.factionflymodule;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.rammex.factionflymodule.commands.FlyCommand;
import org.rammex.factionflymodule.commands.FlyTimeCommand;
import org.rammex.factionflymodule.events.FlyEvent;
import org.rammex.factionflymodule.placeholder.FactionFlyPlaceholder;
import org.rammex.factionflymodule.task.FTimeTask;

public final class FactionFlyModule extends JavaPlugin {

    @Getter
    public static FactionFlyModule instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getCommand("ffly").setExecutor(new FlyCommand(this));
        this.getCommand("flytime").setExecutor(new FlyTimeCommand());

        this.getServer().getPluginManager().registerEvents(new FlyEvent(this), this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new FactionFlyPlaceholder(this).register();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new FTimeTask(), 20L, 20L);

    }

    @Override
    public void onDisable() {
    }
}
