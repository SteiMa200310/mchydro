package org.aec.hydro.item.client.pipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.PipeSplitterItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class PipeSplitterItemModel extends GeoModel<PipeSplitterItem> {
    @Override
    public Identifier getModelResource(PipeSplitterItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/pipe_splitter.geo.json");
    }

    @Override
    public Identifier getTextureResource(PipeSplitterItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/pipe_splitter.png");
    }

    @Override
    public Identifier getAnimationResource(PipeSplitterItem animatable) {
        return null;
    }
}
