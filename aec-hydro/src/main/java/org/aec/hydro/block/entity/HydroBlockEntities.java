package org.aec.hydro.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.HydroBlocks;
import org.aec.hydro.block.entity.client.WindMillRenderer;

public class HydroBlockEntities {
    public static BlockEntityType<WindMillBlockEntity> WINDMILL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "windmill_entity"),
                    FabricBlockEntityTypeBuilder.create(WindMillBlockEntity::new, HydroBlocks.WIND_MILL).build());
    public static void registerHydroBlockEntities(){
        AECHydro.LOGGER.info("Registering Hydro Block Entities for " + AECHydro.MOD_ID);

        BlockEntityRendererFactories.register(HydroBlockEntities.WINDMILL_BLOCK_ENTITY, WindMillRenderer::new);
    }
}
