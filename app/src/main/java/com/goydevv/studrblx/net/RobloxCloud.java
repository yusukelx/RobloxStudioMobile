package com.goydevv.studrblx.net;

import android.content.Context;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: RobloxCloud.kt */
/* JADX INFO: loaded from: classes6.dex */
public final class RobloxCloud {
    public static final long BASEPLATE_TEMPLATE_PLACE = 95206881;
    public static final RobloxCloud INSTANCE = new RobloxCloud();
    private static final Map<String, Integer> avatarTypeNames = MapsKt.mapOf(TuplesKt.to("MorphToR6", 0), TuplesKt.to("MorphToR15", 1), TuplesKt.to("PlayerChoice", 2));
    private static volatile String csrf;

    /* JADX INFO: compiled from: RobloxCloud.kt */
    public static final class User {
        private final String displayName;
        private final long id;
        private final String name;

        public User(long id, String name, String displayName) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(displayName, "displayName");
            this.id = id;
            this.name = name;
            this.displayName = displayName;
        }

        public final String getDisplayName() {
            return this.displayName;
        }

        public final long getId() {
            return this.id;
        }

        public final String getName() {
            return this.name;
        }
    }

    private RobloxCloud() {
    }

    /* JADX INFO: compiled from: RobloxCloud.kt */
    public static final class Game {
        private final long groupId;
        private final boolean isArchived;
        private final String name;
        private final String owner;
        private final String privacyType;
        private final long rootPlaceId;
        private final long universeId;
        private final String updated;

        public Game(long universeId, String name, long rootPlaceId, String updated, String owner, long groupId, String privacyType, boolean isArchived) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(updated, "updated");
            Intrinsics.checkNotNullParameter(owner, "owner");
            this.universeId = universeId;
            this.name = name;
            this.rootPlaceId = rootPlaceId;
            this.updated = updated;
            this.owner = owner;
            this.groupId = groupId;
            this.privacyType = privacyType != null ? privacyType : "";
            this.isArchived = isArchived;
        }

        public Game(long universeId, String name, long rootPlaceId, String updated, String owner, long groupId) {
            this(universeId, name, rootPlaceId, updated, owner, groupId, "", false);
        }

        public /* synthetic */ Game(long j, String str, long j2, String str2, String str3, long j3, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(j, str, j2, str2, str3, (i & 32) != 0 ? 0L : j3);
        }

        public final long getGroupId() {
            return this.groupId;
        }

        public final String getName() {
            return this.name;
        }

        public final String getOwner() {
            return this.owner;
        }

        public final String getPrivacyType() {
            return this.privacyType;
        }

        public final long getRootPlaceId() {
            return this.rootPlaceId;
        }

        public final long getUniverseId() {
            return this.universeId;
        }

        public final String getUpdated() {
            return this.updated;
        }

        public final boolean isArchived() {
            return this.isArchived;
        }
    }

    /* JADX INFO: compiled from: RobloxCloud.kt */
    public static final class Group {
        private final long id;
        private final String name;
        private final int rank;
        private final String role;

        public Group(long id, String name, String role, int rank) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(role, "role");
            this.id = id;
            this.name = name;
            this.role = role;
            this.rank = rank;
        }

        public final long getId() {
            return this.id;
        }

        public final String getName() {
            return this.name;
        }

        public final int getRank() {
            return this.rank;
        }

        public final String getRole() {
            return this.role;
        }
    }

    /* JADX INFO: compiled from: RobloxCloud.kt */
    public static final class Place {
        private final long id;
        private final String name;
        private final long universeId;

        public Place(long id, String name, long universeId) {
            Intrinsics.checkNotNullParameter(name, "name");
            this.id = id;
            this.name = name;
            this.universeId = universeId;
        }

        public final long getId() {
            return this.id;
        }

        public final String getName() {
            return this.name;
        }

        public final long getUniverseId() {
            return this.universeId;
        }
    }

    /* JADX INFO: compiled from: RobloxCloud.kt */
    public static final class Version {
        private final String created;
        private final int number;
        private boolean published;

        public Version(int number, String created, boolean published) {
            Intrinsics.checkNotNullParameter(created, "created");
            this.number = number;
            this.created = created;
            this.published = published;
        }

        public final String getCreated() {
            return this.created;
        }

        public final int getNumber() {
            return this.number;
        }

        public final boolean getPublished() {
            return this.published;
        }

        public final void setPublished(boolean z) {
            this.published = z;
        }
    }

    public final void resetSession() {
        csrf = null;
    }

    private final String enc(String s) {
        try { return URLEncoder.encode(s, "UTF-8"); } catch (java.io.UnsupportedEncodingException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
    }

    private final Map<String, String> cookieHeader(Context ctx) throws AuthRequiredException {
        String c = RobloxApi.INSTANCE.cookie(ctx);
        if (c.length() == 0) {
            throw new AuthRequiredException("Sign in to your Roblox account first (tap the account button at the top right).");
        }
        return MapsKt.mapOf(TuplesKt.to("Cookie", ".ROBLOSECURITY=" + c));
    }

    public static /* synthetic */ Http.Response authed$default(RobloxCloud robloxCloud, Context context, String str, String str2, byte[] bArr, String str3, int i, Object obj) throws AuthRequiredException {
        if ((i & 4) != 0) {
            str2 = "GET";
        }
        return robloxCloud.authed(context, str, str2, (i & 8) != 0 ? null : bArr, (i & 16) != 0 ? null : str3);
    }

    public final Http.Response authed(Context ctx, String url, String method, byte[] body, String contentType) throws AuthRequiredException {
        Map mapEmptyMap;
        String tok;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(method, "method");
        Map<String, String> mapCookieHeader = cookieHeader(ctx);
        Http http = Http.INSTANCE;
        String str = csrf;
        if (str == null || (mapEmptyMap = MapsKt.mapOf(TuplesKt.to("X-CSRF-TOKEN", str))) == null) {
            mapEmptyMap = MapsKt.emptyMap();
        }
        Http.Response r = Http.call$default(http, url, method, MapsKt.plus(mapCookieHeader, mapEmptyMap), body, contentType, 0, 32, null);
        if (r.getCode() == 403 && (tok = r.header("x-csrf-token")) != null && !Intrinsics.areEqual(tok, csrf)) {
            csrf = tok;
            r = Http.call$default(Http.INSTANCE, url, method, MapsKt.plus(mapCookieHeader, TuplesKt.to("X-CSRF-TOKEN", tok)), body, contentType, 0, 32, null);
        }
        if (r.getCode() == 401) {
            throw new AuthRequiredException("Your Roblox sign-in has expired. Sign in again (account button, top right).");
        }
        return r;
    }

    private final JSONObject json(Context ctx, String url) throws HttpException {
        try {
        Long longOrNull;
        Http.Response r = authed$default(this, ctx, url, null, null, null, 28, null);
        int attempt = 0;
        while (true) {
            if ((r.getCode() != 429 && r.getCode() != 503) || attempt >= 4) {
                break;
            }
            String strHeader = r.header("retry-after");
            long wait = (strHeader == null || (longOrNull = StringsKt.toLongOrNull(strHeader)) == null) ? 600 << attempt : RangesKt.coerceIn(longOrNull.longValue(), 1L, 8L) * 1000;
            try {
                Thread.sleep(wait);
                attempt++;
                r = authed$default(this, ctx, url, null, null, null, 28, null);
            } catch (InterruptedException e) {
            }
        }
        if (!r.getOk()) {
            throw new HttpException(r.getCode(), Http.INSTANCE.describe(r.getCode(), r.getBody()));
        }
        return new JSONObject(r.getText());
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final String str(JSONObject $this$str, String... keys) {
        for (String k : keys) {
            String strOptString = $this$str.optString(k);
            Intrinsics.checkNotNull(strOptString);
            if (!((strOptString.length() > 0) && !Intrinsics.areEqual(strOptString, "null"))) {
                strOptString = null;
            }
            if (strOptString != null) {
                return strOptString;
            }
        }
        return "";
    }

    /* JADX INFO: renamed from: long, reason: not valid java name */
    private final long m316long(JSONObject $this$long, String... keys) {
        for (String k : keys) {
            Long lValueOf = Long.valueOf($this$long.optLong(k));
            if (!(lValueOf.longValue() != 0)) {
                lValueOf = null;
            }
            if (lValueOf != null) {
                return lValueOf.longValue();
            }
        }
        return 0L;
    }

    public static /* synthetic */ List<Game> searchUniverses$default(RobloxCloud robloxCloud, Context context, String str, long j, String str2, long j2, int i, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            str2 = "";
        }
        try { return robloxCloud.searchUniverses(context, str, j, str2, (i2 & 16) != 0 ? 0L : j2, (i2 & 32) != 0 ? 10 : i); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    public final List<Game> searchUniverses(Context ctx, String creatorType, long targetId, String owner, long groupId, int pages) throws HttpException {
        return searchUniverses(ctx, creatorType, targetId, owner, groupId, pages, false);
    }

    public final List<Game> searchUniverses(Context ctx, String creatorType, long targetId, String owner, long groupId, int pages, boolean isArchived) throws HttpException {
        LinkedHashMap out;
        long gid;
        Context ctx2 = ctx;
        String creatorType2 = creatorType;
        Intrinsics.checkNotNullParameter(ctx2, "ctx");
        String str = "creatorType";
        Intrinsics.checkNotNullParameter(creatorType2, "creatorType");
        Intrinsics.checkNotNullParameter(owner, "owner");
        LinkedHashMap out2 = new LinkedHashMap();
        int page = 0;
        while (page < pages) {
            JSONObject j = json(ctx2, "https://apis.roblox.com/universes/v1/search?isTemplates=false&isArchived=" + isArchived + "&creatorTargetId=" + targetId + "&searchKey=&sortOrder=Desc&search=&creatorType=" + creatorType2 + "&pageSize=50&sortParam=LastUpdated&surface=StudioStartPage&PageIndex=" + page);
            JSONArray arr = j.optJSONArray("data");
            if (arr == null && (arr = j.optJSONArray("Data")) == null) {
                arr = new JSONArray();
            }
            int i = 0;
            int length = arr.length();
            while (i < length) {
                JSONObject o = arr.optJSONObject(i);
                if (o != null) {
                    long id = m316long(o, "id", "Id", "universeId");
                    boolean archived = o.optBoolean("isArchived") || o.optBoolean("IsArchived");
                    if (id != 0 && (isArchived || !archived)) {
                        if (groupId != 0) {
                            gid = groupId;
                        } else {
                            gid = StringsKt.equals(str(o, str, "CreatorType"), "Group", true) ? m316long(o, "creatorTargetId", "CreatorTargetId") : 0L;
                        }
                        LinkedHashMap linkedHashMap = out2;
                        Long lValueOf = Long.valueOf(id);
                        String str2 = str(o, "name", "Name");
                        long jM316long = m316long(o, "rootPlaceId", "RootPlaceId");
                        String str3 = str(o, "updated", "Updated");
                        String str4 = str(o, "creatorName", "CreatorName");
                        if (str4.length() == 0) {
                            str4 = owner;
                        }
                        String privacy = str(o, "privacyType", "PrivacyType");
                        linkedHashMap.put(lValueOf, new Game(id, str2, jM316long, str3, str4, gid, privacy, archived || isArchived));
                    }
                }
                i++;
            }
            String str5 = str;
            out = out2;
            if (arr.length() >= 50) {
                page++;
                ctx2 = ctx;
                creatorType2 = creatorType;
                out2 = out;
                str = str5;
            } else {
                Collection collectionValues = out.values();
                Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
                return CollectionsKt.toList(collectionValues);
            }
        }
        out = out2;
        Collection collectionValues2 = out.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues2, "<get-values>(...)");
        return CollectionsKt.toList(collectionValues2);
    }

    public final List<Game> sharedGames(Context ctx, User user) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(user, "user");
        return searchUniverses$default(this, ctx, "Team", user.getId(), null, 0L, 0, 56, null);
    }

    private final String nextCursor(JSONObject j) {
        String strOptString = j.optString("nextPageCursor");
        Intrinsics.checkNotNull(strOptString);
        if ((strOptString.length() > 0) && !Intrinsics.areEqual(strOptString, "null")) {
            return strOptString;
        }
        return null;
    }

    public final User currentUser(Context ctx) throws JSONException, HttpException {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        JSONObject j = json(ctx, "https://users.roblox.com/v1/users/authenticated");
        long j2 = j.getLong("id");
        String strOptString = j.optString("name");
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        String strOptString2 = j.optString("displayName");
        Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
        User u = new User(j2, strOptString, strOptString2);
        RobloxApi.INSTANCE.setUser(ctx, u.getId(), u.getName(), u.getDisplayName());
        Accounts.INSTANCE.upsert(ctx, new Accounts.Account(u.getId(), u.getName(), u.getDisplayName(), RobloxApi.INSTANCE.cookie(ctx)));
        return u;
    }

    public final User cachedUser(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        if ((RobloxApi.INSTANCE.cookie(ctx).length() == 0) || RobloxApi.INSTANCE.userId(ctx) == 0) {
            return null;
        }
        return new User(RobloxApi.INSTANCE.userId(ctx), RobloxApi.INSTANCE.userName(ctx), RobloxApi.INSTANCE.displayName(ctx));
    }

    private final void parseUniverses(JSONArray arr, String owner, long groupId, LinkedHashMap<Long, Game> out) {
        parseUniverses(arr, owner, groupId, out, false);
    }

    private final void parseUniverses(JSONArray arr, String owner, long groupId, LinkedHashMap<Long, Game> out, boolean includeArchived) {
        try {
            int length = arr.length();
            for (int i = 0; i < length; i++) {
                JSONObject o = arr.getJSONObject(i);
                long id = o.optLong("id");
                long jLongValue = 0;
                boolean archived = o.optBoolean("isArchived") || o.optBoolean("IsArchived");
                if (id != 0 && (includeArchived || !archived)) {
                    Long lValueOf = Long.valueOf(o.optLong("rootPlaceId"));
                    if (!(lValueOf.longValue() != 0)) {
                        lValueOf = null;
                    }
                    if (lValueOf != null) {
                        jLongValue = lValueOf.longValue();
                    } else {
                        JSONObject jSONObjectOptJSONObject = o.optJSONObject("rootPlace");
                        Long lValueOf2 = jSONObjectOptJSONObject != null ? Long.valueOf(jSONObjectOptJSONObject.optLong("id")) : null;
                        if (lValueOf2 != null) {
                            jLongValue = lValueOf2.longValue();
                        }
                    }
                    long root = jLongValue;
                    Long lValueOf3 = Long.valueOf(id);
                    String strOptString = o.optString("name");
                    Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                    String strOptString2 = o.optString("updated", o.optString("updatedAt"));
                    Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                    String privacy = str(o, "privacyType", "PrivacyType");
                    out.put(lValueOf3, new Game(id, strOptString, root, strOptString2, owner, groupId, privacy, archived));
                }
            }
        } catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX WARN: Code duplicated, block: B:30:0x0085  */
    /* JADX WARN: Code duplicated, block: B:33:0x00a5 A[Catch: Exception -> 0x00ab, AuthRequiredException -> 0x00ae, TRY_ENTER, TRY_LEAVE, TryCatch #9 {AuthRequiredException -> 0x00ae, Exception -> 0x00ab, blocks: (B:28:0x006a, B:33:0x00a5), top: B:83:0x006a }] */
    /* JADX WARN: Code duplicated, block: B:43:0x00c2 A[LOOP:1: B:24:0x0062->B:43:0x00c2, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:60:0x0102 A[Catch: Exception -> 0x0112, AuthRequiredException -> 0x0117, TryCatch #7 {AuthRequiredException -> 0x0117, Exception -> 0x0112, blocks: (B:58:0x00db, B:60:0x0102, B:61:0x0107), top: B:87:0x00db }] */
    /* JADX WARN: Code duplicated, block: B:87:0x00db A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:91:0x00d5 A[EDGE_INSN: B:91:0x00d5->B:56:0x00d5 BREAK  A[LOOP:1: B:24:0x0062->B:43:0x00c2], SYNTHETIC] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    public final List<Game> userGames(Context ctx, User user) throws Exception {
        Context context = null;
        LinkedHashMap<Long, Game> linkedHashMap;
        JSONArray jSONArrayOptJSONArray;
        String str;
        Exception e;
        JSONArray jSONArrayOptJSONArray2;
        String strNextCursor;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(user, "user");
        LinkedHashMap<Long, Game> linkedHashMap2 = new LinkedHashMap<>();
        Exception err = null;
        try {
            context = ctx;
            try {
                for (Game g : searchUniverses$default(this, ctx, "User", user.getId(), user.getName(), 0L, 0, 48, null)) {
                    linkedHashMap2.put(Long.valueOf(g.getUniverseId()), g);
                }
            } catch (AuthRequiredException e2) {
                e = e2;
                throw e;
            } catch (Exception e3) {
                e = e3;
                err = e;
            }
        } catch (AuthRequiredException e4) {
            e = e4;
        } catch (Exception e5) {
            e = e5;
            context = ctx;
        }
        if (!linkedHashMap2.isEmpty()) {
            linkedHashMap = linkedHashMap2;
        } else {
            String cursor = null;
            int page = 0;
            while (true) {
                if (page >= 10) {
                    linkedHashMap = linkedHashMap2;
                    break;
                }
                if (cursor == null) {
                    str = "";
                    JSONObject j = json(context, "https://develop.roblox.com/v1/user/universes?isArchived=false&limit=50&sortOrder=Desc" + str);
                    jSONArrayOptJSONArray2 = j.optJSONArray("data");
                    if (jSONArrayOptJSONArray2 == null) {
                        jSONArrayOptJSONArray2 = new JSONArray();
                    }
                    linkedHashMap = linkedHashMap2;
                    parseUniverses(jSONArrayOptJSONArray2, user.getName(), 0L, linkedHashMap);
                    strNextCursor = nextCursor(j);
                    if (strNextCursor == null) {
                        break;
                    }
                    cursor = strNextCursor;
                    page++;
                    linkedHashMap2 = linkedHashMap;
                } else {
                    try {
                        str = "&cursor=" + INSTANCE.enc(cursor);
                        if (str == null) {
                            str = "";
                        }
                        try {
                            JSONObject j2 = json(context, "https://develop.roblox.com/v1/user/universes?isArchived=false&limit=50&sortOrder=Desc" + str);
                            jSONArrayOptJSONArray2 = j2.optJSONArray("data");
                            if (jSONArrayOptJSONArray2 == null) {
                                jSONArrayOptJSONArray2 = new JSONArray();
                            }
                            linkedHashMap = linkedHashMap2;
                            try {
                                parseUniverses(jSONArrayOptJSONArray2, user.getName(), 0L, linkedHashMap);
                                strNextCursor = nextCursor(j2);
                                if (strNextCursor == null) {
                                    break;
                                }
                                cursor = strNextCursor;
                                page++;
                                linkedHashMap2 = linkedHashMap;
                            } catch (AuthRequiredException e6) {
                                throw e6;
                            } catch (Exception e7) {
                                e = e7;
                                err = e;
                            }
                        } catch (AuthRequiredException e8) {
                            throw e8;
                        } catch (Exception e9) {
                            e = e9;
                            linkedHashMap = linkedHashMap2;
                        }
                    } catch (AuthRequiredException e10) {
                        throw e10;
                    } catch (Exception e11) {
                        e = e11;
                        linkedHashMap = linkedHashMap2;
                        err = e;
                        if (linkedHashMap.isEmpty()) {
                            try {
                                jSONArrayOptJSONArray = json(context, "https://games.roblox.com/v2/users/" + user.getId() + "/games?accessFilter=1&limit=50&sortOrder=Desc").optJSONArray("data");
                                if (jSONArrayOptJSONArray == null) {
                                    jSONArrayOptJSONArray = new JSONArray();
                                }
                                parseUniverses(jSONArrayOptJSONArray, user.getName(), 0L, linkedHashMap);
                            } catch (AuthRequiredException e12) {
                                throw e12;
                            } catch (Exception e13) {
                                if (err == null) {
                                    err = e13;
                                }
                            }
                        }
                        if (!linkedHashMap.isEmpty()) {
                        }
                        Collection<Game> collectionValues = linkedHashMap.values();
                        Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
                        return CollectionsKt.toList(collectionValues);
                    }
                }
            }
        }
        if (linkedHashMap.isEmpty()) {
            jSONArrayOptJSONArray = json(context, "https://games.roblox.com/v2/users/" + user.getId() + "/games?accessFilter=1&limit=50&sortOrder=Desc").optJSONArray("data");
            if (jSONArrayOptJSONArray == null) {
                jSONArrayOptJSONArray = new JSONArray();
            }
            parseUniverses(jSONArrayOptJSONArray, user.getName(), 0L, linkedHashMap);
        }
        if (linkedHashMap.isEmpty() && err != null) {
            throw err;
        }
        Collection<Game> collectionValues2 = linkedHashMap.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues2, "<get-values>(...)");
        return CollectionsKt.toList(collectionValues2);
    }

    public final List<Game> archivedGames(Context ctx, User user) throws Exception {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(user, "user");
        LinkedHashMap<Long, Game> out = new LinkedHashMap<>();
        Exception err = null;
        try {
            for (Game g : searchUniverses(ctx, "User", user.getId(), user.getName(), 0L, 10, true)) {
                out.put(Long.valueOf(g.getUniverseId()), g);
            }
        } catch (AuthRequiredException e) {
            throw e;
        } catch (Exception e2) {
            err = e2;
        }
        if (out.isEmpty()) {
            String cursor = null;
            int page = 0;
            while (page < 10) {
                try {
                    String suffix = cursor == null ? "" : "&cursor=" + enc(cursor);
                    JSONObject j = json(ctx, "https://develop.roblox.com/v1/user/universes?isArchived=true&limit=50&sortOrder=Desc" + suffix);
                    JSONArray arr = j.optJSONArray("data");
                    if (arr == null) {
                        arr = new JSONArray();
                    }
                    parseUniverses(arr, user.getName(), 0L, out, true);
                    String strNextCursor = nextCursor(j);
                    if (strNextCursor == null) {
                        break;
                    }
                    cursor = strNextCursor;
                    page++;
                } catch (AuthRequiredException e3) {
                    throw e3;
                } catch (Exception e4) {
                    if (err == null) {
                        err = e4;
                    }
                    break;
                }
            }
        }
        if (out.isEmpty() && err != null) {
            throw err;
        }
        Collection<Game> values = out.values();
        Intrinsics.checkNotNullExpressionValue(values, "<get-values>(...)");
        return CollectionsKt.toList(values);
    }

    public final List<Group> groups(Context ctx, User user) throws JSONException, HttpException {
        String strOptString;
        Group group;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(user, "user");
        JSONObject g = json(ctx, "https://groups.roblox.com/v1/users/" + user.getId() + "/groups/roles?includeLocked=true");
        JSONArray arr = g.optJSONArray("data");
        if (arr == null) {
            arr = new JSONArray();
        }
        Iterable iterableUntil = RangesKt.until(0, arr.length());
        Collection arrayList = new ArrayList();
        Iterator<Integer> it = iterableUntil.iterator();
        while (it.hasNext()) {
            JSONObject jSONObject = arr.getJSONObject(((IntIterator) it).nextInt());
            JSONObject g2 = g;
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("group");
            if (jSONObjectOptJSONObject == null) {
                group = null;
            } else {
                JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject("role");
                long jOptLong = jSONObjectOptJSONObject.optLong("id");
                String strOptString2 = jSONObjectOptJSONObject.optString("name");
                Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                if (jSONObjectOptJSONObject2 == null || (strOptString = jSONObjectOptJSONObject2.optString("name")) == null) {
                    strOptString = "";
                }
                group = new Group(jOptLong, strOptString2, strOptString, jSONObjectOptJSONObject2 != null ? jSONObjectOptJSONObject2.optInt("rank") : 0);
            }
            if (group != null) {
                arrayList.add(group);
            }
            g = g2;
        }
        return CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.goydevv.studrblx.net.RobloxCloud$groups$$inlined$sortedByDescending$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                return ComparisonsKt.compareValues(Integer.valueOf(((RobloxCloud.Group) t2).getRank()), Integer.valueOf(((RobloxCloud.Group) t).getRank()));
            }
        });
    }

    public final List<Group> studioGroups(Context ctx) throws AuthRequiredException {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        LinkedHashMap out = new LinkedHashMap();
        Ref.BooleanRef any = new Ref.BooleanRef();
        studioGroups$read(ctx, any, out, "https://apis.roblox.com/creator-home-api/v1/groups?surface=StudioStartPage");
        studioGroups$read(ctx, any, out, "https://develop.roblox.com/v1/user/groups/canmanage");
        if (!any.element) {
            return null;
        }
        Collection collectionValues = out.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
        return CollectionsKt.toList(collectionValues);
    }

    private static final void studioGroups$read(Context $ctx, Ref.BooleanRef any, LinkedHashMap<Long, Group> linkedHashMap, String url) throws AuthRequiredException {
        try {
            try {
                JSONObject j = INSTANCE.json($ctx, url);
                JSONArray arr = j.optJSONArray("data");
                if (arr == null && (arr = j.optJSONArray("groups")) == null) {
                    return;
                }
                try {
                    any.element = true;
                    int length = arr.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject o = arr.optJSONObject(i);
                        if (o != null) {
                            long id = INSTANCE.m316long(o, "id", "Id", "groupId");
                            if (id != 0 && !linkedHashMap.containsKey(Long.valueOf(id))) {
                                linkedHashMap.put(Long.valueOf(id), new Group(id, INSTANCE.str(o, "name", "Name"), "", 0));
                            }
                        }
                    }
                } catch (AuthRequiredException e) {
                } catch (Exception e2) {
                }
            } catch (AuthRequiredException e3) {
            } catch (Exception e4) {
            }
        } catch (AuthRequiredException e5) {
            throw e5;
        } catch (Exception e6) {
        }
    }

    /* JADX WARN: Code duplicated, block: B:50:0x0160  */
    public final GroupList allGroups(Context ctx, User user) throws Exception {
        Iterable<Group> iterableEmptyList;
        Set setEmptySet;
        Group group;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(user, "user");
        Exception rolesError = null;
        try {
            iterableEmptyList = groups(ctx, user);
        } catch (AuthRequiredException e) {
            throw e;
        } catch (Exception e2) {
            rolesError = e2;
            iterableEmptyList = CollectionsKt.emptyList();
        }
        Iterable<Group> iterableStudioGroups = studioGroups(ctx);
        if (rolesError != null) {
            Exception exc = rolesError;
            if (iterableStudioGroups == null) {
                throw exc;
            }
        }
        Iterable iterable = iterableEmptyList;
        Map byId = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterable, 10)), 16));
        for (Object obj : iterable) {
            byId.put(Long.valueOf(((Group) obj).getId()), obj);
        }
        LinkedHashMap out = new LinkedHashMap();
        if (iterableStudioGroups != null) {
            for (Group group2 : iterableStudioGroups) {
                LinkedHashMap linkedHashMap = out;
                Long lValueOf = Long.valueOf(group2.getId());
                Group group3 = (Group) byId.get(Long.valueOf(group2.getId()));
                if (group3 != null) {
                    long id = group3.getId();
                    String name = group3.getName();
                    if (name.length() == 0) {
                        name = group2.getName();
                    }
                    group = new Group(id, name, group3.getRole(), group3.getRank());
                } else {
                    group = group2;
                }
                linkedHashMap.put(lValueOf, group);
            }
        }
        for (Group group4 : iterableEmptyList) {
            if (!out.containsKey(Long.valueOf(group4.getId()))) {
                out.put(Long.valueOf(group4.getId()), group4);
            }
        }
        Iterable iterableValues = out.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        List list = CollectionsKt.toList(iterableValues);
        if (iterableStudioGroups != null) {
            Iterable iterable2 = iterableStudioGroups;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
            Iterator it = iterable2.iterator();
            while (it.hasNext()) {
                arrayList.add(Long.valueOf(((Group) it.next()).getId()));
            }
            setEmptySet = CollectionsKt.toSet((List) arrayList);
            if (setEmptySet == null) {
                setEmptySet = SetsKt.emptySet();
            }
        } else {
            setEmptySet = SetsKt.emptySet();
        }
        return new GroupList(list, setEmptySet);
    }

    /* JADX INFO: compiled from: RobloxCloud.kt */
    public static final class GroupList {
        private final List<Group> groups;
        private final Set<Long> studioListed;

        public GroupList(List<Group> groups, Set<Long> studioListed) {
            Intrinsics.checkNotNullParameter(groups, "groups");
            Intrinsics.checkNotNullParameter(studioListed, "studioListed");
            this.groups = groups;
            this.studioListed = studioListed;
        }

        public final List<Group> getGroups() {
            return this.groups;
        }

        public final Set<Long> getStudioListed() {
            return this.studioListed;
        }
    }

    public final List<Game> editableGroupGames(Context ctx, Group group, boolean trusted) throws AuthRequiredException {
        Context ctx2;
        List<Game> listSearchUniverses = null;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(group, "group");
        try {
            ctx2 = ctx;
            try {
                listSearchUniverses = searchUniverses(ctx2, "Group", group.getId(), group.getName(), group.getId(), 6);
            } catch (AuthRequiredException e) {
                throw e;
            } catch (Exception e2) {
                listSearchUniverses = null;
            }
        } catch (AuthRequiredException e3) {
            throw e3;
        } catch (Exception e4) {
            ctx2 = ctx;
        }
        if (listSearchUniverses == null || listSearchUniverses.isEmpty()) {
            return filterEditable(ctx2, groupGames(ctx2, group));
        }
        return trusted ? listSearchUniverses : filterEditable(ctx2, listSearchUniverses);
    }

    /* JADX WARN: Code duplicated, block: B:9:0x003c  */
    public final List<Game> groupGames(Context ctx, Group group) throws AuthRequiredException {
        String str;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(group, "group");
        LinkedHashMap<Long, Game> linkedHashMap = new LinkedHashMap<>();
        String cursor = null;
        for (int page = 0; page < 6; page++) {
            try {
                long id = group.getId();
                if (cursor != null) {
                    str = "&cursor=" + INSTANCE.enc(cursor);
                    if (str == null) {
                        str = "";
                    }
                } else {
                    str = "";
                }
                JSONObject j = json(ctx, "https://develop.roblox.com/v1/groups/" + id + "/universes?isArchived=false&limit=50&sortOrder=Desc" + str);
                JSONArray jSONArrayOptJSONArray = j.optJSONArray("data");
                if (jSONArrayOptJSONArray == null) {
                    jSONArrayOptJSONArray = new JSONArray();
                }
                parseUniverses(jSONArrayOptJSONArray, group.getName(), group.getId(), linkedHashMap);
                String strNextCursor = nextCursor(j);
                if (strNextCursor == null) {
                    break;
                }
                cursor = strNextCursor;
            } catch (AuthRequiredException e) {
                throw e;
            } catch (Exception e2) {
                try {
                    JSONArray jSONArrayOptJSONArray2 = json(ctx, "https://games.roblox.com/v2/groups/" + group.getId() + "/gamesV2?accessFilter=1&limit=50&sortOrder=Desc").optJSONArray("data");
                    if (jSONArrayOptJSONArray2 == null) {
                        jSONArrayOptJSONArray2 = new JSONArray();
                    }
                    parseUniverses(jSONArrayOptJSONArray2, group.getName(), group.getId(), linkedHashMap);
                } catch (AuthRequiredException e3) {
                    throw e3;
                } catch (Exception e4) {
                }
            }
        }
        Collection<Game> collectionValues = linkedHashMap.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
        return CollectionsKt.toList(collectionValues);
    }

    public final Set<Long> editable(Context ctx, List<Long> universeIds) throws JSONException, IOException {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(universeIds, "universeIds");
        HashSet out = new HashSet();
        boolean understood = false;
        for (Object _cObj : CollectionsKt.chunked(CollectionsKt.distinct(universeIds), 50)) {
            List chunk = (List) _cObj;
            if (!chunk.isEmpty()) {
                String url = "https://develop.roblox.com/v1/universes/multiget/permissions?" + CollectionsKt.joinToString(chunk, "&", "", "", -1, "...", new Function1() { // from class: com.goydevv.studrblx.net.RobloxCloud$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return RobloxCloud.editable$lambda$19(((Long) obj).longValue());
                    }
                });
                JSONObject j = json(ctx, url);
                JSONArray arr = j.optJSONArray("data");
                if (arr != null) {
                    int length = arr.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject o = arr.getJSONObject(i);
                        if (o.has("canManage") || o.has("canCloudEdit")) {
                            understood = true;
                        }
                        if (o.optBoolean("canManage") || o.optBoolean("canCloudEdit")) {
                            out.add(Long.valueOf(o.optLong("universeId")));
                        }
                    }
                }
            }
        }
        if (!understood) {
            throw new IOException("Unrecognized permissions response");
        }
        return out;
    }

    static final CharSequence editable$lambda$19(long it) {
        return "ids=" + it;
    }

    public static /* synthetic */ List<Game> games$default(RobloxCloud robloxCloud, Context context, User user, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = true;
        }
        return robloxCloud.games(context, user, z);
    }

    public final List<Game> games(final Context ctx, final User user, boolean includeGroups) {
        Future futureSubmit;
        GroupList groupList;
        List listEmptyList;
        List listEmptyList2;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(user, "user");
        ExecutorService pool = Executors.newFixedThreadPool(4);
        if (includeGroups) {
            try {
                futureSubmit = pool.submit(new Callable() { // from class: com.goydevv.studrblx.net.RobloxCloud$$ExternalSyntheticLambda1
                    @Override // java.util.concurrent.Callable
                    public final Object call() {
                        return RobloxCloud.games$lambda$20(ctx, user);
                    }
                });
            } catch (Throwable th) {
                pool.shutdown();
                throw th;
            }
        } else {
            futureSubmit = null;
        }
        Future groupsF = futureSubmit;
        Future sharedF = pool.submit(new Callable() { // from class: com.goydevv.studrblx.net.RobloxCloud$$ExternalSyntheticLambda2
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return RobloxCloud.games$lambda$21(ctx, user);
            }
        });
        ArrayList out; try { out = new ArrayList(userGames(ctx, user)); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
        if (groupsF != null) {
            try {
                try { groupList = (GroupList) groupsF.get(); } catch (InterruptedException _e) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_e); }
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                AuthRequiredException authRequiredException = cause instanceof AuthRequiredException ? (AuthRequiredException) cause : null;
                if (authRequiredException != null) {
                    throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(authRequiredException);
                }
                groupList = null;
            }
        } else {
            groupList = null;
        }
        final GroupList gl = groupList;
        Iterable groups = gl != null ? gl.getGroups() : null;
        if (groups == null) {
            groups = CollectionsKt.emptyList();
        }
        Iterable<Group> iterable = groups;
        int i = 0;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (final Group group : iterable) {
            arrayList.add(pool.submit(new Callable() { // from class: com.goydevv.studrblx.net.RobloxCloud$$ExternalSyntheticLambda3
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return RobloxCloud.games$lambda$24$lambda$23(ctx, group, gl);
                }
            }));
        }
        List<Future> futures = (List) arrayList;
        try {
            listEmptyList = (List) sharedF.get();
        } catch (Exception e2) {
            listEmptyList = CollectionsKt.emptyList();
        }
        out.addAll(listEmptyList);
        for (Future f : futures) {
            try {
                listEmptyList2 = (List) f.get();
            } catch (Exception e3) {
                listEmptyList2 = CollectionsKt.emptyList();
            }
            out.addAll(listEmptyList2);
        }
        HashSet hashSet = new HashSet();
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : out) {
            if (hashSet.add(Long.valueOf(((Game) obj).getUniverseId()))) {
                arrayList2.add(obj);
            }
        }
        ArrayList arrayList3 = arrayList2;
        pool.shutdown();
        return arrayList3;
    }

    static final GroupList games$lambda$20(Context $ctx, User $user) {
        try {
        try {
            return INSTANCE.allGroups($ctx, $user);
        } catch (AuthRequiredException e) {
            throw e;
        } catch (Exception e2) {
            return new GroupList(CollectionsKt.emptyList(), SetsKt.emptySet());
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final List games$lambda$21(Context $ctx, User $user) {
        try {
            return INSTANCE.sharedGames($ctx, $user);
        } catch (Exception e) {
            return CollectionsKt.emptyList();
        }
    }

    static final List games$lambda$24$lambda$23(Context $ctx, Group $g, GroupList $gl) {
        try {
            RobloxCloud robloxCloud = INSTANCE;
            Intrinsics.checkNotNull($gl);
            return robloxCloud.editableGroupGames($ctx, $g, $gl.getStudioListed().contains(Long.valueOf($g.getId())));
        } catch (Exception e) {
            return CollectionsKt.emptyList();
        }
    }

    public static /* synthetic */ Pair createGame$default(RobloxCloud robloxCloud, Context context, String str, long j, String str2, int i, Object obj) {
        if ((i & 4) != 0) {
            j = 0;
        }
        long j2 = j;
        if ((i & 8) != 0) {
            str2 = "";
        }
        try { return robloxCloud.createGame(context, str, j2, str2); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    public final Pair<Game, Place> createGame(Context ctx, String name, long groupId, String description) throws IOException {
        try {
        JSONObject jSONObject;
        long jOptLong = 0L;
        long universeId;
        Context context = null;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(description, "description");
        String url = "https://apis.roblox.com/universes/v1/universes/create" + (groupId != 0 ? "?groupId=" + groupId : "");
        String string = new JSONObject().put("templatePlaceId", BASEPLATE_TEMPLATE_PLACE).toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        byte[] body = string.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(body, "getBytes(...)");
        Http.Response r = authed(ctx, url, "POST", body, "application/json");
        if (!r.getOk()) {
            throw new HttpException(r.getCode(), "Couldn't create the experience: " + Http.INSTANCE.describe(r.getCode(), r.getBody()));
        }
        try {
            jSONObject = new JSONObject(r.getText());
        } catch (Exception e) {
            jSONObject = new JSONObject();
        }
        JSONObject j = jSONObject;
        long universeId2 = createGame$find(j, "universeId", "UniverseId", "id");
        if (universeId2 == 0) {
            throw new IOException("Roblox didn't say which experience it created.");
        }
        boolean z = false;
        long placeId = createGame$find(j, "rootPlaceId", "RootPlaceId", "placeId", "rootPlace");
        if (placeId == 0) {
            try {
                try {
                    jOptLong = json(ctx, "https://develop.roblox.com/v1/universes/" + universeId2).optLong("rootPlaceId");
                } catch (Exception e2) {
                    jOptLong = 0;
                }
            } catch (Exception e3) {
            }
            placeId = jOptLong;
        }
        long placeId2 = placeId;
        if (placeId2 == 0) {
            throw new IOException("The experience was created but its start place wasn't found.");
        }
        try {
            try {
                try {
                    JSONObject cfg = new JSONObject().put("name", name);
                    if (description.length() > 0) {
                        z = true;
                    }
                    if (z) {
                        try {
                            cfg.put("description", description);
                            String str = "https://develop.roblox.com/v2/universes/" + universeId2 + "/configuration";
                            universeId = universeId2;
                            try {
                                String string2 = cfg.toString();
                                Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
                                byte[] bytes = string2.getBytes(Charsets.UTF_8);
                                Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                                try {
                                    authed(ctx, str, "PATCH", bytes, "application/json");
                                } catch (AuthRequiredException e4) {
                                    throw e4;
                                } catch (Exception e5) {
                                }
                            } catch (AuthRequiredException e6) {
                                throw e6;
                            } catch (Exception e7) {
                            }
                        } catch (AuthRequiredException e8) {
                            throw e8;
                        } catch (Exception e9) {
                            universeId = universeId2;
                        }
                    } else {
                        String str2 = "https://develop.roblox.com/v2/universes/" + universeId2 + "/configuration";
                        universeId = universeId2;
                        String string3 = cfg.toString();
                        Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
                        byte[] bytes2 = string3.getBytes(Charsets.UTF_8);
                        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
                        authed(ctx, str2, "PATCH", bytes2, "application/json");
                    }
                } catch (AuthRequiredException e10) {
                    throw e10;
                }
            } catch (AuthRequiredException e11) {
                throw e11;
            }
        } catch (Exception e12) {
            universeId = universeId2;
        }
        try {
            try {
                try {
                    String str3 = "https://develop.roblox.com/v2/places/" + placeId2;
                    String string4 = new JSONObject().put("name", name).toString();
                    Intrinsics.checkNotNullExpressionValue(string4, "toString(...)");
                    byte[] bytes3 = string4.getBytes(Charsets.UTF_8);
                    Intrinsics.checkNotNullExpressionValue(bytes3, "getBytes(...)");
                    try {
                        authed(ctx, str3, "PATCH", bytes3, "application/json");
                        context = ctx;
                    } catch (AuthRequiredException e13) {
                        throw e13;
                    } catch (Exception e14) {
                        context = ctx;
                    }
                } catch (AuthRequiredException e15) {
                    throw e15;
                }
            } catch (AuthRequiredException e16) {
                throw e16;
            }
        } catch (Exception e17) {
            context = ctx;
        }
        return TuplesKt.to(new Game(universeId, name, placeId2, "", groupId != 0 ? "Group" : RobloxApi.INSTANCE.userName(context), groupId), new Place(placeId2, name, universeId));
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private static final long createGame$find(JSONObject o, String... keys) {
        for (String k : keys) {
            long v = o.optLong(k);
            if (v != 0) {
                return v;
            }
        }
        return 0L;
    }

    /* JADX INFO: compiled from: RobloxCloud.kt */
    public static final class AvatarSettings {
        private final Integer collision;
        private final Map<String, Float> max;
        private final Map<String, Float> min;
        private final int type;

        public AvatarSettings(int type, Integer collision, Map<String, Float> min, Map<String, Float> max) {
            Intrinsics.checkNotNullParameter(min, "min");
            Intrinsics.checkNotNullParameter(max, "max");
            this.type = type;
            this.collision = collision;
            this.min = min;
            this.max = max;
        }

        public final Integer getCollision() {
            return this.collision;
        }

        public final Map<String, Float> getMax() {
            return this.max;
        }

        public final Map<String, Float> getMin() {
            return this.min;
        }

        public final int getType() {
            return this.type;
        }
    }

    public final AvatarSettings avatarSettings(Context ctx, long universeId) throws HttpException {
        int i = 0;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        JSONObject j = json(ctx, "https://develop.roblox.com/v1/universes/" + universeId + "/configuration");
        Integer num = avatarTypeNames.get(j.optString("universeAvatarType"));
        Integer collision = null;
        if (num == null) {
            return null;
        }
        int type = num.intValue();
        String strOptString = j.optString("universeCollisionType");
        if (!Intrinsics.areEqual(strOptString, "OuterBox")) {
            i = Intrinsics.areEqual(strOptString, "InnerBox") ? 1 : 0;
            return new AvatarSettings(type, collision, avatarSettings$scales(j.optJSONObject("universeAvatarMinScales")), avatarSettings$scales(j.optJSONObject("universeAvatarMaxScales")));
        }
        collision = Integer.valueOf(i);
        return new AvatarSettings(type, collision, avatarSettings$scales(j.optJSONObject("universeAvatarMinScales")), avatarSettings$scales(j.optJSONObject("universeAvatarMaxScales")));
    }

    private static final Map<String, Float> avatarSettings$scales(JSONObject o) {
        if (o == null) {
            return MapsKt.emptyMap();
        }
        HashMap out = new HashMap();
        for (String k : CollectionsKt.listOf(new String[]{"height", "width", "head", "bodyType", "proportion"})) {
            if (o.has(k)) {
                out.put(k, Float.valueOf((float) o.optDouble(k)));
            }
        }
        return out;
    }

    public final void setAvatarSettings(Context ctx, long universeId, AvatarSettings s) throws JSONException, AuthRequiredException, HttpException {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(s, "s");
        JSONObject jSONObject = new JSONObject();
        for (Object obj : avatarTypeNames.entrySet()) {
            if (((Number) ((Map.Entry) obj).getValue()).intValue() == s.getType()) {
                JSONObject body = jSONObject.put("universeAvatarType", ((Map.Entry) obj).getKey());
                Integer collision = s.getCollision();
                if (collision != null) {
                    body.put("universeCollisionType", collision.intValue() == 1 ? "InnerBox" : "OuterBox");
                }
                if (!s.getMin().isEmpty()) {
                    body.put("universeAvatarMinScales", setAvatarSettings$scales$30(s.getMin()));
                }
                if (!s.getMax().isEmpty()) {
                    body.put("universeAvatarMaxScales", setAvatarSettings$scales$30(s.getMax()));
                }
                String string = body.toString();
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                byte[] bytes = string.getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                Http.Response r = authed(ctx, "https://develop.roblox.com/v2/universes/" + universeId + "/configuration", "PATCH", bytes, "application/json");
                if (!r.getOk()) {
                    throw new HttpException(r.getCode(), Http.INSTANCE.describe(r.getCode(), r.getBody()));
                }
                return;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    private static final JSONObject setAvatarSettings$scales$30(Map<String, Float> map) {
        try {
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            jSONObject.put(entry.getKey(), entry.getValue().floatValue());
        }
        if (jSONObject.has("width")) {
            jSONObject.put("depth", jSONObject.getDouble("width"));
        }
        return jSONObject;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    public final List<Game> filterEditable(Context ctx, List<Game> list) throws AuthRequiredException {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(list, "list");
        if (list.isEmpty()) {
            return list;
        }
        try {
            List<Game> list2 = list;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
            Iterator it = list2.iterator();
            while (it.hasNext()) {
                arrayList.add(Long.valueOf(((Game) it.next()).getUniverseId()));
            }
            Set<Long> setEditable = editable(ctx, (List) arrayList);
            Collection arrayList2 = new ArrayList();
            for (Object obj : list) {
                if (setEditable.contains(Long.valueOf(((Game) obj).getUniverseId()))) {
                    arrayList2.add(obj);
                }
            }
            return (List) arrayList2;
        } catch (AuthRequiredException e) {
            throw e;
        } catch (Exception e2) {
            return list;
        }
    }

    /* JADX WARN: Code duplicated, block: B:9:0x003f  */
    public final List<Place> places(Context ctx, final Game game) throws AuthRequiredException {
        String str;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(game, "game");
        ArrayList out = new ArrayList();
        String cursor = null;
        for (int page = 0; page < 5; page++) {
            try {
                long universeId = game.getUniverseId();
                if (cursor != null) {
                    str = "&cursor=" + INSTANCE.enc(cursor);
                    if (str == null) {
                        str = "";
                    }
                } else {
                    str = "";
                }
                JSONObject j = json(ctx, "https://develop.roblox.com/v1/universes/" + universeId + "/places?isUniverseCreation=false&limit=100&sortOrder=Asc" + str);
                JSONArray arr = j.optJSONArray("data");
                if (arr == null) {
                    arr = new JSONArray();
                }
                int length = arr.length();
                for (int i = 0; i < length; i++) {
                    JSONObject o = arr.getJSONObject(i);
                    long j2 = o.getLong("id");
                    String strOptString = o.optString("name");
                    Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                    out.add(new Place(j2, strOptString, game.getUniverseId()));
                }
                String strNextCursor = nextCursor(j);
                if (strNextCursor == null) {
                    break;
                }
                cursor = strNextCursor;
            } catch (AuthRequiredException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
        if (out.isEmpty() && game.getRootPlaceId() != 0) {
            out.add(new Place(game.getRootPlaceId(), game.getName(), game.getUniverseId()));
        }
        return CollectionsKt.sortedWith(out, new Comparator() { // from class: com.goydevv.studrblx.net.RobloxCloud$places$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                return ComparisonsKt.compareValues(Integer.valueOf(((RobloxCloud.Place) t).getId() == game.getRootPlaceId() ? 0 : 1), Integer.valueOf(((RobloxCloud.Place) t2).getId() != game.getRootPlaceId() ? 1 : 0));
            }
        });
    }

    public final long universeOf(long placeId) {
        try {
            return Http.json$default(Http.INSTANCE, "https://apis.roblox.com/universes/v1/places/" + placeId + "/universe", null, 2, null).optLong("universeId");
        } catch (Exception e) {
            return 0L;
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public final List<Version> versions(Context ctx, long placeId) throws Exception {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        LinkedHashMap byNum = new LinkedHashMap();
        Exception err = null;
        try {
            versions$read$37(ctx, placeId, byNum, "saved-versions", false);
        } catch (AuthRequiredException e) {
            throw e;
        } catch (Exception e2) {
            err = e2;
        }
        try {
            versions$read$37(ctx, placeId, byNum, "published-versions", true);
        } catch (AuthRequiredException e3) {
            throw e3;
        } catch (Exception e4) {
            if (byNum.isEmpty()) {
                err = err == null ? e4 : err;
            }
        }
        if (byNum.isEmpty() && err != null) {
            throw err;
        }
        Iterable iterableValues = byNum.values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        return CollectionsKt.sortedWith(iterableValues, new Comparator() { // from class: com.goydevv.studrblx.net.RobloxCloud$versions$$inlined$sortedByDescending$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(Object t, Object t2) {
                return ComparisonsKt.compareValues(Integer.valueOf(((RobloxCloud.Version) t2).getNumber()), Integer.valueOf(((RobloxCloud.Version) t).getNumber()));
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:10:0x0029  */
    private static final void versions$read$37(Context $ctx, long $placeId, LinkedHashMap<Integer, Version> linkedHashMap, String kind, boolean published) throws JSONException, HttpException {
        String str;
        Version version;
        String cursor = null;
        int page = 0;
        while (page < 6) {
            RobloxCloud robloxCloud = INSTANCE;
            if (cursor != null) {
                str = "&cursor=" + INSTANCE.enc(cursor);
                if (str == null) {
                    str = "";
                }
            } else {
                str = "";
            }
            JSONObject j = robloxCloud.json($ctx, "https://develop.roblox.com/v1/assets/" + $placeId + "/" + kind + "?limit=100&sortOrder=Desc" + str);
            JSONArray arr = j.optJSONArray("data");
            if (arr == null) {
                arr = new JSONArray();
            }
            int i = 0;
            int length = arr.length();
            while (i < length) {
                JSONObject o = arr.getJSONObject(i);
                int n = o.optInt("assetVersionNumber", o.optInt("versionNumber"));
                if (n <= 0) {
                } else {
                    LinkedHashMap<Integer, Version> linkedHashMap2 = linkedHashMap;
                    Integer numValueOf = Integer.valueOf(n);
                    Version version2 = linkedHashMap2.get(numValueOf);
                    if (version2 == null) {
                        String strOptString = o.optString("created");
                        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                        version = new Version(n, strOptString, published);
                        linkedHashMap2.put(numValueOf, version);
                    } else {
                        version = version2;
                    }
                    Version v = version;
                    if (published) {
                        v.setPublished(true);
                    }
                }
                i++;
            }
            int page2 = page;
            cursor = INSTANCE.nextCursor(j);
            if (cursor != null) {
                page = page2 + 1;
            } else {
                return;
            }
        }
    }

    public static /* synthetic */ byte[] downloadPlace$default(RobloxCloud robloxCloud, Context context, long j, Integer num, int i, Object obj) {
        if ((i & 4) != 0) {
            num = null;
        }
        return robloxCloud.downloadPlace(context, j, num);
    }

    public final byte[] downloadPlace(Context ctx, long placeId, Integer version) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        try { return RobloxApi.INSTANCE.downloadAsset(ctx, placeId, version); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    /* JADX WARN: Code duplicated, block: B:79:0x0221 A[LOOP:0: B:67:0x01e4->B:79:0x0221, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:88:0x0225 A[SYNTHETIC] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public final String publish(Context ctx, long universeId0, long placeId, byte[] bytes, boolean publish) throws IOException {
        RobloxCloud robloxCloud;
        long universeId;
        boolean z;
        boolean z2;
        ArrayList errors;
        String url;
        Iterable iterable;
        boolean z3 = false;
        String str;
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (universeId0 != 0) {
            robloxCloud = this;
            universeId = universeId0;
        } else {
            robloxCloud = this;
            universeId = robloxCloud.universeOf(placeId);
        }
        if (universeId == 0) {
            throw new IOException("Couldn't find which experience place " + placeId + " belongs to.");
        }
        String url2 = "https://apis.roblox.com/universes/v1/" + universeId + "/places/" + placeId + "/versions?versionType=" + (publish ? "Published" : "Saved");
        ArrayList errors2 = new ArrayList();
        String key = RobloxApi.INSTANCE.apiKey(ctx);
        if (!(key.length() > 0)) {
            z = true;
            z2 = false;
        } else {
            z = true;
            z2 = false;
            Http.Response r = Http.call$default(Http.INSTANCE, url2, "POST", MapsKt.mapOf(TuplesKt.to("x-api-key", key)), bytes, "application/octet-stream", 0, 32, null);
            if (r.getOk()) {
                return publish$version(r);
            }
            String strDescribe = Http.INSTANCE.describe(r.getCode(), r.getBody());
            switch (r.getCode()) {
                case 401 /* 401 */:
                case 403 /* 403 */:
                    str = " — the key needs the universe-places write permission for this experience.";
                    break;
                case 402 /* 402 */:
                default:
                    str = "";
                    break;
            }
            errors2.add("API key: " + strDescribe + str);
        }
        if (!(RobloxApi.INSTANCE.cookie(ctx).length() > 0 ? z : z2)) {
            errors = errors2;
            url = key;
        } else {
            errors = errors2;
            url = key;
            try {
                Http.Response r2 = robloxCloud.authed(ctx, url2, "POST", bytes, "application/octet-stream");
                if (r2.getOk()) {
                    return publish$version(r2);
                }
                errors.add("Signed-in upload: " + Http.INSTANCE.describe(r2.getCode(), r2.getBody()));
            } catch (AuthRequiredException e) {
                errors.add("Signed-in upload: " + e.getMessage());
            }
        }
        if (url.length() == 0 ? z : z2) {
            throw new PublishException("Roblox no longer accepts place uploads with only a sign-in (its old upload endpoint was turned off). Add an Open Cloud API key once and publishing and saving will work." + (errors.isEmpty() ? "" : "\n\nDetails: " + CollectionsKt.joinToString(errors, "; ", "", "", -1, "...", null)), z);
        }
        String strJoinToString$default = CollectionsKt.joinToString(errors, "\n", "", "", -1, "...", null);
        Iterable<String> iterable2 = errors;
        if (!(iterable2 instanceof Collection) || !((Collection) iterable2).isEmpty()) {
            for (String str2 : iterable2) {
                if (StringsKt.startsWith(str2, "API key", false)) {
                    iterable = iterable2;
                    if (StringsKt.contains(str2, "401", false) || StringsKt.contains(str2, "403", false)) {
                        z3 = true;
                    }
                    if (z3) {
                        z2 = true;
                    } else {
                        iterable2 = iterable;
                    }
                } else {
                    iterable = iterable2;
                }
                z3 = z2;
                if (z3) {
                    iterable2 = iterable;
                } else {
                    z2 = true;
                }
            }
        }
        throw new PublishException(strJoinToString$default, z2);
    }

    private static final String publish$version(Http.Response r) {
        int v;
        try {
            v = new JSONObject(r.getText()).optInt("versionNumber");
        } catch (Exception e) {
            v = 0;
        }
        return v > 0 ? "version " + v : "done";
    }

    public final Map<Long, String> gameIcons(List<Long> universeIds) {
        Intrinsics.checkNotNullParameter(universeIds, "universeIds");
        if (universeIds.isEmpty()) {
            return MapsKt.emptyMap();
        }
        HashMap out = new HashMap();
        for (Object _cObj : CollectionsKt.chunked(universeIds, 50)) {
            List chunk = (List) _cObj;
            try {
                JSONObject j = Http.json$default(Http.INSTANCE, "https://thumbnails.roblox.com/v1/games/icons?universeIds=" + CollectionsKt.joinToString(chunk, ",", "", "", -1, "...", null) + "&returnPolicy=PlaceHolder&size=150x150&format=Png&isCircular=false", null, 2, null);
                JSONArray arr = j.optJSONArray("data");
                if (arr != null) {
                    int length = arr.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject o = arr.getJSONObject(i);
                        String u = o.optString("imageUrl");
                        Intrinsics.checkNotNull(u);
                        if (u.length() > 0) {
                            out.put(Long.valueOf(o.optLong("targetId")), u);
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return out;
    }

    public final Map<Long, String> groupIcons(List<Long> groupIds) {
        Intrinsics.checkNotNullParameter(groupIds, "groupIds");
        if (groupIds.isEmpty()) {
            return MapsKt.emptyMap();
        }
        HashMap out = new HashMap();
        try {
            JSONObject j = Http.json$default(Http.INSTANCE, "https://thumbnails.roblox.com/v1/groups/icons?groupIds=" + CollectionsKt.joinToString(groupIds, ",", "", "", -1, "...", null) + "&size=150x150&format=Png&isCircular=false", null, 2, null);
            JSONArray arr = j.optJSONArray("data");
            if (arr == null) {
                return out;
            }
            int length = arr.length();
            for (int i = 0; i < length; i++) {
                JSONObject o = arr.getJSONObject(i);
                String u = o.optString("imageUrl");
                Intrinsics.checkNotNull(u);
                if (u.length() > 0) {
                    out.put(Long.valueOf(o.optLong("targetId")), u);
                }
            }
            return out;
        } catch (Exception e) {
            return out;
        }
    }

    public final String headshot(long userId) {
        String strOptString;
        try {
            JSONObject j = Http.json$default(Http.INSTANCE, "https://thumbnails.roblox.com/v1/users/avatar-headshot?userIds=" + userId + "&size=150x150&format=Png&isCircular=true", null, 2, null);
            JSONArray jSONArrayOptJSONArray = j.optJSONArray("data");
            if (jSONArrayOptJSONArray == null) {
                return null;
            }
            JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(0);
            if (jSONObjectOptJSONObject == null || (strOptString = jSONObjectOptJSONObject.optString("imageUrl")) == null) {
                return null;
            }
            if (strOptString.length() > 0) {
                return strOptString;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public final User userByName(String name) throws HttpException {
        try {
        JSONObject o;
        Intrinsics.checkNotNullParameter(name, "name");
        String body = new JSONObject().put("usernames", new JSONArray().put(StringsKt.trim((CharSequence) name).toString())).put("excludeBannedUsers", true).toString();
        Intrinsics.checkNotNullExpressionValue(body, "toString(...)");
        Http http = Http.INSTANCE;
        Map mapEmptyMap = MapsKt.emptyMap();
        byte[] bytes = body.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        Http.Response r = Http.call$default(http, "https://users.roblox.com/v1/usernames/users", "POST", mapEmptyMap, bytes, "application/json", 0, 32, null);
        if (!r.getOk()) {
            throw new HttpException(r.getCode(), Http.INSTANCE.describe(r.getCode(), r.getBody()));
        }
        JSONArray jSONArrayOptJSONArray = new JSONObject(r.getText()).optJSONArray("data");
        if (jSONArrayOptJSONArray == null || (o = jSONArrayOptJSONArray.optJSONObject(0)) == null) {
            return null;
        }
        long jOptLong = o.optLong("id");
        String strOptString = o.optString("name");
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        String strOptString2 = o.optString("displayName");
        Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
        return new User(jOptLong, strOptString, strOptString2);
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public final void logout(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        try {
            authed$default(this, ctx, "https://auth.roblox.com/v2/logout", "POST", null, null, 24, null);
        } catch (Exception e) {
        }
        csrf = null;
    }
}
