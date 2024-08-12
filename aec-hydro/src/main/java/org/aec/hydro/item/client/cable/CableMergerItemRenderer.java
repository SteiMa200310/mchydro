package org.aec.hydro.item.client.cable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.aec.hydro.item.custom.CableMergerItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Environment(EnvType.CLIENT)
public class CableMergerItemRenderer extends GeoItemRenderer<CableMergerItem> {
    public CableMergerItemRenderer() {
        super(new CableMergerItemModel());
    }
}
