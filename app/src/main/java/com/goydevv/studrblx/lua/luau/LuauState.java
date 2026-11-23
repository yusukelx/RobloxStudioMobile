package com.goydevv.studrblx.lua.luau;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public final class LuauState {
    public interface Host {
        void print(String text);

        void warn(String text);

        String typeName(LuaValue value);

        LuaValue vector(float x, float y, float z);
    }

    public interface Continuation {
        Varargs resume(Varargs args);
    }

    public interface FinishListener {
        void finished(ScriptThread thread, ResumeResult result);
    }

    public static final class ResumeResult {
        public static final int DONE = 0;
        public static final int YIELDED = 1;
        public static final int ERROR = 2;
        public static final int DEAD = 3;

        public final int status;
        public final Varargs values;
        public final String error;
        public final String traceback;

        ResumeResult(int status, Varargs values, String error, String traceback) {
            this.status = status;
            this.values = values;
            this.error = error;
            this.traceback = traceback;
        }

        public boolean isError() {
            return status == ERROR;
        }

        public boolean isYielded() {
            return status == YIELDED;
        }
    }

    static final int OP_CALL = 0;
    static final int OP_INDEX = 1;
    static final int OP_NEWINDEX = 2;
    static final int OP_NAMECALL = 3;
    static final int OP_TOSTRING = 4;
    static final int OP_EQ = 5;
    static final int OP_LT = 6;
    static final int OP_LE = 7;
    static final int OP_ADD = 8;
    static final int OP_SUB = 9;
    static final int OP_MUL = 10;
    static final int OP_DIV = 11;
    static final int OP_MOD = 12;
    static final int OP_POW = 13;
    static final int OP_UNM = 14;
    static final int OP_LEN = 15;
    static final int OP_CONCAT = 16;
    static final int OP_IDIV = 17;
    static final int OP_ITER = 18;
    static final int OP_CONTINUE = 19;

    static final int RESULT_ERROR = -1;
    static final int RESULT_YIELD = -2;
    static final int RESULT_ERROR_RAW = -3;

    private static final int MAX_COPY_DEPTH = 64;
    private static final LuaValue IDIV = LuaValue.valueOf("__idiv");
    private static final LuaValue ITER = LuaValue.valueOf("__iter");
    private static final ResumeResult DEAD_RESULT = new ResumeResult(ResumeResult.DEAD, LuaValue.NONE, null, null);
    private static final ConcurrentHashMap<Long, LuauState> STATES = new ConcurrentHashMap<>();

    private final long L;
    private final Host host;
    private volatile boolean closed;
    private boolean closePending;
    private final ArrayList<LuaValue> objects = new ArrayList<>();
    private int[] refCounts = new int[256];
    private final ArrayDeque<Integer> freeHandles = new ArrayDeque<>();
    private final IdentityHashMap<LuaValue, Integer> handles = new IdentityHashMap<>();
    private final HashMap<Long, ScriptThread> threads = new HashMap<>();
    private final HashMap<Long, Continuation> continuations = new HashMap<>();
    private final ArrayDeque<Long> active = new ArrayDeque<>();
    private final ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
    private final HashSet<RefHolder> holders = new HashSet<>();
    private final HashMap<Long, WeakReference<LuauFunction>> functionCache = new HashMap<>();
    private final HashMap<String, byte[]> typeNames = new HashMap<>();
    private final HashMap<String, byte[]> bytecodeCache = new HashMap<>();
    private int resumeDepth;
    private int collectCounter;
    private long timeoutMillis = 10000;

    private static final class RefHolder extends PhantomReference<Object> {
        final int ref;
        final long pointer;

        RefHolder(Object owner, ReferenceQueue<Object> q, int ref, long pointer) {
            super(owner, q);
            this.ref = ref;
            this.pointer = pointer;
        }
    }

    public static boolean available() {
        return LuauNative.ensureLoaded();
    }

    public LuauState(Host host) {
        if (!LuauNative.ensureLoaded()) {
            Throwable t = LuauNative.loadError();
            throw new IllegalStateException("Luau native library unavailable: " + (t != null ? t.getMessage() : "unknown"), t);
        }
        this.host = host;
        long p = LuauNative.newState();
        if (p == 0) {
            throw new IllegalStateException("Luau state allocation failed");
        }
        this.L = p;
        STATES.put(p, this);
    }

    public Host host() {
        return host;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setTimeoutMillis(long ms) {
        timeoutMillis = ms;
    }

    public synchronized void setGlobal(String name, LuaValue value) {
        if (closed) {
            return;
        }
        LuauNative.pushGlobals(L);
        LuauNative.pushBytes(L, name.getBytes(StandardCharsets.UTF_8));
        push(L, value);
        LuauNative.rawSet(L, -3);
        LuauNative.setTop(L, -2);
    }

    public synchronized LuaValue getGlobal(String name) {
        if (closed) {
            return LuaValue.NIL;
        }
        LuauNative.pushGlobals(L);
        LuauNative.pushBytes(L, name.getBytes(StandardCharsets.UTF_8));
        LuauNative.rawGet(L, -2);
        LuaValue v = value(L, -1);
        LuauNative.setTop(L, -3);
        return v;
    }

    public synchronized boolean hasGlobal(String name) {
        if (closed) {
            return false;
        }
        LuauNative.pushGlobals(L);
        LuauNative.pushBytes(L, name.getBytes(StandardCharsets.UTF_8));
        LuauNative.rawGet(L, -2);
        boolean present = LuauNative.type(L, -1) != LuauNative.TNIL;
        LuauNative.setTop(L, -3);
        return present;
    }

    public synchronized void mergeLibrary(String name, LuaTable source) {
        if (closed) {
            return;
        }
        LuauNative.pushGlobals(L);
        LuauNative.pushBytes(L, name.getBytes(StandardCharsets.UTF_8));
        LuauNative.rawGet(L, -2);
        if (LuauNative.type(L, -1) != LuauNative.TTABLE) {
            LuauNative.setTop(L, -2);
            LuauNative.pushBytes(L, name.getBytes(StandardCharsets.UTF_8));
            push(L, source);
            LuauNative.rawSet(L, -3);
            LuauNative.setTop(L, -2);
            return;
        }
        if (LuauNative.isReadonly(L, -1)) {
            LuauNative.setTop(L, -3);
            return;
        }
        LuaValue k = LuaValue.NIL;
        while (true) {
            Varargs n = source.next(k);
            k = n.arg1();
            if (k.isnil()) {
                break;
            }
            push(L, k);
            if (LuauNative.type(L, -1) == LuauNative.TNIL) {
                LuauNative.setTop(L, -2);
                continue;
            }
            LuauNative.pushValue(L, -1);
            LuauNative.rawGet(L, -3);
            boolean exists = LuauNative.type(L, -1) != LuauNative.TNIL;
            LuauNative.setTop(L, -2);
            if (exists) {
                LuauNative.setTop(L, -2);
                continue;
            }
            push(L, n.arg(2));
            LuauNative.rawSet(L, -3);
        }
        LuauNative.setTop(L, -3);
    }

    public synchronized void sandbox() {
        if (!closed) {
            LuauNative.sandbox(L);
        }
    }

    public synchronized int newEnv() {
        if (closed) {
            return 0;
        }
        return LuauNative.newEnv(L);
    }

    public synchronized void envSet(int envRef, String name, LuaValue value) {
        if (closed || envRef <= 0) {
            return;
        }
        LuauNative.pushRef(L, envRef);
        LuauNative.pushBytes(L, name.getBytes(StandardCharsets.UTF_8));
        push(L, value);
        LuauNative.rawSet(L, -3);
        LuauNative.setTop(L, -2);
    }

    public synchronized void releaseRef(int ref) {
        if (!closed && ref > 0) {
            LuauNative.unref(L, ref);
        }
    }

    public static String compileError(String source) {
        if (!LuauNative.ensureLoaded()) {
            return "Luau native library unavailable";
        }
        byte[] bc = LuauNative.compile(source.getBytes(StandardCharsets.UTF_8));
        if (bc != null && bc.length > 0 && bc[0] == 0) {
            return new String(bc, 1, bc.length - 1, StandardCharsets.UTF_8);
        }
        return null;
    }

    public static final class SyntaxError {
        public final int line;
        public final int column;
        public final int endLine;
        public final int endColumn;
        public final String message;

        SyntaxError(int line, int column, int endLine, int endColumn, String message) {
            this.line = line;
            this.column = column;
            this.endLine = endLine;
            this.endColumn = endColumn;
            this.message = message;
        }
    }

    public static java.util.List<SyntaxError> parseErrors(String source) {
        ArrayList<SyntaxError> out = new ArrayList<>();
        if (!LuauNative.ensureLoaded()) {
            return out;
        }
        byte[] raw = LuauNative.parseErrors(source.getBytes(StandardCharsets.UTF_8));
        if (raw == null || raw.length == 0) {
            return out;
        }
        for (String line : new String(raw, StandardCharsets.UTF_8).split("\n")) {
            String[] parts = line.split(":", 5);
            if (parts.length < 5) {
                continue;
            }
            try {
                out.add(new SyntaxError(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4]));
            } catch (NumberFormatException ignored) {
            }
        }
        return out;
    }

    public synchronized LuauFunction load(String chunkName, String source, int envRef) {
        if (closed) {
            throw new LuaError("Script stopped");
        }
        byte[] bc = bytecodeCache.get(source);
        if (bc == null) {
            bc = LuauNative.compile(source.getBytes(StandardCharsets.UTF_8));
            if (bc == null || bc.length == 0) {
                throw new LuaError(chunkName + ": compile failed");
            }
            if (bc[0] == 0) {
                throw new LuaError(chunkName + new String(bc, 1, bc.length - 1, StandardCharsets.UTF_8));
            }
            if (bytecodeCache.size() > 256) {
                bytecodeCache.clear();
            }
            bytecodeCache.put(source, bc);
        }
        int status = LuauNative.load(L, ("=" + chunkName).getBytes(StandardCharsets.UTF_8), bc, envRef);
        if (status != 0) {
            String msg = stringAt(L, -1);
            LuauNative.setTop(L, -2);
            throw new LuaError(msg);
        }
        LuaValue fn = value(L, -1);
        LuauNative.setTop(L, -2);
        if (!(fn instanceof LuauFunction)) {
            throw new LuaError(chunkName + ": load produced no function");
        }
        return (LuauFunction) fn;
    }

    public synchronized ScriptThread newThread(LuaValue fn) {
        if (closed) {
            throw new LuaError("Script stopped");
        }
        long co = LuauNative.newThread(L);
        int ref = LuauNative.ref(L, -1);
        LuauNative.setTop(L, -2);
        ScriptThread t = new ScriptThread(this, co, ref);
        threads.put(co, t);
        if (fn != null) {
            LuauNative.checkStack(co, 2);
            push(co, fn);
        }
        return t;
    }

    public synchronized ResumeResult resume(ScriptThread t, Varargs args) {
        if (closed || t.released || t.state != this) {
            return DEAD_RESULT;
        }
        long co = t.ptr;
        int cs = LuauNative.costatus(L, co);
        if (cs != LuauNative.COSUS) {
            if (cs == LuauNative.COFIN || cs == LuauNative.COERR) {
                release(t);
            }
            return DEAD_RESULT;
        }
        int n = args == null ? 0 : args.narg();
        LuauNative.checkStack(co, n + 4);
        for (int i = 1; i <= n; i++) {
            push(co, args.arg(i));
        }
        long from = active.isEmpty() ? 0 : active.peek();
        boolean outer = resumeDepth == 0;
        if (outer) {
            LuauNative.setDeadline(L, timeoutMillis);
        }
        resumeDepth++;
        active.push(co);
        int st;
        try {
            st = LuauNative.resume(co, from, n);
        } finally {
            active.pop();
            resumeDepth--;
            if (outer) {
                LuauNative.setDeadline(L, 0);
            }
        }
        ResumeResult result;
        if (st == LuauNative.OK) {
            Varargs r = t.rawResults ? readRaw(co) : readAll(co);
            LuauNative.setTop(co, 0);
            release(t);
            result = new ResumeResult(ResumeResult.DONE, r, null, null);
        } else if (st == LuauNative.YIELD) {
            Varargs r = readAll(co);
            LuauNative.setTop(co, 0);
            result = new ResumeResult(ResumeResult.YIELDED, r, null, null);
        } else {
            String msg = errorText(co);
            byte[] tb = LuauNative.traceback(co);
            release(t);
            result = new ResumeResult(ResumeResult.ERROR, LuaValue.NONE, msg, tb == null ? null : new String(tb, StandardCharsets.UTF_8));
        }
        if (result.status != ResumeResult.YIELDED && t.onFinish != null) {
            FinishListener f = t.onFinish;
            t.onFinish = null;
            f.finished(t, result);
        }
        if (outer) {
            afterOuter();
        }
        return result;
    }

    public synchronized Varargs call(LuaValue fn, Varargs args) {
        ScriptThread t = newThread(fn);
        ResumeResult r = resume(t, args);
        if (r.isError()) {
            throw new LuauError(r.error, r.traceback);
        }
        return r.values;
    }

    public synchronized ScriptThread currentThread() {
        if (closed || active.isEmpty()) {
            return null;
        }
        long co = active.peek();
        if (!LuauNative.isYieldable(co)) {
            return null;
        }
        return threads.get(co);
    }

    public synchronized boolean isYieldable() {
        return !closed && !active.isEmpty() && LuauNative.isYieldable(active.peek());
    }

    public synchronized String status(ScriptThread t) {
        if (closed || t.released) {
            return "dead";
        }
        switch (LuauNative.costatus(L, t.ptr)) {
            case LuauNative.CORUN:
                return "running";
            case LuauNative.COSUS:
                return "suspended";
            case LuauNative.CONOR:
                return "normal";
            default:
                return "dead";
        }
    }

    public synchronized void cancel(ScriptThread t) {
        if (closed || t.released) {
            return;
        }
        if (LuauNative.costatus(L, t.ptr) == LuauNative.COSUS) {
            LuauNative.resetThread(t.ptr);
            continuations.remove(t.ptr);
            release(t);
        }
    }

    public synchronized LuaValue callerEnvGet(String name) {
        if (closed || active.isEmpty()) {
            return LuaValue.NIL;
        }
        long p = active.peek();
        LuauNative.checkStack(p, 4);
        if (!LuauNative.pushCallerEnv(p)) {
            return LuaValue.NIL;
        }
        if (LuauNative.type(p, -1) != LuauNative.TTABLE) {
            LuauNative.setTop(p, -2);
            return LuaValue.NIL;
        }
        LuauNative.pushBytes(p, name.getBytes(StandardCharsets.UTF_8));
        LuauNative.rawGet(p, -2);
        LuaValue v = value(p, -1);
        LuauNative.setTop(p, -3);
        return v;
    }

    public synchronized String traceback() {
        if (closed || active.isEmpty()) {
            return "";
        }
        byte[] tb = LuauNative.traceback(active.peek());
        return tb == null ? "" : new String(tb, StandardCharsets.UTF_8);
    }

    public void requestStop() {
        if (!closed) {
            LuauNative.setStopped(L, true);
        }
    }

    public synchronized void close() {
        if (closed) {
            return;
        }
        LuauNative.setStopped(L, true);
        if (resumeDepth > 0 || !active.isEmpty()) {
            closePending = true;
            return;
        }
        closeNow();
    }

    private void closeNow() {
        closed = true;
        STATES.remove(L);
        for (ScriptThread t : threads.values()) {
            t.released = true;
        }
        threads.clear();
        continuations.clear();
        holders.clear();
        functionCache.clear();
        objects.clear();
        handles.clear();
        LuauNative.closeState(L);
    }

    public synchronized int memoryKb() {
        return closed ? 0 : LuauNative.gcCountKb(L);
    }

    public synchronized void collect() {
        if (closed || !active.isEmpty()) {
            return;
        }
        drainHandles();
        if (++collectCounter % 64 == 0 && !threads.isEmpty()) {
            ArrayList<ScriptThread> dead = new ArrayList<>();
            for (ScriptThread t : threads.values()) {
                int cs = LuauNative.costatus(L, t.ptr);
                if ((cs == LuauNative.COFIN || cs == LuauNative.COERR) && !continuations.containsKey(t.ptr) && t.onFinish == null) {
                    dead.add(t);
                }
            }
            for (ScriptThread t : dead) {
                release(t);
            }
        }
        Reference<?> r;
        while ((r = refQueue.poll()) != null) {
            RefHolder h = (RefHolder) r;
            holders.remove(h);
            LuauNative.unref(L, h.ref);
            if (h.pointer != 0) {
                WeakReference<LuauFunction> w = functionCache.get(h.pointer);
                if (w != null && w.get() == null) {
                    functionCache.remove(h.pointer);
                }
            }
        }
    }

    public synchronized int threadCount() {
        return threads.size();
    }

    private void afterOuter() {
        if (closePending) {
            closePending = false;
            closeNow();
            return;
        }
        drainHandles();
    }

    private void drainHandles() {
        int[] freed = LuauNative.drainFreed(L);
        if (freed == null) {
            return;
        }
        for (int h : freed) {
            if (h < 0 || h >= objects.size()) {
                continue;
            }
            if (--refCounts[h] <= 0) {
                refCounts[h] = 0;
                LuaValue v = objects.get(h);
                if (v != null) {
                    handles.remove(v);
                }
                objects.set(h, null);
                freeHandles.push(h);
            }
        }
    }

    void release(ScriptThread t) {
        if (t.released) {
            return;
        }
        t.released = true;
        threads.remove(t.ptr);
        continuations.remove(t.ptr);
        if (!closed) {
            LuauNative.unref(L, t.ref);
        }
    }

    private String errorText(long co) {
        if (LuauNative.getTop(co) == 0) {
            return "Error occurred, no output from Luau";
        }
        int t = LuauNative.type(co, -1);
        if (t == LuauNative.TSTRING || t == LuauNative.TNUMBER) {
            return stringAt(co, -1);
        }
        if (t == LuauNative.TNIL) {
            return "nil";
        }
        try {
            LuaValue v = value(co, -1);
            return v.tojstring();
        } catch (Throwable e) {
            return "error object is not a string";
        }
    }

    private static String stringAt(long p, int idx) {
        byte[] b = LuauNative.toBytes(p, idx);
        if (b == null) {
            int t = LuauNative.type(p, idx);
            if (t == LuauNative.TNUMBER) {
                double d = LuauNative.toNumber(p, idx);
                return LuaValue.valueOf(d).tojstring();
            }
            return "";
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    static int dispatch(long main, long p, int handle, int op) {
        LuauState s = STATES.get(main);
        if (s == null || s.closed) {
            LuauNative.pushBytes(p, "Script stopped".getBytes(StandardCharsets.UTF_8));
            return RESULT_ERROR_RAW;
        }
        return s.onDispatch(p, handle, op);
    }

    static void log(long main, int kind, byte[] msg) {
        LuauState s = STATES.get(main);
        if (s == null) {
            return;
        }
        try {
            String text = msg == null ? "" : new String(msg, StandardCharsets.UTF_8);
            if (kind == 1) {
                s.host.warn(text);
            } else {
                s.host.print(text);
            }
        } catch (Throwable ignored) {
        }
    }

    private int onDispatch(long p, int handle, int op) {
        active.push(p);
        try {
            int top = LuauNative.getTop(p);
            Varargs res;
            switch (op) {
                case OP_CALL:
                    if (handle >= 0) {
                        LuaValue fn = handle < objects.size() ? objects.get(handle) : null;
                        if (fn == null) {
                            throw new LuaError("attempt to call a released function");
                        }
                        res = fn.invoke(args(p, 1, top));
                    } else {
                        LuaValue self = value(p, 1);
                        res = self.invoke(args(p, 2, top));
                    }
                    break;
                case OP_INDEX:
                    res = value(p, 1).get(value(p, 2));
                    break;
                case OP_NEWINDEX:
                    value(p, 1).set(value(p, 2), value(p, 3));
                    res = LuaValue.NONE;
                    break;
                case OP_NAMECALL: {
                    LuaValue name = value(p, top);
                    LuaValue self = value(p, 1);
                    LuaValue m = self.get(name);
                    if (m.isnil()) {
                        throw new LuaError(name.tojstring() + " is not a valid member of " + host.typeName(self));
                    }
                    res = m.invoke(LuaValue.varargsOf(self, args(p, 2, top - 1)));
                    break;
                }
                case OP_TOSTRING: {
                    LuaValue self = value(p, 1);
                    LuaValue mt = self.metatag(LuaValue.TOSTRING);
                    res = mt.isfunction() ? mt.call(self) : LuaValue.valueOf(self.tojstring());
                    break;
                }
                case OP_EQ:
                    res = LuaValue.valueOf(value(p, 1).eq_b(value(p, 2)));
                    break;
                case OP_LT:
                    res = LuaValue.valueOf(value(p, 1).lt_b(value(p, 2)));
                    break;
                case OP_LE:
                    res = LuaValue.valueOf(value(p, 1).lteq_b(value(p, 2)));
                    break;
                case OP_ADD:
                    res = value(p, 1).add(value(p, 2));
                    break;
                case OP_SUB:
                    res = value(p, 1).sub(value(p, 2));
                    break;
                case OP_MUL:
                    res = value(p, 1).mul(value(p, 2));
                    break;
                case OP_DIV:
                    res = value(p, 1).div(value(p, 2));
                    break;
                case OP_MOD:
                    res = value(p, 1).mod(value(p, 2));
                    break;
                case OP_POW:
                    res = value(p, 1).pow(value(p, 2));
                    break;
                case OP_UNM:
                    res = value(p, 1).neg();
                    break;
                case OP_LEN:
                    res = value(p, 1).len();
                    break;
                case OP_CONCAT:
                    res = value(p, 1).concat(value(p, 2));
                    break;
                case OP_IDIV: {
                    LuaValue a = value(p, 1);
                    LuaValue b = value(p, 2);
                    LuaValue m = a.metatag(IDIV);
                    if (m.isnil()) {
                        m = b.metatag(IDIV);
                    }
                    res = m.isfunction() ? m.call(a, b) : LuaValue.valueOf(Math.floor(a.div(b).todouble()));
                    break;
                }
                case OP_ITER: {
                    final LuaValue self = value(p, 1);
                    LuaValue m = self.metatag(ITER);
                    if (m.isfunction()) {
                        res = m.invoke(self);
                    } else if (self.istable()) {
                        res = LuaValue.varargsOf(new VarArgFunction() {
                            @Override
                            public Varargs invoke(Varargs a) {
                                return self.next(a.arg(2));
                            }
                        }, self, LuaValue.NIL);
                    } else {
                        throw new LuaError("attempt to iterate over a " + host.typeName(self) + " value");
                    }
                    break;
                }
                case OP_CONTINUE: {
                    Continuation c = continuations.remove(p);
                    Varargs a = args(p, 1, top);
                    res = c == null ? a : c.resume(a);
                    break;
                }
                default:
                    throw new LuaError("invalid bridge operation");
            }
            return pushResults(p, res);
        } catch (LuauYield y) {
            if (y.continuation != null) {
                continuations.put(p, y.continuation);
            } else {
                continuations.remove(p);
            }
            return RESULT_YIELD;
        } catch (LuauError e) {
            pushError(p, e);
            return RESULT_ERROR_RAW;
        } catch (LuaError e) {
            pushError(p, e);
            return RESULT_ERROR;
        } catch (StackOverflowError e) {
            LuauNative.pushBytes(p, "stack overflow".getBytes(StandardCharsets.UTF_8));
            return RESULT_ERROR;
        } catch (Throwable e) {
            String m = e.getMessage();
            LuauNative.pushBytes(p, (m != null ? m : e.getClass().getSimpleName()).getBytes(StandardCharsets.UTF_8));
            return RESULT_ERROR;
        } finally {
            active.pop();
        }
    }

    private void pushError(long p, LuaError e) {
        LuauNative.setTop(p, 0);
        LuaValue obj = e.getMessageObject();
        if (obj != null && !obj.isstring() && !obj.isnil()) {
            push(p, obj);
            return;
        }
        String m = e instanceof LuauError ? ((LuauError) e).rawMessage() : e.getMessage();
        LuauNative.pushBytes(p, (m == null ? "nil" : m).getBytes(StandardCharsets.UTF_8));
    }

    private int pushResults(long p, Varargs res) {
        if (res == null) {
            return 0;
        }
        int n = res.narg();
        if (n == 0) {
            return 0;
        }
        LuauNative.setTop(p, 0);
        if (!LuauNative.checkStack(p, n + 4)) {
            throw new LuaError("stack overflow");
        }
        for (int i = 1; i <= n; i++) {
            push(p, res.arg(i));
        }
        return n;
    }

    private Varargs args(long p, int from, int to) {
        if (to < from) {
            return LuaValue.NONE;
        }
        if (to == from) {
            return value(p, from);
        }
        LuaValue[] arr = new LuaValue[to - from + 1];
        for (int i = from; i <= to; i++) {
            arr[i - from] = value(p, i);
        }
        return LuaValue.varargsOf(arr);
    }

    private Varargs readAll(long co) {
        return args(co, 1, LuauNative.getTop(co));
    }

    private Varargs readRaw(long co) {
        int n = LuauNative.getTop(co);
        if (n == 0) {
            return LuaValue.NONE;
        }
        LuaValue[] arr = new LuaValue[n];
        for (int i = 1; i <= n; i++) {
            int t = LuauNative.type(co, i);
            if (t == LuauNative.TTABLE) {
                arr[i - 1] = holdRef(new NativeRef(this, LuauNative.ref(co, i), "table"), 0);
            } else {
                arr[i - 1] = value(co, i);
            }
        }
        return LuaValue.varargsOf(arr);
    }

    private <T> T holdRef(T owner, long pointer) {
        int ref = owner instanceof NativeRef ? ((NativeRef) owner).ref : ((LuauFunction) owner).ref;
        holders.add(new RefHolder(owner, refQueue, ref, pointer));
        return owner;
    }

    void push(long p, LuaValue v) {
        push(p, v, 0, null);
    }

    private void push(long p, LuaValue v, int depth, IdentityHashMap<LuaTable, Integer> seen) {
        if (v == null) {
            LuauNative.pushNil(p);
            return;
        }
        switch (v.type()) {
            case LuaValue.TBOOLEAN:
                LuauNative.pushBoolean(p, v.toboolean());
                return;
            case LuaValue.TNUMBER:
                LuauNative.pushNumber(p, v.todouble());
                return;
            case LuaValue.TSTRING: {
                LuaString s = v.checkstring();
                byte[] b = s.m_offset == 0 && s.m_length == s.m_bytes.length ? s.m_bytes : Arrays.copyOfRange(s.m_bytes, s.m_offset, s.m_offset + s.m_length);
                LuauNative.pushBytes(p, b);
                return;
            }
            case LuaValue.TFUNCTION:
                if (v instanceof LuauFunction && ((LuauFunction) v).state == this) {
                    LuauNative.pushRef(p, ((LuauFunction) v).ref);
                } else {
                    pushProxy(p, v, true);
                }
                return;
            case LuaValue.TTABLE:
                if (v instanceof LuaTable && v.getmetatable() == null) {
                    copyTable(p, (LuaTable) v, depth, seen);
                } else {
                    pushProxy(p, v, false);
                }
                return;
            case LuaValue.TUSERDATA:
                if (v instanceof NativeRef && ((NativeRef) v).state == this) {
                    LuauNative.pushRef(p, ((NativeRef) v).ref);
                } else {
                    pushProxy(p, v, false);
                }
                return;
            case LuaValue.TTHREAD:
                if (v instanceof ScriptThread && ((ScriptThread) v).state == this && !((ScriptThread) v).released) {
                    LuauNative.pushRef(p, ((ScriptThread) v).ref);
                } else {
                    LuauNative.pushNil(p);
                }
                return;
            case LuaValue.TNIL:
            case LuaValue.TNONE:
                LuauNative.pushNil(p);
                return;
            default:
                pushProxy(p, v, false);
        }
    }

    private void copyTable(long p, LuaTable t, int depth, IdentityHashMap<LuaTable, Integer> seen) {
        if (seen != null) {
            Integer at = seen.get(t);
            if (at != null) {
                LuauNative.pushValue(p, at);
                return;
            }
        }
        if (depth >= MAX_COPY_DEPTH) {
            LuauNative.pushNil(p);
            return;
        }
        if (seen == null) {
            seen = new IdentityHashMap<>();
        }
        LuauNative.checkStack(p, 8);
        LuauNative.newTable(p, t.length(), 4);
        int idx = LuauNative.getTop(p);
        seen.put(t, idx);
        LuaValue k = LuaValue.NIL;
        while (true) {
            Varargs n = t.next(k);
            k = n.arg1();
            if (k.isnil()) {
                break;
            }
            push(p, k, depth + 1, seen);
            if (LuauNative.type(p, -1) == LuauNative.TNIL) {
                LuauNative.setTop(p, -2);
                continue;
            }
            push(p, n.arg(2), depth + 1, seen);
            LuauNative.rawSet(p, idx);
        }
        seen.remove(t);
    }

    private void pushProxy(long p, LuaValue v, boolean isFunction) {
        Integer h = handles.get(v);
        if (h == null) {
            h = allocHandle(v);
        }
        byte[] name = isFunction ? null : typeName(v);
        if (LuauNative.pushProxy(p, h, isFunction, name)) {
            refCounts[h]++;
        }
    }

    private byte[] typeName(LuaValue v) {
        String n;
        try {
            n = host.typeName(v);
        } catch (Throwable e) {
            n = "userdata";
        }
        if (n == null || n.isEmpty()) {
            n = "userdata";
        }
        byte[] b = typeNames.get(n);
        if (b == null) {
            b = n.getBytes(StandardCharsets.UTF_8);
            typeNames.put(n, b);
        }
        return b;
    }

    private int allocHandle(LuaValue v) {
        int h;
        if (!freeHandles.isEmpty()) {
            h = freeHandles.pop();
            objects.set(h, v);
        } else {
            h = objects.size();
            objects.add(v);
            if (h >= refCounts.length) {
                refCounts = Arrays.copyOf(refCounts, refCounts.length * 2);
            }
        }
        refCounts[h] = 0;
        handles.put(v, h);
        return h;
    }

    LuaValue value(long p, int idx) {
        return value(p, idx, 0, null);
    }

    private LuaValue value(long p, int idx, int depth, HashMap<Long, LuaTable> seen) {
        int t = LuauNative.type(p, idx);
        switch (t) {
            case LuauNative.TNIL:
                return LuaValue.NIL;
            case LuauNative.TBOOLEAN:
                return LuauNative.toBoolean(p, idx) ? LuaValue.TRUE : LuaValue.FALSE;
            case LuauNative.TNUMBER:
                return LuaValue.valueOf(LuauNative.toNumber(p, idx));
            case LuauNative.TSTRING:
                return LuaString.valueOf(LuauNative.toBytes(p, idx));
            case LuauNative.TVECTOR: {
                float[] f = LuauNative.toVector(p, idx);
                return host.vector(f[0], f[1], f[2]);
            }
            case LuauNative.TTABLE:
                return copyOut(p, idx, depth, seen);
            case LuauNative.TFUNCTION: {
                int h = LuauNative.toProxy(p, idx);
                if (h >= 0 && h < objects.size() && objects.get(h) != null) {
                    return objects.get(h);
                }
                long ptr = LuauNative.toPointer(p, idx);
                WeakReference<LuauFunction> w = functionCache.get(ptr);
                LuauFunction f = w != null ? w.get() : null;
                if (f != null) {
                    return f;
                }
                f = new LuauFunction(this, LuauNative.ref(p, idx));
                holdRef(f, ptr);
                functionCache.put(ptr, new WeakReference<>(f));
                return f;
            }
            case LuauNative.TUSERDATA: {
                int h = LuauNative.toProxy(p, idx);
                if (h >= 0 && h < objects.size() && objects.get(h) != null) {
                    return objects.get(h);
                }
                return holdRef(new NativeRef(this, LuauNative.ref(p, idx), "userdata"), 0);
            }
            case LuauNative.TTHREAD: {
                long co = LuauNative.toThread(p, idx);
                ScriptThread st = threads.get(co);
                if (st != null) {
                    return st;
                }
                st = new ScriptThread(this, co, LuauNative.ref(p, idx));
                threads.put(co, st);
                return st;
            }
            case LuauNative.TBUFFER:
                return holdRef(new NativeRef(this, LuauNative.ref(p, idx), "buffer"), 0);
            default:
                return holdRef(new NativeRef(this, LuauNative.ref(p, idx), "userdata"), 0);
        }
    }

    private LuaValue copyOut(long p, int idx, int depth, HashMap<Long, LuaTable> seen) {
        if (idx < 0) {
            idx = LuauNative.getTop(p) + idx + 1;
        }
        long ptr = LuauNative.toPointer(p, idx);
        if (seen != null) {
            LuaTable prior = seen.get(ptr);
            if (prior != null) {
                return prior;
            }
        }
        LuaTable out = new LuaTable();
        if (depth >= MAX_COPY_DEPTH) {
            return out;
        }
        if (seen == null) {
            seen = new HashMap<>();
        }
        seen.put(ptr, out);
        LuauNative.checkStack(p, 6);
        LuauNative.pushNil(p);
        while (LuauNative.next(p, idx)) {
            LuaValue k = value(p, -2, depth + 1, seen);
            LuaValue v = value(p, -1, depth + 1, seen);
            if (!k.isnil()) {
                try {
                    out.rawset(k, v);
                } catch (LuaError ignored) {
                }
            }
            LuauNative.setTop(p, -2);
        }
        return out;
    }

    public synchronized LuaValue tableToJava(NativeRef ref) {
        if (closed || ref.state != this) {
            return LuaValue.NIL;
        }
        LuauNative.pushRef(L, ref.ref);
        LuaValue v = value(L, -1);
        LuauNative.setTop(L, -2);
        return v;
    }

    public synchronized LuaValue index(NativeRef ref, LuaValue key) {
        if (closed || ref.state != this) {
            return LuaValue.NIL;
        }
        LuauNative.pushRef(L, ref.ref);
        if (LuauNative.type(L, -1) != LuauNative.TTABLE) {
            LuauNative.setTop(L, -2);
            return LuaValue.NIL;
        }
        push(L, key);
        LuauNative.rawGet(L, -2);
        LuaValue v = value(L, -1);
        LuauNative.setTop(L, -3);
        return v;
    }

    synchronized Map<String, Integer> debugCounts() {
        HashMap<String, Integer> m = new HashMap<>();
        int live = 0;
        for (LuaValue o : objects) {
            if (o != null) {
                live++;
            }
        }
        m.put("handles", live);
        m.put("threads", threads.size());
        m.put("refs", holders.size());
        Iterator<Map.Entry<Long, WeakReference<LuauFunction>>> it = functionCache.entrySet().iterator();
        int fc = 0;
        while (it.hasNext()) {
            if (it.next().getValue().get() != null) {
                fc++;
            }
        }
        m.put("functions", fc);
        return m;
    }
}
