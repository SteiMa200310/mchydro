package org.aec.hydro.pipeHandling.utils;

import net.minecraft.util.math.Direction;
import org.aec.hydro.pipeHandling.core.EnergyContext;

public record PowerEvaluationContext(
        EnergyContext self,
        Direction openFace1,
        Direction openFace2,
        EnergyContext neighbor1,
        EnergyContext neighbor2,
        PowerFlowDirection cOpenFace1,
        PowerFlowDirection cOpenFace2) { }