package me.mrkirby153.plugins.cloudshop.shop;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class Shoppers {
    public static ArrayList<CloudShopper> shoppers = new ArrayList<CloudShopper>();

    public static CloudShopper registerShopper(UUID uuid) {
        CloudShopper shopper = new CloudShopper(uuid);
        if (shoppers.contains(shopper)) {
            ChatHelper.sendToConsole(Level.SEVERE, "Attempted to register '" + Bukkit.getPlayer(uuid) + "' as a new shopper while he" +
                    " already exists!");
            return null;
        }
        shoppers.add(shopper);
        return shopper;
    }

    public static CloudShopper registerShopper(Player player) {
        CloudShopper shopper = new CloudShopper(player);
        if (shoppers.contains(shopper)) {
            ChatHelper.sendToConsole(Level.SEVERE, "Attempted to register '" + player.getName() + "' as a new shopper while he" +
                    " already exists!");
            return null;
        }
        shoppers.add(shopper);
        return shopper;
    }

    public static void unregisterShopper(UUID uuid) {
        CloudShopper shopper = findShopperByUUID(uuid);
        if (shopper == null) {
            ChatHelper.sendToConsole(Level.SEVERE, "Attempted to unregister '" + Bukkit.getPlayer(uuid).getName() + "' but he does not exist!");
            return;
        }
        shoppers.remove(shopper);
    }

    public static CloudShopper findShopperByUUID(UUID uuid) {
        for (CloudShopper shop : shoppers) {
            if (shop.getName().equalsIgnoreCase(uuid.toString()))
                return shop;
        }
        return null;
    }


    public static void unregisterShopper(Player player) {
        CloudShopper shopper = findShopperByUUID(player.getUniqueId());
        if (shopper == null) {
            ChatHelper.sendToConsole(Level.SEVERE, "Attempted to unregister '" + player.getName() + "' but he does not exist!");
            return;
        }
        shoppers.remove(shopper);
    }

    public static String registerNewPlayer(UUID uuid) {
        if (findShopperByUUID(uuid).isLinked()) {
            return null;
        }
        String nextId = RandomStringUtils.randomAlphanumeric(5);
        CloudShop.mysql().update("INSERT INTO `cs_link` (`uuid`, `linkCode`, `time`, `used`) VALUES ('"
                + uuid.toString() + "', '" + nextId + "', '" + new Timestamp(new Date().getTime()) + "', '0')");
        return nextId;
    }

    public static String forgot(String playerName) {
        try {
            ResultSet rs = CloudShop.mysql().query("SELECT * FROM `cs_link` WHERE username = '" + playerName + "'");
            if (rs.next()) {
                String nextId = RandomStringUtils.randomAlphanumeric(5);
                CloudShop.mysql().update("UPDATE `cs_link` SET `time` = '" + new Timestamp(new Date().getTime()) + "' WHERE username = '" + playerName + "'");
                CloudShop.mysql().update("UPDATE `cs_link` SET `linkCode` = '" + nextId + "' WHERE username = '" + playerName + "'");
                return nextId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
