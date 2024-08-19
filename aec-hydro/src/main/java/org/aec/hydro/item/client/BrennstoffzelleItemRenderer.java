package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.aec.hydro.item.custom.BrennstoffzelleItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Environment(EnvType.CLIENT)
public class BrennstoffzelleItemRenderer extends GeoItemRenderer<BrennstoffzelleItem> {
    public BrennstoffzelleItemRenderer() {
        super(new BrennstoffzelleItemModel());
    }
}
