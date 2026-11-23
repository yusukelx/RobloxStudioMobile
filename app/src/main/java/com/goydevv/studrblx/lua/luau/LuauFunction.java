package com.goydevv.studrblx.lua.luau;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public final class LuauFunction extends VarArgFunction {
    final LuauState state;
    final int ref;

    LuauFunction(LuauState state, int ref) {
        this.state = state;
        this.ref = ref;
        this.name = "luau";
    }

    public LuauState state() {
        return state;
    }

    @Override
    public Varargs invoke(Varargs args) {
        return state.call(this, args);
    }

    @Override
    public String tojstring() {
        return "function: 0x" + Integer.toHexString(ref);
    }
}
