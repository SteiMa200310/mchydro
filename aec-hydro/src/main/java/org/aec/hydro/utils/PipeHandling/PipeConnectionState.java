package org.aec.hydro.utils.PipeHandling;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Direction;

public class PipeConnectionState {
    public final DirectionResult directionResult1;
    public final DirectionResult directionResult2;

    private final String State;

    public PipeConnectionState() {
        this.directionResult1 = null;
        this.directionResult2 = null;

        this.State = "not";
    }

    public PipeConnectionState(DirectionResult directionResult) {
        this.directionResult1 = directionResult;
        this.directionResult2 = null;

        this.State = "one";
    }

    public PipeConnectionState(DirectionResult directionResult1, DirectionResult directionResult2) {
        this.directionResult1 = directionResult1;
        this.directionResult2 = directionResult2;

        this.State = "full";
    }

    public boolean IsNot() {
        return this.State.equals("not");
    }
    public boolean IsOne() {
        return this.State.equals("one");
    }
    public boolean IsFull() {
        return this.State.equals("full");
    }

    public static PipeConnectionState GetNot() {
        return new PipeConnectionState();
    }
    public static PipeConnectionState GetOne(DirectionResult directionResult) {
        return new PipeConnectionState(directionResult);
    }
    public static PipeConnectionState GetFull(DirectionResult directionResult1, DirectionResult directionResult2) {
        return new PipeConnectionState(directionResult1, directionResult2);
    }
}