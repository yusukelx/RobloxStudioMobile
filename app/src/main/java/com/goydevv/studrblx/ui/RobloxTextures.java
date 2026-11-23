package com.goydevv.studrblx.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class RobloxTextures {
    private static final String BASE = "https://raw.githubusercontent.com/MaximumADHD/Roblox-Client-Tracker/roblox/textures/";
    private static final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(4194304) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };
    private static final Set<String> pending = new HashSet<>();
    private static final Set<String> failed = new HashSet<>();
    private static final ExecutorService pool = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "rbx-textures");
        t.setDaemon(true);
        return t;
    });
    private static volatile File dir;

    private RobloxTextures() {
    }

    public static void init(Context ctx) {
        if (dir == null && ctx != null) {
            dir = new File(ctx.getFilesDir(), "rbxtextures");
        }
    }

    public static Bitmap get(String rel) {
        synchronized (cache) {
            Bitmap b = cache.get(rel);
            if (b != null || failed.contains(rel)) return b;
        }
        File d = dir;
        if (d == null) return null;
        File f = new File(d, rel.replace('/', '_'));
        if (f.isFile()) {
            try {
                Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath());
                if (b != null) {
                    synchronized (cache) {
                        cache.put(rel, b);
                    }
                    return b;
                }
            } catch (Throwable ignored) {
            }
            f.delete();
        }
        synchronized (cache) {
            if (!pending.add(rel)) return null;
        }
        pool.execute(() -> fetch(rel, f));
        return null;
    }

    public static void trimMemory(int level) {
        synchronized (cache) {
            cache.trimToSize(level >= 80 ? 0 : cache.maxSize() / 2);
            if (level >= 80) failed.clear();
        }
    }

    private static void fetch(String rel, File f) {
        boolean ok = false;
        HttpURLConnection c = null;
        try {
            File parent = f.getParentFile();
            if (parent != null) parent.mkdirs();
            c = (HttpURLConnection) new URL(BASE + rel.replace("@", "%40")).openConnection();
            c.setConnectTimeout(8000);
            c.setReadTimeout(15000);
            if (c.getResponseCode() == 200) {
                File tmp = new File(f.getPath() + ".part");
                try (InputStream in = c.getInputStream(); FileOutputStream out = new FileOutputStream(tmp)) {
                    byte[] buf = new byte[16384];
                    int n;
                    while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                }
                Bitmap b = BitmapFactory.decodeFile(tmp.getAbsolutePath());
                if (b != null && tmp.renameTo(f)) {
                    synchronized (cache) {
                        cache.put(rel, b);
                    }
                    ok = true;
                } else {
                    tmp.delete();
                }
            }
        } catch (Throwable ignored) {
        } finally {
            if (c != null) c.disconnect();
            synchronized (cache) {
                pending.remove(rel);
                if (!ok) failed.add(rel);
            }
        }
    }
}
