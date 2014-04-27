package me.mrkirby153.plugins.cloudshop.command;

import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;

public class CsExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = args[0];
        ArrayList<String> cmdArgsArray = new ArrayList<String>();
        for (String s : args) {
            cmdArgsArray.add(s);
        }
        cmdArgsArray.remove(0);
        String[] cmdArgs = cmdArgsArray.toArray(new String[0]);
        BaseCommand cmd = Commands.findCommand(commandName);
        if (cmd != null) {
            String permissionRequierd = cmd.getPermissionRequired();
            if (permissionRequierd.equalsIgnoreCase("%all%") || sender.isOp()) {
                cmd.execute(sender, cmdArgs);
                return true;
            }
            if (sender.hasPermission(permissionRequierd)) {
                cmd.execute(sender, cmdArgs);
                return true;
            } else {
                ChatHelper.send(sender, ChatColor.RED + "You do not have permission to preform this command! This command requires permisison node: ");
                sender.sendMessage(ChatColor.BLUE + "[" + permissionRequierd + "]");
                return true;
            }
        } else {
            HashMap<String, BaseCommand> aliases = Commands.getAliases();
            cmd = aliases.get(commandName);
            if(aliases.containsKey(commandName)){
                String permissionRequierd = cmd.getPermissionRequired();
                if (permissionRequierd.equalsIgnoreCase("%all%") || sender.isOp()) {
                    cmd.execute(sender, cmdArgs);
                    return true;
                }
                if (sender.hasPermission(permissionRequierd)) {
                    cmd.execute(sender, cmdArgs);
                    return true;
                } else {
                    ChatHelper.send(sender, ChatColor.RED + "You do not have permission to preform this command! This command requires permisison node: ");
                    sender.sendMessage(ChatColor.BLUE + "[" + permissionRequierd + "]");
                    return true;
                }
            }

        }
        ChatHelper.send(sender, ChatColor.RED + "Unknown Command!");
        return true;
    }


}
