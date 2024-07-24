package org.aec.hydro.item.client;

import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.CableItem;
import software.bernie.geckolib.model.GeoModel;

public class CableItemModel extends GeoModel<CableItem> {
    @Override
    public Identifier getModelResource(CableItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/cable.geo.json");
    }

    @Override
    public Identifier getTextureResource(CableItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/cable.png");
    }

    @Override
    public Identifier getAnimationResource(CableItem animatable) { return null; }
}
