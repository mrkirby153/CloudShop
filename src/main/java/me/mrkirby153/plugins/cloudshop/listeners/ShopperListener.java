package me.mrkirby153.plugins.cloudshop.listeners;

import me.mrkirby153.plugins.cloudshop.shop.CloudShopper;
import me.mrkirby153.plugins.cloudshop.shop.Shoppers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ShopperListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        CloudShopper cs = Shoppers.registerShopper(player);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Shoppers.unregisterShopper(player);
    }
}
