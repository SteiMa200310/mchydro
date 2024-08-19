package org.aec.hydro.block.entity.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.CableSplitterBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class CableSplitterModel extends GeoModel<CableSplitterBlockEntity> {
    @Override
    public Identifier getModelResource(CableSplitterBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/cable_splitter.geo.json");
    }

    @Override
    public Identifier getTextureResource(CableSplitterBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/cable_splitter.png");
    }

    @Override
    public Identifier getAnimationResource(CableSplitterBlockEntity animatable) {
        return null;
    }
}
