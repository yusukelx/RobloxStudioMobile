package com.goydevv.studrblx.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.goydevv.studrblx.audio.AudioEngine;
import com.goydevv.studrblx.engine.Geometry;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.engine.RayHit;
import com.goydevv.studrblx.format.RbxBinaryWriter;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.lua.LuaTypes;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.ClassDb;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Ray;
import com.goydevv.studrblx.model.TreeListener;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.CloudPlace;
import com.goydevv.studrblx.render.FlyCamera;
import com.goydevv.studrblx.render.ViewState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/* JADX INFO: compiled from: Studio.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Studio {
    public static final String PLAY_AS_TAG = "__MobileStudioPlayAs";
    private final Set<String> ADORNMENTS;
    private final Set<String> RENDER_CLASSES;
    private volatile Instance activePlugin;
    private boolean activePluginExclusive;
    private LuaRuntime activePluginRt;
    private final ArrayList<Instance> adornCache;
    private final Context app;
    private boolean boxSelect;
    private Vector3 camVel;
    private final FlyCamera camera;
    private List<? extends Instance> clipboard;
    private final ArrayList<Instance> clothingCache;
    private CloudPlace cloud;
    private final ArrayList<Instance> constraintsCache;
    private final HashMap<String, HashMap<String, String>> dataStores;
    private final ArrayList<Instance> decalsCache;
    private final float density;
    private boolean dirty;
    private Triple<Vector3, Float, Float> editCamSaved;
    private DataModel editModel;
    private LuaRuntime editRuntime;
    private boolean fileIsXml;
    private String fileName;
    private Uri fileUri;
    private volatile boolean flyDown;
    private volatile boolean flyUp;
    private final ConcurrentLinkedQueue<Function0<Unit>> glJobs;
    private volatile Vector2 lastEditPointer;
    private long lastJobEnd;
    private final CopyOnWriteArrayList<Listener> listeners;
    private final Object lock;
    private final Handler main;
    private volatile boolean modifierHeld;
    private volatile float moveX;
    private volatile float moveY;
    private boolean multiSelect;
    private Function1<? super Instance, Unit> openScriptHandler;
    private final ArrayList<Instance> partsCache;
    private volatile boolean partsDirty;
    private float pendingPitch;
    private float pendingYaw;
    private PlaySession play;
    private volatile Vector3 playHereAt;
    private DataModel playModel;
    private final CopyOnWriteArrayList<PluginButton> pluginButtons;
    private Function3<? super String, ? super List<String>, ? super Function1<? super Integer, Unit>, Unit> pluginMenuHandler;
    private final IdentityHashMap<Instance, LuaTable> pluginMice;
    private final CopyOnWriteArrayList<PluginWidget> pluginWidgets;
    private final PluginManager plugins;

    /* JADX INFO: renamed from: prefs$delegate, reason: from kotlin metadata */
    private final Lazy prefs;
    private boolean propsPosted;
    private int renderListVersion;
    private final ArrayList<Instance> selection;
    private volatile boolean shadowsEnabled;
    private final ArrayList<Instance> soundsCache;
    private final ArrayList<Instance> lightsCache;
    private final ArrayList<Instance> beamsCache;
    private final ArrayList<Instance> trailsCache;
    private final ArrayList<Instance> particlesCache;
    private final ArrayList<Instance> surfaceGuisCache;
    private Function1<? super String, Unit> toastHandler;
    private Tool tool;
    private final TreeListener treeListener;
    private boolean treePosted;
    private final UndoManager undo;
    private final ViewState view;

    /* JADX WARN: Type inference failed for: r2v27, types: [com.goydevv.studrblx.core.Studio$treeListener$1] */
    public Studio(Context app) {
        Intrinsics.checkNotNullParameter(app, "app");
        this.app = app;
        this.lock = new Object();
        this.main = new Handler(Looper.getMainLooper());
        this.density = this.app.getResources().getDisplayMetrics().density;
        this.editModel = PlaceFactory.newPlace$default(PlaceFactory.INSTANCE, null, 1, null);
        this.selection = new ArrayList<>();
        this.tool = Tool.MOVE;
        this.camera = new FlyCamera();
        this.view = new ViewState();
        this.undo = new UndoManager(this);
        this.fileName = "Place1.rbxl";
        this.dataStores = new HashMap<>();
        this.pluginButtons = new CopyOnWriteArrayList<>();
        this.pluginWidgets = new CopyOnWriteArrayList<>();
        this.plugins = new PluginManager(this);
        this.clipboard = CollectionsKt.emptyList();
        this.camVel = Vector3.INSTANCE.getZERO();
        this.listeners = new CopyOnWriteArrayList<>();
        this.partsDirty = true;
        this.partsCache = new ArrayList<>();
        this.RENDER_CLASSES = SetsKt.setOf(new String[]{"Decal", "Texture", "SurfaceGui", "Shirt", "Pants", "ShirtGraphic", "Sound", "SelectionBox", "Highlight", "BoxHandleAdornment", "SelectionSphere", "Handles", "ArcHandles", "RopeConstraint", "RodConstraint", "SpringConstraint"});
        this.treeListener = new TreeListener() { // from class: com.goydevv.studrblx.core.Studio$treeListener$1
            @Override // com.goydevv.studrblx.model.TreeListener
            public void childAdded(Instance parent, Instance child) {
                Intrinsics.checkNotNullParameter(parent, "parent");
                Intrinsics.checkNotNullParameter(child, "child");
                if (Studio.this.renderRelevant(child)) {
                    Studio.this.partsDirty = true;
                }
                Studio.this.postTree();
            }

            @Override // com.goydevv.studrblx.model.TreeListener
            public void childRemoved(Instance parent, Instance child) {
                Intrinsics.checkNotNullParameter(parent, "parent");
                Intrinsics.checkNotNullParameter(child, "child");
                if (Studio.this.renderRelevant(child)) {
                    Studio.this.partsDirty = true;
                }
                Studio.this.postTree();
            }

            @Override // com.goydevv.studrblx.model.TreeListener
            public void propChanged(Instance inst, String name) {
                Intrinsics.checkNotNullParameter(inst, "inst");
                Intrinsics.checkNotNullParameter(name, "name");
                if (Intrinsics.areEqual(name, "Name")) {
                    Studio.this.postTree();
                }
                if (Studio.this.getSelection().isEmpty()) {
                    return;
                }
                Iterable selection = Studio.this.getSelection();
                boolean z = false;
                if (!(selection instanceof Collection) || !((Collection) selection).isEmpty()) {
                    Iterator it = selection.iterator();
                    while (it.hasNext()) {
                        if (((Instance) it.next()) == inst) {
                            z = true;
                            break;
                        }
                    }
                }
                if (z) {
                    Studio.this.postProps();
                }
            }
        };
        this.editModel.getListeners().add(this.treeListener);
        this.editRuntime = new LuaRuntime(this, this.editModel, false);
        resetCameraFromModel();
        this.decalsCache = new ArrayList<>();
        this.constraintsCache = new ArrayList<>();
        this.surfaceGuisCache = new ArrayList<>();
        this.clothingCache = new ArrayList<>();
        this.adornCache = new ArrayList<>();
        this.soundsCache = new ArrayList<>();
        this.lightsCache = new ArrayList<>();
        this.beamsCache = new ArrayList<>();
        this.trailsCache = new ArrayList<>();
        this.particlesCache = new ArrayList<>();
        this.ADORNMENTS = SetsKt.setOf(new String[]{"SelectionBox", "Highlight", "BoxHandleAdornment", "SelectionSphere", "Handles", "ArcHandles"});
        this.shadowsEnabled = true;
        this.glJobs = new ConcurrentLinkedQueue<>();
        this.pluginMice = new IdentityHashMap<>();
        this.prefs = LazyKt.lazy(new Function0() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return Studio.this.app.getSharedPreferences("plugin_settings", 0);
            }
        });
    }

    public final Context getApp() {
        return this.app;
    }

    public final Object getLock() {
        return this.lock;
    }

    public final Handler getMain() {
        return this.main;
    }

    public final float getDensity() {
        return this.density;
    }

    public final DataModel getEditModel() {
        return this.editModel;
    }

    public final DataModel getPlayModel() {
        return this.playModel;
    }

    public final DataModel getModel() {
        DataModel dataModel = this.playModel;
        return dataModel == null ? this.editModel : dataModel;
    }

    public final Instance editCounterpart(Instance inst) {
        if (this.playModel == null) return null;
        ArrayList<Instance> chain = new ArrayList<>();
        Instance top = inst;
        while (top.getParent() != null) {
            chain.add(top);
            top = top.getParent();
        }
        Instance cur = top == this.playModel ? this.editModel : (top.getIsService() ? this.editModel.findService(top.getClassName()) : null);
        if (cur == null || chain.isEmpty()) return null;
        for (int i = chain.size() - 1; i >= 0 && cur != null; i--) {
            Instance step = chain.get(i);
            int ordinal = 0;
            for (Instance sib : step.getParent().getChildren()) {
                if (sib == step) break;
                if (sib.getName().equals(step.getName()) && sib.getClassName().equals(step.getClassName())) ordinal++;
            }
            Instance next = null;
            int seen = 0;
            for (Instance ch : cur.getChildren()) {
                if (ch.getName().equals(step.getName()) && ch.getClassName().equals(step.getClassName()) && seen++ == ordinal) {
                    next = ch;
                    break;
                }
            }
            cur = next;
        }
        return cur;
    }

    public final boolean isPlaying() {
        return this.playModel != null;
    }

    public final PlaySession getPlay() {
        return this.play;
    }

    public final LuaRuntime getEditRuntime() {
        return this.editRuntime;
    }

    public final ArrayList<Instance> getSelection() {
        return this.selection;
    }

    public final Tool getTool() {
        return this.tool;
    }

    public final void setTool(Tool v) {
        Intrinsics.checkNotNullParameter(v, "v");
        this.tool = v;
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onToolChanged();
        }
    }

    public final boolean getMultiSelect() {
        return this.multiSelect;
    }

    public final void setMultiSelect(boolean v) {
        this.multiSelect = v;
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onToolChanged();
        }
    }

    public final boolean getBoxSelect() {
        return this.boxSelect;
    }

    public final void setBoxSelect(boolean v) {
        this.boxSelect = v;
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onToolChanged();
        }
    }

    public final boolean getSelectInsideModels() {
        return Settings.INSTANCE.getSelectInsideModels();
    }

    public final void setSelectInsideModels(boolean v) {
        Settings.INSTANCE.setSelectInsideModels(v);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onToolChanged();
        }
    }

    public final boolean getModifierHeld() {
        return this.modifierHeld;
    }

    public final void setModifierHeld(boolean z) {
        this.modifierHeld = z;
    }

    public final FlyCamera getCamera() {
        return this.camera;
    }

    public final ViewState getView() {
        return this.view;
    }

    public final UndoManager getUndo() {
        return this.undo;
    }

    public final float getMoveSnap() {
        return Settings.INSTANCE.getMoveSnap();
    }

    public final void setMoveSnap(float v) {
        Settings.INSTANCE.setMoveSnap(v);
    }

    public final float getRotSnap() {
        return Settings.INSTANCE.getRotSnap();
    }

    public final void setRotSnap(float v) {
        Settings.INSTANCE.setRotSnap(v);
    }

    public final boolean getSnapEnabled() {
        return Settings.INSTANCE.getMoveSnapOn();
    }

    public final void setSnapEnabled(boolean v) {
        Settings.INSTANCE.setMoveSnapOn(v);
    }

    public final boolean getRotSnapEnabled() {
        return Settings.INSTANCE.getRotSnapOn();
    }

    public final void setRotSnapEnabled(boolean v) {
        Settings.INSTANCE.setRotSnapOn(v);
    }

    public final boolean getCollisionsEnabled() {
        return Settings.INSTANCE.getDragCollisions();
    }

    public final void setCollisionsEnabled(boolean v) {
        Settings.INSTANCE.setDragCollisions(v);
    }

    public final boolean getDirty() {
        return this.dirty;
    }

    public final Uri getFileUri() {
        return this.fileUri;
    }

    public final void setFileUri(Uri uri) {
        this.fileUri = uri;
    }

    public final String getFileName() {
        return this.fileName;
    }

    public final void setFileName(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.fileName = str;
    }

    public final boolean getFileIsXml() {
        return this.fileIsXml;
    }

    public final void setFileIsXml(boolean z) {
        this.fileIsXml = z;
    }

    public final CloudPlace getCloud() {
        return this.cloud;
    }

    public final void setCloud(CloudPlace cloudPlace) {
        this.cloud = cloudPlace;
        com.goydevv.studrblx.lua.SpecialProps.prefetch(cloudPlace);
    }

    public final HashMap<String, HashMap<String, String>> getDataStores() {
        return this.dataStores;
    }

    public final CopyOnWriteArrayList<PluginButton> getPluginButtons() {
        return this.pluginButtons;
    }

    public final CopyOnWriteArrayList<PluginWidget> getPluginWidgets() {
        return this.pluginWidgets;
    }

    public final PluginManager getPlugins() {
        return this.plugins;
    }

    public final float getMoveX() {
        return this.moveX;
    }

    public final void setMoveX(float f) {
        this.moveX = f;
    }

    public final float getMoveY() {
        return this.moveY;
    }

    public final void setMoveY(float f) {
        this.moveY = f;
    }

    public final boolean getFlyUp() {
        return this.flyUp;
    }

    public final void setFlyUp(boolean z) {
        this.flyUp = z;
    }

    public final boolean getFlyDown() {
        return this.flyDown;
    }

    public final void setFlyDown(boolean z) {
        this.flyDown = z;
    }

    public final float getFlySpeed() {
        return Settings.INSTANCE.getFlySpeed();
    }

    public final void setFlySpeed(float v) {
        Settings.INSTANCE.setFlySpeed(RangesKt.coerceIn(v, 0.05f, 200.0f));
    }

    public final void rotateEditCamera(float dYaw, float dPitch) {
        float s = Settings.INSTANCE.getCameraSensitivity();
        if (!Settings.INSTANCE.getCameraSmoothing()) {
            this.camera.rotate(dYaw * s, dPitch * s);
        } else {
            this.pendingYaw += dYaw * s;
            this.pendingPitch += dPitch * s;
        }
    }

    /* JADX INFO: compiled from: Studio.kt */
    public interface Listener {

        /* JADX INFO: compiled from: Studio.kt */
        public static final class DefaultImpls {
            @Deprecated
            public static void onTreeChanged(Listener $this) {
            }

            @Deprecated
            public static void onSelectionChanged(Listener $this) {
            }

            @Deprecated
            public static void onPropsChanged(Listener $this) {
            }

            @Deprecated
            public static void onModelReplaced(Listener $this) {
            }

            @Deprecated
            public static void onPlayStateChanged(Listener $this) {
            }

            @Deprecated
            public static void onToolChanged(Listener $this) {
            }

            @Deprecated
            public static void onPluginsChanged(Listener $this) {
            }

            @Deprecated
            public static void onDirtyChanged(Listener $this) {
            }
        }

        default void onTreeChanged() {
        }

        default void onSelectionChanged() {
        }

        default void onPropsChanged() {
        }

        default void onModelReplaced() {
        }

        default void onPlayStateChanged() {
        }

        default void onToolChanged() {
        }

        default void onPluginsChanged() {
        }

        default void onDirtyChanged() {
        }
    }

    public final CopyOnWriteArrayList<Listener> getListeners() {
        return this.listeners;
    }

    public final Function1<? super Instance, Unit> getOpenScriptHandler() {
        return this.openScriptHandler;
    }

    public final void setOpenScriptHandler(Function1<? super Instance, Unit> function1) {
        this.openScriptHandler = function1;
    }

    public final Function1<? super String, Unit> getToastHandler() {
        return this.toastHandler;
    }

    public final void setToastHandler(Function1<? super String, Unit> function1) {
        this.toastHandler = function1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean renderRelevant(Instance i) {
        if (i.isA("BasePart") || this.RENDER_CLASSES.contains(i.getClassName()) || i.getIsService()) {
            return true;
        }
        Iterator<Instance> it = i.getChildren().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Instance c = next;
            if (renderRelevant(c)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void postTree() {
        if (this.treePosted) {
            return;
        }
        this.treePosted = true;
        this.main.postDelayed(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                Studio.postTree$lambda$5(Studio.this);
            }
        }, 120L);
    }

    static final void postTree$lambda$5(Studio this$0) {
        this$0.treePosted = false;
        Iterator it = this$0.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onTreeChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void postProps() {
        if (this.propsPosted) {
            return;
        }
        this.propsPosted = true;
        this.main.postDelayed(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Studio.postProps$lambda$7(Studio.this);
            }
        }, 200L);
    }

    static final void postProps$lambda$7(Studio this$0) {
        this$0.propsPosted = false;
        Iterator it = this$0.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onPropsChanged();
        }
    }

    static final void markDirty$lambda$9(Studio this$0) {
        Iterator it = this$0.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onDirtyChanged();
        }
    }

    public final void markDirty() {
        if (!this.dirty) {
            this.dirty = true;
            this.main.post(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    Studio.markDirty$lambda$9(Studio.this);
                }
            });
        }
    }

    static final void toast$lambda$10(Studio this$0, String $msg) {
        Function1<? super String, Unit> function1 = this$0.toastHandler;
        if (function1 != null) {
            function1.invoke($msg);
        }
    }

    public final boolean toast(final String msg) {
        Intrinsics.checkNotNullParameter(msg, "msg");
        return this.main.post(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                Studio.toast$lambda$10(Studio.this, msg);
            }
        });
    }

    public final float editGuiScale() {
        float set = Settings.INSTANCE.getPluginUiScale();
        return set > 0.0f ? set : RangesKt.coerceIn((this.view.getHeight() / this.density) / 520.0f, 0.6f, 1.0f);
    }

    public final float guiUnit() {
        return this.play != null ? this.density : this.density * editGuiScale();
    }

    public final Vector2 viewportSizeDp() {
        float fGuiUnit = guiUnit();
        return new Vector2(this.view.getWidth() / fGuiUnit, this.view.getHeight() / fGuiUnit);
    }

    public final Vector2 getLastEditPointer() {
        return this.lastEditPointer;
    }

    public final void setLastEditPointer(Vector2 vector2) {
        this.lastEditPointer = vector2;
    }

    private final boolean insideOperation(Instance i) {
        for (Instance p = i.getParent(); p != null && !(p instanceof DataModel); p = p.getParent()) {
            if (p.isA("PartOperation")) {
                return true;
            }
        }
        return false;
    }

    public final int getRenderListVersion() {
        return this.renderListVersion;
    }

    public final List<Instance> renderParts() {
        Iterable<Instance> iterableDescendants$default;
        Instance player;
        Instance instanceFindFirstChild$default;
        Iterable<Instance> iterableDescendants$default2;
        Iterable<Instance> iterableDescendants$default3;
        Instance player2;
        Instance instanceFindFirstChild$default2;
        Iterable<Instance> iterableDescendants$default4;
        Iterable<Instance> iterableDescendants$default5;
        Instance player3;
        Iterable<Instance> iterableDescendants$default6;
        if (this.partsDirty) {
            this.partsDirty = false;
            this.renderListVersion++;
            this.partsCache.clear();
            this.decalsCache.clear();
            this.constraintsCache.clear();
            this.surfaceGuisCache.clear();
            this.clothingCache.clear();
            this.adornCache.clear();
            this.soundsCache.clear();
            this.lightsCache.clear();
            this.beamsCache.clear();
            this.trailsCache.clear();
            this.particlesCache.clear();
            Instance ws = getModel().findService("Workspace");
            if (ws != null && (iterableDescendants$default6 = Instance.descendants$default(ws, null, 1, null)) != null) {
                for (Instance instance : iterableDescendants$default6) {
                    if (instance.isA("BasePart")) {
                        if (!Intrinsics.areEqual(instance.getClassName(), "Terrain") && !insideOperation(instance)) {
                            this.partsCache.add(instance);
                        }
                    } else {
                        if (Intrinsics.areEqual(instance.getClassName(), "Decal") || Intrinsics.areEqual(instance.getClassName(), "Texture")) {
                            Instance parent = instance.getParent();
                            if (parent != null && parent.isA("BasePart")) {
                                this.decalsCache.add(instance);
                            }
                        }
                        if (Intrinsics.areEqual(instance.getClassName(), "SurfaceGui")) {
                            this.surfaceGuisCache.add(instance);
                        } else if (Intrinsics.areEqual(instance.getClassName(), "Shirt") || Intrinsics.areEqual(instance.getClassName(), "Pants") || Intrinsics.areEqual(instance.getClassName(), "ShirtGraphic")) {
                            this.clothingCache.add(instance);
                        } else if (this.ADORNMENTS.contains(instance.getClassName())) {
                            this.adornCache.add(instance);
                        } else if (Intrinsics.areEqual(instance.getClassName(), "Sound")) {
                            this.soundsCache.add(instance);
                        } else if (Intrinsics.areEqual(instance.getClassName(), "PointLight") || Intrinsics.areEqual(instance.getClassName(), "SpotLight") || Intrinsics.areEqual(instance.getClassName(), "SurfaceLight")) {
                            this.lightsCache.add(instance);
                        } else if (Intrinsics.areEqual(instance.getClassName(), "Beam")) {
                            this.beamsCache.add(instance);
                        } else if (Intrinsics.areEqual(instance.getClassName(), "Trail")) {
                            this.trailsCache.add(instance);
                        } else if (Intrinsics.areEqual(instance.getClassName(), "Fire") || Intrinsics.areEqual(instance.getClassName(), "Smoke") || Intrinsics.areEqual(instance.getClassName(), "Sparkles") || Intrinsics.areEqual(instance.getClassName(), "ParticleEmitter")) {
                            this.particlesCache.add(instance);
                        } else if (Intrinsics.areEqual(instance.getClassName(), "RopeConstraint") || Intrinsics.areEqual(instance.getClassName(), "RodConstraint") || Intrinsics.areEqual(instance.getClassName(), "SpringConstraint")) {
                            this.constraintsCache.add(instance);
                        }
                    }
                }
            }
            PlaySession playSession = this.play;
            Instance pg = (playSession == null || (player3 = playSession.getPlayer()) == null) ? null : Instance.findFirstChild$default(player3, "PlayerGui", false, 2, null);
            if (pg != null && (iterableDescendants$default5 = Instance.descendants$default(pg, null, 1, null)) != null) {
                for (Instance instance2 : iterableDescendants$default5) {
                    if (Intrinsics.areEqual(instance2.getClassName(), "SurfaceGui") && (instance2.get("Adornee") instanceof Instance)) {
                        this.surfaceGuisCache.add(instance2);
                    } else if (Intrinsics.areEqual(instance2.getClassName(), "Sound")) {
                        this.soundsCache.add(instance2);
                    } else if (this.ADORNMENTS.contains(instance2.getClassName())) {
                        this.adornCache.add(instance2);
                    }
                }
            }
            PlaySession playSession2 = this.play;
            if (playSession2 != null && (player2 = playSession2.getPlayer()) != null && (instanceFindFirstChild$default2 = Instance.findFirstChild$default(player2, "Backpack", false, 2, null)) != null && (iterableDescendants$default4 = Instance.descendants$default(instanceFindFirstChild$default2, null, 1, null)) != null) {
                for (Instance instance3 : iterableDescendants$default4) {
                    if (this.ADORNMENTS.contains(instance3.getClassName())) {
                        this.adornCache.add(instance3);
                    }
                }
            }
            if (this.play != null) {
                Instance instanceFindService = getModel().findService("SoundService");
                if (instanceFindService != null && (iterableDescendants$default3 = Instance.descendants$default(instanceFindService, null, 1, null)) != null) {
                    for (Instance instance4 : iterableDescendants$default3) {
                        if (Intrinsics.areEqual(instance4.getClassName(), "Sound")) {
                            this.soundsCache.add(instance4);
                        }
                    }
                }
                PlaySession playSession3 = this.play;
                if (playSession3 != null && (player = playSession3.getPlayer()) != null && (instanceFindFirstChild$default = Instance.findFirstChild$default(player, "PlayerScripts", false, 2, null)) != null && (iterableDescendants$default2 = Instance.descendants$default(instanceFindFirstChild$default, null, 1, null)) != null) {
                    for (Instance instance5 : iterableDescendants$default2) {
                        if (Intrinsics.areEqual(instance5.getClassName(), "Sound")) {
                            this.soundsCache.add(instance5);
                        }
                    }
                }
            }
            Instance instanceFindService2 = getModel().findService("CoreGui");
            if (instanceFindService2 != null && (iterableDescendants$default = Instance.descendants$default(instanceFindService2, null, 1, null)) != null) {
                for (Instance instance6 : iterableDescendants$default) {
                    if (this.ADORNMENTS.contains(instance6.getClassName())) {
                        this.adornCache.add(instance6);
                    }
                }
            }
        }
        return (List) this.partsCache;
    }

    public final List<Instance> renderConstraints() {
        renderParts();
        return this.constraintsCache;
    }

    public final List<Instance> renderDecals() {
        renderParts();
        return this.decalsCache;
    }

    public final List<Instance> renderSurfaceGuis() {
        renderParts();
        return this.surfaceGuisCache;
    }

    public final List<Instance> renderClothing() {
        renderParts();
        return this.clothingCache;
    }

    public final List<Instance> renderAdornments() {
        renderParts();
        return this.adornCache;
    }

    public final List<Instance> audibleSounds() {
        renderParts();
        return this.soundsCache;
    }

    public final List<Instance> renderLights() {
        renderParts();
        return this.lightsCache;
    }

    public final List<Instance> renderBeams() {
        renderParts();
        return this.beamsCache;
    }

    public final List<Instance> renderTrails() {
        renderParts();
        return this.trailsCache;
    }

    public final List<Instance> renderParticles() {
        renderParts();
        return this.particlesCache;
    }

    public final void invalidateRenderLists() {
        this.partsDirty = true;
    }

    public final boolean getShadowsEnabled() {
        return this.shadowsEnabled;
    }

    public final void setShadowsEnabled(boolean z) {
        this.shadowsEnabled = z;
    }

    public final ConcurrentLinkedQueue<Function0<Unit>> getGlJobs() {
        return this.glJobs;
    }

    /* JADX WARN: Code duplicated, block: B:27:0x006c  */
    /* JADX WARN: Code duplicated, block: B:29:0x0097  */
    /* JADX WARN: Code duplicated, block: B:32:0x00a3  */
    public final void frame(float dt0) {
        float dt = dt0;
        while (true) {
            Function0<Unit> function0Poll = this.glJobs.poll();
            if (function0Poll == null) {
                break;
            }
            function0Poll.invoke();
            this.lastJobEnd = System.nanoTime();
        }
        if (System.nanoTime() - this.lastJobEnd < 50000000) {
            dt = Math.min(dt, 0.016666668f);
        }
        PlaySession p = this.play;
        if (p != null) {
            p.step(dt);
            try {
                AudioEngine.INSTANCE.update(this);
                return;
            } catch (Exception e) {
                OutputLog.INSTANCE.warn("Audio: " + e.getMessage());
                return;
            }
        }
        FlyCamera cam = this.camera;
        if (this.pendingYaw == 0.0f) {
            if (!(this.pendingPitch == 0.0f)) {
                float k = 1.0f - ((float) Math.exp((-24.0f) * dt));
                float y = this.pendingYaw * k;
                float p2 = this.pendingPitch * k;
                cam.rotate(y, p2);
                this.pendingYaw -= y;
                this.pendingPitch -= p2;
                if (Math.abs(this.pendingYaw) < 1.0E-4f) {
                    this.pendingYaw = 0.0f;
                }
                if (Math.abs(this.pendingPitch) < 1.0E-4f) {
                    this.pendingPitch = 0.0f;
                }
            }
        } else {
            float k2 = 1.0f - ((float) Math.exp((-24.0f) * dt));
            float y2 = this.pendingYaw * k2;
            float p3 = this.pendingPitch * k2;
            cam.rotate(y2, p3);
            this.pendingYaw -= y2;
            this.pendingPitch -= p3;
            if (Math.abs(this.pendingYaw) < 1.0E-4f) {
                this.pendingYaw = 0.0f;
            }
            if (Math.abs(this.pendingPitch) < 1.0E-4f) {
                this.pendingPitch = 0.0f;
            }
        }
        Vector3 mv = cam.getForward().times(-this.moveY).plus(cam.getRight().times(this.moveX));
        if (this.flyUp) {
            mv = mv.plus(Vector3.INSTANCE.getY());
        }
        if (this.flyDown) {
            mv = mv.minus(Vector3.INSTANCE.getY());
        }
        Vector3 target = mv.getMagnitude() > 0.01f ? mv.times(getFlySpeed() * 40.0f) : Vector3.INSTANCE.getZERO();
        this.camVel = Settings.INSTANCE.getCameraSmoothing() ? this.camVel.plus(target.minus(this.camVel).times(1.0f - ((float) Math.exp((-9.0f) * dt)))) : target;
        if (this.camVel.getMagnitude() > 0.001f) {
            cam.setPos(cam.getPos().plus(this.camVel.times(dt)));
        } else {
            this.camVel = Vector3.INSTANCE.getZERO();
        }
        Instance instanceFindService = this.editModel.findService("RunService");
        if (instanceFindService != null) {
            LuaNumber luaNumberValueOf = LuaValue.valueOf(dt);
            LuaRuntime luaRuntime = this.editRuntime;
            Intrinsics.checkNotNull(luaNumberValueOf);
            luaRuntime.fire(instanceFindService, "RenderStepped", luaNumberValueOf);
            this.editRuntime.fire(instanceFindService, "Heartbeat", luaNumberValueOf);
        }
        this.editRuntime.step(dt);
    }

    public final Vector3 getPlayHereAt() {
        return this.playHereAt;
    }

    public final void setPlayHereAt(Vector3 vector3) {
        this.playHereAt = vector3;
    }

    public final void playHere() {
        Vector3 vector3Plus;
        synchronized (this.lock) {
            Pair<Vector3, Vector3> pairScreenRay = this.view.screenRay(this.view.getWidth() / 2.0f, this.view.getHeight() / 2.0f);
            RayHit rayHitRaycast = Geometry.INSTANCE.raycast(renderParts(), pairScreenRay.component1(), pairScreenRay.component2(), 2000.0f, new Function1() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda15
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Boolean.valueOf(Studio.playHere$lambda$20$lambda$19((Instance) obj));
                }
            });
            if (rayHitRaycast == null || (vector3Plus = rayHitRaycast.getPosition()) == null) {
                vector3Plus = this.camera.getPos().plus(this.camera.getForward().times(12.0f));
            }
            this.playHereAt = vector3Plus;
            Unit unit = Unit.INSTANCE;
        }
        startPlay();
    }

    static final boolean playHere$lambda$20$lambda$19(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return !Intrinsics.areEqual(it.get("CanCollide"), (Object) false);
    }

    public final void playAs(Instance target) {
        List<? extends Instance> listListOf;
        Intrinsics.checkNotNullParameter(target, "target");
        synchronized (this.lock) {
            if (isPlaying()) {
                return;
            }
            if (target.isA("BasePart")) {
                listListOf = CollectionsKt.listOf(target);
            } else {
                Iterable iterableDescendants$default = Instance.descendants$default(target, null, 1, null);
                Collection arrayList = new ArrayList();
                for (Object obj : iterableDescendants$default) {
                    if (((Instance) obj).isA("BasePart")) {
                        arrayList.add(obj);
                    }
                }
                listListOf = (List) arrayList;
            }
            if (listListOf.isEmpty()) {
                toast(target.getName() + " has no parts to play as");
                return;
            }
            target.getAttributes().put(PLAY_AS_TAG, true);
            Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(listListOf);
            if (pairBoundsOf != null) {
                Vector3 vector3Component1 = pairBoundsOf.component1();
                Vector3 vector3Component2 = pairBoundsOf.component2();
                this.playHereAt = new Vector3((vector3Component1.getX() + vector3Component2.getX()) / 2.0f, vector3Component1.getY(), (vector3Component1.getZ() + vector3Component2.getZ()) / 2.0f);
                Unit unit = Unit.INSTANCE;
            }
            try {
                startPlay();
                synchronized (this.lock) {
                    target.getAttributes().remove(PLAY_AS_TAG);
                }
            } catch (Throwable th) {
                synchronized (this.lock) {
                    target.getAttributes().remove(PLAY_AS_TAG);
                    throw th;
                }
            }
        }
    }

    public final void startPlay() {
        synchronized (this.lock) {
            if (isPlaying()) {
                return;
            }
            this.undo.commit();
            this.selection.clear();
            this.editCamSaved = new Triple<>(this.camera.getPos(), Float.valueOf(this.camera.getYaw()), Float.valueOf(this.camera.getPitch()));
            DataModel dataModelCloneModel = this.editModel.cloneModel();
            this.playModel = dataModelCloneModel;
            dataModelCloneModel.getListeners().add(this.treeListener);
            this.partsDirty = true;
            this.moveX = 0.0f;
            this.moveY = 0.0f;
            final PlaySession playSession = new PlaySession(this, dataModelCloneModel);
            this.play = playSession;
            AudioEngine audioEngine = AudioEngine.INSTANCE;
            audioEngine.setOnLoaded(new Function1() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Studio.startPlay$lambda$28$lambda$25(playSession, (Instance) obj);
                }
            });
            audioEngine.setOnEnded(new Function1() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda12
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Studio.startPlay$lambda$28$lambda$26(playSession, (Instance) obj);
                }
            });
            this.glJobs.add(new Function0() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda13
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Studio.startPlay$lambda$28$lambda$27(Studio.this, playSession);
                }
            });
            for (Listener listener : this.listeners) {
                listener.onSelectionChanged();
                listener.onModelReplaced();
                listener.onPlayStateChanged();
            }
        }
    }

    static final Unit startPlay$lambda$28$lambda$25(PlaySession $s, Instance snd) {
        String string;
        Intrinsics.checkNotNullParameter(snd, "snd");
        LuaRuntime rt = $s.getRt();
        LuaValue[] luaValueArr = new LuaValue[1];
        Object obj = snd.get("SoundId");
        if (obj == null || (string = obj.toString()) == null) {
            string = "";
        }
        LuaString luaStringValueOf = LuaValue.valueOf(string);
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        luaValueArr[0] = luaStringValueOf;
        rt.fire(snd, "Loaded", luaValueArr);
        return Unit.INSTANCE;
    }

    static final Unit startPlay$lambda$28$lambda$26(PlaySession $s, Instance snd) {
        String string;
        Intrinsics.checkNotNullParameter(snd, "snd");
        LuaRuntime rt = $s.getRt();
        LuaValue[] luaValueArr = new LuaValue[1];
        Object obj = snd.get("SoundId");
        if (obj == null || (string = obj.toString()) == null) {
            string = "";
        }
        LuaString luaStringValueOf = LuaValue.valueOf(string);
        Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
        luaValueArr[0] = luaStringValueOf;
        rt.fire(snd, "Ended", luaValueArr);
        if (Intrinsics.areEqual(snd.getAttributes().get("__oneShot"), (Object) true) || (Intrinsics.areEqual(snd.get("PlayOnRemove"), (Object) true) && snd.getParent() == null)) {
            snd.destroy();
        }
        return Unit.INSTANCE;
    }

    static final Unit startPlay$lambda$28$lambda$27(Studio this$0, PlaySession $s) {
        if (this$0.play == $s) {
            try {
                $s.start();
            } catch (Exception e) {
                OutputLog.INSTANCE.error("Playtest failed to start: " + e);
            }
        }
        return Unit.INSTANCE;
    }

    public final void stopPlay() {
        CopyOnWriteArrayList<TreeListener> listeners;
        synchronized (this.lock) {
            PlaySession playSession = this.play;
            if (playSession == null) {
                return;
            }
            playSession.stop();
            AudioEngine.INSTANCE.stopAll();
            DataModel dataModel = this.playModel;
            if (dataModel != null && (listeners = dataModel.getListeners()) != null) {
                listeners.remove(this.treeListener);
            }
            this.playModel = null;
            this.play = null;
            this.selection.clear();
            this.partsDirty = true;
            Triple<Vector3, Float, Float> triple = this.editCamSaved;
            if (triple != null) {
                Vector3 vector3Component1 = triple.component1();
                float fFloatValue = triple.component2().floatValue();
                float fFloatValue2 = triple.component3().floatValue();
                this.camera.setPos(vector3Component1);
                this.camera.setYaw(fFloatValue);
                this.camera.setPitch(fFloatValue2);
            }
            this.camera.setFov(70.0f);
            this.moveX = 0.0f;
            this.moveY = 0.0f;
            Unit unit = Unit.INSTANCE;
            for (Listener listener : this.listeners) {
                listener.onSelectionChanged();
                listener.onModelReplaced();
                listener.onPlayStateChanged();
            }
        }
    }

    public static /* synthetic */ void replaceEditModel$default(Studio studio, DataModel dataModel, Uri uri, String str, boolean z, CloudPlace cloudPlace, int i, Object obj) {
        if ((i & 16) != 0) {
            cloudPlace = null;
        }
        studio.replaceEditModel(dataModel, uri, str, z, cloudPlace);
    }

    public final void replaceEditModel(DataModel dm, Uri uri, String name, boolean isXml, CloudPlace cloud) {
        Intrinsics.checkNotNullParameter(dm, "dm");
        Intrinsics.checkNotNullParameter(name, "name");
        this.cloud = cloud;
        com.goydevv.studrblx.lua.SpecialProps.prefetch(cloud);
        if (isPlaying()) {
            stopPlay();
        }
        synchronized (this.lock) {
            this.editModel.getListeners().remove(this.treeListener);
            this.editRuntime.shutdown();
            this.pluginButtons.clear();
            this.pluginWidgets.clear();
            this.editModel = dm;
            dm.getListeners().add(this.treeListener);
            this.editRuntime = new LuaRuntime(this, dm, false);
            this.selection.clear();
            this.undo.clear();
            this.partsDirty = true;
            this.fileUri = uri;
            this.fileName = name;
            this.fileIsXml = isXml;
            this.dirty = false;
            resetCameraFromModel();
            Unit unit = Unit.INSTANCE;
        }
        for (Listener listener : this.listeners) {
            listener.onModelReplaced();
            listener.onSelectionChanged();
            listener.onDirtyChanged();
            listener.onPluginsChanged();
        }
        this.plugins.loadAll();
    }

    public static /* synthetic */ void newPlace$default(Studio studio, PlaceFactory.Template template, int i, Object obj) {
        if ((i & 1) != 0) {
            template = PlaceFactory.Template.BASEPLATE;
        }
        studio.newPlace(template);
    }

    public final void newPlace(PlaceFactory.Template template) {
        Intrinsics.checkNotNullParameter(template, "template");
        replaceEditModel$default(this, PlaceFactory.INSTANCE.newPlace(template), null, "Place1.rbxl", false, null, 16, null);
    }

    private final void resetCameraFromModel() {
        Instance ws = this.editModel.getWorkspace();
        Object obj = ws.get("CurrentCamera");
        Instance cam = obj instanceof Instance ? (Instance) obj : null;
        Object obj2 = cam != null ? cam.get("CFrame") : null;
        CFrame cf = obj2 instanceof CFrame ? (CFrame) obj2 : null;
        // Only trust saved CurrentCamera if it is reasonably close to actual Workspace geometry
        boolean savedCamValid = false;
        // Auto-frame SpawnLocation or Workspace BaseParts so imported maps never open looking at empty void
        Instance spawn = null;
        float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
        int partCount = 0;
        for (Instance inst : Instance.descendants$default(ws, null, 1, null)) {
            if ("SpawnLocation".equals(inst.getClassName()) && spawn == null) {
                spawn = inst;
            }
            if (inst.isA("BasePart") && !"Terrain".equals(inst.getClassName())) {
                Vector3 pos = PartData.INSTANCE.cframe(inst).getPosition();
                Vector3 sz = PartData.INSTANCE.size(inst);
                // Skip huge baseplates from skewing center if other parts exist, unless it's the only part
                if (sz.getX() < 500.0f && sz.getZ() < 500.0f) {
                    minX = Math.min(minX, pos.getX());
                    minY = Math.min(minY, pos.getY());
                    minZ = Math.min(minZ, pos.getZ());
                    maxX = Math.max(maxX, pos.getX());
                    maxY = Math.max(maxY, pos.getY());
                    maxZ = Math.max(maxZ, pos.getZ());
                    partCount++;
                }
            }
        }
        if (partCount == 0) {
            // Fallback: include large parts too if map only has huge parts
            for (Instance inst : Instance.descendants$default(ws, null, 1, null)) {
                if (inst.isA("BasePart") && !"Terrain".equals(inst.getClassName())) {
                    Vector3 pos = PartData.INSTANCE.cframe(inst).getPosition();
                    minX = Math.min(minX, pos.getX());
                    minY = Math.min(minY, pos.getY());
                    minZ = Math.min(minZ, pos.getZ());
                    maxX = Math.max(maxX, pos.getX());
                    maxY = Math.max(maxY, pos.getY());
                    maxZ = Math.max(maxZ, pos.getZ());
                    partCount++;
                }
            }
        }
        if (cf != null && cf.getPosition().getMagnitude() > 0.01f && partCount > 0) {
            float cx0 = (minX + maxX) * 0.5f, cy0 = (minY + maxY) * 0.5f, cz0 = (minZ + maxZ) * 0.5f;
            Vector3 cp0 = cf.getPosition();
            float distToMap = (float) Math.sqrt((cp0.getX()-cx0)*(cp0.getX()-cx0) + (cp0.getY()-cy0)*(cp0.getY()-cy0) + (cp0.getZ()-cz0)*(cp0.getZ()-cz0));
            float mapRadius = Math.max(300.0f, Math.max(maxX - minX, maxZ - minZ));
            if (distToMap < mapRadius * 1.5f) {
                savedCamValid = true;
            }
        }
        if (savedCamValid) {
            this.camera.setFromCFrame(cf);
            return;
        }
        if (spawn != null) {
            Vector3 sp = PartData.INSTANCE.cframe(spawn).getPosition();
            this.camera.setPos(new Vector3(sp.getX(), sp.getY() + 18.0f, sp.getZ() + 34.0f));
            this.camera.setYaw(0.0f);
            this.camera.setPitch(-0.42f);
            return;
        }
        if (partCount > 0) {
            float cx = (minX + maxX) * 0.5f;
            float cy = (minY + maxY) * 0.5f;
            float cz = (minZ + maxZ) * 0.5f;
            float span = Math.max(24.0f, Math.min(220.0f, Math.max(maxX - minX, maxZ - minZ) * 0.75f));
            this.camera.setPos(new Vector3(cx, cy + span * 0.55f, cz + span));
            this.camera.setYaw(0.0f);
            this.camera.setPitch(-0.45f);
            return;
        }
        this.camera.setPos(new Vector3(0.0f, 20.0f, 40.0f));
        this.camera.setYaw(0.0f);
        this.camera.setPitch(-0.4f);
    }

    public final byte[] serializePlace() {
        byte[] bArrWrite;
        synchronized (this.lock) {
            Object obj = this.editModel.getWorkspace().get("CurrentCamera");
            Instance instance = obj instanceof Instance ? (Instance) obj : null;
            if (instance != null) {
                instance.set("CFrame", this.camera.cframe(), false);
            }
            RbxBinaryWriter rbxBinaryWriter = RbxBinaryWriter.INSTANCE;
            Iterable children = this.editModel.getChildren();
            Collection arrayList = new ArrayList();
            for (Object obj2 : children) {
                if (((Instance) obj2).getArchivable()) {
                    arrayList.add(obj2);
                }
            }
            bArrWrite = rbxBinaryWriter.write((List) arrayList, this.editModel.getMeta());
        }
        return bArrWrite;
    }

    public final byte[] serializeSelection() {
        byte[] bArrSerializeInstances;
        synchronized (this.lock) {
            bArrSerializeInstances = serializeInstances(CollectionsKt.toList(this.selection));
        }
        return bArrSerializeInstances;
    }

    /* JADX WARN: Code duplicated, block: B:33:0x0085 A[Catch: all -> 0x00a6, TryCatch #0 {, blocks: (B:5:0x000d, B:6:0x001e, B:8:0x0024, B:10:0x0032, B:12:0x003c, B:33:0x0085, B:15:0x004a, B:16:0x004e, B:18:0x0054, B:20:0x0066, B:35:0x008b, B:38:0x009a), top: B:44:0x000d }] */
    /* JADX WARN: Code duplicated, block: B:48:0x0088 A[SYNTHETIC] */
    public final byte[] serializeInstances(List<? extends Instance> list) {
        byte[] bArrWrite;
        int i;
        boolean z = false;
        int i2;
        Intrinsics.checkNotNullParameter(list, "list");
        synchronized (this.lock) {
            int i3 = 0;
            Collection arrayList = new ArrayList();
            for (Object obj : list) {
                Instance instance = (Instance) obj;
                if (instance.getIsService()) {
                    i = i3;
                } else {
                    List<? extends Instance> list2 = list;
                    if ((list2 instanceof Collection) && list2.isEmpty()) {
                        i = i3;
                        i2 = 1;
                    } else {
                        Iterator it = list2.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                i = i3;
                                i2 = 1;
                                break;
                            }
                            Instance instance2 = (Instance) it.next();
                            i = i3;
                            if (instance2 != instance && instance.isDescendantOf(instance2)) {
                                i2 = 0;
                                break;
                            }
                            i3 = i;
                        }
                    }
                    z = i2 != 0;
                    if (z) {
                        arrayList.add(obj);
                    }
                    i3 = i;
                }
                if (z) {
                    arrayList.add(obj);
                }
                i3 = i;
            }
            List list3 = (List) arrayList;
            bArrWrite = list3.isEmpty() ? null : RbxBinaryWriter.INSTANCE.write(list3, MapsKt.emptyMap());
        }
        return bArrWrite;
    }

    public final void markSaved(Uri uri, String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.fileUri = uri;
        this.fileName = name;
        this.fileIsXml = false;
        this.dirty = false;
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onDirtyChanged();
        }
    }

    public final void markSavedToCloud() {
        this.dirty = false;
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onDirtyChanged();
        }
    }

    public static /* synthetic */ void insertDocument$default(Studio studio, RbxDocument rbxDocument, Instance instance, int i, Object obj) {
        if ((i & 2) != 0) {
            instance = null;
        }
        studio.insertDocument(rbxDocument, instance);
    }

    public final void insertDocument(RbxDocument doc, Instance parent) {
        Instance service;
        Object obj;
        Instance instanceCreate;
        Intrinsics.checkNotNullParameter(doc, "doc");
        ArrayList inserted = new ArrayList();
        synchronized (this.lock) {
            int i = 0;
            this.undo.begin("Insert model");
            Iterator<Instance> it = doc.getRoots().iterator();
            while (true) {
                boolean z = true;
                if (!it.hasNext()) {
                    break;
                }
                Instance next = it.next();
                if (parent == null) {
                    boolean isMatOnly = Intrinsics.areEqual(next.getClassName(), "MaterialVariant");
                    if (!isMatOnly && Intrinsics.areEqual(next.getClassName(), "Folder") && !next.getChildren().isEmpty()) {
                        isMatOnly = true;
                        for (Instance instDesc : Instance.descendants$default(next, null, 1, null)) {
                            if (!(Intrinsics.areEqual(instDesc.getClassName(), "MaterialVariant") || Intrinsics.areEqual(instDesc.getClassName(), "Folder"))) {
                                isMatOnly = false;
                                break;
                            }
                        }
                    }
                    boolean isLightOnly = false;
                    if (!isMatOnly) {
                        String rcn = next.getClassName();
                        if ("Sky".equals(rcn) || "Atmosphere".equals(rcn) || "Clouds".equals(rcn) || "ColorCorrectionEffect".equals(rcn) || "BloomEffect".equals(rcn) || "SunRaysEffect".equals(rcn) || "BlurEffect".equals(rcn) || "DepthOfFieldEffect".equals(rcn) || "Lighting".equals(rcn)) {
                            isLightOnly = true;
                        } else if (("Folder".equals(rcn) || "Model".equals(rcn)) && !next.getChildren().isEmpty()) {
                            boolean hasLightChild = false;
                            boolean hasPartChild = false;
                            for (Instance dInst : Instance.descendants$default(next, null, 1, null)) {
                                String dcn = dInst.getClassName();
                                if (dInst.isA("BasePart")) {
                                    hasPartChild = true;
                                    break;
                                }
                                if ("Sky".equals(dcn) || "Atmosphere".equals(dcn) || "ColorCorrectionEffect".equals(dcn) || "BloomEffect".equals(dcn) || "SunRaysEffect".equals(dcn)) {
                                    hasLightChild = true;
                                }
                            }
                            isLightOnly = hasLightChild && !hasPartChild;
                        }
                    }
                    if (isMatOnly) {
                        service = getModel().getService("MaterialService");
                    } else if (isLightOnly) {
                        service = getModel().getService("Lighting");
                    } else if (next.isA("LayerCollector") && !Intrinsics.areEqual(next.getClassName(), "BillboardGui") && !Intrinsics.areEqual(next.getClassName(), "SurfaceGui")) {
                        service = getModel().getService("StarterGui");
                    } else if (next.isA("GuiObject")) {
                        Instance service2 = getModel().getService("StarterGui");
                        Instance instance2 = null;
                        for (Instance ch : service2.getChildren()) {
                            if (Intrinsics.areEqual(ch.getClassName(), "ScreenGui")) {
                                instance2 = ch;
                                break;
                            }
                        }
                        if (instance2 == null) {
                            instanceCreate = InstanceFactory.INSTANCE.create("ScreenGui");
                            reparent(instanceCreate, service2);
                        } else {
                            instanceCreate = instance2;
                        }
                        service = instanceCreate;
                    } else {
                        service = getModel().getWorkspace();
                    }
                } else {
                    service = parent;
                }
                reparent(next, service);
                inserted.add(next);
            }
            ArrayList<Instance> arrayList = inserted;
            Collection arrayList2 = new ArrayList();
            for (Instance instance3 : arrayList) {
                Iterable iterablePlus = CollectionsKt.plus((Collection) CollectionsKt.listOf(instance3), (Iterable) Instance.descendants$default(instance3, null, 1, null));
                Collection arrayList3 = new ArrayList();
                for (Object obj2 : iterablePlus) {
                    Collection collection = arrayList;
                    if (((Instance) obj2).isA("BasePart")) {
                        Collection collection2 = arrayList3;
                        collection2.add(obj2);
                        arrayList3 = (ArrayList) collection2;
                    }
                    arrayList = (ArrayList) collection;
                }
                CollectionsKt.addAll(arrayList2, (List) arrayList3);
            }
            List<Instance> list = (List) arrayList2;
            Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(list);
            if (pairBoundsOf != null) {
                Vector3 vector3Component1 = pairBoundsOf.component1();
                Vector3 vector3Component2 = pairBoundsOf.component2();
                Vector3 vector3Minus = spawnPoint(vector3Component2.minus(vector3Component1)).minus(vector3Component1.plus(vector3Component2).div(2.0f));
                for (Instance instance4 : list) {
                    setProp(instance4, "CFrame", PartData.INSTANCE.cframe(instance4).plus(vector3Minus));
                }
            }
            this.undo.commit();
            this.selection.clear();
            this.selection.addAll(inserted);
        }
        Iterator it4 = this.listeners.iterator();
        while (it4.hasNext()) {
            ((Listener) it4.next()).onSelectionChanged();
        }
    }

    public final void select(List<? extends Instance> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        synchronized (this.lock) {
            this.selection.clear();
            this.selection.addAll(CollectionsKt.distinct(list));
            fireSelectionChangedLua();
            Unit unit = Unit.INSTANCE;
        }
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onSelectionChanged();
        }
    }

    public final void toggleSelect(final Instance i) {
        Intrinsics.checkNotNullParameter(i, "i");
        synchronized (this.lock) {
            Iterable iterable = this.selection;
            boolean z = false;
            if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                Iterator it = iterable.iterator();
                while (it.hasNext()) {
                    if (((Instance) it.next()) == i) {
                        z = true;
                        break;
                    }
                }
            }
            if (z) {
                CollectionsKt.removeAll((List) this.selection, new Function1() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Boolean.valueOf(Studio.toggleSelect$lambda$56$lambda$55(i, (Instance) obj));
                    }
                });
            } else {
                this.selection.add(i);
            }
            fireSelectionChangedLua();
            Unit unit = Unit.INSTANCE;
        }
        Iterator it2 = this.listeners.iterator();
        while (it2.hasNext()) {
            ((Listener) it2.next()).onSelectionChanged();
        }
    }

    static final boolean toggleSelect$lambda$56$lambda$55(Instance $i, Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it == $i;
    }

    public final void selectFromScript(List<? extends Instance> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        this.selection.clear();
        this.selection.addAll(CollectionsKt.distinct(list));
        this.main.post(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                Studio.selectFromScript$lambda$59(Studio.this);
            }
        });
    }

    static final void selectFromScript$lambda$59(Studio this$0) {
        Iterator it = this$0.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onSelectionChanged();
        }
    }

    private final void fireSelectionChangedLua() {
        Instance svc = this.editModel.findService("Selection");
        if (svc != null && !isPlaying()) {
            this.editRuntime.fire(svc, "SelectionChanged", new LuaValue[0]);
        }
    }

    static final boolean pruneSelection$lambda$61$lambda$60(Studio this$0, Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.root() != this$0.getModel();
    }

    public final void pruneSelection() {
        synchronized (this.lock) {
            CollectionsKt.removeAll((List) this.selection, new Function1() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Boolean.valueOf(Studio.pruneSelection$lambda$61$lambda$60(Studio.this, (Instance) obj));
                }
            });
        }
        this.main.post(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                Studio.pruneSelection$lambda$63(Studio.this);
            }
        });
    }

    static final void pruneSelection$lambda$63(Studio this$0) {
        for (Listener listener : this$0.listeners) {
            listener.onSelectionChanged();
            listener.onTreeChanged();
        }
    }

    public final void setProp(Instance inst, String name, Object value) {
        try {
        Instance instance;
        Iterable iterableDescendants$default;
        Object next;
        Instance instance2;
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(name, "name");
        synchronized (this.lock) {
            try {
                boolean zContainsKey = inst.getProps().containsKey(name);
                Object obj = inst.getProps().get(name);
                if (zContainsKey && Intrinsics.areEqual(obj, value)) {
                    return;
                }
                Instance.set$default(inst, name, value, false, 4, null);
                try {
                    try {
                        this.undo.record(new UndoManager.Op.Prop(inst, name, obj, value, zContainsKey));
                        if ((Intrinsics.areEqual(name, "MaterialVariant") || Intrinsics.areEqual(name, "MaterialVariantSerialized")) && inst.isA("BasePart")) {
                            String string = value != null ? value.toString() : null;
                            if (string == null) {
                                string = "";
                            }
                            if (string.length() == 0) {
                                instance = null;
                            } else {
                                Instance instanceFindService = getModel().findService("MaterialService");
                                if (instanceFindService == null || (iterableDescendants$default = Instance.descendants$default(instanceFindService, null, 1, null)) == null) {
                                    instance = null;
                                } else {
                                    Iterator it = iterableDescendants$default.iterator();
                                    do {
                                        if (!it.hasNext()) {
                                            next = null;
                                            break;
                                        } else {
                                            next = it.next();
                                            instance2 = (Instance) next;
                                        }
                                    } while (!(Intrinsics.areEqual(instance2.getClassName(), "MaterialVariant") && Intrinsics.areEqual(instance2.getName(), string)));
                                    instance = (Instance) next;
                                }
                            }
                            Object obj2 = instance != null ? instance.get("BaseMaterial") : null;
                            EnumValue enumValue = obj2 instanceof EnumValue ? (EnumValue) obj2 : null;
                            Integer numValueOf = enumValue != null ? Integer.valueOf(enumValue.getValue()) : null;
                            if (numValueOf != null && PartData.INSTANCE.material(inst) != numValueOf.intValue()) {
                                setProp(inst, "Material", new EnumValue(numValueOf.intValue()));
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                        return;
                    } catch (Throwable th) {
                        throw th;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final void reparent(Instance inst, Instance newParent) {
        List children;
        Intrinsics.checkNotNullParameter(inst, "inst");
        synchronized (this.lock) {
            Instance parent = inst.getParent();
            int i = -1;
            if (parent != null && (children = parent.getChildren()) != null) {
                int i2 = 0;
                Iterator<Instance> it = children.iterator();
                while (it.hasNext()) {
                    if (((Instance) it.next()) == inst) {
                        i = i2;
                        break;
                    }
                    i2++;
                }
            }
            Instance.setParent$default(inst, newParent, 0, 2, null);
            this.undo.record(new UndoManager.Op.Parent(inst, parent, i, newParent));
            Unit unit = Unit.INSTANCE;
        }
    }

    public final Vector3 spawnPoint(Vector3 size) {
        Intrinsics.checkNotNullParameter(size, "size");
        Pair<Vector3, Vector3> pairScreenRay = this.view.screenRay(this.view.getWidth() / 2.0f, this.view.getHeight() / 2.0f);
        Vector3 o = pairScreenRay.component1();
        Vector3 d = pairScreenRay.component2();
        RayHit hit = Geometry.INSTANCE.raycast(renderParts(), o, d, 600.0f, new Function1() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(Studio.spawnPoint$lambda$68((Instance) obj));
            }
        });
        Vector3 p = hit != null ? hit.getPosition().plus(new Vector3(0.0f, size.getY() / 2.0f, 0.0f)) : this.camera.getPos().plus(this.camera.getForward().times(20.0f));
        return (!getSnapEnabled() || getMoveSnap() <= 0.0f) ? p : new Vector3(snap(p.getX(), getMoveSnap()), p.getY(), snap(p.getZ(), getMoveSnap()));
    }

    static final boolean spawnPoint$lambda$68(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return true;
    }

    private final float snap(float v, float s) {
        return MathKt.roundToInt(v / s) * s;
    }

    private final boolean usesAttachments(String cls) {
        return ClassDb.INSTANCE.isA(cls, "Constraint") || Intrinsics.areEqual(cls, "Beam") || Intrinsics.areEqual(cls, "Trail") || Intrinsics.areEqual(cls, "PathfindingLink");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v4, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r7v6 */
    private final boolean insertLinked(Instance inst) {
        int r7;
        Instance instanceCreate;
        Iterable iterable = this.selection;
        Collection arrayList = new ArrayList();
        for (Object obj : iterable) {
            Instance instance = (Instance) obj;
            if (instance.isA("BasePart") || Intrinsics.areEqual(instance.getClassName(), "Attachment")) {
                arrayList.add(obj);
            }
        }
        List list = (List) arrayList;
        if (list.size() != 2) {
            return false;
        }
        this.undo.begin("Insert " + inst.getClassName());
        List<Instance> list2 = list;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
        for (Instance instance2 : list2) {
            if (Intrinsics.areEqual(instance2.getClassName(), "Attachment")) {
                instanceCreate = instance2;
            } else {
                instanceCreate = InstanceFactory.INSTANCE.create("Attachment");
                instanceCreate.setName(StringsKt.removeSuffix(inst.getClassName(), (CharSequence) "Constraint") + "Attachment");
                reparent(instanceCreate, instance2);
            }
            arrayList2.add(instanceCreate);
        }
        List atts = (List) arrayList2;
        inst.set("Attachment0", atts.get(0), false);
        inst.set("Attachment1", atts.get(1), false);
        float d = insertLinked$world((Instance) atts.get(0)).minus(insertLinked$world((Instance) atts.get(1))).getMagnitude();
        if (Intrinsics.areEqual(inst.getClassName(), "RopeConstraint") || Intrinsics.areEqual(inst.getClassName(), "RodConstraint")) {
            r7 = 0;
            inst.set("Length", Float.valueOf(d), false);
        } else {
            r7 = 0;
        }
        if (Intrinsics.areEqual(inst.getClassName(), "SpringConstraint")) {
            inst.set("FreeLength", Float.valueOf(d), r7 != 0);
        }
        Object obj2 = list.get(r7);
        if (!((Instance) obj2).isA("BasePart")) {
            obj2 = null;
        }
        Instance parent = (Instance) obj2;
        if (parent == null && (parent = ((Instance) list.get(0)).getParent()) == null) {
            parent = getModel().getWorkspace();
        }
        reparent(inst, parent);
        this.undo.commit();
        this.selection.clear();
        this.selection.add(inst);
        return true;
    }

    private static final Vector3 insertLinked$world(Instance a) {
        Vector3 zero;
        Instance part = a.getParent();
        if (part != null) {
            if (!part.isA("BasePart")) {
                part = null;
            }
            if (part != null) {
                CFrame cframe = PartData.INSTANCE.cframe(part);
                Object obj = a.get("CFrame");
                CFrame cFrame = obj instanceof CFrame ? (CFrame) obj : null;
                if (cFrame == null || (zero = cFrame.getPosition()) == null) {
                    zero = Vector3.INSTANCE.getZERO();
                }
                return cframe.times(zero);
            }
        }
        return Vector3.INSTANCE.getZERO();
    }

    public static /* synthetic */ Instance insertObject$default(Studio studio, String str, Instance instance, int i, Object obj) {
        if ((i & 2) != 0) {
            instance = null;
        }
        return studio.insertObject(str, instance);
    }

    /* JADX WARN: Code duplicated, block: B:39:0x0082 A[Catch: all -> 0x01a5, TryCatch #1 {, blocks: (B:24:0x0047, B:26:0x0053, B:28:0x005b, B:30:0x0067, B:33:0x0071, B:74:0x013d, B:76:0x015e, B:77:0x0174, B:39:0x0082, B:40:0x0098, B:42:0x009e, B:46:0x00b5, B:48:0x00b9, B:51:0x00cb, B:53:0x00d7, B:54:0x00e2, B:57:0x00f0, B:59:0x00fe, B:68:0x011a, B:70:0x0127, B:72:0x0132), top: B:90:0x0047 }] */
    /* JADX WARN: Code duplicated, block: B:42:0x009e A[Catch: all -> 0x01a5, TryCatch #1 {, blocks: (B:24:0x0047, B:26:0x0053, B:28:0x005b, B:30:0x0067, B:33:0x0071, B:74:0x013d, B:76:0x015e, B:77:0x0174, B:39:0x0082, B:40:0x0098, B:42:0x009e, B:46:0x00b5, B:48:0x00b9, B:51:0x00cb, B:53:0x00d7, B:54:0x00e2, B:57:0x00f0, B:59:0x00fe, B:68:0x011a, B:70:0x0127, B:72:0x0132), top: B:90:0x0047 }] */
    /* JADX WARN: Code duplicated, block: B:48:0x00b9 A[Catch: all -> 0x01a5, TryCatch #1 {, blocks: (B:24:0x0047, B:26:0x0053, B:28:0x005b, B:30:0x0067, B:33:0x0071, B:74:0x013d, B:76:0x015e, B:77:0x0174, B:39:0x0082, B:40:0x0098, B:42:0x009e, B:46:0x00b5, B:48:0x00b9, B:51:0x00cb, B:53:0x00d7, B:54:0x00e2, B:57:0x00f0, B:59:0x00fe, B:68:0x011a, B:70:0x0127, B:72:0x0132), top: B:90:0x0047 }] */
    /* JADX WARN: Code duplicated, block: B:49:0x00c7  */
    /* JADX WARN: Code duplicated, block: B:68:0x011a A[Catch: all -> 0x01a5, TryCatch #1 {, blocks: (B:24:0x0047, B:26:0x0053, B:28:0x005b, B:30:0x0067, B:33:0x0071, B:74:0x013d, B:76:0x015e, B:77:0x0174, B:39:0x0082, B:40:0x0098, B:42:0x009e, B:46:0x00b5, B:48:0x00b9, B:51:0x00cb, B:53:0x00d7, B:54:0x00e2, B:57:0x00f0, B:59:0x00fe, B:68:0x011a, B:70:0x0127, B:72:0x0132), top: B:90:0x0047 }] */
    /* JADX WARN: Code duplicated, block: B:94:0x00b4 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:95:0x00b2 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:96:? A[LOOP:2: B:40:0x0098->B:96:?, LOOP_END, SYNTHETIC] */
    public final Instance insertObject(String cls, Instance parentOverride) {
        Instance workspace;
        Instance service;
        Iterator it;
        Object obj;
        Instance instance;
        Instance instanceCreate;
        Object next;
        boolean done;
        Intrinsics.checkNotNullParameter(cls, "cls");
        Instance inst = InstanceFactory.INSTANCE.create(cls);
        if (parentOverride == null && usesAttachments(cls)) {
            synchronized (this.lock) {
                done = insertLinked(inst);
            }
            if (done) {
                Iterator it2 = this.listeners.iterator();
                while (it2.hasNext()) {
                    ((Listener) it2.next()).onSelectionChanged();
                }
                return inst;
            }
        }
        synchronized (this.lock) {
            Instance instance2 = (Instance) CollectionsKt.firstOrNull((List) this.selection);
            if (parentOverride != null) {
                workspace = parentOverride;
            } else if (inst.isA("BasePart") || Intrinsics.areEqual(inst.getClassName(), "Model")) {
                workspace = getModel().getWorkspace();
            } else if (inst.isA("GuiObject")) {
                if (instance2 == null) {
                    service = getModel().getService("StarterGui");
                    it = service.getChildren().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            obj = null;
                            break;
                        }
                        next = it.next();
                        if (Intrinsics.areEqual(((Instance) next).getClassName(), "ScreenGui")) {
                            obj = next;
                            break;
                        }
                    }
                    instance = (Instance) obj;
                    if (instance == null) {
                        instanceCreate = InstanceFactory.INSTANCE.create("ScreenGui");
                        reparent(instanceCreate, service);
                    } else {
                        instanceCreate = instance;
                    }
                    workspace = instanceCreate;
                } else {
                    workspace = instance2.isA("GuiBase2d") ? instance2 : null;
                    if (workspace == null) {
                        service = getModel().getService("StarterGui");
                        it = service.getChildren().iterator();
                        while (true) {
                            if (it.hasNext()) {
                                obj = null;
                                break;
                            }
                            next = it.next();
                            if (Intrinsics.areEqual(((Instance) next).getClassName(), "ScreenGui")) {
                                obj = next;
                                break;
                            }
                        }
                        instance = (Instance) obj;
                        if (instance == null) {
                            instanceCreate = InstanceFactory.INSTANCE.create("ScreenGui");
                            reparent(instanceCreate, service);
                        } else {
                            instanceCreate = instance;
                        }
                        workspace = instanceCreate;
                    }
                }
            } else if (Intrinsics.areEqual(inst.getClassName(), "ScreenGui")) {
                workspace = getModel().getService("StarterGui");
            } else if (!Intrinsics.areEqual(inst.getClassName(), "MaterialVariant")) {
                workspace = instance2 == null ? getModel().getWorkspace() : instance2;
            } else if (instance2 == null) {
                workspace = getModel().getService("MaterialService");
            } else {
                Instance instance3 = Intrinsics.areEqual(instance2.getClassName(), "MaterialService") || instance2.isDescendantOf(getModel().getService("MaterialService")) ? instance2 : null;
                if (instance3 != null) {
                    workspace = instance3;
                } else {
                    workspace = getModel().getService("MaterialService");
                }
            }
            this.undo.begin("Insert " + cls);
            if (inst.isA("BasePart")) {
                inst.set("CFrame", CFrame.INSTANCE.at(spawnPoint(PartData.INSTANCE.size(inst))), false);
            }
            reparent(inst, workspace);
            this.undo.commit();
            this.selection.clear();
            this.selection.add(inst);
        }
        Iterator it3 = this.listeners.iterator();
        while (it3.hasNext()) {
            ((Listener) it3.next()).onSelectionChanged();
        }
        return inst;
    }

    public final void insertPart(int shape) {
        String str;
        switch (shape) {
            case 3:
                str = "WedgePart";
                break;
            case 4:
                str = "CornerWedgePart";
                break;
            default:
                str = "Part";
                break;
        }
        String cls = str;
        Instance p = insertObject$default(this, cls, null, 2, null);
        synchronized (this.lock) {
            try {
                switch (shape) {
                    case 0:
                        Instance.set$default(p, "shape", new EnumValue(0), false, 4, null);
                        Instance.set$default(p, "size", new Vector3(2.0f, 2.0f, 2.0f), false, 4, null);
                        break;
                    case 2:
                        Instance.set$default(p, "shape", new EnumValue(2), false, 4, null);
                        Instance.set$default(p, "size", new Vector3(2.0f, 2.0f, 2.0f), false, 4, null);
                        break;
                }
                Instance.set$default(p, "Anchored", true, false, 4, null);
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private final boolean deletable(Instance i) {
        return (i.getIsService() || i.getParent() == null || i == getModel().getWorkspace().get("CurrentCamera")) ? false : true;
    }

    public final void deleteSelection() {
        synchronized (this.lock) {
            this.undo.begin("Delete");
            for (Instance instance : CollectionsKt.toList(this.selection)) {
                if (deletable(instance)) {
                    reparent(instance, null);
                }
            }
            this.undo.commit();
            this.selection.clear();
            Unit unit = Unit.INSTANCE;
        }
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onSelectionChanged();
        }
    }

    public final void duplicateSelection() {
        ArrayList created = new ArrayList();
        synchronized (this.lock) {
            this.undo.begin("Duplicate");
            for (Instance instance : CollectionsKt.toList(this.selection)) {
                if (deletable(instance)) {
                    Instance instanceCloneTree = instance.cloneTree();
                    reparent(instanceCloneTree, instance.getParent());
                    created.add(instanceCloneTree);
                }
            }
            this.undo.commit();
            if (!created.isEmpty()) {
                this.selection.clear();
                this.selection.addAll(created);
            }
            Unit unit = Unit.INSTANCE;
        }
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onSelectionChanged();
        }
    }

    public final void copySelection() {
        synchronized (this.lock) {
            Iterable iterable = this.selection;
            Collection arrayList = new ArrayList();
            for (Object obj : iterable) {
                if (deletable((Instance) obj)) {
                    arrayList.add(obj);
                }
            }
            Iterable iterable2 = (List) arrayList;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
            Iterator it = iterable2.iterator();
            while (it.hasNext()) {
                arrayList2.add(((Instance) it.next()).cloneTree());
            }
            this.clipboard = (List) arrayList2;
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void cutSelection() {
        copySelection();
        deleteSelection();
    }

    public final void paste(Instance into) {
        ArrayList created = new ArrayList();
        synchronized (this.lock) {
            if (this.clipboard.isEmpty()) {
                return;
            }
            this.undo.begin("Paste");
            Iterator<? extends Instance> it = this.clipboard.iterator();
            while (it.hasNext()) {
                Instance instanceCloneTree = it.next().cloneTree();
                reparent(instanceCloneTree, into == null ? getModel().getWorkspace() : into);
                created.add(instanceCloneTree);
            }
            this.undo.commit();
            this.selection.clear();
            this.selection.addAll(created);
            Iterator it2 = this.listeners.iterator();
            while (it2.hasNext()) {
                ((Listener) it2.next()).onSelectionChanged();
            }
        }
    }

    public final boolean getHasClipboard() {
        return !this.clipboard.isEmpty();
    }

    public final void groupSelection() {
        synchronized (this.lock) {
            Iterable iterable = this.selection;
            Collection arrayList = new ArrayList();
            for (Object obj : iterable) {
                if (deletable((Instance) obj)) {
                    arrayList.add(obj);
                }
            }
            List list = (List) arrayList;
            if (list.isEmpty()) {
                return;
            }
            Instance parent = ((Instance) CollectionsKt.first(list)).getParent();
            if (parent == null) {
                return;
            }
            this.undo.begin("Group");
            Instance instanceCreate = InstanceFactory.INSTANCE.create("Model");
            reparent(instanceCreate, parent);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                reparent((Instance) it.next(), instanceCreate);
            }
            this.undo.commit();
            this.selection.clear();
            this.selection.add(instanceCreate);
            Iterator it2 = this.listeners.iterator();
            while (it2.hasNext()) {
                ((Listener) it2.next()).onSelectionChanged();
            }
        }
    }

    public final void ungroupSelection() {
        synchronized (this.lock) {
            Iterable iterable = this.selection;
            Collection arrayList = new ArrayList();
            for (Object obj : iterable) {
                Instance instance = (Instance) obj;
                if ((Intrinsics.areEqual(instance.getClassName(), "Model") || Intrinsics.areEqual(instance.getClassName(), "Folder")) && deletable(instance)) {
                    arrayList.add(obj);
                }
            }
            List<Instance> list = (List) arrayList;
            if (list.isEmpty()) {
                return;
            }
            this.undo.begin("Ungroup");
            ArrayList arrayList2 = new ArrayList();
            for (Instance instance2 : list) {
                Instance parent = instance2.getParent();
                for (Instance instance3 : CollectionsKt.toList(instance2.getChildren())) {
                    reparent(instance3, parent);
                    arrayList2.add(instance3);
                }
                reparent(instance2, null);
            }
            this.undo.commit();
            this.selection.clear();
            this.selection.addAll(arrayList2);
            Iterator it = this.listeners.iterator();
            while (it.hasNext()) {
                ((Listener) it.next()).onSelectionChanged();
            }
        }
    }

    public final List<Instance> selectedParts() {
        ArrayList out = new ArrayList();
        Set seen = Collections.newSetFromMap(new IdentityHashMap());
        Iterator<Instance> it = this.selection.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Instance s = next;
            if (s.isA("BasePart") && seen.add(s)) {
                out.add(s);
            }
            if (!s.getIsService() && (s.isA("Model") || Intrinsics.areEqual(s.getClassName(), "Folder"))) {
                for (Object obj : Instance.descendants$default(s, null, 1, null)) {
                    Intrinsics.checkNotNullExpressionValue(obj, "next(...)");
                    Instance d = (Instance) obj;
                    if (d.isA("BasePart") && seen.add(d)) {
                        out.add(d);
                    }
                }
            }
        }
        return out;
    }

    public final void setAnchored(boolean v) {
        synchronized (this.lock) {
            this.undo.begin("Anchor");
            Iterator<Instance> it = selectedParts().iterator();
            while (it.hasNext()) {
                setProp(it.next(), "Anchored", Boolean.valueOf(v));
            }
            this.undo.commit();
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void focusSelection() {
        synchronized (this.lock) {
            Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(selectedParts());
            if (pairBoundsOf == null) {
                return;
            }
            this.camera.focusOn(pairBoundsOf.getFirst().plus(pairBoundsOf.getSecond()).div(2.0f), pairBoundsOf.getSecond().minus(pairBoundsOf.getFirst()).getMagnitude() / 2.0f);
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void openScript(Instance s) {
        Intrinsics.checkNotNullParameter(s, "s");
        Function1<? super Instance, Unit> function1 = this.openScriptHandler;
        if (function1 != null) {
            function1.invoke(s);
        }
    }

    public final void requestOpenScript(Instance s) {
        Intrinsics.checkNotNullParameter(s, "s");
        openScript(s);
    }

    public final void addPluginButton(Instance b, String pluginName, LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(b, "b");
        Intrinsics.checkNotNullParameter(pluginName, "pluginName");
        Intrinsics.checkNotNullParameter(rt, "rt");
        this.pluginButtons.add(new PluginButton(b, pluginName, rt));
        notifyPluginButtons();
    }

    static final void notifyPluginButtons$lambda$101(Studio this$0) {
        Iterator it = this$0.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onPluginsChanged();
        }
    }

    public final boolean notifyPluginButtons() {
        return this.main.post(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                Studio.notifyPluginButtons$lambda$101(Studio.this);
            }
        });
    }

    public final void addPluginWidget(Instance w, LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(w, "w");
        Intrinsics.checkNotNullParameter(rt, "rt");
        this.pluginWidgets.add(new PluginWidget(w, rt));
    }

    public final void clickPluginButton(PluginButton b) {
        Intrinsics.checkNotNullParameter(b, "b");
        synchronized (this.lock) {
            if (Intrinsics.areEqual(b.getInst().get("Enabled"), (Object) false)) {
                return;
            }
            b.getRt().fire(b.getInst(), "Click", new LuaValue[0]);
            Unit unit = Unit.INSTANCE;
        }
    }

    public final IdentityHashMap<Instance, LuaTable> getPluginMice() {
        return this.pluginMice;
    }

    public final Instance getActivePlugin() {
        return this.activePlugin;
    }

    public final boolean getActivePluginExclusive() {
        return this.activePluginExclusive;
    }

    public final Function3<? super String, ? super List<String>, ? super Function1<? super Integer, Unit>, Unit> getPluginMenuHandler() {
        return this.pluginMenuHandler;
    }

    public final void setPluginMenuHandler(Function3<? super String, ? super List<String>, ? super Function1<? super Integer, Unit>, Unit> function3) {
        this.pluginMenuHandler = function3;
    }

    public final void activatePlugin(Instance p, LuaRuntime rt, boolean exclusiveMouse) {
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(rt, "rt");
        Instance old = this.activePlugin;
        if (old != null && old != p) {
            deactivatePlugin(old);
        }
        this.activePlugin = p;
        this.activePluginRt = rt;
        this.activePluginExclusive = exclusiveMouse;
        this.main.post(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                Studio.activatePlugin$lambda$104(Studio.this);
            }
        });
    }

    static final void activatePlugin$lambda$104(Studio this$0) {
        Iterator it = this$0.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onPluginsChanged();
        }
    }

    public final void deactivatePlugin(Instance p) {
        Instance cur = this.activePlugin;
        if (cur == null) {
            return;
        }
        if (p == null || p == cur) {
            this.activePlugin = null;
            LuaRuntime luaRuntime = this.activePluginRt;
            if (luaRuntime != null) {
                luaRuntime.fire(cur, "Deactivation", new LuaValue[0]);
            }
            this.activePluginRt = null;
            this.main.post(new Runnable() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    Studio.deactivatePlugin$lambda$106(Studio.this);
                }
            });
        }
    }

    static final void deactivatePlugin$lambda$106(Studio this$0) {
        Iterator it = this$0.listeners.iterator();
        while (it.hasNext()) {
            ((Listener) it.next()).onPluginsChanged();
        }
    }

    public final boolean pluginMouseEvent(String event, float xPx, float yPx) {
        Vector3 vector3Plus;
        Intrinsics.checkNotNullParameter(event, "event");
        synchronized (this.lock) {
            Instance instance = this.activePlugin;
            if (instance == null) {
                return false;
            }
            LuaRuntime luaRuntime = this.activePluginRt;
            if (luaRuntime == null) {
                return false;
            }
            LuaTable luaTable = this.pluginMice.get(instance);
            if (luaTable == null) {
                return false;
            }
            Pair<Vector3, Vector3> pairScreenRay = this.view.screenRay(xPx, yPx);
            Vector3 vector3Component1 = pairScreenRay.component1();
            Vector3 vector3Component2 = pairScreenRay.component2();
            LuaValue luaValue = luaTable.get("TargetFilter");
            if (!luaValue.isuserdata()) {
                luaValue = null;
            }
            Object obj = luaValue != null ? luaValue.touserdata() : null;
            final Instance instance2 = obj instanceof Instance ? (Instance) obj : null;
            RayHit rayHitRaycast = Geometry.INSTANCE.raycast(renderParts(), vector3Component1, vector3Component2, 5000.0f, new Function1() { // from class: com.goydevv.studrblx.core.Studio$$ExternalSyntheticLambda17
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj2) {
                    return Boolean.valueOf(Studio.pluginMouseEvent$lambda$109$lambda$108(instance2, (Instance) obj2));
                }
            });
            if (rayHitRaycast == null || (vector3Plus = rayHitRaycast.getPosition()) == null) {
                vector3Plus = vector3Component1.plus(vector3Component2.times(1000.0f));
            }
            LuaTypes luaTypes = LuaTypes.INSTANCE;
            luaTable.set("Hit", luaTypes.wrap(CFrame.Companion.lookAt$default(CFrame.INSTANCE, vector3Component1, vector3Plus, null, 4, null).withPosition(vector3Plus)));
            luaTable.set("Target", luaRuntime.wrap(rayHitRaycast != null ? rayHitRaycast.getPart() : null));
            float fGuiUnit = guiUnit();
            this.lastEditPointer = new Vector2(xPx / fGuiUnit, yPx / fGuiUnit);
            luaTable.set("X", LuaValue.valueOf(xPx / fGuiUnit));
            luaTable.set("Y", LuaValue.valueOf(yPx / fGuiUnit));
            luaTable.set("UnitRay", luaTypes.wrap(new Ray(vector3Component1, vector3Component2)));
            luaTable.set("Origin", luaTypes.wrap(CFrame.Companion.lookAt$default(CFrame.INSTANCE, vector3Component1, vector3Component1.plus(vector3Component2), null, 4, null)));
            luaTable.set("ViewSizeX", LuaValue.valueOf(this.view.getWidth() / fGuiUnit));
            luaTable.set("ViewSizeY", LuaValue.valueOf(this.view.getHeight() / fGuiUnit));
            luaRuntime.fire(luaTable, event, new LuaValue[0]);
            return true;
        }
    }

    static final boolean pluginMouseEvent$lambda$109$lambda$108(Instance $filter, Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (!Intrinsics.areEqual(it.get("Locked"), (Object) true)) {
            if ($filter == null) {
                return true;
            }
            if (it != $filter && !it.isDescendantOf($filter)) {
                return true;
            }
        }
        return false;
    }

    private final SharedPreferences getPrefs() {
        return (SharedPreferences) this.prefs.getValue();
    }

    public final String pluginSetting(String id, String key) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(key, "key");
        return getPrefs().getString(id + "/" + key, null);
    }

    public final void setPluginSetting(String id, String key, String json) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(json, "json");
        getPrefs().edit().putString(id + "/" + key, json).apply();
    }

    public final void runCommand(String code) {
        LuaRuntime rt;
        Intrinsics.checkNotNullParameter(code, "code");
        OutputLog.INSTANCE.add(OutputLog.Level.INFO, "> " + code);
        synchronized (this.lock) {
            PlaySession playSession = this.play;
            if (playSession == null || (rt = playSession.getRt()) == null) {
                rt = this.editRuntime;
            }
            try { rt.runCommand(code); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final RbxDocument loadDocumentBytes(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        try { return RbxFile.INSTANCE.read(bytes); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }
}
