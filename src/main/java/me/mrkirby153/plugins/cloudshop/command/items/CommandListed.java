package me.mrkirby153.plugins.cloudshop.command.items;

import me.mrkirby153.plugins.cloudshop.command.BaseCommand;
import me.mrkirby153.plugins.cloudshop.command.Commands;
import me.mrkirby153.plugins.cloudshop.shop.gui.ListedItemsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListed extends BaseCommand {
    public CommandListed(){
        super("listed", "Shows a list of the items you have on sale", "%all%");
        Commands.addAlias(this, "selling");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(notPlayer);
            return;
        }
        Player p = (Player) sender;
        plugin.getServer().getPluginManager().registerEvents(new ListedItemsGUI(p, plugin), plugin);
    }
}
