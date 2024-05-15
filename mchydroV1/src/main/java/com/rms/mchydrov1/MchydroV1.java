package com.rms.mchydrov1;

import com.rms.mchydrov1.powerProduction.ModelTest;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MchydroV1 extends JavaPlugin {

    @Override
    public void onEnable() {
        ModelTest test = new ModelTest(this);

        // Plugin startup logic
        System.out.println("HYDRO STARTED");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("HYDRO STOPPED");
    }
}
