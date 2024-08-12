package org.aec.hydro.block.entity.client.pipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.PipeMergerBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class PipeMergerModel extends GeoModel<PipeMergerBlockEntity> {
    @Override
    public Identifier getModelResource(PipeMergerBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/pipe_merger.geo.json");
    }

    @Override
    public Identifier getTextureResource(PipeMergerBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/pipe_merger.png");
    }

    @Override
    public Identifier getAnimationResource(PipeMergerBlockEntity animatable) {
        return null;
    }
}
