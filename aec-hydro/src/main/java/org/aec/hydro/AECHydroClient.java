package org.aec.hydro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.aec.hydro.block.entity._HydroBlockEntities;
import org.aec.hydro.block.entity.client.*;
import org.aec.hydro.block.entity.client.cable.CableMergerRenderer;
import org.aec.hydro.block.entity.client.cable.CableRenderer;
import org.aec.hydro.block.entity.client.cable.CableSplitterRenderer;
import org.aec.hydro.block.entity.client.pipe.PipeMergerRenderer;
import org.aec.hydro.block.entity.client.pipe.PipeSplitterRenderer;

@Environment(EnvType.CLIENT)
public class AECHydroClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AECHydro.LOGGER.info("Client initialization");

        BlockEntityRendererFactories.register(_HydroBlockEntities.WINDMILL_BLOCK_ENTITY, WindMillRenderer::new);
        BlockEntityRendererFactories.register(_HydroBlockEntities.SOLARPANEL_BLOCK_ENTITY, SolarPanelRenderer::new);
        BlockEntityRendererFactories.register(_HydroBlockEntities.WATERWHEEL_BLOCK_ENTITY, WaterwheelRenderer::new);

        BlockEntityRendererFactories.register(_HydroBlockEntities.CABLE_BLOCK_ENTITY, CableRenderer::new);
        BlockEntityRendererFactories.register(_HydroBlockEntities.CABLE_MERGER_BLOCK_ENTITY, CableMergerRenderer::new);
        BlockEntityRendererFactories.register(_HydroBlockEntities.CABLE_SPLITTER_BLOCK_ENTITY, CableSplitterRenderer::new);

        BlockEntityRendererFactories.register(_HydroBlockEntities.PIPE_MERGER_BLOCK_ENTITY, PipeMergerRenderer::new);
        BlockEntityRendererFactories.register(_HydroBlockEntities.PIPE_SPLITTER_BLOCK_ENTITY, PipeSplitterRenderer::new);

        BlockEntityRendererFactories.register(_HydroBlockEntities.ELEKTROLYSEUR_BLOCK_ENTITY, ElektrolyseurRenderer::new);
        BlockEntityRendererFactories.register(_HydroBlockEntities.BRENSTOFFZELLE_BLOCK_ENTITY, BrennstoffzelleRenderer::new);

        BlockEntityRendererFactories.register(_HydroBlockEntities.PUMP_BLOCK_ENTITY, PumpRenderer::new);
    }
}
