package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.WaterwheelItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class WaterwheelItemModel extends GeoModel<WaterwheelItem> {
    @Override
    public Identifier getModelResource(WaterwheelItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/waterwheel.geo.json");
    }

    @Override
    public Identifier getTextureResource(WaterwheelItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/waterwheel.png");
    }

    @Override
    public Identifier getAnimationResource(WaterwheelItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "animations/waterwheel.animation.json");
    }
}
