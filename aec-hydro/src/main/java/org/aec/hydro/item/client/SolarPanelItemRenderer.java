package org.aec.hydro.item.client;

import org.aec.hydro.item.custom.SolarPanelItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SolarPanelItemRenderer extends GeoItemRenderer<SolarPanelItem> {
    public SolarPanelItemRenderer() { super(new SolarPanelItemModel()); }
}
