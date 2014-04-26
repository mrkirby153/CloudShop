package me.mrkirby153.plugins.cloudshop;

import me.mrkirby153.plugins.cloudshop.command.CsExecutor;
import me.mrkirby153.plugins.cloudshop.listeners.ShopperListener;
import me.mrkirby153.plugins.cloudshop.shop.Shoppers;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import me.mrkirby153.plugins.cloudshop.utils.MySQL;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CloudShop extends JavaPlugin {
    private static CloudShop plugin;
    public Economy economy = null;
    private static MySQL mysql;

    public static CloudShop instance() {
        return plugin;
    }

    public static MySQL mysql() {
        return mysql;
    }

    public void onDisable() {
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null)
            economy = economyProvider.getProvider();
        return (economy != null);
    }


    public void onEnable() {
        saveDefaultConfig();        // Save default configuration file
        plugin = this;              // Define pluign Instance
        // Initialize economy
        getLogger().info("Loading vault integeration...");
        if (!setupEconomy()) {
            getLogger().severe("##### Error #####");
            getLogger().severe("There was a problem configuration vault integeration. Please make sure you have vault isntalled!");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().info("Loaded Vault integration!");
        }
        // Connect to database
        mysql = new MySQL(getConfig());
        if (mysql.connectionErr()) {
            // Errors found in initialization
            getLogger().severe("#### Error ####");
            getLogger().severe("There was a problem while connecting to the database. Check the log for more info");
            getServer().getPluginManager().disablePlugin(this);
        }
        // Register Command
        getCommand("cloudshop").setExecutor(new CsExecutor());
        // Register Listeners
        getServer().getPluginManager().registerEvents(new ShopperListener(), this);
        new BukkitRunnable() {
            public void run() {
                int i = 0;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Shoppers.registerShopper(p);
                    i++;
                }
                if (i > 0)
                    ChatHelper.sendToConsole("Reloaded " + i + " player(s)!");
            }
        }.runTaskLater(this, 30L);
    }

}


