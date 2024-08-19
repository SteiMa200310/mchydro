package org.aec.hydro.block.entity.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.CableBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class CableRenderer extends GeoBlockRenderer<CableBlockEntity> {
    public CableRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new CableModel());
    }
}
