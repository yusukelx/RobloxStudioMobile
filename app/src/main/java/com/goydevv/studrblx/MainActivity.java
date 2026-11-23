package com.goydevv.studrblx;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.PlaceFactory;
import com.goydevv.studrblx.core.PluginButton;
import com.goydevv.studrblx.core.Recents;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.SolidModeling;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.core.Tool;
import com.goydevv.studrblx.engine.Avatar;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.DataModel;
import com.goydevv.studrblx.model.DataTypesKt;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Enums;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.Accounts;
import com.goydevv.studrblx.net.AssetFetcher;
import com.goydevv.studrblx.net.CloudPlace;
import com.goydevv.studrblx.net.ImageLoader;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.net.WebPermissions;
import com.goydevv.studrblx.render.MaterialTextures;
import com.goydevv.studrblx.ui.AboutDialog;
import com.goydevv.studrblx.ui.AccountUi;
import com.goydevv.studrblx.ui.AvatarUi;
import com.goydevv.studrblx.ui.Dialogs;
import com.goydevv.studrblx.ui.DockLayout;
import com.goydevv.studrblx.ui.ExplorerPanel;
import com.goydevv.studrblx.ui.Fonts;
import com.goydevv.studrblx.ui.Icons;
import com.goydevv.studrblx.ui.MainActions;
import com.goydevv.studrblx.ui.OutputPanel;
import com.goydevv.studrblx.ui.PropertiesPanel;
import com.goydevv.studrblx.ui.ScriptEditor;
import com.goydevv.studrblx.ui.SettingsDialog;
import com.goydevv.studrblx.ui.StartScreen;
import com.goydevv.studrblx.ui.ToolboxPanel;
import com.goydevv.studrblx.ui.Ui;
import com.goydevv.studrblx.ui.ViewportView;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.json.JSONException;

/* JADX INFO: compiled from: MainActivity.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class MainActivity extends AppCompatActivity implements MainActions, Studio.Listener, StartScreen.Host {
    private LinearLayout accountChip;
    private Instance activeScript;
    private DockLayout dock;
    private FrameLayout editorHost;
    private ExplorerPanel explorer;
    private LinearLayout groupRow;
    private Boolean hiddenBeforePlay;
    private ImageView hideBtn;
    private OutputPanel output;
    private Function1<OutputLog.Entry, Unit> outTabLogListener;
    private Function0<byte[]> pendingSave;
    private boolean pendingSaveIsPlace;
    private TextView playButton;
    private PropertiesPanel properties;
    private LinearLayout ribbon;
    private View ribbonDivider;
    private HorizontalScrollView ribbonScroll;
    private FrameLayout rootFrame;
    private LinearLayout scriptTabs;
    private HorizontalScrollView scriptTabsScroll;
    private StartScreen startScreen;
    private Studio studio;
    private LinearLayout tabRow;
    private TextView titleView;
    private ToolboxPanel toolbox;
    private ViewportView viewport;

    /* JADX INFO: renamed from: layoutPrefs$delegate, reason: from kotlin metadata */
    private final Lazy layoutPrefs = LazyKt.lazy(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda148
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return MainActivity.this.getSharedPreferences("layout", 0);
        }
    });
    private final Function0<Unit> iconListener = new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda149
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return MainActivity.iconListener$lambda$2(MainActivity.this);
        }
    };
    private final Function0<Unit> accountListener = new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda150
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return MainActivity.accountListener$lambda$4(MainActivity.this);
        }
    };
    private final List<String> ribbonTabs = CollectionsKt.listOf(new String[]{StartScreen.HOME, "Model", "Avatar", "UI", "Test", "View", "Plugins", "Script"});
    private String ribbonTab = StartScreen.HOME;
    private final IdentityHashMap<Instance, ScriptEditor> openEditors = new IdentityHashMap<>();
    private final ArrayList<Instance> editorOrder = new ArrayList<>();
    private final HashSet<Integer> keysDown = new HashSet<>();
    private OpenMode openMode = OpenMode.PLACE;
    private final ActivityResultLauncher<String[]> openLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda151
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            MainActivity.openLauncher$lambda$5(MainActivity.this, (Uri) obj);
        }
    });
    private final ActivityResultLauncher<String> createLauncher = registerForActivityResult(new ActivityResultContracts.CreateDocument("application/octet-stream"), new ActivityResultCallback() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda152
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            MainActivity.createLauncher$lambda$6(MainActivity.this, (Uri) obj);
        }
    });
    private final Function3<String, String, Function1<? super Boolean, Unit>, Unit> webAsker = new Function3() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda153
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return MainActivity.webAsker$lambda$33(MainActivity.this, (String) obj, (String) obj2, (Function1) obj3);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: MainActivity.kt */
    enum OpenMode {
        PLACE,
        TOOLBOX,
        PLUGIN,
        ICONS;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<OpenMode> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: MainActivity.kt */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[OpenMode.values().length];
            try {
                iArr[OpenMode.ICONS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[OpenMode.PLUGIN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[OpenMode.TOOLBOX.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[OpenMode.PLACE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onPropsChanged() {
        Studio.Listener.super.onPropsChanged();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onTreeChanged() {
        Studio.Listener.super.onTreeChanged();
    }

    private final SharedPreferences getLayoutPrefs() {
        return (SharedPreferences) this.layoutPrefs.getValue();
    }

    static final Unit iconListener$lambda$2(final MainActivity this$0) {
        this$0.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                this$0.refreshIcons();
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit accountListener$lambda$4(final MainActivity this$0) {
        this$0.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                this$0.refreshAccountChip();
            }
        });
        return Unit.INSTANCE;
    }

    static final void openLauncher$lambda$5(MainActivity this$0, Uri uri) {
        if (uri != null) {
            this$0.onFilePicked(uri);
        }
    }

    static final void createLauncher$lambda$6(MainActivity this$0, Uri uri) {
        if (uri != null) {
            this$0.writeTo(uri);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        Studio studio;
        setTheme(com.goydevv.studrblx.R.style.Theme_Mobilestudiotest);
        super.onCreate(savedInstanceState);
        try {
            String[] prevCrash = StudioApp.checkPreviousCrashReport(this);
            if (prevCrash != null && prevCrash.length > 0 && prevCrash[0] != null && prevCrash[0].length() > 0) {
                android.content.Intent ci = new android.content.Intent(this, CrashActivity.class);
                ci.putExtra(CrashActivity.EXTRA_TRACE, prevCrash[0]);
                if (prevCrash.length > 1 && prevCrash[1] != null) {
                    ci.putExtra(CrashActivity.EXTRA_SAVED_PATH, prevCrash[1]);
                }
                ci.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ci);
                finish();
                return;
            }
        } catch (Throwable ignored) {
        }
        StudioApp.logBreadcrumb("MainActivity.onCreate started");
        try {
            com.goydevv.studrblx.net.AssetFetcher.INSTANCE.init(getApplicationContext());
            long[] warmAnims = new long[]{507766666L, 507766951L, 913402848L, 913376220L, 507765000L, 507767968L, 507765644L, 180435571L, 180435792L, 180426354L, 125750702L, 180436148L, 180436334L};
            for (long aid : warmAnims) {
                com.goydevv.studrblx.engine.AnimLibrary.INSTANCE.get(aid);
            }
        } catch (Throwable ignored) {
        }
        Settings.INSTANCE.init(this);
        Ui.INSTANCE.applyTheme(Settings.INSTANCE.getTheme());
        getWindow().addFlags(128);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsControllerCompat = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());
        windowInsetsControllerCompat.setSystemBarsBehavior(2);
        Application application = getApplication();
        StudioApp studioApp = application instanceof StudioApp ? (StudioApp) application : null;
        if (studioApp == null || (studio = studioApp.getStudio()) == null) {
            Context applicationContext = getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
            studio = new Studio(applicationContext);
        }
        this.studio = studio;
        Studio studio2 = this.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        studio2.setToastHandler(new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda222
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.onCreate$lambda$8(MainActivity.this, (String) obj);
            }
        });
        Studio studio3 = this.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        studio3.setOpenScriptHandler(new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda223
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.onCreate$lambda$10(MainActivity.this, (Instance) obj);
            }
        });
        Icons icons = Icons.INSTANCE;
        Context applicationContext2 = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext2, "getApplicationContext(...)");
        icons.init(applicationContext2);
        Fonts fonts = Fonts.INSTANCE;
        AssetManager assets = getAssets();
        Intrinsics.checkNotNullExpressionValue(assets, "getAssets(...)");
        fonts.init(assets);
        ImageLoader imageLoader = ImageLoader.INSTANCE;
        Context applicationContext3 = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext3, "getApplicationContext(...)");
        imageLoader.init(applicationContext3);
        AssetFetcher assetFetcher = AssetFetcher.INSTANCE;
        Context applicationContext4 = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext4, "getApplicationContext(...)");
        assetFetcher.init(applicationContext4);
        MaterialTextures materialTextures = MaterialTextures.INSTANCE;
        Context applicationContext5 = getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext5, "getApplicationContext(...)");
        materialTextures.init(applicationContext5);
        Icons.INSTANCE.getListeners().add(this.iconListener);
        Accounts.INSTANCE.getListeners().add(this.accountListener);
        buildUi();
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        studio4.getListeners().add(this);
        Studio studio5 = this.studio;
        if (studio5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio5 = null;
        }
        CopyOnWriteArrayList<Studio.Listener> listeners = studio5.getListeners();
        ExplorerPanel explorerPanel = this.explorer;
        if (explorerPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("explorer");
            explorerPanel = null;
        }
        listeners.add(explorerPanel);
        Studio studio6 = this.studio;
        if (studio6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio6 = null;
        }
        CopyOnWriteArrayList<Studio.Listener> listeners2 = studio6.getListeners();
        PropertiesPanel propertiesPanel = this.properties;
        if (propertiesPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("properties");
            propertiesPanel = null;
        }
        listeners2.add(propertiesPanel);
        Studio studio7 = this.studio;
        if (studio7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio7 = null;
        }
        studio7.setPluginMenuHandler(new Function3() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda224
            @Override // kotlin.jvm.functions.Function3
            public final Object invoke(Object obj, Object obj2, Object obj3) {
                return MainActivity.onCreate$lambda$11(MainActivity.this, (String) obj, (List) obj2, (Function1) obj3);
            }
        });
        WebPermissions.INSTANCE.setAsker(this.webAsker);
        getOnBackPressedDispatcher().addCallback(this, new AnonymousClass5());
        Application application2 = getApplication();
        StudioApp app = application2 instanceof StudioApp ? (StudioApp) application2 : null;
        boolean firstLaunch = false;
        if (app != null && !app.getStarted()) {
            app.setStarted(true);
            firstLaunch = true;
            OutputLog.INSTANCE.info("Roblox Studio ready. Open a .rbxl/.rbxm file or one of your Roblox games, or start building!");
            Studio studio8 = this.studio;
            if (studio8 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio8 = null;
            }
            studio8.getPlugins().loadAll();
            Avatar avatar = Avatar.INSTANCE;
            Context applicationContext6 = getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext6, "getApplicationContext(...)");
            avatar.refresh(applicationContext6);
        }
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
        boolean noPlace = app == null || !app.getPlaceOpened();
        if (firstLaunch || noPlace) {
            Intent intent = getIntent();
            if (!Intrinsics.areEqual(intent != null ? intent.getAction() : null, "android.intent.action.VIEW")) {
                showStart();
            }
        }
    }

    static final Unit onCreate$lambda$8(MainActivity this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Toast.makeText(this$0, it, 0).show();
        return Unit.INSTANCE;
    }

    static final Unit onCreate$lambda$10(final MainActivity this$0, final Instance s) {
        Intrinsics.checkNotNullParameter(s, "s");
        this$0.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                this$0.openScript(s);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit onCreate$lambda$11(MainActivity this$0, String title, List labels, Function1 pick) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(labels, "labels");
        Intrinsics.checkNotNullParameter(pick, "pick");
        this$0.showPluginMenu(title, labels, pick);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: com.goydevv.studrblx.MainActivity$onCreate$5, reason: invalid class name */
    /* JADX INFO: compiled from: MainActivity.kt */
    public final class AnonymousClass5 extends OnBackPressedCallback {
        AnonymousClass5() {
            super(true);
        }

        @Override // androidx.activity.OnBackPressedCallback
        public void handleOnBackPressed() {
            if (MainActivity.this.startScreen != null) {
                if (MainActivity.this.hasOpenPlace()) {
                    MainActivity.this.closeStart();
                    return;
                } else {
                    MainActivity.this.finish();
                    return;
                }
            }
            Studio studio = MainActivity.this.studio;
            DockLayout dockLayout = null;
            Studio studio2 = null;
            if (studio == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio = null;
            }
            if (studio.isPlaying()) {
                ViewportView viewportView = MainActivity.this.viewport;
                if (viewportView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("viewport");
                    viewportView = null;
                }
                if (viewportView.getHud().handleBack()) {
                    return;
                }
            }
            if (MainActivity.this.activeScript != null) {
                MainActivity.this.showPlace();
                return;
            }
            Studio studio3 = MainActivity.this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            if (studio3.isPlaying()) {
                Studio studio4 = MainActivity.this.studio;
                if (studio4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                } else {
                    studio2 = studio4;
                }
                studio2.stopPlay();
                return;
            }
            DockLayout dockLayout2 = MainActivity.this.dock;
            if (dockLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dock");
            } else {
                dockLayout = dockLayout2;
            }
            if (dockLayout.getAllHidden()) {
                MainActivity.this.setAllHidden(false);
                return;
            }
            MainActivity mainActivity = MainActivity.this;
            final MainActivity mainActivity2 = MainActivity.this;
            mainActivity.confirmDiscard(new Function0() { // from class: com.goydevv.studrblx.MainActivity$onCreate$5$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.AnonymousClass5.handleOnBackPressed$lambda$0(mainActivity2);
                }
            });
        }

        static final Unit handleOnBackPressed$lambda$0(MainActivity this$0) {
            this$0.finish();
            return Unit.INSTANCE;
        }
    }

    public final void showStart() {
        if (this.startScreen != null) {
            return;
        }
        MainActivity mainActivity = this;
        Studio studio = this.studio;
        FrameLayout frameLayout = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        StartScreen s = new StartScreen(mainActivity, studio, this, this);
        this.startScreen = s;
        FrameLayout frameLayout2 = this.rootFrame;
        if (frameLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootFrame");
        } else {
            frameLayout = frameLayout2;
        }
        frameLayout.addView(s, new FrameLayout.LayoutParams(-1, -1));
    }

    @Override // com.goydevv.studrblx.ui.StartScreen.Host
    public void closeStart() {
        StartScreen startScreen = this.startScreen;
        if (startScreen != null) {
            FrameLayout frameLayout = this.rootFrame;
            if (frameLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("rootFrame");
                frameLayout = null;
            }
            frameLayout.removeView(startScreen);
        }
        this.startScreen = null;
    }

    @Override // com.goydevv.studrblx.ui.StartScreen.Host
    public boolean hasOpenPlace() {
        Application application = getApplication();
        StudioApp studioApp = application instanceof StudioApp ? (StudioApp) application : null;
        return studioApp != null && studioApp.getPlaceOpened();
    }

    @Override // com.goydevv.studrblx.ui.StartScreen.Host
    public void newFromTemplate(final PlaceFactory.Template t) {
        Intrinsics.checkNotNullParameter(t, "t");
        confirmDiscard(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda219
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.newFromTemplate$lambda$13(MainActivity.this, t);
            }
        });
    }

    static final Unit newFromTemplate$lambda$13(MainActivity this$0, PlaceFactory.Template $t) {
        this$0.closeAllScripts();
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.newPlace($t);
        Application application = this$0.getApplication();
        StudioApp studioApp = application instanceof StudioApp ? (StudioApp) application : null;
        if (studioApp != null) {
            studioApp.setPlaceOpened(true);
        }
        this$0.closeStart();
        return Unit.INSTANCE;
    }

    @Override // com.goydevv.studrblx.ui.StartScreen.Host
    public void openFileFromStart() {
        this.openMode = OpenMode.PLACE;
        this.openLauncher.launch(new String[]{"*/*"});
    }

    @Override // com.goydevv.studrblx.ui.StartScreen.Host
    public void openRecentFile(Recents.Entry e) {
        Uri uri;
        Intrinsics.checkNotNullParameter(e, "e");
        String uri2 = e.getUri();
        if (uri2 == null || (uri = Uri.parse(uri2)) == null) {
            return;
        }
        this.openMode = OpenMode.PLACE;
        onFilePicked(uri);
    }

    @Override // com.goydevv.studrblx.ui.StartScreen.Host
    public void showStudioSettings() {
        showSettings();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:33:0x00a0  */
    public final void refreshAccountChip() {
        String str;
        String displayName;
        if (this.accountChip == null) {
            return;
        }
        LinearLayout linearLayout = this.accountChip;
        LinearLayout linearLayout2 = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("accountChip");
            linearLayout = null;
        }
        linearLayout.removeAllViews();
        ImageView img = new ImageView(this);
        LinearLayout linearLayout3 = this.accountChip;
        if (linearLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("accountChip");
            linearLayout3 = null;
        }
        linearLayout3.addView(img, Ui.lp$default(Ui.INSTANCE, dp(24.0f), dp(24.0f), 0.0f, 4, null));
        Accounts.Account a = Accounts.INSTANCE.active(this);
        if (a != null) {
            AccountUi.INSTANCE.loadHeadshot(img, a.getUserId());
        } else {
            img.setImageDrawable(Icons.general$default(Icons.INSTANCE, this, "Account", null, 4, null));
        }
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            LinearLayout linearLayout4 = this.accountChip;
            if (linearLayout4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("accountChip");
                linearLayout4 = null;
            }
            Ui ui = Ui.INSTANCE;
            MainActivity mainActivity = this;
            if (a == null || (displayName = a.getDisplayName()) == null) {
                str = "Guest";
            } else {
                String name = displayName;
                if (name.length() == 0) {
                    name = a.getName();
                }
                str = name;
                if (str == null) {
                    str = "Guest";
                }
            }
            TextView textViewText$default = Ui.text$default(ui, mainActivity, str, 12.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
            textViewText$default.setPadding(dp(6.0f), 0, dp(2.0f), 0);
            linearLayout4.addView(textViewText$default);
        }
        LinearLayout linearLayout5 = this.accountChip;
        if (linearLayout5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("accountChip");
        } else {
            linearLayout2 = linearLayout5;
        }
        linearLayout2.setContentDescription(a != null ? "Roblox account @" + a.getName() : "Not signed in");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void askWebPermission(final String host, String who, final Function1<? super Boolean, Unit> answer) {
        if (isFinishing() || isDestroyed()) {
            answer.invoke(null);
            return;
        }
        final Ref.BooleanRef answered = new Ref.BooleanRef();
        AlertDialog d = Dialogs.INSTANCE.themed(this).setTitle("Allow access to " + host + "?").setMessage(who + " wants to connect to " + host + " over the internet.\n\nIt could send that website things from the place you have open. Your Roblox sign-in is never shared with it.\n\nOnly allow websites you trust.").setPositiveButton("Allow", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda160
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.askWebPermission$lambda$17(host, answered, answer, dialogInterface, i);
            }
        }).setNegativeButton("Block", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda161
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.askWebPermission$lambda$18(host, answered, answer, dialogInterface, i);
            }
        }).create();
        d.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda162
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                MainActivity.askWebPermission$reply(answered, answer, null);
            }
        });
        d.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void askWebPermission$reply(Ref.BooleanRef answered, Function1<? super Boolean, Unit> function1, Boolean v) {
        if (answered.element) {
            return;
        }
        answered.element = true;
        function1.invoke(v);
    }

    static final void askWebPermission$lambda$17(String $host, Ref.BooleanRef $answered, Function1 $answer, DialogInterface dialogInterface, int i) {
        askWebPermission$reply($answered, $answer, true);
        OutputLog.INSTANCE.info("Allowed scripts and plugins to connect to " + $host);
    }

    static final void askWebPermission$lambda$18(String $host, Ref.BooleanRef $answered, Function1 $answer, DialogInterface dialogInterface, int i) {
        askWebPermission$reply($answered, $answer, false);
        OutputLog.INSTANCE.warn("Blocked scripts and plugins from connecting to " + $host);
    }

    private final void showPluginMenu(String title, List<String> labels, final Function1<? super Integer, Unit> pick) {
        if (labels.isEmpty()) {
            pick.invoke(-1);
            return;
        }
        final Ref.BooleanRef picked = new Ref.BooleanRef();
        AlertDialog.Builder builderThemed = Dialogs.INSTANCE.themed(this);
        String str = title;
        if (str.length() == 0) {
            str = "Plugin";
        }
        AlertDialog d = builderThemed.setTitle(str).setItems((CharSequence[]) labels.toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.showPluginMenu$lambda$21(picked, pick, dialogInterface, i);
            }
        }).create();
        d.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                MainActivity.showPluginMenu$lambda$22(picked, pick, dialogInterface);
            }
        });
        d.show();
    }

    static final void showPluginMenu$lambda$21(Ref.BooleanRef $picked, Function1 $pick, DialogInterface dialogInterface, int i) {
        $picked.element = true;
        $pick.invoke(Integer.valueOf(i));
    }

    static final void showPluginMenu$lambda$22(Ref.BooleanRef $picked, Function1 $pick, DialogInterface it) {
        if ($picked.element) {
            return;
        }
        $pick.invoke(-1);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private final void handleIntent(Intent i) {
        Uri uri;
        final String stringExtra;
        final String stringExtra2;
        boolean debuggable = (getApplicationInfo().flags & 2) != 0;
        Studio studio = null;
        if (i != null && (stringExtra2 = i.getStringExtra("lua")) != null && debuggable) {
            Studio studio2 = this.studio;
            if (studio2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio2 = null;
            }
            studio2.getGlJobs().add(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda168
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.handleIntent$lambda$24$lambda$23(MainActivity.this, stringExtra2);
                }
            });
        }
        if (debuggable) {
            if (i != null && i.getBooleanExtra("play", false)) {
                Studio studio3 = this.studio;
                if (studio3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio3 = null;
                }
                studio3.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda169
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.handleIntent$lambda$25(MainActivity.this);
                    }
                });
            }
        }
        if (debuggable) {
            if (i != null && i.getBooleanExtra("stop", false)) {
                Studio studio4 = this.studio;
                if (studio4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio4 = null;
                }
                studio4.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda170
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.handleIntent$lambda$26(MainActivity.this);
                    }
                });
            }
        }
        if (i != null && (stringExtra = i.getStringExtra("playas")) != null && debuggable) {
            Studio studio5 = this.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio = studio5;
            }
            studio.getMain().post(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda171
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.handleIntent$lambda$30$lambda$29(MainActivity.this, stringExtra);
                }
            });
        }
        if (i == null || (uri = i.getData()) == null || !Intrinsics.areEqual(i.getAction(), "android.intent.action.VIEW")) {
            return;
        }
        this.openMode = OpenMode.PLACE;
        onFilePicked(uri);
    }

    static final Unit handleIntent$lambda$24$lambda$23(MainActivity this$0, String $code) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.runCommand($code);
        return Unit.INSTANCE;
    }

    static final void handleIntent$lambda$25(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.startPlay();
    }

    static final void handleIntent$lambda$26(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.stopPlay();
    }

    static final void handleIntent$lambda$30$lambda$29(MainActivity this$0, String $n) {
        Instance instanceFindFirstChild$default;
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this$0.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            instanceFindFirstChild$default = Instance.findFirstChild$default(studio3.getModel().getWorkspace(), $n, false, 2, null);
        }
        if (instanceFindFirstChild$default != null) {
            Studio studio4 = this$0.studio;
            if (studio4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio4;
            }
            studio2.playAs(instanceFindFirstChild$default);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        ViewportView viewportView = this.viewport;
        if (viewportView == null) {
            return;
        }
        viewportView.onResume();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        ViewportView viewportView = this.viewport;
        if (viewportView == null) {
            return;
        }
        viewportView.onPause();
        Iterable iterableValues = this.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level < 40) return;
        ImageLoader.INSTANCE.trimMemory(level);
        AssetFetcher.INSTANCE.trimMemory(level);
        RobloxTextures.trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.INSTANCE.trimMemory(80);
        AssetFetcher.INSTANCE.trimMemory(80);
        RobloxTextures.trimMemory(80);
    }

    static final Unit webAsker$lambda$33(final MainActivity this$0, final String host, final String who, final Function1 answer) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(who, "who");
        Intrinsics.checkNotNullParameter(answer, "answer");
        this$0.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda220
            @Override // java.lang.Runnable
            public final void run() {
                this$0.askWebPermission(host, who, answer);
            }
        });
        return Unit.INSTANCE;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        if (this.outTabLogListener != null) {
            OutputLog.INSTANCE.getListeners().remove(this.outTabLogListener);
            this.outTabLogListener = null;
        }
        if (WebPermissions.INSTANCE.getAsker() == this.webAsker) {
            WebPermissions.INSTANCE.setAsker(null);
        }
        Studio studio = this.studio;
        if (studio == null || this.explorer == null || this.properties == null) {
            super.onDestroy();
            return;
        }
        studio.getListeners().remove(this);
        Studio studio2 = this.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        CopyOnWriteArrayList<Studio.Listener> listeners = studio2.getListeners();
        ExplorerPanel explorerPanel = this.explorer;
        if (explorerPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("explorer");
            explorerPanel = null;
        }
        listeners.remove(explorerPanel);
        Studio studio3 = this.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        CopyOnWriteArrayList<Studio.Listener> listeners2 = studio3.getListeners();
        PropertiesPanel propertiesPanel = this.properties;
        if (propertiesPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("properties");
            propertiesPanel = null;
        }
        listeners2.remove(propertiesPanel);
        Icons.INSTANCE.getListeners().remove(this.iconListener);
        Accounts.INSTANCE.getListeners().remove(this.accountListener);
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        if (studio4.getPluginMenuHandler() != null) {
            Studio studio5 = this.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio5 = null;
            }
            studio5.setPluginMenuHandler(null);
        }
        super.onDestroy();
    }

    private final void buildUi() {
        DockLayout dockLayout;
        DockLayout dockLayout2;
        DockLayout dockLayout3;
        DockLayout dockLayout4;
        boolean phone = getResources().getConfiguration().smallestScreenWidthDp < 600;
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(1);
        root.setBackgroundColor(Ui.INSTANCE.getBG());
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(0);
        top.setGravity(16);
        top.setBackgroundColor(Ui.INSTANCE.getTOPBAR());
        TextView textViewIconTextButton = Ui.INSTANCE.iconTextButton(this, null, "File", Ui.INSTANCE.getACCENT(), new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda172
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.buildUi$lambda$36(MainActivity.this, (View) obj);
            }
        });
        textViewIconTextButton.setTextColor(-1);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
        layoutParamsLp$default.setMargins(dp(4.0f), dp(3.0f), dp(6.0f), dp(3.0f));
        Unit unit = Unit.INSTANCE;
        top.addView(textViewIconTextButton, layoutParamsLp$default);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        this.tabRow = linearLayout;
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        LinearLayout linearLayout2 = this.tabRow;
        Studio studio = null;
        if (linearLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tabRow");
            linearLayout2 = null;
        }
        horizontalScrollView.addView(linearLayout2);
        top.addView(horizontalScrollView, Ui.INSTANCE.lp(0, -2, 1.0f));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this, "", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setPadding(dp(8.0f), 0, dp(8.0f), 0);
        this.titleView = textViewText$default;
        if (!phone) {
            TextView textView = this.titleView;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("titleView");
                textView = null;
            }
            top.addView(textView);
        }
        this.hideBtn = Ui.iconButton$default(Ui.INSTANCE, this, g$default(this, "HideWindows", null, 2, null), 0.0f, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda173
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.buildUi$lambda$42(MainActivity.this, (View) obj);
            }
        }, 4, null);
        ImageView imageView = this.hideBtn;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hideBtn");
            imageView = null;
        }
        top.addView(imageView);
        top.addView(Ui.iconButton$default(Ui.INSTANCE, this, g$default(this, "Undo", null, 2, null), 0.0f, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda174
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.buildUi$lambda$43(MainActivity.this, (View) obj);
            }
        }, 4, null));
        top.addView(Ui.iconButton$default(Ui.INSTANCE, this, g$default(this, "Redo", null, 2, null), 0.0f, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda175
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.buildUi$lambda$44(MainActivity.this, (View) obj);
            }
        }, 4, null));
        TextView textViewIconTextButton2 = Ui.INSTANCE.iconTextButton(this, g$default(this, "Play", null, 2, null), "Play", -13726889, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda176
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.buildUi$lambda$45(MainActivity.this, (View) obj);
            }
        });
        textViewIconTextButton2.setTextColor(-1);
        this.playButton = textViewIconTextButton2;
        TextView textView2 = this.playButton;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("playButton");
            textView2 = null;
        }
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
        layoutParamsLp$default2.setMargins(dp(6.0f), dp(3.0f), dp(4.0f), dp(3.0f));
        Unit unit2 = Unit.INSTANCE;
        top.addView(textView2, layoutParamsLp$default2);
        LinearLayout linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(16);
        linearLayout3.setPadding(dp(4.0f), dp(3.0f), dp(6.0f), dp(3.0f));
        linearLayout3.setBackground(Ui.INSTANCE.rippleBg(this, Ui.INSTANCE.getHOVER(), 14.0f));
        linearLayout3.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda177
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.buildUi$lambda$49$lambda$48(MainActivity.this, view);
            }
        });
        this.accountChip = linearLayout3;
        LinearLayout linearLayout4 = this.accountChip;
        if (linearLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("accountChip");
            linearLayout4 = null;
        }
        LinearLayout.LayoutParams layoutParamsLp$default3 = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
        layoutParamsLp$default3.setMargins(dp(2.0f), dp(3.0f), dp(6.0f), dp(3.0f));
        Unit unit3 = Unit.INSTANCE;
        top.addView(linearLayout4, layoutParamsLp$default3);
        refreshAccountChip();
        root.addView(top, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        LinearLayout linearLayout5 = new LinearLayout(this);
        linearLayout5.setOrientation(0);
        linearLayout5.setGravity(16);
        linearLayout5.setPadding(dp(4.0f), dp(2.0f), dp(4.0f), dp(2.0f));
        this.ribbon = linearLayout5;
        HorizontalScrollView horizontalScrollView2 = new HorizontalScrollView(this);
        horizontalScrollView2.setBackgroundColor(Ui.INSTANCE.getRIBBON());
        horizontalScrollView2.setHorizontalScrollBarEnabled(false);
        LinearLayout linearLayout6 = this.ribbon;
        if (linearLayout6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbon");
            linearLayout6 = null;
        }
        horizontalScrollView2.addView(linearLayout6);
        this.ribbonScroll = horizontalScrollView2;
        HorizontalScrollView horizontalScrollView3 = this.ribbonScroll;
        if (horizontalScrollView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbonScroll");
            horizontalScrollView3 = null;
        }
        root.addView(horizontalScrollView3, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        this.ribbonDivider = Ui.divider$default(Ui.INSTANCE, this, false, 2, null);
        View view = this.ribbonDivider;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbonDivider");
            view = null;
        }
        root.addView(view);
        LinearLayout center = new LinearLayout(this);
        center.setOrientation(1);
        LinearLayout linearLayout7 = new LinearLayout(this);
        linearLayout7.setOrientation(0);
        linearLayout7.setBackgroundColor(Ui.INSTANCE.getHEADER());
        this.scriptTabs = linearLayout7;
        HorizontalScrollView horizontalScrollView4 = new HorizontalScrollView(this);
        LinearLayout linearLayout8 = this.scriptTabs;
        if (linearLayout8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptTabs");
            linearLayout8 = null;
        }
        horizontalScrollView4.addView(linearLayout8);
        horizontalScrollView4.setVisibility(8);
        this.scriptTabsScroll = horizontalScrollView4;
        HorizontalScrollView horizontalScrollView5 = this.scriptTabsScroll;
        if (horizontalScrollView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptTabsScroll");
            horizontalScrollView5 = null;
        }
        center.addView(horizontalScrollView5, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        FrameLayout stack = new FrameLayout(this);
        MainActivity mainActivity = this;
        Studio studio2 = this.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        this.viewport = new ViewportView(mainActivity, studio2);
        ViewportView viewportView = this.viewport;
        if (viewportView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("viewport");
            viewportView = null;
        }
        stack.addView(viewportView, new FrameLayout.LayoutParams(-1, -1));
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setVisibility(8);
        frameLayout.setBackgroundColor(Ui.INSTANCE.getEDITOR_BG());
        this.editorHost = frameLayout;
        FrameLayout frameLayout2 = this.editorHost;
        if (frameLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editorHost");
            frameLayout2 = null;
        }
        stack.addView(frameLayout2, new FrameLayout.LayoutParams(-1, -1));
        center.addView(stack, Ui.INSTANCE.lp(-1, 0, 1.0f));
        MainActivity mainActivity2 = this;
        Studio studio3 = this.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        this.toolbox = new ToolboxPanel(mainActivity2, studio3, this);
        MainActivity mainActivity3 = this;
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        this.output = new OutputPanel(mainActivity3, studio4);
        MainActivity mainActivity4 = this;
        Studio studio5 = this.studio;
        if (studio5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio5 = null;
        }
        this.explorer = new ExplorerPanel(mainActivity4, studio5, this);
        MainActivity mainActivity5 = this;
        Studio studio6 = this.studio;
        if (studio6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio6 = null;
        }
        this.properties = new PropertiesPanel(mainActivity5, studio6);
        SharedPreferences layoutPrefs = getLayoutPrefs();
        Intrinsics.checkNotNullExpressionValue(layoutPrefs, "<get-layoutPrefs>(...)");
        this.dock = new DockLayout(this, layoutPrefs);
        DockLayout dockLayout5 = this.dock;
        if (dockLayout5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout5 = null;
        }
        dockLayout5.setCenter(center);
        DockLayout dockLayout6 = this.dock;
        if (dockLayout6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        } else {
            dockLayout = dockLayout6;
        }
        ExplorerPanel explorerPanel = this.explorer;
        if (explorerPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("explorer");
            explorerPanel = null;
        }
        DockLayout.addPanel$default(dockLayout, "explorer", "Explorer", explorerPanel, DockLayout.Zone.RIGHT, 0, false, 32, null);
        DockLayout dockLayout7 = this.dock;
        if (dockLayout7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout2 = null;
        } else {
            dockLayout2 = dockLayout7;
        }
        PropertiesPanel propertiesPanel = this.properties;
        if (propertiesPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("properties");
            propertiesPanel = null;
        }
        DockLayout.addPanel$default(dockLayout2, "properties", "Properties", propertiesPanel, DockLayout.Zone.RIGHT, 1, false, 32, null);
        DockLayout dockLayout8 = this.dock;
        if (dockLayout8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout3 = null;
        } else {
            dockLayout3 = dockLayout8;
        }
        ToolboxPanel toolboxPanel = this.toolbox;
        if (toolboxPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("toolbox");
            toolboxPanel = null;
        }
        dockLayout3.addPanel("toolbox", "Toolbox", toolboxPanel, DockLayout.Zone.LEFT, 0, !phone);
        DockLayout dockLayout9 = this.dock;
        if (dockLayout9 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout4 = null;
        } else {
            dockLayout4 = dockLayout9;
        }
        OutputPanel outputPanel = this.output;
        if (outputPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("output");
            outputPanel = null;
        }
        dockLayout4.addPanel("output", "Output", outputPanel, DockLayout.Zone.BOTTOM, 0, !phone);
        DockLayout dockLayout10 = this.dock;
        if (dockLayout10 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout10 = null;
        }
        dockLayout10.setOnChanged(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda178
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildUi$lambda$57(MainActivity.this);
            }
        });
        DockLayout dockLayout11 = this.dock;
        if (dockLayout11 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout11 = null;
        }
        root.addView(dockLayout11, Ui.INSTANCE.lp(-1, 0, 1.0f));
        android.widget.LinearLayout bottomTabBar = new android.widget.LinearLayout(this);
        bottomTabBar.setOrientation(0);
        bottomTabBar.setGravity(16);
        bottomTabBar.setBackgroundColor(Ui.INSTANCE.getHEADER());
        int bPad = Ui.INSTANCE.dp(this, 8.0f);
        bottomTabBar.setPadding(bPad, 0, bPad, 0);
        final android.widget.TextView outTabBtn = new android.widget.TextView(this);
        outTabBtn.setText("Output");
        outTabBtn.setTextSize(11.5f);
        outTabBtn.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        outTabBtn.setTextColor(Ui.INSTANCE.getTEXT());
        outTabBtn.setPadding(Ui.INSTANCE.dp(this, 10.0f), Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 10.0f), Ui.INSTANCE.dp(this, 4.0f));
        final int[] outAlertState = new int[]{0, 0, 0}; // [0]=level (0=none,1=warn,2=error), [1]=errCount, [2]=warnCount
        final int[] pulseStep = new int[]{0};
        final Runnable[] pulseRef = new Runnable[1];
        pulseRef[0] = new Runnable() {
            @Override
            public void run() {
                int lv = outAlertState[0];
                if (lv == 0) {
                    outTabBtn.setAlpha(1.0f);
                    outTabBtn.setBackgroundColor(0);
                    return;
                }
                int alertColor = (lv == 2) ? Ui.ERROR : Ui.WARN;
                int flashBg = (lv == 2) ? 0x55FF4444 : 0x55FFA500;
                if (pulseStep[0] > 0) {
                    boolean hi = (pulseStep[0] % 2 == 1);
                    outTabBtn.setTextColor(hi ? 0xFFFFFFFF : alertColor);
                    outTabBtn.setBackgroundColor(hi ? flashBg : 0);
                    pulseStep[0]--;
                    outTabBtn.postDelayed(this, 180L);
                } else {
                    outTabBtn.setTextColor(alertColor);
                    outTabBtn.setBackgroundColor(0);
                }
            }
        };
        this.outTabLogListener = new Function1<OutputLog.Entry, Unit>() {
            @Override
            public Unit invoke(final OutputLog.Entry entry) {
                outTabBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        if (entry == null) {
                            outAlertState[0] = 0;
                            outAlertState[1] = 0;
                            outAlertState[2] = 0;
                            pulseStep[0] = 0;
                            outTabBtn.removeCallbacks(pulseRef[0]);
                            outTabBtn.setText("Output");
                            outTabBtn.setBackgroundColor(0);
                            boolean shown = MainActivity.this.dock != null && MainActivity.this.dock.isShown("output");
                            outTabBtn.setTextColor(shown ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.getTEXT());
                            return;
                        }
                        OutputLog.Level lv = entry.getLevel();
                        if (lv == OutputLog.Level.ERROR || lv == OutputLog.Level.WARN) {
                            if (lv == OutputLog.Level.ERROR) {
                                outAlertState[0] = 2;
                                outAlertState[1]++;
                            } else {
                                if (outAlertState[0] < 1) outAlertState[0] = 1;
                                outAlertState[2]++;
                            }
                            int total = outAlertState[1] + outAlertState[2];
                            outTabBtn.setText("Output (" + total + ")");
                            outTabBtn.removeCallbacks(pulseRef[0]);
                            pulseStep[0] = 6;
                            outTabBtn.post(pulseRef[0]);
                        }
                    }
                });
                return Unit.INSTANCE;
            }
        };
        OutputLog.INSTANCE.getListeners().add(this.outTabLogListener);
        outTabBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (MainActivity.this.dock != null) {
                    MainActivity.this.dock.toggle("output");
                    boolean shown = MainActivity.this.dock.isShown("output");
                    if (shown) {
                        outAlertState[0] = 0;
                        pulseStep[0] = 0;
                        outTabBtn.removeCallbacks(pulseRef[0]);
                        outTabBtn.setBackgroundColor(0);
                    }
                    int color = shown ? Ui.INSTANCE.getACCENT() : (outAlertState[0] == 2 ? Ui.ERROR : (outAlertState[0] == 1 ? Ui.WARN : Ui.INSTANCE.getTEXT()));
                    outTabBtn.setTextColor(color);
                }
            }
        });
        bottomTabBar.addView(outTabBtn, Ui.INSTANCE.lp(-2, -2, 0.0f));
        android.widget.TextView clrTabBtn = new android.widget.TextView(this);
        clrTabBtn.setText("Clear");
        clrTabBtn.setTextSize(11.0f);
        clrTabBtn.setTextColor(0xFF9AA0A6);
        clrTabBtn.setPadding(Ui.INSTANCE.dp(this, 8.0f), Ui.INSTANCE.dp(this, 4.0f), Ui.INSTANCE.dp(this, 8.0f), Ui.INSTANCE.dp(this, 4.0f));
        clrTabBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                OutputLog.INSTANCE.clear();
            }
        });
        bottomTabBar.addView(clrTabBtn, Ui.INSTANCE.lp(-2, -2, 0.0f));
        root.addView(bottomTabBar, Ui.INSTANCE.lp(-1, Ui.INSTANCE.dp(this, 28.0f), 0.0f));
        DockLayout dockLayout12 = this.dock;
        if (dockLayout12 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout12 = null;
        }
        dockLayout12.load();
        FrameLayout frameLayout3 = new FrameLayout(this);
        frameLayout3.setBackgroundColor(Ui.INSTANCE.getBG());
        this.rootFrame = frameLayout3;
        FrameLayout frameLayout4 = this.rootFrame;
        if (frameLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootFrame");
            frameLayout4 = null;
        }
        frameLayout4.addView(root, new FrameLayout.LayoutParams(-1, -1));
        FrameLayout frameLayout5 = this.rootFrame;
        if (frameLayout5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootFrame");
            frameLayout5 = null;
        }
        setContentView(frameLayout5);
        FrameLayout frameLayout6 = this.rootFrame;
        if (frameLayout6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("rootFrame");
            frameLayout6 = null;
        }
        ViewCompat.setOnApplyWindowInsetsListener(frameLayout6, new OnApplyWindowInsetsListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda179
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view2, WindowInsetsCompat windowInsetsCompat) {
                return MainActivity.buildUi$lambda$59(view2, windowInsetsCompat);
            }
        });
        buildTabs();
        buildRibbon();
        updateTitle();
        updateHideButton();
        Studio studio7 = this.studio;
        if (studio7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio = studio7;
        }
        if (studio.isPlaying()) {
            onPlayStateChanged();
        }
    }

    static final Unit buildUi$lambda$36(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.showFileMenu(it);
        return Unit.INSTANCE;
    }

    static final Unit buildUi$lambda$42(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        DockLayout dockLayout = this$0.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        this$0.setAllHidden(!dockLayout.getAllHidden());
        return Unit.INSTANCE;
    }

    static final Unit buildUi$lambda$43(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.getUndo().undo();
        return Unit.INSTANCE;
    }

    static final Unit buildUi$lambda$44(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.getUndo().redo();
        return Unit.INSTANCE;
    }

    static final Unit buildUi$lambda$45(MainActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.togglePlay();
        return Unit.INSTANCE;
    }

    static final void buildUi$lambda$49$lambda$48(MainActivity this$0, View it) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        accountUi.show(mainActivity, studio, this$0);
    }

    static final Unit buildUi$lambda$57(MainActivity this$0) {
        if (Intrinsics.areEqual(this$0.ribbonTab, "View")) {
            this$0.buildRibbon();
        }
        this$0.updateHideButton();
        return Unit.INSTANCE;
    }

    static final WindowInsetsCompat buildUi$lambda$59(View v, WindowInsetsCompat insets) {
        Intrinsics.checkNotNullParameter(v, "v");
        Intrinsics.checkNotNullParameter(insets, "insets");
        Insets bars = insets.getInsets(WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.ime());
        Intrinsics.checkNotNullExpressionValue(bars, "getInsets(...)");
        v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
        return insets;
    }

    private final int dp(float v) {
        return Ui.INSTANCE.dp(this, v);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setAllHidden(boolean hidden) {
        DockLayout dockLayout = this.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        dockLayout.setAllHidden(hidden);
        updateHideButton();
    }

    private final void updateHideButton() {
        DockLayout dockLayout = this.dock;
        HorizontalScrollView horizontalScrollView = null;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        boolean hidden = dockLayout.getAllHidden();
        ImageView imageView = this.hideBtn;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hideBtn");
            imageView = null;
        }
        imageView.setImageDrawable(g$default(this, hidden ? "ShowWindows" : "HideWindows", null, 2, null));
        ImageView imageView2 = this.hideBtn;
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hideBtn");
            imageView2 = null;
        }
        imageView2.setContentDescription(hidden ? "Show windows" : "Hide all windows");
        HorizontalScrollView horizontalScrollView2 = this.ribbonScroll;
        if (horizontalScrollView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbonScroll");
            horizontalScrollView2 = null;
        }
        horizontalScrollView2.setVisibility(hidden ? 8 : 0);
        View view = this.ribbonDivider;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbonDivider");
            view = null;
        }
        HorizontalScrollView horizontalScrollView3 = this.ribbonScroll;
        if (horizontalScrollView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbonScroll");
        } else {
            horizontalScrollView = horizontalScrollView3;
        }
        view.setVisibility(horizontalScrollView.getVisibility());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void refreshIcons() {
        Iterable iterableValues = this.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
        recreate();
    }

    @Override // com.goydevv.studrblx.ui.MainActions
    public void applyThemeChange() {
        Ui.INSTANCE.applyTheme(Settings.INSTANCE.getTheme());
        Iterable iterableValues = this.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
        recreate();
    }

    private final void buildTabs() {
        LinearLayout linearLayout = this.tabRow;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tabRow");
            linearLayout = null;
        }
        linearLayout.removeAllViews();
        for (final String t : this.ribbonTabs) {
            TextView textViewText = Ui.INSTANCE.text(this, t, 13.0f, Intrinsics.areEqual(t, this.ribbonTab) ? Ui.INSTANCE.getTEXT() : Ui.INSTANCE.getDIM(), Intrinsics.areEqual(t, this.ribbonTab));
            textViewText.setPadding(dp(12.0f), dp(8.0f), dp(12.0f), dp(8.0f));
            textViewText.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda84
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MainActivity.buildTabs$lambda$63$lambda$62(MainActivity.this, t, view);
                }
            });
            if (Intrinsics.areEqual(t, this.ribbonTab)) {
                textViewText.setBackgroundColor(Ui.INSTANCE.getRIBBON());
            }
            LinearLayout linearLayout2 = this.tabRow;
            if (linearLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tabRow");
                linearLayout2 = null;
            }
            linearLayout2.addView(textViewText);
        }
    }

    static final void buildTabs$lambda$63$lambda$62(MainActivity this$0, String $t, View it) {
        this$0.ribbonTab = $t;
        this$0.buildTabs();
        this$0.buildRibbon();
    }

    private final void sep() {
        LinearLayout linearLayout = this.ribbon;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbon");
            linearLayout = null;
        }
        View view = new View(this);
        view.setBackgroundColor(Ui.INSTANCE.getBORDER());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp(1.0f), dp(52.0f));
        layoutParams.setMargins(dp(4.0f), 0, dp(4.0f), 0);
        Unit unit = Unit.INSTANCE;
        linearLayout.addView(view, layoutParams);
    }

    private final Drawable g(String name, String fallback) {
        return Icons.INSTANCE.general(this, name, fallback);
    }

    static /* synthetic */ Drawable g$default(MainActivity mainActivity, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return mainActivity.g(str, str2);
    }

    private final Drawable inst(String cls) {
        return Icons.INSTANCE.instance(this, cls);
    }

    private final void group(String caption, Function0<Unit> fill) {
        LinearLayout linearLayout = this.ribbon;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbon");
            linearLayout = null;
        }
        if (linearLayout.getChildCount() > 0) {
            sep();
        }
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(1);
        linearLayout2.setGravity(1);
        LinearLayout linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(0);
        linearLayout3.setGravity(16);
        LinearLayout.LayoutParams layoutParamsLp = Ui.INSTANCE.lp(-2, 0, 1.0f);
        layoutParamsLp.gravity = 1;
        Unit unit = Unit.INSTANCE;
        linearLayout2.addView(linearLayout3, layoutParamsLp);
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this, caption, 9.5f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setGravity(17);
        linearLayout2.addView(textViewText$default, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        LinearLayout linearLayout4 = this.ribbon;
        if (linearLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbon");
            linearLayout4 = null;
        }
        linearLayout4.addView(linearLayout2, new LinearLayout.LayoutParams(-2, dp(66.0f)));
        this.groupRow = linearLayout3;
        try {
            fill.invoke();
        } finally {
            this.groupRow = null;
        }
    }

    static /* synthetic */ void tool$default(MainActivity mainActivity, Drawable drawable, String str, boolean z, Function0 function0, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        mainActivity.tool(drawable, str, z, function0);
    }

    private final void tool(Drawable icon, String label, boolean selected, final Function0<Unit> fn) {
        LinearLayout b = Ui.INSTANCE.toolButton(this, icon, label, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda120
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.tool$lambda$70(fn, (View) obj);
            }
        });
        b.setSelected(selected);
        LinearLayout linearLayout = null;
        if (selected) {
            View childAt = b.getChildAt(1);
            TextView textView = childAt instanceof TextView ? (TextView) childAt : null;
            if (textView != null) {
                textView.setTextColor(Ui.INSTANCE.getSELECT_TEXT());
            }
        }
        LinearLayout linearLayout2 = this.groupRow;
        if (linearLayout2 == null && (linearLayout2 = this.ribbon) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbon");
        } else {
            linearLayout = linearLayout2;
        }
        LinearLayout pairCol = null;
        int cc = linearLayout.getChildCount();
        if (cc > 0) {
            View last = linearLayout.getChildAt(cc - 1);
            if ((last instanceof LinearLayout) && "tool_pair_col".equals(last.getTag()) && ((LinearLayout) last).getChildCount() < 2) {
                pairCol = (LinearLayout) last;
            }
        }
        if (pairCol == null) {
            pairCol = new LinearLayout(this);
            pairCol.setTag("tool_pair_col");
            pairCol.setOrientation(1);
            pairCol.setGravity(16);
            linearLayout.addView(pairCol);
        }
        pairCol.addView(b);
    }

    static final Unit tool$lambda$70(Function0 $fn, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $fn.invoke();
        return Unit.INSTANCE;
    }

    private final void stack(Triple<? extends Drawable, String, ? extends Function0<Unit>>... items) {
        LinearLayout linearLayout;
        LinearLayout col = new LinearLayout(this);
        col.setOrientation(1);
        col.setGravity(16);
        for (Triple<? extends Drawable, String, ? extends Function0<Unit>> triple : items) {
            Drawable icon = triple.component1();
            String label = triple.component2();
            final Function0<Unit> function0Component3 = triple.component3();
            TextView textViewIconTextButton = Ui.INSTANCE.iconTextButton(this, icon, label, 0, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda106
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return MainActivity.stack$lambda$72(function0Component3, (View) obj);
                }
            });
            textViewIconTextButton.setTextSize(11.0f);
            textViewIconTextButton.setGravity(8388627);
            textViewIconTextButton.setPadding(dp(6.0f), dp(1.0f), dp(8.0f), dp(1.0f));
            textViewIconTextButton.setMinHeight(0);
            textViewIconTextButton.setMinimumHeight(0);
            Drawable drawable = textViewIconTextButton.getCompoundDrawables()[0];
            if (drawable != null) {
                drawable.setBounds(0, 0, dp(14.0f), dp(14.0f));
            }
            textViewIconTextButton.setCompoundDrawables(textViewIconTextButton.getCompoundDrawables()[0], null, null, null);
            col.addView(textViewIconTextButton, Ui.lp$default(Ui.INSTANCE, -2, dp(17.0f), 0.0f, 4, null));
        }
        LinearLayout linearLayout2 = this.groupRow;
        if (linearLayout2 == null && (linearLayout2 = this.ribbon) == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbon");
            linearLayout = null;
        } else {
            linearLayout = linearLayout2;
        }
        linearLayout.addView(col);
    }

    static final Unit stack$lambda$72(Function0 $fn, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $fn.invoke();
        return Unit.INSTANCE;
    }

    private final void toolsGroup() {
        group("Tools", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda53
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.toolsGroup$lambda$78(MainActivity.this);
            }
        });
    }

    static final Unit toolsGroup$lambda$78(final MainActivity this$0) {
        Studio studio = null;
        Drawable drawableG$default = g$default(this$0, "Select", null, 2, null);
        Studio studio2 = this$0.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        this$0.tool(drawableG$default, "Select", studio2.getTool() == Tool.SELECT, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda180
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.toolsGroup$lambda$78$lambda$74(this$0);
            }
        });
        Drawable drawableG$default2 = g$default(this$0, "Move", null, 2, null);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        this$0.tool(drawableG$default2, "Move", studio3.getTool() == Tool.MOVE, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda181
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.toolsGroup$lambda$78$lambda$75(this$0);
            }
        });
        Drawable drawableG$default3 = g$default(this$0, "Scale", null, 2, null);
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        this$0.tool(drawableG$default3, "Scale", studio4.getTool() == Tool.SCALE, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda182
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.toolsGroup$lambda$78$lambda$76(this$0);
            }
        });
        Drawable drawableG$default4 = g$default(this$0, "Rotate", null, 2, null);
        Studio studio5 = this$0.studio;
        if (studio5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio = studio5;
        }
        this$0.tool(drawableG$default4, "Rotate", studio.getTool() == Tool.ROTATE, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda183
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.toolsGroup$lambda$78$lambda$77(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit toolsGroup$lambda$78$lambda$74(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.setTool(Tool.SELECT);
        return Unit.INSTANCE;
    }

    static final Unit toolsGroup$lambda$78$lambda$75(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.setTool(Tool.MOVE);
        return Unit.INSTANCE;
    }

    static final Unit toolsGroup$lambda$78$lambda$76(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.setTool(Tool.SCALE);
        return Unit.INSTANCE;
    }

    static final Unit toolsGroup$lambda$78$lambda$77(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.setTool(Tool.ROTATE);
        return Unit.INSTANCE;
    }

    private final boolean selectedPartsLocked() {
        boolean z;
        boolean z2;
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio3;
            }
            Iterable iterableSelectedParts = studio2.selectedParts();
            z = false;
            if (!((Collection) iterableSelectedParts).isEmpty()) {
                Iterable iterable = iterableSelectedParts;
                if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                    Iterator it = iterable.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            z2 = true;
                            break;
                        }
                        if (!Intrinsics.areEqual(((Instance) it.next()).get("Locked"), (Object) true)) {
                            z2 = false;
                            break;
                        }
                    }
                } else {
                    z2 = true;
                }
                if (z2) {
                    z = true;
                }
            }
        }
        return z;
    }

    private final void setLockedOnSelection(boolean v) {
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            List<Instance> listSelectedParts = studio3.selectedParts();
            if (listSelectedParts.isEmpty()) {
                return;
            }
            Studio studio4 = this.studio;
            if (studio4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio4 = null;
            }
            studio4.getUndo().begin(v ? "Lock" : "Unlock");
            for (Instance instance : listSelectedParts) {
                Studio studio5 = this.studio;
                if (studio5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio5 = null;
                }
                studio5.setProp(instance, "Locked", Boolean.valueOf(v));
            }
            Studio studio6 = this.studio;
            if (studio6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio6 = null;
            }
            studio6.getUndo().commit();
            Unit unit = Unit.INSTANCE;
            Studio studio7 = this.studio;
            if (studio7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio7;
            }
            studio2.markDirty();
            toast(v ? "Locked: these parts can't be picked in the 3D view (select them in the Explorer)" : "Unlocked");
        }
    }

    private final void insertInto(String cls) {
        Object next;
        Instance target;
        Instance workspace;
        Instance instance;
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            Iterator it = studio3.getSelection().iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                } else {
                    next = it.next();
                    instance = (Instance) next;
                }
            } while (!(instance.isA("BasePart") || Intrinsics.areEqual(instance.getClassName(), "Attachment")));
            target = (Instance) next;
        }
        if (target == null && !Intrinsics.areEqual(cls, "Model")) {
            toast("Select a part to put the " + cls + " in (added to Workspace)");
        }
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        if (target == null) {
            Studio studio5 = this.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio5 = null;
            }
            workspace = studio5.getModel().getWorkspace();
        } else {
            workspace = target;
        }
        studio4.insertObject(cls, workspace);
        Studio studio6 = this.studio;
        if (studio6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio6;
        }
        studio2.markDirty();
    }

    private final void insertConstraint(String cls) {
        List<Instance> listSelectedParts;
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            listSelectedParts = studio3.selectedParts();
        }
        if (listSelectedParts.size() < 2) {
            toast("Select the two parts to join");
            return;
        }
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        synchronized (studio4.getLock()) {
            Instance instance = listSelectedParts.get(0);
            Instance instance2 = listSelectedParts.get(1);
            Instance instanceCreate = InstanceFactory.INSTANCE.create(cls);
            instanceCreate.set("Part0", instance, false);
            instanceCreate.set("Part1", instance2, false);
            if (Intrinsics.areEqual(cls, "Weld")) {
                instanceCreate.set("C0", CFrame.INSTANCE.getIDENTITY(), false);
                instanceCreate.set("C1", PartData.INSTANCE.cframe(instance2).inverse().times(PartData.INSTANCE.cframe(instance)), false);
            }
            Studio studio5 = this.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio5 = null;
            }
            studio5.getUndo().begin("Insert " + cls);
            Studio studio6 = this.studio;
            if (studio6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio6 = null;
            }
            studio6.reparent(instanceCreate, instance);
            Studio studio7 = this.studio;
            if (studio7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio7 = null;
            }
            studio7.getUndo().commit();
            Studio studio8 = this.studio;
            if (studio8 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio8 = null;
            }
            studio8.getSelection().clear();
            Studio studio9 = this.studio;
            if (studio9 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio9 = null;
            }
            studio9.getSelection().add(instanceCreate);
        }
        Studio studio10 = this.studio;
        if (studio10 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio10 = null;
        }
        Iterator it = studio10.getListeners().iterator();
        while (it.hasNext()) {
            ((Studio.Listener) it.next()).onSelectionChanged();
        }
        Studio studio11 = this.studio;
        if (studio11 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio11;
        }
        studio2.markDirty();
        toast("Joined " + listSelectedParts.get(0).getName() + " and " + listSelectedParts.get(1).getName() + " with a " + cls);
    }

    /* JADX WARN: Code duplicated, block: B:45:0x00fc  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final void buildRibbon() {
        String name;
        Instance inst;
        Instance parent;
        ArrayList arrayList;
        LinearLayout linearLayout = this.ribbon;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("ribbon");
            linearLayout = null;
        }
        linearLayout.removeAllViews();
        String str = this.ribbonTab;
        switch (str.hashCode()) {
            case 2708:
                if (str.equals("UI")) {
                    group("Containers", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda193
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$172(MainActivity.this);
                        }
                    });
                    group("Text & Images", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda194
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$178(MainActivity.this);
                        }
                    });
                    group("Style", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda195
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$183(MainActivity.this);
                        }
                    });
                    group("Layout", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda196
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$188(MainActivity.this);
                        }
                    });
                    group("3D UI", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda197
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$191(MainActivity.this);
                        }
                    });
                    break;
                }
                break;
            case 2255103:
                if (str.equals(StartScreen.HOME)) {
                    group("Clipboard", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda188
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$92(MainActivity.this);
                        }
                    });
                    toolsGroup();
                    snapGroup();
                    group("Insert", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda199
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$99(MainActivity.this);
                        }
                    });
                    group("Edit", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda206
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$110(MainActivity.this);
                        }
                    });
                    group("Test", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda207
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$114(MainActivity.this);
                        }
                    });
                }
                break;
            case 2603186:
                if (str.equals("Test")) {
                    group("Play", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda198
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$196(MainActivity.this);
                        }
                    });
                    group("Output", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda200
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$199(MainActivity.this);
                        }
                    });
                }
                break;
            case 2666181:
                if (str.equals("View")) {
                    group("Windows", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda201
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$206(MainActivity.this);
                        }
                    });
                    group("Viewport", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda202
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$212(MainActivity.this);
                        }
                    });
                    group("Studio", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda203
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$214(MainActivity.this);
                        }
                    });
                }
                break;
            case 74517257:
                if (str.equals("Model")) {
                    toolsGroup();
                    snapGroup();
                    group("Parts", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda208
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$124(MainActivity.this);
                        }
                    });
                    group("Appearance", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda209
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$128(MainActivity.this);
                        }
                    });
                    group("Solid Modeling", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda210
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$137(MainActivity.this);
                        }
                    });
                    group("Constraints", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda211
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$141(MainActivity.this);
                        }
                    });
                    group("Effects", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda212
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$148(MainActivity.this);
                        }
                    });
                    group("Advanced", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda213
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$156(MainActivity.this);
                        }
                    });
                    group("Selection", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda189
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$160(MainActivity.this);
                        }
                    });
                }
                break;
            case -1823818517:
                if (str.equals("Script")) {
                    buildScriptRibbon();
                }
                break;
            case 1189023616:
                if (str.equals("Plugins")) {
                    group("Plugins", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda204
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$220(MainActivity.this);
                        }
                    });
                    Studio studio = this.studio;
                    if (studio == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("studio");
                        studio = null;
                    }
                    Iterable pluginButtons = studio.getPluginButtons();
                    Map<String, ArrayList<PluginButton>> byPlugin = new LinkedHashMap<>();
                    for (Object obj : pluginButtons) {
                        String pluginName = ((PluginButton) obj).getPluginName();
                        Object obj2 = byPlugin.get(pluginName);
                        if (obj2 == null) {
                            arrayList = new ArrayList();
                            byPlugin.put(pluginName, arrayList);
                        } else {
                            arrayList = (ArrayList) obj2;
                        }
                        ((List) arrayList).add(obj);
                    }
                    for (Map.Entry entry : byPlugin.entrySet()) {
                        String name2 = (String) entry.getKey();
                        final List buttons = (List) entry.getValue();
                        PluginButton pluginButton = (PluginButton) CollectionsKt.firstOrNull(buttons);
                        if (pluginButton == null || (inst = pluginButton.getInst()) == null || (parent = inst.getParent()) == null || (name = parent.getName()) == null) {
                            name = name2;
                        } else {
                            if (!((StringsKt.isBlank(name) || Intrinsics.areEqual(name, "Plugin")) ? false : true)) {
                                name = null;
                            }
                            if (name == null) {
                                name = name2;
                            }
                        }
                        String caption = StringsKt.take(name, 22);
                        group(caption, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda205
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return MainActivity.buildRibbon$lambda$225(buttons, MainActivity.this);
                            }
                        });
                    }
                }
                break;
            case 1972874617:
                if (str.equals("Avatar")) {
                    group("Game", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda190
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$162(MainActivity.this);
                        }
                    });
                    group("Rig Builder", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda191
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$164(MainActivity.this);
                        }
                    });
                    group("Playtest", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda192
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.buildRibbon$lambda$167(MainActivity.this);
                        }
                    });
                }
                break;
        }
    }

    static final Unit buildRibbon$lambda$92(final MainActivity this$0) {
        tool$default(this$0, this$0.g("Paste", "Pst"), "Paste", false, new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda93
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.buildRibbon$lambda$92$lambda$88(this$0);
            }
        }, 4, null);
        this$0.stack(new Triple<Drawable, String, Function0<Unit>>(this$0.g("Copy", "Cp"), "Copy", new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda94
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.buildRibbon$lambda$92$lambda$89(this$0);
            }
        }), new Triple<Drawable, String, Function0<Unit>>(this$0.g("Cut", "Cut"), "Cut", new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda95
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.buildRibbon$lambda$92$lambda$90(this$0);
            }
        }), new Triple<Drawable, String, Function0<Unit>>(g$default(this$0, "Duplicate", null, 2, null), "Duplicate", new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda96
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.buildRibbon$lambda$92$lambda$91(this$0);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$92$lambda$88(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.paste(null);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio2.markDirty();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$92$lambda$89(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.copySelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$92$lambda$90(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.cutSelection();
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio2.markDirty();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$92$lambda$91(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.duplicateSelection();
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio2.markDirty();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$99(final MainActivity this$0) {
        tool$default(this$0, this$0.g("Part_Block", "Part"), "Part", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda48
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$99$lambda$93(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("Script"), "Script", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda49
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$99$lambda$95(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("ScreenGui"), "UI", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda50
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$99$lambda$96(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("InsertObject", "Insert"), "Object…", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda51
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$99$lambda$97(this$0);
            }
        }, 4, null);
        DockLayout dockLayout = null;
        Drawable drawableG$default = g$default(this$0, "Toolbox", null, 2, null);
        DockLayout dockLayout2 = this$0.dock;
        if (dockLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
        } else {
            dockLayout = dockLayout2;
        }
        this$0.tool(drawableG$default, "Toolbox", dockLayout.isShown("toolbox"), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda52
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$99$lambda$98(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$99$lambda$93(MainActivity this$0) {
        this$0.pickPartShape();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x003e  */
    /* JADX WARN: Code duplicated, block: B:22:0x0042  */
    /* JADX WARN: Code duplicated, block: B:23:0x0046  */
    static final Unit buildRibbon$lambda$99$lambda$95(MainActivity this$0) {
        Studio studio;
        Studio studio2 = this$0.studio;
        Studio studio3 = null;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        Instance service = (Instance) CollectionsKt.firstOrNull((List) studio4.getSelection());
        if (service == null) {
            studio = this$0.studio;
            if (studio == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio3 = studio;
            }
            service = studio3.getModel().getService("ServerScriptService");
        } else {
            if (!(!service.getIsService() || Intrinsics.areEqual(service.getClassName(), "ServerScriptService"))) {
                service = null;
            }
            if (service == null) {
                studio = this$0.studio;
                if (studio == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                } else {
                    studio3 = studio;
                }
                service = studio3.getModel().getService("ServerScriptService");
            }
        }
        studio2.insertObject("Script", service);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$99$lambda$96(MainActivity this$0) {
        this$0.ribbonTab = "UI";
        this$0.buildTabs();
        this$0.buildRibbon();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$99$lambda$97(MainActivity this$0) {
        ExplorerPanel explorerPanel = this$0.explorer;
        Studio studio = null;
        if (explorerPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("explorer");
            explorerPanel = null;
        }
        Studio studio2 = this$0.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio = studio2;
        }
        explorerPanel.showInsertDialog((Instance) CollectionsKt.firstOrNull((List) studio.getSelection()));
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$99$lambda$98(MainActivity this$0) {
        try {
        DockLayout dockLayout = this$0.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        dockLayout.toggle("toolbox");
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit buildRibbon$lambda$110(final MainActivity this$0) {
        tool$default(this$0, g$default(this$0, "Color", null, 2, null), "Color", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda85
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$110$lambda$100(this$0);
            }
        }, 4, null);
        tool$default(this$0, g$default(this$0, "Material", null, 2, null), "Material", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda86
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$110$lambda$101(this$0);
            }
        }, 4, null);
        tool$default(this$0, g$default(this$0, "Group", null, 2, null), "Group", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda87
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$110$lambda$102(this$0);
            }
        }, 4, null);
        this$0.tool(g$default(this$0, "Lock", null, 2, null), "Lock", this$0.selectedPartsLocked(), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda88
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$110$lambda$103(this$0);
            }
        });
        tool$default(this$0, g$default(this$0, "Anchor", null, 2, null), "Anchor", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda89
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$110$lambda$106(this$0);
            }
        }, 4, null);
        this$0.stack(new Triple<Drawable, String, Function0<Unit>>(g$default(this$0, "Ungroup", null, 2, null), "Ungroup", new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda90
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.buildRibbon$lambda$110$lambda$107(this$0);
            }
        }), new Triple<Drawable, String, Function0<Unit>>(g$default(this$0, "Delete", null, 2, null), "Delete", new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda91
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.buildRibbon$lambda$110$lambda$108(this$0);
            }
        }), new Triple<Drawable, String, Function0<Unit>>(this$0.g("ZoomTo", "Focus"), "Focus", new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda92
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.buildRibbon$lambda$110$lambda$109(this$0);
            }
        }));
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$110$lambda$100(MainActivity this$0) {
        this$0.pickColorForSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$110$lambda$101(MainActivity this$0) {
        this$0.pickMaterialForSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$110$lambda$102(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.groupSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$110$lambda$103(MainActivity this$0) {
        this$0.setLockedOnSelection(!this$0.selectedPartsLocked());
        this$0.buildRibbon();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$110$lambda$106(MainActivity this$0) {
        boolean anchored;
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this$0.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            Iterable iterableSelectedParts = studio3.selectedParts();
            if (!(iterableSelectedParts instanceof Collection) || !((Collection) iterableSelectedParts).isEmpty()) {
                Iterator it = iterableSelectedParts.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        anchored = true;
                        break;
                    }
                    if (!Intrinsics.areEqual(((Instance) it.next()).get("Anchored"), (Object) true)) {
                        anchored = false;
                        break;
                    }
                }
            } else {
                anchored = true;
            }
        }
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio4;
        }
        studio2.setAnchored(anchored ? false : true);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$110$lambda$107(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.ungroupSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$110$lambda$108(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.deleteSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$110$lambda$109(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.focusSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$114(final MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Drawable drawableG$default = studio.isPlaying() ? g$default(this$0, "Stop", null, 2, null) : g$default(this$0, "Play", null, 2, null);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        tool$default(this$0, drawableG$default, studio3.isPlaying() ? "Stop" : "Play", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$114$lambda$111(this$0);
            }
        }, 4, null);
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio4;
        }
        if (!studio2.isPlaying()) {
            tool$default(this$0, this$0.g("Play", "Here"), "Play Here", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda20
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.buildRibbon$lambda$114$lambda$113(this$0);
                }
            }, 4, null);
        }
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$114$lambda$111(MainActivity this$0) {
        this$0.togglePlay();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$114$lambda$113(MainActivity this$0) {
        this$0.showPlace();
        Iterable iterableValues = this$0.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.playHere();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$124(final MainActivity this$0) {
        tool$default(this$0, this$0.g("Part_Block", "Block"), "Block", false, new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda98
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.buildRibbon$lambda$124$lambda$115(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("Part_Sphere", "Sphere"), "Sphere", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda99
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$124$lambda$116(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("Part_Cylinder", "Cylinder"), "Cylinder", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda100
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$124$lambda$117(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("Part_Wedge", "Wedge"), "Wedge", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda101
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$124$lambda$118(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("Part_CornerWedge", "Corner"), "Corner", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda102
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$124$lambda$119(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("TrussPart"), "Truss", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda103
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$124$lambda$122(this$0);
            }
        }, 4, null);
        tool$default(this$0, g$default(this$0, "Spawn", null, 2, null), "Spawn", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda104
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$124$lambda$123(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$124$lambda$115(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.insertPart(1);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$124$lambda$116(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.insertPart(0);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$124$lambda$117(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.insertPart(2);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$124$lambda$118(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.insertPart(3);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$124$lambda$119(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.insertPart(4);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$124$lambda$122(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Instance instanceInsertObject$default = Studio.insertObject$default(studio, "TrussPart", null, 2, null);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        synchronized (studio2.getLock()) {
            Instance.set$default(instanceInsertObject$default, "Anchored", true, false, 4, null);
            Unit unit = Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$124$lambda$123(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "SpawnLocation", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$128(final MainActivity this$0) {
        tool$default(this$0, g$default(this$0, "Color", null, 2, null), "Color", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda21
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$128$lambda$125(this$0);
            }
        }, 4, null);
        tool$default(this$0, g$default(this$0, "Material", null, 2, null), "Material", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda22
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$128$lambda$126(this$0);
            }
        }, 4, null);
        tool$default(this$0, g$default(this$0, "Surface", null, 2, null), "Surface", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda23
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$128$lambda$127(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$128$lambda$125(MainActivity this$0) {
        this$0.pickColorForSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$128$lambda$126(MainActivity this$0) {
        this$0.pickMaterialForSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$128$lambda$127(MainActivity this$0) {
        this$0.pickSurfaceForSelection();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$137(final MainActivity this$0) {
        tool$default(this$0, this$0.g("Union", "Uni"), "Union", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda64
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$137$lambda$130(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("Intersect", "Int"), "Intersect", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda65
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$137$lambda$132(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("Negate", "Neg"), "Negate", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda66
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$137$lambda$134(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("Separate", "Sep"), "Separate", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda67
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$137$lambda$136(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$137$lambda$130(final MainActivity this$0) {
        this$0.solid(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda131
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$137$lambda$130$lambda$129(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final String buildRibbon$lambda$137$lambda$130$lambda$129(MainActivity this$0) {
        SolidModeling solidModeling = SolidModeling.INSTANCE;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        return solidModeling.combine(studio, false);
    }

    static final Unit buildRibbon$lambda$137$lambda$132(final MainActivity this$0) {
        this$0.solid(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda74
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$137$lambda$132$lambda$131(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final String buildRibbon$lambda$137$lambda$132$lambda$131(MainActivity this$0) {
        SolidModeling solidModeling = SolidModeling.INSTANCE;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        return solidModeling.combine(studio, true);
    }

    static final Unit buildRibbon$lambda$137$lambda$134(final MainActivity this$0) {
        this$0.solid(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda47
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$137$lambda$134$lambda$133(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final String buildRibbon$lambda$137$lambda$134$lambda$133(MainActivity this$0) {
        SolidModeling solidModeling = SolidModeling.INSTANCE;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        if (solidModeling.negate(studio) == 0) {
            return "Select the parts to negate.";
        }
        return null;
    }

    static final Unit buildRibbon$lambda$137$lambda$136(final MainActivity this$0) {
        this$0.solid(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda12
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$137$lambda$136$lambda$135(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final String buildRibbon$lambda$137$lambda$136$lambda$135(MainActivity this$0) {
        SolidModeling solidModeling = SolidModeling.INSTANCE;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        return solidModeling.separate(studio);
    }

    static final Unit buildRibbon$lambda$141(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("WeldConstraint"), "Weld", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda115
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$141$lambda$138(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("NoCollisionConstraint"), "No Collide", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda116
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$141$lambda$139(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("Attachment"), "Attachment", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda117
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$141$lambda$140(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$141$lambda$138(MainActivity this$0) {
        this$0.insertConstraint("WeldConstraint");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$141$lambda$139(MainActivity this$0) {
        this$0.insertConstraint("NoCollisionConstraint");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$141$lambda$140(MainActivity this$0) {
        this$0.insertInto("Attachment");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$148(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("ParticleEmitter"), "Particles", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda133
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$148$lambda$142(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("Fire"), "Fire", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda134
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$148$lambda$143(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("Smoke"), "Smoke", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda135
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$148$lambda$144(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("Sparkles"), "Sparkles", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda136
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$148$lambda$145(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("PointLight"), "Light", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda137
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$148$lambda$146(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("Highlight"), "Highlight", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda138
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$148$lambda$147(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$148$lambda$142(MainActivity this$0) {
        this$0.insertInto("ParticleEmitter");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$148$lambda$143(MainActivity this$0) {
        this$0.insertInto("Fire");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$148$lambda$144(MainActivity this$0) {
        this$0.insertInto("Smoke");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$148$lambda$145(MainActivity this$0) {
        this$0.insertInto("Sparkles");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$148$lambda$146(MainActivity this$0) {
        this$0.insertInto("PointLight");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$148$lambda$147(MainActivity this$0) {
        this$0.insertInto("Highlight");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$156(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("Script"), "Script", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$156$lambda$150(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("LocalScript"), "LocalScript", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$156$lambda$153(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("ModuleScript"), "Module", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$156$lambda$155(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x003e  */
    /* JADX WARN: Code duplicated, block: B:22:0x0042  */
    /* JADX WARN: Code duplicated, block: B:23:0x0046  */
    static final Unit buildRibbon$lambda$156$lambda$150(MainActivity this$0) {
        Studio studio;
        Studio studio2 = this$0.studio;
        Studio studio3 = null;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        Instance service = (Instance) CollectionsKt.firstOrNull((List) studio4.getSelection());
        if (service == null) {
            studio = this$0.studio;
            if (studio == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio3 = studio;
            }
            service = studio3.getModel().getService("ServerScriptService");
        } else {
            if (!(!service.getIsService() || Intrinsics.areEqual(service.getClassName(), "ServerScriptService"))) {
                service = null;
            }
            if (service == null) {
                studio = this$0.studio;
                if (studio == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                } else {
                    studio3 = studio;
                }
                service = studio3.getModel().getService("ServerScriptService");
            }
        }
        studio2.insertObject("Script", service);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:19:0x003f  */
    /* JADX WARN: Code duplicated, block: B:21:0x0043  */
    /* JADX WARN: Code duplicated, block: B:24:0x005b  */
    /* JADX WARN: Code duplicated, block: B:25:0x005d  */
    static final Unit buildRibbon$lambda$156$lambda$153(MainActivity this$0) {
        Studio studio;
        Instance service;
        Instance instanceFindFirstChild$default;
        Studio studio2 = this$0.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        Instance instance = (Instance) CollectionsKt.firstOrNull((List) studio3.getSelection());
        if (instance == null) {
            studio = this$0.studio;
            if (studio == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio = null;
            }
            service = studio.getModel().getService("StarterPlayer");
            instanceFindFirstChild$default = Instance.findFirstChild$default(service, "StarterPlayerScripts", false, 2, null);
            if (instanceFindFirstChild$default == null) {
                instance = service;
            } else {
                instance = instanceFindFirstChild$default;
            }
        } else {
            if (!(!instance.getIsService() || StringsKt.startsWith(instance.getClassName(), "Starter", false))) {
                instance = null;
            }
            if (instance == null) {
                studio = this$0.studio;
                if (studio == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio = null;
                }
                service = studio.getModel().getService("StarterPlayer");
                instanceFindFirstChild$default = Instance.findFirstChild$default(service, "StarterPlayerScripts", false, 2, null);
                if (instanceFindFirstChild$default == null) {
                    instance = service;
                } else {
                    instance = instanceFindFirstChild$default;
                }
            }
        }
        studio2.insertObject("LocalScript", instance);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x003e  */
    /* JADX WARN: Code duplicated, block: B:22:0x0042  */
    /* JADX WARN: Code duplicated, block: B:23:0x0046  */
    static final Unit buildRibbon$lambda$156$lambda$155(MainActivity this$0) {
        Studio studio;
        Studio studio2 = this$0.studio;
        Studio studio3 = null;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        Instance service = (Instance) CollectionsKt.firstOrNull((List) studio4.getSelection());
        if (service == null) {
            studio = this$0.studio;
            if (studio == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio3 = studio;
            }
            service = studio3.getModel().getService("ReplicatedStorage");
        } else {
            if (!(!service.getIsService() || Intrinsics.areEqual(service.getClassName(), "ReplicatedStorage"))) {
                service = null;
            }
            if (service == null) {
                studio = this$0.studio;
                if (studio == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                } else {
                    studio3 = studio;
                }
                service = studio3.getModel().getService("ReplicatedStorage");
            }
        }
        studio2.insertObject("ModuleScript", service);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$160(final MainActivity this$0) {
        Drawable drawableG = this$0.g("BoxSelect", "Box");
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        this$0.tool(drawableG, "Box", studio.getBoxSelect(), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda154
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$160$lambda$157(this$0);
            }
        });
        Drawable drawableG2 = this$0.g("SelectAll", "Multi");
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        this$0.tool(drawableG2, "Multi", studio3.getMultiSelect(), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda155
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$160$lambda$158(this$0);
            }
        });
        Drawable drawableInst = this$0.inst("Model");
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio4;
        }
        this$0.tool(drawableInst, "In Groups", studio2.getSelectInsideModels(), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda156
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$160$lambda$159(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$160$lambda$157(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio.setBoxSelect(!studio2.getBoxSelect());
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$160$lambda$158(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio.setMultiSelect(!studio2.getMultiSelect());
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$160$lambda$159(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio.setSelectInsideModels(!studio2.getSelectInsideModels());
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$162(final MainActivity this$0) {
        tool$default(this$0, g$default(this$0, "Settings", null, 2, null), "Game Settings", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$162$lambda$161(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$162$lambda$161(MainActivity this$0) {
        try {
        AvatarUi avatarUi = AvatarUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        avatarUi.gameSettings(mainActivity, studio);
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit buildRibbon$lambda$164(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("Humanoid"), "Rig Builder", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda186
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$164$lambda$163(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$164$lambda$163(MainActivity this$0) {
        AvatarUi avatarUi = AvatarUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        avatarUi.rigBuilder(mainActivity, studio);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$167(final MainActivity this$0) {
        tool$default(this$0, g$default(this$0, "Account", null, 2, null), "Avatar", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda55
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$167$lambda$165(this$0);
            }
        }, 4, null);
        this$0.tool(g$default(this$0, "Model", null, 2, null), "Character", Avatar.INSTANCE.mode(this$0) == Avatar.Mode.CHARACTER, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda56
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$167$lambda$166(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$167$lambda$165(MainActivity this$0) {
        AvatarUi avatarUi = AvatarUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        avatarUi.playtestAvatar(mainActivity, studio);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$167$lambda$166(MainActivity this$0) {
        AvatarUi avatarUi = AvatarUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        avatarUi.characterChooser(mainActivity, studio);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$172(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("ScreenGui"), "ScreenGui", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda144
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$172$lambda$168(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("Frame"), "Frame", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda145
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$172$lambda$169(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("ScrollingFrame"), "Scrolling", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda146
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$172$lambda$170(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("CanvasGroup"), "Canvas", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda147
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$172$lambda$171(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$172$lambda$168(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "ScreenGui", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$172$lambda$169(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "Frame", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$172$lambda$170(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "ScrollingFrame", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$172$lambda$171(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "CanvasGroup", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$178(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("TextLabel"), "TextLabel", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda125
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$178$lambda$173(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("TextButton"), "TextButton", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda126
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$178$lambda$174(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("TextBox"), "TextBox", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda127
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$178$lambda$175(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("ImageLabel"), "Image", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda128
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$178$lambda$176(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("ImageButton"), "ImageButton", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda129
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$178$lambda$177(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$178$lambda$173(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "TextLabel", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$178$lambda$174(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "TextButton", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$178$lambda$175(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "TextBox", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$178$lambda$176(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "ImageLabel", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$178$lambda$177(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "ImageButton", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$183(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("UICorner"), "Corner", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda110
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$183$lambda$179(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("UIStroke"), "Stroke", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda111
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$183$lambda$180(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("UIGradient"), "Gradient", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda112
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$183$lambda$181(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("UIPadding"), "Padding", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda113
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$183$lambda$182(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$183$lambda$179(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "UICorner", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$183$lambda$180(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "UIStroke", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$183$lambda$181(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "UIGradient", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$183$lambda$182(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "UIPadding", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$188(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("UIListLayout"), "List", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda15
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$188$lambda$184(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("UIGridLayout"), "Grid", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$188$lambda$185(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("UIAspectRatioConstraint"), "Aspect", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$188$lambda$186(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("UIScale"), "Scale", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$188$lambda$187(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$188$lambda$184(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "UIListLayout", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$188$lambda$185(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "UIGridLayout", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$188$lambda$186(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "UIAspectRatioConstraint", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$188$lambda$187(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertObject$default(studio, "UIScale", null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$191(final MainActivity this$0) {
        tool$default(this$0, this$0.inst("BillboardGui"), "Billboard", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda24
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$191$lambda$189(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.inst("SurfaceGui"), "SurfaceGui", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda25
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$191$lambda$190(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$191$lambda$189(MainActivity this$0) {
        this$0.insertInto("BillboardGui");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$191$lambda$190(MainActivity this$0) {
        this$0.insertInto("SurfaceGui");
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$196(final MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Drawable drawableG$default = studio.isPlaying() ? g$default(this$0, "Stop", null, 2, null) : g$default(this$0, "Play", null, 2, null);
        Studio studio2 = this$0.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        tool$default(this$0, drawableG$default, studio2.isPlaying() ? "Stop" : "Play", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda107
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$196$lambda$192(this$0);
            }
        }, 4, null);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        if (!studio3.isPlaying()) {
            tool$default(this$0, this$0.g("Play", "Here"), "Play Here", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda108
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.buildRibbon$lambda$196$lambda$194(this$0);
                }
            }, 4, null);
        }
        tool$default(this$0, g$default(this$0, "Account", null, 2, null), "Avatar", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda109
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$196$lambda$195(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$196$lambda$192(MainActivity this$0) {
        this$0.togglePlay();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$196$lambda$194(MainActivity this$0) {
        this$0.showPlace();
        Iterable iterableValues = this$0.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.playHere();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$196$lambda$195(MainActivity this$0) {
        AvatarUi avatarUi = AvatarUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        avatarUi.playtestAvatar(mainActivity, studio);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$199(final MainActivity this$0) {
        tool$default(this$0, this$0.g("Cleanup", "Clear"), "Clear Output", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda68
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$199$lambda$197();
            }
        }, 4, null);
        tool$default(this$0, this$0.g("CommandBar", "Cmd"), "Command", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda69
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$199$lambda$198(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$199$lambda$197() {
        OutputLog.INSTANCE.clear();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$199$lambda$198(MainActivity this$0) {
        try {
        DockLayout dockLayout = this$0.dock;
        DockLayout dockLayout2 = null;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        if (!dockLayout.isShown("output")) {
            DockLayout dockLayout3 = this$0.dock;
            if (dockLayout3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dock");
            } else {
                dockLayout2 = dockLayout3;
            }
            dockLayout2.setShown("output", true);
        }
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit buildRibbon$lambda$206(final MainActivity this$0) {
        DockLayout dockLayout = null;
        Drawable drawableG$default = g$default(this$0, "Explorer", null, 2, null);
        DockLayout dockLayout2 = this$0.dock;
        if (dockLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout2 = null;
        }
        this$0.tool(drawableG$default, "Explorer", dockLayout2.isShown("explorer"), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda225
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$206$lambda$200(this$0);
            }
        });
        Drawable drawableG$default2 = g$default(this$0, "Properties", null, 2, null);
        DockLayout dockLayout3 = this$0.dock;
        if (dockLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout3 = null;
        }
        this$0.tool(drawableG$default2, "Properties", dockLayout3.isShown("properties"), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda226
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$206$lambda$201(this$0);
            }
        });
        Drawable drawableG$default3 = g$default(this$0, "Toolbox", null, 2, null);
        DockLayout dockLayout4 = this$0.dock;
        if (dockLayout4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout4 = null;
        }
        this$0.tool(drawableG$default3, "Toolbox", dockLayout4.isShown("toolbox"), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda227
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$206$lambda$202(this$0);
            }
        });
        Drawable drawableG$default4 = g$default(this$0, "Output", null, 2, null);
        DockLayout dockLayout5 = this$0.dock;
        if (dockLayout5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout5 = null;
        }
        this$0.tool(drawableG$default4, "Output", dockLayout5.isShown("output"), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda228
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$206$lambda$203(this$0);
            }
        });
        Drawable drawableG$default5 = g$default(this$0, "HideWindows", null, 2, null);
        DockLayout dockLayout6 = this$0.dock;
        if (dockLayout6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
        } else {
            dockLayout = dockLayout6;
        }
        this$0.tool(drawableG$default5, "Hide All", dockLayout.getAllHidden(), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda229
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$206$lambda$204(this$0);
            }
        });
        tool$default(this$0, this$0.g("SwitchWindows", "Lay"), "Reset Layout", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda230
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$206$lambda$205(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$206$lambda$200(MainActivity this$0) {
        try {
        DockLayout dockLayout = this$0.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        dockLayout.toggle("explorer");
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit buildRibbon$lambda$206$lambda$201(MainActivity this$0) {
        try {
        DockLayout dockLayout = this$0.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        dockLayout.toggle("properties");
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit buildRibbon$lambda$206$lambda$202(MainActivity this$0) {
        try {
        DockLayout dockLayout = this$0.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        dockLayout.toggle("toolbox");
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit buildRibbon$lambda$206$lambda$203(MainActivity this$0) {
        try {
        DockLayout dockLayout = this$0.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        dockLayout.toggle("output");
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit buildRibbon$lambda$206$lambda$204(MainActivity this$0) {
        DockLayout dockLayout = this$0.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        this$0.setAllHidden(!dockLayout.getAllHidden());
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$206$lambda$205(MainActivity this$0) {
        DockLayout dockLayout = this$0.dock;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        dockLayout.reset();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$212(final MainActivity this$0) {
        Drawable drawableG = this$0.g("Effects", "Shd");
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        this$0.tool(drawableG, "Shadows", studio.getShadowsEnabled(), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda139
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$212$lambda$207(this$0);
            }
        });
        tool$default(this$0, this$0.g("ViewSelector", "Cam"), "Reset Cam", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda140
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$212$lambda$209(this$0);
            }
        }, 4, null);
        Drawable drawableG$default = g$default(this$0, "Fly", null, 2, null);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        tool$default(this$0, drawableG$default, "Fly x" + DataTypesKt.fmtNum(studio2.getFlySpeed()), false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda141
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$212$lambda$211(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$212$lambda$207(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio.setShadowsEnabled(!studio2.getShadowsEnabled());
        this$0.buildRibbon();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$212$lambda$209(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this$0.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            studio3.getCamera().setPos(new Vector3(0.0f, 20.0f, 40.0f));
            Studio studio4 = this$0.studio;
            if (studio4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio4 = null;
            }
            studio4.getCamera().setYaw(0.0f);
            Studio studio5 = this$0.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio5;
            }
            studio2.getCamera().setPitch(-0.4f);
            Unit unit = Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$212$lambda$211(final MainActivity this$0) {
        SettingsDialog.INSTANCE.pickFlySpeed(this$0, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda58
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$212$lambda$211$lambda$210(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$212$lambda$211$lambda$210(MainActivity this$0) {
        this$0.buildRibbon();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$214(final MainActivity this$0) {
        tool$default(this$0, g$default(this$0, "Settings", null, 2, null), "Settings", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda122
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$214$lambda$213(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$214$lambda$213(MainActivity this$0) {
        this$0.showSettings();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$220(final MainActivity this$0) {
        tool$default(this$0, this$0.g("ManagePlugins", "Mgr"), "Manage", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda163
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$220$lambda$215(this$0);
            }
        }, 4, null);
        tool$default(this$0, g$default(this$0, "Download", null, 2, null), "From Roblox", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda164
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$220$lambda$216(this$0);
            }
        }, 4, null);
        tool$default(this$0, g$default(this$0, "Open", null, 2, null), "From File", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda165
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$220$lambda$217(this$0);
            }
        }, 4, null);
        tool$default(this$0, g$default(this$0, "Toolbox", null, 2, null), "Store", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda166
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$220$lambda$218(this$0);
            }
        }, 4, null);
        tool$default(this$0, this$0.g("PluginsFolder", "Rld"), "Reload", false, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda167
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.buildRibbon$lambda$220$lambda$219(this$0);
            }
        }, 4, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$220$lambda$215(MainActivity this$0) {
        this$0.managePlugins();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$220$lambda$216(MainActivity this$0) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        AccountUi.installPluginFromRoblox$default(accountUi, mainActivity, studio, this$0, null, 8, null);
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$220$lambda$217(MainActivity this$0) {
        this$0.pickPluginFile();
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$220$lambda$218(MainActivity this$0) {
        try {
        DockLayout dockLayout = this$0.dock;
        ToolboxPanel toolboxPanel = null;
        if (dockLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
            dockLayout = null;
        }
        dockLayout.setShown("toolbox", true);
        ToolboxPanel toolboxPanel2 = this$0.toolbox;
        if (toolboxPanel2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("toolbox");
        } else {
            toolboxPanel = toolboxPanel2;
        }
        toolboxPanel.showMarketplace(RobloxApi.Category.PLUGINS);
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit buildRibbon$lambda$220$lambda$219(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.getPlugins().loadAll();
        OutputLog.INSTANCE.info("Plugins reloaded");
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0040  */
    static final Unit buildRibbon$lambda$225(List $buttons, final MainActivity this$0) {
        String label;
        Iterator it = $buttons.iterator();
        while (it.hasNext()) {
            final PluginButton b = (PluginButton) it.next();
            Object obj = b.getInst().get("Text");
            String str = obj instanceof String ? (String) obj : null;
            if (str != null) {
                String name = str;
                if (name.length() == 0) {
                    name = b.getInst().getName();
                }
                label = name;
                if (label == null) {
                    label = b.getInst().getName();
                }
            } else {
                label = b.getInst().getName();
            }
            Intrinsics.checkNotNull(b);
            this$0.tool(this$0.pluginIcon(b), StringsKt.take(label, 14), Intrinsics.areEqual(b.getInst().get("Active"), (Object) true), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.buildRibbon$lambda$225$lambda$224(this$0, b);
                }
            });
        }
        return Unit.INSTANCE;
    }

    static final Unit buildRibbon$lambda$225$lambda$224(MainActivity this$0, PluginButton $b) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Intrinsics.checkNotNull($b);
        studio.clickPluginButton($b);
        return Unit.INSTANCE;
    }

    private final void pickPartShape() {
        final List shapes = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Block", 1), TuplesKt.to("Sphere", 0), TuplesKt.to("Wedge", 3), TuplesKt.to("Corner Wedge", 4), TuplesKt.to("Cylinder", 2)});
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(this).setTitle("Part");
        List list = shapes;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getFirst());
        }
        title.setItems((CharSequence[]) ((List) arrayList).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda14
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.pickPartShape$lambda$227(MainActivity.this, shapes, dialogInterface, i);
            }
        }).show();
    }

    static final void pickPartShape$lambda$227(MainActivity this$0, List $shapes, DialogInterface dialogInterface, int i) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.insertPart(((Number) ((Pair) $shapes.get(i)).getSecond()).intValue());
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio2.markDirty();
    }

    private final void snapGroup() {
        group("Snap to Grid", new Function0<Unit>() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda97
            @Override // kotlin.jvm.functions.Function0
            public final Unit invoke() {
                return MainActivity.snapGroup$lambda$234(MainActivity.this);
            }
        });
    }

    static final Unit snapGroup$lambda$234(final MainActivity this$0) {
        LinearLayout col = new LinearLayout(this$0);
        col.setOrientation(1);
        col.setGravity(16);
        col.setPadding(this$0.dp(4.0f), 0, this$0.dp(4.0f), 0);
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        boolean rotSnapEnabled = studio.getRotSnapEnabled();
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        col.addView(this$0.snapRow("Rotate", rotSnapEnabled, studio3.getRotSnap(), "°", new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda59
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.snapGroup$lambda$234$lambda$229(this$0, ((Boolean) obj).booleanValue());
            }
        }, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda60
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.snapGroup$lambda$234$lambda$230(this$0, ((Float) obj).floatValue());
            }
        }));
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        boolean snapEnabled = studio4.getSnapEnabled();
        Studio studio5 = this$0.studio;
        if (studio5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio5 = null;
        }
        col.addView(this$0.snapRow("Move", snapEnabled, studio5.getMoveSnap(), "studs", new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda61
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.snapGroup$lambda$234$lambda$231(this$0, ((Boolean) obj).booleanValue());
            }
        }, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda62
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.snapGroup$lambda$234$lambda$232(this$0, ((Float) obj).floatValue());
            }
        }));
        LinearLayout linearLayout = this$0.groupRow;
        if (linearLayout != null) {
            linearLayout.addView(col);
        }
        Drawable drawableG = this$0.g("Collisions", "Col");
        Studio studio6 = this$0.studio;
        if (studio6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio6;
        }
        this$0.tool(drawableG, "Collisions", studio2.getCollisionsEnabled(), new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda63
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.snapGroup$lambda$234$lambda$233(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit snapGroup$lambda$234$lambda$229(MainActivity this$0, boolean it) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.setRotSnapEnabled(it);
        return Unit.INSTANCE;
    }

    static final Unit snapGroup$lambda$234$lambda$230(MainActivity this$0, float it) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.setRotSnap(RangesKt.coerceIn(it, 0.01f, 360.0f));
        return Unit.INSTANCE;
    }

    static final Unit snapGroup$lambda$234$lambda$231(MainActivity this$0, boolean it) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.setSnapEnabled(it);
        return Unit.INSTANCE;
    }

    static final Unit snapGroup$lambda$234$lambda$232(MainActivity this$0, float it) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.setMoveSnap(RangesKt.coerceIn(it, 0.001f, 2048.0f));
        return Unit.INSTANCE;
    }

    static final Unit snapGroup$lambda$234$lambda$233(MainActivity this$0) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        studio.setCollisionsEnabled(!studio3.getCollisionsEnabled());
        Studio studio4 = this$0.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio4;
        }
        this$0.toast(studio2.getCollisionsEnabled() ? "Collisions on: moved parts stop at other parts" : "Collisions off");
        this$0.buildRibbon();
        return Unit.INSTANCE;
    }

    private final View snapRow(String label, final boolean on, final float value, String unit, final Function1<? super Boolean, Unit> toggle, final Function1<? super Float, Unit> set) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(0);
        row.setGravity(16);
        row.setPadding(0, dp(1.0f), 0, dp(1.0f));
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(new Ui.CheckBoxDrawable(on, imageView.getResources().getDisplayMetrics().density));
        imageView.setPadding(dp(3.0f), dp(3.0f), dp(3.0f), dp(3.0f));
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda78
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.snapRow$lambda$237$lambda$236(toggle, on, MainActivity.this, view);
            }
        });
        row.addView(imageView, Ui.lp$default(Ui.INSTANCE, dp(22.0f), dp(22.0f), 0.0f, 4, null));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this, label, 11.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default.setPadding(dp(2.0f), 0, dp(4.0f), 0);
        textViewText$default.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda79
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.snapRow$lambda$239$lambda$238(toggle, on, MainActivity.this, view);
            }
        });
        row.addView(textViewText$default, Ui.lp$default(Ui.INSTANCE, dp(44.0f), -2, 0.0f, 4, null));
        final EditText editTextInput$default = Ui.input$default(Ui.INSTANCE, this, DataTypesKt.fmtNum(value), "", true, false, 16, null);
        editTextInput$default.setTextSize(11.0f);
        editTextInput$default.setPadding(dp(5.0f), dp(2.0f), dp(5.0f), dp(2.0f));
        editTextInput$default.setSingleLine(true);
        editTextInput$default.setImeOptions(6);
        editTextInput$default.setEnabled(on);
        editTextInput$default.setAlpha(on ? 1.0f : 0.5f);
        editTextInput$default.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda80
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return MainActivity.snapRow$lambda$242$lambda$240(MainActivity.this, editTextInput$default, set, value, textView, i, keyEvent);
            }
        });
        editTextInput$default.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda81
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z) {
                MainActivity.snapRow$lambda$242$lambda$241(editTextInput$default, set, value, view, z);
            }
        });
        row.addView(editTextInput$default, Ui.lp$default(Ui.INSTANCE, dp(52.0f), dp(24.0f), 0.0f, 4, null));
        TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, this, unit, 10.5f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default2.setPadding(dp(4.0f), 0, 0, 0);
        row.addView(textViewText$default2);
        return row;
    }

    static final void snapRow$lambda$237$lambda$236(Function1 $toggle, boolean $on, MainActivity this$0, View it) {
        $toggle.invoke(Boolean.valueOf(!$on));
        this$0.buildRibbon();
    }

    static final void snapRow$lambda$239$lambda$238(Function1 $toggle, boolean $on, MainActivity this$0, View it) {
        $toggle.invoke(Boolean.valueOf(!$on));
        this$0.buildRibbon();
    }

    private static final void snapRow$lambda$242$commit(EditText $this_apply, Function1<? super Float, Unit> function1, float $value) {
        Float v = StringsKt.toFloatOrNull(StringsKt.trim((CharSequence) StringsKt.replace($this_apply.getText().toString(), ',', '.', false)).toString());
        if (v == null || v.floatValue() <= 0.0f) {
            $this_apply.setText(DataTypesKt.fmtNum($value));
        } else {
            function1.invoke(v);
        }
    }

    static final boolean snapRow$lambda$242$lambda$240(MainActivity this$0, EditText $this_apply, Function1 $set, float $value, TextView v, int i, KeyEvent keyEvent) {
        snapRow$lambda$242$commit($this_apply, $set, $value);
        v.clearFocus();
        Object systemService = this$0.getSystemService("input_method");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
        ((InputMethodManager) systemService).hideSoftInputFromWindow(v.getWindowToken(), 0);
        return true;
    }

    static final void snapRow$lambda$242$lambda$241(EditText $this_apply, Function1 $set, float $value, View view, boolean has) {
        if (has) {
            return;
        }
        snapRow$lambda$242$commit($this_apply, $set, $value);
    }

    private final Drawable pluginIcon(PluginButton b) {
        Object obj = b.getInst().get("Icon");
        if (obj == null) {
            obj = b.getInst().get("Image");
        }
        String content = obj != null ? obj.toString() : null;
        Bitmap bmp = ImageLoader.INSTANCE.get(content);
        return bmp != null ? new BitmapDrawable(getResources(), bmp) : inst("Plugin");
    }

    private final void solid(Function0<String> op) {
        String msg;
        Iterable iterableValues = this.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
        try {
            msg = op.invoke();
        } catch (Exception e) {
            String message = e.getMessage();
            msg = "Couldn't do that: " + (message != null ? message : e);
        }
        if (msg != null) {
            toast(msg);
            return;
        }
        Studio studio = this.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.markDirty();
    }

    private final void pickColorForSelection() {
        final List<Instance> listSelectedParts;
        Color3 c0;
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            listSelectedParts = studio3.selectedParts();
        }
        if (listSelectedParts.isEmpty()) {
            toast("Select a part first");
            return;
        }
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio4;
        }
        synchronized (studio2.getLock()) {
            c0 = PartData.INSTANCE.color(listSelectedParts.get(0));
        }
        Dialogs.INSTANCE.colorPicker(this, "Color", c0, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda121
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MainActivity.pickColorForSelection$lambda$248(MainActivity.this, listSelectedParts, obj);
            }
        });
    }

    static final Unit pickColorForSelection$lambda$248(MainActivity this$0, List $parts, Object c) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this$0.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            studio3.getUndo().begin("Color");
            Iterator it = $parts.iterator();
            while (it.hasNext()) {
                Instance instance = (Instance) it.next();
                String str = (instance.getProps().containsKey("Color3uint8") || !instance.getProps().containsKey("Color")) ? "Color3uint8" : "Color";
                Studio studio4 = this$0.studio;
                if (studio4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio4 = null;
                }
                studio4.setProp(instance, str, c);
                instance.getPropTypes().putIfAbsent(str, 26);
            }
            Studio studio5 = this$0.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio5;
            }
            studio2.getUndo().commit();
            Unit unit = Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    private final void pickMaterialForSelection() {
        final List<Instance> listSelectedParts;
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio3;
            }
            listSelectedParts = studio2.selectedParts();
        }
        if (listSelectedParts.isEmpty()) {
            toast("Select a part first");
            return;
        }
        LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get("Material");
        Intrinsics.checkNotNull(linkedHashMap);
        Iterable iterableEntrySet = linkedHashMap.entrySet();
        Intrinsics.checkNotNullExpressionValue(iterableEntrySet, "<get-entries>(...)");
        Collection arrayList = new ArrayList();
        Iterator it = iterableEntrySet.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            Map.Entry entry = (Map.Entry) next;
            if (!Intrinsics.areEqual(entry.getKey(), "Air") && !Intrinsics.areEqual(entry.getKey(), "Water")) {
                z = true;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        final List items = (List) arrayList;
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(this).setTitle("Material");
        List list = items;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            arrayList2.add((String) ((Map.Entry) it2.next()).getKey());
        }
        title.setItems((CharSequence[]) ((List) arrayList2).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda54
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.pickMaterialForSelection$lambda$253(MainActivity.this, listSelectedParts, items, dialogInterface, i);
            }
        }).show();
    }

    static final void pickMaterialForSelection$lambda$253(MainActivity this$0, List $parts, List $items, DialogInterface dialogInterface, int i) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this$0.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            studio3.getUndo().begin("Material");
            Iterator it = $parts.iterator();
            while (it.hasNext()) {
                Instance instance = (Instance) it.next();
                Studio studio4 = this$0.studio;
                if (studio4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio4 = null;
                }
                Object value = ((Map.Entry) $items.get(i)).getValue();
                Intrinsics.checkNotNullExpressionValue(value, "<get-value>(...)");
                studio4.setProp(instance, "Material", new EnumValue(((Number) value).intValue()));
            }
            Studio studio5 = this$0.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio5;
            }
            studio2.getUndo().commit();
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void pickSurfaceForSelection() {
        final List<Instance> listSelectedParts;
        Studio studio = this.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio2 = this.studio;
            if (studio2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio2 = null;
            }
            listSelectedParts = studio2.selectedParts();
        }
        if (listSelectedParts.isEmpty()) {
            toast("Select a part first");
            return;
        }
        final List faces = CollectionsKt.listOf(new Pair[]{TuplesKt.to("All faces", null), TuplesKt.to("Top", "TopSurface"), TuplesKt.to("Bottom", "BottomSurface"), TuplesKt.to("Front", "FrontSurface"), TuplesKt.to("Back", "BackSurface"), TuplesKt.to("Left", "LeftSurface"), TuplesKt.to("Right", "RightSurface")});
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(this).setTitle("Surface — which face?");
        List list = faces;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getFirst());
        }
        title.setItems((CharSequence[]) ((List) arrayList).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda114
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.pickSurfaceForSelection$lambda$261(MainActivity.this, faces, listSelectedParts, dialogInterface, i);
            }
        }).show();
    }

    static final void pickSurfaceForSelection$lambda$261(final MainActivity this$0, final List $faces, final List $parts, DialogInterface dialogInterface, final int fi) {
        LinkedHashMap<String, Integer> linkedHashMap = Enums.INSTANCE.getTypes().get("SurfaceType");
        Intrinsics.checkNotNull(linkedHashMap);
        Set<Map.Entry<String, Integer>> setEntrySet = linkedHashMap.entrySet();
        Intrinsics.checkNotNullExpressionValue(setEntrySet, "<get-entries>(...)");
        final List types = CollectionsKt.toList(setEntrySet);
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(this$0).setTitle(((Pair) $faces.get(fi)).getFirst() + ": surface type");
        List list = types;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Map.Entry) it.next()).getKey());
        }
        title.setItems((CharSequence[]) ((List) arrayList).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda83
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface2, int i) {
                try {
                    MainActivity.pickSurfaceForSelection$lambda$261$lambda$260($faces, fi, this$0, $parts, types, dialogInterface2, i);
                } catch (Throwable _t) {
                    if (_t instanceof RuntimeException) throw (RuntimeException) _t;
                    if (_t instanceof Error) throw (Error) _t;
                    throw new RuntimeException(_t);
                }
            }
        }).show();
    }

    static final void pickSurfaceForSelection$lambda$261$lambda$260(List $faces, int $fi, MainActivity this$0, List $parts, List $types, DialogInterface dialogInterface, int ti) {
        try {
        List listListOf;
        String str = (String) ((Pair) $faces.get($fi)).getSecond();
        if (str == null || (listListOf = CollectionsKt.listOf(str)) == null) {
            Collection arrayList = new ArrayList();
            Iterator it = $faces.iterator();
            while (it.hasNext()) {
                String str2 = (String) ((Pair) it.next()).getSecond();
                if (str2 != null) {
                    arrayList.add(str2);
                }
            }
            listListOf = (List) arrayList;
        }
        List<String> props = listListOf;
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            try {
                Studio studio3 = this$0.studio;
                if (studio3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio3 = null;
                }
                studio3.getUndo().begin("Surface");
                Iterator it2 = $parts.iterator();
                while (it2.hasNext()) {
                    Instance instance = (Instance) it2.next();
                    for (String str3 : props) {
                        Studio studio4 = this$0.studio;
                        if (studio4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                            studio4 = null;
                        }
                        try {
                            Object value = ((Map.Entry) $types.get(ti)).getValue();
                            Intrinsics.checkNotNullExpressionValue(value, "<get-value>(...)");
                            studio4.setProp(instance, str3, new EnumValue(((Number) value).intValue()));
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }
                Studio studio5 = this$0.studio;
                if (studio5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                } else {
                    studio2 = studio5;
                }
                studio2.getUndo().commit();
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final void togglePlay() {
        Iterable iterableValues = this.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        if (!studio.isPlaying()) {
            showPlace();
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio3;
            }
            studio2.startPlay();
            return;
        }
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio4;
        }
        studio2.stopPlay();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onPlayStateChanged() {
        TextView textView = this.playButton;
        ViewportView viewportView = null;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("playButton");
            textView = null;
        }
        Studio studio = this.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        textView.setText(studio.isPlaying() ? "Stop" : "Play");
        Studio studio2 = this.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        Drawable ic = studio2.isPlaying() ? g$default(this, "Stop", null, 2, null) : g$default(this, "Play", null, 2, null);
        ic.setBounds(0, 0, dp(18.0f), dp(18.0f));
        TextView textView2 = this.playButton;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("playButton");
            textView2 = null;
        }
        textView2.setCompoundDrawables(ic, null, null, null);
        TextView textView3 = this.playButton;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("playButton");
            textView3 = null;
        }
        Ui ui = Ui.INSTANCE;
        MainActivity mainActivity = this;
        Studio studio3 = this.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        textView3.setBackground(Ui.rippleBg$default(ui, mainActivity, studio3.isPlaying() ? Ui.STOP : -13726889, 0.0f, 4, null));
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        if (studio4.isPlaying() && Settings.INSTANCE.getHideWindowsInPlaytest() && this.hiddenBeforePlay == null) {
            DockLayout dockLayout = this.dock;
            if (dockLayout == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dock");
                dockLayout = null;
            }
            this.hiddenBeforePlay = Boolean.valueOf(dockLayout.getAllHidden());
            setAllHidden(true);
        } else {
            Studio studio5 = this.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio5 = null;
            }
            if (!studio5.isPlaying()) {
                Boolean bool = this.hiddenBeforePlay;
                if (bool != null) {
                    setAllHidden(bool.booleanValue());
                }
                this.hiddenBeforePlay = null;
            }
        }
        ViewportView viewportView2 = this.viewport;
        if (viewportView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("viewport");
        } else {
            viewportView = viewportView2;
        }
        viewportView.getHud().onPlayStateChanged();
        buildRibbon();
        updateTitle();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onToolChanged() {
        if ((Intrinsics.areEqual(this.ribbonTab, StartScreen.HOME) || Intrinsics.areEqual(this.ribbonTab, "Model")) && !(getCurrentFocus() instanceof EditText)) {
            buildRibbon();
        }
        ViewportView viewportView = this.viewport;
        if (viewportView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("viewport");
            viewportView = null;
        }
        viewportView.getControls().invalidate();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onPluginsChanged() {
        if (Intrinsics.areEqual(this.ribbonTab, "Plugins")) {
            buildRibbon();
        }
        ViewportView viewportView = this.viewport;
        if (viewportView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("viewport");
            viewportView = null;
        }
        viewportView.getControls().invalidate();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onSelectionChanged() {
        if (!Intrinsics.areEqual(this.ribbonTab, StartScreen.HOME) || (getCurrentFocus() instanceof EditText)) {
            return;
        }
        buildRibbon();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onDirtyChanged() {
        updateTitle();
    }

    @Override // com.goydevv.studrblx.core.Studio.Listener
    public void onModelReplaced() {
        Iterator it = CollectionsKt.toList(this.editorOrder).iterator();
        while (true) {
            Studio studio = null;
            if (!it.hasNext()) {
                break;
            }
            Instance instance = (Instance) it.next();
            DataModel dataModelRoot = instance.root();
            if (dataModelRoot != null) {
                Studio studio2 = this.studio;
                if (studio2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio2 = null;
                }
                if (dataModelRoot != studio2.getEditModel()) {
                    Studio studio3 = this.studio;
                    if (studio3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("studio");
                    } else {
                        studio = studio3;
                    }
                    if (dataModelRoot == studio.getModel()) {
                    }
                }
            }
            closeScript(instance);
        }
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        if (!studio4.isPlaying()) {
            Application application = getApplication();
            StudioApp studioApp = application instanceof StudioApp ? (StudioApp) application : null;
            if (studioApp != null) {
                studioApp.setPlaceOpened(true);
            }
            if (this.startScreen != null) {
                closeStart();
            }
        }
        updateTitle();
    }

    private final void updateTitle() {
        String name;
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        CloudPlace c = studio.getCloud();
        if (c != null) {
            name = c.getName() + "  (Roblox place)";
        } else {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            name = studio3.getFileName();
        }
        TextView textView = this.titleView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleView");
            textView = null;
        }
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        String str = studio4.getDirty() ? "● " : "";
        Studio studio5 = this.studio;
        if (studio5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio5;
        }
        textView.setText(str + name + (studio2.isPlaying() ? "  (Playing)" : ""));
    }

    /* JADX INFO: Access modifiers changed from: private */

    private final void buildScriptRibbon() {
        final MainActivity self = this;
        group("Scripts", new Function0() {
            @Override
            public final Object invoke() {
                tool$default(self, self.inst("Script"), "All Scripts", false, new Function0() {
                    @Override
                    public final Object invoke() {
                        self.showScriptManagerDialog();
                        return Unit.INSTANCE;
                    }
                }, 4, null);
                tool$default(self, g$default(self, "Open", null, 2, null), "Open Selected", false, new Function0() {
                    @Override
                    public final Object invoke() {
                        Studio st = self.studio;
                        if (st == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                            st = null;
                        }
                        Instance sel = (Instance) CollectionsKt.firstOrNull(st.getSelection());
                        if (sel != null && sel.isA("LuaSourceContainer")) {
                            self.openScript(sel);
                        } else {
                            self.showScriptManagerDialog();
                        }
                        return Unit.INSTANCE;
                    }
                }, 4, null);
                return Unit.INSTANCE;
            }
        });
        group("Create", new Function0() {
            @Override
            public final Object invoke() {
                tool$default(self, self.inst("Script"), "New Script", false, new Function0() {
                    @Override
                    public final Object invoke() {
                        self.createAndOpenScript("Script", "ServerScriptService");
                        return Unit.INSTANCE;
                    }
                }, 4, null);
                tool$default(self, self.inst("LocalScript"), "LocalScript", false, new Function0() {
                    @Override
                    public final Object invoke() {
                        self.createAndOpenScript("LocalScript", "StarterPlayer");
                        return Unit.INSTANCE;
                    }
                }, 4, null);
                tool$default(self, self.inst("ModuleScript"), "Module", false, new Function0() {
                    @Override
                    public final Object invoke() {
                        self.createAndOpenScript("ModuleScript", "ReplicatedStorage");
                        return Unit.INSTANCE;
                    }
                }, 4, null);
                return Unit.INSTANCE;
            }
        });
        group("Debug", new Function0() {
            @Override
            public final Object invoke() {
                DockLayout dl = self.dock;
                if (dl == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("dock");
                    dl = null;
                }
                self.tool(g$default(self, "Output", null, 2, null), "Output", dl.isShown("output"), new Function0() {
                    @Override
                    public final Object invoke() {
                        return MainActivity.buildRibbon$lambda$206$lambda$203(self);
                    }
                });
                return Unit.INSTANCE;
            }
        });
    }

    private final void createAndOpenScript(String className, String defaultService) {
        Studio st = this.studio;
        if (st == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            st = null;
        }
        Instance parent = (Instance) CollectionsKt.firstOrNull(st.getSelection());
        if (parent == null || parent.isA("LuaSourceContainer")) {
            Instance svc = st.getModel().getService(defaultService);
            if ("LocalScript".equals(className) && svc != null) {
                Instance sps = Instance.findFirstChild$default(svc, "StarterPlayerScripts", false, 2, null);
                parent = sps != null ? sps : svc;
            } else {
                parent = svc;
            }
        }
        Instance created = st.insertObject(className, parent);
        if (created != null && created.isA("LuaSourceContainer")) {
            openScript(created);
        }
    }

    private final void showScriptManagerDialog() {
        Studio st = this.studio;
        if (st == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            st = null;
        }
        final ArrayList<Instance> scripts = new ArrayList<>();
        for (Instance inst : Instance.descendants$default(st.getModel(), null, 1, null)) {
            if (inst.isA("LuaSourceContainer")) {
                scripts.add(inst);
            }
        }
        if (scripts.isEmpty()) {
            new android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setTitle("Script Manager")
                .setMessage("No scripts in this place yet. Create a Script, LocalScript, or ModuleScript?")
                .setPositiveButton("New Script", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface d, int w) {
                        createAndOpenScript("Script", "ServerScriptService");
                    }
                })
                .setNeutralButton("New LocalScript", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface d, int w) {
                        createAndOpenScript("LocalScript", "StarterPlayer");
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
            return;
        }
        CharSequence[] labels = new CharSequence[scripts.size()];
        for (int i = 0; i < scripts.size(); i++) {
            Instance sc = scripts.get(i);
            StringBuilder path = new StringBuilder(sc.getName());
            Instance par = sc.getParent();
            int depth = 0;
            while (par != null && depth < 3 && !"DataModel".equals(par.getClassName())) {
                path.insert(0, par.getName() + "/");
                par = par.getParent();
                depth++;
            }
            labels[i] = "[" + sc.getClassName() + "] " + path.toString();
        }
        new android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
            .setTitle("Scripts (" + scripts.size() + ") — Tap to Edit")
            .setItems(labels, new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface d, int which) {
                    if (which >= 0 && which < scripts.size()) {
                        openScript(scripts.get(which));
                    }
                }
            })
            .setPositiveButton("New Script", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface d, int w) {
                    createAndOpenScript("Script", "ServerScriptService");
                }
            })
            .setNeutralButton("New LocalScript", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface d, int w) {
                    createAndOpenScript("LocalScript", "StarterPlayer");
                }
            })
            .setNegativeButton("Close", null)
            .show();
    }

    public final void openScript(Instance s) {
        ScriptEditor scriptEditor;
        if (s.isA("LuaSourceContainer")) {
            Map map = this.openEditors;
            ScriptEditor scriptEditor2 = (ScriptEditor) map.get(s);
            FrameLayout frameLayout = null;
            if (scriptEditor2 == null) {
                this.editorOrder.add(s);
                MainActivity mainActivity = this;
                Studio studio = this.studio;
                if (studio == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio = null;
                }
                scriptEditor = new ScriptEditor(mainActivity, studio, s);
                map.put(s, scriptEditor);
            } else {
                scriptEditor = scriptEditor2;
            }
            ScriptEditor ed = scriptEditor;
            this.activeScript = s;
            FrameLayout frameLayout2 = this.editorHost;
            if (frameLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("editorHost");
                frameLayout2 = null;
            }
            frameLayout2.removeAllViews();
            FrameLayout frameLayout3 = this.editorHost;
            if (frameLayout3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("editorHost");
                frameLayout3 = null;
            }
            frameLayout3.addView(ed, new FrameLayout.LayoutParams(-1, -1));
            FrameLayout frameLayout4 = this.editorHost;
            if (frameLayout4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("editorHost");
            } else {
                frameLayout = frameLayout4;
            }
            frameLayout.setVisibility(0);
            rebuildScriptTabs();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showPlace() {
        ScriptEditor scriptEditor;
        Instance instance = this.activeScript;
        if (instance != null && (scriptEditor = this.openEditors.get(instance)) != null) {
            scriptEditor.commit();
        }
        FrameLayout frameLayout = null;
        this.activeScript = null;
        FrameLayout frameLayout2 = this.editorHost;
        if (frameLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editorHost");
            frameLayout2 = null;
        }
        frameLayout2.removeAllViews();
        FrameLayout frameLayout3 = this.editorHost;
        if (frameLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editorHost");
        } else {
            frameLayout = frameLayout3;
        }
        frameLayout.setVisibility(8);
        rebuildScriptTabs();
    }

    private final void closeScript(Instance s) {
        ScriptEditor scriptEditorRemove = this.openEditors.remove(s);
        if (scriptEditorRemove != null) {
            scriptEditorRemove.commit();
        }
        this.editorOrder.remove(s);
        if (this.activeScript == s) {
            FrameLayout frameLayout = null;
            this.activeScript = null;
            FrameLayout frameLayout2 = this.editorHost;
            if (frameLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("editorHost");
                frameLayout2 = null;
            }
            frameLayout2.removeAllViews();
            FrameLayout frameLayout3 = this.editorHost;
            if (frameLayout3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("editorHost");
            } else {
                frameLayout = frameLayout3;
            }
            frameLayout.setVisibility(8);
        }
        rebuildScriptTabs();
    }

    private final void rebuildScriptTabs() {
        LinearLayout linearLayout = this.scriptTabs;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptTabs");
            linearLayout = null;
        }
        linearLayout.removeAllViews();
        HorizontalScrollView horizontalScrollView = this.scriptTabsScroll;
        if (horizontalScrollView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptTabsScroll");
            horizontalScrollView = null;
        }
        horizontalScrollView.setVisibility(this.editorOrder.isEmpty() ? 8 : 0);
        if (this.editorOrder.isEmpty()) {
            return;
        }
        Drawable drawableG$default = g$default(this, "Place", null, 2, null);
        Studio studio = this.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        rebuildScriptTabs$tab(this, drawableG$default, com.goydevv.studrblx.util.KtCompat.substringBeforeLast(studio.getFileName(), '.'), this.activeScript == null, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda157
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.rebuildScriptTabs$lambda$271(MainActivity.this);
            }
        }, null);
        Iterator<Instance> it = this.editorOrder.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            final Instance s = it.next();
            rebuildScriptTabs$tab(this, Ui.INSTANCE.icon(this, s), s.getName(), s == this.activeScript, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda158
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.rebuildScriptTabs$lambda$272(MainActivity.this, s);
                }
            }, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda159
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return MainActivity.rebuildScriptTabs$lambda$273(MainActivity.this, s);
                }
            });
        }
    }

    private static final void rebuildScriptTabs$tab(MainActivity this$0, Drawable icon, String label, boolean active, final Function0<Unit> function0, final Function0<Unit> function1) {
        LinearLayout row = new LinearLayout(this$0);
        row.setOrientation(0);
        row.setGravity(16);
        Ui ui = Ui.INSTANCE;
        row.setBackgroundColor(active ? ui.getEDITOR_BG() : ui.getHEADER());
        row.setPadding(this$0.dp(10.0f), this$0.dp(3.0f), this$0.dp(4.0f), this$0.dp(3.0f));
        row.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda123
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                function0.invoke();
            }
        });
        ImageView imageView = new ImageView(this$0);
        imageView.setImageDrawable(icon);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, this$0.dp(16.0f), this$0.dp(16.0f), 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = this$0.dp(6.0f);
        Unit unit = Unit.INSTANCE;
        row.addView(imageView, layoutParamsLp$default);
        Ui ui2 = Ui.INSTANCE;
        MainActivity mainActivity = this$0;
        Ui ui3 = Ui.INSTANCE;
        row.addView(Ui.text$default(ui2, mainActivity, label, 12.0f, active ? ui3.getTEXT() : ui3.getDIM(), false, 16, null));
        if (function1 == null) {
            row.addView(new View(this$0), Ui.lp$default(Ui.INSTANCE, this$0.dp(8.0f), this$0.dp(26.0f), 0.0f, 4, null));
        } else {
            row.addView(Ui.INSTANCE.iconButton(this$0, Icons.INSTANCE.ui(this$0, "CloseWidget", "x"), 26.0f, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda124
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return MainActivity.rebuildScriptTabs$tab$lambda$270(function1, (View) obj);
                }
            }));
        }
        LinearLayout linearLayout = this$0.scriptTabs;
        LinearLayout linearLayout2 = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptTabs");
            linearLayout = null;
        }
        linearLayout.addView(row);
        LinearLayout linearLayout3 = this$0.scriptTabs;
        if (linearLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scriptTabs");
        } else {
            linearLayout2 = linearLayout3;
        }
        linearLayout2.addView(Ui.INSTANCE.divider(this$0, true));
    }

    static final Unit rebuildScriptTabs$tab$lambda$270(Function0 $onClose, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $onClose.invoke();
        return Unit.INSTANCE;
    }

    static final Unit rebuildScriptTabs$lambda$271(MainActivity this$0) {
        this$0.showPlace();
        return Unit.INSTANCE;
    }

    static final Unit rebuildScriptTabs$lambda$272(MainActivity this$0, Instance $s) {
        this$0.openScript($s);
        return Unit.INSTANCE;
    }

    static final Unit rebuildScriptTabs$lambda$273(MainActivity this$0, Instance $s) {
        this$0.closeScript($s);
        return Unit.INSTANCE;
    }

    private final void showFileMenu(View anchor) {
        String str;
        PopupMenu m = new PopupMenu(Ui.INSTANCE.popupContext(this), anchor);
        boolean signedIn = RobloxApi.INSTANCE.cookie(this).length() > 0;
        Studio studio = this.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        CloudPlace cloud = studio.getCloud();
        Pair[] pairArr = new Pair[18];
        pairArr[0] = TuplesKt.to("Home (Start Page)", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda26
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$274(MainActivity.this);
            }
        });
        pairArr[1] = TuplesKt.to("New Place", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda36
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$276(MainActivity.this);
            }
        });
        pairArr[2] = TuplesKt.to("Open File… (.rbxl / .rbxm)", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda37
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$277(MainActivity.this);
            }
        });
        pairArr[3] = TuplesKt.to("Open from Roblox (My Games)…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda38
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$278(MainActivity.this);
            }
        });
        pairArr[4] = TuplesKt.to(cloud != null ? "Save to Roblox" : "Save", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda39
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$279(MainActivity.this);
            }
        });
        pairArr[5] = TuplesKt.to(cloud != null ? "Save a Copy to This Device…" : "Save As…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda40
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$280(MainActivity.this);
            }
        });
        pairArr[6] = TuplesKt.to(cloud != null ? "Publish to Roblox (" + cloud.getName() + ")" : "Publish to Roblox…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda41
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$281(MainActivity.this);
            }
        });
        pairArr[7] = TuplesKt.to("Publish to Roblox As…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda42
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$282(MainActivity.this);
            }
        });
        pairArr[8] = TuplesKt.to(cloud != null ? "Download / Version History (" + cloud.getName() + ")…" : "Download Game / Old Version…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda43
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$283(MainActivity.this);
            }
        });
        pairArr[9] = TuplesKt.to("Export Selection (.rbxm)…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda44
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$284(MainActivity.this);
            }
        });
        pairArr[10] = TuplesKt.to("Import Model into Toolbox…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda27
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$285(MainActivity.this);
            }
        });
        pairArr[11] = TuplesKt.to("Install Plugin from Roblox (ID or link)…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda28
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$286(MainActivity.this);
            }
        });
        pairArr[12] = TuplesKt.to("Install Plugin from File…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda29
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$287(MainActivity.this);
            }
        });
        pairArr[13] = TuplesKt.to("Playtest Avatar…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda30
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$288(MainActivity.this);
            }
        });
        pairArr[14] = TuplesKt.to("Game Settings (Avatar: R6 / R15)…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda31
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$289(MainActivity.this);
            }
        });
        pairArr[15] = TuplesKt.to("Studio Settings…", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda32
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$290(MainActivity.this);
            }
        });
        if (signedIn) {
            String strUserName = RobloxApi.INSTANCE.userName(this);
            if (strUserName.length() == 0) {
                strUserName = "signed in";
            }
            str = "Roblox Account (@" + ((Object) strUserName) + ")…";
        } else {
            str = "Sign in to Roblox…";
        }
        pairArr[16] = TuplesKt.to(str, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda33
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$292(MainActivity.this);
            }
        });
        pairArr[17] = TuplesKt.to("Crash / Debug Logs (.txt)", new Function0() {
            @Override
            public final Object invoke() {
                StringBuilder sb = new StringBuilder();
                sb.append("=== StudioRblx Debug & Crash Log ===\n");
                try {
                    for (com.goydevv.studrblx.core.OutputLog.Entry en : com.goydevv.studrblx.core.OutputLog.INSTANCE.snapshot()) {
                        sb.append('[').append(en.getLevel()).append("] ").append(en.getText()).append('\n');
                    }
                } catch (Throwable ignoredLog) {}
                try {
                    java.lang.Process pLog = Runtime.getRuntime().exec(new String[]{"logcat", "-d", "-t", "200", "*:W"});
                    java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(pLog.getInputStream()));
                    sb.append("\n--- Logcat ---\n");
                    String ln;
                    while ((ln = br.readLine()) != null) sb.append(ln).append('\n');
                    br.close();
                } catch (Throwable ignoredLog2) {}
                android.content.Intent ci = new android.content.Intent(MainActivity.this, CrashActivity.class);
                ci.putExtra(CrashActivity.EXTRA_TRACE, sb.toString());
                MainActivity.this.startActivity(ci);
                return Unit.INSTANCE;
            }
        });
        final List items = CollectionsKt.listOfNotNull(pairArr);
        int i = 0;
        for (Object obj : items) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            m.getMenu().add(0, i, i, (String) ((Pair) obj).component1());
            i = i2;
        }
        m.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda35
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return MainActivity.showFileMenu$lambda$295(items, menuItem);
            }
        });
        m.show();
    }

    static final Unit showFileMenu$lambda$274(MainActivity this$0) {
        this$0.showStart();
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$276(final MainActivity this$0) {
        this$0.confirmDiscard(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda187
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showFileMenu$lambda$276$lambda$275(this$0);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$276$lambda$275(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.newPlace$default(studio, null, 1, null);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$277(MainActivity this$0) {
        this$0.openMode = OpenMode.PLACE;
        this$0.openLauncher.launch(new String[]{"*/*"});
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$278(MainActivity this$0) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        AccountUi.myGames$default(accountUi, mainActivity, studio, this$0, null, 8, null);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$279(MainActivity this$0) {
        save$default(this$0, null, 1, null);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$280(MainActivity this$0) {
        this$0.saveAs();
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$281(MainActivity this$0) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        accountUi.publish(mainActivity, studio, this$0);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$282(MainActivity this$0) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        accountUi.publishAs(mainActivity, studio, this$0);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$283(MainActivity this$0) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        accountUi.currentPlaceHistory(mainActivity, studio, this$0);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$284(MainActivity this$0) {
        this$0.exportSelection();
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$285(MainActivity this$0) {
        this$0.importToToolbox();
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$286(MainActivity this$0) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        AccountUi.installPluginFromRoblox$default(accountUi, mainActivity, studio, this$0, null, 8, null);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$287(MainActivity this$0) {
        this$0.pickPluginFile();
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$288(MainActivity this$0) {
        AvatarUi avatarUi = AvatarUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        avatarUi.playtestAvatar(mainActivity, studio);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$289(MainActivity this$0) {
        try {
        AvatarUi avatarUi = AvatarUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        avatarUi.gameSettings(mainActivity, studio);
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit showFileMenu$lambda$290(MainActivity this$0) {
        this$0.showSettings();
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$292(MainActivity this$0) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        accountUi.show(mainActivity, studio, this$0);
        return Unit.INSTANCE;
    }

    static final Unit showFileMenu$lambda$293(MainActivity this$0) {
        this$0.about();
        return Unit.INSTANCE;
    }

    static final boolean showFileMenu$lambda$295(List $items, MenuItem it) {
        ((Function0) ((Pair) $items.get(it.getItemId())).getSecond()).invoke();
        return true;
    }

    private final void showSettings() {
        SettingsDialog.INSTANCE.show(this, this, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda142
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$296(MainActivity.this);
            }
        }, new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda143
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.showSettings$lambda$297(MainActivity.this);
            }
        });
    }

    static final Unit showSettings$lambda$296(MainActivity this$0) {
        this$0.openMode = OpenMode.ICONS;
        this$0.openLauncher.launch(new String[]{"application/zip", "application/x-zip-compressed", "*/*"});
        return Unit.INSTANCE;
    }

    static final Unit showSettings$lambda$297(MainActivity this$0) {
        Icons.INSTANCE.remove(this$0);
        return Unit.INSTANCE;
    }

    private final void about() {
        {
                    StringBuilder sb = new StringBuilder();
                    sb.append("=== StudioRblx Debug & Crash Log ===\n");
                    try {
                        for (com.goydevv.studrblx.core.OutputLog.Entry en : com.goydevv.studrblx.core.OutputLog.INSTANCE.snapshot()) {
                            sb.append('[').append(en.getLevel()).append("] ").append(en.getText()).append('\n');
                        }
                    } catch (Throwable ignoredLog) {}
                    try {
                        java.lang.Process pLog = Runtime.getRuntime().exec(new String[]{"logcat", "-d", "-t", "200", "*:W"});
                        java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(pLog.getInputStream()));
                        sb.append("\n--- Logcat ---\n");
                        String ln;
                        while ((ln = br.readLine()) != null) sb.append(ln).append('\n');
                        br.close();
                    } catch (Throwable ignoredLog2) {}
                    android.content.Intent ci = new android.content.Intent(this, CrashActivity.class);
                    ci.putExtra(CrashActivity.EXTRA_TRACE, sb.toString());
                    startActivity(ci);
                }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void confirmDiscard(final Function0<Unit> then) {
        StringBuilder sbAppend;
        String str;
        Studio studio = this.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        if (!studio.getDirty()) {
            then.invoke();
            return;
        }
        Studio studio2 = this.studio;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio2 = null;
        }
        final CloudPlace c = studio2.getCloud();
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(this).setTitle("Unsaved changes");
        if (c != null) {
            sbAppend = new StringBuilder().append("Save changes to “").append(c.getName());
            str = "” on Roblox?";
        } else {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            sbAppend = new StringBuilder().append("Save changes to ").append(studio3.getFileName());
            str = "?";
        }
        title.setMessage(sbAppend.append(str).toString()).setPositiveButton("Save", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda72
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.confirmDiscard$lambda$298(c, MainActivity.this, then, dialogInterface, i);
            }
        }).setNegativeButton("Discard", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda73
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                then.invoke();
            }
        }).setNeutralButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    static final void confirmDiscard$lambda$298(CloudPlace $c, MainActivity this$0, Function0 $then, DialogInterface dialogInterface, int i) {
        if ($c != null) {
            this$0.save($then);
        } else {
            save$default(this$0, null, 1, null);
            $then.invoke();
        }
    }

    private final String displayName(Uri uri) {
        Uri uri2;
        String string = null;
        try {
            uri2 = uri;
            try {
                Cursor cursorQuery = getContentResolver().query(uri2, new String[]{"_display_name"}, null, null, null);
                if (cursorQuery != null) {
                    Cursor cursor = cursorQuery;
                    try {
                        Cursor cursor2 = cursor;
                        string = cursor2.moveToFirst() ? cursor2.getString(0) : null;
                        Unit unit = Unit.INSTANCE;
                        CloseableKt.closeFinally(cursor, null);
                    } catch (Throwable th) {
                        String str = string;
                        try {
                            throw th;
                        } catch (Throwable th2) {
                            try {
                                CloseableKt.closeFinally(cursor, th);
                                throw th2;
                            } catch (Exception e) {
                                string = str;
                            }
                        }
                    }
                }
            } catch (Exception e2) {
            }
        } catch (Exception e3) {
            uri2 = uri;
        }
        if (string != null) {
            return string;
        }
        String lastPathSegment = uri2.getLastPathSegment();
        String strSubstringAfterLast$default = lastPathSegment != null ? com.goydevv.studrblx.util.KtCompat.substringAfterLast(lastPathSegment, '/') : null;
        return strSubstringAfterLast$default == null ? "file" : strSubstringAfterLast$default;
    }

    private final void onFilePicked(final Uri uri) {
        final OpenMode mode = this.openMode;
        final String name = displayName(uri);
        if (mode == OpenMode.PLACE) {
            try {
                getContentResolver().takePersistableUriPermission(uri, 3);
            } catch (Exception e) {
                try {
                    getContentResolver().takePersistableUriPermission(uri, 1);
                } catch (Exception e2) {
                }
            }
        }
        toast("Loading " + name + "…");
        if (mode == OpenMode.ICONS) {
            new Thread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda76
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.onFilePicked$lambda$304(MainActivity.this, uri);
                }
            }).start();
        } else {
            new Thread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda77
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.onFilePicked$lambda$313(MainActivity.this, uri, mode, name);
                }
            }).start();
        }
    }

    static final void onFilePicked$lambda$304(final MainActivity this$0, Uri $uri) {
        try {
            InputStream inputStreamOpenInputStream = this$0.getContentResolver().openInputStream($uri);
            if (inputStreamOpenInputStream == null) {
                throw new Exception("Could not read file");
            }
            InputStream inputStream = inputStreamOpenInputStream;
            try {
                Icons icons = Icons.INSTANCE;
                Context applicationContext = this$0.getApplicationContext();
                Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
                final int n = icons.importZip(applicationContext, inputStream);
                CloseableKt.closeFinally(inputStream, null);
                this$0.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda184
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.onFilePicked$lambda$304$lambda$302(this$0, n);
                    }
                });
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(inputStream, th);
                    throw th2;
                }
            }
        } catch (Exception e) {
            this$0.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda185
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.onFilePicked$lambda$304$lambda$303(this$0, e);
                }
            });
        }
    }

    static final void onFilePicked$lambda$304$lambda$302(MainActivity this$0, int $n) {
        this$0.toast("Installed " + $n + " icons");
        OutputLog.INSTANCE.info("Icon pack installed (" + $n + " icons)");
    }

    static final void onFilePicked$lambda$304$lambda$303(MainActivity this$0, Exception $e) {
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(this$0).setTitle("Couldn't import icons");
        String message = $e.getMessage();
        if (message == null) {
            message = String.valueOf($e);
        }
        title.setMessage(message).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
    }

    static final void onFilePicked$lambda$313(final MainActivity mainActivity, final Uri uri, OpenMode openMode, final String str) {
        try {
            InputStream inputStreamOpenInputStream = mainActivity.getContentResolver().openInputStream(uri);
            try {
                if (inputStreamOpenInputStream != null) {
                    InputStream inputStream = inputStreamOpenInputStream;
                    try {
                        final byte[] bytes = ByteStreamsKt.readBytes(inputStream);
                        Studio studio = null;
                        CloseableKt.closeFinally(inputStream, null);
                        if (bytes != null) {
                            switch (WhenMappings.$EnumSwitchMapping$0[openMode.ordinal()]) {
                                case 1:
                                    return;
                                case 2:
                                    Studio studio2 = mainActivity.studio;
                                    if (studio2 == null) {
                                        Intrinsics.throwUninitializedPropertyAccessException("studio");
                                    } else {
                                        studio = studio2;
                                    }
                                    final File fileInstall = studio.getPlugins().install(str, bytes);
                                    mainActivity.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda4
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            MainActivity.onFilePicked$lambda$313$lambda$306(mainActivity, fileInstall);
                                        }
                                    });
                                    return;
                                case 3:
                                    RbxDocument rbxDocument = RbxFile.INSTANCE.read(bytes);
                                    if (RbxFile.INSTANCE.isPlace(rbxDocument)) {
                                        throw new Exception("This is a place file. Use File → Open to open places; the Toolbox holds models (.rbxm).");
                                    }
                                    Instance instance = (Instance) CollectionsKt.firstOrNull((List) rbxDocument.getRoots());
                                    final String className = instance != null ? instance.getClassName() : null;
                                    mainActivity.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda5
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            try {
                                                MainActivity.onFilePicked$lambda$313$lambda$307(mainActivity, str, bytes, className);
                                            } catch (Exception _e) {
                                                if (_e instanceof RuntimeException) throw (RuntimeException) _e;
                                                throw new RuntimeException(_e);
                                            }
                                        }
                                    });
                                    return;
                                case 4:
                                    String lowName = str != null ? str.toLowerCase(java.util.Locale.ROOT) : "";
                                    if (lowName.endsWith(".md") || lowName.endsWith(".txt") || lowName.endsWith(".json") || lowName.endsWith(".lua") || lowName.endsWith(".luau")) {
                                        final String textBody = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
                                        mainActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                android.widget.ScrollView sv = new android.widget.ScrollView(mainActivity);
                                                android.widget.TextView tv = new android.widget.TextView(mainActivity);
                                                tv.setText(textBody);
                                                tv.setTextColor(Ui.INSTANCE.getTEXT());
                                                tv.setTextSize(12.5f);
                                                tv.setTypeface(android.graphics.Typeface.MONOSPACE);
                                                tv.setTextIsSelectable(true);
                                                int pad = Ui.INSTANCE.dp(mainActivity, 14.0f);
                                                tv.setPadding(pad, pad, pad, pad);
                                                sv.addView(tv);
                                                Dialogs.INSTANCE.themed(mainActivity)
                                                    .setTitle(str)
                                                    .setView(sv)
                                                    .setPositiveButton("Close", (DialogInterface.OnClickListener) null)
                                                    .show();
                                            }
                                        });
                                        return;
                                    }
                                    final RbxDocument rbxDocument2 = RbxFile.INSTANCE.read(bytes);
                                    final boolean z = !RbxFile.INSTANCE.isBinary(RbxFile.INSTANCE.unwrap(bytes));
                                    Iterator it = CollectionsKt.take(rbxDocument2.getWarnings(), 20).iterator();
                                    while (it.hasNext()) {
                                        OutputLog.INSTANCE.warn((String) it.next());
                                    }
                                    if (rbxDocument2.getWarnings().size() > 20) {
                                        OutputLog.INSTANCE.warn("…and " + (rbxDocument2.getWarnings().size() - 20) + " more warnings");
                                    }
                                    if (RbxFile.INSTANCE.isPlace(rbxDocument2)) {
                                        final DataModel dataModelFromDocument = PlaceFactory.INSTANCE.fromDocument(rbxDocument2);
                                        final int size = Instance.descendants$default(dataModelFromDocument, null, 1, null).size();
                                        mainActivity.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda6
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                MainActivity mainActivity2 = mainActivity;
                                                mainActivity2.confirmDiscard(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda45
                                                    @Override // kotlin.jvm.functions.Function0
                                                    public final Object invoke() {
                                                        return MainActivity.onFilePicked$lambda$313$lambda$310$lambda$309(mainActivity, dataModelFromDocument, uri, str, z, size);
                                                    }
                                                });
                                            }
                                        });
                                        return;
                                    }
                                    mainActivity.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda7
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            MainActivity.onFilePicked$lambda$313$lambda$311(mainActivity, rbxDocument2, str);
                                        }
                                    });
                                    return;
                                default:
                                    throw new NoWhenBranchMatchedException();
                            }
                        }
                    } catch (Throwable th) {
                        try {
                            throw th;
                        } catch (Throwable th2) {
                            CloseableKt.closeFinally(inputStream, th);
                            throw th2;
                        }
                    }
                }
                throw new Exception("Could not read file");
            } catch (Exception _e) {
                final Exception e = _e;
                OutputLog.INSTANCE.error("Failed to open " + str + ": " + e.getMessage());
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public final void run() {
                        Dialogs.INSTANCE.themed(mainActivity).setTitle("Couldn't open file").setMessage(str + "\n\n" + e.getMessage()).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
                    }
                });
            }
        } catch (Exception _e2) {
            final Exception e = _e2;
            OutputLog.INSTANCE.error("Failed to open " + str + ": " + e.getMessage());
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public final void run() {
                    Dialogs.INSTANCE.themed(mainActivity).setTitle("Couldn't open file").setMessage(str + "\n\n" + e.getMessage()).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
                }
            });
        }
    }

    static final void onFilePicked$lambda$313$lambda$306(MainActivity this$0, File $f) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.getPlugins().load($f);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio2.notifyPluginButtons();
        this$0.pluginsChanged();
        this$0.toast("Installed " + FilesKt.getNameWithoutExtension($f));
    }

    static final void onFilePicked$lambda$313$lambda$307(MainActivity this$0, String $name, byte[] $bytes, String $cls) {
        try {
        ToolboxPanel toolboxPanel = this$0.toolbox;
        DockLayout dockLayout = null;
        if (toolboxPanel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("toolbox");
            toolboxPanel = null;
        }
        toolboxPanel.saveToLibrary($name, RbxFile.INSTANCE.unwrap($bytes), $cls);
        DockLayout dockLayout2 = this$0.dock;
        if (dockLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dock");
        } else {
            dockLayout = dockLayout2;
        }
        dockLayout.setShown("toolbox", true);
        this$0.toast("Added " + $name + " to Toolbox");
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit onFilePicked$lambda$313$lambda$310$lambda$309(MainActivity this$0, DataModel $dm, Uri $uri, String $name, boolean $isXml, int $count) {
        try {
        this$0.closeAllScripts();
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.replaceEditModel$default(studio, $dm, $uri, $name, $isXml, null, 16, null);
        Recents.INSTANCE.add(this$0, new Recents.Entry($name, 0L, 0L, $uri.toString(), 0L, 0L, 54, null));
        OutputLog.INSTANCE.info("Opened " + $name + " (" + $count + " instances)");
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final void onFilePicked$lambda$313$lambda$311(MainActivity this$0, RbxDocument $doc, String $name) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        Studio.insertDocument$default(studio, $doc, null, 2, null);
        OutputLog.INSTANCE.info("Inserted " + $doc.getRoots().size() + " object(s) from " + $name);
    }

    private final void closeAllScripts() {
        for (Instance s : CollectionsKt.toList(this.editorOrder)) {
            closeScript(s);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void save$default(MainActivity mainActivity, Function0 function0, int i, Object obj) {
        if ((i & 1) != 0) {
            function0 = null;
        }
        mainActivity.save(function0);
    }

    private final void save(Function0<Unit> then) {
        Iterable iterableValues = this.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        if (studio.isPlaying()) {
            toast("Stop the playtest before saving");
            return;
        }
        Studio studio3 = this.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio3 = null;
        }
        if (studio3.getCloud() != null) {
            AccountUi accountUi = AccountUi.INSTANCE;
            MainActivity mainActivity = this;
            Studio studio4 = this.studio;
            if (studio4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio4;
            }
            accountUi.saveToRoblox(mainActivity, studio2, then);
            return;
        }
        Studio studio5 = this.studio;
        if (studio5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio5 = null;
        }
        Uri uri = studio5.getFileUri();
        if (uri != null) {
            Studio studio6 = this.studio;
            if (studio6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio6 = null;
            }
            if (!studio6.getFileIsXml()) {
                Studio studio7 = this.studio;
                if (studio7 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio7 = null;
                }
                String lowerCase = studio7.getFileName().toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                if (StringsKt.endsWith(lowerCase, ".rbxl", false)) {
                    this.pendingSave = new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda130
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return MainActivity.save$lambda$315(MainActivity.this);
                        }
                    };
                    this.pendingSaveIsPlace = true;
                    writeTo(uri);
                    return;
                }
            }
        }
        saveAs();
    }

    static final byte[] save$lambda$315(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        return studio.serializePlace();
    }

    private final void saveAs() {
        Iterable iterableValues = this.openEditors.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterator it = iterableValues.iterator();
        while (it.hasNext()) {
            ((ScriptEditor) it.next()).commit();
        }
        this.pendingSave = new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda132
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.saveAs$lambda$317(MainActivity.this);
            }
        };
        this.pendingSaveIsPlace = true;
        Studio studio = this.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        String base = com.goydevv.studrblx.util.KtCompat.substringBeforeLast(studio.getFileName(), '.');
        this.createLauncher.launch(base + ".rbxl");
    }

    static final byte[] saveAs$lambda$317(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        return studio.serializePlace();
    }

    @Override // com.goydevv.studrblx.ui.MainActions
    public void exportSelection() {
        boolean zIsEmpty;
        String n;
        Studio studio = this.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        synchronized (studio.getLock()) {
            Studio studio3 = this.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio3 = null;
            }
            zIsEmpty = studio3.getSelection().isEmpty();
        }
        if (zIsEmpty) {
            toast("Select something to export");
            return;
        }
        this.pendingSave = new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda221
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.exportSelection$lambda$319(MainActivity.this);
            }
        };
        this.pendingSaveIsPlace = false;
        Studio studio4 = this.studio;
        if (studio4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio4 = null;
        }
        synchronized (studio4.getLock()) {
            Studio studio5 = this.studio;
            if (studio5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio5;
            }
            n = ((Instance) CollectionsKt.first((List) studio2.getSelection())).getName();
        }
        this.createLauncher.launch(n + ".rbxm");
    }

    static final byte[] exportSelection$lambda$319(MainActivity this$0) {
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        return studio.serializeSelection();
    }

    @Override // com.goydevv.studrblx.ui.MainActions
    public void addSelectionToToolbox() {
        try {
            Pair pair;
            Studio studio = this.studio;
            DockLayout dockLayout = null;
            if (studio == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio = null;
            }
            byte[] bytes = studio.serializeSelection();
            if (bytes == null) {
                toast("Nothing selected");
                return;
            }
            Studio studio2 = this.studio;
            if (studio2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio2 = null;
            }
            synchronized (studio2.getLock()) {
                Studio studio3 = this.studio;
                if (studio3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio3 = null;
                }
                Instance instance = (Instance) CollectionsKt.first((List) studio3.getSelection());
                pair = TuplesKt.to(instance.getName(), instance.getClassName());
            }
            String n = (String) pair.component1();
            String cls = (String) pair.component2();
            ToolboxPanel toolboxPanel = this.toolbox;
            if (toolboxPanel == null) {
                Intrinsics.throwUninitializedPropertyAccessException("toolbox");
                toolboxPanel = null;
            }
            toolboxPanel.saveToLibrary(n, bytes, cls);
            DockLayout dockLayout2 = this.dock;
            if (dockLayout2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dock");
            } else {
                dockLayout = dockLayout2;
            }
            dockLayout.setShown("toolbox", true);
            toast("Saved " + n + " to Toolbox");
        } catch (Exception _e) {
            if (_e instanceof RuntimeException) throw (RuntimeException) _e;
            throw new RuntimeException(_e);
        }
    }

    @Override // com.goydevv.studrblx.ui.MainActions
    public void importToToolbox() {
        this.openMode = OpenMode.TOOLBOX;
        this.openLauncher.launch(new String[]{"*/*"});
    }

    @Override // com.goydevv.studrblx.ui.MainActions
    public void saveBytesAs(String fileName, Function0<byte[]> producer) {
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        Intrinsics.checkNotNullParameter(producer, "producer");
        this.pendingSave = producer;
        this.pendingSaveIsPlace = false;
        this.createLauncher.launch(fileName);
    }

    @Override // com.goydevv.studrblx.ui.MainActions
    public void openPlaceBytes(final String name, final byte[] bytes, final CloudPlace cloud) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        new Thread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda218
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.openPlaceBytes$lambda$329(bytes, MainActivity.this, name, cloud);
            }
        }).start();
    }

    static final void openPlaceBytes$lambda$329(byte[] $bytes, final MainActivity this$0, final String $name, final CloudPlace $cloud) {
        try {
            RbxDocument doc = RbxFile.INSTANCE.read($bytes);
            Iterator it = CollectionsKt.take(doc.getWarnings(), 10).iterator();
            while (it.hasNext()) {
                try {
                    OutputLog.INSTANCE.warn((String) it.next());
                } catch (final Exception e) {
                    OutputLog.INSTANCE.error("Failed to open " + $name + ": " + e.getMessage());
                    this$0.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda71
                        @Override // java.lang.Runnable
                        public final void run() {
                            Dialogs.INSTANCE.themed(this$0).setTitle("Couldn't open place").setMessage(e.getMessage()).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
                        }
                    });
                }
            }
            final DataModel dm = PlaceFactory.INSTANCE.fromDocument(doc);
            final int count = Instance.descendants$default(dm, null, 1, null).size();
            try {
                this$0.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda70
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity mainActivity = this$0;
                        mainActivity.confirmDiscard(new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda13
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return MainActivity.openPlaceBytes$lambda$329$lambda$327$lambda$326(this$0, dm, $name, $cloud, count);
                            }
                        });
                    }
                });
            } catch (Exception _e2) {
                final Exception e = _e2;
                OutputLog.INSTANCE.error("Failed to open " + $name + ": " + e.getMessage());
                this$0.runOnUiThread(new Runnable() {
                    @Override
                    public final void run() {
                        Dialogs.INSTANCE.themed(this$0).setTitle("Couldn't open place").setMessage(e.getMessage()).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
                    }
                });
            }
        } catch (Exception _e3) {
            final Exception e = _e3;
            OutputLog.INSTANCE.error("Failed to open " + $name + ": " + e.getMessage());
            this$0.runOnUiThread(new Runnable() {
                @Override
                public final void run() {
                    Dialogs.INSTANCE.themed(this$0).setTitle("Couldn't open place").setMessage(e.getMessage()).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
                }
            });
        }
    }

    static final Unit openPlaceBytes$lambda$329$lambda$327$lambda$326(MainActivity this$0, DataModel $dm, String $name, CloudPlace $cloud, int $count) {
        Studio studio;
        String name;
        this$0.closeAllScripts();
        Studio studio2 = this$0.studio;
        Studio studio3 = null;
        if (studio2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        } else {
            studio = studio2;
        }
        studio.replaceEditModel($dm, null, $name, false, $cloud);
        OutputLog outputLog = OutputLog.INSTANCE;
        if ($cloud == null || (name = $cloud.getName()) == null) {
            name = $name;
        }
        outputLog.info("Opened " + name + " from Roblox (" + $count + " instances)");
        if ($cloud != null) {
            AvatarUi avatarUi = AvatarUi.INSTANCE;
            MainActivity mainActivity = this$0;
            Studio studio4 = this$0.studio;
            if (studio4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio3 = studio4;
            }
            avatarUi.syncFromCloud(mainActivity, studio3, $cloud);
        }
        this$0.updateTitle();
        return Unit.INSTANCE;
    }

    @Override // com.goydevv.studrblx.ui.MainActions
    public void pickPluginFile() {
        this.openMode = OpenMode.PLUGIN;
        this.openLauncher.launch(new String[]{"*/*"});
    }

    @Override // com.goydevv.studrblx.ui.MainActions
    public void pluginsChanged() {
        this.ribbonTab = "Plugins";
        buildTabs();
        buildRibbon();
    }

    private final void writeTo(final Uri uri) {
        final Function0<byte[]> function0 = this.pendingSave;
        if (function0 == null) {
            return;
        }
        final boolean isPlace = this.pendingSaveIsPlace;
        this.pendingSave = null;
        final String name = displayName(uri);
        new Thread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda105
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.writeTo$lambda$333(function0, MainActivity.this, uri, isPlace, name);
            }
        }).start();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    static final void writeTo$lambda$333(Function0 function0, final MainActivity mainActivity, final Uri uri, final boolean z, final String str) {

        boolean r3 = false;
        OutputStream outputStreamOpenOutputStream;
        try {
            final byte[] bArr = (byte[]) function0.invoke();
            try {
                if (bArr == null) {
                    throw new Exception("Nothing to save");
                }
                try {
                    outputStreamOpenOutputStream = mainActivity.getContentResolver().openOutputStream(uri, "wt");
                } catch (Exception e) {
                    outputStreamOpenOutputStream = mainActivity.getContentResolver().openOutputStream(uri, "w");
                }
                OutputStream outputStream = outputStreamOpenOutputStream;
                if (outputStream == null) {
                    throw new Exception("Could not open file for writing");
                }
                OutputStream outputStream2 = outputStream;
                try {
                    r3 = false;
                    outputStream2.write(bArr);
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(outputStream2, null);
                    mainActivity.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda118
                        @Override // java.lang.Runnable
                        public final void run() {
                            try {
                                MainActivity.writeTo$lambda$333$lambda$331(z, mainActivity, uri, str, bArr);
                            } catch (Exception _e) {
                                if (_e instanceof RuntimeException) throw (RuntimeException) _e;
                                throw new RuntimeException(_e);
                            }
                        }
                    });
                } catch (Throwable th) {
                    r3 = z;
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        CloseableKt.closeFinally(outputStream2, th);
                        throw th2;
                    }
                }
            } catch (final Exception e2) {
                final boolean r2 = r3;
                OutputLog.INSTANCE.error("Save failed: " + e2.getMessage());
                mainActivity.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda119
                    @Override // java.lang.Runnable
                    public final void run() {
                        Dialogs.INSTANCE.themed(mainActivity).setTitle("Save failed").setMessage(e2.getMessage() + (r2 ? "\n\nTry File → Save As… to pick a new location." : "")).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
                    }
                });
            }
        } catch (final Exception e3) {
            final boolean r2 = z;
            OutputLog.INSTANCE.error("Save failed: " + e3.getMessage());
            mainActivity.runOnUiThread(new Runnable() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda119
                @Override // java.lang.Runnable
                public final void run() {
                    Dialogs.INSTANCE.themed(mainActivity).setTitle("Save failed").setMessage(e3.getMessage() + (r2 ? "\n\nTry File → Save As… to pick a new location." : "")).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
                }
            });
        }
    }

    static final void writeTo$lambda$333$lambda$331(boolean $isPlace, MainActivity this$0, Uri $uri, String $name, byte[] $bytes) {
        try {
        if ($isPlace) {
            try {
                this$0.getContentResolver().takePersistableUriPermission($uri, 3);
            } catch (Exception e) {
            }
            Studio studio = this$0.studio;
            Studio studio2 = null;
            if (studio == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio = null;
            }
            studio.markSaved($uri, $name);
            Studio studio3 = this$0.studio;
            if (studio3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio2 = studio3;
            }
            if (studio2.getCloud() == null) {
                Recents.INSTANCE.add(this$0, new Recents.Entry($name, 0L, 0L, $uri.toString(), 0L, 0L, 54, null));
            }
            this$0.rebuildScriptTabs();
        }
        this$0.toast("Saved " + $name);
        OutputLog.INSTANCE.info("Saved " + $name + " (" + ($bytes.length / 1024) + " KB)");
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX WARN: Type inference failed for: r2v8, types: [T, android.app.AlertDialog] */
    private final void managePlugins() {
        Studio studio = this.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        List<File> list = studio.getPlugins().list();
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(1);
        box.setPadding(dp(16.0f), dp(6.0f), dp(16.0f), 0);
        final Ref.ObjectRef dlg = new Ref.ObjectRef();
        if (list.isEmpty()) {
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, this, "No plugins installed.\n\nInstall one from the Creator Store (Toolbox → Plugins), from a Roblox asset ID or link (like an assetdelivery.roblox.com URL), or from a file — .rbxm, .rbxmx, a .bin downloaded from Roblox, or .lua.", 13.0f, 0, false, 24, null);
            textViewText$default.setSingleLine(false);
            box.addView(textViewText$default);
        }
        for (final File f : list) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(0);
            row.setGravity(16);
            row.setPadding(0, dp(4.0f), 0, dp(4.0f));
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(inst("Plugin"));
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, dp(20.0f), dp(20.0f), 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = dp(8.0f);
            Unit unit = Unit.INSTANCE;
            row.addView(imageView, layoutParamsLp$default);
            row.addView(Ui.text$default(Ui.INSTANCE, this, FilesKt.getNameWithoutExtension(f), 14.0f, 0, false, 24, null), Ui.INSTANCE.lp(0, -2, 1.0f));
            Switch r9 = new Switch(this);
            Studio studio2 = this.studio;
            if (studio2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio2 = null;
            }
            r9.setChecked(studio2.getPlugins().isEnabled(f));
            r9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda214
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    MainActivity.managePlugins$lambda$340$lambda$339(MainActivity.this, f, compoundButton, z);
                }
            });
            row.addView(r9);
            row.addView(Ui.iconButton$default(Ui.INSTANCE, this, g$default(this, "Delete", null, 2, null), 0.0f, new Function1() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda215
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return MainActivity.managePlugins$lambda$342(MainActivity.this, f, dlg, (View) obj);
                }
            }, 4, null));
            box.addView(row);
        }
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(this).setTitle("Plugins");
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(box);
        Unit unit2 = Unit.INSTANCE;
        dlg.element = title.setView(scrollView).setPositiveButton("From Roblox…", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda216
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.managePlugins$lambda$344(MainActivity.this, dialogInterface, i);
            }
        }).setNeutralButton("From file…", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda217
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.pickPluginFile();
            }
        }).setNegativeButton("Close", (DialogInterface.OnClickListener) null).show();
    }

    static final void managePlugins$lambda$340$lambda$339(MainActivity this$0, File $f, CompoundButton compoundButton, boolean on) {
        Intrinsics.checkNotNullParameter(compoundButton, "<unused var>");
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.getPlugins().setEnabled($f, on);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio2.getPlugins().loadAll();
    }

    static final Unit managePlugins$lambda$342(final MainActivity this$0, final File $f, final Ref.ObjectRef $dlg, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Dialogs.INSTANCE.confirm(this$0, "Remove plugin", "Uninstall " + FilesKt.getNameWithoutExtension($f) + "?", "Remove", new Function0() { // from class: com.goydevv.studrblx.MainActivity$$ExternalSyntheticLambda75
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return MainActivity.managePlugins$lambda$342$lambda$341(this$0, $f, $dlg);
            }
        });
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit managePlugins$lambda$342$lambda$341(MainActivity this$0, File $f, Ref.ObjectRef $dlg) {
        Studio studio = this$0.studio;
        Studio studio2 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        studio.getPlugins().remove($f);
        Studio studio3 = this$0.studio;
        if (studio3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
        } else {
            studio2 = studio3;
        }
        studio2.getPlugins().loadAll();
        this$0.buildRibbon();
        AlertDialog alertDialog = (AlertDialog) $dlg.element;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        this$0.managePlugins();
        return Unit.INSTANCE;
    }

    static final void managePlugins$lambda$344(MainActivity this$0, DialogInterface dialogInterface, int i) {
        AccountUi accountUi = AccountUi.INSTANCE;
        MainActivity mainActivity = this$0;
        Studio studio = this$0.studio;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        AccountUi.installPluginFromRoblox$default(accountUi, mainActivity, studio, this$0, null, 8, null);
    }

    private final void toast(String s) {
        Toast.makeText(this, s, 0).show();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.core.app.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        List<Instance> listBackpackTools;
        Instance instance;
        Intrinsics.checkNotNullParameter(event, "event");
        boolean z = event.isCtrlPressed() || event.isShiftPressed();
        Studio studio = this.studio;
        Studio studio2 = null;
        Studio studio3 = null;
        Studio studio4 = null;
        Studio studio5 = null;
        Studio studio6 = null;
        Studio studio7 = null;
        Studio studio8 = null;
        Studio studio9 = null;
        Studio studio10 = null;
        Studio studio11 = null;
        Studio studio12 = null;
        Studio studio13 = null;
        Studio studio14 = null;
        ViewportView viewportView = null;
        ViewportView viewportView2 = null;
        ViewportView viewportView3 = null;
        Studio studio15 = null;
        Studio studio16 = null;
        Studio studio17 = null;
        Studio studio18 = null;
        Studio studio19 = null;
        Studio studio20 = null;
        Studio studio21 = null;
        Studio studio22 = null;
        if (studio == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio = null;
        }
        if (z != studio.getModifierHeld()) {
            Studio studio23 = this.studio;
            if (studio23 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
                studio23 = null;
            }
            studio23.setModifierHeld(z);
            ViewportView viewportView4 = this.viewport;
            if (viewportView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("viewport");
                viewportView4 = null;
            }
            viewportView4.getControls().invalidate();
        }
        if (getCurrentFocus() instanceof EditText) {
            return super.dispatchKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        Studio studio24 = this.studio;
        if (studio24 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio24 = null;
        }
        boolean zIsPlaying = studio24.isPlaying();
        if (event.getAction() == 0) {
            this.keysDown.add(Integer.valueOf(keyCode));
            if (event.isCtrlPressed()) {
                switch (keyCode) {
                    case 8:
                        Studio studio25 = this.studio;
                        if (studio25 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio13 = studio25;
                        }
                        studio13.setTool(Tool.SELECT);
                        return true;
                    case 9:
                        Studio studio26 = this.studio;
                        if (studio26 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio12 = studio26;
                        }
                        studio12.setTool(Tool.MOVE);
                        return true;
                    case 10:
                        Studio studio27 = this.studio;
                        if (studio27 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio11 = studio27;
                        }
                        studio11.setTool(Tool.SCALE);
                        return true;
                    case 11:
                        Studio studio28 = this.studio;
                        if (studio28 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio10 = studio28;
                        }
                        studio10.setTool(Tool.ROTATE);
                        return true;
                    case 31:
                        Studio studio29 = this.studio;
                        if (studio29 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio9 = studio29;
                        }
                        studio9.copySelection();
                        return true;
                    case 32:
                        Studio studio30 = this.studio;
                        if (studio30 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio8 = studio30;
                        }
                        studio8.duplicateSelection();
                        return true;
                    case 35:
                        if (event.isShiftPressed()) {
                            Studio studio31 = this.studio;
                            if (studio31 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("studio");
                            } else {
                                studio6 = studio31;
                            }
                            studio6.ungroupSelection();
                        } else {
                            Studio studio32 = this.studio;
                            if (studio32 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("studio");
                            } else {
                                studio7 = studio32;
                            }
                            studio7.groupSelection();
                        }
                        return true;
                    case 43:
                        this.openMode = OpenMode.PLACE;
                        this.openLauncher.launch(new String[]{"*/*"});
                        return true;
                    case 47:
                        if (event.isShiftPressed()) {
                            saveAs();
                        } else {
                            save$default(this, null, 1, null);
                        }
                        return true;
                    case 50:
                        Studio studio33 = this.studio;
                        if (studio33 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                            studio33 = null;
                        }
                        studio33.paste(null);
                        return true;
                    case 52:
                        Studio studio34 = this.studio;
                        if (studio34 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio5 = studio34;
                        }
                        studio5.cutSelection();
                        return true;
                    case 53:
                        Studio studio35 = this.studio;
                        if (studio35 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio4 = studio35;
                        }
                        studio4.getUndo().redo();
                        return true;
                    case 54:
                        Studio studio36 = this.studio;
                        if (studio36 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio3 = studio36;
                        }
                        studio3.getUndo().undo();
                        return true;
                }
            }
            if (zIsPlaying && event.getRepeatCount() == 0) {
                switch (keyCode) {
                    case 37:
                        Studio studio37 = this.studio;
                        if (studio37 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                            studio37 = null;
                        }
                        synchronized (studio37.getLock()) {
                            Studio studio38 = this.studio;
                            if (studio38 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("studio");
                            } else {
                                studio17 = studio38;
                            }
                            PlaySession play = studio17.getPlay();
                            if (play != null) {
                                play.cameraZoom(1.25f);
                                Unit unit = Unit.INSTANCE;
                            }                        }
                        return true;
                    case 43:
                        Studio studio39 = this.studio;
                        if (studio39 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                            studio39 = null;
                        }
                        synchronized (studio39.getLock()) {
                            Studio studio40 = this.studio;
                            if (studio40 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("studio");
                            } else {
                                studio16 = studio40;
                            }
                            PlaySession play2 = studio16.getPlay();
                            if (play2 != null) {
                                play2.cameraZoom(0.8f);
                                Unit unit2 = Unit.INSTANCE;
                            }                        }
                        return true;
                    case 59:
                        Studio studio41 = this.studio;
                        if (studio41 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                            studio41 = null;
                        }
                        synchronized (studio41.getLock()) {
                            Studio studio42 = this.studio;
                            if (studio42 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("studio");
                            } else {
                                studio15 = studio42;
                            }
                            PlaySession play3 = studio15.getPlay();
                            if (play3 != null) {
                                play3.toggleShiftLock();
                                Unit unit3 = Unit.INSTANCE;
                            }                        }
                        return true;
                    case 61:
                        ViewportView viewportView5 = this.viewport;
                        if (viewportView5 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("viewport");
                        } else {
                            viewportView3 = viewportView5;
                        }
                        viewportView3.getHud().togglePlayerList();
                        return true;
                    case 76:
                        ViewportView viewportView6 = this.viewport;
                        if (viewportView6 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("viewport");
                        } else {
                            viewportView2 = viewportView6;
                        }
                        viewportView2.getHud().focusChat();
                        return true;
                    case 111:
                        ViewportView viewportView7 = this.viewport;
                        if (viewportView7 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("viewport");
                        } else {
                            viewportView = viewportView7;
                        }
                        viewportView.getHud().toggleMenu();
                        return true;
                    default:
                        if ((8 <= keyCode && keyCode < 17) != false) {
                            Studio studio43 = this.studio;
                            if (studio43 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("studio");
                                studio43 = null;
                            }
                            synchronized (studio43.getLock()) {
                                Studio studio44 = this.studio;
                                if (studio44 == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                                } else {
                                    studio14 = studio44;
                                }
                                PlaySession play4 = studio14.getPlay();
                                if (play4 != null && (listBackpackTools = play4.backpackTools()) != null && (instance = (Instance) CollectionsKt.getOrNull(listBackpackTools, keyCode - 8)) != null) {
                                    play4.toggleTool(instance);
                                    Unit unit4 = Unit.INSTANCE;
                                }                            }
                            return true;
                        }
                        break;
                }
            }
            switch (keyCode) {
                case 34:
                    if (!zIsPlaying) {
                        Studio studio45 = this.studio;
                        if (studio45 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio21 = studio45;
                        }
                        studio21.focusSelection();
                        return true;
                    }
                    break;
                case 62:
                    if (zIsPlaying) {
                        Studio studio46 = this.studio;
                        if (studio46 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                            studio46 = null;
                        }
                        synchronized (studio46.getLock()) {
                            Studio studio47 = this.studio;
                            if (studio47 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("studio");
                            } else {
                                studio20 = studio47;
                            }
                            PlaySession play5 = studio20.getPlay();
                            if (play5 != null) {
                                play5.setJumpHeld(true);
                                Unit unit5 = Unit.INSTANCE;
                            }                        }
                        return true;
                    }
                    break;
                case 67:
                case 112:
                    if (!zIsPlaying) {
                        Studio studio48 = this.studio;
                        if (studio48 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio19 = studio48;
                        }
                        studio19.deleteSelection();
                        return true;
                    }
                    break;
                case 135:
                    if (event.isShiftPressed()) {
                        Studio studio49 = this.studio;
                        if (studio49 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("studio");
                        } else {
                            studio18 = studio49;
                        }
                        studio18.stopPlay();
                    } else if (!zIsPlaying) {
                        togglePlay();
                    }
                    return true;
            }
        } else if (event.getAction() == 1) {
            this.keysDown.remove(Integer.valueOf(keyCode));
            if (keyCode == 62 && zIsPlaying) {
                Studio studio50 = this.studio;
                if (studio50 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("studio");
                    studio50 = null;
                }
                synchronized (studio50.getLock()) {
                    Studio studio51 = this.studio;
                    if (studio51 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("studio");
                    } else {
                        studio2 = studio51;
                    }
                    PlaySession play6 = studio2.getPlay();
                    if (play6 != null) {
                        play6.setJumpHeld(false);
                        Unit unit6 = Unit.INSTANCE;
                    }
                }
                return true;
            }
        }
        if (zIsPlaying && (keyCode == 21 || keyCode == 22)) {
            Studio studio52 = this.studio;
            if (studio52 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("studio");
            } else {
                studio22 = studio52;
            }
            PlaySession play7 = studio22.getPlay();
            if (play7 != null) {
                play7.setKeyTurn((this.keysDown.contains(21) ? 1.0f : 0.0f) - (this.keysDown.contains(22) ? 1.0f : 0.0f));
            }
            return true;
        }
        if (!SetsKt.setOf(new Integer[]{51, 29, 47, 32, 45, 33, 19, 20}).contains(Integer.valueOf(keyCode)) || event.isCtrlPressed()) {
            return super.dispatchKeyEvent(event);
        }
        Studio studio53 = this.studio;
        if (studio53 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio53 = null;
        }
        studio53.setMoveX(dispatchKeyEvent$k(this, 32) - dispatchKeyEvent$k(this, 29));
        Studio studio54 = this.studio;
        if (studio54 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio54 = null;
        }
        studio54.setMoveY(RangesKt.coerceAtMost(dispatchKeyEvent$k(this, 47) + dispatchKeyEvent$k(this, 20), 1.0f) - RangesKt.coerceAtMost(dispatchKeyEvent$k(this, 51) + dispatchKeyEvent$k(this, 19), 1.0f));
        Studio studio55 = this.studio;
        if (studio55 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio55 = null;
        }
        studio55.setFlyUp(this.keysDown.contains(33));
        Studio studio56 = this.studio;
        if (studio56 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("studio");
            studio56 = null;
        }
        studio56.setFlyDown(this.keysDown.contains(45));
        return true;
    }

    private static final float dispatchKeyEvent$k(MainActivity this$0, int c) {
        return this$0.keysDown.contains(Integer.valueOf(c)) ? 1.0f : 0.0f;
    }
}
