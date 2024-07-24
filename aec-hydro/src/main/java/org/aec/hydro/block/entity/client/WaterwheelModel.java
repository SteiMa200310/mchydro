package org.aec.hydro.block.entity.client;

import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.WaterwheelBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class WaterwheelModel extends GeoModel<WaterwheelBlockEntity> {
    @Override
    public Identifier getModelResource(WaterwheelBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/waterwheel.geo.json");
    }

    @Override
    public Identifier getTextureResource(WaterwheelBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/waterwheel.png");
    }

    @Override
    public Identifier getAnimationResource(WaterwheelBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "animations/waterwheel.animation.json");
    }
}
