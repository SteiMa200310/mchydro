package org.aec.hydro.block.entity.client;

import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block.entity.WindMillBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class WindMillModel extends GeoModel<WindMillBlockEntity> {
    @Override
    public Identifier getModelResource(WindMillBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "geo/windmill.geo.json");
    }

    @Override
    public Identifier getTextureResource(WindMillBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "textures/block/windmill.png");
    }

    @Override
    public Identifier getAnimationResource(WindMillBlockEntity animatable) {
        return new Identifier(AECHydro.MOD_ID, "animations/windmill.animation.json");
    }
}
