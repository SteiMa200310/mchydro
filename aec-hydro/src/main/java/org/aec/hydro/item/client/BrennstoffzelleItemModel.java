package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.BrennstoffzelleItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class BrennstoffzelleItemModel extends GeoModel<BrennstoffzelleItem> {
    @Override
    public Identifier getModelResource(BrennstoffzelleItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/brenstoffzelle.geo.json");
    }

    @Override
    public Identifier getTextureResource(BrennstoffzelleItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/brenstoffzelle.png");
    }

    @Override
    public Identifier getAnimationResource(BrennstoffzelleItem animatable) {
        return null;
    }
}
