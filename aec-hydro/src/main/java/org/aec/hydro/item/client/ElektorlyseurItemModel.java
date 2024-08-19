package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.ElektrolyseurItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class ElektorlyseurItemModel extends GeoModel<ElektrolyseurItem> {
    @Override
    public Identifier getModelResource(ElektrolyseurItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/elektrolyseur.geo.json");
    }

    @Override
    public Identifier getTextureResource(ElektrolyseurItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/elektrolyseur.png");
    }

    @Override
    public Identifier getAnimationResource(ElektrolyseurItem animatable) {
        return null;
    }
}
