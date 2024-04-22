package org.aec.hydro;

import net.fabricmc.api.ModInitializer;

import org.aec.hydro.block.HydroBlocks;
import org.aec.hydro.item.ModItemGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AECHydro implements ModInitializer {
	public static final String MOD_ID = "hydro";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hydro Init");

		HydroBlocks.registerHydroBlocks();
		ModItemGroups.registerHydroItemGroup();
	}
}