package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.goydevv.studrblx.model.ClassDb;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import kotlin.Deprecated;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Icons.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class Icons {
    private static File dir;
    private static File studioDir;
    private static volatile boolean studioFetching;
    private static final String STUDIO_ICON_BASE = "https://raw.githubusercontent.com/MaximumADHD/Roblox-Client-Tracker/roblox/studio-svg-textures/Shared/InsertableObjects/";
    public static final Icons INSTANCE = new Icons();
    private static final HashMap<String, Bitmap> cache = new HashMap<>();
    private static final CopyOnWriteArrayList<Function0<Unit>> listeners = new CopyOnWriteArrayList<>();
    private static final Set<String> knownClassIcons = SetsKt.setOf(new String[]{"Script", "LocalScript", "ModuleScript", "Model", "Folder", "Plugin", "Humanoid", "Sound", "Decal"});

    private Icons() {
    }

    public final CopyOnWriteArrayList<Function0<Unit>> getListeners() {
        return listeners;
    }

    public final void init(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        if (dir == null) {
            dir = new File(ctx.getFilesDir(), "iconpack");
        }
        if (studioDir == null) {
            studioDir = new File(ctx.getFilesDir(), "studioicons");
        }
        fetchStudioIcons(ctx.getApplicationContext(), false);
    }

    public final boolean getStudioIconsInstalled() {
        File d = studioDir;
        return d != null && new File(d, ".complete").isFile();
    }

    public final void fetchStudioIcons(final Context ctx, boolean force) {
        final File d = studioDir;
        if (d == null || studioFetching) {
            return;
        }
        if (!force && new File(d, ".complete").isFile()) {
            return;
        }
        studioFetching = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadStudioIcons(ctx, d);
                } catch (Throwable e) {
                    com.goydevv.studrblx.core.OutputLog.INSTANCE.warn("Studio icons: " + e.getMessage());
                } finally {
                    studioFetching = false;
                }
            }
        }, "studio-icons");
        t.setDaemon(true);
        t.start();
    }

    private static void downloadStudioIcons(Context ctx, File d) throws IOException {
        List<String> names = new ArrayList<>();
        try (InputStream in = ctx.getAssets().open("studio_icons.txt")) {
            for (String line : new String(ByteStreamsKt.readBytes(in), "UTF-8").split("\n")) {
                String n = line.trim();
                if (n.matches("[A-Za-z0-9_]+")) {
                    names.add(n);
                }
            }
        }
        int ok = 0;
        int failed = 0;
        for (String theme : new String[]{"Dark", "Light"}) {
            File out = new File(d, theme.toLowerCase(Locale.ROOT) + "/instance");
            out.mkdirs();
            for (String n : names) {
                File f = new File(out, n + ".png");
                if (f.isFile() && f.length() > 0) {
                    ok++;
                    continue;
                }
                if (fetchPng(STUDIO_ICON_BASE + theme + "/Standard/" + n + "@2x.png", f)) {
                    ok++;
                } else {
                    failed++;
                }
            }
        }
        synchronized (cache) {
            cache.clear();
        }
        if (failed == 0) {
            new File(d, ".complete").createNewFile();
        } else {
            com.goydevv.studrblx.core.OutputLog.INSTANCE.warn("Studio icons: " + failed + " of " + (ok + failed) + " icons could not be downloaded");
        }
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            ((Function0) it.next()).invoke();
        }
    }

    private static boolean fetchPng(String url, File dest) {
        java.net.HttpURLConnection c = null;
        try {
            c = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            c.setConnectTimeout(10000);
            c.setReadTimeout(15000);
            if (c.getResponseCode() != 200) {
                return false;
            }
            byte[] data;
            try (InputStream in = c.getInputStream()) {
                data = ByteStreamsKt.readBytes(in);
            }
            if (data.length < 8 || (data[0] & 0xFF) != 0x89 || data[1] != 'P' || data[2] != 'N' || data[3] != 'G') {
                return false;
            }
            File tmp = new File(dest.getPath() + ".part");
            try (FileOutputStream o = new FileOutputStream(tmp)) {
                o.write(data);
            }
            return tmp.renameTo(dest);
        } catch (Exception e) {
            return false;
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
    }

    public final boolean getInstalled() {
        File file = dir;
        if (file != null) {
            return new File(file, "general").isDirectory() || new File(file, "instance").isDirectory();
        }
        return false;
    }

    private final Bitmap load(String kind, String name) {
        boolean light = Ui.INSTANCE.getLIGHT();
        String key = (light ? "light/" : "") + kind + "/" + name;
        synchronized (cache) {
            if (cache.containsKey(key)) {
                return cache.get(key);
            }
            File file = dir;
            Bitmap bitmapDecodeFile = null;
            File file2 = file != null ? new File(file, key + ".png") : null;
            if (file2 != null && file2.isFile()) {
                try {
                    bitmapDecodeFile = BitmapFactory.decodeFile(file2.getPath());
                } catch (Throwable th) {
                }
            }
            if (bitmapDecodeFile == null && Intrinsics.areEqual(kind, "instance") && studioDir != null) {
                File sf = new File(studioDir, (light ? "light" : "dark") + "/instance/" + name + ".png");
                if (sf.isFile()) {
                    try {
                        bitmapDecodeFile = BitmapFactory.decodeFile(sf.getPath());
                    } catch (Throwable th) {
                    }
                }
            }
            cache.put(key, bitmapDecodeFile);
            return bitmapDecodeFile;
        }
    }

    public final Bitmap instanceBitmap(String cls) {
        Intrinsics.checkNotNullParameter(cls, "cls");
        String c = cls;
        int guard = 0;
        while (c != null) {
            int guard2 = guard + 1;
            if (guard < 12) {
                Bitmap bitmapLoad = load("instance", c);
                if (bitmapLoad != null) {
                    return bitmapLoad;
                }
                if (!Intrinsics.areEqual(c, "Instance")) {
                    c = ClassDb.INSTANCE.superclass(c);
                    guard = guard2;
                }
            }
            return null;
        }
        return null;
    }

    public final Bitmap generalBitmap(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        return load("general", name);
    }

    public final Bitmap uiBitmap(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        return load("ui", name);
    }

    public final Drawable instance(Context ctx, String cls) {
        BitmapDrawable bitmapDrawablePixelated;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(cls, "cls");
        Bitmap bitmapInstanceBitmap = instanceBitmap(cls);
        if (bitmapInstanceBitmap != null && (bitmapDrawablePixelated = INSTANCE.pixelated(ctx, bitmapInstanceBitmap)) != null) {
            return bitmapDrawablePixelated;
        }
        Drawable drawableInstance = BuiltinIcons.INSTANCE.instance(cls);
        return drawableInstance == null ? new Badge(abbrev(cls), categoryColor(cls), false, 4, null) : drawableInstance;
    }

    public static /* synthetic */ Drawable general$default(Icons icons, Context context, String str, String str2, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = str;
        }
        return icons.general(context, str, str2);
    }

    public final Drawable general(Context ctx, String name, String fallback) {
        BitmapDrawable bitmapDrawablePixelated;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(fallback, "fallback");
        Bitmap bitmapGeneralBitmap = generalBitmap(name);
        if (bitmapGeneralBitmap != null && (bitmapDrawablePixelated = INSTANCE.pixelated(ctx, bitmapGeneralBitmap)) != null) {
            return bitmapDrawablePixelated;
        }
        Drawable drawableGeneral = BuiltinIcons.INSTANCE.general(name);
        if (drawableGeneral != null) {
            return drawableGeneral;
        }
        Drawable drawableInstance = (ClassDb.INSTANCE.isServiceName(name) || knownClassIcons.contains(name)) ? BuiltinIcons.INSTANCE.instance(name) : null;
        return drawableInstance == null ? new Badge(abbrev(fallback), -10851459, false, 4, null) : drawableInstance;
    }

    public final Drawable ui(Context ctx, String name, String fallback) {
        Drawable badge;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(fallback, "fallback");
        Bitmap bitmapUiBitmap = uiBitmap(name);
        if (bitmapUiBitmap == null || (badge = INSTANCE.pixelated(ctx, bitmapUiBitmap)) == null) {
            Drawable drawableUi = BuiltinIcons.INSTANCE.ui(name);
            if (drawableUi != null) {
                return drawableUi;
            }
            badge = new Badge(fallback, 0, true);
        }
        return badge;
    }

    private final BitmapDrawable pixelated(Context ctx, Bitmap b) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(ctx.getResources(), b);
        bitmapDrawable.setFilterBitmap(true);
        return bitmapDrawable;
    }

    private final String abbrev(String s) {
        try {
        String str = s;
        Appendable sb = new StringBuilder();
        int i = 0;
        int length = str.length();
        while (true) {
            if (i >= length) {
                break;
            }
            char cCharAt = str.charAt(i);
            if (Character.isUpperCase(cCharAt) || Character.isDigit(cCharAt)) {
                sb.append(cCharAt);
            }
            i++;
        }
        String string = ((StringBuilder) sb).toString();
        if (string.length() >= 2) {
            return StringsKt.take(string, 2);
        }
        if (!(s.length() > 0)) {
            return "?";
        }
        String strTake = StringsKt.take(s, 2);
        if (!(strTake.length() > 0)) {
            return strTake;
        }
        StringBuilder sb2 = new StringBuilder();
        String strValueOf = String.valueOf(strTake.charAt(0));
        Intrinsics.checkNotNull(strValueOf, "null cannot be cast to non-null type java.lang.String");
        String upperCase = strValueOf.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        StringBuilder sbAppend = sb2.append((Object) upperCase);
        String strSubstring = strTake.substring(1);
        Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return sbAppend.append(strSubstring).toString();
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final int categoryColor(String cls) {
        if (ClassDb.INSTANCE.isA(cls, "LuaSourceContainer")) {
            return -12944427;
        }
        if (ClassDb.INSTANCE.isA(cls, "BasePart")) {
            return -7566196;
        }
        if (ClassDb.INSTANCE.isA(cls, "GuiBase2d") || ClassDb.INSTANCE.isA(cls, "UIBase")) {
            return IconPainter.GUI;
        }
        if (Intrinsics.areEqual(cls, "Model") || Intrinsics.areEqual(cls, "Folder")) {
            return -3562969;
        }
        if (ClassDb.INSTANCE.isA(cls, "ValueBase")) {
            return -7443257;
        }
        return ClassDb.INSTANCE.isServiceName(cls) ? -11637624 : -9737365;
    }

    /* JADX INFO: compiled from: Icons.kt */
    public static final class Badge extends Drawable {
        private final Paint bg;
        private final int color;
        private final String label;
        private final boolean plain;
        private final Paint tp;

        public Badge(String label, int color, boolean plain) {
            Intrinsics.checkNotNullParameter(label, "label");
            this.label = label;
            this.color = color;
            this.plain = plain;
            this.bg = new Paint(1);
            Paint paint = new Paint(1);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            this.tp = paint;
        }

        public /* synthetic */ Badge(String str, int i, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, i, (i2 & 4) != 0 ? false : z);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas c) {
            Intrinsics.checkNotNullParameter(c, "c");
            Rect b = getBounds();
            Intrinsics.checkNotNullExpressionValue(b, "getBounds(...)");
            RectF r = new RectF(b);
            float s = Math.min(r.width(), r.height());
            r.inset(((r.width() - s) / 2.0f) + (s * 0.06f), ((r.height() - s) / 2.0f) + (0.06f * s));
            if (!this.plain) {
                this.bg.setColor(this.color);
                c.drawRoundRect(r, s * 0.2f, 0.2f * s, this.bg);
            }
            this.tp.setColor(this.plain ? Ui.INSTANCE.getTEXT() : -1);
            this.tp.setTextSize((this.label.length() > 1 ? 0.42f : 0.6f) * s);
            c.drawText(this.label, r.centerX(), r.centerY() - ((this.tp.descent() + this.tp.ascent()) / 2.0f), this.tp);
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
            this.bg.setAlpha(alpha);
            this.tp.setAlpha(alpha);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter cf) {
        }

        @Override // android.graphics.drawable.Drawable
        @Deprecated(message = "Deprecated in Java")
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return 32;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return 32;
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public final int importZip(Context ctx, InputStream input) throws IOException {
        Throwable th;
        List list;
        Object next;
        int count;
        Throwable th2;
        Throwable th3;
        Map map;
        Regex loose;
        Object next2;
        Object obj;
        Collection collection;
        Iterator it;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(input, "input");
        init(ctx);
        File tmp = new File(ctx.getCacheDir(), "iconpack_import.zip");
        FileOutputStream fileOutputStream = new FileOutputStream(tmp);
        try {
            int i = 2;
            int i2 = 0;
            Object obj2 = null;
            ByteStreamsKt.copyTo(input, fileOutputStream, 8192);
            CloseableKt.closeFinally(fileOutputStream, null);
            Regex rx = new Regex("(?:^|/)(?:([^/]+)/)?(Dark|Light)/RobloxCustom/(instance|general|ui)/(16x|32x)/200/([^/]+)\\.png$", RegexOption.IGNORE_CASE);
            Regex loose2 = new Regex("(?:^|/)(instance|general|ui)/(16x|32x)/200/([^/]+)\\.png$", RegexOption.IGNORE_CASE);
            int count2 = 0;
            ZipFile zipFile = new ZipFile(tmp);
            try {
                ZipFile zipFile2 = zipFile;
                ArrayList arrayList = new ArrayList();
                Enumeration<? extends ZipEntry> enumerationEntries = zipFile2.entries();
                Intrinsics.checkNotNullExpressionValue(enumerationEntries, "entries(...)");
                Iterator it2 = CollectionsKt.iterator(enumerationEntries);
                while (it2.hasNext()) {
                    try {
                        final ZipEntry zipEntry = (ZipEntry) it2.next();
                        if (zipEntry.isDirectory()) {
                            i = 2;
                            i2 = 0;
                            obj2 = null;
                        } else {
                            String name = zipEntry.getName();
                            Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                            String strReplace$default = StringsKt.replace(name, '\\', '/', false);
                            MatchResult matchResultFind$default = (rx).find(strReplace$default, (((i) & 2) != 0 ? 0 : i2));
                            if (matchResultFind$default != null) {
                                final String str = matchResultFind$default.getGroupValues().get(1);
                                final String str2 = matchResultFind$default.getGroupValues().get(i);
                                final String lowerCase = matchResultFind$default.getGroupValues().get(3).toLowerCase(Locale.ROOT);
                                Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                                final String lowerCase2 = matchResultFind$default.getGroupValues().get(4).toLowerCase(Locale.ROOT);
                                Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                                it = it2;
                                final String str3 = matchResultFind$default.getGroupValues().get(5);
                                Intrinsics.checkNotNull(zipEntry);
                                Boolean.valueOf(arrayList.add(new ImportZipEntry(str, str2, lowerCase, lowerCase2, str3, zipEntry)));
                            } else {
                                it = it2;
                                MatchResult matchResultFind$default2 = (loose2).find(strReplace$default, 0);
                                if (matchResultFind$default2 != null) {
                                    final String str4 = "";
                                    final String str5 = "Dark";
                                    final String lowerCase3 = matchResultFind$default2.getGroupValues().get(1).toLowerCase(Locale.ROOT);
                                    Intrinsics.checkNotNullExpressionValue(lowerCase3, "toLowerCase(...)");
                                    final String lowerCase4 = matchResultFind$default2.getGroupValues().get(2).toLowerCase(Locale.ROOT);
                                    Intrinsics.checkNotNullExpressionValue(lowerCase4, "toLowerCase(...)");
                                    final String str6 = matchResultFind$default2.getGroupValues().get(3);
                                    Intrinsics.checkNotNull(zipEntry);
                                    Boolean.valueOf(arrayList.add(new ImportZipEntry(str4, str5, lowerCase3, lowerCase4, str6, zipEntry)));
                                }
                            }
                            it2 = it;
                            i = 2;
                            i2 = 0;
                            obj2 = null;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                    }
                }
                try {
                    if (arrayList.isEmpty()) {
                        throw new IllegalArgumentException("No Vanilla icons found in this zip. Pick the \"Vanilla 3 for Roblox Studio\" zip from itch.io.");
                    }
                    ArrayList arrayList2 = arrayList;
                    Collection arrayList3 = new ArrayList();
                    for (Object obj3 : arrayList2) {
                        Iterable iterable = arrayList2;
                        if (StringsKt.equals(((ImportZipEntry) obj3).getTheme(), "Dark", true)) {
                            arrayList3.add(obj3);
                        }
                        arrayList2 = (ArrayList) iterable;
                    }
                    List list2 = (List) arrayList3;
                    if (list2.isEmpty()) {
                        list2 = arrayList;
                    }
                    List list3 = list2;
                    List list4 = list3;
                    Collection arrayList4 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list4, 10));
                    Iterator it3 = list4.iterator();
                    while (it3.hasNext()) {
                        Iterable iterable2 = list4;
                        arrayList4.add(((ImportZipEntry) it3.next()).getPalette());
                        list4 = (List) iterable2;
                    }
                    List listDistinct = CollectionsKt.distinct((List) arrayList4);
                    List list5 = listDistinct;
                    Iterator it4 = list5.iterator();
                    while (true) {
                        if (!it4.hasNext()) {
                            list = list3;
                            next = null;
                            break;
                        }
                        next = it4.next();
                        Iterable iterable3 = list5;
                        list = list3;
                        if (StringsKt.equals((String) next, "Colourful", true)) {
                            break;
                        }
                        list5 = (List) iterable3;
                        list3 = list;
                    }
                    String str7 = (String) next;
                    if (str7 == null) {
                        str7 = (String) CollectionsKt.first(listDistinct);
                    }
                    File file = new File(ctx.getFilesDir(), "iconpack.new");
                    FilesKt.deleteRecursively(file);
                    Pair[] pairArr = new Pair[2];
                    Collection arrayList5 = new ArrayList();
                    for (Object obj4 : list) {
                        List list6 = listDistinct;
                        if (Intrinsics.areEqual(((ImportZipEntry) obj4).getPalette(), str7)) {
                            arrayList5.add(obj4);
                        }
                        listDistinct = list6;
                    }
                    pairArr[0] = TuplesKt.to("", (List) arrayList5);
                    ArrayList arrayList6 = arrayList;
                    Collection arrayList7 = new ArrayList();
                    for (Object obj5 : arrayList6) {
                        try {
                            ImportZipEntry icons$importZip$2$E = (ImportZipEntry) obj5;
                            Iterable iterable4 = arrayList6;
                            File tmp2 = tmp;
                            Regex rx2 = rx;
                            try {
                                if (StringsKt.equals(icons$importZip$2$E.getTheme(), "Light", true) && Intrinsics.areEqual(icons$importZip$2$E.getPalette(), str7)) {
                                    arrayList7.add(obj5);
                                }
                                arrayList6 = (ArrayList) iterable4;
                                tmp = tmp2;
                                rx = rx2;
                            } catch (Throwable th5) {
                                th = th5;
                            }
                        } catch (Throwable th6) {
                            th = th6;
                        }
                    }
                    File tmp3 = tmp;
                    try {
                        pairArr[1] = TuplesKt.to("light/", (List) arrayList7);
                        List listListOf = CollectionsKt.listOf(pairArr);
                        Iterator it5 = listListOf.iterator();
                        while (it5.hasNext()) {
                            Pair pair = (Pair) it5.next();
                            String str8 = (String) pair.component1();
                            List list7 = (List) pair.component2();
                            for (String str9 : CollectionsKt.listOf(new String[]{"instance", "general", "ui"})) {
                                Collection arrayList8 = new ArrayList();
                                for (Object obj6 : list7) {
                                    if (Intrinsics.areEqual(((ImportZipEntry) obj6).getKind(), str9)) {
                                        collection = arrayList8;
                                        collection.add(obj6);
                                    } else {
                                        collection = arrayList8;
                                    }
                                    arrayList8 = (ArrayList) collection;
                                }
                                Collection collection2 = arrayList8;
                                List list8 = (List) collection2;
                                if (list8.isEmpty()) {
                                } else {
                                    List list9 = list8;
                                    Map linkedHashMap = new LinkedHashMap();
                                    for (Object obj7 : list9) {
                                        String name2 = ((ImportZipEntry) obj7).getName();
                                        Iterable iterable5 = list9;
                                        List list10 = listListOf;
                                        Object obj8 = linkedHashMap.get(name2);
                                        if (obj8 == null) {
                                            ArrayList arrayList9 = new ArrayList();
                                            linkedHashMap.put(name2, arrayList9);
                                            obj = arrayList9;
                                        } else {
                                            obj = obj8;
                                        }
                                        ((List) obj).add(obj7);
                                        listListOf = list10;
                                        list9 = (List) iterable5;
                                    }
                                    List list11 = listListOf;
                                    List list12 = list7;
                                    int i3 = 0;
                                    Map linkedHashMap2 = new LinkedHashMap(MapsKt.mapCapacity(linkedHashMap.size()));
                                    Map map2 = linkedHashMap;
                                    for (Object obj9 : map2.entrySet()) {
                                        int i4 = i3;
                                        Object key = ((Map.Entry) obj9).getKey();
                                        List list13 = (List) ((Map.Entry) obj9).getValue();
                                        Iterator it6 = list13.iterator();
                                        while (true) {
                                            if (!it6.hasNext()) {
                                                map = map2;
                                                loose = loose2;
                                                next2 = null;
                                                break;
                                            }
                                            next2 = it6.next();
                                            map = map2;
                                            loose = loose2;
                                            if (Intrinsics.areEqual(((ImportZipEntry) next2).getRes(), "32x")) {
                                                break;
                                            }
                                            map2 = map;
                                            loose2 = loose;
                                        }
                                        ImportZipEntry icons$importZip$2$E2 = (ImportZipEntry) next2;
                                        if (icons$importZip$2$E2 == null) {
                                            icons$importZip$2$E2 = (ImportZipEntry) CollectionsKt.first(list13);
                                        }
                                        linkedHashMap2.put(key, icons$importZip$2$E2);
                                        i3 = i4;
                                        map2 = map;
                                        loose2 = loose;
                                    }
                                    Regex loose3 = loose2;
                                    File file2 = new File(file, str8 + str9);
                                    file2.mkdirs();
                                    File file3 = file2;
                                    Iterator it7 = linkedHashMap2.entrySet().iterator();
                                    while (it7.hasNext()) {
                                        try {
                                            Map.Entry entry = (Map.Entry) it7.next();
                                            String str10 = (String) entry.getKey();
                                            InputStream inputStream = zipFile2.getInputStream(((ImportZipEntry) entry.getValue()).getEntry());
                                            try {
                                                InputStream inputStream2 = inputStream;
                                                try {
                                                    try {
                                                        count = count2;
                                                        try {
                                                            FileOutputStream fileOutputStream2 = new FileOutputStream(new File(file3, str10 + ".png"));
                                                            try {
                                                                Intrinsics.checkNotNull(inputStream2);
                                                                try {
                                                                    ByteStreamsKt.copyTo(inputStream2, fileOutputStream2, 8192);
                                                                    try {
                                                                        CloseableKt.closeFinally(fileOutputStream2, null);
                                                                        try {
                                                                            CloseableKt.closeFinally(inputStream, null);
                                                                            count2 = str8.length() == 0 ? count + 1 : count;
                                                                        } catch (Throwable th7) {
                                                                            th = th7;
                                                                        }
                                                                    } catch (Throwable th8) {
                                                                        th2 = th8;
                                                                        try {
                                                                            throw th2;
                                                                        } catch (Throwable th9) {
                                                                            CloseableKt.closeFinally(inputStream, th2);
                                                                            throw th9;
                                                                        }
                                                                    }
                                                                } catch (Throwable th10) {
                                                                    th3 = th10;
                                                                    try {
                                                                        throw th3;
                                                                    } catch (Throwable th11) {
                                                                        CloseableKt.closeFinally(fileOutputStream2, th3);
                                                                        throw th11;
                                                                    }
                                                                }
                                                            } catch (Throwable th12) {
                                                                th3 = th12;
                                                            }
                                                        } catch (Throwable th13) {
                                                            th2 = th13;
                                                        }
                                                    } catch (Throwable th14) {
                                                        count = count2;
                                                        th2 = th14;
                                                    }
                                                } catch (Throwable th15) {
                                                    count = count2;
                                                    th2 = th15;
                                                }
                                            } catch (Throwable th16) {
                                                count = count2;
                                                th2 = th16;
                                            }
                                        } catch (Throwable th17) {
                                            th = th17;
                                        }
                                    }
                                    listListOf = list11;
                                    list7 = list12;
                                    loose2 = loose3;
                                }
                            }
                        }
                        File file4 = new File(ctx.getFilesDir(), "iconpack");
                        FilesKt.deleteRecursively(file4);
                        file.renameTo(file4);
                        Icons icons = INSTANCE;
                        dir = file4;
                        Unit unit = Unit.INSTANCE;
                        CloseableKt.closeFinally(zipFile, null);
                        tmp3.delete();
                        synchronized (cache) {
                            cache.clear();
                            Unit unit2 = Unit.INSTANCE;
                        }
                        Iterator it8 = listeners.iterator();
                        while (it8.hasNext()) {
                            ((Function0) it8.next()).invoke();
                        }
                        return count2;
                    } catch (Throwable th18) {
                        th = th18;
                    }
                } catch (Throwable th19) {
                    th = th19;
                }
            } catch (Throwable th20) {
                th = th20;
            }
            try {
                throw th;
            } catch (Throwable th21) {
                CloseableKt.closeFinally(zipFile, th);
                throw th21;
            }
        } catch (Throwable th22) {
            try {
                throw th22;
            } catch (Throwable th23) {
                CloseableKt.closeFinally(fileOutputStream, th22);
                throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(th23);
            }
        }
    }

    public final void remove(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        FilesKt.deleteRecursively(new File(ctx.getFilesDir(), "iconpack"));
        synchronized (cache) {
            cache.clear();
            Unit unit = Unit.INSTANCE;
        }
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            ((Function0) it.next()).invoke();
        }
    }

    private static final class ImportZipEntry {
        private final ZipEntry entry;
        private final String kind;
        private final String name;
        private final String palette;
        private final String res;
        private final String theme;

        ImportZipEntry(String palette, String theme, String kind, String res, String name, ZipEntry entry) {
            Intrinsics.checkNotNullParameter(palette, "palette");
            Intrinsics.checkNotNullParameter(theme, "theme");
            Intrinsics.checkNotNullParameter(kind, "kind");
            Intrinsics.checkNotNullParameter(res, "res");
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(entry, "entry");
            this.palette = palette;
            this.theme = theme;
            this.kind = kind;
            this.res = res;
            this.name = name;
            this.entry = entry;
        }

        public final ZipEntry getEntry() { return this.entry; }
        public final String getKind() { return this.kind; }
        public final String getName() { return this.name; }
        public final String getPalette() { return this.palette; }
        public final String getRes() { return this.res; }
        public final String getTheme() { return this.theme; }
    }
}
