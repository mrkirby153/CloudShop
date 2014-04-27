package me.mrkirby153.plugins.cloudshop.command;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CsExecutor implements CommandExecutor {
    private CloudShop plugin = CloudShop.instance();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        
        ChatHelper.send(sender, ChatColor.RED + "Unknown Command!");
        return true;
    }


}
