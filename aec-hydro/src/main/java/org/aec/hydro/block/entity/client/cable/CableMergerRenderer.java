package org.aec.hydro.block.entity.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.CableMergerBlockEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class CableMergerRenderer extends GeoBlockRenderer<CableMergerBlockEntity> {
    public CableMergerRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new CableMergerModel());
    }
}
