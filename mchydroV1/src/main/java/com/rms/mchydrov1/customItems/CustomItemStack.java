package com.rms.mchydrov1.customItems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CustomItemStack {
    public static ItemStack createCustomItemStack(Material material, int CustomModelData){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(CustomModelData);
        meta.setDisplayName(ChatColor.RESET + "Diamond Dust");

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createCustomItemStack(Material material, int CustomModelData,String DisplayName, ArrayList<String> menuLore){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setLore(menuLore);
        meta.setCustomModelData(CustomModelData);
        meta.setDisplayName(ChatColor.RESET + DisplayName);

        item.setItemMeta(meta);

        return item;
    }
}
