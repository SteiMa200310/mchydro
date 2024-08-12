package org.aec.hydro.block.entity.client.pipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.PipeMergerBlockEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class PipeMergerRenderer extends GeoBlockRenderer<PipeMergerBlockEntity> {
    public PipeMergerRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new PipeMergerModel());
    }
}
