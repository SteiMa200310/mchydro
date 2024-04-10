package com.rms.mchydrov1.TorchHandler;

import com.rms.mchydrov1.MchydroV1;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.Torch;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class TorchHandler implements Listener {
    private MchydroV1 _plugin;

    public TorchHandler(MchydroV1 plugin) {
        this._plugin = plugin;
    }

    @EventHandler
    public void onTorchPlaced(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.TORCH) {
            e.getBlock().setType(Material.DIAMOND_BLOCK);
        }
    }
}
