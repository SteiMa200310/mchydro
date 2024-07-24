package org.aec.hydro.block.entity.client;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.WaterwheelBlockEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class WaterwheelRenderer extends GeoBlockRenderer<WaterwheelBlockEntity> {
    public WaterwheelRenderer(BlockEntityRendererFactory.Context ctx) { super(new WaterwheelModel()); }
}
