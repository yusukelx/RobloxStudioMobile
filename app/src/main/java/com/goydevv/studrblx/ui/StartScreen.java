package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.goydevv.studrblx.core.PlaceFactory;
import com.goydevv.studrblx.core.Recents;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.net.Accounts;
import com.goydevv.studrblx.net.AuthRequiredException;
import com.goydevv.studrblx.net.HttpException;
import com.goydevv.studrblx.net.ImageLoader;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.net.RobloxCloud;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.json.JSONException;

/* JADX INFO: compiled from: StartScreen.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class StartScreen extends LinearLayout {
    public static final String GROUPS = "Group Games";
    public static final String HOME = "Home";
    public static final String MY_GAMES = "My Games";
    public static final String RECENT = "Recent";
    public static final String SHARED = "Shared With Me";
    private final LinearLayout accountChip;
    private final Function0<Unit> accountListener;
    private final Activity act;
    private final MainActions actions;
    private final FrameLayout content;
    private int gen;
    private final ArrayList<FlowLayout> grids;
    private final Host host;
    private final HashMap<String, List<ImageView>> iconViews;
    private final Function1<String, Unit> imgListener;
    private final Handler main;
    private final LinearLayout nav;
    private final ExecutorService net;
    private String page;
    private final boolean phone;
    private String query;
    private long selectedGroup;
    private final Studio studio;

    /* JADX INFO: compiled from: StartScreen.kt */
    public interface Host {
        void closeStart();

        boolean hasOpenPlace();

        void newFromTemplate(PlaceFactory.Template t);

        void openFileFromStart();

        void openRecentFile(Recents.Entry e);

        void showStudioSettings();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StartScreen(Activity act, Studio studio, MainActions actions, Host host) {
        super(act);
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Intrinsics.checkNotNullParameter(host, "host");
        this.act = act;
        this.studio = studio;
        this.actions = actions;
        this.host = host;
        this.net = Executors.newFixedThreadPool(2);
        this.main = new Handler(Looper.getMainLooper());
        this.content = new FrameLayout(this.act);
        this.nav = new LinearLayout(this.act);
        this.page = HOME;
        this.accountChip = new LinearLayout(this.act);
        this.phone = this.act.getResources().getConfiguration().smallestScreenWidthDp < 600;
        this.iconViews = new HashMap<>();
        this.imgListener = new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda44
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StartScreen.imgListener$lambda$3(StartScreen.this, (String) obj);
            }
        };
        this.accountListener = new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda45
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return StartScreen.accountListener$lambda$5(StartScreen.this);
            }
        };
        this.grids = new ArrayList<>();
        this.query = "";
        setOrientation(1);
        setBackgroundColor(Ui.INSTANCE.getBG());
        setClickable(true);
        LinearLayout linearLayout = new LinearLayout(this.act);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setBackgroundColor(Ui.INSTANCE.getTOPBAR());
        linearLayout.setPadding(dp(10.0f), dp(6.0f), dp(8.0f), dp(6.0f));
        ImageView imageView = new ImageView(this.act);
        imageView.setImageResource(com.goydevv.studrblx.R.mipmap.ic_launcher);
        imageView.setPadding(0, 0, 0, 0);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Ui.INSTANCE.blend(Ui.INSTANCE.getTOPBAR(), Ui.INSTANCE.getACCENT(), 0.3f));
        gradientDrawable.setCornerRadius(dp(8.0f));
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, dp(30.0f), dp(30.0f), 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = dp(10.0f);
        Unit unit = Unit.INSTANCE;
        linearLayout.addView(imageView, layoutParamsLp$default);
        linearLayout.addView(Ui.INSTANCE.text(this.act, "Roblox Studio", 16.0f, Ui.INSTANCE.getTEXT(), true));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this.act, "v" + ((Object) AboutDialog.INSTANCE.version(this.act).getFirst()), 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setPadding(dp(7.0f), dp(1.0f), dp(7.0f), dp(1.0f));
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setColor(Ui.INSTANCE.getHOVER());
        gradientDrawable2.setCornerRadius(dp(9.0f));
        textViewText$default.setBackground(gradientDrawable2);
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
        layoutParamsLp$default2.leftMargin = dp(8.0f);
        Unit unit2 = Unit.INSTANCE;
        linearLayout.addView(textViewText$default, layoutParamsLp$default2);
        linearLayout.addView(new View(this.act), Ui.INSTANCE.lp(0, 1, 1.0f));
        if (this.host.hasOpenPlace()) {
            TextView textViewIconTextButton$default = Ui.iconTextButton$default(Ui.INSTANCE, this.act, Icons.general$default(Icons.INSTANCE, this.act, "Back", null, 4, null), this.phone ? "Place" : "Back to place", 0, new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda46
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return StartScreen._init_$lambda$13(StartScreen.this, (View) obj);
                }
            }, 8, null);
            LinearLayout.LayoutParams layoutParamsLp$default3 = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            layoutParamsLp$default3.rightMargin = dp(6.0f);
            Unit unit3 = Unit.INSTANCE;
            linearLayout.addView(textViewIconTextButton$default, layoutParamsLp$default3);
        }
        ImageView imageViewIconButton$default = Ui.iconButton$default(Ui.INSTANCE, this.act, Icons.general$default(Icons.INSTANCE, this.act, "Info", null, 4, null), 0.0f, new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda47
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StartScreen._init_$lambda$15(StartScreen.this, (View) obj);
            }
        }, 4, null);
        imageViewIconButton$default.setContentDescription("About Roblox Studio");
        linearLayout.addView(imageViewIconButton$default);
        ImageView imageViewIconButton$default2 = Ui.iconButton$default(Ui.INSTANCE, this.act, Icons.general$default(Icons.INSTANCE, this.act, "Settings", null, 4, null), 0.0f, new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda48
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StartScreen._init_$lambda$17(StartScreen.this, (View) obj);
            }
        }, 4, null);
        imageViewIconButton$default2.setContentDescription("Studio settings");
        linearLayout.addView(imageViewIconButton$default2);
        LinearLayout linearLayout2 = this.accountChip;
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        linearLayout2.setPadding(dp(4.0f), dp(3.0f), dp(10.0f), dp(3.0f));
        linearLayout2.setBackground(Ui.INSTANCE.rippleBg(this.act, Ui.INSTANCE.getHOVER(), 16.0f));
        linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda49
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StartScreen startScreen = StartScreen.this;
                AccountUi.INSTANCE.show(startScreen.act, startScreen.studio, startScreen.actions);
            }
        });
        LinearLayout linearLayout3 = this.accountChip;
        LinearLayout.LayoutParams layoutParamsLp$default4 = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
        layoutParamsLp$default4.leftMargin = dp(6.0f);
        Unit unit4 = Unit.INSTANCE;
        linearLayout.addView(linearLayout3, layoutParamsLp$default4);
        addView(linearLayout, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        addView(Ui.divider$default(Ui.INSTANCE, this.act, false, 2, null));
        LinearLayout linearLayout4 = new LinearLayout(this.act);
        linearLayout4.setOrientation(this.phone ? 1 : 0);
        this.nav.setOrientation(!this.phone ? 1 : 0);
        if (this.phone) {
            this.nav.setPadding(dp(8.0f), dp(6.0f), dp(8.0f), dp(6.0f));
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this.act);
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
            horizontalScrollView.setBackgroundColor(getSideColor());
            horizontalScrollView.addView(this.nav);
            linearLayout4.addView(horizontalScrollView, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            linearLayout4.addView(Ui.divider$default(Ui.INSTANCE, this.act, false, 2, null));
        } else {
            this.nav.setPadding(dp(10.0f), dp(10.0f), dp(10.0f), dp(10.0f));
            ScrollView scrollView = new ScrollView(this.act);
            scrollView.setFillViewport(true);
            scrollView.setBackgroundColor(getSideColor());
            scrollView.addView(this.nav, new FrameLayout.LayoutParams(-1, -1));
            linearLayout4.addView(scrollView, Ui.lp$default(Ui.INSTANCE, dp(214.0f), -1, 0.0f, 4, null));
            linearLayout4.addView(Ui.INSTANCE.divider(this.act, true));
        }
        linearLayout4.addView(this.content, Ui.INSTANCE.lp(this.phone ? -1 : 0, this.phone ? 0 : -1, 1.0f));
        addView(linearLayout4, Ui.INSTANCE.lp(-1, 0, 1.0f));
        refreshAccount();
        show(HOME);
    }

    static final Unit imgListener$lambda$3(final StartScreen this$0, final String k) {
        Intrinsics.checkNotNullParameter(k, "k");
        if (StringsKt.startsWith(k, "url:", false)) {
            this$0.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StartScreen.imgListener$lambda$3$lambda$2(this$0, k);
                }
            });
        }
        return Unit.INSTANCE;
    }

    static final void imgListener$lambda$3$lambda$2(StartScreen this$0, String $k) {
        Iterable<ImageView> iterable = (List) this$0.iconViews.get($k);
        if (iterable != null) {
            for (ImageView imageView : iterable) {
                Bitmap bitmapPeekKey = ImageLoader.INSTANCE.peekKey($k);
                if (bitmapPeekKey != null) {
                    this$0.showBitmap(imageView, bitmapPeekKey);
                }
            }
        }
    }

    static final Unit accountListener$lambda$5(final StartScreen this$0) {
        this$0.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                StartScreen.accountListener$lambda$5$lambda$4(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final void accountListener$lambda$5$lambda$4(StartScreen this$0) {
        this$0.refreshAccount();
        if (Intrinsics.areEqual(this$0.page, RECENT)) {
            return;
        }
        this$0.show(this$0.page);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getLinkColor() {
        return Ui.INSTANCE.getLIGHT() ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.blend(Ui.INSTANCE.getACCENT(), -1, 0.45f);
    }

    private final int getSideColor() {
        Ui ui;
        int bg;
        float f;
        if (Ui.INSTANCE.getLIGHT()) {
            ui = Ui.INSTANCE;
            bg = Ui.INSTANCE.getBG();
            f = 0.035f;
        } else {
            ui = Ui.INSTANCE;
            bg = Ui.INSTANCE.getBG();
            f = 0.18f;
        }
        return ui.blend(bg, ViewCompat.MEASURED_STATE_MASK, f);
    }

    static final Unit _init_$lambda$13(StartScreen this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.host.closeStart();
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$15(StartScreen this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$17(StartScreen this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.host.showStudioSettings();
        return Unit.INSTANCE;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ImageLoader.INSTANCE.getListeners().add(this.imgListener);
        Accounts.INSTANCE.getListeners().add(this.accountListener);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        ImageLoader.INSTANCE.getListeners().remove(this.imgListener);
        Accounts.INSTANCE.getListeners().remove(this.accountListener);
        this.gen++;
        this.net.shutdown();
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int dp(float v) {
        return Ui.INSTANCE.dp(this.act, v);
    }

    private final void buildNav() {
        this.nav.removeAllViews();
        for (Pair pair : CollectionsKt.listOf(new Pair[]{TuplesKt.to(HOME, HOME), TuplesKt.to(RECENT, "History")})) {
            final String p = (String) pair.component1();
            String icon = (String) pair.component2();
            buildNav$item(this, p, icon, Intrinsics.areEqual(this.page, p), new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda21
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.buildNav$lambda$32(StartScreen.this, p);
                }
            });
        }
        buildNav$label(this, "Creations");
        for (Pair pair2 : CollectionsKt.listOf(new Pair[]{TuplesKt.to(MY_GAMES, "Account"), TuplesKt.to(SHARED, "Shared"), TuplesKt.to(GROUPS, "Group")})) {
            final String p2 = (String) pair2.component1();
            String icon2 = (String) pair2.component2();
            buildNav$item(this, p2, icon2, Intrinsics.areEqual(this.page, p2), new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda22
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.buildNav$lambda$33(StartScreen.this, p2);
                }
            });
        }
        buildNav$label(this, "Open");
        buildNav$item(this, "Open File…", "Open", false, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda23
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return StartScreen.buildNav$lambda$34(StartScreen.this);
            }
        });
        buildNav$item(this, "Open by Place ID…", "Download", false, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda24
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return StartScreen.buildNav$lambda$35(StartScreen.this);
            }
        });
        if (!this.phone) {
            this.nav.addView(new View(this.act), Ui.INSTANCE.lp(-1, 0, 1.0f));
        }
        /* About removed */
    }

    private static final void buildNav$item(StartScreen this$0, String label, String icon, boolean selected, final Function0<Unit> function0) {
        int sideColor;
        LinearLayout.LayoutParams layoutParamsLp$default;
        LinearLayout v = new LinearLayout(this$0.act);
        v.setOrientation(0);
        v.setGravity(16);
        v.setPadding(this$0.dp(10.0f), this$0.dp(this$0.phone ? 7.0f : 9.0f), this$0.dp(14.0f), this$0.dp(this$0.phone ? 7.0f : 9.0f));
        Ui ui = Ui.INSTANCE;
        Activity activity = this$0.act;
        if (selected) {
            sideColor = Ui.INSTANCE.blend(this$0.getSideColor(), Ui.INSTANCE.getACCENT(), Ui.INSTANCE.getLIGHT() ? 0.16f : 0.32f);
        } else {
            sideColor = this$0.getSideColor();
        }
        v.setBackground(ui.rippleBg(activity, sideColor, this$0.phone ? 18.0f : 9.0f));
        v.setClickable(true);
        v.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                function0.invoke();
            }
        });
        v.setContentDescription(label);
        ImageView imageView = new ImageView(this$0.act);
        imageView.setImageDrawable(Icons.general$default(Icons.INSTANCE, this$0.act, icon, null, 4, null));
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, this$0.dp(20.0f), this$0.dp(20.0f), 0.0f, 4, null);
        layoutParamsLp$default2.rightMargin = this$0.dp(10.0f);
        Unit unit = Unit.INSTANCE;
        v.addView(imageView, layoutParamsLp$default2);
        v.addView(Ui.INSTANCE.text(this$0.act, label, 13.5f, Ui.INSTANCE.getTEXT(), selected));
        LinearLayout linearLayout = this$0.nav;
        LinearLayout linearLayout2 = v;
        boolean z = this$0.phone;
        Ui ui2 = Ui.INSTANCE;
        if (z) {
            layoutParamsLp$default = Ui.lp$default(ui2, -2, -2, 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = this$0.dp(4.0f);
        } else {
            layoutParamsLp$default = Ui.lp$default(ui2, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default.bottomMargin = this$0.dp(2.0f);
        }
        Unit unit2 = Unit.INSTANCE;
        linearLayout.addView(linearLayout2, layoutParamsLp$default);
    }

    private static final void buildNav$label(StartScreen this$0, String text) {
        if (this$0.phone) {
            return;
        }
        LinearLayout linearLayout = this$0.nav;
        Ui ui = Ui.INSTANCE;
        Activity activity = this$0.act;
        String upperCase = text.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        TextView textViewText = ui.text(activity, upperCase, 10.5f, Ui.INSTANCE.getDIM(), true);
        textViewText.setLetterSpacing(0.08f);
        textViewText.setPadding(this$0.dp(12.0f), this$0.dp(14.0f), 0, this$0.dp(6.0f));
        linearLayout.addView(textViewText);
    }

    static final Unit buildNav$lambda$32(StartScreen this$0, String $p) {
        this$0.show($p);
        return Unit.INSTANCE;
    }

    static final Unit buildNav$lambda$33(StartScreen this$0, String $p) {
        this$0.show($p);
        return Unit.INSTANCE;
    }

    static final Unit buildNav$lambda$34(StartScreen this$0) {
        this$0.host.openFileFromStart();
        return Unit.INSTANCE;
    }

    static final Unit buildNav$lambda$35(StartScreen this$0) {
        AccountUi.INSTANCE.openById(this$0.act, this$0.studio, this$0.actions);
        return Unit.INSTANCE;
    }

    static final Unit buildNav$lambda$36(StartScreen this$0) {
        
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0080  */
    private final void refreshAccount() {
        String name;
        String displayName;
        this.accountChip.removeAllViews();
        ImageView img = new ImageView(this.act);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, dp(26.0f), dp(26.0f), 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = dp(6.0f);
        Unit unit = Unit.INSTANCE;
        this.accountChip.addView(img, layoutParamsLp$default);
        Accounts.Account a = Accounts.INSTANCE.active(this.act);
        if (a != null) {
            AccountUi.INSTANCE.loadHeadshot(img, a.getUserId());
        } else {
            img.setImageDrawable(Icons.general$default(Icons.INSTANCE, this.act, "Account", null, 4, null));
        }
        if (a == null || (displayName = a.getDisplayName()) == null) {
            name = "Guest";
        } else {
            String name2 = displayName;
            if (name2.length() == 0) {
                name2 = a.getName();
            }
            name = name2;
            if (name == null) {
                name = "Guest";
            }
        }
        this.accountChip.addView(Ui.text$default(Ui.INSTANCE, this.act, (this.phone ? StringsKt.take(name, 10) : name) + "  ▾", 13.0f, Ui.INSTANCE.getTEXT(), false, 16, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public final void show(String p) {
        this.page = p;
        this.gen++;
        this.grids.clear();
        this.query = "";
        buildNav();
        this.content.removeAllViews();
        this.iconViews.clear();
        switch (p.hashCode()) {
            case -1851051397:
                if (p.equals(RECENT)) {
                    recent();
                    break;
                }
                break;
            case 2255103:
                if (p.equals(HOME)) {
                    home();
                }
                break;
            case 925030816:
                if (p.equals(GROUPS)) {
                    groupGames();
                }
                break;
            case 1411533463:
                if (p.equals(SHARED)) {
                    games(Kind.SHARED);
                }
                break;
            case 2050873165:
                if (p.equals(MY_GAMES)) {
                    games(Kind.MINE);
                }
                break;
        }
    }

    private final LinearLayout scrollColumn() {
        LinearLayout col = new LinearLayout(this.act);
        col.setOrientation(1);
        col.setPadding(dp(this.phone ? 14.0f : 22.0f), dp(10.0f), dp(this.phone ? 14.0f : 22.0f), dp(28.0f));
        FrameLayout frameLayout = this.content;
        ScrollView scrollView = new ScrollView(this.act);
        scrollView.addView(col);
        frameLayout.addView(scrollView, new FrameLayout.LayoutParams(-1, -1));
        return col;
    }

    static /* synthetic */ void heading$default(StartScreen startScreen, LinearLayout linearLayout, String str, String str2, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = null;
        }
        startScreen.heading(linearLayout, str, str2);
    }

    private final void heading(LinearLayout col, String text, final String seeAll) {
        LinearLayout r = new LinearLayout(this.act);
        r.setOrientation(0);
        r.setGravity(16);
        r.setPadding(0, dp(20.0f), 0, dp(10.0f));
        r.addView(Ui.INSTANCE.text(this.act, text, 17.0f, Ui.INSTANCE.getTEXT(), true), Ui.INSTANCE.lp(0, -2, 1.0f));
        if (seeAll != null) {
            TextView textViewText = Ui.INSTANCE.text(this.act, "See all  ›", 13.0f, getLinkColor(), true);
            textViewText.setPadding(dp(10.0f), dp(4.0f), dp(6.0f), dp(4.0f));
            textViewText.setBackground(Ui.INSTANCE.rippleBg(this.act, 0, 12.0f));
            textViewText.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda11
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StartScreen.this.show(seeAll);
                }
            });
            r.addView(textViewText);
        }
        col.addView(r, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
    }

    private final void pageHeader(LinearLayout col, String title, String sub, boolean search, final Function0<Unit> onRefresh) {
        LinearLayout r = new LinearLayout(this.act);
        r.setOrientation(0);
        r.setGravity(16);
        r.setPadding(0, dp(12.0f), 0, dp(4.0f));
        LinearLayout t = new LinearLayout(this.act);
        t.setOrientation(1);
        t.addView(Ui.INSTANCE.text(this.act, title, 22.0f, Ui.INSTANCE.getTEXT(), true));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this.act, sub, 12.5f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setPadding(0, dp(2.0f), 0, 0);
        t.addView(textViewText$default);
        r.addView(t, Ui.INSTANCE.lp(0, -2, 1.0f));
        if (onRefresh != null) {
            ImageView imageViewIconButton = Ui.INSTANCE.iconButton(this.act, Icons.general$default(Icons.INSTANCE, this.act, "PluginsFolder", null, 4, null), 36.0f, new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return StartScreen.pageHeader$lambda$47(onRefresh, (View) obj);
                }
            });
            imageViewIconButton.setContentDescription("Refresh");
            r.addView(imageViewIconButton);
        }
        col.addView(r, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        if (search) {
            EditText editTextInput$default = Ui.input$default(Ui.INSTANCE, this.act, "", "Search", false, false, 24, null);
            Drawable drawableGeneral$default = Icons.general$default(Icons.INSTANCE, this.act, "FindAll", null, 4, null);
            drawableGeneral$default.setBounds(0, 0, dp(16.0f), dp(16.0f));
            editTextInput$default.setCompoundDrawables(drawableGeneral$default, null, null, null);
            editTextInput$default.setCompoundDrawablePadding(dp(8.0f));
            editTextInput$default.setPadding(dp(12.0f), dp(8.0f), dp(12.0f), dp(8.0f));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Ui.INSTANCE.getINPUT());
            gradientDrawable.setStroke(dp(1.0f), Ui.INSTANCE.blend(Ui.INSTANCE.getINPUT(), Ui.INSTANCE.getTEXT(), 0.18f));
            gradientDrawable.setCornerRadius(dp(20.0f));
            editTextInput$default.setBackground(gradientDrawable);
            editTextInput$default.addTextChangedListener(new TextWatcher() { // from class: com.goydevv.studrblx.ui.StartScreen$pageHeader$box$1$2
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence s, int a, int b, int c) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence s, int a, int b, int c) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable s) {
                    String string;
                    StartScreen startScreen = StartScreen.this;
                    String string2 = (s == null || (string = s.toString()) == null) ? null : StringsKt.trim((CharSequence) string).toString();
                    if (string2 == null) {
                        string2 = "";
                    }
                    startScreen.query = string2;
                    StartScreen.this.applyFilter();
                }
            });
            EditText editText = editTextInput$default;
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, this.phone ? -1 : dp(340.0f), -2, 0.0f, 4, null);
            layoutParamsLp$default.topMargin = dp(10.0f);
            layoutParamsLp$default.bottomMargin = dp(6.0f);
            Unit unit = Unit.INSTANCE;
            col.addView(editText, layoutParamsLp$default);
        }
    }

    static final Unit pageHeader$lambda$47(Function0 $onRefresh, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $onRefresh.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void applyFilter() {
        String q = this.query.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(q, "toLowerCase(...)");
        Iterator<FlowLayout> it = this.grids.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            FlowLayout next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            FlowLayout g = next;
            int childCount = g.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View c = g.getChildAt(i);
                Object tag = c.getTag();
                String str = tag instanceof String ? (String) tag : null;
                if (str != null) {
                    String name = str.toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue(name, "toLowerCase(...)");
                    if (name != null) {
                        int i2 = 0;
                        if (!(q.length() == 0) && !StringsKt.contains((CharSequence) name, (CharSequence) q, false)) {
                            i2 = 8;
                        }
                        c.setVisibility(i2);
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void message$default(StartScreen startScreen, LinearLayout linearLayout, String str, String str2, String str3, Function0 function0, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = null;
        }
        if ((i & 8) != 0) {
            str3 = "Account";
        }
        if ((i & 16) != 0) {
            function0 = null;
        }
        startScreen.message(linearLayout, str, str2, str3, function0);
    }

    private final void message(LinearLayout col, String text, String button, String icon, final Function0<Unit> onClick) {
        LinearLayout card = new LinearLayout(this.act);
        card.setOrientation(1);
        card.setPadding(dp(16.0f), dp(14.0f), dp(16.0f), dp(14.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Ui.INSTANCE.getCARD());
        gradientDrawable.setCornerRadius(dp(12.0f));
        card.setBackground(gradientDrawable);
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this.act, text, 13.5f, Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setLineSpacing(dp(2.0f), 1.0f);
        card.addView(textViewText$default);
        if (button != null && onClick != null) {
            TextView textViewIconTextButton = Ui.INSTANCE.iconTextButton(this.act, Icons.general$default(Icons.INSTANCE, this.act, icon, null, 4, null), button, Ui.INSTANCE.getACCENT(), new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return StartScreen.message$lambda$55(onClick, (View) obj);
                }
            });
            textViewIconTextButton.setTextColor(-1);
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            layoutParamsLp$default.topMargin = dp(10.0f);
            Unit unit = Unit.INSTANCE;
            card.addView(textViewIconTextButton, layoutParamsLp$default);
        }
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default2.topMargin = dp(6.0f);
        layoutParamsLp$default2.bottomMargin = dp(6.0f);
        Unit unit2 = Unit.INSTANCE;
        col.addView(card, layoutParamsLp$default2);
    }

    static final Unit message$lambda$55(Function0 $onClick, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $onClick.invoke();
        return Unit.INSTANCE;
    }

    private final void showBitmap(ImageView img, Bitmap b) {
        img.setPadding(0, 0, 0, 0);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageBitmap(b);
    }

    private final void bindIcon(ImageView img, String url, String fallback) {
        ArrayList arrayList;
        int pad = dp(this.phone ? 30.0f : 36.0f);
        if (url == null) {
            bindIcon$placeholder(img, pad, this, fallback);
            return;
        }
        String key = "url:" + url;
        Bitmap bmp = ImageLoader.INSTANCE.getKey(key);
        if (bmp != null) {
            showBitmap(img, bmp);
            return;
        }
        bindIcon$placeholder(img, pad, this, fallback);
        Map map = this.iconViews;
        List<ImageView> list = (List<ImageView>) map.get(key);
        if (list == null) {
            arrayList = new ArrayList();
            map.put(key, arrayList);
        } else {
            arrayList = (ArrayList) list;
        }
        arrayList.add(img);
    }

    private static final void bindIcon$placeholder(ImageView $img, int pad, StartScreen this$0, String $fallback) {
        $img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        $img.setPadding(pad, pad, pad, pad);
        $img.setImageDrawable(Icons.general$default(Icons.INSTANCE, this$0.act, $fallback, null, 4, null));
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ LinearLayout card$default(StartScreen startScreen, String str, String str2, String str3, String str4, Function0 function0, Function0 function1, Integer num, int i, Object obj) {
        if ((i & 32) != 0) {
            function1 = null;
        }
        if ((i & 64) != 0) {
            num = null;
        }
        return startScreen.card(str, str2, str3, str4, function0, function1, num);
    }

    private final LinearLayout card(String title, String sub, String iconUrl, String fallback, final Function0<Unit> onClick, final Function0<Unit> onLong, Integer tint) {
        int iBlend;
        int w = dp(this.phone ? 140.0f : 168.0f);
        float r = dp(12.0f);
        LinearLayout linearLayout = new LinearLayout(this.act);
        linearLayout.setOrientation(1);
        linearLayout.setBackground(Ui.INSTANCE.rippleBg(this.act, Ui.INSTANCE.getCARD(), 12.0f));
        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke();
            }
        });
        if (onLong != null) {
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda14
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return StartScreen.card$lambda$62$lambda$61(onLong, view);
                }
            });
        }
        linearLayout.setTag(title);
        linearLayout.setContentDescription(title);
        if (tint != null) {
            iBlend = tint.intValue();
        } else {
            iBlend = Ui.INSTANCE.blend(Ui.INSTANCE.getCARD(), ViewCompat.MEASURED_STATE_MASK, Ui.INSTANCE.getLIGHT() ? 0.06f : 0.22f);
        }
        int base = iBlend;
        FrameLayout frameLayout = new FrameLayout(this.act);
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{tint != null ? Ui.INSTANCE.blend(tint.intValue(), -1, 0.18f) : base, tint != null ? Ui.INSTANCE.blend(tint.intValue(), ViewCompat.MEASURED_STATE_MASK, 0.22f) : base});
        gradientDrawable.setCornerRadii(new float[]{r, r, r, r, 0.0f, 0.0f, 0.0f, 0.0f});
        frameLayout.setBackground(gradientDrawable);
        frameLayout.setClipToOutline(true);
        ImageView img = new ImageView(this.act);
        frameLayout.addView(img, new FrameLayout.LayoutParams(-1, -1));
        bindIcon(img, iconUrl, fallback);
        linearLayout.addView(frameLayout, Ui.lp$default(Ui.INSTANCE, w, w, 0.0f, 4, null));
        LinearLayout linearLayout2 = new LinearLayout(this.act);
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(dp(10.0f), dp(8.0f), dp(10.0f), dp(10.0f));
        TextView textViewText = Ui.INSTANCE.text(this.act, title, 13.0f, Ui.INSTANCE.getTEXT(), true);
        textViewText.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout2.addView(textViewText);
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this.act, sub, 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setEllipsize(TextUtils.TruncateAt.END);
        textViewText$default.setPadding(0, dp(2.0f), 0, 0);
        linearLayout2.addView(textViewText$default);
        linearLayout.addView(linearLayout2, Ui.lp$default(Ui.INSTANCE, w, -2, 0.0f, 4, null));
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(w, -2));
        return linearLayout;
    }

    static final boolean card$lambda$62$lambda$61(Function0 $onLong, View it) {
        $onLong.invoke();
        return true;
    }

    private final FlowLayout grid(LinearLayout col) {
        FlowLayout f = new FlowLayout(this.act);
        f.setGap(dp(12.0f));
        col.addView(f, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        this.grids.add(f);
        return f;
    }

    private final LinearLayout row(LinearLayout col) {
        LinearLayout r = new LinearLayout(this.act);
        r.setOrientation(0);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this.act);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.addView(r);
        col.addView(horizontalScrollView, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        return r;
    }

    private final void addToRow(LinearLayout r, View v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(layoutParams != null ? layoutParams.width : -2, -2);
        layoutParams2.rightMargin = dp(12.0f);
        Unit unit = Unit.INSTANCE;
        r.addView(v, layoutParams2);
    }

    private final View loading(LinearLayout col) {
        ProgressBar progressBar = new ProgressBar(this.act);
        progressBar.setIndeterminate(true);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, dp(36.0f), dp(36.0f), 0.0f, 4, null);
        layoutParamsLp$default.topMargin = dp(20.0f);
        layoutParamsLp$default.gravity = 1;
        Unit unit = Unit.INSTANCE;
        col.addView(progressBar, layoutParamsLp$default);
        return progressBar;
    }

    private final void home() {
        LinearLayout col;
        LinearLayout col2 = scrollColumn();
        greeting(col2);
        heading$default(this, col2, "Start creating", null, 4, null);
        LinearLayout tr = row(col2);
        Map looks = MapsKt.mapOf(TuplesKt.to(PlaceFactory.Template.BASEPLATE, TuplesKt.to(-11764775, "Part_Block")), TuplesKt.to(PlaceFactory.Template.CLASSIC, TuplesKt.to(-11625902, "Surface")), TuplesKt.to(PlaceFactory.Template.EMPTY, TuplesKt.to(-7704623, "Place")));
        PlaceFactory.Template[] templateArrValues = PlaceFactory.Template.values();
        int i = 0;
        for (int length = templateArrValues.length; i < length; length = length) {
            final PlaceFactory.Template t = templateArrValues[i];
            Pair pair = (Pair) looks.get(t);
            if (pair == null) {
                pair = TuplesKt.to(-9737365, "Place");
            }
            int tint = ((Number) pair.component1()).intValue();
            String icon = (String) pair.component2();
            addToRow(tr, card$default(this, t.getTitle(), t.getSubtitle(), null, icon, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda42
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.home$lambda$74(StartScreen.this, t);
                }
            }, null, Integer.valueOf(tint), 32, null));
            i++;
        }
        heading(col2, RECENT, RECENT);
        List<Recents.Entry> listRecentEntries = recentEntries(12);
        if (listRecentEntries.isEmpty()) {
            col = col2;
            message$default(this, col, "Places and files you open show up here, so you can jump right back in.", null, null, null, 28, null);
        } else {
            col = col2;
            LinearLayout rr = row(col);
            Iterator<Pair<Recents.Entry, LinearLayout>> it = recentCards(listRecentEntries).iterator();
            while (it.hasNext()) {
                LinearLayout v = it.next().component2();
                addToRow(rr, v);
            }
        }
        if (!RobloxApi.INSTANCE.isGuest(this.act)) {
            heading(col, MY_GAMES, MY_GAMES);
            final LinearLayout holder = new LinearLayout(this.act);
            holder.setOrientation(1);
            col.addView(holder, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            long uid = RobloxApi.INSTANCE.userId(this.act);
            List<RobloxCloud.Game> listGames = StartCache.INSTANCE.games(this.act, uid, Kind.MINE.getKey());
            if (listGames != null) {
                home$fill(holder, this, listGames);
            } else {
                final View spin = loading(holder);
                final int myGen = this.gen;
                this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda43
                    @Override // java.lang.Runnable
                    public final void run() {
                        StartScreen.home$lambda$77(StartScreen.this, myGen, holder, spin);
                    }
                });
            }
        }
        tip(col);
    }

    static final Unit home$lambda$74(StartScreen this$0, PlaceFactory.Template $t) {
        this$0.host.newFromTemplate($t);
        return Unit.INSTANCE;
    }

    private static final void home$fill(LinearLayout holder, StartScreen this$0, List<RobloxCloud.Game> list) {
        holder.removeAllViews();
        if (list.isEmpty()) {
            message$default(this$0, holder, "You don't own any experiences yet. Make one here and publish it with File → Publish to Roblox As.", null, null, null, 28, null);
            return;
        }
        LinearLayout gr = this$0.row(holder);
        for (Object _gObj : CollectionsKt.take(list, 12)) {
            RobloxCloud.Game g = (RobloxCloud.Game) _gObj;
            this$0.addToRow(gr, gameCard$default(this$0, g, false, 2, null));
        }
    }

    static final void home$lambda$77(final StartScreen this$0, final int $myGen, final LinearLayout $holder, final View $spin) {
        List<RobloxCloud.Game> listLoadGames;
        try {
            listLoadGames = this$0.loadGames(Kind.MINE);
        } catch (Exception e) {
            listLoadGames = null;
        }
        final List<RobloxCloud.Game> list = listLoadGames;
        this$0.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                StartScreen.home$lambda$77$lambda$76($myGen, this$0, $holder, $spin, list);
            }
        });
    }

    static final void home$lambda$77$lambda$76(int $myGen, StartScreen this$0, LinearLayout $holder, View $spin, List $r) {
        if ($myGen == this$0.gen) {
            $holder.removeView($spin);
            if ($r != null) {
                home$fill($holder, this$0, $r);
            } else {
                message$default(this$0, $holder, "Couldn't load your games right now. Open My Games to try again.", null, null, null, 28, null);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:52:0x01af  */
    private final void greeting(LinearLayout col) {
        String hello;
        String name;
        String str;
        String displayName;
        Accounts.Account a = Accounts.INSTANCE.active(this.act);
        int hour = Calendar.getInstance().get(11);
        if (5 <= hour && hour < 12) {
            hello = "Good morning";
        } else {
            if (12 <= hour && hour < 17) {
                hello = "Good afternoon";
            } else {
                hello = 17 <= hour && hour < 22 ? "Good evening" : "Hi there";
            }
        }
        if (a == null || (displayName = a.getDisplayName()) == null) {
            name = null;
        } else {
            String name2 = displayName;
            if (name2.length() == 0) {
                name2 = a.getName();
            }
            name = name2;
        }
        LinearLayout box = new LinearLayout(this.act);
        box.setOrientation(1);
        box.setPadding(dp(18.0f), dp(16.0f), dp(18.0f), dp(16.0f));
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Ui.INSTANCE.blend(Ui.INSTANCE.getBG(), Ui.INSTANCE.getACCENT(), Ui.INSTANCE.getLIGHT() ? 0.2f : 0.45f), Ui.INSTANCE.blend(Ui.INSTANCE.getBG(), -7704623, Ui.INSTANCE.getLIGHT() ? 0.16f : 0.28f)});
        gradientDrawable.setCornerRadius(dp(16.0f));
        box.setBackground(gradientDrawable);
        LinearLayout top = new LinearLayout(this.act);
        top.setOrientation(0);
        top.setGravity(16);
        ImageView img = new ImageView(this.act);
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(1);
        gradientDrawable2.setColor(Ui.INSTANCE.blend(Ui.INSTANCE.getCARD(), -1, 0.1f));
        img.setBackground(gradientDrawable2);
        img.setClipToOutline(true);
        if (a != null) {
            AccountUi.INSTANCE.loadHeadshot(img, a.getUserId());
        } else {
            img.setImageDrawable(Icons.general$default(Icons.INSTANCE, this.act, "Account", null, 4, null));
        }
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, dp(56.0f), dp(56.0f), 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = dp(14.0f);
        Unit unit = Unit.INSTANCE;
        top.addView(img, layoutParamsLp$default);
        LinearLayout t = new LinearLayout(this.act);
        t.setOrientation(1);
        Ui ui = Ui.INSTANCE;
        Activity activity = this.act;
        if (name != null) {
            str = ", " + name;
            if (str == null) {
                str = "";
            }
        } else {
            str = "";
        }
        TextView textViewText = ui.text(activity, hello + str + "!", 20.0f, Ui.INSTANCE.getTEXT(), true);
        textViewText.setSingleLine(false);
        t.addView(textViewText);
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this.act, a == null ? "Sign in to open your Roblox games, or start something new below." : "What are we building today?", 13.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setAlpha(0.8f);
        textViewText$default.setPadding(0, dp(2.0f), 0, 0);
        t.addView(textViewText$default);
        top.addView(t, Ui.INSTANCE.lp(0, -2, 1.0f));
        box.addView(top);
        FlowLayout buttons = new FlowLayout(this.act);
        buttons.setGap(dp(8.0f));
        TextView textViewIconTextButton = Ui.INSTANCE.iconTextButton(this.act, Icons.general$default(Icons.INSTANCE, this.act, "Part_Block", null, 4, null), "New Baseplate", Ui.INSTANCE.getACCENT(), new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StartScreen.greeting$lambda$90(StartScreen.this, (View) obj);
            }
        });
        textViewIconTextButton.setTextColor(-1);
        textViewIconTextButton.setBackground(Ui.INSTANCE.rippleBg(this.act, Ui.INSTANCE.getACCENT(), 18.0f));
        textViewIconTextButton.setPadding(dp(14.0f), dp(8.0f), dp(16.0f), dp(8.0f));
        buttons.addView(textViewIconTextButton);
        TextView textViewIconTextButton$default = Ui.iconTextButton$default(Ui.INSTANCE, this.act, Icons.general$default(Icons.INSTANCE, this.act, "Open", null, 4, null), "Open File", 0, new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return StartScreen.greeting$lambda$92(StartScreen.this, (View) obj);
            }
        }, 8, null);
        textViewIconTextButton$default.setBackground(Ui.INSTANCE.rippleBg(this.act, Ui.INSTANCE.blend(Ui.INSTANCE.getCARD(), Ui.INSTANCE.getBG(), 0.2f), 18.0f));
        textViewIconTextButton$default.setPadding(dp(14.0f), dp(8.0f), dp(16.0f), dp(8.0f));
        buttons.addView(textViewIconTextButton$default);
        if (a == null) {
            TextView textViewIconTextButton$default2 = Ui.iconTextButton$default(Ui.INSTANCE, this.act, Icons.general$default(Icons.INSTANCE, this.act, "Account", null, 4, null), "Sign in", 0, new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda10
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return StartScreen.greeting$lambda$94(StartScreen.this, (View) obj);
                }
            }, 8, null);
            textViewIconTextButton$default2.setBackground(Ui.INSTANCE.rippleBg(this.act, Ui.INSTANCE.blend(Ui.INSTANCE.getCARD(), Ui.INSTANCE.getBG(), 0.2f), 18.0f));
            textViewIconTextButton$default2.setPadding(dp(14.0f), dp(8.0f), dp(16.0f), dp(8.0f));
            buttons.addView(textViewIconTextButton$default2);
        }
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default2.topMargin = dp(14.0f);
        Unit unit2 = Unit.INSTANCE;
        box.addView(buttons, layoutParamsLp$default2);
        LinearLayout.LayoutParams layoutParamsLp$default3 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default3.topMargin = dp(8.0f);
        Unit unit3 = Unit.INSTANCE;
        col.addView(box, layoutParamsLp$default3);
    }

    static final Unit greeting$lambda$90(StartScreen this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.host.newFromTemplate(PlaceFactory.Template.BASEPLATE);
        return Unit.INSTANCE;
    }

    static final Unit greeting$lambda$92(StartScreen this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.host.openFileFromStart();
        return Unit.INSTANCE;
    }

    static final Unit greeting$lambda$94(StartScreen this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AccountUi.signIn$default(AccountUi.INSTANCE, this$0.act, null, 2, null);
        return Unit.INSTANCE;
    }

    private final void tip(LinearLayout col) {
        List tips = CollectionsKt.listOf(new String[]{"Long-press an object in the Explorer to drag it into another parent or the Toolbox.", "File → Playtest Avatar lets you play as R6, R15, your own avatar or any model you save.", "Double-tap a panel's header to float it, or drag it to dock it on another side.", "Hold Ctrl or Shift (on a keyboard) and tap to select more than one part.", "Test → Play Here starts the playtest right where the camera is looking.", "The Toolbox's Creator Store tab can insert free models, meshes, audio and plugins."});
        LinearLayout box = new LinearLayout(this.act);
        box.setOrientation(0);
        box.setGravity(16);
        box.setPadding(dp(14.0f), dp(12.0f), dp(14.0f), dp(12.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Ui.INSTANCE.getCARD());
        gradientDrawable.setCornerRadius(dp(12.0f));
        box.setBackground(gradientDrawable);
        ImageView imageView = new ImageView(this.act);
        imageView.setImageDrawable(Icons.general$default(Icons.INSTANCE, this.act, "Info", null, 4, null));
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, dp(22.0f), dp(22.0f), 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = dp(12.0f);
        Unit unit = Unit.INSTANCE;
        box.addView(imageView, layoutParamsLp$default);
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this.act, "Tip: " + tips.get((int) ((System.currentTimeMillis() / 60000) % ((long) tips.size()))), 12.5f, Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default.setSingleLine(false);
        box.addView(textViewText$default, Ui.INSTANCE.lp(0, -2, 1.0f));
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default2.topMargin = dp(24.0f);
        Unit unit2 = Unit.INSTANCE;
        col.addView(box, layoutParamsLp$default2);
    }

    private final List<Recents.Entry> recentEntries(int limit) {
        long myId = RobloxApi.INSTANCE.userId(this.act);
        Iterable list = Recents.INSTANCE.list(this.act);
        Collection arrayList = new ArrayList();
        for (Object obj : list) {
            Recents.Entry entry = (Recents.Entry) obj;
            if (!entry.isCloud() || entry.getUserId() == 0 || entry.getUserId() == myId || RobloxApi.INSTANCE.isGuest(this.act)) {
                arrayList.add(obj);
            }
        }
        return CollectionsKt.take((List) arrayList, limit);
    }

    private final void recent() {
        LinearLayout col = scrollColumn();
        pageHeader(col, RECENT, "Places and files you opened lately. Long-press one to remove it.", true, null);
        List<Recents.Entry> listRecentEntries = recentEntries(60);
        if (listRecentEntries.isEmpty()) {
            message$default(this, col, "Places and files you open show up here.", null, null, null, 28, null);
            return;
        }
        FlowLayout g = grid(col);
        Iterator it = recentCards(listRecentEntries).iterator();
        while (it.hasNext()) {
            g.addView((View) ((Pair) it.next()).getSecond());
        }
    }

    private final List<Pair<Recents.Entry, LinearLayout>> recentCards(List<Recents.Entry> list) {
        List<Recents.Entry> list2 = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
        for (final Recents.Entry entry : list2) {
            String str = (entry.isCloud() ? "Roblox place" : "On this device") + "  ·  " + ((Object) DateUtils.getRelativeTimeSpanString(entry.getTime()));
            String strRemoveSuffix = StringsKt.removeSuffix(StringsKt.removeSuffix(entry.getName(), (CharSequence) ".rbxl"), (CharSequence) ".rbxlx");
            String str2 = StartCache.INSTANCE.getIcons().get(Long.valueOf(entry.getUniverseId()));
            if (!entry.isCloud()) {
                str2 = null;
            }
            arrayList.add(TuplesKt.to(entry, card$default(this, strRemoveSuffix, str, str2, entry.isCloud() ? "Place" : "Open", new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda33
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.recentCards$lambda$110$lambda$107(StartScreen.this, entry);
                }
            }, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda34
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.recentCards$lambda$110$lambda$109(StartScreen.this, entry);
                }
            }, null, 64, null)));
        }
        final List cards = (List) arrayList;
        Collection arrayList2 = new ArrayList();
        for (Object obj : list) {
            Recents.Entry entry2 = (Recents.Entry) obj;
            if ((!entry2.isCloud() || entry2.getUniverseId() == 0 || StartCache.INSTANCE.getIcons().containsKey(Long.valueOf(entry2.getUniverseId()))) ? false : true) {
                arrayList2.add(obj);
            }
        }
        Iterable iterable = (List) arrayList2;
        Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList3.add(Long.valueOf(((Recents.Entry) it.next()).getUniverseId()));
        }
        final List ids = CollectionsKt.distinct((List) arrayList3);
        if (!ids.isEmpty()) {
            final int myGen = this.gen;
            this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    StartScreen.recentCards$lambda$114(ids, StartScreen.this, myGen, cards);
                }
            });
        }
        return cards;
    }

    static final Unit recentCards$lambda$110$lambda$107(StartScreen this$0, Recents.Entry $e) {
        this$0.openRecent($e);
        return Unit.INSTANCE;
    }

    static final Unit recentCards$lambda$110$lambda$109(final StartScreen this$0, final Recents.Entry $e) {
        Dialogs.INSTANCE.themed(this$0.act).setTitle($e.getName()).setItems(new String[]{"Open", "Remove from Recent"}, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                try {
                    StartScreen.recentCards$lambda$110$lambda$109$lambda$108(this$0, $e, dialogInterface, i);
                } catch (Exception _e) {
                    if (_e instanceof RuntimeException) throw (RuntimeException) _e;
                    throw new RuntimeException(_e);
                }
            }
        }).show();
        return Unit.INSTANCE;
    }

    static final void recentCards$lambda$110$lambda$109$lambda$108(StartScreen this$0, Recents.Entry $e, DialogInterface dialogInterface, int i) {
        try {
        if (i == 0) {
            this$0.openRecent($e);
        } else {
            Recents.INSTANCE.remove(this$0.act, $e);
            this$0.show(this$0.page);
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final void recentCards$lambda$114(List $ids, final StartScreen this$0, final int $myGen, final List $cards) {
        Map<Long, String> _mapEmptyMap;
        try {
            _mapEmptyMap = RobloxCloud.INSTANCE.gameIcons($ids);
        } catch (Exception e) {
            _mapEmptyMap = MapsKt.emptyMap();
        }
        final Map<Long, String> mapEmptyMap = _mapEmptyMap;
        StartCache.INSTANCE.getIcons().putAll(mapEmptyMap);
        this$0.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                StartScreen.recentCards$lambda$114$lambda$113($myGen, this$0, $cards, mapEmptyMap);
            }
        });
    }

    static final void recentCards$lambda$114$lambda$113(int $myGen, StartScreen this$0, List $cards, Map $icons) {
        if ($myGen != this$0.gen) {
            return;
        }
        Iterator it = $cards.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            Recents.Entry e = (Recents.Entry) pair.component1();
            LinearLayout v = (LinearLayout) pair.component2();
            String url = (String) $icons.get(Long.valueOf(e.getUniverseId()));
            if (url != null) {
                View childAt = v.getChildAt(0);
                Intrinsics.checkNotNull(childAt, "null cannot be cast to non-null type android.widget.FrameLayout");
                View childAt2 = ((FrameLayout) childAt).getChildAt(0);
                Intrinsics.checkNotNull(childAt2, "null cannot be cast to non-null type android.widget.ImageView");
                this$0.bindIcon((ImageView) childAt2, url, "Place");
            }
        }
    }

    private final void openRecent(final Recents.Entry e) {
        if (e.isCloud()) {
            if (RobloxApi.INSTANCE.isGuest(this.act)) {
                AccountUi.INSTANCE.signIn(this.act, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda36
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return StartScreen.openRecent$lambda$115(StartScreen.this, e);
                    }
                });
                return;
            } else {
                AccountUi.INSTANCE.openPlace(this.act, this.actions, null, new RobloxCloud.Place(e.getPlaceId(), e.getName(), e.getUniverseId()), null);
                return;
            }
        }
        this.host.openRecentFile(e);
    }

    static final Unit openRecent$lambda$115(StartScreen this$0, Recents.Entry $e) {
        this$0.openRecent($e);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: StartScreen.kt */
    enum Kind {
        MINE("games"),
        SHARED("shared");

        private final String key;
        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Kind> getEntries() {
            return $ENTRIES;
        }

        Kind(String key) {
            this.key = key;
        }

        public final String getKey() {
            return this.key;
        }
    }

    private final List<RobloxCloud.Game> loadGames(Kind kind) throws Exception {
        RobloxCloud.User user = RobloxCloud.INSTANCE.cachedUser(this.act);
        if (user == null) {
            user = RobloxCloud.INSTANCE.currentUser(this.act);
        }
        List<RobloxCloud.Game> listUserGames = kind == Kind.MINE ? RobloxCloud.INSTANCE.userGames(this.act, user) : RobloxCloud.INSTANCE.sharedGames(this.act, user);
        Map<Long, String> icons = StartCache.INSTANCE.getIcons();
        RobloxCloud robloxCloud = RobloxCloud.INSTANCE;
        List<RobloxCloud.Game> list = listUserGames;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(Long.valueOf(((RobloxCloud.Game) it.next()).getUniverseId()));
        }
        Collection arrayList2 = new ArrayList();
        for (Object obj : (List) arrayList) {
            RobloxCloud.User user2 = user;
            if (!StartCache.INSTANCE.getIcons().containsKey(Long.valueOf(((Number) obj).longValue()))) {
                arrayList2.add(obj);
            }
            user = user2;
        }
        icons.putAll(robloxCloud.gameIcons((List) arrayList2));
        StartCache.INSTANCE.putGames(this.act, RobloxApi.INSTANCE.userId(this.act), listUserGames, kind.getKey());
        return listUserGames;
    }

    static /* synthetic */ LinearLayout gameCard$default(StartScreen startScreen, RobloxCloud.Game game, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return startScreen.gameCard(game, z);
    }

    /* JADX WARN: Code duplicated, block: B:10:0x002e  */
    /* JADX WARN: Code duplicated, block: B:12:0x003a  */
    /* JADX WARN: Code duplicated, block: B:14:0x003d  */
    /* JADX WARN: Code duplicated, block: B:15:0x0063  */
    private final LinearLayout gameCard(final RobloxCloud.Game game, boolean showOwner) {
        String owner;
        if (showOwner) {
            if (game.getOwner().length() > 0) {
                owner = "by " + game.getOwner();
            } else {
                if (game.getUpdated().length() > 0) {
                    owner = "Updated " + com.goydevv.studrblx.util.KtCompat.substringBefore(AccountUi.INSTANCE.prettyDate(game.getUpdated()), "  ");
                } else {
                    owner = game.getOwner();
                }
            }
        } else {
            if (game.getUpdated().length() > 0) {
                owner = "Updated " + com.goydevv.studrblx.util.KtCompat.substringBefore(AccountUi.INSTANCE.prettyDate(game.getUpdated()), "  ");
            } else {
                owner = game.getOwner();
            }
        }
        String sub = owner;
        return card$default(this, game.getName(), sub, StartCache.INSTANCE.getIcons().get(Long.valueOf(game.getUniverseId())), "Place", new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda37
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return StartScreen.gameCard$lambda$119(StartScreen.this, game);
            }
        }, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda38
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return StartScreen.gameCard$lambda$120(StartScreen.this, game);
            }
        }, null, 64, null);
    }

    static final Unit gameCard$lambda$119(final StartScreen this$0, RobloxCloud.Game $game) {
        AccountUi.INSTANCE.places(this$0.act, this$0.studio, this$0.actions, $game, new Function2() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return StartScreen.gameCard$lambda$119$lambda$118(this$0, (RobloxCloud.Game) obj, (RobloxCloud.Place) obj2);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit gameCard$lambda$119$lambda$118(StartScreen this$0, RobloxCloud.Game gm, RobloxCloud.Place p) {
        Intrinsics.checkNotNullParameter(gm, "gm");
        Intrinsics.checkNotNullParameter(p, "p");
        AccountUi.INSTANCE.openPlace(this$0.act, this$0.actions, gm, p, null);
        return Unit.INSTANCE;
    }

    static final Unit gameCard$lambda$120(StartScreen this$0, RobloxCloud.Game $game) {
        AccountUi.INSTANCE.places(this$0.act, this$0.studio, this$0.actions, $game, null);
        return Unit.INSTANCE;
    }

    private final void games(final Kind kind) {
        final LinearLayout col = scrollColumn();
        final boolean mine = kind == Kind.MINE;
        pageHeader(col, mine ? MY_GAMES : SHARED, mine ? "Experiences you own. Tap one to edit it; long-press for downloads and version history." : "Experiences other creators added you to as a collaborator.", true, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return StartScreen.games$lambda$121(StartScreen.this);
            }
        });
        if (RobloxApi.INSTANCE.isGuest(this.act)) {
            message$default(this, col, "You're using Roblox Studio as a guest. Sign in to see " + (mine ? "the experiences you own." : "the experiences shared with you."), "Sign in with Roblox", null, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.games$lambda$122(StartScreen.this);
                }
            }, 8, null);
            return;
        }
        final int myGen = this.gen;
        long uid = RobloxApi.INSTANCE.userId(this.act);
        final List<RobloxCloud.Game> listGames = StartCache.INSTANCE.games(this.act, uid, kind.getKey());
        final LinearLayout holder = new LinearLayout(this.act);
        holder.setOrientation(1);
        col.addView(holder, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        String empty = mine ? "You don't own any experiences yet. Create one on create.roblox.com (or File → Publish to Roblox As → Create new game) and it will show up here." : "Nothing has been shared with you yet. When someone adds you as a collaborator on their experience (Game Settings → Permissions / Collaborators), it shows up here.";
        if (listGames != null) {
            showGames(holder, listGames, empty, mine ? false : true);
        }
        View spinner = listGames == null ? loading(col) : null;
        final View spinner2 = spinner;
        final String empty2 = empty;
        this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                StartScreen.games$lambda$129(StartScreen.this, kind, myGen, spinner2, col, holder, empty2, mine, listGames);
            }
        });
    }

    static final Unit games$lambda$121(StartScreen this$0) {
        this$0.show(this$0.page);
        return Unit.INSTANCE;
    }

    static final Unit games$lambda$122(StartScreen this$0) {
        AccountUi.signIn$default(AccountUi.INSTANCE, this$0.act, null, 2, null);
        return Unit.INSTANCE;
    }

    static final void games$lambda$129(final StartScreen this$0, Kind $kind, final int $myGen, final View $spinner, final LinearLayout $col, final LinearLayout $holder, final String $empty, final boolean $mine, final List $cached) {
        Object objM457constructorimpl;
        try {
            objM457constructorimpl = this$0.loadGames($kind);
        } catch (Exception e) {
            objM457constructorimpl = ResultKt.createFailure(e);
        }
        final Object r = objM457constructorimpl;
        this$0.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                StartScreen.games$lambda$129$lambda$128($myGen, this$0, $spinner, r, $col, $holder, $empty, $mine, $cached);
            }
        });
    }

    static final void games$lambda$129$lambda$128(int $myGen, StartScreen this$0, View $spinner, Object $r, LinearLayout $col, final LinearLayout $holder, String $empty, boolean $mine, List $cached) {
        if ($myGen != this$0.gen) {
            return;
        }
        if ($spinner != null) {
            $col.removeView($spinner);
        }
        Throwable thM460exceptionOrNullimpl = (($r) instanceof Result.Failure ? ((Result.Failure) ($r)).exception : null);
        if (thM460exceptionOrNullimpl == null) {
            $holder.removeAllViews();
            CollectionsKt.removeAll((List) this$0.grids, new Function1() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda32
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Boolean.valueOf(StartScreen.games$lambda$129$lambda$128$lambda$126$lambda$125($holder, (FlowLayout) obj));
                }
            });
            this$0.showGames($holder, (List) $r, $empty, !$mine);
            this$0.applyFilter();
            return;
        }
        if ($cached == null) {
            this$0.error($col, thM460exceptionOrNullimpl);
        }
    }

    static final boolean games$lambda$129$lambda$128$lambda$126$lambda$125(LinearLayout $holder, FlowLayout it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getParent() == null || it.getParent() == $holder;
    }

    private final void error(LinearLayout col, Throwable e) {
        if (!(e instanceof AuthRequiredException)) {
            String message = e.getMessage();
            message(col, "Couldn't load this from Roblox.\n" + (message != null ? message : e), "Try again", "PluginsFolder", new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda31
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.error$lambda$131(StartScreen.this);
                }
            });
        } else {
            String message2 = e.getMessage();
            if (message2 == null) {
                message2 = "Sign in again.";
            }
            message$default(this, col, message2, "Sign in", null, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda30
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.error$lambda$130(StartScreen.this);
                }
            }, 8, null);
        }
    }

    static final Unit error$lambda$130(StartScreen this$0) {
        AccountUi.signIn$default(AccountUi.INSTANCE, this$0.act, null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit error$lambda$131(StartScreen this$0) {
        this$0.show(this$0.page);
        return Unit.INSTANCE;
    }

    static /* synthetic */ void showGames$default(StartScreen startScreen, LinearLayout linearLayout, List list, String str, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = false;
        }
        startScreen.showGames(linearLayout, list, str, z);
    }

    private final void showGames(LinearLayout col, List<RobloxCloud.Game> list, String empty, boolean showOwner) {
        if (list.isEmpty()) {
            message$default(this, col, empty, null, null, null, 28, null);
            return;
        }
        FlowLayout g = grid(col);
        for (RobloxCloud.Game game : list) {
            g.addView(gameCard(game, showOwner));
        }
    }

    private final void groupGames() {
        final LinearLayout col = scrollColumn();
        pageHeader(col, GROUPS, "Experiences in your groups that you can edit.", true, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda39
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return StartScreen.groupGames$lambda$132(StartScreen.this);
            }
        });
        if (RobloxApi.INSTANCE.isGuest(this.act)) {
            message$default(this, col, "You're using Roblox Studio as a guest. Sign in to see the group experiences you can edit.", "Sign in with Roblox", null, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda40
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.groupGames$lambda$133(StartScreen.this);
                }
            }, 8, null);
            return;
        }
        final int myGen = this.gen;
        final RobloxCloud.User user = RobloxCloud.INSTANCE.cachedUser(this.act);
        final long uid = RobloxApi.INSTANCE.userId(this.act);
        final GroupsView view = new GroupsView(this, col);
        final Pair<Integer, List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>>> pairGroups = StartCache.INSTANCE.groups(this.act, uid);
        if (pairGroups != null) {
            view.set(pairGroups.getSecond(), pairGroups.getFirst().intValue(), false);
        } else {
            GroupsView.status$default(view, "Loading your groups…", true, null, 4, null);
        }
        this.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                StartScreen.groupGames$lambda$150(StartScreen.this, user, pairGroups, uid, myGen, view, col);
            }
        });
    }

    static final Unit groupGames$lambda$132(StartScreen this$0) {
        this$0.show(this$0.page);
        return Unit.INSTANCE;
    }

    static final Unit groupGames$lambda$133(StartScreen this$0) {
        AccountUi.signIn$default(AccountUi.INSTANCE, this$0.act, null, 2, null);
        return Unit.INSTANCE;
    }

    static final void groupGames$lambda$150(StartScreen this$0, RobloxCloud.User $user, final Pair $cached, long $uid, final int $myGen, final GroupsView $view, final LinearLayout $col) {
        final int missing;
        final StartScreen startScreen = this$0;
        try {
            final RobloxCloud.GroupList gl = RobloxCloud.INSTANCE.allGroups(startScreen.act, $user == null ? RobloxCloud.INSTANCE.currentUser(startScreen.act) : $user);
            final List<RobloxCloud.Group> groupList = CollectionsKt.take(gl.getGroups(), 150);
            final int i = $myGen;
            final GroupsView groupsView = $view;
            startScreen.net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda26
                @Override // java.lang.Runnable
                public final void run() {
                    StartScreen.groupGames$lambda$150$lambda$137(groupList, startScreen, i, groupsView);
                }
            });
            final ConcurrentHashMap found = new ConcurrentHashMap();
            final ConcurrentHashMap.KeySetView failed = ConcurrentHashMap.newKeySet();
            final AtomicInteger checked = new AtomicInteger();
            AtomicReference authError = new AtomicReference();
            ExecutorService pool = Executors.newFixedThreadPool(4);
            Iterable iterableEmptyList = $cached != null ? (List) $cached.getSecond() : null;
            if (iterableEmptyList == null) {
                iterableEmptyList = CollectionsKt.emptyList();
            }
            Iterable<Pair> iterable = iterableEmptyList;
            Map linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterable, 10)), 16));
            for (Pair pair : iterable) {
                Iterable iterable2 = iterable;
                Pair pair2 = TuplesKt.to(Long.valueOf(((RobloxCloud.Group) pair.getFirst()).getId()), pair.getSecond());
                linkedHashMap.put(pair2.getFirst(), pair2.getSecond());
                iterable = iterable2;
            }
            final AtomicReference authError2 = authError;
            final Map previous = linkedHashMap;
            for (final RobloxCloud.Group gp : groupList) {
                final int i2 = i;
                final StartScreen startScreen2 = startScreen;
                try {
                    Runnable runnable = new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda27
                        @Override // java.lang.Runnable
                        public final void run() {
                            StartScreen.groupGames$lambda$150$lambda$144(authError2, i2, startScreen2, found, gp, gl, failed, checked, groupList, previous, $cached, groupsView);
                        }
                    };
                    pool.execute(runnable);
                } catch (Exception e) {
                    final Exception e2 = e;
                    startScreen.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda29
                        @Override // java.lang.Runnable
                        public final void run() {
                            StartScreen.groupGames$lambda$150$lambda$149($myGen, startScreen, $view, $cached, $col, e2);
                        }
                    });
                    return;
                }
            }
            Map previous3 = previous;
            ConcurrentHashMap found3 = found;
            ConcurrentHashMap.KeySetView failed3 = failed;
            pool.shutdown();
            pool.awaitTermination(4L, TimeUnit.MINUTES);
            Exception exc = (Exception) authError2.get();
            if (exc != null) {
                throw exc;
            }
            final List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> listGroupGames$lambda$150$snapshot = groupGames$lambda$150$snapshot(groupList, found3, previous3, failed3);
            StartCache.INSTANCE.putGroups(startScreen.act, $uid, groupList.size(), listGroupGames$lambda$150$snapshot);
            Iterable indices = CollectionsKt.getIndices(groupList);
            if ((indices instanceof Collection) && ((Collection) indices).isEmpty()) {
                missing = 0;
            } else {
                int i3 = 0;
                Iterator it = indices.iterator();
                while (it.hasNext()) {
                    int iNextInt = ((IntIterator) it).nextInt();
                    if ((failed3.contains(Long.valueOf(((RobloxCloud.Group) groupList.get(iNextInt)).getId())) && found3.get(Long.valueOf(((RobloxCloud.Group) groupList.get(iNextInt)).getId())) == null) && (i3 = i3 + 1) < 0) {
                        CollectionsKt.throwCountOverflow();
                    }
                }
                missing = i3;
            }
            startScreen.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    StartScreen.groupGames$lambda$150$lambda$148($myGen, startScreen, $view, listGroupGames$lambda$150$snapshot, groupList, missing);
                }
            });
        } catch (Exception e3) {
            final Exception e2 = e3;
            startScreen.main.post(new Runnable() {
                @Override
                public final void run() {
                    StartScreen.groupGames$lambda$150$lambda$149($myGen, startScreen, $view, $cached, $col, e2);
                }
            });
        }
    }

    static final void groupGames$lambda$150$lambda$137(List $groupList, final StartScreen this$0, final int $myGen, final GroupsView $view) {
        Map<Long, String> mapEmptyMap;
        try {
            RobloxCloud robloxCloud = RobloxCloud.INSTANCE;
            List list = $groupList;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
            Iterator it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(Long.valueOf(((RobloxCloud.Group) it.next()).getId()));
            }
            Collection arrayList2 = new ArrayList();
            for (Object obj : (List) arrayList) {
                if (!StartCache.INSTANCE.getGroupIcons().containsKey(Long.valueOf(((Number) obj).longValue()))) {
                    arrayList2.add(obj);
                }
            }
            mapEmptyMap = robloxCloud.groupIcons(CollectionsKt.take((List) arrayList2, 100));
        } catch (Exception e) {
            mapEmptyMap = MapsKt.emptyMap();
        }
        StartCache.INSTANCE.getGroupIcons().putAll(mapEmptyMap);
        if (mapEmptyMap.isEmpty()) {
            return;
        }
        this$0.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                StartScreen.groupGames$lambda$150$lambda$137$lambda$136($myGen, this$0, $view);
            }
        });
    }

    static final void groupGames$lambda$150$lambda$137$lambda$136(int $myGen, StartScreen this$0, GroupsView $view) {
        if ($myGen == this$0.gen) {
            $view.redrawChips();
        }
    }

    /* JADX WARN: Code duplicated, block: B:20:0x008c  */
    private static final List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> groupGames$lambda$150$snapshot(List<RobloxCloud.Group> list, ConcurrentHashMap<Long, List<RobloxCloud.Game>> concurrentHashMap, Map<Long, ? extends List<RobloxCloud.Game>> map, ConcurrentHashMap.KeySetView<Long, Boolean> keySetView) {
        Iterable iterable;
        int i;
        Pair pair;
        List<RobloxCloud.Group> list2 = list;
        int i2 = 0;
        Collection arrayList = new ArrayList();
        for (RobloxCloud.Group group : list2) {
            List<RobloxCloud.Game> list3 = concurrentHashMap.get(Long.valueOf(group.getId()));
            if (list3 == null) {
                list3 = map.get(Long.valueOf(group.getId()));
                if (list3 != null) {
                    iterable = list2;
                    i = i2;
                    if (!keySetView.contains(Long.valueOf(group.getId()))) {
                        list3 = null;
                    }
                } else {
                    iterable = list2;
                    i = i2;
                    list3 = null;
                }
            } else {
                iterable = list2;
                i = i2;
            }
            if (list3 == null) {
                pair = null;
            } else {
                if (list3.isEmpty()) {
                    list3 = null;
                }
                if (list3 != null) {
                    pair = TuplesKt.to(group, list3);
                } else {
                    pair = null;
                }
            }
            if (pair != null) {
                arrayList.add(pair);
            }
            i2 = i;
            list2 = (List) iterable;
        }
        return (List) arrayList;
    }

    static final void groupGames$lambda$150$lambda$144(AtomicReference $authError, final int $myGen, final StartScreen this$0, ConcurrentHashMap $found, RobloxCloud.Group $gp, RobloxCloud.GroupList $gl, ConcurrentHashMap.KeySetView $failed, AtomicInteger $checked, final List $groupList, Map $previous, final Pair $cached, final GroupsView $view) {
        RobloxCloud.Group group;
        if ($authError.get() == null && $myGen == this$0.gen) {
            try {
                group = $gp;
                try {
                    $found.put(Long.valueOf($gp.getId()), RobloxCloud.INSTANCE.editableGroupGames(this$0.act, group, $gl.getStudioListed().contains(Long.valueOf($gp.getId()))));
                } catch (AuthRequiredException e) {
                    $authError.set(e);
                } catch (Exception e2) {
                    $failed.add(Long.valueOf(group.getId()));
                }
            } catch (Exception e4) {
                if (e4 instanceof AuthRequiredException) {
                    $authError.set(e4);
                } else {
                    group = $gp;
                }
            }
            final int n = $checked.incrementAndGet();
            final List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>> listGroupGames$lambda$150$snapshot = groupGames$lambda$150$snapshot($groupList, $found, $previous, $failed);
            this$0.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    StartScreen.groupGames$lambda$150$lambda$144$lambda$143($myGen, this$0, $cached, $view, listGroupGames$lambda$150$snapshot, $groupList, n);
                }
            });
        }
    }

    static final void groupGames$lambda$150$lambda$144$lambda$143(int $myGen, StartScreen this$0, Pair $cached, GroupsView $view, List $snap, List $groupList, int $n) {
        if ($myGen != this$0.gen) {
            return;
        }
        if ($cached == null) {
            $view.set($snap, $groupList.size(), false);
        }
        if ($n < $groupList.size()) {
            GroupsView.status$default($view, "Checked " + $n + " of " + $groupList.size() + " groups…", true, null, 4, null);
        }
    }

    static final void groupGames$lambda$150$lambda$148(int $myGen, final StartScreen this$0, GroupsView $view, List $result, List $groupList, int $missing) {
        if ($myGen != this$0.gen) {
            return;
        }
        $view.set($result, $groupList.size(), true);
        if ($missing > 0) {
            $view.status("Couldn't check " + $missing + " group" + ($missing == 1 ? "" : "s") + " — Roblox was busy. Tap to try again.", false, new Function0() { // from class: com.goydevv.studrblx.ui.StartScreen$$ExternalSyntheticLambda50
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return StartScreen.groupGames$lambda$150$lambda$148$lambda$147(this$0);
                }
            });
        }
    }

    static final Unit groupGames$lambda$150$lambda$148$lambda$147(StartScreen this$0) {
        this$0.show(this$0.page);
        return Unit.INSTANCE;
    }

    static final void groupGames$lambda$150$lambda$149(int $myGen, StartScreen this$0, GroupsView $view, Pair $cached, LinearLayout $col, Exception $e) {
        if ($myGen == this$0.gen) {
            GroupsView.status$default($view, null, false, null, 4, null);
            if ($cached == null) {
                this$0.error($col, $e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: StartScreen.kt */
    final class GroupsView {
        private final LinearLayout area;
        private final LinearLayout chips;
        private Map<Long, ? extends List<RobloxCloud.Game>> gamesOf;
        private List<RobloxCloud.Group> groups;
        private List<Long> shownGames;
        private long shownGroup;
        private final LinearLayout statusRow;
        private final ProgressBar statusSpin;
        private final TextView statusText;
        final /* synthetic */ StartScreen this$0;

        public GroupsView(StartScreen this$0, LinearLayout col) {
            Intrinsics.checkNotNullParameter(col, "col");
            this.this$0 = this$0;
            LinearLayout linearLayout = new LinearLayout(this.this$0.act);
            StartScreen startScreen = StartScreen.this;
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            linearLayout.setPadding(0, startScreen.dp(6.0f), 0, startScreen.dp(6.0f));
            this.statusRow = linearLayout;
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this.this$0.act, "", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default.setSingleLine(false);
            this.statusText = textViewText$default;
            ProgressBar progressBar = new ProgressBar(this.this$0.act);
            progressBar.setIndeterminate(true);
            this.statusSpin = progressBar;
            LinearLayout linearLayout2 = new LinearLayout(this.this$0.act);
            linearLayout2.setOrientation(0);
            this.chips = linearLayout2;
            LinearLayout linearLayout3 = new LinearLayout(this.this$0.act);
            linearLayout3.setOrientation(1);
            this.area = linearLayout3;
            this.groups = CollectionsKt.emptyList();
            this.gamesOf = MapsKt.emptyMap();
            this.shownGames = CollectionsKt.emptyList();
            LinearLayout linearLayout4 = this.statusRow;
            ProgressBar progressBar2 = this.statusSpin;
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, this.this$0.dp(16.0f), this.this$0.dp(16.0f), 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = this.this$0.dp(8.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout4.addView(progressBar2, layoutParamsLp$default);
            this.statusRow.addView(this.statusText, Ui.INSTANCE.lp(0, -2, 1.0f));
            col.addView(this.statusRow, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this.this$0.act);
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
            horizontalScrollView.addView(this.chips);
            LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default2.topMargin = this.this$0.dp(4.0f);
            Unit unit2 = Unit.INSTANCE;
            col.addView(horizontalScrollView, layoutParamsLp$default2);
            col.addView(this.area, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            status$default(this, null, false, null, 4, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void status$default(GroupsView groupsView, String str, boolean z, Function0 function0, int i, Object obj) {
            if ((i & 4) != 0) {
                function0 = null;
            }
            groupsView.status(str, z, function0);
        }

        public final void status(String text, boolean spin, final Function0<Unit> onTap) {
            this.statusRow.setVisibility(text == null ? 8 : 0);
            this.statusText.setText(text != null ? text : "");
            this.statusText.setTextColor(onTap != null ? this.this$0.getLinkColor() : Ui.INSTANCE.getDIM());
            LinearLayout linearLayout = this.statusRow;
            if (onTap != null) {
                linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.StartScreen$GroupsView$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        onTap.invoke();
                    }
                });
            } else {
                linearLayout.setOnClickListener(null);
                this.statusRow.setClickable(false);
            }
            this.statusSpin.setVisibility(spin ? 0 : 8);
        }

        /* JADX WARN: Code duplicated, block: B:42:0x0112  */
        public final void set(List<? extends Pair<RobloxCloud.Group, ? extends List<RobloxCloud.Game>>> editable, int inGroups, boolean done) {
            boolean z;
            Intrinsics.checkNotNullParameter(editable, "editable");
            List<? extends Pair<RobloxCloud.Group, ? extends List<RobloxCloud.Game>>> list = editable;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
            Iterator it = list.iterator();
            while (it.hasNext()) {
                arrayList.add((RobloxCloud.Group) ((Pair) it.next()).getFirst());
            }
            this.groups = (List) arrayList;
            List<? extends Pair<RobloxCloud.Group, ? extends List<RobloxCloud.Game>>> list2 = editable;
            Map linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(list2, 10)), 16));
            Iterator it2 = list2.iterator();
            while (it2.hasNext()) {
                Pair pair = (Pair) it2.next();
                Pair pair2 = TuplesKt.to(Long.valueOf(((RobloxCloud.Group) pair.getFirst()).getId()), pair.getSecond());
                linkedHashMap.put(pair2.getFirst(), pair2.getSecond());
            }
            this.gamesOf = linkedHashMap;
            if (done) {
                status$default(this, null, false, null, 4, null);
            }
            if (this.groups.isEmpty()) {
                this.chips.removeAllViews();
                if (done) {
                    this.area.removeAllViews();
                    StartScreen.message$default(this.this$0, this.area, inGroups == 0 ? "You aren't in any groups yet." : "None of your groups have experiences you can edit. Ask a group owner to give your role permission to edit group experiences.", null, null, null, 28, null);
                    return;
                }
                return;
            }
            if (this.this$0.selectedGroup == 0) {
                this.this$0.selectedGroup = ((RobloxCloud.Group) CollectionsKt.first((List) this.groups)).getId();
            } else {
                Iterable iterable = this.groups;
                StartScreen startScreen = StartScreen.this;
                if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                    Iterator it3 = iterable.iterator();
                    while (true) {
                        if (it3.hasNext()) {
                            if (((RobloxCloud.Group) it3.next()).getId() == startScreen.selectedGroup) {
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
                    this.this$0.selectedGroup = ((RobloxCloud.Group) CollectionsKt.first((List) this.groups)).getId();
                }
            }
            drawChips();
            Iterable iterableEmptyList = (List) this.gamesOf.get(Long.valueOf(this.this$0.selectedGroup));
            if (iterableEmptyList == null) {
                iterableEmptyList = CollectionsKt.emptyList();
            }
            Iterable iterable2 = iterableEmptyList;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
            Iterator it4 = iterable2.iterator();
            while (it4.hasNext()) {
                arrayList2.add(Long.valueOf(((RobloxCloud.Game) it4.next()).getUniverseId()));
            }
            List now = (List) arrayList2;
            if (this.shownGroup == this.this$0.selectedGroup && Intrinsics.areEqual(now, this.shownGames)) {
                return;
            }
            Iterable iterable3 = this.groups;
            StartScreen startScreen2 = this.this$0;
            for (Object obj : iterable3) {
                if (((RobloxCloud.Group) obj).getId() == startScreen2.selectedGroup) {
                    select((RobloxCloud.Group) obj);
                    return;
                }
            }
            throw new NoSuchElementException("Collection contains no element matching the predicate.");
        }

        public final void redrawChips() {
            if (this.groups.isEmpty()) {
                return;
            }
            drawChips();
        }

        private final void drawChips() {
            ArrayList arrayList;
            this.chips.removeAllViews();
            for (final RobloxCloud.Group x : this.groups) {
                boolean on = x.getId() == this.this$0.selectedGroup;
                LinearLayout chip = new LinearLayout(this.this$0.act);
                StartScreen startScreen = StartScreen.this;
                chip.setOrientation(0);
                chip.setGravity(16);
                chip.setPadding(startScreen.dp(5.0f), startScreen.dp(5.0f), startScreen.dp(14.0f), startScreen.dp(5.0f));
                Ui ui = Ui.INSTANCE;
                Activity activity = startScreen.act;
                Ui ui2 = Ui.INSTANCE;
                chip.setBackground(ui.rippleBg(activity, on ? ui2.getACCENT() : ui2.getCARD(), 18.0f));
                chip.setClickable(true);
                chip.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.StartScreen$GroupsView$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        GroupsView.this.select(x);
                    }
                });
                ImageView ic = new ImageView(this.this$0.act);
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(1);
                gradientDrawable.setColor(Ui.INSTANCE.getHOVER());
                ic.setBackground(gradientDrawable);
                ic.setClipToOutline(true);
                ic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                String url = StartCache.INSTANCE.getGroupIcons().get(Long.valueOf(x.getId()));
                Bitmap bmp = url != null ? ImageLoader.INSTANCE.getKey("url:" + url) : null;
                if (bmp != null) {
                    ic.setImageBitmap(bmp);
                } else {
                    ic.setImageDrawable(Icons.general$default(Icons.INSTANCE, this.this$0.act, "Group", null, 4, null));
                    ic.setPadding(this.this$0.dp(4.0f), this.this$0.dp(4.0f), this.this$0.dp(4.0f), this.this$0.dp(4.0f));
                    if (url != null) {
                        Map map = this.this$0.iconViews;
                        String str = "url:" + url;
                        Object obj = map.get(str);
                        if (obj == null) {
                            arrayList = new ArrayList();
                            map.put(str, arrayList);
                        } else {
                            arrayList = (ArrayList) obj;
                        }
                        ((List) arrayList).add(ic);
                    }
                }
                LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, this.this$0.dp(26.0f), this.this$0.dp(26.0f), 0.0f, 4, null);
                layoutParamsLp$default.rightMargin = this.this$0.dp(8.0f);
                Unit unit = Unit.INSTANCE;
                chip.addView(ic, layoutParamsLp$default);
                chip.addView(Ui.INSTANCE.text(this.this$0.act, x.getName(), 13.0f, on ? -1 : Ui.INSTANCE.getTEXT(), on));
                LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
                StartScreen startScreen2 = this.this$0;
                layoutParamsLp$default2.rightMargin = startScreen2.dp(8.0f);
                layoutParamsLp$default2.bottomMargin = startScreen2.dp(6.0f);
                Unit unit2 = Unit.INSTANCE;
                this.chips.addView(chip, layoutParamsLp$default2);
            }
        }

        public final void select(final RobloxCloud.Group gp) {
            Intrinsics.checkNotNullParameter(gp, "gp");
            this.this$0.selectedGroup = gp.getId();
            this.shownGroup = gp.getId();
            List<RobloxCloud.Game> listEmptyList = this.gamesOf.get(Long.valueOf(gp.getId()));
            if (listEmptyList == null) {
                listEmptyList = CollectionsKt.emptyList();
            }
            final List<RobloxCloud.Game> list = listEmptyList;
            List<RobloxCloud.Game> list2 = list;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
            Iterator it = list2.iterator();
            while (it.hasNext()) {
                arrayList.add(Long.valueOf(((RobloxCloud.Game) it.next()).getUniverseId()));
            }
            this.shownGames = (List) arrayList;
            drawChips();
            this.area.removeAllViews();
            this.this$0.grids.clear();
            LinearLayout linearLayout = this.area;
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this.this$0.act, (gp.getRole().length() > 0 ? "Your role: " + gp.getRole() + "  ·  " : "") + list.size() + " experience" + (list.size() != 1 ? "s" : ""), 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            StartScreen startScreen = StartScreen.this;
            textViewText$default.setPadding(0, startScreen.dp(6.0f), 0, startScreen.dp(10.0f));
            linearLayout.addView(textViewText$default);
            final LinearLayout linearLayout2 = new LinearLayout(this.this$0.act);
            linearLayout2.setOrientation(1);
            this.area.addView(linearLayout2, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
            StartScreen.showGames$default(this.this$0, linearLayout2, list, "This group has no experiences you can edit.", false, 8, null);
            StartScreen.this.applyFilter();
            List<RobloxCloud.Game> list3 = list;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list3, 10));
            Iterator it2 = list3.iterator();
            while (it2.hasNext()) {
                arrayList2.add(Long.valueOf(((RobloxCloud.Game) it2.next()).getUniverseId()));
            }
            Collection arrayList3 = new ArrayList();
            for (Object obj : (List) arrayList2) {
                if (!StartCache.INSTANCE.getIcons().containsKey(Long.valueOf(((Number) obj).longValue()))) {
                    arrayList3.add(obj);
                }
            }
            final List missing = (List) arrayList3;
            if (missing.isEmpty()) {
                return;
            }
            final int myGen = this.this$0.gen;
            ExecutorService executorService = this.this$0.net;
            final StartScreen startScreen2 = this.this$0;
            executorService.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$GroupsView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StartScreen.GroupsView.select$lambda$28(missing, startScreen2, myGen, gp, linearLayout2, list);
                }
            });
        }

        static final void select$lambda$28(List $missing, final StartScreen this$0, final int $myGen, final RobloxCloud.Group $gp, final LinearLayout $holder, final List $mine) {
            Map<Long, String> mapEmptyMap;
            try {
                mapEmptyMap = RobloxCloud.INSTANCE.gameIcons($missing);
            } catch (Exception e) {
                mapEmptyMap = MapsKt.emptyMap();
            }
            final Map<Long, String> map = mapEmptyMap;
            StartCache.INSTANCE.getIcons().putAll(map);
            this$0.main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.StartScreen$GroupsView$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    StartScreen.GroupsView.select$lambda$28$lambda$27($myGen, this$0, $gp, map, $holder, $mine);
                }
            });
        }

        static final void select$lambda$28$lambda$27(int $myGen, StartScreen this$0, RobloxCloud.Group $gp, Map $icons, LinearLayout $holder, List $mine) {
            if ($myGen == this$0.gen && this$0.selectedGroup == $gp.getId()) {
                if ($icons.isEmpty()) {
                    return;
                }
                $holder.removeAllViews();
                this$0.grids.clear();
                StartScreen.showGames$default(this$0, $holder, $mine, "This group has no experiences you can edit.", false, 8, null);
                this$0.applyFilter();
            }
        }
    }
}
