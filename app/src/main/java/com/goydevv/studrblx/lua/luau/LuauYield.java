package com.goydevv.studrblx.lua.luau;

public final class LuauYield extends Error {
    final LuauState.Continuation continuation;

    public LuauYield() {
        this(null);
    }

    public LuauYield(LuauState.Continuation continuation) {
        super("yield", null, false, false);
        this.continuation = continuation;
    }
}
