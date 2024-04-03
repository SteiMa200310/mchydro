package com.mdfg.elytraplugint2.events;

import com.mdfg.elytraplugint2.ElytraPluginT2;
import com.mdfg.elytraplugint2.custom.Checker;
import com.mdfg.elytraplugint2.custom.EnchantmentEditor;
import com.mdfg.elytraplugint2.custom.MetaEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilHandler implements Listener {
    private ElytraPluginT2 plugin;
    public AnvilHandler(ElytraPluginT2 plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        AnvilInventory aninv = e.getInventory();
        ItemStack[] contents = aninv.getStorageContents();
        ItemStack item1 = contents[0];
        ItemStack item2 = contents[1];
        //Bukkit.broadcastMessage("item1name: " + item1.toString() + " item2name: " + item2.toString());

        Checker c = new Checker();
        Checker.ElytraChestPlatePackage p = c.checkItems(item1, item2);
        if (p != null) {
            //craft newElytra
            ItemStack newElytra = new ItemStack(Material.ELYTRA);

            ItemMeta meta = newElytra.getItemMeta(); //maybe only give a copy
            MetaEditor metaEditor = new MetaEditor(meta);
            metaEditor.addChestplateAttributes(p.getChestplate().getType());
            //reference is still on
            newElytra.setItemMeta(meta);

            EnchantmentEditor enchantmentEditor = new EnchantmentEditor(newElytra);
            //enchantmentEditor.addEnchantments_Matthias(p.getChestplate().getEnchantments(), p.getElytra().getEnchantments());
            enchantmentEditor.addEnchantments_Gregor(p.getChestplate(), p.getElytra());
            //does not need a set/save since we edit the ItemStack itself

            //set result in anvil
            e.setResult(newElytra);
            plugin.getServer().getScheduler().runTask(plugin, () -> aninv.setRepairCost(10));
            //since otherwise after the PrepareAnvilEvent minecraft calculates the cost himself
            //which results in this set beeing ignored
            //that is why i need to do it within the runTask
        }
    }
}