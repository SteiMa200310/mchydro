package org.aec.hydro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.aec.hydro.block.entity.HydroBlockEntities;
import org.aec.hydro.block.entity.client.WindMillRenderer;

@Environment(EnvType.CLIENT)
public class AECHydroClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AECHydro.LOGGER.info("Client initialization");

        BlockEntityRendererFactories.register(HydroBlockEntities.WINDMILL_BLOCK_ENTITY, WindMillRenderer::new);
    }
}
