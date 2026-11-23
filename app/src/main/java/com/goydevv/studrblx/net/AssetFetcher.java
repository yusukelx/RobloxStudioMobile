package com.goydevv.studrblx.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.model.Instance;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AssetFetcher.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class AssetFetcher {
    private static Context app;
    private static final LruCache<Long, Bitmap> bitmaps;
    private static File dir;
    private static volatile int imageEpoch;
    private static final ConcurrentHashMap<Long, Long> imageFailed;
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> imagePending;
    public static final AssetFetcher INSTANCE = new AssetFetcher();
    private static final ExecutorService pool = Executors.newFixedThreadPool(2);
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> pending = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap<Long, Long> failed = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, String> errors = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<Function1<Long, Unit>> listeners = new CopyOnWriteArrayList<>();

    private AssetFetcher() {
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.goydevv.studrblx.net.AssetFetcher$bitmaps$1] */
    static {
        final int iMin = (int) Math.min(8388608L, Runtime.getRuntime().maxMemory() / 24);
        bitmaps = new LruCache<Long, Bitmap>(iMin) { // from class: com.goydevv.studrblx.net.AssetFetcher$bitmaps$1
            @Override // android.util.LruCache
            public /* bridge */ /* synthetic */ int sizeOf(Long l, Bitmap bitmap) {
                return sizeOf(l.longValue(), bitmap);
            }

            protected int sizeOf(long key, Bitmap value) {
                Intrinsics.checkNotNullParameter(value, "value");
                return value.getByteCount();
            }
        };
        imagePending = ConcurrentHashMap.newKeySet();
        imageFailed = new ConcurrentHashMap<>();
    }

    public final CopyOnWriteArrayList<Function1<Long, Unit>> getListeners() {
        return listeners;
    }

    public final void init(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        if (app == null) {
            app = ctx.getApplicationContext();
            File file = new File(ctx.getCacheDir(), "assets");
            file.mkdirs();
            dir = file;
        }
    }

    private final File file(long id) {
        File file = dir;
        if (file != null) {
            return new File(file, id + ".bin");
        }
        return null;
    }

    public final byte[] request(final long id) {
        File file = file(id);
        if (file != null) {
            if (!file.isFile()) {
                file = null;
            }
            if (file != null) {
                try {
                    return FilesKt.readBytes(file);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        Long f = failed.get(Long.valueOf(id));
        if ((f != null && System.currentTimeMillis() - f.longValue() < 90000) || !pending.add(Long.valueOf(id))) {
            return null;
        }
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.net.AssetFetcher$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AssetFetcher.request$lambda$5(id);
            }
        });
        return null;
    }

    static final void request$lambda$5(long $id) {
        try {
            INSTANCE.load($id);
            AssetFetcher assetFetcher = INSTANCE;
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                ((Function1) it.next()).invoke(Long.valueOf($id));
            }
        } catch (Exception e) {
            failed.put(Long.valueOf($id), Long.valueOf(System.currentTimeMillis()));
            ConcurrentHashMap<Long, String> concurrentHashMap = errors;
            Long lValueOf = Long.valueOf($id);
            String message = e.getMessage();
            if (message == null) {
                message = e.toString();
            }
            concurrentHashMap.put(lValueOf, message);
        } finally {
            pending.remove(Long.valueOf($id));
        }
    }

    public final boolean isCached(long id) {
        File file = file(id);
        return file != null && file.isFile();
    }

    public final String cacheFile(long id) {
        File file = file(id);
        if (file == null) {
            return null;
        }
        if (!file.isFile()) {
            file = null;
        }
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    public final String lastError(long id) {
        return errors.get(Long.valueOf(id));
    }

    public final byte[] load(long id) {
        File f = file(id);
        if (f != null && f.isFile()) {
            return FilesKt.readBytes(f);
        }
        Context ctx = app;
        if (ctx == null) {
            throw new IllegalStateException("AssetFetcher.init not called");
        }
        byte[] bytes = RobloxApi.downloadAsset$default(RobloxApi.INSTANCE, ctx, id, null, 4, null);
        if (f != null) {
            try {
                File tmp = new File(f.getPath() + ".tmp");
                FilesKt.writeBytes(tmp, bytes);
                tmp.renameTo(f);
            } catch (Exception e) {
            }
        }
        failed.remove(Long.valueOf(id));
        errors.remove(Long.valueOf(id));
        return bytes;
    }

    public final void trimMemory(int level) {
        bitmaps.trimToSize(level >= 80 ? 0 : bitmaps.maxSize() / 2);
        if (level >= 80) imageFailed.clear();
    }

    public final int getImageEpoch() {
        return imageEpoch;
    }

    public final Bitmap image(final long id) {
        Bitmap bitmap = bitmaps.get(Long.valueOf(id));
        if (bitmap != null) {
            return bitmap;
        }
        Long f = imageFailed.get(Long.valueOf(id));
        if ((f != null && System.currentTimeMillis() - f.longValue() < 90000) || !imagePending.add(Long.valueOf(id))) {
            return null;
        }
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.net.AssetFetcher$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AssetFetcher.image$lambda$9(id);
            }
        });
        return null;
    }

    static final void image$lambda$9(long $id) {
        try {
            try {
                Bitmap b = INSTANCE.loadImage($id, 0);
                if (b == null) {
                    imageFailed.put(Long.valueOf($id), Long.valueOf(System.currentTimeMillis()));
                } else {
                    bitmaps.put(Long.valueOf($id), b);
                    AssetFetcher assetFetcher = INSTANCE;
                    int i = imageEpoch;
                    AssetFetcher assetFetcher2 = INSTANCE;
                    imageEpoch = i + 1;
                    AssetFetcher assetFetcher3 = INSTANCE;
                    Iterator it = listeners.iterator();
                    while (it.hasNext()) {
                        ((Function1) it.next()).invoke(Long.valueOf($id));
                    }
                }
            } catch (Exception e) {
                imageFailed.put(Long.valueOf($id), Long.valueOf(System.currentTimeMillis()));
            }
        } finally {
            imagePending.remove(Long.valueOf($id));
        }
    }

    public final boolean imageFailed(long id) {
        return imageFailed.containsKey(Long.valueOf(id)) && bitmaps.get(Long.valueOf(id)) == null;
    }

    public final Bitmap imageBlocking(long id) {
        Bitmap bitmap = bitmaps.get(Long.valueOf(id));
        if (bitmap != null) {
            return bitmap;
        }
        try {
            Bitmap bitmapLoadImage = loadImage(id, 0);
            if (bitmapLoadImage == null) {
                return null;
            }
            bitmaps.put(Long.valueOf(id), bitmapLoadImage);
            AssetFetcher assetFetcher = INSTANCE;
            int i = imageEpoch;
            AssetFetcher assetFetcher2 = INSTANCE;
            imageEpoch = i + 1;
            return bitmapLoadImage;
        } catch (Exception e) {
            return null;
        }
    }

    private final Bitmap fetchRobloxThumbnail(long id) {
        try {
            org.json.JSONObject j = Http.INSTANCE.json("https://thumbnails.roblox.com/v1/assets?assetIds=" + id + "&returnPolicy=PlaceHolder&size=420x420&format=Png&isCircular=false", null);
            org.json.JSONArray arr = j.optJSONArray("data");
            if (arr != null && arr.length() > 0) {
                org.json.JSONObject item = arr.optJSONObject(0);
                String url = item != null ? item.optString("imageUrl") : null;
                if (url != null && url.length() > 0) {
                    byte[] imgBytes = Http.get$default(Http.INSTANCE, url, null, 0, 6, null);
                    return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private final Bitmap loadImage(long id, int depth) {
        byte[] bytes = null;
        try {
            bytes = load(id);
        } catch (Exception e) {
            Bitmap thumb = fetchRobloxThumbnail(id);
            if (thumb != null) return thumb;
            return null;
        }
        if (bytes == null || bytes.length == 0) {
            return fetchRobloxThumbnail(id);
        }
        Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmapDecodeByteArray == null) {
            if (depth > 1) {
                return null;
            }
            try {
                RbxDocument doc = RbxFile.INSTANCE.read(bytes);
                for (Instance r : doc.getRoots()) {
                    for (Instance i : (List<Instance>) CollectionsKt.plus((Collection) CollectionsKt.listOf(r), (Iterable) Instance.descendants$default(r, null, 1, null))) {
                        for (String k : CollectionsKt.listOf(new String[]{"ShirtTemplate", "PantsTemplate", "Graphic", "Texture", "TextureId", "Image", "ColorMap"})) {
                            ImageLoader imageLoader = ImageLoader.INSTANCE;
                            Object obj = i.get(k);
                            Long lAssetId = imageLoader.assetId(obj != null ? obj.toString() : null);
                            if (lAssetId != null) {
                                long inner = lAssetId.longValue();
                                if (inner != id) {
                                    return loadImage(inner, depth + 1);
                                }
                            }
                        }
                    }
                }
                return fetchRobloxThumbnail(id);
            } catch (Exception e) {
                return fetchRobloxThumbnail(id);
            }
        }
        return bitmapDecodeByteArray;
    }

    public final Long idOf(Object content) {
        return ImageLoader.INSTANCE.assetId(content != null ? content.toString() : null);
    }
}
