package com.rms.mchydrov1.PipeHandler;

import com.rms.mchydrov1.MchydroV1;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class PipeHandler implements Listener {
    private MchydroV1 _plugin;

    public PipeHandler(MchydroV1 plugin) {
        this._plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, _plugin);
    }
}
