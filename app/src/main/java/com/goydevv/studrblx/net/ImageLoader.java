package com.goydevv.studrblx.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: ImageLoader.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class ImageLoader {
    public static final ImageLoader INSTANCE = new ImageLoader();
    private static volatile boolean batchScheduled;
    private static File cacheDir;
    private static volatile int epoch;
    private static final ConcurrentHashMap<String, Long> failed;
    private static final ConcurrentHashMap<String, Long> idMemo;
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> idQueue;
    private static final List<Regex> idRx;
    private static final ConcurrentHashMap<Long, String> keyMemo;
    private static final CopyOnWriteArrayList<Function1<String, Unit>> listeners;
    private static final LruCache<String, Bitmap> mem;
    private static final ConcurrentHashMap.KeySetView<String, Boolean> pending;
    private static final ExecutorService pool;

    private ImageLoader() {
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.goydevv.studrblx.net.ImageLoader$mem$1] */
    static {
        final int iMin = (int) Math.min(16777216L, Runtime.getRuntime().maxMemory() / 20);
        mem = new LruCache<String, Bitmap>(iMin) { // from class: com.goydevv.studrblx.net.ImageLoader$mem$1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.util.LruCache
            public int sizeOf(String key, Bitmap value) {
                Intrinsics.checkNotNullParameter(key, "key");
                Intrinsics.checkNotNullParameter(value, "value");
                return value.getByteCount();
            }
        };
        pending = ConcurrentHashMap.newKeySet();
        failed = new ConcurrentHashMap<>();
        pool = Executors.newFixedThreadPool(2);
        idQueue = ConcurrentHashMap.newKeySet();
        listeners = new CopyOnWriteArrayList<>();
        idRx = CollectionsKt.listOf(new Regex[]{new Regex("rbxassetid://(\\d+)", RegexOption.IGNORE_CASE), new Regex("[?&]id=(\\d+)", RegexOption.IGNORE_CASE), new Regex("^\\s*(\\d+)\\s*$"), new Regex("rbxthumb://.*id=(\\d+)", RegexOption.IGNORE_CASE)});
        idMemo = new ConcurrentHashMap<>();
        keyMemo = new ConcurrentHashMap<>();
    }

    public final CopyOnWriteArrayList<Function1<String, Unit>> getListeners() {
        return listeners;
    }

    public final void init(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        if (cacheDir == null) {
            File file = new File(ctx.getCacheDir(), "img");
            file.mkdirs();
            cacheDir = file;
        }
    }

    public final Long assetId(String content) {
        String str = content;
        if (str == null || StringsKt.isBlank(str)) {
            return null;
        }
        String low = str.trim().toLowerCase(Locale.ROOT);
        if (low.contains("spawnlocation.png")) {
            return Long.valueOf(12224170L);
        }
        if (low.endsWith("/face.png") || low.equals("rbxasset://textures/face.png")) {
            return Long.valueOf(7075502L);
        }
        Long l = idMemo.get(content);
        if (l != null) {
            long jLongValue = l.longValue();
            if (jLongValue >= 0) {
                return Long.valueOf(jLongValue);
            }
            return null;
        }
        Long id = parseId(content);
        if (idMemo.size() > 50000) {
            idMemo.clear();
        }
        idMemo.put(content, Long.valueOf(id != null ? id.longValue() : -1L));
        return id;
    }

    private final Long parseId(String content) {
        if (StringsKt.startsWith(content, "rbxasset://", true) && !StringsKt.startsWith(content, "rbxassetid://", true)) {
            return null;
        }
        for (Regex r : idRx) {
            MatchResult matchResultFind$default = (r).find(content, 0);
            if (matchResultFind$default != null) {
                return StringsKt.toLongOrNull(matchResultFind$default.getGroupValues().get(1));
            }
        }
        return null;
    }

    public final String keyFor(String content) {
        String strPutIfAbsent;
        String str = content;
        if (str == null || StringsKt.isBlank(str)) {
            return null;
        }
        Long lAssetId = assetId(content);
        if (lAssetId == null) {
            if (StringsKt.startsWith(content, "http://", true) || StringsKt.startsWith(content, "https://", true)) {
                return "url:" + content;
            }
            return null;
        }
        long jLongValue = lAssetId.longValue();
        ConcurrentMap concurrentMap = keyMemo;
        Long lValueOf = Long.valueOf(jLongValue);
        String str2 = (String) concurrentMap.get(lValueOf);
        if (str2 == null && (strPutIfAbsent = (String) concurrentMap.putIfAbsent(lValueOf, (str2 = "asset:" + jLongValue))) != null) {
            str2 = strPutIfAbsent;
        }
        return str2;
    }

    public final Bitmap get(String content) {
        String key = keyFor(content);
        if (key == null) {
            return null;
        }
        return getKey(key);
    }

    public final Bitmap getKey(final String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        Bitmap bitmap = mem.get(key);
        if (bitmap != null) {
            return bitmap;
        }
        Long f = failed.get(key);
        if ((f != null && System.currentTimeMillis() - f.longValue() < 60000) || !pending.add(key)) {
            return null;
        }
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.net.ImageLoader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    ImageLoader.INSTANCE.loadFromDisk(key);
                } catch (Exception _e) {
                    if (_e instanceof RuntimeException) throw (RuntimeException) _e;
                    throw new RuntimeException(_e);
                }
            }
        });
        return null;
    }

    public final Bitmap peekKey(String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        return mem.get(key);
    }

    public final void trimMemory(int level) {
        mem.trimToSize(level >= 80 ? 0 : mem.maxSize() / 2);
        if (level >= 80) {
            idMemo.clear();
            keyMemo.clear();
            failed.clear();
        }
    }

    private final File diskFile(String key) {
        try {
        File dir = cacheDir;
        if (dir == null) {
            return null;
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = key.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] bArrDigest = messageDigest.digest(bytes);
        Intrinsics.checkNotNullExpressionValue(bArrDigest, "digest(...)");
        String h = ArraysKt.joinToString(bArrDigest, (CharSequence) "", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.net.ImageLoader$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ImageLoader.diskFile$lambda$7(((Byte) obj).byteValue());
            }
        });
        return new File(dir, h + ".png");
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final CharSequence diskFile$lambda$7(byte it) {
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void loadFromDisk(String key) {
        try {
        Bitmap b;
        File f = diskFile(key);
        if (f != null && f.isFile()) {
            try {
                b = BitmapFactory.decodeFile(f.getPath());
            } catch (Throwable th) {
                b = null;
            }
            if (b != null) {
                done(key, b);
                return;
            }
        }
        if (!StringsKt.startsWith(key, "url:", false)) {
            if (StringsKt.startsWith(key, "asset:", false)) {
                idQueue.add(Long.valueOf(Long.parseLong(StringsKt.removePrefix(key, (CharSequence) "asset:"))));
                scheduleBatch();
                return;
            } else {
                fail(key);
                return;
            }
        }
        fetchUrl(key, StringsKt.removePrefix(key, (CharSequence) "url:"));
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final void scheduleBatch() {
        if (batchScheduled) {
            return;
        }
        batchScheduled = true;
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.net.ImageLoader$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    ImageLoader.scheduleBatch$lambda$9();
                } catch (Exception _e) {
                    if (_e instanceof RuntimeException) throw (RuntimeException) _e;
                    throw new RuntimeException(_e);
                }
            }
        });
    }

    static final void scheduleBatch$lambda$9() {
        try {
        Thread.sleep(80L);
        ImageLoader imageLoader = INSTANCE;
        batchScheduled = false;
        ConcurrentHashMap.KeySetView<Long, Boolean> idQueue2 = idQueue;
        Intrinsics.checkNotNullExpressionValue(idQueue2, "idQueue");
        List<Long> listTake = CollectionsKt.take(CollectionsKt.toList(idQueue2), 100);
        idQueue.removeAll(CollectionsKt.toSet(listTake));
        ConcurrentHashMap.KeySetView<Long, Boolean> idQueue3 = idQueue;
        Intrinsics.checkNotNullExpressionValue(idQueue3, "idQueue");
        if (!idQueue3.isEmpty()) {
            INSTANCE.scheduleBatch();
        }
        if (listTake.isEmpty()) {
            return;
        }
        try {
            Map<Long, String> mapThumbnailUrls = RobloxApi.INSTANCE.thumbnailUrls(listTake, "420x420");
            for (final Long id : listTake) {
                final String u = mapThumbnailUrls.get(id);
                if (u == null) {
                    INSTANCE.fail("asset:" + id);
                } else if (u.length() == 0) {
                    pending.remove("asset:" + id);
                    failed.put("asset:" + id, Long.valueOf(System.currentTimeMillis() - 55000));
                } else {
                    pool.execute(new Runnable() { // from class: com.goydevv.studrblx.net.ImageLoader$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ImageLoader.INSTANCE.fetchUrl("asset:" + id, u);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Iterator<Long> it = listTake.iterator();
            while (it.hasNext()) {
                INSTANCE.fail("asset:" + it.next());
            }
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void fetchUrl(String key, String url) {
        String lowerCase;
        try {
            String host = new URL(url).getHost();
            Intrinsics.checkNotNullExpressionValue(host, "getHost(...)");
            lowerCase = host.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        } catch (Exception e) {
            lowerCase = "";
        }
        try {
            String host2 = lowerCase;
            try {
                if (!WebPermissions.INSTANCE.isRobloxHost(host2)) {
                    throw new IOException("not a Roblox image");
                }
                byte[] bytes = Http.get$default(Http.INSTANCE, url, null, 8388608, 2, null);
                Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (b == null) {
                    throw new Exception("bad image");
                }
                File fileDiskFile = diskFile(key);
                if (fileDiskFile != null) {
                    try {
                        FilesKt.writeBytes(fileDiskFile, bytes);
                    } catch (Exception e2) {
                    }
                }
                done(key, b);
                return;
            } catch (Exception e3) {
            }
        } catch (Exception e4) {
        }
        fail(key);
    }

    public final int getEpoch() {
        return epoch;
    }

    private final void done(String key, Bitmap b) {
        mem.put(key, b);
        epoch++;
        pending.remove(key);
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            ((Function1) it.next()).invoke(key);
        }
    }

    private final void fail(String key) {
        pending.remove(key);
        failed.put(key, Long.valueOf(System.currentTimeMillis()));
    }
}
