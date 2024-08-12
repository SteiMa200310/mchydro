package org.aec.hydro.item.client.pipe;

import org.aec.hydro.item.custom.PipeSplitterItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PipeSplitterItemRenderer extends GeoItemRenderer<PipeSplitterItem> {
    public PipeSplitterItemRenderer() {
        super(new PipeSplitterItemModel());
    }
}
