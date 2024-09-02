package org.aec.hydro.utils.PipeHandling;

import net.minecraft.util.math.Direction;

public class DirectionResult {
    public final int PowerLevelNeighbor;
    public final Direction DirectionNeighbor;
    public final PipeNeighborType PipeNeighborType;

    public DirectionResult(int powerLevelNeighbor, Direction directionNeighbor, PipeNeighborType pipeNeighborType) {
        PowerLevelNeighbor = powerLevelNeighbor;
        DirectionNeighbor = directionNeighbor;
        PipeNeighborType = pipeNeighborType;
    }
}
