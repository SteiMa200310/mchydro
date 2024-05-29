package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.aec.hydro.item.custom.WindMillItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Environment(EnvType.CLIENT)
public class WindMillItemRenderer extends GeoItemRenderer<WindMillItem> {
    public WindMillItemRenderer() {
        super(new WindMillItemModel());
    }
}
