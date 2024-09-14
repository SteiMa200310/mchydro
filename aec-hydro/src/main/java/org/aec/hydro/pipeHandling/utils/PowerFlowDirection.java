package org.aec.hydro.utils.PipeHandling.Utils;

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

    public CustomDirection getOpposite() {
        return switch (this) {
            case NORTH -> CustomDirection.SOUTH;
            case SOUTH -> CustomDirection.NORTH;
            case EAST -> CustomDirection.WEST;
            case WEST -> CustomDirection.EAST;
            case UP -> CustomDirection.DOWN;
            case DOWN -> CustomDirection.UP;
            default -> NONE;
        };
    }

    public static CustomDirection ConvertDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> CustomDirection.NORTH;
            case SOUTH -> CustomDirection.SOUTH;
            case EAST -> CustomDirection.EAST;
            case WEST -> CustomDirection.WEST;
            case UP -> CustomDirection.UP;
            case DOWN -> CustomDirection.DOWN;
        };
    }
}