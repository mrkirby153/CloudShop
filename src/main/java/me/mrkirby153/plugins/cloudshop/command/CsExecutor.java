package me.mrkirby153.plugins.cloudshop.command;

import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CsExecutor implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = args[0];
        ArrayList<String> cmdArgsArray = new ArrayList<String>();
        for(String s : args){
            cmdArgsArray.add(s);
        }
        cmdArgsArray.remove(0);
        String[] cmdArgs = cmdArgsArray.toArray(new String[0]);
        BaseCommand cmd = Commands.findCommand(commandName);
        if(cmd != null){
            cmd.execute(sender, cmdArgs);
            return true;
        }
        ChatHelper.send(sender, ChatColor.RED + "Unknown Command!");
        return true;
    }


}
