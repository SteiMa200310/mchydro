package org.aec.hydro.item.client;

import org.aec.hydro.item.custom.BaggerItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BaggerItemRenderer extends GeoItemRenderer<BaggerItem> {
    public BaggerItemRenderer() { super(new BaggerItemModel()); }
}
