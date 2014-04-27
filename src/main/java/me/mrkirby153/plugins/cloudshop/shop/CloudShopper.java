package me.mrkirby153.plugins.cloudshop.shop;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

public class CloudShopper {
    private Player player;


    public CloudShopper(String playerName) {
        this.player = Bukkit.getPlayerExact(playerName);
    }

    public CloudShopper(Player player) {
        this.player = player;
    }

    public String getName() {
        return this.player.getName();
    }


    public boolean isLinked() {
        try {
            ResultSet rs = CloudShop.mysql().query("SELECT * FROM `cs_users` WHERE ingame = '" + player.getName() + "'");
            if (rs.next()) {
                String username = rs.getString("ingame");
                String linkCode = rs.getString("linkCode");
                rs.close();
                rs = CloudShop.mysql().query("SELECT * FROM `cs_link` WHERE username = '" + username + "' AND linkCode = '" + linkCode + "'");
                if (rs.next()) {
                    return rs.getString("username").equalsIgnoreCase(username);
                }
            }
        } catch (SQLException e) {
            ChatHelper.sendToConsole(Level.SEVERE, "Error occured when executing query! [" + e.getMessage() + "]");
        }
        return false;
    }

    public static void listItem(ItemStack itemStack, String playerName, int cost) {
        ItemStack item = itemStack.clone();
        ItemMeta meta = item.getItemMeta();
        String name = (item.getItemMeta().getDisplayName() == null) ? "" : item.getItemMeta().getDisplayName();
        int count = item.getAmount();
        short damage = item.getDurability();

        // Encode Enchantments
        StringBuilder sb = new StringBuilder();
        String enchs = "";
        if (item.getEnchantments().size() > 0) {
            for (Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
                sb.append(ench.getKey().getName() + ":" + ench.getValue() + ",");
            }
            enchs = sb.toString();
            enchs = enchs.substring(0, enchs.length() - 1);
        }
        sb = new StringBuilder();
        if (meta.getLore() != null)
            if (meta.getLore().size() > 0) {
                for (String s : meta.getLore()) {
                    sb.append(s + "`");
                }
            }
        String lore = sb.toString();
        // Execute Query
        PreparedStatement prepared = CloudShop.mysql().prepareStatement("INSERT INTO cs_items (`seller`, `cost`, `material`, `damage`, `count`, `name`, `enchantments`, `lore`, `dateListed`, `bought`, `boughtby`, `removed`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            prepared.setString(1, playerName);
            prepared.setInt(2, cost);
            prepared.setString(3, item.getType().toString());
            prepared.setShort(4, damage);
            prepared.setInt(5, count);
            prepared.setString(6, name);
            prepared.setString(7, enchs);
            prepared.setString(8, lore);
            prepared.setTimestamp(9, new Timestamp(new Date().getTime()));
            prepared.setString(10, "0");
            prepared.setString(11, "");
            prepared.setString(12, "0");
            prepared.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Player p = Bukkit.getPlayerExact(playerName);
        String mat = WordUtils.capitalizeFully(item.getType().toString().replace("_", " "));
        if (name == "")
            name = item.getItemMeta().getDisplayName();
        ChatHelper.sendToPlayer(p, ChatColor.GREEN + "Listed " + ChatColor.GOLD + name + ChatColor.GREEN + " ("
                + ChatColor.GOLD + mat + ChatColor.GREEN + ") for " + ChatColor.GOLD + cost
                + CloudShop.instance().economy.currencyNamePlural() + ChatColor.GREEN + "!");
        p.getInventory().removeItem(itemStack);
        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 2);
    }
}
