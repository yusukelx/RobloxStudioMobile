package com.goydevv.studrblx.engine;

import android.content.Context;
import android.util.Log;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.lua.EnumItemV;
import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.lua.LuaTypes;
import com.goydevv.studrblx.lua.LuaTypesKt;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.DataTypesKt;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.FontValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.NumberRange;
import com.goydevv.studrblx.model.OptionalCFrame;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.PhysicalProps;
import com.goydevv.studrblx.model.Ray;
import com.goydevv.studrblx.model.TreeListener;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.render.FlyCamera;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/* JADX INFO: compiled from: PlaySession.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class PlaySession implements TreeListener {
    public static final int AVATAR_PLAYER_CHOICE = 2;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final float CELL;
    private final Set<String> HOLD_TOGETHER;
    private final Set<String> MOVER_CLASSES;
    private final LinkedHashMap<String, ActionButton> actions;
    private volatile Instance activePrompt;
    private float airTime;
    private DefaultAnimate animator;
    private int avatarApplied;
    private final ArrayList<Instance> bigParts;
    private final ArrayList<Body> bodies;
    private final IdentityHashMap<Instance, Body> bodyOf;
    private final ArrayList<Bubble> bubbles;
    private float camDist;
    private float camPitch;
    private float camYaw;
    private final IdentityHashMap<Instance, long[]> cellsOf;
    private float charHalfXZ;
    private float charMaxY;
    private float charMinY;
    private Instance character;
    private final ArrayList<ChatLine> chatLines;
    private volatile int chatVersion;
    private boolean climbing;
    private float bodyPitch;
    private float bodyRoll;
    private float prevRenderFacing;
    private Vector3 groundNormal = new Vector3(0.0f, 1.0f, 0.0f);
    private Vector3 wallBumpNormal = new Vector3(0.0f, 0.0f, 0.0f);
    private float wallBumpDecay;
    private boolean controlsEnabled;
    private boolean coreBackpack;
    private boolean coreChat;
    private boolean coreHealth;
    private boolean corePlayerList;
    private double customAnimateAt;
    private boolean dead;
    private float deadTimer;
    private final ArrayList<Debris> debris;
    private float distTarget;
    private final DataModel dm;
    private boolean dressable;
    private String emote;
    private float emoteTime;
    private Instance equipped;
    private float facing;
    private boolean firstPerson;
    private final HashMap<Long, ArrayList<Instance>> grid;
    private boolean grounded;
    private Instance hrp;
    private Instance humanoid;
    private float idleT;
    private boolean isR15;
    private final ArrayList<Pair<Joint, Boolean>> jointOrder;
    private float jumpAnimTime;
    private boolean jumpHeld;
    private boolean jumpRequested;
    private volatile float keyTurn;
    private double lastCamInput;
    private Vector3 lastDir;
    private Vector2 lastPointer;
    private String lastState;
    private volatile Vector2 lastTouchDp;
    private CFrame lastWritten;
    private final HashMap<String, Float> motorAngle;
    private LuaTable mouseTable;
    private Vector3 moveToTarget;
    private final ArrayList<Instance> movers;
    private float occluded;
    private final IdentityHashMap<Instance, CFrame> offsets;
    private boolean physicsDirty;
    private volatile float pitchTarget;
    private Instance playAsTemplate;
    private Float playAsYaw;
    private Instance player;
    private Vector3 pos;
    private final float[] prof;
    private int profN;
    private float profT;
    private ArrayList<Instance> prompts;
    private final HashMap<String, float[]> r15Pose;
    private float renderFacing;
    private final IdentityHashMap<Instance, Pair<Instance, List<RigJoint>>> rigJoints;
    private final IdentityHashMap<Instance, AnimPlayer> rigPlayers;
    private final IdentityHashMap<Instance, CFrame> rigid;
    private final LuaRuntime rt;
    private Vector3 scriptMoveDir;
    private final ArrayList<ScriptTrack> scriptTracks;
    private float seatCooldown;
    private Instance seated;
    private boolean shiftLock;
    private final Set<Instance> startedScripts;
    private final Set<Instance> statics;
    private final Studio studio;
    private float subjectY;
    private double time;
    private final IdentityHashMap<Instance, CFrame> toolOffsets;
    private final IdentityHashMap<Instance, Set<Instance>> touching;
    private Vector3 vel;
    private float walkT;
    private boolean writing;
    private volatile float yawTarget;

    public PlaySession(Studio studio, DataModel dm) {
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(dm, "dm");
        this.studio = studio;
        this.dm = dm;
        LuaRuntime luaRuntime = new LuaRuntime(this.studio, this.dm, true);
        luaRuntime.setPlay(this);
        this.rt = luaRuntime;
        this.pos = new Vector3(0.0f, 10.0f, 0.0f);
        this.vel = Vector3.INSTANCE.getZERO();
        this.lastDir = Vector3.INSTANCE.getZERO();
        this.charMinY = -3.0f;
        this.charMaxY = 2.0f;
        this.charHalfXZ = 1.0f;
        this.offsets = new IdentityHashMap<>();
        this.controlsEnabled = true;
        this.camPitch = -0.35f;
        this.camDist = 14.0f;
        this.pitchTarget = -0.35f;
        this.distTarget = 14.0f;
        this.occluded = 1000.0f;
        this.subjectY = Float.NaN;
        this.lastCamInput = -10.0d;
        this.actions = new LinkedHashMap<>();
        this.prompts = new ArrayList<>();
        this.toolOffsets = new IdentityHashMap<>();
        this.chatLines = new ArrayList<>();
        this.bubbles = new ArrayList<>();
        this.coreChat = true;
        this.corePlayerList = true;
        this.coreBackpack = true;
        this.coreHealth = true;
        this.movers = new ArrayList<>();
        this.bodies = new ArrayList<>();
        this.bodyOf = new IdentityHashMap<>();
        this.statics = Collections.newSetFromMap(new IdentityHashMap());
        this.grid = new HashMap<>();
        this.cellsOf = new IdentityHashMap<>();
        this.bigParts = new ArrayList<>();
        this.physicsDirty = true;
        this.touching = new IdentityHashMap<>();
        this.startedScripts = Collections.newSetFromMap(new IdentityHashMap());
        this.CELL = 32.0f;
        this.HOLD_TOGETHER = SetsKt.setOf(new String[]{"RigidConstraint", "HingeConstraint", "BallSocketConstraint", "CylindricalConstraint", "PrismaticConstraint", "UniversalConstraint", "TorsionSpringConstraint", "PlaneConstraint", "SpringConstraint"});
        this.MOVER_CLASSES = SetsKt.setOf(new String[]{"BodyForce", "BodyThrust", "BodyVelocity", "BodyPosition", "RocketPropulsion", "VectorForce", "LineForce", "LinearVelocity", "AlignPosition", "SpringConstraint", "RopeConstraint", "RodConstraint"});
        this.jointOrder = new ArrayList<>();
        this.rigid = new IdentityHashMap<>();
        this.avatarApplied = -1;
        this.motorAngle = new HashMap<>();
        this.r15Pose = new HashMap<>();
        this.lastState = "Running";
        this.scriptTracks = new ArrayList<>();
        this.rigPlayers = new IdentityHashMap<>();
        this.customAnimateAt = -100.0d;
        this.rigJoints = new IdentityHashMap<>();
        this.debris = new ArrayList<>();
        this.prof = new float[8];
    }

    public final DataModel getDm() {
        return this.dm;
    }

    public final Studio getStudio() {
        return this.studio;
    }

    public final LuaRuntime getRt() {
        return this.rt;
    }

    public final Instance getPlayer() {
        return this.player;
    }

    public final void setPlayer(Instance instance) {
        this.player = instance;
    }

    public final Instance getCharacter() {
        return this.character;
    }

    public final void setCharacter(Instance instance) {
        this.character = instance;
    }

    public final boolean getControlsEnabled() {
        return this.controlsEnabled;
    }

    public final void setControlsEnabled(boolean z) {
        this.controlsEnabled = z;
    }

    public final float getCamYaw() {
        return this.camYaw;
    }

    public final void setCamYaw(float f) {
        this.camYaw = f;
    }

    public final float getCamPitch() {
        return this.camPitch;
    }

    public final void setCamPitch(float f) {
        this.camPitch = f;
    }

    public final float getCamDist() {
        return this.camDist;
    }

    public final void setCamDist(float f) {
        this.camDist = f;
    }

    public final float getKeyTurn() {
        return this.keyTurn;
    }

    public final void setKeyTurn(float f) {
        this.keyTurn = f;
    }

    public final boolean getShiftLock() {
        return this.shiftLock;
    }

    public final boolean getFirstPerson() {
        return this.firstPerson;
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    public static final class ActionButton {
        private final LuaValue fn;
        private final String name;
        private String title;

        public ActionButton(String name, LuaValue fn, String title) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(fn, "fn");
            Intrinsics.checkNotNullParameter(title, "title");
            this.name = name;
            this.fn = fn;
            this.title = title;
        }

        public final LuaValue getFn() {
            return this.fn;
        }

        public final String getName() {
            return this.name;
        }

        public final String getTitle() {
            return this.title;
        }

        public final void setTitle(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.title = str;
        }
    }

    public final Vector2 getLastTouchDp() {
        return this.lastTouchDp;
    }

    public final void setLastTouchDp(Vector2 vector2) {
        this.lastTouchDp = vector2;
    }

    public final LinkedHashMap<String, ActionButton> getActions() {
        return this.actions;
    }

    public final Instance getActivePrompt() {
        return this.activePrompt;
    }

    public final void setActivePrompt(Instance instance) {
        this.activePrompt = instance;
    }

    public final Instance getEquipped() {
        return this.equipped;
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    public static final class ChatLine {
        private final int color;
        private final String from;
        private final boolean system;
        private final String text;
        private final long time;

        public ChatLine(String from, String text, int color, boolean system, long time) {
            Intrinsics.checkNotNullParameter(text, "text");
            this.from = from;
            this.text = text;
            this.color = color;
            this.system = system;
            this.time = time;
        }

        public /* synthetic */ ChatLine(String str, String str2, int i, boolean z, long j, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, str2, i, z, (i2 & 16) != 0 ? System.currentTimeMillis() : j);
        }

        public final int getColor() {
            return this.color;
        }

        public final String getFrom() {
            return this.from;
        }

        public final boolean getSystem() {
            return this.system;
        }

        public final String getText() {
            return this.text;
        }

        public final long getTime() {
            return this.time;
        }
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    public static final class Bubble {
        private float age;
        private final Instance part;
        private final String text;

        public Bubble(Instance part, String text, float age) {
            Intrinsics.checkNotNullParameter(part, "part");
            Intrinsics.checkNotNullParameter(text, "text");
            this.part = part;
            this.text = text;
            this.age = age;
        }

        public /* synthetic */ Bubble(Instance instance, String str, float f, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(instance, str, (i & 4) != 0 ? 0.0f : f);
        }

        public final float getAge() {
            return this.age;
        }

        public final Instance getPart() {
            return this.part;
        }

        public final String getText() {
            return this.text;
        }

        public final void setAge(float f) {
            this.age = f;
        }

        public int bgColor = 0xFAFAFA;
        public float bgTransparency = 0.1f;
        public int textColor = 0x393B3D;
        public float textSize = 16.0f;
        public boolean tail = true;
        public String fontFamily = "BuilderSans";
        public int fontWeight = 500;
        public boolean italic = false;
    }

    public final ArrayList<ChatLine> getChatLines() {
        return this.chatLines;
    }

    public final ArrayList<Bubble> getBubbles() {
        return this.bubbles;
    }

    public final int getChatVersion() {
        return this.chatVersion;
    }

    public final void setChatVersion(int i) {
        this.chatVersion = i;
    }

    public final boolean getCoreChat() {
        return this.coreChat;
    }

    public final void setCoreChat(boolean z) {
        this.coreChat = z;
    }

    public final boolean getCorePlayerList() {
        return this.corePlayerList;
    }

    public final void setCorePlayerList(boolean z) {
        this.corePlayerList = z;
    }

    public final boolean getCoreBackpack() {
        return this.coreBackpack;
    }

    public final void setCoreBackpack(boolean z) {
        this.coreBackpack = z;
    }

    public final boolean getCoreHealth() {
        return this.coreHealth;
    }

    public final void setCoreHealth(boolean z) {
        this.coreHealth = z;
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    private static final class Body {
        private float elasticity;
        private float friction;
        private boolean hasBox;
        private float mass;
        private Vector3 max;
        private Vector3 min;
        private final List<Instance> parts;
        private float restTime;
        private boolean sleeping;
        private Vector3 vel;

        /* JADX WARN: Multi-variable type inference failed */
        public Body(List<? extends Instance> parts, Vector3 vel) {
            Intrinsics.checkNotNullParameter(parts, "parts");
            Intrinsics.checkNotNullParameter(vel, "vel");
            this.parts = (List<Instance>) parts;
            this.vel = vel;
            this.mass = 1.0f;
            this.friction = 0.3f;
            this.elasticity = 0.5f;
            this.min = Vector3.INSTANCE.getZERO();
            this.max = Vector3.INSTANCE.getZERO();
        }

        public final List<Instance> getParts() {
            return this.parts;
        }

        public final Vector3 getVel() {
            return this.vel;
        }

        public final void setVel(Vector3 vector3) {
            Intrinsics.checkNotNullParameter(vector3, "<set-?>");
            this.vel = vector3;
        }

        public final float getRestTime() {
            return this.restTime;
        }

        public final boolean getSleeping() {
            return this.sleeping;
        }

        public final void setRestTime(float f) {
            this.restTime = f;
        }

        public final void setSleeping(boolean z) {
            this.sleeping = z;
        }

        public final float getMass() {
            return this.mass;
        }

        public final void setMass(float f) {
            this.mass = f;
        }

        public final float getElasticity() {
            return this.elasticity;
        }

        public final float getFriction() {
            return this.friction;
        }

        public final void setElasticity(float f) {
            this.elasticity = f;
        }

        public final void setFriction(float f) {
            this.friction = f;
        }

        public final boolean getHasBox() {
            return this.hasBox;
        }

        public final Vector3 getMax() {
            return this.max;
        }

        public final Vector3 getMin() {
            return this.min;
        }

        public final void setHasBox(boolean z) {
            this.hasBox = z;
        }

        public final void setMax(Vector3 vector3) {
            Intrinsics.checkNotNullParameter(vector3, "<set-?>");
            this.max = vector3;
        }

        public final void setMin(Vector3 vector3) {
            Intrinsics.checkNotNullParameter(vector3, "<set-?>");
            this.min = vector3;
        }
    }

    private final Instance getRunService() {
        Instance service = this.dm.getService("RunService");
        service.setArchivable(false);
        return service;
    }

    private final float getGravity() {
        Object obj = this.dm.getWorkspace().get("Gravity");
        Number number = obj instanceof Number ? (Number) obj : null;
        if (number != null) {
            return number.floatValue();
        }
        return 196.2f;
    }

    public final void start() {
        String str;
        Iterable iterableDescendants$default;
        Iterable children;
        Instance instanceFindFirstChild$default;
        Iterable children2;
        this.dm.getListeners().add(this);
        OutputLog.INSTANCE.info("Playtest started");
        installBridge();
        setupTextChat();
        for (Instance s : Instance.descendants$default(this.dm, null, 1, null)) {
            if (Intrinsics.areEqual(s.getClassName(), "Script")) {
                maybeRunScript(s);
            }
        }
        Instance players = this.dm.getService("Players");
        Instance p = InstanceFactory.INSTANCE.create("Player");
        Context ctx = this.studio.getApp();
        boolean signedIn = (RobloxApi.INSTANCE.isGuest(ctx) || RobloxApi.INSTANCE.userId(ctx) == 0) ? false : true;
        String uname = "Player1";
        if (signedIn) {
            String strUserName = RobloxApi.INSTANCE.userName(ctx);
            if (!(strUserName.length() == 0)) {
                uname = strUserName;
            }
        }
        p.setName(uname);
        p.set("UserId", Long.valueOf(signedIn ? RobloxApi.INSTANCE.userId(ctx) : 0L), false);
        p.getPropTypes().put("UserId", 27);
        if (signedIn) {
            String strDisplayName = RobloxApi.INSTANCE.displayName(ctx);
            if (strDisplayName.length() == 0) {
                strDisplayName = uname;
            }
            str = strDisplayName;
        } else {
            str = uname;
        }
        p.set("DisplayName", str, false);
        p.set("Character", null, false);
        p.set("Team", null, false);
        p.set("Neutral", true, false);
        p.set("AccountAge", 365, false);
        Instance sp = this.dm.findService("StarterPlayer");
        Object obj = sp != null ? sp.get("CameraMaxZoomDistance") : null;
        Number number = obj instanceof Number ? (Number) obj : null;
        p.set("CameraMaxZoomDistance", Float.valueOf(number != null ? number.floatValue() : 128.0f), false);
        Object obj2 = sp != null ? sp.get("CameraMinZoomDistance") : null;
        Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
        p.set("CameraMinZoomDistance", Float.valueOf(number2 != null ? number2.floatValue() : 0.5f), false);
        Object obj3 = sp != null ? sp.get("CameraMode") : null;
        Object enumValue = obj3 instanceof EnumValue ? (EnumValue) obj3 : null;
        if (enumValue == null) {
            enumValue = new EnumValue(0);
        }
        p.set("CameraMode", enumValue, false);
        Instance instanceCreate = InstanceFactory.INSTANCE.create("Backpack");
        instanceCreate.setName("Backpack");
        instanceCreate.attachRaw(p);
        Instance instanceCreate2 = InstanceFactory.INSTANCE.create("PlayerGui");
        instanceCreate2.setName("PlayerGui");
        instanceCreate2.attachRaw(p);
        Instance ps = InstanceFactory.INSTANCE.create("PlayerScripts");
        ps.setName("PlayerScripts");
        ps.attachRaw(p);
        addPlayerModule(ps);
        this.player = p;
        Instance.setParent$default(p, players, 0, 2, null);
        this.rt.fire(players, "PlayerAdded", this.rt.wrap(p));
        if (sp != null && (instanceFindFirstChild$default = Instance.findFirstChild$default(sp, "StarterPlayerScripts", false, 2, null)) != null && (children2 = instanceFindFirstChild$default.getChildren()) != null) {
            Iterable<Instance> iterable = children2;
            for (Instance instance : iterable) {
                if (!Intrinsics.areEqual(instance.getName(), "PlayerModule")) {
                    Instance.setParent$default(instance.cloneTree(), Instance.findFirstChild$default(p, "PlayerScripts", false, 2, null), 0, 2, null);
                }
            }
        }
        resetGui(true);
        Instance instanceFindService = this.dm.findService("StarterPack");
        if (instanceFindService != null && (children = instanceFindService.getChildren()) != null) {
            Iterable iterable2 = children;
            int i = 0;
            for (Iterator it = iterable2.iterator(); it.hasNext(); it = it) {
                Instance.setParent$default(((Instance) it.next()).cloneTree(), Instance.findFirstChild$default(p, "Backpack", false, 2, null), 0, 2, null);
            }
        }
        Instance instanceFindService2 = this.dm.findService("ReplicatedFirst");
        if (instanceFindService2 != null && (iterableDescendants$default = Instance.descendants$default(instanceFindService2, null, 1, null)) != null) {
            Iterator it2 = iterableDescendants$default.iterator();
            while (it2.hasNext()) {
                maybeRunScript((Instance) it2.next());
            }
        }
        runScriptsUnder(ps);
        for (Instance script : Instance.descendants$default(this.dm, null, 1, null)) {
            if (Intrinsics.areEqual(script.getClassName(), "Script")) maybeRunScript(script);
        }
        spawnCharacter();
        systemMessage("Chat '/?' or '/help' for a list of chat commands.", -4934476);
    }

    public final void stop() {
        com.goydevv.studrblx.audio.CharacterSounds.stopRun();
        this.dm.getListeners().remove(this);
        this.rt.shutdown();
        OutputLog.INSTANCE.info("Playtest stopped");
    }

    private final void resetGui(boolean initial) {
        Instance pg;
        Iterable<Instance> children;
        Instance instance = this.player;
        if (instance == null || (pg = Instance.findFirstChild$default(instance, "PlayerGui", false, 2, null)) == null) {
            return;
        }
        if (!initial) {
            for (Instance g : CollectionsKt.toList(pg.getChildren())) {
                if (!Intrinsics.areEqual(g.get("ResetOnSpawn"), (Object) false)) {
                    g.destroy();
                }
            }
        }
        Instance instanceFindService = this.dm.findService("StarterGui");
        if (instanceFindService == null || (children = instanceFindService.getChildren()) == null) {
            return;
        }
        for (Instance instance2 : children) {
            if (initial || !Intrinsics.areEqual(instance2.get("ResetOnSpawn"), (Object) false)) {
                Instance.setParent$default(instance2.cloneTree(), pg, 0, 2, null);
            }
        }
        runScriptsUnder(pg);
    }

    private final void installBridge() {
        LuaTable b = new LuaTable();
        b.set("setControlsEnabled", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlaySession.installBridge$lambda$11(PlaySession.this, (Varargs) obj);
            }
        }));
        b.set("getMoveVector", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlaySession.installBridge$lambda$12(PlaySession.this, (Varargs) obj);
            }
        }));
        b.set("setShiftLock", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlaySession.installBridge$lambda$13(PlaySession.this, (Varargs) obj);
            }
        }));
        b.set("isShiftLock", LuaTypesKt.fnv(new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlaySession.installBridge$lambda$14(PlaySession.this, (Varargs) obj);
            }
        }));
        this.rt.setGlobal("__MobileStudioPlayer", b);
    }

    static final Varargs installBridge$lambda$11(PlaySession this$0, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        this$0.controlsEnabled = a.arg1().toboolean();
        if (!this$0.controlsEnabled) {
            this$0.lastDir = Vector3.INSTANCE.getZERO();
        }
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs installBridge$lambda$12(PlaySession this$0, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        return LuaTypes.INSTANCE.wrap(this$0.controlsEnabled ? new Vector3(this$0.studio.getMoveX(), 0.0f, this$0.studio.getMoveY()) : Vector3.INSTANCE.getZERO());
    }

    static final Varargs installBridge$lambda$13(PlaySession this$0, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        this$0.shiftLock = a.arg1().toboolean();
        LuaValue NONE = LuaValue.NONE;
        Intrinsics.checkNotNullExpressionValue(NONE, "NONE");
        return NONE;
    }

    static final Varargs installBridge$lambda$14(PlaySession this$0, Varargs varargs) {
        Intrinsics.checkNotNullParameter(varargs, "<unused var>");
        LuaBoolean luaBooleanValueOf = LuaValue.valueOf(this$0.shiftLock);
        Intrinsics.checkNotNullExpressionValue(luaBooleanValueOf, "valueOf(...)");
        return luaBooleanValueOf;
    }

    private static final Instance addPlayerModule$module(String name, String src, Instance parent) {
        Instance m = InstanceFactory.INSTANCE.create("ModuleScript");
        m.setName(name);
        m.set("Source", StringsKt.trimIndent(src) + "\n", false);
        m.attachRaw(parent);
        return m;
    }

    private final void addPlayerModule(Instance ps) {
        Instance pm = addPlayerModule$module("PlayerModule", "\n            local bridge = __MobileStudioPlayer\n            local Controls = {}\n            Controls.__index = Controls\n            function Controls:Enable(enable) bridge.setControlsEnabled(enable ~= false) end\n            function Controls:Disable() bridge.setControlsEnabled(false) end\n            function Controls:GetMoveVector() return bridge.getMoveVector() end\n            function Controls:GetActiveController() return self end\n            function Controls:SelectTouchModule() end\n            function Controls:GetIsJumping() return false end\n            local controls = setmetatable({}, Controls)\n            local Cameras = {}\n            Cameras.__index = Cameras\n            function Cameras:GetCameraControlChoice() return nil end\n            function Cameras:SetIsMouseLocked(on) bridge.setShiftLock(on) end\n            function Cameras:GetIsMouseLocked() return bridge.isShiftLock() end\n            local cameras = setmetatable({}, Cameras)\n            local PlayerModule = {}\n            function PlayerModule:GetControls() return controls end\n            function PlayerModule:GetCameras() return cameras end\n            function PlayerModule:GetClickToMoveController() return controls end\n            return PlayerModule\n        ", ps);
        addPlayerModule$module("ControlModule", "local pm = require(script.Parent)\nreturn pm:GetControls()", pm);
        addPlayerModule$module("CameraModule", "local pm = require(script.Parent)\nreturn pm:GetCameras()", pm);
    }

    private final void setupTextChat() {
        Instance tcs = this.dm.getService("TextChatService");
        Instance folder = Instance.findFirstChild$default(tcs, "TextChannels", false, 2, null);
        if (folder == null) {
            folder = InstanceFactory.INSTANCE.create("Folder");
            folder.setName("TextChannels");
            folder.setArchivable(false);
            folder.attachRaw(tcs);
        }
        ensureChatConfigs(tcs);
        for (String n : CollectionsKt.listOf(new String[]{"RBXGeneral", "RBXSystem"})) {
            if (Instance.findFirstChild$default(folder, n, false, 2, null) == null) {
                Instance instanceCreate = InstanceFactory.INSTANCE.create("TextChannel");
                instanceCreate.setName(n);
                instanceCreate.setArchivable(false);
                instanceCreate.attachRaw(folder);
            }
        }
    }

    public static void ensureChatConfigs(Instance tcs) {
        ensureChatConfig(tcs, "ChatWindowConfiguration", new Object[]{"Enabled", true, "BackgroundColor3", new Color3(0.0980f, 0.1059f, 0.1137f), "BackgroundTransparency", 0.3f, "TextColor3", new Color3(1.0f, 1.0f, 1.0f), "TextSize", 14.0f, "TextStrokeColor3", new Color3(0.0f, 0.0f, 0.0f), "TextStrokeTransparency", 0.5f, "HeightScale", 1.0f, "WidthScale", 1.0f, "HorizontalAlignment", new EnumValue(0), "VerticalAlignment", new EnumValue(0)});
        ensureChatConfig(tcs, "ChatInputBarConfiguration", new Object[]{"Enabled", true, "BackgroundColor3", new Color3(0.0980f, 0.1059f, 0.1137f), "BackgroundTransparency", 0.2f, "TextColor3", new Color3(1.0f, 1.0f, 1.0f), "PlaceholderColor3", new Color3(0.698f, 0.698f, 0.698f), "TextSize", 14.0f, "TextStrokeColor3", new Color3(0.0f, 0.0f, 0.0f), "TextStrokeTransparency", 0.5f, "AutocompleteEnabled", true});
        ensureChatConfig(tcs, "BubbleChatConfiguration", new Object[]{"Enabled", true, "BackgroundColor3", new Color3(0.9804f, 0.9804f, 0.9804f), "BackgroundTransparency", 0.1f, "TextColor3", new Color3(0.2235f, 0.2314f, 0.2392f), "TextSize", 16.0f, "MaxDistance", 100.0f, "BubbleDuration", 15.0f, "BubblesSpacing", 6.0f, "VerticalStudsOffset", 0.0f, "TailVisible", true, "AdorneeName", "Head", "MaxBubbles", 3.0f, "LocalPlayerStudsOffset", new Vector3(0.0f, 0.0f, 0.0f), "FontFace", new FontValue("rbxasset://fonts/families/BuilderSans.json", 500, 0, ""), "TextStrokeTransparency", 1.0f});
        ensureChatConfig(tcs, "ChannelTabsConfiguration", new Object[]{"Enabled", false, "BackgroundColor3", new Color3(0.0980f, 0.1059f, 0.1137f), "BackgroundTransparency", 0.0f, "TextColor3", new Color3(0.6863f, 0.6863f, 0.6863f), "SelectedTabTextColor3", new Color3(1.0f, 1.0f, 1.0f), "TextSize", 18.0f});
    }

    private static void ensureChatConfig(Instance tcs, String cls, Object[] defaults) {
        Instance cfg = Instance.findFirstChild$default(tcs, cls, false, 2, null);
        if (cfg == null) {
            cfg = InstanceFactory.INSTANCE.create(cls);
            cfg.setName(cls);
            cfg.attachRaw(tcs);
        }
        for (int i = 0; i + 1 < defaults.length; i += 2) {
            String k = (String) defaults[i];
            if (!cfg.getProps().containsKey(k)) {
                cfg.set(k, defaults[i + 1], false);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:35:0x0084  */
    private final boolean isRunnable(Instance s) {
        boolean inBackpack;
        boolean z;
        if (Intrinsics.areEqual(s.get("Disabled"), (Object) true)) {
            return false;
        }
        Object obj = s.get("RunContext");
        EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
        int ctx = enumValue != null ? enumValue.getValue() : 0;
        Instance p = this.player;
        boolean clientPlaces = isRunnable$underInst(s, p) || isRunnable$underInst(s, this.character) || isRunnable$under(s, "ReplicatedFirst");
        if (p != null) {
            Iterator it = SequencesKt.generateSequence(s.getParent(), (Function1<? super Instance, ? extends Instance>) new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda7
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj2) {
                    return PlaySession.isRunnable$lambda$19((Instance) obj2);
                }
            }).iterator();
            while (true) {
                if (it.hasNext()) {
                    Instance instance = (Instance) it.next();
                    if (Intrinsics.areEqual(instance.getClassName(), "Backpack") && instance.getParent() == p) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (z) {
                inBackpack = true;
            } else {
                inBackpack = false;
            }
        } else {
            inBackpack = false;
        }
        String className = s.getClassName();
        if (!Intrinsics.areEqual(className, "Script")) {
            if (Intrinsics.areEqual(className, "LocalScript")) {
                return clientPlaces;
            }
            return false;
        }
        switch (ctx) {
            case 0:
                return isRunnable$under(s, "Workspace") || isRunnable$under(s, "ServerScriptService") || inBackpack;
            case 1:
                return isRunnable$under(s, "Workspace") || isRunnable$under(s, "ServerScriptService") || isRunnable$under(s, "ReplicatedStorage") || inBackpack;
            case 2:
                return p != null && (clientPlaces || isRunnable$under(s, "Workspace") || isRunnable$under(s, "ReplicatedStorage"));
            default:
                return false;
        }
    }

    private static final boolean isRunnable$under(Instance $s, String cls) {
        Iterator it = SequencesKt.generateSequence($s.getParent(), (Function1<? super Instance, ? extends Instance>) new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PlaySession.isRunnable$under$lambda$17((Instance) obj);
            }
        }).iterator();
        while (it.hasNext()) {
            if (Intrinsics.areEqual(((Instance) it.next()).getClassName(), cls)) {
                return true;
            }
        }
        return false;
    }

    static final Instance isRunnable$under$lambda$17(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getParent();
    }

    private static final boolean isRunnable$underInst(Instance $s, Instance i) {
        return i != null && $s.isDescendantOf(i);
    }

    static final Instance isRunnable$lambda$19(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getParent();
    }

    private final void runScriptsUnder(Instance root) {
        if (root == null) return;
        for (Instance script : Instance.descendants$default(root, null, 1, null)) {
            maybeRunScript(script);
        }
    }

    public final void maybeRunScript(Instance s) {
        Intrinsics.checkNotNullParameter(s, "s");
        if (this.rt.getStopped() || this.startedScripts.contains(s) || !s.isA("BaseScript") || !isRunnable(s)) {
            return;
        }
        this.startedScripts.add(s);
        try { LuaRuntime.runScript$default(this.rt, s, null, 2, null); } catch (Throwable _ktEx) { com.goydevv.studrblx.core.OutputLog.INSTANCE.error("Script error (" + s.fullName() + "): " + _ktEx.getMessage()); }
    }

    public final void onChildAdded(Instance parent, Instance child) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(child, "child");
        maybeRunScript(child);
        for (Instance d : Instance.descendants$default(child, null, 1, null)) {
            maybeRunScript(d);
        }
    }

    public final void onChildRemoved(Instance parent, Instance child) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(child, "child");
    }

    private final boolean physicsRelevant(Instance i) {
        if (i.isA("BasePart") || i.isA("JointInstance") || Intrinsics.areEqual(i.getClassName(), "WeldConstraint") || i.isA("Constraint") || i.isA("BodyMover") || Intrinsics.areEqual(i.getClassName(), "Attachment") || i.isA("Humanoid")) {
            return true;
        }
        Iterator<Instance> it = i.getChildren().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance c = it.next();
            if (physicsRelevant(c)) {
                return true;
            }
        }
        return false;
    }

    private final boolean inWorkspace(Instance i) {
        Instance ws = this.dm.getWorkspace();
        return i != null && (i == ws || i.isDescendantOf(ws));
    }

    @Override // com.goydevv.studrblx.model.TreeListener
    public void childAdded(Instance parent, Instance child) {
        boolean z;
        boolean z2;
        boolean inModel;
        int i;
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(child, "child");
        if (this.physicsDirty || !inWorkspace(parent) || !physicsRelevant(child)) {
            return;
        }
        Iterable sub = CollectionsKt.plus((Collection) CollectionsKt.listOf(child), (Iterable) Instance.descendants$default(child, null, 1, null));
        Iterable iterable = sub;
        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
            Iterator it = iterable.iterator();
            while (true) {
                if (it.hasNext()) {
                    Instance instance = (Instance) it.next();
                    if (instance.isA("JointInstance") || Intrinsics.areEqual(instance.getClassName(), "WeldConstraint") || instance.isA("Constraint") || instance.isA("BodyMover") || instance.isA("Humanoid")) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
        } else {
            z = false;
        }
        if (z) {
            this.physicsDirty = true;
            return;
        }
        Collection arrayList = new ArrayList();
        for (Object obj : sub) {
            Instance instance2 = (Instance) obj;
            if (instance2.isA("BasePart") && !Intrinsics.areEqual(instance2.getClassName(), "Terrain")) {
                arrayList.add(obj);
            }
        }
        List<Instance> parts = (List) arrayList;
        List list = parts;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            Iterator it2 = list.iterator();
            while (true) {
                if (it2.hasNext()) {
                    Instance instance3 = (Instance) it2.next();
                    if (!(Intrinsics.areEqual(instance3.get("Anchored"), (Object) true) && !isCharacterPart(instance3))) {
                        z2 = false;
                        break;
                    }
                } else {
                    z2 = true;
                    break;
                }
            }
        } else {
            z2 = true;
        }
        if (z2) {
            for (Instance p : parts) {
                if (this.statics.add(p)) {
                    insertGrid(p);
                }
            }
            return;
        }
        Iterator it3 = SequencesKt.takeWhile(SequencesKt.generateSequence(child.getParent(), (Function1<? super Instance, ? extends Instance>) new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda13
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return PlaySession.childAdded$lambda$24((Instance) obj2);
            }
        }), new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return Boolean.valueOf(PlaySession.childAdded$lambda$25(PlaySession.this, (Instance) obj2));
            }
        }).iterator();
        while (true) {
            if (it3.hasNext()) {
                if (((Instance) it3.next()).isA("Model")) {
                    inModel = true;
                    break;
                }
            } else {
                inModel = false;
                break;
            }
        }
        if (child.isA("BasePart")) {
            Iterable children = child.getChildren();
            if (!(children instanceof Collection) || !((Collection) children).isEmpty()) {
                Iterator it4 = children.iterator();
                while (true) {
                    if (it4.hasNext()) {
                        if (((Instance) it4.next()).isA("BasePart")) {
                            i = 0;
                            break;
                        }
                    } else {
                        i = 1;
                        break;
                    }
                }
            } else {
                i = 1;
            }
            if (i != 0 && !inModel && !Intrinsics.areEqual(child.get("Anchored"), (Object) true) && !isCharacterPart(child)) {
                Body b = new Body(CollectionsKt.listOf(child), Vector3.INSTANCE.getZERO());
                measure(b);
                this.bodies.add(b);
                this.bodyOf.put(child, b);
                return;
            }
        }
        this.physicsDirty = true;
    }

    static final Instance childAdded$lambda$24(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getParent();
    }

    static final boolean childAdded$lambda$25(PlaySession this$0, Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it != this$0.dm.getWorkspace();
    }

    @Override // com.goydevv.studrblx.model.TreeListener
    public void childRemoved(Instance parent, Instance child) {
        boolean z;
        Instance instance;
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(child, "child");
        if (this.physicsDirty || !inWorkspace(parent) || !physicsRelevant(child)) {
            return;
        }
        List<Instance> sub = CollectionsKt.plus((Collection) CollectionsKt.listOf(child), (Iterable) Instance.descendants$default(child, null, 1, null));
        Set gone = Collections.newSetFromMap(new IdentityHashMap());
        gone.addAll(sub);
        for (Instance p : sub) {
            if (this.statics.contains(p)) {
                this.statics.remove(p);
                removeGrid(p);
            } else {
                Body b = this.bodyOf.get(p);
                if (b != null) {
                    Iterable parts = b.getParts();
                    if ((parts instanceof Collection) && ((Collection) parts).isEmpty()) {
                        z = true;
                    } else {
                        Iterator it = parts.iterator();
                        do {
                            if (!it.hasNext()) {
                                z = true;
                                break;
                            } else {
                                instance = (Instance) it.next();
                                z = false;
                            }
                        } while (gone.contains(instance) || instance.getParent() == null);
                    }
                    if (!z) {
                        this.physicsDirty = true;
                        return;
                    }
                    this.bodies.remove(b);
                    for (Instance q : b.getParts()) {
                        this.bodyOf.remove(q);
                    }
                } else if (p.isA("JointInstance") || Intrinsics.areEqual(p.getClassName(), "WeldConstraint")) {
                    this.physicsDirty = true;
                    return;
                } else if (p.isA("Constraint") || p.isA("BodyMover")) {
                    this.movers.remove(p);
                }
            }
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.goydevv.studrblx.model.TreeListener
    public void propChanged(Instance inst, String name) {
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(name, "name");
        if (this.writing) {
            return;
        }
        switch (name.hashCode()) {
            case -906536396:
                if (name.equals("Anchored")) {
                    Body b = this.bodyOf.get(inst);
                    if (Intrinsics.areEqual(inst.get("Anchored"), (Object) true) && b != null && b.getParts().size() == 1) {
                        this.bodies.remove(b);
                        this.bodyOf.remove(inst);
                        if (this.statics.add(inst)) {
                            insertGrid(inst);
                            return;
                        }
                        return;
                    }
                    this.physicsDirty = true;
                    return;
                }
                return;
            case 2577441:
                if (!name.equals("Size")) {
                    return;
                }
                break;
            case 3530753:
                if (!name.equals("size")) {
                    return;
                }
                break;
            case 1986292458:
                if (!name.equals("CFrame")) {
                    return;
                }
                break;
            default:
                return;
        }
        if (this.statics.contains(inst)) {
            updateGrid(inst);
        }
        Body body = this.bodyOf.get(inst);
        if (body != null) {
            body.setSleeping(false);
            body.setRestTime(0.0f);
        }
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    private static final class Joint {
        private final CFrame fixed;
        private final Instance inst;
        private final boolean motor;
        private final Instance p0;
        private final Instance p1;

        public Joint(Instance inst, Instance p0, Instance p1, boolean motor, CFrame fixed) {
            Intrinsics.checkNotNullParameter(inst, "inst");
            Intrinsics.checkNotNullParameter(p0, "p0");
            Intrinsics.checkNotNullParameter(p1, "p1");
            this.inst = inst;
            this.p0 = p0;
            this.p1 = p1;
            this.motor = motor;
            this.fixed = fixed;
        }

        public final CFrame getFixed() {
            return this.fixed;
        }

        public final Instance getInst() {
            return this.inst;
        }

        public final boolean getMotor() {
            return this.motor;
        }

        public final Instance getP0() {
            return this.p0;
        }

        public final Instance getP1() {
            return this.p1;
        }

        public final String getName() {
            return this.inst.getName();
        }
    }

    private final void lateAvatar() {
        Avatar.Spec spec;
        Instance model;
        Instance root;
        if (!this.dressable || this.dead || (spec = Avatar.INSTANCE.getCurrent()) == null || this.avatarApplied == Avatar.INSTANCE.getVersion() || (model = this.character) == null || (root = this.hrp) == null) {
            return;
        }
        if (spec.getCharacter() != null) {
            spawnCharacter();
            return;
        }
        if (wantR15(spec) || this.isR15) {
            spawnCharacter();
            return;
        }
        this.avatarApplied = Avatar.INSTANCE.getVersion();
        this.writing = true;
        try {
            List<Instance> listApplyTo = Avatar.INSTANCE.applyTo(model, spec);
            CFrame rootCf = PartData.INSTANCE.cframe(root);
            for (Instance h : listApplyTo) {
                this.offsets.put(h, rootCf.inverse().times(PartData.INSTANCE.cframe(h)));
            }
            buildJoints(model, root);
            this.writing = false;
            this.physicsDirty = true;
            Instance instance = this.player;
            if (instance != null) {
                this.rt.fire(instance, "CharacterAppearanceLoaded", this.rt.wrap(model));
            }
        } catch (Throwable th) {
            this.writing = false;
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:57:0x00e0  */
    /* JADX WARN: Code duplicated, block: B:66:0x00f9  */
    public final void spawnCharacter() {
        boolean z;
        Avatar.Spec spec;
        Instance model;
        Object next;
        Vector3 s;
        float fAtan2;
        boolean z2;
        boolean z3;
        Instance instanceFindFirstChild$default;
        Iterable children;
        float fAtan3;
        Object next2;
        Avatar.Spec current;
        byte[] character;
        Instance instance = this.player;
        if (instance == null) {
            return;
        }
        Instance p = instance;
        Instance instance2 = this.character;
        if (instance2 != null) {
            this.rt.fire(p, "CharacterRemoving", this.rt.wrap(instance2));
            Instance instance3 = this.equipped;
            if (instance3 != null && instance3.isDescendantOf(instance2)) {
                this.equipped = null;
            }
            instance2.destroy();
        }
        leaveSeat(false);
        this.climbing = false;
        if (this.playAsTemplate == null && (current = Avatar.INSTANCE.getCurrent()) != null && (character = current.getCharacter()) != null) {
            loadCharacterTemplate(character);
        }
        if (this.playAsTemplate == null) {
            Iterator it = Instance.descendants$default(this.dm.getWorkspace(), null, 1, null).iterator();
            do {
                if (!it.hasNext()) {
                    next2 = null;
                    break;
                }
                next2 = it.next();
            } while (!Intrinsics.areEqual(((Instance) next2).getAttributes().get(Studio.PLAY_AS_TAG), (Object) true));
            Instance instance4 = (Instance) next2;
            if (instance4 != null) {
                makePlayAs$default(this, instance4, false, 2, null);
            }
        }
        Instance starter = this.playAsTemplate;
        if (starter == null) {
            Instance instanceFindService = this.dm.findService("StarterPlayer");
            starter = instanceFindService != null ? Instance.findFirstChild$default(instanceFindService, "StarterCharacter", false, 2, null) : null;
        }
        boolean custom = (starter == null || Instance.findFirstChild$default(starter, "HumanoidRootPart", false, 2, null) == null || starter.findFirstChildOfClass("Humanoid") == null) ? false : true;
        this.avatarApplied = -1;
        if (custom) {
            z = false;
        } else {
            Instance instanceFindService2 = this.dm.findService("StarterPlayer");
            if (instanceFindService2 != null ? Intrinsics.areEqual(instanceFindService2.get("LoadCharacterAppearance"), (Object) false) : false) {
                z = false;
            } else {
                z = true;
            }
        }
        this.dressable = z;
        Avatar.Spec avatar = Avatar.INSTANCE.getCurrent();
        if (avatar == null) {
            avatar = null;
        } else {
            if (!(avatar.getCharacter() == null)) {
                avatar = null;
            }
        }
        boolean r15 = wantR15(avatar);
        if (!this.dressable || avatar == null) {
            spec = null;
        } else {
            spec = gameAvatarType() == 2 ? avatar : avatar.forRig(r15, clampScales(avatar.getScales()));
        }
        if (custom) {
            Intrinsics.checkNotNull(starter);
            model = starter.cloneTree();
            model.setName(p.getName());
        } else if (r15) {
            model = Avatar.INSTANCE.buildR15(p.getName(), spec == null ? new Avatar.Spec(Avatar.INSTANCE.getCLASSIC_COLORS(), null, null, null, null, CollectionsKt.emptyList(), "Default", true, null, null, null, null, 3840, null) : spec);
        } else {
            model = buildR6(p.getName());
        }
        if (spec != null) {
            if (!r15) {
                Avatar.INSTANCE.applyTo(model, spec);
            }
            this.avatarApplied = Avatar.INSTANCE.getVersion();
        } else if (this.dressable) {
            Avatar.Spec current2 = Avatar.INSTANCE.getCurrent();
            if ((current2 != null ? current2.getCharacter() : null) != null) {
                this.avatarApplied = Avatar.INSTANCE.getVersion();
            }
        }
        Instance root = Instance.findFirstChild$default(model, "HumanoidRootPart", false, 2, null);
        Intrinsics.checkNotNull(root);
        Instance hum = model.findFirstChildOfClass("Humanoid");
        Intrinsics.checkNotNull(hum);
        model.set("PrimaryPart", root, false);
        Object obj = hum.get("RigType");
        EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
        this.isR15 = (enumValue != null && enumValue.getValue() == 1) || Instance.findFirstChild$default(model, "UpperTorso", false, 2, null) != null;
        this.offsets.clear();
        CFrame rootCf = PartData.INSTANCE.cframe(root);
        float minY = Float.MAX_VALUE;
        float maxY = -3.4028235E38f;
        Iterator it2 = Instance.descendants$default(model, null, 1, null).iterator();
        float half = 0.5f;
        while (it2.hasNext()) {
            Instance part = (Instance) it2.next();
            if (part.isA("BasePart")) {
                Instance p2 = p;
                CFrame off = rootCf.inverse().times(PartData.INSTANCE.cframe(part));
                this.offsets.put(part, off);
                Iterator it3 = it2;
                part.set("Anchored", true, false);
                Vector3 s2 = PartData.INSTANCE.size(part);
                Pair<Vector3, Vector3> pairWorldAabb = Geometry.INSTANCE.worldAabb(off, s2);
                minY = Math.min(minY, pairWorldAabb.getFirst().getY());
                maxY = Math.max(maxY, pairWorldAabb.getSecond().getY());
                half = Math.max(half, Math.min(Math.max(Math.max(-pairWorldAabb.getFirst().getX(), pairWorldAabb.getSecond().getX()), Math.max(-pairWorldAabb.getFirst().getZ(), pairWorldAabb.getSecond().getZ())), 2.5f));
                p = p2;
                it2 = it3;
            }
        }
        Instance p3 = p;
        this.charMinY = minY;
        this.charMaxY = maxY;
        this.charHalfXZ = RangesKt.coerceAtMost(half, 1.2f);
        buildJoints(model, root);
        Iterable iterableDescendants$default = Instance.descendants$default(this.dm.getWorkspace(), null, 1, null);
        int i = 0;
        Iterator it4 = iterableDescendants$default.iterator();
        while (true) {
            if (!it4.hasNext()) {
                next = null;
                break;
            }
            next = it4.next();
            Instance instance5 = (Instance) next;
            Iterable iterable = iterableDescendants$default;
            int i2 = i;
            if (Intrinsics.areEqual(instance5.getClassName(), "SpawnLocation") && !Intrinsics.areEqual(instance5.get("Enabled"), (Object) false)) {
                break;
            }
            iterableDescendants$default = iterable;
            i = i2;
        }
        Instance spawn = (Instance) next;
        Vector3 here = this.studio.getPlayHereAt();
        if (here != null) {
            this.studio.setPlayHereAt(null);
            this.pos = here.plus(new Vector3(0.0f, (-this.charMinY) + 0.1f, 0.0f));
            Vector3 f = this.studio.getCamera().getForward();
            Float f2 = this.playAsYaw;
            if (f2 != null) {
                Float f3 = f2;
                f3.floatValue();
                this.playAsYaw = null;
                fAtan3 = f3.floatValue();
            } else {
                fAtan3 = (float) Math.atan2(-f.getX(), -f.getZ());
            }
            this.facing = fAtan3;
        } else {
            if (spawn != null) {
                CFrame cf = PartData.INSTANCE.cframe(spawn);
                Vector3 s3 = PartData.INSTANCE.size(spawn);
                s = cf.getPosition().plus(new Vector3(0.0f, ((s3.getY() / 2.0f) - this.charMinY) + 0.1f, 0.0f));
            } else {
                s = new Vector3(0.0f, (-this.charMinY) + 5.0f, 0.0f);
            }
            this.pos = s;
            if (spawn != null) {
                Vector3 lv = PartData.INSTANCE.cframe(spawn).getLookVector();
                fAtan2 = (float) Math.atan2(-lv.getX(), -lv.getZ());
            } else {
                fAtan2 = 0.0f;
            }
            this.facing = fAtan2;
        }
        this.camYaw = this.facing;
        this.yawTarget = this.facing;
        this.renderFacing = this.facing;
        this.motorAngle.clear();
        this.r15Pose.clear();
        Iterable iterable2 = this.jointOrder;
        if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
            Iterator it5 = iterable2.iterator();
            while (true) {
                if (!it5.hasNext()) {
                    z2 = false;
                    break;
                } else if (((Joint) ((Pair) it5.next()).getFirst()).getMotor()) {
                    z2 = true;
                    break;
                }
            }
        } else {
            z2 = false;
        }
        this.animator = z2 ? new DefaultAnimate(this.isR15) : null;
        this.vel = Vector3.INSTANCE.getZERO();
        this.dead = false;
        this.emote = null;
        this.hrp = root;
        this.humanoid = hum;
        this.character = model;
        writeCharacter(0.0f);
        Instance instanceFindService3 = this.dm.findService("StarterPlayer");
        if (instanceFindService3 != null && (instanceFindFirstChild$default = Instance.findFirstChild$default(instanceFindService3, "StarterCharacterScripts", false, 2, null)) != null && (children = instanceFindFirstChild$default.getChildren()) != null) {
            Iterator it6 = children.iterator();
            while (it6.hasNext()) {
                ((Instance) it6.next()).cloneTree().attachRaw(model);
            }
        }
        Instance.setParent$default(model, this.dm.getWorkspace(), 0, 2, null);
        Instance model2 = model;
        Instance.set$default(p3, "Character", model2, false, 4, null);
        this.rt.fire(p3, "CharacterAdded", this.rt.wrap(model2));
        runScriptsUnder(model2);
        if (!this.dressable || this.avatarApplied >= 0) {
            z3 = true;
            this.rt.fire(p3, "CharacterAppearanceLoaded", this.rt.wrap(model2));
        } else {
            z3 = true;
        }
        this.physicsDirty = z3;
    }

    public final int gameAvatarType() {
        Instance instanceFindService = this.dm.findService("StarterPlayer");
        Object obj = instanceFindService != null ? instanceFindService.get("GameSettingsAvatar") : null;
        EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
        if (enumValue != null) {
            return enumValue.getValue();
        }
        return 2;
    }

    private final boolean wantR15(Avatar.Spec spec) {
        switch (gameAvatarType()) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                return spec != null && spec.getR15();
        }
    }

    private final Avatar.Scales clampScales(Avatar.Scales s) {
        Instance sp = this.dm.findService("StarterPlayer");
        return sp == null ? s : new Avatar.Scales(clampScales$c(sp, s.getHeight(), "Height"), clampScales$c(sp, s.getWidth(), "Width"), clampScales$c(sp, s.getDepth(), "Width"), clampScales$c(sp, s.getHead(), "Head"));
    }

    private static final float clampScales$c(Instance sp, float v, String key) {
        Object obj = sp.get("GameSettingsScaleRange" + key);
        NumberRange r = obj instanceof NumberRange ? (NumberRange) obj : null;
        if (r == null) {
            return v;
        }
        return (r.getMax() < r.getMin() || r.getMax() <= 0.0f) ? v : RangesKt.coerceIn(v, r.getMin(), r.getMax());
    }

    private final void loadCharacterTemplate(byte[] bytes) {
        Instance src;
        try {
            try {
                RbxDocument doc = RbxFile.INSTANCE.read(bytes);
                Iterable roots = doc.getRoots();
                Collection arrayList = new ArrayList();
                Iterator it = roots.iterator();
                while (true) {
                    boolean z = true;
                    if (!it.hasNext()) {
                        break;
                    }
                    Object next = it.next();
                    Instance instance = (Instance) next;
                    if (!instance.isA("Model") && !instance.isA("BasePart") && !Intrinsics.areEqual(instance.getClassName(), "Folder")) {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(next);
                    }
                }
                List<Instance> roots2 = (List) arrayList;
                if (roots2.isEmpty()) {
                    OutputLog.INSTANCE.warn("Playtest character: the saved file has no model or part");
                    return;
                }
                if (roots2.size() != 1 || Intrinsics.areEqual(((Instance) roots2.get(0)).getClassName(), "Folder")) {
                    src = InstanceFactory.INSTANCE.create("Model");
                    src.setName(roots2.size() == 1 ? ((Instance) roots2.get(0)).getName() : "Character");
                    for (Instance instance2 : roots2) {
                        if (!Intrinsics.areEqual(instance2.getClassName(), "Folder")) {
                            instance2.attachRaw(src);
                        } else {
                            Iterator it2 = CollectionsKt.toList(instance2.getChildren()).iterator();
                            while (it2.hasNext()) {
                                ((Instance) it2.next()).attachRaw(src);
                            }
                        }
                    }
                } else {
                    src = (Instance) roots2.get(0);
                }
                makePlayAs(src, false);
                if (this.playAsTemplate != null) {
                    OutputLog.INSTANCE.info("Playing as " + src.getName() + " (File → Playtest Avatar)");
                }
            } catch (Exception e) {
                OutputLog.INSTANCE.warn("Playtest character: couldn't read the saved model (" + e.getMessage() + ")");
            }
        } catch (Exception e2) {
        }
    }

    static /* synthetic */ void makePlayAs$default(PlaySession playSession, Instance instance, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        playSession.makePlayAs(instance, z);
    }

    private final void makePlayAs(Instance src, boolean faceModel) {
        Instance model;
        Object next;
        CFrame pivot;
        Vector3 lv;
        src.getAttributes().remove(Studio.PLAY_AS_TAG);
        if (src.isA("BasePart")) {
            model = InstanceFactory.INSTANCE.create("Model");
            model.setName(src.getName());
            src.cloneTree().attachRaw(model);
        } else {
            model = src.cloneTree();
        }
        model.getAttributes().remove(Studio.PLAY_AS_TAG);
        Iterable iterableDescendants$default = Instance.descendants$default(model, null, 1, null);
        Collection arrayList = new ArrayList();
        for (Object obj : iterableDescendants$default) {
            if (((Instance) obj).isA("BasePart")) {
                arrayList.add(obj);
            }
        }
        List parts = (List) arrayList;
        Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(parts);
        if (pairBoundsOf == null) {
            return;
        }
        Object obj2 = model.get("WorldPivotData");
        OptionalCFrame optionalCFrame = obj2 instanceof OptionalCFrame ? (OptionalCFrame) obj2 : null;
        if (optionalCFrame == null || (pivot = optionalCFrame.getCframe()) == null) {
            Object obj3 = model.get("PrimaryPart");
            Instance instance = obj3 instanceof Instance ? (Instance) obj3 : null;
            if (instance != null) {
                pivot = PartData.INSTANCE.cframe(instance);
            } else {
                Iterator it = parts.iterator();
                if (it.hasNext()) {
                    next = it.next();
                    if (it.hasNext()) {
                        Vector3 size = PartData.INSTANCE.size((Instance) next);
                        float x = size.getX() * size.getY() * size.getZ();
                        do {
                            Object next2 = it.next();
                            Vector3 size2 = PartData.INSTANCE.size((Instance) next2);
                            float x2 = size2.getX() * size2.getY() * size2.getZ();
                            if (Float.compare(x, x2) < 0) {
                                next = next2;
                                x = x2;
                            }
                        } while (it.hasNext());
                    }
                } else {
                    next = null;
                }
                Instance instance2 = (Instance) next;
                pivot = instance2 != null ? PartData.INSTANCE.cframe(instance2) : null;
            }
        }
        if (pivot == null || (lv = pivot.getLookVector()) == null) {
            lv = new Vector3(0.0f, 0.0f, -1.0f);
        }
        float yaw = Math.abs(lv.getX()) + Math.abs(lv.getZ()) > 0.001f ? (float) Math.atan2(-lv.getX(), -lv.getZ()) : 0.0f;
        Instance instanceFindFirstChild$default = Instance.findFirstChild$default(model, "HumanoidRootPart", false, 2, null);
        if (!(instanceFindFirstChild$default != null && instanceFindFirstChild$default.isA("BasePart"))) {
            Vector3 c = pairBoundsOf.getFirst().plus(pairBoundsOf.getSecond()).div(2.0f);
            Instance root = InstanceFactory.INSTANCE.create("Part");
            root.setName("HumanoidRootPart");
            root.set("size", new Vector3(2.0f, 2.0f, 1.0f), false);
            root.set("CFrame", CFrame.INSTANCE.at(c).times(CFrame.INSTANCE.ry(yaw)), false);
            root.set("Transparency", Float.valueOf(1.0f), false);
            root.set("CanCollide", false, false);
            root.attachRaw(model);
        }
        if (model.findFirstChildOfClass("Humanoid") == null) {
            Instance hum = InstanceFactory.INSTANCE.create("Humanoid");
            hum.set("RigType", new EnumValue(Instance.findFirstChild$default(model, "UpperTorso", false, 2, null) != null ? 1 : 0), false);
            InstanceFactory.INSTANCE.create("Animator").attachRaw(hum);
            hum.attachRaw(model);
        }
        if (faceModel) {
            PartData partData = PartData.INSTANCE;
            Instance instanceFindFirstChild$default2 = Instance.findFirstChild$default(model, "HumanoidRootPart", false, 2, null);
            Intrinsics.checkNotNull(instanceFindFirstChild$default2);
            Vector3 lookVector = partData.cframe(instanceFindFirstChild$default2).getLookVector();
            this.playAsYaw = Float.valueOf((float) Math.atan2(-lookVector.getX(), -lookVector.getZ()));
        }
        this.playAsTemplate = model;
        src.destroy();
    }

    private final void buildJoints(Instance model, Instance root) {
        this.jointOrder.clear();
        this.rigid.clear();
        Set parts = Collections.newSetFromMap(new IdentityHashMap());
        Iterable iterableDescendants$default = Instance.descendants$default(model, null, 1, null);
        Collection arrayList = new ArrayList();
        for (Object obj : iterableDescendants$default) {
            if (((Instance) obj).isA("BasePart")) {
                arrayList.add(obj);
            }
        }
        Iterator it = ((List) arrayList).iterator();
        while (it.hasNext()) {
            parts.add((Instance) it.next());
        }
        ArrayList joints = new ArrayList();
        for (Instance j : Instance.descendants$default(model, null, 1, null)) {
            if (j.isA("JointInstance")) {
                Object obj2 = j.get("Part0");
                Instance a = obj2 instanceof Instance ? (Instance) obj2 : null;
                Object obj3 = j.get("Part1");
                Instance b = obj3 instanceof Instance ? (Instance) obj3 : null;
                if (a != null && b != null && parts.contains(a) && parts.contains(b) && a != b) {
                    joints.add(new Joint(j, a, b, j.isA("Motor"), null));
                }
            } else if (Intrinsics.areEqual(j.getClassName(), "WeldConstraint") && !Intrinsics.areEqual(j.get("Enabled"), (Object) false)) {
                Object obj4 = j.get("Part0Internal");
                if (obj4 == null) {
                    obj4 = j.get("Part0");
                }
                Instance a2 = obj4 instanceof Instance ? (Instance) obj4 : null;
                Object obj5 = j.get("Part1Internal");
                if (obj5 == null) {
                    obj5 = j.get("Part1");
                }
                Instance b2 = obj5 instanceof Instance ? (Instance) obj5 : null;
                if (a2 != null && b2 != null && parts.contains(a2) && parts.contains(b2) && a2 != b2) {
                    joints.add(new Joint(j, a2, b2, false, PartData.INSTANCE.cframe(a2).inverse().times(PartData.INSTANCE.cframe(b2))));
                }
            }
        }
        Set reached = Collections.newSetFromMap(new IdentityHashMap());
        reached.add(root);
        ArrayDeque queue = new ArrayDeque();
        queue.add(root);
        Set used = Collections.newSetFromMap(new IdentityHashMap());
        while (!queue.isEmpty()) {
            Instance cur = (Instance) queue.removeFirst();
            Iterator it2 = joints.iterator();
            Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
            while (it2.hasNext()) {
                Object next = it2.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                Joint j2 = (Joint) next;
                if (!used.contains(j2)) {
                    if (j2.getP0() == cur && !reached.contains(j2.getP1())) {
                        used.add(j2);
                        reached.add(j2.getP1());
                        this.jointOrder.add(TuplesKt.to(j2, true));
                        queue.add(j2.getP1());
                    } else if (j2.getP1() == cur && !reached.contains(j2.getP0())) {
                        used.add(j2);
                        reached.add(j2.getP0());
                        this.jointOrder.add(TuplesKt.to(j2, false));
                        queue.add(j2.getP0());
                    }
                }
            }
        }
        for (Map.Entry<Instance, CFrame> entry : this.offsets.entrySet()) {
            Instance part = entry.getKey();
            CFrame off = entry.getValue();
            if (!reached.contains(part)) {
                this.rigid.put(part, off);
            }
        }
    }

    private final Instance buildR6(String name) {
        return INSTANCE.buildR6Rig(name);
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Instance buildR6Rig(String name) {
            Intrinsics.checkNotNullParameter(name, "name");
            Instance m = InstanceFactory.INSTANCE.create("Model");
            m.setName(name);
            Color3 yellow = Color3.INSTANCE.fromRGB(245, 205, 48);
            Color3 blue = Color3.INSTANCE.fromRGB(13, 105, 172);
            Color3 green = Color3.INSTANCE.fromRGB(164, 189, 71);
            Instance root = buildR6Rig$part(m, "HumanoidRootPart", new Vector3(2.0f, 2.0f, 1.0f), Vector3.INSTANCE.getZERO(), blue, true);
            Instance torso = buildR6Rig$part$default(m, "Torso", new Vector3(2.0f, 2.0f, 1.0f), Vector3.INSTANCE.getZERO(), blue, false, 32, null);
            Instance head = buildR6Rig$part$default(m, "Head", new Vector3(2.0f, 1.0f, 1.0f), new Vector3(0.0f, 1.5f, 0.0f), yellow, false, 32, null);
            Instance instanceCreate = InstanceFactory.INSTANCE.create("SpecialMesh");
            instanceCreate.setName("Mesh");
            instanceCreate.set("MeshType", new EnumValue(0), false);
            instanceCreate.set("Scale", new Vector3(1.25f, 1.25f, 1.25f), false);
            instanceCreate.attachRaw(head);
            Instance face = InstanceFactory.INSTANCE.create("Decal");
            face.setName("face");
            face.set("Texture", "rbxasset://textures/face.png", false);
            face.set("Face", new EnumValue(5), false);
            face.attachRaw(head);
            Instance la = buildR6Rig$part$default(m, "Left Arm", new Vector3(1.0f, 2.0f, 1.0f), new Vector3(-1.5f, 0.0f, 0.0f), yellow, false, 32, null);
            Instance ra = buildR6Rig$part$default(m, "Right Arm", new Vector3(1.0f, 2.0f, 1.0f), new Vector3(1.5f, 0.0f, 0.0f), yellow, false, 32, null);
            Instance ll = buildR6Rig$part$default(m, "Left Leg", new Vector3(1.0f, 2.0f, 1.0f), new Vector3(-0.5f, -2.0f, 0.0f), green, false, 32, null);
            Instance rl = buildR6Rig$part$default(m, "Right Leg", new Vector3(1.0f, 2.0f, 1.0f), new Vector3(0.5f, -2.0f, 0.0f), green, false, 32, null);
            CFrame upright = CFrame.INSTANCE.rx(1.5707964f).times(CFrame.INSTANCE.ry(3.1415927f));
            buildR6Rig$motor("RootJoint", root, torso, upright, upright, root);
            buildR6Rig$motor("Neck", torso, head, CFrame.INSTANCE.at(new Vector3(0.0f, 1.0f, 0.0f)).times(upright), CFrame.INSTANCE.at(new Vector3(0.0f, -0.5f, 0.0f)).times(upright), torso);
            buildR6Rig$motor("Right Shoulder", torso, ra, CFrame.INSTANCE.at(new Vector3(1.0f, 0.5f, 0.0f)).times(CFrame.INSTANCE.ry(1.5707964f)), CFrame.INSTANCE.at(new Vector3(-0.5f, 0.5f, 0.0f)).times(CFrame.INSTANCE.ry(1.5707964f)), torso);
            buildR6Rig$motor("Left Shoulder", torso, la, CFrame.INSTANCE.at(new Vector3(-1.0f, 0.5f, 0.0f)).times(CFrame.INSTANCE.ry(-1.5707964f)), CFrame.INSTANCE.at(new Vector3(0.5f, 0.5f, 0.0f)).times(CFrame.INSTANCE.ry(-1.5707964f)), torso);
            buildR6Rig$motor("Right Hip", torso, rl, CFrame.INSTANCE.at(new Vector3(1.0f, -1.0f, 0.0f)).times(CFrame.INSTANCE.ry(1.5707964f)), CFrame.INSTANCE.at(new Vector3(0.5f, 1.0f, 0.0f)).times(CFrame.INSTANCE.ry(1.5707964f)), torso);
            buildR6Rig$motor("Left Hip", torso, ll, CFrame.INSTANCE.at(new Vector3(-1.0f, -1.0f, 0.0f)).times(CFrame.INSTANCE.ry(-1.5707964f)), CFrame.INSTANCE.at(new Vector3(-0.5f, 1.0f, 0.0f)).times(CFrame.INSTANCE.ry(-1.5707964f)), torso);
            Instance bc = InstanceFactory.INSTANCE.create("BodyColors");
            bc.setName("Body Colors");
            for (String k : CollectionsKt.listOf(new String[]{"HeadColor3", "LeftArmColor3", "RightArmColor3"})) {
                bc.set(k, yellow, false);
            }
            bc.set("TorsoColor3", blue, false);
            for (String k2 : CollectionsKt.listOf(new String[]{"LeftLegColor3", "RightLegColor3"})) {
                bc.set(k2, green, false);
            }
            bc.attachRaw(m);
            Instance hum = InstanceFactory.INSTANCE.create("Humanoid");
            hum.set("RigType", new EnumValue(0), false);
            hum.attachRaw(m);
            InstanceFactory.INSTANCE.create("Animator").attachRaw(hum);
            return m;
        }

        static /* synthetic */ Instance buildR6Rig$part$default(Instance instance, String str, Vector3 vector3, Vector3 vector4, Color3 color3, boolean z, int i, Object obj) {
            if ((i & 32) != 0) {
                z = false;
            }
            return buildR6Rig$part(instance, str, vector3, vector4, color3, z);
        }

        private static final Instance buildR6Rig$part(Instance m, String n, Vector3 size, Vector3 off, Color3 color, boolean transparent) {
            Instance p = InstanceFactory.INSTANCE.create("Part");
            p.setName(n);
            p.set("size", size, false);
            p.set("CFrame", CFrame.INSTANCE.at(off), false);
            p.set("Color3uint8", color, false);
            p.set("Material", new EnumValue(256), false);
            boolean z = true;
            for (String s : CollectionsKt.listOf(new String[]{"TopSurface", "BottomSurface", "LeftSurface", "RightSurface", "FrontSurface", "BackSurface"})) {
                p.set(s, new EnumValue(0), false);
            }
            if (!Intrinsics.areEqual(n, "Torso") && !Intrinsics.areEqual(n, "Head")) {
                z = false;
            }
            p.set("CanCollide", Boolean.valueOf(z), false);
            if (transparent) {
                p.set("Transparency", Float.valueOf(1.0f), false);
                p.set("CanCollide", false, false);
            }
            p.attachRaw(m);
            return p;
        }

        private static final void buildR6Rig$motor(String n, Instance p0, Instance p1, CFrame c0, CFrame c1, Instance parent) {
            Instance j = InstanceFactory.INSTANCE.create("Motor6D");
            j.setName(n);
            j.set("Part0", p0, false);
            j.set("Part1", p1, false);
            j.set("C0", c0, false);
            j.set("C1", c1, false);
            j.set("MaxVelocity", Float.valueOf(0.1f), false);
            j.attachRaw(parent);
        }
    }

    public final void respawn() {
        resetGui(false);
        spawnCharacter();
    }

    public final void resetCharacter() {
        Instance hum = this.humanoid;
        if (hum != null && !this.dead) {
            Instance.set$default(hum, hum.getProps().containsKey("Health") ? "Health" : "Health_XML", Float.valueOf(0.0f), false, 4, null);
        }
    }

    public final void setJumpHeld(boolean down) {
        this.jumpHeld = down;
        if (down) {
            this.jumpRequested = true;
        }
    }

    private final float health() {
        Object obj;
        Instance instance = this.humanoid;
        if (instance != null) {
            obj = instance.get("Health_XML");
            if (obj == null) {
                obj = instance.get("Health");
            }
        } else {
            obj = null;
        }
        Number number = obj instanceof Number ? (Number) obj : null;
        if (number != null) {
            return number.floatValue();
        }
        return 100.0f;
    }

    private final float approach(float cur, float target, float rate, float dt) {
        return ((target - cur) * (1.0f - ((float) Math.exp((-rate) * dt)))) + cur;
    }

    private final void motor(String name, float desired, float maxVel, float dt) {
        Float f = this.motorAngle.get(name);
        float cur = f != null ? f.floatValue() : 0.0f;
        float step = 60.0f * maxVel * dt;
        this.motorAngle.put(name, Float.valueOf(Math.abs(desired - cur) <= step ? desired : (Math.signum(desired - cur) * step) + cur));
    }

    private final String pose() {
        if (this.seated != null) {
            return "Seated";
        }
        if (this.climbing) {
            return "Climbing";
        }
        if (this.jumpAnimTime > 0.0f || (!this.grounded && this.vel.getY() > 0.5f && this.airTime < 0.4f)) {
            return "Jumping";
        }
        if (this.grounded || this.airTime <= 0.1f) {
            return ((float) Math.sqrt((double) ((this.vel.getX() * this.vel.getX()) + (this.vel.getZ() * this.vel.getZ())))) > 0.5f ? "Running" : "Standing";
        }
        return "FreeFall";
    }

    /* JADX WARN: Code duplicated, block: B:23:0x0095  */
    /* JADX WARN: Code duplicated, block: B:37:0x0112  */
    /* JADX WARN: Code duplicated, block: B:40:0x0152  */
    /* JADX WARN: Code duplicated, block: B:43:0x015e  */
    /* JADX WARN: Code duplicated, block: B:45:0x0168  */
    /* JADX WARN: Code duplicated, block: B:46:0x0173  */
    /* JADX WARN: Code duplicated, block: B:48:0x017b  */
    /* JADX WARN: Code duplicated, block: B:49:0x0181  */
    /* JADX WARN: Code duplicated, block: B:50:0x01b0  */
    /* JADX WARN: Code duplicated, block: B:52:0x01bc  */
    /* JADX WARN: Code duplicated, block: B:53:0x01e6  */
    /* JADX WARN: Code duplicated, block: B:56:0x01f5  */
    /* JADX WARN: Code duplicated, block: B:57:0x0208  */
    /* JADX WARN: Code duplicated, block: B:58:0x020e A[PHI: r22
      0x020e: PHI (r22v1 float) = (r22v0 float), (r22v3 float), (r22v4 float), (r22v6 float) binds: [B:57:0x0208, B:54:0x01f2, B:51:0x01ba, B:48:0x017b] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:61:0x0216  */
    /* JADX WARN: Code duplicated, block: B:66:0x0221  */
    /* JADX WARN: Code duplicated, block: B:67:0x0224  */
    /* JADX WARN: Code duplicated, block: B:70:0x0230  */
    /* JADX WARN: Code duplicated, block: B:73:0x023a  */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00c4, code lost:
    
        if (r31.equals("FreeFall") != false) goto L27;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void animateR6(float dt, java.lang.String state) {
        float t = (float) this.time;
        float rs = 0.0f;
        float ls = 0.0f;
        float rh = 0.0f;
        float lh = 0.0f;
        if ("Running".equals(state)) {
            float s = (float) Math.sin(t * 9.0f);
            rs = s * 0.65f;
            ls = s * 0.65f;
            rh = -s * 0.65f;
            lh = -s * 0.65f;
        } else if ("Jumping".equals(state) || "Freefall".equals(state) || "FreeFall".equals(state)) {
            rs = 3.1415927f;
            ls = -3.1415927f;
            rh = 0.1f;
            lh = -0.1f;
        } else if ("Climbing".equals(state)) {
            float s = (float) Math.sin(t * 7.5f);
            rs = 2.6f + (s * 0.5f);
            ls = -2.6f + (s * 0.5f);
            rh = s * 0.45f;
            lh = s * 0.45f;
        } else if ("Seated".equals(state)) {
            rs = 1.5707964f;
            ls = -1.5707964f;
            rh = 1.5707964f;
            lh = -1.5707964f;
        } else {
            float b = (float) Math.sin(t * 2.0f) * 0.06f;
            rs = b;
            ls = -b;
        }
        this.motorAngle.put("Right Shoulder", Float.valueOf(rs));
        this.motorAngle.put("Left Shoulder", Float.valueOf(ls));
        this.motorAngle.put("Right Hip", Float.valueOf(rh));
        this.motorAngle.put("Left Hip", Float.valueOf(lh));
    }

    static /* synthetic */ void setR15$default(PlaySession playSession, String str, float f, float f2, float f3, float f4, float f5, int i, Object obj) {
        if ((i & 4) != 0) {
            f2 = 0.0f;
        }
        if ((i & 8) != 0) {
            f3 = 0.0f;
        }
        playSession.setR15(str, f, f2, f3, f4, f5);
    }

    private final void setR15(String name, float x, float y, float z, float rate, float dt) {
        float[] fArr;
        Map map = this.r15Pose;
        float[] fArr2 = (float[]) map.get(name);
        if (fArr2 == null) {
            fArr = new float[]{0.0f, 0.0f, 0.0f};
            map.put(name, fArr);
        } else {
            fArr = fArr2;
        }
        float[] cur = fArr;
        cur[0] = approach(cur[0], x, rate, dt);
        cur[1] = approach(cur[1], y, rate, dt);
        cur[2] = approach(cur[2], z, rate, dt);
    }

    /* JADX WARN: Code duplicated, block: B:31:0x01f0 A[PHI: r0
      0x01f0: PHI (r0v3 'r' float) = (r0v1 'r' float), (r0v5 'r' float) binds: [B:30:0x01ee, B:27:0x01b8] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final void animateR15(float dt, String pose) {
        float r;
        PlaySession playSession;
        float r2;
        PlaySession playSession2 = this;
        float speed = (float) Math.sqrt((playSession2.vel.getX() * playSession2.vel.getX()) + (playSession2.vel.getZ() * playSession2.vel.getZ()));
        Instance instance = playSession2.humanoid;
        Object obj = instance != null ? instance.get("WalkSpeed") : null;
        Number number = obj instanceof Number ? (Number) obj : null;
        float walk = RangesKt.coerceAtLeast(number != null ? number.floatValue() : 16.0f, 1.0f);
        playSession2.idleT += dt;
        switch (pose.hashCode()) {
            case -1822467708:
                r = 14.0f;
                if (!pose.equals("Seated")) {
                    r2 = r;
                    playSession2 = this;
                    float b = (float) Math.sin(playSession2.idleT * 1.6f);
                    float r3 = r2;
                    PlaySession playSession3 = playSession2;
                    animateR15$all$default(r3, playSession3, dt, b * 0.04f, 0.04f * b, 0.08f, 0.08f, 0.0f, 0.0f, 0.0f, 0.0f, b * 0.025f, 0.0f, 8.0f, 4096, null);
                    playSession = playSession3;
                    playSession.walkT = 0.0f;
                } else {
                    animateR15$all$default(14.0f, this, dt, 0.35f, 0.35f, 0.6f, 0.6f, 1.57f, 1.57f, -1.57f, -1.57f, 0.0f, 0.0f, 0.0f, 14336, null);
                    playSession = this;
                }
                break;
            case -1474121689:
                r = 14.0f;
                if (!pose.equals("FreeFall")) {
                    r2 = r;
                    playSession2 = this;
                    float b2 = (float) Math.sin(playSession2.idleT * 1.6f);
                    float r4 = r2;
                    PlaySession playSession4 = playSession2;
                    animateR15$all$default(r4, playSession4, dt, b2 * 0.04f, 0.04f * b2, 0.08f, 0.08f, 0.0f, 0.0f, 0.0f, 0.0f, b2 * 0.025f, 0.0f, 8.0f, 4096, null);
                    playSession = playSession4;
                    playSession.walkT = 0.0f;
                } else {
                    animateR15$all$default(14.0f, this, dt, 2.8f, 2.8f, 0.2f, 0.2f, 0.5f, -0.25f, -0.7f, -0.3f, 0.0f, 0.0f, 8.0f, 6144, null);
                    playSession = this;
                }
                break;
            case -1079530081:
                r2 = 14.0f;
                if (!pose.equals("Running")) {
                    float b3 = (float) Math.sin(playSession2.idleT * 1.6f);
                    float r5 = r2;
                    PlaySession playSession5 = playSession2;
                    animateR15$all$default(r5, playSession5, dt, b3 * 0.04f, 0.04f * b3, 0.08f, 0.08f, 0.0f, 0.0f, 0.0f, 0.0f, b3 * 0.025f, 0.0f, 8.0f, 4096, null);
                    playSession = playSession5;
                    playSession.walkT = 0.0f;
                } else {
                    float k = RangesKt.coerceIn(speed / walk, 0.4f, 1.6f);
                    playSession2.walkT += dt * 9.0f * k;
                    float s = (float) Math.sin(playSession2.walkT);
                    float c = (float) Math.cos(playSession2.walkT);
                    float a = RangesKt.coerceAtMost(k, 1.2f) * 0.7f;
                    animateR15$all(14.0f, playSession2, dt, (-0.6f) * s * RangesKt.coerceAtMost(k, 1.2f), 0.6f * s * RangesKt.coerceAtMost(k, 1.2f), (Math.max(0.0f, -s) * 0.2f) + 0.25f, (Math.max(0.0f, s) * 0.2f) + 0.25f, a * s, (-a) * s, Math.max(0.0f, -c) * (-0.9f), Math.max(0.0f, c) * (-0.9f), 0.05f, 0.08f * s, 22.0f);
                    playSession = this;
                }
                break;
            case 409511124:
                if (!pose.equals("Jumping")) {
                    r2 = 14.0f;
                    playSession2 = this;
                    float b4 = (float) Math.sin(playSession2.idleT * 1.6f);
                    float r6 = r2;
                    PlaySession playSession6 = playSession2;
                    animateR15$all$default(r6, playSession6, dt, b4 * 0.04f, 0.04f * b4, 0.08f, 0.08f, 0.0f, 0.0f, 0.0f, 0.0f, b4 * 0.025f, 0.0f, 8.0f, 4096, null);
                    playSession = playSession6;
                    playSession.walkT = 0.0f;
                } else {
                    animateR15$all$default(14.0f, this, dt, 2.4f, 2.4f, 0.4f, 0.4f, 0.35f, -0.1f, -0.5f, -0.2f, 0.0f, 0.0f, 18.0f, 6144, null);
                    playSession = this;
                }
                break;
            case 980073773:
                if (!pose.equals("Climbing")) {
                    r2 = 14.0f;
                    float b5 = (float) Math.sin(playSession2.idleT * 1.6f);
                    float r7 = r2;
                    PlaySession playSession7 = playSession2;
                    animateR15$all$default(r7, playSession7, dt, b5 * 0.04f, 0.04f * b5, 0.08f, 0.08f, 0.0f, 0.0f, 0.0f, 0.0f, b5 * 0.025f, 0.0f, 8.0f, 4096, null);
                    playSession = playSession7;
                    playSession.walkT = 0.0f;
                } else {
                    playSession2.walkT += 8.0f * dt * RangesKt.coerceIn(Math.abs(playSession2.vel.getY()) / (0.7f * walk), 0.0f, 1.2f);
                    float s2 = (float) Math.sin(playSession2.walkT);
                    animateR15$all$default(14.0f, this, dt, (s2 * 0.5f) + 2.6f, 2.6f - (s2 * 0.5f), 0.6f, 0.6f, (s2 * 0.5f) + 0.55f, 0.55f - (0.5f * s2), (-0.8f) - (s2 * 0.4f), (0.4f * s2) - 0.8f, 0.0f, 0.0f, 20.0f, 6144, null);
                    playSession = this;
                }
                break;
            default:
                r2 = 14.0f;
                float b6 = (float) Math.sin(playSession2.idleT * 1.6f);
                float r8 = r2;
                PlaySession playSession8 = playSession2;
                animateR15$all$default(r8, playSession8, dt, b6 * 0.04f, 0.04f * b6, 0.08f, 0.08f, 0.0f, 0.0f, 0.0f, 0.0f, b6 * 0.025f, 0.0f, 8.0f, 4096, null);
                playSession = playSession8;
                playSession.walkT = 0.0f;
                break;
        }
        if (playSession.equipped != null && playSession.emote == null) {
            setR15$default(playSession, "RightShoulder", 1.57f, 0.0f, 0.0f, 20.0f, dt, 12, null);
        }
        String str = playSession.emote;
        if (str != null) {
            switch (str.hashCode()) {
                case 3642105:
                    if (str.equals("wave")) {
                        playSession.setR15("RightShoulder", 2.4f, 0.0f, ((float) Math.sin(playSession.emoteTime * 9.0f)) * 0.3f, 20.0f, dt);
                    }
                    break;
                case 94627149:
                    if (str.equals("cheer")) {
                        setR15$default(this, "RightShoulder", 2.9f, 0.0f, 0.0f, 20.0f, dt, 12, null);
                        setR15$default(this, "LeftShoulder", 2.9f, 0.0f, 0.0f, 20.0f, dt, 12, null);
                    }
                    break;
                case 95350707:
                    if (str.equals("dance")) {
                        float s3 = (float) Math.sin(playSession.emoteTime * 5.0f);
                        setR15$default(playSession, "RightShoulder", s3 + 1.4f, 0.0f, 0.0f, 20.0f, dt, 12, null);
                        setR15$default(this, "LeftShoulder", 1.4f - s3, 0.0f, 0.0f, 20.0f, dt, 12, null);
                        setR15$default(this, "Waist", 0.0f, s3 * 0.3f, 0.0f, 20.0f, dt, 8, null);
                    }
                    break;
                case 106845584:
                    if (str.equals("point")) {
                        setR15$default(playSession, "RightShoulder", 1.57f, 0.0f, 0.0f, 20.0f, dt, 12, null);
                    }
                    break;
            }
        }
    }

    static /* synthetic */ void animateR15$all$default(float f, PlaySession playSession, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, int i, Object obj) {
        animateR15$all(f, playSession, f2, f3, f4, f5, f6, f7, f8, f9, f10, (i & 2048) != 0 ? 0.0f : f11, (i & 4096) != 0 ? 0.0f : f12, (i & 8192) != 0 ? f : f13);
    }

    private static final void animateR15$all(float r, PlaySession this$0, float $dt, float rs, float ls, float re, float le, float rh, float lh, float rk, float lk, float waistX, float waistY, float rate) {
        setR15$default(this$0, "RightShoulder", rs, 0.0f, 0.0f, rate, $dt, 12, null);
        setR15$default(this$0, "LeftShoulder", ls, 0.0f, 0.0f, rate, $dt, 12, null);
        setR15$default(this$0, "RightElbow", re, 0.0f, 0.0f, rate, $dt, 12, null);
        setR15$default(this$0, "LeftElbow", le, 0.0f, 0.0f, rate, $dt, 12, null);
        setR15$default(this$0, "RightHip", rh, 0.0f, 0.0f, rate, $dt, 12, null);
        setR15$default(this$0, "LeftHip", lh, 0.0f, 0.0f, rate, $dt, 12, null);
        setR15$default(this$0, "RightKnee", rk, 0.0f, 0.0f, rate, $dt, 12, null);
        setR15$default(this$0, "LeftKnee", lk, 0.0f, 0.0f, rate, $dt, 12, null);
        setR15$default(this$0, "Waist", waistX, waistY, 0.0f, rate, $dt, 8, null);
    }

    private final void animate(float dt) {
        float dt2;
        this.airTime = (this.grounded || this.climbing || this.seated != null) ? 0.0f : this.airTime + dt;
        if (this.jumpAnimTime > 0.0f) {
            this.jumpAnimTime -= dt;
        }
        if (this.emote != null) {
            this.emoteTime += dt;
            if (((float) Math.sqrt((this.vel.getX() * this.vel.getX()) + (this.vel.getZ() * this.vel.getZ()))) > 0.5f || !this.grounded || (!Intrinsics.areEqual(this.emote, "dance") && this.emoteTime > 2.5f)) {
                this.emote = null;
            }
        }
        String p = pose();
        if (this.isR15) {
            animateR15(dt, p);
        } else {
            animateR6(dt, p);
        }
        float hs = Intrinsics.areEqual(p, "Climbing") ? Math.abs(this.vel.getY()) : (float) Math.sqrt((this.vel.getX() * this.vel.getX()) + (this.vel.getZ() * this.vel.getZ()));
        DefaultAnimate defaultAnimate = this.animator;
        if (defaultAnimate != null) {
            dt2 = dt;
            defaultAnimate.step(dt2, p, hs, this.equipped != null, this.emote, this.jumpAnimTime);
        } else {
            dt2 = dt;
        }
        float diff = this.facing - this.renderFacing;
        while (diff > 3.141592653589793d) {
            diff -= 6.2831855f;
        }
        while (diff < -3.141592653589793d) {
            diff += 6.2831855f;
        }
        this.renderFacing += (1.0f - ((float) Math.exp((-32.0f) * dt2))) * diff;
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    public final class ScriptTrack {
        private float fade;
        private final Long id;
        private final AnimClip inline;
        private Boolean looped;
        private int loopsSeen;
        private Integer priority;
        private final Instance rig;
        private float speed;
        private final LuaTable table;
        final /* synthetic */ PlaySession this$0;
        private AnimPlayer.Track track;
        private boolean wantPlay;
        private float weight;

        public ScriptTrack(PlaySession this$0, Instance rig, Long id, AnimClip inline, LuaTable table) {
            Intrinsics.checkNotNullParameter(rig, "rig");
            Intrinsics.checkNotNullParameter(table, "table");
            this.this$0 = this$0;
            this.rig = rig;
            this.id = id;
            this.inline = inline;
            this.table = table;
            this.fade = 0.1f;
            this.weight = 1.0f;
            this.speed = 1.0f;
        }

        public final Instance getRig() {
            return this.rig;
        }

        public final LuaTable getTable() {
            return this.table;
        }

        public final AnimPlayer.Track getTrack() {
            return this.track;
        }

        public final void setTrack(AnimPlayer.Track track) {
            this.track = track;
        }

        public final boolean getWantPlay() {
            return this.wantPlay;
        }

        public final void setWantPlay(boolean z) {
            this.wantPlay = z;
        }

        public final float getFade() {
            return this.fade;
        }

        public final float getSpeed() {
            return this.speed;
        }

        public final float getWeight() {
            return this.weight;
        }

        public final void setFade(float f) {
            this.fade = f;
        }

        public final void setSpeed(float f) {
            this.speed = f;
        }

        public final void setWeight(float f) {
            this.weight = f;
        }

        public final Boolean getLooped() {
            return this.looped;
        }

        public final void setLooped(Boolean bool) {
            this.looped = bool;
        }

        public final Integer getPriority() {
            return this.priority;
        }

        public final void setPriority(Integer num) {
            this.priority = num;
        }

        public final int getLoopsSeen() {
            return this.loopsSeen;
        }

        public final void setLoopsSeen(int i) {
            this.loopsSeen = i;
        }

        public final AnimClip getClip() {
            AnimClip animClip = this.inline;
            if (animClip != null) {
                return animClip;
            }
            Long l = this.id;
            if (l == null) {
                return null;
            }
            return AnimLibrary.INSTANCE.get(l.longValue());
        }

        public final boolean isPlaying() {
            return this.wantPlay;
        }

        public final float getLength() {
            AnimClip clip = getClip();
            if (clip != null) {
                return clip.getLength();
            }
            return 0.0f;
        }

        public final float getTime() {
            AnimPlayer.Track track = this.track;
            if (track != null) {
                return track.getTime();
            }
            return 0.0f;
        }

        public final float getWeightNow() {
            AnimPlayer.Track track = this.track;
            if (track != null) {
                return track.getWeight();
            }
            return 0.0f;
        }

        public final float getWeightTarget() {
            if (this.wantPlay) {
                return this.weight;
            }
            return 0.0f;
        }
    }

    private final Instance rigOf(Instance owner) {
        Instance r = owner.getParent();
        if (Intrinsics.areEqual(owner.getClassName(), "Animator")) {
            r = r != null ? r.getParent() : null;
        }
        if (r == null) {
            return null;
        }
        Instance instance = r;
        if (instance.isA("Model") || instance.isA("BasePart") || Intrinsics.areEqual(instance.getClassName(), "WorldModel")) {
            return r;
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:19:0x004c  */

    private Boolean customSounds;

    private final boolean customCharSounds() {
        if (this.customSounds != null) return this.customSounds.booleanValue();
        try {
            Instance sp = this.dm.findService("StarterPlayer");
            Instance sps = sp == null ? null : Instance.findFirstChild$default(sp, "StarterPlayerScripts", false, 2, null);
            this.customSounds = Boolean.valueOf(sps != null && Instance.findFirstChild$default(sps, "RbxCharacterSounds", false, 2, null) != null);
            return this.customSounds.booleanValue();
        } catch (Throwable t) {
            return false;
        }
    }

    private final void playDefaultCharSound(String name, String file, float volume) {
        try {
            Instance hrp = this.hrp;
            if (hrp == null || customCharSounds()) {
                return;
            }
            Instance snd = Instance.findFirstChild$default(hrp, name, false, 2, null);
            if (snd == null) {
                snd = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("Sound");
                snd.setName(name);
                snd.set("SoundId", "rbxasset://sounds/" + file, false);
                snd.set("Volume", Double.valueOf(volume), false);
                snd.set("RollOffMaxDistance", Double.valueOf(150.0d), false);
                snd.setArchivable(false);
                Instance.setParent$default(snd, hrp, 0, 2, null);
            }
            if (!"Sound".equals(snd.getClassName())) return;
            Object id = snd.get("SoundId");
            String sid = id == null ? "" : id.toString();
            if (sid.startsWith("rbxasset://sounds/")) {
                Object vo = snd.get("Volume");
                float v = vo instanceof Number ? ((Number) vo).floatValue() : volume;
                Object so = snd.get("PlaybackSpeed");
                float r = so instanceof Number ? ((Number) so).floatValue() : 1.0f;
                com.goydevv.studrblx.audio.CharacterSounds.play(sid.substring("rbxasset://sounds/".length()), v, r);
                return;
            }
            Object seqObj = snd.getAttributes().get("__playSeq");
            int seq = (seqObj instanceof Integer) ? ((Integer) seqObj).intValue() + 1 : 1;
            snd.getAttributes().put("__playSeq", Integer.valueOf(seq));
            snd.set("Playing", Boolean.TRUE, false);
        } catch (Throwable ignored) {
        }
    }

    private final void updateRunSound() {
        try {
            if (this.hrp == null || this.dead || customCharSounds()) {
                com.goydevv.studrblx.audio.CharacterSounds.running(false, 0.0f, 0.0f);
                return;
            }
            float hs = (float) Math.sqrt((this.vel.getX() * this.vel.getX()) + (this.vel.getZ() * this.vel.getZ()));
            com.goydevv.studrblx.audio.CharacterSounds.running(this.grounded && !this.climbing && hs > 0.5f, hs, 0.65f);
        } catch (Throwable ignored) {
        }
    }

    private final void playCharSound(String name, String soundId, float volume) {
        try {
            Instance hrp = this.hrp;
            if (hrp == null) {
                return;
            }
            Instance snd = Instance.findFirstChild$default(hrp, name, false, 2, null);
            if (snd == null || !"Sound".equals(snd.getClassName())) {
                snd = com.goydevv.studrblx.model.InstanceFactory.INSTANCE.create("Sound");
                snd.setName(name);
                snd.set("SoundId", soundId, false);
                snd.set("Volume", Double.valueOf(volume), false);
                snd.set("RollOffMaxDistance", Double.valueOf(150.0d), false);
                Instance.setParent$default(snd, hrp, 0, 2, null);
            }
            Object seqObj = snd.getAttributes().get("__playSeq");
            int seq = (seqObj instanceof Integer) ? ((Integer) seqObj).intValue() + 1 : 1;
            snd.getAttributes().put("__playSeq", Integer.valueOf(seq));
            snd.set("Playing", Boolean.TRUE, false);
        } catch (Exception ignored) {
        }
    }

    public final ScriptTrack loadTrack(Instance owner, Instance anim, LuaTable table) {
        AnimClip inline;
        Iterable iterableDescendants$default;
        Object next;
        Intrinsics.checkNotNullParameter(owner, "owner");
        Intrinsics.checkNotNullParameter(table, "table");
        Instance rig = rigOf(owner);
        if (rig == null) {
            return null;
        }
        if (anim == null || (iterableDescendants$default = Instance.descendants$default(anim, null, 1, null)) == null) {
            inline = null;
        } else {
            Iterator it = iterableDescendants$default.iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!Intrinsics.areEqual(((Instance) next).getClassName(), "KeyframeSequence"));
            Instance instance = (Instance) next;
            if (instance != null) {
                inline = AnimLibrary.INSTANCE.fromInstance(instance);
            } else {
                inline = null;
            }
        }
        Long id = AssetFetcher.INSTANCE.idOf(anim != null ? anim.get("AnimationId") : null);
        if (inline == null && id == null) {
            return null;
        }
        if (inline == null) {
            AnimLibrary animLibrary = AnimLibrary.INSTANCE;
            Intrinsics.checkNotNull(id);
            animLibrary.get(id.longValue());
        }
        ScriptTrack scriptTrack = new ScriptTrack(this, rig, id, inline, table);
        this.scriptTracks.add(scriptTrack);
        return scriptTrack;
    }

    public final void playTrack(ScriptTrack st, float fade, float weight, float speed) {
        AnimPlayer animPlayer;
        Intrinsics.checkNotNullParameter(st, "st");
        AnimPlayer.Track track = st.getTrack();
        if (track != null && (animPlayer = this.rigPlayers.get(st.getRig())) != null) {
            animPlayer.stop(track, fade);
        }
        st.setTrack(null);
        st.setWantPlay(true);
        st.setFade(fade);
        st.setWeight(weight);
        st.setSpeed(speed);
        st.setLoopsSeen(0);
        startTrack(st);
        this.rt.fire(st.getTable(), "Played", new LuaValue[0]);
    }

    public final void stopTrack(ScriptTrack st, float fade) {
        AnimPlayer animPlayer;
        Intrinsics.checkNotNullParameter(st, "st");
        boolean was = st.getWantPlay();
        st.setWantPlay(false);
        AnimPlayer.Track track = st.getTrack();
        if (track != null && (animPlayer = this.rigPlayers.get(st.getRig())) != null) {
            animPlayer.stop(track, fade);
        }
        st.setTrack(null);
        if (was) {
            this.rt.fire(st.getTable(), "Stopped", new LuaValue[0]);
            this.rt.fire(st.getTable(), "Ended", new LuaValue[0]);
        }
    }

    public static /* synthetic */ void adjustTrack$default(PlaySession playSession, ScriptTrack scriptTrack, Float f, Float f2, Float f3, Boolean bool, Integer num, Float f4, int i, Object obj) {
        if ((i & 2) != 0) {
            f = null;
        }
        if ((i & 4) != 0) {
            f2 = null;
        }
        if ((i & 8) != 0) {
            f3 = null;
        }
        if ((i & 16) != 0) {
            bool = null;
        }
        if ((i & 32) != 0) {
            num = null;
        }
        if ((i & 64) != 0) {
            f4 = null;
        }
        playSession.adjustTrack(scriptTrack, f, f2, f3, bool, num, f4);
    }

    public final void adjustTrack(ScriptTrack st, Float speed, Float weight, Float fade, Boolean looped, Integer priority, Float time) {
        Intrinsics.checkNotNullParameter(st, "st");
        if (speed != null) {
            float fFloatValue = speed.floatValue();
            st.setSpeed(fFloatValue);
            AnimPlayer.Track track = st.getTrack();
            if (track != null) {
                track.setSpeed(fFloatValue);
            }
        }
        if (weight != null) {
            float fFloatValue2 = weight.floatValue();
            st.setWeight(fFloatValue2);
            AnimPlayer.Track track2 = st.getTrack();
            if (track2 != null) {
                track2.setTargetWeight(fFloatValue2);
                if (fade != null) {
                    float fFloatValue3 = fade.floatValue();
                    track2.setFadeRate(fFloatValue3 <= 0.001f ? 1000.0f : 1.0f / fFloatValue3);
                }
            }
        }
        if (looped != null) {
            boolean zBooleanValue = looped.booleanValue();
            st.setLooped(Boolean.valueOf(zBooleanValue));
            AnimPlayer.Track track3 = st.getTrack();
            if (track3 != null) {
                track3.setLooped(zBooleanValue);
            }
        }
        if (priority != null) {
            int iIntValue = priority.intValue();
            st.setPriority(Integer.valueOf(iIntValue));
            AnimPlayer.Track track4 = st.getTrack();
            if (track4 != null) {
                track4.setPriority(AnimPlayer.INSTANCE.rank(iIntValue));
            }
        }
        if (time != null) {
            float fFloatValue4 = time.floatValue();
            AnimPlayer.Track track5 = st.getTrack();
            if (track5 != null) {
                track5.setTime(RangesKt.coerceIn(fFloatValue4, 0.0f, track5.getClip().getLength()));
            }
        }
    }

    public final List<LuaTable> playingTrackTables(Instance owner) {
        Intrinsics.checkNotNullParameter(owner, "owner");
        Instance rig = rigOf(owner);
        if (rig == null) {
            return CollectionsKt.emptyList();
        }
        Iterable iterable = this.scriptTracks;
        Collection arrayList = new ArrayList();
        for (Object obj : iterable) {
            ScriptTrack scriptTrack = (ScriptTrack) obj;
            if (scriptTrack.getRig() == rig && scriptTrack.getWantPlay()) {
                arrayList.add(obj);
            }
        }
        Iterable iterable2 = (List) arrayList;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
        Iterator it = iterable2.iterator();
        while (it.hasNext()) {
            arrayList2.add(((ScriptTrack) it.next()).getTable());
        }
        return (List) arrayList2;
    }

    private final void startTrack(ScriptTrack st) {
        AnimPlayer animPlayer;
        AnimClip clip = st.getClip();
        if (clip == null) {
            return;
        }
        Map map = this.rigPlayers;
        Instance rig = st.getRig();
        AnimPlayer animPlayer2 = (AnimPlayer) map.get(rig);
        if (animPlayer2 == null) {
            animPlayer = new AnimPlayer();
            map.put(rig, animPlayer);
        } else {
            animPlayer = animPlayer2;
        }
        AnimPlayer p = animPlayer;
        float fade = st.getFade();
        float weight = st.getWeight();
        float speed = st.getSpeed();
        AnimPlayer.Companion companion = AnimPlayer.INSTANCE;
        Integer priority = st.getPriority();
        AnimPlayer.Track t = p.play(clip, "script", fade, weight, speed, companion.rank(priority != null ? priority.intValue() : clip.getPriority()));
        Boolean looped = st.getLooped();
        if (looped != null) {
            t.setLooped(looped.booleanValue());
        }
        st.setTrack(t);
    }

    private final void stepScriptTracks(float dt) {
        try {
        if (this.scriptTracks.isEmpty() && this.rigPlayers.isEmpty()) {
            return;
        }
        Iterator<ScriptTrack> it = this.scriptTracks.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            ScriptTrack next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            ScriptTrack st = next;
            if (st.getRig().getParent() == null) {
                it.remove();
            } else {
                if (st.getWantPlay() && st.getTrack() == null) {
                    startTrack(st);
                }
                AnimPlayer.Track t = st.getTrack();
                if (t != null) {
                    if (st.getRig() == this.character && st.getWantPlay()) {
                        this.customAnimateAt = this.time;
                    }
                    if (t.getLoops() > st.getLoopsSeen()) {
                        st.setLoopsSeen(t.getLoops());
                        this.rt.fire(st.getTable(), "DidLoop", new LuaValue[0]);
                    }
                    if (t.getEnded() && !t.getLooped() && st.getWantPlay()) {
                        st.setWantPlay(false);
                        AnimPlayer animPlayer = this.rigPlayers.get(st.getRig());
                        if (animPlayer != null) {
                            animPlayer.stop(t, 0.15f);
                        }
                        st.setTrack(null);
                        this.rt.fire(st.getTable(), "Stopped", new LuaValue[0]);
                        this.rt.fire(st.getTable(), "Ended", new LuaValue[0]);
                    }
                }
            }
        }
        Iterator<Map.Entry<Instance, AnimPlayer>> it2 = this.rigPlayers.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Instance, AnimPlayer> next2 = it2.next();
            Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
            Map.Entry<Instance, AnimPlayer> entry = next2;
            Instance rig = entry.getKey();
            AnimPlayer p = entry.getValue();
            if (rig.getParent() == null) {
                it2.remove();
            } else {
                p.step(dt);
                if (rig != this.character && !p.getTracks().isEmpty()) {
                    Intrinsics.checkNotNull(rig);
                    Intrinsics.checkNotNull(p);
                    poseRig(rig, p);
                }
                if (p.getTracks().isEmpty() && rig != this.character) {
                    it2.remove();
                    this.rigJoints.remove(rig);
                }
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    private static final class RigJoint {
        private final CFrame fixed;
        private final Instance inst;
        private final boolean motor;
        private final Instance p0;
        private final Instance p1;

        public RigJoint(Instance inst, Instance p0, Instance p1, boolean motor, CFrame fixed) {
            Intrinsics.checkNotNullParameter(inst, "inst");
            Intrinsics.checkNotNullParameter(p0, "p0");
            Intrinsics.checkNotNullParameter(p1, "p1");
            this.inst = inst;
            this.p0 = p0;
            this.p1 = p1;
            this.motor = motor;
            this.fixed = fixed;
        }

        public final CFrame getFixed() {
            return this.fixed;
        }

        public final Instance getInst() {
            return this.inst;
        }

        public final boolean getMotor() {
            return this.motor;
        }

        public final Instance getP0() {
            return this.p0;
        }

        public final Instance getP1() {
            return this.p1;
        }
    }

    private final void poseRig(Instance rig, AnimPlayer p) {
        try {
        Pair<Instance, List<RigJoint>> pairBuildRig;
        CFrame identity;
        CFrame c0;
        Map map = this.rigJoints;
        Pair<Instance, List<RigJoint>> pair = (Pair<Instance, List<RigJoint>>) map.get(rig);
        if (pair == null) {
            pairBuildRig = buildRig(rig);
            if (pairBuildRig == null) {
                return;
            } else {
                map.put(rig, pairBuildRig);
            }
        } else {
            pairBuildRig = pair;
        }
        Pair<Instance, List<RigJoint>> pair2 = pairBuildRig;
        Instance root = pair2.component1();
        List<RigJoint> listComponent2 = pair2.component2();
        if (root.getParent() == null) {
            this.rigJoints.remove(rig);
            return;
        }
        IdentityHashMap cfs = new IdentityHashMap();
        cfs.put(root, PartData.INSTANCE.cframe(root));
        this.writing = true;
        try {
            for (RigJoint j : listComponent2) {
                CFrame a = (CFrame) cfs.get(j.getP0());
                if (a != null) {
                    if (j.getFixed() != null) {
                        c0 = a.times(j.getFixed());
                    } else {
                        Object obj = j.getInst().get("C0");
                        CFrame c1 = obj instanceof CFrame ? (CFrame) obj : null;
                        if (c1 == null) {
                            c1 = CFrame.INSTANCE.getIDENTITY();
                        }
                        Object obj2 = j.getInst().get("C1");
                        CFrame c2 = obj2 instanceof CFrame ? (CFrame) obj2 : null;
                        if (c2 == null) {
                            c2 = CFrame.INSTANCE.getIDENTITY();
                        }
                        CFrame cFrameTimes = a.times(c1);
                        if (!j.getMotor() || (identity = p.transform(j.getP1().getName())) == null) {
                            CFrame.Companion companion = CFrame.INSTANCE;
                            identity = companion.getIDENTITY();
                        }
                        c0 = cFrameTimes.times(identity).times(c2.inverse());
                    }
                    try {
                        cfs.put(j.getP1(), c0);
                        CFrame cf = c0;
                        Instance.set$default(j.getP1(), "CFrame", cf, false, 4, null);
                    } catch (Throwable th) {
                        this.writing = false;
                        throw th;
                    }
                }
            }
            this.writing = false;
        } catch (Throwable th2) {
            Throwable th = th2;
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX WARN: Code duplicated, block: B:100:0x01d0  */
    /* JADX WARN: Code duplicated, block: B:191:0x01c6 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:192:0x01ca A[EDGE_INSN: B:192:0x01ca->B:97:0x01ca BREAK  A[LOOP:5: B:75:0x0174->B:95:0x01c3], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:193:0x01bd A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:194:0x01b8 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:60:0x011e  */
    /* JADX WARN: Code duplicated, block: B:62:0x0128  */
    /* JADX WARN: Code duplicated, block: B:63:0x012b  */
    /* JADX WARN: Code duplicated, block: B:70:0x013b  */
    /* JADX WARN: Code duplicated, block: B:73:0x0158 A[LOOP:4: B:71:0x0152->B:73:0x0158, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:77:0x017a  */
    /* JADX WARN: Code duplicated, block: B:82:0x0197  */
    /* JADX WARN: Code duplicated, block: B:85:0x01a1  */
    /* JADX WARN: Code duplicated, block: B:87:0x01b3  */
    /* JADX WARN: Code duplicated, block: B:88:0x01b5  */
    /* JADX WARN: Code duplicated, block: B:91:0x01ba A[LOOP:6: B:83:0x019b->B:91:0x01ba, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:95:0x01c3 A[LOOP:5: B:75:0x0174->B:95:0x01c3, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:99:0x01cf A[RETURN] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v14 */
    /* JADX WARN: Type inference failed for: r11v15, types: [com.goydevv.studrblx.model.CFrame] */
    /* JADX WARN: Type inference failed for: r11v18, types: [com.goydevv.studrblx.model.CFrame] */
    /* JADX WARN: Type inference failed for: r11v30 */
    /* JADX WARN: Type inference failed for: r12v10, types: [com.goydevv.studrblx.model.CFrame] */
    /* JADX WARN: Type inference failed for: r12v13, types: [com.goydevv.studrblx.model.CFrame] */
    /* JADX WARN: Type inference failed for: r12v18 */
    /* JADX WARN: Type inference failed for: r12v8 */
    /* JADX WARN: Type inference failed for: r12v9 */
    private final Pair<Instance, List<RigJoint>> buildRig(Instance rig) {
        Object obj;
        Pair<Instance, List<RigJoint>> pair;
        ArrayList arrayList;
        Iterator it;
        Iterator it2;
        Object next;
        Instance instance;
        ArrayList arrayList2;
        Iterator it3;
        boolean z;
        boolean z2;
        CFrame cFrameTimes;
        Pair<Instance, List<RigJoint>> pair2 = null;
        List listDescendants$default = Instance.descendants$default(rig, null, 1, null);
        ArrayList arrayList3 = new ArrayList();
        for (Object obj2 : listDescendants$default) {
            if (((Instance) obj2).isA("BasePart")) {
                arrayList3.add(obj2);
            }
        }
        Set set = CollectionsKt.toSet(arrayList3);
        ArrayList arrayList4 = new ArrayList();
        for (Instance instance2 : Instance.descendants$default(rig, null, 1, null)) {
            if (instance2.isA("JointInstance")) {
                Object obj3 = instance2.get("Part0");
                Instance instance3 = obj3 instanceof Instance ? (Instance) obj3 : null;
                Object obj4 = instance2.get("Part1");
                Instance instance4 = obj4 instanceof Instance ? (Instance) obj4 : null;
                if (instance3 != null && instance4 != null && set.contains(instance3) && set.contains(instance4) && instance3 != instance4) {
                    arrayList4.add(new RigJoint(instance2, instance3, instance4, instance2.isA("Motor"), null));
                }
            } else if (Intrinsics.areEqual(instance2.getClassName(), "WeldConstraint")) {
                Object obj5 = instance2.get("Part0Internal");
                if (obj5 == null) {
                    obj5 = instance2.get("Part0");
                }
                Instance instance5 = obj5 instanceof Instance ? (Instance) obj5 : null;
                Object obj6 = instance2.get("Part1Internal");
                if (obj6 == null) {
                    obj6 = instance2.get("Part1");
                }
                Instance instance6 = obj6 instanceof Instance ? (Instance) obj6 : null;
                if (instance5 != null && instance6 != null && set.contains(instance5) && set.contains(instance6) && instance5 != instance6) {
                    arrayList4.add(new RigJoint(instance2, instance5, instance6, false, PartData.INSTANCE.cframe(instance5).inverse().times(PartData.INSTANCE.cframe(instance6))));
                }
            }
        }
        Instance instanceFindFirstChild$default = Instance.findFirstChild$default(rig, "HumanoidRootPart", false, 2, null);
        if (instanceFindFirstChild$default == null) {
            obj = rig.get("PrimaryPart");
            if (obj instanceof Instance) {
                instanceFindFirstChild$default = (Instance) obj;
            } else {
                instanceFindFirstChild$default = null;
            }
            if (instanceFindFirstChild$default == null || !set.contains(instanceFindFirstChild$default)) {
                instanceFindFirstChild$default = null;
            }
            if (instanceFindFirstChild$default == null) {
                ArrayList arrayList5 = arrayList4;
                arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList5, 10));
                it = arrayList5.iterator();
                while (it.hasNext()) {
                    arrayList.add(((RigJoint) it.next()).getP0());
                }
                it2 = arrayList.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        pair = pair2;
                        next = pair;
                        break;
                    }
                    next = it2.next();
                    instance = (Instance) next;
                    arrayList2 = arrayList4;
                    if ((arrayList2 instanceof Collection) && arrayList2.isEmpty()) {
                        pair = pair2;
                        z = true;
                    } else {
                        it3 = arrayList2.iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                pair = pair2;
                                z = true;
                                break;
                            }
                            pair = pair2;
                            if (((RigJoint) it3.next()).getP1() == instance) {
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            if (z2) {
                                z = false;
                                break;
                            }
                            pair2 = pair;
                        }
                    }
                    if (!z) {
                        break;
                    }
                    pair2 = pair;
                }
                instanceFindFirstChild$default = (Instance) next;
                if (instanceFindFirstChild$default == null) {
                    return pair;
                }
            } else {
                pair = null;
            }
        } else {
            if (!set.contains(instanceFindFirstChild$default)) {
                instanceFindFirstChild$default = null;
            }
            if (instanceFindFirstChild$default != null) {
                pair = null;
            } else {
                obj = rig.get("PrimaryPart");
                if (obj instanceof Instance) {
                    instanceFindFirstChild$default = (Instance) obj;
                } else {
                    instanceFindFirstChild$default = null;
                }
                instanceFindFirstChild$default = instanceFindFirstChild$default != null ? null : null;
                if (instanceFindFirstChild$default == null) {
                    ArrayList arrayList6 = arrayList4;
                    arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList6, 10));
                    it = arrayList6.iterator();
                    while (it.hasNext()) {
                        arrayList.add(((RigJoint) it.next()).getP0());
                    }
                    it2 = arrayList.iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            pair = pair2;
                            next = pair;
                            break;
                        }
                        next = it2.next();
                        instance = (Instance) next;
                        arrayList2 = arrayList4;
                        if (arrayList2 instanceof Collection) {
                            it3 = arrayList2.iterator();
                            while (true) {
                                if (!it3.hasNext()) {
                                    pair = pair2;
                                    z = true;
                                    break;
                                }
                                pair = pair2;
                                if (((RigJoint) it3.next()).getP1() == instance) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                if (z2) {
                                    z = false;
                                    break;
                                }
                                pair2 = pair;
                            }
                        } else {
                            it3 = arrayList2.iterator();
                            while (true) {
                                if (!it3.hasNext()) {
                                    pair = pair2;
                                    z = true;
                                    break;
                                }
                                pair = pair2;
                                if (((RigJoint) it3.next()).getP1() == instance) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                                if (z2) {
                                    z = false;
                                    break;
                                }
                                pair2 = pair;
                            }
                        }
                        if (!z) {
                            break;
                        }
                        pair2 = pair;
                    }
                    instanceFindFirstChild$default = (Instance) next;
                    if (instanceFindFirstChild$default == null) {
                        return pair;
                    }
                } else {
                    pair = null;
                }
            }
        }
        ArrayList arrayList7 = new ArrayList();
        Set setNewSetFromMap = Collections.newSetFromMap(new IdentityHashMap());
        setNewSetFromMap.add(instanceFindFirstChild$default);
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(instanceFindFirstChild$default);
        Set setNewSetFromMap2 = Collections.newSetFromMap(new IdentityHashMap());
        while (!arrayDeque.isEmpty()) {
            Instance instance7 = (Instance) arrayDeque.removeFirst();
            Iterator it4 = arrayList4.iterator();
            Intrinsics.checkNotNullExpressionValue(it4, "iterator(...)");
            while (it4.hasNext()) {
                Object next2 = it4.next();
                Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
                RigJoint rigJoint = (RigJoint) next2;
                if (!setNewSetFromMap2.contains(rigJoint)) {
                    if (rigJoint.getP0() == instance7 && !setNewSetFromMap.contains(rigJoint.getP1())) {
                        setNewSetFromMap2.add(rigJoint);
                        setNewSetFromMap.add(rigJoint.getP1());
                        arrayList7.add(rigJoint);
                        arrayDeque.add(rigJoint.getP1());
                    } else if (rigJoint.getP1() == instance7 && !setNewSetFromMap.contains(rigJoint.getP0()) && !rigJoint.getMotor()) {
                        setNewSetFromMap2.add(rigJoint);
                        setNewSetFromMap.add(rigJoint.getP0());
                        CFrame fixed = rigJoint.getFixed();
                        if (fixed == null || (cFrameTimes = fixed.inverse()) == null) {
                            Object obj7 = rigJoint.getInst().get("C1");
                            CFrame identity = obj7 instanceof CFrame ? (CFrame) obj7 : null;
                            if (identity == null) {
                                identity = CFrame.INSTANCE.getIDENTITY();
                            }
                            Object obj8 = rigJoint.getInst().get("C0");
                            CFrame identity2 = obj8 instanceof CFrame ? (CFrame) obj8 : null;
                            if (identity2 == null) {
                                identity2 = CFrame.INSTANCE.getIDENTITY();
                            }
                            cFrameTimes = identity.times(identity2.inverse());
                        }
                        arrayList7.add(new RigJoint(rigJoint.getInst(), rigJoint.getP1(), rigJoint.getP0(), false, cFrameTimes));
                        arrayDeque.add(rigJoint.getP0());
                    }
                }
            }
        }
        return TuplesKt.to(instanceFindFirstChild$default, arrayList7);
    }

    private final CFrame transformFor(Joint j) {
        CFrame base;
        CFrame cFrameTransformOver;
        if (!j.getMotor()) {
            return CFrame.INSTANCE.getIDENTITY();
        }
        AnimPlayer scripted = this.rigPlayers.get(this.character);
        if (this.time - this.customAnimateAt < 1.0d) {
            base = CFrame.INSTANCE.getIDENTITY();
        } else {
            DefaultAnimate defaultAnimate = this.animator;
            base = defaultAnimate != null ? defaultAnimate.transform(j.getP1().getName()) : null;
        }
        if (scripted != null && !scripted.getTracks().isEmpty() && (cFrameTransformOver = scripted.transformOver(base, j.getP1().getName())) != null) {
            return cFrameTransformOver;
        }
        if (base != null) {
            return base;
        }
        if (!this.isR15) {
            Float f = this.motorAngle.get(j.getName());
            if (f != null) {
                CFrame cFrameRz = CFrame.INSTANCE.rz(f.floatValue());
                if (cFrameRz != null) {
                    return cFrameRz;
                }
            }
            return CFrame.INSTANCE.getIDENTITY();
        }
        float[] a = this.r15Pose.get(j.getName());
        if (a == null) {
            return CFrame.INSTANCE.getIDENTITY();
        }
        CFrame cf = CFrame.INSTANCE.rx(a[0]);
        if (!(a[1] == 0.0f)) {
            cf = CFrame.INSTANCE.ry(a[1]).times(cf);
        }
        if (!(a[2] == 0.0f)) {
            return cf.times(CFrame.INSTANCE.rz(a[2]));
        }
        return cf;
    }

    /* JADX WARN: Code duplicated, block: B:121:0x0295 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:122:0x0280 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:77:0x01ea A[Catch: all -> 0x029e, PHI: r7
      0x01ea: PHI (r7v9 com.goydevv.studrblx.model.Instance) = (r7v7 com.goydevv.studrblx.model.Instance), (r7v10 com.goydevv.studrblx.model.Instance) binds: [B:75:0x01e6, B:70:0x01d7] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {all -> 0x029e, blocks: (B:17:0x0063, B:18:0x0079, B:20:0x0080, B:22:0x00a9, B:25:0x00af, B:26:0x00b5, B:28:0x00c3, B:30:0x00c8, B:32:0x00d0, B:35:0x00dd, B:37:0x00ea, B:40:0x0105, B:39:0x00f1, B:41:0x010c, B:44:0x011a, B:46:0x0127, B:49:0x014a, B:48:0x0132, B:50:0x014f, B:51:0x015b, B:53:0x0161, B:54:0x0181, B:55:0x018c, B:57:0x0192, B:60:0x01ae, B:61:0x01b0, B:62:0x01b9, B:64:0x01bd, B:67:0x01ca, B:69:0x01d0, B:77:0x01ea, B:79:0x01f4, B:81:0x01f9, B:82:0x01ff, B:86:0x0211, B:87:0x0266, B:89:0x026c, B:91:0x0280, B:71:0x01d9, B:73:0x01dd), top: B:100:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:79:0x01f4 A[Catch: all -> 0x029e, TryCatch #0 {all -> 0x029e, blocks: (B:17:0x0063, B:18:0x0079, B:20:0x0080, B:22:0x00a9, B:25:0x00af, B:26:0x00b5, B:28:0x00c3, B:30:0x00c8, B:32:0x00d0, B:35:0x00dd, B:37:0x00ea, B:40:0x0105, B:39:0x00f1, B:41:0x010c, B:44:0x011a, B:46:0x0127, B:49:0x014a, B:48:0x0132, B:50:0x014f, B:51:0x015b, B:53:0x0161, B:54:0x0181, B:55:0x018c, B:57:0x0192, B:60:0x01ae, B:61:0x01b0, B:62:0x01b9, B:64:0x01bd, B:67:0x01ca, B:69:0x01d0, B:77:0x01ea, B:79:0x01f4, B:81:0x01f9, B:82:0x01ff, B:86:0x0211, B:87:0x0266, B:89:0x026c, B:91:0x0280, B:71:0x01d9, B:73:0x01dd), top: B:100:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:81:0x01f9 A[Catch: all -> 0x029e, TryCatch #0 {all -> 0x029e, blocks: (B:17:0x0063, B:18:0x0079, B:20:0x0080, B:22:0x00a9, B:25:0x00af, B:26:0x00b5, B:28:0x00c3, B:30:0x00c8, B:32:0x00d0, B:35:0x00dd, B:37:0x00ea, B:40:0x0105, B:39:0x00f1, B:41:0x010c, B:44:0x011a, B:46:0x0127, B:49:0x014a, B:48:0x0132, B:50:0x014f, B:51:0x015b, B:53:0x0161, B:54:0x0181, B:55:0x018c, B:57:0x0192, B:60:0x01ae, B:61:0x01b0, B:62:0x01b9, B:64:0x01bd, B:67:0x01ca, B:69:0x01d0, B:77:0x01ea, B:79:0x01f4, B:81:0x01f9, B:82:0x01ff, B:86:0x0211, B:87:0x0266, B:89:0x026c, B:91:0x0280, B:71:0x01d9, B:73:0x01dd), top: B:100:0x0063 }] */
    /* JADX WARN: Code duplicated, block: B:84:0x020b  */
    /* JADX WARN: Code duplicated, block: B:85:0x020f  */
    /* JADX WARN: Code duplicated, block: B:89:0x026c A[Catch: all -> 0x029e, TryCatch #0 {all -> 0x029e, blocks: (B:17:0x0063, B:18:0x0079, B:20:0x0080, B:22:0x00a9, B:25:0x00af, B:26:0x00b5, B:28:0x00c3, B:30:0x00c8, B:32:0x00d0, B:35:0x00dd, B:37:0x00ea, B:40:0x0105, B:39:0x00f1, B:41:0x010c, B:44:0x011a, B:46:0x0127, B:49:0x014a, B:48:0x0132, B:50:0x014f, B:51:0x015b, B:53:0x0161, B:54:0x0181, B:55:0x018c, B:57:0x0192, B:60:0x01ae, B:61:0x01b0, B:62:0x01b9, B:64:0x01bd, B:67:0x01ca, B:69:0x01d0, B:77:0x01ea, B:79:0x01f4, B:81:0x01f9, B:82:0x01ff, B:86:0x0211, B:87:0x0266, B:89:0x026c, B:91:0x0280, B:71:0x01d9, B:73:0x01dd), top: B:100:0x0063 }] */
    private final void writeCharacter(float dt) {
        CFrame scf;
        Instance instanceFindFirstChild$default;
        Instance instanceFindFirstChild$default2;
        CFrame c1;
        float f;
        CFrame cFrameTimes;
        Instance key;
        CFrame value;
        CFrame cFrameTimes2;
        CFrame cFrameTimes3;
        Instance root = this.hrp;
        if (root == null) {
            return;
        }
        animate(dt);
        Instance seat = this.seated;
        if (seat == null) {
            float fwdX = (float) (-Math.sin(this.renderFacing));
            float fwdZ = (float) (-Math.cos(this.renderFacing));
            float rgtX = (float) Math.cos(this.renderFacing);
            float rgtZ = (float) (-Math.sin(this.renderFacing));
            float tPitch = 0.0f;
            float tRoll = 0.0f;
            if (this.climbing) {
                tPitch = 0.08f;
            } else {
                float fwdSpd = (this.vel.getX() * fwdX) + (this.vel.getZ() * fwdZ);
                float sideSpd = (this.vel.getX() * rgtX) + (this.vel.getZ() * rgtZ);
                tPitch += RangesKt.coerceIn(-fwdSpd * 0.0055f, -0.11f, 0.08f);
                float yawDelta = wrapAngle(this.renderFacing - this.prevRenderFacing);
                float turnRate = dt > 1.0E-4f ? RangesKt.coerceIn(yawDelta / dt, -8.0f, 8.0f) : 0.0f;
                tRoll += RangesKt.coerceIn((sideSpd * 0.004f) - (turnRate * 0.018f), -0.13f, 0.13f);
                if (this.grounded && this.groundNormal != null) {
                    float gnFwd = (this.groundNormal.getX() * fwdX) + (this.groundNormal.getZ() * fwdZ);
                    float gnRgt = (this.groundNormal.getX() * rgtX) + (this.groundNormal.getZ() * rgtZ);
                    tPitch += RangesKt.coerceIn(gnFwd * 0.28f, -0.18f, 0.18f);
                    tRoll += RangesKt.coerceIn(-gnRgt * 0.28f, -0.18f, 0.18f);
                }
                if (this.wallBumpDecay > 0.001f) {
                    float wbFwd = (this.wallBumpNormal.getX() * fwdX) + (this.wallBumpNormal.getZ() * fwdZ);
                    float wbRgt = (this.wallBumpNormal.getX() * rgtX) + (this.wallBumpNormal.getZ() * rgtZ);
                    tPitch += RangesKt.coerceIn(wbFwd * 0.16f * this.wallBumpDecay, -0.15f, 0.15f);
                    tRoll += RangesKt.coerceIn(-wbRgt * 0.16f * this.wallBumpDecay, -0.15f, 0.15f);
                    this.wallBumpDecay = Math.max(0.0f, this.wallBumpDecay - (dt * 6.5f));
                }
            }
            this.prevRenderFacing = this.renderFacing;
            float tiltBlend = RangesKt.coerceIn(1.0f - ((float) Math.exp((-12.0f) * dt)), 0.0f, 1.0f);
            this.bodyPitch += (tPitch - this.bodyPitch) * tiltBlend;
            this.bodyRoll += (tRoll - this.bodyRoll) * tiltBlend;
            scf = CFrame.INSTANCE.at(this.pos).times(CFrame.INSTANCE.ry(this.renderFacing)).times(CFrame.INSTANCE.rx(this.bodyPitch)).times(CFrame.INSTANCE.rz(this.bodyRoll));
        } else {
            CFrame scf2 = PartData.INSTANCE.cframe(seat);
            Vector3 ss = PartData.INSTANCE.size(seat);
            scf = scf2.times(CFrame.INSTANCE.at(new Vector3(0.0f, (ss.getY() / 2.0f) + (this.isR15 ? (-this.charMinY) - 1.4f : 1.5f), 0.0f)));
        }
        CFrame base = scf;
        if (seat != null) {
            this.pos = base.getPosition();
        }
        this.writing = true;
        try {
            IdentityHashMap cfs = new IdentityHashMap();
            cfs.put(root, base);
            Iterator<Pair<Joint, Boolean>> it = this.jointOrder.iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Pair<Joint, Boolean> next = it.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                Pair<Joint, Boolean> pair = next;
                Joint j = pair.component1();
                boolean forward = pair.component2().booleanValue();
                Object obj = j.getInst().get("C0");
                CFrame c0 = obj instanceof CFrame ? (CFrame) obj : null;
                if (c0 == null) {
                    c0 = CFrame.INSTANCE.getIDENTITY();
                }
                Object obj2 = j.getInst().get("C1");
                c1 = obj2 instanceof CFrame ? (CFrame) obj2 : null;
                if (c1 == null) {
                    c1 = CFrame.INSTANCE.getIDENTITY();
                }
                if (forward) {
                    CFrame a = (CFrame) cfs.get(j.getP0());
                    if (a != null) {
                        IdentityHashMap identityHashMap = cfs;
                        Instance p1 = j.getP1();
                        CFrame fixed = j.getFixed();
                        if (fixed == null || (cFrameTimes2 = a.times(fixed)) == null) {
                            cFrameTimes2 = a.times(c0).times(transformFor(j)).times(c1.inverse());
                        }
                        identityHashMap.put(p1, cFrameTimes2);
                    }
                } else {
                    CFrame b = (CFrame) cfs.get(j.getP1());
                    if (b != null) {
                        IdentityHashMap identityHashMap2 = cfs;
                        Instance p0 = j.getP0();
                        CFrame fixed2 = j.getFixed();
                        if (fixed2 == null || (cFrameTimes3 = b.times(fixed2.inverse())) == null) {
                            cFrameTimes3 = b.times(c1).times(transformFor(j).inverse()).times(c0.inverse());
                        }
                        identityHashMap2.put(p0, cFrameTimes3);
                    }
                }
            }
            for (Map.Entry<Instance, CFrame> entry : this.rigid.entrySet()) {
                Instance part = entry.getKey();
                CFrame off = entry.getValue();
                Intrinsics.checkNotNull(off);
                cfs.put(part, base.times(off));
            }
            for (Map.Entry entry2 : ((Map<?, ?>) cfs).entrySet()) {
                Instance part2 = (Instance) entry2.getKey();
                CFrame cf = (CFrame) entry2.getValue();
                if (part2.getParent() != null) {
                    if (part2 == root) {
                        this.lastWritten = cf;
                    }
                    Instance.set$default(part2, "CFrame", cf, false, 4, null);
                }
            }
            Instance instance = this.equipped;
            if (instance != null && (instanceFindFirstChild$default = Instance.findFirstChild$default(instance, "Handle", false, 2, null)) != null) {
                Instance instance2 = this.character;
                if (instance2 == null || (instanceFindFirstChild$default2 = Instance.findFirstChild$default(instance2, "Right Arm", false, 2, null)) == null) {
                    Instance instance3 = this.character;
                    instanceFindFirstChild$default2 = instance3 != null ? Instance.findFirstChild$default(instance3, "RightHand", false, 2, null) : null;
                    if (instanceFindFirstChild$default2 != null) {
                        Object obj3 = instance.get("Grip");
                        c1 = obj3 instanceof CFrame ? (CFrame) obj3 : null;
                        if (c1 == null) {
                            c1 = CFrame.INSTANCE.getIDENTITY();
                        }
                        CFrame cframe = PartData.INSTANCE.cframe(instanceFindFirstChild$default2);
                        if (this.isR15) {
                            f = -0.15f;
                        } else {
                            f = -1.0f;
                        }
                        cFrameTimes = cframe.times(new CFrame(0.0f, f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null)).times(CFrame.INSTANCE.rx(-1.5708f)).times(c1.inverse());
                        Instance.set$default(instanceFindFirstChild$default, "CFrame", cFrameTimes, false, 4, null);
                        for (Map.Entry<Instance, CFrame> entry3 : this.toolOffsets.entrySet()) {
                            key = entry3.getKey();
                            value = entry3.getValue();
                            if (key != instanceFindFirstChild$default) {
                                Intrinsics.checkNotNull(value);
                                Instance.set$default(key, "CFrame", cFrameTimes.times(value), false, 4, null);
                            }
                        }
                    }
                } else {
                    Object obj4 = instance.get("Grip");
                    c1 = obj4 instanceof CFrame ? (CFrame) obj4 : null;
                    if (c1 == null) {
                        c1 = CFrame.INSTANCE.getIDENTITY();
                    }
                    CFrame cframe2 = PartData.INSTANCE.cframe(instanceFindFirstChild$default2);
                    if (this.isR15) {
                        f = -0.15f;
                    } else {
                        f = -1.0f;
                    }
                    cFrameTimes = cframe2.times(new CFrame(0.0f, f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null)).times(CFrame.INSTANCE.rx(-1.5708f)).times(c1.inverse());
                    Instance.set$default(instanceFindFirstChild$default, "CFrame", cFrameTimes, false, 4, null);
                    for (Map.Entry<Instance, CFrame> entry3 : this.toolOffsets.entrySet()) {
                        key = entry3.getKey();
                        value = entry3.getValue();
                        if (key != instanceFindFirstChild$default) {
                            Intrinsics.checkNotNull(value);
                            Instance.set$default(key, "CFrame", cFrameTimes.times(value), false, 4, null);
                        }
                    }
                }
            }
        } finally {
            this.writing = false;
        }
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    private static final class Debris {
        private final Instance part;
        private Vector3 spin;
        private Vector3 vel;

        public Debris(Instance part, Vector3 vel, Vector3 spin) {
            Intrinsics.checkNotNullParameter(part, "part");
            Intrinsics.checkNotNullParameter(vel, "vel");
            Intrinsics.checkNotNullParameter(spin, "spin");
            this.part = part;
            this.vel = vel;
            this.spin = spin;
        }

        public final Instance getPart() {
            return this.part;
        }

        public final Vector3 getSpin() {
            return this.spin;
        }

        public final Vector3 getVel() {
            return this.vel;
        }

        public final void setSpin(Vector3 vector3) {
            Intrinsics.checkNotNullParameter(vector3, "<set-?>");
            this.spin = vector3;
        }

        public final void setVel(Vector3 vector3) {
            Intrinsics.checkNotNullParameter(vector3, "<set-?>");
            this.vel = vector3;
        }
    }

    private final void breakApart() {
        this.debris.clear();
        Random rnd = new Random();
        Iterator<Map.Entry<Instance, CFrame>> it = this.offsets.entrySet().iterator();
        while (it.hasNext()) {
            Instance part = it.next().getKey();
            if (part != this.hrp && part.getParent() != null) {
                PartData partData = PartData.INSTANCE;
                Intrinsics.checkNotNull(part);
                Vector3 vector3Minus = partData.cframe(part).getPosition().minus(this.pos);
                Vector3 out = new Vector3(vector3Minus.getX(), 0.0f, vector3Minus.getZ());
                Vector3 push = out.getMagnitude() > 0.01f ? out.getUnit().times(6.0f) : Vector3.INSTANCE.getZERO();
                this.debris.add(new Debris(part, this.vel.times(0.5f).plus(push).plus(new Vector3((rnd.nextFloat() - 0.5f) * 8.0f, (rnd.nextFloat() * 6.0f) + 4.0f, (rnd.nextFloat() - 0.5f) * 8.0f)), new Vector3((rnd.nextFloat() - 0.5f) * 10.0f, (rnd.nextFloat() - 0.5f) * 10.0f, (rnd.nextFloat() - 0.5f) * 10.0f)));
            }
        }
        Instance instance = this.character;
        List<Instance> listDescendants$default = instance != null ? Instance.descendants$default(instance, null, 1, null) : null;
        if (listDescendants$default == null) {
            listDescendants$default = CollectionsKt.emptyList();
        }
        for (Instance j : listDescendants$default) {
            if (Intrinsics.areEqual(j.getClassName(), "Motor6D")) {
                j.destroy();
            }
        }
    }

    private final void stepDebris(float dt) {
        Pair<Vector3, Float> pairContact;
        Vector3 m;
        float g = getGravity();
        this.writing = true;
        boolean z = false;
        try {
            Iterator<Debris> it = this.debris.iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (it.hasNext()) {
                Debris next = it.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                Debris b = next;
                Instance p = b.getPart();
                if (p.getParent() == null) {
                    z = false;
                } else {
                    b.setVel(new Vector3(b.getVel().getX(), b.getVel().getY() - (g * dt), b.getVel().getZ()));
                    CFrame cf = PartData.INSTANCE.cframe(p).plus(b.getVel().times(dt));
                    float sm = b.getSpin().getMagnitude();
                    if (sm > 0.01f) {
                        cf = CFrame.INSTANCE.at(cf.getPosition()).times(CFrame.INSTANCE.fromAxisAngle(b.getSpin().div(sm), sm * dt)).times(cf.rotationOnly()).orthonormalize();
                    }
                    Instance.set$default(p, "CFrame", cf, false, 4, null);
                    Pair<Vector3, Vector3> pairPartAabb = Geometry.INSTANCE.partAabb(p);
                    Vector3 a0 = pairPartAabb.component1();
                    Vector3 a1 = pairPartAabb.component2();
                    ConvexShape shape = Geometry.shapeOf$default(Geometry.INSTANCE, p, null, 2, null);
                    Vector3 push = Vector3.INSTANCE.getZERO();
                    Vector3 push2 = push;
                    for (Object _sObj : query$default(this, a0, a1, false, 4, null)) {
                        Instance s = (Instance) _sObj;
                        if (!Intrinsics.areEqual(s.get("CanCollide"), Boolean.valueOf(z)) && !isCharacterPart(s) && (pairContact = Geometry.INSTANCE.contact(shape, a0, a1, s)) != null && (m = pairContact.getFirst()) != null && m.getMagnitude() > push2.getMagnitude()) {
                            push2 = m;
                        }
                    }
                    if (push2.getMagnitude() > 1.0E-4f) {
                        Instance.set$default(p, "CFrame", PartData.INSTANCE.cframe(p).plus(push2), false, 4, null);
                        Vector3 n = push2.getUnit();
                        float vn = b.getVel().dot(n);
                        if (vn < 0.0f) {
                            b.setVel(b.getVel().minus(n.times(1.3f * vn)));
                        }
                        b.setVel(new Vector3(b.getVel().getX() * 0.8f, b.getVel().getY(), b.getVel().getZ() * 0.8f));
                        b.setSpin(b.getSpin().times(0.7f));
                        z = false;
                    } else {
                        z = false;
                    }
                }
            }
        } finally {
            this.writing = false;
        }
    }

    private final Vector3 facingVec() {
        return new Vector3(-((float) Math.sin(this.facing)), 0.0f, -((float) Math.cos(this.facing)));
    }

    private final List<RayHit> frontHits() {
        Vector3 f = facingVec();
        float reach = this.charHalfXZ + 1.1f;
        ArrayList out = new ArrayList();
        Vector3 lo = this.pos.plus(new Vector3((-reach) - 1.0f, this.charMinY, (-reach) - 1.0f));
        Vector3 hi = this.pos.plus(new Vector3(reach + 1.0f, this.charMaxY, 1.0f + reach));
        Iterable iterableQuery$default = query$default(this, lo, hi, false, 4, null);
        Collection arrayList = new ArrayList();
        for (Object obj : iterableQuery$default) {
            Instance instance = (Instance) obj;
            boolean z = false;
            Iterable iterable = iterableQuery$default;
            if (!Intrinsics.areEqual(instance.get("CanCollide"), (Object) false) && !isCharacterPart(instance)) {
                z = true;
            }
            if (z) {
                arrayList.add(obj);
            }
            iterableQuery$default = iterable;
        }
        List cands = (List) arrayList;
        if (cands.isEmpty()) {
            return CollectionsKt.emptyList();
        }
        for (float y = this.charMinY + 0.3f; y <= this.charMaxY - 0.2f; y += 0.4f) {
            out.add(Geometry.INSTANCE.raycast(cands, new Vector3(this.pos.getX(), this.pos.getY() + y, this.pos.getZ()), f, reach, new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda12
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj2) {
                    return Boolean.valueOf(PlaySession.frontHits$lambda$89((Instance) obj2));
                }
            }));
        }
        return out;
    }

    static final boolean frontHits$lambda$89(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return true;
    }

    private final boolean canClimb(List<RayHit> hits, boolean alreadyClimbing) {
        boolean z;
        boolean z2;
        Instance part;
        int lastHit;
        int hitCount;
        if (hits.isEmpty()) {
            return false;
        }
        List<RayHit> list = hits;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = true;
                    break;
                }
                if (!(((RayHit) it.next()) == null)) {
                    z = false;
                    break;
                }
            }
        } else {
            z = true;
        }
        if (z) {
            return false;
        }
        List<RayHit> list2 = hits;
        if (!(list2 instanceof Collection) || !list2.isEmpty()) {
            Iterator it2 = list2.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    z2 = false;
                    break;
                }
                RayHit rayHit = (RayHit) it2.next();
                if (Intrinsics.areEqual((rayHit == null || (part = rayHit.getPart()) == null) ? null : part.getClassName(), "TrussPart")) {
                    z2 = true;
                    break;
                }
            }
        } else {
            z2 = false;
        }
        if (z2) {
            boolean trussUpper = false;
            for (int ti = 1; ti < hits.size(); ti++) {
                RayHit th = hits.get(ti);
                if (th != null && th.getPart() != null && Intrinsics.areEqual(th.getPart().getClassName(), "TrussPart")) {
                    trussUpper = true;
                    break;
                }
            }
            return trussUpper || !alreadyClimbing;
        }
        if (alreadyClimbing) {
            boolean hasThinRung = false;
            boolean hasGap = false;
            boolean sawHit = false;
            boolean sawMissAfterHit = false;
            for (int ri = 1; ri < hits.size(); ri++) {
                RayHit rh = hits.get(ri);
                if (rh != null && rh.getPart() != null) {
                    if (sawMissAfterHit) hasGap = true;
                    sawHit = true;
                    if (PartData.INSTANCE.size(rh.getPart()).getY() <= 1.35f) {
                        hasThinRung = true;
                    }
                } else if (sawHit) {
                    sawMissAfterHit = true;
                }
            }
            return hasThinRung || hasGap;
        }
        int firstHit = 0;
        Iterator<RayHit> it3 = hits.iterator();
        while (true) {
            lastHit = -1;
            if (!it3.hasNext()) {
                firstHit = -1;
                break;
            }
            if (((RayHit) it3.next()) != null) {
                break;
            }
            firstHit++;
        }
        ListIterator<RayHit> listIterator = hits.listIterator(hits.size());
        while (listIterator.hasPrevious()) {
            if (listIterator.previous() != null) {
                lastHit = listIterator.nextIndex();
                break;
            }
        }
        if (firstHit < 0 || lastHit - firstHit < 2) {
            return false;
        }
        int gaps = 0;
        int run = 0;
        int maxRun = 0;
        int i = firstHit;
        if (i <= lastHit) {
            while (true) {
                if (hits.get(i) == null) {
                    run++;
                    maxRun = Math.max(maxRun, run);
                } else {
                    if (run > 0) {
                        gaps++;
                    }
                    run = 0;
                }
                if (i == lastHit) {
                    break;
                }
                i++;
            }
        }
        List<RayHit> list3 = hits;
        if ((list3 instanceof Collection) && list3.isEmpty()) {
            hitCount = 0;
        } else {
            hitCount = 0;
            Iterator it4 = list3.iterator();
            while (it4.hasNext()) {
                if ((((RayHit) it4.next()) != null) && (hitCount = hitCount + 1) < 0) {
                    CollectionsKt.throwCountOverflow();
                }
            }
        }
        Iterable iterableFilterNotNull = CollectionsKt.filterNotNull(hits);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableFilterNotNull, 10));
        Iterator it5 = iterableFilterNotNull.iterator();
        while (it5.hasNext()) {
            arrayList.add(Float.valueOf(((RayHit) it5.next()).getT()));
        }
        List ts = (List) arrayList;
        return CollectionsKt.maxOrThrow((Iterable<Double>) ts) - CollectionsKt.minOrThrow((Iterable<Double>) ts) <= 0.7f && lastHit >= hits.size() / 2 && gaps >= 1 && ((float) maxRun) * 0.4f <= 2.4f && hitCount >= 2 && hitCount < hits.size();
    }

    private final void setState(String name, int value) {
        Instance hum;
        Integer num;
        if (Intrinsics.areEqual(name, this.lastState) || (hum = this.humanoid) == null) {
            return;
        }
        LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get("HumanoidStateType");
        int old = (linkedHashMap == null || (num = linkedHashMap.get(this.lastState)) == null) ? 8 : num.intValue();
        this.rt.fire(hum, "StateChanged", LuaTypes.INSTANCE.wrap(new EnumItemV("HumanoidStateType", this.lastState, old)), LuaTypes.INSTANCE.wrap(new EnumItemV("HumanoidStateType", name, value)));
        hum.getAttributes().put("__state", name);
        this.lastState = name;
    }

    private final void leaveSeat(boolean jump) {
        Instance s = this.seated;
        if (s == null) {
            return;
        }
        this.seated = null;
        this.seatCooldown = 1.0f;
        Instance.set$default(s, "Occupant", null, false, 4, null);
        Instance instance = this.humanoid;
        if (instance != null) {
            Instance.set$default(instance, "Sit", false, false, 4, null);
            Instance.set$default(instance, "SeatPart", null, false, 4, null);
            LuaRuntime luaRuntime = this.rt;
            LuaBoolean FALSE = LuaValue.FALSE;
            Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            luaRuntime.fire(instance, "Seated", FALSE, NIL);
        }
        if (jump) {
            Vector3 f = facingVec();
            this.vel = new Vector3(f.getX() * 4.0f, 45.0f, f.getZ() * 4.0f);
            this.pos = this.pos.plus(new Vector3(0.0f, 0.5f, 0.0f));
        }
        setState("Jumping", 3);
    }

    private final void trySit(float dt) {
        if (this.seated != null || this.seatCooldown > 0.0f || this.dead) {
            this.seatCooldown -= dt;
            return;
        }
        Pair<Vector3, Vector3> pairCharBox = charBox();
        Vector3 c = pairCharBox.component1();
        Vector3 half = pairCharBox.component2();
        for (Instance p : query(c.minus(half).minus(new Vector3(0.2f, 0.4f, 0.2f)), c.plus(half).plus(new Vector3(0.2f, 0.2f, 0.2f)), true)) {
            if (Intrinsics.areEqual(p.getClassName(), "Seat") || Intrinsics.areEqual(p.getClassName(), "VehicleSeat")) {
                if (!Intrinsics.areEqual(p.get("Disabled"), (Object) true) && !(p.get("Occupant") instanceof Instance)) {
                    Pair<Vector3, Vector3> pairPartAabb = Geometry.INSTANCE.partAabb(p);
                    Vector3 sa = pairPartAabb.component1();
                    Vector3 sb = pairPartAabb.component2();
                    if (this.pos.getY() + this.charMaxY >= sa.getY() && this.pos.getY() + this.charMinY <= sb.getY() + 0.6f) {
                        this.seated = p;
                        this.vel = Vector3.INSTANCE.getZERO();
                        Vector3 lookVector = PartData.INSTANCE.cframe(p).getLookVector();
                        this.facing = (float) Math.atan2(-lookVector.getX(), -lookVector.getZ());
                        Instance.set$default(p, "Occupant", this.humanoid, false, 4, null);
                        Instance instance = this.humanoid;
                        if (instance != null) {
                            Instance.set$default(instance, "Sit", true, false, 4, null);
                            Instance.set$default(instance, "SeatPart", p, false, 4, null);
                            LuaRuntime luaRuntime = this.rt;
                            LuaBoolean TRUE = LuaValue.TRUE;
                            Intrinsics.checkNotNullExpressionValue(TRUE, "TRUE");
                            luaRuntime.fire(instance, "Seated", TRUE, this.rt.wrap(p));
                        }
                        setState("Seated", 13);
                        return;
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v57, types: [T, com.goydevv.studrblx.model.Vector3] */
    /* JADX WARN: Type inference failed for: r8v36, types: [T, com.goydevv.studrblx.model.Vector3] */
    private final void stepCharacter(float dt) {
        Instance instance;
        float fFloatValue;
        float f;
        float f2;
        boolean z;
        Instance instance2 = this.hrp;
        if (instance2 == null || (instance = this.humanoid) == null) {
            return;
        }
        Instance instance3 = this.character;
        if (((instance3 != null ? instance3.getParent() : null) == null || instance2.getParent() == null) && !this.dead) {
            this.dead = true;
            this.deadTimer = respawnTime();
        }
        if (!this.dead && health() <= 0.0f) {
            this.dead = true;
            this.deadTimer = respawnTime();
            leaveSeat(false);
            this.rt.fire(instance, "Died", new LuaValue[0]);
            setState("Dead", 15);
            OutputLog outputLog = OutputLog.INSTANCE;
            Instance instance4 = this.player;
            outputLog.info((instance4 != null ? instance4.getName() : null) + " died");
            if (!Intrinsics.areEqual(instance.get("BreakJointsOnDeath"), (Object) false)) {
                breakApart();
            }
        }
        if (this.dead) {
            stepDebris(dt);
            this.deadTimer -= dt;
            if (this.deadTimer <= 0.0f) {
                Instance instanceFindService = this.dm.findService("Players");
                if (!(instanceFindService != null ? Intrinsics.areEqual(instanceFindService.get("CharacterAutoLoads"), (Object) false) : false)) {
                    this.debris.clear();
                    respawn();
                    return;
                }
                return;
            }
            return;
        }
        CFrame cframe = PartData.INSTANCE.cframe(instance2);
        CFrame cFrame = this.lastWritten;
        if (cFrame != null && !Intrinsics.areEqual(cframe, cFrame) && this.seated == null) {
            this.pos = cframe.getPosition();
            this.vel = Vector3.INSTANCE.getZERO();
            Vector3 lookVector = cframe.getLookVector();
            if (Math.abs(lookVector.getY()) < 0.99f) {
                this.facing = (float) Math.atan2(-lookVector.getX(), -lookVector.getZ());
                this.renderFacing = this.facing;
            }
        }
        if (Intrinsics.areEqual(instance.get("Sit"), (Object) false) && this.seated != null) {
            leaveSeat(false);
        }
        Object obj = instance.get("WalkSpeed");
        Number number = obj instanceof Number ? (Number) obj : null;
        float fFloatValue2 = number != null ? number.floatValue() : 16.0f;
        float gravity = getGravity();
        if (Intrinsics.areEqual(instance.get("UseJumpPower"), (Object) false)) {
            float f3 = gravity * 2.0f;
            Object obj2 = instance.get("JumpHeight");
            Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
            fFloatValue = (float) Math.sqrt(f3 * (number2 != null ? number2.floatValue() : 7.2f));
        } else {
            Object obj3 = instance.get("JumpPower");
            Number number3 = obj3 instanceof Number ? (Number) obj3 : null;
            fFloatValue = number3 != null ? number3.floatValue() : 50.0f;
        }
        float f4 = fFloatValue;
        Vector3 vector3 = new Vector3(-((float) Math.sin(this.camYaw)), 0.0f, -((float) Math.cos(this.camYaw)));
        Vector3 vector4 = new Vector3((float) Math.cos(this.camYaw), 0.0f, -((float) Math.sin(this.camYaw)));
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = this.controlsEnabled ? vector4.times(this.studio.getMoveX()).plus(vector3.times(-this.studio.getMoveY())) : Vector3.INSTANCE.getZERO();
        if (((Vector3) objectRef.element).getMagnitude() > 1.0f) {
            objectRef.element = ((Vector3) objectRef.element).getUnit();
        }
        Vector3 vector5 = this.scriptMoveDir;
        if (vector5 != null && vector5.getMagnitude() > 0.01f) {
            Vector3 vector6 = new Vector3(vector5.getX(), 0.0f, vector5.getZ());
            float magnitude = vector6.getMagnitude();
            Vector3 unit = vector6;
            if (magnitude > 1.0f) {
                unit = vector6.getUnit();
            }
            objectRef.element = unit;
        }
        Vector3 vector7 = this.moveToTarget;
        if (vector7 != null) {
            f = 0.05f;
            Vector3 vector8 = new Vector3(vector7.getX() - this.pos.getX(), 0.0f, vector7.getZ() - this.pos.getZ());
            if (vector8.getMagnitude() < 1.2f) {
                this.moveToTarget = null;
                LuaRuntime luaRuntime = this.rt;
                LuaBoolean TRUE = LuaValue.TRUE;
                Intrinsics.checkNotNullExpressionValue(TRUE, "TRUE");
                luaRuntime.fire(instance, "MoveToFinished", TRUE);
            } else if (((Vector3) objectRef.element).getMagnitude() < 0.05f) {
                objectRef.element = vector8.getUnit();
            }
        } else {
            f = 0.05f;
        }
        this.lastDir = (Vector3) objectRef.element;
        if (this.seated != null) {
            if (this.jumpRequested) {
                leaveSeat(true);
            } else {
                writeCharacter(dt);
                return;
            }
        }
        List<RayHit> listFrontHits = (this.climbing || ((Vector3) objectRef.element).getMagnitude() > 0.1f) ? frontHits() : CollectionsKt.emptyList();
        Vector3 vector3FacingVec = facingVec();
        boolean z2 = (((Vector3) objectRef.element).getMagnitude() <= 0.1f || ((Vector3) objectRef.element).getUnit().dot(vector3FacingVec) <= 0.3f) ? false : true;
        boolean ladderKicked = false;
        if (!this.climbing) {
            f2 = gravity;
            if (z2 && canClimb(listFrontHits, false) && (this.grounded || this.vel.getY() < 5.0f)) {
                this.climbing = true;
                setState("Climbing", 12);
            }
        } else if (this.jumpRequested) {
            this.climbing = false;
            this.grounded = false;
            ladderKicked = true;
            f2 = gravity;
            float kickHoriz = Math.max(fFloatValue2 * 1.15f, 18.0f);
            this.vel = new Vector3((-vector3FacingVec.getX()) * kickHoriz, f4 * 0.85f, (-vector3FacingVec.getZ()) * kickHoriz);
            this.jumpAnimTime = 0.3f;
            if (this.animator != null) {
                this.animator.triggerJump();
                playDefaultCharSound("Jumping", com.goydevv.studrblx.audio.CharacterSounds.JUMP, 0.65f);
            }
            this.jumpRequested = this.jumpHeld;
            setState("Jumping", 3);
            LuaRuntime luaRuntime2 = this.rt;
            LuaBoolean TRUE2 = LuaValue.TRUE;
            Intrinsics.checkNotNullExpressionValue(TRUE2, "TRUE");
            luaRuntime2.fire(instance, "Jumping", TRUE2);
        } else {
            f2 = gravity;
            if (!canClimb(listFrontHits, true)) {
                this.climbing = false;
                if (this.vel.getY() > 0.0f) {
                    ladderKicked = true;
                    this.vel = new Vector3(vector3FacingVec.getX() * 10.0f, Math.max(24.0f, f4 * 0.48f), vector3FacingVec.getZ() * 10.0f);
                }
                setState("Freefall", 5);
            }
        }
        if (!this.climbing) {
            Vector3 vector3Times = ((Vector3) objectRef.element).times(fFloatValue2);
            if (ladderKicked) {
                this.vel = new Vector3(this.vel.getX(), this.vel.getY() - (f2 * dt), this.vel.getZ());
            } else if (this.grounded) {
                this.vel = new Vector3(vector3Times.getX(), this.vel.getY() - (f2 * dt), vector3Times.getZ());
            } else {
                // Roblox Freefall air-strafe & ladder-bounce momentum preservation
                float moveMag = ((Vector3) objectRef.element).getMagnitude();
                float blend = RangesKt.coerceIn(dt * (moveMag > 0.05f ? 10.5f : 1.5f), 0.0f, 1.0f);
                float vx = this.vel.getX() + ((vector3Times.getX() - this.vel.getX()) * blend);
                float vz = this.vel.getZ() + ((vector3Times.getZ() - this.vel.getZ()) * blend);
                this.vel = new Vector3(vx, this.vel.getY() - (f2 * dt), vz);
            }
            if (Intrinsics.areEqual(instance.get("Jump"), (Object) true)) {
                this.jumpRequested = true;
                instance.set("Jump", false, false);
            }
            if (this.jumpRequested && this.grounded) {
                this.vel = new Vector3(this.vel.getX(), f4, this.vel.getZ());
                this.grounded = false;
                this.jumpAnimTime = 0.3f;
                if (this.animator != null) {
                    this.animator.triggerJump();
                playDefaultCharSound("Jumping", com.goydevv.studrblx.audio.CharacterSounds.JUMP, 0.65f);
                }
                this.emote = null;
                setState("Jumping", 3);
                LuaRuntime luaRuntime3 = this.rt;
                LuaBoolean TRUE3 = LuaValue.TRUE;
                Intrinsics.checkNotNullExpressionValue(TRUE3, "TRUE");
                luaRuntime3.fire(instance, "Jumping", TRUE3);
            }
        } else {
            float fDot = 0.7f * fFloatValue2 * (((Vector3) objectRef.element).getMagnitude() > f ? ((Vector3) objectRef.element).dot(vector3FacingVec) : 0.0f);
            this.vel = new Vector3(vector3FacingVec.getX() * 2.0f, fDot, vector3FacingVec.getZ() * 2.0f);
            if (Math.abs(fDot) > 0.1f) {
                LuaRuntime luaRuntime4 = this.rt;
                LuaNumber luaNumberValueOf = LuaValue.valueOf(fDot);
                Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
                z = false;
                luaRuntime4.fire(instance, "Climbing", luaNumberValueOf);
            } else {
                z = false;
            }
            this.grounded = z;
        }
        this.jumpRequested = this.jumpHeld;
        boolean z3 = this.shiftLock || this.firstPerson;
        if (!this.climbing) {
            if (z3) {
                this.facing = this.camYaw;
            } else if (((Vector3) objectRef.element).getMagnitude() > f && !Intrinsics.areEqual(instance.get("AutoRotate"), (Object) false)) {
                this.facing = (float) Math.atan2(-((Vector3) objectRef.element).getX(), -((Vector3) objectRef.element).getZ());
            }
        }
        float magnitude2 = this.vel.getMagnitude() * dt;
        int iCoerceIn = RangesKt.coerceIn((int) Math.ceil(magnitude2 / 0.45f), 1, 14);
        float f5 = dt / iCoerceIn;
        float impactVelocity = this.vel.getY();
        boolean z4 = this.grounded;
        this.grounded = false;
        int i = 0;
        while (i < iCoerceIn) {
            this.pos = this.pos.plus(this.vel.times(f5));
            collideCharacter();
            i++;
        }
        if (this.climbing) {
            this.grounded = false;
        }
        if (!z4 && this.grounded) {
            if (impactVelocity < -5.0f) playDefaultCharSound("Landing", com.goydevv.studrblx.audio.CharacterSounds.LAND, Math.min(0.35f, Math.max(0.12f, -impactVelocity * 0.007f)));
            setState("Landed", 7);
            setState("Running", 8);
        } else if (z4 && !this.grounded && !this.climbing && this.vel.getY() <= 0.0f) {
            setState("Freefall", 5);
            LuaRuntime luaRuntime5 = this.rt;
            LuaBoolean TRUE4 = LuaValue.TRUE;
            Intrinsics.checkNotNullExpressionValue(TRUE4, "TRUE");
            luaRuntime5.fire(instance, "FreeFalling", TRUE4);
        }
        trySit(dt);
        updateRunSound();
        Object obj4 = this.dm.getWorkspace().get("FallenPartsDestroyHeight");
        Number number4 = obj4 instanceof Number ? (Number) obj4 : null;
        if (this.pos.getY() < (number4 != null ? number4.floatValue() : -500.0f)) {
            Instance.set$default(instance, instance.getProps().containsKey("Health") ? "Health" : "Health_XML", Float.valueOf(0.0f), false, 4, null);
        }
        writeCharacter(dt);
    }

    private final float respawnTime() {
        Instance instanceFindService = this.dm.findService("Players");
        Object obj = instanceFindService != null ? instanceFindService.get("RespawnTime") : null;
        Number number = obj instanceof Number ? (Number) obj : null;
        if (number != null) {
            return number.floatValue();
        }
        return 5.0f;
    }

    private final Pair<Vector3, Vector3> charBox() {
        Vector3 c = new Vector3(this.pos.getX(), this.pos.getY() + ((this.charMinY + this.charMaxY) / 2.0f), this.pos.getZ());
        Vector3 half = new Vector3(this.charHalfXZ, (this.charMaxY - this.charMinY) / 2.0f, this.charHalfXZ);
        return TuplesKt.to(c, half);
    }

    private final void collideCharacter() {
        int i;
        Pair<Vector3, Float> pairContact;
        boolean z = false;
        int i2 = 0;
        while (i2 < 4) {
            Pair<Vector3, Vector3> pairCharBox = charBox();
            Vector3 vector3Component1 = pairCharBox.component1();
            Vector3 vector3Component2 = pairCharBox.component2();
            ConvexShape convexShapeAabbShape = Geometry.INSTANCE.aabbShape(vector3Component1, vector3Component2);
            boolean z2 = false;
            Iterator<Instance> it = query(vector3Component1.minus(vector3Component2), vector3Component1.plus(vector3Component2), true).iterator();
            while (true) {
                if (!it.hasNext()) {
                    i = i2;
                    break;
                }
                Instance next = it.next();
                if (Intrinsics.areEqual(next.get("CanCollide"), Boolean.valueOf(z))) {
                    z = false;
                } else if (!isCharacterPart(next) && (pairContact = Geometry.INSTANCE.contact(convexShapeAabbShape, vector3Component1.minus(vector3Component2), vector3Component1.plus(vector3Component2), next)) != null) {
                    Vector3 vector3Component3 = pairContact.component1();
                    float fFloatValue = pairContact.component2().floatValue();
                    float magnitude = vector3Component3.getMagnitude();
                    if (magnitude >= 1.0E-4f) {
                        Vector3 vector3Div = vector3Component3.div(magnitude);
                        float maxSlopeDeg = 89.0f;
                        if (this.humanoid != null && (this.humanoid.get("MaxSlopeAngle") instanceof Number)) {
                            maxSlopeDeg = RangesKt.coerceIn(((Number) this.humanoid.get("MaxSlopeAngle")).floatValue(), 0.0f, 89.0f);
                        }
                        float minSlopeNy = RangesKt.coerceIn((float) Math.cos(Math.toRadians((double) maxSlopeDeg)), 0.05f, 0.99f);
                        if (vector3Div.getY() > Math.max(minSlopeNy, 0.35f)) {
                            i = i2;
                            this.pos = new Vector3(this.pos.getX(), this.pos.getY() + (magnitude / vector3Div.getY()), this.pos.getZ());
                            if (this.vel.getY() < 0.0f) {
                                this.vel = new Vector3(this.vel.getX(), 0.0f, this.vel.getZ());
                            }
                            this.grounded = true;
                            this.groundNormal = vector3Div;
                            if (this.humanoid != null) {
                                Instance.set$default(this.humanoid, "FloorMaterial", new EnumValue(PartData.INSTANCE.material(next)), false, 4, null);
                            }
                        } else {
                            i = i2;
                            if (vector3Div.getY() < -0.55f) {
                                this.pos = this.pos.plus(vector3Component3);
                                if (this.vel.getY() > 0.0f) {
                                    this.vel = new Vector3(this.vel.getX(), 0.0f, this.vel.getZ());
                                }
                            } else {
                                float y = fFloatValue - (vector3Component1.getY() - vector3Component2.getY());
                                if (y <= 0.0f || y > 2.1f || this.vel.getY() > 1.0f || this.climbing) {
                                    this.pos = this.pos.plus(new Vector3(vector3Component3.getX(), 0.0f, vector3Component3.getZ()));
                                    float hLen = (float) Math.hypot(vector3Div.getX(), vector3Div.getZ());
                                    if (hLen > 0.01f) {
                                        float nx = vector3Div.getX() / hLen;
                                        float nz = vector3Div.getZ() / hLen;
                                        float vn = (this.vel.getX() * nx) + (this.vel.getZ() * nz);
                                        if (vn < 0.0f) {
                                            this.vel = new Vector3(this.vel.getX() - (nx * vn), this.vel.getY(), this.vel.getZ() - (nz * vn));
                                        }
                                        this.wallBumpNormal = new Vector3(nx, 0.0f, nz);
                                        this.wallBumpDecay = 1.0f;
                                    }
                                    if (this.grounded && y > 2.1f && y <= 4.8f && !this.climbing && this.humanoid != null && !Intrinsics.areEqual(this.humanoid.get("AutoJumpEnabled"), Boolean.FALSE)) {
                                        this.jumpRequested = true;
                                    }
                                } else {
                                    Vector3 stepC = new Vector3(vector3Component1.getX(), vector3Component1.getY() + y + 0.02f, vector3Component1.getZ());
                                    boolean blockedAbove = false;
                                    ConvexShape stepShape = Geometry.INSTANCE.aabbShape(stepC, vector3Component2);
                                    for (Instance ov : query(stepC.minus(vector3Component2), stepC.plus(vector3Component2), true)) {
                                        if (ov != next && !isCharacterPart(ov) && !Intrinsics.areEqual(ov.get("CanCollide"), Boolean.FALSE)) {
                                            Pair<Vector3, Float> oc = Geometry.INSTANCE.contact(stepShape, stepC.minus(vector3Component2), stepC.plus(vector3Component2), ov);
                                            if (oc != null && oc.getFirst().getMagnitude() > 0.08f) {
                                                blockedAbove = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!blockedAbove) {
                                        this.pos = new Vector3(this.pos.getX(), this.pos.getY() + y + 0.01f, this.pos.getZ());
                                    } else {
                                        this.pos = this.pos.plus(new Vector3(vector3Component3.getX(), 0.0f, vector3Component3.getZ()));
                                    }
                                }
                            }
                        }
                        z2 = true;
                        break;
                    }
                    z = false;
                }
            }
            if (!z2) {
                return;
            }
            i2 = i + 1;
            z = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isCharacterPart(Instance p) {
        Instance ch = this.character;
        if (ch == null) {
            return false;
        }
        return p.isDescendantOf(ch);
    }

    public final boolean hiddenInFirstPerson(Instance p) {
        Instance ch;
        Intrinsics.checkNotNullParameter(p, "p");
        if (!this.firstPerson || (ch = this.character) == null || !p.isDescendantOf(ch)) {
            return false;
        }
        Instance t = this.equipped;
        return t == null || !p.isDescendantOf(t);
    }

    private final long cellKey(int x, int y, int z) {
        return ((((long) x) & 2097151) << 42) | ((((long) y) & 2097151) << 21) | (((long) z) & 2097151);
    }

    private final int[] cellRange(Vector3 a, Vector3 b) {
        return new int[]{(int) Math.floor(a.getX() / this.CELL), (int) Math.floor(a.getY() / this.CELL), (int) Math.floor(a.getZ() / this.CELL), (int) Math.floor(b.getX() / this.CELL), (int) Math.floor(b.getY() / this.CELL), (int) Math.floor(b.getZ() / this.CELL)};
    }

    private final void insertGrid(Instance p) {
        Vector3 b;
        char c;
        char c2;
        ArrayList<Instance> arrayList;
        int i;
        Pair<Vector3, Vector3> pairPartAabb = Geometry.INSTANCE.partAabb(p);
        Vector3 a = pairPartAabb.component1();
        Vector3 b2 = pairPartAabb.component2();
        int[] r = cellRange(a, b2);
        char c3 = 1;
        char c4 = 4;
        char c5 = 5;
        char c6 = 2;
        long count = ((r[3] - r[0]) + 1) * ((long) ((r[4] - r[1]) + 1)) * ((long) ((r[5] - r[2]) + 1));
        if (count > 512) {
            this.bigParts.add(p);
            this.cellsOf.put(p, new long[0]);
            return;
        }
        long[] keys = new long[(int) count];
        int i2 = 0;
        int x = r[0];
        int i3 = r[3];
        if (x <= i3) {
            while (true) {
                int y = r[c3];
                int i4 = r[c4];
                if (y <= i4) {
                    while (true) {
                        c2 = c5;
                        int z = r[c6];
                        int i5 = r[c2];
                        if (z <= i5) {
                            while (true) {
                                long k = cellKey(x, y, z);
                                Map map = this.grid;
                                b = b2;
                                Long lValueOf = Long.valueOf(k);
                                ArrayList<Instance> arrayList2 = (ArrayList<Instance>) map.get(lValueOf);
                                if (arrayList2 == null) {
                                    c = 4;
                                    arrayList = new ArrayList<>(4);
                                    map.put(lValueOf, arrayList);
                                } else {
                                    c = 4;
                                    arrayList = arrayList2;
                                }
                                arrayList.add(p);
                                i = i2 + 1;
                                keys[i2] = k;
                                if (z == i5) {
                                    break;
                                }
                                z++;
                                i2 = i;
                                b2 = b;
                            }
                            i2 = i;
                        } else {
                            b = b2;
                            c = c4;
                        }
                        if (y == i4) {
                            break;
                        }
                        y++;
                        c4 = c;
                        c5 = c2;
                        b2 = b;
                        c6 = 2;
                    }
                } else {
                    b = b2;
                    c = c4;
                    c2 = c5;
                }
                if (x == i3) {
                    break;
                }
                x++;
                c4 = c;
                c5 = c2;
                b2 = b;
                c3 = 1;
                c6 = 2;
            }
        }
        this.cellsOf.put(p, keys);
    }

    private final void removeGrid(Instance p) {
        long[] keys = this.cellsOf.remove(p);
        if (keys == null) {
            return;
        }
        if (keys.length == 0) {
            this.bigParts.remove(p);
        }
        for (long k : keys) {
            ArrayList<Instance> arrayList = this.grid.get(Long.valueOf(k));
            if (arrayList != null) {
                arrayList.remove(p);
            }
        }
    }

    private final void updateGrid(Instance p) {
        removeGrid(p);
        insertGrid(p);
    }

    static /* synthetic */ List query$default(PlaySession playSession, Vector3 vector3, Vector3 vector4, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return playSession.query(vector3, vector4, z);
    }

    private final List<Instance> query(Vector3 a, Vector3 b, boolean withBodies) {
        int x;
        int i;
        int[] r = cellRange(a, b);
        ArrayList out = new ArrayList();
        Set seen = Collections.newSetFromMap(new IdentityHashMap());
        Iterator<Instance> it = this.bigParts.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance p = it.next();
            if (seen.add(p)) {
                out.add(p);
            }
        }
        char c = 1;
        char c2 = 4;
        char c3 = 5;
        if (((long) ((r[3] - r[0]) + 1)) * ((long) ((r[4] - r[1]) + 1)) * ((long) ((r[5] - r[2]) + 1)) <= 4096 && (x = r[0]) <= (i = r[3])) {
            while (true) {
                int y = r[c];
                int i2 = r[c2];
                if (y <= i2) {
                    while (true) {
                        int z = r[2];
                        int i3 = r[c3];
                        if (z <= i3) {
                            while (true) {
                                ArrayList<Instance> arrayList = this.grid.get(Long.valueOf(cellKey(x, y, z)));
                                if (arrayList != null) {
                                    Iterator<Instance> it2 = arrayList.iterator();
                                    Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
                                    while (it2.hasNext()) {
                                        Instance next = it2.next();
                                        if (seen.add(next)) {
                                            out.add(next);
                                        }
                                    }
                                }
                                if (z == i3) {
                                    break;
                                }
                                z++;
                            }
                        }
                        if (y == i2) {
                            break;
                        }
                        y++;
                        c3 = 5;
                    }
                }
                if (x == i) {
                    break;
                }
                x++;
                c2 = 4;
                c = 1;
                c3 = 5;
            }
        }
        if (withBodies) {
            Iterator<Body> it3 = this.bodies.iterator();
            Intrinsics.checkNotNullExpressionValue(it3, "iterator(...)");
            while (it3.hasNext()) {
                Body next2 = it3.next();
                Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
                Body bd = next2;
                if (!bd.getHasBox() || Geometry.INSTANCE.aabbOverlap(a, b, bd.getMin(), bd.getMax(), 0.5f)) {
                    for (Instance p2 : bd.getParts()) {
                        if (seen.add(p2)) {
                            out.add(p2);
                        }
                    }
                }
            }
        }
        Collection arrayList2 = new ArrayList();
        for (Object obj : out) {
            Pair<Vector3, Vector3> pairPartAabb = Geometry.INSTANCE.partAabb((Instance) obj);
            if (Geometry.INSTANCE.aabbOverlap(a, b, pairPartAabb.component1(), pairPartAabb.component2(), 0.05f)) {
                arrayList2.add(obj);
            }
        }
        return (List) arrayList2;
    }

    private final void rebuildPhysics() {
        ArrayList parts;
        boolean z;
        boolean z2;
        boolean z3;
        Object arrayList;
        Instance a = null;
        Instance b = null;
        List<Instance> list;
        Iterator it;
        this.physicsDirty = false;
        IdentityHashMap oldVel = new IdentityHashMap();
        Set oldSleep = Collections.newSetFromMap(new IdentityHashMap());
        Iterator<Body> it2 = this.bodies.iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        while (it2.hasNext()) {
            Body next = it2.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Body b2 = next;
            for (Instance p : b2.getParts()) {
                oldVel.put(p, b2.getVel());
                if (b2.getSleeping()) {
                    oldSleep.add(p);
                }
            }
        }
        this.bodies.clear();
        this.bodyOf.clear();
        this.statics.clear();
        this.grid.clear();
        this.cellsOf.clear();
        this.bigParts.clear();
        Map groups = new LinkedHashMap();
        Instance ws = this.dm.getWorkspace();
        List<Instance> listRenderParts = this.studio.renderParts();
        boolean z4 = true;
        List<Instance> wsAll = Instance.descendants$default(ws, null, 1, null);
        Collection arrayList2 = new ArrayList();
        for (Object obj : wsAll) {
            boolean z5 = z4;
            if (Intrinsics.areEqual(((Instance) obj).getClassName(), "ProximityPrompt")) {
                arrayList2.add(obj);
            }
            z4 = z5;
        }
        boolean z6 = z4;
        this.prompts = new ArrayList<>((List) arrayList2);
        IdentityHashMap parent = new IdentityHashMap();
        Set<Instance> simulated = Collections.newSetFromMap(new IdentityHashMap());
        for (Instance p2 : listRenderParts) {
            if (!isCharacterPart(p2) && !Intrinsics.areEqual(p2.getClassName(), "Terrain")) {
                if (this.equipped != null) {
                    Instance instance = this.equipped;
                    Intrinsics.checkNotNull(instance);
                    if (p2.isDescendantOf(instance)) {
                    }
                }
                simulated.add(p2);
            }
        }
        Iterator it3 = wsAll.iterator();
        while (it3.hasNext()) {
            Instance j = (Instance) it3.next();
            if (j.isA("JointInstance")) {
                Object obj2 = j.get("Part0");
                a = obj2 instanceof Instance ? (Instance) obj2 : null;
                Object obj3 = j.get("Part1");
                b = obj3 instanceof Instance ? (Instance) obj3 : null;
                list = listRenderParts;
                it = it3;
            } else {
                list = listRenderParts;
                it = it3;
                if (Intrinsics.areEqual(j.getClassName(), "WeldConstraint") && !Intrinsics.areEqual(j.get("Enabled"), (Object) false)) {
                    Object obj4 = j.get("Part0Internal");
                    if (obj4 == null) {
                        obj4 = j.get("Part0");
                    }
                    a = obj4 instanceof Instance ? (Instance) obj4 : null;
                    Object obj5 = j.get("Part1Internal");
                    if (obj5 == null) {
                        obj5 = j.get("Part1");
                    }
                    b = obj5 instanceof Instance ? (Instance) obj5 : null;
                } else if (!this.HOLD_TOGETHER.contains(j.getClassName()) || Intrinsics.areEqual(j.get("Enabled"), (Object) false)) {
                    listRenderParts = list;
                    it3 = it;
                } else {
                    Object obj6 = j.get("Attachment0");
                    a = attPart(obj6 instanceof Instance ? (Instance) obj6 : null);
                    Object obj7 = j.get("Attachment1");
                    b = attPart(obj7 instanceof Instance ? (Instance) obj7 : null);
                    if (Intrinsics.areEqual(j.getClassName(), "SpringConstraint")) {
                        if (!(a != null ? Intrinsics.areEqual(a.get("Anchored"), Boolean.valueOf(z6)) : false)) {
                            if (b != null ? Intrinsics.areEqual(b.get("Anchored"), Boolean.valueOf(z6)) : false) {
                            }
                        }
                        listRenderParts = list;
                        it3 = it;
                    }
                }
            }
            if (a == null || b == null || !simulated.contains(a) || !simulated.contains(b)) {
                listRenderParts = list;
                it3 = it;
            } else {
                rebuildPhysics$union(parent, a, b);
                listRenderParts = list;
                it3 = it;
            }
        }
        Map groups2 = groups;
        for (Instance p3 : simulated) {
            Map map = groups2;
            Intrinsics.checkNotNull(p3);
            Instance instanceRebuildPhysics$find = rebuildPhysics$find(parent, p3);
            Object obj8 = map.get(instanceRebuildPhysics$find);
            if (obj8 == null) {
                arrayList = new ArrayList();
                map.put(instanceRebuildPhysics$find, arrayList);
            } else {
                arrayList = (ArrayList) obj8;
            }
            ((ArrayList) arrayList).add(p3);
        }
        Iterator it4 = groups2.entrySet().iterator();
        while (it4.hasNext()) {
            ArrayList parts2 = (ArrayList) ((Map.Entry) it4.next()).getValue();
            ArrayList arrayList3 = parts2;
            if (!(arrayList3 instanceof Collection) || !arrayList3.isEmpty()) {
                Iterator it5 = arrayList3.iterator();
                while (true) {
                    if (it5.hasNext()) {
                        parts = parts2;
                        if (Intrinsics.areEqual(((Instance) it5.next()).get("Anchored"), Boolean.valueOf(z6))) {
                            z = z6;
                            break;
                        } else {
                            parts2 = parts;
                        }
                    } else {
                        parts = parts2;
                        z = false;
                        break;
                    }
                }
            } else {
                parts = parts2;
                z = false;
            }
            if (z) {
                this.statics.addAll(parts);
            } else {
                ArrayList arrayList4 = parts;
                Vector3 zero = (Vector3) oldVel.get(CollectionsKt.first((List) parts));
                if (zero == null) {
                    zero = Vector3.INSTANCE.getZERO();
                }
                Body b3 = new Body(arrayList4, zero);
                measure(b3);
                Iterable iterable = parts;
                if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                    Iterator it6 = iterable.iterator();
                    while (true) {
                        if (it6.hasNext()) {
                            if (!oldSleep.contains((Instance) it6.next())) {
                                z2 = false;
                                break;
                            }
                        } else {
                            z2 = z6;
                            break;
                        }
                    }
                } else {
                    z2 = z6;
                }
                if (z2) {
                    z3 = z6;
                    b3.setSleeping(z3);
                } else {
                    z3 = z6;
                }
                this.bodies.add(b3);
                Iterator it7 = parts.iterator();
                Intrinsics.checkNotNullExpressionValue(it7, "iterator(...)");
                while (it7.hasNext()) {
                    this.bodyOf.put((Instance) it7.next(), b3);
                }
                z6 = z3;
            }
        }
        for (Instance p4 : this.statics) {
            Intrinsics.checkNotNull(p4);
            insertGrid(p4);
        }
        this.movers.clear();
        for (Instance d : wsAll) {
            if (this.MOVER_CLASSES.contains(d.getClassName()) && !Intrinsics.areEqual(d.get("Enabled"), (Object) false)) {
                this.movers.add(d);
            }
        }
    }

    private static final Instance rebuildPhysics$find(IdentityHashMap<Instance, Instance> identityHashMap, Instance x) {
        Instance r = x;
        while (true) {
            Instance p = (Instance) identityHashMap.get(r);
            if (p == null || p == r) {
                return r;
            }
            r = p;
        }
    }

    private static final void rebuildPhysics$union(IdentityHashMap<Instance, Instance> identityHashMap, Instance a, Instance b) {
        Instance ra = rebuildPhysics$find(identityHashMap, a);
        Instance rb = rebuildPhysics$find(identityHashMap, b);
        if (ra != rb) {
            identityHashMap.put(ra, rb);
        }
    }

    private final float[] physProps(int mat) {
        switch (mat) {
            case 256:
            case 288:
                return new float[]{0.7f, 0.3f, 0.5f};
            case ClothingCompositor.TORSO_H /* 272 */:
                return new float[]{0.7f, 0.2f, 0.5f};
            case 512:
            case 528:
                return new float[]{0.35f, 0.48f, 0.2f};
            case 784:
                return new float[]{2.56f, 0.2f, 0.17f};
            case 788:
            case 800:
            case 804:
            case 820:
            case 832:
            case 836:
            case 880:
            case 896:
            case 912:
                return new float[]{2.69f, 0.4f, 0.2f};
            case 816:
                return new float[]{2.4f, 0.7f, 0.2f};
            case 848:
                return new float[]{1.92f, 0.8f, 0.15f};
            case 864:
                return new float[]{2.4f, 0.4f, 0.17f};
            case 1040:
            case 1088:
                return new float[]{7.85f, 0.4f, 0.25f};
            case 1056:
                return new float[]{7.85f, 0.35f, 0.25f};
            case 1072:
                return new float[]{2.7f, 0.4f, 0.25f};
            case 1280:
            case 1284:
            case 1344:
            case 1360:
                return new float[]{0.9f, 0.4f, 0.1f};
            case 1296:
                return new float[]{1.6f, 0.5f, 0.05f};
            case 1312:
                return new float[]{0.7f, 0.35f, 0.05f};
            case 1328:
                return new float[]{0.9f, 0.3f, 0.03f};
            case 1376:
                return new float[]{2.36f, 0.8f, 0.2f};
            case 1392:
                return new float[]{2.16f, 0.5f, 0.05f};
            case 1536:
            case 1552:
                return new float[]{0.919f, 0.02f, 0.15f};
            case 1568:
                return new float[]{2.4f, 0.25f, 0.2f};
            case 2311:
                return new float[]{1.3f, 1.5f, 0.95f};
            default:
                return new float[]{0.7f, 0.3f, 0.5f};
        }
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0065  */
    /* JADX WARN: Code duplicated, block: B:24:0x007d  */
    /* JADX WARN: Code duplicated, block: B:31:0x0093  */
    private final void measure(Body b) {
        float dens;
        float fr;
        float el;
        float mass = 0.0f;
        float f = 0.0f;
        float e = 0.0f;
        float w = 0.0f;
        for (Instance p : b.getParts()) {
            Vector3 s = PartData.INSTANCE.size(p);
            float vol = RangesKt.coerceAtLeast(s.getX() * s.getY() * s.getZ(), 0.001f);
            float[] d = physProps(PartData.INSTANCE.material(p));
            Object obj = p.get("CustomPhysicalProperties");
            PhysicalProps cp = obj instanceof PhysicalProps ? (PhysicalProps) obj : null;
            if (cp == null) {
                dens = d[0];
            } else {
                PhysicalProps physicalProps = cp.getCustom() ? cp : null;
                if (physicalProps != null) {
                    dens = physicalProps.getDensity();
                } else {
                    dens = d[0];
                }
            }
            if (cp == null) {
                fr = d[1];
            } else {
                PhysicalProps physicalProps2 = cp.getCustom() ? cp : null;
                if (physicalProps2 != null) {
                    fr = physicalProps2.getFriction();
                } else {
                    fr = d[1];
                }
            }
            if (cp == null) {
                el = d[2];
            } else {
                PhysicalProps physicalProps3 = cp.getCustom() ? cp : null;
                if (physicalProps3 != null) {
                    el = physicalProps3.getElasticity();
                } else {
                    el = d[2];
                }
            }
            float f2 = f;
            if (!Intrinsics.areEqual(p.get("Massless"), (Object) true)) {
                mass += vol * dens;
            }
            f = f2 + (fr * vol);
            e += el * vol;
            w += vol;
        }
        float f3 = f;
        b.setMass(RangesKt.coerceAtLeast(mass, 0.01f));
        if (w > 0.0f) {
            b.setFriction(f3 / w);
            b.setElasticity(e / w);
        }
        Vector3 lo = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3 hi = lo.unaryMinus();
        Iterator<Instance> it = b.getParts().iterator();
        while (it.hasNext()) {
            Pair<Vector3, Vector3> pairPartAabb = Geometry.INSTANCE.partAabb(it.next());
            Vector3 a0 = pairPartAabb.component1();
            Vector3 a1 = pairPartAabb.component2();
            lo = new Vector3(Math.min(lo.getX(), a0.getX()), Math.min(lo.getY(), a0.getY()), Math.min(lo.getZ(), a0.getZ()));
            hi = new Vector3(Math.max(hi.getX(), a1.getX()), Math.max(hi.getY(), a1.getY()), Math.max(hi.getZ(), a1.getZ()));
        }
        if (lo.getX() <= hi.getX()) {
            b.setMin(lo);
            b.setMax(hi);
            b.setHasBox(true);
        }
    }

    private final Instance attPart(Instance a) {
        Instance parent;
        if (a == null || (parent = a.getParent()) == null || !parent.isA("BasePart")) {
            return null;
        }
        return parent;
    }

    private final Vector3 attWorld(Instance a) {
        Vector3 zero;
        Instance part = attPart(a);
        if (part == null) {
            return null;
        }
        CFrame cframe = PartData.INSTANCE.cframe(part);
        Intrinsics.checkNotNull(a);
        Object obj = a.get("CFrame");
        CFrame cFrame = obj instanceof CFrame ? (CFrame) obj : null;
        if (cFrame == null || (zero = cFrame.getPosition()) == null) {
            zero = Vector3.INSTANCE.getZERO();
        }
        return cframe.times(zero);
    }

    private final CFrame attAxis(Instance a) {
        Instance part = attPart(a);
        if (part == null) {
            return null;
        }
        CFrame cframe = PartData.INSTANCE.cframe(part);
        Intrinsics.checkNotNull(a);
        Object obj = a.get("CFrame");
        CFrame identity = obj instanceof CFrame ? (CFrame) obj : null;
        if (identity == null) {
            identity = CFrame.INSTANCE.getIDENTITY();
        }
        return cframe.times(identity);
    }

    /* JADX INFO: compiled from: PlaySession.kt */
    private final class End {
        private final Body body;
        private final boolean isChar;
        private final Instance part;

        public End(Instance part) {
            this.part = part;
            Instance instance = this.part;
            this.body = instance != null ? (Body) PlaySession.this.bodyOf.get(instance) : null;
            this.isChar = this.body == null && this.part != null && PlaySession.this.isCharacterPart(this.part);
        }

        public final Instance getPart() {
            return this.part;
        }

        public final Body getBody() {
            return this.body;
        }

        /* JADX INFO: renamed from: isChar, reason: from getter */
        public final boolean getIsChar() {
            return this.isChar;
        }

        public final float getInvMass() {
            if (this.body != null) {
                return 1.0f / this.body.getMass();
            }
            return this.isChar ? 0.06666667f : 0.0f;
        }

        public final Vector3 getV() {
            Vector3 vel;
            Body body = this.body;
            if (body == null || (vel = body.getVel()) == null) {
                return this.isChar ? PlaySession.this.vel : Vector3.INSTANCE.getZERO();
            }
            return vel;
        }

        public final void setV(Vector3 x) {
            Intrinsics.checkNotNullParameter(x, "x");
            if (this.body != null) {
                this.body.setVel(x);
                this.body.setSleeping(false);
                this.body.setRestTime(0.0f);
            } else if (this.isChar) {
                PlaySession.this.vel = x;
            }
        }

        public final void move(Vector3 d) {
            Intrinsics.checkNotNullParameter(d, "d");
            if (this.body == null) {
                if (this.isChar) {
                    PlaySession.this.pos = PlaySession.this.pos.plus(d);
                    return;
                }
                return;
            }
            for (Instance p : this.body.getParts()) {
                p.set("CFrame", PartData.INSTANCE.cframe(p).plus(d), false);
            }
            this.body.setSleeping(false);
            this.body.setRestTime(0.0f);
        }
    }

    private final float num(Instance i, String n, float d) {
        Object obj = i.get(n);
        Number number = obj instanceof Number ? (Number) obj : null;
        return number != null ? number.floatValue() : d;
    }

    private final Vector3 vec(Instance i, String n) {
        Object obj = i.get(n);
        Vector3 vector3 = obj instanceof Vector3 ? (Vector3) obj : null;
        return vector3 == null ? Vector3.INSTANCE.getZERO() : vector3;
    }

    /* JADX WARN: Code duplicated, block: B:165:0x03cb  */
    /* JADX WARN: Code duplicated, block: B:205:0x0473  */
    /* JADX WARN: Code duplicated, block: B:207:0x047b  */
    /* JADX WARN: Code duplicated, block: B:209:0x0481  */
    /* JADX WARN: Code duplicated, block: B:210:0x048a  */
    /* JADX WARN: Code duplicated, block: B:227:0x0509  */
    /* JADX WARN: Code duplicated, block: B:230:0x0512  */
    /* JADX WARN: Code duplicated, block: B:233:0x0523  */
    /* JADX WARN: Code duplicated, block: B:249:0x05ba  */
    /* JADX WARN: Code duplicated, block: B:250:0x05bd  */
    /* JADX WARN: Code duplicated, block: B:252:0x05c1  */
    /* JADX WARN: Code duplicated, block: B:254:0x05c9  */
    /* JADX WARN: Code duplicated, block: B:256:0x05cd  */
    /* JADX WARN: Code duplicated, block: B:259:0x05dd  */
    /* JADX WARN: Code duplicated, block: B:262:0x0603  */
    /* JADX WARN: Code duplicated, block: B:263:0x0614  */
    /* JADX WARN: Code duplicated, block: B:269:0x0650  */
    /* JADX WARN: Code duplicated, block: B:270:0x0666  */
    /* JADX WARN: Code duplicated, block: B:304:0x0723  */
    /* JADX WARN: Code duplicated, block: B:351:0x06b9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:352:0x053d A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:353:0x0546 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:354:0x063f A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:355:0x0680 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:356:0x0687 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:357:0x0646 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:358:0x068c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:359:0x053a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:360:0x05b0 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:361:0x0687 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:362:0x05a9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:363:0x054c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:364:0x0543 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:365:0x0551 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:366:0x054c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:367:0x0687 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:368:0x0527 A[SYNTHETIC] */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v16 */
    /* JADX WARN: Type inference failed for: r11v18, types: [com.goydevv.studrblx.model.Vector3] */
    /* JADX WARN: Type inference failed for: r11v44 */
    /* JADX WARN: Type inference failed for: r11v45 */
    /* JADX WARN: Type inference failed for: r1v85, types: [com.goydevv.studrblx.model.Instance] */
    /* JADX WARN: Type inference failed for: r25v0, types: [com.goydevv.studrblx.engine.PlaySession] */
    /* JADX WARN: Type inference failed for: r3v25, types: [com.goydevv.studrblx.model.Instance] */
    /* JADX WARN: Type inference failed for: r3v36, types: [com.goydevv.studrblx.model.PartData] */
    /* JADX WARN: Type inference failed for: r3v5, types: [com.goydevv.studrblx.model.Instance] */
    /* JADX WARN: Type inference failed for: r5v19, types: [com.goydevv.studrblx.model.Instance] */
    /* JADX WARN: Type inference failed for: r6v11 */
    /* JADX WARN: Type inference failed for: r6v6 */
    /* JADX WARN: Type inference failed for: r6v7, types: [com.goydevv.studrblx.model.Instance] */
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
    private final void applyMovers(float dt) {
        Instance r6;
        Vector3 vector3VectorToWorld;
        Object obj = null;
        Instance instance;
        CFrame cFrameAttAxis;
        float f;
        Vector3 vector3Times;
        float f2;
        Vector3 vector3;
        Vector3 vector3Vec;
        Instance parent;
        End end;
        float invMass;
        String className;
        Object obj2;
        Instance instance2;
        Vector3 vector3Vec2;
        Vector3 vector3Minus;
        Vector3 zero;
        Vector3 vector3Vec3;
        if (this.movers.isEmpty()) {
            return;
        }
        float f3 = 0.0f;
        Vector3 vector4 = new Vector3(0.0f, (-getGravity()) * dt, 0.0f);
        Iterator<Instance> it = this.movers.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance next = it.next();
            if (next.getParent() != null && !Intrinsics.areEqual(next.get("Enabled"), (Object) false)) {
                String className2 = next.getClassName();
                float f4 = f3;
                Iterator<Instance> it2 = it;
                switch (className2.hashCode()) {
                    case -2068335800:
                        if (!className2.equals("VectorForce")) {
                            it = it2;
                            f3 = 0.0f;
                        } else {
                            Object obj3 = next.get("Attachment0");
                            Instance instance3 = obj3 instanceof Instance ? (Instance) obj3 : null;
                            End end2 = new End(attPart(instance3));
                            if (!(end2.getInvMass() == 0.0f)) {
                                Object obj4 = next.get("RelativeTo");
                                EnumValue enumValue = obj4 instanceof EnumValue ? (EnumValue) obj4 : null;
                                int value = enumValue != null ? enumValue.getValue() : 0;
                                Vector3 vector3Vec4 = vec(next, "Force");
                                if (value != 2) {
                                    if (value == 1) {
                                        Object obj5 = next.get("Attachment1");
                                        obj = obj5 instanceof Instance ? (Instance) obj5 : null;
                                        if (obj != null) {
                                            r6 = (Instance) obj;
                                        } else {
                                            r6 = instance3;
                                        }
                                    } else {
                                        r6 = instance3;
                                    }
                                    CFrame cFrameAttAxis2 = attAxis(r6);
                                    if (cFrameAttAxis2 != null && (vector3VectorToWorld = cFrameAttAxis2.vectorToWorld(vector3Vec4)) != null) {
                                        vector3Vec4 = vector3VectorToWorld;
                                    }
                                }
                                end2.setV(end2.getV().plus(vector3Vec4.times(end2.getInvMass() * dt)));
                                f3 = 0.0f;
                                it = it2;
                            } else {
                                f3 = 0.0f;
                                it = it2;
                            }
                        }
                        break;
                    case -1894814529:
                        if (className2.equals("BodyVelocity")) {
                            parent = next.getParent();
                            if (parent == null || !parent.isA("BasePart")) {
                                parent = null;
                            }
                            end = new End(parent);
                            if ((end.getInvMass() == 0.0f ? 1 : 0) == 0) {
                                invMass = 1.0f / end.getInvMass();
                                className = next.getClassName();
                                switch (className.hashCode()) {
                                    case -1894814529:
                                        if (!className.equals("BodyVelocity")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Velocity"), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case -1061210551:
                                        if (!className.equals("BodyForce")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData = PartData.INSTANCE;
                                                Instance part = end.getPart();
                                                Intrinsics.checkNotNull(part);
                                                vector3Vec3 = partData.cframe(part).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 464782595:
                                        if (!className.equals("RocketPropulsion")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            obj2 = next.get("Target");
                                            if (obj2 instanceof Instance) {
                                                instance2 = (Instance) obj2;
                                            } else {
                                                instance2 = null;
                                            }
                                            if (instance2 == null) {
                                                vector3Vec2 = vec(next, "TargetOffset");
                                            } else {
                                                obj = instance2.isA("BasePart") ? instance2 : null;
                                                if (obj != null) {
                                                    vector3Vec2 = PartData.INSTANCE.cframe((Instance) obj).getPosition();
                                                    if (vector3Vec2 == null) {
                                                        vector3Vec2 = vec(next, "TargetOffset");
                                                    }
                                                } else {
                                                    vector3Vec2 = vec(next, "TargetOffset");
                                                }
                                            }
                                            Vector3 vector5 = vector3Vec2;
                                            PartData partData2 = PartData.INSTANCE;
                                            Instance part2 = end.getPart();
                                            Intrinsics.checkNotNull(part2);
                                            vector3Minus = vector5.minus(partData2.cframe(part2).getPosition());
                                            if (vector3Minus.getMagnitude() > 0.5f) {
                                                zero = vector3Minus.getUnit().times(num(next, "MaxSpeed", 30.0f));
                                            } else {
                                                zero = Vector3.INSTANCE.getZERO();
                                            }
                                            float fNum = num(next, "MaxThrust", 4000.0f);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), zero, new Vector3(fNum, fNum, fNum), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1013696779:
                                        if (!className.equals("BodyPosition")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            PartData partData3 = PartData.INSTANCE;
                                            Instance part3 = end.getPart();
                                            Intrinsics.checkNotNull(part3);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Position").minus(partData3.cframe(part3).getPosition()).times(num(next, "P", 10000.0f) / Math.max(num(next, "D", 1250.0f), 1.0f)), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1856572602:
                                        if (!className.equals("BodyThrust")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData4 = PartData.INSTANCE;
                                                Instance part4 = end.getPart();
                                                Intrinsics.checkNotNull(part4);
                                                vector3Vec3 = partData4.cframe(part4).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    default:
                                        it = it2;
                                        f3 = 0.0f;
                                        break;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                                break;
                            }
                        } else {
                            it = it2;
                            f3 = 0.0f;
                            break;
                        }
                        break;
                    case -1539376451:
                        if (className2.equals("BodyAngularVelocity")) {
                            parent = next.getParent();
                            if (parent != null) {
                                parent = null;
                            } else {
                                parent = null;
                            }
                            end = new End(parent);
                            if (end.getInvMass() == 0.0f) {
                            }
                            if ((end.getInvMass() == 0.0f ? 1 : 0) == 0) {
                                invMass = 1.0f / end.getInvMass();
                                className = next.getClassName();
                                switch (className.hashCode()) {
                                    case -1894814529:
                                        if (!className.equals("BodyVelocity")) {
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Velocity"), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case -1061210551:
                                        if (!className.equals("BodyForce")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData5 = PartData.INSTANCE;
                                                Instance part5 = end.getPart();
                                                Intrinsics.checkNotNull(part5);
                                                vector3Vec3 = partData5.cframe(part5).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 464782595:
                                        if (!className.equals("RocketPropulsion")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            obj2 = next.get("Target");
                                            if (obj2 instanceof Instance) {
                                                instance2 = (Instance) obj2;
                                            } else {
                                                instance2 = null;
                                            }
                                            if (instance2 == null) {
                                                vector3Vec2 = vec(next, "TargetOffset");
                                            } else {
                                                if (instance2.isA("BasePart")) {
                                                }
                                                if (obj != null) {
                                                    vector3Vec2 = PartData.INSTANCE.cframe((Instance) obj).getPosition();
                                                    if (vector3Vec2 == null) {
                                                        vector3Vec2 = vec(next, "TargetOffset");
                                                    }
                                                } else {
                                                    vector3Vec2 = vec(next, "TargetOffset");
                                                }
                                            }
                                            Vector3 vector6 = vector3Vec2;
                                            PartData partData6 = PartData.INSTANCE;
                                            Instance part6 = end.getPart();
                                            Intrinsics.checkNotNull(part6);
                                            vector3Minus = vector6.minus(partData6.cframe(part6).getPosition());
                                            if (vector3Minus.getMagnitude() > 0.5f) {
                                                zero = vector3Minus.getUnit().times(num(next, "MaxSpeed", 30.0f));
                                            } else {
                                                zero = Vector3.INSTANCE.getZERO();
                                            }
                                            float fNum2 = num(next, "MaxThrust", 4000.0f);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), zero, new Vector3(fNum2, fNum2, fNum2), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1013696779:
                                        if (!className.equals("BodyPosition")) {
                                            PartData partData7 = PartData.INSTANCE;
                                            Instance part7 = end.getPart();
                                            Intrinsics.checkNotNull(part7);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Position").minus(partData7.cframe(part7).getPosition()).times(num(next, "P", 10000.0f) / Math.max(num(next, "D", 1250.0f), 1.0f)), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1856572602:
                                        if (!className.equals("BodyThrust")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData8 = PartData.INSTANCE;
                                                Instance part8 = end.getPart();
                                                Intrinsics.checkNotNull(part8);
                                                vector3Vec3 = partData8.cframe(part8).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    default:
                                        it = it2;
                                        f3 = 0.0f;
                                        break;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                                break;
                            }
                        } else {
                            it = it2;
                            f3 = 0.0f;
                            break;
                        }
                        break;
                    case -1061210551:
                        if (className2.equals("BodyForce")) {
                            parent = next.getParent();
                            if (parent != null) {
                                parent = null;
                            } else {
                                parent = null;
                            }
                            end = new End(parent);
                            if (end.getInvMass() == 0.0f) {
                            }
                            if ((end.getInvMass() == 0.0f ? 1 : 0) == 0) {
                                invMass = 1.0f / end.getInvMass();
                                className = next.getClassName();
                                switch (className.hashCode()) {
                                    case -1894814529:
                                        if (!className.equals("BodyVelocity")) {
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Velocity"), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case -1061210551:
                                        if (!className.equals("BodyForce")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData9 = PartData.INSTANCE;
                                                Instance part9 = end.getPart();
                                                Intrinsics.checkNotNull(part9);
                                                vector3Vec3 = partData9.cframe(part9).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 464782595:
                                        if (!className.equals("RocketPropulsion")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            obj2 = next.get("Target");
                                            if (obj2 instanceof Instance) {
                                                instance2 = (Instance) obj2;
                                            } else {
                                                instance2 = null;
                                            }
                                            if (instance2 == null) {
                                                vector3Vec2 = vec(next, "TargetOffset");
                                            } else {
                                                if (instance2.isA("BasePart")) {
                                                }
                                                if (obj != null) {
                                                    vector3Vec2 = PartData.INSTANCE.cframe((Instance) obj).getPosition();
                                                    if (vector3Vec2 == null) {
                                                        vector3Vec2 = vec(next, "TargetOffset");
                                                    }
                                                } else {
                                                    vector3Vec2 = vec(next, "TargetOffset");
                                                }
                                            }
                                            Vector3 vector7 = vector3Vec2;
                                            PartData partData10 = PartData.INSTANCE;
                                            Instance part10 = end.getPart();
                                            Intrinsics.checkNotNull(part10);
                                            vector3Minus = vector7.minus(partData10.cframe(part10).getPosition());
                                            if (vector3Minus.getMagnitude() > 0.5f) {
                                                zero = vector3Minus.getUnit().times(num(next, "MaxSpeed", 30.0f));
                                            } else {
                                                zero = Vector3.INSTANCE.getZERO();
                                            }
                                            float fNum3 = num(next, "MaxThrust", 4000.0f);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), zero, new Vector3(fNum3, fNum3, fNum3), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1013696779:
                                        if (!className.equals("BodyPosition")) {
                                            PartData partData11 = PartData.INSTANCE;
                                            Instance part11 = end.getPart();
                                            Intrinsics.checkNotNull(part11);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Position").minus(partData11.cframe(part11).getPosition()).times(num(next, "P", 10000.0f) / Math.max(num(next, "D", 1250.0f), 1.0f)), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1856572602:
                                        if (!className.equals("BodyThrust")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData12 = PartData.INSTANCE;
                                                Instance part12 = end.getPart();
                                                Intrinsics.checkNotNull(part12);
                                                vector3Vec3 = partData12.cframe(part12).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    default:
                                        it = it2;
                                        f3 = 0.0f;
                                        break;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                                break;
                            }
                        } else {
                            it = it2;
                            f3 = 0.0f;
                            break;
                        }
                        break;
                    case 126027586:
                        if (!className2.equals("LinearVelocity")) {
                            it = it2;
                            f3 = 0.0f;
                        } else {
                            Object obj6 = next.get("Attachment0");
                            Instance instance4 = obj6 instanceof Instance ? (Instance) obj6 : null;
                            End end3 = new End(attPart(instance4));
                            if (!(end3.getInvMass() == 0.0f)) {
                                Object obj7 = next.get("VelocityConstraintMode");
                                EnumValue enumValue2 = obj7 instanceof EnumValue ? (EnumValue) obj7 : null;
                                int value2 = enumValue2 != null ? enumValue2.getValue() : 2;
                                Object obj8 = next.get("RelativeTo");
                                EnumValue enumValue3 = obj8 instanceof EnumValue ? (EnumValue) obj8 : null;
                                int value3 = enumValue3 != null ? enumValue3.getValue() : 2;
                                if (value3 == 2) {
                                    cFrameAttAxis = null;
                                } else {
                                    if (value3 == 1) {
                                        Object obj9 = next.get("Attachment1");
                                        instance = obj9 instanceof Instance ? (Instance) obj9 : null;
                                        if (instance == null) {
                                            instance = instance4;
                                        }
                                    } else {
                                        instance = instance4;
                                    }
                                    cFrameAttAxis = attAxis(instance);
                                }
                                switch (value2) {
                                    case 0:
                                        Object obj10 = next.get("LineDirection");
                                        Vector3 vector8 = obj10 instanceof Vector3 ? (Vector3) obj10 : null;
                                        if (vector8 == null) {
                                            f = 0.0f;
                                            vector8 = new Vector3(1.0f, 0.0f, 0.0f);
                                        } else {
                                            f = 0.0f;
                                        }
                                        vector3Times = vector8.getUnit().times(num(next, "LineVelocity", f));
                                        break;
                                    case 1:
                                        Object obj11 = next.get("PlaneVelocity");
                                        Vector2 vector2 = obj11 instanceof Vector2 ? (Vector2) obj11 : null;
                                        if (vector2 == null) {
                                            f2 = 0.0f;
                                            vector2 = new Vector2(0.0f, 0.0f);
                                        } else {
                                            f2 = 0.0f;
                                        }
                                        vector3Times = new Vector3(vector2.getX(), f2, vector2.getY());
                                        break;
                                    default:
                                        vector3Times = vec(next, "VectorVelocity");
                                        break;
                                }
                                if (cFrameAttAxis != null) {
                                    vector3Times = cFrameAttAxis.vectorToWorld(vector3Times);
                                }
                                float fNum4 = Intrinsics.areEqual(next.get("ForceLimitsEnabled"), (Object) false) ? Float.MAX_VALUE : num(next, "MaxForce", 1000.0f);
                                Vector3 vector3Plus = end3.getV().plus(vector4);
                                if (value2 != 0) {
                                    Vector3 vector3Minus2 = vector3Times.minus(vector3Plus);
                                    float invMass2 = fNum4 * dt * end3.getInvMass();
                                    if (vector3Minus2.getMagnitude() > invMass2) {
                                        vector3Minus2 = vector3Minus2.getUnit().times(invMass2);
                                    }
                                    end3.setV(end3.getV().plus(vector3Minus2));
                                    it = it2;
                                    f3 = 0.0f;
                                } else {
                                    if (cFrameAttAxis != null) {
                                        Object obj12 = next.get("LineDirection");
                                        Vector3 vector9 = obj12 instanceof Vector3 ? (Vector3) obj12 : null;
                                        if (vector9 == null) {
                                            vector9 = new Vector3(1.0f, 0.0f, 0.0f);
                                        }
                                        Vector3 vector3VectorToWorld2 = cFrameAttAxis.vectorToWorld(vector9);
                                        vector3 = vector3VectorToWorld2;
                                        if (vector3VectorToWorld2 == null) {
                                            Object obj13 = next.get("LineDirection");
                                            obj = obj13 instanceof Vector3 ? (Vector3) obj13 : null;
                                            if (obj == null) {
                                                vector3 = new Vector3(1.0f, 0.0f, 0.0f);
                                            } else {
                                                vector3 = (Vector3) obj;
                                            }
                                        }
                                    } else {
                                        Object obj14 = next.get("LineDirection");
                                        if (obj14 instanceof Vector3) {
                                        }
                                        if (obj == null) {
                                            vector3 = new Vector3(1.0f, 0.0f, 0.0f);
                                        } else {
                                            vector3 = (Vector3) obj;
                                        }
                                    }
                                    Vector3 unit = vector3.getUnit();
                                    float fDot = vector3Times.dot(unit) - vector3Plus.dot(unit);
                                    float invMass3 = fNum4 * dt * end3.getInvMass();
                                    end3.setV(end3.getV().plus(unit.times(RangesKt.coerceIn(fDot, -invMass3, invMass3))));
                                    it = it2;
                                    f3 = 0.0f;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                            }
                        }
                        break;
                    case 184673079:
                        if (!className2.equals("LineForce")) {
                            it = it2;
                            f3 = 0.0f;
                        } else {
                            Object obj15 = next.get("Attachment0");
                            Vector3 vector3AttWorld = attWorld(obj15 instanceof Instance ? (Instance) obj15 : null);
                            if (vector3AttWorld != null) {
                                Object obj16 = next.get("Attachment1");
                                Vector3 vector3AttWorld2 = attWorld(obj16 instanceof Instance ? (Instance) obj16 : null);
                                if (vector3AttWorld2 != null) {
                                    Object obj17 = next.get("Attachment0");
                                    End end4 = new End(attPart(obj17 instanceof Instance ? (Instance) obj17 : null));
                                    if ((end4.getInvMass() == 0.0f ? 1 : 0) != 0) {
                                        it = it2;
                                        f3 = 0.0f;
                                    } else {
                                        Vector3 vector3Minus3 = vector3AttWorld2.minus(vector3AttWorld);
                                        float magnitude = vector3Minus3.getMagnitude();
                                        if (magnitude < 0.001f) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            float fNum5 = num(next, "Magnitude", 1000.0f);
                                            if (Intrinsics.areEqual(next.get("InverseSquareLaw"), (Object) true)) {
                                                fNum5 /= RangesKt.coerceAtLeast(magnitude * magnitude, 1.0f);
                                            }
                                            end4.setV(end4.getV().plus(vector3Minus3.getUnit().times(fNum5 * dt * end4.getInvMass())));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                    }
                                } else {
                                    it = it2;
                                    f3 = 0.0f;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                            }
                        }
                        break;
                    case 258273070:
                        if (!className2.equals("AlignPosition")) {
                            it = it2;
                            f3 = 0.0f;
                        } else {
                            Object obj18 = next.get("Attachment0");
                            Instance instance5 = obj18 instanceof Instance ? (Instance) obj18 : null;
                            End end5 = new End(attPart(instance5));
                            if (!(end5.getInvMass() == 0.0f)) {
                                Vector3 vector3AttWorld3 = attWorld(instance5);
                                if (vector3AttWorld3 != null) {
                                    Object obj19 = next.get("Mode");
                                    EnumValue enumValue4 = obj19 instanceof EnumValue ? (EnumValue) obj19 : null;
                                    if (((enumValue4 != null ? enumValue4.getValue() : 1) == 0 ? 1 : 0) != 0) {
                                        vector3Vec = vec(next, "Position");
                                    } else {
                                        Object obj20 = next.get("Attachment1");
                                        vector3Vec = attWorld(obj20 instanceof Instance ? (Instance) obj20 : null);
                                        if (vector3Vec == null) {
                                            it = it2;
                                            f3 = 0.0f;
                                            break;
                                        }
                                    }
                                    Vector3 vector3Minus4 = vector3Vec.minus(vector3AttWorld3);
                                    if (!Intrinsics.areEqual(next.get("RigidityEnabled"), (Object) true)) {
                                        Vector3 vector3Times2 = vector3Minus4.times(num(next, "Responsiveness", 10.0f));
                                        float fNum6 = num(next, "MaxVelocity", Float.MAX_VALUE);
                                        if (vector3Times2.getMagnitude() > fNum6) {
                                            vector3Times2 = vector3Times2.getUnit().times(fNum6);
                                        }
                                        Vector3 vector3Minus5 = vector3Times2.minus(end5.getV().plus(vector4));
                                        float fNum7 = num(next, "MaxForce", 10000.0f) * dt * end5.getInvMass();
                                        if (vector3Minus5.getMagnitude() > fNum7) {
                                            vector3Minus5 = vector3Minus5.getUnit().times(fNum7);
                                        }
                                        end5.setV(end5.getV().plus(vector3Minus5));
                                        it = it2;
                                        f3 = 0.0f;
                                    } else {
                                        end5.move(vector3Minus4);
                                        end5.setV(Vector3.INSTANCE.getZERO());
                                        it = it2;
                                        f3 = 0.0f;
                                    }
                                } else {
                                    it = it2;
                                    f3 = 0.0f;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                            }
                        }
                        break;
                    case 464782595:
                        if (className2.equals("RocketPropulsion")) {
                            parent = next.getParent();
                            if (parent != null) {
                                parent = null;
                            } else {
                                parent = null;
                            }
                            end = new End(parent);
                            if (end.getInvMass() == 0.0f) {
                            }
                            if ((end.getInvMass() == 0.0f ? 1 : 0) == 0) {
                                invMass = 1.0f / end.getInvMass();
                                className = next.getClassName();
                                switch (className.hashCode()) {
                                    case -1894814529:
                                        if (!className.equals("BodyVelocity")) {
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Velocity"), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case -1061210551:
                                        if (!className.equals("BodyForce")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData13 = PartData.INSTANCE;
                                                Instance part13 = end.getPart();
                                                Intrinsics.checkNotNull(part13);
                                                vector3Vec3 = partData13.cframe(part13).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 464782595:
                                        if (!className.equals("RocketPropulsion")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            obj2 = next.get("Target");
                                            if (obj2 instanceof Instance) {
                                                instance2 = (Instance) obj2;
                                            } else {
                                                instance2 = null;
                                            }
                                            if (instance2 == null) {
                                                vector3Vec2 = vec(next, "TargetOffset");
                                            } else {
                                                if (instance2.isA("BasePart")) {
                                                }
                                                if (obj != null) {
                                                    vector3Vec2 = PartData.INSTANCE.cframe((Instance) obj).getPosition();
                                                    if (vector3Vec2 == null) {
                                                        vector3Vec2 = vec(next, "TargetOffset");
                                                    }
                                                } else {
                                                    vector3Vec2 = vec(next, "TargetOffset");
                                                }
                                            }
                                            Vector3 vector10 = vector3Vec2;
                                            PartData partData14 = PartData.INSTANCE;
                                            Instance part14 = end.getPart();
                                            Intrinsics.checkNotNull(part14);
                                            vector3Minus = vector10.minus(partData14.cframe(part14).getPosition());
                                            if (vector3Minus.getMagnitude() > 0.5f) {
                                                zero = vector3Minus.getUnit().times(num(next, "MaxSpeed", 30.0f));
                                            } else {
                                                zero = Vector3.INSTANCE.getZERO();
                                            }
                                            float fNum8 = num(next, "MaxThrust", 4000.0f);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), zero, new Vector3(fNum8, fNum8, fNum8), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1013696779:
                                        if (!className.equals("BodyPosition")) {
                                            PartData partData15 = PartData.INSTANCE;
                                            Instance part15 = end.getPart();
                                            Intrinsics.checkNotNull(part15);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Position").minus(partData15.cframe(part15).getPosition()).times(num(next, "P", 10000.0f) / Math.max(num(next, "D", 1250.0f), 1.0f)), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1856572602:
                                        if (!className.equals("BodyThrust")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData16 = PartData.INSTANCE;
                                                Instance part16 = end.getPart();
                                                Intrinsics.checkNotNull(part16);
                                                vector3Vec3 = partData16.cframe(part16).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    default:
                                        it = it2;
                                        f3 = 0.0f;
                                        break;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                                break;
                            }
                        } else {
                            it = it2;
                            f3 = 0.0f;
                            break;
                        }
                        break;
                    case 1013696779:
                        if (className2.equals("BodyPosition")) {
                            parent = next.getParent();
                            if (parent != null) {
                                parent = null;
                            } else {
                                parent = null;
                            }
                            end = new End(parent);
                            if (end.getInvMass() == 0.0f) {
                            }
                            if ((end.getInvMass() == 0.0f ? 1 : 0) == 0) {
                                invMass = 1.0f / end.getInvMass();
                                className = next.getClassName();
                                switch (className.hashCode()) {
                                    case -1894814529:
                                        if (!className.equals("BodyVelocity")) {
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Velocity"), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case -1061210551:
                                        if (!className.equals("BodyForce")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData17 = PartData.INSTANCE;
                                                Instance part17 = end.getPart();
                                                Intrinsics.checkNotNull(part17);
                                                vector3Vec3 = partData17.cframe(part17).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 464782595:
                                        if (!className.equals("RocketPropulsion")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            obj2 = next.get("Target");
                                            if (obj2 instanceof Instance) {
                                                instance2 = (Instance) obj2;
                                            } else {
                                                instance2 = null;
                                            }
                                            if (instance2 == null) {
                                                vector3Vec2 = vec(next, "TargetOffset");
                                            } else {
                                                if (instance2.isA("BasePart")) {
                                                }
                                                if (obj != null) {
                                                    vector3Vec2 = PartData.INSTANCE.cframe((Instance) obj).getPosition();
                                                    if (vector3Vec2 == null) {
                                                        vector3Vec2 = vec(next, "TargetOffset");
                                                    }
                                                } else {
                                                    vector3Vec2 = vec(next, "TargetOffset");
                                                }
                                            }
                                            Vector3 vector11 = vector3Vec2;
                                            PartData partData18 = PartData.INSTANCE;
                                            Instance part18 = end.getPart();
                                            Intrinsics.checkNotNull(part18);
                                            vector3Minus = vector11.minus(partData18.cframe(part18).getPosition());
                                            if (vector3Minus.getMagnitude() > 0.5f) {
                                                zero = vector3Minus.getUnit().times(num(next, "MaxSpeed", 30.0f));
                                            } else {
                                                zero = Vector3.INSTANCE.getZERO();
                                            }
                                            float fNum9 = num(next, "MaxThrust", 4000.0f);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), zero, new Vector3(fNum9, fNum9, fNum9), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1013696779:
                                        if (!className.equals("BodyPosition")) {
                                            PartData partData19 = PartData.INSTANCE;
                                            Instance part19 = end.getPart();
                                            Intrinsics.checkNotNull(part19);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Position").minus(partData19.cframe(part19).getPosition()).times(num(next, "P", 10000.0f) / Math.max(num(next, "D", 1250.0f), 1.0f)), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1856572602:
                                        if (!className.equals("BodyThrust")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData110 = PartData.INSTANCE;
                                                Instance part110 = end.getPart();
                                                Intrinsics.checkNotNull(part110);
                                                vector3Vec3 = partData110.cframe(part110).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    default:
                                        it = it2;
                                        f3 = 0.0f;
                                        break;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                                break;
                            }
                        } else {
                            it = it2;
                            f3 = 0.0f;
                            break;
                        }
                        break;
                    case 1766922129:
                        if (className2.equals("BodyGyro")) {
                            parent = next.getParent();
                            if (parent != null) {
                                parent = null;
                            } else {
                                parent = null;
                            }
                            end = new End(parent);
                            if (end.getInvMass() == 0.0f) {
                            }
                            if ((end.getInvMass() == 0.0f ? 1 : 0) == 0) {
                                invMass = 1.0f / end.getInvMass();
                                className = next.getClassName();
                                switch (className.hashCode()) {
                                    case -1894814529:
                                        if (!className.equals("BodyVelocity")) {
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Velocity"), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case -1061210551:
                                        if (!className.equals("BodyForce")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData111 = PartData.INSTANCE;
                                                Instance part111 = end.getPart();
                                                Intrinsics.checkNotNull(part111);
                                                vector3Vec3 = partData111.cframe(part111).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 464782595:
                                        if (!className.equals("RocketPropulsion")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            obj2 = next.get("Target");
                                            if (obj2 instanceof Instance) {
                                                instance2 = (Instance) obj2;
                                            } else {
                                                instance2 = null;
                                            }
                                            if (instance2 == null) {
                                                vector3Vec2 = vec(next, "TargetOffset");
                                            } else {
                                                if (instance2.isA("BasePart")) {
                                                }
                                                if (obj != null) {
                                                    vector3Vec2 = PartData.INSTANCE.cframe((Instance) obj).getPosition();
                                                    if (vector3Vec2 == null) {
                                                        vector3Vec2 = vec(next, "TargetOffset");
                                                    }
                                                } else {
                                                    vector3Vec2 = vec(next, "TargetOffset");
                                                }
                                            }
                                            Vector3 vector12 = vector3Vec2;
                                            PartData partData112 = PartData.INSTANCE;
                                            Instance part112 = end.getPart();
                                            Intrinsics.checkNotNull(part112);
                                            vector3Minus = vector12.minus(partData112.cframe(part112).getPosition());
                                            if (vector3Minus.getMagnitude() > 0.5f) {
                                                zero = vector3Minus.getUnit().times(num(next, "MaxSpeed", 30.0f));
                                            } else {
                                                zero = Vector3.INSTANCE.getZERO();
                                            }
                                            float fNum10 = num(next, "MaxThrust", 4000.0f);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), zero, new Vector3(fNum10, fNum10, fNum10), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1013696779:
                                        if (!className.equals("BodyPosition")) {
                                            PartData partData113 = PartData.INSTANCE;
                                            Instance part113 = end.getPart();
                                            Intrinsics.checkNotNull(part113);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Position").minus(partData113.cframe(part113).getPosition()).times(num(next, "P", 10000.0f) / Math.max(num(next, "D", 1250.0f), 1.0f)), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1856572602:
                                        if (!className.equals("BodyThrust")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData114 = PartData.INSTANCE;
                                                Instance part114 = end.getPart();
                                                Intrinsics.checkNotNull(part114);
                                                vector3Vec3 = partData114.cframe(part114).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    default:
                                        it = it2;
                                        f3 = 0.0f;
                                        break;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                                break;
                            }
                        } else {
                            it = it2;
                            f3 = 0.0f;
                            break;
                        }
                        break;
                    case 1856572602:
                        if (className2.equals("BodyThrust")) {
                            parent = next.getParent();
                            if (parent != null) {
                                parent = null;
                            } else {
                                parent = null;
                            }
                            end = new End(parent);
                            if (end.getInvMass() == 0.0f) {
                            }
                            if ((end.getInvMass() == 0.0f ? 1 : 0) == 0) {
                                invMass = 1.0f / end.getInvMass();
                                className = next.getClassName();
                                switch (className.hashCode()) {
                                    case -1894814529:
                                        if (!className.equals("BodyVelocity")) {
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Velocity"), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case -1061210551:
                                        if (!className.equals("BodyForce")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData115 = PartData.INSTANCE;
                                                Instance part115 = end.getPart();
                                                Intrinsics.checkNotNull(part115);
                                                vector3Vec3 = partData115.cframe(part115).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 464782595:
                                        if (!className.equals("RocketPropulsion")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            obj2 = next.get("Target");
                                            if (obj2 instanceof Instance) {
                                                instance2 = (Instance) obj2;
                                            } else {
                                                instance2 = null;
                                            }
                                            if (instance2 == null) {
                                                vector3Vec2 = vec(next, "TargetOffset");
                                            } else {
                                                if (instance2.isA("BasePart")) {
                                                }
                                                if (obj != null) {
                                                    vector3Vec2 = PartData.INSTANCE.cframe((Instance) obj).getPosition();
                                                    if (vector3Vec2 == null) {
                                                        vector3Vec2 = vec(next, "TargetOffset");
                                                    }
                                                } else {
                                                    vector3Vec2 = vec(next, "TargetOffset");
                                                }
                                            }
                                            Vector3 vector13 = vector3Vec2;
                                            PartData partData116 = PartData.INSTANCE;
                                            Instance part116 = end.getPart();
                                            Intrinsics.checkNotNull(part116);
                                            vector3Minus = vector13.minus(partData116.cframe(part116).getPosition());
                                            if (vector3Minus.getMagnitude() > 0.5f) {
                                                zero = vector3Minus.getUnit().times(num(next, "MaxSpeed", 30.0f));
                                            } else {
                                                zero = Vector3.INSTANCE.getZERO();
                                            }
                                            float fNum11 = num(next, "MaxThrust", 4000.0f);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), zero, new Vector3(fNum11, fNum11, fNum11), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1013696779:
                                        if (!className.equals("BodyPosition")) {
                                            PartData partData117 = PartData.INSTANCE;
                                            Instance part117 = end.getPart();
                                            Intrinsics.checkNotNull(part117);
                                            end.setV(drive(end.getV(), end.getV().plus(vector4), vec(next, "Position").minus(partData117.cframe(part117).getPosition()).times(num(next, "P", 10000.0f) / Math.max(num(next, "D", 1250.0f), 1.0f)), vec(next, "MaxForce"), invMass, dt));
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    case 1856572602:
                                        if (!className.equals("BodyThrust")) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            if (Intrinsics.areEqual(next.getClassName(), "BodyThrust")) {
                                                PartData partData118 = PartData.INSTANCE;
                                                Instance part118 = end.getPart();
                                                Intrinsics.checkNotNull(part118);
                                                vector3Vec3 = partData118.cframe(part118).vectorToWorld(vec(next, "Force"));
                                            } else {
                                                vector3Vec3 = vec(next, "Force");
                                            }
                                            end.setV(end.getV().plus(vector3Vec3.times(dt / invMass)));
                                            it = it2;
                                            f3 = 0.0f;
                                        }
                                        break;
                                    default:
                                        it = it2;
                                        f3 = 0.0f;
                                        break;
                                }
                            } else {
                                it = it2;
                                f3 = 0.0f;
                                break;
                            }
                        } else {
                            it = it2;
                            f3 = 0.0f;
                            break;
                        }
                        break;
                    case 2090650602:
                        if (!className2.equals("SpringConstraint")) {
                            f3 = f4;
                            it = it2;
                        } else {
                            Object obj21 = next.get("Attachment0");
                            Instance instance6 = obj21 instanceof Instance ? (Instance) obj21 : null;
                            Object obj22 = next.get("Attachment1");
                            Instance r3 = obj22 instanceof Instance ? (Instance) obj22 : null;
                            Vector3 vector3AttWorld4 = attWorld(instance6);
                            if (vector3AttWorld4 != null) {
                                Vector3 vector3AttWorld5 = attWorld(r3);
                                if (vector3AttWorld5 != null) {
                                    End end6 = new End(attPart(instance6));
                                    End end7 = new End(attPart(r3));
                                    if (end6.getBody() != null && end6.getBody() == end7.getBody()) {
                                        f3 = f4;
                                        it = it2;
                                    } else {
                                        if ((end6.getInvMass() + end7.getInvMass() == f4 ? 1 : 0) != 0) {
                                            it = it2;
                                            f3 = 0.0f;
                                        } else {
                                            Vector3 vector3Minus6 = vector3AttWorld5.minus(vector3AttWorld4);
                                            float magnitude2 = vector3Minus6.getMagnitude();
                                            if (magnitude2 < 1.0E-4f) {
                                                it = it2;
                                                f3 = 0.0f;
                                            } else {
                                                Vector3 vector3Div = vector3Minus6.div(magnitude2);
                                                float fNum12 = (num(next, "Stiffness", f4) * (magnitude2 - num(next, "FreeLength", 1.0f))) + (num(next, "Damping", 0.0f) * end7.getV().minus(end6.getV()).dot(vector3Div));
                                                float fNum13 = num(next, "MaxForce", Float.MAX_VALUE);
                                                float fCoerceIn = RangesKt.coerceIn(fNum12, -fNum13, fNum13) * dt;
                                                end6.setV(end6.getV().plus(vector3Div.times(fCoerceIn * end6.getInvMass())));
                                                end7.setV(end7.getV().minus(vector3Div.times(end7.getInvMass() * fCoerceIn)));
                                                f4 = 0.0f;
                                                f3 = f4;
                                                it = it2;
                                            }
                                        }
                                    }
                                } else {
                                    f3 = f4;
                                    it = it2;
                                }
                            } else {
                                f3 = f4;
                                it = it2;
                            }
                        }
                        break;
                    default:
                        f3 = f4;
                        it = it2;
                        break;
                }
            }
        }
    }

    private static final float drive$ax(float $dt, float $mass, float c, float p, float w, float f) {
        float lim = (f * $dt) / $mass;
        return RangesKt.coerceIn(w - p, -lim, lim) + c;
    }

    private final Vector3 drive(Vector3 v, Vector3 pred, Vector3 want, Vector3 maxF, float mass, float dt) {
        return new Vector3(drive$ax(dt, mass, v.getX(), pred.getX(), want.getX(), maxF.getX()), drive$ax(dt, mass, v.getY(), pred.getY(), want.getY(), maxF.getY()), drive$ax(dt, mass, v.getZ(), pred.getZ(), want.getZ(), maxF.getZ()));
    }

    /*  JADX ERROR: Types fix failed
        jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r16v2 ??, new type: boolean
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:186)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:245)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
        Caused by: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "arg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.verifyType(TypeUpdate.java:210)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.queueTypeUpdate(TypeUpdate.java:171)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.sameFirstArgListener(TypeUpdate.java:454)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:310)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runUpdate(TypeUpdate.java:124)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:91)
        	... 5 more
        */
    private final void solveDistanceConstraints() {
        List<Instance> list = this.studio.renderConstraints();
        if (list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Instance c = list.get(i);
            if (Intrinsics.areEqual(c.get("Enabled"), (Object) false)) {
                continue;
            }
            Object a0Obj = c.get("Attachment0");
            Object a1Obj = c.get("Attachment1");
            if (!(a0Obj instanceof Instance) || !(a1Obj instanceof Instance)) {
                continue;
            }
            Instance a0 = (Instance) a0Obj;
            Instance a1 = (Instance) a1Obj;
            Instance p0 = a0.getParent();
            Instance p1 = a1.getParent();
            if (p0 == null || p1 == null || !p0.isA("BasePart") || !p1.isA("BasePart")) {
                continue;
            }
            CFrame cf0 = PartData.INSTANCE.cframe(p0);
            CFrame cf1 = PartData.INSTANCE.cframe(p1);
            Vector3 off0 = (a0.get("CFrame") instanceof CFrame) ? ((CFrame) a0.get("CFrame")).getPosition() : Vector3.INSTANCE.getZERO();
            Vector3 off1 = (a1.get("CFrame") instanceof CFrame) ? ((CFrame) a1.get("CFrame")).getPosition() : Vector3.INSTANCE.getZERO();
            Vector3 w0 = cf0.times(off0);
            Vector3 w1 = cf1.times(off1);
            Vector3 delta = w1.minus(w0);
            float dist = delta.getMagnitude();
            if (dist < 1.0E-4f) {
                continue;
            }
            Vector3 dir = delta.div(dist);
            Body b0 = this.bodyOf.get(p0);
            Body b1 = this.bodyOf.get(p1);
            boolean m0 = b0 != null && !Intrinsics.areEqual(p0.get("Anchored"), Boolean.TRUE);
            boolean m1 = b1 != null && !Intrinsics.areEqual(p1.get("Anchored"), Boolean.TRUE);
            if (!m0 && !m1) {
                continue;
            }
            String cn = c.getClassName();
            if (Intrinsics.areEqual(cn, "SpringConstraint")) {
                float freeLen = (c.get("FreeLength") instanceof Number) ? ((Number) c.get("FreeLength")).floatValue() : 1.0f;
                float stiff = (c.get("Stiffness") instanceof Number) ? ((Number) c.get("Stiffness")).floatValue() : 1000.0f;
                float damp = (c.get("Damping") instanceof Number) ? ((Number) c.get("Damping")).floatValue() : 0.0f;
                float stretch = dist - freeLen;
                float relVel = (m1 ? b1.getVel().dot(dir) : 0.0f) - (m0 ? b0.getVel().dot(dir) : 0.0f);
                float fMag = (stretch * stiff) + (relVel * damp);
                float dt = 0.016666668f;
                if (m0) {
                    float invM0 = 1.0f / Math.max(b0.getMass(), 0.2f);
                    b0.setVel(b0.getVel().plus(dir.times(fMag * invM0 * dt)));
                    b0.setSleeping(false);
                }
                if (m1) {
                    float invM1 = 1.0f / Math.max(b1.getMass(), 0.2f);
                    b1.setVel(b1.getVel().minus(dir.times(fMag * invM1 * dt)));
                    b1.setSleeping(false);
                }
            } else {
                float targetLen = (c.get("Length") instanceof Number) ? ((Number) c.get("Length")).floatValue() : 5.0f;
                boolean isRod = Intrinsics.areEqual(cn, "RodConstraint");
                if (!isRod && dist <= targetLen) {
                    continue;
                }
                float err = dist - targetLen;
                float wSum = (m0 ? 1.0f : 0.0f) + (m1 ? 1.0f : 0.0f);
                float corr = err / wSum;
                if (m0) {
                    Vector3 shift0 = dir.times(corr);
                    for (Instance part : b0.getParts()) {
                        CFrame cur = PartData.INSTANCE.cframe(part);
                        Instance.set$default(part, "CFrame", cur.plus(shift0), false, 4, null);
                    }
                    float vn0 = b0.getVel().dot(dir);
                    if (isRod || vn0 < 0.0f) {
                        b0.setVel(b0.getVel().minus(dir.times(vn0 * (m1 ? 0.5f : 1.0f))));
                    }
                    b0.setSleeping(false);
                }
                if (m1) {
                    Vector3 shift1 = dir.times(-corr);
                    for (Instance part : b1.getParts()) {
                        CFrame cur = PartData.INSTANCE.cframe(part);
                        Instance.set$default(part, "CFrame", cur.plus(shift1), false, 4, null);
                    }
                    float vn1 = b1.getVel().dot(dir);
                    if (isRod || vn1 > 0.0f) {
                        b1.setVel(b1.getVel().minus(dir.times(vn1 * (m0 ? 0.5f : 1.0f))));
                    }
                    b1.setSleeping(false);
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:138:0x048b A[Catch: all -> 0x0605, TryCatch #1 {all -> 0x0605, blocks: (B:94:0x033e, B:96:0x034f, B:98:0x035b, B:100:0x0363, B:103:0x0373, B:108:0x03c6, B:112:0x03df, B:116:0x03f6, B:120:0x040e, B:122:0x0419, B:123:0x0421, B:125:0x0427, B:126:0x043f, B:128:0x044d, B:130:0x045c, B:133:0x0462, B:137:0x046e, B:141:0x049f, B:143:0x04a5, B:145:0x04af, B:147:0x04b6, B:149:0x04c1, B:153:0x04d4, B:155:0x0509, B:159:0x0523, B:162:0x0538, B:164:0x054c, B:166:0x0560, B:170:0x0586, B:172:0x0591, B:186:0x05c6, B:187:0x05d1, B:189:0x05d7, B:190:0x05e4, B:175:0x059c, B:176:0x05a0, B:178:0x05a6, B:168:0x0572, B:158:0x051b, B:138:0x048b, B:169:0x057a, B:115:0x03f0, B:111:0x03d9, B:195:0x05fc), top: B:206:0x033e }] */
    /* JADX WARN: Code duplicated, block: B:20:0x0063 A[Catch: all -> 0x0607, TRY_ENTER, TryCatch #4 {all -> 0x0607, blocks: (B:10:0x002b, B:11:0x003a, B:13:0x0040, B:15:0x004d, B:33:0x008b, B:34:0x00bf, B:39:0x00db, B:40:0x0106, B:79:0x0299, B:81:0x02a3, B:82:0x02af, B:84:0x02b5, B:86:0x02d5, B:87:0x02f9, B:89:0x02ff, B:92:0x0336, B:78:0x0295, B:20:0x0063, B:21:0x0067), top: B:212:0x002b }] */
    /* JADX WARN: Code duplicated, block: B:225:0x0082 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:226:0x0080 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:229:? A[LOOP:1: B:21:0x0067->B:229:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:23:0x006d A[Catch: all -> 0x028b, TRY_ENTER, TryCatch #5 {all -> 0x028b, blocks: (B:17:0x0058, B:31:0x0085, B:37:0x00c7, B:61:0x01ec, B:62:0x0201, B:64:0x0207, B:66:0x022b, B:69:0x0238, B:70:0x0258, B:72:0x025e, B:73:0x0282, B:23:0x006d), top: B:214:0x0058 }] */
    /* JADX WARN: Code duplicated, block: B:25:0x007b  */
    /* JADX WARN: Code duplicated, block: B:26:0x007d  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v70 */
    /* JADX WARN: Type inference failed for: r0v71 */
    /* JADX WARN: Type inference failed for: r0v72 */
    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v14, types: [int] */
    /* JADX WARN: Type inference failed for: r11v15, types: [int] */
    /* JADX WARN: Type inference failed for: r18v5 */
    /* JADX WARN: Type inference failed for: r18v6 */
    /* JADX WARN: Type inference failed for: r21v10 */
    /* JADX WARN: Type inference failed for: r2v29 */
    /* JADX WARN: Type inference failed for: r2v30 */
    /* JADX WARN: Type inference failed for: r2v31 */
    /* JADX WARN: Type inference failed for: r2v35 */
    /* JADX WARN: Type inference failed for: r6v4 */
    /* JADX WARN: Type inference failed for: r6v5 */
    /* JADX WARN: Type inference failed for: r6v6 */
    private final void stepBodies(float dt) {
        try {
        boolean z;
        String str;
        Vector3 vector3;
        Vector3 vector4;
        ArrayList arrayListEmptyList;
        int r2;
        float f;
        float f2;
        float[] fArrPhysProps;
        float f3;
        ConvexShape convexShapeShapeOf$default;
        ConvexShape convexShape;
        Vector3 first;
        Iterator it;
        boolean z2;
        PlaySession playSession = this;
        float f4 = dt;
        float gravity = playSession.getGravity();
        Object obj = playSession.dm.getWorkspace().get("FallenPartsDestroyHeight");
        Number number = obj instanceof Number ? (Number) obj : null;
        float fFloatValue = number != null ? number.floatValue() : -500.0f;
        boolean z3 = true;
        playSession.writing = true;
        boolean z4 = false;
        try {
            try { applyMovers(dt); } catch (Exception _amEx) { OutputLog.INSTANCE.warn("Physics mover skipped: " + _amEx.getMessage()); }
            for (Body body : CollectionsKt.toList(playSession.bodies)) {
                if (body.getSleeping()) {
                    f4 = dt;
                    z4 = false;
                } else {
                    List<Instance> parts = body.getParts();
                    if (!(parts instanceof Collection)) {
                        it = parts.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                z = z4;
                                break;
                            }
                            if (((Instance) it.next()).getParent() == null) {
                                z2 = z3;
                            } else {
                                z2 = z4;
                            }
                            if (z2) {
                                z = z3;
                                break;
                            }
                        }
                    } else {
                        try {
                            if (!parts.isEmpty()) {
                                it = parts.iterator();
                                while (true) {
                                    if (it.hasNext()) {
                                        z = z4;
                                        break;
                                    }
                                    if (((Instance) it.next()).getParent() == null) {
                                        z2 = z3;
                                    } else {
                                        z2 = z4;
                                    }
                                    if (z2) {
                                        z = z3;
                                        break;
                                    }
                                }
                            } else {
                                z = z4;
                            }
                        } catch (Throwable th) {
                            playSession.writing = false;
                            throw th;
                        }
                    }
                    if (z) {
                        playSession.bodies.remove(body);
                    } else {
                        body.setVel(new Vector3(body.getVel().getX(), body.getVel().getY() - (gravity * f4), body.getVel().getZ()));
                        Vector3 vector3Times = body.getVel().times(f4);
                        for (Instance instance : body.getParts()) {
                            instance.set("CFrame", PartData.INSTANCE.cframe(instance).plus(vector3Times), z4);
                        }
                        Vector3 zero = Vector3.INSTANCE.getZERO();
                        Instance instance2 = null;
                        ArrayList arrayList = new ArrayList(body.getParts().size());
                        Vector3 vector5 = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
                        Vector3 vector3UnaryMinus = vector5.unaryMinus();
                        Iterator<Instance> it2 = body.getParts().iterator();
                        while (true) {
                            str = "CanCollide";
                            if (!it2.hasNext()) {
                                break;
                            }
                            try {
                                Instance next = it2.next();
                                try {
                                    Pair<Vector3, Vector3> pairPartAabb = Geometry.INSTANCE.partAabb(next);
                                    arrayList.add(pairPartAabb);
                                    if (!Intrinsics.areEqual(next.get("CanCollide"), Boolean.valueOf(z4))) {
                                        ArrayList arrayList2 = arrayList;
                                        Vector3 vector6 = new Vector3(Math.min(vector5.getX(), pairPartAabb.getFirst().getX()), Math.min(vector5.getY(), pairPartAabb.getFirst().getY()), Math.min(vector5.getZ(), pairPartAabb.getFirst().getZ()));
                                        vector3UnaryMinus = new Vector3(Math.max(vector3UnaryMinus.getX(), pairPartAabb.getSecond().getX()), Math.max(vector3UnaryMinus.getY(), pairPartAabb.getSecond().getY()), Math.max(vector3UnaryMinus.getZ(), pairPartAabb.getSecond().getZ()));
                                        arrayList = arrayList2;
                                        vector5 = vector6;
                                    }
                                } catch (Throwable th2) {
                                    Throwable th = th2;
                                    playSession = this;
                                    playSession.writing = false;
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                Throwable th = th3;
                            }
                        }
                        ArrayList arrayList3 = arrayList;
                        Vector3 vector7 = vector5;
                        boolean z5 = z4;
                        try {
                            if (vector7.getX() <= vector3UnaryMinus.getX()) {
                                vector3 = vector3UnaryMinus;
                                playSession = this;
                                vector4 = vector7;
                                List listQuery$default = query$default(playSession, vector4, vector3, false, 4, null);
                                int i = 0;
                                ArrayList arrayList4 = new ArrayList();
                                for (Object obj2 : listQuery$default) {
                                    List list = listQuery$default;
                                    int i2 = i;
                                    if (!Intrinsics.areEqual(((Instance) obj2).get("CanCollide"), Boolean.valueOf(z5))) {
                                        arrayList4.add(obj2);
                                    }
                                    i = i2;
                                    listQuery$default = list;
                                }
                                ArrayList<Instance> arrayList5 = arrayList4;
                                int i3 = 0;
                                ArrayList arrayList6 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList5, 10));
                                for (Instance instance3 : arrayList5) {
                                    arrayList6.add(TuplesKt.to(instance3, Geometry.INSTANCE.partAabb(instance3)));
                                }
                                arrayListEmptyList = arrayList6;
                            } else {
                                playSession = this;
                                vector3 = vector3UnaryMinus;
                                vector4 = vector7;
                                arrayListEmptyList = new ArrayList();
                            }
                            if (!arrayListEmptyList.isEmpty()) {
                                Iterator it3 = body.getParts().iterator();
                                int r11 = z5 ? 1 : 0;
                                while (it3.hasNext()) {
                                    int r21 = r11;
                                    r11++;
                                    Instance instance4 = (Instance) it3.next();
                                    if (!Intrinsics.areEqual(instance4.get(str), Boolean.valueOf(z5))) {
                                        Object obj3 = arrayList3.get(r21);
                                        Intrinsics.checkNotNullExpressionValue(obj3, "get(...)");
                                        Pair pair = (Pair) obj3;
                                        Vector3 vector8 = (Vector3) pair.component1();
                                        Vector3 vector9 = (Vector3) pair.component2();
                                        ConvexShape convexShape2 = null;
                                        Iterator it4 = arrayListEmptyList.iterator();
                                        while (it4.hasNext()) {
                                            Pair pair2 = (Pair) it4.next();
                                            Instance instance5 = (Instance) pair2.component1();
                                            Pair pair3 = (Pair) pair2.component2();
                                            boolean zAabbOverlap = Geometry.INSTANCE.aabbOverlap(vector8, vector9, (Vector3) pair3.getFirst(), (Vector3) pair3.getSecond(), 0.05f);
                                            ConvexShape convexShape3 = convexShape2;
                                            if (zAabbOverlap) {
                                                if (convexShape3 == null) {
                                                    try {
                                                        convexShapeShapeOf$default = Geometry.shapeOf$default(Geometry.INSTANCE, instance4, null, 2, null);
                                                        convexShape = convexShapeShapeOf$default;
                                                    } catch (Throwable th4) {
                                                        Throwable th = th4;
                                                        playSession.writing = false;
                                                        throw th;
                                                    }
                                                } else {
                                                    convexShapeShapeOf$default = convexShape3;
                                                    convexShape = convexShapeShapeOf$default;
                                                }
                                                ConvexShape convexShape4 = convexShape;
                                                Pair<Vector3, Float> pairContact = Geometry.INSTANCE.contact(convexShapeShapeOf$default, vector8, vector9, instance5);
                                                if (pairContact == null || (first = pairContact.getFirst()) == null) {
                                                    convexShape2 = convexShape4;
                                                } else {
                                                    if (first.getMagnitude() > zero.getMagnitude()) {
                                                        zero = first;
                                                        instance2 = instance5;
                                                    }
                                                    playSession.touchBegin(instance4, instance5);
                                                    convexShape2 = convexShape4;
                                                }
                                            } else {
                                                convexShape2 = convexShape3;
                                            }
                                        }
                                    }
                                }
                            }
                            float f5 = gravity;
                            Vector3 vector10 = zero;
                            body.setMin(vector4.plus(vector10.getMagnitude() > 1.0E-4f ? vector10 : Vector3.INSTANCE.getZERO()));
                            body.setMax(vector3.plus(vector10.getMagnitude() > 1.0E-4f ? vector10 : Vector3.INSTANCE.getZERO()));
                            body.setHasBox(vector4.getX() <= vector3.getX() ? z3 : z5);
                            if (vector10.getMagnitude() > 1.0E-4f) {
                                for (Instance instance6 : body.getParts()) {
                                    instance6.set("CFrame", PartData.INSTANCE.cframe(instance6).plus(vector10), z5);
                                    z5 = false;
                                }
                                Vector3 unit = vector10.getUnit();
                                float fDot = body.getVel().dot(unit);
                                if (instance2 != null) {
                                    Instance instance7 = instance2;
                                    Object obj4 = instance7.get("CustomPhysicalProperties");
                                    f = 1.0E-4f;
                                    PhysicalProps physicalProps = obj4 instanceof PhysicalProps ? (PhysicalProps) obj4 : null;
                                    if (physicalProps == null) {
                                        f2 = 0.0f;
                                        fArrPhysProps = playSession.physProps(PartData.INSTANCE.material(instance7));
                                    } else {
                                        if (!physicalProps.getCustom()) {
                                            physicalProps = null;
                                        }
                                        if (physicalProps != null) {
                                            float density = physicalProps.getDensity();
                                            float friction = physicalProps.getFriction();
                                            float elasticity = physicalProps.getElasticity();
                                            f2 = 0.0f;
                                            fArrPhysProps = new float[3];
                                            fArrPhysProps[0] = density;
                                            fArrPhysProps[z3 ? 1 : 0] = friction;
                                            fArrPhysProps[2] = elasticity;
                                        } else {
                                            f2 = 0.0f;
                                            fArrPhysProps = playSession.physProps(PartData.INSTANCE.material(instance7));
                                        }
                                    }
                                } else {
                                    f = 1.0E-4f;
                                    f2 = 0.0f;
                                    fArrPhysProps = null;
                                }
                                float friction2 = ((fArrPhysProps != null ? fArrPhysProps[z3 ? 1 : 0] : 0.3f) + body.getFriction()) / 2.0f;
                                float elasticity2 = ((fArrPhysProps != null ? fArrPhysProps[2] : 0.5f) + body.getElasticity()) / 2.0f;
                                if (fDot < f2) {
                                    f3 = 0.5f;
                                    body.setVel(body.getVel().minus(unit.times(fDot * (((-fDot) > ((f5 * dt) * 2.0f) + 6.0f ? elasticity2 : f2) + 1.0f))));
                                    Vector3 vector3Minus = body.getVel().minus(unit.times(body.getVel().dot(unit)));
                                    float magnitude = vector3Minus.getMagnitude();
                                    if (magnitude > f) {
                                        float f6 = (-fDot) * friction2;
                                        body.setVel(body.getVel().minus(f6 >= magnitude ? vector3Minus : vector3Minus.times(f6 / magnitude)));
                                    }
                                } else {
                                    f3 = 0.5f;
                                }
                                if (body.getVel().getMagnitude() < (f5 * dt * 0.1f) + f3) {
                                    body.setRestTime(body.getRestTime() + dt);
                                    if (body.getRestTime() > 0.4f) {
                                        z3 = z3;
                                        body.setSleeping(z3);
                                        body.setVel(Vector3.INSTANCE.getZERO());
                                    } else {
                                        z3 = z3;
                                    }
                                } else {
                                    z3 = z3;
                                    body.setRestTime(f2);
                                }
                            } else {
                                z3 = z3;
                                body.setRestTime(0.0f);
                            }
                            List<Instance> parts2 = body.getParts();
                            if (!(parts2 instanceof Collection) || !parts2.isEmpty()) {
                                Iterator it5 = parts2.iterator();
                                while (true) {
                                    if (it5.hasNext()) {
                                        if ((PartData.INSTANCE.cframe((Instance) it5.next()).getY() < fFloatValue ? z3 ? 1 : 0 : 0) == 0) {
                                            r2 = 0;
                                            break;
                                        }
                                    } else {
                                        r2 = z3 ? 1 : 0;
                                        break;
                                    }
                                }
                            } else {
                                r2 = z3 ? 1 : 0;
                            }
                            if (r2 != 0) {
                                Iterator it6 = body.getParts().iterator();
                                while (it6.hasNext()) {
                                    ((Instance) it6.next()).destroy();
                                }
                                playSession.bodies.remove(body);
                            }
                            f4 = dt;
                            gravity = f5;
                            z4 = false;
                        } catch (Throwable th5) {
                            Throwable th = th5;
                            playSession = this;
                            playSession.writing = false;
                            throw th;
                        }
                    }
                }
            }
            playSession.solveDistanceConstraints();
            playSession.writing = false;
        } catch (Throwable th6) {
            Throwable th = th6;
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final void touchBegin(Instance a, Instance b) {
        Set<Instance> setNewSetFromMap;
        Set<Instance> setNewSetFromMap2;
        if (Intrinsics.areEqual(a.get("CanTouch"), (Object) false) || Intrinsics.areEqual(b.get("CanTouch"), (Object) false)) {
            return;
        }
        Map map = this.touching;
        Set<Instance> set = (Set<Instance>) map.get(a);
        if (set == null) {
            setNewSetFromMap = Collections.newSetFromMap(new IdentityHashMap());
            map.put(a, setNewSetFromMap);
        } else {
            setNewSetFromMap = set;
        }
        if (setNewSetFromMap.add(b)) {
            Map map2 = this.touching;
            Set<Instance> set2 = (Set<Instance>) map2.get(b);
            if (set2 == null) {
                setNewSetFromMap2 = Collections.newSetFromMap(new IdentityHashMap());
                map2.put(b, setNewSetFromMap2);
            } else {
                setNewSetFromMap2 = set2;
            }
            setNewSetFromMap2.add(a);
            this.rt.fire(a, "Touched", this.rt.wrap(b));
            this.rt.fire(b, "Touched", this.rt.wrap(a));
        }
    }

    private final void touchEnd(Instance a, Instance b) {
        Set<Instance> set = this.touching.get(a);
        if (set != null && set.remove(b)) {
            Set<Instance> set2 = this.touching.get(b);
            if (set2 != null) {
                set2.remove(a);
            }
            this.rt.fire(a, "TouchEnded", this.rt.wrap(b));
            this.rt.fire(b, "TouchEnded", this.rt.wrap(a));
        }
    }

    public final List<Instance> touchingParts(Instance p) {
        List<Instance> list;
        Intrinsics.checkNotNullParameter(p, "p");
        Set<Instance> set = this.touching.get(p);
        return (set == null || (list = CollectionsKt.toList(set)) == null) ? CollectionsKt.emptyList() : list;
    }

    private final void checkCharacterTouches() {
        Instance ch = this.character;
        if (ch == null || this.dead) {
            return;
        }
        boolean z = false;
        Instance torso = Instance.findFirstChild$default(ch, "Torso", false, 2, null);
        if (torso == null && (torso = Instance.findFirstChild$default(ch, "UpperTorso", false, 2, null)) == null && (torso = this.hrp) == null) {
            return;
        }
        Instance leg = Instance.findFirstChild$default(ch, "Left Leg", false, 2, null);
        if (leg == null && (leg = Instance.findFirstChild$default(ch, "LeftFoot", false, 2, null)) == null) {
            leg = torso;
        }
        Pair<Vector3, Vector3> pairCharBox = charBox();
        Vector3 c = pairCharBox.component1();
        Vector3 half = pairCharBox.component2();
        Vector3 pad = new Vector3(0.15f, 0.15f, 0.15f);
        ConvexShape box = Geometry.INSTANCE.aabbShape(c, half.plus(pad));
        Set<Instance> now = Collections.newSetFromMap(new IdentityHashMap());
        for (Instance p : query(c.minus(half).minus(pad), c.plus(half).plus(pad), true)) {
            if (isCharacterPart(p)) {
                z = false;
            } else if (!Intrinsics.areEqual(p.get("CanTouch"), Boolean.valueOf(z))) {
                if (Geometry.INSTANCE.contact(box, c.minus(half).minus(pad), c.plus(half).plus(pad), p) != null) {
                    now.add(p);
                }
                z = false;
            }
        }
        Set<Instance> set = this.touching.get(torso);
        List list = set != null ? CollectionsKt.toList(set) : null;
        if (list == null) {
            list = CollectionsKt.emptyList();
        }
        List list2 = list;
        Set<Instance> set2 = this.touching.get(leg);
        List list3 = set2 != null ? CollectionsKt.toList(set2) : null;
        if (list3 == null) {
            list3 = CollectionsKt.emptyList();
        }
        List<Instance> prev = CollectionsKt.plus((Collection) list2, (Iterable) list3);
        for (Instance p2 : now) {
            Geometry geometry = Geometry.INSTANCE;
            Intrinsics.checkNotNull(p2);
            Instance limb = geometry.partAabb(p2).getSecond().getY() < c.getY() - (half.getY() * 0.4f) ? leg : torso;
            Set<Instance> set3 = this.touching.get(torso);
            if (!(set3 != null && set3.contains(p2))) {
                Set<Instance> set4 = this.touching.get(leg);
                if (!(set4 != null && set4.contains(p2))) {
                    touchBegin(limb, p2);
                }
            }
        }
        for (Instance p3 : prev) {
            if (!now.contains(p3)) {
                touchEnd(torso, p3);
                touchEnd(leg, p3);
            }
        }
    }

    public final void step(float dt0) {
        float dt = RangesKt.coerceIn(dt0, 0.0f, 0.05f);
        this.time += (double) dt;
        long t0 = System.nanoTime();
        if (this.physicsDirty) {
            rebuildPhysics();
        }
        long t1 = System.nanoTime();
        Instance rs = getRunService();
        LuaRuntime luaRuntime = this.rt;
        LuaNumber luaNumberValueOf = LuaValue.valueOf(dt);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        luaRuntime.fire(rs, "RenderStepped", luaNumberValueOf);
        LuaRuntime luaRuntime2 = this.rt;
        LuaNumber luaNumberValueOf2 = LuaValue.valueOf(dt);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
        luaRuntime2.fire(rs, "PreRender", luaNumberValueOf2);
        LuaRuntime luaRuntime3 = this.rt;
        LuaNumber luaNumberValueOf3 = LuaValue.valueOf(this.time);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf3, "valueOf(...)");
        LuaNumber luaNumberValueOf4 = LuaValue.valueOf(dt);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf4, "valueOf(...)");
        luaRuntime3.fire(rs, "Stepped", luaNumberValueOf3, luaNumberValueOf4);
        LuaRuntime luaRuntime4 = this.rt;
        LuaNumber luaNumberValueOf5 = LuaValue.valueOf(dt);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf5, "valueOf(...)");
        luaRuntime4.fire(rs, "PreSimulation", luaNumberValueOf5);
        long t2 = System.nanoTime();
        if (this.physicsDirty) {
            rebuildPhysics();
        }
        if (this.avatarApplied != Avatar.INSTANCE.getVersion()) {
            lateAvatar();
        }
        stepScriptTracks(dt);
        stepCharacter(dt);
        long t3 = System.nanoTime();
        stepBodies(dt);
        long t4 = System.nanoTime();
        checkCharacterTouches();
        updatePrompts();
        stepBubbles(dt);
        long t5 = System.nanoTime();
        LuaRuntime luaRuntime5 = this.rt;
        LuaNumber luaNumberValueOf6 = LuaValue.valueOf(dt);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf6, "valueOf(...)");
        luaRuntime5.fire(rs, "PostSimulation", luaNumberValueOf6);
        LuaRuntime luaRuntime6 = this.rt;
        LuaNumber luaNumberValueOf7 = LuaValue.valueOf(dt);
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf7, "valueOf(...)");
        luaRuntime6.fire(rs, "Heartbeat", luaNumberValueOf7);
        long t6 = System.nanoTime();
        this.rt.step(dt);
        long t7 = System.nanoTime();
        updateCamera(dt);
        long t8 = System.nanoTime();
        long[] ts = {t0, t1, t2, t3, t4, t5, t6, t7, t8};
        int i = 0;
        for (int i2 = 8; i < i2; i2 = 8) {
            float[] fArr = this.prof;
            fArr[i] = fArr[i] + ((ts[i + 1] - ts[i]) / 1000000.0f);
            i++;
        }
        this.profN++;
        this.profT += dt;
        if (this.profT > 5.0f) {
            float n = this.profN;
            String str = String.format("play: rebuild %.1f preEvents %.1f character %.1f bodies %.1f (%d) touches %.1f postEvents %.1f scripts %.1f camera %.1f", Arrays.copyOf(new Object[]{Float.valueOf(this.prof[0] / n), Float.valueOf(this.prof[1] / n), Float.valueOf(this.prof[2] / n), Float.valueOf(this.prof[3] / n), Integer.valueOf(this.bodies.size()), Float.valueOf(this.prof[4] / n), Float.valueOf(this.prof[5] / n), Float.valueOf(this.prof[6] / n), Float.valueOf(this.prof[7] / n)}, 9));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            Log.i("MobileStudioGfx", str);
            ArraysKt.fill(this.prof, 0.0f, 0, this.prof.length);
            this.profN = 0;
            this.profT = 0.0f;
        }
    }

    private final Pair<Float, Float> zoomLimits() {
        Instance p = this.player;
        Object obj = p != null ? p.get("CameraMinZoomDistance") : null;
        Number number = obj instanceof Number ? (Number) obj : null;
        float mn = number != null ? number.floatValue() : 0.5f;
        Object obj2 = p != null ? p.get("CameraMaxZoomDistance") : null;
        Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
        float mx = number2 != null ? number2.floatValue() : 128.0f;
        return TuplesKt.to(Float.valueOf(RangesKt.coerceIn(mn, 0.5f, 400.0f)), Float.valueOf(RangesKt.coerceIn(Math.max(mn, mx), 0.5f, 400.0f)));
    }

    private final float wrapAngle(float a) {
        float d = a;
        while (d > 3.141592653589793d) {
            d -= 6.2831855f;
        }
        while (d < -3.141592653589793d) {
            d += 6.2831855f;
        }
        return d;
    }

    private final void updateCamera(float dt) {
        Instance camInst;
        float f;
        Vector3 position;
        Vector3 focus;
        Vector3 target;
        Instance camInst2;
        boolean z;
        Object obj = this.dm.getWorkspace().get("CurrentCamera");
        if (!(obj instanceof Instance)) {
            for (Instance wc : this.dm.getWorkspace().getChildren()) {
                if ("Camera".equals(wc.getClassName())) {
                    obj = wc;
                    this.dm.getWorkspace().set("CurrentCamera", wc, false);
                    break;
                }
            }
        }
        Instance camInst3 = obj instanceof Instance ? (Instance) obj : null;
        FlyCamera cam = this.studio.getCamera();
        Object obj2 = camInst3 != null ? camInst3.get("CameraType") : null;
        EnumValue enumValue = obj2 instanceof EnumValue ? (EnumValue) obj2 : null;
        int type = enumValue != null ? enumValue.getValue() : 5;
        float fExp = 1.0f;
        if (type == 6 && camInst3 != null) {
            Object obj3 = camInst3.get("CFrame");
            CFrame cFrame = obj3 instanceof CFrame ? (CFrame) obj3 : null;
            if (cFrame != null) {
                cam.setFromCFrame(cFrame);
            }
            this.firstPerson = false;
            camInst2 = camInst3;
            f = 1.0f;
        } else {
            Pair<Float, Float> pairZoomLimits = zoomLimits();
            float mn = pairZoomLimits.component1().floatValue();
            float mx = pairZoomLimits.component2().floatValue();
            Instance instance = this.player;
            Object obj4 = instance != null ? instance.get("CameraMode") : null;
            EnumValue enumValue2 = obj4 instanceof EnumValue ? (EnumValue) obj4 : null;
            boolean z2 = true;
            boolean lockFirst = enumValue2 != null && enumValue2.getValue() == 1;
            this.distTarget = lockFirst ? mn : RangesKt.coerceIn(this.distTarget, mn, mx);
            if (!(this.keyTurn == 0.0f)) {
                this.yawTarget += this.keyTurn * dt * 2.6f;
                this.lastCamInput = this.time;
            }
            if (Intrinsics.areEqual(Settings.INSTANCE.getCameraMode(), "Follow") && !this.shiftLock && !this.firstPerson) {
                camInst = camInst3;
                if (this.time - this.lastCamInput > 0.8d && this.lastDir.getMagnitude() > 0.2f && this.grounded) {
                    float desired = (float) Math.atan2(-this.lastDir.getX(), -this.lastDir.getZ());
                    float diff = wrapAngle(desired - this.yawTarget);
                    if (Math.abs(diff) < 2.2f) {
                        this.yawTarget += (1.0f - ((float) Math.exp((-0.9f) * dt))) * diff;
                    }
                }
            } else {
                camInst = camInst3;
            }
            // Official Roblox ClassicCamera: direct 1:1 yaw/pitch & subject tracking (zero floaty lag)
            this.camYaw = this.yawTarget;
            this.camPitch = this.pitchTarget;
            f = 1.0f;
            float f2 = this.camDist;
            float f3 = this.distTarget - this.camDist;
            fExp = 1.0f - ((float) Math.exp((-28.0f) * dt));
            this.camDist = Math.abs(f3) < 0.02f ? this.distTarget : (f2 + (f3 * fExp));
            if (!lockFirst && this.camDist >= f) {
                z2 = false;
            }
            this.firstPerson = z2;
            Instance instance2 = this.hrp;
            if (instance2 == null || (position = PartData.INSTANCE.cframe(instance2).getPosition()) == null) {
                position = this.pos;
            }
            Vector3 root = position;
            boolean z3 = this.isR15;
            Vector3 target2 = root.plus(new Vector3(0.0f, 1.5f, 0.0f));
            this.subjectY = target2.getY();
            cam.setYaw(this.camYaw);
            cam.setPitch(this.camPitch);
            Vector3 focus2 = new Vector3(target2.getX(), this.subjectY, target2.getZ()).plus((!this.shiftLock || this.firstPerson) ? Vector3.INSTANCE.getZERO() : cam.getRight().times(1.75f));
            float dist = this.camDist;
            if (!this.firstPerson) {
                Vector3 back = cam.getForward().unaryMinus();
                Vector3 end = focus2.plus(back.times(dist));
                Vector3 lo = new Vector3(Math.min(focus2.getX(), end.getX()), Math.min(focus2.getY(), end.getY()), Math.min(focus2.getZ(), end.getZ())).minus(new Vector3(0.5f, 0.5f, 0.5f));
                Vector3 hi = new Vector3(Math.max(focus2.getX(), end.getX()), Math.max(focus2.getY(), end.getY()), Math.max(focus2.getZ(), end.getZ())).plus(new Vector3(0.5f, 0.5f, 0.5f));
                target = target2;
                RayHit hit = Geometry.INSTANCE.raycast(query$default(this, lo, hi, false, 4, null), focus2, back, dist, new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda4
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj5) {
                        return Boolean.valueOf(PlaySession.updateCamera$lambda$133(PlaySession.this, (Instance) obj5));
                    }
                });
                focus = focus2;
                float allowed = hit != null ? RangesKt.coerceAtLeast(hit.getT() - 0.4f, 0.5f) : dist;
                this.occluded = allowed < this.occluded ? allowed : approach(this.occluded, allowed, 20.0f, dt);
                dist = Math.min(dist, this.occluded);
            } else {
                focus = focus2;
                target = target2;
                this.occluded = dist;
            }
            cam.setPos(focus.minus(cam.getForward().times(dist)));
            if (camInst != null) {
                camInst2 = camInst;
                z = false;
                camInst2.set("CFrame", cam.cframe(), false);
            } else {
                camInst2 = camInst;
                z = false;
            }
            if (camInst2 != null) {
                camInst2.set("Focus", CFrame.INSTANCE.at(target), z);
            }
        }
        Object obj5 = camInst2 != null ? camInst2.get("FieldOfView") : null;
        Number number = obj5 instanceof Number ? (Number) obj5 : null;
        if (number != null) {
            cam.setFov(RangesKt.coerceIn(number.floatValue(), f, 120.0f));
        }
    }

    static final boolean updateCamera$lambda$133(PlaySession this$0, Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        return (this$0.isCharacterPart(p) || Intrinsics.areEqual(p.get("CanCollide"), (Object) false) || PartData.INSTANCE.transparency(p) >= 0.25f) ? false : true;
    }

    private final void updatePrompts() {
        Instance host;
        Vector3 hp;
        Vector3 zero;
        Instance root = this.hrp;
        if (root == null || this.dead || this.prompts.isEmpty()) {
            this.activePrompt = null;
            return;
        }
        Vector3 p = PartData.INSTANCE.cframe(root).getPosition();
        Instance best = null;
        float bd = Float.MAX_VALUE;
        Iterator<Instance> it = this.prompts.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance pr = it.next();
            if (pr.getParent() != null && !Intrinsics.areEqual(pr.get("Enabled"), (Object) false) && (host = pr.getParent()) != null) {
                if (host.isA("BasePart")) {
                    hp = PartData.INSTANCE.cframe(host).getPosition();
                } else if (Intrinsics.areEqual(host.getClassName(), "Attachment")) {
                    Instance parent = host.getParent();
                    if (parent == null) {
                        hp = null;
                    } else {
                        CFrame cframe = PartData.INSTANCE.cframe(parent);
                        Object obj = host.get("CFrame");
                        CFrame cFrame = obj instanceof CFrame ? (CFrame) obj : null;
                        if (cFrame == null || (zero = cFrame.getPosition()) == null) {
                            zero = Vector3.INSTANCE.getZERO();
                        }
                        hp = cframe.times(zero);
                    }
                } else {
                    hp = host.isA("Model") ? this.rt.getApi().modelPivot(host).getPosition() : null;
                }
                if (hp != null) {
                    float d = hp.minus(p).getMagnitude();
                    Object obj2 = pr.get("MaxActivationDistance");
                    Number number = obj2 instanceof Number ? (Number) obj2 : null;
                    float max = number != null ? number.floatValue() : 10.0f;
                    if (d <= max && d < bd) {
                        bd = d;
                        best = pr;
                    }
                }
            }
        }
        this.activePrompt = best;
    }

    public final void requestJump() {
        this.jumpRequested = true;
    }

    public final void cameraDrag(float dx, float dy) {
        float s = Settings.INSTANCE.getPlayCameraSensitivity() * 0.0075f;
        this.yawTarget -= dx * s;
        this.pitchTarget = RangesKt.coerceIn(this.pitchTarget - (dy * s), -1.4f, 1.4f);
        this.lastCamInput = this.time;
    }

    public final void cameraZoom(float factor) {
        Pair<Float, Float> pairZoomLimits = zoomLimits();
        float mn = pairZoomLimits.component1().floatValue();
        float mx = pairZoomLimits.component2().floatValue();
        this.distTarget = RangesKt.coerceIn(this.distTarget / factor, mn, mx);
    }

    public final boolean getShiftLockAllowed() {
        if (!Settings.INSTANCE.getShiftLockSwitch()) {
            return false;
        }
        Instance instanceFindService = this.dm.findService("StarterPlayer");
        return !(instanceFindService != null ? Intrinsics.areEqual(instanceFindService.get("EnableMouseLockOption"), (Object) false) : false);
    }

    public final void toggleShiftLock() {
        if (getShiftLockAllowed()) {
            this.shiftLock = !this.shiftLock;
        } else {
            this.shiftLock = false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:11:0x004c  */
    private final RayHit aimMouse(float xPx, float yPx) {
        Object obj;
        Vector3 vector3Plus;
        LuaValue luaValue;
        float d = this.studio.getDensity();
        this.lastTouchDp = new Vector2(xPx / d, yPx / d);
        Pair<Vector3, Vector3> pairScreenRay = this.studio.getView().screenRay(xPx, yPx);
        Vector3 o = pairScreenRay.component1();
        Vector3 dir = pairScreenRay.component2();
        LuaTable m = this.mouseTable;
        if (m == null || (luaValue = m.get("TargetFilter")) == null) {
            obj = null;
        } else {
            if (!luaValue.isuserdata()) {
                luaValue = null;
            }
            if (luaValue != null) {
                obj = luaValue.touserdata();
            } else {
                obj = null;
            }
        }
        final Instance filter = obj instanceof Instance ? (Instance) obj : null;
        RayHit hit = Geometry.INSTANCE.raycast(this.studio.renderParts(), o, dir, 2000.0f, new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return Boolean.valueOf(PlaySession.aimMouse$lambda$137(PlaySession.this, filter, (Instance) obj2));
            }
        });
        if (m != null) {
            if (hit == null || (vector3Plus = hit.getPosition()) == null) {
                vector3Plus = o.plus(dir.times(1000.0f));
            }
            Vector3 hitPos = vector3Plus;
            m.set("Hit", LuaTypes.INSTANCE.wrap(CFrame.Companion.lookAt$default(CFrame.INSTANCE, o, hitPos, null, 4, null).withPosition(hitPos)));
            m.set("Target", this.rt.wrap(hit != null ? hit.getPart() : null));
            m.set("TargetSurface", LuaTypes.INSTANCE.wrap(new EnumItemV("NormalId", "Top", 1)));
            m.set("X", LuaValue.valueOf(xPx / d));
            m.set("Y", LuaValue.valueOf(yPx / d));
            m.set("UnitRay", LuaTypes.INSTANCE.wrap(new Ray(o, dir)));
            m.set("Origin", LuaTypes.INSTANCE.wrap(CFrame.Companion.lookAt$default(CFrame.INSTANCE, o, o.plus(dir), null, 4, null)));
            m.set("ViewSizeX", LuaValue.valueOf(this.studio.viewportSizeDp().getX()));
            m.set("ViewSizeY", LuaValue.valueOf(this.studio.viewportSizeDp().getY()));
        }
        return hit;
    }

    static final boolean aimMouse$lambda$137(PlaySession this$0, Instance $filter, Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return (this$0.isCharacterPart(it) || Intrinsics.areEqual(it.get("Transparency"), Float.valueOf(1.0f)) || ($filter != null && (it == $filter || it.isDescendantOf($filter)))) ? false : true;
    }

    static /* synthetic */ LuaTable touchInput$default(PlaySession playSession, float f, float f2, String str, int i, Vector2 vector2, int i2, Object obj) {
        if ((i2 & 16) != 0) {
            vector2 = new Vector2(0.0f, 0.0f);
        }
        return playSession.touchInput(f, f2, str, i, vector2);
    }

    private final LuaTable touchInput(float xPx, float yPx, String state, int stateValue, Vector2 delta) {
        float d = this.studio.getDensity();
        LuaTable luaTable = new LuaTable();
        luaTable.set("UserInputType", LuaTypes.INSTANCE.wrap(new EnumItemV("UserInputType", "Touch", 7)));
        luaTable.set("UserInputState", LuaTypes.INSTANCE.wrap(new EnumItemV("UserInputState", state, stateValue)));
        luaTable.set("KeyCode", LuaTypes.INSTANCE.wrap(new EnumItemV("KeyCode", "Unknown", 0)));
        luaTable.set("Position", LuaTypes.INSTANCE.wrap(new Vector3(xPx / d, yPx / d, 0.0f)));
        luaTable.set("Delta", LuaTypes.INSTANCE.wrap(new Vector3(delta.getX() / d, delta.getY() / d, 0.0f)));
        return luaTable;
    }

    private final Instance getUisService() {
        Instance service = this.dm.getService("UserInputService");
        service.setArchivable(false);
        return service;
    }

    public final void pointerDown(float xPx, float yPx) {
        aimMouse(xPx, yPx);
        this.lastPointer = new Vector2(xPx, yPx);
        LuaTable luaTable = this.mouseTable;
        if (luaTable != null) {
            this.rt.fire(luaTable, "Button1Down", new LuaValue[0]);
        }
        LuaRuntime luaRuntime = this.rt;
        Instance uisService = getUisService();
        LuaBoolean FALSE = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
        luaRuntime.fire(uisService, "InputBegan", touchInput$default(this, xPx, yPx, "Begin", 0, null, 16, null), FALSE);
        LuaRuntime luaRuntime2 = this.rt;
        Instance uisService2 = getUisService();
        LuaBoolean FALSE2 = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE2, "FALSE");
        luaRuntime2.fire(uisService2, "TouchStarted", touchInput$default(this, xPx, yPx, "Begin", 0, null, 16, null), FALSE2);
    }

    public final void pointerMove(float xPx, float yPx) {
        aimMouse(xPx, yPx);
        Vector2 last = this.lastPointer;
        if (last == null) {
            last = new Vector2(xPx, yPx);
        }
        this.lastPointer = new Vector2(xPx, yPx);
        LuaTable luaTable = this.mouseTable;
        if (luaTable != null) {
            this.rt.fire(luaTable, "Move", new LuaValue[0]);
        }
        LuaTable inp = touchInput(xPx, yPx, "Change", 1, new Vector2(xPx - last.getX(), yPx - last.getY()));
        LuaRuntime luaRuntime = this.rt;
        Instance uisService = getUisService();
        LuaBoolean FALSE = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
        luaRuntime.fire(uisService, "InputChanged", inp, FALSE);
        LuaRuntime luaRuntime2 = this.rt;
        Instance uisService2 = getUisService();
        LuaBoolean FALSE2 = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE2, "FALSE");
        luaRuntime2.fire(uisService2, "TouchMoved", inp, FALSE2);
    }

    public final void pointerUp(float xPx, float yPx) {
        aimMouse(xPx, yPx);
        this.lastPointer = null;
        LuaTable luaTable = this.mouseTable;
        if (luaTable != null) {
            this.rt.fire(luaTable, "Button1Up", new LuaValue[0]);
        }
        LuaRuntime luaRuntime = this.rt;
        Instance uisService = getUisService();
        LuaBoolean FALSE = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
        luaRuntime.fire(uisService, "InputEnded", touchInput$default(this, xPx, yPx, "End", 2, null, 16, null), FALSE);
        LuaRuntime luaRuntime2 = this.rt;
        Instance uisService2 = getUisService();
        LuaBoolean FALSE2 = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE2, "FALSE");
        luaRuntime2.fire(uisService2, "TouchEnded", touchInput$default(this, xPx, yPx, "End", 2, null, 16, null), FALSE2);
    }

    public final void tap(float xPx, float yPx) {
        int i;
        Vector3 position;
        float d = this.studio.getDensity();
        Vector3 o = this.studio.getView().screenRay(xPx, yPx).component1();
        RayHit hit = aimMouse(xPx, yPx);
        Instance uis = getUisService();
        LuaRuntime luaRuntime = this.rt;
        LuaTable luaTable = new LuaTable();
        luaTable.set(1, LuaTypes.INSTANCE.wrap(new Vector2(xPx / d, yPx / d)));
        Unit unit = Unit.INSTANCE;
        LuaBoolean FALSE = LuaValue.FALSE;
        Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
        luaRuntime.fire(uis, "TouchTap", luaTable, FALSE);
        if (hit == null) {
            i = 0;
        } else {
            LuaRuntime luaRuntime2 = this.rt;
            i = 0;
            LuaBoolean FALSE2 = LuaValue.FALSE;
            Intrinsics.checkNotNullExpressionValue(FALSE2, "FALSE");
            luaRuntime2.fire(uis, "TouchTapInWorld", LuaTypes.INSTANCE.wrap(new Vector2(xPx / d, yPx / d)), FALSE2);
            for (Instance part = hit.getPart(); part != null && part != this.dm.getWorkspace(); part = part.getParent()) {
                Instance instanceFindFirstChildOfClass = part.findFirstChildOfClass("ClickDetector");
                if (instanceFindFirstChildOfClass != null) {
                    Object obj = instanceFindFirstChildOfClass.get("MaxActivationDistance");
                    Number number = obj instanceof Number ? (Number) obj : null;
                    float fFloatValue = number != null ? number.floatValue() : 32.0f;
                    Instance instance = this.hrp;
                    if (instance == null || (position = PartData.INSTANCE.cframe(instance).getPosition()) == null) {
                        position = o;
                    }
                    if (hit.getPosition().minus(position).getMagnitude() > fFloatValue) {
                        break;
                    }
                    this.rt.fire(instanceFindFirstChildOfClass, "MouseClick", this.rt.wrap(this.player));
                    break;
                }
            }
        }
        Instance instance2 = this.equipped;
        if (instance2 != null) {
            this.rt.fire(instance2, "Activated", new LuaValue[i]);
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$ArrayArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public final void triggerPrompt() {
        Instance pr = this.activePrompt;
        if (pr == null) {
            return;
        }
        this.rt.fire(pr, "Triggered", this.rt.wrap(this.player));
        LuaRuntime luaRuntime = this.rt;
        Instance service = this.dm.getService("ProximityPromptService");
        service.setArchivable(false);
        luaRuntime.fire(service, "PromptTriggered", this.rt.wrap(pr), this.rt.wrap(this.player));
        this.rt.fire(pr, "TriggerEnded", this.rt.wrap(this.player));
    }

    public final LuaValue mouseValue() {
        LuaTable luaTable = this.mouseTable;
        if (luaTable != null) {
            return luaTable;
        }
        final LuaTable m = new LuaTable();
        m.set("Hit", LuaTypes.INSTANCE.wrap(CFrame.INSTANCE.getIDENTITY()));
        m.set("Target", LuaValue.NIL);
        m.set("X", 0);
        m.set("Y", 0);
        m.set("ViewSizeX", LuaValue.valueOf(this.studio.viewportSizeDp().getX()));
        m.set("ViewSizeY", LuaValue.valueOf(this.studio.viewportSizeDp().getY()));
        m.set("Icon", "");
        m.set("TargetFilter", LuaValue.NIL);
        m.set("Origin", LuaTypes.INSTANCE.wrap(this.studio.getCamera().cframe()));
        m.set("UnitRay", LuaTypes.INSTANCE.wrap(new Ray(this.studio.getCamera().getPos(), this.studio.getCamera().getForward())));
        m.set("ClassName", "PlayerMouse");
        m.set("Name", "Instance");
        LuaTable mt = new LuaTable();
        mt.set("__type", "Instance");
        mt.set(LuaValue.INDEX, LuaTypesKt.fn2(new Function2() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return PlaySession.mouseValue$lambda$151(PlaySession.this, m, (LuaValue) obj, (LuaValue) obj2);
            }
        }));
        m.setmetatable(mt);
        this.mouseTable = m;
        return m;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0029, code lost:
    
        if (r4.equals("Button1Up") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0033, code lost:
    
        if (r4.equals("WheelBackward") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x003d, code lost:
    
        if (r4.equals("KeyDown") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0047, code lost:
    
        if (r4.equals("WheelForward") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0051, code lost:
    
        if (r4.equals("KeyUp") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x005b, code lost:
    
        if (r4.equals("Move") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0064, code lost:
    
        if (r4.equals("Idle") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0081, code lost:
    
        if (r4.equals("Button2Down") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008a, code lost:
    
        if (r4.equals("Button1Down") != false) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x001f, code lost:
    
        if (r4.equals("Button2Up") != false) goto L39;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static final org.luaj.vm2.LuaValue mouseValue$lambda$151(final com.goydevv.studrblx.engine.PlaySession r2, final org.luaj.vm2.LuaTable r3, org.luaj.vm2.LuaValue r4, org.luaj.vm2.LuaValue r5) {
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
            case "Changed":
                return r2.rt.getApi().signal(r3, key);
            case "IsA":
                return LuaTypesKt.fnv(new Function1() {
                    @Override
                    public final Object invoke(Object obj) {
                        return PlaySession.mouseValue$lambda$151$lambda$149((Varargs) obj);
                    }
                });
            case "GetPropertyChangedSignal":
                return LuaTypesKt.fnv(new Function1() {
                    @Override
                    public final Object invoke(Object obj) {
                        return PlaySession.mouseValue$lambda$151$lambda$150(r2, r3, (Varargs) obj);
                    }
                });
            default:
                LuaValue raw = r3.rawget(r5);
                return raw != null ? raw : LuaValue.NIL;
        }
    }

    static final Varargs mouseValue$lambda$151$lambda$149(Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        LuaBoolean luaBooleanValueOf = LuaValue.valueOf(SetsKt.setOf(new String[]{"Mouse", "PlayerMouse", "Instance"}).contains(a.arg(2).tojstring()));
        Intrinsics.checkNotNullExpressionValue(luaBooleanValueOf, "valueOf(...)");
        return luaBooleanValueOf;
    }

    static final Varargs mouseValue$lambda$151$lambda$150(PlaySession this$0, LuaTable $m, Varargs a) {
        Intrinsics.checkNotNullParameter(a, "a");
        return this$0.rt.getApi().signal($m, "Changed:" + a.arg(2).tojstring());
    }

    public final int nameColor(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        int[] colors = {-186045, -16669953, -16599209, -9751940, -2456255, -668368, -1525048, -2636390};
        int value = 0;
        int length = name.length();
        for (int i = 0; i < length; i++) {
            int i2 = i;
            char ch = name.charAt(i);
            int c = ch;
            int rev = name.length() - i2;
            if (name.length() % 2 == 1) {
                rev--;
            }
            if (rev % 4 >= 2) {
                c = -c;
            }
            value += c;
        }
        return colors[Math.floorMod(value, colors.length)];
    }

    private final void addLine(ChatLine l) {
        this.chatLines.add(l);
        while (this.chatLines.size() > 200) {
            this.chatLines.remove(0);
        }
        this.chatVersion++;
    }

    public static /* synthetic */ void systemMessage$default(PlaySession playSession, String str, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = -1644826;
        }
        playSession.systemMessage(str, i);
    }

    public final void systemMessage(String text, int color) {
        Intrinsics.checkNotNullParameter(text, "text");
        addLine(new ChatLine(null, text, color, true, 0L, 16, null));
    }

    /* JADX WARN: Code duplicated, block: B:51:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:53:0x00ec  */
    /* JADX WARN: Code duplicated, block: B:54:0x00ee  */
    /* JADX WARN: Code duplicated, block: B:81:0x0168  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
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
    public final void sendChat(String raw) {
        Instance instance;
        String name;
        Instance instanceFindFirstChild$default;
        Instance instanceFindFirstChild$default2;
        Intrinsics.checkNotNullParameter(raw, "raw");
        String string = StringsKt.trim((CharSequence) raw).toString();
        if ((string.length() == 0) == true || (instance = this.player) == null) {
            return;
        }
        Instance instanceFindFirstChild$default3 = null;
        String str = null;
        instanceFindFirstChild$default3 = null;
        if (StringsKt.startsWith(string, "/", false)) {
            String strSubstring = string.substring(1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            String lowerCase = StringsKt.trim((CharSequence) strSubstring).toString().toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            if (Intrinsics.areEqual(lowerCase, "?") || Intrinsics.areEqual(lowerCase, "help")) {
                Iterator it = CollectionsKt.listOf(new String[]{"/e <emote> — wave, point, cheer, dance", "/clear — clear the chat", "/shiftlock — toggle shift lock"}).iterator();
                while (it.hasNext()) {
                    systemMessage((String) it.next(), -4934476);
                }
                return;
            }
            if (Intrinsics.areEqual(lowerCase, "clear") || Intrinsics.areEqual(lowerCase, "cls")) {
                this.chatLines.clear();
                this.chatVersion++;
                return;
            }
            if (Intrinsics.areEqual(lowerCase, "shiftlock")) {
                toggleShiftLock();
                return;
            }
            if (StringsKt.startsWith(lowerCase, "e ", false) || StringsKt.startsWith(lowerCase, "emote ", false)) {
                String string2 = StringsKt.trim((CharSequence) com.goydevv.studrblx.util.KtCompat.substringAfter(lowerCase, ' ')).toString();
                if (CollectionsKt.listOf(new String[]{"wave", "point", "cheer", "dance", "dance2", "dance3", "laugh"}).contains(string2)) {
                    str = string2;
                }
                this.emote = str;
                this.emoteTime = 0.0f;
                if (this.emote == null) {
                    systemMessage("You can't use that emote.", -4934476);
                }
                LuaRuntime luaRuntime = this.rt;
                LuaString luaStringValueOf = LuaValue.valueOf(string);
                Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
                luaRuntime.fire(instance, "Chatted", luaStringValueOf);
                return;
            }
        }
        if (TextChat.runCommand(this, string)) {
            return;
        }
        TextChat.send(this, TextChat.channel(this.dm, "RBXGeneral"), string, "");
    }

    public final void addChatLine(String from, String text, int color, boolean system) {
        addLine(new ChatLine(from, text, color, system, 0L, 16, null));
    }

    static final boolean bubble$lambda$157(Instance $part, PlaySession this$0, Bubble it) {
        int i;
        Intrinsics.checkNotNullParameter(it, "it");
        if (it.getPart() != $part) {
            return false;
        }
        Iterable iterable = this$0.bubbles;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            i = 0;
        } else {
            i = 0;
            Iterator it2 = iterable.iterator();
            while (it2.hasNext()) {
                if ((((Bubble) it2.next()).getPart() == $part) && (i = i + 1) < 0) {
                    CollectionsKt.throwCountOverflow();
                }
            }
        }
        return i >= 3;
    }

    public final void bubble(final Instance part, String text) {
        Intrinsics.checkNotNullParameter(part, "part");
        Intrinsics.checkNotNullParameter(text, "text");
        CollectionsKt.removeAll((List) this.bubbles, new Function1() { // from class: com.goydevv.studrblx.engine.PlaySession$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(PlaySession.bubble$lambda$157(part, PlaySession.this, (PlaySession.Bubble) obj));
            }
        });
        this.bubbles.add(new Bubble(part, StringsKt.take(text, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION), 0.0f, 4, null));
    }

    public final Bubble lastBubble() {
        return this.bubbles.isEmpty() ? null : this.bubbles.get(this.bubbles.size() - 1);
    }

    private final void stepBubbles(float dt) {
        if (this.bubbles.isEmpty()) {
            return;
        }
        Iterator<Bubble> it = this.bubbles.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Bubble next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Bubble b = next;
            b.setAge(b.getAge() + dt);
            if (b.getAge() > 12.0f || b.getPart().getParent() == null) {
                it.remove();
            }
        }
    }

    public final List<Pair<String, String>> leaderstats() {
        Instance ls;
        String string;
        Instance instance = this.player;
        if (instance == null || (ls = Instance.findFirstChild$default(instance, "leaderstats", false, 2, null)) == null) {
            return CollectionsKt.emptyList();
        }
        Iterable children = ls.getChildren();
        Collection arrayList = new ArrayList();
        for (Object obj : children) {
            if (((Instance) obj).isA("ValueBase")) {
                arrayList.add(obj);
            }
        }
        Iterable<Instance> iterableTake = CollectionsKt.take((List) arrayList, 4);
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableTake, 10));
        for (Instance instance2 : iterableTake) {
            Object obj2 = instance2.get("Value");
            if (obj2 instanceof Double) {
                string = (!Intrinsics.areEqual((Double) obj2, Math.floor(((Number) obj2).doubleValue())) || Math.abs(((Number) obj2).doubleValue()) >= 1.0E15d) ? DataTypesKt.fmtNum((float) ((Number) obj2).doubleValue()) : String.valueOf((long) ((Number) obj2).doubleValue());
            } else if (obj2 instanceof Float) {
                string = DataTypesKt.fmtNum(((Number) obj2).floatValue());
            } else if (obj2 instanceof Number) {
                string = String.valueOf(((Number) obj2).longValue());
            } else if (obj2 instanceof Boolean) {
                string = ((Boolean) obj2).booleanValue() ? "true" : "false";
            } else if (obj2 instanceof Instance) {
                string = ((Instance) obj2).getName();
            } else {
                string = obj2 == null ? "-" : obj2.toString();
            }
            arrayList2.add(TuplesKt.to(instance2.getName(), string));
        }
        return (List) arrayList2;
    }

    public final List<Instance> backpackTools() {
        Instance bp;
        Instance instance = this.player;
        if (instance == null || (bp = Instance.findFirstChild$default(instance, "Backpack", false, 2, null)) == null) {
            return CollectionsKt.emptyList();
        }
        Iterable children = bp.getChildren();
        Collection arrayList = new ArrayList();
        for (Object obj : children) {
            Instance instance2 = (Instance) obj;
            if (Intrinsics.areEqual(instance2.getClassName(), "Tool") || Intrinsics.areEqual(instance2.getClassName(), "HopperBin")) {
                arrayList.add(obj);
            }
        }
        List<Instance> mutableList = CollectionsKt.toMutableList(arrayList);
        Instance instance3 = this.equipped;
        if (instance3 != null && !mutableList.contains(instance3)) {
            mutableList.add(0, instance3);
        }
        return mutableList;
    }

    public final void equip(Instance tool) {
        Intrinsics.checkNotNullParameter(tool, "tool");
        Instance ch = this.character;
        if (ch == null || this.equipped == tool) {
            return;
        }
        unequip();
        Instance.setParent$default(tool, ch, 0, 2, null);
        this.equipped = tool;
        this.toolOffsets.clear();
        Instance handle = Instance.findFirstChild$default(tool, "Handle", false, 2, null);
        if (handle != null) {
            CFrame hcf = PartData.INSTANCE.cframe(handle);
            for (Instance p : Instance.descendants$default(tool, null, 1, null)) {
                if (p.isA("BasePart")) {
                    this.toolOffsets.put(p, hcf.inverse().times(PartData.INSTANCE.cframe(p)));
                }
            }
        }
        this.physicsDirty = true;
        this.rt.fire(tool, "Equipped", mouseValue());
    }

    public final void unequip() {
        Instance t = this.equipped;
        if (t == null) {
            return;
        }
        this.equipped = null;
        Instance instance = this.player;
        Instance.setParent$default(t, instance != null ? Instance.findFirstChild$default(instance, "Backpack", false, 2, null) : null, 0, 2, null);
        this.physicsDirty = true;
        this.rt.fire(t, "Unequipped", new LuaValue[0]);
    }

    public final void toggleTool(Instance t) {
        Intrinsics.checkNotNullParameter(t, "t");
        if (this.equipped == t) {
            unequip();
        } else {
            equip(t);
        }
    }

    public final void bindAction(String name, LuaValue fn, boolean touchButton) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(fn, "fn");
        if (touchButton) {
            this.actions.put(name, new ActionButton(name, fn, name));
        }
    }

    public final void unbindAction(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.actions.remove(name);
    }

    public final void unbindAll() {
        this.actions.clear();
    }

    public final void setActionTitle(String name, String title) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(title, "title");
        ActionButton actionButton = this.actions.get(name);
        if (actionButton != null) {
            actionButton.setTitle(title);
        }
    }

    public final void pressAction(String name, boolean begin) {
        Intrinsics.checkNotNullParameter(name, "name");
        ActionButton a = this.actions.get(name);
        if (a == null) {
            return;
        }
        EnumItemV state = begin ? new EnumItemV("UserInputState", "Begin", 0) : new EnumItemV("UserInputState", "End", 2);
        LuaTable input = new LuaTable();
        input.set("UserInputType", LuaTypes.INSTANCE.wrap(new EnumItemV("UserInputType", "Touch", 7)));
        input.set("UserInputState", LuaTypes.INSTANCE.wrap(state));
        input.set("KeyCode", LuaTypes.INSTANCE.wrap(new EnumItemV("KeyCode", "Unknown", 0)));
        input.set("Position", LuaTypes.INSTANCE.wrap(Vector3.INSTANCE.getZERO()));
        LuaRuntime luaRuntime = this.rt;
        LuaValue fn = a.getFn();
        Varargs varargsVarargsOf = LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf(name), LuaTypes.INSTANCE.wrap(state), input});
        Intrinsics.checkNotNullExpressionValue(varargsVarargsOf, "varargsOf(...)");
        luaRuntime.spawn(fn, varargsVarargsOf);
    }

    public final Vector3 velocityOf(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        if (isCharacterPart(p)) {
            return this.vel;
        }
        Body body = this.bodyOf.get(p);
        if (body != null) {
            return body.getVel();
        }
        return null;
    }

    public final void setVelocity(Instance p, Vector3 v) {
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(v, "v");
        if (isCharacterPart(p)) {
            this.vel = v;
            if (v.getY() > 0.0f) {
                this.grounded = false;
                this.climbing = false;
                return;
            }
            return;
        }
        Body body = this.bodyOf.get(p);
        if (body != null) {
            body.setVel(v);
            body.setSleeping(false);
            body.setRestTime(0.0f);
        }
    }

    public final Vector3 moveDirection(Instance h) {
        Intrinsics.checkNotNullParameter(h, "h");
        return h == this.humanoid ? this.lastDir : Vector3.INSTANCE.getZERO();
    }

    public final Instance seatOf(Instance h) {
        return h == this.humanoid ? this.seated : null;
    }

    public final boolean isGrounded(Instance h) {
        Intrinsics.checkNotNullParameter(h, "h");
        return h == this.humanoid && this.grounded;
    }

    public final void jump(Instance h) {
        Intrinsics.checkNotNullParameter(h, "h");
        if (h == this.humanoid) {
            this.jumpRequested = true;
        }
    }

    public final void moveTo(Instance h, Vector3 target) {
        Intrinsics.checkNotNullParameter(h, "h");
        Intrinsics.checkNotNullParameter(target, "target");
        if (h == this.humanoid) {
            this.moveToTarget = target;
        }
    }

    public final void scriptMove(Instance h, Vector3 dir) {
        Intrinsics.checkNotNullParameter(h, "h");
        Intrinsics.checkNotNullParameter(dir, "dir");
        if (h == this.humanoid) {
            this.scriptMoveDir = dir;
        }
    }

    /* JADX INFO: renamed from: isDead, reason: from getter */
    public final boolean getDead() {
        return this.dead;
    }

    public final float max3(float a, float b, float c) {
        return Math.max(a, Math.max(b, c));
    }
}
