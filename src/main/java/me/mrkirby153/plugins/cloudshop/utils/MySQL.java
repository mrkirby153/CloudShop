package me.mrkirby153.plugins.cloudshop.utils;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public class MySQL {

    private boolean errors = false;
    private Connection conn = null;
    private Statement st = null;
    private CloudShop plugin = CloudShop.instance();

    private static String url, username, password;

    public MySQL(FileConfiguration config) {
        String host = config.getString("mysql.host");
        String port = config.getString("mysql.port");
        String dbName = config.getString("mysql.dbName");
        username = config.getString("mysql.username");
        password = config.getString("mysql.password");
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        // Create database connection
        try {
            conn = DriverManager.getConnection(url, username, password);
            st = conn.createStatement();
        } catch (SQLException e) {
            errors = true;
            plugin.getLogger().severe("There was a problem connecting to the database! [" + e.getMessage() + "]");
        }
    }

    public boolean connectionErr() {
        return errors;
    }

    /**
     * Query the database for informaton
     * @param query Query to execute
     * @return Result of the query
     */
    public ResultSet query(String query) {
        try {
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            plugin.getLogger().severe("Error occured when executing query \"" + query + "\"! [" + e.getMessage() + "]");
        }
        return null;
    }

    /**
     * Executes an MySQL update statement Asyncronously
     * @param query The query to update
     */
    public void update(final String query) {
        new BukkitRunnable() {
            public void run() {
                try {
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    plugin.getLogger().severe("Error occured when executing query \"" + query + "\"! [" + e.getMessage() + "]");
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public PreparedStatement prepareStatement(String s){
        try {
            return conn.prepareStatement(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
