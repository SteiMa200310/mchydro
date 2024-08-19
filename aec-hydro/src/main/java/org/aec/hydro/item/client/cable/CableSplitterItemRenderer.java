package org.aec.hydro.item.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.aec.hydro.item.custom.CableSplitterItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Environment(EnvType.CLIENT)
public class CableSplitterItemRenderer extends GeoItemRenderer<CableSplitterItem> {
    public CableSplitterItemRenderer() { super(new CableSplitterItemModel()); }
}
