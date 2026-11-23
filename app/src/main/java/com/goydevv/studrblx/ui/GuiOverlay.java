package com.goydevv.studrblx.ui;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import androidx.core.view.ViewCompat;
import androidx.emoji2.text.EmojiCompat;
import com.goydevv.studrblx.core.PluginWidget;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.lua.EnumItemV;
import com.goydevv.studrblx.lua.LuaRuntime;
import com.goydevv.studrblx.lua.LuaTypes;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.ColorKey;
import com.goydevv.studrblx.model.ColorSequence;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.NumberKey;
import com.goydevv.studrblx.model.NumberSequence;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.UDim;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.ImageLoader;
import com.goydevv.studrblx.render.FlyCamera;
import com.goydevv.studrblx.render.ViewState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.sequences.SequencesKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/* JADX INFO: compiled from: GuiOverlay.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class GuiOverlay extends View {
    private static final Regex WS_REGEX = new Regex("\\s+");
    public static final int BUTTON = 0;
    public static final int CORE_MENU = 3;
    public static final int SINK = 2;
    public static final int TEXTBOX = 1;
    public static final float TOPBAR_INSET = 58.0f;
    public static final int WIDGET_CLOSE = 4;
    private final ArrayList<Pair<Instance, String>> absChanged;
    private float absInset;
    private int absRounds;
    private float absUnit;
    private long billboardScan;
    private List<? extends Instance> billboards;
    private IdentityHashMap<Instance, EditBox> boxes;
    private final float d;
    private final Paint fill;
    private volatile List<float[]> guides;
    private volatile boolean hasContent;
    private ArrayList<Hit> hits;
    private final Paint imgPaint;
    private Vector3 lastPointer;
    private final LinkedHashMap<TKey, StaticLayout> layouts;
    private int emojiLoadState = -1;
    private IdentityHashMap<Instance, EditBox> newBoxes;
    private ArrayList<Hit> newHits;
    private ArrayList<Pair<Instance, RectF>> newPicks;
    private ArrayList<Scroll> newScrolls;
    private Function2<? super Float, ? super Float, Unit> onCoreMenu;
    private ArrayList<Pair<Instance, RectF>> picks;
    private final IdentityHashMap<Hit, List<Hit>> pressExtras;
    private Instance pressed;
    private LuaRuntime rt;
    private final LinkedHashMap<SKey, Float> scaledSizes;
    private ArrayList<Scroll> scrolls;
    private final Paint stroke;
    private final Studio studio;
    private final TextPaint textPaint;

    /* JADX INFO: compiled from: GuiOverlay.kt */
    public static final class Hit {
        private final Instance inst;
        private final int kind;
        private final RectF rect;
        private final LuaRuntime rt;

        public Hit(Instance inst, RectF rect, LuaRuntime rt, int kind) {
            Intrinsics.checkNotNullParameter(inst, "inst");
            Intrinsics.checkNotNullParameter(rect, "rect");
            this.inst = inst;
            this.rect = rect;
            this.rt = rt;
            this.kind = kind;
        }

        public final Instance getInst() {
            return this.inst;
        }

        public final int getKind() {
            return this.kind;
        }

        public final RectF getRect() {
            return this.rect;
        }

        public final LuaRuntime getRt() {
            return this.rt;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Type inference failed for: r0v21, types: [com.goydevv.studrblx.ui.GuiOverlay$layouts$1] */
    /* JADX WARN: Type inference failed for: r0v22, types: [com.goydevv.studrblx.ui.GuiOverlay$scaledSizes$1] */
    public GuiOverlay(Context ctx, Studio studio) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(studio, "studio");
        this.studio = studio;
        this.fill = new Paint(1);
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        this.stroke = paint;
        this.imgPaint = new Paint(3);
        this.textPaint = new TextPaint(1);
        this.d = getResources().getDisplayMetrics().density;
        this.hits = new ArrayList<>();
        this.scrolls = new ArrayList<>();
        this.picks = new ArrayList<>();
        this.newHits = new ArrayList<>();
        this.newScrolls = new ArrayList<>();
        this.newPicks = new ArrayList<>();
        this.boxes = new IdentityHashMap<>();
        this.newBoxes = new IdentityHashMap<>();
        this.guides = CollectionsKt.emptyList();
        this.billboards = CollectionsKt.emptyList();
        ImageLoader.INSTANCE.getListeners().add(new Function1() { // from class: com.goydevv.studrblx.ui.GuiOverlay$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return GuiOverlay._init_$lambda$2(GuiOverlay.this, (String) obj);
            }
        });
        this.layouts = new LinkedHashMap<TKey, StaticLayout>() { // from class: com.goydevv.studrblx.ui.GuiOverlay$layouts$1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<GuiOverlay.TKey, StaticLayout> eldest) {
                return size() > 500;
            }
        };
        this.scaledSizes = new LinkedHashMap<SKey, Float>() { // from class: com.goydevv.studrblx.ui.GuiOverlay$scaledSizes$1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<GuiOverlay.SKey, Float> eldest) {
                return size() > 400;
            }
        };
        this.absInset = 58.0f;
        this.absChanged = new ArrayList<>();
        this.pressExtras = new IdentityHashMap<>();
    }

    /* JADX INFO: compiled from: GuiOverlay.kt */
    public static final class Scroll {
        private final Instance inst;
        private final float maxX;
        private final float maxY;
        private final RectF rect;
        private final float unit;

        public Scroll(Instance inst, RectF rect, float maxX, float maxY, float unit) {
            Intrinsics.checkNotNullParameter(inst, "inst");
            Intrinsics.checkNotNullParameter(rect, "rect");
            this.inst = inst;
            this.rect = rect;
            this.maxX = maxX;
            this.maxY = maxY;
            this.unit = unit;
        }

        public final Instance getInst() {
            return this.inst;
        }

        public final float getMaxX() {
            return this.maxX;
        }

        public final float getMaxY() {
            return this.maxY;
        }

        public final RectF getRect() {
            return this.rect;
        }

        public final float getUnit() {
            return this.unit;
        }
    }

    public final boolean getHasContent() {
        return this.hasContent;
    }

    public final void setHasContent(boolean z) {
        this.hasContent = z;
    }

    /* JADX INFO: compiled from: GuiOverlay.kt */
    public static final class EditBox {
        private final RectF area;
        private final boolean inLayout;
        private final RectF rect;
        private final float scale;

        public EditBox(RectF rect, RectF area, float scale, boolean inLayout) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            Intrinsics.checkNotNullParameter(area, "area");
            this.rect = rect;
            this.area = area;
            this.scale = scale;
            this.inLayout = inLayout;
        }

        public final RectF getArea() {
            return this.area;
        }

        public final boolean getInLayout() {
            return this.inLayout;
        }

        public final RectF getRect() {
            return this.rect;
        }

        public final float getScale() {
            return this.scale;
        }
    }

    public final List<float[]> getGuides() {
        return this.guides;
    }

    public final void setGuides(List<float[]> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.guides = list;
    }

    public final Instance getSelectedGui() {
        Object obj = null;
        if (this.studio.isPlaying()) {
            return null;
        }
        for (Object obj2 : this.studio.getSelection()) {
            if (((Instance) obj2).isA("GuiObject")) {
                obj = obj2;
                break;
            }
        }
        return (Instance) obj;
    }

    public final void setSelectedGui(Instance instance) {
    }

    public final Instance getPressed() {
        return this.pressed;
    }

    public final void setPressed(Instance instance) {
        this.pressed = instance;
    }

    public final Function2<? super Float, ? super Float, Unit> getOnCoreMenu() {
        return this.onCoreMenu;
    }

    public final void setOnCoreMenu(Function2<? super Float, ? super Float, Unit> function2) {
        this.onCoreMenu = function2;
    }

    static final Unit _init_$lambda$2(GuiOverlay this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.postInvalidate();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: GuiOverlay.kt */
    static final /* data */ class TKey {
        private final Layout.Alignment align;
        private final boolean bold;
        private final float lineHeight;
        private final boolean rich;
        private final float size;
        private final String text;
        private final Typeface tf;
        private final boolean truncate;
        private final int width;

        public static /* synthetic */ TKey copy$default(TKey tKey, String str, boolean z, Typeface typeface, boolean z2, float f, int i, Layout.Alignment alignment, float f2, boolean z3, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = tKey.text;
            }
            if ((i2 & 2) != 0) {
                z = tKey.rich;
            }
            if ((i2 & 4) != 0) {
                typeface = tKey.tf;
            }
            if ((i2 & 8) != 0) {
                z2 = tKey.bold;
            }
            if ((i2 & 16) != 0) {
                f = tKey.size;
            }
            if ((i2 & 32) != 0) {
                i = tKey.width;
            }
            if ((i2 & 64) != 0) {
                alignment = tKey.align;
            }
            if ((i2 & 128) != 0) {
                f2 = tKey.lineHeight;
            }
            if ((i2 & 256) != 0) {
                z3 = tKey.truncate;
            }
            float f3 = f2;
            boolean z4 = z3;
            int i3 = i;
            Layout.Alignment alignment2 = alignment;
            float f4 = f;
            Typeface typeface2 = typeface;
            return tKey.copy(str, z, typeface2, z2, f4, i3, alignment2, f3, z4);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String component1() {
            return this.text;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final boolean component2() {
            return this.rich;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final Typeface component3() {
            return this.tf;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final boolean component4() {
            return this.bold;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final float component5() {
            return this.size;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final int component6() {
            return this.width;
        }

        /* JADX INFO: renamed from: component7, reason: from getter */
        public final Layout.Alignment component7() {
            return this.align;
        }

        /* JADX INFO: renamed from: component8, reason: from getter */
        public final float component8() {
            return this.lineHeight;
        }

        /* JADX INFO: renamed from: component9, reason: from getter */
        public final boolean component9() {
            return this.truncate;
        }

        public final TKey copy(String text, boolean rich, Typeface tf, boolean bold, float size, int width, Layout.Alignment align, float lineHeight, boolean truncate) {
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(tf, "tf");
            Intrinsics.checkNotNullParameter(align, "align");
            return new TKey(text, rich, tf, bold, size, width, align, lineHeight, truncate);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof TKey)) {
                return false;
            }
            TKey tKey = (TKey) other;
            return Intrinsics.areEqual(this.text, tKey.text) && this.rich == tKey.rich && Intrinsics.areEqual(this.tf, tKey.tf) && this.bold == tKey.bold && Float.compare(this.size, tKey.size) == 0 && this.width == tKey.width && this.align == tKey.align && Float.compare(this.lineHeight, tKey.lineHeight) == 0 && this.truncate == tKey.truncate;
        }

        public int hashCode() {
            return (((((((((((((((this.text.hashCode() * 31) + Boolean.hashCode(this.rich)) * 31) + this.tf.hashCode()) * 31) + Boolean.hashCode(this.bold)) * 31) + Float.hashCode(this.size)) * 31) + Integer.hashCode(this.width)) * 31) + this.align.hashCode()) * 31) + Float.hashCode(this.lineHeight)) * 31) + Boolean.hashCode(this.truncate);
        }

        public String toString() {
            return "TKey(text=" + this.text + ", rich=" + this.rich + ", tf=" + this.tf + ", bold=" + this.bold + ", size=" + this.size + ", width=" + this.width + ", align=" + this.align + ", lineHeight=" + this.lineHeight + ", truncate=" + this.truncate + ")";
        }

        public TKey(String text, boolean rich, Typeface tf, boolean bold, float size, int width, Layout.Alignment align, float lineHeight, boolean truncate) {
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(tf, "tf");
            Intrinsics.checkNotNullParameter(align, "align");
            this.text = text;
            this.rich = rich;
            this.tf = tf;
            this.bold = bold;
            this.size = size;
            this.width = width;
            this.align = align;
            this.lineHeight = lineHeight;
            this.truncate = truncate;
        }

        public final boolean getBold() {
            return this.bold;
        }

        public final boolean getRich() {
            return this.rich;
        }

        public final float getSize() {
            return this.size;
        }

        public final String getText() {
            return this.text;
        }

        public final Typeface getTf() {
            return this.tf;
        }

        public final int getWidth() {
            return this.width;
        }

        public final Layout.Alignment getAlign() {
            return this.align;
        }

        public final float getLineHeight() {
            return this.lineHeight;
        }

        public final boolean getTruncate() {
            return this.truncate;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: GuiOverlay.kt */
    static final /* data */ class SKey {
        private final boolean bold;
        private final int h;
        private final float lineHeight;
        private final float max;
        private final float min;
        private final boolean rich;
        private final String text;
        private final Typeface tf;
        private final int w;

        public static /* synthetic */ SKey copy$default(SKey sKey, String str, boolean z, Typeface typeface, boolean z2, int i, int i2, float f, float f2, float f3, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                str = sKey.text;
            }
            if ((i3 & 2) != 0) {
                z = sKey.rich;
            }
            if ((i3 & 4) != 0) {
                typeface = sKey.tf;
            }
            if ((i3 & 8) != 0) {
                z2 = sKey.bold;
            }
            if ((i3 & 16) != 0) {
                i = sKey.w;
            }
            if ((i3 & 32) != 0) {
                i2 = sKey.h;
            }
            if ((i3 & 64) != 0) {
                f = sKey.max;
            }
            if ((i3 & 128) != 0) {
                f2 = sKey.min;
            }
            if ((i3 & 256) != 0) {
                f3 = sKey.lineHeight;
            }
            float f4 = f2;
            float f5 = f3;
            int i4 = i2;
            float f6 = f;
            int i5 = i;
            Typeface typeface2 = typeface;
            return sKey.copy(str, z, typeface2, z2, i5, i4, f6, f4, f5);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String component1() {
            return this.text;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final boolean component2() {
            return this.rich;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final Typeface component3() {
            return this.tf;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final boolean component4() {
            return this.bold;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final int component5() {
            return this.w;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final int component6() {
            return this.h;
        }

        /* JADX INFO: renamed from: component7, reason: from getter */
        public final float component7() {
            return this.max;
        }

        /* JADX INFO: renamed from: component8, reason: from getter */
        public final float component8() {
            return this.min;
        }

        /* JADX INFO: renamed from: component9, reason: from getter */
        public final float component9() {
            return this.lineHeight;
        }

        public final SKey copy(String text, boolean rich, Typeface tf, boolean bold, int w, int h, float max, float min, float lineHeight) {
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(tf, "tf");
            return new SKey(text, rich, tf, bold, w, h, max, min, lineHeight);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof SKey)) {
                return false;
            }
            SKey sKey = (SKey) other;
            return Intrinsics.areEqual(this.text, sKey.text) && this.rich == sKey.rich && Intrinsics.areEqual(this.tf, sKey.tf) && this.bold == sKey.bold && this.w == sKey.w && this.h == sKey.h && Float.compare(this.max, sKey.max) == 0 && Float.compare(this.min, sKey.min) == 0 && Float.compare(this.lineHeight, sKey.lineHeight) == 0;
        }

        public int hashCode() {
            return (((((((((((((((this.text.hashCode() * 31) + Boolean.hashCode(this.rich)) * 31) + this.tf.hashCode()) * 31) + Boolean.hashCode(this.bold)) * 31) + Integer.hashCode(this.w)) * 31) + Integer.hashCode(this.h)) * 31) + Float.hashCode(this.max)) * 31) + Float.hashCode(this.min)) * 31) + Float.hashCode(this.lineHeight);
        }

        public String toString() {
            return "SKey(text=" + this.text + ", rich=" + this.rich + ", tf=" + this.tf + ", bold=" + this.bold + ", w=" + this.w + ", h=" + this.h + ", max=" + this.max + ", min=" + this.min + ", lineHeight=" + this.lineHeight + ")";
        }

        public SKey(String text, boolean rich, Typeface tf, boolean bold, int w, int h, float max, float min, float lineHeight) {
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(tf, "tf");
            this.text = text;
            this.rich = rich;
            this.tf = tf;
            this.bold = bold;
            this.w = w;
            this.h = h;
            this.max = max;
            this.min = min;
            this.lineHeight = lineHeight;
        }

        public final boolean getBold() {
            return this.bold;
        }

        public final int getH() {
            return this.h;
        }

        public final float getLineHeight() {
            return this.lineHeight;
        }

        public final float getMax() {
            return this.max;
        }

        public final float getMin() {
            return this.min;
        }

        public final boolean getRich() {
            return this.rich;
        }

        public final String getText() {
            return this.text;
        }

        public final Typeface getTf() {
            return this.tf;
        }

        public final int getW() {
            return this.w;
        }
    }

    private final CharSequence content(String text, boolean rich) {
        if (rich) return RichText.INSTANCE.parse(text);
        try {
            return EmojiCompat.get().process(text);
        } catch (IllegalStateException ignored) {
            return text;
        }
    }

    private final StaticLayout layoutFor(TKey k) {
        StaticLayout staticLayoutBuild;
        int loadState;
        try {
            loadState = EmojiCompat.get().getLoadState();
        } catch (IllegalStateException ignored) {
            loadState = EmojiCompat.LOAD_STATE_DEFAULT;
        }
        if (loadState != this.emojiLoadState) {
            this.emojiLoadState = loadState;
            this.layouts.clear();
            this.scaledSizes.clear();
        }
        Map map = this.layouts;
        StaticLayout staticLayout = (StaticLayout) map.get(k);
        if (staticLayout == null) {
            TextPaint textPaint = new TextPaint(1);
            textPaint.setTypeface(k.getTf());
            textPaint.setTextSize(k.getSize());
            textPaint.setFakeBoldText(k.getBold());
            CharSequence charSequenceContent = content(k.getText(), k.getRich());
            if (k.getTruncate()) {
                CharSequence charSequenceEllipsize = TextUtils.ellipsize(charSequenceContent, textPaint, k.getWidth(), TextUtils.TruncateAt.END);
                Intrinsics.checkNotNullExpressionValue(charSequenceEllipsize, "ellipsize(...)");
                charSequenceContent = charSequenceEllipsize;
            }
            staticLayoutBuild = StaticLayout.Builder.obtain(charSequenceContent, 0, charSequenceContent.length(), textPaint, RangesKt.coerceAtLeast(k.getWidth(), 1)).setAlignment(k.getAlign()).setIncludePad(false).setLineSpacing(0.0f, k.getLineHeight()).build();
            Intrinsics.checkNotNullExpressionValue(staticLayoutBuild, "build(...)");
            map.put(k, staticLayoutBuild);
        } else {
            staticLayoutBuild = staticLayout;
        }
        return staticLayoutBuild;
    }

    private final float scaledSize(SKey k) {
        Float fValueOf;
        Map map = this.scaledSizes;
        Float f = (Float) map.get(k);
        if (f == null) {
            TextPaint textPaint = new TextPaint(1);
            textPaint.setTypeface(k.getTf());
            textPaint.setFakeBoldText(k.getBold());
            CharSequence charSequenceContent = content(k.getText(), k.getRich());
            Iterable iterableSplit = WS_REGEX.split(charSequenceContent.toString(), 0);
            Collection arrayList = new ArrayList();
            for (Object obj : iterableSplit) {
                if (((String) obj).length() > 0) {
                    arrayList.add(obj);
                }
            }
            List list = (List) arrayList;
            float fCoerceAtLeast = RangesKt.coerceAtLeast(k.getMin(), 1.0f);
            float fCoerceAtLeast2 = RangesKt.coerceAtLeast(k.getMax(), fCoerceAtLeast);
            if (!scaledSize$lambda$9$fits(textPaint, list, charSequenceContent, k, fCoerceAtLeast2)) {
                for (int i = 0; i < 12; i++) {
                    float f2 = (fCoerceAtLeast + fCoerceAtLeast2) / 2.0f;
                    if (scaledSize$lambda$9$fits(textPaint, list, charSequenceContent, k, f2)) {
                        fCoerceAtLeast = f2;
                    } else {
                        fCoerceAtLeast2 = f2;
                    }
                }
                fCoerceAtLeast2 = fCoerceAtLeast;
            }
            fValueOf = Float.valueOf(fCoerceAtLeast2);
            map.put(k, fValueOf);
        } else {
            fValueOf = f;
        }
        return fValueOf.floatValue();
    }

    private static final boolean scaledSize$lambda$9$fits(TextPaint p, List<String> list, CharSequence cs, SKey $k, float s) {
        boolean z;
        p.setTextSize(s);
        List<String> list2 = list;
        if (!(list2 instanceof Collection) || !list2.isEmpty()) {
            Iterator it = list2.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (p.measureText((String) it.next()) > ((float) $k.getW())) {
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
            return false;
        }
        StaticLayout l = StaticLayout.Builder.obtain(cs, 0, cs.length(), p, RangesKt.coerceAtLeast($k.getW(), 1)).setIncludePad(false).setLineSpacing(0.0f, $k.getLineHeight()).build();
        Intrinsics.checkNotNullExpressionValue(l, "build(...)");
        return l.getHeight() <= $k.getH();
    }

    public final void renderSurface(Instance gui, float cw, float ch, Bitmap bmp) {
        Intrinsics.checkNotNullParameter(gui, "gui");
        Intrinsics.checkNotNullParameter(bmp, "bmp");
        bmp.eraseColor(0);
        Canvas c = new Canvas(bmp);
        c.scale(bmp.getWidth() / cw, bmp.getHeight() / ch);
        ArrayList<Hit> arrayList = this.newHits;
        ArrayList<Scroll> arrayList2 = this.newScrolls;
        ArrayList<Pair<Instance, RectF>> arrayList3 = this.newPicks;
        LuaRuntime sr = this.rt;
        this.newHits = new ArrayList<>();
        this.newScrolls = new ArrayList<>();
        this.newPicks = new ArrayList<>();
        RectF area = new RectF(0.0f, 0.0f, cw, ch);
        float su = this.absUnit;
        float si = this.absInset;
        this.absUnit = 1.0f;
        this.absInset = 0.0f;
        try {
            drawChildren(c, gui, area, 1.0f / this.d, area);
        } finally {
            this.newHits = arrayList;
            this.newScrolls = arrayList2;
            this.newPicks = arrayList3;
            this.rt = sr;
            this.absUnit = su;
            this.absInset = si;
        }
    }

    /* JADX WARN: Code duplicated, block: B:101:0x022c  */
    /* JADX WARN: Code duplicated, block: B:102:0x022e  */
    /* JADX WARN: Code duplicated, block: B:105:0x0234 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:107:0x0238  */
    /* JADX WARN: Code duplicated, block: B:110:0x023e A[Catch: all -> 0x027a, TRY_ENTER, TRY_LEAVE, TryCatch #4 {all -> 0x027a, blocks: (B:71:0x01d1, B:74:0x01d7, B:80:0x01e8, B:86:0x0208, B:110:0x023e, B:115:0x024c, B:117:0x0256, B:123:0x0270, B:90:0x0213), top: B:191:0x01d1 }] */
    /* JADX WARN: Code duplicated, block: B:112:0x0242 A[Catch: all -> 0x027c, TRY_ENTER, TryCatch #5 {all -> 0x027c, blocks: (B:69:0x01c7, B:78:0x01e2, B:83:0x01ee, B:99:0x0227, B:103:0x022f, B:113:0x0245, B:121:0x025f, B:112:0x0242), top: B:193:0x01c7 }] */
    /* JADX WARN: Code duplicated, block: B:115:0x024c A[Catch: all -> 0x027a, TRY_ENTER, TryCatch #4 {all -> 0x027a, blocks: (B:71:0x01d1, B:74:0x01d7, B:80:0x01e8, B:86:0x0208, B:110:0x023e, B:115:0x024c, B:117:0x0256, B:123:0x0270, B:90:0x0213), top: B:191:0x01d1 }] */
    /* JADX WARN: Code duplicated, block: B:117:0x0256 A[Catch: all -> 0x027a, TRY_LEAVE, TryCatch #4 {all -> 0x027a, blocks: (B:71:0x01d1, B:74:0x01d7, B:80:0x01e8, B:86:0x0208, B:110:0x023e, B:115:0x024c, B:117:0x0256, B:123:0x0270, B:90:0x0213), top: B:191:0x01d1 }] */
    /* JADX WARN: Code duplicated, block: B:119:0x025b  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r18v6 */
    /* JADX WARN: Type inference failed for: r19v3 */
    /* JADX WARN: Type inference failed for: r22v0 */
    /* JADX WARN: Type inference failed for: r22v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r22v2 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v22 */
    /* JADX WARN: Type inference failed for: r3v38 */
    /* JADX WARN: Type inference failed for: r3v42 */
    /* JADX WARN: Type inference failed for: r3v44 */
    /* JADX WARN: Type inference failed for: r4v44 */
    /* JADX WARN: Type inference failed for: r4v45 */
    /* JADX WARN: Type inference failed for: r4v52 */
    /* JADX WARN: Type inference failed for: r4v53 */
    /* JADX WARN: Type inference failed for: r4v54 */
    /* JADX WARN: Type inference failed for: r4v55 */
    /* JADX WARN: Type inference failed for: r4v65 */
    /* JADX WARN: Type inference failed for: r4v66 */
    /* JADX WARN: Type inference failed for: r4v67 */
    /* JADX WARN: Type inference failed for: r6v21 */
    /* JADX WARN: Type inference failed for: r6v22 */
    /* JADX WARN: Type inference failed for: r6v25 */
    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        try {
            List<Instance> arrayListEmptyList;
            ArrayList<Instance> children;
            LuaRuntime editRuntime;
            GuiOverlay guiOverlay;
            Object next;
            EnumValue enumValue;
            int r4;
            int r5 = 0;
            int r3 = 0;
            float f;
            int r6;
            int r19;
            float fPluginScale;
            ArrayList<Instance> children2;
            Instance instanceFindFirstChild$default;
            ArrayList<Instance> children3;
            GuiOverlay guiOverlay2 = this;
            Intrinsics.checkNotNullParameter(canvas, "canvas");
            synchronized (guiOverlay2.studio.getLock()) {
                int i = 0;
                guiOverlay2.newHits = new ArrayList<>();
                guiOverlay2.newScrolls = new ArrayList<>();
                guiOverlay2.newPicks = new ArrayList<>();
                guiOverlay2.newBoxes = new IdentityHashMap<>();
                float width = guiOverlay2.getWidth();
                float height = guiOverlay2.getHeight();
                PlaySession play = guiOverlay2.studio.getPlay();
                char c = 2;
                boolean z = false;
                if (play != null) {
                    Instance player = play.getPlayer();
                    arrayListEmptyList = (player == null || (instanceFindFirstChild$default = Instance.findFirstChild$default(player, "PlayerGui", false, 2, null)) == null || (children3 = instanceFindFirstChild$default.getChildren()) == null) ? CollectionsKt.emptyList() : children3;
                } else {
                    Instance instanceFindService = guiOverlay2.studio.getModel().findService("StarterGui");
                    arrayListEmptyList = (instanceFindService == null || (children = instanceFindService.getChildren()) == null) ? CollectionsKt.emptyList() : children;
                }
                Iterable iterable = arrayListEmptyList;
                if (play == null || (editRuntime = play.getRt()) == null) {
                    editRuntime = guiOverlay2.studio.getEditRuntime();
                }
                guiOverlay2.rt = editRuntime;
                float f2 = guiOverlay2.d * 58.0f;
                boolean zDrawBillboards = false | guiOverlay2.drawBillboards(canvas, play != null);
                Instance instanceFindService2 = guiOverlay2.studio.getModel().findService("CoreGui");
                Iterable iterableEmptyList = (instanceFindService2 == null || (children2 = instanceFindService2.getChildren()) == null) ? CollectionsKt.emptyList() : children2;
                ArrayList arrayList = new ArrayList();
                for (Object obj : iterable) {
                    Instance instance = (Instance) obj;
                    char c2 = c;
                    boolean z2 = z;
                    if ((!Intrinsics.areEqual(instance.getClassName(), "ScreenGui") || Intrinsics.areEqual(instance.get("Enabled"), Boolean.valueOf(z2))) ? z2 : true) {
                        ArrayList arrayList2 = arrayList;
                        arrayList2.add(obj);
                        arrayList = arrayList2;
                    }
                    c = c2;
                    z = z2;
                }
                char c3 = c;
                boolean z3 = z;
                List listSortedWith = CollectionsKt.sortedWith(arrayList, new Comparator() { // from class: com.goydevv.studrblx.ui.GuiOverlay$onDraw$lambda$17$$inlined$sortedBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(Object t, Object t2) {
                        Object obj2 = ((Instance) t).get("DisplayOrder");
                        Number number = obj2 instanceof Number ? (Number) obj2 : null;
                        Integer numValueOf = Integer.valueOf(number != null ? number.intValue() : 0);
                        Object obj3 = ((Instance) t2).get("DisplayOrder");
                        Number number2 = obj3 instanceof Number ? (Number) obj3 : null;
                        return ComparisonsKt.compareValues(numValueOf, Integer.valueOf(number2 != null ? number2.intValue() : 0));
                    }
                });
                ArrayList arrayList3 = new ArrayList();
                for (Object obj2 : iterableEmptyList) {
                    Instance instance2 = (Instance) obj2;
                    int i2 = i;
                    if ((!Intrinsics.areEqual(instance2.getClassName(), "ScreenGui") || Intrinsics.areEqual(instance2.get("Enabled"), Boolean.valueOf(z3))) ? z3 : true) {
                        arrayList3.add(obj2);
                    }
                    i = i2;
                }
                int r22 = 1;
                List<Instance> listPlus = CollectionsKt.plus((Collection) listSortedWith, (Iterable) CollectionsKt.sortedWith(arrayList3, new Comparator() { // from class: com.goydevv.studrblx.ui.GuiOverlay$onDraw$lambda$17$$inlined$sortedBy$2
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(Object t, Object t2) {
                        Object obj3 = ((Instance) t).get("DisplayOrder");
                        Number number = obj3 instanceof Number ? (Number) obj3 : null;
                        Integer numValueOf = Integer.valueOf(number != null ? number.intValue() : 0);
                        Object obj4 = ((Instance) t2).get("DisplayOrder");
                        Number number2 = obj4 instanceof Number ? (Number) obj4 : null;
                        return ComparisonsKt.compareValues(numValueOf, Integer.valueOf(number2 != null ? number2.intValue() : 0));
                    }
                }));
                for (Instance instance3 : listPlus) {
                    try {
                        try {
                            Object obj3 = instance3.get("ScreenInsets");
                            if (obj3 instanceof EnumValue) {
                                try {
                                    enumValue = (EnumValue) obj3;
                                } catch (Throwable th) {
                                    throw th;
                                }
                            } else {
                                enumValue = null;
                            }
                            Integer numValueOf = enumValue != null ? Integer.valueOf(enumValue.getValue()) : null;
                            Instance parent = instance3.getParent();
                            boolean zAreEqual = Intrinsics.areEqual(parent != null ? parent.getClassName() : null, "CoreGui");
                            if (Intrinsics.areEqual(instance3.get("IgnoreGuiInset"), Boolean.valueOf(r22 != 0)) || (numValueOf != null && numValueOf.intValue() == 0)) {
                                r4 = r22;
                            } else {
                                if (numValueOf == null) {
                                    r4 = r22;
                                } else {
                                    r4 = r22;
                                    if (numValueOf.intValue() != r4) {
                                    }
                                    if (r3 != 0) {
                                        f = 0.0f;
                                    } else {
                                        f = f2;
                                    }
                                    RectF rectF = new RectF(0.0f, f, width, height);
                                    if (zAreEqual || play != null) {
                                        r6 = z3 ? 1 : 0;
                                    } else {
                                        r6 = r5;
                                    }
                                    r19 = r6;
                                    if (r19 != 0) {
                                        guiOverlay2.usePluginUnits();
                                    } else {
                                        guiOverlay2.useGameUnits();
                                    }
                                    guiOverlay2.setAbs(instance3, rectF);
                                    LuaRuntime luaRuntime = guiOverlay2.rt;
                                    if (r19 != 0) {
                                        guiOverlay2.rt = guiOverlay2.studio.getEditRuntime();
                                    }
                                    if (r19 != 0) {
                                        fPluginScale = guiOverlay2.pluginScale(height);
                                    } else {
                                        fPluginScale = 1.0f;
                                    }
                                    List list = listPlus;
                                    r22 = 1;
                                    guiOverlay2.drawChildren(canvas, instance3, rectF, fPluginScale, new RectF(0.0f, 0.0f, width, height));
                                    guiOverlay2.rt = luaRuntime;
                                    zDrawBillboards = true;
                                    listPlus = list;
                                }
                                if (!zAreEqual || play != null) {
                                    r3 = z3 ? 1 : 0;
                                    r5 = r4;
                                }
                                if (r3 != 0) {
                                    f = 0.0f;
                                } else {
                                    f = f2;
                                }
                                RectF rectF2 = new RectF(0.0f, f, width, height);
                                if (zAreEqual) {
                                    r6 = z3 ? 1 : 0;
                                } else {
                                    r6 = z3 ? 1 : 0;
                                }
                                r19 = r6;
                                if (r19 != 0) {
                                    guiOverlay2.usePluginUnits();
                                } else {
                                    guiOverlay2.useGameUnits();
                                }
                                guiOverlay2.setAbs(instance3, rectF2);
                                LuaRuntime luaRuntime2 = guiOverlay2.rt;
                                if (r19 != 0) {
                                    guiOverlay2.rt = guiOverlay2.studio.getEditRuntime();
                                }
                                if (r19 != 0) {
                                    fPluginScale = guiOverlay2.pluginScale(height);
                                } else {
                                    fPluginScale = 1.0f;
                                }
                                List list2 = listPlus;
                                r22 = 1;
                                guiOverlay2.drawChildren(canvas, instance3, rectF2, fPluginScale, new RectF(0.0f, 0.0f, width, height));
                                guiOverlay2.rt = luaRuntime2;
                                zDrawBillboards = true;
                                listPlus = list2;
                            }
                            r3 = r4;
                            r5 = r4;
                            if (r3 != 0) {
                                f = 0.0f;
                            } else {
                                f = f2;
                            }
                            RectF rectF3 = new RectF(0.0f, f, width, height);
                            if (zAreEqual) {
                                r6 = z3 ? 1 : 0;
                            } else {
                                r6 = z3 ? 1 : 0;
                            }
                            r19 = r6;
                            if (r19 != 0) {
                                guiOverlay2.usePluginUnits();
                            } else {
                                guiOverlay2.useGameUnits();
                            }
                            guiOverlay2.setAbs(instance3, rectF3);
                            LuaRuntime luaRuntime3 = guiOverlay2.rt;
                            if (r19 != 0) {
                                guiOverlay2.rt = guiOverlay2.studio.getEditRuntime();
                            }
                            if (r19 != 0) {
                                fPluginScale = guiOverlay2.pluginScale(height);
                            } else {
                                fPluginScale = 1.0f;
                            }
                            List list3 = listPlus;
                            r22 = 1;
                            guiOverlay2.drawChildren(canvas, instance3, rectF3, fPluginScale, new RectF(0.0f, 0.0f, width, height));
                            guiOverlay2.rt = luaRuntime3;
                            zDrawBillboards = true;
                            listPlus = list3;
                        } catch (Throwable th2) {
                            Throwable th = th2;
                        }
                    } catch (Throwable th3) {
                        Throwable th = th3;
                        throw th;
                    }
                }
                guiOverlay2.useGameUnits();
                if (play != null) {
                    zDrawBillboards = true;
                }
                boolean zDrawPluginWidgets = play == null ? guiOverlay2.drawPluginWidgets(canvas, width, height) | zDrawBillboards : zDrawBillboards;
                try {
                    Instance selectedGui = guiOverlay2.getSelectedGui();
                    if (selectedGui != null) {
                        ArrayList<Pair<Instance, RectF>> arrayList4 = guiOverlay2.newPicks;
                        Iterator it = arrayList4.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                next = null;
                                break;
                            }
                            next = it.next();
                            ArrayList<Pair<Instance, RectF>> arrayList5 = arrayList4;
                            if ((((Pair) next).getFirst() == selectedGui ? r22 : z3 ? 1 : 0) != 0) {
                                break;
                            } else {
                                arrayList4 = arrayList5;
                            }
                        }
                        Pair pair = (Pair) next;
                        if (pair != null) {
                            RectF rectF4 = (RectF) pair.component2();
                            guiOverlay2.stroke.setColor(-15096065);
                            guiOverlay2.stroke.setStrokeWidth(guiOverlay2.d * 2.0f);
                            guiOverlay2.stroke.setShader(null);
                            canvas.drawRect(rectF4, guiOverlay2.stroke);
                            for (float[] fArr : guiOverlay2.handlePoints(rectF4)) {
                                guiOverlay2.fill.setShader(null);
                                guiOverlay2.fill.setColor(-1);
                                Instance instance4 = selectedGui;
                                RectF rectF5 = rectF4;
                                float f3 = width;
                                GuiOverlay guiOverlay3 = guiOverlay2;
                                try {
                                    canvas.drawRect(fArr[z3 ? 1 : 0] - (guiOverlay2.d * 4.0f), fArr[r22] - (guiOverlay2.d * 4.0f), fArr[z3 ? 1 : 0] + (guiOverlay2.d * 4.0f), fArr[r22] + (guiOverlay2.d * 4.0f), guiOverlay2.fill);
                                    canvas.drawRect(fArr[z3 ? 1 : 0] - (guiOverlay3.d * 4.0f), fArr[r22] - (guiOverlay3.d * 4.0f), (guiOverlay3.d * 4.0f) + fArr[z3 ? 1 : 0], (guiOverlay3.d * 4.0f) + fArr[r22], guiOverlay3.stroke);
                                    rectF4 = rectF5;
                                    guiOverlay2 = guiOverlay3;
                                    selectedGui = instance4;
                                    width = f3;
                                } catch (Throwable th4) {
                                    Throwable th = th4;
                                    throw th;
                                }
                            }
                            guiOverlay = guiOverlay2;
                            zDrawPluginWidgets = true;
                        } else {
                            guiOverlay = guiOverlay2;
                        }
                    } else {
                        guiOverlay = guiOverlay2;
                    }
                    List<float[]> list4 = guiOverlay.guides;
                    if (!list4.isEmpty()) {
                        guiOverlay.stroke.setColor(-49766);
                        guiOverlay.stroke.setStrokeWidth(guiOverlay.d * 1.5f);
                        guiOverlay.stroke.setShader(null);
                        for (float[] fArr2 : list4) {
                            canvas.drawLine(fArr2[z3 ? 1 : 0], fArr2[r22], fArr2[c3], fArr2[3], guiOverlay.stroke);
                        }
                    }
                    guiOverlay.hits = guiOverlay.newHits;
                    guiOverlay.scrolls = guiOverlay.newScrolls;
                    guiOverlay.picks = guiOverlay.newPicks;
                    guiOverlay.boxes = guiOverlay.newBoxes;
                    guiOverlay.fireAbsChanges();
                    Unit unit = Unit.INSTANCE;
                    guiOverlay.hasContent = zDrawPluginWidgets;
                } catch (Throwable th5) {
                    Throwable th = th5;
                }
            }
        } catch (Throwable _t) {
            if (_t instanceof RuntimeException) throw (RuntimeException) _t;
            if (_t instanceof Error) throw (Error) _t;
            throw new RuntimeException(_t);
        }
    }

    public final List<float[]> handlePoints(RectF r) {
        Intrinsics.checkNotNullParameter(r, "r");
        return CollectionsKt.listOf(new float[][]{new float[]{r.left, r.top}, new float[]{r.centerX(), r.top}, new float[]{r.right, r.top}, new float[]{r.right, r.centerY()}, new float[]{r.right, r.bottom}, new float[]{r.centerX(), r.bottom}, new float[]{r.left, r.bottom}, new float[]{r.left, r.centerY()}});
    }

    public final EditBox editBox(Instance i) {
        Intrinsics.checkNotNullParameter(i, "i");
        return this.boxes.get(i);
    }

    public final Integer handleAt(Instance i, float x, float y) {
        Intrinsics.checkNotNullParameter(i, "i");
        EditBox b = this.boxes.get(i);
        if (b == null) {
            return null;
        }
        float tol = this.d * 16.0f;
        int i2 = 0;
        for (Object obj : handlePoints(b.getRect())) {
            int i3 = i2 + 1;
            if (i2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            float[] fArr = (float[]) obj;
            if (Math.abs(fArr[0] - x) <= tol && Math.abs(fArr[1] - y) <= tol) {
                return Integer.valueOf(i2);
            }
            i2 = i3;
        }
        return (!b.getRect().contains(x, y) || seeThrough(i)) ? null : -1;
    }

    public final boolean seeThrough(Instance c) {
        String string;
        boolean z;
        Intrinsics.checkNotNullParameter(c, "c");
        if (Intrinsics.areEqual(c.get("Visible"), (Object) false)) {
            return true;
        }
        if (num(c, "BackgroundTransparency", 0.0f) < 0.999f) {
            return false;
        }
        String img = "";
        if (c.isA("TextLabel") || c.isA("TextButton") || Intrinsics.areEqual(c.getClassName(), "TextBox")) {
            Object obj = c.get("Text");
            String text = obj instanceof String ? (String) obj : null;
            if (text == null) {
                text = "";
            }
            if (!StringsKt.isBlank(text) && num(c, "TextTransparency", 0.0f) < 0.999f) {
                return false;
            }
        }
        if (Intrinsics.areEqual(c.getClassName(), "ImageLabel") || Intrinsics.areEqual(c.getClassName(), "ImageButton")) {
            Object obj2 = c.get("Image");
            if (obj2 != null && (string = obj2.toString()) != null) {
                img = string;
            }
            if (!StringsKt.isBlank(img) && num(c, "ImageTransparency", 0.0f) < 0.999f) {
                return false;
            }
        }
        Iterable<Instance> children = c.getChildren();
        if ((children instanceof Collection) && ((Collection) children).isEmpty()) {
            z = false;
        } else {
            for (Instance instance : children) {
                if (Intrinsics.areEqual(instance.getClassName(), "UIStroke") && !Intrinsics.areEqual(instance.get("Enabled"), (Object) false) && num(instance, "Transparency", 0.0f) < 0.999f) {
                    z = true;
                }
            }
            z = false;
        }
        return !z;
    }

    /* JADX INFO: compiled from: GuiOverlay.kt */
    public static final class DragState {
        private final Vector2 anchor;
        private final RectF area;
        private final int handle;
        private final boolean inLayout;
        private final Instance inst;
        private final UDim2 pos;
        private final UDim2 size;
        private final RectF start;
        private final float unit;

        public DragState(Instance inst, int handle, RectF start, RectF area, float unit, UDim2 pos, UDim2 size, Vector2 anchor, boolean inLayout) {
            Intrinsics.checkNotNullParameter(inst, "inst");
            Intrinsics.checkNotNullParameter(start, "start");
            Intrinsics.checkNotNullParameter(area, "area");
            Intrinsics.checkNotNullParameter(pos, "pos");
            Intrinsics.checkNotNullParameter(size, "size");
            Intrinsics.checkNotNullParameter(anchor, "anchor");
            this.inst = inst;
            this.handle = handle;
            this.start = start;
            this.area = area;
            this.unit = unit;
            this.pos = pos;
            this.size = size;
            this.anchor = anchor;
            this.inLayout = inLayout;
        }

        public final RectF getArea() {
            return this.area;
        }

        public final int getHandle() {
            return this.handle;
        }

        public final Instance getInst() {
            return this.inst;
        }

        public final RectF getStart() {
            return this.start;
        }

        public final float getUnit() {
            return this.unit;
        }

        public final Vector2 getAnchor() {
            return this.anchor;
        }

        public final boolean getInLayout() {
            return this.inLayout;
        }

        public final UDim2 getPos() {
            return this.pos;
        }

        public final UDim2 getSize() {
            return this.size;
        }
    }

    public final DragState beginDrag(Instance i, int handle) {
        Intrinsics.checkNotNullParameter(i, "i");
        synchronized (this.studio.getLock()) {
            EditBox editBox = this.boxes.get(i);
            if (editBox == null) {
                return null;
            }
            RectF rectF = new RectF(editBox.getRect());
            RectF rectF2 = new RectF(editBox.getArea());
            float scale = this.d * editBox.getScale();
            UDim2 uDim2Udim = udim(i.get("Position"));
            UDim2 uDim2Udim2 = udim(i.get("Size"));
            Object obj = i.get("AnchorPoint");
            Vector2 vector2 = obj instanceof Vector2 ? (Vector2) obj : null;
            if (vector2 == null) {
                vector2 = new Vector2(0.0f, 0.0f);
            }
            return new DragState(i, handle, rectF, rectF2, scale, uDim2Udim, uDim2Udim2, vector2, editBox.getInLayout());
        }
    }

    /* JADX WARN: Code duplicated, block: B:100:0x0297 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:101:0x0299 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:104:0x02ba  */
    /* JADX WARN: Code duplicated, block: B:105:0x02bd  */
    /* JADX WARN: Code duplicated, block: B:114:0x02cf  */
    /* JADX WARN: Code duplicated, block: B:116:0x02d7 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:118:0x02dc  */
    /* JADX WARN: Code duplicated, block: B:119:0x02e3  */
    /* JADX WARN: Code duplicated, block: B:120:0x02ea  */
    /* JADX WARN: Code duplicated, block: B:122:0x02f6 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:124:0x02fb  */
    /* JADX WARN: Code duplicated, block: B:125:0x0302  */
    /* JADX WARN: Code duplicated, block: B:126:0x0309  */
    /* JADX WARN: Code duplicated, block: B:129:0x032f  */
    /* JADX WARN: Code duplicated, block: B:139:0x0384 A[Catch: all -> 0x043c, TryCatch #3 {all -> 0x043c, blocks: (B:137:0x036d, B:139:0x0384, B:148:0x03b4, B:150:0x03ba, B:152:0x0420, B:154:0x0434), top: B:167:0x036d }] */
    /* JADX WARN: Code duplicated, block: B:140:0x0396  */
    /* JADX WARN: Code duplicated, block: B:147:0x03ae  */
    /* JADX WARN: Code duplicated, block: B:150:0x03ba A[Catch: all -> 0x043c, TryCatch #3 {all -> 0x043c, blocks: (B:137:0x036d, B:139:0x0384, B:148:0x03b4, B:150:0x03ba, B:152:0x0420, B:154:0x0434), top: B:167:0x036d }] */
    /* JADX WARN: Code duplicated, block: B:152:0x0420 A[Catch: all -> 0x043c, TryCatch #3 {all -> 0x043c, blocks: (B:137:0x036d, B:139:0x0384, B:148:0x03b4, B:150:0x03ba, B:152:0x0420, B:154:0x0434), top: B:167:0x036d }] */
    /* JADX WARN: Code duplicated, block: B:153:0x0432  */
    /* JADX WARN: Code duplicated, block: B:163:0x0335 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:171:0x01ab A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:54:0x00ba  */
    /* JADX WARN: Code duplicated, block: B:58:0x00ca  */
    /* JADX WARN: Code duplicated, block: B:60:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:64:0x0126 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:66:0x0144 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:73:0x01bb A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:74:0x01be A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:75:0x01c5 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:76:0x01d0 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:80:0x01fd A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:82:0x0216  */
    /* JADX WARN: Code duplicated, block: B:83:0x0217 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:84:0x021a A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:87:0x023d  */
    /* JADX WARN: Code duplicated, block: B:88:0x0240  */
    /* JADX WARN: Code duplicated, block: B:90:0x0246 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:91:0x0249 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:92:0x024e A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:93:0x0253 A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:97:0x027e A[Catch: all -> 0x02c4, TryCatch #0 {, blocks: (B:61:0x00d4, B:62:0x0120, B:64:0x0126, B:66:0x0144, B:68:0x0154, B:71:0x01b2, B:73:0x01bb, B:74:0x01be, B:77:0x01f0, B:78:0x01f4, B:80:0x01fd, B:81:0x0213, B:85:0x021c, B:86:0x021e, B:90:0x0246, B:91:0x0249, B:94:0x0271, B:95:0x0275, B:97:0x027e, B:98:0x0293, B:102:0x029a, B:103:0x029b, B:106:0x02bf, B:100:0x0297, B:101:0x0299, B:92:0x024e, B:93:0x0253, B:83:0x0217, B:84:0x021a, B:75:0x01c5, B:76:0x01d0), top: B:161:0x00d4 }] */
    /* JADX WARN: Code duplicated, block: B:99:0x0296  */
    public final void dragTo(DragState s, float dx, float dy) {
        try {
        float b;
        float b2;
        float r;
        float t;
        char c;
        boolean moveX = false;
        char c2;
        char c3 = 0;
        char c4;
        boolean snappedX;
        boolean snappedY;
        int grid;
        float w;
        float hh;
        float aw;
        float ah;
        float u;
        UDim2 uDim2;
        float w2 = 0.0f;
        float l = 0.0f;
        UDim2 uDim3;
        float g;
        int i;
        ArrayList arrayListArrayListOf;
        ArrayList arrayListArrayListOf2;
        float f;
        float f2;
        List listListOf;
        Float fValueOf;
        Pair<Float, Float> pairDragTo$lambda$23$best;
        float fFloatValue;
        List listListOf2;
        Pair<Float, Float> pairDragTo$lambda$23$best2;
        float fFloatValue2;
        Instance key;
        Intrinsics.checkNotNullParameter(s, "s");
        if (s.getHandle() == -1 && s.getInLayout()) {
            return;
        }
        float l2 = s.getStart().left;
        float t2 = s.getStart().top;
        float r2 = s.getStart().right;
        float b3 = s.getStart().bottom;
        int h = s.getHandle();
        if (h == -1) {
            b = b3 + dy;
            b2 = r2 + dx;
            r = t2 + dy;
            t = l2 + dx;
        } else {
            switch (h) {
                case 0:
                case 6:
                case 7:
                    l2 += dx;
                    break;
            }
            switch (h) {
                case 2:
                case 3:
                case 4:
                    r2 += dx;
                    break;
            }
            switch (h) {
                case 0:
                case 1:
                case 2:
                    t2 += dy;
                    break;
            }
            switch (h) {
                case 4:
                case 5:
                case 6:
                    b3 += dy;
                    break;
            }
            if (r2 - l2 < 1.0f) {
                switch (h) {
                    case 0:
                    case 6:
                    case 7:
                        l2 = r2 - 1.0f;
                        break;
                    default:
                        r2 = l2 + 1.0f;
                        break;
                }
            }
            if (b3 - t2 < 1.0f) {
                switch (h) {
                    case 0:
                    case 1:
                    case 2:
                        t2 = b3 - 1.0f;
                        break;
                    default:
                        b3 = t2 + 1.0f;
                        break;
                }
            }
            b = b3;
            b2 = r2;
            r = t2;
            t = l2;
        }
        ArrayList gl = new ArrayList();
        RectF area = s.getArea();
        if (h != -1 && h != 0 && h != 2 && h != 3 && h != 4 && h != 6) {
            c = 0;
            moveX = h == 7;
            if (h == -1 && h != 0 && h != 1 && h != 2 && h != 4) {
                c2 = 2;
                if (h != 5 && h != 6) {
                    c3 = c;
                }
                c4 = c3;
                snappedX = false;
                snappedY = false;
                if (Settings.INSTANCE.getGuiSmartGuides()) {
                    synchronized (this.studio.getLock()) {
                        i = 0;
                        Float[] fArr = new Float[3];
                        fArr[c] = Float.valueOf(area.left);
                        fArr[1] = Float.valueOf(area.centerX());
                        fArr[c2] = Float.valueOf(area.right);
                        arrayListArrayListOf = CollectionsKt.arrayListOf(fArr);
                        Float[] fArr2 = new Float[3];
                        fArr2[c] = Float.valueOf(area.top);
                        fArr2[1] = Float.valueOf(area.centerY());
                        fArr2[c2] = Float.valueOf(area.bottom);
                        arrayListArrayListOf2 = CollectionsKt.arrayListOf(fArr2);
                        for (Map.Entry<Instance, EditBox> entry : this.boxes.entrySet()) {
                            key = entry.getKey();
                            EditBox value = entry.getValue();
                            if (key == s.getInst() && key.getParent() == s.getInst().getParent()) {
                                arrayListArrayListOf.add(Float.valueOf(value.getRect().left));
                                arrayListArrayListOf.add(Float.valueOf(value.getRect().centerX()));
                                arrayListArrayListOf.add(Float.valueOf(value.getRect().right));
                                arrayListArrayListOf2.add(Float.valueOf(value.getRect().top));
                                arrayListArrayListOf2.add(Float.valueOf(value.getRect().centerY()));
                                arrayListArrayListOf2.add(Float.valueOf(value.getRect().bottom));
                            }
                        }
                        f = this.d * 6.0f;
                        if (moveX) {
                            switch (h) {
                                case -1:
                                    f2 = 2.0f;
                                    Float[] fArr3 = new Float[3];
                                    fArr3[c] = Float.valueOf(t);
                                    fArr3[1] = Float.valueOf((t + b2) / 2.0f);
                                    fArr3[c2] = Float.valueOf(b2);
                                    listListOf2 = CollectionsKt.listOf(fArr3);
                                    break;
                                case 0:
                                case 6:
                                case 7:
                                    listListOf2 = CollectionsKt.listOf(Float.valueOf(t));
                                    f2 = 2.0f;
                                    break;
                                default:
                                    f2 = 2.0f;
                                    listListOf2 = CollectionsKt.listOf(Float.valueOf(b2));
                                    break;
                            }
                            pairDragTo$lambda$23$best2 = dragTo$lambda$23$best(f, listListOf2, arrayListArrayListOf);
                            if (pairDragTo$lambda$23$best2 != null) {
                                fFloatValue2 = pairDragTo$lambda$23$best2.component1().floatValue();
                                float fFloatValue3 = pairDragTo$lambda$23$best2.component2().floatValue();
                                switch (h) {
                                    case -1:
                                        t += fFloatValue2;
                                        b2 += fFloatValue2;
                                        break;
                                    case 0:
                                    case 6:
                                    case 7:
                                        t += fFloatValue2;
                                        break;
                                    default:
                                        b2 += fFloatValue2;
                                        break;
                                }
                                float f3 = area.top;
                                float f4 = area.bottom;
                                float[] fArr4 = new float[4];
                                fArr4[c] = fFloatValue3;
                                fArr4[1] = f3;
                                fArr4[c2] = fFloatValue3;
                                fArr4[3] = f4;
                                gl.add(fArr4);
                                snappedX = true;
                            }
                        } else {
                            f2 = 2.0f;
                        }
                        if (c4 != 0) {
                            switch (h) {
                                case -1:
                                    Float[] fArr5 = new Float[3];
                                    fArr5[c] = Float.valueOf(r);
                                    fArr5[1] = Float.valueOf((r + b) / f2);
                                    fArr5[c2] = Float.valueOf(b);
                                    listListOf = CollectionsKt.listOf(fArr5);
                                    break;
                                case 0:
                                case 1:
                                case 2:
                                    fValueOf = Float.valueOf(r);
                                    listListOf = CollectionsKt.listOf(fValueOf);
                                    break;
                                default:
                                    fValueOf = Float.valueOf(b);
                                    listListOf = CollectionsKt.listOf(fValueOf);
                                    break;
                            }
                            pairDragTo$lambda$23$best = dragTo$lambda$23$best(f, listListOf, arrayListArrayListOf2);
                            if (pairDragTo$lambda$23$best != null) {
                                fFloatValue = pairDragTo$lambda$23$best.component1().floatValue();
                                float fFloatValue4 = pairDragTo$lambda$23$best.component2().floatValue();
                                switch (h) {
                                    case -1:
                                        r += fFloatValue;
                                        b += fFloatValue;
                                        break;
                                    case 0:
                                    case 1:
                                    case 2:
                                        r += fFloatValue;
                                        break;
                                    default:
                                        b += fFloatValue;
                                        break;
                                }
                                float f5 = area.left;
                                float f6 = area.right;
                                float[] fArr6 = new float[4];
                                fArr6[c] = f5;
                                fArr6[1] = fFloatValue4;
                                fArr6[c2] = f6;
                                fArr6[3] = fFloatValue4;
                                gl.add(fArr6);
                                snappedY = true;
                            }
                        }
                        Unit unit = Unit.INSTANCE;
                    }
                }
                grid = Settings.INSTANCE.getGuiGrid();
                if (grid > 0) {
                    g = grid * s.getUnit();
                    if (moveX && !snappedX) {
                        switch (h) {
                            case -1:
                                float nl = dragTo$snapV(g, t, area.left);
                                b2 += nl - t;
                                t = nl;
                                break;
                            case 0:
                            case 6:
                            case 7:
                                t = dragTo$snapV(g, t, area.left);
                                break;
                            default:
                                b2 = dragTo$snapV(g, b2, area.left);
                                break;
                        }
                    }
                    if (c4 != 0 && !snappedY) {
                        switch (h) {
                            case -1:
                                float nt = dragTo$snapV(g, r, area.top);
                                b += nt - r;
                                r = nt;
                                break;
                            case 0:
                            case 1:
                            case 2:
                                r = dragTo$snapV(g, r, area.top);
                                break;
                            default:
                                b = dragTo$snapV(g, b, area.top);
                                break;
                        }
                    }
                }
                this.guides = gl;
                w = b2 - t;
                hh = b - r;
                aw = area.width();
                ah = area.height();
                u = s.getUnit();
                synchronized (this.studio.getLock()) {
                    if (h != -1) {
                        try {
                            try {
                                w2 = w;
                                try {
                                    l = t;
                                    try {
                                        uDim2 = new UDim2(s.getSize().getXs(), Math.round((w - (s.getSize().getXs() * aw)) / u), s.getSize().getYs(), Math.round((hh - (s.getSize().getYs() * ah)) / u));
                                        if (!Intrinsics.areEqual(uDim2, s.getInst().get("Size"))) {
                                            Instance.set$default(s.getInst(), "Size", uDim2, false, 4, null);
                                        }
                                    } catch (Throwable th) {
                                        throw th;
                                    }
                                } catch (Throwable th2) {
                                    Throwable th = th2;
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                Throwable th = th3;
                            }
                        } catch (Throwable th4) {
                            Throwable th = th4;
                        }
                    } else {
                        w2 = w;
                        l = t;
                    }
                    if (!s.getInLayout()) {
                        uDim3 = new UDim2(s.getPos().getXs(), Math.round((((l + (s.getAnchor().getX() * w2)) - area.left) - (s.getPos().getXs() * aw)) / u), s.getPos().getYs(), Math.round(((((s.getAnchor().getY() * hh) + r) - area.top) - (s.getPos().getYs() * ah)) / u));
                        if (!Intrinsics.areEqual(uDim3, s.getInst().get("Position"))) {
                            Instance.set$default(s.getInst(), "Position", uDim3, false, 4, null);
                        }
                    }
                    Unit unit2 = Unit.INSTANCE;
                    postInvalidate();
                }
            }
            c2 = 2;
            c3 = 1;
            c4 = c3;
            snappedX = false;
            snappedY = false;
            if (Settings.INSTANCE.getGuiSmartGuides()) {
                synchronized (this.studio.getLock()) {
                    i = 0;
                    Float[] fArr7 = new Float[3];
                    fArr7[c] = Float.valueOf(area.left);
                    fArr7[1] = Float.valueOf(area.centerX());
                    fArr7[c2] = Float.valueOf(area.right);
                    arrayListArrayListOf = CollectionsKt.arrayListOf(fArr7);
                    Float[] fArr8 = new Float[3];
                    fArr8[c] = Float.valueOf(area.top);
                    fArr8[1] = Float.valueOf(area.centerY());
                    fArr8[c2] = Float.valueOf(area.bottom);
                    arrayListArrayListOf2 = CollectionsKt.arrayListOf(fArr8);
                    for (Map.Entry<Instance, EditBox> entry : this.boxes.entrySet()) {
                        key = entry.getKey();
                        EditBox value2 = entry.getValue();
                        if (key == s.getInst()) {
                        }
                    }
                    f = this.d * 6.0f;
                    if (moveX) {
                        switch (h) {
                            case -1:
                                f2 = 2.0f;
                                Float[] fArr9 = new Float[3];
                                fArr9[c] = Float.valueOf(t);
                                fArr9[1] = Float.valueOf((t + b2) / 2.0f);
                                fArr9[c2] = Float.valueOf(b2);
                                listListOf2 = CollectionsKt.listOf(fArr9);
                                break;
                            case 0:
                            case 6:
                            case 7:
                                listListOf2 = CollectionsKt.listOf(Float.valueOf(t));
                                f2 = 2.0f;
                                break;
                            default:
                                f2 = 2.0f;
                                listListOf2 = CollectionsKt.listOf(Float.valueOf(b2));
                                break;
                        }
                        pairDragTo$lambda$23$best2 = dragTo$lambda$23$best(f, listListOf2, arrayListArrayListOf);
                        if (pairDragTo$lambda$23$best2 != null) {
                            fFloatValue2 = pairDragTo$lambda$23$best2.component1().floatValue();
                            float fFloatValue5 = pairDragTo$lambda$23$best2.component2().floatValue();
                            switch (h) {
                                case -1:
                                    t += fFloatValue2;
                                    b2 += fFloatValue2;
                                    break;
                                case 0:
                                case 6:
                                case 7:
                                    t += fFloatValue2;
                                    break;
                                default:
                                    b2 += fFloatValue2;
                                    break;
                            }
                            float f7 = area.top;
                            float f8 = area.bottom;
                            float[] fArr10 = new float[4];
                            fArr10[c] = fFloatValue5;
                            fArr10[1] = f7;
                            fArr10[c2] = fFloatValue5;
                            fArr10[3] = f8;
                            gl.add(fArr10);
                            snappedX = true;
                        }
                    } else {
                        f2 = 2.0f;
                    }
                    if (c4 != 0) {
                        switch (h) {
                            case -1:
                                Float[] fArr11 = new Float[3];
                                fArr11[c] = Float.valueOf(r);
                                fArr11[1] = Float.valueOf((r + b) / f2);
                                fArr11[c2] = Float.valueOf(b);
                                listListOf = CollectionsKt.listOf(fArr11);
                                break;
                            case 0:
                            case 1:
                            case 2:
                                fValueOf = Float.valueOf(r);
                                listListOf = CollectionsKt.listOf(fValueOf);
                                break;
                            default:
                                fValueOf = Float.valueOf(b);
                                listListOf = CollectionsKt.listOf(fValueOf);
                                break;
                        }
                        pairDragTo$lambda$23$best = dragTo$lambda$23$best(f, listListOf, arrayListArrayListOf2);
                        if (pairDragTo$lambda$23$best != null) {
                            fFloatValue = pairDragTo$lambda$23$best.component1().floatValue();
                            float fFloatValue6 = pairDragTo$lambda$23$best.component2().floatValue();
                            switch (h) {
                                case -1:
                                    r += fFloatValue;
                                    b += fFloatValue;
                                    break;
                                case 0:
                                case 1:
                                case 2:
                                    r += fFloatValue;
                                    break;
                                default:
                                    b += fFloatValue;
                                    break;
                            }
                            float f9 = area.left;
                            float f10 = area.right;
                            float[] fArr12 = new float[4];
                            fArr12[c] = f9;
                            fArr12[1] = fFloatValue6;
                            fArr12[c2] = f10;
                            fArr12[3] = fFloatValue6;
                            gl.add(fArr12);
                            snappedY = true;
                        }
                    }
                    Unit unit3 = Unit.INSTANCE;
                }
            }
            grid = Settings.INSTANCE.getGuiGrid();
            if (grid > 0) {
                g = grid * s.getUnit();
                if (moveX) {
                    switch (h) {
                        case -1:
                            float nl2 = dragTo$snapV(g, t, area.left);
                            b2 += nl2 - t;
                            t = nl2;
                            break;
                        case 0:
                        case 6:
                        case 7:
                            t = dragTo$snapV(g, t, area.left);
                            break;
                        default:
                            b2 = dragTo$snapV(g, b2, area.left);
                            break;
                    }
                }
                if (c4 != 0) {
                    switch (h) {
                        case -1:
                            float nt2 = dragTo$snapV(g, r, area.top);
                            b += nt2 - r;
                            r = nt2;
                            break;
                        case 0:
                        case 1:
                        case 2:
                            r = dragTo$snapV(g, r, area.top);
                            break;
                        default:
                            b = dragTo$snapV(g, b, area.top);
                            break;
                    }
                }
            }
            this.guides = gl;
            w = b2 - t;
            hh = b - r;
            aw = area.width();
            ah = area.height();
            u = s.getUnit();
            synchronized (this.studio.getLock()) {
                if (h != -1) {
                    w2 = w;
                    l = t;
                    uDim2 = new UDim2(s.getSize().getXs(), Math.round((w - (s.getSize().getXs() * aw)) / u), s.getSize().getYs(), Math.round((hh - (s.getSize().getYs() * ah)) / u));
                    if (!Intrinsics.areEqual(uDim2, s.getInst().get("Size"))) {
                        Instance.set$default(s.getInst(), "Size", uDim2, false, 4, null);
                    }
                } else {
                    w2 = w;
                    l = t;
                }
                if (!s.getInLayout()) {
                    uDim3 = new UDim2(s.getPos().getXs(), Math.round((((l + (s.getAnchor().getX() * w2)) - area.left) - (s.getPos().getXs() * aw)) / u), s.getPos().getYs(), Math.round(((((s.getAnchor().getY() * hh) + r) - area.top) - (s.getPos().getYs() * ah)) / u));
                    if (!Intrinsics.areEqual(uDim3, s.getInst().get("Position"))) {
                        Instance.set$default(s.getInst(), "Position", uDim3, false, 4, null);
                    }
                }
                Unit unit4 = Unit.INSTANCE;
                postInvalidate();
            }
        }
        c = 0;
        if (h == -1) {
            c2 = 2;
            c3 = 1;
        } else {
            c2 = 2;
            c3 = 1;
        }
        c4 = c3;
        snappedX = false;
        snappedY = false;
        if (Settings.INSTANCE.getGuiSmartGuides()) {
            synchronized (this.studio.getLock()) {
                i = 0;
                Float[] fArr13 = new Float[3];
                fArr13[c] = Float.valueOf(area.left);
                fArr13[1] = Float.valueOf(area.centerX());
                fArr13[c2] = Float.valueOf(area.right);
                arrayListArrayListOf = CollectionsKt.arrayListOf(fArr13);
                Float[] fArr14 = new Float[3];
                fArr14[c] = Float.valueOf(area.top);
                fArr14[1] = Float.valueOf(area.centerY());
                fArr14[c2] = Float.valueOf(area.bottom);
                arrayListArrayListOf2 = CollectionsKt.arrayListOf(fArr14);
                for (Map.Entry<Instance, EditBox> entry : this.boxes.entrySet()) {
                    key = entry.getKey();
                    EditBox value3 = entry.getValue();
                    if (key == s.getInst()) {
                    }
                }
                f = this.d * 6.0f;
                if (moveX) {
                    switch (h) {
                        case -1:
                            f2 = 2.0f;
                            Float[] fArr15 = new Float[3];
                            fArr15[c] = Float.valueOf(t);
                            fArr15[1] = Float.valueOf((t + b2) / 2.0f);
                            fArr15[c2] = Float.valueOf(b2);
                            listListOf2 = CollectionsKt.listOf(fArr15);
                            break;
                        case 0:
                        case 6:
                        case 7:
                            listListOf2 = CollectionsKt.listOf(Float.valueOf(t));
                            f2 = 2.0f;
                            break;
                        default:
                            f2 = 2.0f;
                            listListOf2 = CollectionsKt.listOf(Float.valueOf(b2));
                            break;
                    }
                    pairDragTo$lambda$23$best2 = dragTo$lambda$23$best(f, listListOf2, arrayListArrayListOf);
                    if (pairDragTo$lambda$23$best2 != null) {
                        fFloatValue2 = pairDragTo$lambda$23$best2.component1().floatValue();
                        float fFloatValue7 = pairDragTo$lambda$23$best2.component2().floatValue();
                        switch (h) {
                            case -1:
                                t += fFloatValue2;
                                b2 += fFloatValue2;
                                break;
                            case 0:
                            case 6:
                            case 7:
                                t += fFloatValue2;
                                break;
                            default:
                                b2 += fFloatValue2;
                                break;
                        }
                        float f11 = area.top;
                        float f12 = area.bottom;
                        float[] fArr16 = new float[4];
                        fArr16[c] = fFloatValue7;
                        fArr16[1] = f11;
                        fArr16[c2] = fFloatValue7;
                        fArr16[3] = f12;
                        gl.add(fArr16);
                        snappedX = true;
                    }
                } else {
                    f2 = 2.0f;
                }
                if (c4 != 0) {
                    switch (h) {
                        case -1:
                            Float[] fArr17 = new Float[3];
                            fArr17[c] = Float.valueOf(r);
                            fArr17[1] = Float.valueOf((r + b) / f2);
                            fArr17[c2] = Float.valueOf(b);
                            listListOf = CollectionsKt.listOf(fArr17);
                            break;
                        case 0:
                        case 1:
                        case 2:
                            fValueOf = Float.valueOf(r);
                            listListOf = CollectionsKt.listOf(fValueOf);
                            break;
                        default:
                            fValueOf = Float.valueOf(b);
                            listListOf = CollectionsKt.listOf(fValueOf);
                            break;
                    }
                    pairDragTo$lambda$23$best = dragTo$lambda$23$best(f, listListOf, arrayListArrayListOf2);
                    if (pairDragTo$lambda$23$best != null) {
                        fFloatValue = pairDragTo$lambda$23$best.component1().floatValue();
                        float fFloatValue8 = pairDragTo$lambda$23$best.component2().floatValue();
                        switch (h) {
                            case -1:
                                r += fFloatValue;
                                b += fFloatValue;
                                break;
                            case 0:
                            case 1:
                            case 2:
                                r += fFloatValue;
                                break;
                            default:
                                b += fFloatValue;
                                break;
                        }
                        float f13 = area.left;
                        float f14 = area.right;
                        float[] fArr18 = new float[4];
                        fArr18[c] = f13;
                        fArr18[1] = fFloatValue8;
                        fArr18[c2] = f14;
                        fArr18[3] = fFloatValue8;
                        gl.add(fArr18);
                        snappedY = true;
                    }
                }
                Unit unit5 = Unit.INSTANCE;
            }
        }
        grid = Settings.INSTANCE.getGuiGrid();
        if (grid > 0) {
            g = grid * s.getUnit();
            if (moveX) {
                switch (h) {
                    case -1:
                        float nl3 = dragTo$snapV(g, t, area.left);
                        b2 += nl3 - t;
                        t = nl3;
                        break;
                    case 0:
                    case 6:
                    case 7:
                        t = dragTo$snapV(g, t, area.left);
                        break;
                    default:
                        b2 = dragTo$snapV(g, b2, area.left);
                        break;
                }
            }
            if (c4 != 0) {
                switch (h) {
                    case -1:
                        float nt3 = dragTo$snapV(g, r, area.top);
                        b += nt3 - r;
                        r = nt3;
                        break;
                    case 0:
                    case 1:
                    case 2:
                        r = dragTo$snapV(g, r, area.top);
                        break;
                    default:
                        b = dragTo$snapV(g, b, area.top);
                        break;
                }
            }
        }
        this.guides = gl;
        w = b2 - t;
        hh = b - r;
        aw = area.width();
        ah = area.height();
        u = s.getUnit();
        synchronized (this.studio.getLock()) {
            if (h != -1) {
                w2 = w;
                l = t;
                uDim2 = new UDim2(s.getSize().getXs(), Math.round((w - (s.getSize().getXs() * aw)) / u), s.getSize().getYs(), Math.round((hh - (s.getSize().getYs() * ah)) / u));
                if (!Intrinsics.areEqual(uDim2, s.getInst().get("Size"))) {
                    Instance.set$default(s.getInst(), "Size", uDim2, false, 4, null);
                }
            } else {
                w2 = w;
                l = t;
            }
            if (!s.getInLayout()) {
                uDim3 = new UDim2(s.getPos().getXs(), Math.round((((l + (s.getAnchor().getX() * w2)) - area.left) - (s.getPos().getXs() * aw)) / u), s.getPos().getYs(), Math.round(((((s.getAnchor().getY() * hh) + r) - area.top) - (s.getPos().getYs() * ah)) / u));
                if (!Intrinsics.areEqual(uDim3, s.getInst().get("Position"))) {
                    Instance.set$default(s.getInst(), "Position", uDim3, false, 4, null);
                }
            }
            Unit unit6 = Unit.INSTANCE;
            postInvalidate();
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private static final Pair<Float, Float> dragTo$lambda$23$best(float tol, List<Float> list, List<Float> list2) {
        float bd = tol;
        Pair<Float, Float> pair = null;
        Iterator<Float> it = list.iterator();
        while (it.hasNext()) {
            float e = it.next().floatValue();
            Iterator<Float> it2 = list2.iterator();
            while (it2.hasNext()) {
                float c = it2.next().floatValue();
                float diff = c - e;
                if (Math.abs(diff) < bd) {
                    bd = Math.abs(diff);
                    pair = TuplesKt.to(Float.valueOf(diff), Float.valueOf(c));
                }
            }
        }
        return pair;
    }

    private static final float dragTo$snapV(float g, float v, float origin) {
        return (Math.round((v - origin) / g) * g) + origin;
    }

    public final void endDrag() {
        this.guides = CollectionsKt.emptyList();
        postInvalidate();
    }

    private final void useGameUnits() {
        this.absUnit = this.d;
        this.absInset = 58.0f;
    }

    private final void usePluginUnits() {
        this.absUnit = this.d * this.studio.editGuiScale();
        this.absInset = 0.0f;
    }

    private final void setAbs(Instance c, RectF r) {
        if (this.absUnit <= 0.0f) {
            useGameUnits();
        }
        float u = this.absUnit;
        float x = r.left / u;
        float y = (r.top / u) - this.absInset;
        float w = r.width() / u;
        float h = r.height() / u;
        boolean sizeChanged = Math.abs(c.getAbsW() - w) > 0.25f || Math.abs(c.getAbsH() - h) > 0.25f;
        boolean posChanged = Math.abs(c.getAbsX() - x) > 0.25f || Math.abs(c.getAbsY() - y) > 0.25f;
        c.setAbsX(x);
        c.setAbsY(y);
        c.setAbsW(w);
        c.setAbsH(h);
        if (sizeChanged) {
            this.absChanged.add(TuplesKt.to(c, "AbsoluteSize"));
        }
        if (posChanged) {
            this.absChanged.add(TuplesKt.to(c, "AbsolutePosition"));
        }
    }

    private final void setContentSize(Instance layout, float wPx, float hPx) {
        if (this.absUnit <= 0.0f) {
            useGameUnits();
        }
        float w = wPx / this.absUnit;
        float h = hPx / this.absUnit;
        if (Math.abs(layout.getAbsW() - w) > 0.25f || Math.abs(layout.getAbsH() - h) > 0.25f) {
            this.absChanged.add(TuplesKt.to(layout, "AbsoluteContentSize"));
        }
        layout.setAbsW(w);
        layout.setAbsH(h);
    }

    private final void fireAbsChanges() {
        if (this.absChanged.isEmpty()) {
            this.absRounds = 0;
            return;
        }
        ArrayList list = new ArrayList(this.absChanged);
        this.absChanged.clear();
        LuaRuntime[] luaRuntimeArr = new LuaRuntime[2];
        luaRuntimeArr[0] = this.studio.getEditRuntime();
        PlaySession play = this.studio.getPlay();
        luaRuntimeArr[1] = play != null ? play.getRt() : null;
        List<LuaRuntime> runtimes = CollectionsKt.distinct(CollectionsKt.listOfNotNull(luaRuntimeArr));
        Iterator it = list.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            Instance inst = (Instance) pair.component1();
            String prop = (String) pair.component2();
            for (LuaRuntime r : runtimes) {
                if (r.hasConn(inst, "Changed:" + prop)) {
                    r.fire(inst, "Changed:" + prop, new LuaValue[0]);
                }
                if (r.hasConn(inst, "Changed")) {
                    LuaString luaStringValueOf = LuaValue.valueOf(prop);
                    Intrinsics.checkNotNullExpressionValue(luaStringValueOf, "valueOf(...)");
                    r.fire(inst, "Changed", luaStringValueOf);
                }
            }
        }
        this.absRounds++;
        if (this.absRounds <= 30) {
            postInvalidate();
        }
    }

    private final void drawTopBar(Canvas canvas) {
        float r = this.d * 20.0f;
        float cx = (this.d * 12.0f) + r;
        float cy = (this.d * 8.0f) + r;
        this.fill.setShader(null);
        this.fill.setColor(-1946157056);
        canvas.drawCircle(cx, cy, r, this.fill);
        this.stroke.setColor(-1);
        this.stroke.setStrokeWidth(this.d * 2.6f);
        this.stroke.setShader(null);
        canvas.save();
        canvas.rotate(15.0f, cx, cy);
        float s = this.d * 8.0f;
        canvas.drawRoundRect(cx - s, cy - s, cx + s, cy + s, this.d * 2.0f, this.d * 2.0f, this.stroke);
        this.fill.setColor(-1);
        canvas.drawRect(cx - (this.d * 2.2f), cy - (this.d * 2.2f), (this.d * 2.2f) + cx, (this.d * 2.2f) + cy, this.fill);
        canvas.restore();
        this.newHits.add(new Hit(this.studio.getModel(), new RectF(cx - r, cy - r, cx + r, cy + r), null, 3));
        float cx2 = cx + (2.0f * r) + (this.d * 8.0f);
        this.fill.setColor(-1946157056);
        canvas.drawCircle(cx2, cy, r, this.fill);
        this.fill.setColor(-1);
        canvas.drawRoundRect(cx2 - (this.d * 9.0f), cy - (this.d * 7.0f), (this.d * 9.0f) + cx2, (this.d * 5.0f) + cy, this.d * 4.0f, this.d * 4.0f, this.fill);
        Path tail = new Path();
        tail.moveTo(cx2 - (this.d * 4.0f), (this.d * 4.0f) + cy);
        tail.lineTo(cx2 - (this.d * 6.0f), (this.d * 10.0f) + cy);
        tail.lineTo((this.d * 1.0f) + cx2, (this.d * 4.0f) + cy);
        tail.close();
        canvas.drawPath(tail, this.fill);
    }

    private final float pluginScale(float viewH) {
        return this.studio.editGuiScale();
    }

    private final boolean drawPluginWidgets(Canvas canvas, float w, float h) {
        boolean any = false;
        float x = 12.0f;
        float y = this.d * 12.0f;
        float ps = pluginScale(h);
        usePluginUnits();
        Iterator<PluginWidget> it = this.studio.getPluginWidgets().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        float y2 = y;
        while (it.hasNext()) {
            PluginWidget pw = it.next();
            Instance wi = pw.getInst();
            if (Intrinsics.areEqual(wi.get("Enabled"), (Object) true)) {
                Object obj = wi.getAttributes().get("__w");
                Number number = obj instanceof Number ? (Number) obj : null;
                float ww = RangesKt.coerceAtMost((number != null ? number.floatValue() : 300.0f) * ps, (w / this.d) * 0.6f) * this.d;
                Object obj2 = wi.getAttributes().get("__h");
                Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
                float wh = RangesKt.coerceAtMost((number2 != null ? number2.floatValue() : 300.0f) * ps, (h / this.d) * 0.8f) * this.d;
                float x2 = (w - ww) - (this.d * x);
                this.fill.setShader(null);
                this.fill.setColor(-13750738);
                canvas.drawRect(x2, y2, x2 + ww, (this.d * 24.0f) + y2 + wh, this.fill);
                this.fill.setColor(Ui.INSTANCE.getHEADER());
                canvas.drawRect(x2, y2, x2 + ww, y2 + (this.d * 24.0f), this.fill);
                this.textPaint.setColor(Ui.INSTANCE.getTEXT());
                this.textPaint.setTextSize(this.d * 13.0f);
                this.textPaint.setTypeface(Typeface.DEFAULT);
                this.textPaint.setShader(null);
                Object obj3 = wi.get("Title");
                String name = obj3 instanceof String ? (String) obj3 : null;
                if (name == null) {
                    name = wi.getName();
                }
                float f = x;
                canvas.drawText(name, x2 + (this.d * 8.0f), (this.d * 17.0f) + y2, this.textPaint);
                this.stroke.setColor(Ui.INSTANCE.getTEXT());
                this.stroke.setStrokeWidth(this.d * 1.6f);
                this.stroke.setShader(null);
                float cx = (x2 + ww) - (this.d * 14.0f);
                float cy = y2 + (this.d * f);
                float k = this.d * 4.5f;
                float x3 = cx - k;
                float y3 = y2;
                float y4 = cy - k;
                float x4 = cx + k;
                float y5 = cy + k;
                canvas.drawLine(x3, y4, x4, y5, this.stroke);
                canvas.drawLine(cx + k, cy - k, cx - k, cy + k, this.stroke);
                ArrayList<Hit> arrayList = this.newHits;
                float ps2 = ps;
                float ps3 = this.d;
                arrayList.add(new Hit(wi, new RectF((x2 + ww) - (this.d * 28.0f), y3, x2 + ww, (ps3 * 24.0f) + y3), pw.getRt(), 4));
                RectF content = new RectF(x2, (this.d * 24.0f) + y3, x2 + ww, (this.d * 24.0f) + y3 + wh);
                canvas.save();
                canvas.clipRect(content);
                LuaRuntime saved = this.rt;
                this.rt = pw.getRt();
                setAbs(wi, new RectF(content));
                drawChildren(canvas, wi, content, ps2, content);
                this.rt = saved;
                canvas.restore();
                y2 = y3 + wh + (this.d * 36.0f);
                ps = ps2;
                any = true;
                x = f;
            }
        }
        useGameUnits();
        return any;
    }

    private final boolean drawBillboards(Canvas canvas, boolean playing) {
        char c;
        boolean z;
        Vector3 vector3Plus = null;
        float f = 0.0f;
        PlaySession play;
        Instance player;
        Instance instanceFindFirstChild$default;
        List<Instance> listDescendants$default;
        List<Instance> listDescendants$default2;
        GuiOverlay guiOverlay = this;
        long jCurrentTimeMillis = System.currentTimeMillis();
        char c2 = 2;
        boolean z2 = false;
        Instance instance = null;
        if (jCurrentTimeMillis - guiOverlay.billboardScan > 500) {
            guiOverlay.billboardScan = jCurrentTimeMillis;
            ArrayList arrayList = new ArrayList();
            Instance instanceFindService = guiOverlay.studio.getModel().findService("Workspace");
            if (instanceFindService != null && (listDescendants$default2 = Instance.descendants$default(instanceFindService, null, 1, null)) != null) {
                for (Instance instance2 : listDescendants$default2) {
                    if (Intrinsics.areEqual(instance2.getClassName(), "BillboardGui")) {
                        arrayList.add(instance2);
                    }
                }
            }
            if (playing && (play = guiOverlay.studio.getPlay()) != null && (player = play.getPlayer()) != null && (instanceFindFirstChild$default = Instance.findFirstChild$default(player, "PlayerGui", false, 2, null)) != null && (listDescendants$default = Instance.descendants$default(instanceFindFirstChild$default, null, 1, null)) != null) {
                for (Instance instance3 : listDescendants$default) {
                    if (Intrinsics.areEqual(instance3.getClassName(), "BillboardGui") && (instance3.get("Adornee") instanceof Instance)) {
                        arrayList.add(instance3);
                    }
                }
            }
            guiOverlay.billboards = arrayList;
        }
        if (guiOverlay.billboards.isEmpty()) {
            return false;
        }
        ViewState view = guiOverlay.studio.getView();
        FlyCamera camera = guiOverlay.studio.getCamera();
        float fTan = (float) Math.tan(Math.toRadians(((double) camera.getFov()) / 2.0d));
        ArrayList arrayList2 = new ArrayList();
        Iterator<? extends Instance> it = guiOverlay.billboards.iterator();
        while (it.hasNext()) {
            Instance next = it.next();
            if (next.getParent() == null) {
                instance = null;
            } else if (!Intrinsics.areEqual(next.get("Enabled"), Boolean.valueOf(z2))) {
                Object obj = next.get("Adornee");
                Instance parent = obj instanceof Instance ? (Instance) obj : instance;
                if (parent != null || (parent = next.getParent()) != null) {
                    if (parent.isA("BasePart")) {
                        Vector3 position = PartData.INSTANCE.cframe(parent).getPosition();
                        c = c2;
                        CFrame cFrameRotationOnly = PartData.INSTANCE.cframe(parent).rotationOnly();
                        z = z2;
                        Object obj2 = next.get("ExtentsOffset");
                        Vector3 zero = obj2 instanceof Vector3 ? (Vector3) obj2 : null;
                        if (zero == null) {
                            zero = Vector3.INSTANCE.getZERO();
                        }
                        vector3Plus = position.plus(cFrameRotationOnly.times(zero));
                        f = 1.0f;
                    } else {
                        c = c2;
                        z = z2;
                        if (Intrinsics.areEqual(parent.getClassName(), "Attachment")) {
                            Instance parent2 = parent.getParent();
                            if (parent2 != null) {
                                if (!parent2.isA("BasePart")) {
                                    parent2 = null;
                                }
                                if (parent2 != null) {
                                    CFrame cframe = PartData.INSTANCE.cframe(parent2);
                                    Object obj3 = parent.get("CFrame");
                                    f = 1.0f;
                                    CFrame identity = obj3 instanceof CFrame ? (CFrame) obj3 : null;
                                    if (identity == null) {
                                        identity = CFrame.INSTANCE.getIDENTITY();
                                    }
                                    vector3Plus = cframe.times(identity.getPosition());
                                    if (vector3Plus != null) {
                                    }
                                }
                            }
                            c2 = c;
                            z2 = z;
                            instance = null;
                        } else {
                            f = 1.0f;
                            if (parent.isA("Model")) {
                                Object obj4 = parent.get("PrimaryPart");
                                Instance instanceFindFirstChildWhichIsA = obj4 instanceof Instance ? (Instance) obj4 : null;
                                if (instanceFindFirstChildWhichIsA == null) {
                                    instanceFindFirstChildWhichIsA = parent.findFirstChildWhichIsA("BasePart", true);
                                }
                                if (instanceFindFirstChildWhichIsA == null || (vector3Plus = PartData.INSTANCE.cframe(instanceFindFirstChildWhichIsA).getPosition()) == null) {
                                    c2 = c;
                                    z2 = z;
                                    instance = null;
                                }
                            } else {
                                c2 = c;
                                z2 = z;
                                instance = null;
                            }
                        }
                    }
                    Object obj5 = next.get("StudsOffset");
                    Vector3 zero2 = obj5 instanceof Vector3 ? (Vector3) obj5 : null;
                    if (zero2 == null) {
                        zero2 = Vector3.INSTANCE.getZERO();
                    }
                    Object obj6 = next.get("StudsOffsetWorldSpace");
                    Vector3 zero3 = obj6 instanceof Vector3 ? (Vector3) obj6 : null;
                    if (zero3 == null) {
                        zero3 = Vector3.INSTANCE.getZERO();
                    }
                    Vector3 vector3Minus = vector3Plus.plus(zero3).plus(camera.getRight().times(zero2.getX())).plus(camera.getUp().times(zero2.getY())).minus(camera.getForward().times(zero2.getZ()));
                    float fDistTo = view.distTo(vector3Minus);
                    Object obj7 = next.get("MaxDistance");
                    Iterator<? extends Instance> it2 = it;
                    Number number = obj7 instanceof Number ? (Number) obj7 : null;
                    if (fDistTo > (number != null ? number.floatValue() : Float.POSITIVE_INFINITY)) {
                        c2 = c;
                        z2 = z;
                        it = it2;
                        instance = null;
                    } else if (fDistTo < 0.1f) {
                        c2 = c;
                        z2 = z;
                        it = it2;
                        instance = null;
                    } else {
                        float[] fArrProject = view.project(vector3Minus);
                        if (fArrProject == null) {
                            c2 = c;
                            z2 = z;
                            it = it2;
                            instance = null;
                        } else if (fArrProject[c] <= f) {
                            arrayList2.add(new Triple(next, fArrProject, Float.valueOf(fDistTo)));
                            c2 = c;
                            z2 = z;
                            it = it2;
                            instance = null;
                        } else {
                            c2 = c;
                            z2 = z;
                            it = it2;
                            instance = null;
                        }
                    }
                }
            }
        }
        boolean z3 = z2;
        if (arrayList2.isEmpty()) {
            return z3;
        }
        ArrayList arrayList3 = arrayList2;
        if (arrayList3.size() > 1) {
            CollectionsKt.sortWith(arrayList3, new Comparator() { // from class: com.goydevv.studrblx.ui.GuiOverlay$drawBillboards$$inlined$sortByDescending$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(Object t, Object t2) {
                    return ComparisonsKt.compareValues((Float) ((Triple) t2).getThird(), (Float) ((Triple) t).getThird());
                }
            });
        }
        Iterator it3 = arrayList2.iterator();
        Intrinsics.checkNotNullExpressionValue(it3, "iterator(...)");
        while (it3.hasNext()) {
            Object next2 = it3.next();
            Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
            Triple triple = (Triple) next2;
            Instance instance4 = (Instance) triple.component1();
            float[] fArr = (float[]) triple.component2();
            float height = guiOverlay.getHeight() / ((((Number) triple.component3()).floatValue() * 2.0f) * fTan);
            UDim2 uDim2Udim = guiOverlay.udim(instance4.get("Size"));
            float xs = (uDim2Udim.getXs() * height) + (uDim2Udim.getXo() * guiOverlay.d);
            float ys = (uDim2Udim.getYs() * height) + (uDim2Udim.getYo() * guiOverlay.d);
            if (xs < 1.0f) {
                guiOverlay = this;
            } else if (ys >= 1.0f) {
                Object obj8 = instance4.get("SizeOffset");
                Vector2 vector2 = obj8 instanceof Vector2 ? (Vector2) obj8 : null;
                if (vector2 == null) {
                    vector2 = new Vector2(0.0f, 0.0f);
                }
                Vector2 vector3 = vector2;
                float x = (fArr[z3 ? 1 : 0] - (xs / 2.0f)) + (vector3.getX() * xs);
                float y = (fArr[1] - (ys / 2.0f)) - (vector3.getY() * ys);
                RectF rectF = new RectF(x, y, x + xs, y + ys);
                if (RectF.intersects(rectF, new RectF(0.0f, 0.0f, getWidth(), getHeight()))) {
                    boolean zAreEqual = Intrinsics.areEqual(instance4.get("ClipsDescendants"), (Object) true);
                    if (zAreEqual) {
                        canvas.save();
                        canvas.clipRect(rectF);
                    }
                    drawChildren(canvas, instance4, rectF, 1.0f, zAreEqual ? rectF : new RectF(0.0f, 0.0f, getWidth(), getHeight()));
                    if (zAreEqual) {
                        canvas.restore();
                        guiOverlay = this;
                    } else {
                        guiOverlay = this;
                    }
                } else {
                    guiOverlay = this;
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: GuiOverlay.kt */
    static final class Node {
        private final RectF area;
        private final boolean inLayout;
        private final Instance inst;
        private final RectF rect;
        private final float scale;

        public Node(Instance inst, RectF rect, float scale, RectF area, boolean inLayout) {
            Intrinsics.checkNotNullParameter(inst, "inst");
            Intrinsics.checkNotNullParameter(rect, "rect");
            this.inst = inst;
            this.rect = rect;
            this.scale = scale;
            this.area = area;
            this.inLayout = inLayout;
        }

        public /* synthetic */ Node(Instance instance, RectF rectF, float f, RectF rectF2, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(instance, rectF, f, (i & 8) != 0 ? null : rectF2, (i & 16) != 0 ? false : z);
        }

        public final RectF getArea() {
            return this.area;
        }

        public final boolean getInLayout() {
            return this.inLayout;
        }

        public final Instance getInst() {
            return this.inst;
        }

        public final RectF getRect() {
            return this.rect;
        }

        public final float getScale() {
            return this.scale;
        }
    }

    private final UDim2 udim(Object v) {
        UDim2 uDim2 = v instanceof UDim2 ? (UDim2) v : null;
        return uDim2 == null ? new UDim2(0.0f, 0, 0.0f, 0) : uDim2;
    }

    private final int enumOf(Instance i, String prop, int def) {
        Object obj = i != null ? i.get(prop) : null;
        EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
        return enumValue != null ? enumValue.getValue() : def;
    }

    private final float num(Instance i, String prop, float def) {
        Object obj = i != null ? i.get(prop) : null;
        Number number = obj instanceof Number ? (Number) obj : null;
        return number != null ? number.floatValue() : def;
    }

    private final Instance child(Instance p, String cls) {
        Object next;
        Iterator it = p.getChildren().iterator();
        while (it.hasNext()) {
            next = it.next();
            if (Intrinsics.areEqual(((Instance) next).getClassName(), cls)) {
                return (Instance) next;
            }
        }
        next = null;
        return (Instance) next;
    }

    private final boolean isText(Instance c) {
        return Intrinsics.areEqual(c.getClassName(), "TextLabel") || Intrinsics.areEqual(c.getClassName(), "TextButton") || Intrinsics.areEqual(c.getClassName(), "TextBox");
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final String textOf(Instance c) {
        String className = c.getClassName();
        switch (className.hashCode()) {
            case -935338273:
                if (!className.equals("TextButton")) {
                    return null;
                }
                break;
            case 246787390:
                if (!className.equals("TextBox")) {
                    return null;
                }
                Object obj = c.get("Text");
                String str = obj instanceof String ? (String) obj : null;
                if (str == null) {
                    return null;
                }
                String str2 = str;
                if (str2.length() == 0) {
                    Object obj2 = c.get("PlaceholderText");
                    str2 = obj2 instanceof String ? (String) obj2 : null;
                }
                return str2;
            case 948280743:
                if (!className.equals("TextLabel")) {
                    return null;
                }
                break;
            default:
                return null;
        }
        Object obj3 = c.get("Text");
        if (obj3 instanceof String) {
            return (String) obj3;
        }
        return null;
    }

    private final RectF padded(Instance p, RectF r, float scale) {
        Instance pad = child(p, "UIPadding");
        return pad == null ? r : new RectF(r.left + padded$u(pad, this, scale, "PaddingLeft", r.width()), r.top + padded$u(pad, this, scale, "PaddingTop", r.height()), r.right - padded$u(pad, this, scale, "PaddingRight", r.width()), r.bottom - padded$u(pad, this, scale, "PaddingBottom", r.height()));
    }

    private static final float padded$u(Instance pad, GuiOverlay this$0, float $scale, String n, float size) {
        Object obj = pad.get(n);
        UDim uDim = obj instanceof UDim ? (UDim) obj : null;
        if (uDim != null) {
            return (uDim.getScale() * size) + (uDim.getOffset() * this$0.d * $scale);
        }
        return 0.0f;
    }

    private final float[] constrain(Instance c, float w0, float h0, float scale) {
        float fCoerceIn = w0;
        float fCoerceIn2 = h0;
        Iterator<Instance> it = c.getChildren().iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (true) {
            if (it.hasNext()) {
                Instance next = it.next();
                Intrinsics.checkNotNullExpressionValue(next, "next(...)");
                Instance instance = next;
                String className = instance.getClassName();
                if (Intrinsics.areEqual(className, "UIAspectRatioConstraint")) {
                    Float fValueOf = Float.valueOf(num(instance, "AspectRatio", 1.0f));
                    Float f = fValueOf.floatValue() > 0.0f ? fValueOf : null;
                    float fFloatValue = f != null ? f.floatValue() : 1.0f;
                    if (enumOf(instance, "AspectType", 0) == 0) {
                        if (fCoerceIn2 <= 0.0f || fCoerceIn / fCoerceIn2 <= fFloatValue) {
                            fCoerceIn2 = fCoerceIn / fFloatValue;
                        } else {
                            fCoerceIn = fCoerceIn2 * fFloatValue;
                        }
                    } else if (enumOf(instance, "DominantAxis", 0) == 0) {
                        fCoerceIn2 = fCoerceIn / fFloatValue;
                    } else {
                        fCoerceIn = fCoerceIn2 * fFloatValue;
                    }
                } else if (Intrinsics.areEqual(className, "UISizeConstraint")) {
                    Object obj = instance.get("MinSize");
                    Vector2 vector2 = obj instanceof Vector2 ? (Vector2) obj : null;
                    if (vector2 == null) {
                        vector2 = new Vector2(0.0f, 0.0f);
                    }
                    Object obj2 = instance.get("MaxSize");
                    Vector2 vector3 = obj2 instanceof Vector2 ? (Vector2) obj2 : null;
                    if (vector3 == null) {
                        vector3 = new Vector2(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
                    }
                    fCoerceIn = RangesKt.coerceIn(fCoerceIn, vector2.getX() * this.d * scale, Math.max(vector2.getX(), vector3.getX()) * this.d * scale);
                    fCoerceIn2 = RangesKt.coerceIn(fCoerceIn2, vector2.getY() * this.d * scale, Math.max(vector2.getY(), vector3.getY()) * this.d * scale);
                }
            } else {
                return new float[]{fCoerceIn, fCoerceIn2};
            }
        }
    }

    private final float[] sizeOf(Instance c, RectF area, float scale) {
        char c2;
        char c3;
        char c4;
        RectF tmp;
        float fMax;
        float fMax2;
        UDim2 s = udim(c.get("Size"));
        float aw = area.width();
        float ah = area.height();
        float w = (s.getXs() * aw) + (s.getXo() * this.d * scale);
        float h = (s.getYs() * ah) + (s.getYo() * this.d * scale);
        switch (enumOf(c, "SizeConstraint", 0)) {
            case 1:
                h = (s.getYs() * aw) + (s.getYo() * this.d * scale);
                break;
            case 2:
                w = (s.getXs() * ah) + (s.getXo() * this.d * scale);
                break;
        }
        int auto = enumOf(c, "AutomaticSize", 0);
        if (auto != 0 && isText(c)) {
            String strTextOf = textOf(c);
            if (strTextOf == null) {
                strTextOf = "";
            }
            String text = strTextOf;
            Fonts.Face face = Fonts.INSTANCE.of(c);
            this.textPaint.setTypeface(face.getTypeface());
            c2 = 0;
            this.textPaint.setTextSize(num(c, "TextSize", 14.0f) * this.d * scale);
            List lines = StringsKt.split((CharSequence) text, new char[]{'\n'}, false, 0);
            Iterator it = lines.iterator();
            if (!it.hasNext()) {
                throw new NoSuchElementException();
            }
            c3 = 1;
            float fMeasureText = this.textPaint.measureText((String) it.next());
            while (it.hasNext()) {
                fMeasureText = Math.max(fMeasureText, this.textPaint.measureText((String) it.next()));
            }
            float tw = fMeasureText + (this.d * 2.0f);
            float th = lines.size() * this.textPaint.getFontSpacing();
            switch (auto) {
                case 1:
                case 3:
                    w = Math.max(w, tw);
                    break;
            }
            switch (auto) {
                case 2:
                case 3:
                    int wrapW = (int) Math.max(w, 1.0f);
                    StaticLayout l = layoutFor(new TKey(text, Intrinsics.areEqual(c.get("RichText"), (Object) true), face.getTypeface(), face.getFakeBold(), this.textPaint.getTextSize(), wrapW, Layout.Alignment.ALIGN_NORMAL, 1.0f, false));
                    h = Math.max(h, Intrinsics.areEqual(c.get("TextWrapped"), (Object) true) ? l.getHeight() : th);
                    break;
            }
        } else {
            c2 = 0;
            c3 = 1;
        }
        float[] cs = constrain(c, w, h, scale);
        float w2 = cs[c2];
        float h2 = cs[c3];
        Instance instanceChild = child(c, "UIScale");
        if (instanceChild != null) {
            float fNum = num(instanceChild, "Scale", 1.0f);
            w2 *= fNum;
            h2 *= fNum;
        }
        if (auto != 0 && !Intrinsics.areEqual(c.getClassName(), "ScrollingFrame")) {
            Iterable children = c.getChildren();
            if ((children instanceof Collection) && ((Collection) children).isEmpty()) {
                c4 = c2;
            } else {
                c4 = c2;
                Iterator it2 = children.iterator();
                while (it2.hasNext()) {
                    Instance instance = (Instance) it2.next();
                    String cn = instance.getClassName();
                    if (Intrinsics.areEqual(cn, "UIListLayout") || Intrinsics.areEqual(cn, "UIGridLayout") || Intrinsics.areEqual(cn, "UITableLayout") || Intrinsics.areEqual(cn, "UIPageLayout")) {
                        c4 = c3;
                        break;
                    }
                }
            }
            if (c4 != 0) {
                tmp = new RectF(0.0f, 0.0f, w2, h2);
            } else {
                switch (auto) {
                    case 1:
                    case 3:
                        fMax = Math.max(w2, aw);
                        break;
                    case 2:
                    default:
                        fMax = w2;
                        break;
                }
                switch (auto) {
                    case 2:
                    case 3:
                        fMax2 = Math.max(h2, ah);
                        break;
                    default:
                        fMax2 = h2;
                        break;
                }
                tmp = new RectF(0.0f, 0.0f, fMax, fMax2);
            }
            float mx = w2;
            float my = h2;
            for (Node n : layoutChildren(c, padded(c, tmp, scale), scaleFor(c, scale))) {
                mx = Math.max(mx, n.getRect().right);
                my = Math.max(my, n.getRect().bottom);
            }
            Instance instanceChild2 = child(c, "UIPadding");
            if (instanceChild2 != null) {
                Object obj = instanceChild2.get("PaddingRight");
                UDim uDim = obj instanceof UDim ? (UDim) obj : null;
                mx += uDim != null ? uDim.getOffset() * this.d * scale : 0.0f;
                Object obj2 = instanceChild2.get("PaddingBottom");
                UDim uDim2 = obj2 instanceof UDim ? (UDim) obj2 : null;
                my += uDim2 != null ? uDim2.getOffset() * this.d * scale : 0.0f;
            }
            switch (auto) {
                case 1:
                case 3:
                    w2 = mx;
                    break;
            }
            switch (auto) {
                case 2:
                case 3:
                    h2 = my;
                    break;
            }
        }
        float fMax3 = Math.max(w2, 0.0f);
        float fMax4 = Math.max(h2, 0.0f);
        float[] fArr = new float[2];
        fArr[c2] = fMax3;
        fArr[c3] = fMax4;
        return fArr;
    }

    private final float scaleFor(Instance c, float parentScale) {
        Instance instanceChild = child(c, "UIScale");
        return (instanceChild != null ? num(instanceChild, "Scale", 1.0f) : 1.0f) * parentScale;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @SuppressWarnings("unchecked")
    private final List<Instance> sortKids(Instance parent, List<? extends Instance> kids, Instance layout) {
        if (layout == null) {
            return (List<Instance>) (List<?>) kids;
        }
        switch (enumOf(layout, "SortOrder", 2)) {
            case 0:
                return CollectionsKt.sortedWith(kids, new Comparator() { // from class: com.goydevv.studrblx.ui.GuiOverlay$sortKids$$inlined$sortedBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(Object t, Object t2) {
                        return ComparisonsKt.compareValues(((Instance) t).getName(), ((Instance) t2).getName());
                    }
                });
            case 1:
            default:
                return (List<Instance>) (List<?>) kids;
            case 2:
                return CollectionsKt.sortedWith(kids, new Comparator() { // from class: com.goydevv.studrblx.ui.GuiOverlay$sortKids$$inlined$sortedBy$2
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(Object t, Object t2) {
                        Object obj = ((Instance) t).get("LayoutOrder");
                        Number number = obj instanceof Number ? (Number) obj : null;
                        Integer numValueOf = Integer.valueOf(number != null ? number.intValue() : 0);
                        Object obj2 = ((Instance) t2).get("LayoutOrder");
                        Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
                        return ComparisonsKt.compareValues(numValueOf, Integer.valueOf(number2 != null ? number2.intValue() : 0));
                    }
                });
        }
    }

    /* JADX WARN: Code duplicated, block: B:199:0x0545  */
    /* JADX WARN: Code duplicated, block: B:203:0x0551  */
    /* JADX WARN: Code duplicated, block: B:205:0x0598  */
    /* JADX WARN: Code duplicated, block: B:206:0x059b  */
    /* JADX WARN: Code duplicated, block: B:208:0x059e  */
    /* JADX WARN: Code duplicated, block: B:209:0x05a5  */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00e7, code lost:
    
        if (r2.equals("UITableLayout") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00ee, code lost:
    
        if (r2.equals("UIPageLayout") != false) goto L46;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final java.util.List<com.goydevv.studrblx.ui.GuiOverlay.Node> layoutChildren(com.goydevv.studrblx.model.Instance parent, android.graphics.RectF area, float scale) {
        ArrayList<Instance> kids = new ArrayList<>();
        Instance listLayout = null;
        Instance gridLayout = null;
        Instance pageLayout = null;
        Instance tableLayout = null;
        float childScale = scale;
        for (Instance ch : parent.getChildren()) {
            String cn = ch.getClassName();
            if ("UIScale".equals(cn)) {
                float sc = num(ch, "Scale", 1.0f);
                if (sc > 0.01f) {
                    childScale *= sc;
                }
            } else if ("UIListLayout".equals(cn)) {
                listLayout = ch;
            } else if ("UIGridLayout".equals(cn)) {
                gridLayout = ch;
            } else if ("UIPageLayout".equals(cn)) {
                pageLayout = ch;
            } else if ("UITableLayout".equals(cn)) {
                tableLayout = ch;
            } else if (ch.isA("GuiObject") && !Intrinsics.areEqual(ch.get("Visible"), Boolean.FALSE)) {
                kids.add(ch);
            }
        }
        if (kids.isEmpty()) {
            if (listLayout != null) {
                setContentSize(listLayout, 0.0f, 0.0f);
            }
            if (gridLayout != null) {
                setContentSize(gridLayout, 0.0f, 0.0f);
                gridLayout.getAttributes().put("__absCellCount", new Vector2(0.0f, 0.0f));
            }
            return java.util.Collections.emptyList();
        }
        final Instance activeLayout = listLayout != null ? listLayout : (gridLayout != null ? gridLayout : (pageLayout != null ? pageLayout : tableLayout));
        if (activeLayout != null) {
            final int sortOrder = enumOf(activeLayout, "SortOrder", 2);
            java.util.Collections.sort(kids, new Comparator<Instance>() {
                @Override
                public int compare(Instance a, Instance b) {
                    if (sortOrder == 0) {
                        return a.getName().compareToIgnoreCase(b.getName());
                    }
                    int la = (int) num(a, "LayoutOrder", 0.0f);
                    int lb = (int) num(b, "LayoutOrder", 0.0f);
                    return Integer.compare(la, lb);
                }
            });
        } else {
            java.util.Collections.sort(kids, new Comparator<Instance>() {
                @Override
                public int compare(Instance a, Instance b) {
                    int za = (int) num(a, "ZIndex", 1.0f);
                    int zb = (int) num(b, "ZIndex", 1.0f);
                    return Integer.compare(za, zb);
                }
            });
        }

        ArrayList<Node> out = new ArrayList<>(kids.size());
        float aw = area.width();
        float ah = area.height();

        if (listLayout != null) {
            boolean horiz = enumOf(listLayout, "FillDirection", 1) == 0;
            int hAlign = enumOf(listLayout, "HorizontalAlignment", 1);
            int vAlign = enumOf(listLayout, "VerticalAlignment", 1);
            Object padObj = listLayout.get("Padding");
            float padPx = 0.0f;
            if (padObj instanceof UDim) {
                UDim pu = (UDim) padObj;
                padPx = (pu.getScale() * (horiz ? aw : ah)) + (pu.getOffset() * this.d * childScale);
            }
            float[][] sizes = new float[kids.size()][];
            float[] flexGrow = new float[kids.size()];
            float totalGrow = 0.0f;
            float totalPrimary = 0.0f;
            for (int i = 0; i < kids.size(); i++) {
                Instance kid = kids.get(i);
                sizes[i] = sizeOf(kid, area, childScale);
                for (Instance fc : kid.getChildren()) {
                    if ("UIFlexItem".equals(fc.getClassName())) {
                        int fm = enumOf(fc, "FlexMode", 0);
                        float gr = fm == 1 ? 1.0f : (fm == 2 ? 1.0f : num(fc, "GrowRatio", 0.0f));
                        if (gr > 0.0f) {
                            flexGrow[i] = gr;
                            totalGrow += gr;
                        }
                        break;
                    }
                }
                totalPrimary += horiz ? sizes[i][0] : sizes[i][1];
                if (i > 0) totalPrimary += padPx;
            }
            float availPrimary = horiz ? aw : ah;
            if (totalGrow > 0.0f && availPrimary > totalPrimary) {
                float extra = availPrimary - totalPrimary;
                for (int i = 0; i < kids.size(); i++) {
                    if (flexGrow[i] > 0.0f) {
                        float add = extra * (flexGrow[i] / totalGrow);
                        if (horiz) sizes[i][0] += add;
                        else sizes[i][1] += add;
                    }
                }
                totalPrimary = availPrimary;
            }
            float cursor;
            if (horiz) {
                if (hAlign == 0) cursor = area.left + ((aw - totalPrimary) * 0.5f);
                else if (hAlign == 2) cursor = area.right - totalPrimary;
                else cursor = area.left;
            } else {
                if (vAlign == 0) cursor = area.top + ((ah - totalPrimary) * 0.5f);
                else if (vAlign == 2) cursor = area.bottom - totalPrimary;
                else cursor = area.top;
            }
            boolean wraps = Intrinsics.areEqual(listLayout.get("Wraps"), Boolean.TRUE);
            float lineOffset = 0.0f;
            float lineMaxCross = 0.0f;
            for (int i = 0; i < kids.size(); i++) {
                Instance c = kids.get(i);
                float cw = sizes[i][0];
                float chh = sizes[i][1];
                float x;
                float y;
                if (horiz) {
                    if (wraps && i > 0 && (cursor + cw) > area.right + 0.5f) {
                        cursor = area.left;
                        lineOffset += lineMaxCross + padPx;
                        lineMaxCross = 0.0f;
                    }
                    x = cursor;
                    if (wraps) {
                        y = area.top + lineOffset;
                        if (chh > lineMaxCross) lineMaxCross = chh;
                    } else if (vAlign == 0) y = area.top + ((ah - chh) * 0.5f);
                    else if (vAlign == 2) y = area.bottom - chh;
                    else y = area.top;
                    cursor += cw + padPx;
                } else {
                    if (wraps && i > 0 && (cursor + chh) > area.bottom + 0.5f) {
                        cursor = area.top;
                        lineOffset += lineMaxCross + padPx;
                        lineMaxCross = 0.0f;
                    }
                    if (wraps) {
                        x = area.left + lineOffset;
                        if (cw > lineMaxCross) lineMaxCross = cw;
                    } else if (hAlign == 0) x = area.left + ((aw - cw) * 0.5f);
                    else if (hAlign == 2) x = area.right - cw;
                    else x = area.left;
                    y = cursor;
                    cursor += chh + padPx;
                }
                out.add(new Node(c, new RectF(x, y, x + cw, y + chh), childScale, area, true));
            }
            if (!out.isEmpty()) {
                float minX = Float.MAX_VALUE;
                float minY = Float.MAX_VALUE;
                float maxX = -Float.MAX_VALUE;
                float maxY = -Float.MAX_VALUE;
                for (Node nd : out) {
                    RectF nr = nd.getRect();
                    if (nr.left < minX) minX = nr.left;
                    if (nr.top < minY) minY = nr.top;
                    if (nr.right > maxX) maxX = nr.right;
                    if (nr.bottom > maxY) maxY = nr.bottom;
                }
                setContentSize(listLayout, Math.max(0.0f, maxX - minX), Math.max(0.0f, maxY - minY));
            } else {
                setContentSize(listLayout, 0.0f, 0.0f);
            }
            return out;
        }

        if (gridLayout != null) {
            UDim2 cs = udim(gridLayout.get("CellSize"));
            UDim2 cp = udim(gridLayout.get("CellPadding"));
            float cw = Math.max(1.0f, (cs.getXs() * aw) + (cs.getXo() * this.d * childScale));
            float chh = Math.max(1.0f, (cs.getYs() * ah) + (cs.getYo() * this.d * childScale));
            float px = (cp.getXs() * aw) + (cp.getXo() * this.d * childScale);
            float py = (cp.getYs() * ah) + (cp.getYo() * this.d * childScale);
            boolean horiz = enumOf(gridLayout, "FillDirection", 0) == 0;
            int maxCells = (int) num(gridLayout, "FillDirectionMaxCells", 0.0f);
            int hAlign = enumOf(gridLayout, "HorizontalAlignment", 1);
            int vAlign = enumOf(gridLayout, "VerticalAlignment", 1);
            int startCorner = enumOf(gridLayout, "StartCorner", 0);
            int nKids = kids.size();
            int cols;
            int rows;
            if (horiz) {
                int fitCols = Math.max(1, (int) Math.floor((aw + px) / Math.max(cw + px, 1.0f)));
                if (maxCells > 0) fitCols = Math.min(fitCols, maxCells);
                cols = Math.max(1, fitCols);
                rows = (nKids + cols - 1) / cols;
            } else {
                int fitRows = Math.max(1, (int) Math.floor((ah + py) / Math.max(chh + py, 1.0f)));
                if (maxCells > 0) fitRows = Math.min(fitRows, maxCells);
                rows = Math.max(1, fitRows);
                cols = (nKids + rows - 1) / rows;
            }
            int usedCols = horiz ? Math.min(nKids, cols) : cols;
            int usedRows = horiz ? rows : Math.min(nKids, rows);
            float contentW = usedCols > 0 ? (usedCols * cw) + (Math.max(0, usedCols - 1) * px) : 0.0f;
            float contentH = usedRows > 0 ? (usedRows * chh) + (Math.max(0, usedRows - 1) * py) : 0.0f;
            float originX;
            if (hAlign == 0) originX = area.left + ((aw - contentW) * 0.5f);
            else if (hAlign == 2) originX = area.right - contentW;
            else originX = area.left;
            float originY;
            if (vAlign == 0) originY = area.top + ((ah - contentH) * 0.5f);
            else if (vAlign == 2) originY = area.bottom - contentH;
            else originY = area.top;
            boolean flipX = (startCorner == 1 || startCorner == 3);
            boolean flipY = (startCorner == 2 || startCorner == 3);
            for (int i = 0; i < nKids; i++) {
                int col = horiz ? (i % cols) : (i / rows);
                int row = horiz ? (i / cols) : (i % rows);
                if (flipX) col = (usedCols - 1) - col;
                if (flipY) row = (usedRows - 1) - row;
                float x = originX + (col * (cw + px));
                float y = originY + (row * (chh + py));
                out.add(new Node(kids.get(i), new RectF(x, y, x + cw, y + chh), childScale, area, true));
            }
            setContentSize(gridLayout, contentW, contentH);
            float au = this.absUnit > 0.0f ? this.absUnit : (this.d > 0.0f ? this.d : 1.0f);
            gridLayout.getAttributes().put("__absCellSize", new Vector2(cw / au, chh / au));
            gridLayout.getAttributes().put("__absCellCount", new Vector2((float) usedCols, (float) usedRows));
            return out;
        }

        if (pageLayout != null) {
            boolean horiz = enumOf(pageLayout, "FillDirection", 0) == 0;
            Object padObj = pageLayout.get("Padding");
            float padPx = 0.0f;
            if (padObj instanceof UDim) {
                UDim pu = (UDim) padObj;
                padPx = (pu.getScale() * (horiz ? aw : ah)) + (pu.getOffset() * this.d * childScale);
            }
            Object curPageObj = pageLayout.get("CurrentPage");
            int curIdx = 0;
            if (curPageObj instanceof Instance) {
                int found = kids.indexOf(curPageObj);
                if (found >= 0) curIdx = found;
            }
            for (int i = 0; i < kids.size(); i++) {
                int rel = i - curIdx;
                float x = horiz ? (area.left + rel * (aw + padPx)) : area.left;
                float y = horiz ? area.top : (area.top + rel * (ah + padPx));
                out.add(new Node(kids.get(i), new RectF(x, y, x + aw, y + ah), childScale, area, true));
            }
            return out;
        }

        if (tableLayout != null) {
            boolean horiz = enumOf(tableLayout, "FillDirection", 1) == 0;
            UDim2 pad = udim(tableLayout.get("Padding"));
            float px = (pad.getXs() * aw) + (pad.getXo() * this.d * childScale);
            float py = (pad.getYs() * ah) + (pad.getYo() * this.d * childScale);
            int nKids = kids.size();
            float cellW = horiz ? Math.max(1.0f, (aw - px * Math.max(0, nKids - 1)) / Math.max(1, nKids)) : aw;
            float cellH = horiz ? ah : Math.max(1.0f, (ah - py * Math.max(0, nKids - 1)) / Math.max(1, nKids));
            for (int i = 0; i < nKids; i++) {
                float x = horiz ? (area.left + i * (cellW + px)) : area.left;
                float y = horiz ? area.top : (area.top + i * (cellH + py));
                out.add(new Node(kids.get(i), new RectF(x, y, x + cellW, y + cellH), childScale, area, true));
            }
            return out;
        }

        for (Instance c : kids) {
            float[] sz = sizeOf(c, area, childScale);
            float cw = sz[0];
            float chh = sz[1];
            UDim2 pos = udim(c.get("Position"));
            Object apObj = c.get("AnchorPoint");
            Vector2 ap = apObj instanceof Vector2 ? (Vector2) apObj : new Vector2(0.0f, 0.0f);
            float px = area.left + (pos.getXs() * aw) + (pos.getXo() * this.d * childScale) - (ap.getX() * cw);
            float py = area.top + (pos.getYs() * ah) + (pos.getYo() * this.d * childScale) - (ap.getY() * chh);
            out.add(new Node(c, new RectF(px, py, px + cw, py + chh), childScale, area, false));
        }
        return out;
    }

    private final void drawChildren(Canvas canvas, Instance parent, RectF area, float scale, RectF clip) {
        RectF content = padded(parent, area, scale);
        for (Node n : layoutChildren(parent, content, scale)) {
            drawNode(canvas, n, clip);
        }
    }

    private final void drawNode(Canvas canvas, Node n, RectF clip) {
        Canvas canvas2;
        RectF childClip;
        Instance c = n.getInst();
        RectF r = n.getRect();
        setAbs(c, r);
        if ((r.width() <= 0.0f || r.height() <= 0.0f) && !Intrinsics.areEqual(c.getClassName(), "ScrollingFrame") && Intrinsics.areEqual(c.get("ClipsDescendants"), (Object) true)) {
            return;
        }
        float rot = num(c, "Rotation", 0.0f);
        float parentAbsRot = 0.0f;
        Instance parInst = c.getParent();
        if (parInst != null) {
            Object prObj = parInst.getAttributes().get("__absRotation");
            if (prObj instanceof Number) {
                parentAbsRot = ((Number) prObj).floatValue();
            }
        }
        c.getAttributes().put("__absRotation", Float.valueOf(parentAbsRot + rot));
        float groupT = Intrinsics.areEqual(c.getClassName(), "CanvasGroup") ? num(c, "GroupTransparency", 0.0f) : 0.0f;
        if (groupT >= 0.999f) {
            return;
        }
        int saveCount = canvas.save();
        if (!(rot == 0.0f)) {
            canvas.rotate(rot, r.centerX(), r.centerY());
        }
        if (Intrinsics.areEqual(c.getClassName(), "CanvasGroup")) {
            Object gcObj = c.get("GroupColor3");
            Color3 gc = gcObj instanceof Color3 ? (Color3) gcObj : null;
            boolean hasTint = gc != null && (gc.getR() < 0.995f || gc.getG() < 0.995f || gc.getB() < 0.995f);
            if (groupT > 0.001f || hasTint) {
                android.graphics.Paint lp = new android.graphics.Paint();
                lp.setAlpha((int) ((1.0f - groupT) * 255.0f));
                if (hasTint) {
                    lp.setColorFilter(new android.graphics.PorterDuffColorFilter(gc.toArgb(), android.graphics.PorterDuff.Mode.MULTIPLY));
                }
                canvas.saveLayer(r, lp);
            }
            canvas.clipRect(r);
        }
        drawObject(canvas, c, r, n.getScale());
        RectF visible = new RectF(r);
        RectF rectF = new RectF(r);
        if (!rectF.intersect(clip)) {
            rectF.setEmpty();
        }
        if (n.getArea() != null && !this.studio.isPlaying()) {
            this.newBoxes.put(c, new EditBox(new RectF(r), new RectF(n.getArea()), n.getScale(), n.getInLayout()));
        }
        if (!rectF.isEmpty()) {
            this.newPicks.add(TuplesKt.to(c, new RectF(r)));
            if (isInteractable(c)) {
                if (Intrinsics.areEqual(c.getClassName(), "TextBox")) {
                    this.newHits.add(new Hit(c, rectF, this.rt, 1));
                } else if (c.isA("GuiButton")) {
                    this.newHits.add(new Hit(c, rectF, this.rt, 0));
                } else if (Intrinsics.areEqual(c.get("Active"), (Object) true)) {
                    this.newHits.add(new Hit(c, rectF, this.rt, 2));
                }
            }
        }
        if (Intrinsics.areEqual(c.getClassName(), "ScrollingFrame")) {
            canvas2 = canvas;
            drawScrolling(canvas2, c, r, n.getScale(), clip);
        } else {
            canvas2 = canvas;
            boolean clips = Intrinsics.areEqual(c.get("ClipsDescendants"), (Object) true);
            if (clips) {
                childClip = new RectF(visible);
                if (!childClip.intersect(clip)) {
                    childClip.setEmpty();
                }
            } else {
                childClip = clip;
            }
            if (clips) {
                canvas2.clipRect(r);
            }
            drawChildren(canvas2, c, r, n.getScale(), childClip);
        }
        canvas2.restoreToCount(saveCount);
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0118  */
    /* JADX WARN: Code duplicated, block: B:39:0x012d A[LOOP:1: B:37:0x0127->B:39:0x012d, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:53:0x0166  */
    /* JADX WARN: Code duplicated, block: B:54:0x0175  */
    /* JADX WARN: Code duplicated, block: B:62:0x01b1  */
    /* JADX WARN: Code duplicated, block: B:65:0x01ba  */
    /* JADX WARN: Code duplicated, block: B:73:0x01fb  */
    /* JADX WARN: Code duplicated, block: B:77:0x0212 A[LOOP:2: B:75:0x020c->B:77:0x0212, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:80:0x0238  */
    private final boolean isInteractable(Instance c) {
        Instance cur = c;
        while (cur != null) {
            if (cur.isA("GuiObject") && Boolean.FALSE.equals(cur.get("Interactable"))) {
                return false;
            }
            cur = cur.getParent();
        }
        return true;
    }

    private final void drawScrolling(Canvas canvas, Instance c, RectF r, float scale, RectF clip) {
        float unit = this.d * scale;
        Object uDim2 = c.get("CanvasSize");
        if (uDim2 == null) {
            uDim2 = new UDim2(0.0f, 0, 2.0f, 0);
        }
        UDim2 cs = udim(uDim2);
        Ref.FloatRef cw = new Ref.FloatRef();
        cw.element = Math.max((cs.getXs() * r.width()) + (cs.getXo() * unit), r.width());
        Ref.FloatRef ch = new Ref.FloatRef();
        ch.element = Math.max((cs.getYs() * r.height()) + (cs.getYo() * unit), r.height());
        int auto = enumOf(c, "AutomaticCanvasSize", 0);
        Object obj = c.get("CanvasPosition");
        Vector2 pos1 = obj instanceof Vector2 ? (Vector2) obj : new Vector2(0.0f, 0.0f);
        RectF area = drawScrolling$canvasRect(r, pos1, unit, cw, ch);
        List<Node> nodes = layoutChildren(c, padded(c, area, scale), scale);
        if (auto != 0 && !nodes.isEmpty()) {
            float maxBottom = nodes.get(0).getRect().bottom;
            float maxRight = nodes.get(0).getRect().right;
            for (int i = 1; i < nodes.size(); i++) {
                RectF nr = nodes.get(i).getRect();
                if (nr.bottom > maxBottom) maxBottom = nr.bottom;
                if (nr.right > maxRight) maxRight = nr.right;
            }
            float bottom = maxBottom - area.top;
            float right = maxRight - area.left;
            Instance padInst = child(c, "UIPadding");
            if (padInst != null) {
                Object pbObj = padInst.get("PaddingBottom");
                if (pbObj instanceof UDim) {
                    UDim pb = (UDim) pbObj;
                    bottom += (pb.getScale() * area.height()) + (pb.getOffset() * unit);
                }
                Object prObj = padInst.get("PaddingRight");
                if (prObj instanceof UDim) {
                    UDim pr = (UDim) prObj;
                    right += (pr.getScale() * area.width()) + (pr.getOffset() * unit);
                }
            }
            boolean changed = false;
            if ((auto == 2 || auto == 3) && bottom > ch.element) {
                ch.element = bottom;
                changed = true;
            }
            if ((auto == 1 || auto == 3) && right > cw.element) {
                cw.element = right;
                changed = true;
            }
            if (changed) {
                nodes = layoutChildren(c, padded(c, drawScrolling$canvasRect(r, pos1, unit, cw, ch), scale), scale);
            }
        }
        if (this.absUnit <= 0.0f) {
            useGameUnits();
        }
        float au = this.absUnit;
        c.getAttributes().put("__absCanvasSize", new Vector2(cw.element / au, ch.element / au));
        c.getAttributes().put("__absWindowSize", new Vector2(r.width() / au, r.height() / au));
        float maxX = Math.max(0.0f, cw.element - r.width());
        float maxY = Math.max(0.0f, ch.element - r.height());
        RectF vis = new RectF(r);
        if (!vis.intersect(clip)) {
            vis.setEmpty();
        }
        if (!vis.isEmpty() && !Intrinsics.areEqual(c.get("ScrollingEnabled"), (Object) false) && (maxX > 0.0f || maxY > 0.0f)) {
            this.newScrolls.add(new Scroll(c, vis, maxX / unit, maxY / unit, unit));
        }
        canvas.save();
        canvas.clipRect(r);
        for (Node n : nodes) {
            drawNode(canvas, n, vis);
        }
        canvas.restore();
        float th = num(c, "ScrollBarThickness", 12.0f) * unit * 0.5f;
        float bt = num(c, "ScrollBarImageTransparency", 0.0f);
        if (maxY > 0.0f && th > 0.0f && bt < 1.0f) {
            float barH = r.height() * (r.height() / ch.element);
            float y = r.top + ((r.height() - barH) * RangesKt.coerceIn((pos1.getY() * unit) / maxY, 0.0f, 1.0f));
            this.fill.setShader(null);
            this.fill.setColor(alpha(colorOf(c.get("ScrollBarImageColor3"), ViewCompat.MEASURED_STATE_MASK), (0.7f * bt) + 0.3f));
            canvas.drawRoundRect((r.right - th) - (this.d * 2.0f), y, r.right - (this.d * 2.0f), y + barH, th / 2.0f, th / 2.0f, this.fill);
        }
    }

    private static final RectF drawScrolling$canvasRect(RectF $r, Vector2 pos0, float unit, Ref.FloatRef cw, Ref.FloatRef ch) {
        return new RectF($r.left - (pos0.getX() * unit), $r.top - (pos0.getY() * unit), ($r.left - (pos0.getX() * unit)) + cw.element, ($r.top - (pos0.getY() * unit)) + ch.element);
    }

    private final int colorOf(Object v, int i) {
        Color3 color3 = v instanceof Color3 ? (Color3) v : null;
        return color3 != null ? color3.toArgb() : i;
    }

    private final int alpha(int c, float transparency) {
        int a = (int) ((1.0f - RangesKt.coerceIn(transparency, 0.0f, 1.0f)) * ((c >>> 24) & 255));
        return (16777215 & c) | (a << 24);
    }

    private final float cornerOf(Instance c, RectF r, float scale) {
        Instance instanceChild = child(c, "UICorner");
        if (instanceChild == null) {
            return 0.0f;
        }
        Object obj = instanceChild.get("CornerRadius");
        UDim uDim = obj instanceof UDim ? (UDim) obj : null;
        return RangesKt.coerceIn(uDim != null ? (uDim.getScale() * Math.min(r.width(), r.height())) + (uDim.getOffset() * this.d * scale) : this.d * 8.0f * scale, 0.0f, Math.min(r.width(), r.height()) / 2.0f);
    }

    private final Shader gradientShader(Instance g, RectF r, float baseTransparency) {
        List listEmptyList;
        List listEmptyList2;
        Iterable keys;
        Iterable keys2;
        Object obj = g.get("Color");
        ColorSequence cseq = obj instanceof ColorSequence ? (ColorSequence) obj : null;
        Object obj2 = g.get("Transparency");
        NumberSequence tseq = obj2 instanceof NumberSequence ? (NumberSequence) obj2 : null;
        float f = 1.0f;
        List listListOf = CollectionsKt.listOf(new Float[]{Float.valueOf(0.0f), Float.valueOf(1.0f)});
        if (cseq == null || (keys2 = cseq.getKeys()) == null) {
            listEmptyList = CollectionsKt.emptyList();
        } else {
            Iterable iterable = keys2;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(Float.valueOf(((ColorKey) it.next()).getTime()));
            }
            listEmptyList = (List) arrayList;
        }
        List listPlus = CollectionsKt.plus((Collection) listListOf, (Iterable) listEmptyList);
        if (tseq == null || (keys = tseq.getKeys()) == null) {
            listEmptyList2 = CollectionsKt.emptyList();
        } else {
            Iterable iterable2 = keys;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
            Iterator it2 = iterable2.iterator();
            while (it2.hasNext()) {
                arrayList2.add(Float.valueOf(((NumberKey) it2.next()).getTime()));
            }
            listEmptyList2 = (List) arrayList2;
        }
        Iterable times = CollectionsKt.sorted(CollectionsKt.distinct(CollectionsKt.plus((Collection) listPlus, listEmptyList2)));
        Iterable iterable3 = times;
        Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable3, 10));
        Iterator it3 = iterable3.iterator();
        while (it3.hasNext()) {
            float fFloatValue = ((Number) it3.next()).floatValue();
            float f2 = f;
            arrayList3.add(Integer.valueOf(alpha(gradientShader$colorAt(cseq, fFloatValue).toArgb(), f2 - ((f2 - baseTransparency) * (f2 - gradientShader$transAt(tseq, fFloatValue))))));
            f = f2;
        }
        int[] colors = CollectionsKt.toIntArray((List) arrayList3);
        double rot = Math.toRadians(num(g, "Rotation", 0.0f));
        float dx = (float) Math.cos(rot);
        float dy = (float) Math.sin(rot);
        Object obj3 = g.get("Offset");
        Vector2 off = obj3 instanceof Vector2 ? (Vector2) obj3 : null;
        if (off == null) {
            off = new Vector2(0.0f, 0.0f);
        }
        float half = (Math.abs(r.width() * dx) + Math.abs(r.height() * dy)) / 2.0f;
        float cx = r.centerX() + (off.getX() * r.width());
        float cy = r.centerY() + (off.getY() * r.height());
        return new LinearGradient(cx - (dx * half), cy - (dy * half), cx + (dx * half), cy + (dy * half), colors, CollectionsKt.toFloatArray((Collection) times), Shader.TileMode.CLAMP);
    }

    private static final Color3 gradientShader$colorAt(ColorSequence cseq, float t) {
        List<ColorKey> keys;
        if (cseq == null || (keys = cseq.getKeys()) == null) {
            return new Color3(1.0f, 1.0f, 1.0f);
        }
        if (keys.isEmpty()) {
            return new Color3(1.0f, 1.0f, 1.0f);
        }
        int size = keys.size() - 1;
        for (int i = 0; i < size; i++) {
            if (t <= keys.get(i + 1).getTime()) {
                ColorKey a = keys.get(i);
                ColorKey b = keys.get(i + 1);
                float f = b.getTime() > a.getTime() ? (t - a.getTime()) / (b.getTime() - a.getTime()) : 0.0f;
                return a.getColor().lerp(b.getColor(), RangesKt.coerceIn(f, 0.0f, 1.0f));
            }
        }
        return ((ColorKey) CollectionsKt.last((List) keys)).getColor();
    }

    private static final float gradientShader$transAt(NumberSequence tseq, float t) {
        List<NumberKey> keys;
        if (tseq == null || (keys = tseq.getKeys()) == null || keys.isEmpty()) {
            return 0.0f;
        }
        int size = keys.size() - 1;
        for (int i = 0; i < size; i++) {
            if (t <= keys.get(i + 1).getTime()) {
                NumberKey a = keys.get(i);
                NumberKey b = keys.get(i + 1);
                float f = b.getTime() > a.getTime() ? (t - a.getTime()) / (b.getTime() - a.getTime()) : 0.0f;
                return a.getValue() + ((b.getValue() - a.getValue()) * RangesKt.coerceIn(f, 0.0f, 1.0f));
            }
        }
        return ((NumberKey) CollectionsKt.last((List) keys)).getValue();
    }

    /* JADX WARN: Code duplicated, block: B:67:0x019c  */
    private final void drawObject(Canvas canvas, Instance c, RectF r, float scale) {
        boolean z;
        float f;
        Object next;
        boolean z2;
        Object next2;
        float f2;
        Instance instance;
        float f3;
        float f4;
        float f5;
        float f6 = 0.0f;
        float fNum = num(c, "BackgroundTransparency", 0.0f);
        float fCornerOf = cornerOf(c, r, scale);
        Iterator it = c.getChildren().iterator();
        while (true) {
            z = false;
            if (it.hasNext()) {
                next = it.next();
                Instance instance2 = (Instance) next;
                f = f6;
                if (Intrinsics.areEqual(instance2.getClassName(), "UIGradient") && !Intrinsics.areEqual(instance2.get("Enabled"), (Object) false)) {
                    break;
                } else {
                    f6 = f;
                }
            } else {
                f = f6;
                next = null;
                break;
            }
        }
        Instance instance3 = (Instance) next;
        ArrayList<Instance> children = c.getChildren();
        Iterator it2 = children.iterator();
        while (true) {
            if (!it2.hasNext()) {
                z2 = z;
                next2 = null;
                break;
            }
            next2 = it2.next();
            Instance instance4 = (Instance) next2;
            z2 = z;
            ArrayList<Instance> arrayList = children;
            if ((!Intrinsics.areEqual(instance4.getClassName(), "UIStroke") || Intrinsics.areEqual(instance4.get("Enabled"), Boolean.valueOf(z2))) ? z2 : true) {
                break;
            }
            z = z2;
            children = arrayList;
        }
        Instance instance5 = (Instance) next2;
        boolean z3 = instance5 != null && isText(c) && enumOf(instance5, "ApplyStrokeMode", z2 ? 1 : 0) == 0;
        if (fNum < 1.0f) {
            this.fill.setColor(alpha(colorOf(c.get("BackgroundColor3"), -6053211), fNum));
            this.fill.setShader(instance3 != null ? gradientShader(instance3, r, fNum) : null);
            if (fCornerOf > f) {
                canvas.drawRoundRect(r, fCornerOf, fCornerOf, this.fill);
            } else {
                canvas.drawRect(r, this.fill);
            }
            this.fill.setShader(null);
            if (this.pressed == c && !Intrinsics.areEqual(c.get("AutoButtonColor"), (Object) false)) {
                this.fill.setColor(1073741824);
                if (fCornerOf > f) {
                    canvas.drawRoundRect(r, fCornerOf, fCornerOf, this.fill);
                } else {
                    canvas.drawRect(r, this.fill);
                }
            }
            float f7 = f;
            float fNum2 = num(c, "BorderSizePixel", f7) * scale;
            if (fNum2 > f7) {
                if ((fCornerOf == f7) && instance5 == null) {
                    this.stroke.setShader(null);
                    this.stroke.setColor(alpha(colorOf(c.get("BorderColor3"), -14996939), fNum));
                    this.stroke.setStrokeWidth(this.d * fNum2);
                    switch (enumOf(c, "BorderMode", 0)) {
                        case 0:
                            f4 = this.d * fNum2;
                            f5 = f4 / 2.0f;
                            break;
                        case 1:
                            f5 = 0.0f;
                            break;
                        default:
                            f4 = (-fNum2) * this.d;
                            f5 = f4 / 2.0f;
                            break;
                    }
                    float f8 = f5;
                    canvas.drawRect(r.left - f8, r.top - f8, r.right + f8, r.bottom + f8, this.stroke);
                }
            }
        }
        if (instance5 != null && !z3) {
            float fNum3 = num(instance5, "Thickness", 1.0f) * this.d * scale;
            this.stroke.setShader(null);
            this.stroke.setColor(alpha(colorOf(instance5.get("Color"), ViewCompat.MEASURED_STATE_MASK), num(instance5, "Transparency", 0.0f)));
            this.stroke.setStrokeWidth(fNum3);
            this.stroke.setStrokeJoin(Paint.Join.ROUND);
            f2 = 1.0f;
            RectF rectF = new RectF(r.left - (fNum3 / 2.0f), r.top - (fNum3 / 2.0f), r.right + (fNum3 / 2.0f), r.bottom + (fNum3 / 2.0f));
            if (fCornerOf > 0.0f) {
                canvas.drawRoundRect(rectF, (fNum3 / 2.0f) + fCornerOf, (fNum3 / 2.0f) + fCornerOf, this.stroke);
            } else {
                canvas.drawRect(rectF, this.stroke);
            }
        } else {
            f2 = 1.0f;
        }
        if (Intrinsics.areEqual(c.getClassName(), "ImageLabel") || Intrinsics.areEqual(c.getClassName(), "ImageButton")) {
            instance = c;
            drawImage(canvas, instance, r, fCornerOf, scale);
            f3 = scale;
        } else {
            instance = c;
            f3 = scale;
        }
        if (isText(instance)) {
            drawText(canvas, instance, padded(instance, r, f3), f3, z3 ? instance5 : null, fNum >= f2 ? instance3 : null);
        }
    }

    private final void drawImage(Canvas canvas, Instance c, RectF r, float corner, float scale) {
        Object obj;
        String content;
        Bitmap bmp;
        float it = num(c, "ImageTransparency", 0.0f);
        if (it >= 1.0f || (obj = c.get("Image")) == null || (content = obj.toString()) == null || (bmp = ImageLoader.INSTANCE.get(content)) == null) {
            return;
        }
        this.imgPaint.setAlpha((int) ((1.0f - it) * 255.0f));
        int tint = colorOf(c.get("ImageColor3"), -1);
        this.imgPaint.setColorFilter((tint & ViewCompat.MEASURED_SIZE_MASK) != 16777215 ? new LightingColorFilter(16777215 & tint, 0) : null);
        Rect src = sourceRect(c, bmp);
        canvas.save();
        if (corner > 0.0f) {
            Path path = new Path();
            path.addRoundRect(r, corner, corner, Path.Direction.CW);
            canvas.clipPath(path);
        }
        switch (enumOf(c, "ScaleType", 0)) {
            case 1:
                drawSliced(canvas, c, bmp, src, r, scale);
                break;
            case 2:
                Object uDim2 = c.get("TileSize");
                if (uDim2 == null) {
                    uDim2 = new UDim2(1.0f, 0, 1.0f, 0);
                }
                UDim2 ts = udim(uDim2);
                float tw = RangesKt.coerceAtLeast((ts.getXs() * r.width()) + (ts.getXo() * this.d * scale), 2.0f);
                float th = RangesKt.coerceAtLeast((ts.getYs() * r.height()) + (ts.getYo() * this.d * scale), 2.0f);
                Bitmap sub = Bitmap.createBitmap(bmp, src.left, src.top, RangesKt.coerceAtLeast(src.width(), 1), RangesKt.coerceAtLeast(src.height(), 1));
                Intrinsics.checkNotNullExpressionValue(sub, "createBitmap(...)");
                BitmapShader sh = new BitmapShader(sub, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                Matrix matrix = new Matrix();
                matrix.setScale(tw / sub.getWidth(), th / sub.getHeight());
                matrix.postTranslate(r.left, r.top);
                sh.setLocalMatrix(matrix);
                this.imgPaint.setShader(sh);
                canvas.drawRect(r, this.imgPaint);
                this.imgPaint.setShader(null);
                break;
            case 3:
                float k = Math.min(r.width() / src.width(), r.height() / src.height());
                float w = src.width() * k;
                float h = src.height() * k;
                canvas.drawBitmap(bmp, src, new RectF(r.centerX() - (w / 2.0f), r.centerY() - (h / 2.0f), r.centerX() + (w / 2.0f), r.centerY() + (h / 2.0f)), this.imgPaint);
                break;
            case 4:
                float k2 = Math.max(r.width() / src.width(), r.height() / src.height());
                float sw = r.width() / k2;
                float sh2 = r.height() / k2;
                Rect s2 = new Rect((int) (src.exactCenterX() - (sw / 2.0f)), (int) (src.exactCenterY() - (sh2 / 2.0f)), (int) (src.exactCenterX() + (sw / 2.0f)), (int) (src.exactCenterY() + (sh2 / 2.0f)));
                canvas.drawBitmap(bmp, s2, r, this.imgPaint);
                break;
            default:
                canvas.drawBitmap(bmp, src, r, this.imgPaint);
                break;
        }
        canvas.restore();
    }

    private final Rect sourceRect(Instance c, Bitmap bmp) {
        Object obj = c.get("ImageRectSize");
        Vector2 size = obj instanceof Vector2 ? (Vector2) obj : null;
        Object obj2 = c.get("ImageRectOffset");
        Vector2 off = obj2 instanceof Vector2 ? (Vector2) obj2 : null;
        if (off == null) {
            off = new Vector2(0.0f, 0.0f);
        }
        if (size == null || size.getX() <= 0.0f || size.getY() <= 0.0f) {
            return new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        }
        float need = Math.max(off.getX() + size.getX(), off.getY() + size.getY());
        float sheet = 64.0f;
        while (sheet < need) {
            sheet *= 2.0f;
        }
        float k = bmp.getWidth() / sheet;
        return new Rect((int) (off.getX() * k), (int) (off.getY() * k), RangesKt.coerceAtMost((int) ((off.getX() + size.getX()) * k), bmp.getWidth()), RangesKt.coerceAtMost((int) ((off.getY() + size.getY()) * k), bmp.getHeight()));
    }

    private final void drawSliced(Canvas canvas, Instance c, Bitmap bmp, Rect src, RectF r, float scale) {
        int j;
        Object obj = c.get("SliceCenter");
        com.goydevv.studrblx.model.Rect sc = obj instanceof com.goydevv.studrblx.model.Rect ? (com.goydevv.studrblx.model.Rect) obj : null;
        if (sc == null || sc.getMaxX() <= sc.getMinX() || sc.getMaxY() <= sc.getMinY()) {
            canvas.drawBitmap(bmp, src, r, this.imgPaint);
            return;
        }
        float ow = RangesKt.coerceAtLeast(sc.getMinX() + sc.getMaxX(), 1.0f);
        float oh = RangesKt.coerceAtLeast(sc.getMinY() + sc.getMaxY(), 1.0f);
        float kx = src.width() / ow;
        float ky = src.height() / oh;
        float ss = num(c, "SliceScale", 1.0f) * this.d * scale;
        float sl = sc.getMinX() * kx;
        float st = sc.getMinY() * ky;
        float sr = (ow - sc.getMaxX()) * kx;
        float sb = (oh - sc.getMaxY()) * ky;
        float dl = sc.getMinX() * ss;
        float dt = sc.getMinY() * ss;
        float dr = (ow - sc.getMaxX()) * ss;
        float db = (oh - sc.getMaxY()) * ss;
        if (dl + dr > r.width()) {
            float f = r.width() / (dl + dr);
            dl *= f;
            dr *= f;
        }
        if (dt + db > r.height()) {
            float f2 = r.height() / (dt + db);
            dt *= f2;
            db *= f2;
        }
        float[] xs = {src.left, src.left + sl, src.right - sr, src.right};
        int i = 3;
        float[] xs2 = {src.top, src.top + st, src.bottom - sb, src.bottom};
        float[] xd = {r.left, r.left + dl, r.right - dr, r.right};
        float[] yd = {r.top, r.top + dt, r.bottom - db, r.bottom};
        int j2 = 0;
        while (true) {
            float[] xd2 = xd;
            int i2 = i;
            if (j2 >= i2) {
                return;
            }
            int i3 = j2;
            int j3 = 0;
            while (j3 < i2) {
                if (xs[i3 + 1] <= xs[i3] || xs2[j3 + 1] <= xs2[j3] || xd2[i3 + 1] <= xd2[i3]) {
                    j = j3;
                } else if (yd[j3 + 1] <= yd[j3]) {
                    j = j3;
                } else {
                    j = j3;
                    canvas.drawBitmap(bmp, new Rect((int) xs[i3], (int) xs2[j], (int) xs[i3 + 1], (int) xs2[j + 1]), new RectF(xd2[i3], yd[j], xd2[i3 + 1], yd[j + 1]), this.imgPaint);
                }
                j3 = j + 1;
                i2 = 3;
            }
            j2 = i3 + 1;
            xd = xd2;
            i = 3;
        }
    }

    /* JADX WARN: Code duplicated, block: B:43:0x0094  */
    private final void drawText(Canvas canvas, Instance c, RectF r, float scale, Instance uiStroke, Instance gradient) {
        boolean placeholder;
        Object color3;
        String str;
        int b;
        Layout.Alignment alignment;
        int wrapW;
        float tx;
        float ty;
        float f;
        float strokeT;
        Shader shader;
        Shader shaderGradientShader;
        int i;
        Object obj;
        float fWidth;
        String text = textOf(c);
        if (text == null || text.length() == 0) {
            c.getAttributes().put("__textBounds", new Vector2(0.0f, 0.0f));
            c.getAttributes().put("__textFits", Boolean.TRUE);
            return;
        }
        float tt = num(c, "TextTransparency", 0.0f);
        if (tt >= 1.0f) {
            return;
        }
        Object obj2 = c.get("MaxVisibleGraphemes");
        Number number = obj2 instanceof Number ? (Number) obj2 : null;
        int maxG = number != null ? number.intValue() : -1;
        if (maxG >= 0) {
            text = StringsKt.take(text, maxG);
        }
        String text2 = text;
        if (text2.length() == 0) {
            return;
        }
        boolean rich = Intrinsics.areEqual(c.get("RichText"), (Object) true);
        if (Intrinsics.areEqual(c.getClassName(), "TextBox")) {
            Object obj3 = c.get("Text");
            String str2 = obj3 instanceof String ? (String) obj3 : null;
            if (str2 == null || str2.length() == 0) {
                placeholder = true;
            } else {
                placeholder = false;
            }
        } else {
            placeholder = false;
        }
        if (placeholder) {
            color3 = c.get("PlaceholderColor3");
            if (color3 == null) {
                color3 = new Color3(0.7f, 0.7f, 0.7f);
            }
        } else {
            color3 = c.get("TextColor3");
        }
        int color = alpha(colorOf(color3, ViewCompat.MEASURED_STATE_MASK), tt);
        Fonts.Face face = Fonts.INSTANCE.of(c);
        float minS = RangesKt.coerceIn(num(c, "LineHeight", 1.0f), 0.5f, 3.0f);
        Instance sizeLimit = child(c, "UITextSizeConstraint");
        float size = num(c, "TextSize", 14.0f) * this.d * scale;
        boolean wrapped = Intrinsics.areEqual(c.get("TextWrapped"), (Object) true);
        if (Intrinsics.areEqual(c.get("TextScaled"), (Object) true)) {
            float maxS = Math.min(100.0f, sizeLimit != null ? num(sizeLimit, "MaxTextSize", 100.0f) : 100.0f) * this.d * scale;
            str = "TextScaled";
            SKey sKey = new SKey(text2, rich, face.getTypeface(), face.getFakeBold(), (int) r.width(), (int) r.height(), maxS, (sizeLimit != null ? num(sizeLimit, "MinTextSize", 1.0f) : 1.0f) * this.d * scale, minS);
            size = scaledSize(sKey);
            b = -16777216;
        } else {
            str = "TextScaled";
            b = 0;
            if (sizeLimit != null) {
                size = RangesKt.coerceIn(size, num(sizeLimit, "MinTextSize", 1.0f) * this.d * scale, num(sizeLimit, "MaxTextSize", 100.0f) * this.d * scale);
            }
        }
        if (size < 1.0f) {
            return;
        }
        int xAlign = enumOf(c, "TextXAlignment", 2);
        int yAlign = enumOf(c, "TextYAlignment", 1);
        switch (xAlign) {
            case 0:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case 1:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            default:
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
        }
        Layout.Alignment align = alignment;
        boolean wrap = wrapped || Intrinsics.areEqual(c.get(str), (Object) true);
        boolean truncate = (wrap || enumOf(c, "TextTruncate", 0) == 0) ? false : true;
        int rw = RangesKt.coerceAtLeast((int) r.width(), 1);
        if (wrap || truncate) {
            wrapW = rw;
        } else {
            this.textPaint.setTypeface(face.getTypeface());
            this.textPaint.setTextSize(size);
            this.textPaint.setFakeBoldText(face.getFakeBold());
            String plain = rich ? RichText.INSTANCE.parse(text2).toString() : text2;
            Iterator it = StringsKt.split((CharSequence) plain, new char[]{'\n'}, false, 0).iterator();
            if (!it.hasNext()) {
                throw new NoSuchElementException();
            }
            float fMeasureText = this.textPaint.measureText((String) it.next());
            while (it.hasNext()) {
                fMeasureText = Math.max(fMeasureText, this.textPaint.measureText((String) it.next()));
            }
            wrapW = Math.max(rw, ((int) fMeasureText) + 2);
        }
        int wrapW2 = wrapW;
        StaticLayout sl = layoutFor(new TKey(text2, rich, face.getTypeface(), face.getFakeBold(), size, wrapW, align, minS, truncate));
        float auText = this.absUnit > 0.0f ? this.absUnit : (this.d > 0.0f ? this.d : 1.0f);
        float maxLineW = 0.0f;
        int slLines = sl.getLineCount();
        for (int li = 0; li < slLines; li++) {
            float lw = sl.getLineWidth(li);
            if (lw > maxLineW) maxLineW = lw;
        }
        float slH = sl.getHeight();
        c.getAttributes().put("__textBounds", new Vector2(maxLineW / auText, slH / auText));
        c.getAttributes().put("__textFits", Boolean.valueOf((maxLineW <= r.width() + 0.5f) && (slH <= r.height() + 0.5f)));
        if (wrapW2 > r.width()) {
            float f2 = r.left;
            switch (xAlign) {
                case 0:
                    fWidth = 0.0f;
                    break;
                case 1:
                    fWidth = wrapW2 - r.width();
                    break;
                default:
                    fWidth = (wrapW2 - r.width()) / 2.0f;
                    break;
            }
            tx = f2 - fWidth;
        } else {
            tx = r.left;
        }
        switch (yAlign) {
            case 0:
                ty = r.top;
                break;
            case 1:
            default:
                ty = r.top + ((r.height() - sl.getHeight()) / 2.0f);
                break;
            case 2:
                ty = r.bottom - sl.getHeight();
                break;
        }
        TextPaint p = sl.getPaint();
        Intrinsics.checkNotNullExpressionValue(p, "getPaint(...)");
        canvas.save();
        if (Intrinsics.areEqual(c.get("ClipsDescendants"), (Object) true)) {
            canvas.clipRect(r);
        }
        canvas.translate(tx, ty);
        if (uiStroke != null) {
            strokeT = num(uiStroke, "Transparency", 0.0f);
            f = 1.0f;
        } else {
            f = 1.0f;
            strokeT = num(c, "TextStrokeTransparency", 1.0f);
        }
        if (strokeT < f) {
            if (uiStroke != null) {
                obj = uiStroke.get("Color");
                i = ViewCompat.MEASURED_STATE_MASK;
            } else {
                i = ViewCompat.MEASURED_STATE_MASK;
                obj = c.get("TextStrokeColor3");
            }
            int sc = colorOf(obj, i);
            float sw = uiStroke != null ? num(uiStroke, "Thickness", 1.0f) * this.d * scale * 2.0f : this.d * 1.6f * scale;
            p.setShader(null);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(sw);
            p.setStrokeJoin(Paint.Join.ROUND);
            p.setColor(alpha(sc, 1.0f - ((1.0f - strokeT) * (1.0f - tt))));
            if (rich) {
                drawRichStroke(canvas, sl, p);
            } else {
                sl.draw(canvas);
            }
            p.setStyle(Paint.Style.FILL);
        }
        p.setColor(color);
        if (gradient == null || (shaderGradientShader = gradientShader(gradient, r, tt)) == null) {
            shader = null;
        } else {
            Matrix matrix = new Matrix();
            matrix.setTranslate(-tx, -ty);
            shaderGradientShader.setLocalMatrix(matrix);
            shader = shaderGradientShader;
        }
        p.setShader(shader);
        sl.draw(canvas);
        p.setShader(null);
        canvas.restore();
    }

    private final void drawRichStroke(Canvas canvas, StaticLayout sl, TextPaint p) {
        CharSequence text = sl.getText();
        StaticLayout.Builder.obtain(text, 0, text.length(), p, sl.getWidth()).setAlignment(sl.getAlignment()).setIncludePad(false).setLineSpacing(0.0f, sl.getSpacingMultiplier()).build().draw(canvas);
    }

    public final Hit buttonAt(float x, float y) {
        Object objPrevious;
        List list = this.hits;
        ListIterator<Hit> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            objPrevious = listIterator.previous();
            Hit hit = (Hit) objPrevious;
            if (hit.getKind() != 2 && hit.getRect().contains(x, y)) {
                return (Hit) objPrevious;
            }
        }
        objPrevious = null;
        return (Hit) objPrevious;
    }

    public final boolean isPluginUi(Instance i) {
        boolean z;
        Intrinsics.checkNotNullParameter(i, "i");
        Iterable pluginWidgets = this.studio.getPluginWidgets();
        if (!(pluginWidgets instanceof Collection) || !((Collection) pluginWidgets).isEmpty()) {
            Iterator it = pluginWidgets.iterator();
            while (true) {
                if (it.hasNext()) {
                    PluginWidget pluginWidget = (PluginWidget) it.next();
                    if (i == pluginWidget.getInst() || i.isDescendantOf(pluginWidget.getInst())) {
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
            return true;
        }
        for (Instance p = i.getParent(); p != null; p = p.getParent()) {
            if (Intrinsics.areEqual(p.getClassName(), "CoreGui")) {
                return true;
            }
        }
        return false;
    }

    public final boolean pluginUiAt(float x, float y) {
        Iterable<Hit> iterable = this.hits;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (Hit hit : iterable) {
            if (hit.getRect().contains(x, y) && (hit.getKind() == 4 || (isPluginUi(hit.getInst()) && !(hit.getKind() == 2 && seeThrough(hit.getInst()))))) {
                return true;
            }
        }
        return false;
    }

    public final boolean blocksAt(float x, float y) {
        Iterable<Hit> iterable = this.hits;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (Hit hit : iterable) {
            if (hit.getRect().contains(x, y) && !(hit.getKind() == 2 && seeThrough(hit.getInst()))) {
                return true;
            }
        }
        return false;
    }

    public final Scroll scrollAt(float x, float y) {
        Object objPrevious;
        List list = this.scrolls;
        ListIterator<Scroll> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            objPrevious = listIterator.previous();
            if (((Scroll) objPrevious).getRect().contains(x, y)) {
                return (Scroll) objPrevious;
            }
        }
        objPrevious = null;
        return (Scroll) objPrevious;
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0067 A[Catch: all -> 0x0080, TryCatch #0 {, blocks: (B:5:0x000d, B:7:0x001b, B:11:0x0022, B:12:0x0027, B:17:0x005c, B:22:0x0078, B:21:0x0067), top: B:29:0x000d }] */
    public final void scrollBy(Scroll s, float dxPx, float dyPx) {
        Intrinsics.checkNotNullParameter(s, "s");
        synchronized (this.studio.getLock()) {
            Object obj = s.getInst().get("CanvasPosition");
            Vector2 vector2 = obj instanceof Vector2 ? (Vector2) obj : null;
            if (vector2 == null) {
                vector2 = new Vector2(0.0f, 0.0f);
            }
            float fCoerceIn = RangesKt.coerceIn(vector2.getX() - (dxPx / s.getUnit()), 0.0f, s.getMaxX());
            float fCoerceIn2 = RangesKt.coerceIn(vector2.getY() - (dyPx / s.getUnit()), 0.0f, s.getMaxY());
            if (fCoerceIn == vector2.getX()) {
                if (!(fCoerceIn2 == vector2.getY())) {
                    Instance.set$default(s.getInst(), "CanvasPosition", new Vector2(fCoerceIn, fCoerceIn2), false, 4, null);
                }
            } else {
                Instance.set$default(s.getInst(), "CanvasPosition", new Vector2(fCoerceIn, fCoerceIn2), false, 4, null);
            }
            Unit unit = Unit.INSTANCE;
        }
        invalidate();
    }

    private final boolean mouseFor(Instance i) {
        return this.studio.getPlay() == null && isPluginUi(i);
    }

    private final void uisFire(String event, LuaValue input) {
        Instance uis = this.studio.getEditModel().findService("UserInputService");
        if (uis == null) {
            return;
        }
        LuaRuntime editRuntime = this.studio.getEditRuntime();
        LuaBoolean TRUE = LuaValue.TRUE;
        Intrinsics.checkNotNullExpressionValue(TRUE, "TRUE");
        editRuntime.fire(uis, event, input, TRUE);
    }

    public final Hit pluginFrameAt(float x, float y) {
        Object obj = null;
        if (this.studio.getPlay() != null) {
            return null;
        }
        List list = this.hits;
        ListIterator<Hit> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            Object objPrevious = listIterator.previous();
            Hit hit = (Hit) objPrevious;
            if (hit.getKind() == 2 && hit.getRt() != null && hit.getRect().contains(x, y) && isPluginUi(hit.getInst())) {
                obj = objPrevious;
                break;
            }
        }
        return (Hit) obj;
    }

    private final List<Hit> extrasFor(Hit h, float x, float y) {
        int i = this.hits.indexOf(h);
        if (i < 0) {
            return CollectionsKt.emptyList();
        }
        Iterable iterableSubList = this.hits.subList(i + 1, this.hits.size());
        Intrinsics.checkNotNullExpressionValue(iterableSubList, "subList(...)");
        Collection arrayList = new ArrayList();
        for (Object obj : iterableSubList) {
            Hit hit = (Hit) obj;
            if (hit.getKind() == 2 && hit.getRt() != null && hit.getRect().contains(x, y) && isPluginUi(hit.getInst())) {
                arrayList.add(obj);
            }
        }
        return CollectionsKt.asReversed((List) arrayList);
    }

    public final void press(Hit h, float x, float y) {
        try {
        GuiOverlay guiOverlay;
        Intrinsics.checkNotNullParameter(h, "h");
        if (h.getKind() != 0 && h.getKind() != 2) {
            return;
        }
        if (h.getKind() == 0) {
            this.pressed = h.getInst();
        }
        LuaRuntime r = h.getRt();
        if (r == null) {
            return;
        }
        boolean mouse = mouseFor(h.getInst());
        synchronized (this.studio.getLock()) {
            int i = 0;
            try {
                List<Hit> listExtrasFor = mouse ? extrasFor(h, x, y) : CollectionsKt.emptyList();
                if (listExtrasFor.isEmpty()) {
                    this.pressExtras.remove(h);
                } else {
                    this.pressExtras.put(h, listExtrasFor);
                }
                int i2 = 1;
                if (mouse) {
                    for (Hit hit : listExtrasFor) {
                        Intrinsics.checkNotNullExpressionValue(hit, "next(...)");
                        hover(hit, x, y, true);
                    }
                    hover(h, x, y, true);
                }
                for (Hit hit2 : listExtrasFor) {
                    Intrinsics.checkNotNullExpressionValue(hit2, "next(...)");
                    Hit hit3 = hit2;
                    LuaRuntime rt = hit3.getRt();
                    if (rt != null) {
                        Instance inst = hit3.getInst();
                        LuaValue[] luaValueArr = new LuaValue[i2];
                        luaValueArr[0] = touchInput$default(this, x, y, 0, true, false, 16, null);
                        rt.fire(inst, "InputBegan", luaValueArr);
                    }
                }
                int i3 = i2;
                if (h.getKind() == 0) {
                    Vector2 vector2GuiPoint = guiPoint(x, y);
                    Instance inst2 = h.getInst();
                    LuaValue[] luaValueArr2 = new LuaValue[2];
                    LuaNumber luaNumberValueOf = LuaValue.valueOf(vector2GuiPoint.getX());
                    Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
                    luaValueArr2[0] = luaNumberValueOf;
                    LuaNumber luaNumberValueOf2 = LuaValue.valueOf(vector2GuiPoint.getY());
                    Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
                    luaValueArr2[i3] = luaNumberValueOf2;
                    r.fire(inst2, "MouseButton1Down", luaValueArr2);
                }
                Instance inst3 = h.getInst();
                LuaValue[] luaValueArr3 = new LuaValue[i3];
                try {
                    try {
                        luaValueArr3[0] = touchInput$default(this, x, y, 0, mouse, false, 16, null);
                        r.fire(inst3, "InputBegan", luaValueArr3);
                        if (mouse) {
                            guiOverlay = this;
                            guiOverlay.uisFire("InputBegan", touchInput$default(guiOverlay, x, y, 0, true, false, 16, null));
                        } else {
                            guiOverlay = this;
                        }
                        Unit unit = Unit.INSTANCE;
                        guiOverlay.invalidate();
                    } catch (Throwable th) {
                        throw th;
                    }
                } catch (Throwable th2) {
                    Throwable th = th2;
                }
            } catch (Throwable th3) {
                Throwable th = th3;
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final void releaseExtras(Hit h, float x, float y) {
        List<Hit> listRemove = this.pressExtras.remove(h);
        if (listRemove == null) {
            return;
        }
        for (Hit hit : listRemove) {
            Intrinsics.checkNotNullExpressionValue(hit, "next(...)");
            Hit e = hit;
            LuaRuntime rt = e.getRt();
            if (rt != null) {
                rt.fire(e.getInst(), "InputEnded", touchInput$default(this, x, y, 2, true, false, 16, null));
            }
            hover(e, x, y, false);
        }
    }

    private final void hover(Hit h, float x, float y, boolean enter) {
        LuaRuntime r = h.getRt();
        if (r == null) {
            return;
        }
        Vector2 p = guiPoint(x, y);
        Instance inst = h.getInst();
        String str = enter ? "MouseEnter" : "MouseLeave";
        LuaNumber luaNumberValueOf = LuaValue.valueOf(p.getX());
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
        LuaNumber luaNumberValueOf2 = LuaValue.valueOf(p.getY());
        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
        r.fire(inst, str, luaNumberValueOf, luaNumberValueOf2);
        Instance inst2 = h.getInst();
        String str2 = enter ? "InputBegan" : "InputEnded";
        LuaValue[] luaValueArr = new LuaValue[1];
        luaValueArr[0] = touchInput(x, y, enter ? 0 : 2, true, true);
        r.fire(inst2, str2, luaValueArr);
    }

    /* JADX WARN: Code duplicated, block: B:37:0x0093  */
    public final boolean canWheel(Hit h, float x, float y) {
        try {
        LuaRuntime r;
        boolean z;
        boolean z2;
        Intrinsics.checkNotNullParameter(h, "h");
        boolean z3 = false;
        if (this.studio.getPlay() != null || !isPluginUi(h.getInst()) || (r = h.getRt()) == null) {
            return false;
        }
        synchronized (this.studio.getLock()) {
            try {
                if (!r.hasConn(h.getInst(), "InputChanged")) {
                    Iterable iterable = (List) this.pressExtras.get(h);
                    if (iterable != null) {
                        Iterable iterable2 = iterable;
                        if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
                            Iterator it = iterable2.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    Hit hit = (Hit) it.next();
                                    LuaRuntime rt = hit.getRt();
                                    if (rt != null && rt.hasConn(hit.getInst(), "InputChanged")) {
                                        z2 = true;
                                        break;
                                    }
                                } else {
                                    z2 = false;
                                    break;
                                }
                            }
                        } else {
                            z2 = false;
                        }
                        if (z2) {
                            z = true;
                        } else {
                            z = false;
                        }
                    } else {
                        z = false;
                    }
                    if (!z) {
                        try {
                            Instance instancePluginObjectAt = pluginObjectAt(x, y);
                            if (instancePluginObjectAt != null) {
                                Iterator it2 = SequencesKt.takeWhile(SequencesKt.generateSequence(instancePluginObjectAt, (Function1<? super Instance, ? extends Instance>) new Function1() { // from class: com.goydevv.studrblx.ui.GuiOverlay$$ExternalSyntheticLambda3
                                    @Override // kotlin.jvm.functions.Function1
                                    public final Object invoke(Object obj) {
                                        return GuiOverlay.canWheel$lambda$92$lambda$89((Instance) obj);
                                    }
                                }), new Function1() { // from class: com.goydevv.studrblx.ui.GuiOverlay$$ExternalSyntheticLambda4
                                    @Override // kotlin.jvm.functions.Function1
                                    public final Object invoke(Object obj) {
                                        return Boolean.valueOf(GuiOverlay.canWheel$lambda$92$lambda$90((Instance) obj));
                                    }
                                }).iterator();
                                while (it2.hasNext()) {
                                    boolean z4 = z3;
                                    if (r.hasConn((Instance) it2.next(), "InputChanged")) {
                                        z3 = true;
                                        break;
                                    }
                                    z3 = z4;
                                }
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }
                return z3;
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Instance canWheel$lambda$92$lambda$89(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getParent();
    }

    static final boolean canWheel$lambda$92$lambda$90(Instance it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return !Intrinsics.areEqual(it.getClassName(), "CoreGui");
    }

    private final Instance pluginObjectAt(float x, float y) {
        Object objPrevious;
        Pair<Instance, RectF> pair;
        if (this.studio.getPlay() != null) {
            return null;
        }
        List list = this.picks;
        ListIterator<Pair<Instance, RectF>> listIterator = list.listIterator(list.size());
        do {
            if (listIterator.hasPrevious()) {
                objPrevious = listIterator.previous();
                pair = (Pair) objPrevious;
            } else {
                objPrevious = null;
                break;
            }
        } while (!(pair.component2().contains(x, y) && isPluginUi(pair.component1())));
        Pair<Instance, RectF> pair2 = (Pair) objPrevious;
        if (pair2 != null) {
            return pair2.getFirst();
        }
        return null;
    }

    private final LuaRuntime pluginRt(Instance i) {
        Object next;
        LuaRuntime rt;
        PluginWidget pluginWidget;
        Iterator it = this.studio.getPluginWidgets().iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
            pluginWidget = (PluginWidget) next;
        } while (!(i == pluginWidget.getInst() || i.isDescendantOf(pluginWidget.getInst())));
        PluginWidget pluginWidget2 = (PluginWidget) next;
        return (pluginWidget2 == null || (rt = pluginWidget2.getRt()) == null) ? this.studio.getEditRuntime() : rt;
    }

    public final void pluginWheel(float x, float y, int z) {
        Instance top = pluginObjectAt(x, y);
        if (top == null) {
            return;
        }
        LuaRuntime r = pluginRt(top);
        synchronized (this.studio.getLock()) {
            LuaTypes luaTypes = LuaTypes.INSTANCE;
            for (Instance parent = top; parent != null && !Intrinsics.areEqual(parent.getClassName(), "CoreGui"); parent = parent.getParent()) {
                if (r.hasConn(parent, "InputChanged")) {
                    r.fire(parent, "InputChanged", pluginWheel$lambda$95$wheel(luaTypes, this, x, y, z));
                }
            }
            uisFire("InputChanged", pluginWheel$lambda$95$wheel(luaTypes, this, x, y, z));
            Unit unit = Unit.INSTANCE;
        }
        postInvalidate();
    }

    private static final LuaValue pluginWheel$lambda$95$wheel(LuaTypes lt, GuiOverlay this$0, float $x, float $y, int $z) {
        LuaTable t = new LuaTable();
        t.set("UserInputType", lt.wrap(new EnumItemV("UserInputType", "MouseWheel", 3)));
        t.set("UserInputState", lt.wrap(new EnumItemV("UserInputState", "Change", 1)));
        Vector2 p = this$0.guiPoint($x, $y);
        t.set("Position", lt.wrap(new Vector3(p.getX(), p.getY(), $z)));
        t.set("Delta", lt.wrap(new Vector3(0.0f, 0.0f, $z)));
        t.set("KeyCode", lt.wrap(new EnumItemV("KeyCode", "Unknown", 0)));
        return t;
    }

    public final void pluginPointerMove(Hit h, float x, float y) {
        char c;
        int i;
        char c2;
        Intrinsics.checkNotNullParameter(h, "h");
        if (mouseFor(h.getInst())) {
            synchronized (this.studio.getLock()) {
                int i2 = 0;
                LuaValue luaValue = touchInput(x, y, 1, true, true);
                LuaRuntime rt = h.getRt();
                char c3 = 0;
                int i3 = 1;
                if (rt != null) {
                    rt.fire(h.getInst(), "InputChanged", luaValue);
                }
                Iterable<Hit> iterable = (List) this.pressExtras.get(h);
                if (iterable != null) {
                    for (Hit hit : iterable) {
                        LuaRuntime rt2 = hit.getRt();
                        if (rt2 != null) {
                            Instance inst = hit.getInst();
                            LuaValue[] luaValueArr = new LuaValue[i3];
                            c2 = c3;
                            luaValueArr[c2] = touchInput(x, y, 1, true, true);
                            rt2.fire(inst, "InputChanged", luaValueArr);
                        } else {
                            c2 = c3;
                        }
                        c3 = c2;
                    }
                    c = c3;
                    i = i3;
                } else {
                    c = 0;
                    i = 1;
                }
                Vector2 vector2GuiPoint = guiPoint(x, y);
                LuaRuntime rt3 = h.getRt();
                if (rt3 != null) {
                    Instance inst2 = h.getInst();
                    LuaValue[] luaValueArr2 = new LuaValue[2];
                    LuaNumber luaNumberValueOf = LuaValue.valueOf(vector2GuiPoint.getX());
                    Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
                    luaValueArr2[c] = luaNumberValueOf;
                    LuaNumber luaNumberValueOf2 = LuaValue.valueOf(vector2GuiPoint.getY());
                    Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
                    luaValueArr2[i] = luaNumberValueOf2;
                    rt3.fire(inst2, "MouseMoved", luaValueArr2);
                }
                uisFire("InputChanged", touchInput(x, y, 1, true, true));
                Unit unit = Unit.INSTANCE;
            }
            postInvalidate();
        }
    }

    public final void release(Hit h, float x, float y, boolean click) {
        try {
        GuiOverlay guiOverlay;
        float f;
        float f2;
        GuiOverlay guiOverlay2;
        float f3;
        float f4;
        Function2<? super Float, ? super Float, Unit> function2;
        Intrinsics.checkNotNullParameter(h, "h");
        if (this.pressed == h.getInst()) {
            this.pressed = null;
        }
        invalidate();
        switch (h.getKind()) {
            case 0:
                LuaRuntime r = h.getRt();
                if (r == null) {
                    return;
                }
                boolean mouse = mouseFor(h.getInst());
                synchronized (this.studio.getLock()) {
                    try {
                        releaseExtras(h, x, y);
                        Vector2 vector2GuiPoint = guiPoint(x, y);
                        Instance inst = h.getInst();
                        LuaNumber luaNumberValueOf = LuaValue.valueOf(vector2GuiPoint.getX());
                        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf, "valueOf(...)");
                        LuaNumber luaNumberValueOf2 = LuaValue.valueOf(vector2GuiPoint.getY());
                        Intrinsics.checkNotNullExpressionValue(luaNumberValueOf2, "valueOf(...)");
                        r.fire(inst, "MouseButton1Up", luaNumberValueOf, luaNumberValueOf2);
                        Instance inst2 = h.getInst();
                        LuaValue[] luaValueArr = new LuaValue[1];
                        try {
                            luaValueArr[0] = touchInput$default(this, x, y, 2, mouse, false, 16, null);
                            r.fire(inst2, "InputEnded", luaValueArr);
                            if (mouse) {
                                guiOverlay = this;
                                try {
                                    guiOverlay.uisFire("InputEnded", touchInput$default(guiOverlay, x, y, 2, true, false, 16, null));
                                } catch (Throwable th) {
                                    throw th;
                                }
                            } else {
                                guiOverlay = this;
                            }
                            if (!click) {
                                f = x;
                                f2 = y;
                            } else {
                                r.fire(h.getInst(), "MouseButton1Click", new LuaValue[0]);
                                Instance inst3 = h.getInst();
                                LuaValue[] luaValueArr2 = new LuaValue[2];
                                f = x;
                                f2 = y;
                                try {
                                    luaValueArr2[0] = touchInput$default(guiOverlay, f, f2, 2, false, false, 24, null);
                                    LuaInteger luaIntegerValueOf = LuaValue.valueOf(0);
                                    Intrinsics.checkNotNullExpressionValue(luaIntegerValueOf, "valueOf(...)");
                                    luaValueArr2[1] = luaIntegerValueOf;
                                    r.fire(inst3, "Activated", luaValueArr2);
                                } catch (Throwable th2) {
                                    Throwable th = th2;
                                    throw th;
                                }
                            }
                            if (mouse) {
                                guiOverlay.hover(h, f, f2, false);
                            }
                            Unit unit = Unit.INSTANCE;
                            return;
                        } catch (Throwable th3) {
                            Throwable th = th3;
                        }
                    } catch (Throwable th4) {
                        Throwable th = th4;
                    }
                }
                break;
            case 1:
                if (click) {
                    editTextBox(h);
                    return;
                }
                return;
            case 2:
                LuaRuntime r2 = h.getRt();
                if (r2 == null) {
                    return;
                }
                boolean mouse2 = mouseFor(h.getInst());
                synchronized (this.studio.getLock()) {
                    try {
                        releaseExtras(h, x, y);
                        Instance inst4 = h.getInst();
                        LuaValue[] luaValueArr3 = new LuaValue[1];
                        try {
                            try {
                                luaValueArr3[0] = touchInput$default(this, x, y, 2, mouse2, false, 16, null);
                                r2.fire(inst4, "InputEnded", luaValueArr3);
                                if (mouse2) {
                                    guiOverlay2 = this;
                                    f3 = x;
                                    f4 = y;
                                    try {
                                        guiOverlay2.uisFire("InputEnded", touchInput$default(guiOverlay2, f3, f4, 2, true, false, 16, null));
                                    } catch (Throwable th5) {
                                        Throwable th = th5;
                                        throw th;
                                    }
                                } else {
                                    guiOverlay2 = this;
                                    f3 = x;
                                    f4 = y;
                                }
                                if (mouse2) {
                                    guiOverlay2.hover(h, f3, f4, false);
                                }
                                Unit unit2 = Unit.INSTANCE;
                                return;
                            } catch (Throwable th6) {
                                Throwable th = th6;
                            }
                        } catch (Throwable th7) {
                            Throwable th = th7;
                            throw th;
                        }
                    } catch (Throwable th8) {
                        Throwable th = th8;
                    }
                }
                break;
            case 3:
                if (click && (function2 = this.onCoreMenu) != null) {
                    function2.invoke(Float.valueOf(x), Float.valueOf(y));
                }
                return;
            case 4:
                if (click) {
                    synchronized (this.studio.getLock()) {
                        Instance.set$default(h.getInst(), "Enabled", false, false, 4, null);
                        Unit unit3 = Unit.INSTANCE;
                    }
                    postInvalidate();
                }
                return;
            default:
                return;
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final Vector2 guiPoint(float x, float y) {
        boolean edit = this.studio.getPlay() == null;
        float u = this.d;
        if (edit) {
            u *= this.studio.editGuiScale();
        }
        Vector2 p = new Vector2(x / u, (y / u) - (edit ? 0.0f : 58.0f));
        if (edit) {
            this.studio.setLastEditPointer(p);
        }
        return p;
    }

    static /* synthetic */ LuaValue touchInput$default(GuiOverlay guiOverlay, float f, float f2, int i, boolean z, boolean z2, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            z = false;
        }
        if ((i2 & 16) != 0) {
            z2 = false;
        }
        return guiOverlay.touchInput(f, f2, i, z, z2);
    }

    private final LuaValue touchInput(float x, float y, int state, boolean mouse, boolean movement) {
        EnumItemV type;
        String stateName;
        LuaTypes lt = LuaTypes.INSTANCE;
        LuaTable t = new LuaTable();
        if (movement) {
            type = new EnumItemV("UserInputType", "MouseMovement", 4);
        } else if (mouse) {
            type = new EnumItemV("UserInputType", "MouseButton1", 0);
        } else {
            type = new EnumItemV("UserInputType", "Touch", 7);
        }
        t.set("UserInputType", lt.wrap(type));
        switch (state) {
            case 0:
                stateName = "Begin";
                break;
            case 1:
                stateName = "Change";
                break;
            default:
                stateName = "End";
                break;
        }
        t.set("UserInputState", lt.wrap(new EnumItemV("UserInputState", stateName, state)));
        Vector2 gp = guiPoint(x, y);
        Vector3 pos = new Vector3(gp.getX(), gp.getY(), 0.0f);
        t.set("Position", lt.wrap(pos));
        Vector3 prev = this.lastPointer;
        t.set("Delta", lt.wrap((state != 1 || prev == null) ? Vector3.INSTANCE.getZERO() : pos.minus(prev)));
        t.set("KeyCode", lt.wrap(new EnumItemV("KeyCode", "Unknown", 0)));
        this.lastPointer = pos;
        return t;
    }

    /* JADX WARN: Code duplicated, block: B:36:0x007a  */
    private final void editTextBox(Hit h) {
        String str = null;
        String str2;
        String str3;
        String str4;
        boolean multiLine;
        final Instance inst = h.getInst();
        final LuaRuntime r = h.getRt();
        if (r == null) {
            return;
        }
        Ui ui = Ui.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        synchronized (this.studio.getLock()) {
            if (Boolean.FALSE.equals(inst.get("TextEditable"))) {
                return;
            }
            boolean clearOnFocus = !Boolean.FALSE.equals(inst.get("ClearTextOnFocus"));
            multiLine = Boolean.TRUE.equals(inst.get("MultiLine"));
            if (clearOnFocus) {
                str2 = "";
                Instance.set$default(inst, "Text", "", false, 4, null);
            } else {
                Object obj = inst.get("Text");
                str2 = obj instanceof String ? (String) obj : "";
            }
        }
        final EditText e = Ui.input$default(ui, context, str2, null, false, false, 28, null);
        e.setSingleLine(!multiLine);
        if (multiLine) {
            e.setMinLines(3);
            e.setMaxLines(8);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        synchronized (this.studio.getLock()) {
            Object obj2 = inst.get("PlaceholderText");
            str3 = obj2 instanceof String ? (String) obj2 : null;
        }
        if (str3 != null) {
            if (!(str3.length() == 0)) {
                str = str3;
            }
            str4 = str != null ? str : "Enter text";
        } else {
            str4 = "Enter text";
        }
        builder.setTitle(str4).setView(e).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.GuiOverlay$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                try {
                    GuiOverlay.editTextBox$lambda$107(GuiOverlay.this, inst, e, r, dialogInterface, i);
                } catch (Throwable _t) {
                    if (_t instanceof RuntimeException) throw (RuntimeException) _t;
                    if (_t instanceof Error) throw (Error) _t;
                    throw new RuntimeException(_t);
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.GuiOverlay$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                GuiOverlay.editTextBox$lambda$109(GuiOverlay.this, r, inst, dialogInterface, i);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public final void onCancel(DialogInterface dialogInterface) {
                GuiOverlay.editTextBox$lambda$109(GuiOverlay.this, r, inst, dialogInterface, 0);
            }
        }).show();
        synchronized (this.studio.getLock()) {
            r.setFocusedTextBox(inst);
            r.fire(inst, "Focused", new LuaValue[0]);
            Unit unit = Unit.INSTANCE;
        }
    }

    static final void editTextBox$lambda$107(GuiOverlay this$0, Instance $inst, EditText $e, LuaRuntime $r, DialogInterface dialogInterface, int i) {
        try {
            synchronized (this$0.studio.getLock()) {
                Instance.set$default($inst, "Text", $e.getText().toString(), false, 4, null);
                if ($r.getFocusedTextBox() == $inst) {
                    $r.setFocusedTextBox(null);
                }
                LuaBoolean TRUE = LuaValue.TRUE;
                Intrinsics.checkNotNullExpressionValue(TRUE, "TRUE");
                LuaValue NIL = LuaValue.NIL;
                Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
                $r.fire($inst, "FocusLost", TRUE, NIL);
                Unit unit = Unit.INSTANCE;
            }
        } catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final void editTextBox$lambda$109(GuiOverlay this$0, LuaRuntime $r, Instance $inst, DialogInterface dialogInterface, int i) {
        synchronized (this$0.studio.getLock()) {
            if ($r.getFocusedTextBox() == $inst) {
                $r.setFocusedTextBox(null);
            }
            LuaBoolean FALSE = LuaValue.FALSE;
            Intrinsics.checkNotNullExpressionValue(FALSE, "FALSE");
            LuaValue NIL = LuaValue.NIL;
            Intrinsics.checkNotNullExpressionValue(NIL, "NIL");
            $r.fire($inst, "FocusLost", FALSE, NIL);
            Unit unit = Unit.INSTANCE;
        }
    }

    public final boolean tap(float x, float y) {
        try {
        Hit h = buttonAt(x, y);
        if (h == null) {
            return false;
        }
        if (this.studio.getPlay() == null && h.getRt() == this.studio.getEditRuntime() && h.getKind() != 4 && !isPluginUi(h.getInst())) {
            return false;
        }
        press(h, x, y);
        release(h, x, y, true);
        return true;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final Instance pick(float x, float y) {
        Instance sg;
        Object objPrevious;
        Pair<Instance, RectF> pair;
        Instance instanceComponent1;
        if (this.studio.getPlay() != null || (sg = this.studio.getModel().findService("StarterGui")) == null) {
            return null;
        }
        List list = this.picks;
        ListIterator<Pair<Instance, RectF>> listIterator = list.listIterator(list.size());
        do {
            if (listIterator.hasPrevious()) {
                objPrevious = listIterator.previous();
                pair = (Pair) objPrevious;
                instanceComponent1 = pair.component1();
            } else {
                objPrevious = null;
                break;
            }
        } while (!(pair.component2().contains(x, y) && instanceComponent1.isDescendantOf(sg) && !seeThrough(instanceComponent1)));
        Pair<Instance, RectF> pair2 = (Pair) objPrevious;
        if (pair2 != null) {
            return pair2.getFirst();
        }
        return null;
    }
}
