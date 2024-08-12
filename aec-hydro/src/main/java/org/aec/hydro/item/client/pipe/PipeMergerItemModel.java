package org.aec.hydro.item.client.pipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.PipeMergerItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class PipeMergerItemModel extends GeoModel<PipeMergerItem> {
    @Override
    public Identifier getModelResource(PipeMergerItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/pipe_merger.geo.json");
    }

    @Override
    public Identifier getTextureResource(PipeMergerItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/pipe_merger.png");
    }

    @Override
    public Identifier getAnimationResource(PipeMergerItem animatable) {
        return null;
    }
}
