package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.audio.AudioEngine;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.FontValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.PropNames;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.CloudPlace;
import com.goydevv.studrblx.net.Http;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SpecialProps {
    public static final Object NONE = new Object();
    private static final ConcurrentHashMap<Long, long[]> creators = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Thread> fetching = new ConcurrentHashMap<>();
    private static final Pattern CREATOR = Pattern.compile("\"creator\"\\s*:\\s*\\{([^}]*)\\}");
    private static final Pattern ID = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
    private static final Pattern TYPE = Pattern.compile("\"type\"\\s*:\\s*\"(\\w+)\"");
    private static final Pattern TAG = Pattern.compile("<[^>]*>");
    private static final HashMap<Instance, String> jobIds = new HashMap<>();

    private static final HashMap<String, java.util.HashSet<String>> KEYS = new HashMap<>();

    static {
        key("DataModel", "PlaceId GameId CreatorId CreatorType PlaceVersion JobId PrivateServerId PrivateServerOwnerId");
        key("Workspace", "DistributedGameTime StreamingEnabled Gravity CurrentCamera");
        key("Camera", "ViewportSize CFrame CameraType Focus FieldOfView CameraSubject");
        key("Lighting", "TimeOfDay FogEnd FogStart FogColor GlobalShadows");
        key("UserInputService", "TouchEnabled KeyboardEnabled MouseEnabled GamepadEnabled VREnabled AccelerometerEnabled GyroscopeEnabled MouseBehavior MouseIconEnabled ModalEnabled");
        key("HttpService", "HttpEnabled");
        key("StarterGui", "ShowDevelopmentGui ResetPlayerGuiOnSpawn");
        key("Humanoid", "RootPart MoveDirection FloorMaterial SeatPart Sit Jump PlatformStand AutoRotate UseJumpPower RigType HipHeight DisplayName");
        key("Sound", "TimePosition TimeLength IsPlaying IsLoaded PlaybackSpeed PlaybackLoudness Volume");
        key("TextLabel", "ContentText TextBounds TextFits FontFace");
        key("TextButton", "ContentText TextBounds TextFits FontFace");
        key("TextBox", "ContentText TextBounds TextFits FontFace");
    }

    private static void key(String cls, String names) {
        java.util.HashSet<String> set = new java.util.HashSet<>();
        for (String n : names.split(" ")) set.add(n);
        KEYS.put(cls, set);
    }

    public static boolean has(Instance inst, String k) {
        java.util.HashSet<String> set = KEYS.get(inst.getClassName());
        return set != null && set.contains(k);
    }

    private SpecialProps() {
    }

    public static void prefetch(CloudPlace cloud) {
        if (cloud == null || cloud.getUniverseId() <= 0) return;
        start(cloud.getUniverseId());
    }

    private static Thread start(final long universe) {
        if (creators.containsKey(universe)) return null;
        Thread t = fetching.get(universe);
        if (t != null) return t;
        t = new Thread(() -> {
            try {
                byte[] b = Http.INSTANCE.get("https://games.roblox.com/v1/games?universeIds=" + universe, new HashMap<>(), 262144);
                String s = new String(b, StandardCharsets.UTF_8);
                Matcher m = CREATOR.matcher(s);
                if (m.find()) {
                    Matcher mi = ID.matcher(m.group(1));
                    Matcher mt = TYPE.matcher(m.group(1));
                    if (mi.find()) {
                        long id = Long.parseLong(mi.group(1));
                        long type = (mt.find() && "Group".equalsIgnoreCase(mt.group(1))) ? 1L : 0L;
                        creators.put(universe, new long[]{id, type});
                    }
                }
            } catch (Throwable ignored) {
            } finally {
                fetching.remove(universe);
            }
        }, "creator-info");
        t.setDaemon(true);
        Thread prev = fetching.putIfAbsent(universe, t);
        if (prev != null) return prev;
        t.start();
        return t;
    }

    private static long[] creator(CloudPlace cloud) {
        if (cloud == null || cloud.getUniverseId() <= 0) return null;
        long u = cloud.getUniverseId();
        long[] c = creators.get(u);
        if (c != null) return c;
        Thread t = start(u);
        if (t != null) {
            try {
                t.join(2500L);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
        return creators.get(u);
    }

    private static EnumItemV en(String type, int value, String fallback) {
        String n = null;
        try {
            n = Enums.INSTANCE.itemName(type, value);
        } catch (Throwable ignored) {
        }
        return new EnumItemV(type, n != null ? n : fallback, value);
    }

    private static Object stored(Instance inst, String k) {
        Object v = inst.get(k);
        if (v == null) {
            try {
                String r = PropNames.INSTANCE.resolve(inst, k);
                if (r != null && !r.equals(k)) v = inst.get(r);
            } catch (Throwable ignored) {
            }
        }
        return v;
    }

    private static Object or(Instance inst, String k, Object def) {
        Object v = stored(inst, k);
        return v != null ? v : def;
    }

    private static Instance rootPartOf(Instance hum) {
        Instance m = hum.getParent();
        if (m == null) return null;
        for (Instance c : m.getChildren()) {
            if ("HumanoidRootPart".equals(c.getName()) && c.isA("BasePart")) return c;
        }
        Object pp = m.get("PrimaryPart");
        if (pp instanceof Instance) return (Instance) pp;
        for (Instance c : m.getChildren()) {
            if ("Torso".equals(c.getName()) && c.isA("BasePart")) return c;
        }
        return null;
    }

    private static float num(Object o, float def) {
        return o instanceof Number ? ((Number) o).floatValue() : def;
    }

    private static String timeOfDay(float clock) {
        double c = ((clock % 24.0d) + 24.0d) % 24.0d;
        int total = (int) Math.round(c * 3600.0d) % 86400;
        return String.format(Locale.ROOT, "%02d:%02d:%02d", total / 3600, (total / 60) % 60, total % 60);
    }

    private static String contentText(Instance inst) {
        Object t = stored(inst, "Text");
        String s = t != null ? t.toString() : "";
        if (Boolean.TRUE.equals(stored(inst, "RichText"))) {
            s = TAG.matcher(s).replaceAll("");
            s = s.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&apos;", "'").replace("&amp;", "&");
        }
        return s;
    }

    private static Object derived(Instance inst, String k) {
        if (inst.isA("BasePart")) {
            switch (k) {
                case "CenterOfMass":
                case "AssemblyCenterOfMass":
                    return PartData.INSTANCE.cframe(inst).getPosition();
                case "ExtentsSize":
                    return PartData.INSTANCE.size(inst);
                case "ExtentsCFrame":
                    return PartData.INSTANCE.cframe(inst);
                default:
                    return NONE;
            }
        }
        if ("WorldPivot".equals(k) && inst.isA("PVInstance") && !inst.isA("BasePart")) {
            Object stored = stored(inst, "WorldPivot");
            if (stored instanceof CFrame) return stored;
            Object pp = inst.get("PrimaryPart");
            if (pp instanceof Instance && ((Instance) pp).isA("BasePart")) return PartData.INSTANCE.cframe((Instance) pp);
            for (Instance d : Instance.descendants$default(inst, null, 1, null)) {
                if (d.isA("BasePart")) return PartData.INSTANCE.cframe(d);
            }
            return CFrame.INSTANCE.getIDENTITY();
        }
        if ("Active".equals(k) && (inst.isA("JointInstance") || "WeldConstraint".equals(inst.getClassName()))) {
            Object en = stored(inst, "Enabled");
            boolean enabled = !(en instanceof Boolean) || (Boolean) en;
            Object p0 = inst.get("Part0");
            Object p1 = inst.get("Part1");
            return Boolean.valueOf(enabled && p0 instanceof Instance && p1 instanceof Instance && ((Instance) p0).getParent() != null && ((Instance) p1).getParent() != null);
        }
        if ("BodyColors".equals(inst.getClassName()) && k.endsWith("Color") && !k.endsWith("Color3")) {
            Object c = stored(inst, k + "3");
            if (c instanceof Color3) return new com.goydevv.studrblx.model.BrickColorV(com.goydevv.studrblx.model.BrickColors.INSTANCE.closest((Color3) c));
            return NONE;
        }
        return NONE;
    }

    public static boolean derivedHas(Instance inst, String k) {
        return derived(inst, k) != NONE;
    }

    public static Object get(LuaRuntime rt, Instance inst, String k) {
        Object dv = derived(inst, k);
        if (dv != NONE) return dv;
        String cls = inst.getClassName();
        Studio studio = rt.getStudio();
        PlaySession play = rt.getPlay();
        switch (cls) {
            case "DataModel": {
                CloudPlace cloud = studio != null ? studio.getCloud() : null;
                switch (k) {
                    case "PlaceId":
                        return Double.valueOf(cloud != null ? cloud.getPlaceId() : 0L);
                    case "GameId":
                        return Double.valueOf(cloud != null ? cloud.getUniverseId() : 0L);
                    case "CreatorId": {
                        long[] c = creator(cloud);
                        return Double.valueOf(c != null ? c[0] : 0L);
                    }
                    case "CreatorType": {
                        long[] c = creator(cloud);
                        return (c != null && c[1] == 1L) ? en("CreatorType", 1, "Group") : en("CreatorType", 0, "User");
                    }
                    case "PlaceVersion":
                        return Double.valueOf(0.0d);
                    case "JobId": {
                        if (play == null) return "";
                        synchronized (jobIds) {
                            String id = jobIds.get(inst);
                            if (id == null) {
                                id = java.util.UUID.randomUUID().toString();
                                jobIds.clear();
                                jobIds.put(inst, id);
                            }
                            return id;
                        }
                    }
                    case "PrivateServerId":
                        return "";
                    case "PrivateServerOwnerId":
                        return Double.valueOf(0.0d);
                    default:
                        return NONE;
                }
            }
            case "Workspace":
                switch (k) {
                    case "DistributedGameTime":
                        return Double.valueOf(play != null ? rt.getTime() : 0.0d);
                    case "StreamingEnabled":
                        return or(inst, k, Boolean.FALSE);
                    case "Gravity":
                        return or(inst, k, Float.valueOf(196.2f));
                    case "CurrentCamera": {
                        Object cam = inst.get("CurrentCamera");
                        if (cam instanceof Instance) return cam;
                        for (Instance c : inst.getChildren()) {
                            if ("Camera".equals(c.getClassName())) {
                                inst.set("CurrentCamera", c, false);
                                return c;
                            }
                        }
                        Instance c = new Instance("Camera");
                        c.setName("Camera");
                        c.set("CameraType", new EnumValue(5), false);
                        c.set("FieldOfView", Float.valueOf(70.0f), false);
                        Instance.setParent$default(c, inst, 0, 2, null);
                        inst.set("CurrentCamera", c, false);
                        return c;
                    }
                    default:
                        return NONE;
                }
            case "Camera":
                switch (k) {
                    case "ViewportSize": {
                        if (studio != null) {
                            try {
                                Vector2 v = studio.viewportSizeDp();
                                if (v.getX() > 0 && v.getY() > 0) return v;
                            } catch (Throwable ignored) {
                            }
                        }
                        return new Vector2(800.0f, 600.0f);
                    }
                    case "CFrame":
                        return or(inst, k, CFrame.INSTANCE.getIDENTITY());
                    case "CameraType": {
                        Object v = stored(inst, k);
                        if (v instanceof EnumItemV) return v;
                        int i = v instanceof EnumValue ? ((EnumValue) v).getValue() : v instanceof Number ? ((Number) v).intValue() : 5;
                        return en("CameraType", i, i == 6 ? "Scriptable" : i == 0 ? "Fixed" : "Custom");
                    }
                    case "Focus":
                        return or(inst, k, CFrame.INSTANCE.getIDENTITY());
                    case "FieldOfView":
                        return or(inst, k, Float.valueOf(70.0f));
                    case "CameraSubject": {
                        Object s = stored(inst, k);
                        if (s instanceof Instance) return s;
                        if (play != null && play.getCharacter() != null) {
                            for (Instance c : play.getCharacter().getChildren()) {
                                if ("Humanoid".equals(c.getClassName())) return c;
                            }
                        }
                        return null;
                    }
                    default:
                        return NONE;
                }
            case "Lighting":
                switch (k) {
                    case "TimeOfDay": {
                        Object ct = stored(inst, "ClockTime");
                        if (ct instanceof Number) return timeOfDay(((Number) ct).floatValue());
                        return or(inst, k, "14:00:00");
                    }
                    case "FogEnd":
                        return or(inst, k, Float.valueOf(100000.0f));
                    case "FogStart":
                        return or(inst, k, Float.valueOf(0.0f));
                    case "FogColor":
                        return or(inst, k, new Color3(0.752941f, 0.752941f, 0.752941f));
                    case "GlobalShadows":
                        return or(inst, k, Boolean.TRUE);
                    default:
                        return NONE;
                }
            case "UserInputService":
                switch (k) {
                    case "TouchEnabled":
                        return Boolean.TRUE;
                    case "KeyboardEnabled":
                    case "MouseEnabled":
                    case "GamepadEnabled":
                    case "VREnabled":
                    case "AccelerometerEnabled":
                    case "GyroscopeEnabled":
                        return Boolean.FALSE;
                    case "MouseBehavior": {
                        Object v = stored(inst, k);
                        if (v instanceof EnumItemV) return v;
                        int i = v instanceof EnumValue ? ((EnumValue) v).getValue() : v instanceof Number ? ((Number) v).intValue() : 0;
                        return en("MouseBehavior", i, i == 1 ? "LockCenter" : i == 2 ? "LockCurrentPosition" : "Default");
                    }
                    case "MouseIconEnabled":
                        return or(inst, k, Boolean.TRUE);
                    case "ModalEnabled":
                        return or(inst, k, Boolean.FALSE);
                    default:
                        return NONE;
                }
            case "HttpService":
                if ("HttpEnabled".equals(k)) return or(inst, k, Boolean.FALSE);
                return NONE;
            case "StarterGui":
                if ("ShowDevelopmentGui".equals(k) || "ResetPlayerGuiOnSpawn".equals(k)) return or(inst, k, Boolean.TRUE);
                return NONE;
            case "Humanoid":
                switch (k) {
                    case "RootPart":
                        return rootPartOf(inst);
                    case "MoveDirection":
                        return play != null ? play.moveDirection(inst) : Vector3.INSTANCE.getZERO();
                    case "FloorMaterial": {
                        Object v = stored(inst, k);
                        int i = v instanceof EnumValue ? ((EnumValue) v).getValue() : v instanceof Number ? ((Number) v).intValue() : 1792;
                        if (play != null && !play.isGrounded(inst)) i = 1792;
                        return en("Material", i, i == 1792 ? "Air" : "Plastic");
                    }
                    case "SeatPart": {
                        Instance s = play != null ? play.seatOf(inst) : null;
                        if (s != null) return s;
                        Object v = stored(inst, k);
                        return v instanceof Instance ? v : null;
                    }
                    case "Sit": {
                        if (play != null && play.seatOf(inst) != null) return Boolean.TRUE;
                        return or(inst, k, Boolean.FALSE);
                    }
                    case "Jump":
                    case "PlatformStand":
                        return or(inst, k, Boolean.FALSE);
                    case "AutoRotate":
                    case "UseJumpPower":
                        return or(inst, k, Boolean.TRUE);
                    case "RigType": {
                        Object v = stored(inst, k);
                        int i = v instanceof EnumValue ? ((EnumValue) v).getValue() : v instanceof Number ? ((Number) v).intValue() : -1;
                        if (i < 0) {
                            Instance m = inst.getParent();
                            boolean r6 = m != null && Instance.findFirstChild$default(m, "Torso", false, 2, null) != null && Instance.findFirstChild$default(m, "UpperTorso", false, 2, null) == null;
                            i = r6 ? 0 : 1;
                        }
                        return en("HumanoidRigType", i, i == 0 ? "R6" : "R15");
                    }
                    case "HipHeight":
                        return or(inst, k, Float.valueOf(2.0f));
                    case "DisplayName": {
                        Object v = stored(inst, k);
                        if (v != null && !v.toString().isEmpty()) return v;
                        Instance m = inst.getParent();
                        return m != null ? m.getName() : "";
                    }
                    default:
                        return NONE;
                }
            case "Sound": {
                double[] st = null;
                switch (k) {
                    case "TimePosition":
                    case "TimeLength":
                    case "IsPlaying":
                    case "IsLoaded":
                        try {
                            st = AudioEngine.INSTANCE.soundState(inst);
                        } catch (Throwable ignored) {
                        }
                        break;
                    default:
                        break;
                }
                switch (k) {
                    case "TimePosition":
                        if (st != null) return Double.valueOf(st[0]);
                        return Double.valueOf(num(stored(inst, k), 0.0f));
                    case "TimeLength":
                        return Double.valueOf(st != null ? st[1] : 0.0d);
                    case "IsPlaying":
                        if (st != null) return Boolean.valueOf(st[3] != 0.0d);
                        return Boolean.valueOf(play != null && Boolean.TRUE.equals(stored(inst, "Playing")));
                    case "IsLoaded":
                        return Boolean.valueOf(st != null && st[2] != 0.0d);
                    case "PlaybackSpeed":
                        return or(inst, k, Float.valueOf(1.0f));
                    case "PlaybackLoudness":
                        return Double.valueOf(0.0d);
                    case "Volume":
                        return or(inst, k, Float.valueOf(0.5f));
                    default:
                        return NONE;
                }
            }
            default:
                break;
        }
        if ("TextLabel".equals(cls) || "TextButton".equals(cls) || "TextBox".equals(cls)) {
            switch (k) {
                case "ContentText":
                    return contentText(inst);
                case "TextBounds": {
                    float size = num(stored(inst, "TextSize"), 14.0f);
                    float maxW = Float.MAX_VALUE;
                    if (Boolean.TRUE.equals(stored(inst, "TextWrapped"))) {
                        Object as = inst.get("AbsoluteSize");
                        if (as instanceof Vector2 && ((Vector2) as).getX() > 0) maxW = ((Vector2) as).getX();
                    }
                    try {
                        return ApiExtras.measure(contentText(inst), size, maxW);
                    } catch (Throwable t) {
                        String s = contentText(inst);
                        return new Vector2(s.length() * size * 0.5f, s.isEmpty() ? 0.0f : size);
                    }
                }
                case "TextFits":
                    return Boolean.TRUE;
                case "FontFace": {
                    Object v = stored(inst, k);
                    if (v != null) return v;
                    return new FontValue("rbxasset://fonts/families/SourceSansPro.json", 400, 0, "");
                }
                default:
                    break;
            }
        }
        if (inst.isA("BasePart")) {
            switch (k) {
                case "Orientation":
                case "Rotation": {
                    CFrame cf = PartData.INSTANCE.cframe(inst);
                    Vector3 o = "Orientation".equals(k) ? cf.toOrientation() : cf.toEulerXYZ();
                    float deg = (float) (180.0d / Math.PI);
                    return new Vector3(o.getX() * deg, o.getY() * deg, o.getZ() * deg);
                }
                case "Shape":
                    if (!"Part".equals(cls)) return NONE;
                    int sh = PartData.INSTANCE.shape(inst);
                    return en("PartType", sh, sh == 0 ? "Ball" : sh == 2 ? "Cylinder" : sh == 3 ? "Wedge" : sh == 4 ? "CornerWedge" : "Block");
                case "AssemblyLinearVelocity":
                case "Velocity": {
                    Vector3 v = play != null ? play.velocityOf(inst) : null;
                    if (v != null) return v;
                    Object s = stored(inst, k);
                    if (s == null) s = stored(inst, "Velocity");
                    return s instanceof Vector3 ? s : Vector3.INSTANCE.getZERO();
                }
                case "Mass":
                case "AssemblyMass": {
                    Vector3 s = PartData.INSTANCE.size(inst);
                    return Double.valueOf(0.7d * s.getX() * s.getY() * s.getZ());
                }
                case "CanQuery":
                case "CanTouch":
                case "CastShadow":
                    return or(inst, k, Boolean.TRUE);
                case "Locked":
                case "Massless":
                    return or(inst, k, Boolean.FALSE);
                case "Reflectance":
                    return or(inst, k, Float.valueOf(0.0f));
                default:
                    return NONE;
            }
        }
        return NONE;
    }
}
