package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.CableBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class CableModel extends GeoModel<CableBlockEntity> {
    @Override
    public Identifier getModelResource(CableBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/cable.geo.json");
    }

    @Override
    public Identifier getTextureResource(CableBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/cable.png");
    }

    @Override
    public Identifier getAnimationResource(CableBlockEntity animatable) {
        return null;
    }
}
