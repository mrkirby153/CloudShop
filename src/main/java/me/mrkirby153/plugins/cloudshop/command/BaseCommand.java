package me.mrkirby153.plugins.cloudshop.command;

import org.bukkit.command.CommandSender;

/**
 * The base command class that all sub commands must extend.
 */
public abstract class BaseCommand {
    private String permissionRequired = "";
    private String commandName;
    private String commandDescription;
    private int argsRequired;

    public BaseCommand(String commandName, int requiredArgs, String description, String permissionRequired) {
        this.commandName = commandName;
        this.argsRequired = requiredArgs;
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

    /**
     * Called when an incorrect number of arguments are passed to the command
     *
     * @param expected The expected number of arguments
     * @param got      The actual number of arguments recieved.
     */
    public abstract void illegalArguments(int expected, int got);
}
