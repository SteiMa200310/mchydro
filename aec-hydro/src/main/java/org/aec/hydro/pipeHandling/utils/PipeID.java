package org.aec.hydro.pipeHandling.utils;

import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

import java.util.List;

public enum PipeID implements StringIdentifiable {
    F1,
    F2,
    F3,

    E1,
    E2,
    E3,
    E4,

    E5,
    E6,
    E7,
    E8,

    E9,
    E10,
    E11,
    E12;

    public static final List<PipeID> FullSerialPipePriority = List.of(
            PipeID.F1,
            PipeID.F2,
            PipeID.F3
    );

    public static final List<PipeID> AllSerialPipePriority = List.of(
            PipeID.F1,
            PipeID.F2,
            PipeID.F3,

            PipeID.E1,
            PipeID.E2,
            PipeID.E3,
            PipeID.E4,

            PipeID.E5,
            PipeID.E6,
            PipeID.E7,
            PipeID.E8,

            PipeID.E9,
            PipeID.E10,
            PipeID.E11,
            PipeID.E12
    );

    @Override
    public String asString() {
        return this.toString().toLowerCase();
    }

    public Pair<Direction, Direction> GetOpenFacesBasedOnPipeId() {
        return switch (this) {
            case F1 -> new Pair<>(Direction.NORTH, Direction.SOUTH);
            case F2 -> new Pair<>(Direction.EAST, Direction.WEST);
            case F3 -> new Pair<>(Direction.UP, Direction.DOWN);

            case E1 -> new Pair<>(Direction.NORTH, Direction.EAST);
            case E2 -> new Pair<>(Direction.EAST, Direction.SOUTH);
            case E3 -> new Pair<>(Direction.SOUTH, Direction.WEST);
            case E4 -> new Pair<>(Direction.WEST, Direction.NORTH);

            case E5 -> new Pair<>(Direction.NORTH, Direction.UP);
            case E6 -> new Pair<>(Direction.NORTH, Direction.DOWN);
            case E7 -> new Pair<>(Direction.EAST, Direction.UP);
            case E8 -> new Pair<>(Direction.EAST, Direction.DOWN);

            case E9 -> new Pair<>(Direction.SOUTH, Direction.UP);
            case E10 -> new Pair<>(Direction.SOUTH, Direction.DOWN);
            case E11 -> new Pair<>(Direction.WEST, Direction.UP);
            case E12 -> new Pair<>(Direction.WEST, Direction.DOWN);
        };
    }
}