package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.ElektrolyseurBlockEntity;
import org.aec.hydro.block.entity.PipeMergerBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class ElektrolyseurModel extends GeoModel<ElektrolyseurBlockEntity> {
    @Override
    public Identifier getModelResource(ElektrolyseurBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/elektrolyseur.geo.json");
    }

    @Override
    public Identifier getTextureResource(ElektrolyseurBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/elektrolyseur.png");
    }

    @Override
    public Identifier getAnimationResource(ElektrolyseurBlockEntity animatable) {
        return null;
    }
}
