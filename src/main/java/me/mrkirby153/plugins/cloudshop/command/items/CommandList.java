package me.mrkirby153.plugins.cloudshop.command.items;

import me.mrkirby153.plugins.cloudshop.command.BaseCommand;
import me.mrkirby153.plugins.cloudshop.command.Commands;
import me.mrkirby153.plugins.cloudshop.shop.CloudShopper;
import me.mrkirby153.plugins.cloudshop.shop.Shoppers;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandList extends BaseCommand {
    public CommandList() {
        super("list", "Puts the item in your hand up for sale", "%all%");
        Commands.addAlias(this, "sell");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(notPlayer);
            return;
        }
        Player p = (Player) sender;
        CloudShopper cs = Shoppers.findShopperByUUID(p.getUniqueId());
        if (cs == null) {
            ChatHelper.sendToPlayer(p, ChatColor.RED + "You must register via " + ChatColor.GOLD + "/cloudshop link");
            return;
        }
        if (args.length == 0) {
            ChatHelper.sendToPlayer(p, ChatColor.GOLD + "You must specify a price!");
            return;
        }
        if (args.length == 1) {
            if (!StringUtils.isNumeric(args[0])) {
                ChatHelper.sendToPlayer(p, ChatColor.GOLD + "\"" + args[0] + "\" is not numeric");
                return;
            }
            CloudShopper.listItem(p.getItemInHand(), p.getUniqueId(), Integer.parseInt(args[0]));
        }
    }
}
