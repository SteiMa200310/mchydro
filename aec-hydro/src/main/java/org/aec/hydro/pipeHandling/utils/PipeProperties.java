package org.aec.hydro.pipeHandling.utils;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

public class PipeProperties {
    public static final EnumProperty<PipeID> PIPE_ID = EnumProperty.of("pipe_id", PipeID.class);
    public static final EnumProperty<PowerFlowDirection> RecieverFace = EnumProperty.of("recieverface", PowerFlowDirection.class);
    public static final EnumProperty<PowerFlowDirection> ProviderFace = EnumProperty.of("providerface", PowerFlowDirection.class);
    public static final IntProperty PowerLevel = IntProperty.of("powerlevel", 0, 30);
}
