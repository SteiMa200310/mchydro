package com.rms.mchydrov1.powerProduction;

import com.rms.mchydrov1.MchydroV1;
import com.rms.mchydrov1.customItems.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.UUID;

public class ModelTest implements Listener {
    private MchydroV1 _plugin;

    public ModelTest(MchydroV1 plugin) {
        this._plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, _plugin);
    }

    public Location getBlockCenter(Block block) {
        double x = block.getX() + 0.5; // Add 0.5 to get the center along the x-axis
        double z = block.getZ() + 0.5; // Add 0.5 to get the center along the z-axis
        return new Location(block.getWorld(), x, block.getY(), z);
    }
    public static final String UUID_METADATA_KEY = "UUID";

    public void setBlockUUID(Block block, UUID uuid) {
        block.setMetadata(UUID_METADATA_KEY, new FixedMetadataValue(_plugin, uuid.toString()));
    }

    public UUID getBlockCustomUUID(Block block) {
        if (block.hasMetadata(UUID_METADATA_KEY)) {
            for (MetadataValue value : block.getMetadata(UUID_METADATA_KEY)) {
                if (value.value() instanceof String) {
                    try {
                        return UUID.fromString((String) value.value());
                    } catch (IllegalArgumentException e) {
                        // Invalid UUID format, ignore and continue
                    }
                }
            }
        }
        return null; // No custom UUID metadata found for the block
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        PlayerInventory inventory = p.getInventory();

        ArrayList<String> menuLore = new ArrayList<>();
        menuLore.add("Info to the Item");
        inventory.addItem(CustomItemStack.createCustomItemStack(Material.END_STONE, 2, "Solar Panel",menuLore));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        ItemMeta blockmeta = e.getItemInHand().getItemMeta();
        if (e.getBlockPlaced().getType() != Material.END_STONE || blockmeta.getCustomModelData() != 2) return;

        Location BlockPos = getBlockCenter(e.getBlock());
        ArmorStand armorStand = (ArmorStand) BlockPos.getWorld().spawnEntity(BlockPos, EntityType.ARMOR_STAND);
        EntityEquipment equipment = armorStand.getEquipment();
        ItemStack customBlock = new ItemStack(Material.SHULKER_SHELL);
        ItemMeta meta = customBlock.getItemMeta();


        assert meta != null;
        meta.setCustomModelData(2);
        customBlock.setItemMeta(meta);

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);

        assert equipment != null;
        equipment.setHelmet(customBlock);
        setBlockUUID(e.getBlockPlaced(), new UUID((long) (Math.random()*100),1));
        System.out.println(getBlockCustomUUID(e.getBlockPlaced()).toString());
    }
}
