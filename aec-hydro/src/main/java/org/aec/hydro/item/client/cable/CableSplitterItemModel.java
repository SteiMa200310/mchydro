package org.aec.hydro.item.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.CableSplitterItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class CableSplitterItemModel extends GeoModel<CableSplitterItem> {
    @Override
    public Identifier getModelResource(CableSplitterItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/cable_splitter.geo.json");
    }

    @Override
    public Identifier getTextureResource(CableSplitterItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/cable_splitter.png");
    }

    @Override
    public Identifier getAnimationResource(CableSplitterItem animatable) {
        return null;
    }
}
