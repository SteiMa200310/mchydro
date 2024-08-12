package org.aec.hydro.item.client;

import org.aec.hydro.item.custom.ElektrolyseurItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ElektrolyseurItemRenderer extends GeoItemRenderer<ElektrolyseurItem> {
    public ElektrolyseurItemRenderer() {
        super(new ElektorlyseurItemModel());
    }
}
