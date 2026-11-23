package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.util.BackgroundPool;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.model.BrickColors;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.InstanceFactory;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import com.goydevv.studrblx.net.Http;
import com.goydevv.studrblx.net.RobloxApi;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Avatar.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class Avatar {
    private static final long DEFAULT_HEAD_TEXTURE = 111092388570647L;
    private static final String PREFS = "avatar";
    public static final String TEX_PREFIX = "mobilestudio-avatar://";
    private static Context app;
    private static volatile Spec current;
    private static volatile boolean loading;
    private static int texSerial;
    private static volatile int version;
    public static final Avatar INSTANCE = new Avatar();
    private static final ConcurrentHashMap<String, Bitmap> generated = new ConcurrentHashMap<>();
    private static final Map<byte[], float[]> accMetaMap = Collections.synchronizedMap(new IdentityHashMap<byte[], float[]>());

    private static void putAccMeta(byte[] key, float[] adj) {
        if (key != null && adj != null) {
            accMetaMap.put(key, adj);
        }
    }

    private static float[] getAccMeta(byte[] key) {
        return key != null ? accMetaMap.get(key) : null;
    }
    private static final ExecutorService pool = BackgroundPool.create(1);
    private static final CopyOnWriteArrayList<Function0<Unit>> listeners = new CopyOnWriteArrayList<>();
    private static final List<String> PARTS = CollectionsKt.listOf(new String[]{"Head", "Torso", "Left Arm", "Right Arm", "Left Leg", "Right Leg"});
    private static final Map<String, String> colorKeys = MapsKt.mapOf(TuplesKt.to("Head", "head"), TuplesKt.to("Torso", "torso"), TuplesKt.to("Left Arm", "leftArm"), TuplesKt.to("Right Arm", "rightArm"), TuplesKt.to("Left Leg", "leftLeg"), TuplesKt.to("Right Leg", "rightLeg"));
    private static final Map<String, Color3> CLASSIC_COLORS = MapsKt.mapOf(TuplesKt.to("Head", Color3.INSTANCE.fromRGB(245, 205, 48)), TuplesKt.to("Left Arm", Color3.INSTANCE.fromRGB(245, 205, 48)), TuplesKt.to("Right Arm", Color3.INSTANCE.fromRGB(245, 205, 48)), TuplesKt.to("Torso", Color3.INSTANCE.fromRGB(13, 105, 172)), TuplesKt.to("Left Leg", Color3.INSTANCE.fromRGB(164, 189, 71)), TuplesKt.to("Right Leg", Color3.INSTANCE.fromRGB(164, 189, 71)));
    private static final Map<String, List<Pair<String, Vector3>>> r6Attachments = MapsKt.mapOf(TuplesKt.to("Head", CollectionsKt.listOf(new Pair[]{TuplesKt.to("HatAttachment", new Vector3(0.0f, 0.6f, 0.0f)), TuplesKt.to("HairAttachment", new Vector3(0.0f, 0.6f, 0.0f)), TuplesKt.to("FaceFrontAttachment", new Vector3(0.0f, 0.0f, -0.6f)), TuplesKt.to("FaceCenterAttachment", Vector3.INSTANCE.getZERO())})), TuplesKt.to("Torso", CollectionsKt.listOf(new Pair[]{TuplesKt.to("NeckAttachment", new Vector3(0.0f, 1.0f, 0.0f)), TuplesKt.to("BodyFrontAttachment", new Vector3(0.0f, 0.0f, -0.5f)), TuplesKt.to("BodyBackAttachment", new Vector3(0.0f, 0.0f, 0.5f)), TuplesKt.to("LeftCollarAttachment", new Vector3(-1.0f, 1.0f, 0.0f)), TuplesKt.to("RightCollarAttachment", new Vector3(1.0f, 1.0f, 0.0f)), TuplesKt.to("WaistFrontAttachment", new Vector3(0.0f, -1.0f, -0.5f)), TuplesKt.to("WaistCenterAttachment", new Vector3(0.0f, -1.0f, 0.0f)), TuplesKt.to("WaistBackAttachment", new Vector3(0.0f, -1.0f, 0.5f))})), TuplesKt.to("Left Arm", CollectionsKt.listOf(new Pair[]{TuplesKt.to("LeftShoulderAttachment", new Vector3(0.0f, 1.0f, 0.0f)), TuplesKt.to("LeftGripAttachment", new Vector3(0.0f, -1.0f, 0.0f))})), TuplesKt.to("Right Arm", CollectionsKt.listOf(new Pair[]{TuplesKt.to("RightShoulderAttachment", new Vector3(0.0f, 1.0f, 0.0f)), TuplesKt.to("RightGripAttachment", new Vector3(0.0f, -1.0f, 0.0f))})), TuplesKt.to("Left Leg", CollectionsKt.listOf(TuplesKt.to("LeftFootAttachment", new Vector3(0.0f, -1.0f, 0.0f)))), TuplesKt.to("Right Leg", CollectionsKt.listOf(TuplesKt.to("RightFootAttachment", new Vector3(0.0f, -1.0f, 0.0f)))));
    private static final List<String> R15_PARTS = CollectionsKt.listOf(new String[]{"Head", "UpperTorso", "LowerTorso", "LeftUpperArm", "LeftLowerArm", "LeftHand", "RightUpperArm", "RightLowerArm", "RightHand", "LeftUpperLeg", "LeftLowerLeg", "LeftFoot", "RightUpperLeg", "RightLowerLeg", "RightFoot"});
    private static final LinkedHashMap<String, Group> GROUPS = MapsKt.linkedMapOf(TuplesKt.to("Torso", new Group(CollectionsKt.listOf(new String[]{"UpperTorso", "LowerTorso"}), "Torso", 27)), TuplesKt.to("RightArm", new Group(CollectionsKt.listOf(new String[]{"RightUpperArm", "RightLowerArm", "RightHand"}), "Right Arm", 28)), TuplesKt.to("LeftArm", new Group(CollectionsKt.listOf(new String[]{"LeftUpperArm", "LeftLowerArm", "LeftHand"}), "Left Arm", 29)), TuplesKt.to("LeftLeg", new Group(CollectionsKt.listOf(new String[]{"LeftUpperLeg", "LeftLowerLeg", "LeftFoot"}), "Left Leg", 30)), TuplesKt.to("RightLeg", new Group(CollectionsKt.listOf(new String[]{"RightUpperLeg", "RightLowerLeg", "RightFoot"}), "Right Leg", 31)));
    private static final List<Tpl> DEFAULT_R15 = CollectionsKt.listOf(new Tpl[]{new Tpl("LowerTorso", INSTANCE.v(2.0f, 0.4f, 1.0f), 7430071109L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("RootRigAttachment", INSTANCE.v(0.0f, -0.2f, 0.0f)), TuplesKt.to("WaistRigAttachment", INSTANCE.v(0.0f, 0.2f, 0.0f)), TuplesKt.to("LeftHipRigAttachment", INSTANCE.v(-0.5f, -0.2f, 0.0f)), TuplesKt.to("RightHipRigAttachment", INSTANCE.v(0.5f, -0.2f, 0.0f)), TuplesKt.to("WaistCenterAttachment", INSTANCE.v(0.0f, -0.2f, 0.0f)), TuplesKt.to("WaistFrontAttachment", INSTANCE.v(0.0f, -0.2f, -0.5f)), TuplesKt.to("WaistBackAttachment", INSTANCE.v(0.0f, -0.2f, 0.5f))})), new Tpl("UpperTorso", INSTANCE.v(2.0f, 1.6f, 1.0f), 7430071038L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("WaistRigAttachment", INSTANCE.v(0.0f, -0.8f, 0.0f)), TuplesKt.to("NeckRigAttachment", INSTANCE.v(0.0f, 0.8f, 0.0f)), TuplesKt.to("LeftShoulderRigAttachment", INSTANCE.v(-1.0f, 0.563f, 0.0f)), TuplesKt.to("RightShoulderRigAttachment", INSTANCE.v(1.0f, 0.563f, 0.0f)), TuplesKt.to("NeckAttachment", INSTANCE.v(0.0f, 0.8f, 0.0f)), TuplesKt.to("BodyFrontAttachment", INSTANCE.v(0.0f, -0.2f, -0.5f)), TuplesKt.to("BodyBackAttachment", INSTANCE.v(0.0f, -0.2f, 0.5f)), TuplesKt.to("LeftCollarAttachment", INSTANCE.v(-1.0f, 0.8f, 0.0f)), TuplesKt.to("RightCollarAttachment", INSTANCE.v(1.0f, 0.8f, 0.0f))})), new Tpl("LeftUpperArm", INSTANCE.v(1.0f, 1.169f, 1.0f), 7430071044L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("LeftShoulderRigAttachment", INSTANCE.v(0.5f, 0.394f, 0.0f)), TuplesKt.to("LeftElbowRigAttachment", INSTANCE.v(0.0f, -0.334f, 0.0f)), TuplesKt.to("LeftShoulderAttachment", INSTANCE.v(0.0f, 0.584f, 0.0f))})), new Tpl("LeftLowerArm", INSTANCE.v(1.0f, 1.052f, 1.0f), 7430071005L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("LeftElbowRigAttachment", INSTANCE.v(0.0f, 0.259f, 0.0f)), TuplesKt.to("LeftWristRigAttachment", INSTANCE.v(0.0f, -0.501f, 0.0f))})), new Tpl("LeftHand", INSTANCE.v(1.0f, 0.3f, 1.0f), 7430070991L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("LeftWristRigAttachment", INSTANCE.v(0.0f, 0.125f, 0.0f)), TuplesKt.to("LeftGripAttachment", INSTANCE.v(0.0f, -0.15f, 0.0f))})), new Tpl("RightUpperArm", INSTANCE.v(1.0f, 1.169f, 1.0f), 7430071041L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("RightShoulderRigAttachment", INSTANCE.v(-0.5f, 0.394f, 0.0f)), TuplesKt.to("RightElbowRigAttachment", INSTANCE.v(0.0f, -0.334f, 0.0f)), TuplesKt.to("RightShoulderAttachment", INSTANCE.v(0.0f, 0.584f, 0.0f))})), new Tpl("RightLowerArm", INSTANCE.v(1.0f, 1.052f, 1.0f), 7430071013L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("RightElbowRigAttachment", INSTANCE.v(0.0f, 0.259f, 0.0f)), TuplesKt.to("RightWristRigAttachment", INSTANCE.v(0.0f, -0.501f, 0.0f))})), new Tpl("RightHand", INSTANCE.v(1.0f, 0.3f, 1.0f), 7430070997L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("RightWristRigAttachment", INSTANCE.v(0.0f, 0.125f, 0.0f)), TuplesKt.to("RightGripAttachment", INSTANCE.v(0.0f, -0.15f, 0.0f))})), new Tpl("LeftUpperLeg", INSTANCE.v(1.0f, 1.217f, 1.0f), 7430071065L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("LeftHipRigAttachment", INSTANCE.v(0.0f, 0.421f, 0.0f)), TuplesKt.to("LeftKneeRigAttachment", INSTANCE.v(0.0f, -0.401f, 0.0f))})), new Tpl("LeftLowerLeg", INSTANCE.v(1.0f, 1.193f, 1.0f), 7430071049L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("LeftKneeRigAttachment", INSTANCE.v(0.0f, 0.379f, 0.0f)), TuplesKt.to("LeftAnkleRigAttachment", INSTANCE.v(0.0f, -0.547f, 0.0f))})), new Tpl("LeftFoot", INSTANCE.v(1.0f, 0.3f, 1.0f), 7430071039L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("LeftAnkleRigAttachment", INSTANCE.v(0.0f, 0.102f, 0.0f)), TuplesKt.to("LeftFootAttachment", INSTANCE.v(0.0f, -0.15f, 0.0f))})), new Tpl("RightUpperLeg", INSTANCE.v(1.0f, 1.217f, 1.0f), 7430071119L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("RightHipRigAttachment", INSTANCE.v(0.0f, 0.421f, 0.0f)), TuplesKt.to("RightKneeRigAttachment", INSTANCE.v(0.0f, -0.401f, 0.0f))})), new Tpl("RightLowerLeg", INSTANCE.v(1.0f, 1.193f, 1.0f), 7430071105L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("RightKneeRigAttachment", INSTANCE.v(0.0f, 0.379f, 0.0f)), TuplesKt.to("RightAnkleRigAttachment", INSTANCE.v(0.0f, -0.547f, 0.0f))})), new Tpl("RightFoot", INSTANCE.v(1.0f, 0.3f, 1.0f), 7430071082L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("RightAnkleRigAttachment", INSTANCE.v(0.0f, 0.102f, 0.0f)), TuplesKt.to("RightFootAttachment", INSTANCE.v(0.0f, -0.15f, 0.0f))})), new Tpl("Head", INSTANCE.v(1.196f, 1.203f, 1.198f), 123480233606534L, CollectionsKt.listOf(new Pair[]{TuplesKt.to("NeckRigAttachment", INSTANCE.v(0.0f, -0.586f, 0.0f)), TuplesKt.to("HatAttachment", INSTANCE.v(0.0f, 0.6f, 0.0f)), TuplesKt.to("HairAttachment", INSTANCE.v(0.0f, 0.6f, 0.0f)), TuplesKt.to("FaceFrontAttachment", INSTANCE.v(0.0f, 0.0f, -0.6f)), TuplesKt.to("FaceCenterAttachment", INSTANCE.v(0.0f, 0.0f, 0.0f))}))});

    /* JADX INFO: compiled from: Avatar.kt */
    public enum Mode {
        ACCOUNT,
        USER,
        CUSTOM,
        CLASSIC,
        CHARACTER;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Mode> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: Avatar.kt */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Mode.values().length];
            try {
                iArr[Mode.ACCOUNT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Mode.USER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Mode.CUSTOM.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Mode.CLASSIC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Mode.CHARACTER.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private Avatar() {
    }

    /* JADX INFO: compiled from: Avatar.kt */
    public static final class Scales {
        private final float depth;
        private final float head;
        private final float height;
        private final float width;

        public Scales() {
            this(0.0f, 0.0f, 0.0f, 0.0f, 15, null);
        }

        public Scales(float height, float width, float depth, float head) {
            this.height = height;
            this.width = width;
            this.depth = depth;
            this.head = head;
        }

        public /* synthetic */ Scales(float f, float f2, float f3, float f4, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? 1.0f : f, (i & 2) != 0 ? 1.0f : f2, (i & 4) != 0 ? 1.0f : f3, (i & 8) != 0 ? 1.0f : f4);
        }

        public final float getDepth() {
            return this.depth;
        }

        public final float getHead() {
            return this.head;
        }

        public final float getHeight() {
            return this.height;
        }

        public final float getWidth() {
            return this.width;
        }
    }

    /* JADX INFO: compiled from: Avatar.kt */
    public static final class Spec {
        private final List<byte[]> accessories;
        private final Map<Integer, byte[]> bodyParts;
        private final byte[] character;
        private final Map<String, Bitmap> clothing;
        private final Map<String, Color3> colors;
        private final String face;
        private final String label;
        private final String pants;
        private final boolean r15;
        private final Scales scales;
        private final String shirt;
        private final String tshirt;

        public Spec(Map<String, Color3> colors, String shirt, String pants, String tshirt, String face, List<byte[]> accessories, String label, boolean r15, Scales scales, Map<Integer, byte[]> bodyParts, Map<String, Bitmap> clothing, byte[] character) {
            Intrinsics.checkNotNullParameter(colors, "colors");
            Intrinsics.checkNotNullParameter(accessories, "accessories");
            Intrinsics.checkNotNullParameter(label, "label");
            Intrinsics.checkNotNullParameter(scales, "scales");
            Intrinsics.checkNotNullParameter(bodyParts, "bodyParts");
            Intrinsics.checkNotNullParameter(clothing, "clothing");
            this.colors = colors;
            this.shirt = shirt;
            this.pants = pants;
            this.tshirt = tshirt;
            this.face = face;
            this.accessories = accessories;
            this.label = label;
            this.r15 = r15;
            this.scales = scales;
            this.bodyParts = bodyParts;
            this.clothing = clothing;
            this.character = character;
        }

        public /* synthetic */ Spec(Map map, String str, String str2, String str3, String str4, List list, String str5, boolean z, Scales scales, Map map2, Map map3, byte[] bArr, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(map, str, str2, str3, str4, list, str5, (i & 128) != 0 ? false : z, (i & 256) != 0 ? new Scales(0.0f, 0.0f, 0.0f, 0.0f, 15, null) : scales, (i & 512) != 0 ? MapsKt.emptyMap() : map2, (i & 1024) != 0 ? MapsKt.emptyMap() : map3, (i & 2048) != 0 ? null : bArr);
        }

        public final Map<String, Color3> getColors() {
            return this.colors;
        }

        public final String getFace() {
            return this.face;
        }

        public final String getPants() {
            return this.pants;
        }

        public final String getShirt() {
            return this.shirt;
        }

        public final String getTshirt() {
            return this.tshirt;
        }

        public final List<byte[]> getAccessories() {
            return this.accessories;
        }

        public final String getLabel() {
            return this.label;
        }

        public final boolean getR15() {
            return this.r15;
        }

        public final Scales getScales() {
            return this.scales;
        }

        public final Map<Integer, byte[]> getBodyParts() {
            return this.bodyParts;
        }

        public final Map<String, Bitmap> getClothing() {
            return this.clothing;
        }

        public final byte[] getCharacter() {
            return this.character;
        }

        public static /* synthetic */ Spec forRig$default(Spec spec, boolean z, Scales scales, int i, Object obj) {
            if ((i & 2) != 0) {
                scales = spec.scales;
            }
            return spec.forRig(z, scales);
        }

        public final Spec forRig(boolean r15, Scales scales) {
            Intrinsics.checkNotNullParameter(scales, "scales");
            return new Spec(this.colors, this.shirt, this.pants, this.tshirt, this.face, this.accessories, this.label, r15, scales, this.bodyParts, this.clothing, this.character);
        }
    }

    public final Bitmap texture(String content) {
        Intrinsics.checkNotNullParameter(content, "content");
        return generated.get(content);
    }

    public final void putTexture(String content, Bitmap bmp) {
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(bmp, "bmp");
        generated.put(content, bmp);
    }

    public final Spec getCurrent() {
        return current;
    }

    public final int getVersion() {
        return version;
    }

    public final boolean getLoading() {
        return loading;
    }

    public final CopyOnWriteArrayList<Function0<Unit>> getListeners() {
        return listeners;
    }

    public final List<String> getPARTS() {
        return PARTS;
    }

    public final Map<String, Color3> getCLASSIC_COLORS() {
        return CLASSIC_COLORS;
    }

    private final SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences(PREFS, 0);
    }

    public final Mode mode(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        try {
            String string = prefs(ctx).getString("mode", "ACCOUNT");
            Intrinsics.checkNotNull(string);
            return Mode.valueOf(string);
        } catch (Exception e) {
            return Mode.ACCOUNT;
        }
    }

    public final Pair<Long, String> otherUser(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Long lValueOf = Long.valueOf(prefs(ctx).getLong("userId", 0L));
        String string = prefs(ctx).getString("userName", "");
        return TuplesKt.to(lValueOf, string != null ? string : "");
    }

    public final JSONObject custom(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        try {
            String string = prefs(ctx).getString("custom", "{}");
            Intrinsics.checkNotNull(string);
            return new JSONObject(string);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static /* synthetic */ void setMode$default(Avatar avatar, Context context, Mode mode, long j, String str, int i, Object obj) {
        if ((i & 4) != 0) {
            j = 0;
        }
        long j2 = j;
        if ((i & 8) != 0) {
            str = "";
        }
        avatar.setMode(context, mode, j2, str);
    }

    public final void setMode(Context ctx, Mode m, long userId, String userName) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(m, "m");
        Intrinsics.checkNotNullParameter(userName, "userName");
        SharedPreferences.Editor editorPutString = prefs(ctx).edit().putString("mode", m.name());
        if (m == Mode.USER) {
            editorPutString.putLong("userId", userId).putString("userName", userName);
        }
        editorPutString.apply();
        refresh(ctx);
    }

    public final void setCustom(Context ctx, JSONObject o) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(o, "o");
        prefs(ctx).edit().putString("custom", o.toString()).putString("mode", "CUSTOM").apply();
        refresh(ctx);
    }

    public final String describe(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        switch (WhenMappings.$EnumSwitchMapping$0[mode(ctx).ordinal()]) {
            case 1:
                if (RobloxApi.INSTANCE.isGuest(ctx)) {
                    return "Classic (guest)";
                }
                return "My avatar (@" + RobloxApi.INSTANCE.userName(ctx) + ")";
            case 2:
                return "@" + ((Object) otherUser(ctx).getSecond()) + "'s avatar";
            case 3:
                return "Custom avatar";
            case 4:
                return "Classic";
            case 5:
                String strCharacterName = characterName(ctx);
                if (strCharacterName.length() == 0) {
                    strCharacterName = "custom model";
                }
                return "Character: " + ((Object) strCharacterName);
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    private final File characterFile(Context ctx) {
        return new File(ctx.getApplicationContext().getFilesDir(), "playtest_character.rbxm");
    }

    public final String characterName(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        String string = prefs(ctx).getString("characterName", "");
        return string != null ? string : "";
    }

    public final boolean hasCharacter(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        return characterFile(ctx).isFile();
    }

    public final void setCharacter(Context ctx, byte[] bytes, String name) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Intrinsics.checkNotNullParameter(name, "name");
        File f = characterFile(ctx);
        File tmp = new File(f.getPath() + ".tmp");
        FilesKt.writeBytes(tmp, bytes);
        if (!tmp.renameTo(f)) {
            f.delete();
            tmp.renameTo(f);
        }
        prefs(ctx).edit().putString("characterName", name).putString("mode", "CHARACTER").apply();
        refresh(ctx);
    }

    public final void clearCharacter(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        characterFile(ctx).delete();
        prefs(ctx).edit().remove("characterName").apply();
        if (mode(ctx) == Mode.CHARACTER) {
            setMode$default(this, ctx, Mode.ACCOUNT, 0L, null, 12, null);
        }
    }

    public final void refresh(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        app = ctx.getApplicationContext();
        final Context c = ctx.getApplicationContext();
        loading = true;
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.engine.Avatar$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Avatar.refresh$lambda$3(c);
            }
        });
    }

    static final void refresh$lambda$3(Context $c) {
        Spec spec;
        try {
            Avatar avatar = INSTANCE;
            Intrinsics.checkNotNull($c);
            spec = avatar.resolve($c);
        } catch (Exception e) {
            OutputLog outputLog = OutputLog.INSTANCE;
            String message = e.getMessage();
            outputLog.warn("Avatar: " + (message != null ? message : e));
            spec = INSTANCE.classic();
        }
        Avatar avatar2 = INSTANCE;
        current = spec;
        Avatar avatar3 = INSTANCE;
        int i = version;
        Avatar avatar4 = INSTANCE;
        version = i + 1;
        Avatar avatar5 = INSTANCE;
        loading = false;
        Avatar avatar6 = INSTANCE;
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            ((Function0) it.next()).invoke();
        }
    }

    private final Spec classic() {
        return new Spec(CLASSIC_COLORS, null, null, null, null, CollectionsKt.emptyList(), "Classic", false, null, null, null, null, 3968, null);
    }

    private final Spec resolve(Context ctx) {
        switch (WhenMappings.$EnumSwitchMapping$0[mode(ctx).ordinal()]) {
            case 1:
                long id = RobloxApi.INSTANCE.userId(ctx);
                if (RobloxApi.INSTANCE.isGuest(ctx) || id == 0) {
                    return classic();
                }
                return fromUser(id, "@" + RobloxApi.INSTANCE.userName(ctx));
            case 2:
                Pair<Long, String> pairOtherUser = otherUser(ctx);
                long id2 = pairOtherUser.component1().longValue();
                String n = pairOtherUser.component2();
                return id2 == 0 ? classic() : fromUser(id2, "@" + n);
            case 3:
                return fromCustom(custom(ctx));
            case 4:
                return classic();
            case 5:
                File f = characterFile(ctx);
                if (!f.isFile()) {
                    return classic();
                }
                Map<String, Color3> map = CLASSIC_COLORS;
                List listEmptyList = CollectionsKt.emptyList();
                String strCharacterName = characterName(ctx);
                if (strCharacterName.length() == 0) {
                    strCharacterName = "Character";
                }
                return new Spec(map, null, null, null, null, listEmptyList, strCharacterName, false, null, null, null, FilesKt.readBytes(f), 1920, null);
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    private final Color3 hex(String s) {
        if (s != null) {
            return Color3.INSTANCE.fromHex(s);
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:41:0x0155  */
    /* JADX WARN: Code duplicated, block: B:44:0x015a  */
    private final Spec fromUser(long userId, String label) {
        try {
        JSONArray assets;
        JSONObject j;
        boolean z = false;
        String pants;
        String tshirt;
        String face;
        String pants2;
        JSONArray assets2;
        HashMap bodyParts = null;
        boolean z2 = false;
        Object colors = new HashMap(CLASSIC_COLORS);
        new JSONArray();
        try {
            JSONObject j2 = Http.json$default(Http.INSTANCE, "https://avatar.roblox.com/v2/avatar/users/" + userId + "/avatar", null, 2, null);
            JSONObject jSONObjectOptJSONObject = j2.optJSONObject("bodyColor3s");
            if (jSONObjectOptJSONObject != null) {
                for (Map.Entry<String, String> entry : colorKeys.entrySet()) {
                    String key = entry.getKey();
                    Color3 color3Hex = INSTANCE.hex(jSONObjectOptJSONObject.optString(entry.getValue() + "Color3"));
                    if (color3Hex != null) {
                        ((Map) colors).put(key, color3Hex);
                    }
                }
            }
            JSONArray jSONArrayOptJSONArray = j2.optJSONArray("assets");
            if (jSONArrayOptJSONArray == null) {
                jSONArrayOptJSONArray = new JSONArray();
            }
            assets = jSONArrayOptJSONArray;
            j = j2;
        } catch (Exception e) {
            JSONObject j3 = Http.json$default(Http.INSTANCE, "https://avatar.roblox.com/v1/users/" + userId + "/avatar", null, 2, null);
            JSONObject jSONObjectOptJSONObject2 = j3.optJSONObject("bodyColors");
            if (jSONObjectOptJSONObject2 != null) {
                for (Map.Entry<String, String> entry2 : colorKeys.entrySet()) {
                    String key2 = entry2.getKey();
                    int iOptInt = jSONObjectOptJSONObject2.optInt(entry2.getValue() + "ColorId", -1);
                    if (iOptInt >= 0) {
                        ((Map) colors).put(key2, BrickColors.INSTANCE.color(iOptInt));
                    }
                }
            }
            JSONArray jSONArrayOptJSONArray2 = j3.optJSONArray("assets");
            if (jSONArrayOptJSONArray2 == null) {
                jSONArrayOptJSONArray2 = new JSONArray();
            }
            assets = jSONArrayOptJSONArray2;
            j = j3;
        }
        ArrayList acc = new ArrayList();
        HashMap bodyParts2 = new HashMap();
        Context context = app;
        if (context == null) {
            z = false;
        } else {
            if (!RobloxApi.INSTANCE.isGuest(context)) {
                z2 = true;
            } else if (RobloxApi.INSTANCE.apiKey(context).length() > 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                z = true;
            } else {
                z = false;
            }
        }
        boolean signedIn = z;
        int length = assets.length();
        int i = 0;
        String face2 = null;
        String tshirt2 = null;
        String pants3 = null;
        String shirt = null;
        while (i < length) {
            int i2 = length;
            JSONObject a = assets.getJSONObject(i);
            long id = a.optLong("id");
            Object colors2 = colors;
            JSONObject jSONObjectOptJSONObject3 = a.optJSONObject("assetType");
            if (jSONObjectOptJSONObject3 != null) {
                int type = jSONObjectOptJSONObject3.optInt("id");
                switch (type) {
                    case 2:
                        assets2 = assets;
                        bodyParts = bodyParts2;
                        tshirt2 = "rbxassetid://" + id;
                        break;
                    case 8:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 76:
                    case 77:
                        assets2 = assets;
                        bodyParts = bodyParts2;
                        try {
                            byte[] accBytes = AssetFetcher.INSTANCE.load(id);
                            acc.add(accBytes);
                            JSONObject meta = a.optJSONObject("meta");
                            if (meta != null) {
                                JSONObject mPos = meta.optJSONObject("position");
                                JSONObject mRot = meta.optJSONObject("rotation");
                                JSONObject mScl = meta.optJSONObject("scale");
                                if (mPos != null || mRot != null || mScl != null) {
                                    putAccMeta(accBytes, new float[]{
                                        mPos != null ? (float) mPos.optDouble("X", mPos.optDouble("x", 0.0d)) : 0.0f,
                                        mPos != null ? (float) mPos.optDouble("Y", mPos.optDouble("y", 0.0d)) : 0.0f,
                                        mPos != null ? (float) mPos.optDouble("Z", mPos.optDouble("z", 0.0d)) : 0.0f,
                                        mRot != null ? (float) mRot.optDouble("X", mRot.optDouble("x", 0.0d)) : 0.0f,
                                        mRot != null ? (float) mRot.optDouble("Y", mRot.optDouble("y", 0.0d)) : 0.0f,
                                        mRot != null ? (float) mRot.optDouble("Z", mRot.optDouble("z", 0.0d)) : 0.0f,
                                        mScl != null ? (float) mScl.optDouble("X", mScl.optDouble("x", 1.0d)) : 1.0f,
                                        mScl != null ? (float) mScl.optDouble("Y", mScl.optDouble("y", 1.0d)) : 1.0f,
                                        mScl != null ? (float) mScl.optDouble("Z", mScl.optDouble("z", 1.0d)) : 1.0f
                                    });
                                }
                            }
                        } catch (Exception e2) {
                            OutputLog.INSTANCE.warn("Avatar accessory " + id + ": " + e2.getMessage());
                        }
                        break;
                    case 11:
                        assets2 = assets;
                        bodyParts = bodyParts2;
                        shirt = "rbxassetid://" + id;
                        break;
                    case 12:
                        assets2 = assets;
                        bodyParts = bodyParts2;
                        pants3 = "rbxassetid://" + id;
                        break;
                    case 17:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 79:
                        if (!signedIn) {
                            assets2 = assets;
                            bodyParts = bodyParts2;
                        } else {
                            try {
                                try {
                                    assets2 = assets;
                                    try {
                                        bodyParts2.put(Integer.valueOf(type), AssetFetcher.INSTANCE.load(id));
                                        bodyParts = bodyParts2;
                                    } catch (Exception e3) {
                                        Exception e = e3;
                                        bodyParts = bodyParts2;
                                        OutputLog.INSTANCE.warn("Avatar body part " + id + ": " + e.getMessage());
                                    }
                                } catch (Exception e4) {
                                    Exception e = e4;
                                    assets2 = assets;
                                }
                            } catch (Exception e5) {
                                Exception e = e5;
                                assets2 = assets;
                            }
                        }
                        break;
                    case 18:
                        face2 = "rbxassetid://" + id;
                        assets2 = assets;
                        bodyParts = bodyParts2;
                        break;
                    default:
                        assets2 = assets;
                        bodyParts = bodyParts2;
                        break;
                }
            } else {
                assets2 = assets;
                bodyParts = bodyParts2;
            }
            i++;
            length = i2;
            colors = colors2;
            assets = assets2;
            bodyParts2 = bodyParts;
        }
        Object colors3 = colors;
        HashMap bodyParts3 = bodyParts2;
        if (signedIn) {
            String shirt2 = template(shirt, "ShirtTemplate");
            pants = template(pants3, "PantsTemplate");
            tshirt = template(tshirt2, "Graphic");
            face = template(face2, "Texture");
            pants2 = shirt2;
        } else {
            pants = pants3;
            tshirt = tshirt2;
            face = face2;
            pants2 = shirt;
        }
        boolean r15 = signedIn && Intrinsics.areEqual(j.optString("playerAvatarType", "R6"), "R15");
        JSONObject sc = j.optJSONObject("scales");
        Scales scales = new Scales((float) (sc != null ? sc.optDouble("height", 1.0d) : 1.0d), (float) (sc != null ? sc.optDouble("width", 1.0d) : 1.0d), (float) (sc != null ? sc.optDouble("depth", 1.0d) : 1.0d), (float) (sc != null ? sc.optDouble("head", 1.0d) : 1.0d));
        String shirt3 = pants2;
        return new Spec((Map) colors3, shirt3, pants, tshirt, face, acc, label, r15, scales, bodyParts3, signedIn ? composeClothing((Map) colors3, pants2, pants, tshirt, bodyParts3) : MapsKt.emptyMap(), null, 2048, null);
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final String template(String content, String prop) {
        String str;
        String string;
        Long lIdOf = AssetFetcher.INSTANCE.idOf(content);
        if (lIdOf == null) {
            return content;
        }
        long id = lIdOf.longValue();
        try {
            RbxDocument doc = RbxFile.INSTANCE.read(AssetFetcher.INSTANCE.load(id));
            Iterable roots = doc.getRoots();
            Collection arrayList = new ArrayList();
            Iterator it = roots.iterator();
            while (true) {
                str = null;
                if (!it.hasNext()) {
                    break;
                }
                Instance instance = (Instance) it.next();
                CollectionsKt.addAll(arrayList, CollectionsKt.plus((Collection) CollectionsKt.listOf(instance), (Iterable) Instance.descendants$default(instance, null, 1, null)));
            }
            for (Instance instance2 : (List<Instance>) arrayList) {
                try {
                    Object obj = instance2.get(prop);
                    if (obj == null) {
                        obj = instance2.get("Texture");
                    }
                    if (obj == null || (string = obj.toString()) == null || StringsKt.isBlank(string)) {
                        string = null;
                    }
                    if (string != null) {
                        str = string;
                        break;
                    }
                } catch (Exception e) {
                }
            }
            return str == null ? content : str;
        } catch (Exception e2) {
            return content;
        }
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0135  */
    private final Spec fromCustom(JSONObject o) {
        boolean z = false;
        HashMap colors = new HashMap(CLASSIC_COLORS);
        JSONObject jSONObjectOptJSONObject = o.optJSONObject("colors");
        if (jSONObjectOptJSONObject != null) {
            for (Map.Entry<String, String> entry : colorKeys.entrySet()) {
                String key = entry.getKey();
                Color3 color3Hex = INSTANCE.hex(jSONObjectOptJSONObject.optString(entry.getValue()));
                if (color3Hex != null) {
                    colors.put(key, color3Hex);
                }
            }
        }
        ArrayList acc = new ArrayList();
        String strOptString = o.optString("accessories");
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        Iterable iterableSplit$default = StringsKt.split((CharSequence) strOptString, new char[]{',', ' ', '\n'}, false, 0);
        Collection arrayList = new ArrayList();
        Iterator it = iterableSplit$default.iterator();
        while (it.hasNext()) {
            Iterable iterable = iterableSplit$default;
            Long assetId = RobloxApi.INSTANCE.parseAssetId((String) it.next());
            if (assetId != null) {
                arrayList.add(assetId);
            }
            iterableSplit$default = iterable;
        }
        List list = (List) arrayList;
        Iterator it2 = CollectionsKt.take(list, 12).iterator();
        while (it2.hasNext()) {
            long a = ((Number) it2.next()).longValue();
            try {
                acc.add(AssetFetcher.INSTANCE.load(a));
            } catch (Exception e) {
                OutputLog.INSTANCE.warn("Avatar accessory " + a + ": " + e.getMessage());
            }
        }
        String shirt = fromCustom$id(o, "shirt");
        String pants = fromCustom$id(o, "pants");
        String tshirt = fromCustom$id(o, "tshirt");
        String face = fromCustom$id(o, "face");
        Context context = app;
        boolean signedIn = false;
        if (context != null) {
            if (!RobloxApi.INSTANCE.isGuest(context)) {
                z = true;
            } else if (RobloxApi.INSTANCE.apiKey(context).length() > 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                signedIn = true;
            }
        }
        if (signedIn) {
            shirt = template(shirt, "ShirtTemplate");
            pants = template(pants, "PantsTemplate");
            tshirt = template(tshirt, "Graphic");
            face = template(face, "Texture");
        }
        return new Spec(colors, shirt, pants, tshirt, face, acc, "Custom", false, null, null, null, null, 3968, null);
    }

    private static final String fromCustom$id(JSONObject $o, String k) {
        RobloxApi robloxApi = RobloxApi.INSTANCE;
        String strOptString = $o.optString(k);
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        Long assetId = robloxApi.parseAssetId(strOptString);
        if (assetId == null) {
            return null;
        }
        return "rbxassetid://" + assetId.longValue();
    }

    public final List<Instance> applyTo(Instance model, Spec spec) {
        Color3 color3;
        Intrinsics.checkNotNullParameter(model, "model");
        Intrinsics.checkNotNullParameter(spec, "spec");
        for (String name : PARTS) {
            Instance p = Instance.findFirstChild$default(model, name, false, 2, null);
            if (p != null && (color3 = spec.getColors().get(name)) != null) {
                PartData.INSTANCE.setColor(p, color3);
            }
        }
        Instance instanceFindFirstChildOfClass = model.findFirstChildOfClass("BodyColors");
        if (instanceFindFirstChildOfClass != null) {
            Color3 color4 = spec.getColors().get("Head");
            if (color4 != null) {
                instanceFindFirstChildOfClass.set("HeadColor3", color4, false);
            }
            Color3 color5 = spec.getColors().get("Torso");
            if (color5 != null) {
                instanceFindFirstChildOfClass.set("TorsoColor3", color5, false);
            }
            Color3 color6 = spec.getColors().get("Left Arm");
            if (color6 != null) {
                instanceFindFirstChildOfClass.set("LeftArmColor3", color6, false);
            }
            Color3 color7 = spec.getColors().get("Right Arm");
            if (color7 != null) {
                instanceFindFirstChildOfClass.set("RightArmColor3", color7, false);
            }
            Color3 color8 = spec.getColors().get("Left Leg");
            if (color8 != null) {
                instanceFindFirstChildOfClass.set("LeftLegColor3", color8, false);
            }
            Color3 color9 = spec.getColors().get("Right Leg");
            if (color9 != null) {
                instanceFindFirstChildOfClass.set("RightLegColor3", color9, false);
            }
        }
        Iterable children = model.getChildren();
        Collection arrayList = new ArrayList();
        for (Object obj : children) {
            Instance instance = (Instance) obj;
            if (Intrinsics.areEqual(instance.getClassName(), "Shirt") || Intrinsics.areEqual(instance.getClassName(), "Pants") || Intrinsics.areEqual(instance.getClassName(), "ShirtGraphic") || instance.isA("Accoutrement")) {
                arrayList.add(obj);
            }
        }
        Iterator it = ((List) arrayList).iterator();
        while (it.hasNext()) {
            ((Instance) it.next()).destroy();
        }
        String shirt = spec.getShirt();
        if (shirt != null) {
            Instance instanceCreate = InstanceFactory.INSTANCE.create("Shirt");
            instanceCreate.setName("Shirt");
            instanceCreate.set("ShirtTemplate", shirt, false);
            Instance.setParent$default(instanceCreate, model, 0, 2, null);
        }
        String pants = spec.getPants();
        if (pants != null) {
            Instance instanceCreate2 = InstanceFactory.INSTANCE.create("Pants");
            instanceCreate2.setName("Pants");
            instanceCreate2.set("PantsTemplate", pants, false);
            Instance.setParent$default(instanceCreate2, model, 0, 2, null);
        }
        String tshirt = spec.getTshirt();
        if (tshirt != null) {
            Instance instanceCreate3 = InstanceFactory.INSTANCE.create("ShirtGraphic");
            instanceCreate3.setName("Shirt Graphic");
            instanceCreate3.set("Graphic", tshirt, false);
            Instance.setParent$default(instanceCreate3, model, 0, 2, null);
        }
        String face = spec.getFace();
        Instance faceHead = Instance.findFirstChild$default(model, "Head", false, 2, null);
        if (faceHead != null) {
            Instance faceDecal = null;
            boolean dynamicHead = false;
            for (Instance ch : faceHead.getChildren()) {
                if ("Decal".equals(ch.getClassName()) && "face".equalsIgnoreCase(ch.getName())) faceDecal = ch;
                if ("FaceControls".equals(ch.getClassName())) dynamicHead = true;
            }
            if (faceDecal == null && (face != null || !dynamicHead)) {
                faceDecal = InstanceFactory.INSTANCE.create("Decal");
                faceDecal.setName("face");
                faceDecal.set("Face", new EnumValue(5), false);
                Instance.setParent$default(faceDecal, faceHead, 0, 2, null);
            }
            if (faceDecal != null) {
                faceDecal.set("Texture", face != null ? face : "rbxasset://textures/face.png", false);
            }
        }
        for (Map.Entry<String, List<Pair<String, Vector3>>> entry : r6Attachments.entrySet()) {
            String partName = entry.getKey();
            List<Pair<String, Vector3>> value = entry.getValue();
            Instance p2 = Instance.findFirstChild$default(model, partName, false, 2, null);
            if (p2 != null) {
                for (Pair<String, Vector3> pair : value) {
                    String n = pair.component1();
                    Vector3 off = pair.component2();
                    if (Instance.findFirstChild$default(p2, n, false, 2, null) == null) {
                        Instance instanceCreate4 = InstanceFactory.INSTANCE.create("Attachment");
                        instanceCreate4.setName(n);
                        instanceCreate4.set("CFrame", CFrame.INSTANCE.at(off), false);
                        instanceCreate4.attachRaw(p2);
                    }
                }
            }
        }
        return addAccessories(model, spec);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r0v24 */
    /* JADX WARN: Type inference failed for: r0v26 */
    /* JADX WARN: Type inference failed for: r10v2, types: [com.goydevv.studrblx.model.Instance] */
    /* JADX WARN: Type inference failed for: r6v3 */
    /* JADX WARN: Type inference failed for: r6v4, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r6v6 */
    /* JADX WARN: Type inference failed for: r7v3, types: [com.goydevv.studrblx.model.Instance, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r7v4 */
    /* JADX WARN: Type inference failed for: r7v5 */
    private final List<Instance> addAccessories(Instance model, Spec spec) {
        Instance instance;
        boolean z = false;
        Object next;
        Iterator<byte[]> it;
        byte[] bArr;
        Iterator<Instance> it2;
        int r6 = 0;
        Object next2;
        ArrayList arrayList = new ArrayList();
        Iterator<byte[]> it3 = spec.getAccessories().iterator();
        while (it3.hasNext()) {
            byte[] next3 = it3.next();
            try {
                RbxDocument rbxDocument = RbxFile.INSTANCE.read(next3);
                Iterator<Instance> it4 = rbxDocument.getRoots().iterator();
                while (it4.hasNext()) {
                    Instance next4 = it4.next();
                    boolean z2 = true;
                    if (next4.isA("Accoutrement")) {
                        instance = next4;
                    } else {
                        Iterator it5 = Instance.descendants$default(next4, null, 1, null).iterator();
                        do {
                            if (!it5.hasNext()) {
                                next2 = null;
                                break;
                            }
                            next2 = it5.next();
                        } while (!((Instance) next2).isA("Accoutrement"));
                        instance = (Instance) next2;
                        if (instance == null) {
                            continue;
                        }
                    }
                    Instance instanceFindFirstChild$default = Instance.findFirstChild$default(instance, "Handle", false, 2, null);
                    if (instanceFindFirstChild$default == null) {
                        for (Instance chPart : instance.getChildren()) {
                            if (chPart.isA("BasePart")) {
                                instanceFindFirstChild$default = chPart;
                                break;
                            }
                        }
                    }
                    if (instanceFindFirstChild$default != null) {
                        boolean zIsA = instanceFindFirstChild$default.isA("BasePart");
                        Instance r7 = instanceFindFirstChild$default;
                        if (!zIsA) {
                            r7 = null;
                        }
                        if (r7 != null) {
                            Iterator it6 = r7.getChildren().iterator();
                            while (true) {
                                if (it6.hasNext()) {
                                    next = it6.next();
                                    Instance instance2 = (Instance) next;
                                    z = z2;
                                    if ((!Intrinsics.areEqual(instance2.getClassName(), "Attachment") || INSTANCE.findBodyAttachment(model, instance2.getName()) == null) ? false : z) {
                                        break;
                                    }
                                    z2 = z;
                                } else {
                                    z = z2;
                                    next = null;
                                    break;
                                }
                            }
                            Instance instance3 = (Instance) next;
                            Instance Create = InstanceFactory.INSTANCE.create("Weld");
                            Create.setName("AccessoryWeld");
                            RbxDocument rbxDocument2 = rbxDocument;
                            if (instance3 != null) {
                                it = it3;
                                bArr = next3;
                                Instance instanceFindBodyAttachment = findBodyAttachment(model, instance3.getName());
                                Intrinsics.checkNotNull(instanceFindBodyAttachment);
                                Instance parent = instanceFindBodyAttachment.getParent();
                                Intrinsics.checkNotNull(parent);
                                Object obj = instanceFindBodyAttachment.get("CFrame");
                                CFrame identity = obj instanceof CFrame ? (CFrame) obj : null;
                                if (identity == null) {
                                    identity = CFrame.INSTANCE.getIDENTITY();
                                }
                                Object obj2 = instance3.get("CFrame");
                                it2 = it4;
                                CFrame identity2 = obj2 instanceof CFrame ? (CFrame) obj2 : null;
                                if (identity2 == null) {
                                    identity2 = CFrame.INSTANCE.getIDENTITY();
                                }
                                r6 = 0;
                                float[] adj = getAccMeta(bArr);
                                if (adj != null) {
                                    float sx = RangesKt.coerceIn(adj[6], 0.5f, 2.0f);
                                    float sy = RangesKt.coerceIn(adj[7], 0.5f, 2.0f);
                                    float sz = RangesKt.coerceIn(adj[8], 0.5f, 2.0f);
                                    if (Math.abs(sx - 1.0f) > 0.001f || Math.abs(sy - 1.0f) > 0.001f || Math.abs(sz - 1.0f) > 0.001f) {
                                        Vector3 curSz = PartData.INSTANCE.size(r7);
                                        r7.set("Size", new Vector3(curSz.getX() * sx, curSz.getY() * sy, curSz.getZ() * sz), false);
                                        for (Instance sm : r7.getChildren()) {
                                            if ("SpecialMesh".equals(sm.getClassName()) || "FileMesh".equals(sm.getClassName())) {
                                                Object scObj = sm.get("Scale");
                                                Vector3 curSc = scObj instanceof Vector3 ? (Vector3) scObj : new Vector3(1.0f, 1.0f, 1.0f);
                                                sm.set("Scale", new Vector3(curSc.getX() * sx, curSc.getY() * sy, curSc.getZ() * sz), false);
                                            }
                                        }
                                        Vector3 c1p = identity2.getPosition();
                                        identity2 = new CFrame(c1p.getX() * sx, c1p.getY() * sy, c1p.getZ() * sz,
                                                identity2.getR00(), identity2.getR01(), identity2.getR02(),
                                                identity2.getR10(), identity2.getR11(), identity2.getR12(),
                                                identity2.getR20(), identity2.getR21(), identity2.getR22());
                                    }
                                    if (Math.abs(adj[0]) > 1.0E-4f || Math.abs(adj[1]) > 1.0E-4f || Math.abs(adj[2]) > 1.0E-4f) {
                                        identity = identity.times(CFrame.INSTANCE.at(new Vector3(adj[0], adj[1], adj[2])));
                                    }
                                    if (Math.abs(adj[3]) > 1.0E-3f) identity = identity.times(CFrame.INSTANCE.rx((float) Math.toRadians(adj[3])));
                                    if (Math.abs(adj[4]) > 1.0E-3f) identity = identity.times(CFrame.INSTANCE.ry((float) Math.toRadians(adj[4])));
                                    if (Math.abs(adj[5]) > 1.0E-3f) identity = identity.times(CFrame.INSTANCE.rz((float) Math.toRadians(adj[5])));
                                }
                                Create.set("Part0", parent, false);
                                Create.set("Part1", r7, false);
                                Create.set("C0", identity, false);
                                Create.set("C1", identity2, false);
                                r7.set("CFrame", PartData.INSTANCE.cframe(parent).times(identity).times(identity2.inverse()), false);
                            } else {
                                it = it3;
                                bArr = next3;
                                it2 = it4;
                                Instance instanceFindFirstChild$default2 = Instance.findFirstChild$default(model, "Head", false, 2, null);
                                if (instanceFindFirstChild$default2 == null) {
                                    rbxDocument = rbxDocument2;
                                    it3 = it;
                                    next3 = bArr;
                                    it4 = it2;
                                } else {
                                    Object obj3 = instance.get("AttachmentPoint");
                                    CFrame identity3 = obj3 instanceof CFrame ? (CFrame) obj3 : null;
                                    if (identity3 == null) {
                                        identity3 = CFrame.INSTANCE.getIDENTITY();
                                    }
                                    CFrame cFrame = new CFrame(0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4088, null);
                                    r6 = 0;
                                    Create.set("Part0", instanceFindFirstChild$default2, false);
                                    Create.set("Part1", r7, false);
                                    Create.set("C0", cFrame, false);
                                    Create.set("C1", identity3, false);
                                    r7.set("CFrame", PartData.INSTANCE.cframe(instanceFindFirstChild$default2).times(cFrame).times(identity3.inverse()), false);
                                }
                            }
                            r7.set("CanCollide", Boolean.valueOf(r6 != 0), r6 != 0);
                            r7.set("Massless", Boolean.valueOf(z), r6 != 0);
                            r7.set("Anchored", Boolean.valueOf(z), r6 != 0);
                            Create.attachRaw(r7);
                            Instance.setParent$default(instance, model, r6, 2, null);
                            arrayList.add(r7);
                            List listDescendants$default = Instance.descendants$default(instance, null, z ? 1 : 0, null);
                            ArrayList arrayList2 = new ArrayList();
                            for (Object obj4 : listDescendants$default) {
                                Instance instance4 = (Instance) obj4;
                                if (((!instance4.isA("BasePart") || instance4 == r7) ? r6 : 1) != 0) {
                                    arrayList2.add(obj4);
                                }
                            }
                            arrayList.addAll(arrayList2);
                            rbxDocument = rbxDocument2;
                            it3 = it;
                            next3 = bArr;
                            it4 = it2;
                        }
                    }
                }
            } catch (Exception e) {
                OutputLog.INSTANCE.warn("Accessory attach: " + e.getMessage());
            }
        }
        return arrayList;
    }

    private final Instance findBodyAttachment(Instance model, String name) {
        Instance instanceFindFirstChild$default;
        for (String p : (List<String>) CollectionsKt.plus((Collection) PARTS, (Iterable) R15_PARTS)) {
            Instance instanceFindFirstChild$default2 = Instance.findFirstChild$default(model, p, false, 2, null);
            if (instanceFindFirstChild$default2 != null && (instanceFindFirstChild$default = Instance.findFirstChild$default(instanceFindFirstChild$default2, name, false, 2, null)) != null && Intrinsics.areEqual(instanceFindFirstChild$default.getClassName(), "Attachment")) {
                return instanceFindFirstChild$default;
            }
        }
        return null;
    }

    public final List<String> getR15_PARTS() {
        return R15_PARTS;
    }

    /* JADX INFO: compiled from: Avatar.kt */
    private static final class Group {
        private final int assetType;
        private final String colorKey;
        private final List<String> parts;

        public Group(List<String> parts, String colorKey, int assetType) {
            Intrinsics.checkNotNullParameter(parts, "parts");
            Intrinsics.checkNotNullParameter(colorKey, "colorKey");
            this.parts = parts;
            this.colorKey = colorKey;
            this.assetType = assetType;
        }

        public final int getAssetType() {
            return this.assetType;
        }

        public final String getColorKey() {
            return this.colorKey;
        }

        public final List<String> getParts() {
            return this.parts;
        }
    }

    private final String groupOf(String part) {
        Object next;
        Iterable iterableEntrySet = GROUPS.entrySet();
        Intrinsics.checkNotNullExpressionValue(iterableEntrySet, "<get-entries>(...)");
        Iterator it = iterableEntrySet.iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!((Group) ((Map.Entry) next).getValue()).getParts().contains(part));
        Map.Entry entry = (Map.Entry) next;
        if (entry != null) {
            return (String) entry.getKey();
        }
        return null;
    }

    /* JADX INFO: compiled from: Avatar.kt */
    private static final class Tpl {
        private final List<Pair<String, Vector3>> atts;
        private final long mesh;
        private final String name;
        private final Vector3 size;

        public Tpl(String name, Vector3 size, long mesh, List<Pair<String, Vector3>> atts) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(size, "size");
            Intrinsics.checkNotNullParameter(atts, "atts");
            this.name = name;
            this.size = size;
            this.mesh = mesh;
            this.atts = atts;
        }

        public final List<Pair<String, Vector3>> getAtts() {
            return this.atts;
        }

        public final long getMesh() {
            return this.mesh;
        }

        public final String getName() {
            return this.name;
        }

        public final Vector3 getSize() {
            return this.size;
        }
    }

    private final Vector3 v(float x, float y, float z) {
        return new Vector3(x, y, z);
    }

    private final Instance attachment(String name, Vector3 pos) {
        Instance instanceCreate = InstanceFactory.INSTANCE.create("Attachment");
        instanceCreate.setName(name);
        instanceCreate.set("CFrame", CFrame.INSTANCE.at(pos), false);
        return instanceCreate;
    }

    private final Instance templatePart(Tpl t) {
        Instance p = InstanceFactory.INSTANCE.create("MeshPart");
        p.setName(t.getName());
        p.set("size", t.getSize(), false);
        p.set("MeshId", "rbxassetid://" + t.getMesh(), false);
        p.set("TextureID", Intrinsics.areEqual(t.getName(), "Head") ? "rbxassetid://111092388570647" : "", false);
        for (Pair<String, Vector3> pair : t.getAtts()) {
            String n = pair.component1();
            Vector3 pos = pair.component2();
            attachment(n, pos).attachRaw(p);
        }
        return p;
    }

    /* JADX WARN: Code duplicated, block: B:38:0x00e9  */
    /* JADX WARN: Code duplicated, block: B:58:0x0154  */
    /* JADX WARN: Code duplicated, block: B:85:0x00ec A[SYNTHETIC] */
    private final List<Instance> r15PartsIn(byte[] bytes) {
        Object obj;
        boolean z = false;
        boolean z2 = false;
        Object next;
        RbxDocument doc;
        boolean z3 = false;
        Instance instance;
        try {
            try {
                RbxDocument doc2 = RbxFile.INSTANCE.read(bytes);
                Iterable roots = doc2.getRoots();
                Collection arrayList = new ArrayList();
                Iterator it = roots.iterator();
                while (true) {
                    obj = null;
                    z = true;
                    if (!it.hasNext()) {
                        break;
                    }
                    Instance instance2 = (Instance) it.next();
                    CollectionsKt.addAll(arrayList, CollectionsKt.plus((Collection) CollectionsKt.listOf(instance2), (Iterable) Instance.descendants$default(instance2, null, 1, null)));
                }
                Iterable all = (List) arrayList;
                for (String folder : CollectionsKt.listOf(new String[]{"R15ArtistIntent", "R15Fixed", "R15"})) {
                    Iterator it2 = all.iterator();
                    do {
                        if (!it2.hasNext()) {
                            next = obj;
                            break;
                        }
                        next = it2.next();
                        instance = (Instance) next;
                    } while (!((Intrinsics.areEqual(instance.getClassName(), "Folder") && Intrinsics.areEqual(instance.getName(), folder)) ? z : false));
                    Instance f = (Instance) next;
                    if (f != null) {
                        Iterable children = f.getChildren();
                        Collection arrayList2 = new ArrayList();
                        for (Object obj2 : children) {
                            Instance instance3 = (Instance) obj2;
                            if (instance3.isA("BasePart")) {
                                Avatar avatar = INSTANCE;
                                doc = doc2;
                                z3 = R15_PARTS.contains(instance3.getName());
                                if (z3) {
                                    arrayList2.add(obj2);
                                }
                                doc2 = doc;
                            } else {
                                doc = doc2;
                            }
                            if (z3) {
                                arrayList2.add(obj2);
                            }
                            doc2 = doc;
                        }
                        RbxDocument doc3 = doc2;
                        List parts = (List) arrayList2;
                        if (!parts.isEmpty()) {
                            return parts;
                        }
                        doc2 = doc3;
                        obj = null;
                        z = true;
                    }
                }
                Collection arrayList3 = new ArrayList();
                for (Object obj3 : all) {
                    Instance instance4 = (Instance) obj3;
                    if (instance4.isA("BasePart")) {
                        Avatar avatar2 = INSTANCE;
                        if (R15_PARTS.contains(instance4.getName())) {
                            Instance parent = instance4.getParent();
                            if (Intrinsics.areEqual(parent != null ? parent.getName() : null, "R6")) {
                                z2 = false;
                            } else {
                                z2 = true;
                            }
                        } else {
                            z2 = false;
                        }
                    } else {
                        z2 = false;
                    }
                    if (z2) {
                        arrayList3.add(obj3);
                    }
                }
                return (List) arrayList3;
            } catch (Exception e) {
                return CollectionsKt.emptyList();
            }
        } catch (Exception e2) {
            return CollectionsKt.emptyList();
        }
    }

    private final Bitmap bitmapOf(String content) {
        Long lIdOf = AssetFetcher.INSTANCE.idOf(content);
        if (lIdOf == null) {
            return null;
        }
        return AssetFetcher.INSTANCE.imageBlocking(lIdOf.longValue());
    }

    public final Map<String, Bitmap> composeClothing(Map<String, Color3> colors, String shirt, String pants, String tshirt, final Map<Integer, byte[]> bodyParts) {
        Intrinsics.checkNotNullParameter(colors, "colors");
        Intrinsics.checkNotNullParameter(bodyParts, "bodyParts");
        return composeClothingBitmaps(colors, bitmapOf(shirt), bitmapOf(pants), bitmapOf(tshirt), new Function1() { // from class: com.goydevv.studrblx.engine.Avatar$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Avatar.composeClothing$lambda$51(bodyParts, ((Integer) obj).intValue());
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0033  */
    /* JADX WARN: Code duplicated, block: B:18:? A[RETURN, SYNTHETIC] */
    static final Bitmap composeClothing$lambda$51(Map $bodyParts, int g) {
        String strTextureContent;
        byte[] bArr = (byte[]) $bodyParts.get(Integer.valueOf(g));
        if (bArr == null) {
            return null;
        }
        Iterator it = INSTANCE.r15PartsIn(bArr).iterator();
        while (it.hasNext()) {
            strTextureContent = PartData.INSTANCE.textureContent((Instance) it.next());
            if (strTextureContent != null) {
                if (strTextureContent != null) {
                    return INSTANCE.bitmapOf(strTextureContent);
                }
                return null;
            }
        }
        strTextureContent = null;
        if (strTextureContent != null) {
            return INSTANCE.bitmapOf(strTextureContent);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Map<String, Bitmap> composeClothingBitmaps$default(Avatar avatar, Map map, Bitmap bitmap, Bitmap bitmap2, Bitmap bitmap3, Function1 function1, int i, Object obj) {
        Function1 function2;
        if ((i & 16) == 0) {
            function2 = function1;
        } else {
            function2 = new Function1() { // from class: com.goydevv.studrblx.engine.Avatar.composeClothingBitmaps.1
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                    return invoke(((Number) p1).intValue());
                }

                public final Void invoke(int it) {
                    return null;
                }
            };
        }
        return avatar.composeClothingBitmaps(map, bitmap, bitmap2, bitmap3, function2);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:23:0x007d  */
    /* JADX WARN: Code duplicated, block: B:24:0x0082  */
    public final Map<String, Bitmap> composeClothingBitmaps(Map<String, Color3> colors, Bitmap shirtB, Bitmap pantsB, Bitmap teeB, Function1<? super Integer, Bitmap> ownTexture) {
        List layers;
        int i;
        Map<String, Color3> colors2 = colors;
        Function1<? super Integer, Bitmap> ownTexture2 = ownTexture;
        Intrinsics.checkNotNullParameter(colors2, "colors");
        Intrinsics.checkNotNullParameter(ownTexture2, "ownTexture");
        if (shirtB == null && pantsB == null && teeB == null) {
            return MapsKt.emptyMap();
        }
        HashMap out = new HashMap();
        int i2 = 2;
        Paint paint = new Paint(2);
        ClothingCompositor cc = ClothingCompositor.INSTANCE;
        Iterator<Map.Entry<String, Group>> it = GROUPS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Group> next = it.next();
            String name = next.getKey();
            Group g = next.getValue();
            boolean torso = Intrinsics.areEqual(name, "Torso");
            switch (name) {
                case "RightArm":
                    layers = CollectionsKt.listOfNotNull(shirtB);
                    break;
                case "Torso":
                    Bitmap[] bitmapArr = new Bitmap[i2];
                    bitmapArr[0] = pantsB;
                    bitmapArr[1] = shirtB;
                    layers = CollectionsKt.listOfNotNull(bitmapArr);
                    break;
                case "LeftArm":
                    layers = CollectionsKt.listOfNotNull(shirtB);
                    break;
                default:
                    layers = CollectionsKt.listOfNotNull(pantsB);
                    break;
            }
            Bitmap tee = torso ? teeB : null;
            if (!layers.isEmpty() || tee != null) {
                int w = torso ? ClothingCompositor.TORSO_W : ClothingCompositor.LIMB_W;
                int h = torso ? ClothingCompositor.TORSO_H : ClothingCompositor.LIMB_H;
                Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Intrinsics.checkNotNullExpressionValue(bmp, "createBitmap(...)");
                Canvas c = new Canvas(bmp);
                HashMap out2 = out;
                Color3 col = colors2.get(g.getColorKey());
                if (col == null) {
                    col = Color3.INSTANCE.fromRGB(163, 162, 165);
                }
                List<Bitmap> layers2 = layers;
                c.drawColor(Color.rgb(RangesKt.coerceIn((int) (col.getR() * 255.0f), 0, 255), RangesKt.coerceIn((int) (col.getG() * 255.0f), 0, 255), RangesKt.coerceIn((int) (col.getB() * 255.0f), 0, 255)));
                Bitmap bitmapInvoke = ownTexture2.invoke(Integer.valueOf(g.getAssetType()));
                if (bitmapInvoke != null) {
                    c.drawBitmap(bitmapInvoke, (Rect) null, new Rect(0, 0, w, h), paint);
                }
                ClothingCompositor.Limb limb = (Intrinsics.areEqual(name, "LeftArm") || Intrinsics.areEqual(name, "LeftLeg")) ? cc.getLEFT() : cc.getRIGHT();
                for (Bitmap l : layers2) {
                    if (torso) {
                        cc.drawTorso(c, l);
                    } else {
                        cc.drawLimb(c, l, limb);
                    }
                }
                if (tee != null) {
                    i = 2;
                    c.drawBitmap(tee, (Rect) null, new Rect(2, 74, 130, 202), paint);
                } else {
                    i = 2;
                }
                out2.put(name, bmp);
                colors2 = colors;
                i2 = i;
                out = out2;
                ownTexture2 = ownTexture;
            }
        }
        return out;
    }

    public final String r15GroupOf(String part) {
        Intrinsics.checkNotNullParameter(part, "part");
        return groupOf(part);
    }

    public final String r15ColorKey(String group) {
        Intrinsics.checkNotNullParameter(group, "group");
        Group group2 = GROUPS.get(group);
        if (group2 != null) {
            return group2.getColorKey();
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:144:0x05d9  */
    /* JADX WARN: Code duplicated, block: B:168:0x065c A[LOOP:13: B:158:0x0629->B:168:0x065c, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:229:0x080c A[LOOP:16: B:207:0x07a3->B:229:0x080c, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:252:0x0879 A[LOOP:17: B:242:0x0847->B:252:0x0879, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:295:0x0939 A[LOOP:19: B:285:0x0908->B:295:0x0939, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:351:0x01b2 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:35:0x01a7 A[LOOP:4: B:23:0x016c->B:35:0x01a7, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:368:0x05dc A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:381:0x0667 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:402:0x0817 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:405:0x0883 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:407:0x0943 A[SYNTHETIC] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public final Instance buildR15(String name, Spec spec) {
        int i;
        int i2;
        int i3;
        Instance instance;
        char c;
        Iterator<Tpl> it;
        ArrayDeque arrayDeque;
        Object next;
        Tpl tpl;
        Object next2;
        Instance instance2;
        Tpl tpl2;
        Object next3;
        LinkedHashMap linkedHashMap;
        Object next4;
        int i4;
        boolean z = false;
        List<Pair<String, Vector3>> atts;
        ArrayList<Instance> arrayList;
        boolean z2 = false;
        List<Tpl> list;
        boolean z3 = false;
        boolean z4;
        Iterator it2;
        Iterator it3;
        Object next5;
        boolean z5 = false;
        ArrayList<Instance> arrayList2;
        int i5;
        boolean z6 = false;
        String colorKey;
        Bitmap bitmap;
        Object next6;
        List<Instance> list2;
        int i6 = 0;
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(spec, "spec");
        Instance instanceCreate = InstanceFactory.INSTANCE.create("Model");
        instanceCreate.setName(name);
        Instance instanceCreate2 = InstanceFactory.INSTANCE.create("Part");
        instanceCreate2.setName("HumanoidRootPart");
        boolean z7 = false;
        instanceCreate2.set("size", new Vector3(2.0f, 2.0f, 1.0f), false);
        instanceCreate2.set("Transparency", Float.valueOf(1.0f), false);
        String str = "CanCollide";
        instanceCreate2.set("CanCollide", false, false);
        float f = 0.0f;
        attachment("RootRigAttachment", new Vector3(0.0f, -1.0f, 0.0f)).attachRaw(instanceCreate2);
        attachment("RootAttachment", Vector3.INSTANCE.getZERO()).attachRaw(instanceCreate2);
        instanceCreate2.attachRaw(instanceCreate);
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        for (Tpl tpl3 : DEFAULT_R15) {
            float f2 = f;
            linkedHashMap2.put(tpl3.getName(), templatePart(tpl3));
            f = f2;
        }
        float f3 = f;
        boolean z8 = z7;
        Iterator<Map.Entry<Integer, byte[]>> it4 = spec.getBodyParts().entrySet().iterator();
        while (it4.hasNext()) {
            Map.Entry<Integer, byte[]> next7 = it4.next();
            int iIntValue = next7.getKey().intValue();
            byte[] value = next7.getValue();
            switch (iIntValue) {
                case 17:
                case 79:
                    break;
                default:
                    for (Instance instance3 : r15PartsIn(value)) {
                        if (!Intrinsics.areEqual(instance3.getName(), "Head")) {
                            linkedHashMap2.put(instance3.getName(), instance3.cloneTree());
                        }
                    }
                    break;
            }
        }
        int i7 = 2;
        byte[][] bArr = new byte[2][];
        bArr[z8 ? 1 : 0] = spec.getBodyParts().get(79);
        int i8 = 17;
        int i9 = 1;
        bArr[1] = spec.getBodyParts().get(17);
        Iterator it5 = CollectionsKt.listOfNotNull(bArr).iterator();
        while (true) {
            i = i8;
            if (it5.hasNext()) {
                i2 = i7;
                List<Instance> listR15PartsIn = INSTANCE.r15PartsIn((byte[]) it5.next());
                Iterator it6 = listR15PartsIn.iterator();
                while (true) {
                    if (it6.hasNext()) {
                        next6 = it6.next();
                        i3 = i9;
                        Instance instance4 = (Instance) next6;
                        list2 = listR15PartsIn;
                        if (Intrinsics.areEqual(instance4.getName(), "Head") && (!Intrinsics.areEqual(instance4.getClassName(), "MeshPart") || PartData.INSTANCE.meshContent(instance4) != null)) {
                            break;
                        }
                        i9 = i3;
                        listR15PartsIn = list2;
                    } else {
                        i3 = i9;
                        next6 = null;
                        break;
                    }
                }
                instance = (Instance) next6;
                if (instance == null) {
                    i8 = i;
                    i7 = i2;
                    i9 = i3;
                } else {
                    break;
                }
            } else {
                i2 = i7;
                i3 = i9;
                instance = null;
                break;
            }
        }
        if (instance == null && (spec.getBodyParts().get(79) != null || spec.getBodyParts().get(Integer.valueOf(i)) != null)) {
            OutputLog.INSTANCE.warn("Avatar: the worn head has no mesh this app can read; using the default head");
        }
        if (instance != null) {
            LinkedHashMap linkedHashMap3 = linkedHashMap2;
            Instance instanceCloneTree = instance.cloneTree();
            c = 3;
            if (PartData.INSTANCE.transparency(instanceCloneTree) >= 0.99f) {
                instanceCloneTree.set("Transparency", Float.valueOf(f3), z8);
            }
            linkedHashMap3.put("Head", instanceCloneTree);
        } else {
            c = 3;
            if (spec.getFace() != null) {
                LinkedHashMap linkedHashMap4 = linkedHashMap2;
                Instance instanceCreate3 = InstanceFactory.INSTANCE.create("Part");
                instanceCreate3.setName("Head");
                instanceCreate3.set("size", new Vector3(2.0f, 1.0f, 1.0f), false);
                Instance instanceCreate4 = InstanceFactory.INSTANCE.create("SpecialMesh");
                instanceCreate4.setName("Mesh");
                instanceCreate4.set("MeshType", new EnumValue(0), false);
                instanceCreate4.set("Scale", new Vector3(1.25f, 1.25f, 1.25f), false);
                instanceCreate4.attachRaw(instanceCreate3);
                Instance instanceCreate5 = InstanceFactory.INSTANCE.create("Decal");
                instanceCreate5.setName("face");
                instanceCreate5.set("Texture", spec.getFace(), false);
                instanceCreate5.set("Face", new EnumValue(5), false);
                instanceCreate5.attachRaw(instanceCreate3);
                Pair[] pairArr = new Pair[5];
                pairArr[0] = TuplesKt.to("NeckRigAttachment", INSTANCE.v(f3, -0.5f, f3));
                pairArr[i3] = TuplesKt.to("HatAttachment", INSTANCE.v(f3, 0.6f, f3));
                pairArr[i2] = TuplesKt.to("HairAttachment", INSTANCE.v(f3, 0.6f, f3));
                pairArr[3] = TuplesKt.to("FaceFrontAttachment", INSTANCE.v(f3, f3, -0.6f));
                pairArr[4] = TuplesKt.to("FaceCenterAttachment", INSTANCE.v(f3, f3, f3));
                for (Pair pair : CollectionsKt.listOf(pairArr)) {
                    INSTANCE.attachment((String) pair.component1(), (Vector3) pair.component2()).attachRaw(instanceCreate3);
                }
                linkedHashMap4.put("Head", instanceCreate3);
            }
        }
        texSerial++;
        int i10 = texSerial;
        Scales scales = spec.getScales();
        Iterator it7 = linkedHashMap2.entrySet().iterator();
        while (it7.hasNext()) {
            Map.Entry entry = (Map.Entry) it7.next();
            String str2 = (String) entry.getKey();
            Instance instance5 = (Instance) entry.getValue();
            Scales scales2 = scales;
            List listDescendants$default = Instance.descendants$default(instance5, null, i3, null);
            int i11 = 0;
            ArrayList arrayList3 = new ArrayList();
            for (Object obj : listDescendants$default) {
                List list3 = listDescendants$default;
                Iterator it8 = it7;
                Instance instance6 = (Instance) obj;
                int i12 = i11;
                if (instance6.isA("JointInstance") || Intrinsics.areEqual(instance6.getClassName(), "WeldConstraint")) {
                    arrayList3.add(obj);
                }
                listDescendants$default = list3;
                it7 = it8;
                i11 = i12;
            }
            Iterator it9 = it7;
            Iterator it10 = arrayList3.iterator();
            while (it10.hasNext()) {
                ((Instance) it10.next()).destroy();
            }
            instance5.set("Anchored", false, false);
            instance5.set(str, Boolean.valueOf(Intrinsics.areEqual(str2, "UpperTorso") || Intrinsics.areEqual(str2, "LowerTorso") || Intrinsics.areEqual(str2, "Head")), false);
            instance5.set("Massless", false, false);
            if (Intrinsics.areEqual(str2, "Head")) {
                colorKey = "Head";
            } else {
                Group group = GROUPS.get(groupOf(str2));
                colorKey = group != null ? group.getColorKey() : null;
            }
            if (colorKey != null) {
                Color3 color3 = spec.getColors().get(colorKey);
                if (color3 != null) {
                    PartData.INSTANCE.setColor(instance5, color3);
                    Unit unit = Unit.INSTANCE;
                    Unit unit2 = Unit.INSTANCE;
                }
            }
            String strGroupOf = groupOf(str2);
            if (strGroupOf != null && (bitmap = spec.getClothing().get(strGroupOf)) != null) {
                String str3 = TEX_PREFIX + i10 + "/" + strGroupOf;
                generated.put(str3, bitmap);
                instance5.set("TextureID", str3, false);
                Unit unit3 = Unit.INSTANCE;
                Unit unit4 = Unit.INSTANCE;
            }
            Vector3 vector3 = Intrinsics.areEqual(str2, "Head") ? new Vector3(scales2.getHead(), scales2.getHead(), scales2.getHead()) : new Vector3(scales2.getWidth(), scales2.getHeight(), scales2.getDepth());
            if (!Intrinsics.areEqual(vector3, Vector3.INSTANCE.getONE())) {
                PartData.INSTANCE.setSize(instance5, PartData.INSTANCE.size(instance5).times(vector3));
                Iterator<Instance> it11 = instance5.getChildren().iterator();
                Intrinsics.checkNotNullExpressionValue(it11, "iterator(...)");
                while (it11.hasNext()) {
                    Instance next8 = it11.next();
                    if (Intrinsics.areEqual(next8.getClassName(), "Attachment")) {
                        Object obj2 = next8.get("CFrame");
                        CFrame identity = obj2 instanceof CFrame ? (CFrame) obj2 : null;
                        if (identity == null) {
                            identity = CFrame.INSTANCE.getIDENTITY();
                        }
                        next8.set("CFrame", identity.rotationOnly().withPosition(identity.getPosition().times(vector3)), false);
                    }
                }
            }
            instance5.attachRaw(instanceCreate);
            scales = scales2;
            it7 = it9;
            i3 = 1;
        }
        instanceCreate2.set("CFrame", CFrame.INSTANCE.getIDENTITY(), false);
        List listListOf = CollectionsKt.listOf(instanceCreate2);
        Collection collectionValues = linkedHashMap2.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
        List listPlus = CollectionsKt.plus((Collection) listListOf, (Iterable) collectionValues);
        Set setNewSetFromMap = Collections.newSetFromMap(new IdentityHashMap());
        setNewSetFromMap.add(instanceCreate2);
        ArrayDeque arrayDeque2 = new ArrayDeque();
        arrayDeque2.add(instanceCreate2);
        while (!arrayDeque2.isEmpty()) {
            Instance instance7 = (Instance) arrayDeque2.removeFirst();
            ArrayList<Instance> children = instance7.getChildren();
            int i13 = 0;
            ArrayList arrayList4 = new ArrayList();
            for (Object obj3 : children) {
                List list4 = listPlus;
                Instance instance8 = (Instance) obj3;
                Instance instance9 = instanceCreate2;
                if (Intrinsics.areEqual(instance8.getClassName(), "Attachment")) {
                    arrayList2 = children;
                    i5 = i13;
                    z6 = StringsKt.endsWith(instance8.getName(), "RigAttachment", (((i2) & 2) != 0 ? false : false));
                    if (z6) {
                        arrayList4.add(obj3);
                    }
                    listPlus = list4;
                    instanceCreate2 = instance9;
                    children = arrayList2;
                    i13 = i5;
                    i2 = 2;
                } else {
                    arrayList2 = children;
                    i5 = i13;
                }
                if (z6) {
                    arrayList4.add(obj3);
                }
                listPlus = list4;
                instanceCreate2 = instance9;
                children = arrayList2;
                i13 = i5;
                i2 = 2;
            }
            List list5 = listPlus;
            Instance instance10 = instanceCreate2;
            Iterator it12 = arrayList4.iterator();
            while (it12.hasNext()) {
                Instance instance11 = (Instance) it12.next();
                Iterator it13 = list5.iterator();
                while (it13.hasNext()) {
                    Instance instance12 = (Instance) it13.next();
                    if (instance12 != instance7 && !setNewSetFromMap.contains(instance12)) {
                        Iterator it14 = instance12.getChildren().iterator();
                        while (true) {
                            if (it14.hasNext()) {
                                next5 = it14.next();
                                Instance instance13 = (Instance) next5;
                                it2 = it12;
                                if (Intrinsics.areEqual(instance13.getClassName(), "Attachment")) {
                                    it3 = it13;
                                    z5 = Intrinsics.areEqual(instance13.getName(), instance11.getName());
                                    if (!z5) {
                                        it12 = it2;
                                        it13 = it3;
                                    }
                                } else {
                                    it3 = it13;
                                }
                                if (!z5) {
                                    it12 = it2;
                                    it13 = it3;
                                } else {
                                    break;
                                }
                            } else {
                                it2 = it12;
                                it3 = it13;
                                next5 = null;
                                break;
                            }
                        }
                        Instance instance14 = (Instance) next5;
                        if (instance14 == null) {
                            it12 = it2;
                            it13 = it3;
                        } else {
                            Object obj4 = instance11.get("CFrame");
                            CFrame identity2 = obj4 instanceof CFrame ? (CFrame) obj4 : null;
                            if (identity2 == null) {
                                identity2 = CFrame.INSTANCE.getIDENTITY();
                            }
                            Object obj5 = instance14.get("CFrame");
                            CFrame identity3 = obj5 instanceof CFrame ? (CFrame) obj5 : null;
                            if (identity3 == null) {
                                identity3 = CFrame.INSTANCE.getIDENTITY();
                            }
                            Instance instanceCreate6 = InstanceFactory.INSTANCE.create("Motor6D");
                            Instance instance15 = instance11;
                            instanceCreate6.setName(StringsKt.removeSuffix(instance15.getName(), (CharSequence) "RigAttachment"));
                            instanceCreate6.set("Part0", instance7, false);
                            instanceCreate6.set("Part1", instance12, false);
                            instanceCreate6.set("C0", identity2, false);
                            instanceCreate6.set("C1", identity3, false);
                            instanceCreate6.set("MaxVelocity", Float.valueOf(0.1f), false);
                            instanceCreate6.attachRaw(instance12);
                            instance12.set("CFrame", PartData.INSTANCE.cframe(instance7).times(identity2).times(identity3.inverse()), false);
                            setNewSetFromMap.add(instance12);
                            arrayDeque2.add(instance12);
                            instance11 = instance15;
                            it12 = it2;
                            it13 = it3;
                        }
                    }
                }
            }
            listPlus = list5;
            instanceCreate2 = instance10;
            i2 = 2;
        }
        Instance instance16 = instanceCreate2;
        Iterator<Tpl> it15 = DEFAULT_R15.iterator();
        while (it15.hasNext()) {
            Tpl next9 = it15.next();
            Instance instance17 = (Instance) linkedHashMap2.get(next9.getName());
            if (instance17 != null && !setNewSetFromMap.contains(instance17)) {
                List<Pair<String, Vector3>> atts2 = next9.getAtts();
                int i14 = 0;
                Iterator it16 = atts2.iterator();
                while (true) {
                    if (it16.hasNext()) {
                        next = it16.next();
                        it = it15;
                        List<Pair<String, Vector3>> list6 = atts2;
                        arrayDeque = arrayDeque2;
                        int i15 = i14;
                        if (!StringsKt.endsWith((String) ((Pair) next).getFirst(), "RigAttachment", false)) {
                            atts2 = list6;
                            it15 = it;
                            arrayDeque2 = arrayDeque;
                            i14 = i15;
                        } else {
                            break;
                        }
                    } else {
                        it = it15;
                        arrayDeque = arrayDeque2;
                        next = null;
                        break;
                    }
                }
                Pair pair2 = (Pair) next;
                if (pair2 == null) {
                    it15 = it;
                    arrayDeque2 = arrayDeque;
                } else {
                    List<Tpl> list7 = DEFAULT_R15;
                    Iterator it17 = list7.iterator();
                    while (true) {
                        if (it17.hasNext()) {
                            next2 = it17.next();
                            Tpl tpl4 = (Tpl) next2;
                            if (tpl4 != next9) {
                                List<Pair<String, Vector3>> atts3 = tpl4.getAtts();
                                list = list7;
                                if ((atts3 instanceof Collection) && atts3.isEmpty()) {
                                    tpl = next9;
                                    z4 = false;
                                } else {
                                    tpl = next9;
                                    z4 = false;
                                    Iterator it18 = atts3.iterator();
                                    while (it18.hasNext()) {
                                        if (Intrinsics.areEqual(((Pair) it18.next()).getFirst(), pair2.getFirst())) {
                                            z4 = true;
                                            break;
                                        }
                                    }
                                }
                                z3 = z4;
                                if (!z3) {
                                    list7 = list;
                                    next9 = tpl;
                                }
                            } else {
                                list = list7;
                                tpl = next9;
                            }
                            if (!z3) {
                                list7 = list;
                                next9 = tpl;
                            } else {
                                break;
                            }
                        } else {
                            tpl = next9;
                            next2 = null;
                            break;
                        }
                    }
                    Tpl tpl5 = (Tpl) next2;
                    if (tpl5 == null) {
                        instance2 = instance16;
                    } else {
                        instance2 = (Instance) linkedHashMap2.get(tpl5.getName());
                        if (instance2 != null) {
                            if (!setNewSetFromMap.contains(instance2)) {
                                instance2 = null;
                            }
                            if (instance2 == null) {
                            }
                        }
                        it15 = it;
                        arrayDeque2 = arrayDeque;
                    }
                    ArrayList<Instance> children2 = instance2.getChildren();
                    Iterator it20 = children2.iterator();
                    while (true) {
                        if (it20.hasNext()) {
                            next3 = it20.next();
                            Instance instance18 = (Instance) next3;
                            tpl2 = tpl5;
                            if (Intrinsics.areEqual(instance18.getClassName(), "Attachment")) {
                                arrayList = children2;
                                z2 = Intrinsics.areEqual(instance18.getName(), pair2.getFirst());
                                if (!z2) {
                                    tpl5 = tpl2;
                                    children2 = arrayList;
                                }
                            } else {
                                arrayList = children2;
                            }
                            if (!z2) {
                                tpl5 = tpl2;
                                children2 = arrayList;
                            } else {
                                break;
                            }
                        } else {
                            tpl2 = tpl5;
                            next3 = null;
                            break;
                        }
                    }
                    Instance instance19 = (Instance) next3;
                    Object obj6 = instance19 != null ? instance19.get("CFrame") : null;
                    CFrame cFrameAt = obj6 instanceof CFrame ? (CFrame) obj6 : null;
                    if (cFrameAt == null) {
                        CFrame.Companion companion = CFrame.INSTANCE;
                        if (tpl2 == null || (atts = tpl2.getAtts()) == null) {
                            linkedHashMap = linkedHashMap2;
                            cFrameAt = companion.at(new Vector3(0.0f, -1.0f, 0.0f));
                        } else {
                            Vector3 vector3 = null;
                            Iterator it21 = atts.iterator();
                            while (true) {
                                if (!it21.hasNext()) {
                                    throw new NoSuchElementException("Collection contains no element matching the predicate.");
                                }
                                Object next10 = it21.next();
                                Instance instance20 = instance19;
                                linkedHashMap = linkedHashMap2;
                                if (Intrinsics.areEqual(((Pair) next10).getFirst(), pair2.getFirst())) {
                                    Pair pair3 = (Pair) next10;
                                    if (pair3 != null) {
                                        vector3 = (Vector3) pair3.getSecond();
                                    }
                                    break;
                                } else {
                                    instance19 = instance20;
                                    linkedHashMap2 = linkedHashMap;
                                }
                            }
                            cFrameAt = companion.at(vector3 != null ? vector3 : new Vector3(0.0f, -1.0f, 0.0f));
                        }
                    } else {
                        linkedHashMap = linkedHashMap2;
                    }
                    ArrayList<Instance> children3 = instance17.getChildren();
                    int i16 = 0;
                    Iterator it22 = children3.iterator();
                    while (true) {
                        if (it22.hasNext()) {
                            next4 = it22.next();
                            Instance instance21 = (Instance) next4;
                            ArrayList<Instance> arrayList5 = children3;
                            if (Intrinsics.areEqual(instance21.getClassName(), "Attachment")) {
                                i4 = i16;
                                z = Intrinsics.areEqual(instance21.getName(), pair2.getFirst());
                                if (!z) {
                                    children3 = arrayList5;
                                    i16 = i4;
                                }
                            } else {
                                i4 = i16;
                            }
                            if (!z) {
                                children3 = arrayList5;
                                i16 = i4;
                            } else {
                                break;
                            }
                        } else {
                            next4 = null;
                            break;
                        }
                    }
                    Instance instance22 = (Instance) next4;
                    Object obj7 = instance22 != null ? instance22.get("CFrame") : null;
                    CFrame cFrameAt2 = obj7 instanceof CFrame ? (CFrame) obj7 : null;
                    if (cFrameAt2 == null) {
                        cFrameAt2 = CFrame.INSTANCE.at(((Vector3) pair2.getSecond()).times(new Vector3(1.0f, PartData.INSTANCE.size(instance17).getY() / tpl.getSize().getY(), 1.0f)));
                    }
                    Instance instanceCreate7 = InstanceFactory.INSTANCE.create("Motor6D");
                    instanceCreate7.setName(StringsKt.removeSuffix((String) pair2.getFirst(), (CharSequence) "RigAttachment"));
                    instanceCreate7.set("Part0", instance2, false);
                    instanceCreate7.set("Part1", instance17, false);
                    instanceCreate7.set("C0", cFrameAt, false);
                    instanceCreate7.set("C1", cFrameAt2, false);
                    instanceCreate7.set("MaxVelocity", Float.valueOf(0.1f), false);
                    instanceCreate7.attachRaw(instance17);
                    instance17.set("CFrame", PartData.INSTANCE.cframe(instance2).times(cFrameAt).times(cFrameAt2.inverse()), false);
                    setNewSetFromMap.add(instance17);
                    it15 = it;
                    arrayDeque2 = arrayDeque;
                    linkedHashMap2 = linkedHashMap;
                }
            }
        }
        float fMin = Float.MAX_VALUE;
        for (Object _instObj : linkedHashMap2.values()) {
            Instance instance23 = (Instance) _instObj;
            if (setNewSetFromMap.contains(instance23)) {
                fMin = Math.min(fMin, PartData.INSTANCE.cframe(instance23).getY() - (PartData.INSTANCE.size(instance23).getY() / 2.0f));
            }
        }
        Instance instanceCreate8 = InstanceFactory.INSTANCE.create("Humanoid");
        instanceCreate8.set("RigType", new EnumValue(1), false);
        instanceCreate8.set("HipHeight", Float.valueOf(fMin < Float.MAX_VALUE ? RangesKt.coerceAtLeast((-1.0f) - fMin, 0.0f) : 2.0f), false);
        instanceCreate8.attachRaw(instanceCreate);
        InstanceFactory.INSTANCE.create("Animator").attachRaw(instanceCreate8);
        Instance instanceCreate9 = InstanceFactory.INSTANCE.create("BodyColors");
        instanceCreate9.setName("Body Colors");
        Pair[] pairArr2 = new Pair[6];
        pairArr2[0] = TuplesKt.to("Head", "HeadColor3");
        pairArr2[1] = TuplesKt.to("Torso", "TorsoColor3");
        pairArr2[2] = TuplesKt.to("Left Arm", "LeftArmColor3");
        pairArr2[c] = TuplesKt.to("Right Arm", "RightArmColor3");
        pairArr2[4] = TuplesKt.to("Left Leg", "LeftLegColor3");
        pairArr2[5] = TuplesKt.to("Right Leg", "RightLegColor3");
        for (Pair pair4 : CollectionsKt.listOf(pairArr2)) {
            String str4 = (String) pair4.component1();
            String str5 = (String) pair4.component2();
            Color3 color4 = spec.getColors().get(str4);
            if (color4 != null) {
                instanceCreate9.set(str5, color4, false);
                Unit unit5 = Unit.INSTANCE;
                Unit unit6 = Unit.INSTANCE;
            }
        }
        instanceCreate9.attachRaw(instanceCreate);
        String shirt = spec.getShirt();
        if (shirt != null) {
            Instance instanceCreate10 = InstanceFactory.INSTANCE.create("Shirt");
            instanceCreate10.setName("Shirt");
            instanceCreate10.set("ShirtTemplate", shirt, false);
            instanceCreate10.attachRaw(instanceCreate);
            Unit unit7 = Unit.INSTANCE;
            Unit unit8 = Unit.INSTANCE;
        }
        String pants = spec.getPants();
        if (pants != null) {
            Instance instanceCreate11 = InstanceFactory.INSTANCE.create("Pants");
            instanceCreate11.setName("Pants");
            instanceCreate11.set("PantsTemplate", pants, false);
            instanceCreate11.attachRaw(instanceCreate);
            Unit unit9 = Unit.INSTANCE;
            Unit unit10 = Unit.INSTANCE;
        }
        addAccessories(instanceCreate, spec);
        return instanceCreate;
    }
}
