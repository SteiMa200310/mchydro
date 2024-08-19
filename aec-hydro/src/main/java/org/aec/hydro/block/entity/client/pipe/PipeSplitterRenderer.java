package org.aec.hydro.block.entity.client.pipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.PipeSplitterBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class PipeSplitterRenderer extends GeoBlockRenderer<PipeSplitterBlockEntity> {
    public PipeSplitterRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new PipeSplitterModel());
    }
}
