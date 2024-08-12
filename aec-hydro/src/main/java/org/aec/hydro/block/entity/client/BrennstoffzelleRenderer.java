package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.BrennstoffzelleBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class BrennstoffzelleRenderer extends GeoBlockRenderer<BrennstoffzelleBlockEntity> {
    public BrennstoffzelleRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new BrennstoffzelleModel());
    }
}
