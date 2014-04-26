package me.mrkirby153.plugins.cloudshop.shop;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EconHelper {

    private static CloudShop plugin = CloudShop.instance();

    public static void updateBalances(){
        for(OfflinePlayer op : Bukkit.getOfflinePlayers()){
            syncBalance(op.getName());
        }
    }

    public static void syncBalance(String playerName){
        CloudShopper shopper = Shoppers.findShopperByName(playerName);
        if(shopper == null || !shopper.isLinked())
            return;
        if(!hasAccount(playerName))
            return;
        plugin.mysql().update("UPDATE `cs_users` SET balance = '"+plugin.economy.getBalance(playerName));
    }

    public static boolean hasAccount(String playerName){
        try{
            ResultSet rs = plugin.mysql().query("SELECT * FROM `cs_users` WHERE ingame = '"+playerName+"'");
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
