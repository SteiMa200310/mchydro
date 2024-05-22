package org.aec.hydro;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.aec.hydro.block.HydroBlocks;
import org.aec.hydro.block.entity.HydroBlockEntities;
import org.aec.hydro.block.entity.client.WindMillRenderer;

public class AECHydroClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AECHydro.LOGGER.info("Client initialization");
    }
}
