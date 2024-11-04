package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.BaggerBlockEntity;
import org.aec.hydro.block.entity.SolarPanelBlockEntity;
import org.aec.hydro.block.entity.WindMillBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class BaggerModel extends GeoModel<BaggerBlockEntity> {
    @Override
    public Identifier getModelResource(BaggerBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/bagger.geo.json");
    }

    @Override
    public Identifier getTextureResource(BaggerBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/bagger.png");
    }

    @Override
    public Identifier getAnimationResource(BaggerBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "animations/bagger.animation.json");
    }
}
