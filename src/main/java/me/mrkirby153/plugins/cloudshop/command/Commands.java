package me.mrkirby153.plugins.cloudshop.command;

import me.mrkirby153.plugins.cloudshop.CloudShop;

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
}
