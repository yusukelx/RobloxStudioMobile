package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.lua.luau.LuauState;
import com.goydevv.studrblx.model.Vector3;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

public final class LuauHost {
    private static final Set<String> NATIVE_OWNED = new HashSet<>(Arrays.asList(
            "print", "warn", "loadstring", "load", "dofile", "loadfile", "module", "package", "io", "luajava",
            "shared", "_G", "_VERSION", "assert", "error", "getmetatable", "setmetatable", "ipairs", "pairs", "next",
            "pcall", "xpcall", "ypcall", "rawequal", "rawget", "rawset", "rawlen", "select", "tonumber", "tostring",
            "type", "typeof", "unpack", "collectgarbage", "newproxy", "gcinfo", "getfenv", "setfenv", "__iter",
            "coroutine", "utf8", "buffer", "bit32", "vector", "package"));

    private static final Set<String> MERGED = new HashSet<>(Arrays.asList(
            "string", "table", "math", "os", "debug"));

    private LuauHost() {
    }

    public static Globals javaGlobals() {
        Globals g = new Globals();
        LuaTable pkg = new LuaTable();
        pkg.set("loaded", new LuaTable());
        g.set("package", pkg);
        g.load(new JseBaseLib());
        g.load(new Bit32Lib());
        g.load(new TableLib());
        g.load(new StringLib());
        g.load(new JseMathLib());
        g.load(new JseOsLib());
        g.set("debug", new LuaTable());
        g.set("dofile", LuaValue.NIL);
        g.set("loadfile", LuaValue.NIL);
        g.set("load", LuaValue.NIL);
        return g;
    }

    private static boolean isLuajBuiltin(LuaValue v) {
        return v.getClass().getName().startsWith("org.luaj.vm2.lib.");
    }

    private static LuaTable javaOnly(LuaTable src) {
        LuaTable out = new LuaTable();
        LuaValue k = LuaValue.NIL;
        while (true) {
            Varargs n = src.next(k);
            k = n.arg1();
            if (k.isnil()) {
                break;
            }
            if (!isLuajBuiltin(n.arg(2))) {
                out.rawset(k, n.arg(2));
            }
        }
        return out;
    }

    public static LuauState create(LuaRuntime rt) {
        LuauState state = new LuauState(new LuauState.Host() {
            @Override
            public void print(String text) {
                if (!rt.getStopped()) {
                    OutputLog.INSTANCE.print(text);
                }
            }

            @Override
            public void warn(String text) {
                if (!rt.getStopped()) {
                    OutputLog.INSTANCE.warn(text);
                }
            }

            @Override
            public String typeName(LuaValue value) {
                return LuaTypes.INSTANCE.typeOf(value);
            }

            @Override
            public LuaValue vector(float x, float y, float z) {
                return LuaTypes.INSTANCE.wrap(new Vector3(x, y, z));
            }
        });
        Globals g = rt.getGlobals();
        LuaValue k = LuaValue.NIL;
        while (true) {
            Varargs n = g.next(k);
            k = n.arg1();
            if (k.isnil()) {
                break;
            }
            if (!k.isstring()) {
                continue;
            }
            String name = k.tojstring();
            LuaValue v = n.arg(2);
            if (NATIVE_OWNED.contains(name)) {
                continue;
            }
            if (MERGED.contains(name)) {
                if (v.istable()) {
                    state.mergeLibrary(name, javaOnly((LuaTable) v.checktable()));
                }
                continue;
            }
            if (isLuajBuiltin(v)) {
                continue;
            }
            state.setGlobal(name, v);
        }
        state.sandbox();
        return state;
    }
}
