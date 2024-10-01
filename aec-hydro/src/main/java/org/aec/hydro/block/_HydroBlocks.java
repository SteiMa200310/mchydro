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
import org.aec.hydro.block.custom.ColorBlock;
import org.aec.hydro.block.custom.cable.Cable;
import org.aec.hydro.block.custom.cable.CableCombiner;
import org.aec.hydro.block.custom.cell.Brennstoffzelle;
import org.aec.hydro.block.custom.cell.Elektrolyseur;
import org.aec.hydro.block.custom.geo.SolarPanel;
import org.aec.hydro.block.custom.geo.Waterwheel;
import org.aec.hydro.block.custom.geo.WindMill;
import org.aec.hydro.block.custom.pipe.Pipe;
import org.aec.hydro.block.custom.pipe.PipeCombiner;
import org.aec.hydro.block.custom.pipe.Pump;

public class _HydroBlocks {
    //Power Providers
    public static final Block PUMP = registerBlock("pump", new Pump(FabricBlockSettings.create().breakInstantly().nonOpaque()));

    //Blocks With Blockentity
    public static final Block WIND_MILL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "windmill"), new WindMill(FabricBlockSettings.create().breakInstantly()));
    public static final Block SOLAR_PANEL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "solarpanel"), new SolarPanel(FabricBlockSettings.create().breakInstantly()));
    public static final Block WATERWHEEL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "waterwheel"), new Waterwheel(FabricBlockSettings.create().breakInstantly()));

    //Power Providers NEED TO BE BEFORE PIPE to not again and again create a list of proverproviders
    public static final Block PIPE = registerBlock("pipe", new Pipe(FabricBlockSettings.create().breakInstantly()));
    public static final Block PIPECOMBINER = registerBlock("pipecombiner", new PipeCombiner(FabricBlockSettings.create().breakInstantly()));

    public static final Block CABLE = registerBlock("cable", new Cable(FabricBlockSettings.create().breakInstantly()));
    public static final Block CABLECOMBINER = registerBlock("cablecombiner", new CableCombiner(FabricBlockSettings.create().breakInstantly()));

    public static final Block ELEKTROLYZEUR = registerBlock("elektrolyseur", new Elektrolyseur(FabricBlockSettings.create().breakInstantly()));
    public static final Block BRENNSTOFFZELLE = registerBlock("brennstoffzelle", new Brennstoffzelle(FabricBlockSettings.create().breakInstantly()));

    public static final Block COLORBLOCK = registerBlock("colorblock", new ColorBlock(FabricBlockSettings.copyOf(Blocks.STONE)));

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
