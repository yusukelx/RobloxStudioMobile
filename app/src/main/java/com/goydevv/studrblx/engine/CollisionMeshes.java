package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.core.SolidModeling;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import com.goydevv.studrblx.render.MeshData;
import com.goydevv.studrblx.render.MeshLoader;
import com.goydevv.studrblx.render.UnionMeshes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: CollisionMeshes.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class CollisionMeshes {
    public static final int BOX = 2;
    public static final int DEFAULT = 0;
    private static final List<Vector3> DIRS;
    public static final int HULL = 1;
    public static final int PRECISE = 3;
    public static final CollisionMeshes INSTANCE = new CollisionMeshes();
    private static final WeakHashMap<Instance, Mesh> cache = new WeakHashMap<>();

    private CollisionMeshes() {
    }

    /* JADX INFO: compiled from: CollisionMeshes.kt */
    public static final class Mesh {
        private final float cell;
        private final int[][] cells;
        private final int count;
        private ConvexShape hull;
        private final long key;
        private final Vector3 max;
        private final Vector3 min;
        private final float[] nrm;
        private final int nx;
        private final int ny;
        private final int nz;
        private final float[] pos;
        private final int[] stamp;
        private int stampGen;

        public Mesh(long key, float[] pos, float[] nrm, int count, Vector3 min, Vector3 max) {
            float y1;
            float z0;
            float z1;
            Object arrayList;
            Intrinsics.checkNotNullParameter(pos, "pos");
            Intrinsics.checkNotNullParameter(nrm, "nrm");
            Intrinsics.checkNotNullParameter(min, "min");
            Intrinsics.checkNotNullParameter(max, "max");
            this.key = key;
            this.pos = pos;
            this.nrm = nrm;
            this.count = count;
            this.min = min;
            this.max = max;
            this.stamp = new int[this.count];
            Vector3 ext = this.max.minus(this.min);
            this.cell = RangesKt.coerceAtLeast(Math.max(ext.getX(), Math.max(ext.getY(), ext.getZ())) / 14.0f, 1.0f);
            this.nx = ((int) (ext.getX() / this.cell)) + 1;
            this.ny = ((int) (ext.getY() / this.cell)) + 1;
            this.nz = ((int) (ext.getZ() / this.cell)) + 1;
            Map lists = new HashMap();
            int i = this.count;
            for (int t = 0; t < i; t++) {
                int o = t * 9;
                float x0 = Math.min(this.pos[o], Math.min(this.pos[o + 3], this.pos[o + 6]));
                float x1 = Math.max(this.pos[o], Math.max(this.pos[o + 3], this.pos[o + 6]));
                float y0 = Math.min(this.pos[o + 1], Math.min(this.pos[o + 4], this.pos[o + 7]));
                float y2 = Math.max(this.pos[o + 1], Math.max(this.pos[o + 4], this.pos[o + 7]));
                float z2 = Math.min(this.pos[o + 2], Math.min(this.pos[o + 5], this.pos[o + 8]));
                float z3 = Math.max(this.pos[o + 2], Math.max(this.pos[o + 5], this.pos[o + 8]));
                int i2 = ci(x0, 0);
                int iCi = ci(x1, 0);
                int spanX = (iCi - i2) + 1;
                int spanY = (ci(y2, 1) - ci(y0, 1)) + 1;
                int spanZ = (ci(z3, 2) - ci(z2, 2)) + 1;
                if (((long) spanX) * ((long) spanY) * ((long) spanZ) > 64L) {
                    iCi = i2;
                    y2 = y0;
                    z3 = z2;
                }
                if (i2 <= iCi) {
                    while (true) {
                        float x2 = x1;
                        int j = ci(y0, 1);
                        float y3 = y0;
                        int iCi2 = ci(y2, 1);
                        if (j <= iCi2) {
                            while (true) {
                                y1 = y2;
                                int k = ci(z2, 2);
                                int iCi3 = ci(z3, 2);
                                if (k <= iCi3) {
                                    while (true) {
                                        z0 = z2;
                                        Map map = lists;
                                        Integer numValueOf = Integer.valueOf(idx(i2, j, k));
                                        z1 = z3;
                                        Object obj = map.get(numValueOf);
                                        if (obj == null) {
                                            arrayList = new ArrayList();
                                            map.put(numValueOf, arrayList);
                                        } else {
                                            arrayList = (ArrayList) obj;
                                        }
                                        ((ArrayList) arrayList).add(Integer.valueOf(t));
                                        if (k == iCi3) {
                                            break;
                                        }
                                        k++;
                                        z2 = z0;
                                        z3 = z1;
                                    }
                                } else {
                                    z0 = z2;
                                    z1 = z3;
                                }
                                if (j == iCi2) {
                                    break;
                                }
                                j++;
                                y2 = y1;
                                z2 = z0;
                                z3 = z1;
                            }
                        } else {
                            y1 = y2;
                            z0 = z2;
                            z1 = z3;
                        }
                        if (i2 == iCi) {
                            break;
                        }
                        i2++;
                        x1 = x2;
                        y0 = y3;
                        y2 = y1;
                        z2 = z0;
                        z3 = z1;
                    }
                }
            }
            this.cells = new int[this.nx * this.ny * this.nz][];
            for (Map.Entry entry : ((Map<?, ?>) lists).entrySet()) {
                int k2 = ((Number) entry.getKey()).intValue();
                ArrayList l = (ArrayList) entry.getValue();
                this.cells[k2] = CollectionsKt.toIntArray(l);
            }
        }

        public final int getCount() {
            return this.count;
        }

        public final long getKey() {
            return this.key;
        }

        public final Vector3 getMax() {
            return this.max;
        }

        public final Vector3 getMin() {
            return this.min;
        }

        public final float[] getNrm() {
            return this.nrm;
        }

        public final float[] getPos() {
            return this.pos;
        }

        public final float getCell() {
            return this.cell;
        }

        public final int getNx() {
            return this.nx;
        }

        public final int getNy() {
            return this.ny;
        }

        public final int getNz() {
            return this.nz;
        }

        public final int[][] getCells() {
            return this.cells;
        }

        public final ConvexShape getHull() {
            return this.hull;
        }

        public final void setHull(ConvexShape convexShape) {
            this.hull = convexShape;
        }

        private final int ci(float v, int axis) {
            float o;
            int n;
            if (axis == 0) {
                o = this.min.getX();
                n = this.nx;
            } else if (axis == 1) {
                o = this.min.getY();
                n = this.ny;
            } else {
                o = this.min.getZ();
                n = this.nz;
            }
            int idx = (int) ((v - o) / this.cell);
            if (idx < 0) return 0;
            return idx >= n ? n - 1 : idx;
        }

        private final int idx(int i, int j, int k) {
            return (((this.ny * i) + j) * this.nz) + k;
        }

        public final void forTris(Vector3 lo, Vector3 hi, Function1<? super Integer, Unit> f) {
            Mesh mesh = this;
            Intrinsics.checkNotNullParameter(lo, "lo");
            Intrinsics.checkNotNullParameter(hi, "hi");
            Intrinsics.checkNotNullParameter(f, "f");
            if (hi.getX() < mesh.getMin().getX() || lo.getX() > mesh.getMax().getX() || hi.getY() < mesh.getMin().getY() || lo.getY() > mesh.getMax().getY() || hi.getZ() < mesh.getMin().getZ() || lo.getZ() > mesh.getMax().getZ()) {
                return;
            }
            int g = mesh.nextStamp();
            int i = 0;
            int i2 = mesh.cellIndex(lo.getX(), 0);
            int iCellIndex = mesh.cellIndex(hi.getX(), 0);
            if (i2 > iCellIndex) {
                return;
            }
            while (true) {
                int j = mesh.cellIndex(lo.getY(), 1);
                int iCellIndex2 = mesh.cellIndex(hi.getY(), 1);
                if (j <= iCellIndex2) {
                    while (true) {
                        int k = mesh.cellIndex(lo.getZ(), 2);
                        int iCellIndex3 = mesh.cellIndex(hi.getZ(), 2);
                        if (k <= iCellIndex3) {
                            while (true) {
                                int[] list = mesh.getCells()[(((mesh.getNy() * i2) + j) * mesh.getNz()) + k];
                                if (list != null) {
                                    int length = list.length;
                                    int i3 = i;
                                    while (i3 < length) {
                                        int t = list[i3];
                                        if (mesh.mark(t, g)) {
                                            f.invoke(Integer.valueOf(t));
                                        }
                                        i3++;
                                        mesh = this;
                                    }
                                }
                                if (k == iCellIndex3) {
                                    break;
                                }
                                k++;
                                i = 0;
                                mesh = this;
                            }
                        }
                        if (j == iCellIndex2) {
                            break;
                        }
                        j++;
                        i = 0;
                        mesh = this;
                    }
                }
                if (i2 == iCellIndex) {
                    return;
                }
                i2++;
                i = 0;
                mesh = this;
            }
        }

        public final int cellIndex(float v, int axis) {
            return ci(v, axis);
        }

        public final int nextStamp() {
            this.stampGen++;
            if (this.stampGen == Integer.MAX_VALUE) {
                ArraysKt.fill(this.stamp, 0, 0, this.stamp.length);
                this.stampGen = 1;
            }
            return this.stampGen;
        }

        public final boolean mark(int t, int g) {
            if (this.stamp[t] == g) {
                return false;
            }
            this.stamp[t] = g;
            return true;
        }
    }

    static {
        Iterable iterableListOf = CollectionsKt.listOf(new Vector3[]{new Vector3(1.0f, 0.0f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f), new Vector3(0.0f, 0.0f, 1.0f), new Vector3(1.0f, 1.0f, 0.0f), new Vector3(1.0f, -1.0f, 0.0f), new Vector3(1.0f, 0.0f, 1.0f), new Vector3(1.0f, 0.0f, -1.0f), new Vector3(0.0f, 1.0f, 1.0f), new Vector3(0.0f, 1.0f, -1.0f), new Vector3(1.0f, 1.0f, 1.0f), new Vector3(1.0f, 1.0f, -1.0f), new Vector3(1.0f, -1.0f, 1.0f), new Vector3(-1.0f, 1.0f, 1.0f)});
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf, 10));
        Iterator it = iterableListOf.iterator();
        while (it.hasNext()) {
            arrayList.add(((Vector3) it.next()).getUnit());
        }
        DIRS = (List) arrayList;
    }

    public final int fidelity(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        Object obj = p.get("CollisionFidelity");
        EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
        if (enumValue != null) {
            return enumValue.getValue();
        }
        return 0;
    }

    public final boolean hasGeometry(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        return (p.isA("PartOperation") && !Intrinsics.areEqual(p.getClassName(), "NegateOperation")) || Intrinsics.areEqual(p.getClassName(), "MeshPart");
    }

    public static /* synthetic */ Mesh forPart$default(CollisionMeshes collisionMeshes, Instance instance, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return collisionMeshes.forPart(instance, z);
    }

    public final void trimMemory() {
        synchronized (cache) {
            cache.clear();
        }
    }

    public final Mesh getCached(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        synchronized (cache) {
            return cache.get(p);
        }
    }

    public final Mesh forPart(Instance p, boolean forRays) {
        UnionMeshes.Data d;
        long id;
        List<float[]> list;
        Vector3 center;
        Vector3 scale;
        MeshData m;
        Intrinsics.checkNotNullParameter(p, "p");
        if (!hasGeometry(p)) {
            return null;
        }
        if (!forRays && fidelity(p) == 2) {
            return null;
        }
        CFrame cf = PartData.INSTANCE.cframe(p);
        Vector3 size = PartData.INSTANCE.size(p);
        if (Intrinsics.areEqual(p.getClassName(), "MeshPart")) {
            Long lIdOf = AssetFetcher.INSTANCE.idOf(PartData.INSTANCE.meshContent(p));
            if (lIdOf == null || (m = MeshLoader.INSTANCE.get((id = lIdOf.longValue()))) == null || m.getTriangles() == 0) {
                return null;
            }
            List<float[]> listListOf = CollectionsKt.listOf(m.getVerts());
            Vector3 center2 = new Vector3(m.center(0), m.center(1), m.center(2));
            Vector3 scale2 = new Vector3(forPart$k(size.getX(), m.size(0)), forPart$k(size.getY(), m.size(1)), forPart$k(size.getZ(), m.size(2)));
            list = listListOf;
            center = center2;
            scale = scale2;
        } else {
            SolidModeling.Result r = SolidModeling.INSTANCE.result(p);
            if (r != null) {
                List<float[]> list2 = CollectionsKt.toList(r.getTris().values());
                Vector3 center3 = Vector3.INSTANCE.getZERO();
                Vector3 scale3 = SolidModeling.INSTANCE.scaleOf(p, r);
                id = r.getSig();
                list = list2;
                center = center3;
                scale = scale3;
            } else {
                if (!UnionMeshes.INSTANCE.isStored(p) || (d = UnionMeshes.INSTANCE.get(p)) == null) {
                    return null;
                }
                Iterable groups = d.getGroups();
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(groups, 10));
                Iterator it = groups.iterator();
                while (it.hasNext()) {
                    arrayList.add((float[]) ((Pair) it.next()).getSecond());
                }
                Vector3 center4 = d.getCenter();
                Vector3 scale4 = new Vector3(forPart$k(size.getX(), d.getSize().getX()), forPart$k(size.getY(), d.getSize().getY()), forPart$k(size.getZ(), d.getSize().getZ()));
                id = d.getKey().hashCode();
                list = (List) arrayList;
                center = center4;
                scale = scale4;
            }
        }
        long key = (((((id * 31) + ((long) cf.hashCode())) * 31) + ((long) size.hashCode())) * 31) + ((long) fidelity(p));
        synchronized (cache) {
            Mesh mesh = cache.get(p);
            if (mesh != null && mesh.getKey() == key) {
                return mesh;
            }
            Mesh meshBuild = INSTANCE.build(key, list, center, scale, cf, INSTANCE.fidelity(p) == 1);
            cache.put(p, meshBuild);
            return meshBuild;
        }
    }

    private static final float forPart$k(float a, float b) {
        if (b > 1.0E-5f) {
            return a / b;
        }
        return 1.0f;
    }

    private final Mesh build(long key, List<float[]> src, Vector3 center, Vector3 scale, CFrame cf, boolean hull) {
        Iterator it = src.iterator();
        int n = 0;
        while (it.hasNext()) {
            n += ((float[]) it.next()).length / 24;
        }
        int strideTris = n > 3000 ? ((n + 2999) / 3000) : 1;
        int capTris = (n + strideTris - 1) / strideTris;
        float[] pos = new float[capTris * 9];
        float[] nrm = new float[capTris * 3];
        Vector3 mn = new Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3 mx = mn.unaryMinus();
        int t = 0;
        for (float[] arr : src) {
            int o = 0;
            while (o + 24 <= arr.length) {
                Vector3[] w = new Vector3[3];
                int i = 0;
                for (int i2 = 3; i < i2; i2 = 3) {
                    int b = (i * 8) + o;
                    w[i] = cf.times(new Vector3((arr[b] - center.getX()) * scale.getX(), (arr[b + 1] - center.getY()) * scale.getY(), (arr[b + 2] - center.getZ()) * scale.getZ()));
                    i++;
                }
                Vector3 mn2 = mn;
                char c = 2;
                Vector3 fn = w[1].minus(w[0]).cross(w[2].minus(w[0]));
                Vector3 vn = cf.vectorToWorld(new Vector3(arr[o + 3] + arr[o + 11] + arr[o + 19], arr[o + 4] + arr[o + 12] + arr[o + 20], arr[o + 5] + arr[o + 13] + arr[o + 21]));
                if (fn.dot(vn) < 0.0f) {
                    fn = fn.unaryMinus();
                }
                float len = fn.getMagnitude();
                o += 24 * strideTris;
                if (len < 1.0E-9f) {
                    mn = mn2;
                } else {
                    Vector3 fn2 = fn.div(len);
                    int v = 0;
                    Vector3 mx2 = mx;
                    Vector3 mn3 = mn2;
                    while (true) {
                        char c2 = c;
                        if (v >= 3) {
                            break;
                        }
                        pos[(t * 9) + (v * 3)] = w[v].getX();
                        pos[(t * 9) + (v * 3) + 1] = w[v].getY();
                        pos[(t * 9) + (v * 3) + 2] = w[v].getZ();
                        mn3 = mn3.min(w[v]);
                        mx2 = mx2.max(w[v]);
                        v++;
                        c = c2;
                    }
                    nrm[t * 3] = fn2.getX();
                    nrm[(t * 3) + 1] = fn2.getY();
                    nrm[(t * 3) + 2] = fn2.getZ();
                    t++;
                    mn = mn3;
                    mx = mx2;
                }
            }
        }
        if (t == 0) {
            mn = cf.getPosition();
            mx = cf.getPosition();
        }
        Mesh m = new Mesh(key, pos, nrm, t, mn, mx);
        if (hull && t > 0) {
            m.setHull(hullOf(pos, t, cf));
        }
        return m;
    }

    private final ConvexShape hullOf(float[] pos, int count, CFrame cf) {
        Iterable iterable = DIRS;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(cf.vectorToWorld((Vector3) it.next()));
        }
        List<Vector3> dirs = (List) arrayList;
        LinkedHashSet extremes = new LinkedHashSet();
        for (Vector3 d : dirs) {
            float lo = Float.MAX_VALUE;
            float hi = -3.4028235E38f;
            Vector3 a = Vector3.INSTANCE.getZERO();
            Vector3 b = Vector3.INSTANCE.getZERO();
            int i = count * 3;
            for (int i2 = 0; i2 < i; i2++) {
                Vector3 v = new Vector3(pos[i2 * 3], pos[(i2 * 3) + 1], pos[(i2 * 3) + 2]);
                float s = v.dot(d);
                if (s < lo) {
                    lo = s;
                    a = v;
                }
                if (s > hi) {
                    hi = s;
                    b = v;
                }
            }
            extremes.add(a);
            extremes.add(b);
        }
        return new ConvexShape(CollectionsKt.toList(extremes), dirs, dirs);
    }

    public final Pair<Vector3, Float> push(Mesh m, ConvexShape shape, Vector3 lo, Vector3 hi) {
        Pair<Vector3, Float> pair;
        Object best;
        Vector3 c;
        float[] p;
        float[] nr;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int[] iArr;
        int i7;
        Vector3 c2;
        float[] p2;
        float[] nr2;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        Intrinsics.checkNotNullParameter(m, "m");
        Intrinsics.checkNotNullParameter(shape, "shape");
        Intrinsics.checkNotNullParameter(lo, "lo");
        Intrinsics.checkNotNullParameter(hi, "hi");
        ConvexShape hull = m.getHull();
        if (hull != null) {
            Vector3 vector3Sat = Geometry.INSTANCE.sat(shape, hull);
            if (vector3Sat != null) {
                return TuplesKt.to(vector3Sat, Float.valueOf(m.getMax().getY()));
            }
            return null;
        }
        Vector3 c3 = shape.getCenter();
        float bestLen = Float.MAX_VALUE;
        float top = -3.4028235E38f;
        float[] p3 = m.getPos();
        float[] nr3 = m.getNrm();
        if (hi.getX() < m.getMin().getX() || lo.getX() > m.getMax().getX() || hi.getY() < m.getMin().getY() || lo.getY() > m.getMax().getY() || hi.getZ() < m.getMin().getZ() || lo.getZ() > m.getMax().getZ()) {
            pair = null;
            best = null;
        } else {
            int iNextStamp = m.nextStamp();
            pair = null;
            int iCellIndex = m.cellIndex(lo.getX(), 0);
            Object best2 = null;
            int iCellIndex2 = m.cellIndex(hi.getX(), 0);
            if (iCellIndex <= iCellIndex2) {
                while (true) {
                    boolean z = true;
                    int iCellIndex3 = m.cellIndex(lo.getY(), 1);
                    int i13 = iCellIndex2;
                    int iCellIndex4 = m.cellIndex(hi.getY(), 1);
                    if (iCellIndex3 <= iCellIndex4) {
                        while (true) {
                            boolean z2 = z;
                            char c4 = 2;
                            int iCellIndex5 = m.cellIndex(lo.getZ(), 2);
                            int i14 = iCellIndex4;
                            int iCellIndex6 = m.cellIndex(hi.getZ(), 2);
                            if (iCellIndex5 <= iCellIndex6) {
                                while (true) {
                                    int[] iArr2 = m.getCells()[(((m.getNy() * iCellIndex) + iCellIndex3) * m.getNz()) + iCellIndex5];
                                    if (iArr2 == null) {
                                        c = c3;
                                        p = p3;
                                        nr = nr3;
                                        i = i13;
                                        i4 = i14;
                                        i5 = iCellIndex6;
                                        i2 = iNextStamp;
                                        i3 = iCellIndex;
                                    } else {
                                        int[] iArr3 = iArr2;
                                        int length = iArr3.length;
                                        float top2 = top;
                                        Object best3 = best2;
                                        float bestLen2 = bestLen;
                                        int i15 = 0;
                                        while (i15 < length) {
                                            int i16 = iArr3[i15];
                                            if (m.mark(i16, iNextStamp)) {
                                                int i17 = i16 * 9;
                                                iArr = iArr3;
                                                float f = p3[i17];
                                                i7 = length;
                                                float f2 = p3[i17 + 1];
                                                c2 = c3;
                                                float f3 = p3[i17 + 2];
                                                int i18 = iCellIndex6;
                                                if (Math.max(f, Math.max(p3[i17 + 3], p3[i17 + 6])) < lo.getX()) {
                                                    p2 = p3;
                                                    i10 = i18;
                                                    i6 = i15;
                                                    nr2 = nr3;
                                                    i9 = i14;
                                                    i12 = iCellIndex;
                                                    i8 = i13;
                                                    i11 = iNextStamp;
                                                } else if (Math.min(f, Math.min(p3[i17 + 3], p3[i17 + 6])) > hi.getX()) {
                                                    p2 = p3;
                                                    i10 = i18;
                                                    i6 = i15;
                                                    nr2 = nr3;
                                                    i8 = i13;
                                                    i9 = i14;
                                                    i11 = iNextStamp;
                                                    i12 = iCellIndex;
                                                } else if (Math.max(f2, Math.max(p3[i17 + 4], p3[i17 + 7])) < lo.getY()) {
                                                    p2 = p3;
                                                    i10 = i18;
                                                    i6 = i15;
                                                    nr2 = nr3;
                                                    i9 = i14;
                                                    i12 = iCellIndex;
                                                    i8 = i13;
                                                    i11 = iNextStamp;
                                                } else if (Math.min(f2, Math.min(p3[i17 + 4], p3[i17 + 7])) > hi.getY()) {
                                                    p2 = p3;
                                                    i10 = i18;
                                                    i6 = i15;
                                                    nr2 = nr3;
                                                    i8 = i13;
                                                    i9 = i14;
                                                    i11 = iNextStamp;
                                                    i12 = iCellIndex;
                                                } else if (Math.max(f3, Math.max(p3[i17 + 5], p3[i17 + 8])) < lo.getZ()) {
                                                    p2 = p3;
                                                    i10 = i18;
                                                    i6 = i15;
                                                    nr2 = nr3;
                                                    i9 = i14;
                                                    i12 = iCellIndex;
                                                    i8 = i13;
                                                    i11 = iNextStamp;
                                                } else if (Math.min(f3, Math.min(p3[i17 + 5], p3[i17 + 8])) > hi.getZ()) {
                                                    p2 = p3;
                                                    i10 = i18;
                                                    i6 = i15;
                                                    nr2 = nr3;
                                                    i8 = i13;
                                                    i9 = i14;
                                                    i11 = iNextStamp;
                                                    i12 = iCellIndex;
                                                } else {
                                                    int i19 = i15;
                                                    Vector3 vector3 = new Vector3(nr3[i16 * 3], nr3[(i16 * 3) + 1], nr3[(i16 * 3) + 2]);
                                                    if ((vector3.getX() * (c2.getX() - f)) + (vector3.getY() * (c2.getY() - f2)) + (vector3.getZ() * (c2.getZ() - f3)) < 0.0f) {
                                                        p2 = p3;
                                                        i10 = i18;
                                                        i9 = i14;
                                                        i8 = i13;
                                                        i6 = i19;
                                                        i11 = iNextStamp;
                                                        nr2 = nr3;
                                                        i12 = iCellIndex;
                                                    } else {
                                                        Vector3 vector4 = new Vector3(f, f2, f3);
                                                        Vector3 vector5 = new Vector3(p3[i17 + 3], p3[i17 + 4], p3[i17 + 5]);
                                                        Vector3 vector6 = new Vector3(p3[i17 + 6], p3[i17 + 7], p3[i17 + 8]);
                                                        float[] fArr = nr3;
                                                        i12 = iCellIndex;
                                                        i6 = i19;
                                                        nr2 = fArr;
                                                        p2 = p3;
                                                        i10 = i18;
                                                        i9 = i14;
                                                        Vector3 vector7 = vector5;
                                                        i8 = i13;
                                                        i11 = iNextStamp;
                                                        if (INSTANCE.overlaps(shape, vector4, vector7, vector6, vector3)) {
                                                            float f4 = Float.MAX_VALUE;
                                                            Iterator<Vector3> it = shape.getVerts().iterator();
                                                            while (it.hasNext()) {
                                                                float fDot = vector3.dot(it.next().minus(vector4));
                                                                if (fDot < f4) {
                                                                    f4 = fDot;
                                                                }
                                                            }
                                                            float f5 = -f4;
                                                            if (f5 > 1.0E-5f) {
                                                                float fMax = Math.max(f2, Math.max(p2[i17 + 4], p2[i17 + 7]));
                                                                if (f5 < bestLen2) {
                                                                    best3 = vector3.times(f5);
                                                                    bestLen2 = f5;
                                                                }
                                                                if (fMax > top2) {
                                                                    top2 = fMax;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                i6 = i15;
                                                iArr = iArr3;
                                                i7 = length;
                                                c2 = c3;
                                                p2 = p3;
                                                nr2 = nr3;
                                                i8 = i13;
                                                i9 = i14;
                                                i10 = iCellIndex6;
                                                i11 = iNextStamp;
                                                i12 = iCellIndex;
                                            }
                                            i15 = i6 + 1;
                                            i14 = i9;
                                            iCellIndex6 = i10;
                                            iCellIndex = i12;
                                            iNextStamp = i11;
                                            length = i7;
                                            c3 = c2;
                                            p3 = p2;
                                            nr3 = nr2;
                                            i13 = i8;
                                            iArr3 = iArr;
                                        }
                                        c = c3;
                                        p = p3;
                                        nr = nr3;
                                        i = i13;
                                        i4 = i14;
                                        i5 = iCellIndex6;
                                        i2 = iNextStamp;
                                        i3 = iCellIndex;
                                        bestLen = bestLen2;
                                        top = top2;
                                        best2 = best3;
                                    }
                                    if (iCellIndex5 == i5) {
                                        break;
                                    }
                                    iCellIndex5++;
                                    i14 = i4;
                                    iCellIndex6 = i5;
                                    iCellIndex = i3;
                                    iNextStamp = i2;
                                    c3 = c;
                                    p3 = p;
                                    nr3 = nr;
                                    i13 = i;
                                }
                            } else {
                                c = c3;
                                p = p3;
                                nr = nr3;
                                i = i13;
                                i4 = i14;
                                i2 = iNextStamp;
                                i3 = iCellIndex;
                            }
                            if (iCellIndex3 == i4) {
                                break;
                            }
                            iCellIndex3++;
                            iCellIndex4 = i4;
                            iCellIndex = i3;
                            iNextStamp = i2;
                            z = z2;
                            c3 = c;
                            p3 = p;
                            nr3 = nr;
                            i13 = i;
                        }
                    } else {
                        c = c3;
                        p = p3;
                        nr = nr3;
                        i = i13;
                        i2 = iNextStamp;
                        i3 = iCellIndex;
                    }
                    if (i3 == i) {
                        break;
                    }
                    iCellIndex = i3 + 1;
                    iCellIndex2 = i;
                    iNextStamp = i2;
                    c3 = c;
                    p3 = p;
                    nr3 = nr;
                }
                best = best2;
            } else {
                best = null;
            }
        }
        if (best == null) {
            return pair;
        }
        Object b = best;
        return TuplesKt.to((Vector3) b, Float.valueOf(top));
    }

    private static final boolean overlaps$sep(ConvexShape $s, Vector3 $a, Vector3 $b, Vector3 $c, Vector3 axis) {
        float l = axis.getMagnitude();
        if (l < 1.0E-6f) {
            return false;
        }
        Pair<Float, Float> pairProject = $s.project(axis);
        float s0 = pairProject.component1().floatValue();
        float s1 = pairProject.component2().floatValue();
        float ta = $a.dot(axis);
        float tb = $b.dot(axis);
        float tc = $c.dot(axis);
        return s1 < Math.min(ta, Math.min(tb, tc)) || s0 > Math.max(ta, Math.max(tb, tc));
    }

    private final boolean overlaps(ConvexShape s, Vector3 a, Vector3 b, Vector3 c, Vector3 n) {
        if (overlaps$sep(s, a, b, c, n)) {
            return false;
        }
        for (Vector3 ax : s.getNormals()) {
            if (overlaps$sep(s, a, b, c, ax)) {
                return false;
            }
        }
        Vector3[] edges = {b.minus(a), c.minus(b), a.minus(c)};
        for (Vector3 e : s.getEdges()) {
            for (Vector3 te : edges) {
                if (overlaps$sep(s, a, b, c, e.cross(te))) {
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final Pair<Float, Vector3> ray(final Mesh m, final Vector3 o, final Vector3 d, final float maxDist) {
        Pair<Float, Vector3> pair;
        Mesh mesh;
        Vector3 vector3;
        Vector3 end;
        Intrinsics.checkNotNullParameter(m, "m");
        Intrinsics.checkNotNullParameter(o, "o");
        Intrinsics.checkNotNullParameter(d, "d");
        Vector3 end2 = o.plus(d.times(RangesKt.coerceAtMost(maxDist, 100000.0f)));
        Vector3 lo = o.min(end2).max(m.getMin());
        Vector3 hi = o.max(end2).min(m.getMax());
        if (lo.getX() > hi.getX() || lo.getY() > hi.getY()) {
            return null;
        }
        if (lo.getZ() > hi.getZ()) {
            return null;
        }
        final Ref.FloatRef best = new Ref.FloatRef();
        best.element = Float.MAX_VALUE;
        final Ref.ObjectRef bestN = new Ref.ObjectRef();
        final float[] p = m.getPos();
        Function1 visit = new Function1() { // from class: com.goydevv.studrblx.engine.CollisionMeshes$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CollisionMeshes.ray$lambda$9(p, d, o, best, maxDist, bestN, m, ((Integer) obj).intValue());
            }
        };
        if (hi.minus(lo).getMagnitude() > m.getCell() * 12.0f) {
            int count = m.getCount();
            for (int t = 0; t < count; t++) {
                visit.invoke(Integer.valueOf(t));
            }
            pair = null;
        } else {
            Vector3 vector4 = hi;
            Mesh mesh2 = m;
            Vector3 vector5 = lo;
            if (vector4.getX() < mesh2.getMin().getX() || vector5.getX() > mesh2.getMax().getX() || vector4.getY() < mesh2.getMin().getY() || vector5.getY() > mesh2.getMax().getY() || vector4.getZ() < mesh2.getMin().getZ() || vector5.getZ() > mesh2.getMax().getZ()) {
                pair = null;
            } else {
                int iNextStamp = mesh2.nextStamp();
                int iCellIndex = mesh2.cellIndex(vector5.getX(), 0);
                pair = null;
                int iCellIndex2 = mesh2.cellIndex(vector4.getX(), 0);
                if (iCellIndex <= iCellIndex2) {
                    while (true) {
                        float[] p2 = p;
                        int iCellIndex3 = mesh2.cellIndex(vector5.getY(), 1);
                        Vector3 vector6 = vector4;
                        int iCellIndex4 = mesh2.cellIndex(vector6.getY(), 1);
                        if (iCellIndex3 <= iCellIndex4) {
                            while (true) {
                                vector3 = vector5;
                                int iCellIndex5 = mesh2.cellIndex(vector5.getZ(), 2);
                                end = end2;
                                int iCellIndex6 = mesh2.cellIndex(vector6.getZ(), 2);
                                if (iCellIndex5 <= iCellIndex6) {
                                    while (true) {
                                        int[] iArr = mesh2.getCells()[(((mesh2.getNy() * iCellIndex) + iCellIndex3) * mesh2.getNz()) + iCellIndex5];
                                        if (iArr == null) {
                                            mesh = mesh2;
                                        } else {
                                            int length = iArr.length;
                                            int i = 0;
                                            while (i < length) {
                                                int i2 = i;
                                                int i3 = iArr[i2];
                                                if (mesh2.mark(i3, iNextStamp)) {
                                                    visit.invoke(Integer.valueOf(i3));
                                                }
                                                i = i2 + 1;
                                            }
                                            mesh = mesh2;
                                        }
                                        if (iCellIndex5 == iCellIndex6) {
                                            break;
                                        }
                                        iCellIndex5++;
                                        mesh2 = mesh;
                                    }
                                } else {
                                    mesh = mesh2;
                                }
                                if (iCellIndex3 == iCellIndex4) {
                                    break;
                                }
                                iCellIndex3++;
                                vector5 = vector3;
                                end2 = end;
                                mesh2 = mesh;
                            }
                        } else {
                            mesh = mesh2;
                            vector3 = vector5;
                            end = end2;
                        }
                        if (iCellIndex == iCellIndex2) {
                            break;
                        }
                        iCellIndex++;
                        vector4 = vector6;
                        vector5 = vector3;
                        end2 = end;
                        mesh2 = mesh;
                    }
                }
            }
        }
        Vector3 n = (Vector3) bestN.element;
        if (n == null) {
            return pair;
        }
        return TuplesKt.to(Float.valueOf(best.element), n.dot(d) > 0.0f ? n.unaryMinus() : n);
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [T, com.goydevv.studrblx.model.Vector3] */
    static final Unit ray$lambda$9(float[] $p, Vector3 $d, Vector3 $o, Ref.FloatRef $best, float $maxDist, Ref.ObjectRef $bestN, Mesh $m, int t) {
        int o9 = t * 9;
        float ax = $p[o9];
        float ay = $p[o9 + 1];
        float az = $p[o9 + 2];
        float e1x = $p[o9 + 3] - ax;
        float e1y = $p[o9 + 4] - ay;
        float e1z = $p[o9 + 5] - az;
        float e2x = $p[o9 + 6] - ax;
        float e2y = $p[o9 + 7] - ay;
        float e2z = $p[o9 + 8] - az;
        float px = ($d.getY() * e2z) - ($d.getZ() * e2y);
        float py = ($d.getZ() * e2x) - ($d.getX() * e2z);
        float pz = ($d.getX() * e2y) - ($d.getY() * e2x);
        float det = (e1x * px) + (e1y * py) + (e1z * pz);
        if (Math.abs(det) > 1.0E-9f) {
            float inv = 1.0f / det;
            float tx = $o.getX() - ax;
            float ty = $o.getY() - ay;
            float tz = $o.getZ() - az;
            float u = ((tx * px) + (ty * py) + (tz * pz)) * inv;
            boolean z = false;
            if (0.0f <= u && u <= 1.0f) {
                z = true;
            }
            if (z) {
                float qx = (ty * e1z) - (tz * e1y);
                float qy = (tz * e1x) - (tx * e1z);
                float qz = (tx * e1y) - (ty * e1x);
                float v = (($d.getX() * qx) + ($d.getY() * qy) + ($d.getZ() * qz)) * inv;
                if (v >= 0.0f && u + v <= 1.0f) {
                    float dist = ((e2x * qx) + (e2y * qy) + (e2z * qz)) * inv;
                    if (dist >= 0.0f && dist < $best.element && dist <= $maxDist) {
                        $best.element = dist;
                        $bestN.element = new Vector3($m.getNrm()[t * 3], $m.getNrm()[(t * 3) + 1], $m.getNrm()[(t * 3) + 2]);
                    }
                }
            }
        }
        return Unit.INSTANCE;
    }
}
