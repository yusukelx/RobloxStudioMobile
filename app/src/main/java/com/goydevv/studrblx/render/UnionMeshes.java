package com.goydevv.studrblx.render;

import androidx.core.view.ViewCompat;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.format.CsgMesh;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.format.RbxFormatException;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.SharedStr;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: compiled from: UnionMeshes.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class UnionMeshes {
    private static volatile int epoch;
    private static volatile boolean listening;
    public static final UnionMeshes INSTANCE = new UnionMeshes();
    private static final ConcurrentHashMap<String, Data> ready = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap.KeySetView<String, Boolean> bad = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap.KeySetView<String, Boolean> pending = ConcurrentHashMap.newKeySet();
    private static final ExecutorService pool = Executors.newSingleThreadExecutor();
    private static final Map<Object, String> inlineKeys = Collections.synchronizedMap(new WeakHashMap());

    private UnionMeshes() {
    }

    /* JADX INFO: compiled from: UnionMeshes.kt */
    public static final class Data {
        private final Vector3 center;
        private final List<Pair<Integer, float[]>> groups;
        private final String key;
        private final Vector3 size;
        private final List<float[]> triangles;

        public Data(String key, List<Pair<Integer, float[]>> groups, Vector3 center, Vector3 size, List<float[]> triangles) {
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(groups, "groups");
            Intrinsics.checkNotNullParameter(center, "center");
            Intrinsics.checkNotNullParameter(size, "size");
            Intrinsics.checkNotNullParameter(triangles, "triangles");
            this.key = key;
            this.groups = groups;
            this.center = center;
            this.size = size;
            this.triangles = triangles;
        }

        public final Vector3 getCenter() {
            return this.center;
        }

        public final List<Pair<Integer, float[]>> getGroups() {
            return this.groups;
        }

        public final String getKey() {
            return this.key;
        }

        public final Vector3 getSize() {
            return this.size;
        }

        public final List<float[]> getTriangles() {
            return this.triangles;
        }
    }

    public final int getEpoch() {
        return epoch;
    }

    public final void trimMemory(int level) {
        if (level >= 40) {
            ready.clear();
            bad.clear();
            inlineKeys.clear();
            epoch++;
        }
    }

    private final byte[] bytesOf(Object v) {
        byte[] bytes;
        if (v instanceof BinaryString) {
            bytes = ((BinaryString) v).getBytes();
        } else if (v instanceof SharedStr) {
            bytes = ((SharedStr) v).getBytes();
        } else if (!(v instanceof String) || ((String) v).length() <= 16) {
            bytes = null;
        } else {
            bytes = ((String) v).getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        }
        if (bytes == null) {
            return null;
        }
        if (bytes.length > 16) {
            return bytes;
        }
        return null;
    }

    public final boolean isStored(Instance p) {
        boolean z;
        Intrinsics.checkNotNullParameter(p, "p");
        if (!p.isA("PartOperation") || Intrinsics.areEqual(p.getClassName(), "NegateOperation")) {
            return false;
        }
        Iterable children = p.getChildren();
        if ((children instanceof Collection) && ((Collection) children).isEmpty()) {
            z = true;
        } else {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                if (((Instance) it.next()).isA("BasePart")) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0026  */
    /* JADX WARN: Code duplicated, block: B:13:0x0032  */
    /* JADX WARN: Code duplicated, block: B:15:0x003c  */
    /* JADX WARN: Code duplicated, block: B:18:0x0040  */
    public final Data get(final Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        Object inlineVal = p.getProps().get("MeshData2");
        if (inlineVal == null) {
            inlineVal = p.getProps().get("MeshData");
            if (inlineVal == null) {
                inlineVal = null;
            } else {
                if (!(INSTANCE.bytesOf(inlineVal) != null)) {
                    inlineVal = null;
                }
            }
        } else {
            if (!(INSTANCE.bytesOf(inlineVal) != null)) {
                inlineVal = null;
            }
            if (inlineVal == null) {
                inlineVal = p.getProps().get("MeshData");
                if (inlineVal == null) {
                    inlineVal = null;
                } else {
                    if (!(INSTANCE.bytesOf(inlineVal) != null)) {
                        inlineVal = null;
                    }
                }
            }
        }
        if (inlineVal != null) {
            String keyTmp = inlineKeys.get(inlineVal);
            if (keyTmp == null) {
                byte[] bArrBytesOf = bytesOf(inlineVal);
                Intrinsics.checkNotNull(bArrBytesOf);
                String str = "inline:" + bArrBytesOf.length + ":" + Arrays.hashCode(bArrBytesOf);
                Map<Object, String> inlineKeys2 = inlineKeys;
                Intrinsics.checkNotNullExpressionValue(inlineKeys2, "inlineKeys");
                inlineKeys2.put(inlineVal, str);
                keyTmp = str;
            }
            final String key = keyTmp;
            Data data = ready.get(key);
            if (data != null) {
                return data;
            }
            if (bad.contains(key) || !pending.add(key)) {
                return null;
            }
            final byte[] b = bytesOf(inlineVal);
            Intrinsics.checkNotNull(b);
            pool.execute(new Runnable() { // from class: com.goydevv.studrblx.render.UnionMeshes$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    UnionMeshes.INSTANCE.build(key, p.getName(), new Function0() { // from class: com.goydevv.studrblx.render.UnionMeshes$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return UnionMeshes.get$lambda$8$lambda$7(b);
                        }
                    });
                }
            });
            return null;
        }
        Long lIdOf = AssetFetcher.INSTANCE.idOf(p.getProps().get("AssetId"));
        if (lIdOf == null) {
            return null;
        }
        long id = lIdOf.longValue();
        final String key2 = "asset:" + id;
        Data data2 = ready.get(key2);
        if (data2 != null) {
            return data2;
        }
        if (bad.contains(key2) || pending.contains(key2)) {
            return null;
        }
        final byte[] bytes = AssetFetcher.INSTANCE.request(id);
        if (bytes == null) {
            if (!listening) {
                listening = true;
                AssetFetcher.INSTANCE.getListeners().add(new Function1() { // from class: com.goydevv.studrblx.render.UnionMeshes$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return UnionMeshes.get$lambda$10(((Long) obj).longValue());
                    }
                });
            }
            return null;
        }
        if (!pending.add(key2)) {
            return null;
        }
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.render.UnionMeshes$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                UnionMeshes.INSTANCE.build(key2, p.getName(), new Function0() { // from class: com.goydevv.studrblx.render.UnionMeshes$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        try { return UnionMeshes.INSTANCE.meshDataFromAsset(bytes); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
                    }
                });
            }
        });
        return null;
    }

    static final byte[] get$lambda$8$lambda$7(byte[] $b) {
        return $b;
    }

    static final Unit get$lambda$10(long done) {
        if (!ready.containsKey("asset:" + done)) {
            UnionMeshes unionMeshes = INSTANCE;
            int i = epoch;
            UnionMeshes unionMeshes2 = INSTANCE;
            epoch = i + 1;
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final byte[] meshDataFromAsset(byte[] bytes) throws RbxFormatException, XmlPullParserException, CsgMesh.CsgException, IOException {
        if (CsgMesh.INSTANCE.isCsgMesh(bytes)) {
            return bytes;
        }
        RbxDocument doc = RbxFile.INSTANCE.read(bytes);
        for (Instance r : doc.getRoots()) {
            for (Instance i : (List<Instance>) CollectionsKt.plus((Collection) CollectionsKt.listOf(r), (Iterable) Instance.descendants$default(r, null, 1, null))) {
                byte[] bArrBytesOf = bytesOf(i.getProps().get("MeshData"));
                if (bArrBytesOf != null) {
                    return bArrBytesOf;
                }
                byte[] bArrBytesOf2 = bytesOf(i.getProps().get("MeshData2"));
                if (bArrBytesOf2 != null) {
                    return bArrBytesOf2;
                }
            }
        }
        throw new CsgMesh.CsgException("the union asset has no mesh");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void build(String key, String name, Function0<byte[]> source) {
        try {
            try {
                CsgMesh.Decoded m = CsgMesh.INSTANCE.decode(source.invoke());
                ready.put(key, toData(key, m));
            } catch (Exception e) {
                bad.add(key);
                OutputLog outputLog = OutputLog.INSTANCE;
                String message = e.getMessage();
                outputLog.warn("Union '" + name + "' can't be shown (" + (message != null ? message : e) + "); it is drawn as a box.");
            }
        } finally {
            pending.remove(key);
            epoch++;
        }
    }

    private final Data toData(String key, CsgMesh.Decoded m) {
        float[] mx;
        Object arrayList;
        float nx;
        float ny;
        float nz;
        float[] p = m.getPos();
        float[] n = m.getNrm();
        int k = 3;
        float[] mn = {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};
        float[] mx2 = {-3.4028235E38f, -3.4028235E38f, -3.4028235E38f};
        int i = 0;
        for (int i2 : m.getIndices()) {
            for (int c = 0; c < 3; c++) {
                float v = p[(i2 * 3) + c];
                if (v < mn[c]) {
                    mn[c] = v;
                }
                if (v > mx2[c]) {
                    mx2[c] = v;
                }
            }
        }
        if (mn[0] > mx2[0]) {
            ArraysKt.fill(mn, 0.0f, 0, mn.length);
            mx = mx2;
            ArraysKt.fill(mx, 0.0f, 0, mx.length);
        } else {
            mx = mx2;
        }
        Map groups = new LinkedHashMap();
        ArrayList all = new ArrayList();
        int t = 0;
        while (t + 2 < m.getIndices().length) {
            int a = m.getIndices()[t];
            int b = m.getIndices()[t + 1];
            int c2 = m.getIndices()[t + 2];
            t += 3;
            int color = m.getColors()[a] | ViewCompat.MEASURED_STATE_MASK;
            Map map = groups;
            Integer numValueOf = Integer.valueOf(color);
            Object obj = map.get(numValueOf);
            if (obj == null) {
                arrayList = new ArrayList();
                map.put(numValueOf, arrayList);
            } else {
                arrayList = (ArrayList) obj;
            }
            ArrayList list = (ArrayList) arrayList;
            float[] tri = new float[9];
            int k2 = 0;
            int[] iArr = {a, b, c2};
            int i3 = i;
            while (i3 < k) {
                int v2 = iArr[i3];
                float[] p2 = p;
                int q = 0;
                while (q < k) {
                    list.add(Float.valueOf(p2[(v2 * 3) + q]));
                    tri[k2] = p2[(v2 * 3) + q];
                    q++;
                    k2++;
                    k = 3;
                }
                int q2 = v2 * 3;
                float nx2 = n[q2];
                float ny2 = n[(v2 * 3) + 1];
                float nz2 = n[(v2 * 3) + 2];
                float[] n2 = n;
                float l = (float) Math.sqrt((nx2 * nx2) + (ny2 * ny2) + (nz2 * nz2));
                if (l > 1.0E-6f) {
                    nx = nx2 / l;
                    ny = ny2 / l;
                    nz = nz2 / l;
                } else {
                    nx = 0.0f;
                    ny = 1.0f;
                    nz = 0.0f;
                }
                list.add(Float.valueOf(nx));
                list.add(Float.valueOf(ny));
                list.add(Float.valueOf(nz));
                list.add(Float.valueOf(0.0f));
                list.add(Float.valueOf(0.0f));
                i3++;
                p = p2;
                n = n2;
                k = 3;
            }
            all.add(tri);
            k = 3;
        }
        int i4 = i;
        Map map2 = groups;
        Collection arrayList2 = new ArrayList(map2.size());
        for (Map.Entry entry : ((Map<?, ?>) map2).entrySet()) {
            arrayList2.add(TuplesKt.to(Integer.valueOf(((Number) entry.getKey()).intValue()), CollectionsKt.toFloatArray((ArrayList) entry.getValue())));
        }
        return new Data(key, (List) arrayList2, new Vector3((mn[i4] + mx[i4]) / 2.0f, (mn[1] + mx[1]) / 2.0f, (mn[2] + mx[2]) / 2.0f), new Vector3(mx[i4] - mn[i4], mx[1] - mn[1], mx[2] - mn[2]), all);
    }
}
