package org.aec.hydro.block.entity.client.pipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.PipeSplitterBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class PipeSplitterModel extends GeoModel<PipeSplitterBlockEntity> {
    @Override
    public Identifier getModelResource(PipeSplitterBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/pipe_splitter.geo.json");
    }

    @Override
    public Identifier getTextureResource(PipeSplitterBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/pipe_splitter.png");
    }

    @Override
    public Identifier getAnimationResource(PipeSplitterBlockEntity animatable) {
        return null;
    }
}
