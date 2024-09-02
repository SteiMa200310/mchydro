package org.aec.hydro.utils.PipeHandling;

import net.minecraft.util.StringIdentifiable;

public enum PipeType implements StringIdentifiable {
    WATER("water"),
    OXIGEN("oxigen"),
    HYDROGEN("hydrogen");

    private final String name;

    PipeType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}