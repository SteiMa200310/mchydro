package org.aec.hydro.utils;

import net.minecraft.util.StringIdentifiable;

public enum PipeConnectionState implements StringIdentifiable {
    Not,
    One,
    Full;

    @Override
    public String asString() {
        return this.toString().toLowerCase();
    }
}
