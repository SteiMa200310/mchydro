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

public class HydroBlocks {
    public static final Block DI_BLOCK = registerBlock("di_block", new DIBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP)));


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
