package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.engine.TextChat;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public final class ApiExtras {

    private static final Map<LuaRuntime, Map<String, Connection>> RENDER_STEPS = new WeakHashMap<>();
    private static final Map<LuaRuntime, Map<String, List<LuaValue>>> SUBSCRIPTIONS = new WeakHashMap<>();
    private static final HashSet<String> WARNED = new HashSet<>();

    private static final String[][] EXTRA_METHODS = {
            {"BasePart", "SetNetworkOwnershipAuto", "GetNetworkOwnershipAuto"},
            {"MarketplaceService", "PromptBundlePurchase", "PromptPremiumPurchase"},
            {"InsertService", "LoadAssetVersion"},
            {"LocalizationService", "GetTranslatorForLocaleAsync"},
            {"Chat", "FilterStringAsync"},
            {"GuiService", "GetFocusedTextBox"},
    };

    private ApiExtras() {
    }

    public static boolean allows(Instance inst, String name) {
        for (String[] row : EXTRA_METHODS) {
            if (!inst.isA(row[0])) continue;
            for (int k = 1; k < row.length; k++) if (row[k].equals(name)) return true;
        }
        return false;
    }

    private static Instance inst(LuaValue v) {
        Object o = v.isuserdata() ? v.touserdata() : null;
        return o instanceof Instance ? (Instance) o : null;
    }

    private static Object ud(LuaValue v) {
        return v.isuserdata() ? v.touserdata() : null;
    }

    private static void warnOnce(String key, String msg) {
        synchronized (WARNED) {
            if (!WARNED.add(key)) return;
        }
        OutputLog.INSTANCE.warn(msg);
    }

    public static List<String> tags(Instance i) {
        Object t = i.get("Tags");
        String raw;
        if (t instanceof BinaryString) raw = new String(((BinaryString) t).getBytes(), StandardCharsets.UTF_8);
        else raw = t instanceof String ? (String) t : "";
        ArrayList<String> out = new ArrayList<>();
        for (String s : raw.split("\u0000")) {
            if (!s.isEmpty() && !out.contains(s)) out.add(s);
        }
        for (String k : new ArrayList<>(i.getAttributes().keySet())) {
            if (k.startsWith("__tag:")) {
                String tg = k.substring(6);
                if (!out.contains(tg)) out.add(tg);
                i.getAttributes().remove(k);
            }
        }
        return out;
    }

    private static void setTags(Instance i, List<String> t) {
        StringBuilder b = new StringBuilder();
        for (int k = 0; k < t.size(); k++) {
            if (k > 0) b.append('\u0000');
            b.append(t.get(k));
        }
        i.set("Tags", new BinaryString(b.toString().getBytes(StandardCharsets.UTF_8)), false);
        i.getPropTypes().put("Tags", 1);
    }

    private static boolean addTag(LuaRuntime rt, Instance i, String tag) {
        List<String> t = tags(i);
        if (t.contains(tag)) return false;
        t.add(tag);
        setTags(i, t);
        Instance cs = rt.getStudio().getModel().findService("CollectionService");
        if (cs != null) rt.fire(cs, "TagAdded:" + tag, rt.wrap(i));
        return true;
    }

    private static boolean removeTag(LuaRuntime rt, Instance i, String tag) {
        List<String> t = tags(i);
        if (!t.remove(tag)) return false;
        setTags(i, t);
        Instance cs = rt.getStudio().getModel().findService("CollectionService");
        if (cs != null) rt.fire(cs, "TagRemoved:" + tag, rt.wrap(i));
        return true;
    }

    private static LuaTable strList(List<String> l) {
        LuaTable t = new LuaTable();
        for (int k = 0; k < l.size(); k++) t.set(k + 1, LuaValue.valueOf(l.get(k)));
        return t;
    }

    private static LuaTable instList(LuaRuntime rt, List<Instance> l) {
        LuaTable t = new LuaTable();
        for (int k = 0; k < l.size(); k++) t.set(k + 1, rt.wrap(l.get(k)));
        return t;
    }

    private static List<Instance> partsOf(Instance m) {
        ArrayList<Instance> out = new ArrayList<>();
        if (m.isA("BasePart")) out.add(m);
        for (Instance d : Instance.descendants$default(m, null, 1, null)) {
            if (d.isA("BasePart")) out.add(d);
        }
        return out;
    }

    private static Vector3 vec(Object o, Vector3 def) {
        return o instanceof Vector3 ? (Vector3) o : def;
    }

    private static float mass(Instance p) {
        Vector3 s = PartData.INSTANCE.size(p);
        Object dens = p.get("__density");
        float d = dens instanceof Number ? ((Number) dens).floatValue() : 0.7f;
        return Math.max(1.0E-4f, s.getX() * s.getY() * s.getZ() * d);
    }

    private static LuaValue[] bounds(Instance m, CFrame orient) {
        List<Instance> parts = partsOf(m);
        if (parts.isEmpty()) {
            return new LuaValue[]{LuaTypes.INSTANCE.wrap(orient), LuaTypes.INSTANCE.wrap(new Vector3(0.0f, 0.0f, 0.0f))};
        }
        CFrame rot = orient.rotationOnly();
        float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
        for (Instance p : parts) {
            CFrame cf = PartData.INSTANCE.cframe(p);
            Vector3 h = PartData.INSTANCE.size(p).times(0.5f);
            for (int c = 0; c < 8; c++) {
                Vector3 local = new Vector3((c & 1) == 0 ? -h.getX() : h.getX(), (c & 2) == 0 ? -h.getY() : h.getY(), (c & 4) == 0 ? -h.getZ() : h.getZ());
                Vector3 w = rot.pointToObject(cf.pointToWorld(local));
                minX = Math.min(minX, w.getX()); minY = Math.min(minY, w.getY()); minZ = Math.min(minZ, w.getZ());
                maxX = Math.max(maxX, w.getX()); maxY = Math.max(maxY, w.getY()); maxZ = Math.max(maxZ, w.getZ());
            }
        }
        Vector3 centerLocal = new Vector3((minX + maxX) * 0.5f, (minY + maxY) * 0.5f, (minZ + maxZ) * 0.5f);
        CFrame center = rot.withPosition(rot.pointToWorld(centerLocal));
        return new LuaValue[]{LuaTypes.INSTANCE.wrap(center), LuaTypes.INSTANCE.wrap(new Vector3(maxX - minX, maxY - minY, maxZ - minZ))};
    }

    private static CFrame pivotOrientation(InstanceApi api, Instance m) {
        try {
            return api.modelPivot(m);
        } catch (RuntimeException e) {
            return CFrame.INSTANCE.getIDENTITY();
        }
    }

    private static List<Instance> jointsOf(LuaRuntime rt, Instance p) {
        ArrayList<Instance> out = new ArrayList<>();
        for (Instance d : Instance.descendants$default(rt.getStudio().getModel(), null, 1, null)) {
            if (!(d.isA("JointInstance") || d.isA("WeldConstraint") || d.isA("Constraint"))) continue;
            Object a = d.get("Part0");
            Object b = d.get("Part1");
            Object c = d.get("Attachment0");
            Object e = d.get("Attachment1");
            boolean hit = a == p || b == p
                    || (c instanceof Instance && ((Instance) c).getParent() == p)
                    || (e instanceof Instance && ((Instance) e).getParent() == p);
            if (hit) out.add(d);
        }
        return out;
    }

    private static LuaValue enumItem(String type, String name) {
        return LuaTypes.INSTANCE.enumItem(type, name);
    }

    private static Instance character(LuaRuntime rt, Instance player) {
        PlaySession ps = rt.getPlay();
        if (ps != null && ps.getPlayer() == player) return ps.getCharacter();
        Object c = player.get("Character");
        return c instanceof Instance ? (Instance) c : null;
    }

    public static Varargs call(InstanceApi api, LuaRuntime rt, Instance inst, String name, Varargs a) {
        String cn = inst.getClassName();
        switch (name) {
            case "FindFirstAncestor": {
                String n = a.checkjstring(2);
                for (Instance p = inst.getParent(); p != null; p = p.getParent()) {
                    if (n.equals(p.getName())) return rt.wrap(p);
                }
                return LuaValue.NIL;
            }
            case "FindFirstAncestorOfClass": {
                String n = a.checkjstring(2);
                for (Instance p = inst.getParent(); p != null; p = p.getParent()) {
                    if (n.equals(p.getClassName())) return rt.wrap(p);
                }
                return LuaValue.NIL;
            }
            case "FindFirstAncestorWhichIsA": {
                String n = a.checkjstring(2);
                for (Instance p = inst.getParent(); p != null; p = p.getParent()) {
                    if (p.isA(n)) return rt.wrap(p);
                }
                return LuaValue.NIL;
            }
            case "FindFirstDescendant": {
                String n = a.checkjstring(2);
                for (Instance d : Instance.descendants$default(inst, null, 1, null)) {
                    if (n.equals(d.getName())) return rt.wrap(d);
                }
                return LuaValue.NIL;
            }
            case "GetActor": {
                for (Instance p = inst; p != null; p = p.getParent()) {
                    if ("Actor".equals(p.getClassName())) return rt.wrap(p);
                }
                return LuaValue.NIL;
            }
            case "GetDebugId": {
                return LuaValue.valueOf(String.format(Locale.ROOT, "%08X", System.identityHashCode(inst)));
            }
            case "SetAttribute": {
                String ak = a.checkjstring(2);
                if (ak.startsWith("RBX") || ak.isEmpty() || ak.length() > 100 || !ak.matches("[A-Za-z0-9_]+")) {
                    throw new LuaError("Attribute name '" + ak + "' is invalid");
                }
                LuaValue av = a.arg(3);
                Object before = inst.getAttributes().get(ak);
                if (av.isnil()) inst.getAttributes().remove(ak);
                else inst.getAttributes().put(ak, rt.fromLua(av, null, inst, ak));
                Object after = inst.getAttributes().get(ak);
                if (before == null ? after != null : !before.equals(after)) {
                    rt.fire(inst, "AttributeChanged", LuaValue.valueOf(ak));
                    rt.fire(inst, "AttributeChanged:" + ak);
                }
                return LuaValue.NONE;
            }
            case "GetAttributes": {
                LuaTable tbl = new LuaTable();
                for (Map.Entry<String, Object> e : inst.getAttributes().entrySet()) {
                    if (e.getKey().startsWith("__")) continue;
                    tbl.set(e.getKey(), rt.toLua(e.getValue(), inst, e.getKey()));
                }
                return tbl;
            }
            case "GetAttribute": {
                String ak = a.checkjstring(2);
                if (ak.startsWith("__")) return LuaValue.NIL;
                Object av = inst.getAttributes().get(ak);
                return av != null ? rt.toLua(av, inst, ak) : LuaValue.NIL;
            }
            case "GetAttributeChangedSignal": {
                return api.signal(inst, "AttributeChanged:" + a.checkjstring(2));
            }
            case "Destroy": {
                boolean detached = inst.getParent() == null;
                inst.destroy();
                if (detached) {
                    if (rt.hasConn(inst, "Destroying")) rt.fire(inst, "Destroying");
                    rt.disconnectTree(inst);
                }
                return LuaValue.NONE;
            }
            default:
                break;
        }
        if (!"CollectionService".equals(cn)) {
            switch (name) {
                case "AddTag": addTag(rt, inst, a.checkjstring(2)); return LuaValue.NONE;
                case "RemoveTag": removeTag(rt, inst, a.checkjstring(2)); return LuaValue.NONE;
                case "HasTag": return LuaValue.valueOf(tags(inst).contains(a.checkjstring(2)));
                case "GetTags": return strList(tags(inst));
                default: break;
            }
        }
        switch (cn) {
            case "CollectionService": {
                switch (name) {
                    case "AddTag": { Instance t = inst(a.arg(2)); if (t != null) addTag(rt, t, a.checkjstring(3)); return LuaValue.NONE; }
                    case "RemoveTag": { Instance t = inst(a.arg(2)); if (t != null) removeTag(rt, t, a.checkjstring(3)); return LuaValue.NONE; }
                    case "HasTag": { Instance t = inst(a.arg(2)); return LuaValue.valueOf(t != null && tags(t).contains(a.checkjstring(3))); }
                    case "GetTags": { Instance t = inst(a.arg(2)); return t == null ? new LuaTable() : strList(tags(t)); }
                    case "GetTagged": {
                        String tag = a.checkjstring(2);
                        ArrayList<Instance> out = new ArrayList<>();
                        for (Instance d : Instance.descendants$default(rt.getStudio().getModel(), null, 1, null)) {
                            if (tags(d).contains(tag)) out.add(d);
                        }
                        return instList(rt, out);
                    }
                    case "GetAllTags": {
                        ArrayList<String> all = new ArrayList<>();
                        for (Instance d : Instance.descendants$default(rt.getStudio().getModel(), null, 1, null)) {
                            for (String tg : tags(d)) if (!all.contains(tg)) all.add(tg);
                        }
                        return strList(all);
                    }
                    default: return null;
                }
            }
            case "RunService": {
                Map<String, Connection> steps;
                synchronized (RENDER_STEPS) {
                    steps = RENDER_STEPS.get(rt);
                    if (steps == null) { steps = new HashMap<>(); RENDER_STEPS.put(rt, steps); }
                }
                if ("BindToRenderStep".equals(name)) {
                    String n = a.checkjstring(2);
                    LuaValue fn = a.checkfunction(4);
                    Connection old = steps.remove(n);
                    if (old != null) rt.disconnect(old);
                    steps.put(n, rt.connect(inst, "RenderStepped", fn, false));
                    return LuaValue.NONE;
                }
                if ("UnbindFromRenderStep".equals(name)) {
                    Connection old = steps.remove(a.checkjstring(2));
                    if (old != null) rt.disconnect(old);
                    return LuaValue.NONE;
                }
                if ("IsEdit".equals(name)) return LuaValue.valueOf(rt.getPlay() == null);
                return null;
            }
            case "TextChannel": {
                return TextChat.channelCall(rt.getPlay(), inst, name, a);
            }
            case "TextService": {
                if ("GetTextSize".equals(name)) {
                    String text = a.checkjstring(2);
                    float size = (float) a.checkdouble(3);
                    Object frame = ud(a.arg(5));
                    float maxW = frame instanceof Vector2 ? ((Vector2) frame).getX() : Float.MAX_VALUE;
                    return LuaTypes.INSTANCE.wrap(measure(text, size, maxW));
                }
                return null;
            }
            case "Players": {
                if ("GetPlayerByUserId".equals(name)) {
                    long id = a.checklong(2);
                    for (Instance ch : inst.getChildren()) {
                        Object u = ch.get("UserId");
                        if ("Player".equals(ch.getClassName()) && u instanceof Number && ((Number) u).longValue() == id) return rt.wrap(ch);
                    }
                    return LuaValue.NIL;
                }
                if ("GetUserIdFromNameAsync".equals(name)) {
                    String n = a.checkjstring(2);
                    for (Instance ch : inst.getChildren()) {
                        Object u = ch.get("UserId");
                        if ("Player".equals(ch.getClassName()) && ch.getName().equalsIgnoreCase(n) && u instanceof Number) return LuaValue.valueOf(((Number) u).doubleValue());
                    }
                    throw new LuaError("Players:GetUserIdFromNameAsync() failed: the user is not in this session and web lookups are not available");
                }
                if ("GetNameFromUserIdAsync".equals(name)) {
                    long id = a.checklong(2);
                    for (Instance ch : inst.getChildren()) {
                        Object u = ch.get("UserId");
                        if ("Player".equals(ch.getClassName()) && u instanceof Number && ((Number) u).longValue() == id) return LuaValue.valueOf(ch.getName());
                    }
                    throw new LuaError("Players:GetNameFromUserIdAsync() failed: the user is not in this session and web lookups are not available");
                }
                if ("GetUserThumbnailAsync".equals(name)) {
                    long id = a.checklong(2);
                    Object t = ud(a.arg(3));
                    Object s = ud(a.arg(4));
                    String type = t instanceof EnumItemV ? ((EnumItemV) t).getName() : "HeadShot";
                    String sz = s instanceof EnumItemV ? ((EnumItemV) s).getName().replace("Size", "") : "420x420";
                    String[] wh = sz.split("x");
                    String thumbType = "AvatarThumbnail".equals(type) ? "Avatar" : ("AvatarBust".equals(type) ? "AvatarBust" : "AvatarHeadShot");
                    String url = "rbxthumb://type=" + thumbType + "&id=" + id + "&w=" + wh[0] + "&h=" + (wh.length > 1 ? wh[1] : wh[0]);
                    return LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf(url), LuaValue.TRUE});
                }
                return null;
            }
            case "Player": {
                switch (name) {
                    case "Kick": {
                        String msg = a.optjstring(2, "");
                        OutputLog.INSTANCE.warn("Player " + inst.getName() + " was kicked" + (msg.isEmpty() ? "" : ": " + msg));
                        final com.goydevv.studrblx.core.Studio st = rt.getStudio();
                        try {
                            new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    st.stopPlay();
                                }
                            });
                        } catch (RuntimeException e) {
                            OutputLog.INSTANCE.error("Kick could not stop the session: " + e);
                        }
                        return LuaValue.NONE;
                    }
                    case "LoadCharacter": {
                        PlaySession ps = rt.getPlay();
                        if (ps == null || ps.getPlayer() != inst) throw new LuaError("LoadCharacter can only be called while the game is running");
                        ps.respawn();
                        return LuaValue.NONE;
                    }
                    case "DistanceFromCharacter": {
                        Vector3 pt = vec(ud(a.arg(2)), null);
                        Instance chr = character(rt, inst);
                        if (pt == null || chr == null) return LuaValue.valueOf(0);
                        Instance head = Instance.findFirstChild$default(chr, "Head", false, 2, null);
                        if (head == null) head = Instance.findFirstChild$default(chr, "HumanoidRootPart", false, 2, null);
                        if (head == null) return LuaValue.valueOf(0);
                        return LuaValue.valueOf(PartData.INSTANCE.cframe(head).getPosition().minus(pt).getMagnitude());
                    }
                    case "GetRankInGroup": return LuaValue.valueOf(0);
                    case "GetRoleInGroup": return LuaValue.valueOf("Guest");
                    case "GetJoinData": {
                        LuaTable t = new LuaTable();
                        t.set("Members", new LuaTable());
                        return t;
                    }
                    case "GetNetworkPing": return LuaValue.valueOf(0);
                    case "ClearCharacterAppearance": {
                        Instance chr = character(rt, inst);
                        if (chr != null) {
                            for (Instance ch : new ArrayList<>(chr.getChildren())) {
                                String c = ch.getClassName();
                                if (ch.isA("Accessory") || ch.isA("Clothing") || "ShirtGraphic".equals(c) || "BodyColors".equals(c) || "CharacterMesh".equals(c)) ch.destroy();
                            }
                        }
                        return LuaValue.NONE;
                    }
                    default: return null;
                }
            }
            case "Humanoid": {
                switch (name) {
                    case "SetStateEnabled": {
                        Object st = ud(a.arg(2));
                        String sn = st instanceof EnumItemV ? ((EnumItemV) st).getName() : a.checkjstring(2);
                        if (a.arg(3).toboolean()) inst.getAttributes().remove("__stateOff:" + sn);
                        else inst.getAttributes().put("__stateOff:" + sn, Boolean.TRUE);
                        return LuaValue.NONE;
                    }
                    case "GetStateEnabled": {
                        Object st = ud(a.arg(2));
                        String sn = st instanceof EnumItemV ? ((EnumItemV) st).getName() : a.checkjstring(2);
                        return LuaValue.valueOf(!inst.getAttributes().containsKey("__stateOff:" + sn));
                    }
                    case "GetAccessories": {
                        ArrayList<Instance> out = new ArrayList<>();
                        Instance chr = inst.getParent();
                        if (chr != null) for (Instance ch : chr.getChildren()) if (ch.isA("Accoutrement")) out.add(ch);
                        return instList(rt, out);
                    }
                    case "GetAppliedDescription": {
                        Instance d = inst.findFirstChildOfClass("HumanoidDescription");
                        if (d == null) {
                            d = InstanceFactory.INSTANCE.create("HumanoidDescription");
                            d.setName("HumanoidDescription");
                            Instance.setParent$default(d, inst, 0, 2, null);
                        }
                        return rt.wrap(d);
                    }
                    default: return null;
                }
            }
            case "GuiService":
            case "UserInputService": {
                if ("GetFocusedTextBox".equals(name)) return LuaValue.NIL;
                if ("GetLastInputType".equals(name)) return enumItem("UserInputType", "Touch");
                if ("GetConnectedGamepads".equals(name)) return new LuaTable();
                return null;
            }
            case "Selection": {
                ArrayList<Instance> sel = rt.getStudio().getSelection();
                if ("Get".equals(name)) return instList(rt, new ArrayList<>(sel));
                if ("Set".equals(name) || "Add".equals(name) || "Remove".equals(name)) {
                    LuaValue lst = a.arg(2);
                    ArrayList<Instance> items = new ArrayList<>();
                    if (lst.istable()) {
                        for (int k = 1; k <= lst.length(); k++) {
                            Instance x = inst(lst.get(k));
                            if (x != null) items.add(x);
                        }
                    }
                    if ("Set".equals(name)) sel.clear();
                    for (Instance x : items) {
                        if ("Remove".equals(name)) sel.remove(x);
                        else if (!sel.contains(x)) sel.add(x);
                    }
                    rt.fire(inst, "SelectionChanged");
                    return LuaValue.NONE;
                }
                return null;
            }
            case "ChangeHistoryService": {
                com.goydevv.studrblx.core.UndoManager um = rt.getStudio().getUndo();
                switch (name) {
                    case "SetWaypoint": return LuaValue.NONE;
                    case "TryBeginRecording": {
                        String id = java.util.UUID.randomUUID().toString();
                        um.begin(a.optjstring(2, "Plugin"));
                        return LuaValue.valueOf(id);
                    }
                    case "FinishRecording": um.commit(); return LuaValue.NONE;
                    case "Undo": um.undo(); return LuaValue.NONE;
                    case "Redo": um.redo(); return LuaValue.NONE;
                    case "GetCanUndo": return LuaValue.valueOf(um.getCanUndo());
                    case "GetCanRedo": return LuaValue.valueOf(um.getCanRedo());
                    case "ResetWaypoints": um.clear(); return LuaValue.NONE;
                    case "SetEnabled": um.setSuspended(!a.arg(2).toboolean()); return LuaValue.NONE;
                    default: return null;
                }
            }
            case "DataModel": {
                if ("IsLoaded".equals(name)) return LuaValue.TRUE;
                if ("BindToClose".equals(name)) {
                    rt.connect(inst, "__BindToClose", a.checkfunction(2), false);
                    return LuaValue.NONE;
                }
                if ("GetJobsInfo".equals(name)) return new LuaTable();
                return null;
            }
            case "MessagingService": {
                Map<String, List<LuaValue>> subs;
                synchronized (SUBSCRIPTIONS) {
                    subs = SUBSCRIPTIONS.get(rt);
                    if (subs == null) { subs = new HashMap<>(); SUBSCRIPTIONS.put(rt, subs); }
                }
                if ("SubscribeAsync".equals(name)) {
                    final String topic = a.checkjstring(2);
                    final LuaValue fn = a.checkfunction(3);
                    List<LuaValue> l = subs.get(topic);
                    if (l == null) { l = new ArrayList<>(); subs.put(topic, l); }
                    l.add(fn);
                    final List<LuaValue> fl = l;
                    LuaTable conn = new LuaTable();
                    conn.set("Connected", LuaValue.TRUE);
                    conn.set("Disconnect", LuaTypesKt.fnv(new kotlin.jvm.functions.Function1<Varargs, Varargs>() {
                        @Override
                        public Varargs invoke(Varargs v) {
                            fl.remove(fn);
                            conn.set("Connected", LuaValue.FALSE);
                            return LuaValue.NONE;
                        }
                    }));
                    return conn;
                }
                if ("PublishAsync".equals(name)) {
                    String topic = a.checkjstring(2);
                    List<LuaValue> l = subs.get(topic);
                    if (l != null) {
                        LuaTable msg = new LuaTable();
                        msg.set("Data", a.arg(3));
                        msg.set("Sent", LuaValue.valueOf(System.currentTimeMillis() / 1000.0d));
                        for (LuaValue fn : new ArrayList<>(l)) {
                            try {
                                fn.call(msg);
                            } catch (LuaError e) {
                                OutputLog.INSTANCE.error(e.getMessage() == null ? "MessagingService callback error" : e.getMessage());
                            }
                        }
                    }
                    return LuaValue.NONE;
                }
                return null;
            }
            case "PolicyService": {
                if ("GetPolicyInfoForPlayerAsync".equals(name)) {
                    LuaTable t = new LuaTable();
                    t.set("AreAdsAllowed", LuaValue.FALSE);
                    t.set("ArePaidRandomItemsRestricted", LuaValue.FALSE);
                    t.set("AllowedExternalLinkReferences", new LuaTable());
                    t.set("IsContentSharingAllowed", LuaValue.TRUE);
                    t.set("IsEligibleToPurchaseSubscription", LuaValue.TRUE);
                    t.set("IsPaidItemTradingAllowed", LuaValue.TRUE);
                    t.set("IsSubjectToChinaPolicies", LuaValue.FALSE);
                    return t;
                }
                return null;
            }
            case "LocalizationService": {
                if ("GetCountryRegionForPlayerAsync".equals(name)) {
                    String c = Locale.getDefault().getCountry();
                    return LuaValue.valueOf(c == null || c.isEmpty() ? "US" : c);
                }
                if ("GetTranslatorForPlayerAsync".equals(name) || "GetTranslatorForLocaleAsync".equals(name)) {
                    LuaTable t = new LuaTable();
                    String loc = Locale.getDefault().toLanguageTag().toLowerCase(Locale.ROOT);
                    t.set("LocaleId", loc);
                    t.set("Translate", LuaTypesKt.fnv(new kotlin.jvm.functions.Function1<Varargs, Varargs>() {
                        @Override
                        public Varargs invoke(Varargs v) {
                            return v.arg(3);
                        }
                    }));
                    t.set("FormatByKey", LuaTypesKt.fnv(new kotlin.jvm.functions.Function1<Varargs, Varargs>() {
                        @Override
                        public Varargs invoke(Varargs v) {
                            throw new LuaError("FormatByKey: no LocalizationTable entry for key " + v.optjstring(2, ""));
                        }
                    }));
                    return t;
                }
                return null;
            }
            case "StudioService": {
                if ("GetUserId".equals(name)) {
                    PlaySession ps = rt.getPlay();
                    Object u = ps != null && ps.getPlayer() != null ? ps.getPlayer().get("UserId") : null;
                    return LuaValue.valueOf(u instanceof Number ? ((Number) u).doubleValue() : 0);
                }
                return null;
            }
            case "MarketplaceService": {
                switch (name) {
                    case "PromptPurchase":
                    case "PromptGamePassPurchase":
                    case "PromptProductPurchase":
                    case "PromptBundlePurchase":
                    case "PromptPremiumPurchase":
                        warnOnce("mps-prompt", "MarketplaceService:" + name + " has no purchase dialog in this app; no purchase was made");
                        return LuaValue.NONE;
                    case "PlayerOwnsAsset":
                    case "UserOwnsGamePassAsync":
                        warnOnce("mps-own", "MarketplaceService:" + name + " cannot reach Roblox ownership data here and reports false");
                        return LuaValue.FALSE;
                    default: return null;
                }
            }
            case "Chat": {
                if ("Chat".equals(name)) {
                    Instance part = inst(a.arg(2));
                    PlaySession ps = rt.getPlay();
                    if (ps != null && part != null) ps.bubble(part, a.checkjstring(3));
                    return LuaValue.NONE;
                }
                if ("FilterStringForBroadcast".equals(name) || "FilterStringAsync".equals(name)) return a.arg(2);
                return null;
            }
            case "PlayerGui": {
                if ("SetTopbarTransparency".equals(name)) return LuaValue.NONE;
                return null;
            }
            case "InsertService": {
                if ("LoadAsset".equals(name) || "LoadAssetVersion".equals(name)) {
                    throw new LuaError("InsertService:" + name + "() is not supported in this app; import the asset from the Toolbox instead");
                }
                return null;
            }
            default:
                break;
        }
        if (inst.isA("Model") && !"Workspace".equals(cn)) {
            switch (name) {
                case "GetBoundingBox": return LuaValue.varargsOf(bounds(inst, pivotOrientation(api, inst)));
                case "GetExtentsSize": return bounds(inst, pivotOrientation(api, inst))[1];
                case "TranslateBy": {
                    Vector3 d = vec(ud(a.arg(2)), null);
                    if (d == null) throw new LuaError("TranslateBy expects a Vector3");
                    api.pivotTo(inst, pivotOrientation(api, inst).plus(d));
                    return LuaValue.NONE;
                }
                case "GetScale": {
                    Object s = inst.getAttributes().get("__scale");
                    return LuaValue.valueOf(s instanceof Number ? ((Number) s).doubleValue() : 1.0d);
                }
                case "ScaleTo": {
                    float target = (float) a.checkdouble(2);
                    if (target <= 0.0f) throw new LuaError("Model:ScaleTo() scale must be positive");
                    Object s = inst.getAttributes().get("__scale");
                    float cur = s instanceof Number ? ((Number) s).floatValue() : 1.0f;
                    float f = target / cur;
                    CFrame pivot = pivotOrientation(api, inst);
                    for (Instance p : partsOf(inst)) {
                        CFrame cf = PartData.INSTANCE.cframe(p);
                        Vector3 local = pivot.pointToObject(cf.getPosition()).times(f);
                        PartData.INSTANCE.setSize(p, PartData.INSTANCE.size(p).times(f));
                        p.set("CFrame", cf.withPosition(pivot.pointToWorld(local)), true);
                    }
                    inst.getAttributes().put("__scale", Float.valueOf(target));
                    return LuaValue.NONE;
                }
                case "BreakJoints": {
                    for (Instance p : partsOf(inst)) for (Instance j : jointsOf(rt, p)) j.destroy();
                    return LuaValue.NONE;
                }
                case "MakeJoints": return LuaValue.NONE;
                default: break;
            }
        }
        if (inst.isA("BasePart")) {
            switch (name) {
                case "ApplyImpulse": {
                    Vector3 imp = vec(ud(a.arg(2)), null);
                    if (imp == null) throw new LuaError("ApplyImpulse expects a Vector3");
                    Vector3 v = vec(api.getK(inst, "AssemblyLinearVelocity"), new Vector3(0.0f, 0.0f, 0.0f));
                    api.setK(inst, "AssemblyLinearVelocity", v.plus(imp.div(mass(inst))));
                    return LuaValue.NONE;
                }
                case "ApplyAngularImpulse": {
                    Vector3 imp = vec(ud(a.arg(2)), null);
                    if (imp == null) throw new LuaError("ApplyAngularImpulse expects a Vector3");
                    Vector3 s = PartData.INSTANCE.size(inst);
                    float inertia = mass(inst) * (s.getX() * s.getX() + s.getY() * s.getY() + s.getZ() * s.getZ()) / 12.0f;
                    Vector3 w = vec(api.getK(inst, "AssemblyAngularVelocity"), new Vector3(0.0f, 0.0f, 0.0f));
                    api.setK(inst, "AssemblyAngularVelocity", w.plus(imp.div(Math.max(1.0E-4f, inertia))));
                    return LuaValue.NONE;
                }
                case "GetVelocityAtPosition": {
                    Vector3 pos = vec(ud(a.arg(2)), null);
                    if (pos == null) throw new LuaError("GetVelocityAtPosition expects a Vector3");
                    Vector3 v = vec(api.getK(inst, "AssemblyLinearVelocity"), new Vector3(0.0f, 0.0f, 0.0f));
                    Vector3 w = vec(api.getK(inst, "AssemblyAngularVelocity"), new Vector3(0.0f, 0.0f, 0.0f));
                    return LuaTypes.INSTANCE.wrap(v.plus(w.cross(pos.minus(PartData.INSTANCE.cframe(inst).getPosition()))));
                }
                case "SetNetworkOwner":
                case "SetNetworkOwnershipAuto":
                    return LuaValue.NONE;
                case "GetNetworkOwner": return LuaValue.NIL;
                case "GetNetworkOwnershipAuto": return LuaValue.TRUE;
                case "CanSetNetworkOwnership": {
                    boolean anchored = Boolean.TRUE.equals(inst.get("Anchored"));
                    return LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf(!anchored), anchored ? LuaValue.valueOf("Cannot change network ownership of anchored parts") : LuaValue.NIL});
                }
                case "GetJoints": return instList(rt, jointsOf(rt, inst));
                case "BreakJoints": {
                    for (Instance j : jointsOf(rt, inst)) j.destroy();
                    return LuaValue.NONE;
                }
                case "MakeJoints": return LuaValue.NONE;
                case "IsGrounded": return LuaValue.valueOf(Boolean.TRUE.equals(inst.get("Anchored")));
                case "Resize": {
                    Object nObj = ud(a.arg(2));
                    int normal = nObj instanceof EnumItemV ? ((EnumItemV) nObj).getValue() : a.checkint(2);
                    float delta = (float) a.checkdouble(3);
                    Vector3 size = PartData.INSTANCE.size(inst);
                    int axis = normal % 3;
                    float sign = normal < 3 ? 1.0f : -1.0f;
                    float cur = size.get(axis);
                    float next = Math.max(0.001f, cur + delta);
                    float applied = next - cur;
                    Vector3 ns = new Vector3(axis == 0 ? next : size.getX(), axis == 1 ? next : size.getY(), axis == 2 ? next : size.getZ());
                    Vector3 shiftLocal = new Vector3(axis == 0 ? sign * applied / 2 : 0, axis == 1 ? sign * applied / 2 : 0, axis == 2 ? sign * applied / 2 : 0);
                    CFrame cf = PartData.INSTANCE.cframe(inst);
                    PartData.INSTANCE.setSize(inst, ns);
                    inst.set("CFrame", cf.withPosition(cf.pointToWorld(shiftLocal)), true);
                    return LuaValue.TRUE;
                }
                default: break;
            }
        }
        if (inst.isA("GuiObject") && "TweenSizeAndPosition".equals(name)) {
            LuaValue self = a.arg(1);
            LuaValue size = a.arg(2), pos = a.arg(3), dir = a.arg(4), style = a.arg(5), time = a.arg(6), over = a.arg(7), cb = a.arg(8);
            Varargs r = api.callMethod("TweenSize", LuaValue.varargsOf(new LuaValue[]{self, size, dir, style, time, over}));
            api.callMethod("TweenPosition", LuaValue.varargsOf(new LuaValue[]{self, pos, dir, style, time, over, cb}));
            return r;
        }
        if ("Camera".equals(cn) && "WorldToScreenPoint".equals(name)) {
            Varargs r = api.callMethod("WorldToViewportPoint", a);
            Object v = ud(r.arg1());
            if (v instanceof Vector3) {
                Vector3 p = (Vector3) v;
                return LuaValue.varargsOf(new LuaValue[]{LuaTypes.INSTANCE.wrap(new Vector3(p.getX(), p.getY() - 36.0f, p.getZ())), r.arg(2)});
            }
            return r;
        }
        return null;
    }

    private static Vector2 measure(String text, float size, float maxW) {
        try {
            android.graphics.Paint paint = new android.graphics.Paint();
            paint.setTextSize(size);
            android.graphics.Paint.FontMetrics fm = paint.getFontMetrics();
            float lineH = Math.max(size, fm.descent - fm.ascent);
            float w = 0;
            int lines = 0;
            for (String line : text.split("\n", -1)) {
                float lw = paint.measureText(line);
                if (maxW > 0 && lw > maxW) {
                    lines += (int) Math.ceil(lw / maxW);
                    w = maxW;
                } else {
                    lines++;
                    w = Math.max(w, lw);
                }
            }
            return new Vector2((float) Math.ceil(w), (float) Math.ceil(lineH * Math.max(1, lines)));
        } catch (RuntimeException e) {
            float w = 0;
            int lines = 0;
            for (String line : text.split("\n", -1)) {
                lines++;
                w = Math.max(w, line.length() * size * 0.5f);
            }
            if (maxW > 0 && w > maxW) {
                lines = (int) Math.ceil(w / maxW);
                w = maxW;
            }
            return new Vector2((float) Math.ceil(w), size * lines);
        }
    }
}
