package org.aec.hydro.item.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.CableMergerItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class CableMergerItemModel extends GeoModel<CableMergerItem> {
    @Override
    public Identifier getModelResource(CableMergerItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/cable_merger.geo.json");
    }

    @Override
    public Identifier getTextureResource(CableMergerItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/cable_merger.png");
    }

    @Override
    public Identifier getAnimationResource(CableMergerItem animatable) {
        return null;
    }
}
