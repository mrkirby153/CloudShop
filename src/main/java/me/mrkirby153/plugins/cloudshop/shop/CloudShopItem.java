package me.mrkirby153.plugins.cloudshop.shop;

import me.mrkirby153.plugins.cloudshop.CloudShop;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CloudShopItem {

    private int id;
    private String boughtBy;
    private int cost;

    public CloudShopItem(int id){
        this.id = id;
        try{
            ResultSet rs = CloudShop.mysql().query("SELECT * FROM `cs_items` WHERE id = '"+id+"'");
            if(rs.next()){
                boughtBy = rs.getString("boughtBy");
                cost = rs.getInt("cost");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String getBoughtBy(){
        return boughtBy;
    }

    public int getCost(){
        return cost;
    }

    public ItemStack recosntruct(){
        // Query database
        try {
            ResultSet rs = CloudShop.mysql().query("SELECT * FROM `cs_items` WHERE id = '" + this.id + "'");
            if (rs.next()) {
                Material material = Material.valueOf(rs.getString("material"));
                short damage = rs.getShort("damage");
                int count = rs.getInt("count");
                String name = rs.getString("name");
                String enchantments = rs.getString("enchantments");
                String lore = rs.getString("lore");
                // Begin itemstack reconstruction
                ItemStack item = new ItemStack(material, count, damage);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                for(String ench : enchantments.split(",")){
                    String[] idLvl = ench.split(":");
                    if(idLvl.length < 2)
                        continue;
                    Enchantment enchantment = Enchantment.getByName(idLvl[0]);
                    if(enchantment == null)
                        continue;
                    meta.addEnchant(enchantment, Integer.parseInt(idLvl[1]), true);
                }
                // Lore
                List<String> list = new ArrayList<String>();
                for(String s : lore.split("\n"))
                    list.add(s);
                meta.setLore(list);
                item.setItemMeta(meta);
                return item;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
