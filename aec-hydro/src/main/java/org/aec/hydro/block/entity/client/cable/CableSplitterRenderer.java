package org.aec.hydro.block.entity.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.CableSplitterBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class CableSplitterRenderer extends GeoBlockRenderer<CableSplitterBlockEntity> {
    public CableSplitterRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new CableSplitterModel());
    }
}
