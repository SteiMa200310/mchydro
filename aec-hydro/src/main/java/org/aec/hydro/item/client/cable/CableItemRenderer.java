package org.aec.hydro.item.client.cable;

import org.aec.hydro.item.custom.CableItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CableItemRenderer extends GeoItemRenderer<CableItem> {
    public CableItemRenderer() {
        super(new CableItemModel());
    }
}
