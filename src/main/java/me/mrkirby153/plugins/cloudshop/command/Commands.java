package me.mrkirby153.plugins.cloudshop.command;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.command.general.CommandHelp;

import java.util.ArrayList;

public class Commands {

    private static ArrayList<BaseCommand> commands = new ArrayList<BaseCommand>();

    public static void registerCommand(BaseCommand command) {
        if (!commands.contains(command)) {
            commands.add(command);
        } else {
            CloudShop.instance().getLogger().severe("Attempted to register " + command.getName() + " when it already exists!");
        }
    }

    public static BaseCommand findCommand(String name){
        for(BaseCommand cmd : commands){
            if(cmd.getName().equalsIgnoreCase(name))
                return cmd;
        }
        return null;
    }

    public static void registerAllCommands(){
        registerCommand(new CommandHelp());
    }

    public static ArrayList<BaseCommand> commandList(){
        return commands;
    }
}
