package me.mrkirby153.plugins.cloudshop.shop.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ListedItemsGUI extends BukkitRunnable implements Listener {
    private Inventory inventory;
    private Plugin plugin;
    private int page;

    public ListedItemsGUI(Player holder, Plugin plugin) {
        this.inventory = Bukkit.createInventory(null, 54, "Listed Items:");
        holder.openInventory(this.inventory);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        runTaskTimer(plugin, 1L, 20L);
    }

    public void run() {
        if (this.inventory.getViewers().isEmpty()) {
            cancel();
            return;
        }
        this.inventory.clear();
        int lowerLimit = (page - 1) * 54;
        int upperLimit = lowerLimit + 54;

    }
}
