package org.aec.hydro.utils.PipeHandling;

import net.minecraft.util.StringIdentifiable;

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
}
