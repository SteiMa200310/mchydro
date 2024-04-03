package com.mdfg.elytraplugint2.custom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class Checker {
    public Checker() {}

    //internal classes
    public class ElytraChestPlatePackage {
        private ItemStack elytra;
        private ItemStack chestplate;

        public ElytraChestPlatePackage(ItemStack elytra, ItemStack chestplate) {
            this.elytra = elytra;
            this.chestplate = chestplate;
        }

        public ItemStack getElytra() {
            return this.elytra;
        }
        public ItemStack getChestplate() {
            return this.chestplate;
        }
    }

    public ElytraChestPlatePackage checkItems(ItemStack item1, ItemStack item2) {
        if (item1.getType() == Material.ELYTRA && isItemStackChestPlate(item2)) {
            ElytraChestPlatePackage p = new ElytraChestPlatePackage(item1, item2);
            return p;
        }

        if (item2.getType() == Material.ELYTRA && isItemStackChestPlate(item1)) {
            ElytraChestPlatePackage p = new ElytraChestPlatePackage(item2, item1);
            return p;
        }

        return null;
    }
    
    public boolean isItemStackChestPlate(ItemStack s) {
        return this.isMaterialTypeChestplate(s.getType());
    }

    public boolean isMaterialTypeChestplate(Material m) {
        if (m == Material.LEATHER_CHESTPLATE ||
            m == Material.CHAINMAIL_CHESTPLATE ||
            m == Material.GOLDEN_CHESTPLATE ||
            m == Material.IRON_CHESTPLATE ||
            m == Material.DIAMOND_CHESTPLATE ||
            m == Material.NETHERITE_CHESTPLATE) {
            return true;
        } else {
            return false;
        }
    }
}