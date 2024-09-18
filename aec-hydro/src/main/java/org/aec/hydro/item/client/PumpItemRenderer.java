package org.aec.hydro.item.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.aec.hydro.item.custom.PumpItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Environment(EnvType.CLIENT)
public class PumpItemRenderer extends GeoItemRenderer<PumpItem> {
    public PumpItemRenderer() {
        super(new PumpItemModel());
    }
}
