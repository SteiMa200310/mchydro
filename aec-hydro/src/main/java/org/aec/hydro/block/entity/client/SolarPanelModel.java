package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.SolarPanelBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class SolarPanelModel extends GeoModel<SolarPanelBlockEntity> {
    @Override
    public Identifier getModelResource(SolarPanelBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/solarpanel.geo.json");
    }

    @Override
    public Identifier getTextureResource(SolarPanelBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/solarpanel.png");
    }

    @Override
    public Identifier getAnimationResource(SolarPanelBlockEntity animatable) {
        return null;
    }
}
