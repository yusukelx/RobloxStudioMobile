package com.goydevv.studrblx.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import com.goydevv.studrblx.core.OutputLog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class CharacterSounds {
    public static final String JUMP = "action_jump.mp3";
    public static final String LAND = "action_jump_land.mp3";
    public static final String RUN = "action_footsteps_plastic.mp3";
    private static final String[] FILES = {JUMP, LAND, RUN, "action_get_up.mp3", "action_swim.mp3", "action_falling.ogg", "impact_water.mp3"};
    private static final Pattern VERSION = Pattern.compile("\"clientVersionUpload\"\\s*:\\s*\"(version-[0-9a-f]+)\"");
    private static final HashMap<String, Integer> ids = new HashMap<>();
    private static final HashMap<Integer, Boolean> loaded = new HashMap<>();
    private static volatile SoundPool pool;
    private static volatile File dir;
    private static volatile boolean started;
    private static volatile boolean muted;
    private static int runStream;
    private static float runRate = -1.0f;

    private CharacterSounds() {
    }

    public static void init(Context ctx) {
        if (started || ctx == null) return;
        started = true;
        try {
            dir = new File(ctx.getFilesDir(), "rbxsounds");
        } catch (Throwable t) {
            return;
        }
        Thread th = new Thread(CharacterSounds::prepare, "rbx-sounds");
        th.setDaemon(true);
        th.start();
    }

    public static void setMuted(boolean m) {
        muted = m;
        if (m) stopRun();
    }

    public static File file(String name) {
        File d = dir;
        if (d == null) return null;
        File f = new File(d, name);
        return f.isFile() && f.length() > 0 ? f : null;
    }

    private static boolean haveAll() {
        for (String f : FILES) {
            if (file(f) == null) return false;
        }
        return true;
    }

    private static byte[] get(String url, int max) throws Exception {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(10000);
        c.setReadTimeout(20000);
        c.setRequestProperty("User-Agent", "MobileStudio/1.0");
        try {
            int code = c.getResponseCode();
            if (code != 200) throw new Exception("HTTP " + code + " for " + url);
            try (InputStream in = c.getInputStream()) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[16384];
                int n;
                while ((n = in.read(buf)) > 0) {
                    out.write(buf, 0, n);
                    if (out.size() > max) throw new Exception("response too large");
                }
                return out.toByteArray();
            }
        } finally {
            c.disconnect();
        }
    }

    private static void download() throws Exception {
        String json = new String(get("https://clientsettings.roblox.com/v2/client-version/WindowsStudio64", 65536), StandardCharsets.UTF_8);
        Matcher m = VERSION.matcher(json);
        if (!m.find()) throw new Exception("client version not found");
        byte[] zip = get("https://setup.rbxcdn.com/" + m.group(1) + "-content-sounds.zip", 8 * 1024 * 1024);
        File d = dir;
        if (d == null) return;
        if (!d.isDirectory() && !d.mkdirs()) throw new Exception("cannot create " + d);
        try (ZipInputStream zin = new ZipInputStream(new java.io.ByteArrayInputStream(zip))) {
            ZipEntry e;
            byte[] buf = new byte[16384];
            while ((e = zin.getNextEntry()) != null) {
                String name = e.getName().replace('\\', '/');
                int slash = name.lastIndexOf('/');
                if (slash >= 0) name = name.substring(slash + 1);
                boolean want = false;
                for (String f : FILES) {
                    if (f.equals(name)) want = true;
                }
                if (!want || e.isDirectory()) continue;
                File tmp = new File(d, name + ".part");
                try (FileOutputStream out = new FileOutputStream(tmp)) {
                    int n;
                    while ((n = zin.read(buf)) > 0) out.write(buf, 0, n);
                }
                File dst = new File(d, name);
                if (!tmp.renameTo(dst)) {
                    tmp.delete();
                }
            }
        }
    }

    private static void prepare() {
        try {
            if (!haveAll()) download();
        } catch (Throwable t) {
            OutputLog.INSTANCE.warn("Default character sounds unavailable: " + t.getMessage());
        }
        try {
            SoundPool p = new SoundPool.Builder().setMaxStreams(6).setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()).build();
            p.setOnLoadCompleteListener((sp, sampleId, status) -> {
                synchronized (loaded) {
                    loaded.put(sampleId, status == 0);
                }
            });
            for (String f : FILES) {
                File file = file(f);
                if (file == null) continue;
                try {
                    int id = p.load(file.getAbsolutePath(), 1);
                    synchronized (ids) {
                        ids.put(f, id);
                    }
                } catch (Throwable t) {
                    OutputLog.INSTANCE.warn("Couldn't load " + f + ": " + t.getMessage());
                }
            }
            pool = p;
        } catch (Throwable t) {
            OutputLog.INSTANCE.warn("Character sound player unavailable: " + t.getMessage());
        }
    }

    private static int ready(String name) {
        Integer id;
        synchronized (ids) {
            id = ids.get(name);
        }
        if (id == null) return 0;
        synchronized (loaded) {
            return Boolean.TRUE.equals(loaded.get(id)) ? id : 0;
        }
    }

    public static boolean play(String name, float volume, float rate) {
        if (muted) return false;
        SoundPool p = pool;
        if (p == null) return false;
        try {
            int id = ready(name);
            if (id == 0) return false;
            float v = Math.max(0.0f, Math.min(1.0f, volume));
            return p.play(id, v, v, 1, 0, Math.max(0.5f, Math.min(2.0f, rate))) != 0;
        } catch (Throwable t) {
            return false;
        }
    }

    public static synchronized void running(boolean on, float speed, float volume) {
        SoundPool p = pool;
        if (p == null) return;
        try {
            if (!on || muted) {
                stopRun();
                return;
            }
            float rate = Math.max(0.5f, Math.min(2.0f, speed / 16.0f));
            if (runStream == 0) {
                int id = ready(RUN);
                if (id == 0) return;
                float v = Math.max(0.0f, Math.min(1.0f, volume));
                runStream = p.play(id, v, v, 0, -1, rate);
                runRate = rate;
            } else if (Math.abs(rate - runRate) > 0.05f) {
                p.setRate(runStream, rate);
                runRate = rate;
            }
        } catch (Throwable ignored) {
        }
    }

    public static synchronized void stopRun() {
        SoundPool p = pool;
        if (p != null && runStream != 0) {
            try {
                p.stop(runStream);
            } catch (Throwable ignored) {
            }
        }
        runStream = 0;
        runRate = -1.0f;
    }
}
