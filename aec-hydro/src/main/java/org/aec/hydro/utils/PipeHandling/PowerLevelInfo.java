package org.aec.hydro.utils.PipeHandling;

import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.util.math.Direction;

public record PowerLevelInfo(int powerLevel, CustomDirection flowFrom, CustomDirection flowTo) { }
