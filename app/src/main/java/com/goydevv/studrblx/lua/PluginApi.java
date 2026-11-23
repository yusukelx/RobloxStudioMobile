package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.Ray;
import com.goydevv.studrblx.model.Vector3;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaTable;
import com.goydevv.studrblx.lua.luau.LuauYield;
import com.goydevv.studrblx.lua.luau.ScriptThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/* JADX INFO: compiled from: RobloxGlobals.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class PluginApi {
    public static final PluginApi INSTANCE = new PluginApi();

    private PluginApi() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:150:0x0501  */
    /* JADX WARN: Code duplicated, block: B:152:0x0523  */
    /* JADX WARN: Code duplicated, block: B:153:0x052e  */
    /* JADX WARN: Code duplicated, block: B:156:0x0538  */
    /* JADX WARN: Code duplicated, block: B:159:0x053f  */
    /* JADX WARN: Code duplicated, block: B:163:0x0572  */
    /* JADX WARN: Code duplicated, block: B:164:0x0578  */
    /* JADX WARN: Code duplicated, block: B:166:0x057e  */
    /* JADX WARN: Code duplicated, block: B:167:0x0585  */
    /* JADX WARN: Code duplicated, block: B:170:0x05ae  */
    /* JADX WARN: Code duplicated, block: B:183:0x0608  */
    /* JADX WARN: Code duplicated, block: B:203:0x0543 A[SYNTHETIC] */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0334, code lost:
    
        if (r5.get("InitialEnabled").toboolean() == false) goto L103;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0336, code lost:
    
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0339, code lost:
    
        r6 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x033a, code lost:
    
        r2.set("Enabled", java.lang.Boolean.valueOf(r6), false);
        r2.set("Title", r2.getName(), false);
        r2.set("ZIndexBehavior", new com.goydevv.studrblx.model.EnumValue(1), false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x035a, code lost:
    
        if (r5.istable() == false) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x035c, code lost:
    
        r2.getAttributes().put("__w", java.lang.Integer.valueOf(r5.get("FloatingXSize").optint(300)));
        r2.getAttributes().put("__h", java.lang.Integer.valueOf(r5.get("FloatingYSize").optint(300)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0390, code lost:
    
        r10.addPluginWidget(r2, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x03d5, code lost:
    
        if (r31.equals("PromptImportFile") != false) goto L116;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x040d, code lost:
    
        if (r31.equals("SaveSelectedToRoblox") != false) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x040f, code lost:
    
        r2 = org.luaj.vm2.LuaValue.NONE;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, "NONE");
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x04da, code lost:
    
        if (r31.equals(r4) != false) goto L147;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00e4, code lost:
    
        if (r31.equals("CreateDockWidgetPluginGui") == false) goto L199;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:?, code lost:
    
        return r29.wrap(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:?, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00fd, code lost:
    
        if (r31.equals("SetReady") == false) goto L199;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0109, code lost:
    
        if (r31.equals("OpenWikiPage") != false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0116, code lost:
    
        if (r31.equals("CreateQWidgetPluginGui") != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x027e, code lost:
    
        if (r31.equals("Union") != false) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x02e1, code lost:
    
        if (r31.equals("StartDrag") == false) goto L199;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0309, code lost:
    
        if (r31.equals("CreateDockWidgetPluginGuiAsync") != false) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x030b, code lost:
    
        r2 = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("DockWidgetPluginGui");
        r5 = r11.optjstring("Widget");
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, "optjstring(...)");
        r2.setName(r5);
        r5 = r32.arg(3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0328, code lost:
    
        if (r5.istable() == false) goto L103;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final org.luaj.vm2.Varargs call(com.goydevv.studrblx.lua.LuaRuntime r29, com.goydevv.studrblx.model.Instance r30, java.lang.String r31, org.luaj.vm2.Varargs r32) {
        Intrinsics.checkNotNullParameter(r29, "rt");
        Intrinsics.checkNotNullParameter(r30, "inst");
        Intrinsics.checkNotNullParameter(r31, "name");
        Intrinsics.checkNotNullParameter(r32, "a");
        Studio studio = r29.getStudio();
        LuaValue arg2 = r32.arg(2);
        switch (r31) {
            case "CreateToolbar": {
                Instance tb = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("PluginToolbar");
                String tName = arg2.optjstring("Toolbar");
                Intrinsics.checkNotNullExpressionValue(tName, "optjstring(...)");
                tb.setName(tName);
                Instance.setParent$default(tb, r30, 0, 2, null);
                return r29.wrap(tb);
            }
            case "CreateButton": {
                Instance btn = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("PluginToolbarButton");
                String bId = arg2.optjstring("Button");
                String tip = r32.arg(3).optjstring("");
                String icon = r32.arg(4).optjstring("");
                String text = r32.arg(5).optjstring(bId);
                btn.setName(text.length() > 0 ? text : bId);
                btn.set("Tooltip", tip, false);
                btn.set("Icon", icon, false);
                btn.set("Enabled", Boolean.TRUE, false);
                Instance.setParent$default(btn, r30, 0, 2, null);
                studio.addPluginButton(btn, pluginId(r30.getParent() != null ? r30.getParent() : r30), r29);
                return r29.wrap(btn);
            }
            case "SetActive": {
                r30.set("Active", Boolean.valueOf(arg2.toboolean()), false);
                studio.notifyPluginButtons();
                return LuaValue.NONE;
            }
            case "CreateDockWidgetPluginGui":
            case "CreateDockWidgetPluginGuiAsync":
            case "CreateQWidgetPluginGui": {
                Instance w = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("DockWidgetPluginGui");
                String wName = arg2.optjstring("Widget");
                Intrinsics.checkNotNullExpressionValue(wName, "optjstring(...)");
                w.setName(wName);
                LuaValue info = r32.arg(3);
                boolean enabled = info.istable() && info.get("InitialEnabled").toboolean();
                w.set("Enabled", Boolean.valueOf(enabled), false);
                w.set("Title", w.getName(), false);
                w.set("ZIndexBehavior", new com.goydevv.studrblx.model.EnumValue(1), false);
                if (info.istable()) {
                    w.getAttributes().put("__w", Integer.valueOf(info.get("FloatingXSize").optint(300)));
                    w.getAttributes().put("__h", Integer.valueOf(info.get("FloatingYSize").optint(300)));
                }
                studio.addPluginWidget(w, r29);
                return r29.wrap(w);
            }
            case "GetSetting": {
                String k = arg2.optjstring("");
                String raw = studio.pluginSetting(pluginId(r30), k);
                if (raw == null) {
                    return LuaValue.NIL;
                }
                return LuaValue.valueOf(raw);
            }
            case "SetSetting": {
                String k = arg2.optjstring("");
                LuaValue v = r32.arg(3);
                studio.setPluginSetting(pluginId(r30), k, v.isnil() ? null : v.tojstring());
                return LuaValue.NONE;
            }
            case "Activate": {
                studio.activatePlugin(r30, r29, arg2.optboolean(false));
                return LuaValue.NONE;
            }
            case "Deactivate": {
                studio.deactivatePlugin(r30);
                return LuaValue.NONE;
            }
            case "IsActivated":
                return LuaValue.valueOf(studio.getActivePlugin() == r30);
            case "IsActivatedWithExclusiveMouse":
                return LuaValue.valueOf(studio.getActivePlugin() == r30 && studio.getActivePluginExclusive());
            case "GetMouse": {
                LuaTable m = studio.getPluginMice().get(r30);
                if (m == null) {
                    m = pluginMouse(r29);
                    studio.getPluginMice().put(r30, m);
                }
                return m;
            }
            case "CreatePluginAction": {
                String id = arg2.optjstring("");
                String txt = r32.arg(3).optjstring("");
                String tip = r32.arg(4).optjstring("");
                return action(r29, id, txt, tip);
            }
            case "CreatePluginMenu": {
                String id = arg2.optjstring("");
                String title = r32.arg(3).optjstring(id);
                return menu(r29, studio, id, title);
            }
            case "GetStudioUserId":
                return LuaValue.valueOf(0);
            case "Select":
            case "OpenScript":
            case "OpenWikiPage":
            case "SetReady":
            case "StartDrag":
            case "SaveSelectedToRoblox":
            case "PromptSaveSelection":
            case "PromptImportFile":
                return LuaValue.NONE;
            default:
                return LuaValue.NIL;
        }
    }

    private final String pluginId(Instance i) {
        return i.getName();
    }

    public final LuaTable pluginMouse(final LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(rt, "rt");
        final LuaTable m = new LuaTable();
        m.set("Hit", LuaTypes.INSTANCE.wrap(CFrame.INSTANCE.getIDENTITY()));
        m.set("Target", LuaValue.NIL);
        m.set("X", 0);
        m.set("Y", 0);
        m.set("Icon", "");
        m.set("TargetFilter", LuaValue.NIL);
        m.set("Origin", LuaTypes.INSTANCE.wrap(CFrame.INSTANCE.getIDENTITY()));
        m.set("UnitRay", LuaTypes.INSTANCE.wrap(new Ray(Vector3.INSTANCE.getZERO(), new Vector3(0.0f, 0.0f, -1.0f))));
        m.set("ViewSizeX", 0);
        m.set("ViewSizeY", 0);
        m.set("ClassName", "PluginMouse");
        m.set("Name", "Instance");
        LuaTable mt = new LuaTable();
        mt.set("__type", "Instance");
        mt.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return PluginApi.pluginMouse$lambda$10(rt, m, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        m.setmetatable(mt);
        return m;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0029, code lost:
    
        if (r4.equals("Button1Up") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0033, code lost:
    
        if (r4.equals("DragEnter") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x003d, code lost:
    
        if (r4.equals("WheelBackward") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0047, code lost:
    
        if (r4.equals("KeyDown") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0051, code lost:
    
        if (r4.equals("WheelForward") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x005b, code lost:
    
        if (r4.equals("KeyUp") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0064, code lost:
    
        if (r4.equals("Move") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006d, code lost:
    
        if (r4.equals("Idle") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008a, code lost:
    
        if (r4.equals("Button2Down") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0093, code lost:
    
        if (r4.equals("Button1Down") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00b0, code lost:
    
        if (r4.equals("Changed") != false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x001f, code lost:
    
        if (r4.equals("Button2Up") != false) goto L48;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue pluginMouse$lambda$10(final com.goydevv.studrblx.lua.LuaRuntime r2, final org.luaj.vm2.LuaTable r3, org.luaj.vm2.LuaValue r4, org.luaj.vm2.LuaValue r5) {
        String key = r5 != null ? r5.tojstring() : "";
        switch (key) {
            case "Button1Down":
            case "Button1Up":
            case "Button2Down":
            case "Button2Up":
            case "Move":
            case "Idle":
            case "WheelForward":
            case "WheelBackward":
            case "KeyDown":
            case "KeyUp":
            case "DragEnter":
            case "Changed":
                return r2.getApi().signal(r3, key);
            case "IsA":
                return LuaTypesKt.fnv(new Function1() {
                    @Override
                    public final Object invoke(Object obj) {
                        return PluginApi.pluginMouse$lambda$10$lambda$8((Varargs) obj);
                    }
                });
            case "GetPropertyChangedSignal":
                return LuaTypesKt.fnv(new Function1() {
                    @Override
                    public final Object invoke(Object obj) {
                        return PluginApi.pluginMouse$lambda$10$lambda$9(r2, r3, (Varargs) obj);
                    }
                });
            default:
                LuaValue raw = r3.rawget(r5);
                return raw != null ? raw : LuaValue.NIL;
        }
    }

    static final Varargs pluginMouse$lambda$10$lambda$8(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaBoolean luaBooleanValueOf = LuaValue.valueOf(SetsKt.setOf(new String[]{"Mouse", "PluginMouse", "Instance"}).contains(a.arg(2).tojstring()));
        Intrinsics.checkNotNullExpressionValue(luaBooleanValueOf, "valueOf(...)");
        return luaBooleanValueOf;
    }

    static final Varargs pluginMouse$lambda$10$lambda$9(LuaRuntime $rt, LuaTable $m, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return $rt.getApi().signal($m, "Changed:" + a.arg(2).tojstring());
    }

    private final LuaTable action(final LuaRuntime rt, String id, String text, String tip) {
        final LuaTable t = new LuaTable();
        t.set("ActionId", id);
        t.set("Text", text);
        t.set("StatusTip", tip);
        t.set("Enabled", LuaValue.TRUE);
        t.set("Checked", LuaValue.FALSE);
        String str = text;
        if (str.length() == 0) {
            str = id;
        }
        t.set("Name", str);
        LuaTable mt = new LuaTable();
        mt.set("__type", "Instance");
        mt.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return PluginApi.action$lambda$12(rt, t, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        t.setmetatable(mt);
        return t;
    }

    static final LuaValue action$lambda$12(LuaRuntime $rt, LuaTable $t, LuaValue luaValue, LuaValue k) {
        Intrinsics.checkNotNullParameter(luaValue, "<unused var>");
        Intrinsics.checkNotNullParameter(k, "k");
        if (Intrinsics.areEqual(k.tojstring(), "Triggered")) {
            return $rt.getApi().signal($t, "Triggered");
        }
        LuaValue NIL = LuaValue.NIL;
        Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
        return NIL;
    }

    private final LuaTable menu(final LuaRuntime rt, final Studio studio, String id, final String title) {
        LuaTable t = new LuaTable();
        final ArrayList items = new ArrayList();
        t.set("Title", title);
        t.set("Name", id);
        t.set("AddAction", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PluginApi.menu$lambda$13(items, (Varargs) obj);
            }
        }));
        t.set("AddNewAction", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PluginApi.menu$lambda$14(rt, items, (Varargs) obj);
            }
        }));
        t.set("AddSeparator", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PluginApi.menu$lambda$15(items, (Varargs) obj);
            }
        }));
        t.set("AddMenu", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PluginApi.menu$lambda$16(items, (Varargs) obj);
            }
        }));
        t.set("Clear", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PluginApi.menu$lambda$17(items, (Varargs) obj);
            }
        }));
        t.set("Destroy", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PluginApi.menu$lambda$18(items, (Varargs) obj);
            }
        }));
        t.set("ShowAsync", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PluginApi.menu$lambda$26(rt, items, studio, title, (Varargs) obj);
            }
        }));
        LuaTable mt = new LuaTable();
        mt.set("__type", "Instance");
        t.setmetatable(mt);
        return t;
    }

    static final Varargs menu$lambda$13(ArrayList $items, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        $items.add(a.arg(2));
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs menu$lambda$14(LuaRuntime $rt, ArrayList $items, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        PluginApi pluginApi = INSTANCE;
        String strOptjstring = a.arg(2).optjstring("");
        Intrinsics.checkNotNullExpressionValue(strOptjstring, "optjstring(...)");
        String strOptjstring2 = a.arg(3).optjstring("");
        Intrinsics.checkNotNullExpressionValue(strOptjstring2, "optjstring(...)");
        LuaTable act = pluginApi.action($rt, strOptjstring, strOptjstring2, "");
        $items.add(act);
        return act;
    }

    static final Varargs menu$lambda$15(ArrayList $items, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $items.add(LuaValue.NIL);
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs menu$lambda$16(ArrayList $items, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        $items.add(a.arg(2));
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs menu$lambda$17(ArrayList $items, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $items.clear();
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs menu$lambda$18(ArrayList $items, Varargs it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $items.clear();
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs menu$lambda$26(final LuaRuntime $rt, ArrayList $items, final Studio $studio, final String $title, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        final ScriptThread co = $rt.currentCoroutine();
        if (co == null) {
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            return NIL;
        }
        Collection arrayList = new ArrayList();
        Iterator it = $items.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            LuaValue luaValue = (LuaValue) next;
            if (luaValue.istable() && !luaValue.get("ActionId").isnil()) {
                arrayList.add(next);
            }
        }
        final List acts = (List) arrayList;
        List<LuaValue> list = acts;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        for (LuaValue luaValue2 : list) {
            String strOptjstring = luaValue2.get("Text").optjstring("");
            if (strOptjstring.length() == 0) {
                strOptjstring = luaValue2.get("ActionId").tojstring();
            }
            arrayList2.add(strOptjstring);
        }
        final List labels = (List) arrayList2;
        $studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PluginApi.menu$lambda$26$lambda$25($studio, $title, labels, $rt, co, acts);
            }
        });
        throw new LuauYield();
    }

    static final void menu$lambda$26$lambda$25(final Studio $studio, String $title, List $labels, final LuaRuntime $rt, final ScriptThread $co, final List $acts) {
        Function3<? super String, ? super List<String>, ? super Function1<? super Integer, Unit>, Unit> pluginMenuHandler = $studio.getPluginMenuHandler();
        if (pluginMenuHandler == null) {
            synchronized ($studio.getLock()) {
                LuaValue NIL = LuaValue.NIL;
                Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
                $rt.resume($co, NIL);
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        ((Function3) pluginMenuHandler).invoke($title, $labels, new Function1() { // from class: com.goydevv.studrblx.lua.PluginApi$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PluginApi.menu$lambda$26$lambda$25$lambda$24($studio, $acts, $rt, $co, ((Integer) obj).intValue());
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0017 A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:6:0x0009, B:11:0x0017, B:13:0x0020, B:15:0x0026, B:16:0x0030, B:12:0x001e), top: B:23:0x0009 }] */
    /* JADX WARN: Code duplicated, block: B:12:0x001e A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:6:0x0009, B:11:0x0017, B:13:0x0020, B:15:0x0026, B:16:0x0030, B:12:0x001e), top: B:23:0x0009 }] */
    /* JADX WARN: Code duplicated, block: B:15:0x0026 A[Catch: all -> 0x0040, TryCatch #0 {, blocks: (B:6:0x0009, B:11:0x0017, B:13:0x0020, B:15:0x0026, B:16:0x0030, B:12:0x001e), top: B:23:0x0009 }] */
    /* JADX WARN: Code duplicated, block: B:9:0x0014  */
    static final Unit menu$lambda$26$lambda$25$lambda$24(Studio $studio, List $acts, LuaRuntime $rt, ScriptThread $co, int idx) {
        boolean z;
        LuaValue luaValue;
        synchronized ($studio.getLock()) {
            if (idx < 0) {
                z = false;
                if (z) {
                    luaValue = (LuaValue) $acts.get(idx);
                } else {
                    luaValue = LuaValue.NIL;
                }
                if (!luaValue.isnil()) {
                    Intrinsics.checkNotNull(luaValue);
                    $rt.fire(luaValue, "Triggered", new LuaValue[0]);
                }
                Intrinsics.checkNotNull(luaValue);
                $rt.resume($co, luaValue);
                Unit unit = Unit.INSTANCE;
            } else {
                if (idx < $acts.size()) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    luaValue = (LuaValue) $acts.get(idx);
                } else {
                    luaValue = LuaValue.NIL;
                }
                if (!luaValue.isnil()) {
                    Intrinsics.checkNotNull(luaValue);
                    $rt.fire(luaValue, "Triggered", new LuaValue[0]);
                }
                Intrinsics.checkNotNull(luaValue);
                $rt.resume($co, luaValue);
                Unit unit2 = Unit.INSTANCE;
            }
            return Unit.INSTANCE;
        }
    }
}
