package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.SolarPanelItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class SolarPanelItemModel extends GeoModel<SolarPanelItem> {
    @Override
    public Identifier getModelResource(SolarPanelItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/solarpanel.geo.json");
    }

    @Override
    public Identifier getTextureResource(SolarPanelItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/solarpanel.png");
    }

    @Override
    public Identifier getAnimationResource(SolarPanelItem animatable) {
        return null;
    }
}
