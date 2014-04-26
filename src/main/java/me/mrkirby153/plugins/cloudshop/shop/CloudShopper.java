package me.mrkirby153.plugins.cloudshop.shop;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import me.mrkirby153.plugins.cloudshop.utils.ChatHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class CloudShopper {
    private Player player;



    public CloudShopper(String playerName) {
        this.player = Bukkit.getPlayerExact(playerName);
    }

    public CloudShopper(Player player) {
        this.player = player;
    }

    public String getName() {
        return this.player.getName();
    }


    public boolean isLinked() {
        try {
            ResultSet rs = CloudShop.mysql().query("SELECT * FROM `cs_users` WHERE ingame = '" + player.getName() + "'");
            if (rs.next()) {
                String username = rs.getString("ingame");
                String linkCode = rs.getString("linkCode");
                rs.close();
                rs = CloudShop.mysql().query("SELECT * FROM `cs_link` WHERE username = '" + username + "' AND linkCode = '" + linkCode + "'");
                if (rs.next()) {
                    return rs.getString("username").equalsIgnoreCase(username);
                }
            }
        } catch (SQLException e) {
            ChatHelper.sendToConsole(Level.SEVERE, "Error occured when executing query! [" + e.getMessage() + "]");
        }
        return false;
    }
}
