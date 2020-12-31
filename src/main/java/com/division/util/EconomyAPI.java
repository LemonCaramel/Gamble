package com.division.util;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyAPI {

    private static EconomyAPI instance;
    private Economy economy;

    static {
        instance = new EconomyAPI();
    }

    private EconomyAPI(){
        if (!setupEconomy())
            Bukkit.getLogger().info("Can't find Vault and Economy - Gamble");
    }

    public static EconomyAPI getInstance(){
        return instance;
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public void giveMoney(Player target, double money){
        OfflinePlayer value = Bukkit.getOfflinePlayer(target.getUniqueId());
        economy.depositPlayer(value, money);
    }

    public double getMoney(Player target){
        OfflinePlayer value = Bukkit.getOfflinePlayer(target.getUniqueId());
        return economy.getBalance(value);
    }

    public void steelMoney(Player target, double money){
        OfflinePlayer value = Bukkit.getOfflinePlayer(target.getUniqueId());
        economy.withdrawPlayer(value, money);
    }


}
