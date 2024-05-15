package org.aec.hydro.entity;

import org.aec.hydro.AECHydro;

public class HydroBlockEntities {
    //public static final BlockEntityType<SolarPanelBlockEntity> SOLAR_PANEL_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(AECHydro.MOD_ID,"solar_panel_be"),FabricBlockEntityTypeBuilder.create(SolarPanelBlockEntity::new, HydroBlocks.SOLAR_PANEL).build());
    public void registerHydroBlockEntities(){
        AECHydro.LOGGER.info("Registering Hydro Block Entities for " + AECHydro.MOD_ID);
    }
}
