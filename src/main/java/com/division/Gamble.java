package com.division;

import com.division.command.GambleCommand;
import com.division.command.GambleTabCompleter;
import com.division.data.StockManager;
import com.division.file.ConfigManager;
import com.division.file.GambleLogger;
import com.division.file.StockFileManager;
import com.division.listener.InventoryClickListener;
import com.division.listener.InventoryCloseListener;
import com.division.listener.ZombieEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Gamble extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Gamble Enabled");
        getCommand("gamble").setExecutor(new GambleCommand(this));
        getCommand("gamble").setTabCompleter(new GambleTabCompleter());
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        // Bukkit.getPluginManager().registerEvents(new ZombieEventListener(), this);
        ConfigManager.getInstance().loadData();
        GambleLogger.getInstance().logging();
        StockFileManager.getInstance().load();
    }

    @Override
    public void onDisable() {
        getLogger().info("Gamble Disabled");
        ConfigManager.getInstance().saveData();
        GambleLogger.getInstance().forceSaveLog();
        StockFileManager.getInstance().save();
    }


}
