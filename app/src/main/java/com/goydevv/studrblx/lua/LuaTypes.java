package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.BrickColors;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.ColorKey;
import com.goydevv.studrblx.model.ColorSequence;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.FontValue;
import com.goydevv.studrblx.model.NumberKey;
import com.goydevv.studrblx.model.NumberRange;
import com.goydevv.studrblx.model.NumberSequence;
import com.goydevv.studrblx.model.PhysicalProps;
import com.goydevv.studrblx.model.Ray;
import com.goydevv.studrblx.model.Rect;
import com.goydevv.studrblx.model.UDim;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.model.Vector3int16;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class LuaTypes {
    public static final LuaTypes INSTANCE = new LuaTypes();
    private static final HashMap<Class<?>, LuaTable> metas = new HashMap<>();

    private LuaTypes() {
    }

    static {
        INSTANCE.meta(Vector3.class, "Vector3", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$17(obj, (String) obj2);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda26
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes._init_$lambda$23((LuaTable) obj);
            }
        });
        INSTANCE.meta(Vector2.class, "Vector2", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda37
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$26(obj, (String) obj2);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda38
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes._init_$lambda$32((LuaTable) obj);
            }
        });
        INSTANCE.meta(CFrame.class, "CFrame", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda39
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$47(obj, (String) obj2);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda40
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes._init_$lambda$51((LuaTable) obj);
            }
        });
        meta$default(INSTANCE, Color3.class, "Color3", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda41
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$55(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, BrickColorV.class, "BrickColor", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda42
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$56(obj, (String) obj2);
            }
        }, null, 8, null);
        INSTANCE.meta(UDim.class, "UDim", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda43
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$57(obj, (String) obj2);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda44
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes._init_$lambda$60((LuaTable) obj);
            }
        });
        INSTANCE.meta(UDim2.class, "UDim2", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$62(obj, (String) obj2);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes._init_$lambda$66((LuaTable) obj);
            }
        });
        INSTANCE.meta(EnumItemV.class, "EnumItem", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$68(obj, (String) obj2);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes._init_$lambda$70((LuaTable) obj);
            }
        });
        INSTANCE.meta(EnumTypeV.class, "Enum", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$76(obj, (String) obj2);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda21
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes._init_$lambda$78((LuaTable) obj);
            }
        });
        meta$default(INSTANCE, TweenInfoV.class, "TweenInfo", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda22
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$79(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, NumberRange.class, "NumberRange", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda23
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$80(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, Ray.class, "Ray", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda24
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$82(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, Region3V.class, "Region3", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda25
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$84(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, Region3int16V.class, "Region3int16", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda27
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$85(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, RandomV.class, "Random", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda28
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$89(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, RaycastResultV.class, "RaycastResult", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda29
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$90(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, NumberSequence.class, "NumberSequence", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda30
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$93(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, NumberKey.class, "NumberSequenceKeypoint", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda31
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$94(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, ColorSequence.class, "ColorSequence", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda32
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$97(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, ColorKey.class, "ColorSequenceKeypoint", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda33
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$98(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, FontValue.class, "Font", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda34
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$99(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, Rect.class, "Rect", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda35
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$100(obj, (String) obj2);
            }
        }, null, 8, null);
        meta$default(INSTANCE, PhysicalProps.class, "PhysicalProperties", new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda36
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes._init_$lambda$101(obj, (String) obj2);
            }
        }, null, 8, null);
    }

    public final String typeOf(LuaValue v) {
        Intrinsics.checkNotNullParameter(v, "v");
        if (v.isuserdata()) {
            if (v.touserdata() instanceof java.nio.ByteBuffer) {
                return "buffer";
            }
            LuaValue mt = v.getmetatable();
            LuaValue t = mt != null ? mt.get("__type") : null;
            if (t == null || !t.isstring()) {
                return "userdata";
            }
            String str = t.tojstring();
            Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
            return str;
        }
        if (v.istable()) {
            LuaValue mt2 = v.getmetatable();
            LuaValue t2 = mt2 != null ? mt2.get("__type") : null;
            if (t2 != null && t2.isstring()) {
                String st2 = t2.tojstring();
                if (st2 != null) return st2;
            }
        }
        String strTypename = v.typename();
        Intrinsics.checkNotNullExpressionValue(strTypename, "typename(...)");
        return strTypename;
    }

    public final LuaValue wrap(Object o) {
        Intrinsics.checkNotNullParameter(o, "o");
        LuaTable mt = metas.get(o.getClass());
        if (mt == null) {
            String simpleName = o.getClass().getSimpleName();
            Intrinsics.checkNotNullExpressionValue(simpleName, "getSimpleName(...)");
            mt = generic(simpleName);
        }
        return new LuaUserdata(o, mt);
    }

    private final LuaTable generic(String name) {
        LuaTable luaTable = new LuaTable();
        luaTable.set("__type", name);
        luaTable.set(LuaValue.TOSTRING, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda100
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.generic$lambda$1$lambda$0((LuaValue) obj);
            }
        }));
        return luaTable;
    }

    static final LuaValue generic$lambda$1$lambda$0(LuaValue it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaString luaStringValueOf = LuaValue.valueOf(it.touserdata().toString());
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void meta$default(LuaTypes luaTypes, Class cls, String str, Function2 function2, Function1 function1, int i, Object obj) {
        if ((i & 8) != 0) {
            function1 = new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda109
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj2) {
                    return LuaTypes.meta$lambda$2((LuaTable) obj2);
                }
            };
        }
        luaTypes.meta(cls, str, function2, function1);
    }

    static final Unit meta$lambda$2(LuaTable luaTable) {
        Intrinsics.checkNotNullParameter(luaTable, "<this>");
        return Unit.INSTANCE;
    }

    private final void meta(Class<?> cls, final String typeName, final Function2<Object, ? super String, ? extends LuaValue> index, Function1<? super LuaTable, Unit> extra) {
        LuaTable mt = new LuaTable();
        mt.set("__type", typeName);
        mt.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.meta$lambda$3(index, typeName, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        mt.set(LuaValue.NEWINDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.meta$lambda$4((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        mt.set(LuaValue.TOSTRING, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.meta$lambda$5((LuaValue) obj);
            }
        }));
        extra.invoke(mt);
        metas.put(cls, mt);
    }

    static final LuaValue meta$lambda$3(Function2 $index, String $typeName, LuaValue self, LuaValue key) {
        Intrinsics.checkNotNullParameter(self, "self");
        Intrinsics.checkNotNullParameter(key, "key");
        String k = key.tojstring();
        Object obj = self.touserdata();
        Intrinsics.checkNotNullExpressionValue(obj, "touserdata(...)");
        Intrinsics.checkNotNull(k);
        LuaValue luaValue = (LuaValue) $index.invoke(obj, k);
        if (luaValue != null) {
            return luaValue;
        }
        throw new LuaError(k + " is not a valid member of " + $typeName);
    }

    static final LuaValue meta$lambda$4(LuaValue luaValue, LuaValue key) {
        Intrinsics.checkNotNullParameter(luaValue, "<unused var>");
        Intrinsics.checkNotNullParameter(key, "key");
        throw new LuaError(key.tojstring() + " cannot be assigned to");
    }

    private static String fnum(float f) {
        if (Float.isNaN(f)) return "nan";
        if (Float.isInfinite(f)) return f > 0 ? "inf" : "-inf";
        String r = new java.math.BigDecimal(Float.toString(f)).stripTrailingZeros().toPlainString();
        return "-0".equals(r) ? "0" : r;
    }

    private static final String[] EASING_STYLES = {"Linear", "Sine", "Back", "Quad", "Quart", "Quint", "Bounce", "Elastic", "Exponential", "Circular", "Cubic"};
    private static final String[] EASING_DIRS = {"In", "Out", "InOut"};

    static String describe(Object o) {
        if (o instanceof Color3) {
            Color3 c = (Color3) o;
            return fnum(c.getR()) + ", " + fnum(c.getG()) + ", " + fnum(c.getB());
        }
        if (o instanceof TweenInfoV) {
            TweenInfoV t = (TweenInfoV) o;
            String st = t.getStyle() >= 0 && t.getStyle() < EASING_STYLES.length ? EASING_STYLES[t.getStyle()] : String.valueOf(t.getStyle());
            String dr = t.getDirection() >= 0 && t.getDirection() < EASING_DIRS.length ? EASING_DIRS[t.getDirection()] : String.valueOf(t.getDirection());
            return "Time:" + fnum(t.getTime()) + " DelayTime:" + fnum(t.getDelay()) + " RepeatCount:" + t.getRepeatCount() + " Reverses:" + (t.getReverses() ? "True" : "False") + " EasingDirection:" + dr + " EasingStyle:" + st;
        }
        return o.toString();
    }

    static final LuaValue meta$lambda$5(LuaValue it) {
        Intrinsics.checkNotNullParameter(it, "it");
        LuaString luaStringValueOf = LuaValue.valueOf(describe(it.touserdata()));
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    private final LuaValue method(Function1<? super Varargs, ? extends Varargs> f) {
        return LuaTypesKt.fnv(f);
    }

    private final LuaValue n(float v) {
        LuaNumber luaNumberValueOf = LuaValue.valueOf(v);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    public final LuaValue v3(Vector3 v) {
        Intrinsics.checkNotNullParameter(v, "v");
        return wrap(v);
    }

    private final Vector3 arg3(Varargs a, int i) {
        LuaValue luaValueArg = a.arg(i);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj instanceof Vector3 ? obj : null);
        if (vector3 != null) {
            return vector3;
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0070, code lost:
    
        if (r4.equals("unit") == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x007a, code lost:
    
        if (r4.equals("lerp") == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0084, code lost:
    
        if (r4.equals("Unit") != false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00ad, code lost:
    
        if (r4.equals("Lerp") != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x012b, code lost:
    
        if (r4.equals("z") == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0134, code lost:
    
        if (r4.equals("y") == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x013d, code lost:
    
        if (r4.equals("x") == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0146, code lost:
    
        if (r4.equals("Z") != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0148, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getZ();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0155, code lost:
    
        if (r4.equals("Y") != false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0157, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getY();
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0164, code lost:
    
        if (r4.equals("X") != false) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0166, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getX();
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0177, code lost:
    
        if (r4.equals("Magnitude") == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0180, code lost:
    
        if (r4.equals("magnitude") == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r0.getUnit());
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda154());
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:?, code lost:
    
        return r1.n(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.n(r0.getMagnitude());
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue _init_$lambda$17(java.lang.Object o, java.lang.String k) {
        Vector3 v = (Vector3) o;
        if (k == null) return LuaValue.NIL;
        switch (k) {
            case "X": case "x": return INSTANCE.n(v.getX());
            case "Y": case "y": return INSTANCE.n(v.getY());
            case "Z": case "z": return INSTANCE.n(v.getZ());
            case "Magnitude": case "magnitude": return INSTANCE.n(v.getMagnitude());
            case "Unit": case "unit": return INSTANCE.wrap(v.getUnit());
            case "Dot": case "dot": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$6(a); } });
            case "Cross": case "cross": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$7(a); } });
            case "Lerp": case "lerp": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$8(a); } });
            case "Abs": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$9(a); } });
            case "Floor": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$10(a); } });
            case "Ceil": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$11(a); } });
            case "Sign": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$12(a); } });
            case "Min": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$13(a); } });
            case "Max": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$14(a); } });
            case "Angle": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$15(a); } });
            case "FuzzyEq": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$17$lambda$16(a); } });
            default: return LuaValue.NIL;
        }
    }

    static final Varargs lambda$17$lambda$6(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.n(INSTANCE.arg3(a, 1).dot(INSTANCE.arg3(a, 2)));
    }

    static final Varargs lambda$17$lambda$7(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.wrap(INSTANCE.arg3(a, 1).cross(INSTANCE.arg3(a, 2)));
    }

    static final Varargs lambda$17$lambda$8(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        Vector3 vector3Arg3 = INSTANCE.arg3(a, 1);
        Vector3 vector3Arg4 = INSTANCE.arg3(a, 2);
        LuaValue luaValueArg = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        return luaTypes.wrap(vector3Arg3.lerp(vector3Arg4, LuaTypesKt.num(luaValueArg)));
    }

    static final Varargs lambda$17$lambda$9(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.wrap(INSTANCE.arg3(a, 1).abs());
    }

    static final Varargs lambda$17$lambda$10(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        Vector3 x = INSTANCE.arg3(a, 1);
        return INSTANCE.wrap(new Vector3((float) Math.floor(x.getX()), (float) Math.floor(x.getY()), (float) Math.floor(x.getZ())));
    }

    static final Varargs lambda$17$lambda$11(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        Vector3 x = INSTANCE.arg3(a, 1);
        return INSTANCE.wrap(new Vector3((float) Math.ceil(x.getX()), (float) Math.ceil(x.getY()), (float) Math.ceil(x.getZ())));
    }

    static final Varargs lambda$17$lambda$12(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        Vector3 x = INSTANCE.arg3(a, 1);
        return INSTANCE.wrap(new Vector3(Math.signum(x.getX()), Math.signum(x.getY()), Math.signum(x.getZ())));
    }

    static final Varargs lambda$17$lambda$13(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.wrap(INSTANCE.arg3(a, 1).min(INSTANCE.arg3(a, 2)));
    }

    static final Varargs lambda$17$lambda$14(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.wrap(INSTANCE.arg3(a, 1).max(INSTANCE.arg3(a, 2)));
    }

    static final Varargs lambda$17$lambda$15(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        Vector3 x = INSTANCE.arg3(a, 1).getUnit();
        Vector3 y = INSTANCE.arg3(a, 2).getUnit();
        return INSTANCE.n((float) Math.acos(RangesKt.coerceIn(x.dot(y), -1.0f, 1.0f)));
    }

    static final Varargs lambda$17$lambda$16(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float e = LuaTypesKt.optNum(luaValueArg, 1.0E-5f);
        LuaBoolean luaBooleanValueOf = LuaValue.valueOf(INSTANCE.arg3(a, 1).minus(INSTANCE.arg3(a, 2)).getMagnitude() <= e);
        Intrinsics.checkNotNullExpressionValue(luaBooleanValueOf, "valueOf(...)");
        return luaBooleanValueOf;
    }

    static final Unit _init_$lambda$23(LuaTable meta) {
        Intrinsics.checkNotNullParameter(meta, "$this$meta");
        meta.set(LuaValue.ADD, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda70
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$23$lambda$18((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.SUB, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda71
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$23$lambda$19((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.MUL, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda72
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$23$lambda$20((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.DIV, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda73
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$23$lambda$21((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.UNM, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda74
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.lambda$23$lambda$22((LuaValue) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final LuaValue lambda$23$lambda$18(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof Vector3)) {
            obj = null;
        }
        Vector3 vector3 = (Vector3) obj;
        if (vector3 == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        Vector3 vector4 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector4 != null) {
            return luaTypes.wrap(vector3.plus(vector4));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$23$lambda$19(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof Vector3)) {
            obj = null;
        }
        Vector3 vector3 = (Vector3) obj;
        if (vector3 == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        Vector3 vector4 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector4 != null) {
            return luaTypes.wrap(vector3.minus(vector4));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$23$lambda$20(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        if (!a.isnumber()) {
            if (!b.isnumber()) {
                LuaTypes luaTypes = INSTANCE;
                Object obj = a.isuserdata() ? a.touserdata() : null;
                if (!(obj instanceof Vector3)) {
                    obj = null;
                }
                Vector3 vector3 = (Vector3) obj;
                if (vector3 == null) {
                    throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(a));
                }
                Object obj2 = b.isuserdata() ? b.touserdata() : null;
                Vector3 vector4 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
                if (vector4 != null) {
                    return luaTypes.wrap(vector3.times(vector4));
                }
                throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(b));
            }
            LuaTypes luaTypes2 = INSTANCE;
            Object obj3 = a.isuserdata() ? a.touserdata() : null;
            Vector3 vector5 = (Vector3) (obj3 instanceof Vector3 ? obj3 : null);
            if (vector5 != null) {
                return luaTypes2.wrap(vector5.times(LuaTypesKt.num(b)));
            }
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(a));
        }
        LuaTypes luaTypes3 = INSTANCE;
        Object obj4 = b.isuserdata() ? b.touserdata() : null;
        Vector3 vector6 = (Vector3) (obj4 instanceof Vector3 ? obj4 : null);
        if (vector6 != null) {
            return luaTypes3.wrap(vector6.times(LuaTypesKt.num(a)));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$23$lambda$21(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        if (!b.isnumber()) {
            if (!a.isnumber()) {
                LuaTypes luaTypes = INSTANCE;
                Object obj = a.isuserdata() ? a.touserdata() : null;
                if (!(obj instanceof Vector3)) {
                    obj = null;
                }
                Vector3 vector3 = (Vector3) obj;
                if (vector3 == null) {
                    throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(a));
                }
                Object obj2 = b.isuserdata() ? b.touserdata() : null;
                Vector3 vector4 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
                if (vector4 != null) {
                    return luaTypes.wrap(vector3.div(vector4));
                }
                throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(b));
            }
            Object obj3 = b.isuserdata() ? b.touserdata() : null;
            Vector3 v = (Vector3) (obj3 instanceof Vector3 ? obj3 : null);
            if (v != null) {
                return INSTANCE.wrap(new Vector3(LuaTypesKt.num(a) / v.getX(), LuaTypesKt.num(a) / v.getY(), LuaTypesKt.num(a) / v.getZ()));
            }
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(b));
        }
        LuaTypes luaTypes2 = INSTANCE;
        Object obj4 = a.isuserdata() ? a.touserdata() : null;
        Vector3 vector5 = (Vector3) (obj4 instanceof Vector3 ? obj4 : null);
        if (vector5 != null) {
            return luaTypes2.wrap(vector5.div(LuaTypesKt.num(b)));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(a));
    }

    static final LuaValue lambda$23$lambda$22(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj instanceof Vector3 ? obj : null);
        if (vector3 != null) {
            return luaTypes.wrap(vector3.unaryMinus());
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(a));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0063, code lost:
    
        if (r4.equals("y") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x006c, code lost:
    
        if (r4.equals("x") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0075, code lost:
    
        if (r4.equals("Y") != false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0077, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getY();
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0084, code lost:
    
        if (r4.equals("X") != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0086, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getX();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0097, code lost:
    
        if (r4.equals("Magnitude") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00a0, code lost:
    
        if (r4.equals("magnitude") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r0.getUnit());
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:?, code lost:
    
        return r1.n(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.n(r0.getMagnitude());
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001c, code lost:
    
        if (r4.equals("unit") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0026, code lost:
    
        if (r4.equals("Unit") != false) goto L10;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue _init_$lambda$26(java.lang.Object o, java.lang.String k) {
        Vector2 v = (Vector2) o;
        if (k == null) return LuaValue.NIL;
        switch (k) {
            case "X": case "x": return INSTANCE.n(v.getX());
            case "Y": case "y": return INSTANCE.n(v.getY());
            case "Magnitude": case "magnitude": return INSTANCE.n(v.getMagnitude());
            case "Unit": case "unit": return INSTANCE.wrap(v.getUnit());
            case "Dot": case "dot": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$26$lambda$24(a); } });
            case "Lerp": case "lerp": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$26$lambda$25(a); } });
            default: return LuaValue.NIL;
        }
    }

    static final Varargs lambda$26$lambda$24(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector2)) {
            obj = null;
        }
        Vector2 vector2 = (Vector2) obj;
        if (vector2 == null) {
            throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector2 vector3 = (Vector2) (obj2 instanceof Vector2 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.n(vector2.dot(vector3));
        }
        throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$26$lambda$25(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector2)) {
            obj = null;
        }
        Vector2 vector2 = (Vector2) obj;
        if (vector2 == null) {
            throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector2 vector3 = (Vector2) (obj2 instanceof Vector2 ? obj2 : null);
        if (vector3 != null) {
            LuaValue luaValueArg3 = a.arg(3);
            Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
            return luaTypes.wrap(vector2.lerp(vector3, LuaTypesKt.num(luaValueArg3)));
        }
        throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Unit _init_$lambda$32(LuaTable meta) {
        Intrinsics.checkNotNullParameter(meta, "$this$meta");
        meta.set(LuaValue.ADD, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda57
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$32$lambda$27((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.SUB, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda58
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$32$lambda$28((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.MUL, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda59
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$32$lambda$29((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.DIV, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda60
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$32$lambda$30((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.UNM, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda61
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.lambda$32$lambda$31((LuaValue) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final LuaValue lambda$32$lambda$27(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof Vector2)) {
            obj = null;
        }
        Vector2 vector2 = (Vector2) obj;
        if (vector2 == null) {
            throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        Vector2 vector3 = (Vector2) (obj2 instanceof Vector2 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.wrap(vector2.plus(vector3));
        }
        throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$32$lambda$28(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof Vector2)) {
            obj = null;
        }
        Vector2 vector2 = (Vector2) obj;
        if (vector2 == null) {
            throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        Vector2 vector3 = (Vector2) (obj2 instanceof Vector2 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.wrap(vector2.minus(vector3));
        }
        throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$32$lambda$29(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        if (!a.isnumber()) {
            if (!b.isnumber()) {
                LuaTypes luaTypes = INSTANCE;
                Object obj = a.isuserdata() ? a.touserdata() : null;
                if (!(obj instanceof Vector2)) {
                    obj = null;
                }
                Vector2 vector2 = (Vector2) obj;
                if (vector2 == null) {
                    throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(a));
                }
                Object obj2 = b.isuserdata() ? b.touserdata() : null;
                Vector2 vector3 = (Vector2) (obj2 instanceof Vector2 ? obj2 : null);
                if (vector3 != null) {
                    return luaTypes.wrap(vector2.times(vector3));
                }
                throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(b));
            }
            LuaTypes luaTypes2 = INSTANCE;
            Object obj3 = a.isuserdata() ? a.touserdata() : null;
            Vector2 vector4 = (Vector2) (obj3 instanceof Vector2 ? obj3 : null);
            if (vector4 != null) {
                return luaTypes2.wrap(vector4.times(LuaTypesKt.num(b)));
            }
            throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(a));
        }
        LuaTypes luaTypes3 = INSTANCE;
        Object obj4 = b.isuserdata() ? b.touserdata() : null;
        Vector2 vector5 = (Vector2) (obj4 instanceof Vector2 ? obj4 : null);
        if (vector5 != null) {
            return luaTypes3.wrap(vector5.times(LuaTypesKt.num(a)));
        }
        throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$32$lambda$30(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        Vector2 vector2 = (Vector2) (obj instanceof Vector2 ? obj : null);
        if (vector2 != null) {
            return luaTypes.wrap(vector2.div(LuaTypesKt.num(b)));
        }
        throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(a));
    }

    static final LuaValue lambda$32$lambda$31(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        Vector2 vector2 = (Vector2) (obj instanceof Vector2 ? obj : null);
        if (vector2 != null) {
            return luaTypes.wrap(vector2.unaryMinus());
        }
        throw new LuaError("Vector2 expected, got " + INSTANCE.typeOf(a));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0217, code lost:
    
        if (r7.equals("pointToWorldSpace") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0221, code lost:
    
        if (r7.equals("ToObjectSpace") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x022b, code lost:
    
        if (r7.equals("ToWorldSpace") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x0235, code lost:
    
        if (r7.equals("ToOrientation") != false) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x024a, code lost:
    
        if (r7.equals("PointToWorldSpace") != false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x025f, code lost:
    
        if (r7.equals("toWorldSpace") != false) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0273, code lost:
    
        if (r7.equals("VectorToWorldSpace") != false) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x029b, code lost:
    
        if (r7.equals("PointToObjectSpace") != false) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0030, code lost:
    
        if (r7.equals("VectorToObjectSpace") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x02af, code lost:
    
        if (r7.equals("LookVector") != false) goto L132;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x02c2, code lost:
    
        if (r7.equals("toObjectSpace") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r0.getUpVector());
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda118());
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r0.getPosition());
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:?, code lost:
    
        return r1.n(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda124());
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda125());
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r0.getRightVector());
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda115());
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda114());
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda126());
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda121());
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda119());
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda123());
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda122());
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(r0.getLookVector());
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda120());
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x003a, code lost:
    
        if (r7.equals("UpVector") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0044, code lost:
    
        if (r7.equals("upVector") != false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0075, code lost:
    
        if (r7.equals("Position") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x007f, code lost:
    
        if (r7.equals("vectorToWorldSpace") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0089, code lost:
    
        if (r7.equals("toEulerAnglesYXZ") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0093, code lost:
    
        if (r7.equals("toEulerAnglesXYZ") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x009d, code lost:
    
        if (r7.equals("pointToObjectSpace") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00a7, code lost:
    
        if (r7.equals("lookVector") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00b1, code lost:
    
        if (r7.equals("GetComponents") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00cf, code lost:
    
        if (r7.equals("lerp") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00d9, code lost:
    
        if (r7.equals("Lerp") != false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00ee, code lost:
    
        if (r7.equals("z") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00f8, code lost:
    
        if (r7.equals("y") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0102, code lost:
    
        if (r7.equals("x") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x010c, code lost:
    
        if (r7.equals("p") != false) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0120, code lost:
    
        if (r7.equals("Z") != false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0122, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getZ();
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x012f, code lost:
    
        if (r7.equals("Y") != false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001c, code lost:
    
        if (r7.equals("inverse") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0131, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getY();
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x013e, code lost:
    
        if (r7.equals("X") != false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0140, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getX();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x016f, code lost:
    
        if (r7.equals("vectorToObjectSpace") != false) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0184, code lost:
    
        if (r7.equals("ToEulerAnglesYXZ") == false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x018e, code lost:
    
        if (r7.equals("ToEulerAnglesXYZ") != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x01a3, code lost:
    
        if (r7.equals("rightVector") != false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01b7, code lost:
    
        if (r7.equals("components") != false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x01e1, code lost:
    
        if (r7.equals("Inverse") != false) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0026, code lost:
    
        if (r7.equals("RightVector") == false) goto L137;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue _init_$lambda$47(java.lang.Object o, java.lang.String k) {
        CFrame cf = (CFrame) o;
        if (k == null) return LuaValue.NIL;
        switch (k) {
            case "Position": case "p": return INSTANCE.wrap(cf.getPosition());
            case "X": case "x": return INSTANCE.n(cf.getX());
            case "Y": case "y": return INSTANCE.n(cf.getY());
            case "Z": case "z": return INSTANCE.n(cf.getZ());
            case "LookVector": case "lookVector": return INSTANCE.wrap(cf.getLookVector());
            case "RightVector": case "rightVector": case "XVector": return INSTANCE.wrap(cf.getRightVector());
            case "UpVector": case "upVector": case "YVector": return INSTANCE.wrap(cf.getUpVector());
            case "ZVector": return INSTANCE.wrap(cf.getBackVector());
            case "Rotation": return INSTANCE.wrap(cf.rotationOnly());
            case "Inverse": case "inverse": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return INSTANCE.wrap(((CFrame) a.arg(1).touserdata()).inverse()); } });
            case "Lerp": case "lerp": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$34(a); } });
            case "ToWorldSpace": case "toWorldSpace": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$35(a); } });
            case "ToObjectSpace": case "toObjectSpace": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$36(a); } });
            case "PointToWorldSpace": case "pointToWorldSpace": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$37(a); } });
            case "PointToObjectSpace": case "pointToObjectSpace": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$38(a); } });
            case "VectorToWorldSpace": case "vectorToWorldSpace": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$39(a); } });
            case "VectorToObjectSpace": case "vectorToObjectSpace": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$40(a); } });
            case "GetComponents": case "components": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$44(a); } });
            case "Orthonormalize": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$45(a); } });
            case "ToEulerAnglesXYZ": case "toEulerAnglesXYZ": case "ToEulerAngles": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$41(a); } });
            case "ToEulerAnglesYXZ": case "toEulerAnglesYXZ": case "ToOrientation": case "toOrientation": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$42(a); } });
            case "ToAxisAngle": case "toAxisAngle": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$47$lambda$46(a); } });
            default: return LuaValue.NIL;
        }
    }

    static final Varargs lambda$47$lambda$33(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        CFrame cFrame = (CFrame) (obj instanceof CFrame ? obj : null);
        if (cFrame != null) {
            return luaTypes.wrap(cFrame.inverse());
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    static final Varargs lambda$47$lambda$34(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        CFrame cFrame2 = (CFrame) (obj2 instanceof CFrame ? obj2 : null);
        if (cFrame2 != null) {
            LuaValue luaValueArg3 = a.arg(3);
            Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
            return luaTypes.wrap(cFrame.lerp(cFrame2, LuaTypesKt.num(luaValueArg3)));
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$47$lambda$35(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        CFrame cFrame2 = (CFrame) (obj2 instanceof CFrame ? obj2 : null);
        if (cFrame2 != null) {
            return luaTypes.wrap(cFrame.times(cFrame2));
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$47$lambda$36(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        CFrame cFrameInverse = cFrame.inverse();
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        CFrame cFrame2 = (CFrame) (obj2 instanceof CFrame ? obj2 : null);
        if (cFrame2 != null) {
            return luaTypes.wrap(cFrameInverse.times(cFrame2));
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$47$lambda$37(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.wrap(cFrame.times(vector3));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$47$lambda$38(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.wrap(cFrame.pointToObject(vector3));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$47$lambda$39(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.wrap(cFrame.vectorToWorld(vector3));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$47$lambda$40(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.wrap(cFrame.vectorToObject(vector3));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$47$lambda$41(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        CFrame cFrame = (CFrame) (obj instanceof CFrame ? obj : null);
        if (cFrame != null) {
            Vector3 e = cFrame.toEulerXYZ();
            Varargs varargsVarargsOf = LuaValue.varargsOf(new LuaValue[]{INSTANCE.n(e.getX()), INSTANCE.n(e.getY()), INSTANCE.n(e.getZ())});
            Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
            return varargsVarargsOf;
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    static final Varargs lambda$47$lambda$42(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        CFrame cFrame = (CFrame) (obj instanceof CFrame ? obj : null);
        if (cFrame != null) {
            Vector3 e = cFrame.toOrientation();
            Varargs varargsVarargsOf = LuaValue.varargsOf(new LuaValue[]{INSTANCE.n(e.getX()), INSTANCE.n(e.getY()), INSTANCE.n(e.getZ())});
            Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
            return varargsVarargsOf;
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    static final Varargs lambda$47$lambda$44(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        CFrame cFrame = (CFrame) (obj instanceof CFrame ? obj : null);
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        float[] fArrComponents = cFrame.components();
        Collection arrayList = new ArrayList(fArrComponents.length);
        for (float f : fArrComponents) {
            arrayList.add(INSTANCE.n(f));
        }
        Varargs varargsVarargsOf = LuaValue.varargsOf((LuaValue[]) ((List) arrayList).toArray(new LuaValue[0]));
        Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
        return varargsVarargsOf;
    }

    static final Varargs lambda$47$lambda$45(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        CFrame cFrame = (CFrame) (obj instanceof CFrame ? obj : null);
        if (cFrame != null) {
            return luaTypes.wrap(cFrame.orthonormalize());
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    static final Varargs lambda$47$lambda$46(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        CFrame cFrame = (CFrame) (obj instanceof CFrame ? obj : null);
        if (cFrame != null) {
            float[] q = cFrame.toQuaternion();
            float ang = ((float) Math.acos(RangesKt.coerceIn(q[3], -1.0f, 1.0f))) * 2.0f;
            float s = (float) Math.sqrt(RangesKt.coerceAtLeast(1.0f - (q[3] * q[3]), 0.0f));
            Vector3 axis = s < 1.0E-5f ? Vector3.INSTANCE.getX() : new Vector3(q[0] / s, q[1] / s, q[2] / s);
            Varargs varargsVarargsOf = LuaValue.varargsOf(INSTANCE.wrap(axis), INSTANCE.n(ang));
            Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
            return varargsVarargsOf;
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    static final Unit _init_$lambda$51(LuaTable meta) {
        Intrinsics.checkNotNullParameter(meta, "$this$meta");
        meta.set(LuaValue.MUL, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda139
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$51$lambda$48((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.ADD, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda140
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$51$lambda$49((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.SUB, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda141
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$51$lambda$50((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        return Unit.INSTANCE;
    }

    static final LuaValue lambda$51$lambda$48(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Object obj = a.isuserdata() ? a.touserdata() : null;
        CFrame c = (CFrame) (obj instanceof CFrame ? obj : null);
        if (c != null) {
            Object o = b.touserdata();
            if (o instanceof CFrame) {
                return INSTANCE.wrap(c.times((CFrame) o));
            }
            if (o instanceof Vector3) {
                return INSTANCE.wrap(c.times((Vector3) o));
            }
            throw new LuaError("invalid argument to CFrame multiplication");
        }
        throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(a));
    }

    static final LuaValue lambda$51$lambda$49(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.wrap(cFrame.plus(vector3));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$51$lambda$50(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        LuaTypes luaTypes = INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof CFrame)) {
            obj = null;
        }
        CFrame cFrame = (CFrame) obj;
        if (cFrame == null) {
            throw new LuaError("CFrame expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector3 != null) {
            return luaTypes.wrap(cFrame.minus(vector3));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(b));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0046, code lost:
    
        if (r4.equals("lerp") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004f, code lost:
    
        if (r4.equals("Lerp") != false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0063, code lost:
    
        if (r4.equals("r") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x006c, code lost:
    
        if (r4.equals("g") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0075, code lost:
    
        if (r4.equals("b") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x007e, code lost:
    
        if (r4.equals("R") != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0080, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getR();
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x008d, code lost:
    
        if (r4.equals("G") != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x008f, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getG();
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x009c, code lost:
    
        if (r4.equals("B") == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x009f, code lost:
    
        r1 = com.goydevv.studrblx.lua.LuaTypes.INSTANCE;
        r2 = r0.getB();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.method(new com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda136());
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:?, code lost:
    
        return r1.n(r2);
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue _init_$lambda$55(java.lang.Object o, java.lang.String k) {
        Color3 c = (Color3) o;
        if (k == null) return LuaValue.NIL;
        switch (k) {
            case "R": case "r": return INSTANCE.n(c.getR());
            case "G": case "g": return INSTANCE.n(c.getG());
            case "B": case "b": return INSTANCE.n(c.getB());
            case "Lerp": case "lerp": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$55$lambda$52(a); } });
            case "ToHSV": case "toHSV": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$55$lambda$53(a); } });
            case "ToHex": case "toHex": return INSTANCE.method(new Function1<Varargs, Varargs>() { @Override public Varargs invoke(Varargs a) { return lambda$55$lambda$54(a); } });
            default: return LuaValue.NIL;
        }
    }

    static final Varargs lambda$55$lambda$52(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Color3)) {
            obj = null;
        }
        Color3 color3 = (Color3) obj;
        if (color3 == null) {
            throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Color3 color4 = (Color3) (obj2 instanceof Color3 ? obj2 : null);
        if (color4 != null) {
            LuaValue luaValueArg3 = a.arg(3);
            Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
            return luaTypes.wrap(color3.lerp(color4, LuaTypesKt.num(luaValueArg3)));
        }
        throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Varargs lambda$55$lambda$53(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        Color3 color3 = (Color3) (obj instanceof Color3 ? obj : null);
        if (color3 != null) {
            float[] h = color3.toHSV();
            Varargs varargsVarargsOf = LuaValue.varargsOf(new LuaValue[]{INSTANCE.n(h[0]), INSTANCE.n(h[1]), INSTANCE.n(h[2])});
            Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
            return varargsVarargsOf;
        }
        throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    static final Varargs lambda$55$lambda$54(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        Color3 color3 = (Color3) (obj instanceof Color3 ? obj : null);
        if (color3 != null) {
            String lowerCase = color3.toHex().toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            LuaString luaStringValueOf = LuaValue.valueOf(lowerCase);
            Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
            return luaStringValueOf;
        }
        throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$56(Object o, String k) {
        LuaTypes luaTypes;
        float b;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        BrickColorV b2 = (BrickColorV) o;
        switch (k.hashCode()) {
            case -1950496919:
                if (k.equals("Number")) {
                    return LuaValue.valueOf(b2.getNumber());
                }
                return null;
            case 98:
                if (k.equals("b")) {
                    luaTypes = INSTANCE;
                    b = b2.getColor().getB();
                    return luaTypes.n(b);
                }
                return null;
            case 103:
                if (k.equals("g")) {
                    luaTypes = INSTANCE;
                    b = b2.getColor().getG();
                    return luaTypes.n(b);
                }
                return null;
            case 114:
                if (k.equals("r")) {
                    luaTypes = INSTANCE;
                    b = b2.getColor().getR();
                    return luaTypes.n(b);
                }
                return null;
            case 2420395:
                if (k.equals("Name")) {
                    return LuaValue.valueOf(b2.getName());
                }
                return null;
            case 65290051:
                if (k.equals("Color")) {
                    return INSTANCE.wrap(b2.getColor());
                }
                return null;
            default:
                return null;
        }
    }

    static final LuaValue _init_$lambda$57(Object o, String k) {
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        UDim u = (UDim) o;
        if (Intrinsics.areEqual(k, "Scale")) {
            return INSTANCE.n(u.getScale());
        }
        if (Intrinsics.areEqual(k, "Offset")) {
            return LuaValue.valueOf(u.getOffset());
        }
        return null;
    }

    static final Unit _init_$lambda$60(LuaTable meta) {
        Intrinsics.checkNotNullParameter(meta, "$this$meta");
        meta.set(LuaValue.ADD, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda46
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$60$lambda$58((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.SUB, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda47
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$60$lambda$59((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        return Unit.INSTANCE;
    }

    static final LuaValue lambda$60$lambda$58(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof UDim)) {
            obj = null;
        }
        UDim x = (UDim) obj;
        if (x == null) {
            throw new LuaError("UDim expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        UDim y = (UDim) (obj2 instanceof UDim ? obj2 : null);
        if (y != null) {
            return INSTANCE.wrap(new UDim(x.getScale() + y.getScale(), x.getOffset() + y.getOffset()));
        }
        throw new LuaError("UDim expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$60$lambda$59(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof UDim)) {
            obj = null;
        }
        UDim x = (UDim) obj;
        if (x == null) {
            throw new LuaError("UDim expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        UDim y = (UDim) (obj2 instanceof UDim ? obj2 : null);
        if (y != null) {
            return INSTANCE.wrap(new UDim(x.getScale() - y.getScale(), x.getOffset() - y.getOffset()));
        }
        throw new LuaError("UDim expected, got " + INSTANCE.typeOf(b));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0038, code lost:
    
        if (r6.equals("Y") == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0041, code lost:
    
        if (r6.equals("X") != false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x005d, code lost:
    
        if (r6.equals("Height") == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(new com.goydevv.studrblx.model.UDim(r0.getXs(), r0.getXo()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:?, code lost:
    
        return com.goydevv.studrblx.lua.LuaTypes.INSTANCE.wrap(new com.goydevv.studrblx.model.UDim(r0.getYs(), r0.getYo()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x001b, code lost:
    
        if (r6.equals("Width") != false) goto L16;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue _init_$lambda$62(java.lang.Object r5, java.lang.String r6) {
        Intrinsics.checkNotNullParameter(r5, "o");
        Intrinsics.checkNotNullParameter(r6, "k");
        UDim2 u = (UDim2) r5;
        switch (r6) {
            case "X":
            case "Width":
                return INSTANCE.wrap(new UDim(u.getXs(), u.getXo()));
            case "Y":
            case "Height":
                return INSTANCE.wrap(new UDim(u.getYs(), u.getYo()));
            case "Lerp":
                return INSTANCE.method(new Function1() {
                    @Override
                    public final Object invoke(Object a) {
                        return LuaTypes.lambda$62$lambda$61((Varargs) a);
                    }
                });
            default:
                return null;
        }
    }

    static final Varargs lambda$62$lambda$61(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof UDim2)) {
            obj = null;
        }
        UDim2 uDim2 = (UDim2) obj;
        if (uDim2 == null) {
            throw new LuaError("UDim2 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        UDim2 uDim3 = (UDim2) (obj2 instanceof UDim2 ? obj2 : null);
        if (uDim3 != null) {
            LuaValue luaValueArg3 = a.arg(3);
            Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
            return luaTypes.wrap(uDim2.lerp(uDim3, LuaTypesKt.num(luaValueArg3)));
        }
        throw new LuaError("UDim2 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Unit _init_$lambda$66(LuaTable meta) {
        Intrinsics.checkNotNullParameter(meta, "$this$meta");
        meta.set(LuaValue.ADD, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda103
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$66$lambda$63((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.SUB, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda104
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.lambda$66$lambda$64((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        meta.set(LuaValue.UNM, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda105
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.lambda$66$lambda$65((LuaValue) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final LuaValue lambda$66$lambda$63(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof UDim2)) {
            obj = null;
        }
        UDim2 x = (UDim2) obj;
        if (x == null) {
            throw new LuaError("UDim2 expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        UDim2 y = (UDim2) (obj2 instanceof UDim2 ? obj2 : null);
        if (y != null) {
            return INSTANCE.wrap(new UDim2(x.getXs() + y.getXs(), x.getXo() + y.getXo(), x.getYs() + y.getYs(), x.getYo() + y.getYo()));
        }
        throw new LuaError("UDim2 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$66$lambda$64(LuaValue a, LuaValue b) {
        Intrinsics.checkNotNullParameter(a, "a");
        Intrinsics.checkNotNullParameter(b, "b");
        Object obj = a.isuserdata() ? a.touserdata() : null;
        if (!(obj instanceof UDim2)) {
            obj = null;
        }
        UDim2 x = (UDim2) obj;
        if (x == null) {
            throw new LuaError("UDim2 expected, got " + INSTANCE.typeOf(a));
        }
        Object obj2 = b.isuserdata() ? b.touserdata() : null;
        UDim2 y = (UDim2) (obj2 instanceof UDim2 ? obj2 : null);
        if (y != null) {
            return INSTANCE.wrap(new UDim2(x.getXs() - y.getXs(), x.getXo() - y.getXo(), x.getYs() - y.getYs(), x.getYo() - y.getYo()));
        }
        throw new LuaError("UDim2 expected, got " + INSTANCE.typeOf(b));
    }

    static final LuaValue lambda$66$lambda$65(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        Object obj = a.isuserdata() ? a.touserdata() : null;
        UDim2 x = (UDim2) (obj instanceof UDim2 ? obj : null);
        if (x != null) {
            return INSTANCE.wrap(new UDim2(-x.getXs(), -x.getXo(), -x.getYs(), -x.getYo()));
        }
        throw new LuaError("UDim2 expected, got " + INSTANCE.typeOf(a));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$68(Object o, String k) {
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        final EnumItemV e = (EnumItemV) o;
        switch (k.hashCode()) {
            case -2006084613:
                if (k.equals("EnumType")) {
                    return INSTANCE.wrap(new EnumTypeV(e.getType()));
                }
                return null;
            case 73783:
                if (k.equals("IsA")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda135
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$68$lambda$67(e, (Varargs) obj);
                        }
                    });
                }
                return null;
            case 2420395:
                if (k.equals("Name")) {
                    return LuaValue.valueOf(e.getName());
                }
                return null;
            case 82420049:
                if (k.equals("Value")) {
                    return LuaValue.valueOf(e.getValue());
                }
                return null;
            default:
                return null;
        }
    }

    static final Varargs lambda$68$lambda$67(EnumItemV $e, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaBoolean luaBooleanValueOf = LuaValue.valueOf(Intrinsics.areEqual(a.arg(2).tojstring(), $e.getType()));
        Intrinsics.checkNotNullExpressionValue(luaBooleanValueOf, "valueOf(...)");
        return luaBooleanValueOf;
    }

    static final Unit _init_$lambda$70(LuaTable meta) {
        Intrinsics.checkNotNullParameter(meta, "$this$meta");
        meta.set(LuaValue.TOSTRING, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda133
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.lambda$70$lambda$69((LuaValue) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final LuaValue lambda$70$lambda$69(LuaValue it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Object obj = it.touserdata();
        Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type com.goydevv.studrblx.lua.EnumItemV");
        EnumItemV e = (EnumItemV) obj;
        LuaString luaStringValueOf = LuaValue.valueOf("Enum." + e.getType() + "." + e.getName());
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$76(Object o, String k) {
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        final EnumTypeV t = (EnumTypeV) o;
        switch (k.hashCode()) {
            case -1180203659:
                if (k.equals("FromName")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda149
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$76$lambda$73(t, (Varargs) obj);
                        }
                    });
                }
                break;
            case 376316073:
                if (k.equals("GetEnumItems")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda148
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$76$lambda$72(t, (Varargs) obj);
                        }
                    });
                }
                break;
            case 2075780039:
                if (k.equals("FromValue")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda150
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$76$lambda$75(t, (Varargs) obj);
                        }
                    });
                }
                break;
        }
        return INSTANCE.enumItem(t.getName(), k);
    }

    static final Varargs lambda$76$lambda$72(EnumTypeV $t, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        LuaTable tbl = new LuaTable();
        Map map = (LinkedHashMap) Enums.INSTANCE.getTypes().get($t.getName());
        if (map != null) {
            for (Map.Entry<String, Integer> entry : ((Map<String, Integer>) map).entrySet()) {
                tbl.insert(0, INSTANCE.wrap(new EnumItemV($t.getName(), entry.getKey(), entry.getValue().intValue())));
            }
        }
        return tbl;
    }

    static final Varargs lambda$76$lambda$73(EnumTypeV $t, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        String name = $t.getName();
        String str = a.arg(2).tojstring();
        Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
        LuaValue NIL = luaTypes.enumItem(name, str);
        if (NIL == null) {
            NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
        }
        return NIL;
    }

    static final Varargs lambda$76$lambda$75(EnumTypeV $t, Varargs a) {
        LuaValue NIL;
        Intrinsics.checkNotNullParameter(a, "a");
        String strItemName = Enums.INSTANCE.itemName($t.getName(), a.arg(2).toint());
        if (strItemName == null || (NIL = INSTANCE.wrap(new EnumItemV($t.getName(), strItemName, a.arg(2).toint()))) == null) {
            NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
        }
        return NIL;
    }

    static final Unit _init_$lambda$78(LuaTable meta) {
        Intrinsics.checkNotNullParameter(meta, "$this$meta");
        meta.set(LuaValue.TOSTRING, LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda102
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.lambda$78$lambda$77((LuaValue) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final LuaValue lambda$78$lambda$77(LuaValue it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Object obj = it.touserdata();
        Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type com.goydevv.studrblx.lua.EnumTypeV");
        LuaString luaStringValueOf = LuaValue.valueOf(((EnumTypeV) obj).getName());
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$79(Object o, String k) {
        LuaTypes luaTypes;
        float time;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        TweenInfoV t = (TweenInfoV) o;
        switch (k.hashCode()) {
            case -1681309068:
                if (k.equals("EasingDirection")) {
                    LuaTypes luaTypes2 = INSTANCE;
                    String strItemName = Enums.INSTANCE.itemName("EasingDirection", t.getDirection());
                    if (strItemName == null) {
                        strItemName = "Out";
                    }
                    return luaTypes2.wrap(new EnumItemV("EasingDirection", strItemName, t.getDirection()));
                }
                return null;
            case -1055579116:
                if (k.equals("RepeatCount")) {
                    return LuaValue.valueOf(t.getRepeatCount());
                }
                return null;
            case -199856655:
                if (k.equals("Reverses")) {
                    return LuaValue.valueOf(t.getReverses());
                }
                return null;
            case 2606829:
                if (k.equals("Time")) {
                    luaTypes = INSTANCE;
                    time = t.getTime();
                    return luaTypes.n(time);
                }
                return null;
            case 1460280102:
                if (k.equals("EasingStyle")) {
                    LuaTypes luaTypes3 = INSTANCE;
                    String strItemName2 = Enums.INSTANCE.itemName("EasingStyle", t.getStyle());
                    if (strItemName2 == null) {
                        strItemName2 = "Quad";
                    }
                    return luaTypes3.wrap(new EnumItemV("EasingStyle", strItemName2, t.getStyle()));
                }
                return null;
            case 1534863056:
                if (k.equals("DelayTime")) {
                    luaTypes = INSTANCE;
                    time = t.getDelay();
                    return luaTypes.n(time);
                }
                return null;
            default:
                return null;
        }
    }

    static final LuaValue _init_$lambda$80(Object o, String k) {
        LuaTypes luaTypes;
        float max;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        NumberRange r = (NumberRange) o;
        if (Intrinsics.areEqual(k, "Min")) {
            luaTypes = INSTANCE;
            max = r.getMin();
        } else {
            if (!Intrinsics.areEqual(k, "Max")) {
                return null;
            }
            luaTypes = INSTANCE;
            max = r.getMax();
        }
        return luaTypes.n(max);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$82(Object o, String k) {
        LuaTypes luaTypes;
        Object origin;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        final Ray r = (Ray) o;
        switch (k.hashCode()) {
            case -1924752570:
                if (k.equals("Origin")) {
                    luaTypes = INSTANCE;
                    origin = r.getOrigin();
                    return luaTypes.wrap(origin);
                }
                return null;
            case -463439145:
                if (k.equals("ClosestPoint")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$82$lambda$81(r, (Varargs) obj);
                        }
                    });
                }
                return null;
            case 2641316:
                if (k.equals("Unit")) {
                    luaTypes = INSTANCE;
                    origin = new Ray(r.getOrigin(), r.getDirection().getUnit());
                    return luaTypes.wrap(origin);
                }
                return null;
            case 1041377119:
                if (k.equals("Direction")) {
                    luaTypes = INSTANCE;
                    origin = r.getDirection();
                    return luaTypes.wrap(origin);
                }
                return null;
            default:
                return null;
        }
    }

    static final Varargs lambda$82$lambda$81(Ray $r, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        Vector3 p = (Vector3) (obj instanceof Vector3 ? obj : null);
        if (p != null) {
            Vector3 d = $r.getDirection().getUnit();
            return INSTANCE.wrap($r.getOrigin().plus(d.times(RangesKt.coerceAtLeast(p.minus($r.getOrigin()).dot(d), 0.0f))));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$84(Object o, String k) {
        LuaTypes luaTypes;
        Object size;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        final Region3V r = (Region3V) o;
        switch (k.hashCode()) {
            case -831874117:
                if (k.equals("ExpandToGrid")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda134
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$84$lambda$83(r, (Varargs) obj);
                        }
                    });
                }
                return null;
            case 2577441:
                if (k.equals("Size")) {
                    luaTypes = INSTANCE;
                    size = r.getSize();
                    return luaTypes.wrap(size);
                }
                return null;
            case 1986292458:
                if (k.equals("CFrame")) {
                    luaTypes = INSTANCE;
                    size = r.getCframe();
                    return luaTypes.wrap(size);
                }
                return null;
            default:
                return null;
        }
    }

    static final Varargs lambda$84$lambda$83(Region3V $r, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float res = RangesKt.coerceAtLeast(LuaTypesKt.num(luaValueArg), 0.001f);
        return INSTANCE.wrap(new Region3V(new Vector3(lambda$84$lambda$83$lo(res, $r.getMin().getX()), lambda$84$lambda$83$lo(res, $r.getMin().getY()), lambda$84$lambda$83$lo(res, $r.getMin().getZ())), new Vector3(lambda$84$lambda$83$hi(res, $r.getMax().getX()), lambda$84$lambda$83$hi(res, $r.getMax().getY()), lambda$84$lambda$83$hi(res, $r.getMax().getZ()))));
    }

    private static final float lambda$84$lambda$83$lo(float res, float x) {
        return ((float) Math.floor(x / res)) * res;
    }

    private static final float lambda$84$lambda$83$hi(float res, float x) {
        return ((float) Math.ceil(x / res)) * res;
    }

    static final LuaValue _init_$lambda$85(Object o, String k) {
        LuaTypes luaTypes;
        Vector3int16 max;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        Region3int16V r = (Region3int16V) o;
        if (Intrinsics.areEqual(k, "Min")) {
            luaTypes = INSTANCE;
            max = r.getMin();
        } else {
            if (!Intrinsics.areEqual(k, "Max")) {
                return null;
            }
            luaTypes = INSTANCE;
            max = r.getMax();
        }
        return luaTypes.wrap(max);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$89(Object o, String k) {
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        final Random r = ((RandomV) o).getRng();
        switch (k.hashCode()) {
            case -64826534:
                if (k.equals("NextUnitVector")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda64
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$89$lambda$88(r, (Varargs) obj);
                        }
                    });
                }
                return null;
            case 587066428:
                if (k.equals("NextNumber")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda62
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$89$lambda$86(r, (Varargs) obj);
                        }
                    });
                }
                return null;
            case 682790571:
                if (k.equals("NextInteger")) {
                    return INSTANCE.method(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda63
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return LuaTypes.lambda$89$lambda$87(r, (Varargs) obj);
                        }
                    });
                }
                return null;
            default:
                return null;
        }
    }

    static final Varargs lambda$89$lambda$86(Random $r, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        double mn = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        double mx = LuaTypesKt.optNum(luaValueArg2, 1.0f);
        LuaNumber luaNumberValueOf = LuaValue.valueOf(($r.nextDouble() * (mx - mn)) + mn);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        return luaNumberValueOf;
    }

    static final Varargs lambda$89$lambda$87(Random $r, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        int mn = a.arg(2).checkint();
        int mx = a.arg(3).checkint();
        LuaInteger luaIntegerValueOf = LuaValue.valueOf($r.nextInt(Math.max(1, (mx - mn) + 1)) + mn);
        Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
        return luaIntegerValueOf;
    }

    static final Varargs lambda$89$lambda$88(Random $r, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        return INSTANCE.wrap(new Vector3((float) $r.nextGaussian(), (float) $r.nextGaussian(), (float) $r.nextGaussian()).getUnit());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$90(Object o, String k) {
        LuaTypes luaTypes;
        Vector3 normal;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        RayHitInfo h = ((RaycastResultV) o).getHit();
        if (k.equals("Instance")) {
            LuaRuntime hrt = ((RaycastResultV) o).getRt();
            return (hrt == null || h.getInst() == null) ? LuaValue.NIL : hrt.wrap(h.getInst());
        }
        switch (k.hashCode()) {
            case -1955878649:
                if (k.equals("Normal")) {
                    luaTypes = INSTANCE;
                    normal = h.getNormal();
                    return luaTypes.wrap(normal);
                }
                return null;
            case 353103893:
                if (k.equals("Distance")) {
                    return INSTANCE.n(h.getDistance());
                }
                return null;
            case 363710791:
                if (k.equals("Material")) {
                    LuaTypes luaTypes2 = INSTANCE;
                    String strItemName = Enums.INSTANCE.itemName("Material", h.getMaterial());
                    if (strItemName == null) {
                        strItemName = "Plastic";
                    }
                    return luaTypes2.wrap(new EnumItemV("Material", strItemName, h.getMaterial()));
                }
                return null;
            case 812449097:
                if (k.equals("Position")) {
                    luaTypes = INSTANCE;
                    normal = h.getPosition();
                    return luaTypes.wrap(normal);
                }
                return null;
            default:
                return null;
        }
    }

    static final LuaValue _init_$lambda$93(Object o, String k) {
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        NumberSequence s = (NumberSequence) o;
        if (!Intrinsics.areEqual(k, "Keypoints")) {
            return null;
        }
        LuaTable luaTable = new LuaTable();
        Iterator it = s.getKeys().iterator();
        while (it.hasNext()) {
            luaTable.insert(0, INSTANCE.wrap((NumberKey) it.next()));
        }
        return luaTable;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    static final LuaValue _init_$lambda$94(Object o, String k) {
        LuaTypes luaTypes;
        float envelope;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        NumberKey kp = (NumberKey) o;
        switch (k) {
            case "Envelope":
                luaTypes = INSTANCE;
                envelope = kp.getEnvelope();
                return luaTypes.n(envelope);
            case "Time":
                luaTypes = INSTANCE;
                envelope = kp.getTime();
                return luaTypes.n(envelope);
            case "Value":
                luaTypes = INSTANCE;
                envelope = kp.getValue();
                return luaTypes.n(envelope);
            default:
                if (k.equals("Envelope")) {
                    luaTypes = INSTANCE;
                    envelope = kp.getEnvelope();
                    return luaTypes.n(envelope);
                }
                return null;
        }
    }

    static final LuaValue _init_$lambda$97(Object o, String k) {
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        ColorSequence s = (ColorSequence) o;
        if (!Intrinsics.areEqual(k, "Keypoints")) {
            return null;
        }
        LuaTable luaTable = new LuaTable();
        Iterator it = s.getKeys().iterator();
        while (it.hasNext()) {
            luaTable.insert(0, INSTANCE.wrap((ColorKey) it.next()));
        }
        return luaTable;
    }

    static final LuaValue _init_$lambda$98(Object o, String k) {
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        ColorKey kp = (ColorKey) o;
        if (Intrinsics.areEqual(k, "Time")) {
            return INSTANCE.n(kp.getTime());
        }
        if (Intrinsics.areEqual(k, "Value")) {
            return INSTANCE.wrap(kp.getColor());
        }
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static final LuaValue _init_$lambda$99(Object o, String k) {
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        FontValue f = (FontValue) o;
        switch (k.hashCode()) {
            case -1707725160:
                if (k.equals("Weight")) {
                    return INSTANCE.wrap(new EnumItemV("FontWeight", String.valueOf(f.getWeight()), f.getWeight()));
                }
                return null;
            case 2076325:
                if (k.equals("Bold")) {
                    return LuaValue.valueOf(f.getWeight() >= 700);
                }
                return null;
            case 80227729:
                if (k.equals("Style")) {
                    return INSTANCE.wrap(new EnumItemV("FontStyle", f.getStyle() == 1 ? "Italic" : "Normal", f.getStyle()));
                }
                return null;
            case 2096973700:
                if (k.equals("Family")) {
                    return LuaValue.valueOf(f.getFamily());
                }
                return null;
            default:
                return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    static final LuaValue _init_$lambda$100(Object o, String k) {
        LuaTypes luaTypes;
        float maxY;
        float minY;
        LuaTypes luaTypes2;
        Vector2 vector2;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        Rect r = (Rect) o;
        switch (k) {
            case "Height":
                luaTypes = INSTANCE;
                maxY = r.getMaxY();
                minY = r.getMinY();
                return luaTypes.n(maxY - minY);
            case "Max":
                luaTypes2 = INSTANCE;
                vector2 = new Vector2(r.getMaxX(), r.getMaxY());
                return luaTypes2.wrap(vector2);
            case "Min":
                luaTypes2 = INSTANCE;
                vector2 = new Vector2(r.getMinX(), r.getMinY());
                return luaTypes2.wrap(vector2);
            case "Width":
                luaTypes = INSTANCE;
                maxY = r.getMaxX();
                minY = r.getMinX();
                return luaTypes.n(maxY - minY);
            default:
                if (k.equals("Height")) {
                    luaTypes = INSTANCE;
                    maxY = r.getMaxY();
                    minY = r.getMinY();
                    return luaTypes.n(maxY - minY);
                }
                return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    static final LuaValue _init_$lambda$101(Object o, String k) {
        LuaTypes luaTypes;
        float frictionWeight;
        LuaTypes luaTypes2;
        float friction;
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(k, "k");
        PhysicalProps p = (PhysicalProps) o;
        switch (k) {
            case "FrictionWeight":
                luaTypes = INSTANCE;
                frictionWeight = p.getFrictionWeight();
                return luaTypes.n(frictionWeight);
            case "Friction":
                luaTypes2 = INSTANCE;
                friction = p.getFriction();
                return luaTypes2.n(friction);
            case "Density":
                luaTypes2 = INSTANCE;
                friction = p.getDensity();
                return luaTypes2.n(friction);
            case "ElasticityWeight":
                luaTypes = INSTANCE;
                frictionWeight = p.getElasticityWeight();
                return luaTypes.n(frictionWeight);
            case "Elasticity":
                luaTypes2 = INSTANCE;
                friction = p.getElasticity();
                return luaTypes2.n(friction);
            default:
                if (k.equals("FrictionWeight")) {
                    luaTypes = INSTANCE;
                    frictionWeight = p.getFrictionWeight();
                    return luaTypes.n(frictionWeight);
                }
                return null;
        }
    }

    public final LuaValue enumItem(String type, String name) {
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(name, "name");
        LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get(type);
        Integer v = linkedHashMap != null ? linkedHashMap.get(name) : null;
        if (v != null) {
            return wrap(new EnumItemV(type, name, v.intValue()));
        }
        if ((name.length() == 0) || !Character.isLetter(name.charAt(0))) {
            return null;
        }
        return wrap(new EnumItemV(type, name, (name.hashCode() & 65535) | 65536));
    }

    public final LuaValue enumLib() {
        LuaTable mt = new LuaTable();
        mt.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda107
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return LuaTypes.enumLib$lambda$102((LuaValue) obj, (LuaValue) obj2);
            }
        }));
        LuaTable t = new LuaTable();
        t.set("GetEnums", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda108
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.enumLib$lambda$105((Varargs) obj);
            }
        }));
        t.setmetatable(mt);
        return t;
    }

    static final LuaValue enumLib$lambda$102(LuaValue luaValue, LuaValue k) {
        Intrinsics.checkNotNullParameter(luaValue, "<unused var>");
        Intrinsics.checkNotNullParameter(k, "k");
        LuaTypes luaTypes = INSTANCE;
        String str = k.tojstring();
        Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
        return luaTypes.wrap(new EnumTypeV(str));
    }

    static final Varargs enumLib$lambda$105(Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        LuaTable luaTable = new LuaTable();
        Iterator it = Enums.INSTANCE.getTypes().keySet().iterator();
        while (it.hasNext()) {
            luaTable.insert(0, INSTANCE.wrap(new EnumTypeV((String) it.next())));
        }
        return luaTable;
    }

    private static final void install$lib(LuaTable $g, String name, Function1<? super LuaTable, Unit> function1) {
        LuaTable luaTable = new LuaTable();
        function1.invoke(luaTable);
        $g.set(name, luaTable);
    }

    public final void install(LuaTable g) {
        Intrinsics.checkNotNullParameter(g, "g");
        install$lib(g, "Vector3", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda75
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$109((LuaTable) obj);
            }
        });
        install$lib(g, "Vector2", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda86
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$111((LuaTable) obj);
            }
        });
        install$lib(g, "Vector3int16", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda89
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$113((LuaTable) obj);
            }
        });
        install$lib(g, "Content", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda90
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$117((LuaTable) obj);
            }
        });
        install$lib(g, "CFrame", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda91
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126((LuaTable) obj);
            }
        });
        install$lib(g, "Color3", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda92
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$132((LuaTable) obj);
            }
        });
        install$lib(g, "BrickColor", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda93
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$137((LuaTable) obj);
            }
        });
        install$lib(g, "UDim", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda94
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$139((LuaTable) obj);
            }
        });
        install$lib(g, "UDim2", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda95
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$143((LuaTable) obj);
            }
        });
        install$lib(g, "TweenInfo", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda96
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$145((LuaTable) obj);
            }
        });
        install$lib(g, "Ray", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda76
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$147((LuaTable) obj);
            }
        });
        install$lib(g, "Region3", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda77
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$149((LuaTable) obj);
            }
        });
        install$lib(g, "Region3int16", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda78
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$151((LuaTable) obj);
            }
        });
        install$lib(g, "NumberRange", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda79
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$153((LuaTable) obj);
            }
        });
        install$lib(g, "NumberSequenceKeypoint", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda80
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$155((LuaTable) obj);
            }
        });
        install$lib(g, "ColorSequenceKeypoint", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda81
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$157((LuaTable) obj);
            }
        });
        install$lib(g, "NumberSequence", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda82
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$160((LuaTable) obj);
            }
        });
        install$lib(g, "ColorSequence", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda83
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$163((LuaTable) obj);
            }
        });
        install$lib(g, "Random", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda84
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$165((LuaTable) obj);
            }
        });
        install$lib(g, "Rect", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda85
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$167((LuaTable) obj);
            }
        });
        install$lib(g, "PhysicalProperties", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda87
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$169((LuaTable) obj);
            }
        });
        install$lib(g, "Font", new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda88
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$173((LuaTable) obj);
            }
        });
        g.set("Enum", enumLib());
    }

    static final Unit install$lambda$109(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda66
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$109$lambda$106((Varargs) obj);
            }
        }));
        lib.set("zero", INSTANCE.wrap(Vector3.INSTANCE.getZERO()));
        lib.set("one", INSTANCE.wrap(Vector3.INSTANCE.getONE()));
        lib.set("xAxis", INSTANCE.wrap(Vector3.INSTANCE.getX()));
        lib.set("yAxis", INSTANCE.wrap(Vector3.INSTANCE.getY()));
        lib.set("zAxis", INSTANCE.wrap(Vector3.INSTANCE.getZ()));
        lib.set("FromNormalId", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda67
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$109$lambda$107((LuaValue) obj);
            }
        }));
        lib.set("FromAxis", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda68
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$109$lambda$108((LuaValue) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$109$lambda$106(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fOptNum2 = LuaTypesKt.optNum(luaValueArg2, 0.0f);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        return luaTypes.wrap(new Vector3(fOptNum, fOptNum2, LuaTypesKt.optNum(luaValueArg3, 0.0f)));
    }

    static final LuaValue install$lambda$109$lambda$107(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        Object obj = a.isuserdata() ? a.touserdata() : null;
        EnumItemV enumItemV = (EnumItemV) (obj instanceof EnumItemV ? obj : null);
        int i = enumItemV != null ? enumItemV.getValue() : a.toint();
        return INSTANCE.wrap(CollectionsKt.listOf(new Vector3[]{Vector3.INSTANCE.getX(), Vector3.INSTANCE.getY(), Vector3.INSTANCE.getZ(), Vector3.INSTANCE.getX().unaryMinus(), Vector3.INSTANCE.getY().unaryMinus(), Vector3.INSTANCE.getZ().unaryMinus()}).get(RangesKt.coerceIn(i, 0, 5)));
    }

    static final LuaValue install$lambda$109$lambda$108(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        Vector3.Companion companion = Vector3.INSTANCE;
        Object obj = a.isuserdata() ? a.touserdata() : null;
        EnumItemV enumItemV = (EnumItemV) (obj instanceof EnumItemV ? obj : null);
        return luaTypes.wrap(companion.axis(enumItemV != null ? enumItemV.getValue() : a.toint()));
    }

    static final Unit install$lambda$111(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda69
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$111$lambda$110((Varargs) obj);
            }
        }));
        lib.set("zero", INSTANCE.wrap(new Vector2(0.0f, 0.0f)));
        lib.set("one", INSTANCE.wrap(new Vector2(1.0f, 1.0f)));
        lib.set("xAxis", INSTANCE.wrap(new Vector2(1.0f, 0.0f)));
        lib.set("yAxis", INSTANCE.wrap(new Vector2(0.0f, 1.0f)));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$111$lambda$110(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        return luaTypes.wrap(new Vector2(fOptNum, LuaTypesKt.optNum(luaValueArg2, 0.0f)));
    }

    static final Unit install$lambda$113(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$113$lambda$112((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$113$lambda$112(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.wrap(new Vector3int16(a.arg(1).optint(0), a.arg(2).optint(0), a.arg(3).optint(0)));
    }

    static final Unit install$lambda$117(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("none", LuaValue.valueOf(""));
        lib.set("fromUri", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda54
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$117$lambda$114((LuaValue) obj);
            }
        }));
        lib.set("fromAssetId", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda55
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$117$lambda$115((LuaValue) obj);
            }
        }));
        lib.set("fromObject", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda56
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$117$lambda$116((LuaValue) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final LuaValue install$lambda$117$lambda$114(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaString luaStringValueOf = LuaValue.valueOf(a.optjstring(""));
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    static final LuaValue install$lambda$117$lambda$115(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaString luaStringValueOf = LuaValue.valueOf("rbxassetid://" + a.optlong(0L));
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        return luaStringValueOf;
    }

    static final LuaValue install$lambda$117$lambda$116(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return a;
    }

    static final Unit install$lambda$126(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126$lambda$118((Varargs) obj);
            }
        }));
        VarArgFunction angles = LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126$lambda$119((Varargs) obj);
            }
        });
        lib.set("Angles", angles);
        lib.set("fromEulerAnglesXYZ", angles);
        VarArgFunction orient = LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126$lambda$120((Varargs) obj);
            }
        });
        lib.set("fromOrientation", orient);
        lib.set("fromEulerAnglesYXZ", orient);
        lib.set("fromEulerAngles", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126$lambda$121((Varargs) obj);
            }
        }));
        lib.set("lookAt", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126$lambda$122((Varargs) obj);
            }
        }));
        lib.set("lookAlong", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126$lambda$123((Varargs) obj);
            }
        }));
        lib.set("fromAxisAngle", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126$lambda$124((Varargs) obj);
            }
        }));
        lib.set("fromMatrix", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$126$lambda$125((Varargs) obj);
            }
        }));
        lib.set("identity", INSTANCE.wrap(CFrame.INSTANCE.getIDENTITY()));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$126$lambda$118(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        int n = a.narg();
        if (n == 0) {
            return INSTANCE.wrap(CFrame.INSTANCE.getIDENTITY());
        }
        if (n != 1) {
            if (n != 2 || !a.arg(1).isuserdata()) {
                if (n == 3) {
                    LuaTypes luaTypes = INSTANCE;
                    LuaValue luaValueArg = a.arg(1);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
                    float fNum = LuaTypesKt.num(luaValueArg);
                    LuaValue luaValueArg2 = a.arg(2);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
                    float fNum2 = LuaTypesKt.num(luaValueArg2);
                    LuaValue luaValueArg3 = a.arg(3);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
                    return luaTypes.wrap(new CFrame(fNum, fNum2, LuaTypesKt.num(luaValueArg3), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null));
                }
                if (n == 7) {
                    LuaTypes luaTypes2 = INSTANCE;
                    CFrame.Companion companion = CFrame.INSTANCE;
                    LuaValue luaValueArg4 = a.arg(1);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg4, "arg(...)");
                    float fNum3 = LuaTypesKt.num(luaValueArg4);
                    LuaValue luaValueArg5 = a.arg(2);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg5, "arg(...)");
                    float fNum4 = LuaTypesKt.num(luaValueArg5);
                    LuaValue luaValueArg6 = a.arg(3);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg6, "arg(...)");
                    Vector3 vector3 = new Vector3(fNum3, fNum4, LuaTypesKt.num(luaValueArg6));
                    LuaValue luaValueArg7 = a.arg(4);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg7, "arg(...)");
                    float fNum5 = LuaTypesKt.num(luaValueArg7);
                    LuaValue luaValueArg8 = a.arg(5);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg8, "arg(...)");
                    float fNum6 = LuaTypesKt.num(luaValueArg8);
                    LuaValue luaValueArg9 = a.arg(6);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg9, "arg(...)");
                    float fNum7 = LuaTypesKt.num(luaValueArg9);
                    LuaValue luaValueArg10 = a.arg(7);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg10, "arg(...)");
                    return luaTypes2.wrap(companion.fromQuaternion(vector3, fNum5, fNum6, fNum7, LuaTypesKt.num(luaValueArg10)));
                }
                if (n >= 12) {
                    LuaTypes luaTypes3 = INSTANCE;
                    LuaValue luaValueArg11 = a.arg(1);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg11, "arg(...)");
                    float fNum8 = LuaTypesKt.num(luaValueArg11);
                    LuaValue luaValueArg12 = a.arg(2);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg12, "arg(...)");
                    float fNum9 = LuaTypesKt.num(luaValueArg12);
                    LuaValue luaValueArg13 = a.arg(3);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg13, "arg(...)");
                    float fNum10 = LuaTypesKt.num(luaValueArg13);
                    LuaValue luaValueArg14 = a.arg(4);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg14, "arg(...)");
                    float fNum11 = LuaTypesKt.num(luaValueArg14);
                    LuaValue luaValueArg15 = a.arg(5);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg15, "arg(...)");
                    float fNum12 = LuaTypesKt.num(luaValueArg15);
                    LuaValue luaValueArg16 = a.arg(6);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg16, "arg(...)");
                    float fNum13 = LuaTypesKt.num(luaValueArg16);
                    LuaValue luaValueArg17 = a.arg(7);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg17, "arg(...)");
                    float fNum14 = LuaTypesKt.num(luaValueArg17);
                    LuaValue luaValueArg18 = a.arg(8);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg18, "arg(...)");
                    float fNum15 = LuaTypesKt.num(luaValueArg18);
                    LuaValue luaValueArg19 = a.arg(9);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg19, "arg(...)");
                    float fNum16 = LuaTypesKt.num(luaValueArg19);
                    LuaValue luaValueArg20 = a.arg(10);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg20, "arg(...)");
                    float fNum17 = LuaTypesKt.num(luaValueArg20);
                    LuaValue luaValueArg21 = a.arg(11);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg21, "arg(...)");
                    float fNum18 = LuaTypesKt.num(luaValueArg21);
                    LuaValue luaValueArg22 = a.arg(12);
                    Intrinsics.checkNotNullExpressionValue(luaValueArg22, "arg(...)");
                    return luaTypes3.wrap(new CFrame(fNum8, fNum9, fNum10, fNum11, fNum12, fNum13, fNum14, fNum15, fNum16, fNum17, fNum18, LuaTypesKt.num(luaValueArg22)));
                }
                throw new LuaError("Invalid number of arguments: " + n);
            }
            LuaTypes luaTypes4 = INSTANCE;
            CFrame.Companion companion2 = CFrame.INSTANCE;
            LuaValue luaValueArg23 = a.arg(1);
            Intrinsics.checkNotNullExpressionValue(luaValueArg23, "arg(...)");
            Object obj = luaValueArg23.isuserdata() ? luaValueArg23.touserdata() : null;
            if (!(obj instanceof Vector3)) {
                obj = null;
            }
            Vector3 vector4 = (Vector3) obj;
            if (vector4 == null) {
                throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg23));
            }
            LuaValue luaValueArg24 = a.arg(2);
            Intrinsics.checkNotNullExpressionValue(luaValueArg24, "arg(...)");
            Object obj2 = luaValueArg24.isuserdata() ? luaValueArg24.touserdata() : null;
            Vector3 vector5 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
            if (vector5 != null) {
                return luaTypes4.wrap(CFrame.Companion.lookAt$default(companion2, vector4, vector5, null, 4, null));
            }
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg24));
        }
        LuaTypes luaTypes5 = INSTANCE;
        CFrame.Companion companion3 = CFrame.INSTANCE;
        LuaValue luaValueArg25 = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg25, "arg(...)");
        Object obj3 = luaValueArg25.isuserdata() ? luaValueArg25.touserdata() : null;
        Vector3 vector6 = (Vector3) (obj3 instanceof Vector3 ? obj3 : null);
        if (vector6 != null) {
            return luaTypes5.wrap(companion3.at(vector6));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg25));
    }

    static final Varargs install$lambda$126$lambda$119(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        CFrame.Companion companion = CFrame.INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fOptNum2 = LuaTypesKt.optNum(luaValueArg2, 0.0f);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        return luaTypes.wrap(companion.angles(fOptNum, fOptNum2, LuaTypesKt.optNum(luaValueArg3, 0.0f)));
    }

    static final Varargs install$lambda$126$lambda$120(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        CFrame.Companion companion = CFrame.INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fOptNum2 = LuaTypesKt.optNum(luaValueArg2, 0.0f);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        return luaTypes.wrap(companion.fromOrientation(fOptNum, fOptNum2, LuaTypesKt.optNum(luaValueArg3, 0.0f)));
    }

    static final Varargs install$lambda$126$lambda$121(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        CFrame.Companion companion = CFrame.INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fOptNum2 = LuaTypesKt.optNum(luaValueArg2, 0.0f);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        return luaTypes.wrap(companion.angles(fOptNum, fOptNum2, LuaTypesKt.optNum(luaValueArg3, 0.0f)));
    }

    static final Varargs install$lambda$126$lambda$122(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        CFrame.Companion companion = CFrame.INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector3)) {
            obj = null;
        }
        Vector3 vector3 = (Vector3) obj;
        if (vector3 == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        if (!(obj2 instanceof Vector3)) {
            obj2 = null;
        }
        Vector3 vector4 = (Vector3) obj2;
        if (vector4 == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg2));
        }
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        Object obj3 = luaValueArg3.isuserdata() ? luaValueArg3.touserdata() : null;
        Vector3 y = (Vector3) (obj3 instanceof Vector3 ? obj3 : null);
        if (y == null) {
            y = Vector3.INSTANCE.getY();
        }
        return luaTypes.wrap(companion.lookAt(vector3, vector4, y));
    }

    static final Varargs install$lambda$126$lambda$123(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector3)) {
            obj = null;
        }
        Vector3 p = (Vector3) obj;
        if (p == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaTypes luaTypes = INSTANCE;
        CFrame.Companion companion = CFrame.INSTANCE;
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        if (!(obj2 instanceof Vector3)) {
            obj2 = null;
        }
        Vector3 vector3 = (Vector3) obj2;
        if (vector3 == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg2));
        }
        Vector3 vector3Plus = p.plus(vector3);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        Object obj3 = luaValueArg3.isuserdata() ? luaValueArg3.touserdata() : null;
        Vector3 y = (Vector3) (obj3 instanceof Vector3 ? obj3 : null);
        if (y == null) {
            y = Vector3.INSTANCE.getY();
        }
        return luaTypes.wrap(companion.lookAt(p, vector3Plus, y));
    }

    static final Varargs install$lambda$126$lambda$124(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        CFrame.Companion companion = CFrame.INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        Vector3 vector3 = (Vector3) (obj instanceof Vector3 ? obj : null);
        if (vector3 != null) {
            LuaValue luaValueArg2 = a.arg(2);
            Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
            return luaTypes.wrap(companion.fromAxisAngle(vector3, LuaTypesKt.num(luaValueArg2)));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg));
    }

    static final Varargs install$lambda$126$lambda$125(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector3)) {
            obj = null;
        }
        Vector3 p = (Vector3) obj;
        if (p == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        if (!(obj2 instanceof Vector3)) {
            obj2 = null;
        }
        Vector3 x = (Vector3) obj2;
        if (x == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg2));
        }
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        Object obj3 = luaValueArg3.isuserdata() ? luaValueArg3.touserdata() : null;
        if (!(obj3 instanceof Vector3)) {
            obj3 = null;
        }
        Vector3 y = (Vector3) obj3;
        if (y == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg3));
        }
        LuaValue luaValueArg4 = a.arg(4);
        Intrinsics.checkNotNullExpressionValue(luaValueArg4, "arg(...)");
        Object obj4 = luaValueArg4.isuserdata() ? luaValueArg4.touserdata() : null;
        Vector3 z = (Vector3) (obj4 instanceof Vector3 ? obj4 : null);
        if (z == null) {
            z = x.cross(y).getUnit();
        }
        return INSTANCE.wrap(CFrame.INSTANCE.fromColumns(p, x, y, z));
    }

    static final Unit install$lambda$132(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda128
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$132$lambda$127((Varargs) obj);
            }
        }));
        lib.set("fromRGB", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda129
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$132$lambda$128((Varargs) obj);
            }
        }));
        lib.set("fromHSV", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda130
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$132$lambda$129((Varargs) obj);
            }
        }));
        lib.set("fromHex", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda131
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$132$lambda$130((LuaValue) obj);
            }
        }));
        lib.set("toHSV", LuaTypesKt.fn1(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda132
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$132$lambda$131((LuaValue) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$132$lambda$127(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fOptNum2 = LuaTypesKt.optNum(luaValueArg2, 0.0f);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        return luaTypes.wrap(new Color3(fOptNum, fOptNum2, LuaTypesKt.optNum(luaValueArg3, 0.0f)));
    }

    static final Varargs install$lambda$132$lambda$128(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.wrap(Color3.INSTANCE.fromRGB(a.arg(1).optint(0), a.arg(2).optint(0), a.arg(3).optint(0)));
    }

    static final Varargs install$lambda$132$lambda$129(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        Color3.Companion companion = Color3.INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fNum = LuaTypesKt.num(luaValueArg);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fNum2 = LuaTypesKt.num(luaValueArg2);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        return luaTypes.wrap(companion.fromHSV(fNum, fNum2, LuaTypesKt.num(luaValueArg3)));
    }

    static final LuaValue install$lambda$132$lambda$130(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        Color3.Companion companion = Color3.INSTANCE;
        String str = a.tojstring();
        Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
        Color3 color3FromHex = companion.fromHex(str);
        if (color3FromHex != null) {
            return luaTypes.wrap(color3FromHex);
        }
        throw new LuaError("Unable to convert characters to hex value");
    }

    static final LuaValue install$lambda$132$lambda$131(LuaValue a) {
        Intrinsics.checkNotNullParameter(a, "a");
        Object obj = a.isuserdata() ? a.touserdata() : null;
        Color3 color3 = (Color3) (obj instanceof Color3 ? obj : null);
        if (color3 != null) {
            float[] h = color3.toHSV();
            LuaValue luaValueArg1 = LuaValue.varargsOf(new LuaValue[]{INSTANCE.n(h[0]), INSTANCE.n(h[1]), INSTANCE.n(h[2])}).arg1();
            Intrinsics.checkNotNullExpressionValue(luaValueArg1, "arg1(...)");
            return luaValueArg1;
        }
        throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(a));
    }

    static final Unit install$lambda$137(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda48
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$137$lambda$133((Varargs) obj);
            }
        }));
        lib.set("random", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda49
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$137$lambda$134((Varargs) obj);
            }
        }));
        lib.set("Random", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda50
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$137$lambda$135((Varargs) obj);
            }
        }));
        for (Map.Entry entry : MapsKt.mapOf(TuplesKt.to("White", 1), TuplesKt.to("Black", 26), TuplesKt.to("Red", 21), TuplesKt.to("Blue", 23), TuplesKt.to("Green", 28), TuplesKt.to("Yellow", 24), TuplesKt.to("Gray", 194), TuplesKt.to("DarkGray", 199)).entrySet()) {
            String k = (String) entry.getKey();
            final int v = ((Number) entry.getValue()).intValue();
            lib.set(k, LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda51
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return LuaTypes.install$lambda$137$lambda$136(v, (Varargs) obj);
                }
            }));
        }
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$137$lambda$133(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue x = a.arg(1);
        if (x.isnumber() && a.narg() >= 3) {
            LuaTypes luaTypes = INSTANCE;
            BrickColors brickColors = BrickColors.INSTANCE;
            LuaValue luaValueArg = a.arg(1);
            Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
            float fNum = LuaTypesKt.num(luaValueArg);
            LuaValue luaValueArg2 = a.arg(2);
            Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
            float fNum2 = LuaTypesKt.num(luaValueArg2);
            LuaValue luaValueArg3 = a.arg(3);
            Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
            return luaTypes.wrap(new BrickColorV(brickColors.closest(new Color3(fNum, fNum2, LuaTypesKt.num(luaValueArg3)))));
        }
        if (x.isnumber()) {
            return INSTANCE.wrap(new BrickColorV(x.toint()));
        }
        if (x.isstring()) {
            LuaTypes luaTypes2 = INSTANCE;
            BrickColors brickColors2 = BrickColors.INSTANCE;
            String str = x.tojstring();
            Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
            Integer numByName = brickColors2.byName(str);
            return luaTypes2.wrap(new BrickColorV(numByName != null ? numByName.intValue() : 194));
        }
        if (!x.isuserdata()) {
            return INSTANCE.wrap(new BrickColorV(194));
        }
        LuaTypes luaTypes3 = INSTANCE;
        BrickColors brickColors3 = BrickColors.INSTANCE;
        Intrinsics.checkNotNull(x);
        Object obj = x.isuserdata() ? x.touserdata() : null;
        Color3 color3 = (Color3) (obj instanceof Color3 ? obj : null);
        if (color3 != null) {
            return luaTypes3.wrap(new BrickColorV(brickColors3.closest(color3)));
        }
        throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(x));
    }

    static final Varargs install$lambda$137$lambda$134(Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        return INSTANCE.wrap(new BrickColorV(((Number) CollectionsKt.random(BrickColors.INSTANCE.getAll(), kotlin.random.Random.Default)).intValue()));
    }

    static final Varargs install$lambda$137$lambda$135(Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        return INSTANCE.wrap(new BrickColorV(((Number) CollectionsKt.random(BrickColors.INSTANCE.getAll(), kotlin.random.Random.Default)).intValue()));
    }

    static final Varargs install$lambda$137$lambda$136(int $v, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        return INSTANCE.wrap(new BrickColorV($v));
    }

    static final Unit install$lambda$139(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda101
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$139$lambda$138((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$139$lambda$138(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        return luaTypes.wrap(new UDim(LuaTypesKt.optNum(luaValueArg, 0.0f), a.arg(2).optint(0)));
    }

    static final Unit install$lambda$143(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda111
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$143$lambda$140((Varargs) obj);
            }
        }));
        lib.set("fromScale", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda112
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$143$lambda$141((Varargs) obj);
            }
        }));
        lib.set("fromOffset", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda113
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$143$lambda$142((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$143$lambda$140(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        if (a.arg(1).isuserdata()) {
            LuaValue luaValueArg = a.arg(1);
            Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
            Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
            if (!(obj instanceof UDim)) {
                obj = null;
            }
            UDim x = (UDim) obj;
            if (x == null) {
                throw new LuaError("UDim expected, got " + INSTANCE.typeOf(luaValueArg));
            }
            LuaValue luaValueArg2 = a.arg(2);
            Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
            Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
            UDim y = (UDim) (obj2 instanceof UDim ? obj2 : null);
            if (y != null) {
                return INSTANCE.wrap(new UDim2(x.getScale(), x.getOffset(), y.getScale(), y.getOffset()));
            }
            throw new LuaError("UDim expected, got " + INSTANCE.typeOf(luaValueArg2));
        }
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg3 = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg3, 0.0f);
        LuaValue luaValueArg4 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg4, "arg(...)");
        int iOptNum = (int) LuaTypesKt.optNum(luaValueArg4, 0.0f);
        LuaValue luaValueArg5 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg5, "arg(...)");
        float fOptNum2 = LuaTypesKt.optNum(luaValueArg5, 0.0f);
        LuaValue luaValueArg6 = a.arg(4);
        Intrinsics.checkNotNullExpressionValue(luaValueArg6, "arg(...)");
        return luaTypes.wrap(new UDim2(fOptNum, iOptNum, fOptNum2, (int) LuaTypesKt.optNum(luaValueArg6, 0.0f)));
    }

    static final Varargs install$lambda$143$lambda$141(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        return luaTypes.wrap(new UDim2(fOptNum, 0, LuaTypesKt.optNum(luaValueArg2, 0.0f), 0));
    }

    static final Varargs install$lambda$143$lambda$142(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        int iOptNum = (int) LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        return luaTypes.wrap(new UDim2(0.0f, iOptNum, 0.0f, (int) LuaTypesKt.optNum(luaValueArg2, 0.0f)));
    }

    static final Unit install$lambda$145(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda147
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$145$lambda$144((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$145$lambda$144(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 1.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        if (!(obj instanceof EnumItemV)) {
            obj = null;
        }
        EnumItemV enumItemV = (EnumItemV) obj;
        int value = enumItemV != null ? enumItemV.getValue() : 3;
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        Object obj2 = luaValueArg3.isuserdata() ? luaValueArg3.touserdata() : null;
        EnumItemV enumItemV2 = (EnumItemV) (obj2 instanceof EnumItemV ? obj2 : null);
        int value2 = enumItemV2 != null ? enumItemV2.getValue() : 1;
        int iOptint = a.arg(4).optint(0);
        boolean zOptboolean = a.arg(5).optboolean(false);
        LuaValue luaValueArg4 = a.arg(6);
        Intrinsics.checkNotNullExpressionValue(luaValueArg4, "arg(...)");
        return luaTypes.wrap(new TweenInfoV(fOptNum, value, value2, iOptint, zOptboolean, LuaTypesKt.optNum(luaValueArg4, 0.0f)));
    }

    static final Unit install$lambda$147(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda143
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$147$lambda$146((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$147$lambda$146(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector3)) {
            obj = null;
        }
        Vector3 vector3 = (Vector3) obj;
        if (vector3 == null) {
            throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector3 vector4 = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (vector4 != null) {
            return luaTypes.wrap(new Ray(vector3, vector4));
        }
        throw new LuaError("Vector3 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Unit install$lambda$149(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$149$lambda$148((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$149$lambda$148(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector3)) {
            obj = null;
        }
        Vector3 p = (Vector3) obj;
        if (p == null) {
            p = Vector3.INSTANCE.getZERO();
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector3 q = (Vector3) (obj2 instanceof Vector3 ? obj2 : null);
        if (q == null) {
            q = Vector3.INSTANCE.getZERO();
        }
        return INSTANCE.wrap(new Region3V(p.min(q), p.max(q)));
    }

    static final Unit install$lambda$151(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda142
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$151$lambda$150((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$151$lambda$150(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof Vector3int16)) {
            obj = null;
        }
        Vector3int16 vector3int16 = (Vector3int16) obj;
        if (vector3int16 == null) {
            vector3int16 = new Vector3int16(0, 0, 0);
        }
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Vector3int16 vector3int17 = (Vector3int16) (obj2 instanceof Vector3int16 ? obj2 : null);
        if (vector3int17 == null) {
            vector3int17 = new Vector3int16(0, 0, 0);
        }
        return luaTypes.wrap(new Region3int16V(vector3int16, vector3int17));
    }

    static final Unit install$lambda$153(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda127
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$153$lambda$152((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$153$lambda$152(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float mn = LuaTypesKt.num(luaValueArg);
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        return luaTypes.wrap(new NumberRange(mn, LuaTypesKt.optNum(luaValueArg2, mn)));
    }

    static final Unit install$lambda$155(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda146
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$155$lambda$154((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$155$lambda$154(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fNum = LuaTypesKt.num(luaValueArg);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fNum2 = LuaTypesKt.num(luaValueArg2);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        return luaTypes.wrap(new NumberKey(fNum, fNum2, LuaTypesKt.optNum(luaValueArg3, 0.0f)));
    }

    static final Unit install$lambda$157(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda45
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$157$lambda$156((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$157$lambda$156(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fNum = LuaTypesKt.num(luaValueArg);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        Color3 color3 = (Color3) (obj instanceof Color3 ? obj : null);
        if (color3 != null) {
            return luaTypes.wrap(new ColorKey(fNum, color3, 0.0f));
        }
        throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(luaValueArg2));
    }

    static final Unit install$lambda$160(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda145
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$160$lambda$159((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$160$lambda$159(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue x = a.arg(1);
        if (!x.istable()) {
            if (a.narg() < 2) {
                LuaTypes luaTypes = INSTANCE;
                Intrinsics.checkNotNull(x);
                return luaTypes.wrap(new NumberSequence(CollectionsKt.listOf(new NumberKey[]{new NumberKey(0.0f, LuaTypesKt.num(x), 0.0f), new NumberKey(1.0f, LuaTypesKt.num(x), 0.0f)})));
            }
            LuaTypes luaTypes2 = INSTANCE;
            Intrinsics.checkNotNull(x);
            LuaValue luaValueArg = a.arg(2);
            Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
            return luaTypes2.wrap(new NumberSequence(CollectionsKt.listOf(new NumberKey[]{new NumberKey(0.0f, LuaTypesKt.num(x), 0.0f), new NumberKey(1.0f, LuaTypesKt.num(luaValueArg), 0.0f)})));
        }
        LuaTypes luaTypes3 = INSTANCE;
        Iterable intRange = new IntRange(1, x.length());
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(intRange, 10));
        Iterator<Integer> it = intRange.iterator();
        while (it.hasNext()) {
            LuaValue luaValue = x.get(((IntIterator) it).nextInt());
            Intrinsics.checkNotNullExpressionValue(luaValue, "get(...)");
            Object obj = luaValue.isuserdata() ? luaValue.touserdata() : null;
            Iterable iterable = intRange;
            NumberKey numberKey = (NumberKey) (obj instanceof NumberKey ? obj : null);
            if (numberKey == null) {
                throw new LuaError("NumberSequenceKeypoint expected, got " + INSTANCE.typeOf(luaValue));
            }
            arrayList.add(numberKey);
            intRange = (IntRange) iterable;
        }
        return luaTypes3.wrap(new NumberSequence((List) arrayList));
    }

    static final Unit install$lambda$163(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda65
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$163$lambda$162((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$163$lambda$162(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaValue x = a.arg(1);
        if (x.istable()) {
            LuaTypes luaTypes = INSTANCE;
            Iterable intRange = new IntRange(1, x.length());
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(intRange, 10));
            Iterator<Integer> it = intRange.iterator();
            while (it.hasNext()) {
                LuaValue luaValue = x.get(((IntIterator) it).nextInt());
                Intrinsics.checkNotNullExpressionValue(luaValue, "get(...)");
                Object obj = luaValue.isuserdata() ? luaValue.touserdata() : null;
                Iterable iterable = intRange;
                if (!(obj instanceof ColorKey)) {
                    obj = null;
                }
                ColorKey colorKey = (ColorKey) obj;
                if (colorKey == null) {
                    throw new LuaError("ColorSequenceKeypoint expected, got " + INSTANCE.typeOf(luaValue));
                }
                arrayList.add(colorKey);
                intRange = (IntRange) iterable;
            }
            return luaTypes.wrap(new ColorSequence((List) arrayList));
        }
        if (a.narg() < 2) {
            Intrinsics.checkNotNull(x);
            Object obj2 = x.isuserdata() ? x.touserdata() : null;
            Color3 c = (Color3) (!(obj2 instanceof Color3) ? null : obj2);
            if (c != null) {
                return INSTANCE.wrap(new ColorSequence(CollectionsKt.listOf(new ColorKey[]{new ColorKey(0.0f, c, 0.0f), new ColorKey(1.0f, c, 0.0f)})));
            }
            throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(x));
        }
        LuaTypes luaTypes2 = INSTANCE;
        ColorKey[] colorKeyArr = new ColorKey[2];
        Intrinsics.checkNotNull(x);
        Object obj3 = x.isuserdata() ? x.touserdata() : null;
        if (!(obj3 instanceof Color3)) {
            obj3 = null;
        }
        Color3 color3 = (Color3) obj3;
        if (color3 == null) {
            throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(x));
        }
        colorKeyArr[0] = new ColorKey(0.0f, color3, 0.0f);
        LuaValue luaValueArg = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj4 = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        Color3 color4 = (Color3) (!(obj4 instanceof Color3) ? null : obj4);
        if (color4 == null) {
            throw new LuaError("Color3 expected, got " + INSTANCE.typeOf(luaValueArg));
        }
        colorKeyArr[1] = new ColorKey(1.0f, color4, 0.0f);
        return luaTypes2.wrap(new ColorSequence(CollectionsKt.listOf(colorKeyArr)));
    }

    static final Unit install$lambda$165(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$165$lambda$164((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$165$lambda$164(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.wrap(new RandomV(a.arg(1).isnil() ? System.nanoTime() : a.arg(1).tolong()));
    }

    static final Unit install$lambda$167(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda110
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$167$lambda$166((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$167$lambda$166(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.0f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fOptNum2 = LuaTypesKt.optNum(luaValueArg2, 0.0f);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        float fOptNum3 = LuaTypesKt.optNum(luaValueArg3, 0.0f);
        LuaValue luaValueArg4 = a.arg(4);
        Intrinsics.checkNotNullExpressionValue(luaValueArg4, "arg(...)");
        return luaTypes.wrap(new Rect(fOptNum, fOptNum2, fOptNum3, LuaTypesKt.optNum(luaValueArg4, 0.0f)));
    }

    static final Unit install$lambda$169(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda144
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$169$lambda$168((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$169$lambda$168(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        if (a.arg(1).isuserdata()) {
            return INSTANCE.wrap(new PhysicalProps(0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 126, null));
        }
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        float fOptNum = LuaTypesKt.optNum(luaValueArg, 0.7f);
        LuaValue luaValueArg2 = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        float fOptNum2 = LuaTypesKt.optNum(luaValueArg2, 0.3f);
        LuaValue luaValueArg3 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg3, "arg(...)");
        float fOptNum3 = LuaTypesKt.optNum(luaValueArg3, 0.5f);
        LuaValue luaValueArg4 = a.arg(4);
        Intrinsics.checkNotNullExpressionValue(luaValueArg4, "arg(...)");
        float fOptNum4 = LuaTypesKt.optNum(luaValueArg4, 1.0f);
        LuaValue luaValueArg5 = a.arg(5);
        Intrinsics.checkNotNullExpressionValue(luaValueArg5, "arg(...)");
        return luaTypes.wrap(new PhysicalProps(1, fOptNum, fOptNum2, fOptNum3, fOptNum4, LuaTypesKt.optNum(luaValueArg5, 1.0f), 0.0f, 64, null));
    }

    static final Unit install$lambda$173(LuaTable lib) {
        Intrinsics.checkNotNullParameter(lib, "$this$lib");
        lib.set("new", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda97
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$173$lambda$170((Varargs) obj);
            }
        }));
        lib.set("fromEnum", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda98
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$173$lambda$171((Varargs) obj);
            }
        }));
        lib.set("fromName", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.lua.LuaTypes$$ExternalSyntheticLambda99
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return LuaTypes.install$lambda$173$lambda$172((Varargs) obj);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Varargs install$lambda$173$lambda$170(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        String str = a.arg(1).tojstring();
        Intrinsics.checkNotNullExpressionValue(str, "tojstring(...)");
        LuaValue luaValueArg = a.arg(2);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        if (!(obj instanceof EnumItemV)) {
            obj = null;
        }
        EnumItemV enumItemV = (EnumItemV) obj;
        int value = enumItemV != null ? enumItemV.getValue() : 400;
        LuaValue luaValueArg2 = a.arg(3);
        Intrinsics.checkNotNullExpressionValue(luaValueArg2, "arg(...)");
        Object obj2 = luaValueArg2.isuserdata() ? luaValueArg2.touserdata() : null;
        EnumItemV enumItemV2 = (EnumItemV) (obj2 instanceof EnumItemV ? obj2 : null);
        return luaTypes.wrap(new FontValue(str, value, enumItemV2 != null ? enumItemV2.getValue() : 0, ""));
    }

    static final Varargs install$lambda$173$lambda$171(Varargs a) {
        String name;
        Intrinsics.checkNotNullParameter(a, "a");
        LuaTypes luaTypes = INSTANCE;
        LuaValue luaValueArg = a.arg(1);
        Intrinsics.checkNotNullExpressionValue(luaValueArg, "arg(...)");
        Object obj = luaValueArg.isuserdata() ? luaValueArg.touserdata() : null;
        EnumItemV enumItemV = (EnumItemV) (obj instanceof EnumItemV ? obj : null);
        if (enumItemV == null || (name = enumItemV.getName()) == null) {
            name = "SourceSansPro";
        }
        return luaTypes.wrap(new FontValue("rbxasset://fonts/families/" + name + ".json", 400, 0, ""));
    }

    static final Varargs install$lambda$173$lambda$172(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return INSTANCE.wrap(new FontValue("rbxasset://fonts/families/" + a.arg(1).tojstring() + ".json", 400, 0, ""));
    }

    public final float ease(int style, int dir, float t0) {
        float t = RangesKt.coerceIn(t0, 0.0f, 1.0f);
        switch (dir) {
            case 0:
                return ease$easeIn(style, t);
            case 1:
                return 1.0f - ease$easeIn(style, 1.0f - t);
            default:
                return t < 0.5f ? ease$easeIn(style, t * 2.0f) / 2.0f : 1.0f - (ease$easeIn(style, (1.0f - t) * 2.0f) / 2.0f);
        }
    }

    private static final float ease$easeIn(int $style, float x) {
        switch ($style) {
            case 0:
                return x;
            case 1:
                return 1.0f - ((float) Math.cos((3.1415927f * x) / 2.0f));
            case 2:
                return x * x * (((1.0f + 1.70158f) * x) - 1.70158f);
            case 3:
                return x * x;
            case 4:
                return x * x * x * x;
            case 5:
                return x * x * x * x * x;
            case 6:
                return 1.0f - INSTANCE.bounceOut(1.0f - x);
            case 7:
                if (x == 0.0f) {
                    return x;
                }
                if (!(x == 1.0f)) {
                    return (-((float) Math.pow(2.0d, (((double) x) * 10.0d) - 10.0d))) * ((float) Math.sin(((10.0f * x) - 10.75f) * 2.0943952f));
                }
                return x;
            case 8:
                if (x == 0.0f) {
                    return 0.0f;
                }
                return (float) Math.pow(2.0d, (((double) x) * 10.0d) - 10.0d);
            case 9:
                return 1.0f - ((float) Math.sqrt(1.0f - (x * x)));
            case 10:
                return x * x * x;
            default:
                return x * x;
        }
    }

    private final float bounceOut(float x) {
        if (x < 1.0f / 2.75f) {
            return 7.5625f * x * x;
        }
        if (x < 2.0f / 2.75f) {
            float y = x - (1.5f / 2.75f);
            return (7.5625f * y * y) + 0.75f;
        }
        if (x < 2.5d / ((double) 2.75f)) {
            float y2 = x - (2.25f / 2.75f);
            return (7.5625f * y2 * y2) + 0.9375f;
        }
        float y3 = x - (2.625f / 2.75f);
        return (7.5625f * y3 * y3) + 0.984375f;
    }

    public final Object lerpValue(Object a, Object b, float t) {
        if ((a instanceof Float) && (b instanceof Number)) {
            return Float.valueOf(((Number) a).floatValue() + ((((Number) b).floatValue() - ((Number) a).floatValue()) * t));
        }
        if ((a instanceof Double) && (b instanceof Number)) {
            return Double.valueOf(((Number) a).doubleValue() + ((((Number) b).doubleValue() - ((Number) a).doubleValue()) * ((double) t)));
        }
        if ((a instanceof Integer) && (b instanceof Number)) {
            return Integer.valueOf((int) (((Number) a).doubleValue() + ((((Number) b).doubleValue() - ((Number) a).doubleValue()) * ((double) t))));
        }
        if ((a instanceof Long) && (b instanceof Number)) {
            return Long.valueOf((long) (((Number) a).doubleValue() + ((((Number) b).doubleValue() - ((Number) a).doubleValue()) * ((double) t))));
        }
        if ((a instanceof Vector3) && (b instanceof Vector3)) {
            return ((Vector3) a).lerp((Vector3) b, t);
        }
        if ((a instanceof Vector2) && (b instanceof Vector2)) {
            return ((Vector2) a).lerp((Vector2) b, t);
        }
        if ((a instanceof CFrame) && (b instanceof CFrame)) {
            return ((CFrame) a).lerp((CFrame) b, t);
        }
        if ((a instanceof Color3) && (b instanceof Color3)) {
            return ((Color3) a).lerp((Color3) b, t);
        }
        if ((a instanceof UDim2) && (b instanceof UDim2)) {
            return ((UDim2) a).lerp((UDim2) b, t);
        }
        if ((a instanceof UDim) && (b instanceof UDim)) {
            return new UDim(((UDim) a).getScale() + ((((UDim) b).getScale() - ((UDim) a).getScale()) * t), (int) (((UDim) a).getOffset() + ((((UDim) b).getOffset() - ((UDim) a).getOffset()) * t)));
        }
        if ((a instanceof Rect) && (b instanceof Rect)) {
            Rect ra = (Rect) a;
            Rect rb = (Rect) b;
            return new Rect(
                ra.getMinX() + ((rb.getMinX() - ra.getMinX()) * t),
                ra.getMinY() + ((rb.getMinY() - ra.getMinY()) * t),
                ra.getMaxX() + ((rb.getMaxX() - ra.getMaxX()) * t),
                ra.getMaxY() + ((rb.getMaxY() - ra.getMaxY()) * t)
            );
        }
        if ((a instanceof Number) && (b instanceof Number)) {
            return Double.valueOf(((Number) a).doubleValue() + ((((Number) b).doubleValue() - ((Number) a).doubleValue()) * ((double) t)));
        }
        return t >= 1.0f ? b : a;
    }

    public final float absf(float x) {
        return Math.abs(x);
    }
}
