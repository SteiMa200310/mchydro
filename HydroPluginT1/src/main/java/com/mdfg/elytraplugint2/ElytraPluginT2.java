package com.mdfg.elytraplugint2;

import com.mdfg.elytraplugint2.events.AnvilHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElytraPluginT2 extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("STARTED THE PLUGIN - ELYTRAPLUGIN!");

        AnvilHandler t = new AnvilHandler(this);
    }

    @Override
    public void onDisable() {
        System.out.println("STOPPED THE PLUGIN - ELYTRAPLUGIN!");
    }
}
