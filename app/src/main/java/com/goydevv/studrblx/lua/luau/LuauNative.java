package com.goydevv.studrblx.lua.luau;

public final class LuauNative {
    public static final int TNIL = 0;
    public static final int TBOOLEAN = 1;
    public static final int TLIGHTUSERDATA = 2;
    public static final int TNUMBER = 3;
    public static final int TVECTOR = 4;
    public static final int TSTRING = 5;
    public static final int TTABLE = 6;
    public static final int TFUNCTION = 7;
    public static final int TUSERDATA = 8;
    public static final int TTHREAD = 9;
    public static final int TBUFFER = 10;

    public static final int OK = 0;
    public static final int YIELD = 1;

    public static final int CORUN = 0;
    public static final int COSUS = 1;
    public static final int CONOR = 2;
    public static final int COFIN = 3;
    public static final int COERR = 4;

    private static volatile boolean loaded;
    private static volatile Throwable loadError;

    private LuauNative() {
    }

    public static synchronized boolean ensureLoaded() {
        if (loaded) {
            return true;
        }
        if (loadError != null) {
            return false;
        }
        try {
            System.loadLibrary("luaubridge");
            loaded = true;
        } catch (Throwable t) {
            loadError = t;
        }
        return loaded;
    }

    public static Throwable loadError() {
        return loadError;
    }

    static native long newState();
    static native void closeState(long L);
    static native void sandbox(long L);
    static native void setStopped(long L, boolean stopped);
    static native void setDeadline(long L, long millis);
    static native int[] drainFreed(long L);

    public static native byte[] compile(byte[] source);
    public static native byte[] parseErrors(byte[] source);

    static native int load(long L, byte[] chunkName, byte[] bytecode, int envRef);
    static native int newEnv(long L);

    static native long newThread(long L);
    static native long toThread(long L, int idx);
    static native void pushThread(long L);
    static native int resume(long co, long from, int nargs);
    static native int costatus(long L, long co);
    static native int status(long co);
    static native boolean isYieldable(long L);
    static native byte[] traceback(long L);
    static native void resetThread(long co);

    static native boolean pushCallerEnv(long L);
    static native int getTop(long L);
    static native void setTop(long L, int idx);
    static native boolean checkStack(long L, int n);
    static native void xmove(long from, long to, int n);
    static native void pushValue(long L, int idx);
    static native void remove(long L, int idx);

    static native int type(long L, int idx);
    static native boolean toBoolean(long L, int idx);
    static native double toNumber(long L, int idx);
    static native byte[] toBytes(long L, int idx);
    static native float[] toVector(long L, int idx);
    static native int toProxy(long L, int idx);
    static native long toPointer(long L, int idx);

    static native void pushNil(long L);
    static native void pushBoolean(long L, boolean b);
    static native void pushNumber(long L, double d);
    static native void pushBytes(long L, byte[] bytes);
    static native void pushVector(long L, float x, float y, float z);
    static native boolean pushProxy(long L, int handle, boolean isFunction, byte[] typeName);
    static native void pushRef(long L, int ref);
    static native int ref(long L, int idx);
    static native void unref(long L, int ref);
    static native void pushGlobals(long L);

    static native void newTable(long L, int narr, int nrec);
    static native void rawSet(long L, int idx);
    static native void rawGet(long L, int idx);
    static native int rawLen(long L, int idx);
    static native boolean next(long L, int idx);
    static native boolean isReadonly(long L, int idx);

    static native int gcCountKb(long L);
    static native void gcStep(long L, int kb);
}
