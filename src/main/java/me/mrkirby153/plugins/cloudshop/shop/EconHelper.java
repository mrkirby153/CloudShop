package me.mrkirby153.plugins.cloudshop.shop;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EconHelper {

    private static CloudShop plugin = CloudShop.instance();

    public static void updateBalances(){
        for(OfflinePlayer op : Bukkit.getOfflinePlayers()){
            syncBalance(op.getUniqueId());
        }
    }

    public static void syncBalance(UUID uuid){
        CloudShopper shopper = Shoppers.findShopperByUUID(uuid);
        if(shopper == null || !shopper.isLinked())
            return;
        if(!hasAccount(uuid))
            return;
        plugin.mysql().update("UPDATE `cs_users` SET balance = '"+plugin.economy.getBalance(Bukkit.getPlayer(uuid).getName()));
    }

    public static boolean hasAccount(UUID uuid){
        try{
            ResultSet rs = plugin.mysql().query("SELECT * FROM `cs_users` WHERE uuid = '"+uuid+"'");
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static double getBalance(UUID uuid){
        return plugin.economy.getBalance(Bukkit.getPlayer(uuid).getName());
    }

    public static double getBalance(String playerName){
        return plugin.economy.getBalance(playerName);
    }
}
