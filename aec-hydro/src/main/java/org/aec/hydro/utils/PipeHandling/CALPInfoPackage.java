package org.aec.hydro.utils.PipeHandling;

import net.minecraft.util.math.Direction;

public class CALPInfoPackage {
    public Direction direction;

    public int PowerLevel = -1;
    public boolean IsLookingDirection = false;
    public boolean IsPowerProvider = false;
    public boolean IsConnectionSeeker = false;
    public boolean IsAlreadyConnected = false;

    public CALPInfoPackage(Direction direction) {
        this.direction = direction;
    }

    public boolean IsAny() {
        return IsLookingDirection || IsPowerProvider || IsConnectionSeeker || IsAlreadyConnected;
    }
}