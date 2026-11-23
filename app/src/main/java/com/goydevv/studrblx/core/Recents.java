package com.goydevv.studrblx.core;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: Recents.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Recents {
    public static final Recents INSTANCE = new Recents();

    /* JADX INFO: compiled from: Recents.kt */
    public static final class Entry {
        private final boolean archived;
        private final String name;
        private final long placeId;
        private final long time;
        private final long universeId;
        private final String uri;
        private final long userId;

        public Entry(String name, long placeId, long universeId, String uri, long time, long userId, boolean archived) {
            Intrinsics.checkNotNullParameter(name, "name");
            this.name = name;
            this.placeId = placeId;
            this.universeId = universeId;
            this.uri = uri;
            this.time = time;
            this.userId = userId;
            this.archived = archived;
        }

        public Entry(String name, long placeId, long universeId, String uri, long time, long userId) {
            this(name, placeId, universeId, uri, time, userId, false);
        }

        public /* synthetic */ Entry(String str, long j, long j2, String str2, long j3, long j4, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? 0L : j, (i & 4) != 0 ? 0L : j2, (i & 8) != 0 ? null : str2, (i & 16) != 0 ? System.currentTimeMillis() : j3, (i & 32) != 0 ? 0L : j4, false);
        }

        public final String getName() {
            return this.name;
        }

        public final long getPlaceId() {
            return this.placeId;
        }

        public final long getUniverseId() {
            return this.universeId;
        }

        public final String getUri() {
            return this.uri;
        }

        public final long getTime() {
            return this.time;
        }

        public final long getUserId() {
            return this.userId;
        }

        public final boolean isArchived() {
            return this.archived;
        }

        public final boolean isCloud() {
            return this.placeId != 0;
        }

        public final String key() {
            StringBuilder sbAppend;
            if (isCloud()) {
                sbAppend = new StringBuilder().append("p:").append(this.placeId);
            } else {
                sbAppend = new StringBuilder().append("f:").append(this.uri);
            }
            return sbAppend.toString();
        }
    }

    private Recents() {
    }

    private final SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences("recents", 0);
    }

    private final void saveList(Context ctx, List<Entry> list) {
        try {
            JSONArray a = new JSONArray();
            for (Entry x : list) {
                JSONObject jSONObjectPut = new JSONObject()
                        .put("name", x.getName())
                        .put("placeId", x.getPlaceId())
                        .put("universeId", x.getUniverseId());
                String uri = x.getUri();
                if (uri == null) {
                    uri = "";
                }
                a.put(jSONObjectPut
                        .put("uri", uri)
                        .put("time", x.getTime())
                        .put("userId", x.getUserId())
                        .put("archived", x.isArchived()));
            }
            prefs(ctx).edit().putString("list", a.toString()).apply();
        } catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final List<Entry> list(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String raw = prefs(ctx).getString("list", null);
        if (raw == null) {
            return CollectionsKt.emptyList();
        }
        try {
            JSONArray a = new JSONArray(raw);
            Iterable iterableUntil = RangesKt.until(0, a.length());
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil, 10));
            Iterator<Integer> it = iterableUntil.iterator();
            while (it.hasNext()) {
                JSONObject jSONObject = a.getJSONObject(((IntIterator) it).nextInt());
                String strOptString = jSONObject.optString("name");
                Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                long jOptLong = jSONObject.optLong("placeId");
                long jOptLong2 = jSONObject.optLong("universeId");
                String strOptString2 = jSONObject.optString("uri");
                if (strOptString2.length() == 0) {
                    strOptString2 = null;
                }
                arrayList.add(new Entry(strOptString, jOptLong, jOptLong2, strOptString2, jSONObject.optLong("time"), jSONObject.optLong("userId"), jSONObject.optBoolean("archived", false)));
            }
            return (List) arrayList;
        } catch (Exception e) {
            return CollectionsKt.emptyList();
        }
    }

    public final void add(Context ctx, Entry e) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(e, "e");
        Iterable list = list(ctx);
        Collection arrayList = new ArrayList();
        for (Object obj : list) {
            if (!Intrinsics.areEqual(((Entry) obj).key(), e.key())) {
                arrayList.add(obj);
            }
        }
        List<Entry> l = CollectionsKt.toMutableList(arrayList);
        l.add(0, e);
        saveList(ctx, CollectionsKt.take(l, 40));
    }

    public final void remove(Context ctx, Entry e) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(e, "e");
        Iterable list = list(ctx);
        Collection arrayList = new ArrayList();
        for (Object obj : list) {
            if (!Intrinsics.areEqual(((Entry) obj).key(), e.key())) {
                arrayList.add(obj);
            }
        }
        saveList(ctx, (List) arrayList);
    }

    public final void rename(Context ctx, Entry e, String newName) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(e, "e");
        Intrinsics.checkNotNullParameter(newName, "newName");
        String trimmed = newName.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        List<Entry> current = list(ctx);
        List<Entry> updated = new ArrayList<>(current.size());
        for (Entry x : current) {
            if (Intrinsics.areEqual(x.key(), e.key())) {
                updated.add(new Entry(trimmed, x.getPlaceId(), x.getUniverseId(), x.getUri(), x.getTime(), x.getUserId(), x.isArchived()));
            } else {
                updated.add(x);
            }
        }
        saveList(ctx, updated);
    }

    public final void setArchived(Context ctx, Entry e, boolean archived) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(e, "e");
        List<Entry> current = list(ctx);
        List<Entry> updated = new ArrayList<>(current.size());
        for (Entry x : current) {
            if (Intrinsics.areEqual(x.key(), e.key())) {
                updated.add(new Entry(x.getName(), x.getPlaceId(), x.getUniverseId(), x.getUri(), x.getTime(), x.getUserId(), archived));
            } else {
                updated.add(x);
            }
        }
        saveList(ctx, updated);
    }
}
