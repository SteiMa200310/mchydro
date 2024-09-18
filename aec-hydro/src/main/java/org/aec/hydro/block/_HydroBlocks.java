package org.aec.hydro.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.custom.*;
import org.aec.hydro.block.custom.cable.Cable;
import org.aec.hydro.block.custom.cable.CableMerger;
import org.aec.hydro.block.custom.cable.CableSplitter;
import org.aec.hydro.block.custom.pipe.PipeMerger;
import org.aec.hydro.block.custom.pipe.PipeSplitter;

public class _HydroBlocks {
    public static final Block DI_BLOCK = registerBlock("di_block", new DIBlock(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block PIPE = registerBlock("pipe", new Pipe(FabricBlockSettings.copyOf(Blocks.STONE)));

    //Blocks With Blockentity
    public static final Block WIND_MILL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "windmill"), new WindMill(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block SOLAR_PANEL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "solar_panel"), new SolarPanel(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block WATERWHEEL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "waterwheel"), new Waterwheel(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block CABLE = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "cable"), new Cable(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block CABLE_MERGER = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "cable_merger"), new CableMerger(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block CABLE_SPLITTER = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "cable_splitter"), new CableSplitter(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block PIPE_MERGER = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "pipe_merger"), new PipeMerger(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block PIPE_SPLITTER = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "pipe_splitter"), new PipeSplitter(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block ELEKTROLYSEUR = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "elektrolyseur"), new Elektrolyseur(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block BRENSTOFFZELLE = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "brenstoffzelle"), new Brennstoffzelle(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block PUMP = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "pump"), new Pump(FabricBlockSettings.copyOf(Blocks.STONE)));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(AECHydro.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerHydroBlocks(){
        AECHydro.LOGGER.info("Registering Hydro Blocks for " + AECHydro.MOD_ID);
    }
}
