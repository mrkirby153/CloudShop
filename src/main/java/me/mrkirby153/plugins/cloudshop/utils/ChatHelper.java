package me.mrkirby153.plugins.cloudshop.utils;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Level;

public class ChatHelper {

    private static CloudShop plugin = CloudShop.instance();

    public static void sendToPlayer(Player p, String message) {
        if(p == null)
            return;
        p.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "CloudShop" + ChatColor.WHITE + "] " + message);
    }

    public static void sendToPlayer(UUID uuid, String message) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null)
            sendToPlayer(p, message);
    }

    @SuppressWarnings("unused")
    public static void sendToConsole(Level level, String message) {
        plugin.getLogger().log(level, ChatColor.stripColor(message));
    }

    public static void sendToConsole(String message) {
        plugin.getLogger().info(ChatColor.stripColor(message));
    }

    public static void send(CommandSender sender, String message) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            sendToPlayer(p, message);
        } else {
            sendToConsole(message);
        }
    }
}
