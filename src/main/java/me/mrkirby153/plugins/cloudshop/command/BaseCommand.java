package me.mrkirby153.plugins.cloudshop.command;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import org.bukkit.command.CommandSender;

/**
 * The base command class that all sub commands must extend.
 */
public abstract class BaseCommand {
    private String permissionRequired = "";
    private String commandName;
    private String commandDescription;

    public CloudShop plugin = CloudShop.instance();

    public BaseCommand() {
    }

    public BaseCommand(String commandName, String description, String permissionRequired) {
        this.commandName = commandName;
        this.commandDescription = description;
        this.permissionRequired = permissionRequired;

    }

    /**
     * The command's name
     *
     * @return The Command's name
     */
    public String getName() {
        return this.commandName;
    }

    /**
     * Returns the permission required to execute this command
     *
     * @return The permission required to execute the command
     */
    public String getPermissionRequired() {
        return this.permissionRequired;
    }

    /**
     * Returns the command description. Only use right now is for the help command
     *
     * @return The command's description
     */
    public String getCommandDescription() {
        return this.commandDescription;
    }


    /**
     * Called when the correct number of arguements are passed along with this command
     *
     * @param sender The Sender of the command
     * @param args   The respective areguments for the command
     */
    public abstract void execute(CommandSender sender, String[] args);
}
