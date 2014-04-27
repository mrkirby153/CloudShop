package me.mrkirby153.plugins.cloudshop.shop.gui;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.shop.CloudShopItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListedItemsGUI extends BukkitRunnable implements Listener {
    private Inventory inventory;
    private Plugin plugin;
    private Player player;
    private int page = 1;

    private ArrayList<ItemStack> noClick = new ArrayList<ItemStack>();
    private ItemStack pgUp;
    private ItemStack pgDwn;

    public ListedItemsGUI(Player holder, Plugin plugin) {
        this.inventory = Bukkit.createInventory(null, 54, "Listed Items:");
        holder.openInventory(this.inventory);
        this.player = holder;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        runTaskTimer(plugin, 1L, 20L);
    }

    public void run() {
        if (this.inventory.getViewers().isEmpty()) {
            cancel();
            return;
        }
        this.inventory.clear();
        int lowerLimit;
        if (page <= 1)
            lowerLimit = 0;
        else
            lowerLimit = ((page - 1) * 54);
        int upperLimit = lowerLimit + 54;
        ResultSet rs = CloudShop.mysql().query("SELECT * FROM cs_items WHERE seller = '" + player.getName() + "' LIMIT " + lowerLimit + ", " + upperLimit, true);
        try {
            while (rs.next()) {
                ItemStack item = new CloudShopItem(rs.getInt("id")).recosntruct();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if (lore == null)
                    lore = new ArrayList<String>();
                lore.add(ChatColor.AQUA + "----------");
                lore.add(ChatColor.GREEN + "Listed on: " + rs.getTimestamp("dateListed").toString());
                lore.add(ChatColor.GREEN + "Price: " + ChatColor.GOLD + rs.getString("cost"));
                if (rs.getBoolean("bought")) {
                    lore.add(ChatColor.RED + "" + ChatColor.MAGIC + "aaa" + ChatColor.RESET + ChatColor.AQUA + " SOLD! " + ChatColor.RED + ChatColor.MAGIC + "aaa");
                    lore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.UNDERLINE + "Bought by: " + rs.getString("boughtBy"));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
                inventory.addItem(item);
                noClick.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ItemStack nullItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
        ItemMeta nullMeta = nullItem.getItemMeta();
        nullMeta.setDisplayName(Integer.toString(page));
        nullItem.setItemMeta(nullMeta);
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null)
                inventory.setItem(i, nullItem);
        }
        noClick.add(nullItem);
        // Page forward/backward
        pgUp = new ItemStack(Material.ARROW);
        ItemMeta forwardMeta = pgUp.getItemMeta();
        forwardMeta.setDisplayName(ChatColor.GOLD + "Go to page: " + Integer.toString(page + 1));
        pgUp.setItemMeta(forwardMeta);
        inventory.setItem(53, pgUp);
        noClick.add(pgUp);

        if (page > 1) {
            pgDwn = new ItemStack(Material.ARROW);
            ItemMeta bkmeta = pgDwn.getItemMeta();
            bkmeta.setDisplayName(ChatColor.GOLD + "Go to page: " + Integer.toString(page - 1));
            pgDwn.setItemMeta(bkmeta);
            inventory.setItem(45, pgDwn);
            noClick.add(pgDwn);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (noClick.contains(event.getCurrentItem()))
                event.setCancelled(true);
            //TODO: Fix pagination
            if (event.getCurrentItem().equals(pgDwn)) {
                page -= 1;
            }
            if (event.getCurrentItem().equals(pgUp)) {
                page += 1;
            }
        }
    }
}
