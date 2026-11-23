package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.model.ClassDb;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import com.goydevv.studrblx.lua.luau.ScriptThread;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/* JADX INFO: compiled from: RobloxGlobals.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class RobloxGlobals {
    public static final RobloxGlobals INSTANCE = new RobloxGlobals();

    private RobloxGlobals() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:44:0x00dd  */
    /* JADX WARN: Code duplicated, block: B:45:0x00e2  */
    /* JADX WARN: Code duplicated, block: B:49:0x00f7  */
    /* JADX WARN: Code duplicated, block: B:54:0x0104  */
    /* JADX WARN: Code duplicated, block: B:57:0x010b  */
    /* JADX WARN: Code duplicated, block: B:74:0x00d5 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:75:0x00e5 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:76:0x00db A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:77:0x0100 A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:80:0x00c1 A[SYNTHETIC] */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public final Instance resolveRequirePath(Instance caller, String path) {
        Instance parent;
        String clean = "";
        String strFullName;
        Instance instanceFindFirstChild$default;
        Intrinsics.checkNotNullParameter(caller, "caller");
        Intrinsics.checkNotNullParameter(path, "path");
        Instance cur = caller;
        Iterable iterableSplit$default = StringsKt.split((CharSequence) path, new char[]{'/'}, false, 0);
        Collection arrayList = new ArrayList();
        for (Object obj : iterableSplit$default) {
            if (((String) obj).length() > 0) {
                arrayList.add(obj);
            }
        }
        List<String> parts = CollectionsKt.toMutableList(arrayList);
        if (parts.isEmpty()) {
            throw new LuaError("require path \"" + path + "\" is empty");
        }
        String str = (String) parts.get(0);
        switch (str.hashCode()) {
            case 46:
                if (str.equals(".")) {
                    cur = caller.getParent();
                    parts.remove(0);
                    for (String name : parts) {
                        if (!Intrinsics.areEqual(name, ".")) {
                            if (Intrinsics.areEqual(name, "..")) {
                                if (cur != null) {
                                    parent = cur.getParent();
                                } else {
                                    parent = null;
                                }
                                cur = parent;
                            } else {
                                clean = StringsKt.removeSuffix(StringsKt.removeSuffix(name, (CharSequence) ".luau"), (CharSequence) ".lua");
                                if (cur != null || (instanceFindFirstChild$default = Instance.findFirstChild$default(cur, clean, false, 2, null)) == null) {
                                    if (cur != null || (strFullName = cur.fullName()) == null) {
                                        strFullName = "nil";
                                    }
                                    throw new LuaError("require(\"" + path + "\"): " + clean + " is not a child of " + strFullName);
                                }
                                cur = instanceFindFirstChild$default;
                            }
                        }
                    }
                    if (cur != null) {
                        return cur;
                    }
                    throw new LuaError("require(\"" + path + "\") went above the game");
                }
                throw new LuaError("require path \"" + path + "\" must start with ./, ../ or @self (aliases aren't supported)");
            case 1472:
                if (str.equals("..")) {
                    Instance parent2 = caller.getParent();
                    Instance cur2 = parent2 != null ? parent2.getParent() : null;
                    parts.remove(0);
                    cur = cur2;
                    while (Intrinsics.areEqual(CollectionsKt.firstOrNull(parts), "..")) {
                        cur = cur != null ? cur.getParent() : null;
                        parts.remove(0);
                    }
                    Unit unit = Unit.INSTANCE;
                    while (true) {
                        String name = parts.isEmpty() ? "" : (String) parts.get(0);
                        if (!Intrinsics.areEqual(name, ".")) {
                            if (Intrinsics.areEqual(name, "..")) {
                                if (cur != null) {
                                    parent = cur.getParent();
                                } else {
                                    parent = null;
                                }
                                cur = parent;
                            } else {
                                clean = StringsKt.removeSuffix(StringsKt.removeSuffix(name, (CharSequence) ".luau"), (CharSequence) ".lua");
                                if (cur != null) {
                                }
                            }
                        }
                        break;
                    }
                    if (cur != null) {
                        strFullName = "nil";
                    } else {
                        strFullName = "nil";
                    }
                    throw new LuaError("require(\"" + path + "\"): " + clean + " is not a child of " + strFullName);
                }
                throw new LuaError("require path \"" + path + "\" must start with ./, ../ or @self (aliases aren't supported)");
            case 62631820:
                if (str.equals("@self")) {
                    parts.remove(0);
                    while (true) {
                        String name = parts.isEmpty() ? "" : (String) parts.get(0);
                        if (!Intrinsics.areEqual(name, ".")) {
                            if (Intrinsics.areEqual(name, "..")) {
                                if (cur != null) {
                                    parent = cur.getParent();
                                } else {
                                    parent = null;
                                }
                                cur = parent;
                            } else {
                                clean = StringsKt.removeSuffix(StringsKt.removeSuffix(name, (CharSequence) ".luau"), (CharSequence) ".lua");
                                if (cur != null) {
                                }
                            }
                        }
                        break;
                    }
                    if (cur != null) {
                        strFullName = "nil";
                    } else {
                        strFullName = "nil";
                    }
                    throw new LuaError("require(\"" + path + "\"): " + clean + " is not a child of " + strFullName);
                }
                throw new LuaError("require path \"" + path + "\" must start with ./, ../ or @self (aliases aren't supported)");
            default:
                throw new LuaError("require path \"" + path + "\" must start with ./, ../ or @self (aliases aren't supported)");
        }
    }

    public final void install(final LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(rt, "rt");
        final Globals g = rt.getGlobals();
        final long start = System.nanoTime();
        LuaValue luaValue = g.get("bit32");
        if (!luaValue.istable()) {
            luaValue = null;
        }
        if (luaValue != null) {
            luaValue.set("countlz", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return RobloxGlobals.install$lambda$5$lambda$2((Varargs) obj);
                }
            }));
            luaValue.set("countrz", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda13
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return RobloxGlobals.install$lambda$5$lambda$3((Varargs) obj);
                }
            }));
            luaValue.set("byteswap", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda24
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return RobloxGlobals.install$lambda$5$lambda$4((Varargs) obj);
                }
            }));
        }
        g.set("wait", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda49
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$8(rt, (Varargs) obj);
            }
        }));
        g.set("Wait", g.get("wait"));
        g.set("delay", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda50
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$9(rt, (Varargs) obj);
            }
        }));
        g.set("spawn", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda51
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$10(rt, (Varargs) obj);
            }
        }));
        g.set("tick", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda52
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$11((Varargs) obj);
            }
        }));
        g.set("time", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda53
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$12(rt, (Varargs) obj);
            }
        }));
        g.set("elapsedTime", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$13(start, (Varargs) obj);
            }
        }));
        g.set("game", rt.wrap(rt.getDm()));
        g.set("Game", rt.wrap(rt.getDm()));
        g.set("workspace", rt.wrap(rt.getDm().getWorkspace()));
        g.set("Workspace", rt.wrap(rt.getDm().getWorkspace()));
        g.set("require", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$16(rt, (LuaValue) obj);
            }
        }));
        final LuaTable settingsRoot = StudioSettings.INSTANCE.create(rt);
        g.set("settings", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$23(settingsRoot, (Varargs) obj);
            }
        }));
        final Instance instanceCreate = InstanceFactory.INSTANCE.create("UserSettings");
        Instance instanceCreate2 = InstanceFactory.INSTANCE.create("UserGameSettings");
        instanceCreate2.setName("GameSettings");
        instanceCreate2.set("ControlMode", new EnumValue(0), false);
        instanceCreate2.set("ComputerMovementMode", new EnumValue(0), false);
        instanceCreate2.set("TouchMovementMode", new EnumValue(0), false);
        instanceCreate2.set("MasterVolume", Float.valueOf(1.0f), false);
        instanceCreate2.set("SavedQualityLevel", new EnumValue(0), false);
        instanceCreate2.set("RotationType", new EnumValue(0), false);
        instanceCreate2.attachRaw(instanceCreate);
        g.set("UserSettings", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$26(rt, instanceCreate, (Varargs) obj);
            }
        }));
        g.set("version", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$27((Varargs) obj);
            }
        }));
        g.set("printidentity", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$28((Varargs) obj);
            }
        }));
        LuaTable task = new LuaTable();
        task.set("wait", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$31(rt, (Varargs) obj);
            }
        }));
        task.set("spawn", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$32(rt, (Varargs) obj);
            }
        }));
        task.set("defer", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$33(rt, (Varargs) obj);
            }
        }));
        task.set("delay", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$35(rt, (Varargs) obj);
            }
        }));
        task.set("cancel", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda21
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$37(rt, (Varargs) obj);
            }
        }));
        task.set("synchronize", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda22
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$38((Varargs) obj);
            }
        }));
        task.set("desynchronize", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda23
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$39((Varargs) obj);
            }
        }));
        g.set("task", task);
        LuaTable instLib = new LuaTable();
        instLib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda25
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$41(rt, (Varargs) obj);
            }
        }));
        instLib.set("fromExisting", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda26
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$44(rt, (LuaValue) obj);
            }
        }));
        g.set("Instance", instLib);
        LuaTable luaTable = new LuaTable();
        luaTable.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda27
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$48$lambda$47((Varargs) obj);
            }
        }));
        Unit unit = Unit.INSTANCE;
        g.set("RaycastParams", luaTable);
        LuaTable luaTable2 = new LuaTable();
        luaTable2.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda28
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$51$lambda$50((Varargs) obj);
            }
        }));
        Unit unit2 = Unit.INSTANCE;
        g.set("OverlapParams", luaTable2);
        LuaTable luaTable3 = new LuaTable();
        luaTable3.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda29
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$54$lambda$53((Varargs) obj);
            }
        }));
        Unit unit3 = Unit.INSTANCE;
        g.set("DockWidgetPluginGuiInfo", luaTable3);
        LuaValue math = g.get("math");
        math.set("clamp", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda30
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$55((Varargs) obj);
            }
        }));
        math.set("sign", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda31
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$56((LuaValue) obj);
            }
        }));
        math.set("round", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda32
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$57((LuaValue) obj);
            }
        }));
        math.set("noise", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda33
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$58((Varargs) obj);
            }
        }));
        math.set("lerp", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda34
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$59((Varargs) obj);
            }
        }));
        math.set("map", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda36
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$60((Varargs) obj);
            }
        }));
        LuaValue string = g.get("string");
        string.set("split", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda37
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$63((Varargs) obj);
            }
        }));
        LuaValue table = g.get("table");
        table.set("find", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda38
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$64((Varargs) obj);
            }
        }));
        table.set("clear", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda39
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$66((LuaValue) obj);
            }
        }));
        table.set("create", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda40
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$67((Varargs) obj);
            }
        }));
        table.set("freeze", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda41
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$68((LuaValue) obj);
            }
        }));
        table.set("isfrozen", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda42
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$69((LuaValue) obj);
            }
        }));
        table.set("clone", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda43
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$70((LuaValue) obj);
            }
        }));
        table.set("move", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda44
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$71((Varargs) obj);
            }
        }));
        table.set("getn", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda45
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$72((LuaValue) obj);
            }
        }));
        LuaTable utf8 = new LuaTable();
        utf8.set("char", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda47
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$73((Varargs) obj);
            }
        }));
        utf8.set("len", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda48
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$74((LuaValue) obj);
            }
        }));
        utf8.set("charpattern", "[\u0000-\u007fÂ-ý][\u0080-¿]*");
        utf8.set("codepoint", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                String s = a.arg(1).checkjstring();
                int len = s.length();
                int i = a.arg(2).optint(1);
                int j = a.arg(3).optint(i);
                if (i < 0) i = len + i + 1;
                if (j < 0) j = len + j + 1;
                if (i < 1) i = 1;
                if (j > len) j = len;
                if (i > j) return LuaValue.NONE;
                ArrayList<LuaValue> cps = new ArrayList<>();
                int idx = i - 1;
                while (idx < j) {
                    int cp = s.codePointAt(idx);
                    cps.add(LuaValue.valueOf(cp));
                    idx += Character.charCount(cp);
                }
                return LuaValue.varargsOf(cps.toArray(new LuaValue[0]));
            }
        }));
        utf8.set("offset", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                String s = a.arg(1).checkjstring();
                int n = a.arg(2).checkint();
                int pos = a.arg(3).optint(n >= 0 ? 1 : s.length() + 1);
                if (pos < 0) pos = s.length() + pos + 1;
                int target = pos + (n > 0 ? n - 1 : n);
                if (target < 1 || target > s.length() + 1) return LuaValue.NIL;
                return LuaValue.valueOf(target);
            }
        }));
        g.set("utf8", utf8);
        math.set("isfinite", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                double d = ((LuaValue) obj).checkdouble();
                return LuaValue.valueOf(!Double.isNaN(d) && !Double.isInfinite(d));
            }
        }));
        math.set("isnan", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return LuaValue.valueOf(Double.isNaN(((LuaValue) obj).checkdouble()));
            }
        }));
        math.set("isinf", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return LuaValue.valueOf(Double.isInfinite(((LuaValue) obj).checkdouble()));
            }
        }));
        installBuffer(g);
        installDateTime(g);
        installDebugAndOs(g, table, string);
    }

    private static final java.nio.ByteBuffer checkBuf(LuaValue v) {
        Object ud = v.isuserdata() ? v.touserdata() : null;
        if (ud instanceof java.nio.ByteBuffer) {
            return (java.nio.ByteBuffer) ud;
        }
        throw new org.luaj.vm2.LuaError("buffer expected, got " + LuaTypes.INSTANCE.typeOf(v));
    }

    private final void installBuffer(Globals g) {
        LuaTable buf = new LuaTable();
        buf.set("create", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                int sz = ((LuaValue) obj).checkint();
                if (sz < 0) throw new org.luaj.vm2.LuaError("buffer size cannot be negative");
                java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(sz).order(java.nio.ByteOrder.LITTLE_ENDIAN);
                return new org.luaj.vm2.LuaUserdata(bb);
            }
        }));
        buf.set("fromstring", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                org.luaj.vm2.LuaString ls = ((LuaValue) obj).checkstring();
                byte[] arr = new byte[ls.m_length];
                System.arraycopy(ls.m_bytes, ls.m_offset, arr, 0, ls.m_length);
                java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(arr).order(java.nio.ByteOrder.LITTLE_ENDIAN);
                return new org.luaj.vm2.LuaUserdata(bb);
            }
        }));
        buf.set("tostring", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                java.nio.ByteBuffer bb = checkBuf((LuaValue) obj);
                return org.luaj.vm2.LuaString.valueOf(bb.array(), 0, bb.capacity());
            }
        }));
        buf.set("len", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return LuaValue.valueOf(checkBuf((LuaValue) obj).capacity());
            }
        }));
        buf.set("readi8", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf((int) checkBuf(a.arg(1)).get(a.arg(2).checkint()));
            }
        }));
        buf.set("readu8", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf(checkBuf(a.arg(1)).get(a.arg(2).checkint()) & 0xFF);
            }
        }));
        buf.set("readi16", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf((int) checkBuf(a.arg(1)).getShort(a.arg(2).checkint()));
            }
        }));
        buf.set("readu16", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf(checkBuf(a.arg(1)).getShort(a.arg(2).checkint()) & 0xFFFF);
            }
        }));
        buf.set("readi32", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf(checkBuf(a.arg(1)).getInt(a.arg(2).checkint()));
            }
        }));
        buf.set("readu32", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf(((long) checkBuf(a.arg(1)).getInt(a.arg(2).checkint())) & 0xFFFFFFFFL);
            }
        }));
        buf.set("readf32", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf((double) checkBuf(a.arg(1)).getFloat(a.arg(2).checkint()));
            }
        }));
        buf.set("readf64", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf(checkBuf(a.arg(1)).getDouble(a.arg(2).checkint()));
            }
        }));
        buf.set("writei8", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                checkBuf(a.arg(1)).put(a.arg(2).checkint(), (byte) a.arg(3).checkint());
                return LuaValue.NONE;
            }
        }));
        buf.set("writeu8", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                checkBuf(a.arg(1)).put(a.arg(2).checkint(), (byte) (a.arg(3).checkint() & 0xFF));
                return LuaValue.NONE;
            }
        }));
        buf.set("writei16", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                checkBuf(a.arg(1)).putShort(a.arg(2).checkint(), (short) a.arg(3).checkint());
                return LuaValue.NONE;
            }
        }));
        buf.set("writeu16", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                checkBuf(a.arg(1)).putShort(a.arg(2).checkint(), (short) (a.arg(3).checkint() & 0xFFFF));
                return LuaValue.NONE;
            }
        }));
        buf.set("writei32", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                checkBuf(a.arg(1)).putInt(a.arg(2).checkint(), a.arg(3).checkint());
                return LuaValue.NONE;
            }
        }));
        buf.set("writeu32", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                checkBuf(a.arg(1)).putInt(a.arg(2).checkint(), (int) a.arg(3).checklong());
                return LuaValue.NONE;
            }
        }));
        buf.set("writef32", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                checkBuf(a.arg(1)).putFloat(a.arg(2).checkint(), (float) a.arg(3).checkdouble());
                return LuaValue.NONE;
            }
        }));
        buf.set("writef64", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                checkBuf(a.arg(1)).putDouble(a.arg(2).checkint(), a.arg(3).checkdouble());
                return LuaValue.NONE;
            }
        }));
        buf.set("readstring", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                java.nio.ByteBuffer bb = checkBuf(a.arg(1));
                int off = a.arg(2).checkint();
                int cnt = a.arg(3).checkint();
                return org.luaj.vm2.LuaString.valueOf(bb.array(), off, cnt);
            }
        }));
        buf.set("writestring", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                java.nio.ByteBuffer bb = checkBuf(a.arg(1));
                int off = a.arg(2).checkint();
                org.luaj.vm2.LuaString ls = a.arg(3).checkstring();
                int cnt = a.arg(4).optint(ls.m_length);
                System.arraycopy(ls.m_bytes, ls.m_offset, bb.array(), off, Math.min(cnt, ls.m_length));
                return LuaValue.NONE;
            }
        }));
        buf.set("copy", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                java.nio.ByteBuffer dst = checkBuf(a.arg(1));
                int dstOff = a.arg(2).checkint();
                java.nio.ByteBuffer src = checkBuf(a.arg(3));
                int srcOff = a.arg(4).optint(0);
                int cnt = a.arg(5).optint(src.capacity() - srcOff);
                System.arraycopy(src.array(), srcOff, dst.array(), dstOff, cnt);
                return LuaValue.NONE;
            }
        }));
        buf.set("fill", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                java.nio.ByteBuffer bb = checkBuf(a.arg(1));
                int off = a.arg(2).checkint();
                byte val = (byte) (a.arg(3).checkint() & 0xFF);
                int cnt = a.arg(4).optint(bb.capacity() - off);
                java.util.Arrays.fill(bb.array(), off, off + cnt, val);
                return LuaValue.NONE;
            }
        }));
        buf.set("readbits", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                byte[] arr = checkBuf(a.arg(1)).array();
                int bitOff = a.arg(2).checkint();
                int bitCnt = a.arg(3).checkint();
                long res = 0L;
                for (int i = 0; i < bitCnt; i++) {
                    int bIdx = (bitOff + i) >>> 3;
                    int bBit = (bitOff + i) & 7;
                    if (((arr[bIdx] >>> bBit) & 1) != 0) {
                        res |= (1L << i);
                    }
                }
                return LuaValue.valueOf(res);
            }
        }));
        buf.set("writebits", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                byte[] arr = checkBuf(a.arg(1)).array();
                int bitOff = a.arg(2).checkint();
                int bitCnt = a.arg(3).checkint();
                long val = a.arg(4).checklong();
                for (int i = 0; i < bitCnt; i++) {
                    int bIdx = (bitOff + i) >>> 3;
                    int bBit = (bitOff + i) & 7;
                    if (((val >>> i) & 1L) != 0L) {
                        arr[bIdx] = (byte) (arr[bIdx] | (1 << bBit));
                    } else {
                        arr[bIdx] = (byte) (arr[bIdx] & ~(1 << bBit));
                    }
                }
                return LuaValue.NONE;
            }
        }));
        g.set("buffer", buf);
    }

    private static final LuaTable makeDateTime(final long ms) {
        LuaTable t = new LuaTable();
        t.set("UnixTimestamp", LuaValue.valueOf(ms / 1000L));
        t.set("UnixTimestampMillis", LuaValue.valueOf(ms));
        t.set("ToIsoDate", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US);
                sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                return LuaValue.valueOf(sdf.format(new java.util.Date(ms)));
            }
        }));
        t.set("ToUniversalTime", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
                cal.setTimeInMillis(ms);
                LuaTable r = new LuaTable();
                r.set("Year", cal.get(java.util.Calendar.YEAR));
                r.set("Month", cal.get(java.util.Calendar.MONTH) + 1);
                r.set("Day", cal.get(java.util.Calendar.DAY_OF_MONTH));
                r.set("Hour", cal.get(java.util.Calendar.HOUR_OF_DAY));
                r.set("Minute", cal.get(java.util.Calendar.MINUTE));
                r.set("Second", cal.get(java.util.Calendar.SECOND));
                r.set("Millisecond", cal.get(java.util.Calendar.MILLISECOND));
                return r;
            }
        }));
        t.set("ToLocalTime", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTimeInMillis(ms);
                LuaTable r = new LuaTable();
                r.set("Year", cal.get(java.util.Calendar.YEAR));
                r.set("Month", cal.get(java.util.Calendar.MONTH) + 1);
                r.set("Day", cal.get(java.util.Calendar.DAY_OF_MONTH));
                r.set("Hour", cal.get(java.util.Calendar.HOUR_OF_DAY));
                r.set("Minute", cal.get(java.util.Calendar.MINUTE));
                r.set("Second", cal.get(java.util.Calendar.SECOND));
                r.set("Millisecond", cal.get(java.util.Calendar.MILLISECOND));
                return r;
            }
        }));
        LuaTable dtMt = new LuaTable();
        dtMt.set("__type", "DateTime");
        t.setmetatable(dtMt);
        return t;
    }


    private final void installDebugAndOs(Globals g, LuaValue table, LuaValue string) {
        table.set("pack", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                int n = a.narg();
                LuaTable t = new LuaTable();
                for (int i = 1; i <= n; i++) {
                    t.set(i, a.arg(i));
                }
                t.set("n", n);
                return t;
            }
        }));
        LuaTable dbg = new LuaTable();
        dbg.set("profilebegin", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) { return LuaValue.NONE; }
        }));
        dbg.set("profileend", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) { return LuaValue.NONE; }
        }));
        dbg.set("setmemorycategory", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) { return LuaValue.NONE; }
        }));
        dbg.set("resetmemorycategory", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) { return LuaValue.NONE; }
        }));
        dbg.set("traceback", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                String msg = a.arg(1).isstring() ? a.arg(1).tojstring() : "";
                return LuaValue.valueOf(msg.length() > 0 ? msg + "\nstack traceback:" : "stack traceback:");
            }
        }));
        dbg.set("info", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                LuaValue target = a.arg(1);
                String opts = a.arg(2).optjstring("sln");
                ArrayList<LuaValue> out = new ArrayList<>();
                for (int i = 0; i < opts.length(); i++) {
                    char ch = opts.charAt(i);
                    if (ch == 's') out.add(LuaValue.valueOf("[Script]"));
                    else if (ch == 'l') out.add(LuaValue.valueOf(1));
                    else if (ch == 'n') out.add(LuaValue.valueOf(""));
                    else if (ch == 'a') { out.add(LuaValue.valueOf(0)); out.add(LuaValue.TRUE); }
                    else if (ch == 'f') out.add(target.isfunction() ? target : LuaValue.NIL);
                }
                return LuaValue.varargsOf(out.toArray(new LuaValue[0]));
            }
        }));
        g.set("debug", dbg);

        LuaValue osVal = g.get("os");
        LuaTable osTbl = osVal.istable() ? osVal.checktable() : new LuaTable();
        osTbl.set("clock", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return LuaValue.valueOf(((double) System.nanoTime()) / 1.0E9d);
            }
        }));
        osTbl.set("difftime", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                return LuaValue.valueOf(a.arg(1).checkdouble() - a.arg(2).checkdouble());
            }
        }));
        osTbl.set("time", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                if (a.arg(1).istable()) {
                    LuaTable t = a.arg(1).checktable();
                    java.util.Calendar cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
                    cal.set(java.util.Calendar.YEAR, t.get("year").optint(1970));
                    cal.set(java.util.Calendar.MONTH, t.get("month").optint(1) - 1);
                    cal.set(java.util.Calendar.DAY_OF_MONTH, t.get("day").optint(1));
                    cal.set(java.util.Calendar.HOUR_OF_DAY, t.get("hour").optint(12));
                    cal.set(java.util.Calendar.MINUTE, t.get("min").optint(0));
                    cal.set(java.util.Calendar.SECOND, t.get("sec").optint(0));
                    cal.set(java.util.Calendar.MILLISECOND, 0);
                    return LuaValue.valueOf(cal.getTimeInMillis() / 1000L);
                }
                return LuaValue.valueOf(System.currentTimeMillis() / 1000L);
            }
        }));
        osTbl.set("date", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                String fmt = a.arg(1).optjstring("%c");
                long ms = a.arg(2).isnumber() ? ((long) (a.arg(2).checkdouble() * 1000.0d)) : System.currentTimeMillis();
                boolean utc = fmt.startsWith("!");
                String f = utc ? fmt.substring(1) : fmt;
                java.util.TimeZone tz = utc ? java.util.TimeZone.getTimeZone("UTC") : java.util.TimeZone.getDefault();
                java.util.Calendar cal = java.util.Calendar.getInstance(tz);
                cal.setTimeInMillis(ms);
                if ("*t".equals(f)) {
                    LuaTable r = new LuaTable();
                    r.set("year", cal.get(java.util.Calendar.YEAR));
                    r.set("month", cal.get(java.util.Calendar.MONTH) + 1);
                    r.set("day", cal.get(java.util.Calendar.DAY_OF_MONTH));
                    r.set("yday", cal.get(java.util.Calendar.DAY_OF_YEAR));
                    r.set("wday", cal.get(java.util.Calendar.DAY_OF_WEEK));
                    r.set("hour", cal.get(java.util.Calendar.HOUR_OF_DAY));
                    r.set("min", cal.get(java.util.Calendar.MINUTE));
                    r.set("sec", cal.get(java.util.Calendar.SECOND));
                    r.set("isdst", LuaValue.valueOf(tz.inDaylightTime(new java.util.Date(ms))));
                    return r;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < f.length(); i++) {
                    char c = f.charAt(i);
                    if (c == '%' && i + 1 < f.length()) {
                        char nc = f.charAt(++i);
                        switch (nc) {
                            case 'Y': sb.append(String.format(java.util.Locale.US, "%04d", cal.get(java.util.Calendar.YEAR))); break;
                            case 'y': sb.append(String.format(java.util.Locale.US, "%02d", cal.get(java.util.Calendar.YEAR) % 100)); break;
                            case 'm': sb.append(String.format(java.util.Locale.US, "%02d", cal.get(java.util.Calendar.MONTH) + 1)); break;
                            case 'd': sb.append(String.format(java.util.Locale.US, "%02d", cal.get(java.util.Calendar.DAY_OF_MONTH))); break;
                            case 'H': sb.append(String.format(java.util.Locale.US, "%02d", cal.get(java.util.Calendar.HOUR_OF_DAY))); break;
                            case 'M': sb.append(String.format(java.util.Locale.US, "%02d", cal.get(java.util.Calendar.MINUTE))); break;
                            case 'S': sb.append(String.format(java.util.Locale.US, "%02d", cal.get(java.util.Calendar.SECOND))); break;
                            case 'j': sb.append(String.format(java.util.Locale.US, "%03d", cal.get(java.util.Calendar.DAY_OF_YEAR))); break;
                            case '%': sb.append('%'); break;
                            default: sb.append(new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", java.util.Locale.US).format(new java.util.Date(ms))); break;
                        }
                    } else {
                        sb.append(c);
                    }
                }
                return LuaValue.valueOf(sb.toString());
            }
        }));
        g.set("os", osTbl);

        final LuaValue origFmt = string.get("format");
        final LuaValue tostringFn = g.get("tostring");
        if (origFmt.isfunction()) {
            string.set("format", LuaTypesKt.fnv(new Function1() {
                @Override
                public final Object invoke(Object obj) {
                    Varargs a = (Varargs) obj;
                    String fmt = a.arg(1).checkjstring();
                    if (!fmt.contains("%*")) {
                        return origFmt.invoke(a);
                    }
                    StringBuilder sb = new StringBuilder();
                    ArrayList<LuaValue> newArgs = new ArrayList<>();
                    newArgs.add(LuaValue.NIL);
                    int argIdx = 2;
                    for (int i = 0; i < fmt.length(); i++) {
                        char c = fmt.charAt(i);
                        if (c == '%' && i + 1 < fmt.length()) {
                            char nc = fmt.charAt(i + 1);
                            if (nc == '%') {
                                sb.append("%%");
                                i++;
                            } else if (nc == '*') {
                                sb.append("%s");
                                LuaValue v = a.arg(argIdx++);
                                newArgs.add(tostringFn.isfunction() ? tostringFn.call(v) : LuaValue.valueOf(v.tojstring()));
                                i++;
                            } else {
                                sb.append(c);
                                int j = i + 1;
                                while (j < fmt.length() && " -+#0123456789.".indexOf(fmt.charAt(j)) >= 0) {
                                    sb.append(fmt.charAt(j++));
                                }
                                if (j < fmt.length()) {
                                    sb.append(fmt.charAt(j));
                                    newArgs.add(a.arg(argIdx++));
                                    i = j;
                                }
                            }
                        } else {
                            sb.append(c);
                        }
                    }
                    newArgs.set(0, LuaValue.valueOf(sb.toString()));
                    return origFmt.invoke(LuaValue.varargsOf(newArgs.toArray(new LuaValue[0])));
                }
            }));
        }
        string.set("packsize", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                String fmt = ((LuaValue) obj).checkjstring();
                int sz = 0;
                for (int i = 0; i < fmt.length(); i++) {
                    char c = fmt.charAt(i);
                    switch (c) {
                        case '<': case '>': case '=': case ' ': break;
                        case 'b': case 'B': case 'x': sz += 1; break;
                        case 'h': case 'H': sz += 2; break;
                        case 'i': case 'I': case 'l': case 'L': case 'f': sz += 4; break;
                        case 'd': sz += 8; break;
                        case 'c': {
                            int num = 0;
                            while (i + 1 < fmt.length() && Character.isDigit(fmt.charAt(i + 1))) {
                                num = num * 10 + (fmt.charAt(++i) - '0');
                            }
                            sz += num;
                            break;
                        }
                        default: break;
                    }
                }
                return LuaValue.valueOf(sz);
            }
        }));
        string.set("pack", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                String fmt = a.arg(1).checkjstring();
                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                java.nio.ByteOrder ord = java.nio.ByteOrder.LITTLE_ENDIAN;
                int argIdx = 2;
                byte[] tmp8 = new byte[8];
                for (int i = 0; i < fmt.length(); i++) {
                    char c = fmt.charAt(i);
                    switch (c) {
                        case '<': ord = java.nio.ByteOrder.LITTLE_ENDIAN; break;
                        case '>': ord = java.nio.ByteOrder.BIG_ENDIAN; break;
                        case '=': case ' ': break;
                        case 'x': baos.write(0); break;
                        case 'b': case 'B': baos.write(a.arg(argIdx++).checkint() & 0xFF); break;
                        case 'h': case 'H': {
                            java.nio.ByteBuffer.wrap(tmp8).order(ord).putShort(0, (short) a.arg(argIdx++).checkint());
                            baos.write(tmp8, 0, 2);
                            break;
                        }
                        case 'i': case 'I': case 'l': case 'L': {
                            java.nio.ByteBuffer.wrap(tmp8).order(ord).putInt(0, (int) a.arg(argIdx++).checklong());
                            baos.write(tmp8, 0, 4);
                            break;
                        }
                        case 'f': {
                            java.nio.ByteBuffer.wrap(tmp8).order(ord).putFloat(0, (float) a.arg(argIdx++).checkdouble());
                            baos.write(tmp8, 0, 4);
                            break;
                        }
                        case 'd': {
                            java.nio.ByteBuffer.wrap(tmp8).order(ord).putDouble(0, a.arg(argIdx++).checkdouble());
                            baos.write(tmp8, 0, 8);
                            break;
                        }
                        case 'z': {
                            org.luaj.vm2.LuaString ls = a.arg(argIdx++).checkstring();
                            baos.write(ls.m_bytes, ls.m_offset, ls.m_length);
                            baos.write(0);
                            break;
                        }
                        case 'c': {
                            int num = 0;
                            while (i + 1 < fmt.length() && Character.isDigit(fmt.charAt(i + 1))) {
                                num = num * 10 + (fmt.charAt(++i) - '0');
                            }
                            org.luaj.vm2.LuaString ls = a.arg(argIdx++).checkstring();
                            int cp = Math.min(num, ls.m_length);
                            baos.write(ls.m_bytes, ls.m_offset, cp);
                            for (int p = cp; p < num; p++) baos.write(0);
                            break;
                        }
                        case 's': {
                            org.luaj.vm2.LuaString ls = a.arg(argIdx++).checkstring();
                            java.nio.ByteBuffer.wrap(tmp8).order(ord).putInt(0, ls.m_length);
                            baos.write(tmp8, 0, 4);
                            baos.write(ls.m_bytes, ls.m_offset, ls.m_length);
                            break;
                        }
                        default: break;
                    }
                }
                byte[] res = baos.toByteArray();
                return org.luaj.vm2.LuaString.valueOf(res, 0, res.length);
            }
        }));
        string.set("unpack", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Varargs a = (Varargs) obj;
                String fmt = a.arg(1).checkjstring();
                org.luaj.vm2.LuaString data = a.arg(2).checkstring();
                int pos = a.arg(3).optint(1) - 1;
                if (pos < 0) pos = 0;
                java.nio.ByteOrder ord = java.nio.ByteOrder.LITTLE_ENDIAN;
                java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(data.m_bytes, data.m_offset, data.m_length);
                ArrayList<LuaValue> out = new ArrayList<>();
                for (int i = 0; i < fmt.length(); i++) {
                    char c = fmt.charAt(i);
                    bb.order(ord);
                    switch (c) {
                        case '<': ord = java.nio.ByteOrder.LITTLE_ENDIAN; break;
                        case '>': ord = java.nio.ByteOrder.BIG_ENDIAN; break;
                        case '=': case ' ': break;
                        case 'x': pos += 1; break;
                        case 'b': out.add(LuaValue.valueOf((int) bb.get(data.m_offset + pos))); pos += 1; break;
                        case 'B': out.add(LuaValue.valueOf(bb.get(data.m_offset + pos) & 0xFF)); pos += 1; break;
                        case 'h': out.add(LuaValue.valueOf((int) bb.getShort(data.m_offset + pos))); pos += 2; break;
                        case 'H': out.add(LuaValue.valueOf(bb.getShort(data.m_offset + pos) & 0xFFFF)); pos += 2; break;
                        case 'i': case 'l': out.add(LuaValue.valueOf(bb.getInt(data.m_offset + pos))); pos += 4; break;
                        case 'I': case 'L': out.add(LuaValue.valueOf(((long) bb.getInt(data.m_offset + pos)) & 0xFFFFFFFFL)); pos += 4; break;
                        case 'f': out.add(LuaValue.valueOf((double) bb.getFloat(data.m_offset + pos))); pos += 4; break;
                        case 'd': out.add(LuaValue.valueOf(bb.getDouble(data.m_offset + pos))); pos += 8; break;
                        case 'z': {
                            int start = pos;
                            while (pos < data.m_length && data.m_bytes[data.m_offset + pos] != 0) pos++;
                            out.add(org.luaj.vm2.LuaString.valueOf(data.m_bytes, data.m_offset + start, pos - start));
                            if (pos < data.m_length) pos++;
                            break;
                        }
                        case 'c': {
                            int num = 0;
                            while (i + 1 < fmt.length() && Character.isDigit(fmt.charAt(i + 1))) {
                                num = num * 10 + (fmt.charAt(++i) - '0');
                            }
                            out.add(org.luaj.vm2.LuaString.valueOf(data.m_bytes, data.m_offset + pos, Math.min(num, data.m_length - pos)));
                            pos += num;
                            break;
                        }
                        case 's': {
                            int slen = bb.getInt(data.m_offset + pos);
                            pos += 4;
                            out.add(org.luaj.vm2.LuaString.valueOf(data.m_bytes, data.m_offset + pos, Math.min(slen, data.m_length - pos)));
                            pos += slen;
                            break;
                        }
                        default: break;
                    }
                }
                out.add(LuaValue.valueOf(pos + 1));
                return LuaValue.varargsOf(out.toArray(new LuaValue[0]));
            }
        }));
    }

    private final void installDateTime(Globals g) {
        LuaTable dt = new LuaTable();
        dt.set("now", LuaTypesKt.fnv(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return makeDateTime(System.currentTimeMillis());
            }
        }));
        dt.set("fromUnixTimestamp", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return makeDateTime((long) (((LuaValue) obj).checkdouble() * 1000.0d));
            }
        }));
        dt.set("fromUnixTimestampMillis", LuaTypesKt.fn1(new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return makeDateTime(((LuaValue) obj).checklong());
            }
        }));
        g.set("DateTime", dt);
    }

    static final Varargs install$lambda$5$lambda$2(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaInteger luaIntegerValueOf = LuaValue.valueOf(Integer.numberOfLeadingZeros((int) a.arg(1).checklong()));
        Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
        return luaIntegerValueOf;
    }

    static final Varargs install$lambda$5$lambda$3(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaInteger luaIntegerValueOf = LuaValue.valueOf(Integer.numberOfTrailingZeros((int) a.arg(1).checklong()));
        Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
        return luaIntegerValueOf;
    }

    static final Varargs install$lambda$5$lambda$4(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaNumber luaNumberValueOf = LuaValue.valueOf(((long) Integer.reverseBytes((int) a.arg(1).checklong())) & 4294967295L);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final Varargs install$lambda$8(LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return $rt.yieldFor(RangesKt.coerceAtLeast(a.arg(1).optdouble(0.03d), 0.029d));
    }

    static final Varargs install$lambda$9(LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaFunction luaFunctionCheckfunction = a.arg(2).checkfunction();
        Intrinsics.checkNotNullExpressionValue(luaFunctionCheckfunction, "checkfunction(...)");
        ScriptThread co = $rt.newThread(luaFunctionCheckfunction);
        $rt.delay(a.arg(1).optdouble(0.0d), co);
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs install$lambda$10(LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaFunction luaFunctionCheckfunction = a.arg(1).checkfunction();
        Intrinsics.checkNotNullExpressionValue(luaFunctionCheckfunction, "checkfunction(...)");
        ScriptThread co = $rt.newThread(luaFunctionCheckfunction);
        $rt.delay(0.0d, co);
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs install$lambda$11(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaNumber luaNumberValueOf = LuaValue.valueOf(System.currentTimeMillis() / 1000.0d);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final Varargs install$lambda$12(LuaRuntime $rt, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaNumber luaNumberValueOf = LuaValue.valueOf($rt.getTime());
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final Varargs install$lambda$13(long $start, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaNumber luaNumberValueOf = LuaValue.valueOf((System.nanoTime() - $start) / 1.0E9d);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x006f, code lost:
    
        if (((r1 == null || (r1 = r1.get(org.luaj.vm2.LuaValue.CALL)) == null || !r1.isfunction()) ? false : true) != false) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final LuaValue install$lambda$16(LuaRuntime $rt, LuaValue m) {
        Intrinsics.checkNotNullParameter(m, "m");
        Object obj = m.isuserdata() ? m.touserdata() : null;
        Instance inst = obj instanceof Instance ? (Instance) obj : null;
        if (inst == null) {
            if (m.isnumber()) {
                throw new LuaError("require(assetId) is not supported offline");
            }
            if (m.isstring()) {
                LuaValue scriptValue = $rt.getLuau().callerEnvGet("script");
                Object obj2 = scriptValue.isuserdata() ? scriptValue.touserdata() : null;
                Instance caller = obj2 instanceof Instance ? (Instance) obj2 : null;
                if (caller == null) {
                    throw new LuaError("require(\"" + m.tojstring() + "\") needs to be called from a script");
                }
                inst = INSTANCE.resolveRequirePath(caller, m.tojstring());
                if (inst == null) {
                    throw new LuaError("Could not resolve require path \"" + m.tojstring() + "\"");
                }
            } else {
                throw new LuaError("Attempted to call require with invalid argument(s).");
            }
        }
        try {
            return $rt.require(inst);
        } catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Varargs install$lambda$23(LuaTable $settingsRoot, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return $settingsRoot;
    }

    static final Varargs install$lambda$26(LuaRuntime $rt, Instance $userSettings, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return $rt.wrap($userSettings);
    }

    static final Varargs install$lambda$27(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaString luaStringValueOf = LuaValue.valueOf("0.600.0");
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    static final Varargs install$lambda$28(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        OutputLog.INSTANCE.print("Current identity is 2");
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs install$lambda$31(LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return $rt.yieldForElapsed(a.arg(1).optdouble(0.0d));
    }

    static final Varargs install$lambda$32(LuaRuntime $rt, Varargs a) {
        ScriptThread co;
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue f = a.arg(1);
        if (f.isthread()) {
            Intrinsics.checkNotNull(f, "null cannot be cast to non-null type ScriptThread");
            co = (ScriptThread) f;
        } else {
            LuaFunction luaFunctionCheckfunction = f.checkfunction();
            Intrinsics.checkNotNullExpressionValue(luaFunctionCheckfunction, "checkfunction(...)");
            co = $rt.newThread(luaFunctionCheckfunction);
        }
        Varargs varargsSubargs = a.subargs(2);
        Intrinsics.checkNotNullExpressionValue(varargsSubargs, "subargs(...)");
        $rt.resume(co, varargsSubargs);
        return co;
    }

    static final Varargs install$lambda$33(LuaRuntime $rt, Varargs a) {
        ScriptThread co;
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue f = a.arg(1);
        if (f.isthread()) {
            Intrinsics.checkNotNull(f, "null cannot be cast to non-null type ScriptThread");
            co = (ScriptThread) f;
        } else {
            LuaFunction luaFunctionCheckfunction = f.checkfunction();
            Intrinsics.checkNotNullExpressionValue(luaFunctionCheckfunction, "checkfunction(...)");
            co = $rt.newThread(luaFunctionCheckfunction);
        }
        Varargs varargsSubargs = a.subargs(2);
        Intrinsics.checkNotNullExpressionValue(varargsSubargs, "subargs(...)");
        $rt.defer(co, varargsSubargs);
        return co;
    }

    static final Varargs install$lambda$35(final LuaRuntime $rt, Varargs a) {
        final ScriptThread co;
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue f = a.arg(2);
        if (f.isthread()) {
            Intrinsics.checkNotNull(f, "null cannot be cast to non-null type ScriptThread");
            co = (ScriptThread) f;
        } else {
            LuaFunction luaFunctionCheckfunction = f.checkfunction();
            Intrinsics.checkNotNullExpressionValue(luaFunctionCheckfunction, "checkfunction(...)");
            co = $rt.newThread(luaFunctionCheckfunction);
        }
        final Varargs args = a.subargs(3);
        $rt.after(a.arg(1).optdouble(0.0d), new Function0() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return RobloxGlobals.install$lambda$35$lambda$34($rt, co, args);
            }
        });
        return co;
    }

    static final Unit install$lambda$35$lambda$34(LuaRuntime $rt, ScriptThread $co, Varargs $args) {
        Intrinsics.checkNotNull($args);
        $rt.resume($co, $args);
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$37(LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        ScriptThread luaThread = luaValueArg instanceof ScriptThread ? (ScriptThread) luaValueArg : null;
        if (luaThread != null) {
            $rt.cancel(luaThread);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs install$lambda$38(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs install$lambda$39(Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs install$lambda$41(LuaRuntime $rt, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        String cls = a.arg(1).checkjstring();
        ClassDb classDb = ClassDb.INSTANCE;
        Intrinsics.checkNotNull(cls);
        if (classDb.isServiceName(cls) || SetsKt.setOf(new String[]{"DataModel", "Player", "PlayerGui"}).contains(cls)) {
            throw new LuaError("Unable to create an Instance of type \"" + cls + "\"");
        }
        Instance i = InstanceFactory.INSTANCE.create(cls);
        LuaValue luaValueArg = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Instance)) {
            obj = null;
        }
        Instance instance = (Instance) obj;
        if (instance != null) {
            Instance.setParent$default(i, instance, 0, 2, null);
        }
        return $rt.wrap(i);
    }

    static final LuaValue install$lambda$44(LuaRuntime $rt, LuaValue v) {
        Intrinsics.checkNotNullParameter(v, "v");
        Object obj = v.isuserdata() ? v.touserdata() : null;
        Instance instance = (Instance) (obj instanceof Instance ? obj : null);
        if (instance == null) {
            throw new LuaError("Instance expected, got " + LuaTypes.INSTANCE.typeOf(v));
        }
        Instance instanceCloneTree = instance.cloneTree();
        Iterator it = CollectionsKt.toList(instanceCloneTree.getChildren()).iterator();
        while (it.hasNext()) {
            ((Instance) it.next()).destroy();
        }
        return $rt.wrap(instanceCloneTree);
    }

    static final Varargs install$lambda$48$lambda$47(Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        LuaTable luaTable = new LuaTable();
        luaTable.set("FilterDescendantsInstances", new LuaTable());
        luaTable.set("FilterType", LuaTypes.INSTANCE.wrap(new EnumItemV("RaycastFilterType", "Exclude", 0)));
        luaTable.set("IgnoreWater", LuaValue.FALSE);
        luaTable.set("CollisionGroup", "Default");
        luaTable.set("RespectCanCollide", LuaValue.FALSE);
        luaTable.set("AddToFilter", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.RobloxGlobals$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RobloxGlobals.install$lambda$48$lambda$47$lambda$46$lambda$45((Varargs) obj);
            }
        }));
        return luaTable;
    }

    static final Varargs install$lambda$48$lambda$47$lambda$46$lambda$45(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue list = a.arg(1).get("FilterDescendantsInstances");
        LuaValue v = a.arg(2);
        if (v.istable()) {
            int i = 1;
            int length = v.length();
            if (1 <= length) {
                while (true) {
                    list.set(list.length() + 1, v.get(i));
                    if (i == length) {
                        break;
                    }
                    i++;
                }
            }
        } else {
            int i2 = list.length();
            list.set(i2 + 1, v);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs install$lambda$51$lambda$50(Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        LuaTable luaTable = new LuaTable();
        luaTable.set("FilterDescendantsInstances", new LuaTable());
        luaTable.set("FilterType", LuaTypes.INSTANCE.wrap(new EnumItemV("RaycastFilterType", "Exclude", 0)));
        luaTable.set("MaxParts", 0);
        luaTable.set("CollisionGroup", "Default");
        luaTable.set("RespectCanCollide", LuaValue.FALSE);
        return luaTable;
    }

    static final Varargs install$lambda$54$lambda$53(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTable luaTable = new LuaTable();
        luaTable.set("InitialDockState", a.arg(1));
        luaTable.set("InitialEnabled", LuaValue.valueOf(a.arg(2).optboolean(false)));
        luaTable.set("FloatingXSize", LuaValue.valueOf(a.arg(4).optint(300)));
        luaTable.set("FloatingYSize", LuaValue.valueOf(a.arg(5).optint(300)));
        luaTable.set("MinWidth", LuaValue.valueOf(a.arg(6).optint(150)));
        luaTable.set("MinHeight", LuaValue.valueOf(a.arg(7).optint(150)));
        return luaTable;
    }

    static final Varargs install$lambda$55(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        double x = a.arg(1).checkdouble();
        double mn = a.arg(2).checkdouble();
        double mx = a.arg(3).checkdouble();
        if (mn > mx) {
            throw new LuaError("max must be greater than or equal to min");
        }
        LuaNumber luaNumberValueOf = LuaValue.valueOf(RangesKt.coerceIn(x, mn, mx));
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final LuaValue install$lambda$56(LuaValue a) {
        int i;
        Intrinsics.checkNotNullParameter(a, "a");
        double x = a.checkdouble();
        if (x > 0.0d) {
            i = 1;
        } else {
            i = x < 0.0d ? -1 : 0;
        }
        LuaInteger luaIntegerValueOf = LuaValue.valueOf(i);
        Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
        return luaIntegerValueOf;
    }

    static final LuaValue install$lambda$57(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        double x = a.checkdouble();
        LuaNumber luaNumberValueOf = LuaValue.valueOf(x >= 0.0d ? Math.floor(0.5d + x) : -Math.floor((-x) + 0.5d));
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final Varargs install$lambda$58(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaNumber luaNumberValueOf = LuaValue.valueOf(Noise.INSTANCE.perlin(a.arg(1).optdouble(0.0d), a.arg(2).optdouble(0.0d), a.arg(3).optdouble(0.0d)));
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final Varargs install$lambda$59(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        double x = a.arg(1).checkdouble();
        LuaNumber luaNumberValueOf = LuaValue.valueOf(((a.arg(2).checkdouble() - x) * a.arg(3).checkdouble()) + x);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final Varargs install$lambda$60(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        double x = a.arg(1).checkdouble();
        double i0 = a.arg(2).checkdouble();
        double i1 = a.arg(3).checkdouble();
        double o0 = a.arg(4).checkdouble();
        double o1 = a.arg(5).checkdouble();
        LuaNumber luaNumberValueOf = LuaValue.valueOf((((x - i0) * (o1 - o0)) / (i1 - i0)) + o0);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final Varargs install$lambda$63(Varargs a) {
        Collection parts;
        Intrinsics.checkNotNullParameter(a, "a");
        CharSequence s = a.arg(1).checkjstring();
        String sep = a.arg(2).optjstring(",");
        LuaTable t = new LuaTable();
        Intrinsics.checkNotNull(sep);
        boolean z = sep.length() == 0;
        Intrinsics.checkNotNull(s);
        if (!z) {
            parts = StringsKt.split(s, new String[]{sep}, false, 0);
        } else {
            CharSequence charSequence = s;
            Collection arrayList = new ArrayList(charSequence.length());
            for (int i = 0; i < charSequence.length(); i++) {
                arrayList.add(String.valueOf(charSequence.charAt(i)));
            }
            parts = (List) arrayList;
        }
        Iterator it = parts.iterator();
        while (it.hasNext()) {
            t.insert(0, LuaValue.valueOf((String) it.next()));
        }
        return t;
    }

    static final Varargs install$lambda$64(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTable t = a.arg(1).checktable();
        LuaValue v = a.arg(2);
        int init = a.arg(3).optint(1);
        LuaValue NIL = LuaValue.NIL;
        Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
        int i = init;
        int length = t.length();
        if (i <= length) {
            while (!t.get(i).eq_b(v)) {
                if (i != length) {
                    i++;
                }
            }
            LuaValue r = LuaValue.valueOf(i);
            Intrinsics.checkNotNullExpressionValue(r, "valueOf(...)");
            NIL = r;
        }
        return NIL;
    }

    static final LuaValue install$lambda$66(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTable t = a.checktable();
        ArrayList keys = new ArrayList();
        LuaValue k = LuaValue.NIL;
        while (true) {
            Varargs n = t.next(k);
            k = n.arg1();
            if (k.isnil()) {
                break;
            }
            keys.add(k);
        }
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            t.set((LuaValue) it.next(), LuaValue.NIL);
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs install$lambda$67(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTable t = new LuaTable();
        int n = a.arg(1).checkint();
        LuaValue v = a.arg(2);
        if (!v.isnil()) {
            int i = 1;
            if (1 <= n) {
                while (true) {
                    t.set(i, v);
                    if (i == n) {
                        break;
                    }
                    i++;
                }
            }
        }
        return t;
    }

    static final LuaValue install$lambda$68(LuaValue it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it;
    }

    static final LuaValue install$lambda$69(LuaValue it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaBoolean FALSE = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
        return FALSE;
    }

    static final LuaValue install$lambda$70(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTable src = a.checktable();
        LuaTable t = new LuaTable();
        LuaValue k = LuaValue.NIL;
        while (true) {
            Varargs n = src.next(k);
            k = n.arg1();
            if (k.isnil()) {
                return t;
            }
            t.set(k, n.arg(2));
        }
    }

    static final Varargs install$lambda$71(Varargs a) {
        LuaTable dst;
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTable src = a.arg(1).checktable();
        int f = a.arg(2).checkint();
        int e = a.arg(3).checkint();
        int tpos = a.arg(4).checkint();
        if (a.arg(5).istable()) {
            LuaValue luaValueArg = a.arg(5);
            Intrinsics.checkNotNull(luaValueArg, "null cannot be cast to non-null type org.luaj.vm2.LuaTable");
            dst = (LuaTable) luaValueArg;
        } else {
            dst = src;
        }
        if (tpos <= f) {
            int i = f;
            if (i <= e) {
                while (true) {
                    dst.set((tpos + i) - f, src.get(i));
                    if (i == e) {
                        break;
                    }
                    i++;
                }
            }
        } else {
            int i2 = e;
            if (f <= i2) {
                while (true) {
                    dst.set((tpos + i2) - f, src.get(i2));
                    if (i2 == f) {
                        break;
                    }
                    i2--;
                }
            }
        }
        Intrinsics.checkNotNull(dst);
        return dst;
    }

    static final LuaValue install$lambda$72(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaInteger luaIntegerValueOf = LuaValue.valueOf(a.length());
        Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
        return luaIntegerValueOf;
    }

    static final Varargs install$lambda$73(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        StringBuilder sb = new StringBuilder();
        int i = 1;
        int iNarg = a.narg();
        if (1 <= iNarg) {
            while (true) {
                sb.appendCodePoint(a.arg(i).checkint());
                if (i == iNarg) {
                    break;
                }
                i++;
            }
        }
        LuaString luaStringValueOf = LuaValue.valueOf(sb.toString());
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    static final LuaValue install$lambda$74(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        byte[] m_bytes = a.checkstring().m_bytes;
        Intrinsics.checkNotNullExpressionValue(m_bytes, "m_bytes");
        String s = new String(m_bytes, a.checkstring().m_offset, a.checkstring().m_length, Charsets.UTF_8);
        LuaInteger luaIntegerValueOf = LuaValue.valueOf(s.codePointCount(0, s.length()));
        Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
        return luaIntegerValueOf;
    }
}
