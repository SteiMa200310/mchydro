package com.mdfg.elytraplugint2.custom;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnchantmentEditor {
    private ItemStack item;

    public EnchantmentEditor(ItemStack item) {
        this.item = item;
    }

    public void addEnchantments_Matthias(Map<Enchantment, Integer> chestplatemap, Map<Enchantment, Integer> elytramap) {
        this.clearEnchantments();

        //every Enchantment provided is static which means their instance only exists once
        for (Enchantment enchantment : chestplatemap.keySet()) {
            int LVL_chestplate = chestplatemap.get(enchantment);
            int maxlvl = enchantment.getMaxLevel();

            if (elytramap.containsKey(enchantment)) {
                int LVL_elytra = elytramap.get(enchantment);

                if ((LVL_chestplate != maxlvl && LVL_elytra != maxlvl) &&
                    (LVL_chestplate == LVL_elytra)) {
                    this.item.addUnsafeEnchantment(enchantment, LVL_chestplate + 1);
                } else {
                    this.item.addUnsafeEnchantment(enchantment, Math.max(LVL_chestplate, LVL_elytra));
                }
            } else {
                this.item.addUnsafeEnchantment(enchantment, LVL_chestplate);
            }
        }

        for (Enchantment enchantment : elytramap.keySet()) {
            int LVL_elytra = elytramap.get(enchantment);

            if (!chestplatemap.containsKey(enchantment)) {
                this.item.addUnsafeEnchantment(enchantment, LVL_elytra);
            }
        }
    }

    public void addEnchantments_Gregor(ItemStack item1, ItemStack item2) {
        for(Enchantment enchantment : item2.getEnchantments().keySet()){
            if(item1.containsEnchantment(enchantment)){
                if(item2.getEnchantmentLevel(enchantment) == item1.getEnchantmentLevel(enchantment)
                        && item2.getEnchantmentLevel(enchantment) != enchantment.getMaxLevel()){
                    item.addUnsafeEnchantment(enchantment, item2.getEnchantmentLevel(enchantment) +1);
                }
                else if(item2.getEnchantmentLevel(enchantment) < item1.getEnchantmentLevel(enchantment)){
                    item.addUnsafeEnchantment(enchantment, item1.getEnchantmentLevel(enchantment));
                }
                else if(item2.getEnchantmentLevel(enchantment) > item1.getEnchantmentLevel(enchantment)){
                    item.addUnsafeEnchantment(enchantment, item2.getEnchantmentLevel(enchantment));
                }
                else{
                    item.addUnsafeEnchantment(enchantment, item2.getEnchantmentLevel(enchantment));
                }
            }
            else{
                item.addUnsafeEnchantment(enchantment,item2.getEnchantmentLevel(enchantment));
            }
        }
        for(Enchantment enchantment : item1.getEnchantments().keySet()){
            if(!item2.containsEnchantment(enchantment)){
                item.addUnsafeEnchantment(enchantment,item1.getEnchantmentLevel(enchantment));
            }
        }
    }

    private void clearEnchantments() {
        this.item.removeEnchantments();
    }
}
