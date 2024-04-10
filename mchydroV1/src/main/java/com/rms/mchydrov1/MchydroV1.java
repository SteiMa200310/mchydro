package com.rms.mchydrov1;

import com.rms.mchydrov1.TorchHandler.TorchHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MchydroV1 extends JavaPlugin {

    @Override
    public void onEnable() {
        TorchHandler torchHandler = new TorchHandler(this);

        Bukkit.getPluginManager().registerEvents(torchHandler, this);

        // Plugin startup logic
        System.out.println("HYDRO STARTED");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("HYDRO STOPPED");
    }
}
