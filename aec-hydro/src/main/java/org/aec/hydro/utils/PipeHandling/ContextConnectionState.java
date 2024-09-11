package org.aec.hydro.utils.PipeHandling;

import net.minecraft.util.math.Direction;

public class ContextConnectionState {
    public final Direction contextDirection1;
    public final PipeContext context1;

    public final Direction contextDirection2;
    public final PipeContext context2;

    private final String State;

    public ContextConnectionState() {
        this.context1 = null;
        this.contextDirection1 = null;

        this.context2 = null;
        this.contextDirection2 = null;

        this.State = "not";
    }

    public ContextConnectionState(PipeContext context, Direction direction) {
        this.context1 = context;
        this.contextDirection1 = direction;

        this.context2 = null;
        this.contextDirection2 = null;

        this.State = "one";
    }

    public ContextConnectionState(PipeContext context1, Direction contextDirection1, PipeContext context2, Direction contextDirection2) {
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

    public static ContextConnectionState GetNot() {
        return new ContextConnectionState();
    }
    public static ContextConnectionState GetOne(PipeContext context, Direction direction) {
        return new ContextConnectionState(context, direction);
    }
    public static ContextConnectionState GetTwo(PipeContext context1, Direction contextDirection1, PipeContext context2, Direction contextDirection2) {
        return new ContextConnectionState(context1, contextDirection1, context2, contextDirection2);
    }
}