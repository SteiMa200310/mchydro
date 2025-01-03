package org.aec.hydro;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.aec.hydro.block._HydroBlocks;

import org.aec.hydro.block.entity._HydroBlockEntities;

import org.aec.hydro.commands.base.CommandSetup;
import org.aec.hydro.item.ModItemGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

public class AECHydro implements ModInitializer {
	public static final String MOD_ID = "hydro";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info(Text.translatable("generic.hydro.initialize").getString());

		_HydroBlocks.registerHydroBlocks();

		GeckoLib.initialize();

		ModItemGroups.registerHydroItemGroup();

		_HydroBlockEntities.registerHydroBlockEntities();

		CommandSetup.RegisterCommands();

		// register function to be called on server shutdown
		ServerLifecycleEvents.SERVER_STOPPING.register(this::onShutdown);
	}

	private void onShutdown(MinecraftServer server) {
		LOGGER.info(Text.translatable("generic.hydro.shutdown").getString());
	}
}