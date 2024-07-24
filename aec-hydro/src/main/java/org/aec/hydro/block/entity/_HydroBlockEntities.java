package org.aec.hydro.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block._HydroBlocks;

public class _HydroBlockEntities {
    public static BlockEntityType<WindMillBlockEntity> WINDMILL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "windmill_entity"),
                    FabricBlockEntityTypeBuilder.create(WindMillBlockEntity::new, _HydroBlocks.WIND_MILL).build());
    public static BlockEntityType<SolarPanelBlockEntity> SOLARPANEL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "solarpanel_entity"),
                    FabricBlockEntityTypeBuilder.create(SolarPanelBlockEntity::new, _HydroBlocks.SOLAR_PANEL).build());
    public static BlockEntityType<CableBlockEntity> CABLE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "cable_entity"),
                    FabricBlockEntityTypeBuilder.create(CableBlockEntity::new, _HydroBlocks.CABLE).build());
    public static BlockEntityType<WaterwheelBlockEntity> WATERWHEEL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "waterwheel_entity"),
                    FabricBlockEntityTypeBuilder.create(WaterwheelBlockEntity::new, _HydroBlocks.WATERWHEEL).build());

    public static void registerHydroBlockEntities(){
        AECHydro.LOGGER.info("Registering Hydro Block Entities for " + AECHydro.MOD_ID);
    }
}