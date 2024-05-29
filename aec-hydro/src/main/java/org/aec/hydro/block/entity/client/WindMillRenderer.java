package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.Identifier;
import org.aec.hydro.block.entity.WindMillBlockEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WindMillRenderer extends GeoBlockRenderer<WindMillBlockEntity> {
    public WindMillRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new WindMillModel());
    }
}
