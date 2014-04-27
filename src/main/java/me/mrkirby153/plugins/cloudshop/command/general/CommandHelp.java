package me.mrkirby153.plugins.cloudshop.command.general;

import me.mrkirby153.plugins.cloudshop.command.BaseCommand;
import me.mrkirby153.plugins.cloudshop.command.Commands;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandHelp extends BaseCommand {

    public CommandHelp() {
        super("help", "Shows this help message", "%all%");
        Commands.addAlias(this, "?");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length == 1) {
            page = Integer.parseInt(args[0]);
        }
        if (sender instanceof Player) {
            showHelpPlayer((Player) sender, page);
        } else {
            showHelpConsole();
        }
    }

    private void showHelpPlayer(Player player, int page) {
        ArrayList<BaseCommand> cmds = Commands.commandList();
        double totalCmdPgs = Math.ceil(page / 12D);
        if (page > totalCmdPgs)
            page = (int) totalCmdPgs;
        player.sendMessage(ChatColor.BLUE + "------ Cloudshop (" + page + "/" + (int) totalCmdPgs + ") ------");
        for (int i = (page - 1) * 12; i < ((page - 1) * 12) + 12; i++) {
            if (i >= cmds.size())
                break;
            BaseCommand cmd = cmds.get(i);
            player.sendMessage(ChatColor.GREEN + " - " + ChatColor.LIGHT_PURPLE + "/cloudshop " + cmd.getName() + ChatColor.GREEN + " : " + ChatColor.AQUA + cmd.getCommandDescription());
        }
    }

    private void showHelpConsole() {
        plugin.getLogger().info("----- Cloudshop Help -----");
        for (BaseCommand cmd : Commands.commandList()) {
            plugin.getLogger().info(" - /cloudshop " + cmd.getName() + " : " + cmd.getCommandDescription());
        }
    }
}
