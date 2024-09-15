package org.aec.hydro.pipeHandling.core;

import net.minecraft.util.math.Direction;

public class PipeConnectionState {
    public final Direction contextDirection1;
    public final EnergyContext context1;

    public final Direction contextDirection2;
    public final EnergyContext context2;

    private final String State;

    public PipeConnectionState() {
        this.context1 = null;
        this.contextDirection1 = null;

        this.context2 = null;
        this.contextDirection2 = null;

        this.State = "not";
    }

    public PipeConnectionState(EnergyContext context, Direction direction) {
        this.context1 = context;
        this.contextDirection1 = direction;

        this.context2 = null;
        this.contextDirection2 = null;

        this.State = "one";
    }

    public PipeConnectionState(EnergyContext context1, Direction contextDirection1, EnergyContext context2, Direction contextDirection2) {
        this.context1 = context1;
        this.contextDirection1 = contextDirection1;

        this.context2 = context2;
        this.contextDirection2 = contextDirection2;

        this.State = "two";
    }

    public boolean IsNot() {
        return this.State.equals("not");
    }
    public boolean IsOne() {
        return this.State.equals("one");
    }
    public boolean IsTwo() {
        return this.State.equals("two");
    }

    public static PipeConnectionState GetNot() {
        return new PipeConnectionState();
    }
    public static PipeConnectionState GetOne(EnergyContext context, Direction direction) {
        return new PipeConnectionState(context, direction);
    }
    public static PipeConnectionState GetTwo(EnergyContext context1, Direction contextDirection1, EnergyContext context2, Direction contextDirection2) {
        return new PipeConnectionState(context1, contextDirection1, context2, contextDirection2);
    }
}