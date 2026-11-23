package com.goydevv.studrblx.lua;

import android.content.Context;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.BrickColors;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.ContentValue;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.OptionalCFrame;
import com.goydevv.studrblx.model.PropNames;
import com.goydevv.studrblx.model.SharedStr;
import com.goydevv.studrblx.model.TreeListener;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.Vector3;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import com.goydevv.studrblx.lua.luau.LuauFunction;
import com.goydevv.studrblx.lua.luau.LuauState;
import com.goydevv.studrblx.lua.luau.LuauYield;
import com.goydevv.studrblx.lua.luau.ScriptThread;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/* JADX INFO: compiled from: LuaRuntime.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class LuaRuntime {
    private static final long WAIT_BUDGET_NS = 12000000;
    private final InstanceApi api;
    private final IdentityHashMap<Instance, HashMap<String, LuaValue>> callbacks;
    private final ArrayList<ChildWait> childWaits;

    /* JADX INFO: renamed from: commandEnv$delegate, reason: from kotlin metadata */
    private int commandEnv;
    private final LuauState luau;
    private final IdentityHashMap<Object, HashMap<String, ArrayList<Connection>>> conns;
    private final ArrayList<Pair<ScriptThread, Varargs>> deferred;
    private final Set<String> directEvents;
    private final DataModel dm;
    private Map<String, ? extends LuaValue> extraEnvForModules;
    private final Globals globals;
    private final LuaTable instanceMeta;
    private final boolean isPlay;
    private final ConcurrentLinkedQueue<Function0<Unit>> mainQueue;
    private final IdentityHashMap<Instance, LuaValue> moduleCache;
    private final IdentityHashMap<Instance, ArrayList<ScriptThread>> moduleLoading;
    private PlaySession play;
    private final LuaTable sharedTable;
    private final IdentityHashMap<Object, HashMap<String, ArrayList<ScriptThread>>> signalWaiters;
    private boolean stopped;
    private final Studio studio;
    private double time;
    private final ArrayList<Pair<Double, Function0<Unit>>> timers;
    private final TreeListener treeListener;
    private final ArrayList<Tween> tweens;
    private final ArrayList<Waiting> waiting;
    private final WeakHashMap<Instance, LuaValue> wrappers;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    public LuaRuntime(Studio studio, DataModel dm, boolean isPlay) {
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(dm, "dm");
        this.studio = studio;
        this.dm = dm;
        this.isPlay = isPlay;
        this.globals = LuauHost.javaGlobals();
        this.instanceMeta = new LuaTable();
        this.api = new InstanceApi(this);
        this.waiting = new ArrayList<>();
        this.deferred = new ArrayList<>();
        this.childWaits = new ArrayList<>();
        this.conns = new IdentityHashMap<>();
        this.signalWaiters = new IdentityHashMap<>();
        this.tweens = new ArrayList<>();
        this.moduleCache = new IdentityHashMap<>();
        this.wrappers = new WeakHashMap<>();
        this.callbacks = new IdentityHashMap<>();
        this.mainQueue = new ConcurrentLinkedQueue<>();
        this.sharedTable = new LuaTable();
        this.directEvents = SetsKt.setOf(new String[]{"Heartbeat", "RenderStepped", "Stepped", "PreSimulation", "PostSimulation", "PreRender", "Changed", "AttributeChanged"});
        this.treeListener = new TreeListener() { // from class: com.goydevv.studrblx.lua.LuaRuntime$treeListener$1
            @Override // com.goydevv.studrblx.model.TreeListener
            public void childAdded(Instance parent, Instance child) {
                PlaySession play;
                Intrinsics.checkNotNullParameter(parent, "parent");
                Intrinsics.checkNotNullParameter(child, "child");
                if (LuaRuntime.this.getStopped()) {
                    return;
                }
                if (LuaRuntime.this.hasConn(parent, "ChildAdded")) {
                    LuaRuntime.this.fire(parent, "ChildAdded", LuaRuntime.this.wrap(child));
                }
                for (Instance p = parent; p != null; p = p.getParent()) {
                    if (LuaRuntime.this.hasConn(p, "DescendantAdded")) {
                        LuaRuntime.this.fire(p, "DescendantAdded", LuaRuntime.this.wrap(child));
                        for (Instance d : Instance.descendants$default(child, null, 1, null)) {
                            LuaRuntime.this.fire(p, "DescendantAdded", LuaRuntime.this.wrap(d));
                        }
                    }
                }
                LuaRuntime.this.notifyAncestry(child);
                if (!LuaRuntime.this.childWaits.isEmpty()) {
                    LuaRuntime.this.checkChildWaits();
                }
                if (!LuaRuntime.this.getIsPlay() || (play = LuaRuntime.this.getPlay()) == null) {
                    return;
                }
                play.onChildAdded(parent, child);
            }

            @Override // com.goydevv.studrblx.model.TreeListener
            public void childRemoved(Instance parent, Instance child) {
                PlaySession play;
                Intrinsics.checkNotNullParameter(parent, "parent");
                Intrinsics.checkNotNullParameter(child, "child");
                if (LuaRuntime.this.getStopped()) {
                    return;
                }
                if (LuaRuntime.this.hasConn(parent, "ChildRemoved")) {
                    LuaRuntime.this.fire(parent, "ChildRemoved", LuaRuntime.this.wrap(child));
                }
                for (Instance p = parent; p != null; p = p.getParent()) {
                    if (LuaRuntime.this.hasConn(p, "DescendantRemoving")) {
                        LuaRuntime.this.fire(p, "DescendantRemoving", LuaRuntime.this.wrap(child));
                    }
                }
                LuaRuntime.this.notifyAncestry(child);
                if (child.getDestroyed()) {
                    if (LuaRuntime.this.hasConn(child, "Destroying")) {
                        LuaRuntime.this.fire(child, "Destroying", new LuaValue[0]);
                    }
                    LuaRuntime.this.disconnectTree(child);
                }
                if (!LuaRuntime.this.getIsPlay() || (play = LuaRuntime.this.getPlay()) == null) {
                    return;
                }
                play.onChildRemoved(parent, child);
            }

            @Override // com.goydevv.studrblx.model.TreeListener
            public void propChanged(Instance inst, String name) {
                PlaySession play;
                Intrinsics.checkNotNullParameter(inst, "inst");
                Intrinsics.checkNotNullParameter(name, "name");
                if (LuaRuntime.this.getStopped()) {
                    return;
                }
                if (Intrinsics.areEqual(name, "Name") && !LuaRuntime.this.childWaits.isEmpty()) {
                    LuaRuntime.this.checkChildWaits();
                }
                if (LuaRuntime.this.getIsPlay() && Intrinsics.areEqual(name, "Disabled") && inst.isA("BaseScript") && Intrinsics.areEqual(inst.get("Disabled"), (Object) false) && (play = LuaRuntime.this.getPlay()) != null) {
                    play.maybeRunScript(inst);
                }
                HashMap m = (HashMap) LuaRuntime.this.conns.get(inst);
                if (m == null) {
                    return;
                }
                String disp = PropNames.INSTANCE.display(name);
                if (m.containsKey("Changed")) {
                    if (inst.isA("ValueBase") && Intrinsics.areEqual(disp, "Value")) {
                        LuaRuntime.this.fire(inst, "Changed", LuaRuntime.this.toLua(inst.get(name), inst, name));
                    } else {
                        LuaRuntime luaRuntime = LuaRuntime.this;
                        LuaString luaStringValueOf = LuaValue.valueOf(disp);
                        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
                        luaRuntime.fire(inst, "Changed", luaStringValueOf);
                    }
                }
                if (m.containsKey("Changed:" + disp)) {
                    LuaRuntime.this.fire(inst, "Changed:" + disp, new LuaValue[0]);
                }
                if (Intrinsics.areEqual(disp, "CFrame")) {
                    if (m.containsKey("Changed:Position")) {
                        LuaRuntime.this.fire(inst, "Changed:Position", new LuaValue[0]);
                    }
                    if (m.containsKey("Changed:Orientation")) {
                        LuaRuntime.this.fire(inst, "Changed:Orientation", new LuaValue[0]);
                    }
                }
                if ((Intrinsics.areEqual(disp, "Health_XML") || Intrinsics.areEqual(disp, "Health")) && m.containsKey("HealthChanged")) {
                    LuaRuntime luaRuntime2 = LuaRuntime.this;
                    LuaValue[] luaValueArr = new LuaValue[1];
                    LuaRuntime luaRuntime3 = LuaRuntime.this;
                    Object obj = inst.get("Health_XML");
                    if (obj == null) {
                        obj = inst.get("Health");
                    }
                    luaValueArr[0] = LuaRuntime.toLua$default(luaRuntime3, obj, null, null, 6, null);
                    luaRuntime2.fire(inst, "HealthChanged", luaValueArr);
                }
            }
        };
        LuaTypes.INSTANCE.install(this.globals);
        RobloxGlobals.INSTANCE.install(this);
        this.luau = LuauHost.create(this);
        this.dm.getListeners().add(this.treeListener);
        this.timers = new ArrayList<>();
        this.extraEnvForModules = MapsKt.emptyMap();
        this.moduleLoading = new IdentityHashMap<>();
    }

    public final DataModel getDm() {
        return this.dm;
    }

    public final Studio getStudio() {
        return this.studio;
    }

    /* JADX INFO: renamed from: isPlay, reason: from getter */
    public final boolean getIsPlay() {
        return this.isPlay;
    }

    public final Globals getGlobals() {
        return this.globals;
    }

    public final LuaTable getInstanceMeta() {
        return this.instanceMeta;
    }

    public final InstanceApi getApi() {
        return this.api;
    }

    public final PlaySession getPlay() {
        return this.play;
    }

    public final void setPlay(PlaySession playSession) {
        this.play = playSession;
    }

    public final double getTime() {
        return this.time;
    }

    public final void setTime(double d) {
        this.time = d;
    }

    public final boolean getStopped() {
        return this.stopped;
    }

    public final void setStopped(boolean z) {
        this.stopped = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: LuaRuntime.kt */
    static final class Waiting {
        private final double at;
        private final ScriptThread co;
        private final double start;

        public Waiting(ScriptThread co, double at, double start) {
            Intrinsics.checkNotNullParameter(co, "co");
            this.co = co;
            this.at = at;
            this.start = start;
        }

        public final double getAt() {
            return this.at;
        }

        public final ScriptThread getCo() {
            return this.co;
        }

        public final double getStart() {
            return this.start;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: LuaRuntime.kt */
    static final class ChildWait {
        private final ScriptThread co;
        private final double deadline;
        private final String name;
        private final Instance parent;
        private final double start;
        private boolean warned;

        public ChildWait(Instance parent, String name, ScriptThread co, double deadline, double start) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(co, "co");
            this.parent = parent;
            this.name = name;
            this.co = co;
            this.deadline = deadline;
            this.start = start;
        }

        public final ScriptThread getCo() {
            return this.co;
        }

        public final double getDeadline() {
            return this.deadline;
        }

        public final String getName() {
            return this.name;
        }

        public final Instance getParent() {
            return this.parent;
        }

        public final double getStart() {
            return this.start;
        }

        public final boolean getWarned() {
            return this.warned;
        }

        public final void setWarned(boolean z) {
            this.warned = z;
        }
    }

    public final ArrayList<Tween> getTweens() {
        return this.tweens;
    }

    public final IdentityHashMap<Instance, LuaValue> getModuleCache() {
        return this.moduleCache;
    }

    public final IdentityHashMap<Instance, HashMap<String, LuaValue>> getCallbacks() {
        return this.callbacks;
    }

    public final ConcurrentLinkedQueue<Function0<Unit>> getMainQueue() {
        return this.mainQueue;
    }

    public final LuaTable getSharedTable() {
        return this.sharedTable;
    }

    public final TreeListener getTreeListener() {
        return this.treeListener;
    }

    public final void shutdown() {
        this.stopped = true;
        this.luau.requestStop();
        this.dm.getListeners().remove(this.treeListener);
        this.waiting.clear();
        this.deferred.clear();
        this.childWaits.clear();
        this.conns.clear();
        this.signalWaiters.clear();
        this.tweens.clear();
        this.timers.clear();
        this.moduleLoading.clear();
        this.luau.close();
    }

    public final LuauState getLuau() {
        return this.luau;
    }

    public final void setGlobal(String name, LuaValue value) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(value, "value");
        this.globals.set(name, value);
        this.luau.setGlobal(name, value);
    }

    public final LuaValue wrap(Instance inst) {
        LuaUserdata luaUserdata;
        if (inst == null) {
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            return NIL;
        }
        Map map = this.wrappers;
        LuaValue luaValue = (LuaValue) map.get(inst);
        if (luaValue == null) {
            luaUserdata = new LuaUserdata(inst, this.instanceMeta);
            map.put(inst, luaUserdata);
        } else {
            luaUserdata = (LuaUserdata) luaValue;
        }
        return luaUserdata;
    }

    public static /* synthetic */ LuaValue toLua$default(LuaRuntime luaRuntime, Object obj, Instance instance, String str, int i, Object obj2) {
        if ((i & 2) != 0) {
            instance = null;
        }
        if ((i & 4) != 0) {
            str = null;
        }
        return luaRuntime.toLua(obj, instance, str);
    }

    public final LuaValue toLua(Object v, Instance owner, String prop) {
        LuaValue luaValueWrap;
        LuaRuntime luaRuntime = this;
        if (v == null) {
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            return NIL;
        }
        if (v instanceof LuaValue) {
            return (LuaValue) v;
        }
        if (v instanceof String) {
            LuaString luaStringValueOf = LuaValue.valueOf((String) v);
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
            return luaStringValueOf;
        }
        if (v instanceof Boolean) {
            LuaBoolean luaBooleanValueOf = LuaValue.valueOf(((Boolean) v).booleanValue());
            Intrinsics.checkNotNullExpressionValue(luaBooleanValueOf, "valueOf(...)");
            return luaBooleanValueOf;
        }
        if (v instanceof Integer) {
            LuaInteger luaIntegerValueOf = LuaValue.valueOf(((Number) v).intValue());
            Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
            return luaIntegerValueOf;
        }
        if (v instanceof Long) {
            LuaNumber luaNumberValueOf = LuaValue.valueOf(((Number) v).longValue());
            Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
            return luaNumberValueOf;
        }
        if (v instanceof Float) {
            LuaNumber luaNumberValueOf2 = LuaValue.valueOf(((Number) v).floatValue());
            Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
            return luaNumberValueOf2;
        }
        if (v instanceof Double) {
            LuaNumber luaNumberValueOf3 = LuaValue.valueOf(((Number) v).doubleValue());
            Intrinsics.checkNotNullExpressionValue(luaNumberValueOf3, "valueOf(...)");
            return luaNumberValueOf3;
        }
        if (v instanceof Instance) {
            return luaRuntime.wrap((Instance) v);
        }
        if (v instanceof EnumValue) {
            String type = (owner == null || prop == null) ? null : Enums.INSTANCE.typeForProp(owner.getClassName(), prop);
            String name = Enums.INSTANCE.itemName(type, ((EnumValue) v).getValue());
            if (type != null && name != null) {
                return LuaTypes.INSTANCE.wrap(new EnumItemV(type, name, ((EnumValue) v).getValue()));
            }
            LuaInteger luaIntegerValueOf2 = LuaValue.valueOf(((EnumValue) v).getValue());
            Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf2, "valueOf(...)");
            return luaIntegerValueOf2;
        }
        if (v instanceof BinaryString) {
            LuaString luaStringValueOf2 = LuaValue.valueOf(((BinaryString) v).getBytes());
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf2, "valueOf(...)");
            return luaStringValueOf2;
        }
        if (v instanceof SharedStr) {
            LuaString luaStringValueOf3 = LuaValue.valueOf(((SharedStr) v).getBytes());
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf3, "valueOf(...)");
            return luaStringValueOf3;
        }
        if (v instanceof OptionalCFrame) {
            CFrame cframe = ((OptionalCFrame) v).getCframe();
            if (cframe == null || (luaValueWrap = LuaTypes.INSTANCE.wrap(cframe)) == null) {
                luaValueWrap = LuaValue.NIL;
            }
            Intrinsics.checkNotNull(luaValueWrap);
            return luaValueWrap;
        }
        if (v instanceof ContentValue) {
            if (((ContentValue) v).getSourceType() == 2) {
                return luaRuntime.wrap(((ContentValue) v).getObj());
            }
            LuaString luaStringValueOf4 = LuaValue.valueOf(((ContentValue) v).getUri());
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf4, "valueOf(...)");
            return luaStringValueOf4;
        }
        if (v instanceof RaycastResultV) {
            return LuaTypes.INSTANCE.wrap(v);
        }
        if (!(v instanceof List)) {
            return LuaTypes.INSTANCE.wrap(v);
        }
        LuaTable luaTable = new LuaTable();
        Iterator it = ((Iterable) v).iterator();
        while (it.hasNext()) {
            luaTable.insert(0, toLua$default(luaRuntime, it.next(), null, null, 6, null));
            luaRuntime = this;
        }
        return luaTable;
    }

    public static /* synthetic */ Object fromLua$default(LuaRuntime luaRuntime, LuaValue luaValue, Integer num, Instance instance, String str, int i, Object obj) {
        if ((i & 4) != 0) {
            instance = null;
        }
        if ((i & 8) != 0) {
            str = null;
        }
        return luaRuntime.fromLua(luaValue, num, instance, str);
    }

    public final Object fromLua(LuaValue v, Integer type, Instance inst, String prop) {
        Integer num;
        Intrinsics.checkNotNullParameter(v, "v");
        String strTypeForProp = null;
        if (v.isnil()) {
            if (type == null || type.intValue() != 30) {
                return null;
            }
            return new OptionalCFrame(null);
        }
        Object obj = v.isuserdata() ? v.touserdata() : null;
        if (type != null && type.intValue() == 2) {
            return Boolean.valueOf(v.toboolean());
        }
        if (type != null && type.intValue() == 3) {
            return Integer.valueOf(obj instanceof EnumItemV ? ((EnumItemV) obj).getValue() : (int) v.checkdouble());
        }
        if (type != null && type.intValue() == 4) {
            return Float.valueOf((float) v.checkdouble());
        }
        if (type != null && type.intValue() == 5) {
            return Double.valueOf(v.checkdouble());
        }
        if (type != null && type.intValue() == 27) {
            return Long.valueOf((long) v.checkdouble());
        }
        if (type != null && type.intValue() == 1) {
            if (v.isstring() || v.isnumber()) {
                return v.tojstring();
            }
            throw new LuaError("string expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 18) {
            if (obj instanceof EnumItemV) {
                return new EnumValue(((EnumItemV) obj).getValue());
            }
            if (v.isnumber()) {
                return new EnumValue(v.toint());
            }
            if (v.isstring()) {
                if (inst != null && prop != null) {
                    strTypeForProp = Enums.INSTANCE.typeForProp(inst.getClassName(), prop);
                }
                LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get(strTypeForProp);
                if (linkedHashMap == null || (num = linkedHashMap.get(v.tojstring())) == null) {
                    throw new LuaError("Invalid value \"" + v.tojstring() + "\" for enum " + (strTypeForProp == null ? "?" : strTypeForProp));
                }
                return new EnumValue(num.intValue());
            }
            throw new LuaError("EnumItem expected");
        }
        if (type != null && type.intValue() == 11) {
            if (obj instanceof BrickColorV) {
                return (BrickColorV) obj;
            }
            return obj instanceof Color3 ? new BrickColorV(BrickColors.INSTANCE.closest((Color3) obj)) : new BrickColorV(v.checkint());
        }
        if ((type != null && type.intValue() == 12) || (type != null && type.intValue() == 26)) {
            if (obj instanceof Color3) {
                return (Color3) obj;
            }
            if (obj instanceof BrickColorV) {
                return ((BrickColorV) obj).getColor();
            }
            throw new LuaError("Color3 expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 19) {
            Instance instance = obj instanceof Instance ? (Instance) obj : null;
            if (instance != null) {
                return instance;
            }
            throw new LuaError("Instance expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 30) {
            return new OptionalCFrame(obj instanceof CFrame ? (CFrame) obj : null);
        }
        if (type != null && type.intValue() == 34) {
            if (obj instanceof Instance) {
                return new ContentValue(2, "", (Instance) obj);
            }
            String str = v.tojstring();
            Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
            return new ContentValue(1, str, null, 4, null);
        }
        if (type != null && type.intValue() == 14) {
            Vector3 vector3 = obj instanceof Vector3 ? (Vector3) obj : null;
            if (vector3 != null) {
                return vector3;
            }
            throw new LuaError("Vector3 expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 16) {
            CFrame cFrame = obj instanceof CFrame ? (CFrame) obj : null;
            if (cFrame != null) {
                return cFrame;
            }
            throw new LuaError("CFrame expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (type != null && type.intValue() == 7) {
            UDim2 uDim2 = obj instanceof UDim2 ? (UDim2) obj : null;
            if (uDim2 != null) {
                return uDim2;
            }
            throw new LuaError("UDim2 expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        if (obj != null) {
            return obj instanceof EnumItemV ? new EnumValue(((EnumItemV) obj).getValue()) : obj;
        }
        if (v.isboolean()) {
            return Boolean.valueOf(v.toboolean());
        }
        if (v.isnumber()) {
            return Double.valueOf(v.todouble());
        }
        if (v.isstring()) {
            return v.tojstring();
        }
        throw new LuaError("Unable to assign value of type " + LuaTypes.INSTANCE.typeOf(v));
    }

    public final boolean hasConn(Object owner, String name) {
        ArrayList<ScriptThread> arrayList;
        ArrayList<Connection> arrayList2;
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        HashMap<String, ArrayList<Connection>> map = this.conns.get(owner);
        if (!((map == null || (arrayList2 = map.get(name)) == null || !(arrayList2.isEmpty() ^ true)) ? false : true)) {
            HashMap<String, ArrayList<ScriptThread>> map2 = this.signalWaiters.get(owner);
            if (!((map2 == null || (arrayList = map2.get(name)) == null || !(arrayList.isEmpty() ^ true)) ? false : true)) {
                return false;
            }
        }
        return true;
    }

    public final Connection connect(Object owner, String name, LuaValue fn, boolean once) {
        HashMap<String, ArrayList<Connection>> map;
        ArrayList<Connection> arrayList;
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(fn, "fn");
        Connection c = new Connection(owner, name, fn, once);
        Map map2 = this.conns;
        HashMap<String, ArrayList<Connection>> map3 = (HashMap<String, ArrayList<Connection>>) map2.get(owner);
        if (map3 == null) {
            map = new HashMap<>();
            map2.put(owner, map);
        } else {
            map = map3;
        }
        Map map4 = map;
        ArrayList<Connection> arrayList2 = (ArrayList<Connection>) map4.get(name);
        if (arrayList2 == null) {
            arrayList = new ArrayList<>();
            map4.put(name, arrayList);
        } else {
            arrayList = arrayList2;
        }
        arrayList.add(c);
        return c;
    }

    public final void disconnect(Connection c) {
        ArrayList<Connection> arrayList;
        Intrinsics.checkNotNullParameter(c, "c");
        c.setConnected(false);
        HashMap<String, ArrayList<Connection>> map = this.conns.get(c.getOwner());
        if (map == null || (arrayList = map.get(c.getName())) == null) {
            return;
        }
        arrayList.remove(c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void disconnectTree(Instance i) {
        this.conns.remove(i);
        this.signalWaiters.remove(i);
        this.callbacks.remove(i);
        Iterator<Instance> it = i.getChildren().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance c = it.next();
            disconnectTree(c);
        }
    }

    public final Varargs waitSignal(Object owner, String name) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        ScriptThread co = currentCoroutine();
        if (co == null) {
            throw new LuaError("Wait can only be called from a yieldable thread");
        }
        HashMap<String, ArrayList<ScriptThread>> map = this.signalWaiters.get(owner);
        if (map == null) {
            map = new HashMap<>();
            this.signalWaiters.put(owner, map);
        }
        ArrayList<ScriptThread> list = map.get(name);
        if (list == null) {
            list = new ArrayList<>();
            map.put(name, list);
        }
        list.add(co);
        throw new LuauYield();
    }

    public final void fire(Object owner, String name, LuaValue... args) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(args, "args");
        Varargs varargsVarargsOf = LuaValue.varargsOf(args);
        Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
        fireArgs(owner, name, varargsVarargsOf);
    }

    /* JADX WARN: Code duplicated, block: B:19:0x004d  */
    public final void fireArgs(Object owner, String name, Varargs args) {
        boolean direct = false;
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(args, "args");
        if (this.stopped) {
            return;
        }
        HashMap<String, ArrayList<Connection>> map = this.conns.get(owner);
        ArrayList<Connection> arrayList = map != null ? map.get(name) : null;
        if (arrayList != null && !arrayList.isEmpty()) {
            if (!this.directEvents.contains(name)) {
                direct = StringsKt.startsWith(name, "Changed:", false) || StringsKt.startsWith(name, "Attr:", false);
            }
            for (Connection c : CollectionsKt.toList(arrayList)) {
                if (c.getConnected()) {
                    if (c.getOnce()) {
                        disconnect(c);
                    }
                    LuaValue fn = c.getFn();
                    if (direct) {
                        callDirect(fn, args);
                    } else {
                        spawn(fn, args);
                    }
                }
            }
        }
        HashMap<String, ArrayList<ScriptThread>> map2 = this.signalWaiters.get(owner);
        ArrayList ws = map2 != null ? map2.remove(name) : null;
        if (ws == null) {
            return;
        }
        Iterator it = ws.iterator();
        while (it.hasNext()) {
            resume((ScriptThread) it.next(), args);
        }
    }

    public final ScriptThread currentCoroutine() {
        return this.luau.currentThread();
    }

    public final boolean isYieldable() {
        return this.luau.isYieldable();
    }

    public final Varargs yieldWith(LuauState.Continuation continuation) {
        throw new LuauYield(continuation);
    }

    public final Varargs callDirect(LuaValue fn, Varargs args) {
        Intrinsics.checkNotNullParameter(fn, "fn");
        Intrinsics.checkNotNullParameter(args, "args");
        if (this.stopped) {
            return LuaValue.NONE;
        }
        try {
            ScriptThread co = this.luau.newThread(fn);
            LuauState.ResumeResult r = this.luau.resume(co, args);
            if (r.isError()) {
                reportError(r.error, r.traceback);
                return LuaValue.NONE;
            }
            return r.values;
        } catch (LuaError e) {
            String message = e.getMessage();
            reportError(message == null ? e.toString() : message);
            return LuaValue.NONE;
        } catch (RuntimeException e) {
            reportError(e.toString());
            return LuaValue.NONE;
        }
    }

    public final ScriptThread newThread(LuaValue fn) {
        Intrinsics.checkNotNullParameter(fn, "fn");
        return this.luau.newThread(fn);
    }

    public final ScriptThread spawn(LuaValue fn, Varargs args) {
        Intrinsics.checkNotNullParameter(fn, "fn");
        Intrinsics.checkNotNullParameter(args, "args");
        if (this.stopped) {
            return null;
        }
        ScriptThread co = newThread(fn);
        resume(co, args);
        return co;
    }

    public final void resume(ScriptThread co, Varargs args) {
        Intrinsics.checkNotNullParameter(co, "co");
        Intrinsics.checkNotNullParameter(args, "args");
        if (this.stopped || co.isDead()) {
            return;
        }
        try {
            LuauState.ResumeResult r = this.luau.resume(co, args);
            if (r.isError()) {
                reportError(r.error, r.traceback);
            }
        } catch (Throwable e) {
            reportError(e.toString());
        }
    }

    public final void reportError(String msg, String traceback) {
        if (this.stopped || msg == null) {
            return;
        }
        if (msg.startsWith("Script stopped")) {
            return;
        }
        OutputLog.INSTANCE.error(cleanError(msg));
        if (traceback != null && !traceback.isEmpty()) {
            StringBuilder sb = new StringBuilder("Stack Begin");
            for (String line : traceback.split("\n")) {
                if (line.isEmpty() || line.startsWith("[C]")) {
                    continue;
                }
                sb.append("\n").append(line);
            }
            sb.append("\nStack End");
            OutputLog.INSTANCE.info(cleanError(sb.toString()));
        }
    }

    public final void reportError(String msg) {
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (this.stopped) {
            return;
        }
        OutputLog.INSTANCE.error(cleanError(msg));
    }

    public final String cleanError(String m) {
        Intrinsics.checkNotNullParameter(m, "m");
        return new Regex("(^|\\s)=(?=[^=:\\n]+:\\d+)").replace(m, "$1");
    }

    public final Varargs yieldFor(double seconds) {
        ScriptThread co = currentCoroutine();
        if (co == null) {
            throw new LuaError("attempt to yield across a non-yieldable boundary (use task.spawn)");
        }
        double start = this.time;
        this.waiting.add(new Waiting(co, this.time + RangesKt.coerceAtLeast(seconds, 0.0d), start));
        throw new LuauYield();
    }

    public final void delay(double seconds, ScriptThread co) {
        Intrinsics.checkNotNullParameter(co, "co");
        this.waiting.add(new Waiting(co, this.time + RangesKt.coerceAtLeast(seconds, 0.0d), this.time));
    }

    public final void defer(ScriptThread co, Varargs args) {
        Intrinsics.checkNotNullParameter(co, "co");
        Intrinsics.checkNotNullParameter(args, "args");
        this.deferred.add(TuplesKt.to(co, args));
    }

    public final void after(double seconds, Function0<Unit> fn) {
        Intrinsics.checkNotNullParameter(fn, "fn");
        this.timers.add(TuplesKt.to(Double.valueOf(this.time + RangesKt.coerceAtLeast(seconds, 0.0d)), fn));
    }

    static final boolean cancel$lambda$13(ScriptThread $co, Waiting it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getCo() == $co;
    }

    public final void cancel(final ScriptThread co) {
        Intrinsics.checkNotNullParameter(co, "co");
        this.luau.cancel(co);
        CollectionsKt.removeAll((List) this.waiting, new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(LuaRuntime.cancel$lambda$13(co, (LuaRuntime.Waiting) obj));
            }
        });
        CollectionsKt.removeAll((List) this.deferred, new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(LuaRuntime.cancel$lambda$14(co, (Pair) obj));
            }
        });
    }

    static final boolean cancel$lambda$14(ScriptThread $co, Pair it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getFirst() == $co;
    }

    public final Varargs yieldForElapsed(double seconds) {
        ScriptThread co = currentCoroutine();
        if (co == null) {
            throw new LuaError("attempt to yield across a non-yieldable boundary (use task.spawn)");
        }
        this.waiting.add(new Waiting(co, this.time + RangesKt.coerceAtLeast(seconds, 0.0d), this.time));
        throw new LuauYield(new LuauState.Continuation() {
            @Override
            public Varargs resume(Varargs args) {
                return args.arg1();
            }
        });
    }

    public final Varargs waitForChild(Instance parent, String name, Double timeout) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(name, "name");
        Instance found = Instance.findFirstChild$default(parent, name, false, 2, null);
        if (found != null) {
            return wrap(found);
        }
        ScriptThread co = currentCoroutine();
        if (co == null) {
            throw new LuaError("WaitForChild must be called from a yieldable thread");
        }
        this.childWaits.add(new ChildWait(parent, name, co, timeout == null ? Double.MAX_VALUE : this.time + timeout.doubleValue(), this.time));
        throw new LuauYield();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void checkChildWaits() {
        Iterable iterable = this.childWaits;
        Collection arrayList = new ArrayList();
        Iterator it = iterable.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            ChildWait childWait = (ChildWait) next;
            if (Instance.findFirstChild$default(childWait.getParent(), childWait.getName(), false, 2, null) != null) {
                arrayList.add(next);
            }
        }
        List<ChildWait> done = (List) arrayList;
        if (done.isEmpty()) {
            return;
        }
        this.childWaits.removeAll(done);
        for (ChildWait w : done) {
            this.deferred.add(TuplesKt.to(w.getCo(), wrap(Instance.findFirstChild$default(w.getParent(), w.getName(), false, 2, null))));
        }
    }

    /* JADX WARN: Code duplicated, block: B:80:0x0230  */
    public final void step(double dt) {
        List<Waiting> sorted;
        if (this.stopped) {
            return;
        }
        this.time += dt;
        while (true) {
            Function0<Unit> function0Poll = this.mainQueue.poll();
            if (function0Poll == null) {
                break;
            } else {
                function0Poll.invoke();
            }
        }
        if (!this.waiting.isEmpty()) {
            Iterable iterable = this.waiting;
            Collection arrayList = new ArrayList();
            for (Object obj : iterable) {
                if (((Waiting) obj).getAt() <= this.time) {
                    arrayList.add(obj);
                }
            }
            List due = (List) arrayList;
            if (!due.isEmpty()) {
                if (due.size() <= 1) {
                    sorted = due;
                } else {
                    sorted = CollectionsKt.sortedWith(due, new Comparator() { // from class: com.goydevv.studrblx.lua.LuaRuntime$step$$inlined$sortedBy$1
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.util.Comparator
                        public final int compare(Object t, Object t2) {
                            return ComparisonsKt.compareValues(Double.valueOf(((LuaRuntime.Waiting) t).getAt()), Double.valueOf(((LuaRuntime.Waiting) t2).getAt()));
                        }
                    });
                }
                Set ran = Collections.newSetFromMap(new IdentityHashMap());
                long t0 = System.nanoTime();
                for (Waiting w : sorted) {
                    Intrinsics.checkNotNull(ran);
                    if (!ran.isEmpty() && System.nanoTime() - t0 > WAIT_BUDGET_NS) {
                        break;
                    }
                    ran.add(w);
                    ScriptThread co = w.getCo();
                    Varargs varargsVarargsOf = LuaValue.varargsOf(LuaValue.valueOf(this.time - w.getStart()), LuaValue.valueOf(this.time));
                    Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
                    resume(co, varargsVarargsOf);
                }
                ArrayList<Waiting> arrayList2 = this.waiting;
                Intrinsics.checkNotNull(ran);
                arrayList2.removeAll(ran);
            }
        }
        List sorted2 = this.timers;
        if (!sorted2.isEmpty()) {
            Iterable iterable2 = this.timers;
            Collection arrayList3 = new ArrayList();
            for (Object obj2 : iterable2) {
                if (((Number) ((Pair) obj2).getFirst()).doubleValue() <= this.time) {
                    arrayList3.add(obj2);
                }
            }
            List due2 = (List) arrayList3;
            if (!due2.isEmpty()) {
                this.timers.removeAll(CollectionsKt.toSet(due2));
                Iterator it = due2.iterator();
                while (it.hasNext()) {
                    Function0 f = (Function0) ((Pair) it.next()).component2();
                    try {
                        f.invoke();
                    } catch (LuaError e) {
                        String message = e.getMessage();
                        if (message == null) {
                            message = "error";
                        }
                        reportError(message);
                    }
                }
            }
        }
        if (!this.deferred.isEmpty()) {
            List<? extends Pair<?, ?>> d = CollectionsKt.toList(this.deferred);
            this.deferred.clear();
            for (Pair<?, ?> pair : d) {
                ScriptThread co2 = (ScriptThread) pair.component1();
                Varargs a = (Varargs) pair.component2();
                resume(co2, a);
            }
        }
        List d2 = this.childWaits;
        if (!d2.isEmpty()) {
            Iterator<ChildWait> it2 = this.childWaits.iterator();
            Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
            ArrayList timedOut = new ArrayList();
            while (it2.hasNext()) {
                ChildWait next = it2.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                ChildWait w2 = next;
                if (!w2.getWarned() && this.time - w2.getStart() > 5.0d) {
                    if (w2.getDeadline() == Double.MAX_VALUE) {
                        w2.setWarned(true);
                        OutputLog.INSTANCE.warn("Infinite yield possible on '" + w2.getParent().fullName() + ":WaitForChild(\"" + w2.getName() + "\")'");
                    }
                }
                if (this.time >= w2.getDeadline()) {
                    it2.remove();
                    timedOut.add(w2);
                }
            }
            Iterator it3 = timedOut.iterator();
            while (it3.hasNext()) {
                ScriptThread co3 = ((ChildWait) it3.next()).getCo();
                LuaValue NIL = LuaValue.NIL;
                Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
                resume(co3, NIL);
            }
        }
        stepTweens((float) dt);
        this.luau.collect();
    }

    private final void stepTweens(float dt) {
        if (this.tweens.isEmpty()) {
            return;
        }
        for (Tween tw : CollectionsKt.toList(this.tweens)) {
            if (tw.getState() == 2 || tw.getState() == 1) {
                tw.setElapsed(tw.getElapsed() + dt);
                if (tw.getState() == 1) {
                    if (tw.getElapsed() >= tw.getInfo().getDelay()) {
                        tw.setState(2);
                        tw.setElapsed(0.0f);
                        Iterable iterableKeySet = tw.getGoals().keySet();
                        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterableKeySet, 10)), 16));
                        for (Object obj : iterableKeySet) {
                            linkedHashMap.put(obj, this.api.getK(tw.getInst(), (String) obj));
                        }
                        tw.setStart(linkedHashMap);
                    }
                }
                float dur = RangesKt.coerceAtLeast(tw.getInfo().getTime(), 1.0E-4f);
                float a = RangesKt.coerceIn(tw.getElapsed() / dur, 0.0f, 1.0f);
                if (tw.getReversing()) {
                    a = 1.0f - a;
                }
                float e = LuaTypes.INSTANCE.ease(tw.getInfo().getStyle(), tw.getInfo().getDirection(), a);
                for (Map.Entry<String, Object> entry : tw.getGoals().entrySet()) {
                    String k = entry.getKey();
                    Object goal = entry.getValue();
                    try {
                        this.api.setK(tw.getInst(), k, LuaTypes.INSTANCE.lerpValue(tw.getStart().get(k), goal, e));
                    } catch (Exception e2) {
                    }
                }
                if (tw.getElapsed() >= dur) {
                    if (tw.getInfo().getReverses() && !tw.getReversing()) {
                        tw.setReversing(true);
                        tw.setElapsed(0.0f);
                    } else if (tw.getInfo().getRepeatCount() < 0 || tw.getRepeatsDone() < tw.getInfo().getRepeatCount()) {
                        tw.setRepeatsDone(tw.getRepeatsDone() + 1);
                        tw.setReversing(false);
                        tw.setElapsed(0.0f);
                    } else {
                        tw.setState(4);
                        this.tweens.remove(tw);
                        fire(tw, "Completed", LuaTypes.INSTANCE.wrap(new EnumItemV("PlaybackState", "Completed", 4)));
                    }
                }
            }
        }
    }

    static final boolean playTween$lambda$23(Tween $tw, Tween it) {
        boolean z;
        Intrinsics.checkNotNullParameter(it, "it");
        if (it == $tw || it.getInst() != $tw.getInst()) {
            return false;
        }
        Iterable iterableKeySet = it.getGoals().keySet();
        if ((iterableKeySet instanceof Collection) && ((Collection) iterableKeySet).isEmpty()) {
            z = false;
        } else {
            Iterator it2 = iterableKeySet.iterator();
            while (it2.hasNext()) {
                if ($tw.getGoals().containsKey((String) it2.next())) {
                    z = true;
                }
            }
            z = false;
        }
        return z;
    }

    public final void playTween(final Tween tw) {
        Intrinsics.checkNotNullParameter(tw, "tw");
        CollectionsKt.removeAll((List) this.tweens, new Function1() { // from class: com.goydevv.studrblx.lua.LuaRuntime$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(LuaRuntime.playTween$lambda$23(tw, (Tween) obj));
            }
        });
        if (tw.getState() == 3) {
            tw.setState(2);
            if (this.tweens.contains(tw)) {
                return;
            }
            this.tweens.add(tw);
            return;
        }
        tw.setElapsed(0.0f);
        tw.setRepeatsDone(0);
        tw.setReversing(false);
        if (tw.getInfo().getDelay() > 0.0f) {
            tw.setState(1);
        } else {
            tw.setState(2);
            Iterable iterableKeySet = tw.getGoals().keySet();
            LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterableKeySet, 10)), 16));
            for (Object obj : iterableKeySet) {
                linkedHashMap.put(obj, this.api.getK(tw.getInst(), (String) obj));
            }
            tw.setStart(linkedHashMap);
        }
        if (!this.tweens.contains(tw)) {
            this.tweens.add(tw);
        }
    }

    public final LuauFunction compile(String source, String chunkName, int env) {
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(chunkName, "chunkName");
        return this.luau.load(chunkName, source, env);
    }

    /* JADX INFO: compiled from: LuaRuntime.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

    }

    public final int envFor(Instance script, Map<String, ? extends LuaValue> extra) {
        Intrinsics.checkNotNullParameter(extra, "extra");
        int env = this.luau.newEnv();
        this.luau.envSet(env, "script", wrap(script));
        for (Map.Entry<String, ? extends LuaValue> entry : extra.entrySet()) {
            this.luau.envSet(env, entry.getKey(), entry.getValue());
        }
        return env;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void runScript$default(LuaRuntime luaRuntime, Instance instance, Map map, int i, Object obj) throws Exception {
        if ((i & 2) != 0) {
            map = MapsKt.emptyMap();
        }
        try { luaRuntime.runScript(instance, map); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    public final void runScript(Instance script, Map<String, ? extends LuaValue> extra) throws Exception {
        Intrinsics.checkNotNullParameter(script, "script");
        Intrinsics.checkNotNullParameter(extra, "extra");
        Object obj = script.get("Source");
        String src = obj instanceof String ? (String) obj : null;
        if (src == null || this.stopped) {
            return;
        }
        int env = 0;
        try {
            env = envFor(script, extra);
            LuaValue fn = compile(src, script.fullName(), env);
            spawn(fn, LuaValue.NONE);
        } catch (Throwable e) {
            String message = e.getMessage();
            if (message == null || message.length() == 0) {
                message = e.getClass().getSimpleName() + " in " + script.fullName();
            }
            OutputLog.INSTANCE.error(cleanError(message));
        } finally {
            this.luau.releaseRef(env);
        }
    }

    public final void runCommand(String code) throws Exception {
        Intrinsics.checkNotNullParameter(code, "code");
        if (this.stopped) {
            return;
        }
        try {
            if (this.commandEnv == 0) {
                this.commandEnv = envFor(null, MapsKt.emptyMap());
            }
            LuaValue fn = compile(code, "CommandBar", this.commandEnv);
            spawn(fn, LuaValue.NONE);
        } catch (LuaError e) {
            String message = e.getMessage();
            if (message == null) {
                message = "Syntax error";
            }
            OutputLog.INSTANCE.error(cleanError(message));
        }
    }

    public final LuaValue require(final Instance module) throws Exception {
        Intrinsics.checkNotNullParameter(module, "module");
        LuaValue cached = this.moduleCache.get(module);
        if (cached != null) {
            return cached;
        }
        if (!module.isA("ModuleScript")) {
            throw new LuaError("Attempted to call require with invalid argument(s).");
        }
        ArrayList<ScriptThread> pending = this.moduleLoading.get(module);
        if (pending != null) {
            ScriptThread co = currentCoroutine();
            if (co == null) {
                throw new LuaError("Requested module was required recursively or is still loading");
            }
            pending.add(co);
            throw new LuauYield(new LuauState.Continuation() {
                @Override
                public Varargs resume(Varargs args) {
                    LuaValue v = LuaRuntime.this.moduleCache.get(module);
                    if (v == null) {
                        throw new LuaError("Requested module experienced an error while loading");
                    }
                    return v;
                }
            });
        }
        Instance top = module;
        while (top.getParent() != null) {
            top = top.getParent();
        }
        Map<String, ? extends LuaValue> extra = Intrinsics.areEqual(top.getClassName(), "Plugin") ? MapsKt.plus(this.extraEnvForModules, TuplesKt.to("plugin", wrap(top))) : this.extraEnvForModules;
        Object obj = module.get("Source");
        String str = obj instanceof String ? (String) obj : "";
        int env = envFor(module, extra);
        LuauFunction fn;
        try {
            fn = compile(str, module.fullName(), env);
        } finally {
            this.luau.releaseRef(env);
        }
        ScriptThread co = this.luau.newThread(fn);
        co.setRawResults(true);
        final ArrayList<ScriptThread> waiters = new ArrayList<>();
        final String[] failure = new String[1];
        this.moduleLoading.put(module, waiters);
        co.setOnFinish(new LuauState.FinishListener() {
            @Override
            public void finished(ScriptThread t, LuauState.ResumeResult r) {
                LuaRuntime.this.moduleLoading.remove(module);
                if (r.isError()) {
                    failure[0] = r.error;
                    LuaRuntime.this.reportError(r.error, r.traceback);
                } else if (r.values.narg() != 1 || r.values.arg1().isnil()) {
                    failure[0] = "Module code did not return exactly one value";
                    LuaRuntime.this.reportError(module.fullName() + ": Module code did not return exactly one value", null);
                } else {
                    LuaRuntime.this.moduleCache.put(module, r.values.arg1());
                }
                for (ScriptThread w : waiters) {
                    LuaRuntime.this.deferred.add(TuplesKt.to(w, LuaValue.NONE));
                }
            }
        });
        LuauState.ResumeResult r = this.luau.resume(co, LuaValue.NONE);
        if (r.isYielded()) {
            ScriptThread caller = currentCoroutine();
            if (caller == null) {
                throw new LuaError("Requested module yielded in a non-yieldable context");
            }
            waiters.add(caller);
            throw new LuauYield(new LuauState.Continuation() {
                @Override
                public Varargs resume(Varargs args) {
                    LuaValue v = LuaRuntime.this.moduleCache.get(module);
                    if (v == null) {
                        throw new LuaError("Requested module experienced an error while loading");
                    }
                    return v;
                }
            });
        }
        LuaValue v = this.moduleCache.get(module);
        if (v == null) {
            throw new LuaError(failure[0] != null && failure[0].contains("exactly one value") ? "Module code did not return exactly one value" : "Requested module experienced an error while loading");
        }
        return v;
    }

    public final Map<String, ? extends LuaValue> getExtraEnvForModules() {
        return this.extraEnvForModules;
    }

    public final void setExtraEnvForModules(Map<String, ? extends LuaValue> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.extraEnvForModules = map;
    }


    public final void onAncestryChanged(Instance i) {
        Intrinsics.checkNotNullParameter(i, "i");
        if (hasConn(i, "AncestryChanged")) {
            fire(i, "AncestryChanged", wrap(i), wrap(i.getParent()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyAncestry(Instance i) {
        if (this.conns.isEmpty()) {
            return;
        }
        onAncestryChanged(i);
        for (Instance d : Instance.descendants$default(i, null, 1, null)) {
            if (this.conns.containsKey(d)) {
                onAncestryChanged(d);
            }
        }
    }
}
