package org.aec.hydro.block.entity.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.CableMergerBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class CableMergerModel extends GeoModel<CableMergerBlockEntity> {
    @Override
    public Identifier getModelResource(CableMergerBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/cable_merger.geo.json");
    }

    @Override
    public Identifier getTextureResource(CableMergerBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/cable_merger.png");
    }

    @Override
    public Identifier getAnimationResource(CableMergerBlockEntity animatable) {
        return null;
    }
}
