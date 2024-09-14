package org.aec.hydro.pipeHandling.core;

import net.minecraft.util.math.Direction;

public class ConnectionState {
    public final Direction contextDirection1;
    public final PipeContext context1;

    public final Direction contextDirection2;
    public final PipeContext context2;

    private final String State;

    public ConnectionState() {
        this.context1 = null;
        this.contextDirection1 = null;

        this.context2 = null;
        this.contextDirection2 = null;

        this.State = "not";
    }

    public ConnectionState(PipeContext context, Direction direction) {
        this.context1 = context;
        this.contextDirection1 = direction;

        this.context2 = null;
        this.contextDirection2 = null;

        this.State = "one";
    }

    public ConnectionState(PipeContext context1, Direction contextDirection1, PipeContext context2, Direction contextDirection2) {
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

    public static ConnectionState GetNot() {
        return new ConnectionState();
    }
    public static ConnectionState GetOne(PipeContext context, Direction direction) {
        return new ConnectionState(context, direction);
    }
    public static ConnectionState GetTwo(PipeContext context1, Direction contextDirection1, PipeContext context2, Direction contextDirection2) {
        return new ConnectionState(context1, contextDirection1, context2, contextDirection2);
    }
}