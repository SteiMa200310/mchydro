package org.aec.hydro.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block._HydroBlocks;

import org.aec.hydro.item.custom.WindMillItem;

public class ModItemGroups {
    public static final Item WIND_MILL_ITEM = registerItem("windmill", new WindMillItem(_HydroBlocks.WIND_MILL, new FabricItemSettings()));
    public static final ItemGroup Hydro = Registry.register(Registries.ITEM_GROUP,
            new Identifier(AECHydro.MOD_ID, "hydro"),
            FabricItemGroup.builder().displayName(Text.translatable("itemGroup.hydro"))
                    .icon(() -> new ItemStack(_HydroBlocks.DI_BLOCK)).entries((displayContext, entries) -> {
                        entries.add(_HydroBlocks.DI_BLOCK);
                        entries.add(_HydroBlocks.SOLAR_PANEL);
                        entries.add(_HydroBlocks.PIPE);
                        entries.add(_HydroBlocks.PIPEV2);
                        entries.add(WIND_MILL_ITEM);
                    }).build());

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(AECHydro.MOD_ID, name), item);
    }
    public static void registerHydroItemGroup(){
        AECHydro.LOGGER.info("Registering Hydro Item Group for " + AECHydro.MOD_ID);
    }
}
