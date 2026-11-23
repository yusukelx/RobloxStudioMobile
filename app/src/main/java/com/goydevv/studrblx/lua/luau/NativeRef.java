package com.goydevv.studrblx.lua.luau;

import org.luaj.vm2.LuaValue;

public final class NativeRef extends LuaValue {
    final LuauState state;
    final int ref;
    final String kind;

    NativeRef(LuauState state, int ref, String kind) {
        this.state = state;
        this.ref = ref;
        this.kind = kind;
    }

    public String kind() {
        return kind;
    }

    public LuaValue toJava() {
        return state.tableToJava(this);
    }

    @Override
    public int type() {
        return TUSERDATA;
    }

    @Override
    public String typename() {
        return kind;
    }

    @Override
    public LuaValue get(LuaValue key) {
        if ("table".equals(kind)) {
            return state.index(this, key);
        }
        return super.get(key);
    }

    @Override
    public String tojstring() {
        return kind + ": 0x" + Integer.toHexString(ref);
    }
}
