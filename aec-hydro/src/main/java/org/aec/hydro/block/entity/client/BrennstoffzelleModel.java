package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.BrennstoffzelleBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class BrennstoffzelleModel extends GeoModel<BrennstoffzelleBlockEntity> {
    @Override
    public Identifier getModelResource(BrennstoffzelleBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/brenstoffzelle.geo.json");
    }

    @Override
    public Identifier getTextureResource(BrennstoffzelleBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/brenstoffzelle.png");
    }

    @Override
    public Identifier getAnimationResource(BrennstoffzelleBlockEntity animatable) {
        return null;
    }
}
