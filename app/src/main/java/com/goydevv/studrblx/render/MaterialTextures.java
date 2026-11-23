package com.goydevv.studrblx.render;

import com.goydevv.studrblx.util.BackgroundPool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: MaterialTextures.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class MaterialTextures {
    private static Context app;
    private static volatile int epoch;
    private static Bitmap surfaces;
    private static boolean surfacesTried;
    public static final MaterialTextures INSTANCE = new MaterialTextures();
    private static final Map<Integer, String> names = MapsKt.mapOf(TuplesKt.to(256, "plastic"), TuplesKt.to(512, "wood"), TuplesKt.to(528, "woodplanks"), TuplesKt.to(784, "marble"), TuplesKt.to(788, "basalt"), TuplesKt.to(800, "slate"), TuplesKt.to(804, "crackedlava"), TuplesKt.to(816, "concrete"), TuplesKt.to(820, "limestone"), TuplesKt.to(832, "granite"), TuplesKt.to(836, "pavement"), TuplesKt.to(848, "brick"), TuplesKt.to(864, "pebble"), TuplesKt.to(880, "cobblestone"), TuplesKt.to(896, "rock"), TuplesKt.to(912, "sandstone"), TuplesKt.to(1040, "corrodedmetal"), TuplesKt.to(1056, "diamondplate"), TuplesKt.to(1072, "foil"), TuplesKt.to(1088, "metal"), TuplesKt.to(1280, "grass"), TuplesKt.to(1284, "leafygrass"), TuplesKt.to(1296, "sand"), TuplesKt.to(1312, "fabric"), TuplesKt.to(1328, "snow"), TuplesKt.to(1344, "mud"), TuplesKt.to(1360, "ground"), TuplesKt.to(1376, "asphalt"), TuplesKt.to(1392, "salt"), TuplesKt.to(1536, "ice"), TuplesKt.to(1552, "glacier"), TuplesKt.to(1568, "glass"), TuplesKt.to(2306, "ceramictiles"), TuplesKt.to(2307, "clayrooftiles"), TuplesKt.to(2308, "roofshingles"), TuplesKt.to(2311, "rubber"));
    private static final Set<String> doubleTile = SetsKt.setOf(new String[]{"brick", "cobblestone", "grass", "marble", "woodplanks"});
    private static final HashMap<String, Boolean> available = new HashMap<>();
    private static final ConcurrentHashMap<String, MatTex> ready = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap.KeySetView<String, Boolean> pending = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap.KeySetView<String, Boolean> failed = ConcurrentHashMap.newKeySet();
    private static final ExecutorService pool = BackgroundPool.create(1);

    private MaterialTextures() {
    }

    /* JADX INFO: compiled from: MaterialTextures.kt */
    public static final class MatTex {
        private final Bitmap color;
        private final Bitmap normal;
        private final float studsPerTile;

        public MatTex(Bitmap color, Bitmap normal, float studsPerTile) {
            this.color = color;
            this.normal = normal;
            this.studsPerTile = studsPerTile;
        }

        public final Bitmap getColor() {
            return this.color;
        }

        public final Bitmap getNormal() {
            return this.normal;
        }

        public final float getStudsPerTile() {
            return this.studsPerTile;
        }
    }

    public final int getEpoch() {
        return epoch;
    }

    public final void trimMemory(int level) {
        if (level >= 40) {
            ready.clear();
            failed.clear();
            surfaces = null;
            surfacesTried = false;
            epoch++;
        }
    }

    public final void init(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        if (app == null) {
            app = ctx.getApplicationContext();
        }
    }

    public final boolean hasTexture(int material) {
        return names.containsKey(Integer.valueOf(material));
    }

    private final boolean exists(String path) {
        Boolean boolValueOf;
        synchronized (available) {
            Map map = available;
            Boolean bool = (Boolean) map.get(path);
            if (bool == null) {
                Context context = app;
                boolean z = false;
                if (context == null) {
                    return false;
                }
                try {
                    String[] list = context.getAssets().list(com.goydevv.studrblx.util.KtCompat.substringBeforeLast(path, '/'));
                    if (list != null && ArraysKt.contains(list, com.goydevv.studrblx.util.KtCompat.substringAfterLast(path, '/'))) {
                        z = true;
                    }
                } catch (Exception e) {
                }
                boolValueOf = Boolean.valueOf(z);
                map.put(path, boolValueOf);
            } else {
                boolValueOf = bool;
            }
            return boolValueOf.booleanValue();
        }
    }

    private final Pair<String, String> pick(int material, boolean modern) {
        String n = names.get(Integer.valueOf(material));
        if (n == null) {
            return null;
        }
        if (Intrinsics.areEqual(n, "plastic")) {
            if (modern && exists("materials/modern/plastic_n.jpg")) {
                return TuplesKt.to("modern", n);
            }
            return null;
        }
        String first = modern ? "modern" : "pre2022";
        String second = modern ? "pre2022" : "modern";
        if (exists("materials/" + first + "/" + n + "_c.jpg")) {
            return TuplesKt.to(first, n);
        }
        if (exists("materials/" + second + "/" + n + "_c.jpg")) {
            return TuplesKt.to(second, n);
        }
        return null;
    }

    public final Bitmap surfaces() {
        if (surfacesTried) {
            return surfaces;
        }
        surfacesTried = true;
        Context ctx = app;
        Bitmap bitmap = null;
        if (ctx == null) {
            return null;
        }
        try {
            InputStream inputStreamOpen = ctx.getAssets().open("materials/surfaces.png");
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inScaled = false;
                Unit unit = Unit.INSTANCE;
                Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpen, null, options);
                CloseableKt.closeFinally(inputStreamOpen, null);
                bitmap = bitmapDecodeStream;
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(inputStreamOpen, th);
                    throw th2;
                }
            }
        } catch (Exception e) {
        }
        surfaces = bitmap;
        return surfaces;
    }

    public final MatTex get(int material, boolean modern) {
        final Context ctx;
        Pair<String, String> pairPick = pick(material, modern);
        if (pairPick == null) {
            return null;
        }
        final String set = pairPick.component1();
        final String n = pairPick.component2();
        final String key = set + "/" + n;
        MatTex matTex = ready.get(key);
        if (matTex != null) {
            return matTex;
        }
        if (failed.contains(key) || !pending.add(key) || (ctx = app) == null) {
            return null;
        }
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.render.MaterialTextures$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MaterialTextures.get$lambda$8(n, ctx, key, set);
            }
        });
        return null;
    }

    static final void get$lambda$8(String $n, Context $ctx, String $key, String $set) {
        Bitmap color;
        float spt;
        try {
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap normal = null;
                if (Intrinsics.areEqual($n, "plastic")) {
                    color = null;
                } else {
                    InputStream inputStreamOpen = $ctx.getAssets().open("materials/" + $key + "_c.jpg");
                    try {
                        color = BitmapFactory.decodeStream(inputStreamOpen, null, opts);
                        CloseableKt.closeFinally(inputStreamOpen, null);
                        if (color == null) {
                            throw new IllegalStateException("bad image");
                        }
                    } catch (Throwable th) {
                        try {
                            throw th;
                        } catch (Throwable th2) {
                            CloseableKt.closeFinally(inputStreamOpen, th);
                            throw th2;
                        }
                    }
                }
                try {
                    InputStream inputStreamOpen2 = $ctx.getAssets().open("materials/" + $key + "_n.jpg");
                    try {
                        normal = BitmapFactory.decodeStream(inputStreamOpen2, null, opts);
                        CloseableKt.closeFinally(inputStreamOpen2, null);
                    } catch (Throwable th3) {
                        try {
                            throw th3;
                        } catch (Throwable th4) {
                            CloseableKt.closeFinally(inputStreamOpen2, th3);
                            throw th4;
                        }
                    }
                } catch (Exception e) {
                }
                if (color == null && normal == null) {
                    throw new IllegalStateException("no maps");
                }
                if (Intrinsics.areEqual($n, "plastic")) {
                    spt = 4.0f;
                } else {
                    spt = (Intrinsics.areEqual($set, "pre2022") && doubleTile.contains($n)) ? 16.0f : 8.0f;
                }
                ready.put($key, new MatTex(color, normal, spt));
                pending.remove($key);
                epoch++;
            } catch (Exception e2) {
                failed.add($key);
            }
        } catch (Throwable th5) {
            pending.remove($key);
            MaterialTextures materialTextures3 = INSTANCE;
            int i2 = epoch;
            MaterialTextures materialTextures4 = INSTANCE;
            epoch = i2 + 1;
            throw th5;
        }
    }
}
