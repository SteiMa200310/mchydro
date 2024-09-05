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
    public static BlockEntityType<WaterwheelBlockEntity> WATERWHEEL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "waterwheel_entity"),
                    FabricBlockEntityTypeBuilder.create(WaterwheelBlockEntity::new, _HydroBlocks.WATERWHEEL).build());

    public static BlockEntityType<CableBlockEntity> CABLE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "cable_entity"),
                    FabricBlockEntityTypeBuilder.create(CableBlockEntity::new, _HydroBlocks.CABLE).build());
    public static BlockEntityType<CableMergerBlockEntity> CABLE_MERGER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "cable_merger_entity"),
                    FabricBlockEntityTypeBuilder.create(CableMergerBlockEntity::new, _HydroBlocks.CABLE_MERGER).build());
    public static BlockEntityType<CableSplitterBlockEntity> CABLE_SPLITTER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "cable_splitter_entity"),
                    FabricBlockEntityTypeBuilder.create(CableSplitterBlockEntity::new, _HydroBlocks.CABLE_SPLITTER).build());

    public static BlockEntityType<PipeMergerBlockEntity> PIPE_MERGER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "pipe_merger_entity"),
                    FabricBlockEntityTypeBuilder.create(PipeMergerBlockEntity::new, _HydroBlocks.PIPE_MERGER).build());
    public static BlockEntityType<PipeSplitterBlockEntity> PIPE_SPLITTER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "pipe_splitter_entity"),
                    FabricBlockEntityTypeBuilder.create(PipeSplitterBlockEntity::new, _HydroBlocks.PIPE_SPLITTER).build());

    public static BlockEntityType<ElektrolyseurBlockEntity> ELEKTROLYSEUR_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "elektrolyseur_entity"),
                    FabricBlockEntityTypeBuilder.create(ElektrolyseurBlockEntity::new, _HydroBlocks.ELEKTROLYSEUR).build());
    public static BlockEntityType<BrennstoffzelleBlockEntity> BRENSTOFFZELLE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "brenstoffzelle_entity"),
                    FabricBlockEntityTypeBuilder.create(BrennstoffzelleBlockEntity::new, _HydroBlocks.BRENSTOFFZELLE).build());

    public static BlockEntityType<PipeBlockEntity> PIPE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID, "pipe_entity"),
                    FabricBlockEntityTypeBuilder.create(PipeBlockEntity::new, _HydroBlocks.PIPE).build());

    public static void registerHydroBlockEntities(){
        AECHydro.LOGGER.info("Registering Hydro Block Entities for " + AECHydro.MOD_ID);
    }
}
