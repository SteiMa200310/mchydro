package org.aec.hydro.item.client;

import org.aec.hydro.item.custom.WindMillItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class WindMillItemRenderer extends GeoItemRenderer<WindMillItem> {
    public WindMillItemRenderer() {
        super(new WindMillItemModel());
    }
}
