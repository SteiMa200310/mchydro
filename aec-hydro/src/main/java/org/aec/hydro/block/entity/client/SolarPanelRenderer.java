package org.aec.hydro.block.entity.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import org.aec.hydro.block.entity.SolarPanelBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Environment(EnvType.CLIENT)
public class SolarPanelRenderer extends GeoBlockRenderer<SolarPanelBlockEntity> {
    public SolarPanelRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new SolarPanelModel());
    }
}
