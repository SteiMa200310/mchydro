package org.aec.hydro.utils.PipeHandling;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

public class PipeProperties {
    public static final EnumProperty<PipeID> PIPE_ID = EnumProperty.of("pipe_id", PipeID.class);
    public static final IntProperty PowerLevel = IntProperty.of("powerlevel", 0, 500);
}
