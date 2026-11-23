package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.goydevv.studrblx.audio.AudioEngine;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import com.goydevv.studrblx.net.AuthRequiredException;
import com.goydevv.studrblx.net.HttpException;
import com.goydevv.studrblx.net.ImageLoader;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.render.MeshData;
import com.goydevv.studrblx.render.RbxMesh;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: ToolboxPanel.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class ToolboxPanel extends LinearLayout {
    private final MainActions actions;
    private final MarketAdapter adapter;
    private AudioBrowser audioBrowser;
    private final FrameLayout body;
    private RobloxApi.Category category;
    private String cursor;
    private final List<String> deviceKinds;
    private boolean dropHighlight;
    private boolean endReached;
    private View gridHost;
    private final HashMap<Integer, Pair<ArrayList<RobloxApi.InvItem>, String>> invCache;
    private String invKind;
    private boolean invLoading;
    private boolean invRoblox;
    private int invRobloxCat;
    private boolean invSortNewest;
    private final HashMap<String, List<ImageView>> invThumbViews;
    private final HashMap<Long, String> invThumbs;
    private final ArrayList<RobloxApi.Item> items;
    private boolean loading;
    private View market;

    /* JADX INFO: renamed from: meta$delegate, reason: from kotlin metadata */
    private final Lazy meta;
    private final ExecutorService net;
    private RobloxApi.Price price;
    private View priceRow;
    private ProgressBar progress;
    private String query;
    private Function0<Unit> restyleCats;
    private final List<InvCat> robloxCats;
    private EditText searchBox;
    private int searchGen;
    private TextView status;
    private final Studio studio;
    private int tab;
    private final List<String> tabNames;
    private final ArrayList<TextView> tabViews;
    private final HashMap<Long, String> thumbUrls;

    /* JADX INFO: compiled from: ToolboxPanel.kt */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] iArr = new int[RobloxApi.Price.values().length];
            try {
                iArr[RobloxApi.Price.FREE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[RobloxApi.Price.PAID.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[RobloxApi.Category.values().length];
            try {
                iArr2[RobloxApi.Category.DECALS.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr2[RobloxApi.Category.AUDIO.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr2[RobloxApi.Category.MESHES.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr2[RobloxApi.Category.PLUGINS.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            $EnumSwitchMapping$1 = iArr2;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ToolboxPanel(final Context ctx, Studio studio, MainActions actions) {
        super(ctx);
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        this.studio = studio;
        this.actions = actions;
        this.body = new FrameLayout(ctx);
        this.tabViews = new ArrayList<>();
        int i = 4;
        int i2 = 1;
        this.tabNames = CollectionsKt.listOf(new String[]{"Creator Store", "Inventory", "Parts", "Objects"});
        this.net = Executors.newSingleThreadExecutor();
        this.items = new ArrayList<>();
        this.query = "";
        this.category = RobloxApi.Category.MODELS;
        this.adapter = new MarketAdapter();
        this.thumbUrls = new HashMap<>();
        this.meta = LazyKt.lazy(new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.this.getContext().getSharedPreferences("toolbox_meta", 0);
            }
        });
        this.price = RobloxApi.Price.ANY;
        this.invThumbViews = new HashMap<>();
        setOrientation(1);
        setBackgroundColor(Ui.INSTANCE.getPANEL());
        addView(Ui.INSTANCE.header(ctx, "Toolbox", Icons.general$default(Icons.INSTANCE, ctx, "Toolbox", null, 4, null)), Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        setOnDragListener(new View.OnDragListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda6
            @Override // android.view.View.OnDragListener
            public final boolean onDrag(View view, DragEvent dragEvent) {
                return ToolboxPanel._init_$lambda$3(ToolboxPanel.this, ctx, view, dragEvent);
            }
        });
        LinearLayout tabs = new LinearLayout(ctx);
        tabs.setOrientation(0);
        tabs.setBackgroundColor(Ui.INSTANCE.getHEADER());
        int i3 = 0;
        for (String name : this.tabNames) {
            final int i4 = i3;
            int i5 = i3 + 1;
            LinearLayout tabs2 = tabs;
            TextView t = Ui.text$default(Ui.INSTANCE, ctx, name, 12.0f, 0, false, 24, null);
            t.setGravity(17);
            t.setPadding(Ui.INSTANCE.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 7.0f), Ui.INSTANCE.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 7.0f));
            t.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ToolboxPanel.lambda$6$lambda$5(ToolboxPanel.this, i4, view);
                }
            });
            this.tabViews.add(t);
            tabs2.addView(t, Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null));
            tabs = tabs2;
            i3 = i5;
        }
        int i6 = i;
        int i7 = i2;
        LinearLayout tabs3 = tabs;
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(ctx);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.setBackgroundColor(Ui.INSTANCE.getHEADER());
        horizontalScrollView.addView(tabs3);
        addView(horizontalScrollView, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        addView(this.body, Ui.INSTANCE.lp(-1, 0, 1.0f));
        ImageLoader.INSTANCE.getListeners().add(new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel._init_$lambda$12(ToolboxPanel.this, (String) obj);
            }
        });
        refresh();
        this.invKind = "All";
        InvCat[] invCatArr = new InvCat[7];
        invCatArr[0] = new InvCat("Models", CollectionsKt.listOf(10), RobloxApi.Category.MODELS);
        Integer[] numArr = new Integer[2];
        numArr[0] = 40;
        numArr[i7] = Integer.valueOf(i6);
        invCatArr[i7] = new InvCat("Meshes", CollectionsKt.listOf(numArr), RobloxApi.Category.MESHES);
        invCatArr[2] = new InvCat("Plugins", CollectionsKt.listOf(38), RobloxApi.Category.PLUGINS);
        invCatArr[3] = new InvCat("Packages", CollectionsKt.listOf(32), RobloxApi.Category.MODELS);
        invCatArr[i6] = new InvCat("Decals", CollectionsKt.listOf(13), RobloxApi.Category.DECALS);
        invCatArr[5] = new InvCat("Audio", CollectionsKt.listOf(3), RobloxApi.Category.AUDIO);
        invCatArr[6] = new InvCat("Animations", CollectionsKt.listOf(24), RobloxApi.Category.MODELS);
        this.robloxCats = CollectionsKt.listOf(invCatArr);
        String[] strArr = new String[9];
        strArr[0] = "All";
        strArr[i7] = "Models";
        strArr[2] = "Parts";
        strArr[3] = "Meshes";
        strArr[i6] = "Scripts";
        strArr[5] = "GUI";
        strArr[6] = "Audio";
        strArr[7] = "Images";
        strArr[8] = "Plugins";
        this.deviceKinds = CollectionsKt.listOf(strArr);
        this.invCache = new HashMap<>();
        this.invThumbs = new HashMap<>();
    }

    public final File getLibraryDir() {
        File file = new File(getContext().getFilesDir(), "toolbox");
        file.mkdirs();
        return file;
    }

    private final SharedPreferences getMeta() {
        return (SharedPreferences) this.meta.getValue();
    }

    static final boolean _init_$lambda$3(ToolboxPanel this$0, Context $ctx, View view, DragEvent e) {
        String name;
        switch (e.getAction()) {
            case 1:
                return e.getLocalState() instanceof ExplorerPanel.DragPayload;
            case 2:
            default:
                return true;
            case 3:
                this$0.setForeground(null);
                Object localState = e.getLocalState();
                ExplorerPanel.DragPayload payload = localState instanceof ExplorerPanel.DragPayload ? (ExplorerPanel.DragPayload) localState : null;
                if (payload == null) {
                    return false;
                }
                byte[] bytes = this$0.studio.serializeInstances(payload.getItems());
                if (bytes == null) {
                    this$0.studio.toast("Those objects can't be saved");
                    return false;
                }
                Instance first = (Instance) CollectionsKt.first((List) payload.getItems());
                if (payload.getItems().size() > 1) {
                    name = first.getName() + " (+" + (payload.getItems().size() - 1) + ")";
                } else {
                    name = first.getName();
                }
                this$0.saveToLibrary(name, bytes, first.getClassName());
                this$0.studio.toast("Saved " + name + " to your Toolbox inventory");
                return true;
            case 4:
            case 6:
                this$0.dropHighlight = false;
                this$0.setForeground(null);
                return true;
            case 5:
                this$0.dropHighlight = true;
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(859540459);
                gradientDrawable.setStroke(Ui.INSTANCE.dp($ctx, 2.0f), Ui.INSTANCE.getACCENT());
                this$0.setForeground(gradientDrawable);
                return true;
        }
    }

    static final void lambda$6$lambda$5(ToolboxPanel this$0, int $i, View it) {
        this$0.tab = $i;
        this$0.refresh();
    }

    static final Unit _init_$lambda$12(final ToolboxPanel this$0, final String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        this$0.post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ToolboxPanel.lambda$12$lambda$11(key, this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final void lambda$12$lambda$11(String $key, ToolboxPanel this$0) {
        Bitmap bitmapPeekKey;
        if (StringsKt.startsWith($key, "url:", false)) {
            this$0.adapter.notifyDataSetChanged();
        }
        Iterable iterable = (List) this$0.invThumbViews.remove($key);
        if (iterable == null || (bitmapPeekKey = ImageLoader.INSTANCE.peekKey($key)) == null) {
            return;
        }
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            ((ImageView) it.next()).setImageBitmap(bitmapPeekKey);
        }
    }

    public final void refresh() {
        GradientDrawable gradientDrawable;
        int i = 0;
        for (Object obj : this.tabViews) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            TextView textView = (TextView) obj;
            textView.setTextColor(i == this.tab ? Ui.INSTANCE.getTEXT() : Ui.INSTANCE.getDIM());
            if (i == this.tab) {
                GradientDrawable gradientDrawable2 = new GradientDrawable();
                gradientDrawable2.setColor(Ui.INSTANCE.getPANEL());
                gradientDrawable = gradientDrawable2;
            } else {
                gradientDrawable = null;
            }
            textView.setBackground(gradientDrawable);
            i = i2;
        }
        this.body.removeAllViews();
        switch (this.tab) {
            case 0:
                FrameLayout frameLayout = this.body;
                View viewBuildMarket = this.market;
                if (viewBuildMarket == null) {
                    viewBuildMarket = buildMarket();
                    this.market = viewBuildMarket;
                }
                frameLayout.addView(viewBuildMarket);
                break;
            case 1:
                this.body.addView(scroll(new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda61
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return ToolboxPanel.refresh$lambda$16(ToolboxPanel.this, (LinearLayout) obj2);
                    }
                }));
                break;
            case 2:
                this.body.addView(scroll(new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda62
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return ToolboxPanel.refresh$lambda$17(ToolboxPanel.this, (LinearLayout) obj2);
                    }
                }));
                break;
            default:
                this.body.addView(scroll(new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda63
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return ToolboxPanel.refresh$lambda$18(ToolboxPanel.this, (LinearLayout) obj2);
                    }
                }));
                break;
        }
    }

    static final Unit refresh$lambda$16(ToolboxPanel this$0, LinearLayout it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.myModels(it);
        return Unit.INSTANCE;
    }

    static final Unit refresh$lambda$17(ToolboxPanel this$0, LinearLayout it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.parts(it);
        return Unit.INSTANCE;
    }

    static final Unit refresh$lambda$18(ToolboxPanel this$0, LinearLayout it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.objects(it);
        return Unit.INSTANCE;
    }

    private final View scroll(Function1<? super LinearLayout, Unit> fill) {
        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(1);
        fill.invoke(content);
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(content);
        return scrollView;
    }

    private final View buildMarket() {
        RobloxApi.Price priceValueOf;
        String str = "ANY";
        final Context ctx = getContext();
        try {
            String string = getMeta().getString("priceFilter", "ANY");
            if (string != null) {
                str = string;
            }
            priceValueOf = RobloxApi.Price.valueOf(str);
        } catch (Exception e) {
            priceValueOf = RobloxApi.Price.ANY;
        }
        this.price = priceValueOf;
        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(1);
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        Ui ui = Ui.INSTANCE;
        Intrinsics.checkNotNull(ctx);
        linearLayout.setPadding(ui.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 2.0f));
        final EditText search = Ui.input$default(Ui.INSTANCE, ctx, "", "Search the Creator Store", false, false, 24, null);
        search.setTextSize(13.0f);
        search.setImeOptions(3);
        search.setInputType(1);
        this.searchBox = search;
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda35
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return ToolboxPanel.buildMarket$lambda$24(ToolboxPanel.this, search, textView, i, keyEvent);
            }
        });
        linearLayout.addView(search, Ui.INSTANCE.lp(0, -2, 1.0f));
        linearLayout.addView(Ui.INSTANCE.iconButton(ctx, Icons.INSTANCE.general(ctx, "FindAll", "Go"), 34.0f, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda36
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.buildMarket$lambda$25(ToolboxPanel.this, search, (View) obj);
            }
        }));
        root.addView(linearLayout, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        LinearLayout linearLayout2 = new LinearLayout(ctx);
        linearLayout2.setOrientation(0);
        linearLayout2.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 2.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 4.0f));
        ArrayList catViews = new ArrayList();
        RobloxApi.Category[] categoryArrValues = RobloxApi.Category.values();
        int length = categoryArrValues.length;
        int i = 0;
        while (i < length) {
            final RobloxApi.Category c = categoryArrValues[i];
            EditText search2 = search;
            RobloxApi.Category[] categoryArr = categoryArrValues;
            int i2 = length;
            final ArrayList catViews2 = catViews;
            TextView v = Ui.text$default(Ui.INSTANCE, ctx, c == RobloxApi.Category.DECALS ? "Images" : c.getLabel(), 12.0f, 0, false, 24, null);
            v.setPadding(Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 4.0f));
            v.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda37
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ToolboxPanel.buildMarket$lambda$30$lambda$29(ToolboxPanel.this, c, catViews2, ctx, view);
                }
            });
            catViews2.add(v);
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = Ui.INSTANCE.dp(ctx, 6.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout2.addView(v, layoutParamsLp$default);
            i++;
            catViews = catViews2;
            categoryArrValues = categoryArr;
            length = i2;
        }
        final ArrayList catViews3 = catViews;
        buildMarket$styleCats(catViews3, this, ctx);
        this.restyleCats = new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda38
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.buildMarket$lambda$32(catViews3, ToolboxPanel.this, ctx);
            }
        };
        LinearLayout linearLayout3 = new LinearLayout(ctx);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(16);
        LinearLayout catRow = linearLayout3;
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(ctx);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.addView(linearLayout2);
        catRow.addView(horizontalScrollView, Ui.INSTANCE.lp(0, -2, 1.0f));
        catRow.addView(Ui.INSTANCE.iconButton(ctx, Icons.INSTANCE.general(ctx, "Account", "Acc"), 28.0f, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda39
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.buildMarket$lambda$35(ToolboxPanel.this, (View) obj);
            }
        }));
        root.addView(catRow, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        LinearLayout linearLayout4 = new LinearLayout(ctx);
        linearLayout4.setOrientation(0);
        linearLayout4.setGravity(16);
        linearLayout4.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), 0, Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 4.0f));
        final ArrayList priceViews = new ArrayList();
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, "Price:", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
        layoutParamsLp$default2.rightMargin = Ui.INSTANCE.dp(ctx, 6.0f);
        Unit unit2 = Unit.INSTANCE;
        linearLayout4.addView(textViewText$default, layoutParamsLp$default2);
        RobloxApi.Price[] priceArrValues = RobloxApi.Price.values();
        int length2 = priceArrValues.length;
        int i3 = 0;
        while (i3 < length2) {
            final RobloxApi.Price p = priceArrValues[i3];
            int i4 = length2;
            int i5 = i3;
            TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, ctx, p.getLabel(), 11.0f, 0, false, 24, null);
            textViewText$default2.setPadding(Ui.INSTANCE.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 3.0f), Ui.INSTANCE.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 3.0f));
            textViewText$default2.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda40
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ToolboxPanel.buildMarket$lambda$41$lambda$40(ToolboxPanel.this, p, priceViews, ctx, view);
                }
            });
            priceViews.add(textViewText$default2);
            TextView textView = textViewText$default2;
            LinearLayout.LayoutParams layoutParamsLp$default3 = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            layoutParamsLp$default3.rightMargin = Ui.INSTANCE.dp(ctx, 5.0f);
            Unit unit3 = Unit.INSTANCE;
            linearLayout4.addView(textView, layoutParamsLp$default3);
            i3 = i5 + 1;
            length2 = i4;
        }
        buildMarket$stylePrice(priceViews, this, ctx);
        this.priceRow = linearLayout4;
        root.addView(linearLayout4, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        FrameLayout listHost = new FrameLayout(ctx);
        RecyclerView rv = new RecyclerView(ctx);
        final GridLayoutManager glm = new GridLayoutManager(ctx, 2);
        rv.setLayoutManager(glm);
        rv.setAdapter(this.adapter);
        rv.setItemAnimator(null);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel.buildMarket.10
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView r, int dx, int dy) {
                Intrinsics.checkNotNullParameter(r, "r");
                if (glm.findLastVisibleItemPosition() >= ToolboxPanel.this.items.size() - 6) {
                    ToolboxPanel.this.loadMore();
                }
            }
        });
        rv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda41
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13) {
                ToolboxPanel.buildMarket$lambda$43(glm, ctx, view, i6, i7, i8, i9, i10, i11, i12, i13);
            }
        });
        listHost.addView(rv, new FrameLayout.LayoutParams(-1, -1));
        ProgressBar progressBar = new ProgressBar(ctx);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(8);
        this.progress = progressBar;
        ProgressBar progressBar2 = this.progress;
        if (progressBar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progress");
            progressBar2 = null;
        }
        listHost.addView(progressBar2, new FrameLayout.LayoutParams(Ui.INSTANCE.dp(ctx, 36.0f), Ui.INSTANCE.dp(ctx, 36.0f), 17));
        TextView textViewText$default3 = Ui.text$default(Ui.INSTANCE, ctx, "", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default3.setSingleLine(false);
        textViewText$default3.setGravity(17);
        textViewText$default3.setPadding(Ui.INSTANCE.dp(ctx, 16.0f), 0, Ui.INSTANCE.dp(ctx, 16.0f), 0);
        this.status = textViewText$default3;
        TextView textView2 = this.status;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("status");
            textView2 = null;
        }
        listHost.addView(textView2, new FrameLayout.LayoutParams(-1, -2, 17));
        FrameLayout stack = new FrameLayout(ctx);
        stack.addView(listHost, new FrameLayout.LayoutParams(-1, -1));
        this.gridHost = listHost;
        final Activity act = (Activity) ctx;
        AudioBrowser ab = new AudioBrowser(ctx, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda42
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.buildMarket$lambda$46(ToolboxPanel.this, (RobloxApi.AudioItem) obj);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda43
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.buildMarket$lambda$48(ctx, act, (Function0) obj);
            }
        });
        ab.setVisibility(8);
        this.audioBrowser = ab;
        stack.addView(ab, new FrameLayout.LayoutParams(-1, -1));
        root.addView(stack, Ui.INSTANCE.lp(-1, 0, 1.0f));
        showCategory();
        return root;
    }

    private static final void buildMarket$go(ToolboxPanel this$0, EditText search) {
        this$0.query = search.getText().toString();
        if (this$0.category != RobloxApi.Category.AUDIO) {
            this$0.newSearch();
            return;
        }
        AudioBrowser audioBrowser = this$0.audioBrowser;
        if (audioBrowser != null) {
            audioBrowser.search(this$0.query);
        }
    }

    static final boolean buildMarket$lambda$24(ToolboxPanel this$0, EditText $search, TextView textView, int id, KeyEvent ev) {
        if (id != 3) {
            if (!(ev != null && ev.getKeyCode() == 66) || ev.getAction() != 0) {
                return false;
            }
        }
        buildMarket$go(this$0, $search);
        return true;
    }

    static final Unit buildMarket$lambda$25(ToolboxPanel this$0, EditText $search, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        buildMarket$go(this$0, $search);
        return Unit.INSTANCE;
    }

    private static final void buildMarket$styleCats(ArrayList<TextView> arrayList, ToolboxPanel this$0, Context ctx) {
        int i = 0;
        for (Object obj : arrayList) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            TextView textView = (TextView) obj;
            boolean z = RobloxApi.Category.values()[i] == this$0.category;
            GradientDrawable gradientDrawable = new GradientDrawable();
            Ui ui = Ui.INSTANCE;
            gradientDrawable.setColor(z ? ui.getACCENT() : ui.getHOVER());
            Ui ui2 = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            gradientDrawable.setCornerRadius(ui2.dp(ctx, 12.0f));
            textView.setBackground(gradientDrawable);
            textView.setTextColor(z ? -1 : Ui.INSTANCE.getTEXT());
            i = i2;
        }
    }

    static final void buildMarket$lambda$30$lambda$29(ToolboxPanel this$0, RobloxApi.Category $c, ArrayList $catViews, Context $ctx, View it) {
        this$0.category = $c;
        buildMarket$styleCats($catViews, this$0, $ctx);
        this$0.showCategory();
    }

    static final Unit buildMarket$lambda$32(ArrayList $catViews, ToolboxPanel this$0, Context $ctx) {
        buildMarket$styleCats($catViews, this$0, $ctx);
        return Unit.INSTANCE;
    }

    static final Unit buildMarket$lambda$35(ToolboxPanel this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AccountUi accountUi = AccountUi.INSTANCE;
        Context context = this$0.getContext();
        Intrinsics.checkNotNull(context, "null cannot be cast to non-null type android.app.Activity");
        accountUi.show((Activity) context, this$0.studio, this$0.actions);
        return Unit.INSTANCE;
    }

    private static final void buildMarket$stylePrice(ArrayList<TextView> arrayList, ToolboxPanel this$0, Context ctx) {
        int i = 0;
        for (Object obj : arrayList) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            TextView textView = (TextView) obj;
            boolean z = RobloxApi.Price.values()[i] == this$0.price;
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(z ? Ui.INSTANCE.getACCENT() : 0);
            Ui ui = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            int iDp = ui.dp(ctx, 1.0f);
            Ui ui2 = Ui.INSTANCE;
            gradientDrawable.setStroke(iDp, z ? ui2.getACCENT() : ui2.getBORDER());
            gradientDrawable.setCornerRadius(Ui.INSTANCE.dp(ctx, 10.0f));
            textView.setBackground(gradientDrawable);
            textView.setTextColor(z ? -1 : Ui.INSTANCE.getDIM());
            i = i2;
        }
    }

    static final void buildMarket$lambda$41$lambda$40(ToolboxPanel this$0, RobloxApi.Price $p, ArrayList $priceViews, Context $ctx, View it) {
        this$0.price = $p;
        this$0.getMeta().edit().putString("priceFilter", $p.name()).apply();
        buildMarket$stylePrice($priceViews, this$0, $ctx);
        if (this$0.category != RobloxApi.Category.AUDIO) {
            this$0.newSearch();
        }
    }

    static final void buildMarket$lambda$43(GridLayoutManager $glm, Context $ctx, View view, int l, int i, int r, int i2, int oldL, int i3, int oldR, int i4) {
        if (r - l != oldR - oldL) {
            Ui ui = Ui.INSTANCE;
            Intrinsics.checkNotNull($ctx);
            $glm.setSpanCount(RangesKt.coerceIn((r - l) / ui.dp($ctx, 112.0f), 1, 6));
        }
    }

    static final Unit buildMarket$lambda$46(ToolboxPanel this$0, RobloxApi.AudioItem item) {
        Intrinsics.checkNotNullParameter(item, "item");
        this$0.insertAudio(item);
        return Unit.INSTANCE;
    }

    static final Unit buildMarket$lambda$48(Context $ctx, Activity $act, final Function0 then) {
        Intrinsics.checkNotNullParameter(then, "then");
        if (RobloxApi.INSTANCE.hasCredentials($ctx)) {
            then.invoke();
        } else {
            AccountUi.INSTANCE.signIn($act, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda54
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return ToolboxPanel.buildMarket$lambda$48$lambda$47(then);
                }
            });
        }
        return Unit.INSTANCE;
    }

    static final Unit buildMarket$lambda$48$lambda$47(Function0 $then) {
        $then.invoke();
        return Unit.INSTANCE;
    }

    private final void showCategory() {
        AudioBrowser audioBrowser;
        boolean audio = this.category == RobloxApi.Category.AUDIO;
        AudioBrowser audioBrowser2 = this.audioBrowser;
        if (audioBrowser2 != null) {
            audioBrowser2.setVisibility(audio ? 0 : 8);
        }
        View view = this.gridHost;
        if (view != null) {
            view.setVisibility(audio ? 8 : 0);
        }
        View view2 = this.priceRow;
        if (view2 != null) {
            view2.setVisibility(audio ? 8 : 0);
        }
        EditText editText = this.searchBox;
        if (editText != null) {
            editText.setHint(audio ? "Search audio" : "Search the Creator Store");
        }
        if (!audio) {
            AudioEngine.INSTANCE.stopPreview();
            newSearch();
        } else {
            if (StringsKt.isBlank(this.query) || (audioBrowser = this.audioBrowser) == null) {
                return;
            }
            audioBrowser.search(this.query);
        }
    }

    public final void showMarketplace(RobloxApi.Category cat) {
        Intrinsics.checkNotNullParameter(cat, "cat");
        this.tab = 0;
        refresh();
        if (this.category != cat) {
            this.category = cat;
            Function0<Unit> function0 = this.restyleCats;
            if (function0 != null) {
                function0.invoke();
            }
            showCategory();
        }
    }

    private final void insertAudio(RobloxApi.AudioItem item) {
        long id = item.getId();
        String title = item.getTitle();
        if (StringsKt.isBlank(title)) {
            title = item.getName();
        }
        insertSound(new RobloxApi.Item(id, title, item.getCreator(), item.getVerified(), "", 0, 0, false, false, 0L, 0, 0, 0, 0, 0, null, null, 0L, false, null, 1048064, null));
    }

    private final void newSearch() {
        this.searchGen++;
        this.items.clear();
        this.cursor = null;
        this.endReached = false;
        this.loading = false;
        this.adapter.notifyDataSetChanged();
        loadMore();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void loadMore() {
        if (this.loading || this.endReached) {
            return;
        }
        this.loading = true;
        final int gen = this.searchGen;
        final String q = this.query;
        final RobloxApi.Category cat = this.category;
        final String cur = this.cursor;
        final RobloxApi.Price pr = this.price;
        ProgressBar progressBar = this.progress;
        TextView textView = null;
        if (progressBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progress");
            progressBar = null;
        }
        progressBar.setVisibility(this.items.isEmpty() ? 0 : 8);
        TextView textView2 = this.status;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("status");
        } else {
            textView = textView2;
        }
        textView.setText("");
        this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                ToolboxPanel.loadMore$lambda$57(q, cat, pr, cur, ToolboxPanel.this, gen);
            }
        });
    }

    static final void loadMore$lambda$57(final String $q, RobloxApi.Category $cat, RobloxApi.Price $pr, String $cur, final ToolboxPanel this$0, final int $gen) {
        RobloxApi.Price price;
        RobloxApi.Page _page = null;
        LinkedHashMap _thumbs = null;
        try {
            try {
                try {
                    price = $pr;
                    _page = RobloxApi.searchPriced$default(RobloxApi.INSTANCE, $q, $cat, $pr, $cur, 0, 16, null);
                } catch (Exception e) {
                    try {
                        price = $pr;
                        if (price == RobloxApi.Price.ANY) {
                            throw e;
                        }
                        _page = RobloxApi.search$default(RobloxApi.INSTANCE, $q, $cat, $cur, 0, 8, null);
                    } catch (final Exception e2) {
                        this$0.post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda77
                            @Override // java.lang.Runnable
                            public final void run() {
                                ToolboxPanel.loadMore$lambda$57$lambda$56($gen, this$0, e2);
                            }
                        });
                        return;
                    }
                }
                final RobloxApi.Page page = _page;
                Iterable iterableChunked = CollectionsKt.chunked(page.getIds(), 30);
                Collection arrayList = new ArrayList();
                Iterator it = iterableChunked.iterator();
                while (it.hasNext()) {
                    CollectionsKt.addAll(arrayList, RobloxApi.INSTANCE.details((List) it.next()));
                }
                Collection arrayList2 = new ArrayList();
                for (Object obj : (List) arrayList) {
                    RobloxApi.Item item = (RobloxApi.Item) obj;
                    boolean zIsPaid = true;
                    switch (WhenMappings.$EnumSwitchMapping$0[price.ordinal()]) {
                        case 1:
                            if (item.isPaid()) {
                                zIsPaid = false;
                            }
                            break;
                        case 2:
                            zIsPaid = item.isPaid();
                            break;
                        default:
                            break;
                    }
                    if (zIsPaid) {
                        arrayList2.add(obj);
                    }
                }
                final List details = (List) arrayList2;
                try {
                    Iterable iterableChunked2 = CollectionsKt.chunked(page.getIds(), 50);
                    Collection arrayList3 = new ArrayList();
                    Iterator it2 = iterableChunked2.iterator();
                    while (it2.hasNext()) {
                        CollectionsKt.addAll(arrayList3, RobloxApi.INSTANCE.thumbnailUrls((List) it2.next(), "150x150").entrySet());
                    }
                    Iterable<Map.Entry> iterable = (List) arrayList3;
                    _thumbs = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterable, 10)), 16));
                    for (Map.Entry entry : iterable) {
                        Pair pair = TuplesKt.to(entry.getKey(), entry.getValue());
                        _thumbs.put(pair.getFirst(), pair.getSecond());
                    }
                } catch (Exception e3) {
                    _thumbs = new LinkedHashMap();
                }
                final LinkedHashMap thumbs = _thumbs;
                this$0.post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda76
                    @Override // java.lang.Runnable
                    public final void run() {
                        ToolboxPanel.loadMore$lambda$57$lambda$55($gen, this$0, thumbs, details, page, $q);
                    }
                });
            } catch (Exception e4) {
                Exception e = e4;
                this$0.post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda77
                    @Override // java.lang.Runnable
                    public final void run() {
                        ToolboxPanel.loadMore$lambda$57$lambda$56($gen, this$0, e);
                    }
                });
            }
        } catch (Exception e5) {
            Exception e = e5;
        }
    }

    static final void loadMore$lambda$57$lambda$55(int $gen, ToolboxPanel this$0, Map $thumbs, List $details, RobloxApi.Page $page, String $q) {
        boolean z;
        if ($gen != this$0.searchGen) {
            return;
        }
        HashMap<Long, String> map = this$0.thumbUrls;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator it = $thumbs.entrySet().iterator();
        while (true) {
            z = true;
            if (!it.hasNext()) {
                break;
            }
            Map.Entry entry = (Map.Entry) it.next();
            String str = (String) entry.getValue();
            Intrinsics.checkNotNull(str);
            if (str.length() > 0) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
        }
        map.putAll(linkedHashMap);
        int start = this$0.items.size();
        this$0.items.addAll($details);
        this$0.adapter.notifyItemRangeInserted(start, $details.size());
        this$0.cursor = $page.getNextCursor();
        if ($page.getNextCursor() != null && !$page.getIds().isEmpty()) {
            z = false;
        }
        this$0.endReached = z;
        this$0.loading = false;
        ProgressBar progressBar = this$0.progress;
        TextView textView = null;
        if (progressBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progress");
            progressBar = null;
        }
        progressBar.setVisibility(8);
        if (this$0.items.isEmpty()) {
            TextView textView2 = this$0.status;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("status");
            } else {
                textView = textView2;
            }
            textView.setText(StringsKt.isBlank($q) ? "Nothing here yet." : "No results for \"" + $q + "\".");
        }
    }

    static final void loadMore$lambda$57$lambda$56(int $gen, ToolboxPanel this$0, Exception $e) {
        if ($gen != this$0.searchGen) {
            return;
        }
        this$0.loading = false;
        ProgressBar progressBar = this$0.progress;
        TextView textView = null;
        if (progressBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("progress");
            progressBar = null;
        }
        progressBar.setVisibility(8);
        if (this$0.items.isEmpty()) {
            TextView textView2 = this$0.status;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("status");
            } else {
                textView = textView2;
            }
            String message = $e.getMessage();
            if (message == null) {
                message = "";
            }
            textView.setText("Couldn't reach the Creator Store.\n" + message + "\nCheck your internet connection.");
        }
    }

    static /* synthetic */ void insertAsset$default(ToolboxPanel toolboxPanel, RobloxApi.Item item, RobloxApi.Category category, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            category = toolboxPanel.category;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        toolboxPanel.insertAsset(item, category, z);
    }

    private final void insertAsset(final RobloxApi.Item item, final RobloxApi.Category cat, final boolean ownedCheck) {
        final Context ctx = getContext();
        Intrinsics.checkNotNull(ctx, "null cannot be cast to non-null type android.app.Activity");
        final Activity act = (Activity) ctx;
        switch (WhenMappings.$EnumSwitchMapping$1[cat.ordinal()]) {
            case 1:
                insertDecal(item);
                break;
            case 2:
                insertSound(item);
                break;
            case 3:
                insertMesh(item);
                break;
            case 4:
                if (item.isPaid() && !ownedCheck) {
                    paidDialog$default(this, item, cat, false, 4, null);
                } else {
                    Dialogs.INSTANCE.confirm(ctx, "Install plugin", "Install “" + item.getName() + "” by " + item.getCreator() + "?" + (item.getHasScripts() ? "\n\nPlugins run code in Studio; only install ones you trust." : ""), "Install", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda9
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return ToolboxPanel.insertAsset$lambda$59(ctx, act, ToolboxPanel.this, item);
                        }
                    });
                }
                break;
            default:
                if (!RobloxApi.INSTANCE.hasCredentials(ctx)) {
                    AccountUi.INSTANCE.signIn(act, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda10
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return ToolboxPanel.insertAsset$lambda$60(ToolboxPanel.this, item, cat, ownedCheck);
                        }
                    });
                } else if (item.isPaid() && !ownedCheck) {
                    paidDialog$default(this, item, cat, false, 4, null);
                } else {
                    this.studio.toast("Inserting " + item.getName() + "…");
                    this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda11
                        @Override // java.lang.Runnable
                        public final void run() {
                            ToolboxPanel.insertAsset$lambda$67(ctx, item, ToolboxPanel.this, cat, act, ownedCheck);
                        }
                    });
                }
                break;
        }
    }

    static final Unit insertAsset$lambda$59(Context $ctx, final Activity $act, final ToolboxPanel this$0, final RobloxApi.Item $item) {
        if (RobloxApi.INSTANCE.hasCredentials($ctx)) {
            AccountUi.INSTANCE.installPluginId($act, this$0.studio, this$0.actions, $item.getId(), $item.getName());
        } else {
            AccountUi.INSTANCE.signIn($act, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda47
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return ToolboxPanel.insertAsset$lambda$59$lambda$58($act, this$0, $item);
                }
            });
        }
        return Unit.INSTANCE;
    }

    static final Unit insertAsset$lambda$59$lambda$58(Activity $act, ToolboxPanel this$0, RobloxApi.Item $item) {
        AccountUi.INSTANCE.installPluginId($act, this$0.studio, this$0.actions, $item.getId(), $item.getName());
        return Unit.INSTANCE;
    }

    static final Unit insertAsset$lambda$60(ToolboxPanel this$0, RobloxApi.Item $item, RobloxApi.Category $cat, boolean $ownedCheck) {
        this$0.insertAsset($item, $cat, $ownedCheck);
        return Unit.INSTANCE;
    }

    static final void insertAsset$lambda$67(Context $ctx, final RobloxApi.Item $item, final ToolboxPanel this$0, final RobloxApi.Category $cat, final Activity $act, final boolean $ownedCheck) {
        try {
            final byte[] bytes = RobloxApi.downloadAsset$default(RobloxApi.INSTANCE, $ctx, $item.getId(), null, 4, null);
            final RbxDocument doc = RbxFile.INSTANCE.read(bytes);
            if (RbxFile.INSTANCE.isPlace(doc)) {
                throw new Exception("This asset is a place, not a model");
            }
            if ($item.getHasScripts()) {
                OutputLog.INSTANCE.warn($item.getName() + " contains scripts. Check them before playtesting, like you would in Studio.");
            }
            this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    this$0.insertDownloaded($item, bytes, doc);
                }
            });
        } catch (AuthRequiredException e) {
            this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    ToolboxPanel.insertAsset$lambda$67$lambda$63($item, this$0, $cat, e, $act);
                }
            });
        } catch (HttpException e2) {
            if (e2.getCode() != 403) {
                if (e2.getCode() != 401) {
                    OutputLog.INSTANCE.error("Couldn't insert " + $item.getName() + ": " + e2.getMessage());
                    this$0.studio.toast("Insert failed: " + e2.getMessage());
                    return;
                }
                this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda24
                    @Override // java.lang.Runnable
                    public final void run() {
                        AccountUi.INSTANCE.signIn($act, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda31
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return ToolboxPanel.insertAsset$lambda$67$lambda$66$lambda$65(this$0, $item, $cat, $ownedCheck);
                            }
                        });
                    }
                });
                return;
            }
            this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    this$0.paidDialog($item, $cat, true);
                }
            });
        } catch (Exception e3) {
            OutputLog.INSTANCE.error("Couldn't insert " + $item.getName() + ": " + e3.getMessage());
            this$0.studio.toast("Insert failed: " + e3.getMessage());
        }
    }

    static final void insertAsset$lambda$67$lambda$63(final RobloxApi.Item $item, final ToolboxPanel this$0, final RobloxApi.Category $cat, AuthRequiredException $e, Activity $act) {
        if (!$item.isPaid()) {
            Studio studio = this$0.studio;
            String message = $e.getMessage();
            if (message == null) {
                message = "Sign in needed";
            }
            studio.toast(message);
            AccountUi.INSTANCE.signIn($act, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda65
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return ToolboxPanel.insertAsset$lambda$67$lambda$63$lambda$62(this$0, $item, $cat);
                }
            });
            return;
        }
        this$0.paidDialog($item, $cat, true);
    }

    static final Unit insertAsset$lambda$67$lambda$63$lambda$62(ToolboxPanel this$0, RobloxApi.Item $item, RobloxApi.Category $cat) {
        insertAsset$default(this$0, $item, $cat, false, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit insertAsset$lambda$67$lambda$66$lambda$65(ToolboxPanel this$0, RobloxApi.Item $item, RobloxApi.Category $cat, boolean $ownedCheck) {
        this$0.insertAsset($item, $cat, $ownedCheck);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void openInBrowser(String url) {
        try {
            getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        } catch (Exception e) {
            this.studio.toast("No browser found to open " + url);
        }
    }

    static /* synthetic */ void paidDialog$default(ToolboxPanel toolboxPanel, RobloxApi.Item item, RobloxApi.Category category, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        toolboxPanel.paidDialog(item, category, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void paidDialog(final RobloxApi.Item item, final RobloxApi.Category cat, final boolean refused) {
        String str;
        StringBuilder sb = new StringBuilder();
        if (item.isPaid()) {
            sb.append("“" + item.getName() + "” costs " + item.getPriceText() + " on the Creator Store, so it can't be loaded until it's bought on Roblox with the account you're signed in with.");
        } else {
            sb.append("Roblox didn't let your account download “" + item.getName() + "”. It may be a paid or private asset.");
        }
        if (refused) {
            sb.append("\n\nRoblox refused the download for the signed-in account.");
        }
        sb.append("\n\nAfter buying it, tap Insert again (or find it in Inventory → My Roblox items).");
        String msg = sb.toString();
        Dialogs dialogs = Dialogs.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        AlertDialog.Builder message = dialogs.themed(context).setTitle(item.isPaid() ? "Can't load paid model" : "Can't load this asset").setMessage(msg);
        if (item.isPaid()) {
            str = "Buy on Roblox (" + item.getPriceText() + ")";
        } else {
            str = "Open on Roblox";
        }
        message.setPositiveButton(str, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ToolboxPanel.this.openInBrowser(RobloxApi.INSTANCE.storeUrl(item.getId()));
            }
        }).setNeutralButton(refused ? "Close" : "I own it, insert", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ToolboxPanel.paidDialog$lambda$70(refused, ToolboxPanel.this, item, cat, dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    static final void paidDialog$lambda$70(boolean $refused, ToolboxPanel this$0, RobloxApi.Item $item, RobloxApi.Category $cat, DialogInterface dialogInterface, int i) {
        if ($refused) {
            return;
        }
        this$0.insertAsset($item, $cat, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void insertDownloaded(RobloxApi.Item item, byte[] bytes, RbxDocument doc) {
        Studio.insertDocument$default(this.studio, doc, null, 2, null);
        OutputLog.INSTANCE.info("Inserted " + item.getName() + " (" + item.getId() + ") from Roblox");
        List<String> warns = doc.getWarnings();
        if (warns != null && !warns.isEmpty()) {
            int limit = Math.min(20, warns.size());
            for (int wi = 0; wi < limit; wi++) {
                OutputLog.INSTANCE.warn(warns.get(wi));
            }
            if (warns.size() > 20) {
                OutputLog.INSTANCE.warn("…and " + (warns.size() - 20) + " more warnings");
            }
        }
        try {
            new File(getLibraryDir(), "recent").mkdirs();
            File rf = new File(new File(getLibraryDir(), "recent"), safeName(item.getName()) + ".rbxm");
            FilesKt.writeBytes(rf, RbxFile.INSTANCE.unwrap(bytes));
            Instance instance = (Instance) CollectionsKt.firstOrNull((List) doc.getRoots());
            if (instance != null) {
                getMeta().edit().putString("recent/" + rf.getName(), instance.getClassName()).apply();
            }
        } catch (Exception e) {
        }
    }

    private final void insertDecal(RobloxApi.Item item) {
        Object next;
        Instance instanceInsertObject$default;
        synchronized (this.studio.getLock()) {
            Iterator it = this.studio.getSelection().iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!((Instance) next).isA("BasePart"));
            Instance instance = (Instance) next;
            if (instance == null) {
                instanceInsertObject$default = Studio.insertObject$default(this.studio, "Part", null, 2, null);
                Instance.set$default(instanceInsertObject$default, "size", new Vector3(6.0f, 6.0f, 0.4f), false, 4, null);
                Instance.set$default(instanceInsertObject$default, "Anchored", true, false, 4, null);
            } else {
                instanceInsertObject$default = instance;
            }
            Instance instanceCreate = InstanceFactory.INSTANCE.create("Decal");
            String strTake = StringsKt.take(item.getName(), 40);
            if (StringsKt.isBlank(strTake)) {
                strTake = "Decal";
            }
            instanceCreate.setName(strTake);
            Instance.set$default(instanceCreate, "Texture", "rbxassetid://" + item.getId(), false, 4, null);
            Instance.set$default(instanceCreate, "Face", new EnumValue(5), false, 4, null);
            this.studio.getUndo().begin("Insert decal");
            this.studio.reparent(instanceCreate, instanceInsertObject$default);
            this.studio.getUndo().commit();
            Unit unit = Unit.INSTANCE;
        }
        this.studio.toast("Added decal " + item.getName());
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0083 A[Catch: all -> 0x00f7, TryCatch #0 {, blocks: (B:5:0x0008, B:8:0x0027, B:10:0x005b, B:12:0x0063, B:14:0x006f, B:22:0x008d, B:21:0x0083), top: B:33:0x0008 }] */
    private final void insertSound(RobloxApi.Item item) {
        synchronized (this.studio.getLock()) {
            Instance instanceCreate = InstanceFactory.INSTANCE.create("Sound");
            String strTake = StringsKt.take(item.getName(), 40);
            if (StringsKt.isBlank(strTake)) {
                strTake = "Sound";
            }
            instanceCreate.setName(strTake);
            Instance.set$default(instanceCreate, "SoundId", "rbxassetid://" + item.getId(), false, 4, null);
            Instance workspace = (Instance) CollectionsKt.firstOrNull((List) this.studio.getSelection());
            if (workspace == null) {
                workspace = this.studio.getModel().getWorkspace();
            } else {
                if (!(!workspace.getIsService() || Intrinsics.areEqual(workspace.getClassName(), "Workspace") || Intrinsics.areEqual(workspace.getClassName(), "SoundService"))) {
                    workspace = null;
                }
                if (workspace == null) {
                    workspace = this.studio.getModel().getWorkspace();
                }
            }
            this.studio.getUndo().begin("Insert sound");
            this.studio.reparent(instanceCreate, workspace);
            this.studio.getUndo().commit();
            this.studio.getSelection().clear();
            this.studio.getSelection().add(instanceCreate);
        }
        Iterator it = this.studio.getListeners().iterator();
        while (it.hasNext()) {
            ((Studio.Listener) it.next()).onSelectionChanged();
        }
        this.studio.toast("Added sound " + item.getName());
    }

    private final void insertMesh(final RobloxApi.Item item) {
        Context ctx = getContext();
        Intrinsics.checkNotNull(ctx, "null cannot be cast to non-null type android.app.Activity");
        final Activity act = (Activity) ctx;
        if (!RobloxApi.INSTANCE.hasCredentials(ctx)) {
            AccountUi.INSTANCE.signIn(act, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda28
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return ToolboxPanel.insertMesh$lambda$80(ToolboxPanel.this, item);
                }
            });
            return;
        }
        this.studio.toast("Inserting " + item.getName() + "…");
        this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda29
            @Override // java.lang.Runnable
            public final void run() {
                ToolboxPanel.insertMesh$lambda$89(item, ToolboxPanel.this, act);
            }
        });
    }

    static final Unit insertMesh$lambda$80(ToolboxPanel this$0, RobloxApi.Item $item) {
        this$0.insertMesh($item);
        return Unit.INSTANCE;
    }

    static final void insertMesh$lambda$89(final RobloxApi.Item $item, final ToolboxPanel this$0, final Activity $act) {
        try {
            try {
                final byte[] bytes = AssetFetcher.INSTANCE.load($item.getId());
                RbxDocument docTmp = null;
                try {
                    RbxDocument rbxDocument = RbxFile.INSTANCE.read(bytes);
                    if (!rbxDocument.getRoots().isEmpty()) {
                        docTmp = rbxDocument;
                    }
                } catch (Exception e) {
                }
                final RbxDocument doc = docTmp;
                if (doc != null) {
                    this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda25
                        @Override // java.lang.Runnable
                        public final void run() {
                            this$0.insertDownloaded($item, bytes, doc);
                        }
                    });
                    return;
                }
                MeshData m = RbxMesh.INSTANCE.parse(bytes);
                Vector3 sTmp = new Vector3(m.size(0), m.size(1), m.size(2));
                if (sTmp.getX() <= 0.01f || sTmp.getY() <= 0.01f || sTmp.getZ() <= 0.01f) {
                    sTmp = new Vector3(4.0f, 4.0f, 4.0f);
                }
                final Vector3 s = sTmp;
                this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda26
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            ToolboxPanel.insertMesh$lambda$89$lambda$86(this$0, $item, s);
                        } catch (Throwable _t) {
                            if (_t instanceof RuntimeException) throw (RuntimeException) _t;
                            if (_t instanceof Error) throw (Error) _t;
                            throw new RuntimeException(_t);
                        }
                    }
                });
            } catch (Exception e2) {
                if (e2 instanceof AuthRequiredException) {
                    final AuthRequiredException _authEx = (AuthRequiredException) e2;
                    this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda27
                        @Override // java.lang.Runnable
                        public final void run() {
                            ToolboxPanel.insertMesh$lambda$89$lambda$88(this$0, _authEx, $act, $item);
                        }
                    });
                } else {
                    throw e2;
                }
            }
        } catch (Exception e3) {
            OutputLog outputLog = OutputLog.INSTANCE;
            String name = $item.getName();
            String message = e3.getMessage();
            outputLog.error("Couldn't insert mesh " + name + ": " + (message != null ? message : e3));
            this$0.studio.toast("Insert failed: " + e3.getMessage());
        }
    }

    static final void insertMesh$lambda$89$lambda$86(ToolboxPanel this$0, RobloxApi.Item $item, Vector3 $s) {
        try {
        Instance p = Studio.insertObject$default(this$0.studio, "MeshPart", null, 2, null);
        synchronized (this$0.studio.getLock()) {
            try {
                Vector3 size = PartData.INSTANCE.size(p);
                String strTake = StringsKt.take($item.getName(), 40);
                if (StringsKt.isBlank(strTake)) {
                    strTake = "MeshPart";
                }
                p.setName(strTake);
                Instance.set$default(p, "MeshId", "rbxassetid://" + $item.getId(), false, 4, null);
                try {
                    Instance.set$default(p, "size", $s, false, 4, null);
                    p.set("InitialSize", $s, false);
                    Instance.set$default(p, "Anchored", true, false, 4, null);
                    Instance.set$default(p, "CFrame", PartData.INSTANCE.cframe(p).plus(new Vector3(0.0f, ($s.getY() - size.getY()) / 2.0f, 0.0f)), false, 4, null);
                    Unit unit = Unit.INSTANCE;
                    OutputLog.INSTANCE.info("Inserted mesh " + $item.getName() + " (" + $item.getId() + ")");
                } catch (Throwable th) {
                    throw th;
                }
            } catch (Throwable th2) {
                Throwable th = th2;
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final void insertMesh$lambda$89$lambda$88(final ToolboxPanel this$0, AuthRequiredException $e, Activity $act, final RobloxApi.Item $item) {
        Studio studio = this$0.studio;
        String message = $e.getMessage();
        if (message == null) {
            message = "Sign in needed";
        }
        studio.toast(message);
        AccountUi.INSTANCE.signIn($act, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.insertMesh$lambda$89$lambda$88$lambda$87(this$0, $item);
            }
        });
    }

    static final Unit insertMesh$lambda$89$lambda$88$lambda$87(ToolboxPanel this$0, RobloxApi.Item $item) {
        this$0.insertMesh($item);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void bindImage(ImageView v, String key) {
        ArrayList arrayList;
        Bitmap b = ImageLoader.INSTANCE.getKey(key);
        if (b != null) {
            v.setImageBitmap(b);
            return;
        }
        Map map = this.invThumbViews;
        List<ImageView> list = (List<ImageView>) map.get(key);
        if (list == null) {
            arrayList = new ArrayList();
            map.put(key, arrayList);
        } else {
            arrayList = (ArrayList) list;
        }
        arrayList.add(v);
    }

    private final String fmtDate(String iso) {
        String strTake = StringsKt.take(iso, 10);
        if (StringsKt.isBlank(strTake)) {
            strTake = "—";
        }
        return strTake;
    }

    private final String fmtCount(int n) {
        if (n < 1000000) {
            if (n < 1000) {
                return String.valueOf(n);
            }
            String str = String.format("%.1fK", Arrays.copyOf(new Object[]{Float.valueOf(n / 1000.0f)}, 1));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            return str;
        }
        String str2 = String.format("%.1fM", Arrays.copyOf(new Object[]{Float.valueOf(n / 1000000.0f)}, 1));
        Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void showDetails$default(ToolboxPanel toolboxPanel, RobloxApi.Item item, RobloxApi.Category category, int i, Object obj) {
        if ((i & 2) != 0) {
            category = toolboxPanel.category;
        }
        toolboxPanel.showDetails(item, category);
    }

    private final void showDetails(final RobloxApi.Item item, final RobloxApi.Category cat) {
        LinearLayout box;
        Context ctx;
        CharSequence charSequence;
        String str;
        String insertLabel;
        String str2;
        final Context ctx2 = getContext();
        LinearLayout box2 = new LinearLayout(ctx2);
        box2.setOrientation(1);
        Ui ui = Ui.INSTANCE;
        Intrinsics.checkNotNull(ctx2);
        box2.setPadding(ui.dp(ctx2, 16.0f), Ui.INSTANCE.dp(ctx2, 8.0f), Ui.INSTANCE.dp(ctx2, 16.0f), Ui.INSTANCE.dp(ctx2, 8.0f));
        final ImageView pic = new ImageView(ctx2);
        pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        pic.setBackgroundColor(Ui.INSTANCE.blend(Ui.INSTANCE.getPANEL(), ViewCompat.MEASURED_STATE_MASK, 0.1f));
        box2.addView(pic, Ui.lp$default(Ui.INSTANCE, -1, Ui.INSTANCE.dp(ctx2, 200.0f), 0.0f, 4, null));
        bindImage(pic, "asset:" + item.getId());
        final LinearLayout linearLayout = new LinearLayout(ctx2);
        linearLayout.setOrientation(0);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(ctx2);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.addView(linearLayout);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default.topMargin = Ui.INSTANCE.dp(ctx2, 4.0f);
        Unit unit = Unit.INSTANCE;
        box2.addView(horizontalScrollView, layoutParamsLp$default);
        this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda78
            @Override // java.lang.Runnable
            public final void run() {
                ToolboxPanel.showDetails$lambda$101(item, ToolboxPanel.this, ctx2, linearLayout, pic);
            }
        });
        showDetails$line(box2, ctx2, item.getName(), 16.0f, Ui.INSTANCE.getTEXT(), true);
        showDetails$line$default(box2, ctx2, "By " + item.getCreator() + (item.getVerified() ? "  ✔ verified" : "") + (item.getCreatorIsGroup() ? " (group)" : ""), 12.0f, item.getVerified() ? Ui.INFO : Ui.INSTANCE.getDIM(), false, 32, null);
        showDetails$line(box2, ctx2, item.isPaid() ? "Price: " + item.getPriceText() + " (buy it on Roblox to use it)" : "Price: Free", 13.0f, item.isPaid() ? -18611 : -8595844, true);
        int votes = item.getUpVotes() + item.getDownVotes();
        showDetails$line(box2, ctx2, "Ratings", 11.0f, Ui.INSTANCE.getDIM(), true);
        if (votes > 0) {
            int pct = item.getLikePercent();
            LinearLayout bar = new LinearLayout(ctx2);
            bar.setOrientation(0);
            View view = new View(ctx2);
            view.setBackgroundColor(-11751600);
            bar.addView(view, Ui.INSTANCE.lp(0, Ui.INSTANCE.dp(ctx2, 8.0f), RangesKt.coerceAtLeast(pct, 0.01f)));
            View view2 = new View(ctx2);
            view2.setBackgroundColor(-2075307);
            bar.addView(view2, Ui.INSTANCE.lp(0, Ui.INSTANCE.dp(ctx2, 8.0f), RangesKt.coerceAtLeast(100 - pct, 0.01f)));
            LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default2.topMargin = Ui.INSTANCE.dp(ctx2, 2.0f);
            Unit unit2 = Unit.INSTANCE;
            box2.addView(bar, layoutParamsLp$default2);
            box = box2;
            ctx = ctx2;
            showDetails$line$default(box, ctx, pct + "% liked  ·  👍 " + fmtCount(item.getUpVotes()) + "  👎 " + fmtCount(item.getDownVotes()) + "  ·  " + fmtCount(votes) + " ratings", 0.0f, 0, false, 56, null);
        } else {
            box = box2;
            ctx = ctx2;
            showDetails$line$default(box, ctx, "No ratings yet", 12.0f, Ui.INSTANCE.getDIM(), false, 32, null);
        }
        if (item.getEndorsed()) {
            showDetails$line$default(box, ctx, "★ Endorsed by Roblox", 12.0f, -10929, false, 32, null);
        }
        if (StringsKt.isBlank(item.getDescription())) {
            charSequence = "  ·  ";
        } else {
            charSequence = "  ·  ";
            showDetails$line(box, ctx, "Description", 11.0f, Ui.INSTANCE.getDIM(), true);
            showDetails$line$default(box, ctx, StringsKt.take(item.getDescription(), 2000), 0.0f, 0, false, 56, null);
        }
        LinearLayout box3 = box;
        showDetails$line(box3, ctx, "Details", 11.0f, Ui.INSTANCE.getDIM(), true);
        ArrayList facts = new ArrayList();
        if (item.getHasScripts()) {
            if (item.getScriptCount() > 0) {
                str2 = item.getScriptCount() + " script" + (item.getScriptCount() != 1 ? "s" : "");
            } else {
                str2 = "scripts";
            }
            str = "⚠ Contains " + str2;
        } else {
            str = "No scripts";
        }
        facts.add(str);
        if (item.getMeshParts() > 0) {
            facts.add(item.getMeshParts() + " MeshParts");
        }
        if (item.getTriangles() > 0) {
            facts.add(fmtCount(item.getTriangles()) + " triangles");
        }
        if (item.getAnimations() > 0) {
            facts.add(item.getAnimations() + " animations");
        }
        if (item.getSounds() > 0) {
            facts.add(item.getSounds() + " sounds");
        }
        showDetails$line$default(box3, ctx, CollectionsKt.joinToString(facts, charSequence, "", "", -1, "...", null), 0.0f, 0, false, 56, null);
        if (!StringsKt.isBlank(item.getCategory())) {
            showDetails$line$default(box3, ctx, "Category: " + CollectionsKt.joinToString(StringsKt.split((CharSequence) item.getCategory(), new String[]{"__"}, false, 0), " › ", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda79
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return ToolboxPanel.showDetails$lambda$108((String) obj);
                }
            }), 12.0f, Ui.INSTANCE.getDIM(), false, 32, null);
        }
        showDetails$line$default(box3, ctx, "Created " + fmtDate(item.getCreated()) + "  ·  Updated " + fmtDate(item.getUpdated()), 12.0f, Ui.INSTANCE.getDIM(), false, 32, null);
        showDetails$line$default(box3, ctx, "Asset ID " + item.getId(), 12.0f, Ui.INSTANCE.getDIM(), false, 32, null);
        ScrollView scrollView = new ScrollView(ctx);
        scrollView.addView(box3);
        if (item.isPaid()) {
            insertLabel = "Buy (" + item.getPriceText() + ")";
        } else {
            insertLabel = cat == RobloxApi.Category.PLUGINS ? "Install" : "Insert";
        }
        Dialogs.INSTANCE.themed(ctx).setView(scrollView).setPositiveButton(insertLabel, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda80
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ToolboxPanel.showDetails$lambda$110(item, ToolboxPanel.this, cat, dialogInterface, i);
            }
        }).setNeutralButton("Open in browser", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda81
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ToolboxPanel.this.openInBrowser(RobloxApi.INSTANCE.storeUrl(item.getId()));
            }
        }).setNegativeButton("Close", (DialogInterface.OnClickListener) null).show();
    }

    static final void showDetails$lambda$101(RobloxApi.Item $item, final ToolboxPanel this$0, final Context $ctx, final LinearLayout $shots, final ImageView $pic) {
        final List<Long> listPreviewImages = RobloxApi.INSTANCE.previewImages($item.getId());
        if (!listPreviewImages.isEmpty()) {
            this$0.post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    ToolboxPanel.showDetails$lambda$101$lambda$100(listPreviewImages, $ctx, $shots, this$0, $pic);
                }
            });
        }
    }

    static final void showDetails$lambda$101$lambda$100(List $ids, Context $ctx, LinearLayout $shots, final ToolboxPanel this$0, final ImageView $pic) {
        Iterator it = CollectionsKt.take($ids, 8).iterator();
        while (it.hasNext()) {
            final long id = ((Number) it.next()).longValue();
            ImageView iv = new ImageView($ctx);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda48
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this$0.bindImage($pic, "asset:" + id);
                }
            });
            Ui ui = Ui.INSTANCE;
            Ui ui2 = Ui.INSTANCE;
            Intrinsics.checkNotNull($ctx);
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(ui, ui2.dp($ctx, 64.0f), Ui.INSTANCE.dp($ctx, 64.0f), 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = Ui.INSTANCE.dp($ctx, 4.0f);
            Unit unit = Unit.INSTANCE;
            $shots.addView(iv, layoutParamsLp$default);
            this$0.bindImage(iv, "asset:" + id);
        }
    }

    static /* synthetic */ void showDetails$line$default(LinearLayout linearLayout, Context context, String str, float f, int i, boolean z, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            f = 12.0f;
        }
        float f2 = f;
        if ((i2 & 16) != 0) {
            i = Ui.INSTANCE.getTEXT();
        }
        int i3 = i;
        if ((i2 & 32) != 0) {
            z = false;
        }
        showDetails$line(linearLayout, context, str, f2, i3, z);
    }

    private static final void showDetails$line(LinearLayout box, Context ctx, String t, float size, int color, boolean bold) {
        Ui ui = Ui.INSTANCE;
        Intrinsics.checkNotNull(ctx);
        TextView textViewText = ui.text(ctx, t, size, color, bold);
        textViewText.setSingleLine(false);
        textViewText.setPadding(0, Ui.INSTANCE.dp(ctx, 3.0f), 0, Ui.INSTANCE.dp(ctx, 1.0f));
        box.addView(textViewText);
    }

    static final CharSequence showDetails$lambda$108(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String it2 = StringsKt.replace(it, '_', ' ', false);
        if (it2.length() > 0) {
            StringBuilder sb = new StringBuilder();
            String strValueOf = String.valueOf(it2.charAt(0));
            Intrinsics.checkNotNull(strValueOf, "null cannot be cast to non-null type java.lang.String");
            String upperCase = strValueOf.toUpperCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
            StringBuilder sbAppend = sb.append((Object) upperCase);
            String strSubstring = it2.substring(1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            it2 = sbAppend.append(strSubstring).toString();
        }
        return it2;
    }

    static final void showDetails$lambda$110(RobloxApi.Item $item, ToolboxPanel this$0, RobloxApi.Category $cat, DialogInterface dialogInterface, int i) {
        if ($item.isPaid()) {
            paidDialog$default(this$0, $item, $cat, false, 4, null);
        } else {
            insertAsset$default(this$0, $item, $cat, false, 4, null);
        }
    }

    /* JADX INFO: compiled from: ToolboxPanel.kt */
    private final class VH extends RecyclerView.ViewHolder {
        private final TextView badge;
        private final TextView by;
        private final ImageView img;
        private final View info;
        private final TextView name;
        private final LinearLayout root;
        final /* synthetic */ ToolboxPanel this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public VH(ToolboxPanel this$0, LinearLayout root, ImageView img, TextView name, TextView by, TextView badge, View info) {
            super(root);
            Intrinsics.checkNotNullParameter(root, "root");
            Intrinsics.checkNotNullParameter(img, "img");
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(by, "by");
            Intrinsics.checkNotNullParameter(badge, "badge");
            Intrinsics.checkNotNullParameter(info, "info");
            this.this$0 = this$0;
            this.root = root;
            this.img = img;
            this.name = name;
            this.by = by;
            this.badge = badge;
            this.info = info;
        }

        public final TextView getBadge() {
            return this.badge;
        }

        public final TextView getBy() {
            return this.by;
        }

        public final ImageView getImg() {
            return this.img;
        }

        public final View getInfo() {
            return this.info;
        }

        public final TextView getName() {
            return this.name;
        }

        public final LinearLayout getRoot() {
            return this.root;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: ToolboxPanel.kt */
    final class MarketAdapter extends RecyclerView.Adapter<VH> {
        public MarketAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ToolboxPanel.this.items.size();
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [com.goydevv.studrblx.ui.ToolboxPanel$MarketAdapter$onCreateViewHolder$img$1] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            Intrinsics.checkNotNullParameter(parent, "parent");
            final Context ctx = parent.getContext();
            LinearLayout root = new LinearLayout(ctx);
            root.setOrientation(1);
            Ui ui = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            root.setPadding(ui.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 6.0f));
            root.setBackground(Ui.INSTANCE.rippleBg(ctx, Ui.INSTANCE.getCARD(), 4.0f));
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-1, -2);
            layoutParams.setMargins(Ui.INSTANCE.dp(ctx, 3.0f), Ui.INSTANCE.dp(ctx, 3.0f), Ui.INSTANCE.dp(ctx, 3.0f), Ui.INSTANCE.dp(ctx, 3.0f));
            root.setLayoutParams(layoutParams);
            ImageView r2 = new ImageView(ctx) { // from class: com.goydevv.studrblx.ui.ToolboxPanel$MarketAdapter$onCreateViewHolder$img$1
                @Override // android.widget.ImageView, android.view.View
                protected void onMeasure(int w, int h) {
                    super.onMeasure(w, w);
                }
            };
            r2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            r2.setBackgroundColor(Ui.INSTANCE.blend(Ui.INSTANCE.getPANEL(), ViewCompat.MEASURED_STATE_MASK, 0.08f));
            FrameLayout frame = new FrameLayout(ctx);
            frame.addView((View) r2, new FrameLayout.LayoutParams(-1, -2));
            TextView textViewText = Ui.INSTANCE.text(ctx, "", 10.0f, -15000805, true);
            textViewText.setPadding(Ui.INSTANCE.dp(ctx, 5.0f), Ui.INSTANCE.dp(ctx, 1.0f), Ui.INSTANCE.dp(ctx, 5.0f), Ui.INSTANCE.dp(ctx, 1.0f));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(-18611);
            gradientDrawable.setCornerRadius(Ui.INSTANCE.dp(ctx, 4.0f));
            textViewText.setBackground(gradientDrawable);
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2, 8388659);
            layoutParams2.setMargins(Ui.INSTANCE.dp(ctx, 3.0f), Ui.INSTANCE.dp(ctx, 3.0f), 0, 0);
            Unit unit = Unit.INSTANCE;
            frame.addView(textViewText, layoutParams2);
            TextView textViewText2 = Ui.INSTANCE.text(ctx, "ⓘ", 15.0f, -1, true);
            textViewText2.setGravity(17);
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setShape(1);
            gradientDrawable2.setColor(-1728053248);
            textViewText2.setBackground(gradientDrawable2);
            textViewText2.setContentDescription("Details");
            FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(Ui.INSTANCE.dp(ctx, 26.0f), Ui.INSTANCE.dp(ctx, 26.0f), 8388661);
            layoutParams3.setMargins(0, Ui.INSTANCE.dp(ctx, 2.0f), Ui.INSTANCE.dp(ctx, 2.0f), 0);
            Unit unit2 = Unit.INSTANCE;
            frame.addView(textViewText2, layoutParams3);
            root.addView(frame, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, "", 12.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
            textViewText$default.setMaxLines(2);
            textViewText$default.setSingleLine(false);
            textViewText$default.setEllipsize(TextUtils.TruncateAt.END);
            textViewText$default.setPadding(0, Ui.INSTANCE.dp(ctx, 4.0f), 0, 0);
            TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, ctx, "", 10.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default2.setEllipsize(TextUtils.TruncateAt.END);
            root.addView(textViewText$default);
            root.addView(textViewText$default2);
            return new VH(ToolboxPanel.this, root, (ImageView) r2, textViewText$default, textViewText$default2, textViewText, textViewText2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(VH h, int position) {
            Intrinsics.checkNotNullParameter(h, "h");
            Object obj = ToolboxPanel.this.items.get(position);
            Intrinsics.checkNotNullExpressionValue(obj, "get(...)");
            final RobloxApi.Item it = (RobloxApi.Item) obj;
            h.getName().setText(it.getName());
            int pct = it.getLikePercent();
            h.getBy().setText(it.getCreator() + (pct >= 0 ? "  ·  👍 " + pct + "%" : ""));
            h.getBy().setTextColor(it.getVerified() ? Ui.INFO : Ui.INSTANCE.getDIM());
            h.getBadge().setVisibility(it.isPaid() ? 0 : 8);
            h.getBadge().setText(it.getPriceText());
            h.getImg().setAlpha(it.isPaid() ? 0.75f : 1.0f);
            String url = (String) ToolboxPanel.this.thumbUrls.get(Long.valueOf(it.getId()));
            Bitmap bmp = url != null ? ImageLoader.INSTANCE.getKey("url:" + url) : null;
            if (bmp != null) {
                h.getImg().setImageBitmap(bmp);
            } else {
                h.getImg().setImageDrawable(null);
            }
            LinearLayout root = h.getRoot();
            final ToolboxPanel toolboxPanel = ToolboxPanel.this;
            root.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$MarketAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ToolboxPanel.MarketAdapter.onBindViewHolder$lambda$12(it, toolboxPanel, view);
                }
            });
            LinearLayout root2 = h.getRoot();
            final ToolboxPanel toolboxPanel2 = ToolboxPanel.this;
            root2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$MarketAdapter$$ExternalSyntheticLambda1
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return ToolboxPanel.MarketAdapter.onBindViewHolder$lambda$13(toolboxPanel2, it, view);
                }
            });
            View info = h.getInfo();
            final ToolboxPanel toolboxPanel3 = ToolboxPanel.this;
            info.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$MarketAdapter$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ToolboxPanel.showDetails$default(toolboxPanel3, it, null, 2, null);
                }
            });
        }

        static final void onBindViewHolder$lambda$12(RobloxApi.Item $it, ToolboxPanel this$0, View view) {
            if ($it.isPaid()) {
                ToolboxPanel.paidDialog$default(this$0, $it, this$0.category, false, 4, null);
            } else {
                ToolboxPanel.insertAsset$default(this$0, $it, null, false, 6, null);
            }
        }

        static final boolean onBindViewHolder$lambda$13(ToolboxPanel this$0, RobloxApi.Item $it, View view) {
            ToolboxPanel.showDetails$default(this$0, $it, null, 2, null);
            return true;
        }
    }

    private final void grid(LinearLayout into, List<? extends Triple<? extends Drawable, String, ? extends Function0<Unit>>> cells) {
        GridLayout g = new GridLayout(getContext());
        g.setColumnCount(3);
        Ui ui = Ui.INSTANCE;
        Context context = g.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        int iDp = ui.dp(context, 4.0f);
        Ui ui2 = Ui.INSTANCE;
        Context context2 = g.getContext();
        Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
        int iDp2 = ui2.dp(context2, 4.0f);
        Ui ui3 = Ui.INSTANCE;
        Context context3 = g.getContext();
        Intrinsics.checkNotNullExpressionValue(context3, "getContext(...)");
        g.setPadding(iDp, iDp2, ui3.dp(context3, 4.0f), 0);
        Ui ui4 = Ui.INSTANCE;
        Context context4 = getContext();
        Intrinsics.checkNotNullExpressionValue(context4, "getContext(...)");
        int w = ui4.dp(context4, 74.0f);
        Iterator<? extends Triple<? extends Drawable, String, ? extends Function0<Unit>>> it = cells.iterator();
        while (it.hasNext()) {
            Triple<? extends Drawable, String, ? extends Function0<Unit>> next = it.next();
            Drawable icon = next.component1();
            String label = next.component2();
            final Function0<Unit> function0Component3 = next.component3();
            Ui ui5 = Ui.INSTANCE;
            Context context5 = getContext();
            Intrinsics.checkNotNullExpressionValue(context5, "getContext(...)");
            LinearLayout cell = ui5.toolButton(context5, icon, label, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda64
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return ToolboxPanel.grid$lambda$113(function0Component3, (View) obj);
                }
            });
            Ui ui6 = Ui.INSTANCE;
            Context context6 = getContext();
            Intrinsics.checkNotNullExpressionValue(context6, "getContext(...)");
            cell.setBackground(Ui.rippleBg$default(ui6, context6, Ui.INSTANCE.getHOVER(), 0.0f, 4, null));
            View childAt = cell.getChildAt(1);
            Intrinsics.checkNotNull(childAt, "null cannot be cast to non-null type android.widget.TextView");
            TextView textView = (TextView) childAt;
            textView.setTextSize(10.0f);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = w;
            Ui ui7 = Ui.INSTANCE;
            Context context7 = getContext();
            Intrinsics.checkNotNullExpressionValue(context7, "getContext(...)");
            layoutParams.height = ui7.dp(context7, 62.0f);
            Ui ui8 = Ui.INSTANCE;
            Context context8 = getContext();
            Intrinsics.checkNotNullExpressionValue(context8, "getContext(...)");
            int iDp3 = ui8.dp(context8, 3.0f);
            Ui ui9 = Ui.INSTANCE;
            Context context9 = getContext();
            Intrinsics.checkNotNullExpressionValue(context9, "getContext(...)");
            int iDp4 = ui9.dp(context9, 3.0f);
            Ui ui10 = Ui.INSTANCE;
            int w2 = w;
            Context context10 = getContext();
            Intrinsics.checkNotNullExpressionValue(context10, "getContext(...)");
            int iDp5 = ui10.dp(context10, 3.0f);
            Ui ui11 = Ui.INSTANCE;
            Iterator<? extends Triple<? extends Drawable, String, ? extends Function0<Unit>>> it2 = it;
            Context context11 = getContext();
            Intrinsics.checkNotNullExpressionValue(context11, "getContext(...)");
            layoutParams.setMargins(iDp3, iDp4, iDp5, ui11.dp(context11, 3.0f));
            Unit unit = Unit.INSTANCE;
            g.addView(cell, layoutParams);
            w = w2;
            it = it2;
        }
        into.addView(g);
    }

    static final Unit grid$lambda$113(Function0 $fn, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $fn.invoke();
        return Unit.INSTANCE;
    }

    private final void section(LinearLayout into, String title) {
        Ui ui = Ui.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        TextView textViewText = ui.text(context, title, 11.0f, Ui.INSTANCE.getDIM(), true);
        Ui ui2 = Ui.INSTANCE;
        Context context2 = textViewText.getContext();
        Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
        int iDp = ui2.dp(context2, 10.0f);
        Ui ui3 = Ui.INSTANCE;
        Context context3 = textViewText.getContext();
        Intrinsics.checkNotNullExpressionValue(context3, "getContext(...)");
        int iDp2 = ui3.dp(context3, 10.0f);
        Ui ui4 = Ui.INSTANCE;
        Context context4 = textViewText.getContext();
        Intrinsics.checkNotNullExpressionValue(context4, "getContext(...)");
        textViewText.setPadding(iDp, iDp2, 0, ui4.dp(context4, 2.0f));
        into.addView(textViewText);
    }

    private final void parts(LinearLayout into) {
        Context c = getContext();
        section(into, "PARTS");
        Icons icons = Icons.INSTANCE;
        Intrinsics.checkNotNull(c);
        grid(into, (List) CollectionsKt.listOf(new Triple[]{new Triple(icons.general(c, "Part_Block", "Block"), "Block", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda66
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.parts$lambda$117(ToolboxPanel.this);
            }
        }), new Triple(Icons.INSTANCE.general(c, "Part_Sphere", "Sphere"), "Sphere", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda67
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.parts$lambda$118(ToolboxPanel.this);
            }
        }), new Triple(Icons.INSTANCE.general(c, "Part_Cylinder", "Cylinder"), "Cylinder", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda68
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.parts$lambda$119(ToolboxPanel.this);
            }
        }), new Triple(Icons.INSTANCE.general(c, "Part_Wedge", "Wedge"), "Wedge", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda69
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.parts$lambda$120(ToolboxPanel.this);
            }
        }), new Triple(Icons.INSTANCE.general(c, "Part_CornerWedge", "Corner"), "Corner", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda70
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.parts$lambda$121(ToolboxPanel.this);
            }
        }), new Triple(Icons.INSTANCE.instance(c, "SpawnLocation"), "Spawn", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda71
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.parts$lambda$122(ToolboxPanel.this);
            }
        }), new Triple(Icons.INSTANCE.instance(c, "Seat"), "Seat", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda72
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.parts$lambda$125(ToolboxPanel.this);
            }
        }), new Triple(Icons.INSTANCE.instance(c, "TrussPart"), "Truss", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda73
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.parts$lambda$128(ToolboxPanel.this);
            }
        })}));
        section(into, "SAMPLES");
        Iterable<Samples.Sample> all = Samples.INSTANCE.getAll();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(all, 10));
        for (final Samples.Sample sample : all) {
            arrayList.add(new Triple(Icons.INSTANCE.instance(c, sample.getIcon()), sample.getName(), new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda74
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return ToolboxPanel.parts$lambda$130$lambda$129(ToolboxPanel.this, sample);
                }
            }));
        }
        grid(into, (List) arrayList);
    }

    static final Unit parts$lambda$117(ToolboxPanel this$0) {
        this$0.studio.insertPart(1);
        return Unit.INSTANCE;
    }

    static final Unit parts$lambda$118(ToolboxPanel this$0) {
        this$0.studio.insertPart(0);
        return Unit.INSTANCE;
    }

    static final Unit parts$lambda$119(ToolboxPanel this$0) {
        this$0.studio.insertPart(2);
        return Unit.INSTANCE;
    }

    static final Unit parts$lambda$120(ToolboxPanel this$0) {
        this$0.studio.insertPart(3);
        return Unit.INSTANCE;
    }

    static final Unit parts$lambda$121(ToolboxPanel this$0) {
        this$0.studio.insertPart(4);
        return Unit.INSTANCE;
    }

    static final Unit parts$lambda$122(ToolboxPanel this$0) {
        Studio.insertObject$default(this$0.studio, "SpawnLocation", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit parts$lambda$125(ToolboxPanel this$0) {
        Instance instanceInsertObject$default = Studio.insertObject$default(this$0.studio, "Seat", null, 2, null);
        synchronized (this$0.studio.getLock()) {
            Instance.set$default(instanceInsertObject$default, "Anchored", true, false, 4, null);
            Unit unit = Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    static final Unit parts$lambda$128(ToolboxPanel this$0) {
        Instance instanceInsertObject$default = Studio.insertObject$default(this$0.studio, "TrussPart", null, 2, null);
        synchronized (this$0.studio.getLock()) {
            Instance.set$default(instanceInsertObject$default, "Anchored", true, false, 4, null);
            Unit unit = Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    static final Unit parts$lambda$130$lambda$129(ToolboxPanel this$0, Samples.Sample $s) {
        Studio.insertDocument$default(this$0.studio, new RbxDocument(CollectionsKt.listOf($s.getBuild().invoke()), MapsKt.emptyMap(), CollectionsKt.emptyList()), null, 2, null);
        return Unit.INSTANCE;
    }

    private final void objects(LinearLayout into) {
        Iterable<String> iterableListOf = CollectionsKt.listOf(new String[]{"Script", "LocalScript", "ModuleScript", "Model", "Folder", "ScreenGui", "Frame", "TextLabel", "TextButton", "TextBox", "ImageLabel", "ScrollingFrame", "UICorner", "UIListLayout", "UIStroke", "UIGradient", "PointLight", "SpotLight", "Sound", "ParticleEmitter", "ClickDetector", "ProximityPrompt", "Tool", "RemoteEvent", "BindableEvent", "StringValue", "NumberValue", "BoolValue", "Attachment", "WeldConstraint", "Humanoid", "Decal", "BillboardGui"});
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf, 10));
        for (final String str : iterableListOf) {
            Icons icons = Icons.INSTANCE;
            Context context = getContext();
            Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
            arrayList.add(new Triple(icons.instance(context, str), str, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda45
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return ToolboxPanel.objects$lambda$132$lambda$131(ToolboxPanel.this, str);
                }
            }));
        }
        grid(into, (List) arrayList);
        Ui ui = Ui.INSTANCE;
        Context context2 = getContext();
        Intrinsics.checkNotNullExpressionValue(context2, "getContext(...)");
        Icons icons2 = Icons.INSTANCE;
        Context context3 = getContext();
        Intrinsics.checkNotNullExpressionValue(context3, "getContext(...)");
        TextView textViewIconTextButton$default = Ui.iconTextButton$default(ui, context2, Icons.general$default(icons2, context3, "InsertObject", null, 4, null), "All objects…", 0, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda46
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.objects$lambda$134(ToolboxPanel.this, (View) obj);
            }
        }, 8, null);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        Ui ui2 = Ui.INSTANCE;
        Context context4 = getContext();
        Intrinsics.checkNotNullExpressionValue(context4, "getContext(...)");
        int iDp = ui2.dp(context4, 8.0f);
        Ui ui3 = Ui.INSTANCE;
        Context context5 = getContext();
        Intrinsics.checkNotNullExpressionValue(context5, "getContext(...)");
        int iDp2 = ui3.dp(context5, 8.0f);
        Ui ui4 = Ui.INSTANCE;
        Context context6 = getContext();
        Intrinsics.checkNotNullExpressionValue(context6, "getContext(...)");
        int iDp3 = ui4.dp(context6, 8.0f);
        Ui ui5 = Ui.INSTANCE;
        Context context7 = getContext();
        Intrinsics.checkNotNullExpressionValue(context7, "getContext(...)");
        layoutParamsLp$default.setMargins(iDp, iDp2, iDp3, ui5.dp(context7, 8.0f));
        Unit unit = Unit.INSTANCE;
        into.addView(textViewIconTextButton$default, layoutParamsLp$default);
    }

    static final Unit objects$lambda$132$lambda$131(ToolboxPanel this$0, String $cls) {
        Studio.insertObject$default(this$0.studio, $cls, null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit objects$lambda$134(final ToolboxPanel this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Dialogs dialogs = Dialogs.INSTANCE;
        Context context = this$0.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        dialogs.pickClass(context, "Insert Object", new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.objects$lambda$134$lambda$133(this$0, (String) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit objects$lambda$134$lambda$133(ToolboxPanel this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Studio.insertObject$default(this$0.studio, it, null, 2, null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: ToolboxPanel.kt */
    static final class InvCat {
        private final RobloxApi.Category insertAs;
        private final String label;
        private final List<Integer> types;

        public InvCat(String label, List<Integer> types, RobloxApi.Category insertAs) {
            Intrinsics.checkNotNullParameter(label, "label");
            Intrinsics.checkNotNullParameter(types, "types");
            Intrinsics.checkNotNullParameter(insertAs, "insertAs");
            this.label = label;
            this.types = types;
            this.insertAs = insertAs;
        }

        public final RobloxApi.Category getInsertAs() {
            return this.insertAs;
        }

        public final String getLabel() {
            return this.label;
        }

        public final List<Integer> getTypes() {
            return this.types;
        }
    }

    private final String kindOf(String cls) {
        if (Intrinsics.areEqual(cls, "Script") || Intrinsics.areEqual(cls, "LocalScript") || Intrinsics.areEqual(cls, "ModuleScript")) {
            return "Scripts";
        }
        if (Intrinsics.areEqual(cls, "MeshPart") || Intrinsics.areEqual(cls, "SpecialMesh")) {
            return "Meshes";
        }
        if (Intrinsics.areEqual(cls, "Sound") || Intrinsics.areEqual(cls, "SoundGroup")) {
            return "Audio";
        }
        if (Intrinsics.areEqual(cls, "Decal") || Intrinsics.areEqual(cls, "Texture") || Intrinsics.areEqual(cls, "SurfaceAppearance")) {
            return "Images";
        }
        if (StringsKt.endsWith(cls, "Part", false) || StringsKt.endsWith(cls, "Operation", false) || Intrinsics.areEqual(cls, "Seat") || Intrinsics.areEqual(cls, "VehicleSeat") || Intrinsics.areEqual(cls, "SpawnLocation") || Intrinsics.areEqual(cls, "Terrain")) {
            return "Parts";
        }
        if (StringsKt.endsWith(cls, "Gui", false) || StringsKt.startsWith(cls, "UI", false) || StringsKt.startsWith(cls, "Text", false) || StringsKt.startsWith(cls, "Image", false) || StringsKt.endsWith(cls, "Frame", false) || Intrinsics.areEqual(cls, "VideoFrame")) {
            return "GUI";
        }
        return "Models";
    }

    private final void chipRow(LinearLayout into, List<String> labels, String selected, final Function1<? super String, Unit> onPick) {
        Context ctx = getContext();
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        Ui ui = Ui.INSTANCE;
        Intrinsics.checkNotNull(ctx);
        linearLayout.setPadding(ui.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 2.0f));
        for (final String l : labels) {
            boolean on = Intrinsics.areEqual(l, selected);
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, l, 11.0f, on ? -1 : Ui.INSTANCE.getTEXT(), false, 16, null);
            textViewText$default.setPadding(Ui.INSTANCE.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 4.0f));
            GradientDrawable gradientDrawable = new GradientDrawable();
            Ui ui2 = Ui.INSTANCE;
            gradientDrawable.setColor(on ? ui2.getACCENT() : ui2.getHOVER());
            gradientDrawable.setCornerRadius(Ui.INSTANCE.dp(ctx, 12.0f));
            textViewText$default.setBackground(gradientDrawable);
            textViewText$default.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda13
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    onPick.invoke(l);
                }
            });
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = Ui.INSTANCE.dp(ctx, 5.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout.addView(textViewText$default, layoutParamsLp$default);
        }
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(ctx);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.addView(linearLayout);
        into.addView(horizontalScrollView, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
    }

    /* JADX WARN: Code duplicated, block: B:26:0x0193  */
    /* JADX WARN: Code duplicated, block: B:44:0x01e6  */
    /* JADX WARN: Code duplicated, block: B:51:0x020e  */
    /* JADX WARN: Code duplicated, block: B:81:0x01e9 A[SYNTHETIC] */
    private final void myModels(LinearLayout into) {
        List listEmptyList;
        List listEmptyList2;
        boolean z = false;
        boolean z2;
        Context ctx = getContext();
        chipRow(into, CollectionsKt.listOf(new String[]{"On this device", "My Roblox items"}), this.invRoblox ? "My Roblox items" : "On this device", new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda55
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.myModels$lambda$142(ToolboxPanel.this, (String) obj);
            }
        });
        if (this.invRoblox) {
            robloxInventory(into);
            return;
        }
        chipRow(into, this.deviceKinds, this.invKind, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda56
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.myModels$lambda$143(ToolboxPanel.this, (String) obj);
            }
        });
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        Ui ui = Ui.INSTANCE;
        Intrinsics.checkNotNull(ctx);
        linearLayout.setPadding(ui.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 2.0f), Ui.INSTANCE.dp(ctx, 6.0f), 0);
        linearLayout.addView(Ui.text$default(Ui.INSTANCE, ctx, "Sort:", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, this.invSortNewest ? "Newest first ▾" : "Name A–Z ▾", 11.0f, Ui.INFO, false, 16, null);
        textViewText$default.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 4.0f));
        textViewText$default.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda57
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ToolboxPanel.myModels$lambda$146$lambda$145(ToolboxPanel.this, view);
            }
        });
        linearLayout.addView(textViewText$default);
        into.addView(linearLayout);
        if (Intrinsics.areEqual(this.invKind, "Plugins")) {
            installedPlugins(into);
            return;
        }
        TextView textViewIconTextButton$default = Ui.iconTextButton$default(Ui.INSTANCE, ctx, Icons.general$default(Icons.INSTANCE, ctx, "Open", null, 4, null), "Import .rbxm / .rbxmx…", 0, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda58
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.myModels$lambda$147(ToolboxPanel.this, (View) obj);
            }
        }, 8, null);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default.setMargins(Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 4.0f));
        Unit unit = Unit.INSTANCE;
        into.addView(textViewIconTextButton$default, layoutParamsLp$default);
        TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, ctx, "Tip: long-press anything in the Explorer (parts, models, sounds, meshes, scripts, GUIs…) and drag it here to keep it.", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default2.setSingleLine(false);
        textViewText$default2.setPadding(Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 2.0f), Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 4.0f));
        into.addView(textViewText$default2);
        File[] fileArrListFiles = getLibraryDir().listFiles();
        if (fileArrListFiles != null) {
            Collection arrayList = new ArrayList();
            for (File file : fileArrListFiles) {
                if (file.isFile()) {
                    Intrinsics.checkNotNull(file);
                    if (myModels$keep(this, file, false)) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                } else {
                    z2 = false;
                }
                if (z2) {
                    arrayList.add(file);
                }
            }
            listEmptyList = (List) arrayList;
        } else {
            listEmptyList = CollectionsKt.emptyList();
        }
        List<File> listMyModels$sorted = myModels$sorted(this, listEmptyList);
        File[] fileArrListFiles2 = new File(getLibraryDir(), "recent").listFiles();
        if (fileArrListFiles2 != null) {
            Collection arrayList2 = new ArrayList();
            for (File file2 : fileArrListFiles2) {
                if (file2.isFile()) {
                    Intrinsics.checkNotNull(file2);
                    z = myModels$keep(this, file2, true);
                    if (z) {
                        arrayList2.add(file2);
                    }
                }
                if (z) {
                    arrayList2.add(file2);
                }
            }
            Iterable iterableSortedWith = CollectionsKt.sortedWith((List) arrayList2, new Comparator() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$myModels$$inlined$sortedByDescending$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(Object t, Object t2) {
                    return ComparisonsKt.compareValues(Long.valueOf(((File) t2).lastModified()), Long.valueOf(((File) t).lastModified()));
                }
            });
            if (iterableSortedWith == null || (listEmptyList2 = CollectionsKt.take(iterableSortedWith, 30)) == null) {
                listEmptyList2 = CollectionsKt.emptyList();
            }
        } else {
            listEmptyList2 = CollectionsKt.emptyList();
        }
        List<File> recent = listEmptyList2;
        if (listMyModels$sorted.isEmpty() && recent.isEmpty()) {
            TextView textViewText$default3 = Ui.text$default(Ui.INSTANCE, ctx, !Intrinsics.areEqual(this.invKind, "All") ? "Nothing of this kind in your inventory yet." : "Your inventory is empty.\nDrag objects here from the Explorer, import a model file, or insert something from the Creator Store.", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default3.setSingleLine(false);
            textViewText$default3.setPadding(Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 12.0f), 0);
            into.addView(textViewText$default3);
        }
        if (!listMyModels$sorted.isEmpty()) {
            section(into, "MY INVENTORY");
            Iterator it = listMyModels$sorted.iterator();
            while (it.hasNext()) {
                myModels$row(ctx, into, this, (File) it.next(), false);
            }
        }
        if (recent.isEmpty()) {
            return;
        }
        section(into, "RECENTLY INSERTED");
        for (File file3 : recent) {
            Intrinsics.checkNotNull(file3);
            myModels$row(ctx, into, this, file3, true);
        }
    }

    static final Unit myModels$lambda$142(ToolboxPanel this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.invRoblox = Intrinsics.areEqual(it, "My Roblox items");
        this$0.refresh();
        return Unit.INSTANCE;
    }

    static final Unit myModels$lambda$143(ToolboxPanel this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.invKind = it;
        this$0.refresh();
        return Unit.INSTANCE;
    }

    static final void myModels$lambda$146$lambda$145(ToolboxPanel this$0, View it) {
        this$0.invSortNewest = !this$0.invSortNewest;
        this$0.refresh();
    }

    static final Unit myModels$lambda$147(ToolboxPanel this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.actions.importToToolbox();
        return Unit.INSTANCE;
    }

    private static final String myModels$clsOf(ToolboxPanel this$0, File f, boolean recentItem) {
        String string = this$0.getMeta().getString((recentItem ? "recent/" : "") + f.getName(), null);
        return string == null ? "Model" : string;
    }

    private static final boolean myModels$keep(ToolboxPanel this$0, File f, boolean recentItem) {
        return Intrinsics.areEqual(this$0.invKind, "All") || Intrinsics.areEqual(this$0.kindOf(myModels$clsOf(this$0, f, recentItem)), this$0.invKind);
    }

    private static final List<File> myModels$sorted(ToolboxPanel this$0, List<? extends File> list) {
        if (this$0.invSortNewest) {
            return CollectionsKt.sortedWith(list, new Comparator() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$myModels$sorted$$inlined$sortedByDescending$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(Object t, Object t2) {
                    return ComparisonsKt.compareValues(Long.valueOf(((File) t2).lastModified()), Long.valueOf(((File) t).lastModified()));
                }
            });
        }
        return CollectionsKt.sortedWith(list, new Comparator() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$myModels$sorted$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                String name = ((File) t).getName();
                Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                String lowerCase = name.toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                String name2 = ((File) t2).getName();
                Intrinsics.checkNotNullExpressionValue(name2, "getName(...)");
                String lowerCase2 = name2.toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                return ComparisonsKt.compareValues(lowerCase, lowerCase2);
            }
        });
    }

    private static final void myModels$row(final Context ctx, LinearLayout $into, final ToolboxPanel this$0, final File f, final boolean recentItem) {
        final String cls = myModels$clsOf(this$0, f, recentItem);
        Ui ui = Ui.INSTANCE;
        Intrinsics.checkNotNull(ctx);
        TextView r = ui.iconTextButton(ctx, Icons.INSTANCE.instance(ctx, cls), FilesKt.getNameWithoutExtension(f), Ui.INSTANCE.getHOVER(), new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda32
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.myModels$row$lambda$156(this$0, f, (View) obj);
            }
        });
        r.setGravity(8388627);
        r.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda33
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return ToolboxPanel.myModels$row$lambda$160(ctx, f, this$0, recentItem, cls, view);
            }
        });
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default.setMargins(Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 2.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 2.0f));
        Unit unit = Unit.INSTANCE;
        $into.addView(r, layoutParamsLp$default);
    }

    static final Unit myModels$row$lambda$156(ToolboxPanel this$0, File $f, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.insertFile($f);
        return Unit.INSTANCE;
    }

    static final boolean myModels$row$lambda$160(final Context $ctx, final File $f, final ToolboxPanel this$0, final boolean $recentItem, final String $cls, View it) {
        Collection opts = CollectionsKt.listOf(new String[]{"Insert", "Rename…", "Delete"});
        Dialogs dialogs = Dialogs.INSTANCE;
        Intrinsics.checkNotNull($ctx);
        dialogs.themed($ctx).setTitle(FilesKt.getNameWithoutExtension($f)).setItems((CharSequence[]) opts.toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda59
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ToolboxPanel.myModels$row$lambda$160$lambda$159(this$0, $f, $ctx, $recentItem, $cls, dialogInterface, i);
            }
        }).show();
        return true;
    }

    static final void myModels$row$lambda$160$lambda$159(final ToolboxPanel this$0, final File $f, Context $ctx, final boolean $recentItem, final String $cls, DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:
                this$0.insertFile($f);
                break;
            case 1:
                Dialogs dialogs = Dialogs.INSTANCE;
                Intrinsics.checkNotNull($ctx);
                dialogs.prompt($ctx, "Rename", FilesKt.getNameWithoutExtension($f), new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda49
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return ToolboxPanel.myModels$row$lambda$160$lambda$159$lambda$157($f, this$0, $recentItem, $cls, (String) obj);
                    }
                });
                break;
            case 2:
                Dialogs dialogs2 = Dialogs.INSTANCE;
                Intrinsics.checkNotNull($ctx);
                dialogs2.confirm($ctx, "Remove from Toolbox", "Delete " + FilesKt.getNameWithoutExtension($f) + " from your toolbox?", "Delete", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda50
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return ToolboxPanel.myModels$row$lambda$160$lambda$159$lambda$158($f, this$0);
                    }
                });
                break;
            default:
                break;
        }
    }

    static final Unit myModels$row$lambda$160$lambda$159$lambda$157(File $f, ToolboxPanel this$0, boolean $recentItem, String $cls, String n) {
        Intrinsics.checkNotNullParameter(n, "n");
        File target = new File($f.getParentFile(), this$0.safeName(n) + "." + FilesKt.getExtension($f));
        if (!target.exists() && $f.renameTo(target)) {
            String key = $recentItem ? "recent/" : "";
            this$0.getMeta().edit().putString(key + target.getName(), $cls).remove(key + $f.getName()).apply();
        }
        this$0.refresh();
        return Unit.INSTANCE;
    }

    static final Unit myModels$row$lambda$160$lambda$159$lambda$158(File $f, ToolboxPanel this$0) {
        $f.delete();
        this$0.refresh();
        return Unit.INSTANCE;
    }

    private final void installedPlugins(LinearLayout into) {
        final ToolboxPanel toolboxPanel = this;
        final Context ctx = toolboxPanel.getContext();
        List<File> list = toolboxPanel.studio.getPlugins().list();
        if (toolboxPanel.invSortNewest) {
            list = CollectionsKt.sortedWith(list, new Comparator() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$installedPlugins$lambda$165$$inlined$sortedByDescending$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(Object t, Object t2) {
                    return ComparisonsKt.compareValues(Long.valueOf(((File) t2).lastModified()), Long.valueOf(((File) t).lastModified()));
                }
            });
        }
        List<File> list2 = list;
        float f = 8.0f;
        if (list2.isEmpty()) {
            Ui ui = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            TextView textViewText$default = Ui.text$default(ui, ctx, "No plugins installed. Get some from the Creator Store (Plugins) or My Roblox items → Plugins.", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default.setSingleLine(false);
            textViewText$default.setPadding(Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 12.0f), 0);
            into.addView(textViewText$default);
            return;
        }
        for (final File f2 : list2) {
            final boolean on = toolboxPanel.studio.getPlugins().isEnabled(f2);
            Ui ui2 = Ui.INSTANCE;
            Intrinsics.checkNotNull(ctx);
            TextView r = ui2.iconTextButton(ctx, Icons.INSTANCE.instance(ctx, "Plugin"), FilesKt.getNameWithoutExtension(f2) + (on ? "" : "  (off)"), Ui.INSTANCE.getHOVER(), new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda60
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return ToolboxPanel.installedPlugins$lambda$169(on, ctx, f2, toolboxPanel, (View) obj);
                }
            });
            r.setGravity(8388627);
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default.setMargins(Ui.INSTANCE.dp(ctx, f), Ui.INSTANCE.dp(ctx, 2.0f), Ui.INSTANCE.dp(ctx, f), Ui.INSTANCE.dp(ctx, 2.0f));
            Unit unit = Unit.INSTANCE;
            into.addView(r, layoutParamsLp$default);
            f = 8.0f;
        }
    }

    static final Unit installedPlugins$lambda$169(final boolean $on, final Context $ctx, final File $f, final ToolboxPanel this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String[] strArr = new String[2];
        strArr[0] = $on ? "Turn off" : "Turn on";
        strArr[1] = "Uninstall";
        Collection opts = CollectionsKt.listOf(strArr);
        Dialogs dialogs = Dialogs.INSTANCE;
        Intrinsics.checkNotNull($ctx);
        dialogs.themed($ctx).setTitle(FilesKt.getNameWithoutExtension($f)).setItems((CharSequence[]) opts.toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda34
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ToolboxPanel.installedPlugins$lambda$169$lambda$168(this$0, $f, $on, $ctx, dialogInterface, i);
            }
        }).show();
        return Unit.INSTANCE;
    }

    static final void installedPlugins$lambda$169$lambda$168(final ToolboxPanel this$0, final File $f, boolean $on, Context $ctx, DialogInterface dialogInterface, int i) {
        if (i == 0) {
            this$0.studio.getPlugins().setEnabled($f, !$on);
            this$0.studio.getPlugins().loadAll();
        } else {
            Dialogs dialogs = Dialogs.INSTANCE;
            Intrinsics.checkNotNull($ctx);
            dialogs.confirm($ctx, "Remove plugin", "Uninstall " + FilesKt.getNameWithoutExtension($f) + "?", "Remove", new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda52
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return ToolboxPanel.installedPlugins$lambda$169$lambda$168$lambda$167(this$0, $f);
                }
            });
        }
        this$0.refresh();
    }

    static final Unit installedPlugins$lambda$169$lambda$168$lambda$167(ToolboxPanel this$0, File $f) {
        this$0.studio.getPlugins().remove($f);
        this$0.studio.getPlugins().loadAll();
        return Unit.INSTANCE;
    }

    private final void robloxInventory(LinearLayout into) {
        String next;
        ArrayList arrayList;
        final Context ctx = getContext();
        Intrinsics.checkNotNull(ctx, "null cannot be cast to non-null type android.app.Activity");
        final Activity act = (Activity) ctx;
        Iterable iterable = this.robloxCats;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList2.add(((InvCat) it.next()).getLabel());
        }
        chipRow(into, (List) arrayList2, this.robloxCats.get(this.invRobloxCat).getLabel(), new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ToolboxPanel.robloxInventory$lambda$173(ToolboxPanel.this, (String) obj);
            }
        });
        int i = 0;
        if (RobloxApi.INSTANCE.isGuest(ctx)) {
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, "Sign in to see the models, meshes, plugins and other items on your Roblox account.", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default.setSingleLine(false);
            textViewText$default.setPadding(Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 6.0f));
            into.addView(textViewText$default);
            TextView textViewButton$default = Ui.button$default(Ui.INSTANCE, ctx, "Sign in with Roblox", 0, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda16
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return ToolboxPanel.robloxInventory$lambda$176(act, ToolboxPanel.this, (View) obj);
                }
            }, 4, null);
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default.setMargins(Ui.INSTANCE.dp(ctx, 8.0f), 0, Ui.INSTANCE.dp(ctx, 8.0f), 0);
            Unit unit = Unit.INSTANCE;
            into.addView(textViewButton$default, layoutParamsLp$default);
            return;
        }
        final InvCat cat = this.robloxCats.get(this.invRobloxCat);
        final int key = this.invRobloxCat;
        Pair<ArrayList<RobloxApi.InvItem>, String> pair = this.invCache.get(Integer.valueOf(key));
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(Ui.INSTANCE.dp(ctx, 10.0f), 0, Ui.INSTANCE.dp(ctx, 6.0f), 0);
        Ui ui = Ui.INSTANCE;
        String str = "";
        if (pair != null) {
            String str2 = pair.getFirst().size() + " item" + (pair.getFirst().size() == 1 ? "" : "s");
            if (str2 != null) {
                str = str2;
            }
        }
        LinearLayout head = linearLayout;
        head.addView(Ui.text$default(ui, ctx, str, 11.0f, Ui.INSTANCE.getDIM(), false, 16, null), Ui.INSTANCE.lp(0, -2, 1.0f));
        TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, ctx, "Reload", 11.0f, Ui.INFO, false, 16, null);
        textViewText$default2.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 4.0f));
        textViewText$default2.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ToolboxPanel.robloxInventory$lambda$181$lambda$180(ToolboxPanel.this, key, view);
            }
        });
        head.addView(textViewText$default2);
        into.addView(head);
        if (pair == null) {
            ProgressBar progressBar = new ProgressBar(ctx);
            progressBar.setIndeterminate(true);
            LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(ctx, 32.0f), Ui.INSTANCE.dp(ctx, 32.0f), 0.0f, 4, null);
            layoutParamsLp$default2.gravity = 1;
            layoutParamsLp$default2.topMargin = Ui.INSTANCE.dp(ctx, 12.0f);
            Unit unit2 = Unit.INSTANCE;
            into.addView(progressBar, layoutParamsLp$default2);
            loadInventory(key, cat, null);
            return;
        }
        ArrayList<RobloxApi.InvItem> arrayListComponent1 = pair.component1();
        String next2 = pair.component2();
        if (arrayListComponent1.isEmpty()) {
            Ui ui2 = Ui.INSTANCE;
            String lowerCase = cat.getLabel().toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            TextView textViewText$default3 = Ui.text$default(ui2, ctx, "No " + lowerCase + " on your account.", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default3.setPadding(Ui.INSTANCE.dp(ctx, 12.0f), Ui.INSTANCE.dp(ctx, 8.0f), 0, 0);
            into.addView(textViewText$default3);
            return;
        }
        String next3 = next2;
        List<RobloxApi.InvItem> list = this.invSortNewest ? arrayListComponent1 : CollectionsKt.sortedWith(arrayListComponent1, new Comparator() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$robloxInventory$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                String lowerCase2 = ((RobloxApi.InvItem) t).getName().toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                String lowerCase3 = ((RobloxApi.InvItem) t2).getName().toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase3, "toLowerCase(...)");
                return ComparisonsKt.compareValues(lowerCase2, lowerCase3);
            }
        });
        for (final RobloxApi.InvItem it2 : list) {
            LinearLayout linearLayout2 = new LinearLayout(ctx);
            linearLayout2.setOrientation(i);
            linearLayout2.setGravity(16);
            linearLayout2.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 3.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 3.0f));
            linearLayout2.setBackground(Ui.INSTANCE.rippleBg(ctx, Ui.INSTANCE.getHOVER(), 4.0f));
            linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda18
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ToolboxPanel.this.insertInventoryItem(it2, cat);
                }
            });
            linearLayout2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda19
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return ToolboxPanel.robloxInventory$lambda$188$lambda$187(ctx, it2, ToolboxPanel.this, view);
                }
            });
            ImageView imageView = new ImageView(ctx);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setBackgroundColor(Ui.INSTANCE.blend(Ui.INSTANCE.getPANEL(), ViewCompat.MEASURED_STATE_MASK, 0.08f));
            String url = this.invThumbs.get(Long.valueOf(it2.getId()));
            Bitmap bmp = url != null ? ImageLoader.INSTANCE.getKey("url:" + url) : null;
            if (bmp != null) {
                imageView.setImageBitmap(bmp);
                next = next3;
            } else {
                next = next3;
                imageView.setImageDrawable(Icons.general$default(Icons.INSTANCE, ctx, cat.getInsertAs() == RobloxApi.Category.PLUGINS ? "ManagePlugins" : "Toolbox", null, 4, null));
                if (url != null) {
                    Map map = this.invThumbViews;
                    String str3 = "url:" + url;
                    List<ImageView> list2 = (List<ImageView>) map.get(str3);
                    if (list2 == null) {
                        arrayList = new ArrayList();
                        map.put(str3, arrayList);
                    } else {
                        arrayList = (ArrayList) list2;
                    }
                    arrayList.add(imageView);
                }
            }
            ImageView imageView2 = imageView;
            LinearLayout.LayoutParams layoutParamsLp$default3 = Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(ctx, 40.0f), Ui.INSTANCE.dp(ctx, 40.0f), 0.0f, 4, null);
            layoutParamsLp$default3.rightMargin = Ui.INSTANCE.dp(ctx, 8.0f);
            Unit unit3 = Unit.INSTANCE;
            linearLayout2.addView(imageView2, layoutParamsLp$default3);
            TextView textViewText$default4 = Ui.text$default(Ui.INSTANCE, ctx, it2.getName(), 12.0f, 0, false, 24, null);
            textViewText$default4.setMaxLines(2);
            textViewText$default4.setSingleLine(false);
            textViewText$default4.setEllipsize(TextUtils.TruncateAt.END);
            linearLayout2.addView(textViewText$default4, Ui.INSTANCE.lp(0, -2, 1.0f));
            LinearLayout linearLayout3 = linearLayout2;
            LinearLayout.LayoutParams layoutParamsLp$default4 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default4.setMargins(Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 1.0f), Ui.INSTANCE.dp(ctx, 4.0f), Ui.INSTANCE.dp(ctx, 1.0f));
            Unit unit4 = Unit.INSTANCE;
            into.addView(linearLayout3, layoutParamsLp$default4);
            next3 = next;
            i = 0;
        }
        final String next4 = next3;
        if (next4 != null) {
            TextView textViewButton$default2 = Ui.button$default(Ui.INSTANCE, ctx, this.invLoading ? "Loading…" : "Load more", 0, new Function1() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda20
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return ToolboxPanel.robloxInventory$lambda$195(ToolboxPanel.this, key, cat, next4, (View) obj);
                }
            }, 4, null);
            LinearLayout.LayoutParams layoutParamsLp$default5 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default5.setMargins(Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 8.0f));
            Unit unit5 = Unit.INSTANCE;
            into.addView(textViewButton$default2, layoutParamsLp$default5);
        }
    }

    static final Unit robloxInventory$lambda$173(ToolboxPanel this$0, String l) {
        Intrinsics.checkNotNullParameter(l, "l");
        int i = 0;
        Iterator<InvCat> it = this$0.robloxCats.iterator();
        while (it.hasNext()) {
            if (Intrinsics.areEqual(((InvCat) it.next()).getLabel(), l)) {
                this$0.invRobloxCat = RangesKt.coerceAtLeast(i, 0);
                this$0.refresh();
                return Unit.INSTANCE;
            }
            i++;
        }
        i = -1;
        this$0.invRobloxCat = RangesKt.coerceAtLeast(i, 0);
        this$0.refresh();
        return Unit.INSTANCE;
    }

    static final Unit robloxInventory$lambda$176(Activity $act, final ToolboxPanel this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AccountUi.INSTANCE.signIn($act, new Function0() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda75
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return ToolboxPanel.robloxInventory$lambda$176$lambda$175(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit robloxInventory$lambda$176$lambda$175(ToolboxPanel this$0) {
        this$0.refresh();
        return Unit.INSTANCE;
    }

    static final void robloxInventory$lambda$181$lambda$180(ToolboxPanel this$0, int $key, View it) {
        this$0.invCache.remove(Integer.valueOf($key));
        this$0.refresh();
    }

    static final boolean robloxInventory$lambda$188$lambda$187(Context $ctx, RobloxApi.InvItem $it, ToolboxPanel this$0, View view) {
        Object systemService = ((Activity) $ctx).getSystemService("clipboard");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.content.ClipboardManager");
        ((ClipboardManager) systemService).setPrimaryClip(ClipData.newPlainText("Asset ID", String.valueOf($it.getId())));
        this$0.studio.toast("Copied asset ID " + $it.getId());
        return true;
    }

    static final Unit robloxInventory$lambda$195(ToolboxPanel this$0, int $key, InvCat $cat, String $next, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (!this$0.invLoading) {
            this$0.loadInventory($key, $cat, $next);
        }
        return Unit.INSTANCE;
    }

    private final void loadInventory(final int key, final InvCat cat, final String cursor) {
        if (this.invLoading) {
            return;
        }
        this.invLoading = true;
        final Context ctx = getContext();
        this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                ToolboxPanel.loadInventory$lambda$210(ctx, cursor, cat, ToolboxPanel.this, key);
            }
        });
    }

    static final void loadInventory$lambda$210(Context $ctx, final String $cursor, final InvCat $cat, final ToolboxPanel this$0, final int $key) {
        Object _r;
        List listListOf;
        Pair<List<RobloxApi.InvItem>, String> pairInventory;
        LinkedHashMap thumbs;
        try {
            RobloxApi robloxApi = RobloxApi.INSTANCE;
            Intrinsics.checkNotNull($ctx);
            long uid = robloxApi.userId($ctx);
            if ($cursor != null) {
                listListOf = CollectionsKt.listOf(RobloxApi.INSTANCE.inventory($ctx, uid, $cat.getTypes().get(0).intValue(), $cursor));
            } else {
                Iterable types = $cat.getTypes();
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(types, 10));
                Iterator it = types.iterator();
                while (it.hasNext()) {
                    int iIntValue = ((Number) it.next()).intValue();
                    try {
                        pairInventory = RobloxApi.INSTANCE.inventory($ctx, uid, iIntValue, null);
                    } catch (Exception e) {
                        if (iIntValue == $cat.getTypes().get(0).intValue()) {
                            throw e;
                        }
                        pairInventory = TuplesKt.to(CollectionsKt.emptyList(), null);
                    }
                    arrayList.add(pairInventory);
                }
                listListOf = (List) arrayList;
            }
            List pages = listListOf;
            Collection arrayList2 = new ArrayList();
            Iterator it2 = pages.iterator();
            while (it2.hasNext()) {
                CollectionsKt.addAll(arrayList2, (Iterable) ((Pair) it2.next()).getFirst());
            }
            HashSet hashSet = new HashSet();
            ArrayList arrayList3 = new ArrayList();
            for (Object obj : (List) arrayList2) {
                if (hashSet.add(Long.valueOf(((RobloxApi.InvItem) obj).getId()))) {
                    arrayList3.add(obj);
                }
            }
            ArrayList items = arrayList3;
            try {
                ArrayList arrayList4 = items;
                Collection arrayList5 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList4, 10));
                Iterator it3 = arrayList4.iterator();
                while (it3.hasNext()) {
                    arrayList5.add(Long.valueOf(((RobloxApi.InvItem) it3.next()).getId()));
                }
                int i = 0;
                Collection arrayList6 = new ArrayList();
                for (Object obj2 : (List) arrayList5) {
                    int i2 = i;
                    if (!this$0.invThumbs.containsKey(Long.valueOf(((Number) obj2).longValue()))) {
                        arrayList6.add(obj2);
                    }
                    i = i2;
                }
                Iterable iterableChunked = CollectionsKt.chunked((List) arrayList6, 100);
                Collection arrayList7 = new ArrayList();
                Iterator it4 = iterableChunked.iterator();
                while (it4.hasNext()) {
                    CollectionsKt.addAll(arrayList7, RobloxApi.INSTANCE.thumbnailUrls((List) it4.next(), "150x150").entrySet());
                }
                Iterable<Map.Entry> iterable = (List) arrayList7;
                int i3 = 0;
                thumbs = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterable, 10)), 16));
                for (Map.Entry entry : iterable) {
                    int i4 = i3;
                    Pair pair = TuplesKt.to(entry.getKey(), entry.getValue());
                    thumbs.put(pair.getFirst(), pair.getSecond());
                    i3 = i4;
                }
            } catch (Exception e2) {
                thumbs = new LinkedHashMap();
            }
            _r = new Triple(items, ((Pair) CollectionsKt.first(pages)).getSecond(), thumbs);
        } catch (Exception e3) {
            _r = ResultKt.createFailure(e3);
        }
        final Object r = _r;
        this$0.post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                ToolboxPanel.loadInventory$lambda$210$lambda$209(this$0, r, $cursor, $key, $cat);
            }
        });
    }

    static final void loadInventory$lambda$210$lambda$209(ToolboxPanel this$0, Object $r, String $cursor, int $key, InvCat $cat) {
        ArrayList<RobloxApi.InvItem> arrayList;
        boolean z;
        Pair<ArrayList<RobloxApi.InvItem>, String> pair;
        this$0.invLoading = false;
        Throwable thM460exceptionOrNullimpl = (($r) instanceof Result.Failure ? ((Result.Failure) ($r)).exception : null);
        if (thM460exceptionOrNullimpl == null) {
            Triple triple = (Triple) $r;
            Iterable iterable = (List) triple.component1();
            String str = (String) triple.component2();
            Map map = (Map) triple.component3();
            HashMap<Long, String> map2 = this$0.invThumbs;
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Map.Entry entry : ((Map<?, ?>) map).entrySet()) {
                String str2 = (String) entry.getValue();
                Intrinsics.checkNotNull(str2);
                if (str2.length() > 0) {
                    linkedHashMap.put(entry.getKey(), entry.getValue());
                }
            }
            map2.putAll(linkedHashMap);
            if ($cursor == null || (pair = this$0.invCache.get(Integer.valueOf($key))) == null || (arrayList = pair.getFirst()) == null) {
                arrayList = new ArrayList<>();
            }
            Collection arrayList2 = new ArrayList();
            for (Object obj : iterable) {
                RobloxApi.InvItem invItem = (RobloxApi.InvItem) obj;
                Iterable iterable2 = arrayList;
                if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
                    Iterator it = iterable2.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (((RobloxApi.InvItem) it.next()).getId() == invItem.getId()) {
                                z = false;
                                break;
                            }
                        } else {
                            z = true;
                            break;
                        }
                    }
                } else {
                    z = true;
                }
                if (z) {
                    arrayList2.add(obj);
                }
            }
            arrayList.addAll((List) arrayList2);
            this$0.invCache.put(Integer.valueOf($key), TuplesKt.to(arrayList, str));
        } else {
            this$0.invCache.put(Integer.valueOf($key), TuplesKt.to(new ArrayList(), null));
            Studio studio = this$0.studio;
            String lowerCase = $cat.getLabel().toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            studio.toast("Couldn't load your " + lowerCase + ": " + thM460exceptionOrNullimpl.getMessage());
        }
        if (this$0.tab == 1 && this$0.invRoblox) {
            this$0.refresh();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:21:0x00c2 A[Catch: all -> 0x0136, TryCatch #0 {, blocks: (B:7:0x0053, B:10:0x0072, B:12:0x00a6, B:14:0x00ae, B:22:0x00cc, B:21:0x00c2), top: B:35:0x0053 }] */
    public final void insertInventoryItem(RobloxApi.InvItem it, InvCat cat) {
        long id = it.getId();
        String name = it.getName();
        RobloxApi robloxApi = RobloxApi.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        RobloxApi.Item item = new RobloxApi.Item(id, name, robloxApi.userName(context), false, "", 0, 0, false, false, 0L, 0, 0, 0, 0, 0, null, null, 0L, false, null, 1048064, null);
        if (!Intrinsics.areEqual(cat.getLabel(), "Animations")) {
            insertAsset$default(this, item, cat.getInsertAs(), false, 4, null);
            return;
        }
        synchronized (this.studio.getLock()) {
            Instance instanceCreate = InstanceFactory.INSTANCE.create("Animation");
            String strTake = StringsKt.take(it.getName(), 40);
            if (StringsKt.isBlank(strTake)) {
                strTake = "Animation";
            }
            instanceCreate.setName(strTake);
            Instance.set$default(instanceCreate, "AnimationId", "rbxassetid://" + it.getId(), false, 4, null);
            Instance workspace = (Instance) CollectionsKt.firstOrNull((List) this.studio.getSelection());
            if (workspace == null) {
                workspace = this.studio.getModel().getWorkspace();
            } else {
                if (!(!workspace.getIsService() || Intrinsics.areEqual(workspace.getClassName(), "Workspace"))) {
                    workspace = null;
                }
                if (workspace == null) {
                    workspace = this.studio.getModel().getWorkspace();
                }
            }
            this.studio.getUndo().begin("Insert animation");
            this.studio.reparent(instanceCreate, workspace);
            this.studio.getUndo().commit();
            this.studio.getSelection().clear();
            this.studio.getSelection().add(instanceCreate);
        }
        Iterator it2 = this.studio.getListeners().iterator();
        while (it2.hasNext()) {
            ((Studio.Listener) it2.next()).onSelectionChanged();
        }
        this.studio.toast("Added animation " + it.getName());
    }

    private final void insertFile(final File f) {
        new Thread(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                ToolboxPanel.insertFile$lambda$217(f, ToolboxPanel.this);
            }
        }).start();
    }

    static final void insertFile$lambda$217(File $f, final ToolboxPanel this$0) {
        try {
            final RbxDocument doc = RbxFile.INSTANCE.read(FilesKt.readBytes($f));
            if (RbxFile.INSTANCE.isPlace(doc)) {
                OutputLog.INSTANCE.error($f.getName() + " is a place file; open it with File → Open");
            } else {
                Iterator it = CollectionsKt.take(doc.getWarnings(), 5).iterator();
                while (it.hasNext()) {
                    OutputLog.INSTANCE.warn((String) it.next());
                }
                this$0.studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.ui.ToolboxPanel$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Studio.insertDocument$default(this$0.studio, doc, null, 2, null);
                    }
                });
            }
        } catch (Exception e) {
            OutputLog.INSTANCE.error("Could not insert " + $f.getName() + ": " + e.getMessage());
        }
    }

    private final String safeName(String name) {
        String strReplace = new Regex("[^A-Za-z0-9._ -]").replace(name, "_");
        if (StringsKt.isBlank(strReplace)) {
            strReplace = "Model";
        }
        return StringsKt.take(strReplace, 60);
    }

    public static /* synthetic */ void saveToLibrary$default(ToolboxPanel toolboxPanel, String str, byte[] bArr, String str2, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = null;
        }
        toolboxPanel.saveToLibrary(str, bArr, str2);
    }

    public final void saveToLibrary(String name, byte[] bytes, String cls) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        String safe = safeName(name);
        if (StringsKt.endsWith(safe, ".rbxm", false) || StringsKt.endsWith(safe, ".rbxmx", false)) {
            safe = com.goydevv.studrblx.util.KtCompat.substringBeforeLast(safe, '.');
        }
        File f = new File(getLibraryDir(), safe + ".rbxm");
        int i = 2;
        while (f.exists()) {
            f = new File(getLibraryDir(), safe + " (" + i + ").rbxm");
            i++;
        }
        FilesKt.writeBytes(f, bytes);
        if (cls != null) {
            getMeta().edit().putString(f.getName(), cls).apply();
        }
        this.tab = 1;
        refresh();
    }
}
