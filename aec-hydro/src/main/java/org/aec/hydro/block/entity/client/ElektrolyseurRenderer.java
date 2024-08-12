package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.ElektrolyseurBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class ElektrolyseurRenderer extends GeoBlockRenderer<ElektrolyseurBlockEntity> {
    public ElektrolyseurRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new ElektrolyseurModel());
    }
}
