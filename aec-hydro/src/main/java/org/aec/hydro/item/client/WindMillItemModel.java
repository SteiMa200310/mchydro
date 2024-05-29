package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.item.custom.WindMillItem;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class WindMillItemModel extends GeoModel<WindMillItem> {

    @Override
    public Identifier getModelResource(WindMillItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/windmill.geo.json");
    }

    @Override
    public Identifier getTextureResource(WindMillItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/windmill.png");
    }

    @Override
    public Identifier getAnimationResource(WindMillItem animatable) {
        return new Identifier(AECHydro.MOD_ID, "animations/windmill.animation.json");
    }
}
