package me.mrkirby153.plugins.cloudshop.command;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.shop.CloudShopper;
import me.mrkirby153.plugins.cloudshop.shop.Shoppers;
import me.mrkirby153.plugins.cloudshop.shop.gui.ListedItemsGUI;
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
                plugin.getServer().getPluginManager().registerEvents(new ListedItemsGUI(p, plugin), plugin);
                return true;
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
                CloudShopper.listItem(p.getItemInHand(), p.getName(), Integer.parseInt(args[1]));
                return  true;
            }
            if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("unlist")){
                //TODO: Deleting items
            }
        }
        ChatHelper.send(sender, ChatColor.RED + "Unknown Command!");
        return true;
    }


}
