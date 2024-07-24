package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.aec.hydro.item.custom.WaterwheelItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Environment(EnvType.CLIENT)
public class WaterwheelItemRenderer extends GeoItemRenderer<WaterwheelItem> {
    public WaterwheelItemRenderer() { super(new WaterwheelItemModel()); }
}
