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
import org.aec.hydro.block.custom.geo.SolarPanel;
import org.aec.hydro.block.custom.geo.Waterwheel;
import org.aec.hydro.block.custom.geo.WindMill;
import org.aec.hydro.block.custom.pipe.Pipe;
import org.aec.hydro.block.custom.pipe.PipeCorner;

public class _HydroBlocks {
    public static final Block PIPE = registerBlock("pipe", new Pipe(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block PIPECORNER = registerBlock("pipecorner", new PipeCorner(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block PIPECOMBINER = registerBlock("pipecombiner", new PipeCorner(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block PIPEPUMP = registerBlock("pipepump", new PipeCorner(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block ELEKTROLYZEUR = registerBlock("elektrolyseur", new PipeCorner(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block BRENNSTOFFZELLE = registerBlock("brennstoffzelle", new PipeCorner(FabricBlockSettings.copyOf(Blocks.STONE)));

    public static final Block CABLE = registerBlock("cable", new Pipe(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block CABLECORNER = registerBlock("cablecorner", new PipeCorner(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block CABLECOMBINER = registerBlock("cablecombiner", new PipeCorner(FabricBlockSettings.copyOf(Blocks.STONE)));

    //TODO: alle als Block hinzufügen so wie die pipe

    //Blocks With Blockentity
    public static final Block WIND_MILL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "windmill"), new WindMill(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block SOLAR_PANEL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "solar_panel"), new SolarPanel(FabricBlockSettings.copyOf(Blocks.STONE)));
    public static final Block WATERWHEEL = Registry.register(Registries.BLOCK, new Identifier(AECHydro.MOD_ID, "waterwheel"), new Waterwheel(FabricBlockSettings.copyOf(Blocks.STONE)));

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
