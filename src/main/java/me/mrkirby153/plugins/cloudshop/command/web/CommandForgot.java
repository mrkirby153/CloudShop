package me.mrkirby153.plugins.cloudshop.command.web;

import me.mrkirby153.plugins.cloudshop.command.BaseCommand;
import me.mrkirby153.plugins.cloudshop.shop.CloudShopper;
import me.mrkirby153.plugins.cloudshop.shop.Shoppers;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandForgot extends BaseCommand {
    public CommandForgot() {
        super("forgot", "Gives you a new regestration code", "%all%");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(notPlayer);
            return;
        }
        Player p = (Player) sender;
        CloudShopper cs = Shoppers.findShopperByName(p.getName());
        String code = Shoppers.forgot(cs.getName());
        ChatHelper.sendToPlayer(p, ChatColor.GOLD + "Your new link code is " + ChatColor.GREEN + "[" + code + "]");
    }
}
