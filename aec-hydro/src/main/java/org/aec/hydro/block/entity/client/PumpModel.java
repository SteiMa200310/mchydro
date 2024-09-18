package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.PumpBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class PumpModel extends GeoModel<PumpBlockEntity> {
    @Override
    public Identifier getModelResource(PumpBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/pump.geo.json");
    }

    @Override
    public Identifier getTextureResource(PumpBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/pump.png");
    }

    @Override
    public Identifier getAnimationResource(PumpBlockEntity animatable) {
        return null;
    }
}
