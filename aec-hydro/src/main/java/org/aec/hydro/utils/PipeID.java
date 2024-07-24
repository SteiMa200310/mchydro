package org.aec.hydro.utils;

import net.minecraft.util.StringIdentifiable;

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
    E12,
    ;

    @Override
    public String asString() {
        return this.toString().toLowerCase();
    }
}
