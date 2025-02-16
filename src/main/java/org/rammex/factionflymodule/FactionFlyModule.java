package org.rammex.factionflymodule;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.rammex.factionflymodule.commands.FlyCommand;
import org.rammex.factionflymodule.commands.FlyTimeCommand;
import org.rammex.factionflymodule.data.DbManager;
import org.rammex.factionflymodule.events.FlyEvent;
import org.rammex.factionflymodule.placeholder.FactionFlyPlaceholder;
import org.rammex.factionflymodule.task.FTimeTask;

import java.io.File;


public final class FactionFlyModule extends JavaPlugin {

    @Getter
    public static FactionFlyModule instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Register commands
        if (this.getCommand("ffly") != null) {
            this.getCommand("ffly").setExecutor(new FlyCommand(this));
        } else {
            getLogger().severe("Command 'ffly' not found in plugin.yml");
        }

        if (this.getCommand("flytime") != null) {
            this.getCommand("flytime").setExecutor(new FlyTimeCommand());
        } else {
            getLogger().severe("Command 'flytime' not found in plugin.yml");
        }

        // Register events
        this.getServer().getPluginManager().registerEvents(new FlyEvent(this), this);

        // Register PlaceholderAPI placeholder
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new FactionFlyPlaceholder(this).register();
        } else {
            getLogger().severe("PlaceholderAPI not found. Placeholders will not be available.");
        }

        // Initialize the database
        DbManager database = new DbManager("factionflymodule", new File(getDataFolder(), "data.db"));
        database.load();

        // Schedule the FlyTimeTask to run every second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new FTimeTask(), 20L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}