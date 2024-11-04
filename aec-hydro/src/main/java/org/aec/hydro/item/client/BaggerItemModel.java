package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.BaggerItem;
import org.aec.hydro.item.custom.WindMillItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class BaggerItemModel extends GeoModel<BaggerItem> {
    @Override
    public Identifier getModelResource(BaggerItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/bagger.geo.json");
    }

    @Override
    public Identifier getTextureResource(BaggerItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/bagger.png");
    }

    @Override
    public Identifier getAnimationResource(BaggerItem animatable) { return null;}
}
