package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.model.Axes;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.ColorKey;
import com.goydevv.studrblx.model.ColorSequence;
import com.goydevv.studrblx.model.Faces;
import com.goydevv.studrblx.model.FontValue;
import com.goydevv.studrblx.model.NumberKey;
import com.goydevv.studrblx.model.NumberRange;
import com.goydevv.studrblx.model.NumberSequence;
import com.goydevv.studrblx.model.Rect;
import com.goydevv.studrblx.model.UDim;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.model.Vector3int16;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

public final class RobloxDefaults {
    private static final class Entry {
        final String type;
        final String[] f;
        Object value;
        boolean parsed;

        Entry(String type, String[] f) {
            this.type = type;
            this.f = f;
        }
    }

    private static final HashMap<String, HashMap<String, Entry>> classes = new HashMap<>();
    private static final HashMap<String, String> supers = new HashMap<>();
    private static volatile boolean loaded;
    private static volatile boolean failed;

    private RobloxDefaults() {
    }

    public static void preload() {
        Thread t = new Thread(RobloxDefaults::ensure, "rbx-defaults");
        t.setDaemon(true);
        t.start();
    }

    private static synchronized void ensure() {
        if (loaded || failed) return;
        try (InputStream raw = RobloxDefaults.class.getResourceAsStream("/rbx/defaults.tsv.gz")) {
            if (raw == null) {
                failed = true;
                return;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(new GZIPInputStream(raw), StandardCharsets.UTF_8));
            String line;
            HashMap<String, Entry> cur = null;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\t", -1);
                if (p.length >= 2 && "C".equals(p[0])) {
                    cur = new HashMap<>();
                    classes.put(p[1], cur);
                    if (p.length > 2 && !p[2].isEmpty()) supers.put(p[1], p[2]);
                } else if (p.length >= 4 && "P".equals(p[0]) && cur != null) {
                    String[] rest = new String[p.length - 3];
                    System.arraycopy(p, 3, rest, 0, rest.length);
                    cur.put(p[1], new Entry(p[2], rest));
                }
            }
            loaded = true;
        } catch (Throwable t) {
            failed = true;
        }
    }

    public static boolean has(String cls, String prop) {
        return find(cls, prop) != null;
    }

    private static Entry find(String cls, String prop) {
        ensure();
        if (!loaded || cls == null || prop == null) return null;
        String c = cls;
        int guard = 0;
        while (c != null && guard++ < 32) {
            HashMap<String, Entry> m = classes.get(c);
            if (m != null) {
                Entry e = m.get(prop);
                if (e != null) return e;
            }
            c = supers.get(c);
        }
        return null;
    }

    public static Object get(String cls, String prop) {
        Entry e = find(cls, prop);
        if (e == null) return null;
        synchronized (e) {
            if (!e.parsed) {
                try {
                    e.value = parse(e.type, e.f);
                } catch (Throwable t) {
                    e.value = null;
                }
                e.parsed = true;
            }
            return e.value;
        }
    }

    private static float fl(String s) {
        if ("inf".equals(s)) return Float.POSITIVE_INFINITY;
        if ("-inf".equals(s)) return Float.NEGATIVE_INFINITY;
        return Float.parseFloat(s);
    }

    private static float[] fs(String s) {
        String[] p = s.split(",");
        float[] o = new float[p.length];
        for (int i = 0; i < p.length; i++) o[i] = fl(p[i]);
        return o;
    }

    private static String unesc(String s) {
        if (s.indexOf('\\') < 0) return s;
        StringBuilder b = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char n = s.charAt(++i);
                b.append(n == 't' ? '\t' : n == 'n' ? '\n' : n);
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }

    private static Object parse(String t, String[] f) {
        String a = f[0];
        switch (t) {
            case "b":
                return Boolean.valueOf("1".equals(a));
            case "f":
                return Float.valueOf(fl(a));
            case "d":
                return Double.valueOf("inf".equals(a) ? Double.POSITIVE_INFINITY : "-inf".equals(a) ? Double.NEGATIVE_INFINITY : Double.parseDouble(a));
            case "i":
                return Integer.valueOf(Integer.parseInt(a));
            case "l":
                return Long.valueOf(Long.parseLong(a));
            case "s":
                return unesc(a);
            case "e": {
                int v = Integer.parseInt(f[1]);
                String name = f.length > 2 && !f[2].isEmpty() ? f[2] : String.valueOf(v);
                return new EnumItemV(a, name, v);
            }
            case "c": {
                float[] v = fs(a);
                return new Color3(v[0], v[1], v[2]);
            }
            case "v2": {
                float[] v = fs(a);
                return new Vector2(v[0], v[1]);
            }
            case "v3": {
                float[] v = fs(a);
                return new Vector3(v[0], v[1], v[2]);
            }
            case "u": {
                String[] p = a.split(",");
                return new UDim(fl(p[0]), Integer.parseInt(p[1]));
            }
            case "u2": {
                String[] p = a.split(",");
                return new UDim2(fl(p[0]), Integer.parseInt(p[1]), fl(p[2]), Integer.parseInt(p[3]));
            }
            case "cf": {
                float[] v = fs(a);
                return new CFrame(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9], v[10], v[11]);
            }
            case "nr": {
                float[] v = fs(a);
                return new NumberRange(v[0], v[1]);
            }
            case "ns": {
                ArrayList<NumberKey> keys = new ArrayList<>();
                for (String k : a.split(";")) {
                    float[] v = fs(k);
                    keys.add(new NumberKey(v[0], v[1], v[2]));
                }
                return new NumberSequence(keys);
            }
            case "cs": {
                ArrayList<ColorKey> keys = new ArrayList<>();
                for (String k : a.split(";")) {
                    float[] v = fs(k);
                    keys.add(new ColorKey(v[0], new Color3(v[1], v[2], v[3]), 0.0f));
                }
                return new ColorSequence(keys);
            }
            case "r": {
                float[] v = fs(a);
                return new Rect(v[0], v[1], v[2], v[3]);
            }
            case "ft":
                return new FontValue(a, Integer.parseInt(f[1]), Integer.parseInt(f[2]), f.length > 3 ? f[3] : "");
            case "bc":
                return new BrickColorV(Integer.parseInt(a));
            case "v3i": {
                String[] p = a.split(",");
                return new Vector3int16(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
            }
            case "fa":
                return new Faces(Integer.parseInt(a));
            case "ax":
                return new Axes(Integer.parseInt(a));
            default:
                return null;
        }
    }
}
