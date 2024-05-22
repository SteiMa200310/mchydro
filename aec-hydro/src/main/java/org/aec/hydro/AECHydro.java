package org.aec.hydro;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.aec.hydro.block.HydroBlocks;
import org.aec.hydro.block.entity.HydroBlockEntities;
import org.aec.hydro.block.entity.client.WindMillRenderer;
import org.aec.hydro.commands.ConfigureCommand;
import org.aec.hydro.item.ModItemGroups;
import org.aec.hydro.utils.Config;
import org.aec.hydro.utils.Pi4JHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

public class AECHydro implements ModInitializer {
	public static final String MOD_ID = "hydro";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Pi4JHelper PI4JHELPER = new Pi4JHelper();

	@Override
	public void onInitialize() {
		LOGGER.info(Text.translatable("generic.hydro.initialize").getString());

		// save / check if config exists
		Config.saveConfig();

		//Initialize Geckolib
		GeckoLib.initialize();

		// Register Hydro
		HydroBlocks.registerHydroBlocks();
		ModItemGroups.registerHydroItemGroup();
		HydroBlockEntities.registerHydroBlockEntities();


		new ConfigureCommand("reconfigure");

		// register function to be called on server shutdown
		ServerLifecycleEvents.SERVER_STOPPING.register(this::onShutdown);
	}

	private void onShutdown(MinecraftServer server) {

		LOGGER.info(Text.translatable("generic.hydro.shutdown").getString());
	}
}