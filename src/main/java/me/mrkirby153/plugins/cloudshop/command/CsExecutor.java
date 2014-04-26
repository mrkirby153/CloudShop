package me.mrkirby153.plugins.cloudshop.command;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.shop.CloudShopper;
import me.mrkirby153.plugins.cloudshop.shop.Shoppers;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class CsExecutor implements CommandExecutor {
    private CloudShop plugin = CloudShop.instance();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length <= 0) {
            // TODO: Show help
            sender.sendMessage("TODO: Show help");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("link")) {
                if (!(sender instanceof Player)) {
                    ChatHelper.send(sender, "You must be a player to preform this command!");
                    return true;
                }
                Player p = (Player) sender;
                // Link
                CloudShopper cs = Shoppers.findShopperByName(p.getName());
                if (cs.isLinked()) {
                    ChatHelper.sendToPlayer(p, ChatColor.DARK_GREEN + "You are already registered on this server!");
                    p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
                    return true;
                } else {
                    String code = Shoppers.registerNewPlayer(p.getName());
                    ChatHelper.sendToPlayer(p, ChatColor.RED + "Please visit " + ChatColor.GOLD
                            + CloudShop.instance().getConfig().getString("web.site") + "/register" + ChatColor.RED + " and input" +
                            " the code " + ChatColor.BLUE + code + ChatColor.RED + " when asked.");
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("forgot")) {
                if (!(sender instanceof Player)) {
                    ChatHelper.send(sender, "You must be a player to preform this command!");
                    return true;
                }
                Player p = (Player) sender;
                // Link
                CloudShopper cs = Shoppers.findShopperByName(p.getName());
                String code = Shoppers.forgot(cs.getName());
                ChatHelper.sendToPlayer(p, ChatColor.GOLD + "Youre new link code is '" + ChatColor.GREEN + code + ChatColor.GOLD + "'");
                return true;

            }
            if(args[0].equalsIgnoreCase("listed")){
                if(!(sender instanceof Player)){
                    ChatHelper.send(sender, "You must be a player to preform this command!");
                    return true;
                }
                Player p = (Player) sender;
            }
            if (args[0].equalsIgnoreCase("list")) {
                if (!(sender instanceof Player)) {
                    ChatHelper.send(sender, "You must be a player to preform this command!");
                    return true;
                }
                Player p = (Player) sender;
                CloudShopper cs = Shoppers.findShopperByName(p.getName());
                if (!cs.isLinked()) {
                    ChatHelper.sendToPlayer(p, ChatColor.RED + "Error: You must register via " + ChatColor.GOLD + "/cloudshop link");
                    return true;
                }
                ChatHelper.sendToPlayer(p, ChatColor.GOLD + "You must specify a cost!");
                return true;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("list")) {
                if (!(sender instanceof Player)) {
                    ChatHelper.send(sender, "You must be a player to preform this command!");
                    return true;
                }
                Player p = (Player) sender;
                CloudShopper cs = Shoppers.findShopperByName(p.getName());
                if (!cs.isLinked()) {
                    ChatHelper.sendToPlayer(p, ChatColor.RED + "Error: You must reigster via " + ChatColor.GOLD + "/cloudshop link");
                    return true;
                }
                if (!StringUtils.isNumeric(args[1])) {
                    ChatHelper.sendToPlayer(p, ChatColor.DARK_PURPLE + args[1] + ChatColor.RED + " is not a number!");
                    return true;
                }
                listItem(p.getItemInHand(), p.getName(), Integer.parseInt(args[1]));
                return  true;
            }
        }
        ChatHelper.send(sender, ChatColor.RED + "Unknown Command!");
        return true;
    }

    private static void listItem(ItemStack itemStack, String playerName, int cost) {
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
                    sb.append(s + "\n");
                }
            }
        String lore = sb.toString();
        // Execute Query
        PreparedStatement prepared = CloudShop.mysql().prepareStatement("INSERT INTO cs_items (`seller`, `cost`, `material`, `damage`, `count`, `name`, `enchantments`, `lore`, `dateListed`, `bought`, `boughtby`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
