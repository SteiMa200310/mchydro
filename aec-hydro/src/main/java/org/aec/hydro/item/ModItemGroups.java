package org.aec.hydro.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.HydroBlocks;

public class ModItemGroups {
    public static final ItemGroup Hydro = Registry.register(Registries.ITEM_GROUP,
            new Identifier(AECHydro.MOD_ID, "hydro"),
            FabricItemGroup.builder().displayName(Text.translatable("itemGroup.hydro"))
                    .icon(() -> new ItemStack(HydroBlocks.DI_BLOCK)).entries((displayContext, entries) -> {
                        entries.add(HydroBlocks.DI_BLOCK);
                        entries.add(HydroBlocks.SOLAR_PANEL);
                    }).build());


    public static void registerHydroItemGroup(){
        AECHydro.LOGGER.info("Registering Hydro Item Group for " + AECHydro.MOD_ID);
    }
}
