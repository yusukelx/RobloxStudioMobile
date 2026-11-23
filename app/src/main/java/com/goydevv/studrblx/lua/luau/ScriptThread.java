package com.goydevv.studrblx.lua.luau;

import org.luaj.vm2.LuaValue;

public final class ScriptThread extends LuaValue {
    final LuauState state;
    final long ptr;
    final int ref;
    volatile boolean released;
    boolean rawResults;
    LuauState.FinishListener onFinish;

    ScriptThread(LuauState state, long ptr, int ref) {
        this.state = state;
        this.ptr = ptr;
        this.ref = ref;
    }

    public LuauState state() {
        return state;
    }

    public boolean isDead() {
        return released;
    }

    public void setRawResults(boolean raw) {
        rawResults = raw;
    }

    public void setOnFinish(LuauState.FinishListener listener) {
        onFinish = listener;
    }

    public String status() {
        return state.status(this);
    }

    @Override
    public int type() {
        return TTHREAD;
    }

    @Override
    public String typename() {
        return "thread";
    }

    @Override
    public boolean isthread() {
        return true;
    }

    @Override
    public String tojstring() {
        return "thread: 0x" + Long.toHexString(ptr);
    }
}
