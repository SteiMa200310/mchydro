package org.aec.hydro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.aec.hydro.block.entity._HydroBlockEntities;
import org.aec.hydro.block.entity.client.*;

@Environment(EnvType.CLIENT)
public class AECHydroClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AECHydro.LOGGER.info("Client initialization");

        BlockEntityRendererFactories.register(_HydroBlockEntities.WINDMILL_BLOCK_ENTITY, WindMillRenderer::new);
        BlockEntityRendererFactories.register(_HydroBlockEntities.SOLARPANEL_BLOCK_ENTITY, SolarPanelRenderer::new);
        BlockEntityRendererFactories.register(_HydroBlockEntities.WATERWHEEL_BLOCK_ENTITY, WaterwheelRenderer::new);
    }
}
