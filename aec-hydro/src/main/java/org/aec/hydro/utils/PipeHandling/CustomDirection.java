package org.aec.hydro.utils.PipeHandling;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

public enum CustomDirection implements StringIdentifiable {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN,

    NONE;

    @Override
    public String asString() {
        return this.toString().toLowerCase();
    }

    public Direction toDirection() {
        return switch (this) {
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case EAST -> Direction.EAST;
            case WEST -> Direction.WEST;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            default -> null;
        };
    }
}
