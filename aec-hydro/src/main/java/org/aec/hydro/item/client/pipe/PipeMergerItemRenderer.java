package org.aec.hydro.item.client.pipe;

import org.aec.hydro.item.client.cable.CableMergerItemModel;
import org.aec.hydro.item.custom.PipeMergerItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PipeMergerItemRenderer extends GeoItemRenderer<PipeMergerItem> {
    public PipeMergerItemRenderer() {
        super(new PipeMergerItemModel());
    }
}
