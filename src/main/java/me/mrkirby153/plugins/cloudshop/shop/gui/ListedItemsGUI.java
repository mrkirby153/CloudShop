package me.mrkirby153.plugins.cloudshop.shop.gui;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.shop.CloudShopItem;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListedItemsGUI implements Listener {
    private Inventory inventory;
    private Player player;

    private ArrayList<ItemStack> noClick = new ArrayList<ItemStack>();
    private ItemStack more;

    public ListedItemsGUI(Player holder, Plugin plugin) {
        this.inventory = Bukkit.createInventory(null, 54, "Listed Items: ");
        holder.openInventory(this.inventory);
        this.player = holder;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
//        runTaskTimer(plugin, 1L, 20L);
        populate();
    }

    public void populate() {
        this.inventory.clear();
        ResultSet rs = CloudShop.mysql().query("SELECT * FROM cs_items WHERE seller = '" + player.getName() + "' AND removed = '0' LIMIT 0, 45", true);
        try {
            while (rs.next()) {
                ItemStack item = new CloudShopItem(rs.getInt("id")).recosntruct();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if (lore == null)
                    lore = new ArrayList<String>();
                lore.add(ChatColor.AQUA + "----------");
                lore.add(ChatColor.GREEN+"ID: "+ChatColor.GOLD+rs.getInt("id"));
                lore.add(ChatColor.GREEN + "Listed on: " + rs.getTimestamp("dateListed").toString());
                lore.add(ChatColor.GREEN + "Price: " + ChatColor.GOLD + rs.getString("cost"));
                if (rs.getBoolean("bought")) {
                    lore.add(ChatColor.RED + "" + ChatColor.MAGIC + "aaa" + ChatColor.RESET + ChatColor.AQUA + " SOLD! " + ChatColor.RED + ChatColor.MAGIC + "aaa");
                    lore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.UNDERLINE + "Bought by: " + rs.getString("boughtBy"));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
                inventory.addItem(item);
                storeItems.put(item, rs.getInt("id"));
                noClick.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        more = new ItemStack(Material.DIAMOND);
        ItemMeta moreMeta = more.getItemMeta();
        moreMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "View More Items");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.WHITE + "Click to view more items online!");
        moreMeta.setLore(lore);
        more.setItemMeta(moreMeta);
        inventory.setItem(49, more);
        noClick.add(more);

        ItemStack nullItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
        ItemMeta nullMeta = nullItem.getItemMeta();
        nullMeta.setDisplayName(" ");
        nullItem.setItemMeta(nullMeta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null)
                inventory.setItem(i, nullItem);
        }
        noClick.add(nullItem);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().equals(more)) {
                ChatHelper.sendToPlayer((Player) event.getWhoClicked(), ChatColor.GOLD + "View more items here: @WEBSITE@");
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_PLING, 1, 0.5F);
                event.getWhoClicked().closeInventory();
                event.setCancelled(true);
                new BukkitRunnable() {
                    public void run() {
                        cleanup();
                    }
                }.runTask(CloudShop.instance());
                return;
            }

        }
        if (noClick.contains(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        cleanup();
    }

    private void cleanup() {
        HandlerList.unregisterAll(this);
        inventory = null;
        player = null;
        more = null;
    }
}
