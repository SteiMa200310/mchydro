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

import org.aec.hydro.item.custom.*;

public class ModItemGroups {
    public static final Item WIND_MILL_ITEM = registerItem("windmill", new WindMillItem(_HydroBlocks.WIND_MILL, new FabricItemSettings()));
    public static final Item SOLAR_PANEL_ITEM = registerItem("solar_panel", new SolarPanelItem(_HydroBlocks.SOLAR_PANEL, new FabricItemSettings()));
    public static final Item WATERWHEEL_ITEM = registerItem("waterwheel", new WaterwheelItem(_HydroBlocks.WATERWHEEL, new FabricItemSettings()));

    //Custom Items
    public static final Item VOLTMETER = registerItem("voltmeter", new Item(new FabricItemSettings()));
    public static final Item ANODE = registerItem("anode", new Item(new FabricItemSettings()));
    public static final Item KATHODE = registerItem("kathode", new Item(new FabricItemSettings()));
    public static final Item MEMBRAN = registerItem("membran", new Item(new FabricItemSettings()));
    public static final Item WRENCH = registerItem("wrench", new Item(new FabricItemSettings()));

    public static final ItemGroup Hydro = Registry.register(Registries.ITEM_GROUP,
            new Identifier(AECHydro.MOD_ID, "hydro"),
            FabricItemGroup.builder().displayName(Text.translatable("itemGroup.hydro"))
                    .icon(() -> new ItemStack(WIND_MILL_ITEM)).entries((displayContext, entries) -> {
                        entries.add(_HydroBlocks.PIPE);
                        entries.add(_HydroBlocks.PIPECORNER);
                        entries.add(_HydroBlocks.PIPECOMBINER);
                        entries.add(_HydroBlocks.PIPEPUMP);

                        entries.add(_HydroBlocks.ELEKTROLYZEUR);
                        entries.add(_HydroBlocks.BRENNSTOFFZELLE);

                        entries.add(_HydroBlocks.CABLE);
                        entries.add(_HydroBlocks.CABLECORNER);
                        entries.add(_HydroBlocks.CABLECOMBINER);

                        entries.add(WIND_MILL_ITEM);
                        entries.add(SOLAR_PANEL_ITEM);
                        entries.add(WATERWHEEL_ITEM);

                        entries.add(VOLTMETER);
                        entries.add(ANODE);
                        entries.add(KATHODE);
                        entries.add(MEMBRAN);
                        entries.add(WRENCH);
                    }).build());

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(AECHydro.MOD_ID, name), item);
    }
    public static void registerHydroItemGroup(){
        AECHydro.LOGGER.info("Registering Hydro Item Group for " + AECHydro.MOD_ID);
    }
}
