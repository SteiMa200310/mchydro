package com.mdfg.elytraplugint2.custom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

public class MetaEditor {
    private ItemMeta meta;

    public MetaEditor(ItemMeta meta) {
        this.meta = meta;
    }

    public void addChestplateAttributes(Material type) {
        Checker c = new Checker();
        if (!c.isMaterialTypeChestplate(type)) {
            System.out.println("The Material Type was not a chestplate");
            return; //maybe through an exception later on
        }

        switch (type) {
            case NETHERITE_CHESTPLATE:
                meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, this.createModifier("3", 0.1));
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, this.createModifier("2", 3.0));
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR, this.createModifier("1", 8.0));
                break;
            case DIAMOND_CHESTPLATE:
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, this.createModifier("2", 2.0));
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR, this.createModifier("1", 8.0));
                break;
            case IRON_CHESTPLATE:
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR, this.createModifier("1", 6.0));
                break;
            //this seemes like to do both
            case GOLDEN_CHESTPLATE:
            case CHAINMAIL_CHESTPLATE:
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR, this.createModifier("1", 5.0));
                break;
            case LEATHER_CHESTPLATE:
                meta.addAttributeModifier(Attribute.GENERIC_ARMOR, this.createModifier("1", 3.0));
                break;
            default:
                System.out.println("The Material Type was not a chestplate - security check has been IGNORED !!");
                break;
        }

        //meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        //could be used to hide those attributes on the armour - if for example i don't like the order
        //and create such a text with lore
        //like this:
        //List<String> lore = new ArrayList<>();
        //lore.add("\n");
        //lore.add("§7When on Body:");
        //boolean fake = false;
        //lore.add("§9+" + 8 + " §9Armor" + (fake ? "" : " FAKE"));
        //lore.add("§9+" + 3 + " §9Armor Toughness" + (fake ? "" : " FAKE"));
        //lore.add("§9+" + 1 + " §9Knockback Resistance" + (fake ? "" : " FAKE"));
        //meta.setLore(lore); //add fake attributes to assure correct order
        //meta.setLore(Arrays.asList("Custom Elytra")); //set certain text
        //can also add colour scheme and stuff like that
    }

    public ItemMeta getMeta() {
        return meta;
    }

    private AttributeModifier createModifier(String name, double amount) {
        return new AttributeModifier(
                UUID.randomUUID(),
                name,
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.CHEST
        );
    }
}
