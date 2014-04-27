package me.mrkirby153.plugins.cloudshop.command.web;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.command.BaseCommand;
import me.mrkirby153.plugins.cloudshop.command.Commands;
import me.mrkirby153.plugins.cloudshop.shop.CloudShopper;
import me.mrkirby153.plugins.cloudshop.shop.Shoppers;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLink extends BaseCommand {
    public CommandLink() {
        super("link", "Links your minecraft account with a cloudshop account", "%all%");
        Commands.addAlias(this, "register");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(notPlayer);
            return;
        }
        Player p = (Player) sender;
        CloudShopper cs = Shoppers.findShopperByName(p.getName());
        if (cs.isLinked()) {
            ChatHelper.sendToPlayer(p, ChatColor.DARK_GREEN + "You are already registered on this server!");
            p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
            return;
        }
        String code = Shoppers.registerNewPlayer(p.getName());
        ChatHelper.sendToPlayer(p, ChatColor.GOLD + "Please visit " + ChatColor.GREEN +
                CloudShop.instance().getConfig().getString("web.site") + "/register" + ChatColor.GOLD +
                " and input the code " + ChatColor.BLUE + "[" + code + "]" + ChatColor.GOLD + " when asked!");
        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
        return;
    }
}
