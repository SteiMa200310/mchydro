package org.aec.hydro.pipeHandling.utils;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

public enum PowerFlowDirection implements StringIdentifiable {
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

    public PowerFlowDirection getOpposite() {
        return switch (this) {
            case NORTH -> PowerFlowDirection.SOUTH;
            case SOUTH -> PowerFlowDirection.NORTH;
            case EAST -> PowerFlowDirection.WEST;
            case WEST -> PowerFlowDirection.EAST;
            case UP -> PowerFlowDirection.DOWN;
            case DOWN -> PowerFlowDirection.UP;
            default -> NONE;
        };
    }

    public static PowerFlowDirection ConvertDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> PowerFlowDirection.NORTH;
            case SOUTH -> PowerFlowDirection.SOUTH;
            case EAST -> PowerFlowDirection.EAST;
            case WEST -> PowerFlowDirection.WEST;
            case UP -> PowerFlowDirection.UP;
            case DOWN -> PowerFlowDirection.DOWN;
        };
    }
}