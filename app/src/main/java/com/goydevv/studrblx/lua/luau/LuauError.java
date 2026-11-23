package com.goydevv.studrblx.lua.luau;

import org.luaj.vm2.LuaError;

public final class LuauError extends LuaError {
    private final String raw;
    private final String luauTraceback;

    public LuauError(String message, String traceback) {
        super(message == null ? "nil" : message);
        this.raw = message == null ? "nil" : message;
        this.luauTraceback = traceback;
    }

    public String rawMessage() {
        return raw;
    }

    public String luauTraceback() {
        return luauTraceback;
    }

    @Override
    public String getMessage() {
        return raw;
    }
}
