package com.goydevv.studrblx.ui;

import android.content.Context;
import android.content.SharedPreferences;
import com.goydevv.studrblx.net.RobloxCloud;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: StartScreen.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class StartCache {
    public static final StartCache INSTANCE = new StartCache();
    private static final Map<Long, String> icons = new ConcurrentHashMap();
    private static final Map<Long, String> groupIcons = new ConcurrentHashMap();

    private StartCache() {
    }

    public final Map<Long, String> getIcons() {
        return icons;
    }

    public final Map<Long, String> getGroupIcons() {
        return groupIcons;
    }

    private final SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences("start_cache", 0);
    }

    public final void forget(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        prefs(ctx).edit().clear().apply();
    }

    private final JSONObject gameJson(RobloxCloud.Game g) {
        try {
            return new JSONObject()
                    .put("u", g.getUniverseId())
                    .put("n", g.getName())
                    .put("r", g.getRootPlaceId())
                    .put("t", g.getUpdated())
                    .put("o", g.getOwner())
                    .put("g", g.getGroupId())
                    .put("p", g.getPrivacyType())
                    .put("a", g.isArchived());
        } catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final RobloxCloud.Game gameOf(JSONObject o) {
        long jOptLong = o.optLong("u");
        String strOptString = o.optString("n");
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        long jOptLong2 = o.optLong("r");
        String strOptString2 = o.optString("t");
        Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
        String strOptString3 = o.optString("o");
        Intrinsics.checkNotNullExpressionValue(strOptString3, "optString(...)");
        String strOptString4 = o.optString("p", "");
        Intrinsics.checkNotNullExpressionValue(strOptString4, "optString(...)");
        return new RobloxCloud.Game(jOptLong, strOptString, jOptLong2, strOptString2, strOptString3, o.optLong("g"), strOptString4, o.optBoolean("a", false));
    }

    public static /* synthetic */ List<RobloxCloud.Game> games$default(StartCache startCache, Context context, long j, String str, int i, Object obj) {
        if ((i & 4) != 0) {
            str = "games";
        }
        return startCache.games(context, j, str);
    }

    public final List<RobloxCloud.Game> games(Context ctx, long uid, String kind) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(kind, "kind");
        try {
            try {
                String string = prefs(ctx).getString(kind + "_" + uid, null);
                if (string == null) {
                    return null;
                }
                JSONArray a = new JSONArray(string);
                Iterable iterableUntil = RangesKt.until(0, a.length());
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil, 10));
                Iterator<Integer> it = iterableUntil.iterator();
                while (it.hasNext()) {
                    int iNextInt = ((IntIterator) it).nextInt();
                    StartCache startCache = INSTANCE;
                    JSONObject jSONObject = a.getJSONObject(iNextInt);
                    Intrinsics.checkNotNullExpressionValue(jSONObject, "getJSONObject(...)");
                    arrayList.add(startCache.gameOf(jSONObject));
                }
                return (List) arrayList;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public static /* synthetic */ void putGames$default(StartCache startCache, Context context, long j, List list, String str, int i, Object obj) {
        if ((i & 8) != 0) {
            str = "games";
        }
        startCache.putGames(context, j, list, str);
    }

    public final void putGames(Context ctx, long uid, List<RobloxCloud.Game> list, String kind) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(list, "list");
        Intrinsics.checkNotNullParameter(kind, "kind");
        JSONArray a = new JSONArray();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            a.put(INSTANCE.gameJson((RobloxCloud.Game) it.next()));
        }
        prefs(ctx).edit().putString(kind + "_" + uid, a.toString()).apply();
    }

    public final Pair<Integer, List<Pair<RobloxCloud.Group, List<RobloxCloud.Game>>>> groups(Context ctx, long uid) {
        String str = "optString(...)";
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        try {
            try {
                String string = prefs(ctx).getString("groups_" + uid, null);
                if (string == null) {
                    return null;
                }
                JSONObject o = new JSONObject(string);
                JSONArray a = o.getJSONArray("list");
                Integer numValueOf = Integer.valueOf(o.optInt("in"));
                Iterable iterableUntil = RangesKt.until(0, a.length());
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil, 10));
                Iterator<Integer> it = iterableUntil.iterator();
                while (it.hasNext()) {
                    JSONObject jSONObject = a.getJSONObject(((IntIterator) it).nextInt());
                    JSONArray jSONArray = jSONObject.getJSONArray("games");
                    long jOptLong = jSONObject.optLong("id");
                    String strOptString = jSONObject.optString("name");
                    Intrinsics.checkNotNullExpressionValue(strOptString, str);
                    String strOptString2 = jSONObject.optString("role");
                    Intrinsics.checkNotNullExpressionValue(strOptString2, str);
                    String str2 = str;
                    RobloxCloud.Group group = new RobloxCloud.Group(jOptLong, strOptString, strOptString2, jSONObject.optInt("rank"));
                    Iterable iterableUntil2 = RangesKt.until(0, jSONArray.length());
                    JSONObject o2 = o;
                    JSONArray a2 = a;
                    Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil2, 10));
                    Iterable iterable = iterableUntil2;
                    Iterator<Integer> it2 = iterable.iterator();
                    while (it2.hasNext()) {
                        int iNextInt = ((IntIterator) it2).nextInt();
                        StartCache startCache = INSTANCE;
                        Iterable iterable2 = iterable;
                        JSONObject jSONObject2 = jSONArray.getJSONObject(iNextInt);
                        Intrinsics.checkNotNullExpressionValue(jSONObject2, "getJSONObject(...)");
                        arrayList2.add(startCache.gameOf(jSONObject2));
                        iterable = iterable2;
                    }
                    arrayList.add(TuplesKt.to(group, (List) arrayList2));
                    a = a2;
                    o = o2;
                    str = str2;
                }
                return TuplesKt.to(numValueOf, (List) arrayList);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public final void putGroups(Context ctx, long uid, int inGroups, List<? extends Pair<RobloxCloud.Group, ? extends List<RobloxCloud.Game>>> list) {
        try {
            Intrinsics.checkNotNullParameter(ctx, "ctx");
            Intrinsics.checkNotNullParameter(list, "list");
            JSONArray a = new JSONArray();
            for (Pair<RobloxCloud.Group, ? extends List<RobloxCloud.Game>> pair : list) {
                RobloxCloud.Group g = pair.component1();
                Iterable iterable = (List) pair.component2();
                JSONObject jSONObjectPut = new JSONObject().put("id", g.getId()).put("name", g.getName()).put("role", g.getRole()).put("rank", g.getRank());
                JSONArray jSONArray = new JSONArray();
                Iterator it = iterable.iterator();
                while (it.hasNext()) {
                    jSONArray.put(INSTANCE.gameJson((RobloxCloud.Game) it.next()));
                }
                Unit unit = Unit.INSTANCE;
                a.put(jSONObjectPut.put("games", jSONArray));
            }
            prefs(ctx).edit().putString("groups_" + uid, new JSONObject().put("in", inGroups).put("list", a).toString()).apply();
        } catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }
}
