package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.core.Tool;
import com.goydevv.studrblx.core.UndoManager;
import com.goydevv.studrblx.engine.ConvexShape;
import com.goydevv.studrblx.engine.Geometry;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.engine.RayHit;
import com.goydevv.studrblx.lua.EnumItemV;
import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.lua.LuaTypes;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.DataTypesKt;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.render.AdornHandles;
import com.goydevv.studrblx.render.FlyCamera;
import com.goydevv.studrblx.render.Handle;
import com.goydevv.studrblx.render.HandleSet;
import com.goydevv.studrblx.render.Handles;
import com.goydevv.studrblx.render.SceneRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaValue;

/* JADX INFO: compiled from: ViewportView.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class ViewportView extends FrameLayout {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static volatile int depthBits = 16;
    private final TouchLayer controls;
    private final GLSurfaceView gl;
    private final GuiOverlay gui;
    private final PlayHud hud;
    private boolean hudPosted;
    private long lastControls;
    private long lastOverlay;
    private final SceneRenderer renderer;
    private final Studio studio;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ViewportView(Context ctx, Studio studio) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(studio, "studio");
        this.studio = studio;
        this.gl = new GLSurfaceView(ctx);
        this.renderer = new SceneRenderer(this.studio);
        this.gui = new GuiOverlay(ctx, this.studio);
        this.controls = new TouchLayer(this, ctx);
        this.hud = new PlayHud(ctx, this.studio);
        this.gl.setEGLContextFactory(new Es3ContextFactory());
        this.gl.setEGLConfigChooser(new MsaaConfigChooser());
        this.gl.setPreserveEGLContextOnPause(false);
        this.gl.setRenderer(this.renderer);
        this.gl.setRenderMode(1);
        addView(this.gl, new FrameLayout.LayoutParams(-1, -1));
        addView(this.gui, new FrameLayout.LayoutParams(-1, -1));
        addView(this.controls, new FrameLayout.LayoutParams(-1, -1));
        addView(this.hud, new FrameLayout.LayoutParams(-1, -1));
        this.renderer.setOnFrame(new Function0() { // from class: com.goydevv.studrblx.ui.ViewportView$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ViewportView._init_$lambda$1(ViewportView.this);
            }
        });
        this.renderer.setSurfacePainter(new Function4() { // from class: com.goydevv.studrblx.ui.ViewportView$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function4
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return ViewportView._init_$lambda$2(ViewportView.this, (Instance) obj, ((Float) obj2).floatValue(), ((Float) obj3).floatValue(), (Bitmap) obj4);
            }
        });
    }

    public final GLSurfaceView getGl() {
        return this.gl;
    }

    public final SceneRenderer getRenderer() {
        return this.renderer;
    }

    public final GuiOverlay getGui() {
        return this.gui;
    }

    public final TouchLayer getControls() {
        return this.controls;
    }

    public final PlayHud getHud() {
        return this.hud;
    }

    static final Unit _init_$lambda$1(final ViewportView this$0) {
        ArrayList<Instance> children;
        long now = System.currentTimeMillis();
        if (now - this$0.lastOverlay > 33) {
            this$0.lastOverlay = now;
            Instance instanceFindService = this$0.studio.getModel().findService("CoreGui");
            boolean core = false;
            if (instanceFindService != null && (children = instanceFindService.getChildren()) != null && (!children.isEmpty())) {
                core = true;
            }
            if (this$0.gui.getHasContent() || this$0.studio.isPlaying() || !this$0.studio.getPluginWidgets().isEmpty() || this$0.gui.getSelectedGui() != null || core) {
                this$0.gui.postInvalidate();
            }
            if (this$0.controls.getBusy() || now - this$0.lastControls > (this$0.studio.isPlaying() ? 33 : 500)) {
                this$0.lastControls = now;
                this$0.controls.postInvalidate();
            }
            if (this$0.studio.isPlaying() && !this$0.hudPosted) {
                this$0.hudPosted = true;
                this$0.hud.post(new Runnable() { // from class: com.goydevv.studrblx.ui.ViewportView$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ViewportView.lambda$1$lambda$0(this$0);
                    }
                });
            }
        }
        return Unit.INSTANCE;
    }

    static final void lambda$1$lambda$0(ViewportView this$0) {
        this$0.hudPosted = false;
        this$0.hud.onFrame();
    }

    static final Unit _init_$lambda$2(ViewportView this$0, Instance g, float w, float h, Bitmap bmp) {
        Intrinsics.checkNotNullParameter(g, "g");
        Intrinsics.checkNotNullParameter(bmp, "bmp");
        this$0.gui.renderSurface(g, w, h, bmp);
        return Unit.INSTANCE;
    }

    public final void onResume() {
        this.gl.onResume();
    }

    public final void onPause() {
        this.gl.onPause();
        this.renderer.trimMemory();
    }

    /* JADX INFO: compiled from: ViewportView.kt */
    private static final class Es3ContextFactory implements GLSurfaceView.EGLContextFactory {
        @Override // android.opengl.GLSurfaceView.EGLContextFactory
        public javax.microedition.khronos.egl.EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) {
            javax.microedition.khronos.egl.EGLContext ctx = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, new int[]{12440, 3, 12344});
            if (ctx == null || ctx == EGL10.EGL_NO_CONTEXT) {
                ctx = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
            }
            return ctx;
        }

        @Override // android.opengl.GLSurfaceView.EGLContextFactory
        public void destroyContext(EGL10 egl, EGLDisplay display, javax.microedition.khronos.egl.EGLContext context) {
            egl.eglDestroyContext(display, context);
        }
    }

    private static final class MsaaConfigChooser implements GLSurfaceView.EGLConfigChooser {
        private static final EGLConfig chooseConfig$pick(EGL10 $egl, EGLDisplay $display, int samples, int depth) {
            List attrs = CollectionsKt.mutableListOf(12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, Integer.valueOf(depth), 12352, 4);
            if (samples > 0) {
                CollectionsKt.addAll(attrs, CollectionsKt.listOf(new Integer[]{12338, 1, 12337, Integer.valueOf(samples)}));
            }
            attrs.add(12344);
            int[] arr = CollectionsKt.toIntArray(attrs);
            int[] n = new int[1];
            if (!$egl.eglChooseConfig($display, arr, null, 0, n) || n[0] <= 0) {
                return null;
            }
            EGLConfig[] configs = new EGLConfig[n[0]];
            $egl.eglChooseConfig($display, arr, configs, n[0], n);
            int length = configs.length;
            for (int i = 0; i < length; i++) {
                EGLConfig eGLConfig = configs[i];
                if (eGLConfig != null) {
                    return eGLConfig;
                }
            }
            return null;
        }

        @Override // android.opengl.GLSurfaceView.EGLConfigChooser
        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            Intrinsics.checkNotNullParameter(egl, "egl");
            Intrinsics.checkNotNullParameter(display, "display");
            EGLConfig c = chooseConfig$pick(egl, display, 0, 24);
            if (c == null) {
                EGLConfig eGLConfigChooseConfig$pick = chooseConfig$pick(egl, display, 2, 24);
                if (eGLConfigChooseConfig$pick == null) {
                    EGLConfig eGLConfigChooseConfig$pick2 = chooseConfig$pick(egl, display, 0, 24);
                    if (eGLConfigChooseConfig$pick2 == null) {
                        EGLConfig eGLConfigChooseConfig$pick3 = chooseConfig$pick(egl, display, 4, 16);
                        if (eGLConfigChooseConfig$pick3 == null) {
                            c = chooseConfig$pick(egl, display, 2, 16);
                            if (c == null && (c = chooseConfig$pick(egl, display, 0, 16)) == null) {
                                throw new IllegalStateException("No suitable EGL config");
                            }
                        } else {
                            c = eGLConfigChooseConfig$pick3;
                        }
                    } else {
                        c = eGLConfigChooseConfig$pick2;
                    }
                } else {
                    c = eGLConfigChooseConfig$pick;
                }
            }
            int[] v = new int[1];
            if (egl.eglGetConfigAttrib(display, c, 12325, v)) {
                ViewportView.INSTANCE.setDepthBits(v[0]);
            }
            return c;
        }
    }

    /* JADX INFO: compiled from: ViewportView.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int getDepthBits() {
            return ViewportView.depthBits;
        }

        public final void setDepthBits(int i) {
            ViewportView.depthBits = i;
        }
    }

    /* JADX INFO: compiled from: ViewportView.kt */
    public final class TouchLayer extends View {
        private final HashMap<String, RectF> buttons;
        private final float d;
        private float joyCx;
        private float joyCy;
        private float joyX;
        private float joyY;
        private boolean jumpDown;
        private final Paint paint;
        private float pinchDist;
        private float[] pinchMid;
        private final HashMap<Integer, Ptr> ptrs;
        private final Paint text;
        final /* synthetic */ ViewportView this$0;

        /* JADX INFO: compiled from: ViewportView.kt */
        public static final /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[Tool.values().length];
                try {
                    iArr[Tool.MOVE.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Tool.SCALE.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Tool.ROTATE.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TouchLayer(ViewportView this$0, Context ctx) {
            super(ctx);
            Intrinsics.checkNotNullParameter(ctx, "ctx");
            this.this$0 = this$0;
            this.d = getResources().getDisplayMetrics().density;
            this.paint = new Paint(1);
            Paint paint = new Paint(1);
            paint.setColor(-1);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setFakeBoldText(true);
            this.text = paint;
            this.ptrs = new HashMap<>();
            this.pinchMid = new float[]{0.0f, 0.0f};
            this.buttons = new HashMap<>();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: compiled from: ViewportView.kt */
        abstract class Ptr {
            private final int id;
            private final long t0 = System.currentTimeMillis();
            private float x;
            private final float x0;
            private float y;
            private final float y0;

            public Ptr(int id, float x, float y) {
                this.id = id;
                this.x = x;
                this.y = y;
                this.x0 = this.x;
                this.y0 = this.y;
            }

            public final int getId() {
                return this.id;
            }

            public final long getT0() {
                return this.t0;
            }

            public final float getX() {
                return this.x;
            }

            public final float getX0() {
                return this.x0;
            }

            public final float getY() {
                return this.y;
            }

            public final float getY0() {
                return this.y0;
            }

            public final void setX(float f) {
                this.x = f;
            }

            public final void setY(float f) {
                this.y = f;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class Joy extends Ptr {
            public Joy(int id, float x, float y) {
                super(id, x, y);
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class Btn extends Ptr {
            private final String name;
            final /* synthetic */ TouchLayer this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Btn(TouchLayer this$0, int id, float x, float y, String name) {
                super(id, x, y);
                Intrinsics.checkNotNullParameter(name, "name");
                this.this$0 = this$0;
                this.name = name;
            }

            public final String getName() {
                return this.name;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: compiled from: ViewportView.kt */
        final class Cam extends Ptr {
            private boolean moved;
            private boolean playDown;

            public Cam(int id, float x, float y) {
                super(id, x, y);
            }

            public final boolean getMoved() {
                return this.moved;
            }

            public final boolean getPlayDown() {
                return this.playDown;
            }

            public final void setMoved(boolean z) {
                this.moved = z;
            }

            public final void setPlayDown(boolean z) {
                this.playDown = z;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class GuiPress extends Ptr {
            private boolean cancelled;
            private final GuiOverlay.Hit hit;
            private final GuiOverlay.Scroll scroll;
            final /* synthetic */ TouchLayer this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public GuiPress(TouchLayer this$0, int id, float x, float y, GuiOverlay.Hit hit, GuiOverlay.Scroll scroll) {
                super(id, x, y);
                Intrinsics.checkNotNullParameter(hit, "hit");
                this.this$0 = this$0;
                this.hit = hit;
                this.scroll = scroll;
            }

            public final boolean getCancelled() {
                return this.cancelled;
            }

            public final GuiOverlay.Hit getHit() {
                return this.hit;
            }

            public final GuiOverlay.Scroll getScroll() {
                return this.scroll;
            }

            public final void setCancelled(boolean z) {
                this.cancelled = z;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class GuiScroll extends Ptr {
            private final GuiOverlay.Scroll scroll;
            final /* synthetic */ TouchLayer this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public GuiScroll(TouchLayer this$0, int id, float x, float y, GuiOverlay.Scroll scroll) {
                super(id, x, y);
                Intrinsics.checkNotNullParameter(scroll, "scroll");
                this.this$0 = this$0;
                this.scroll = scroll;
            }

            public final GuiOverlay.Scroll getScroll() {
                return this.scroll;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class GuiDrag extends Ptr {
            private boolean active;
            private final GuiOverlay.DragState state;
            final /* synthetic */ TouchLayer this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public GuiDrag(TouchLayer this$0, int id, float x, float y, GuiOverlay.DragState state) {
                super(id, x, y);
                Intrinsics.checkNotNullParameter(state, "state");
                this.this$0 = this$0;
                this.state = state;
            }

            public final boolean getActive() {
                return this.active;
            }

            public final GuiOverlay.DragState getState() {
                return this.state;
            }

            public final void setActive(boolean z) {
                this.active = z;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class Sink extends Ptr {
            public Sink(int id, float x, float y) {
                super(id, x, y);
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class PluginWheel extends Ptr {
            private float acc;

            public PluginWheel(int id, float x, float y) {
                super(id, x, y);
            }

            public final float getAcc() {
                return this.acc;
            }

            public final void setAcc(float f) {
                this.acc = f;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class Drag extends Ptr {
            private final Vector3 center;
            private final Handle handle;
            private final int localAxis;
            private final List<Instance> parts;
            private final float pxPerStud;
            private final float[] screenDir;
            private final List<CFrame> startCf;
            private final List<Vector3> startSize;
            private final boolean startedOverlapping;
            private final Float t0Axis;
            final /* synthetic */ TouchLayer this$0;
            private final Tool tool;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            public Drag(TouchLayer this$0, int id, float x, float y, Handle handle, Tool tool, List<? extends Instance> parts, List<CFrame> startCf, List<Vector3> startSize, float pxPerStud, float[] screenDir, Vector3 center, int localAxis, Float t0Axis) {
                super(id, x, y);
                Intrinsics.checkNotNullParameter(handle, "handle");
                Intrinsics.checkNotNullParameter(tool, "tool");
                Intrinsics.checkNotNullParameter(parts, "parts");
                Intrinsics.checkNotNullParameter(startCf, "startCf");
                Intrinsics.checkNotNullParameter(startSize, "startSize");
                Intrinsics.checkNotNullParameter(screenDir, "screenDir");
                Intrinsics.checkNotNullParameter(center, "center");
                this.this$0 = this$0;
                this.handle = handle;
                this.tool = tool;
                this.parts = (List<Instance>) parts;
                this.startCf = startCf;
                this.startSize = startSize;
                this.pxPerStud = pxPerStud;
                this.screenDir = screenDir;
                this.center = center;
                this.localAxis = localAxis;
                this.t0Axis = t0Axis;
                this.startedOverlapping = this.tool == Tool.MOVE && this.this$0.this$0.studio.getCollisionsEnabled() && this.this$0.overlapsOthers(this.parts);
            }

            public final Handle getHandle() {
                return this.handle;
            }

            public final List<Instance> getParts() {
                return this.parts;
            }

            public final Tool getTool() {
                return this.tool;
            }

            public final float getPxPerStud() {
                return this.pxPerStud;
            }

            public final float[] getScreenDir() {
                return this.screenDir;
            }

            public final List<CFrame> getStartCf() {
                return this.startCf;
            }

            public final List<Vector3> getStartSize() {
                return this.startSize;
            }

            public final Vector3 getCenter() {
                return this.center;
            }

            public final int getLocalAxis() {
                return this.localAxis;
            }

            public final Float getT0Axis() {
                return this.t0Axis;
            }

            public final boolean getStartedOverlapping() {
                return this.startedOverlapping;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class PartDrag extends Ptr {
            private boolean active;
            private final Vector3 center;
            private final Vector3 grab;
            private final Vector3 half;
            private final List<Instance> parts;
            private final List<CFrame> startCf;
            final /* synthetic */ TouchLayer this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            public PartDrag(TouchLayer this$0, int id, float x, float y, List<? extends Instance> parts, List<CFrame> startCf, Vector3 grab, Vector3 center, Vector3 half) {
                super(id, x, y);
                Intrinsics.checkNotNullParameter(parts, "parts");
                Intrinsics.checkNotNullParameter(startCf, "startCf");
                Intrinsics.checkNotNullParameter(grab, "grab");
                Intrinsics.checkNotNullParameter(center, "center");
                Intrinsics.checkNotNullParameter(half, "half");
                this.this$0 = this$0;
                this.parts = (List<Instance>) parts;
                this.startCf = startCf;
                this.grab = grab;
                this.center = center;
                this.half = half;
            }

            public final List<Instance> getParts() {
                return this.parts;
            }

            public final List<CFrame> getStartCf() {
                return this.startCf;
            }

            public final boolean getActive() {
                return this.active;
            }

            public final Vector3 getCenter() {
                return this.center;
            }

            public final Vector3 getGrab() {
                return this.grab;
            }

            public final Vector3 getHalf() {
                return this.half;
            }

            public final void setActive(boolean z) {
                this.active = z;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: compiled from: ViewportView.kt */
        final class BoxSel extends Ptr {
            private final boolean additive;

            public BoxSel(int id, float x, float y, boolean additive) {
                super(id, x, y);
                this.additive = additive;
            }

            public final boolean getAdditive() {
                return this.additive;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class PluginPtr extends Ptr {
            private boolean moved;

            public PluginPtr(int id, float x, float y) {
                super(id, x, y);
            }

            public final boolean getMoved() {
                return this.moved;
            }

            public final void setMoved(boolean z) {
                this.moved = z;
            }
        }

        /* JADX INFO: compiled from: ViewportView.kt */
        private final class AdornDrag extends Ptr {
            private final Instance adorn;
            private final LuaValue arg;
            private final Float axis0;
            private final LuaRuntime rt;
            final /* synthetic */ TouchLayer this$0;
            private final AdornHandles.Tip tip;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public AdornDrag(TouchLayer this$0, int id, float x, float y, Instance adorn, AdornHandles.Tip tip, Float axis0, LuaRuntime rt, LuaValue arg) {
                super(id, x, y);
                Intrinsics.checkNotNullParameter(adorn, "adorn");
                Intrinsics.checkNotNullParameter(tip, "tip");
                Intrinsics.checkNotNullParameter(rt, "rt");
                Intrinsics.checkNotNullParameter(arg, "arg");
                this.this$0 = this$0;
                this.adorn = adorn;
                this.tip = tip;
                this.axis0 = axis0;
                this.rt = rt;
                this.arg = arg;
            }

            public final Instance getAdorn() {
                return this.adorn;
            }

            public final Float getAxis0() {
                return this.axis0;
            }

            public final AdornHandles.Tip getTip() {
                return this.tip;
            }

            public final LuaValue getArg() {
                return this.arg;
            }

            public final LuaRuntime getRt() {
                return this.rt;
            }
        }

        public final boolean getBusy() {
            return !this.ptrs.isEmpty() || this.this$0.studio.getFlyUp() || this.this$0.studio.getFlyDown();
        }

        private final float getJoyR() {
            return this.d * 56.0f;
        }

        private final float getDefaultJoyX() {
            return (this.d * 40.0f) + getJoyR();
        }

        private final float getDefaultJoyY() {
            return (getHeight() - (this.d * 40.0f)) - getJoyR();
        }

        private final boolean getActiveJoy() {
            Iterable iterableValues = this.ptrs.values();
            Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
            Iterable iterable = iterableValues;
            if (((Collection) iterable).isEmpty()) {
                return false;
            }
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                if (((Ptr) it.next()) instanceof Joy) {
                    return true;
                }
            }
            return false;
        }

        private final boolean getFixedStick() {
            return this.this$0.studio.isPlaying() && Intrinsics.areEqual(Settings.INSTANCE.getMovementMode(), "Thumbstick");
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Intrinsics.checkNotNullParameter(canvas, "canvas");
            this.buttons.clear();
            PlaySession play = this.this$0.studio.getPlay();
            if (!getActiveJoy() || getFixedStick()) {
                this.joyCx = getDefaultJoyX();
                this.joyCy = getDefaultJoyY();
            }
            if (play != null) {
                drawPlayControls(canvas, play);
            } else {
                drawEditControls(canvas);
            }
            if (Settings.INSTANCE.getShowFps()) {
                this.text.setTextSize(this.d * 11.0f);
                this.text.setTextAlign(Paint.Align.RIGHT);
                this.text.setColor(-1711276033);
                this.text.setFakeBoldText(false);
                canvas.drawText(MathKt.roundToInt(this.this$0.getRenderer().getFps()) + " FPS", getWidth() - (this.d * 8.0f), getHeight() - (this.d * 6.0f), this.text);
                this.text.setTextAlign(Paint.Align.CENTER);
                this.text.setColor(-1);
                this.text.setFakeBoldText(true);
            }
        }

        private final void drawThumbstick(Canvas canvas) {
            boolean active = getActiveJoy();
            float r = getJoyR();
            float kx = this.joyCx + (this.joyX * r);
            float ky = this.joyCy + (this.joyY * r);
            // Roblox PlayerModule DynamicThumbstick outer ring
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(active ? 0x36000000 : 0x24000000);
            canvas.drawCircle(this.joyCx, this.joyCy, r, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(this.d * 1.75f);
            this.paint.setColor(active ? 0x6EFFFFFF : 0x42FFFFFF);
            canvas.drawCircle(this.joyCx, this.joyCy, r, this.paint);
            // Roblox DynamicThumbstick middle directional trail dots when dragged
            float mag = (float) Math.hypot(this.joyX, this.joyY);
            if (active && mag > 0.18f) {
                this.paint.setStyle(Paint.Style.FILL);
                for (int di = 1; di <= 3; di++) {
                    float frac = di * 0.24f;
                    float dotR = this.d * (2.6f + di * 0.7f);
                    this.paint.setColor(0x58FFFFFF);
                    canvas.drawCircle(this.joyCx + (this.joyX * r * frac), this.joyCy + (this.joyY * r * frac), dotR, this.paint);
                }
            }
            // Inner thumbstick knob (Roblox mobile translucent disc + crisp subtle rim)
            float kr = r * 0.36f;
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(active ? 0xC8FFFFFF : 0x78FFFFFF);
            canvas.drawCircle(kx, ky, kr, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(this.d * 1.2f);
            this.paint.setColor(active ? 0xE8FFFFFF : 0x88FFFFFF);
            canvas.drawCircle(kx, ky, kr, this.paint);
        }

        /* JADX WARN: Code duplicated, block: B:41:0x0280  */
        private final void drawPlayControls(Canvas canvas, PlaySession play) {
            List<Instance> listEmptyList;
            String str;
            Canvas canvas2 = canvas;
            drawThumbstick(canvas);
            boolean smallScreen = Math.min(getWidth(), getHeight()) / this.d <= 500.0f;
            float jumpSize = this.d * (smallScreen ? 70.0f : 120.0f);
            float jr = jumpSize * 0.5f;
            float jumpLeft = getWidth() - (jumpSize * 1.5f - this.d * 10.0f);
            float jumpTop = getHeight() - (smallScreen ? jumpSize + this.d * 20.0f : jumpSize * 1.75f);
            float jx = jumpLeft + jr;
            float jy = jumpTop + jr;
            android.graphics.Bitmap jumpTex = RobloxTextures.get("ui/Input/TouchControlsSheetV2.png");
            if (jumpTex != null) {
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setColor(0xFFFFFFFF);
                this.paint.setFilterBitmap(true);
                Rect source = this.jumpDown ? new Rect(146, 146, 290, 290) : new Rect(1, 146, 145, 290);
                canvas2.drawBitmap(jumpTex, source, new RectF(jx - jr, jy - jr, jx + jr, jy + jr), this.paint);
            } else {
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setColor(this.jumpDown ? 0x88FFFFFF : 0x48000000);
                canvas2.drawCircle(jx, jy, jr, this.paint);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setStrokeWidth(this.d * 1.5f);
                this.paint.setColor(this.jumpDown ? 0xDDFFFFFF : 0x55FFFFFF);
                canvas2.drawCircle(jx, jy, jr, this.paint);
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setColor(this.jumpDown ? 0xFF1A1A1A : 0xF0FFFFFF);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setStrokeWidth(jr * 0.13f);
                this.paint.setStrokeCap(Paint.Cap.ROUND);
                this.paint.setStrokeJoin(Paint.Join.ROUND);
                Path path = new Path();
                path.moveTo(jx - (jr * 0.38f), jy + (jr * 0.16f));
                path.lineTo(jx, jy - (jr * 0.22f));
                path.lineTo(jx + (jr * 0.38f), jy + (jr * 0.16f));
                canvas2.drawPath(path, this.paint);
                this.paint.setStrokeCap(Paint.Cap.BUTT);
                this.paint.setStrokeJoin(Paint.Join.MITER);
            }
            this.paint.setStyle(Paint.Style.FILL);
            this.buttons.put("jump", new RectF(jx - (jr * 1.15f), jy - (jr * 1.15f), jx + (jr * 1.15f), (1.15f * jr) + jy));
            float ay = (jy - jr) - (this.d * 46.0f);
            if (play.getShiftLockAllowed()) {
                float sr = 21.0f * this.d;
                float sx = ((jx - jr) - (this.d * 14.0f)) - sr;
                float sy = (jy - (this.d * 6.0f));
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setColor(play.getShiftLock() ? 0xCC00A2FF : 0x48000000);
                canvas2.drawCircle(sx, sy, sr, this.paint);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setStrokeWidth(this.d * 1.5f);
                this.paint.setColor(play.getShiftLock() ? 0xFFFFFFFF : 0x55FFFFFF);
                canvas2.drawCircle(sx, sy, sr, this.paint);
                this.paint.setStrokeWidth(this.d * 2.2f);
                canvas2.drawArc(sx - (this.d * 5.5f), sy - (this.d * 11.0f), (this.d * 5.5f) + sx, (this.d * 1.0f) + sy, 180.0f, 180.0f, false, this.paint);
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setColor(-1);
                canvas2 = canvas;
                canvas2.drawRoundRect(sx - (this.d * 8.0f), sy - (this.d * 4.0f), sx + (this.d * 8.0f), sy + (this.d * 9.0f), this.d * 2.5f, this.d * 2.5f, this.paint);
                this.paint.setColor(play.getShiftLock() ? -16735489 : -13421773);
                canvas2.drawCircle(sx, sy + (this.d * 2.5f), this.d * 1.8f, this.paint);
                this.buttons.put("shiftlock", new RectF(sx - (sr * 1.2f), sy - (sr * 1.2f), (sr * 1.2f) + sx, sy + (1.2f * sr)));
            }
            float ay2 = ay;
            for (PlaySession.ActionButton actionButton : play.getActions().values()) {
                Intrinsics.checkNotNullExpressionValue(actionButton, "next(...)");
                PlaySession.ActionButton a = actionButton;
                circleButton(canvas2, "action:" + a.getName(), jx, ay2, this.d * 28.0f, StringsKt.take(a.getTitle(), 6), false);
                ay2 -= this.d * 66.0f;
            }
            float jx2 = jx;
            Instance activePrompt = play.getActivePrompt();
            if (activePrompt != null) {
                Object obj = activePrompt.get("ActionText");
                String str2 = obj instanceof String ? (String) obj : null;
                if (str2 != null) {
                    String str3 = str2;
                    if (str3.length() == 0) {
                        str3 = "Interact";
                    }
                    str = str3;
                    if (str == null) {
                        str = "Interact";
                    }
                } else {
                    str = "Interact";
                }
                Object obj2 = activePrompt.get("ObjectText");
                String str4 = obj2 instanceof String ? (String) obj2 : null;
                if (str4 == null) {
                    str4 = "";
                }
                this.text.setTextSize(this.d * 15.0f);
                float fMax = Math.max(this.text.measureText(str), this.text.measureText(str4) * 0.8f) + (this.d * 84.0f);
                RectF rectF = new RectF((getWidth() / 2.0f) - (fMax / 2.0f), getHeight() * 0.58f, (getWidth() / 2.0f) + (fMax / 2.0f), (getHeight() * 0.58f) + (this.d * 56.0f));
                this.paint.setColor(-1291845632);
                canvas2.drawRoundRect(rectF, this.d * 10.0f, this.d * 10.0f, this.paint);
                float f = rectF.left + (this.d * 30.0f);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setStrokeWidth(this.d * 2.0f);
                this.paint.setColor(-1);
                canvas2.drawCircle(f, rectF.centerY(), this.d * 16.0f, this.paint);
                this.paint.setStyle(Paint.Style.FILL);
                this.text.setTextSize(this.d * 13.0f);
                canvas2.drawText("Tap", f, rectF.centerY() + (this.d * 4.5f), this.text);
                this.text.setTextAlign(Paint.Align.LEFT);
                if (str4.length() > 0) {
                    this.text.setTextSize(this.d * 12.0f);
                    this.text.setColor(-3355444);
                    this.text.setFakeBoldText(false);
                    canvas2.drawText(str4, (this.d * 26.0f) + f, rectF.centerY() - (this.d * 6.0f), this.text);
                    this.text.setTextSize(this.d * 15.0f);
                    this.text.setColor(-1);
                    this.text.setFakeBoldText(true);
                    canvas2.drawText(str, (this.d * 26.0f) + f, rectF.centerY() + (this.d * 13.0f), this.text);
                } else {
                    this.text.setTextSize(this.d * 15.0f);
                    canvas2.drawText(str, (this.d * 26.0f) + f, rectF.centerY() + (this.d * 5.5f), this.text);
                }
                this.text.setTextAlign(Paint.Align.CENTER);
                this.buttons.put("prompt", rectF);
            }
            if (play.getCoreBackpack()) {
                synchronized (this.this$0.studio.getLock()) {
                    listEmptyList = play.backpackTools();
                }
            } else {
                listEmptyList = CollectionsKt.emptyList();
            }
            if (!listEmptyList.isEmpty()) {
                float slot = this.d * 58.0f;
                float total = (listEmptyList.size() * ((this.d * 6.0f) + slot)) - (this.d * 6.0f);
                float x = (getWidth() / 2.0f) - (total / 2.0f);
                int i = 0;
                for (Object obj3 : listEmptyList) {
                    int i2 = i + 1;
                    if (i < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    float total2 = total;
                    Instance instance = (Instance) obj3;
                    float slot2 = slot;
                    float jx3 = jx2;
                    float ay3 = ay2;
                    float ay4 = x + slot2;
                    List<Instance> list = listEmptyList;
                    RectF rectF2 = new RectF(x, (getHeight() - slot2) - (this.d * 10.0f), ay4, getHeight() - (this.d * 10.0f));
                    this.paint.setColor(-1728053248);
                    canvas2.drawRoundRect(rectF2, this.d * 6.0f, this.d * 6.0f, this.paint);
                    if (play.getEquipped() == instance) {
                        this.paint.setStyle(Paint.Style.STROKE);
                        this.paint.setStrokeWidth(this.d * 3.0f);
                        this.paint.setColor(-16735489);
                        canvas2.drawRoundRect(rectF2, this.d * 6.0f, this.d * 6.0f, this.paint);
                        this.paint.setStyle(Paint.Style.FILL);
                    }
                    this.text.setTextSize(this.d * 11.0f);
                    this.text.setTextAlign(Paint.Align.LEFT);
                    this.text.setFakeBoldText(false);
                    canvas2.drawText(String.valueOf(i + 1), rectF2.left + (this.d * 5.0f), rectF2.top + (this.d * 13.0f), this.text);
                    this.text.setTextAlign(Paint.Align.CENTER);
                    this.text.setFakeBoldText(true);
                    canvas2.drawText(StringsKt.take(instance.getName(), 9), rectF2.centerX(), rectF2.centerY() + (this.d * 8.0f), this.text);
                    this.buttons.put("tool:" + i, rectF2);
                    x += slot2 + (this.d * 6.0f);
                    i = i2;
                    total = total2;
                    slot = slot2;
                    jx2 = jx3;
                    ay2 = ay3;
                    listEmptyList = list;
                }
            }
        }

        private final void drawEditControls(Canvas canvas) {
            drawThumbstick(canvas);
            float bx = (getWidth() - (this.d * 30.0f)) - (this.d * 34.0f);
            arrowButton(canvas, "up", bx, getHeight() - (this.d * 170.0f), this.d * 32.0f, true, this.this$0.studio.getFlyUp());
            arrowButton(canvas, "down", bx, getHeight() - (this.d * 90.0f), this.d * 32.0f, false, this.this$0.studio.getFlyDown());
            circleButton(canvas, "speed", bx - (this.d * 80.0f), getHeight() - (this.d * 90.0f), this.d * 26.0f, "x" + StringsKt.take(DataTypesKt.fmtNum(this.this$0.studio.getFlySpeed()), 4), false);
            boxIconButton(canvas, "boxsel", bx - (this.d * 80.0f), getHeight() - (this.d * 170.0f), this.d * 24.0f, this.this$0.studio.getBoxSelect());
            multiIconButton(canvas, "multisel", bx - (this.d * 80.0f), getHeight() - (this.d * 234.0f), this.d * 24.0f, this.this$0.studio.getMultiSelect() || this.this$0.studio.getModifierHeld());
            insideIconButton(canvas, "insidesel", bx - (this.d * 144.0f), getHeight() - (this.d * 170.0f), this.d * 24.0f, this.this$0.studio.getSelectInsideModels());
            Iterable iterableValues = this.ptrs.values();
            Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
            Collection arrayList = new ArrayList();
            for (Object obj : iterableValues) {
                if (obj instanceof BoxSel) {
                    arrayList.add(obj);
                }
            }
            BoxSel boxSel = (BoxSel) CollectionsKt.firstOrNull((List) arrayList);
            if (boxSel != null) {
                RectF rectF = new RectF(Math.min(boxSel.getX0(), boxSel.getX()), Math.min(boxSel.getY0(), boxSel.getY()), Math.max(boxSel.getX0(), boxSel.getX()), Math.max(boxSel.getY0(), boxSel.getY()));
                this.paint.setStyle(Paint.Style.FILL);
                this.paint.setColor(773496831);
                canvas.drawRect(rectF, this.paint);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setStrokeWidth(this.d * 1.5f);
                this.paint.setColor(-15032321);
                canvas.drawRect(rectF, this.paint);
                this.paint.setStyle(Paint.Style.FILL);
            }
            Instance activePlugin = this.this$0.studio.getActivePlugin();
            if (activePlugin != null) {
                this.text.setTextSize(this.d * 12.0f);
                this.text.setFakeBoldText(false);
                String str = activePlugin.getName() + " is active  ✕";
                float fMeasureText = this.text.measureText(str) + (this.d * 24.0f);
                RectF rectF2 = new RectF((getWidth() / 2.0f) - (fMeasureText / 2.0f), this.d * 8.0f, (getWidth() / 2.0f) + (fMeasureText / 2.0f), this.d * 36.0f);
                this.paint.setColor(-871671121);
                canvas.drawRoundRect(rectF2, this.d * 14.0f, this.d * 14.0f, this.paint);
                this.text.setColor(-1);
                canvas.drawText(str, rectF2.centerX(), rectF2.centerY() + (this.d * 4.5f), this.text);
                this.text.setFakeBoldText(true);
                this.buttons.put("plugin_off", rectF2);
            }
        }

        private final void insideIconButton(Canvas c, String name, float x, float y, float r, boolean active) {
            circleButton(c, name, x, y, r, "", active);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(this.d * 1.6f);
            this.paint.setColor(-1);
            this.paint.setPathEffect(new DashPathEffect(new float[]{this.d * 3.0f, this.d * 2.5f}, 0.0f));
            c.drawRect(x - (r * 0.5f), y - (r * 0.42f), (0.5f * r) + x, (0.42f * r) + y, this.paint);
            this.paint.setPathEffect(null);
            this.paint.setStyle(Paint.Style.FILL);
            c.drawRect(x - (r * 0.08f), y - (r * 0.2f), x + (r * 0.3f), (r * 0.2f) + y, this.paint);
            Paint paint = this.paint;
            paint.setAlpha(120);
            Unit unit = Unit.INSTANCE;
            c.drawRect(x - (r * 0.34f), y - (r * 0.05f), x - (r * 0.16f), (r * 0.2f) + y, paint);
            this.paint.setAlpha(255);
        }

        private final void boxIconButton(Canvas c, String name, float x, float y, float r, boolean active) {
            circleButton(c, name, x, y, r, "", active);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(this.d * 2.0f);
            this.paint.setColor(-1);
            this.paint.setPathEffect(new DashPathEffect(new float[]{this.d * 4.0f, this.d * 3.0f}, 0.0f));
            c.drawRect(x - (r * 0.45f), y - (r * 0.38f), (0.45f * r) + x, (0.38f * r) + y, this.paint);
            this.paint.setPathEffect(null);
            this.paint.setStyle(Paint.Style.FILL);
        }

        private final void multiIconButton(Canvas c, String name, float x, float y, float r, boolean active) {
            circleButton(c, name, x, y, r, "", active);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(this.d * 2.0f);
            this.paint.setColor(-1);
            float s = r * 0.32f;
            c.drawRect(x - (s * 1.4f), y - (s * 1.4f), (s * 0.4f) + x, (s * 0.4f) + y, this.paint);
            this.paint.setStyle(Paint.Style.FILL);
            c.drawRect(x - (s * 0.4f), y - (0.4f * s), (s * 1.4f) + x, y + (1.4f * s), this.paint);
        }

        private final void arrowButton(Canvas c, String name, float x, float y, float r, boolean up, boolean active) {
            Path p;
            circleButton(c, name, x, y, r, "", active);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(this.d * 3.5f);
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setStrokeJoin(Paint.Join.ROUND);
            this.paint.setColor(-1);
            float s = r * 0.38f;
            if (up) {
                p = new Path();
                p.moveTo(x - s, (s * 0.5f) + y);
                p.lineTo(x, y - (0.6f * s));
                p.lineTo(x + s, (0.5f * s) + y);
            } else {
                p = new Path();
                p.moveTo(x - s, y - (s * 0.5f));
                p.lineTo(x, (0.6f * s) + y);
                p.lineTo(x + s, y - (0.5f * s));
            }
            c.drawPath(p, this.paint);
            this.paint.setStyle(Paint.Style.FILL);
        }

        private final void circleButton(Canvas c, String name, float x, float y, float r, String label, boolean active) {
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(active ? -871671121 : 1493172224);
            c.drawCircle(x, y, r, this.paint);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(this.d * 2.5f);
            this.paint.setColor(-855638017);
            c.drawCircle(x, y, r, this.paint);
            this.paint.setStyle(Paint.Style.FILL);
            if (label.length() > 0) {
                this.text.setTextSize(RangesKt.coerceAtMost(0.55f * r, this.d * 18.0f));
                c.drawText(label, x, (this.text.getTextSize() * 0.36f) + y, this.text);
            }
            this.buttons.put(name, new RectF(x - r, y - r, x + r, y + r));
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent e) {
            try {
                Intrinsics.checkNotNullParameter(e, "e");
                int idx = e.getActionIndex();
                switch (e.getActionMasked()) {
                    case 0:
                    case 5:
                        down(e.getPointerId(idx), e.getX(idx), e.getY(idx));
                        break;
                    case 1:
                    case 6:
                        up(e.getPointerId(idx), e.getX(idx), e.getY(idx), false);
                        break;
                    case 2:
                        int pointerCount = e.getPointerCount();
                        for (int i = 0; i < pointerCount; i++) {
                            Ptr p = this.ptrs.get(Integer.valueOf(e.getPointerId(i)));
                            if (p != null) {
                                move(p, e.getX(i), e.getY(i));
                            }
                        }
                        camGesture();
                        break;
                    case 3:
                        Set<Integer> setKeySet = this.ptrs.keySet();
                        Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
                        Iterator it = CollectionsKt.toList(setKeySet).iterator();
                        while (it.hasNext()) {
                            int id = ((Number) it.next()).intValue();
                            up(id, 0.0f, 0.0f, true);
                        }
                        break;
                }
                invalidate();
                return true;
            } catch (Throwable _t) {
                if (_t instanceof RuntimeException) throw (RuntimeException) _t;
                if (_t instanceof Error) throw (Error) _t;
                throw new RuntimeException(_t);
            }
        }

        private final boolean getAdditive() {
            return this.this$0.studio.getMultiSelect() || this.this$0.studio.getModifierHeld();
        }

        private final void down(final int id, float x, float y) {
            try {
            Object next;
            boolean z;
            GuiOverlay.DragState dragStateBeginDrag;
            boolean playing = this.this$0.studio.isPlaying();
            Iterable iterableEntrySet = this.buttons.entrySet();
            Intrinsics.checkNotNullExpressionValue(iterableEntrySet, "<get-entries>(...)");
            Iterator it = iterableEntrySet.iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!((RectF) ((Map.Entry) next).getValue()).contains(x, y));
            Map.Entry entry = (Map.Entry) next;
            if (entry != null) {
                Object key = entry.getKey();
                Intrinsics.checkNotNullExpressionValue(key, "component1(...)");
                String str = (String) key;
                this.ptrs.put(Integer.valueOf(id), new Btn(this, id, x, y, str));
                pressButton(str, true);
                return;
            }
            boolean z2 = false;
            if (playing) {
                GuiOverlay.Hit hit = this.this$0.getGui().buttonAt(x, y);
                if (hit != null) {
                    this.ptrs.put(Integer.valueOf(id), new GuiPress(this, id, x, y, hit, this.this$0.getGui().scrollAt(x, y)));
                    this.this$0.getGui().press(hit, x, y);
                    return;
                }
                GuiOverlay.Scroll scrollScrollAt = this.this$0.getGui().scrollAt(x, y);
                if (scrollScrollAt != null) {
                    this.ptrs.put(Integer.valueOf(id), new GuiScroll(this, id, x, y, scrollScrollAt));
                    return;
                }
                AdornDrag adornDragTryStartAdorn = tryStartAdorn(id, x, y);
                if (adornDragTryStartAdorn != null) {
                    this.ptrs.put(Integer.valueOf(id), adornDragTryStartAdorn);
                    return;
                }
            } else {
                GuiOverlay.Hit hit2 = this.this$0.getGui().buttonAt(x, y);
                if (hit2 != null && (hit2.getKind() == 4 || this.this$0.getGui().isPluginUi(hit2.getInst()))) {
                    this.ptrs.put(Integer.valueOf(id), new GuiPress(this, id, x, y, hit2, this.this$0.getGui().scrollAt(x, y)));
                    this.this$0.getGui().press(hit2, x, y);
                    return;
                }
                GuiOverlay.Scroll scrollScrollAt2 = this.this$0.getGui().scrollAt(x, y);
                if (scrollScrollAt2 != null) {
                    GuiOverlay.Scroll scroll = this.this$0.getGui().isPluginUi(scrollScrollAt2.getInst()) ? scrollScrollAt2 : null;
                    if (scroll != null) {
                        this.ptrs.put(Integer.valueOf(id), new GuiScroll(this, id, x, y, scroll));
                        return;
                    }
                }
                if (this.this$0.getGui().pluginUiAt(x, y)) {
                    GuiOverlay.Hit hitPluginFrameAt = this.this$0.getGui().pluginFrameAt(x, y);
                    if (hitPluginFrameAt == null) {
                        this.ptrs.put(Integer.valueOf(id), new PluginWheel(id, x, y));
                        return;
                    }
                    ViewportView viewportView = this.this$0;
                    this.ptrs.put(Integer.valueOf(id), new GuiPress(this, id, x, y, hitPluginFrameAt, null));
                    viewportView.getGui().press(hitPluginFrameAt, x, y);
                    return;
                }
                Instance selectedGui = this.this$0.getGui().getSelectedGui();
                if (selectedGui != null) {
                    ViewportView viewportView2 = this.this$0;
                    Integer numHandleAt = viewportView2.getGui().handleAt(selectedGui, x, y);
                    if (numHandleAt != null && (dragStateBeginDrag = viewportView2.getGui().beginDrag(selectedGui, numHandleAt.intValue())) != null) {
                        this.ptrs.put(Integer.valueOf(id), new GuiDrag(this, id, x, y, dragStateBeginDrag));
                        return;
                    }
                }
                AdornDrag adornDragTryStartAdorn2 = tryStartAdorn(id, x, y);
                if (adornDragTryStartAdorn2 != null) {
                    this.ptrs.put(Integer.valueOf(id), adornDragTryStartAdorn2);
                    return;
                }
                if (this.this$0.studio.getActivePlugin() != null) {
                    Iterable iterableValues = this.ptrs.values();
                    Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
                    Iterable iterable = iterableValues;
                    if (!((Collection) iterable).isEmpty()) {
                        Iterator it2 = iterable.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                z = true;
                                break;
                            } else if (((Ptr) it2.next()) instanceof PluginPtr) {
                                z = false;
                                break;
                            }
                        }
                    } else {
                        z = true;
                    }
                    if (z && this.this$0.studio.pluginMouseEvent("Button1Down", x, y)) {
                        this.ptrs.put(Integer.valueOf(id), new PluginPtr(id, x, y));
                        return;
                    }
                }
                Drag dragTryStartDrag = tryStartDrag(id, x, y);
                if (dragTryStartDrag != null) {
                    this.ptrs.put(Integer.valueOf(id), dragTryStartDrag);
                    return;
                }
                if (this.this$0.studio.getBoxSelect() && this.ptrs.isEmpty()) {
                    this.ptrs.put(Integer.valueOf(id), new BoxSel(id, x, y, getAdditive()));
                    return;
                }
                PartDrag partDragTryStartPartDrag = tryStartPartDrag(id, x, y);
                if (partDragTryStartPartDrag != null) {
                    this.ptrs.put(Integer.valueOf(id), partDragTryStartPartDrag);
                    return;
                }
            }
            if (getFixedStick()) {
                if (((float) Math.hypot(x - getDefaultJoyX(), y - getDefaultJoyY())) < getJoyR() * 1.6f) {
                    z2 = true;
                }
            } else if (x < getWidth() * 0.4f && y > getHeight() * 0.35f) {
                z2 = true;
            }
            boolean inStickArea = z2;
            if (!getActiveJoy() && inStickArea && (!playing || !this.this$0.getGui().blocksAt(x, y))) {
                if (!getFixedStick()) {
                    this.joyCx = RangesKt.coerceAtLeast(x, getJoyR() * 0.6f);
                    this.joyCy = RangesKt.coerceAtMost(y, getHeight() - (getJoyR() * 0.6f));
                }
                this.ptrs.put(Integer.valueOf(id), new Joy(id, x, y));
                setJoy(x, y);
                return;
            }
            if (playing && this.this$0.getGui().blocksAt(x, y)) {
                this.ptrs.put(Integer.valueOf(id), new Sink(id, x, y));
                return;
            }
            final Cam cam = new Cam(id, x, y);
            this.ptrs.put(Integer.valueOf(id), cam);
            if (playing) {
                cam.setPlayDown(true);
                Object lock = this.this$0.studio.getLock();
                ViewportView viewportView3 = this.this$0;
                synchronized (lock) {
                    PlaySession play = viewportView3.studio.getPlay();
                    if (play != null) {
                        play.pointerDown(x, y);
                        Unit unit = Unit.INSTANCE;
                    }
                }
            } else {
                final ViewportView viewportView4 = this.this$0;
                postDelayed(new Runnable() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ViewportView.TouchLayer.down$lambda$27(TouchLayer.this, id, cam, viewportView4);
                    }
                }, 420L);
            }
            resetPinch();
        }
            catch (Throwable _ktEx) {
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
            }
        }

        static final void down$lambda$27(TouchLayer this$0, int $id, Cam $cam, ViewportView this$1) {
            Ptr p = this$0.ptrs.get(Integer.valueOf($id));
            if (p == $cam && !$cam.getMoved() && this$0.ptrs.size() == 1 && !this$1.studio.isPlaying()) {
                HashMap<Integer, Ptr> map = this$0.ptrs;
                Integer numValueOf = Integer.valueOf($id);
                BoxSel boxSel = this$0.new BoxSel($id, $cam.getX(), $cam.getY(), this$0.getAdditive());
                boxSel.setX($cam.getX());
                boxSel.setY($cam.getY());
                map.put(numValueOf, boxSel);
                this$0.performHapticFeedback(0);
                this$0.invalidate();
            }
        }

        private final void move(Ptr p, float nx, float ny) {
            try {
            if (!(p instanceof Joy)) {
                if (!(p instanceof Drag)) {
                    if (p instanceof PartDrag) {
                        p.setX(nx);
                        p.setY(ny);
                        if (!((PartDrag) p).getActive() && ((float) Math.hypot(nx - p.getX0(), ny - p.getY0())) > this.d * 8.0f) {
                            ((PartDrag) p).setActive(true);
                        }
                        if (((PartDrag) p).getActive()) {
                            updatePartDrag((PartDrag) p);
                            return;
                        }
                        return;
                    }
                    if (!(p instanceof BoxSel)) {
                        if (!(p instanceof AdornDrag)) {
                            if (p instanceof PluginPtr) {
                                if (((float) Math.hypot(nx - p.getX0(), ny - p.getY0())) > this.d * 6.0f) {
                                    ((PluginPtr) p).setMoved(true);
                                }
                                p.setX(nx);
                                p.setY(ny);
                                if (this.ptrs.size() == 1) {
                                    this.this$0.studio.pluginMouseEvent("Move", nx, ny);
                                    return;
                                }
                                return;
                            }
                            if (p instanceof GuiDrag) {
                                p.setX(nx);
                                p.setY(ny);
                                if (!((GuiDrag) p).getActive() && ((float) Math.hypot(nx - p.getX0(), ny - p.getY0())) > this.d * 6.0f) {
                                    ((GuiDrag) p).setActive(true);
                                }
                                if (((GuiDrag) p).getActive()) {
                                    this.this$0.getGui().dragTo(((GuiDrag) p).getState(), nx - p.getX0(), ny - p.getY0());
                                    return;
                                }
                                return;
                            }
                            if (!(p instanceof GuiScroll)) {
                                if (p instanceof PluginWheel) {
                                    PluginWheel pluginWheel = (PluginWheel) p;
                                    pluginWheel.setAcc(pluginWheel.getAcc() + (ny - p.getY()));
                                    p.setX(nx);
                                    p.setY(ny);
                                    float step = this.this$0.studio.guiUnit() * 18.0f;
                                    while (Math.abs(((PluginWheel) p).getAcc()) >= step) {
                                        int z = ((PluginWheel) p).getAcc() > 0.0f ? 1 : -1;
                                        PluginWheel pluginWheel2 = (PluginWheel) p;
                                        pluginWheel2.setAcc(pluginWheel2.getAcc() - (z * step));
                                        this.this$0.getGui().pluginWheel(p.getX0(), p.getY0(), z);
                                    }
                                    return;
                                }
                                int i = 0;
                                if (p instanceof GuiPress) {
                                    if (!((GuiPress) p).getCancelled() && ((float) Math.hypot(nx - p.getX0(), ny - p.getY0())) > this.d * 12.0f) {
                                        GuiOverlay.Scroll s = ((GuiPress) p).getScroll();
                                        if (s == null) {
                                            if (!this.this$0.studio.isPlaying() && Math.abs(ny - p.getY0()) > Math.abs(nx - p.getX0()) && this.this$0.getGui().canWheel(((GuiPress) p).getHit(), p.getX0(), p.getY0())) {
                                                ((GuiPress) p).setCancelled(true);
                                                this.this$0.getGui().release(((GuiPress) p).getHit(), nx, ny, false);
                                                HashMap<Integer, Ptr> map = this.ptrs;
                                                Integer numValueOf = Integer.valueOf(p.getId());
                                                PluginWheel pluginWheel3 = new PluginWheel(p.getId(), p.getX0(), p.getY0());
                                                pluginWheel3.setX(nx);
                                                pluginWheel3.setY(ny);
                                                pluginWheel3.setAcc(ny - p.getY0());
                                                map.put(numValueOf, pluginWheel3);
                                                return;
                                            }
                                        } else {
                                            ((GuiPress) p).setCancelled(true);
                                            this.this$0.getGui().release(((GuiPress) p).getHit(), nx, ny, false);
                                            GuiScroll g = new GuiScroll(this, p.getId(), p.getX(), p.getY(), s);
                                            this.ptrs.put(Integer.valueOf(p.getId()), g);
                                            this.this$0.getGui().scrollBy(s, nx - p.getX(), ny - p.getY());
                                            g.setX(nx);
                                            g.setY(ny);
                                            return;
                                        }
                                    }
                                    p.setX(nx);
                                    p.setY(ny);
                                    if (((GuiPress) p).getCancelled() || this.this$0.studio.isPlaying()) {
                                        return;
                                    }
                                    this.this$0.getGui().pluginPointerMove(((GuiPress) p).getHit(), nx, ny);
                                    return;
                                }
                                if (p instanceof Cam) {
                                    float dx = nx - p.getX();
                                    float dy = ny - p.getY();
                                    if (((float) Math.hypot(nx - p.getX0(), ny - p.getY0())) > this.d * 10.0f) {
                                        ((Cam) p).setMoved(true);
                                    }
                                    p.setX(nx);
                                    p.setY(ny);
                                    Iterable iterableValues = this.ptrs.values();
                                    Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
                                    Iterable iterable = iterableValues;
                                    if (!((Collection) iterable).isEmpty()) {
                                        i = 0;
                                        Iterator it = iterable.iterator();
                                        while (it.hasNext()) {
                                            if ((((Ptr) it.next()) instanceof Cam) && (i = i + 1) < 0) {
                                                CollectionsKt.throwCountOverflow();
                                            }
                                        }
                                    }
                                    if (i == 1 && ((Cam) p).getMoved()) {
                                        Object lock = this.this$0.studio.getLock();
                                        ViewportView viewportView = this.this$0;
                                        synchronized (lock) {
                                            PlaySession play = viewportView.studio.getPlay();
                                            if (play != null) {
                                                play.cameraDrag(dx, dy);
                                                play.pointerMove(nx, ny);
                                            } else {
                                                viewportView.studio.rotateEditCamera((-dx) * 0.005f, (-dy) * 0.005f);
                                            }
                                            Unit unit = Unit.INSTANCE;
                                        }
                                        return;
                                    }
                                    return;
                                }
                                p.setX(nx);
                                p.setY(ny);
                                return;
                            }
                            this.this$0.getGui().scrollBy(((GuiScroll) p).getScroll(), nx - p.getX(), ny - p.getY());
                            p.setX(nx);
                            p.setY(ny);
                            return;
                        }
                        p.setX(nx);
                        p.setY(ny);
                        dragAdorn((AdornDrag) p);
                        return;
                    }
                    p.setX(nx);
                    p.setY(ny);
                    return;
                }
                p.setX(nx);
                p.setY(ny);
                updateDrag((Drag) p);
                return;
            }
            p.setX(nx);
            p.setY(ny);
            setJoy(nx, ny);
        }
            catch (Throwable _ktEx) {
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
            }
        }

        private final void up(int id, float x, float y, boolean cancel) {
            try {
            Ptr p = this.ptrs.remove(Integer.valueOf(id));
            if (p == null) {
                return;
            }
            if (p instanceof Joy) {
                this.joyX = 0.0f;
                this.joyY = 0.0f;
                this.this$0.studio.setMoveX(0.0f);
                this.this$0.studio.setMoveY(0.0f);
                return;
            }
            boolean z = false;
            if (!(p instanceof Btn)) {
                if (!(p instanceof Drag)) {
                    if (p instanceof PartDrag) {
                        if (((PartDrag) p).getActive()) {
                            finishPartDrag((PartDrag) p);
                            return;
                        } else {
                            if (!cancel) {
                                tap(x, y);
                                return;
                            }
                            return;
                        }
                    }
                    if (p instanceof BoxSel) {
                        if (!cancel) {
                            finishBox((BoxSel) p);
                            return;
                        }
                        return;
                    }
                    if (p instanceof AdornDrag) {
                        synchronized (this.this$0.studio.getLock()) {
                            ((AdornDrag) p).getRt().fire(((AdornDrag) p).getAdorn(), "MouseButton1Up", ((AdornDrag) p).getArg());
                            Unit unit = Unit.INSTANCE;
                        }
                        return;
                    }
                    if (p instanceof PluginPtr) {
                        this.this$0.studio.pluginMouseEvent("Button1Up", cancel ? p.getX() : x, cancel ? p.getY() : y);
                        return;
                    }
                    if (p instanceof GuiDrag) {
                        this.this$0.getGui().endDrag();
                        if (!((GuiDrag) p).getActive()) {
                            if (!cancel) {
                                tap(x, y);
                                return;
                            }
                            return;
                        }
                        finishGuiDrag((GuiDrag) p);
                        return;
                    }
                    if (p instanceof GuiPress) {
                        if (!((GuiPress) p).getCancelled()) {
                            GuiOverlay gui = this.this$0.getGui();
                            GuiOverlay.Hit hit = ((GuiPress) p).getHit();
                            if (!cancel && ((GuiPress) p).getHit().getRect().contains(x, y)) {
                                z = true;
                            }
                            gui.release(hit, x, y, z);
                            return;
                        }
                        return;
                    }
                    if (p instanceof Cam) {
                        if (((Cam) p).getPlayDown()) {
                            Object lock = this.this$0.studio.getLock();
                            ViewportView viewportView = this.this$0;
                            synchronized (lock) {
                                PlaySession play = viewportView.studio.getPlay();
                                if (play != null) {
                                    play.pointerUp(cancel ? p.getX() : x, cancel ? p.getY() : y);
                                    Unit unit2 = Unit.INSTANCE;
                                }
                            }
                        }
                        if (!cancel && !((Cam) p).getMoved() && System.currentTimeMillis() - p.getT0() < 400) {
                            Iterable iterableValues = this.ptrs.values();
                            Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
                            Iterable iterable = iterableValues;
                            if (!((Collection) iterable).isEmpty()) {
                                Iterator it = iterable.iterator();
                                do {
                                    if (!it.hasNext()) {
                                        z = true;
                                        break;
                                    }
                                } while (!(((Ptr) it.next()) instanceof Cam));
                            } else {
                                z = true;
                            }
                            if (z) {
                                tap(x, y);
                            }
                        }
                        resetPinch();
                        return;
                    }
                    return;
                }
                finishDrag((Drag) p);
                return;
            }
            pressButton(((Btn) p).getName(), false);
        }
            catch (Throwable _ktEx) {
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
            }
        }

        private final void finishGuiDrag(GuiDrag p) {
            GuiOverlay.DragState s = p.getState();
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                viewportView.studio.getUndo().begin(s.getHandle() == -1 ? "Move GUI" : "Resize GUI");
                Object obj = s.getInst().get("Position");
                Object obj2 = s.getInst().get("Size");
                if (!Intrinsics.areEqual(obj, s.getPos())) {
                    viewportView.studio.getUndo().record(new UndoManager.Op.Prop(s.getInst(), "Position", s.getPos(), obj, true));
                }
                if (!Intrinsics.areEqual(obj2, s.getSize())) {
                    viewportView.studio.getUndo().record(new UndoManager.Op.Prop(s.getInst(), "Size", s.getSize(), obj2, true));
                }
                viewportView.studio.getUndo().commit();
                viewportView.studio.markDirty();
                Unit unit = Unit.INSTANCE;
            }
        }

        private final void setJoy(float x, float y) {
            float dx = (x - this.joyCx) / getJoyR();
            float dy = (y - this.joyCy) / getJoyR();
            float m = (float) Math.hypot(dx, dy);
            if (m > 1.0f) {
                if (!getFixedStick()) {
                    float over = (m - 1.0f) * getJoyR();
                    this.joyCx += (dx / m) * over;
                    this.joyCy += (dy / m) * over;
                }
                dx /= m;
                dy /= m;
            }
            this.joyX = dx;
            this.joyY = dy;
            this.this$0.studio.setMoveX(dx);
            this.this$0.studio.setMoveY(dy);
        }

        private final void pressButton(String name, boolean down) {
            List<Instance> listBackpackTools;
            Instance instance;
            PlaySession play = this.this$0.studio.getPlay();
            if (Intrinsics.areEqual(name, "up")) {
                this.this$0.studio.setFlyUp(down);
                return;
            }
            if (Intrinsics.areEqual(name, "down")) {
                this.this$0.studio.setFlyDown(down);
                return;
            }
            if (Intrinsics.areEqual(name, "speed") && down) {
                post(new Runnable() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ViewportView.TouchLayer.pressButton$lambda$36(TouchLayer.this);
                    }
                });
                return;
            }
            if (Intrinsics.areEqual(name, "boxsel") && down) {
                final ViewportView viewportView = this.this$0;
                post(new Runnable() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        ViewportView.TouchLayer.pressButton$lambda$37(viewportView);
                    }
                });
                return;
            }
            if (Intrinsics.areEqual(name, "multisel") && down) {
                final ViewportView viewportView2 = this.this$0;
                post(new Runnable() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        ViewportView.TouchLayer.pressButton$lambda$38(viewportView2);
                    }
                });
                return;
            }
            if (Intrinsics.areEqual(name, "insidesel") && down) {
                final ViewportView viewportView3 = this.this$0;
                post(new Runnable() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        ViewportView.TouchLayer.pressButton$lambda$39(viewportView3);
                    }
                });
                return;
            }
            if (Intrinsics.areEqual(name, "plugin_off") && down) {
                Object lock = this.this$0.studio.getLock();
                ViewportView viewportView4 = this.this$0;
                synchronized (lock) {
                    viewportView4.studio.deactivatePlugin(null);
                    Unit unit = Unit.INSTANCE;
                }
                return;
            }
            if (Intrinsics.areEqual(name, "jump")) {
                this.jumpDown = down;
                synchronized (this.this$0.studio.getLock()) {
                    if (play != null) {
                        play.setJumpHeld(down);
                        Unit unit2 = Unit.INSTANCE;
                    }
                }
                return;
            }
            if (Intrinsics.areEqual(name, "shiftlock") && down) {
                synchronized (this.this$0.studio.getLock()) {
                    if (play != null) {
                        play.toggleShiftLock();
                        Unit unit3 = Unit.INSTANCE;
                    }
                }
                return;
            }
            if (Intrinsics.areEqual(name, "prompt") && down) {
                synchronized (this.this$0.studio.getLock()) {
                    if (play != null) {
                        play.triggerPrompt();
                        Unit unit4 = Unit.INSTANCE;
                    }
                }
                return;
            }
            if (StringsKt.startsWith(name, "action:", false)) {
                synchronized (this.this$0.studio.getLock()) {
                    if (play != null) {
                        play.pressAction(StringsKt.removePrefix(name, (CharSequence) "action:"), down);
                        Unit unit5 = Unit.INSTANCE;
                    }
                }
                return;
            }
            if (StringsKt.startsWith(name, "tool:", false) && down) {
                synchronized (this.this$0.studio.getLock()) {
                    int i = Integer.parseInt(StringsKt.removePrefix(name, (CharSequence) "tool:"));
                    if (play != null && (listBackpackTools = play.backpackTools()) != null && (instance = (Instance) CollectionsKt.getOrNull(listBackpackTools, i)) != null) {
                        play.toggleTool(instance);
                        Unit unit6 = Unit.INSTANCE;
                    }
                }
            }
        }

        static final void pressButton$lambda$36(final TouchLayer this$0) {
            SettingsDialog settingsDialog = SettingsDialog.INSTANCE;
            Context context = this$0.getContext();
            Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
            settingsDialog.pickFlySpeed(context, new Function0() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return ViewportView.TouchLayer.pressButton$lambda$36$lambda$35(this$0);
                }
            });
        }

        static final Unit pressButton$lambda$36$lambda$35(TouchLayer this$0) {
            this$0.invalidate();
            return Unit.INSTANCE;
        }

        static final void pressButton$lambda$37(ViewportView this$0) {
            this$0.studio.setBoxSelect(!this$0.studio.getBoxSelect());
            this$0.studio.toast(this$0.studio.getBoxSelect() ? "Box select: drag to select (long-press works any time)" : "Box select off");
        }

        static final void pressButton$lambda$38(ViewportView this$0) {
            this$0.studio.setMultiSelect(!this$0.studio.getMultiSelect());
            this$0.studio.toast(this$0.studio.getMultiSelect() ? "Multi-select: taps add or remove" : "Multi-select off");
        }

        static final void pressButton$lambda$39(ViewportView this$0) {
            this$0.studio.setSelectInsideModels(!this$0.studio.getSelectInsideModels());
            this$0.studio.toast(this$0.studio.getSelectInsideModels() ? "Select inside models: taps pick single parts in groups" : "Taps select whole models again");
        }

        private final void resetPinch() {
            this.pinchDist = 0.0f;
        }

        private final void camGesture() {
            try {
            char c;
            char c2;
            Iterable iterableValues = this.ptrs.values();
            Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
            Collection arrayList = new ArrayList();
            Iterator it = iterableValues.iterator();
            while (true) {
                c = 0;
                c2 = 1;
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                Ptr ptr = (Ptr) next;
                if ((((ptr instanceof Cam) || (ptr instanceof PluginPtr) || (ptr instanceof BoxSel)) ? (char) 1 : (char) 0) != 0) {
                    arrayList.add(next);
                }
            }
            List cams = (List) arrayList;
            if (cams.size() < 2) {
                return;
            }
            for (Object obj : cams) {
                Intrinsics.checkNotNullExpressionValue(obj, "next(...)");
                Ptr c3 = (Ptr) obj;
                if (c3 instanceof PluginPtr) {
                    HashMap<Integer, Ptr> map = this.ptrs;
                    Integer numValueOf = Integer.valueOf(c3.getId());
                    Cam cam = new Cam(c3.getId(), c3.getX(), c3.getY());
                    cam.setMoved(true);
                    map.put(numValueOf, cam);
                    this.this$0.studio.pluginMouseEvent("Button1Up", c3.getX(), c3.getY());
                }
                if (c3 instanceof BoxSel) {
                    HashMap<Integer, Ptr> map2 = this.ptrs;
                    Integer numValueOf2 = Integer.valueOf(c3.getId());
                    Cam cam2 = new Cam(c3.getId(), c3.getX(), c3.getY());
                    cam2.setMoved(true);
                    map2.put(numValueOf2, cam2);
                }
            }
            Iterable iterableValues2 = this.ptrs.values();
            Intrinsics.checkNotNullExpressionValue(iterableValues2, "<get-values>(...)");
            Collection arrayList2 = new ArrayList();
            for (Object obj2 : iterableValues2) {
                if (obj2 instanceof Cam) {
                    arrayList2.add(obj2);
                }
            }
            List pair = (List) arrayList2;
            if (pair.size() < 2) {
                return;
            }
            Cam a = (Cam) pair.get(0);
            Cam b = (Cam) pair.get(1);
            a.setMoved(true);
            b.setMoved(true);
            float dist = (float) Math.hypot(a.getX() - b.getX(), a.getY() - b.getY());
            float mx = (a.getX() + b.getX()) / 2.0f;
            float my = (a.getY() + b.getY()) / 2.0f;
            if (this.pinchDist > 0.0f) {
                float zoom = dist / this.pinchDist;
                float pdx = mx - this.pinchMid[0];
                float pdy = my - this.pinchMid[1];
                Object lock = this.this$0.studio.getLock();
                ViewportView viewportView = this.this$0;
                synchronized (lock) {
                    try {
                        PlaySession play = viewportView.studio.getPlay();
                        if (play == null) {
                            FlyCamera camera = viewportView.studio.getCamera();
                            float fCoerceAtMost = RangesKt.coerceAtMost(viewportView.studio.getFlySpeed(), 8.0f) * 0.08f;
                            camera.setPos(camera.getPos().plus(camera.getForward().times((dist - this.pinchDist) * fCoerceAtMost)));
                            try {
                                camera.setPos(camera.getPos().plus(camera.getRight().times((-pdx) * fCoerceAtMost).plus(camera.getUp().times(pdy * fCoerceAtMost))));
                            } catch (Throwable th) {
                                throw th;
                            }
                        } else {
                            try {
                                play.cameraZoom(zoom);
                            } catch (Throwable th2) {
                                Throwable th = th2;
                                throw th;
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                    } catch (Throwable th3) {
                        Throwable th = th3;
                    }
                }
            } else {
                c = 0;
                c2 = 1;
            }
            this.pinchDist = dist;
            float[] fArr = new float[2];
            fArr[c] = mx;
            fArr[c2] = my;
            this.pinchMid = fArr;
        }
            catch (Throwable _ktEx) {
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
            }
        }

        private final Instance selectTarget(Instance part) {
            if (this.this$0.studio.getSelectInsideModels()) {
                return part;
            }
            Instance instance = null;
            Instance ws = this.this$0.studio.getModel().getWorkspace();
            for (Instance a = part.getParent(); a != null && a != ws; a = a.getParent()) {
                if (Intrinsics.areEqual(a.getClassName(), "Model") || a.isA("PartOperation")) {
                    instance = a;
                }
            }
            if (instance != null) {
                Iterable selection = this.this$0.studio.getSelection();
                boolean z = true;
                if (!(selection instanceof Collection) || !((Collection) selection).isEmpty()) {
                    Iterator it = selection.iterator();
                    while (it.hasNext()) {
                        if (((Instance) it.next()) == instance) {
                            z = false;
                            break;
                        }
                    }
                }
                if (z) {
                    return instance;
                }
            }
            return part;
        }

        private final void tap(float x, float y) {
            Instance target;
            Instance part;
            if (this.this$0.getGui().tap(x, y)) {
                return;
            }
            PlaySession play = this.this$0.studio.getPlay();
            if (play != null) {
                synchronized (this.this$0.studio.getLock()) {
                    play.tap(x, y);
                    Unit unit = Unit.INSTANCE;
                }
                return;
            }
            if (this.this$0.studio.getActivePlugin() == null || !this.this$0.studio.getPluginMice().containsKey(this.this$0.studio.getActivePlugin())) {
                Instance instancePick = this.this$0.getGui().pick(x, y);
                if (instancePick == null) {
                    Object lock = this.this$0.studio.getLock();
                    ViewportView viewportView = this.this$0;
                    synchronized (lock) {
                        Pair<Vector3, Vector3> pairScreenRay = viewportView.studio.getView().screenRay(x, y);
                        RayHit rayHitRaycast = Geometry.INSTANCE.raycast(viewportView.studio.renderParts(), pairScreenRay.component1(), pairScreenRay.component2(), 5000.0f, new Function1() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda8
                            @Override // kotlin.jvm.functions.Function1
                            public final Object invoke(Object obj) {
                                return Boolean.valueOf(ViewportView.TouchLayer.tap$lambda$56$lambda$54((Instance) obj));
                            }
                        });
                        target = (rayHitRaycast == null || (part = rayHitRaycast.getPart()) == null) ? null : selectTarget(part);
                    }
                    if (target == null) {
                        if (!getAdditive()) {
                            this.this$0.studio.select(CollectionsKt.emptyList());
                            return;
                        }
                        return;
                    } else if (getAdditive()) {
                        this.this$0.studio.toggleSelect(target);
                        return;
                    } else {
                        this.this$0.studio.select(CollectionsKt.listOf(target));
                        return;
                    }
                }
                ViewportView viewportView2 = this.this$0;
                boolean additive = getAdditive();
                Studio studio = viewportView2.studio;
                if (additive) {
                    studio.toggleSelect(instancePick);
                } else {
                    studio.select(CollectionsKt.listOf(instancePick));
                }
            }
        }

        static final boolean tap$lambda$56$lambda$54(Instance it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return !Intrinsics.areEqual(it.get("Locked"), (Object) true);
        }

        private final void finishBox(BoxSel b) {
            try {
            boolean z;
            float[] fArrProject;
            float l = Math.min(b.getX0(), b.getX());
            float r = Math.max(b.getX0(), b.getX());
            float t = Math.min(b.getY0(), b.getY());
            float bt = Math.max(b.getY0(), b.getY());
            if (r - l < this.d * 6.0f && bt - t < this.d * 6.0f) {
                return;
            }
            ArrayList picked = new ArrayList();
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                try {
                    Set setNewSetFromMap = Collections.newSetFromMap(new IdentityHashMap());
                    for (Instance instance : viewportView.studio.renderParts()) {
                        try {
                            if (!Intrinsics.areEqual(instance.get("Locked"), (Object) true) && (fArrProject = viewportView.studio.getView().project(PartData.INSTANCE.cframe(instance).getPosition())) != null) {
                                if (fArrProject[0] >= l && fArrProject[0] <= r && fArrProject[1] >= t) {
                                    if (fArrProject[1] <= bt) {
                                        if (fArrProject.length > 2) {
                                            try {
                                                if (fArrProject[2] > 1.0f) {
                                                    continue;
                                                }
                                            } catch (Throwable th) {
                                                throw th;
                                            }
                                        }
                                        Instance instance2 = instance;
                                        Instance parent = instance.getParent();
                                        Instance workspace = viewportView.studio.getModel().getWorkspace();
                                        if (!viewportView.studio.getSelectInsideModels()) {
                                            while (parent != null && parent != workspace) {
                                                ViewportView viewportView2 = viewportView;
                                                float l2 = l;
                                                try {
                                                    if (Intrinsics.areEqual(parent.getClassName(), "Model") || parent.isA("PartOperation")) {
                                                        instance2 = parent;
                                                    }
                                                    parent = parent.getParent();
                                                    viewportView = viewportView2;
                                                    l = l2;
                                                } catch (Throwable th2) {
                                                    Throwable th = th2;
                                                    throw th;
                                                }
                                            }
                                        }
                                        ViewportView viewportView3 = viewportView;
                                        float l3 = l;
                                        if (setNewSetFromMap.add(instance2)) {
                                            picked.add(instance2);
                                        }
                                        viewportView = viewportView3;
                                        l = l3;
                                    }
                                }
                            }
                        } catch (Throwable th3) {
                            Throwable th = th3;
                        }
                    }
                    try {
                        Unit unit = Unit.INSTANCE;
                        if (b.getAdditive()) {
                            Studio studio = this.this$0.studio;
                            List list = CollectionsKt.toList(this.this$0.studio.getSelection());
                            ViewportView viewportView4 = this.this$0;
                            Collection arrayList = new ArrayList();
                            for (Object obj : picked) {
                                Instance instance3 = (Instance) obj;
                                float r2 = r;
                                Iterable selection = viewportView4.studio.getSelection();
                                float t2 = t;
                                if ((selection instanceof Collection) && ((Collection) selection).isEmpty()) {
                                    z = true;
                                } else {
                                    Iterator it = selection.iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            z = true;
                                            break;
                                        }
                                        Iterable iterable = selection;
                                        if (((Instance) it.next()) == instance3) {
                                            z = false;
                                            break;
                                        }
                                        selection = (List) iterable;
                                    }
                                }
                                if (z) {
                                    arrayList.add(obj);
                                }
                                r = r2;
                                t = t2;
                            }
                            studio.select(CollectionsKt.plus((Collection) list, arrayList));
                        } else {
                            this.this$0.studio.select(picked);
                        }
                        if (picked.isEmpty()) {
                            return;
                        }
                        this.this$0.studio.toast("Selected " + picked.size() + " object" + (picked.size() == 1 ? "" : "s"));
                    } catch (Throwable th4) {
                        Throwable th = th4;
                        throw th;
                    }
                } catch (Throwable th5) {
                    Throwable th = th5;
                }
            }
        }
            catch (Throwable _ktEx) {
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
            }
        }

        private final Float axisParam(Vector3 center, Vector3 axis, float x, float y) {
            Pair<Vector3, Vector3> pairScreenRay = this.this$0.studio.getView().screenRay(x, y);
            Vector3 o = pairScreenRay.component1();
            Vector3 dir = pairScreenRay.component2();
            Vector3 w0 = center.minus(o);
            float b = axis.dot(dir);
            float denom = 1.0f - (b * b);
            if (denom < 0.02f) {
                return null;
            }
            return Float.valueOf(((dir.dot(w0) * b) - axis.dot(w0)) / denom);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private final Drag tryStartDrag(int id, float x, float y) {
            boolean z;
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                HandleSet handleSetCompute = Handles.INSTANCE.compute(viewportView.studio);
                if (handleSetCompute == null) {
                    return null;
                }
                float f = this.d * 40.0f;
                Iterator<Handle> it = handleSetCompute.getHandles().iterator();
                Handle handle = null;
                float f2 = f;
                while (true) {
                    z = true;
                    if (!it.hasNext()) {
                        break;
                    }
                    Handle next = it.next();
                    float[] fArrProject = viewportView.studio.getView().project(next.getTip());
                    if (fArrProject != null) {
                        float fHypot = (float) Math.hypot(fArrProject[0] - x, fArrProject[1] - y);
                        if (fHypot < f2) {
                            f2 = fHypot;
                            handle = next;
                        }
                    }
                }
                if (handle == null) {
                    return null;
                }
                Handle handle2 = handle;
                List<Instance> listSelectedParts = viewportView.studio.selectedParts();
                if (handleSetCompute.getTool() == Tool.SCALE && listSelectedParts.size() != 1) {
                    return null;
                }
                float[] fArrProject2 = viewportView.studio.getView().project(handleSetCompute.getCenter());
                if (fArrProject2 == null) {
                    return null;
                }
                float[] fArrProject3 = viewportView.studio.getView().project(handleSetCompute.getCenter().plus(handle2.getAxis()));
                if (fArrProject3 == null) {
                    return null;
                }
                float f3 = fArrProject3[0] - fArrProject2[0];
                float f4 = fArrProject3[1] - fArrProject2[1];
                float fHypot2 = (float) Math.hypot(f3, f4);
                if (fHypot2 < 0.5f) {
                    f3 = 0.0f;
                    f4 = -1.0f;
                    fHypot2 = 1.0f;
                }
                float f5 = f3;
                float f6 = f4;
                Float fAxisParam = handleSetCompute.getTool() != Tool.ROTATE ? axisParam(handleSetCompute.getCenter(), handle2.getAxis(), x, y) : null;
                boolean objArr = false;
                Tool tool = handleSetCompute.getTool();
                List<Instance> list = listSelectedParts;
                ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    arrayList.add(PartData.INSTANCE.cframe((Instance) it2.next()));
                }
                boolean objArr2 = objArr;
                boolean z2 = z;
                ArrayList arrayList2 = arrayList;
                List<Instance> list2 = listSelectedParts;
                ArrayList arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
                Iterator it3 = list2.iterator();
                while (it3.hasNext()) {
                    arrayList3.add(PartData.INSTANCE.size((Instance) it3.next()));
                }
                float[] fArr = new float[2];
                fArr[objArr2 ? 1 : 0] = f5 / fHypot2;
                fArr[z2 ? 1 : 0] = f6 / fHypot2;
                return new Drag(this, id, x, y, handle2, tool, listSelectedParts, arrayList2, arrayList3, fHypot2, fArr, handleSetCompute.getCenter(), handle2.getIndex() % 3, fAxisParam);
            }
        }

        private final float snap(float v, float step) {
            return (!this.this$0.studio.getSnapEnabled() || step <= 0.0f) ? v : MathKt.roundToInt(v / step) * step;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean overlapsOthers(List<? extends Instance> parts) {
            Pair<Vector3, Float> pairContact;
            Vector3 push;
            Set moved = Collections.newSetFromMap(new IdentityHashMap());
            Iterator it = parts.iterator();
            while (it.hasNext()) {
                moved.add((Instance) it.next());
            }
            List<Instance> listRenderParts = this.this$0.studio.renderParts();
            for (Instance p : parts) {
                Pair<Vector3, Vector3> pairPartAabb = Geometry.INSTANCE.partAabb(p);
                Vector3 a = pairPartAabb.component1();
                Vector3 b = pairPartAabb.component2();
                ConvexShape shape = Geometry.shapeOf$default(Geometry.INSTANCE, p, null, 2, null);
                for (Instance o : listRenderParts) {
                    if (!moved.contains(o) && !Intrinsics.areEqual(o.get("CanCollide"), (Object) false)) {
                        Pair<Vector3, Vector3> pairPartAabb2 = Geometry.INSTANCE.partAabb(o);
                        Vector3 oa = pairPartAabb2.component1();
                        Vector3 ob = pairPartAabb2.component2();
                        if (Geometry.INSTANCE.aabbOverlap(a, b, oa, ob, -0.03f) && (pairContact = Geometry.INSTANCE.contact(shape, a, b, o)) != null && (push = pairContact.getFirst()) != null && push.getMagnitude() > 0.03f) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private final void updateDrag(Drag p) {
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                int i = 0;
                Float fAxisParam = p.getT0Axis() != null ? axisParam(p.getCenter(), p.getHandle().getAxis(), p.getX(), p.getY()) : null;
                float x = (fAxisParam == null || p.getT0Axis() == null) ? (((p.getX() - p.getX0()) * p.getScreenDir()[0]) + ((p.getY() - p.getY0()) * p.getScreenDir()[1])) / p.getPxPerStud() : RangesKt.coerceIn(fAxisParam.floatValue() - p.getT0Axis().floatValue(), -2000.0f, 2000.0f);
                float f = 2.0f;
                float f2 = 0.0f;
                switch (WhenMappings.$EnumSwitchMapping$0[p.getTool().ordinal()]) {
                    case 1:
                        float fSnap = snap(x, viewportView.studio.getMoveSnap());
                        int i2 = 0;
                        for (Object obj : p.getParts()) {
                            int i3 = i2 + 1;
                            if (i2 < 0) {
                                CollectionsKt.throwIndexOverflow();
                            }
                            Instance.set$default((Instance) obj, "CFrame", p.getStartCf().get(i2).plus(p.getHandle().getAxis().times(fSnap)), false, 4, null);
                            i2 = i3;
                        }
                        if (viewportView.studio.getCollisionsEnabled()) {
                            if (!(fSnap == 0.0f) && !p.getStartedOverlapping() && overlapsOthers(p.getParts())) {
                                float f3 = 0.0f;
                                float f4 = 1.0f;
                                int i4 = 0;
                                while (i4 < 10) {
                                    float f5 = (f3 + f4) / f;
                                    int i5 = 0;
                                    for (Object obj2 : p.getParts()) {
                                        int i6 = i5 + 1;
                                        if (i5 < 0) {
                                            CollectionsKt.throwIndexOverflow();
                                        }
                                        Instance.set$default((Instance) obj2, "CFrame", p.getStartCf().get(i5).plus(p.getHandle().getAxis().times(fSnap * f5)), false, 4, null);
                                        i5 = i6;
                                    }
                                    ViewportView viewportView2 = viewportView;
                                    float f6 = f3;
                                    float f7 = f2;
                                    if (overlapsOthers(p.getParts())) {
                                        f4 = f5;
                                        f3 = f6;
                                    } else {
                                        f3 = f5;
                                    }
                                    i4++;
                                    f2 = f7;
                                    viewportView = viewportView2;
                                    f = 2.0f;
                                }
                                ViewportView viewportView3 = viewportView;
                                float f8 = f2;
                                float fTruncate = fSnap * f3;
                                if (viewportView3.studio.getSnapEnabled() && viewportView3.studio.getMoveSnap() > f8) {
                                    fTruncate = MathKt.truncate(fTruncate / viewportView3.studio.getMoveSnap()) * viewportView3.studio.getMoveSnap();
                                }
                                Iterable parts = p.getParts();
                                int i7 = 0;
                                for (Object obj3 : parts) {
                                    int i8 = i7 + 1;
                                    if (i7 < 0) {
                                        CollectionsKt.throwIndexOverflow();
                                    }
                                    Instance.set$default((Instance) obj3, "CFrame", p.getStartCf().get(i7).plus(p.getHandle().getAxis().times(fTruncate)), false, 4, null);
                                    i7 = i8;
                                }
                            }
                        }
                        break;
                    case 2:
                        Instance instance = p.getParts().get(0);
                        float fSnap2 = snap(x, viewportView.studio.getMoveSnap());
                        Vector3 vector3 = p.getStartSize().get(0);
                        float x2 = vector3.getX();
                        float y = vector3.getY();
                        float z = vector3.getZ();
                        float[] fArr = new float[3];
                        fArr[0] = x2;
                        fArr[1] = y;
                        fArr[2] = z;
                        float f9 = fArr[p.getLocalAxis()];
                        fArr[p.getLocalAxis()] = RangesKt.coerceAtLeast(f9 + fSnap2, 0.05f);
                        float f10 = fArr[p.getLocalAxis()] - f9;
                        PartData.INSTANCE.setSize(instance, new Vector3(fArr[0], fArr[1], fArr[2]));
                        Instance.set$default(instance, "CFrame", p.getStartCf().get(0).plus(p.getHandle().getAxis().times(f10 / 2.0f)), false, 4, null);
                        break;
                    case 3:
                        float x3 = ((((p.getX() - p.getX0()) * p.getScreenDir()[0]) + ((p.getY() - p.getY0()) * p.getScreenDir()[1])) / p.getPxPerStud()) * 12.0f;
                        CFrame cFrameTimes = CFrame.INSTANCE.at(p.getCenter()).times(CFrame.INSTANCE.fromAxisAngle(p.getHandle().getAxis(), (float) Math.toRadians((!viewportView.studio.getRotSnapEnabled() || viewportView.studio.getRotSnap() <= 0.0f) ? x3 : MathKt.roundToInt(x3 / viewportView.studio.getRotSnap()) * viewportView.studio.getRotSnap()))).times(CFrame.INSTANCE.at(p.getCenter().unaryMinus()));
                        int i9 = 0;
                        for (Object obj4 : p.getParts()) {
                            int i10 = i9 + 1;
                            if (i9 < 0) {
                                CollectionsKt.throwIndexOverflow();
                            }
                            Instance.set$default((Instance) obj4, "CFrame", cFrameTimes.times(p.getStartCf().get(i9)).orthonormalize(), false, 4, null);
                            i9 = i10;
                        }
                        break;
                }
                Unit unit = Unit.INSTANCE;
            }
        }

        private final void finishDrag(Drag p) {
            String str;
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                UndoManager undo = viewportView.studio.getUndo();
                switch (WhenMappings.$EnumSwitchMapping$0[p.getTool().ordinal()]) {
                    case 1:
                        str = "Move";
                        break;
                    case 2:
                        str = "Resize";
                        break;
                    default:
                        str = "Rotate";
                        break;
                }
                undo.begin(str);
                int i = 0;
                for (Object obj : p.getParts()) {
                    int i2 = i + 1;
                    if (i < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    Instance instance = (Instance) obj;
                    CFrame cframe = PartData.INSTANCE.cframe(instance);
                    if (!Intrinsics.areEqual(cframe, p.getStartCf().get(i))) {
                        viewportView.studio.getUndo().record(new UndoManager.Op.Prop(instance, "CFrame", p.getStartCf().get(i), cframe, true));
                    }
                    String str2 = instance.getProps().containsKey("size") ? "size" : "Size";
                    Vector3 size = PartData.INSTANCE.size(instance);
                    if (!Intrinsics.areEqual(size, p.getStartSize().get(i))) {
                        viewportView.studio.getUndo().record(new UndoManager.Op.Prop(instance, str2, p.getStartSize().get(i), size, true));
                    }
                    i = i2;
                }
                ViewportView viewportView2 = viewportView;
                viewportView2.studio.getUndo().commit();
                viewportView2.studio.markDirty();
                Unit unit = Unit.INSTANCE;
            }
        }

        private final LuaRuntime runtimeFor(Instance a) {
            DataModel root = a.root();
            if (root == null) {
                return null;
            }
            PlaySession play = this.this$0.studio.getPlay();
            if (play != null && root == play.getDm()) {
                return play.getRt();
            }
            if (root == this.this$0.studio.getEditModel()) {
                return this.this$0.studio.getEditRuntime();
            }
            return null;
        }

        private final AdornDrag tryStartAdorn(int id, float x, float y) {
            Iterator<Instance> it;
            char c;
            Iterator<AdornHandles.Tip> it2;
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                float f = this.d * 34.0f;
                Iterator<Instance> it3 = viewportView.studio.renderAdornments().iterator();
                Pair pair = null;
                float f2 = f;
                while (true) {
                    char c2 = 1;
                    char c3 = 0;
                    if (!it3.hasNext()) {
                        break;
                    }
                    Instance next = it3.next();
                    if (AdornHandles.INSTANCE.isHandles(next) && next.getParent() != null) {
                        Iterator<AdornHandles.Tip> it4 = AdornHandles.INSTANCE.tips(next, viewportView.studio.getView()).iterator();
                        while (it4.hasNext()) {
                            AdornHandles.Tip next2 = it4.next();
                            float[] fArrProject = viewportView.studio.getView().project(next2.getTip());
                            if (fArrProject == null) {
                                it = it3;
                                c = c3;
                                it2 = it4;
                            } else {
                                c = c3;
                                it2 = it4;
                                it = it3;
                                float fHypot = (float) Math.hypot(fArrProject[c3] - x, fArrProject[c2] - y);
                                if (fHypot < f2) {
                                    f2 = fHypot;
                                    pair = TuplesKt.to(next, next2);
                                    c3 = c;
                                    it4 = it2;
                                    it3 = it;
                                    c2 = 1;
                                }
                            }
                            c3 = c;
                            it4 = it2;
                            it3 = it;
                            c2 = 1;
                        }
                    } else {
                    }
                }
                if (pair == null) {
                    return null;
                }
                Instance instance = (Instance) pair.component1();
                AdornHandles.Tip tip = (AdornHandles.Tip) pair.component2();
                LuaRuntime luaRuntimeRuntimeFor = runtimeFor(instance);
                if (luaRuntimeRuntimeFor == null) {
                    return null;
                }
                LuaTypes luaTypes = LuaTypes.INSTANCE;
                LuaValue luaValueWrap = Intrinsics.areEqual(instance.getClassName(), "Handles") ? luaTypes.wrap(new EnumItemV("NormalId", AdornHandles.INSTANCE.getNORMAL_NAMES().get(tip.getId()), tip.getId())) : luaTypes.wrap(new EnumItemV("Axis", AdornHandles.INSTANCE.getAXIS_NAMES().get(tip.getId()), tip.getId()));
                luaRuntimeRuntimeFor.fire(instance, "MouseButton1Down", luaValueWrap);
                Float fAxisParam = null;
                if (Intrinsics.areEqual(instance.getClassName(), "Handles")) {
                    fAxisParam = axisParam(tip.getBase(), tip.getAxis(), x, y);
                }
                return new AdornDrag(this, id, x, y, instance, tip, fAxisParam, luaRuntimeRuntimeFor, luaValueWrap);
            }
        }

        private final void dragAdorn(AdornDrag p) {
            float x;
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                if (!Intrinsics.areEqual(p.getAdorn().getClassName(), "Handles")) {
                    float[] fArrProject = viewportView.studio.getView().project(p.getTip().getBase());
                    if (fArrProject == null) {
                        return;
                    }
                    float fAtan2 = ((float) Math.atan2(p.getY() - fArrProject[1], p.getX() - fArrProject[0])) - ((float) Math.atan2(p.getY0() - fArrProject[1], p.getX0() - fArrProject[0]));
                    while (fAtan2 > 3.141592653589793d) {
                        fAtan2 -= 6.2831855f;
                    }
                    while (fAtan2 < -3.141592653589793d) {
                        fAtan2 += 6.2831855f;
                    }
                    float f = p.getTip().getAxis().dot(viewportView.studio.getCamera().getForward()) > 0.0f ? 1.0f : -1.0f;
                    LuaRuntime rt = p.getRt();
                    Instance adorn = p.getAdorn();
                    LuaNumber luaNumberValueOf = LuaValue.valueOf(fAtan2 * f);
                    Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
                    LuaNumber luaNumberValueOf2 = LuaValue.valueOf(0.0d);
                    Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
                    rt.fire(adorn, "MouseDrag", p.getArg(), luaNumberValueOf, luaNumberValueOf2);
                } else {
                    Float fAxisParam = axisParam(p.getTip().getBase(), p.getTip().getAxis(), p.getX(), p.getY());
                    if (fAxisParam == null || p.getAxis0() == null) {
                        float[] fArrProject2 = viewportView.studio.getView().project(p.getTip().getBase());
                        if (fArrProject2 == null) {
                            return;
                        }
                        float[] fArrProject3 = viewportView.studio.getView().project(p.getTip().getBase().plus(p.getTip().getAxis()));
                        if (fArrProject3 == null) {
                            return;
                        }
                        float f2 = fArrProject3[0] - fArrProject2[0];
                        float f3 = fArrProject3[1] - fArrProject2[1];
                        float fCoerceAtLeast = RangesKt.coerceAtLeast((float) Math.hypot(f2, f3), 0.5f);
                        x = (((p.getX() - p.getX0()) * f2) + ((p.getY() - p.getY0()) * f3)) / (fCoerceAtLeast * fCoerceAtLeast);
                    } else {
                        x = fAxisParam.floatValue() - p.getAxis0().floatValue();
                    }
                    LuaRuntime rt2 = p.getRt();
                    Instance adorn2 = p.getAdorn();
                    LuaNumber luaNumberValueOf3 = LuaValue.valueOf(x);
                    Intrinsics.checkNotNullExpressionValue(luaNumberValueOf3, "valueOf(...)");
                    rt2.fire(adorn2, "MouseDrag", p.getArg(), luaNumberValueOf3);
                }
                Unit unit = Unit.INSTANCE;
            }
        }

        /* JADX WARN: Code duplicated, block: B:51:0x0100  */
        private final PartDrag tryStartPartDrag(int id, float x, float y) {
            PartDrag partDrag;
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            PartDrag partDrag2 = null;
            if (this.this$0.studio.getTool() == Tool.ROTATE && Handles.INSTANCE.compute(this.this$0.studio) == null) {
                return null;
            }
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                Pair<Vector3, Vector3> pairScreenRay = viewportView.studio.getView().screenRay(x, y);
                RayHit rayHitRaycast = Geometry.INSTANCE.raycast(viewportView.studio.renderParts(), pairScreenRay.component1(), pairScreenRay.component2(), 5000.0f, new Function1() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Boolean.valueOf(ViewportView.TouchLayer.tryStartPartDrag$lambda$80$lambda$75((Instance) obj));
                    }
                });
                if (rayHitRaycast == null) {
                    return null;
                }
                Iterable selection = viewportView.studio.getSelection();
                Iterable iterable = selection;
                if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
                    partDrag = null;
                    z2 = false;
                    z = true;
                } else {
                    Iterator it = iterable.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            partDrag = partDrag2;
                            z = true;
                            z2 = false;
                            break;
                        }
                        partDrag = partDrag2;
                        z = true;
                        if (((Instance) it.next()) == rayHitRaycast.getPart()) {
                            z2 = true;
                            break;
                        }
                        partDrag2 = partDrag;
                    }
                }
                if (z2) {
                    z3 = z;
                } else {
                    Iterable iterable2 = selection;
                    if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
                        Iterator it2 = iterable2.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                z4 = false;
                                break;
                            }
                            Instance instance = (Instance) it2.next();
                            if ((instance.getIsService() || !rayHitRaycast.getPart().isDescendantOf(instance)) ? false : z) {
                                z4 = z;
                                break;
                            }
                        }
                    } else {
                        z4 = false;
                    }
                    if (z4) {
                        z3 = z;
                    } else {
                        z3 = false;
                    }
                }
                if (!z3) {
                    return partDrag;
                }
                Iterable iterableSelectedParts = viewportView.studio.selectedParts();
                Collection arrayList = new ArrayList();
                for (Object obj : iterableSelectedParts) {
                    Iterable iterable3 = iterableSelectedParts;
                    if (!Intrinsics.areEqual(((Instance) obj).get("Locked"), Boolean.valueOf(z))) {
                        arrayList.add(obj);
                    }
                    iterableSelectedParts = iterable3;
                }
                List list = (List) arrayList;
                if (list.isEmpty()) {
                    return partDrag;
                }
                Pair<Vector3, Vector3> pairBoundsOf = Geometry.INSTANCE.boundsOf(list);
                if (pairBoundsOf == null) {
                    return partDrag;
                }
                Vector3 vector3Component1 = pairBoundsOf.component1();
                Vector3 vector3Component2 = pairBoundsOf.component2();
                List list2 = list;
                Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
                Iterator it3 = list2.iterator();
                while (it3.hasNext()) {
                    arrayList2.add(PartData.INSTANCE.cframe((Instance) it3.next()));
                }
                return new PartDrag(this, id, x, y, list, (List) arrayList2, rayHitRaycast.getPosition(), vector3Component1.plus(vector3Component2).div(2.0f), vector3Component2.minus(vector3Component1).div(2.0f));
            }
        }

        static final boolean tryStartPartDrag$lambda$80$lambda$75(Instance it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return !Intrinsics.areEqual(it.get("Locked"), (Object) true);
        }

        private final void updatePartDrag(PartDrag p) {
            Vector3 vector3;
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                Pair<Vector3, Vector3> pairScreenRay = viewportView.studio.getView().screenRay(p.getX(), p.getY());
                Vector3 vector3Component1 = pairScreenRay.component1();
                Vector3 vector3Component2 = pairScreenRay.component2();
                final Set setNewSetFromMap = Collections.newSetFromMap(new IdentityHashMap());
                Iterator it = p.getParts().iterator();
                while (it.hasNext()) {
                    setNewSetFromMap.add((Instance) it.next());
                }
                RayHit rayHitRaycast = Geometry.INSTANCE.raycast(viewportView.studio.renderParts(), vector3Component1, vector3Component2, 5000.0f, new Function1() { // from class: com.goydevv.studrblx.ui.ViewportView$TouchLayer$$ExternalSyntheticLambda7
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Boolean.valueOf(ViewportView.TouchLayer.updatePartDrag$lambda$85$lambda$83(setNewSetFromMap, (Instance) obj));
                    }
                });
                Vector3 vector3Minus = p.getCenter().minus(p.getGrab());
                if (rayHitRaycast != null) {
                    Vector3 normal = rayHitRaycast.getNormal();
                    Vector3 vector3Abs = normal.abs();
                    int i = (vector3Abs.getY() < vector3Abs.getX() || vector3Abs.getY() < vector3Abs.getZ()) ? vector3Abs.getX() >= vector3Abs.getZ() ? 0 : 2 : 1;
                    float f = normal.get(i) >= 0.0f ? 1.0f : -1.0f;
                    Vector3 vector3Plus = rayHitRaycast.getPosition().plus(new Vector3(i == 0 ? 0.0f : vector3Minus.getX(), i == 1 ? 0.0f : vector3Minus.getY(), i == 2 ? 0.0f : vector3Minus.getZ())).plus(new Vector3(i == 0 ? p.getHalf().getX() * f : 0.0f, i == 1 ? p.getHalf().getY() * f : 0.0f, i == 2 ? p.getHalf().getZ() * f : 0.0f));
                    if (!viewportView.studio.getSnapEnabled() || viewportView.studio.getMoveSnap() <= 0.0f) {
                        vector3 = vector3Plus;
                    } else {
                        float moveSnap = viewportView.studio.getMoveSnap();
                        vector3 = new Vector3(i == 0 ? vector3Plus.getX() : updatePartDrag$lambda$85$sn(this, moveSnap, vector3Plus.getX(), p.getHalf().getX()), i == 1 ? vector3Plus.getY() : updatePartDrag$lambda$85$sn(this, moveSnap, vector3Plus.getY(), p.getHalf().getY()), i == 2 ? vector3Plus.getZ() : updatePartDrag$lambda$85$sn(this, moveSnap, vector3Plus.getZ(), p.getHalf().getZ()));
                    }
                } else {
                    if (Math.abs(vector3Component2.getY()) < 1.0E-4f) {
                        return;
                    }
                    float y = (p.getGrab().getY() - vector3Component1.getY()) / vector3Component2.getY();
                    if (y < 0.0f) {
                        return;
                    }
                    Vector3 vector3Plus2 = vector3Component1.plus(vector3Component2.times(y)).plus(vector3Minus);
                    if (viewportView.studio.getSnapEnabled() && viewportView.studio.getMoveSnap() > 0.0f) {
                        vector3Plus2 = new Vector3(snap(vector3Plus2.getX(), viewportView.studio.getMoveSnap()), vector3Plus2.getY(), snap(vector3Plus2.getZ(), viewportView.studio.getMoveSnap()));
                    }
                    vector3 = vector3Plus2;
                }
                Vector3 vector3Minus2 = vector3.minus(p.getCenter());
                int i2 = 0;
                for (Object obj : p.getParts()) {
                    int i3 = i2 + 1;
                    if (i2 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    Instance.set$default((Instance) obj, "CFrame", p.getStartCf().get(i2).plus(vector3Minus2), false, 4, null);
                    i2 = i3;
                }
                Unit unit = Unit.INSTANCE;
            }
        }

        static final boolean updatePartDrag$lambda$85$lambda$83(Set $dragged, Instance it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return !$dragged.contains(it) && PartData.INSTANCE.transparency(it) < 0.99f;
        }

        private static final float updatePartDrag$lambda$85$sn(TouchLayer this$0, float s, float v, float h) {
            return this$0.snap(v - h, s) + h;
        }

        private final void finishPartDrag(PartDrag p) {
            Object lock = this.this$0.studio.getLock();
            ViewportView viewportView = this.this$0;
            synchronized (lock) {
                viewportView.studio.getUndo().begin("Drag");
                int i = 0;
                for (Object obj : p.getParts()) {
                    int i2 = i + 1;
                    if (i < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    Instance instance = (Instance) obj;
                    CFrame cframe = PartData.INSTANCE.cframe(instance);
                    if (!Intrinsics.areEqual(cframe, p.getStartCf().get(i))) {
                        viewportView.studio.getUndo().record(new UndoManager.Op.Prop(instance, "CFrame", p.getStartCf().get(i), cframe, true));
                    }
                    i = i2;
                }
                ViewportView viewportView2 = viewportView;
                viewportView2.studio.getUndo().commit();
                viewportView2.studio.markDirty();
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
