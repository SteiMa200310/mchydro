package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.PumpItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class PumpItemModel extends GeoModel<PumpItem> {
    @Override
    public Identifier getModelResource(PumpItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/pump.geo.json");
    }

    @Override
    public Identifier getTextureResource(PumpItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/pump.png");
    }

    @Override
    public Identifier getAnimationResource(PumpItem animatable) {
        return null;
    }
}
