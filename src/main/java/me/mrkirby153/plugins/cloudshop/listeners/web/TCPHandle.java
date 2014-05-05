package me.mrkirby153.plugins.cloudshop.listeners.web;

import me.mrkirby153.plugins.cloudshop.shop.EconHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

public class TCPHandle {

    public static void handle(BufferedWriter writer, String line) throws IOException {
        String[] split = line.split(":");
        if (split.length <= 0)
            return;
        String commamnd = split[0];
        if (commamnd.equalsIgnoreCase("currBal")) {
            if (split.length == 2) {
                writer.write(EconHelper.getBalance(split[1]) + "\n");
                writer.flush();
            }
        }
        if (commamnd.equalsIgnoreCase("bought")) {
            if (split.length == 4) {
                int id = Integer.parseInt(split[1]);
                UUID fromUuid = UUID.fromString(split[2]);
                if (fromUuid == null) {
                    writer.write(fromUuid.toString() + " is not a UUID! \n");
                    writer.flush();
                    return;
                }
                Player from = Bukkit.getPlayer(fromUuid); // The UUID person buying the item
                if (from == null) {
                    writer.write("No player found with UUID " + fromUuid.toString() + "\n");
                    return;
                }
                UUID toUuid = UUID.fromString(split[3]);
                if (toUuid == null) {
                    writer.write(split[3] + " is not a UUID!\n");
                    writer.flush();
                    return;
                }
                Player to = Bukkit.getPlayer(toUuid); // The UUID person selling the item
                if (to == null) {
                    writer.write("No player found with UUID " + toUuid.toString() + "\n");
                    writer.flush();
                    return;
                }
                System.out.println("Item id " + id + " listed by " + from + " has been sold to " + to);
            }
        }
    }
}
