package com.goydevv.studrblx.render;

import android.opengl.GLES20;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: StaticBatches.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class StaticBatches {
    public static final float CELL = 64.0f;
    public static final int COOL = 45;
    public static final int MESH_STRIDE_BYTES = 28;
    public static final int MESH_STRIDE_INTS = 7;
    public static final int STRIDE_BYTES = 40;
    public static final int STRIDE_INTS = 10;
    public static final int UNLOAD_FRAMES = 900;
    private final HashMap<Long, Chunk> chunks;
    private ByteBuffer direct;
    private int frame;
    private int gen;
    private final Host host;
    private int lastListVersion;
    private final boolean little;
    private final ArrayList<Instance> loose;
    private int[] scratch;
    private long totalBytes;
    private final ArrayList<Chunk> visible;

    /* JADX INFO: compiled from: StaticBatches.kt */
    public interface Host {
        boolean batchable(Instance p);

        int loadSignature();

        List<BatchSpec> spec(Instance p);
    }

    public StaticBatches(Host host) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.host = host;
        this.chunks = new HashMap<>();
        this.loose = new ArrayList<>();
        this.visible = new ArrayList<>();
        this.gen = 1;
        this.lastListVersion = -1;
        this.scratch = new int[92160];
        this.little = Intrinsics.areEqual(ByteOrder.nativeOrder(), ByteOrder.LITTLE_ENDIAN);
    }

    /* JADX INFO: compiled from: StaticBatches.kt */
    public static final class PartRec {
        private boolean batchable;
        private int changedFrame = -1073741824;
        private Chunk chunk;
        private final int gen;
        private boolean known;
        private int seen;
        private int ver;

        public PartRec(int gen) {
            this.gen = gen;
        }

        public final int getGen() {
            return this.gen;
        }

        public final int getVer() {
            return this.ver;
        }

        public final void setVer(int i) {
            this.ver = i;
        }

        public final int getChangedFrame() {
            return this.changedFrame;
        }

        public final void setChangedFrame(int i) {
            this.changedFrame = i;
        }

        public final int getSeen() {
            return this.seen;
        }

        public final void setSeen(int i) {
            this.seen = i;
        }

        public final Chunk getChunk() {
            return this.chunk;
        }

        public final void setChunk(Chunk chunk) {
            this.chunk = chunk;
        }

        public final boolean getKnown() {
            return this.known;
        }

        public final void setKnown(boolean z) {
            this.known = z;
        }

        public final boolean getBatchable() {
            return this.batchable;
        }

        public final void setBatchable(boolean z) {
            this.batchable = z;
        }
    }

    /* JADX INFO: compiled from: StaticBatches.kt */
    public static final class Batch {
        private final int bytes;
        private final int count;
        private final int kind;
        private final TexBind tex;
        private final int tier;
        private final int vbo;

        public Batch(int vbo, int count, int tier, int kind, TexBind tex, int bytes) {
            this.vbo = vbo;
            this.count = count;
            this.tier = tier;
            this.kind = kind;
            this.tex = tex;
            this.bytes = bytes;
        }

        public final int getBytes() {
            return this.bytes;
        }

        public final int getCount() {
            return this.count;
        }

        public final int getKind() {
            return this.kind;
        }

        public final TexBind getTex() {
            return this.tex;
        }

        public final int getTier() {
            return this.tier;
        }

        public final int getVbo() {
            return this.vbo;
        }

        public final int getStrideBytes() {
            return this.kind == 3 ? 28 : 40;
        }
    }

    /* JADX INFO: compiled from: StaticBatches.kt */
    public static final class Chunk {
        private boolean built;
        private long bytes;
        private float dist;
        private final long key;
        private int lastInRange;
        private int maxTier;
        private boolean pendingTex;
        private ArrayList<Instance> parts = new ArrayList<>();
        private boolean dirty = true;
        private List<Batch> batches = CollectionsKt.emptyList();
        private float x0 = Float.MAX_VALUE;
        private float y0 = Float.MAX_VALUE;
        private float z0 = Float.MAX_VALUE;
        private float x1 = -3.4028235E38f;
        private float y1 = -3.4028235E38f;
        private float z1 = -3.4028235E38f;
        private int texEpoch = -1;

        public Chunk(long key) {
            this.key = key;
        }

        public final long getKey() {
            return this.key;
        }

        public final ArrayList<Instance> getParts() {
            return this.parts;
        }

        public final void setParts(ArrayList<Instance> arrayList) {
            Intrinsics.checkNotNullParameter(arrayList, "<set-?>");
            this.parts = arrayList;
        }

        public final boolean getDirty() {
            return this.dirty;
        }

        public final void setDirty(boolean z) {
            this.dirty = z;
        }

        public final boolean getBuilt() {
            return this.built;
        }

        public final void setBuilt(boolean z) {
            this.built = z;
        }

        public final List<Batch> getBatches() {
            return this.batches;
        }

        public final void setBatches(List<Batch> list) {
            Intrinsics.checkNotNullParameter(list, "<set-?>");
            this.batches = list;
        }

        public final float getX0() {
            return this.x0;
        }

        public final float getY0() {
            return this.y0;
        }

        public final float getZ0() {
            return this.z0;
        }

        public final void setX0(float f) {
            this.x0 = f;
        }

        public final void setY0(float f) {
            this.y0 = f;
        }

        public final void setZ0(float f) {
            this.z0 = f;
        }

        public final float getX1() {
            return this.x1;
        }

        public final float getY1() {
            return this.y1;
        }

        public final float getZ1() {
            return this.z1;
        }

        public final void setX1(float f) {
            this.x1 = f;
        }

        public final void setY1(float f) {
            this.y1 = f;
        }

        public final void setZ1(float f) {
            this.z1 = f;
        }

        public final int getMaxTier() {
            return this.maxTier;
        }

        public final void setMaxTier(int i) {
            this.maxTier = i;
        }

        public final int getTexEpoch() {
            return this.texEpoch;
        }

        public final void setTexEpoch(int i) {
            this.texEpoch = i;
        }

        public final boolean getPendingTex() {
            return this.pendingTex;
        }

        public final void setPendingTex(boolean z) {
            this.pendingTex = z;
        }

        public final int getLastInRange() {
            return this.lastInRange;
        }

        public final void setLastInRange(int i) {
            this.lastInRange = i;
        }

        public final float getDist() {
            return this.dist;
        }

        public final void setDist(float f) {
            this.dist = f;
        }

        public final long getBytes() {
            return this.bytes;
        }

        public final void setBytes(long j) {
            this.bytes = j;
        }

        public final void grow(float cx, float cy, float cz, float r) {
            this.x0 = Math.min(this.x0, cx - r);
            this.y0 = Math.min(this.y0, cy - r);
            this.z0 = Math.min(this.z0, cz - r);
            this.x1 = Math.max(this.x1, cx + r);
            this.y1 = Math.max(this.y1, cy + r);
            this.z1 = Math.max(this.z1, cz + r);
        }

        public final float distTo(Vector3 p) {
            Intrinsics.checkNotNullParameter(p, "p");
            float dx = Math.max(Math.max(this.x0 - p.getX(), 0.0f), p.getX() - this.x1);
            float dy = Math.max(Math.max(this.y0 - p.getY(), 0.0f), p.getY() - this.y1);
            float dz = Math.max(Math.max(this.z0 - p.getZ(), 0.0f), p.getZ() - this.z1);
            return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
        }
    }

    public final HashMap<Long, Chunk> getChunks() {
        return this.chunks;
    }

    public final ArrayList<Instance> getLoose() {
        return this.loose;
    }

    public final ArrayList<Chunk> getVisible() {
        return this.visible;
    }

    public final long getTotalBytes() {
        return this.totalBytes;
    }

    public final void reset(boolean deleteBuffers) {
        if (deleteBuffers) {
            for (Chunk chunk : this.chunks.values()) {
                Intrinsics.checkNotNullExpressionValue(chunk, "next(...)");
                Chunk c = chunk;
                free(c);
            }
        }
        this.chunks.clear();
        this.visible.clear();
        this.loose.clear();
        this.totalBytes = 0L;
        this.gen++;
    }

    private final int tierOf(float r) {
        if (r < 1.2f) {
            return 0;
        }
        return r < 5.0f ? 1 : 2;
    }

    public final float tierRange(int tier, float detail) {
        float f;
        float f2;
        switch (tier) {
            case 0:
                f = 1.2f * detail;
                f2 = 150.0f;
                break;
            case 1:
                f = 5.0f * detail;
                f2 = 900.0f;
                break;
            default:
                return Float.MAX_VALUE;
        }
        return Math.max(f, f2);
    }

    private final long keyOf(float x, float y, float z) {
        long ix = ((long) Math.floor(x / 64.0f)) & 2097151;
        long iy = ((long) Math.floor(y / 64.0f)) & 2097151;
        long iz = ((long) Math.floor(z / 64.0f)) & 2097151;
        return (ix << 42) | (iy << 21) | iz;
    }

    /* JADX WARN: Code duplicated, block: B:77:0x01fb  */
    /* JADX WARN: Code duplicated, block: B:78:0x01fd  */
    /* JADX WARN: Code duplicated, block: B:81:0x0201  */
    /* JADX WARN: Code duplicated, block: B:82:0x0207  */
    /* JADX WARN: Code duplicated, block: B:95:0x023c  */
    public final void update(List<? extends Instance> parts, int listVersion, int frame, Vector3 cam, Frustum fr, float detail, Vector3 shadowAt, float shadowR, long budgetNs, long maxBytes) {
        String str;
        Intrinsics.checkNotNullParameter(parts, "parts");
        Intrinsics.checkNotNullParameter(cam, "cam");
        Frustum fr2 = fr;
        Intrinsics.checkNotNullParameter(fr2, "fr");
        this.frame = frame;
        this.loose.clear();
        this.visible.clear();
        for (Instance p : parts) {
            Object obj = p.renderRec;
            PartRec partRec = obj instanceof PartRec ? (PartRec) obj : null;
            if (partRec == null || partRec.getGen() != this.gen) {
                partRec = new PartRec(this.gen);
                p.renderRec = partRec;
                partRec.setVer(p.version);
                partRec.setChangedFrame(frame - 45);
            } else if (partRec.getVer() != p.version) {
                partRec.setVer(p.version);
                partRec.setChangedFrame(frame);
                partRec.setKnown(false);
                Chunk chunk = partRec.getChunk();
                if (chunk != null) {
                    chunk.setDirty(true);
                    partRec.setChunk(null);
                }
            }
            partRec.setSeen(frame);
            if (partRec.getChunk() == null) {
                if (frame - partRec.getChangedFrame() >= 45) {
                    if (!partRec.getKnown()) {
                        partRec.setKnown(true);
                        partRec.setBatchable(this.host.batchable(p));
                    }
                    if (partRec.getBatchable()) {
                        join(p, partRec);
                    }
                }
                this.loose.add(p);
            }
        }
        String str4 = "iterator(...)";
        String str5 = "next(...)";
        if (listVersion != this.lastListVersion) {
            this.lastListVersion = listVersion;
            for (Chunk chunk2 : this.chunks.values()) {
                Intrinsics.checkNotNullExpressionValue(chunk2, "next(...)");
                Chunk c3 = chunk2;
                if (!c3.getDirty()) {
                    Iterator<Instance> it = c3.getParts().iterator();
                    Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                    while (it.hasNext()) {
                        Instance p2 = it.next();
                        Intrinsics.checkNotNullExpressionValue(p2, "next(...)");
                        Object obj2 = p2.renderRec;
                        PartRec r = obj2 instanceof PartRec ? (PartRec) obj2 : null;
                        if (r == null || r.getSeen() != frame || r.getChunk() != c3) {
                            c3.setDirty(true);
                            break;
                        }
                    }
                }
            }
        }
        int texEpoch = this.host.loadSignature();
        ArrayList toBuild = new ArrayList();
        float shR2 = shadowR * shadowR;
        Iterator<Chunk> it2 = this.chunks.values().iterator();
        while (it2.hasNext()) {
            Chunk next = it2.next();
            Intrinsics.checkNotNullExpressionValue(next, str5);
            Chunk c4 = next;
            if (!c4.getParts().isEmpty() || c4.getBuilt()) {
                if (c4.getBuilt() && c4.getPendingTex() && c4.getTexEpoch() != texEpoch) {
                    c4.setDirty(true);
                }
                float d = c4.distTo(cam);
                c4.setDist(d);
                boolean inRange = d <= tierRange(c4.getMaxTier(), detail);
                boolean need = inRange && fr.box(c4.getX0(), c4.getY0(), c4.getZ0(), c4.getX1(), c4.getY1(), c4.getZ1());
                if (!need && shadowAt != null) {
                    float dx = Math.max(Math.max(c4.getX0() - shadowAt.getX(), 0.0f), shadowAt.getX() - c4.getX1());
                    float dz = Math.max(Math.max(c4.getZ0() - shadowAt.getZ(), 0.0f), shadowAt.getZ() - c4.getZ1());
                    need = (dx * dx) + (dz * dz) < shR2;
                }
                if (need) {
                    c4.setLastInRange(frame);
                } else if (c4.getBuilt() && (!inRange || frame - c4.getLastInRange() > 900)) {
                    free(c4);
                    c4.setDirty(true);
                }
                if (!c4.getDirty() && c4.getBuilt()) {
                    if (need) {
                        this.visible.add(c4);
                    }
                } else if (need) {
                    toBuild.add(c4);
                }
            } else {
                it2.remove();
            }
        }
        String str6 = str4;
        String str7 = str5;
        if (toBuild.isEmpty()) {
            str = str7;
        } else {
            ArrayList arrayList = toBuild;
            if (arrayList.size() > 1) {
                CollectionsKt.sortWith(arrayList, new Comparator() { // from class: com.goydevv.studrblx.render.StaticBatches$update$$inlined$sortBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(Object t, Object t2) {
                        return ComparisonsKt.compareValues(Float.valueOf(((StaticBatches.Chunk) t).getDist()), Float.valueOf(((StaticBatches.Chunk) t2).getDist()));
                    }
                });
            }
            long end = System.nanoTime() + budgetNs;
            boolean first = true;
            Iterator it3 = toBuild.iterator();
            Intrinsics.checkNotNullExpressionValue(it3, str6);
            while (it3.hasNext()) {
                Object next2 = it3.next();
                Intrinsics.checkNotNullExpressionValue(next2, str7);
                Chunk c5 = (Chunk) next2;
                if (first || System.nanoTime() < end) {
                    first = false;
                    build(c5, frame, texEpoch);
                    if (c5.getBuilt()) {
                        this.visible.add(c5);
                    }
                    if (c5.getParts().isEmpty()) {
                        this.chunks.remove(Long.valueOf(c5.getKey()));
                    }
                } else {
                    Iterator<Instance> it4 = c5.getParts().iterator();
                    Intrinsics.checkNotNullExpressionValue(it4, str6);
                    while (it4.hasNext()) {
                        Instance next3 = it4.next();
                        Intrinsics.checkNotNullExpressionValue(next3, str7);
                        Instance p3 = next3;
                        Object obj3 = p3.renderRec;
                        PartRec r2 = obj3 instanceof PartRec ? (PartRec) obj3 : null;
                        if (r2 != null) {
                            if (r2.getChunk() == c5 && r2.getSeen() == frame) {
                                this.loose.add(p3);
                            }
                        }
                    }
                }
            }
            str = str7;
        }
        if (this.totalBytes > maxBytes) {
            Iterable iterableValues = this.chunks.values();
            Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
            Collection arrayList2 = new ArrayList();
            for (Object obj4 : iterableValues) {
                Chunk chunk3 = (Chunk) obj4;
                if (chunk3.getBuilt() && chunk3.getLastInRange() != frame) {
                    arrayList2.add(obj4);
                }
            }
            List idle = CollectionsKt.sortedWith((List) arrayList2, new Comparator() { // from class: com.goydevv.studrblx.render.StaticBatches$update$$inlined$sortedBy$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(Object t, Object t2) {
                    return ComparisonsKt.compareValues(Integer.valueOf(((StaticBatches.Chunk) t).getLastInRange()), Integer.valueOf(((StaticBatches.Chunk) t2).getLastInRange()));
                }
            });
            for (Object obj5 : idle) {
                Intrinsics.checkNotNullExpressionValue(obj5, str);
                Chunk c6 = (Chunk) obj5;
                if (this.totalBytes <= maxBytes) {
                    return;
                }
                free(c6);
                c6.setDirty(true);
            }
        }
    }

    private final void join(Instance p, PartRec rec) {
        Chunk chunk;
        CFrame cf = PartData.INSTANCE.cframe(p);
        float r = PartData.INSTANCE.size(p).getMagnitude() * 0.5f;
        Map map = this.chunks;
        Long lValueOf = Long.valueOf(keyOf(cf.getX(), cf.getY(), cf.getZ()));
        Chunk chunk2 = (Chunk) map.get(lValueOf);
        if (chunk2 == null) {
            chunk = new Chunk(keyOf(cf.getX(), cf.getY(), cf.getZ()));
            map.put(lValueOf, chunk);
        } else {
            chunk = chunk2;
        }
        Chunk c = chunk;
        c.getParts().add(p);
        c.grow(cf.getX(), cf.getY(), cf.getZ(), r);
        c.setMaxTier(Math.max(c.getMaxTier(), tierOf(r)));
        c.setDirty(true);
        rec.setChunk(c);
    }

    private final void free(Chunk c) {
        if (!c.getBatches().isEmpty()) {
            int size = c.getBatches().size();
            int[] ids = new int[size];
            for (int i = 0; i < size; i++) {
                ids[i] = c.getBatches().get(i).getVbo();
            }
            GLES20.glDeleteBuffers(ids.length, ids, 0);
        }
        this.totalBytes -= c.getBytes();
        c.setBytes(0L);
        c.setBatches(CollectionsKt.emptyList());
        c.setBuilt(false);
    }

    /* JADX INFO: compiled from: StaticBatches.kt */
    private static final class Group {
        private final int kind;
        private final ArrayList<BatchSpec> specs = new ArrayList<>();
        private final TexBind tex;
        private final int tier;
        private int verts;

        public Group(int tier, int kind, TexBind tex) {
            this.tier = tier;
            this.kind = kind;
            this.tex = tex;
        }

        public final int getKind() {
            return this.kind;
        }

        public final ArrayList<BatchSpec> getSpecs() {
            return this.specs;
        }

        public final TexBind getTex() {
            return this.tex;
        }

        public final int getTier() {
            return this.tier;
        }

        public final int getVerts() {
            return this.verts;
        }

        public final void setVerts(int i) {
            this.verts = i;
        }
    }

    /* JADX WARN: Code duplicated, block: B:68:0x023c  */
    /* JADX WARN: Code duplicated, block: B:70:0x0240  */
    /* JADX WARN: Code duplicated, block: B:71:0x0245  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v1, types: [java.util.List] */
    private final void build(Chunk c, int frame, int texEpoch) {
        Chunk chunk;
        ByteBuffer byteBuffer;
        int iCapacity;
        Object objSpec;
        Object group;
        ArrayList<Instance> arrayList = new ArrayList<>(c.getParts().size());
        Set once = Collections.newSetFromMap(new IdentityHashMap());
        Iterator<Instance> it = c.getParts().iterator();
        String str = "iterator(...)";
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (true) {
            chunk = null;
            if (!it.hasNext()) {
                break;
            }
            Instance next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Instance p = next;
            Object obj = p.renderRec;
            PartRec r = obj instanceof PartRec ? (PartRec) obj : null;
            if (r != null && r.getChunk() == c && once.add(p)) {
                if (r.getSeen() != frame) {
                    r.setChunk(null);
                } else {
                    arrayList.add(p);
                }
            }
        }
        c.setParts(arrayList);
        free(c);
        c.setX0(Float.MAX_VALUE);
        c.setY0(Float.MAX_VALUE);
        c.setZ0(Float.MAX_VALUE);
        c.setX1(-3.4028235E38f);
        c.setY1(-3.4028235E38f);
        c.setZ1(-3.4028235E38f);
        c.setMaxTier(0);
        LinkedHashMap groups = new LinkedHashMap();
        ArrayList<Instance> arrayList2 = new ArrayList<>(arrayList.size());
        Iterator<Instance> it2 = arrayList.iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        boolean pending = false;
        while (it2.hasNext()) {
            Instance next2 = it2.next();
            Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
            Instance p2 = next2;
            try {
                objSpec = this.host.spec(p2);
            } catch (Exception e) {
                objSpec = chunk;
            }
            Object obj2 = p2.renderRec;
            Intrinsics.checkNotNull(obj2, "null cannot be cast to non-null type com.goydevv.studrblx.render.StaticBatches.PartRec");
            PartRec rec = (PartRec) obj2;
            Collection collection = (Collection) objSpec;
            if (collection == null || collection.isEmpty()) {
                rec.setChunk(chunk);
                rec.setBatchable(false);
                rec.setKnown(true);
                this.loose.add(p2);
            } else {
                arrayList2.add(p2);
                float pr = PartData.INSTANCE.size(p2).getMagnitude() * 0.5f;
                CFrame pc = PartData.INSTANCE.cframe(p2);
                List r16 = (List) objSpec;
                ArrayList<Instance> arrayList3 = arrayList;
                Set once2 = once;
                c.grow(pc.getX(), pc.getY(), pc.getZ(), pr);
                int tier = tierOf(pr);
                c.setMaxTier(Math.max(c.getMaxTier(), tier));
                Iterator it3 = r16.iterator();
                while (it3.hasNext()) {
                    BatchSpec s = (BatchSpec) it3.next();
                    if (s.getPending()) {
                        pending = true;
                    }
                    Iterator it4 = it3;
                    PartRec rec2 = rec;
                    CFrame pc2 = pc;
                    GroupKey gk = new GroupKey(tier, s.getKind(), s.getTex());
                    Map map = groups;
                    Object obj3 = map.get(gk);
                    if (obj3 == null) {
                        group = new Group(tier, s.getKind(), s.getTex());
                        map.put(gk, group);
                    } else {
                        group = obj3;
                    }
                    Group g = (Group) group;
                    g.getSpecs().add(s);
                    g.setVerts(g.getVerts() + (s.getMesh().length / s.getStride()));
                    it3 = it4;
                    rec = rec2;
                    pc = pc2;
                }
                arrayList = arrayList3;
                once = once2;
                chunk = null;
            }
        }
        LinkedHashMap groups2 = groups;
        c.setParts(arrayList2);
        ArrayList out = new ArrayList(groups2.size());
        Iterator it5 = groups2.values().iterator();
        while (it5.hasNext()) {
            Object next3 = it5.next();
            Intrinsics.checkNotNullExpressionValue(next3, "next(...)");
            Group g2 = (Group) next3;
            if (g2.getVerts() != 0) {
                int ints = g2.getVerts() * 10;
                if (this.scratch.length < ints) {
                    this.scratch = new int[Math.max(ints, this.scratch.length * 2)];
                }
                int o = 0;
                Iterator<BatchSpec> it6 = g2.getSpecs().iterator();
                Intrinsics.checkNotNullExpressionValue(it6, str);
                while (it6.hasNext()) {
                    BatchSpec next4 = it6.next();
                    Intrinsics.checkNotNullExpressionValue(next4, "next(...)");
                    BatchSpec s2 = next4;
                    o = g2.getKind() == 3 ? packMesh(s2, this.scratch, o) : pack(s2, this.scratch, o);
                }
                int bytes = o * 4;
                if (this.direct != null) {
                    ByteBuffer byteBuffer2 = this.direct;
                    Intrinsics.checkNotNull(byteBuffer2);
                    if (byteBuffer2.capacity() < bytes) {
                        byteBuffer = this.direct;
                        if (byteBuffer != null) {
                            iCapacity = byteBuffer.capacity();
                        } else {
                            iCapacity = 0;
                        }
                        this.direct = ByteBuffer.allocateDirect(Math.max(bytes, iCapacity * 2)).order(ByteOrder.nativeOrder());
                    }
                } else {
                    byteBuffer = this.direct;
                    if (byteBuffer != null) {
                        iCapacity = byteBuffer.capacity();
                    } else {
                        iCapacity = 0;
                    }
                    this.direct = ByteBuffer.allocateDirect(Math.max(bytes, iCapacity * 2)).order(ByteOrder.nativeOrder());
                }
                ByteBuffer bb = this.direct;
                Intrinsics.checkNotNull(bb);
                bb.clear();
                IntBuffer ib = bb.asIntBuffer();
                Intrinsics.checkNotNullExpressionValue(ib, "asIntBuffer(...)");
                ib.put(this.scratch, 0, o);
                bb.position(0);
                bb.limit(bytes);
                Iterator it7 = it5;
                int[] ids = new int[1];
                GLES20.glGenBuffers(1, ids, 0);
                GLES20.glBindBuffer(34962, ids[0]);
                GLES20.glBufferData(34962, bytes, bb, 35044);
                out.add(new Batch(ids[0], o / (g2.getKind() == 3 ? 7 : 10), g2.getTier(), g2.getKind(), g2.getTex(), bytes));
                c.setBytes(c.getBytes() + ((long) bytes));
                it5 = it7;
            }
        }
        GLES20.glBindBuffer(34962, 0);
        this.totalBytes += c.getBytes();
        c.setBatches(out);
        c.setBuilt(true);
        c.setDirty(false);
        c.setTexEpoch(texEpoch);
        c.setPendingTex(pending);
        c.setLastInRange(frame);
    }

    /* JADX INFO: compiled from: StaticBatches.kt */
    private static final /* data */ class GroupKey {
        private final int kind;
        private final TexBind tex;
        private final int tier;

        public static /* synthetic */ GroupKey copy$default(GroupKey groupKey, int i, int i2, TexBind texBind, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                i = groupKey.tier;
            }
            if ((i3 & 2) != 0) {
                i2 = groupKey.kind;
            }
            if ((i3 & 4) != 0) {
                texBind = groupKey.tex;
            }
            return groupKey.copy(i, i2, texBind);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int component1() {
            return this.tier;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int component2() {
            return this.kind;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final TexBind component3() {
            return this.tex;
        }

        public final GroupKey copy(int tier, int kind, TexBind tex) {
            return new GroupKey(tier, kind, tex);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof GroupKey)) {
                return false;
            }
            GroupKey groupKey = (GroupKey) other;
            return this.tier == groupKey.tier && this.kind == groupKey.kind && Intrinsics.areEqual(this.tex, groupKey.tex);
        }

        public int hashCode() {
            return (((Integer.hashCode(this.tier) * 31) + Integer.hashCode(this.kind)) * 31) + (this.tex == null ? 0 : this.tex.hashCode());
        }

        public String toString() {
            return "GroupKey(tier=" + this.tier + ", kind=" + this.kind + ", tex=" + this.tex + ")";
        }

        public GroupKey(int tier, int kind, TexBind tex) {
            this.tier = tier;
            this.kind = kind;
            this.tex = tex;
        }

        public final int getKind() {
            return this.kind;
        }

        public final TexBind getTex() {
            return this.tex;
        }

        public final int getTier() {
            return this.tier;
        }
    }

    private final int b4(int a, int b, int c, int d) {
        return this.little ? (a & 255) | ((b & 255) << 8) | ((c & 255) << 16) | ((d & 255) << 24) : ((a & 255) << 24) | ((b & 255) << 16) | ((c & 255) << 8) | (d & 255);
    }

    private final int sb(float v) {
        float fCoerceIn = RangesKt.coerceIn(v, -1.0f, 1.0f) * 127.0f;
        return fCoerceIn >= 0.0f ? (int) (0.5f + fCoerceIn) : (int) (fCoerceIn - 0.5f);
    }

    private final int ub(float v) {
        return (int) ((RangesKt.coerceIn(v, 0.0f, 1.0f) * 255.0f) + 0.5f);
    }

    /* JADX WARN: Code duplicated, block: B:83:0x0287  */
    /* JADX WARN: Code duplicated, block: B:87:0x0318  */
    /* JADX WARN: Code duplicated, block: B:89:0x031c  */
    /* JADX WARN: Code duplicated, block: B:90:0x031f  */
    private final int pack(BatchSpec s, int[] out, int start) {
        float bx;
        float by;
        float bz;
        int surf = 0;
        int v;
        float ux;
        float uy;
        float uz;
        float u;
        float w;
        float ln;
        float z;
        CFrame cf = s.getCf();
        float sx = s.getScale().getX();
        float sy = s.getScale().getY();
        float sz = s.getScale().getZ();
        boolean z2 = false;
        float ix = !((sx > 0.0f ? 1 : (sx == 0.0f ? 0 : -1)) == 0) ? 1.0f / sx : 0.0f;
        float iy = !((sy > 0.0f ? 1 : (sy == 0.0f ? 0 : -1)) == 0) ? 1.0f / sy : 0.0f;
        float iz = !((sz > 0.0f ? 1 : (sz == 0.0f ? 0 : -1)) == 0) ? 1.0f / sz : 0.0f;
        int col = b4(ub(s.getR()), ub(s.getG()), ub(s.getB()), s.getEmissive() ? 255 : 0);
        float[] m = s.getMat();
        int iCoerceIn = RangesKt.coerceIn((int) m[0], 0, 255);
        int iCoerceIn2 = RangesKt.coerceIn((int) ((m[1] * 1000.0f) + 0.5f), 0, 255);
        int iCoerceIn3 = RangesKt.coerceIn((int) ((m[2] * 50.0f) + 0.5f), 0, 255);
        int i = 3;
        int o = start;
        int o2 = RangesKt.coerceIn((int) ((m[3] * 120.0f) + 0.5f), 0, 255);
        int matI = b4(iCoerceIn, iCoerceIn2, iCoerceIn3, o2);
        float[] mesh = s.getMesh();
        int st = s.getStride();
        int tris = mesh.length / (st * 3);
        int t = 0;
        while (t < tris) {
            int base = t * 3 * st;
            float anx = Math.abs(mesh[base + 3] + mesh[base + st + 3] + mesh[base + (st * 2) + 3]);
            float any = Math.abs(mesh[base + 4] + mesh[base + st + 4] + mesh[base + (st * 2) + 4]);
            float anz = Math.abs(mesh[base + 5] + mesh[base + st + 5] + mesh[base + (st * 2) + 5]);
            boolean yF = (any <= anx || any <= anz) ? z2 : true;
            boolean xF = (yF || anx <= anz) ? z2 : true;
            float tlx = (yF || !xF) ? 1.0f : 0.0f;
            float tlz = (yF || !xF) ? 0.0f : 1.0f;
            float tx = (cf.getR00() * tlx * sx) + (cf.getR02() * tlz * sz);
            float ty = (cf.getR10() * tlx * sx) + (cf.getR12() * tlz * sz);
            float tz = (cf.getR20() * tlx * sx) + (cf.getR22() * tlz * sz);
            int matI2 = matI;
            CFrame cf2 = cf;
            float tl = RangesKt.coerceAtLeast((float) Math.sqrt((tx * tx) + (ty * ty) + (tz * tz)), 1.0E-9f);
            float tx2 = tx / tl;
            float ty2 = ty / tl;
            float tl2 = tz / tl;
            if (yF) {
                bx = cf2.getR02();
                by = cf2.getR12();
                bz = cf2.getR22();
            } else {
                bx = cf2.getR01();
                by = cf2.getR11();
                bz = cf2.getR21();
            }
            float sx2 = sx;
            float sy2 = sy;
            float tl3 = RangesKt.coerceAtLeast((float) Math.sqrt((bx * bx) + (by * by) + (bz * bz)), 1.0E-9f);
            float bx2 = bx / tl3;
            float tl4 = by / tl3;
            float sz2 = sz;
            float bz2 = bz / tl3;
            float[] mesh2 = mesh;
            float ix2 = ix;
            int tanI = b4(sb(tx2), sb(ty2), sb(tl2), 0);
            int iSb = sb(bx2);
            int iSb2 = sb(tl4);
            int tanI2 = sb(bz2);
            int bitI = b4(iSb, iSb2, tanI2, 0);
            if (s.getCube()) {
                Vector3 a = s.getSurfA();
                Vector3 bb = s.getSurfB();
                if (yF) {
                    ln = mesh2[base + 4];
                } else {
                    ln = xF ? mesh2[base + 3] : mesh2[base + 5];
                }
                if (a != null && bb != null) {
                    if (yF) {
                        z = ln > 0.0f ? a.getY() : bb.getY();
                    } else if (xF) {
                        z = ln > 0.0f ? a.getX() : bb.getX();
                    } else {
                        z = ln > 0.0f ? a.getZ() : bb.getZ();
                    }
                    float v2 = z;
                    surf = RangesKt.coerceIn((int) v2, 0, 127);
                }
                v = 0;
                while (v < i) {
                    int k = base + (v * st);
                    float lx = mesh2[k];
                    float ly = mesh2[k + 1];
                    float lz = mesh2[k + 2];
                    float px = lx * sx2;
                    float py = ly * sy2;
                    float pz = lz * sz2;
                    out[o] = Float.floatToRawIntBits((cf2.getR00() * px) + (cf2.getR01() * py) + (cf2.getR02() * pz) + cf2.getX());
                    out[o + 1] = Float.floatToRawIntBits((cf2.getR10() * px) + (cf2.getR11() * py) + (cf2.getR12() * pz) + cf2.getY());
                    out[o + 2] = Float.floatToRawIntBits((cf2.getR20() * px) + (cf2.getR21() * py) + (cf2.getR22() * pz) + cf2.getZ());
                    ux = px + (sx2 * 0.5f);
                    uy = py + (sy2 * 0.5f);
                    uz = pz + (sz2 * 0.5f);
                    if (yF && xF) {
                        u = uz;
                    } else {
                        u = ux;
                    }
                    if (yF) {
                        w = uz;
                    } else {
                        w = uy;
                    }
                    out[o + 3] = Float.floatToRawIntBits(u);
                    out[o + 4] = Float.floatToRawIntBits(w);
                    float nx0 = mesh2[k + 3] * ix2;
                    float ny0 = mesh2[k + 4] * iy;
                    float nz0 = mesh2[k + 5] * iz;
                    float nx = (cf2.getR00() * nx0) + (cf2.getR01() * ny0) + (cf2.getR02() * nz0);
                    float ny = (cf2.getR10() * nx0) + (cf2.getR11() * ny0) + (cf2.getR12() * nz0);
                    float nz = (cf2.getR20() * nx0) + (cf2.getR21() * ny0) + (cf2.getR22() * nz0);
                    float nl = RangesKt.coerceAtLeast((float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz)), 1.0E-9f);
                    out[o + 5] = b4(sb(nx / nl), sb(ny / nl), sb(nz / nl), surf);
                    out[o + 6] = col;
                    out[o + 7] = matI2;
                    out[o + 8] = tanI;
                    out[o + 9] = bitI;
                    o += 10;
                    v++;
                    i = 3;
                }
                t++;
                sz = sz2;
                cf = cf2;
                matI = matI2;
                mesh = mesh2;
                ix = ix2;
                sy = sy2;
                sx = sx2;
                z2 = false;
                i = 3;
            }
            surf = 0;
            v = 0;
            while (v < i) {
                int k2 = base + (v * st);
                float lx2 = mesh2[k2];
                float ly2 = mesh2[k2 + 1];
                float lz2 = mesh2[k2 + 2];
                float px2 = lx2 * sx2;
                float py2 = ly2 * sy2;
                float pz2 = lz2 * sz2;
                out[o] = Float.floatToRawIntBits((cf2.getR00() * px2) + (cf2.getR01() * py2) + (cf2.getR02() * pz2) + cf2.getX());
                out[o + 1] = Float.floatToRawIntBits((cf2.getR10() * px2) + (cf2.getR11() * py2) + (cf2.getR12() * pz2) + cf2.getY());
                out[o + 2] = Float.floatToRawIntBits((cf2.getR20() * px2) + (cf2.getR21() * py2) + (cf2.getR22() * pz2) + cf2.getZ());
                ux = px2 + (sx2 * 0.5f);
                uy = py2 + (sy2 * 0.5f);
                uz = pz2 + (sz2 * 0.5f);
                if (yF) {
                    u = ux;
                } else {
                    u = ux;
                }
                if (yF) {
                    w = uz;
                } else {
                    w = uy;
                }
                out[o + 3] = Float.floatToRawIntBits(u);
                out[o + 4] = Float.floatToRawIntBits(w);
                float nx1 = mesh2[k2 + 3] * ix2;
                float ny1 = mesh2[k2 + 4] * iy;
                float nz1 = mesh2[k2 + 5] * iz;
                float nx2 = (cf2.getR00() * nx1) + (cf2.getR01() * ny1) + (cf2.getR02() * nz1);
                float ny2 = (cf2.getR10() * nx1) + (cf2.getR11() * ny1) + (cf2.getR12() * nz1);
                float nz2 = (cf2.getR20() * nx1) + (cf2.getR21() * ny1) + (cf2.getR22() * nz1);
                float nl2 = RangesKt.coerceAtLeast((float) Math.sqrt((nx2 * nx2) + (ny2 * ny2) + (nz2 * nz2)), 1.0E-9f);
                out[o + 5] = b4(sb(nx2 / nl2), sb(ny2 / nl2), sb(nz2 / nl2), surf);
                out[o + 6] = col;
                out[o + 7] = matI2;
                out[o + 8] = tanI;
                out[o + 9] = bitI;
                o += 10;
                v++;
                i = 3;
            }
            t++;
            sz = sz2;
            cf = cf2;
            matI = matI2;
            mesh = mesh2;
            ix = ix2;
            sy = sy2;
            sx = sx2;
            z2 = false;
            i = 3;
        }
        return o;
    }

    private final int packMesh(BatchSpec s, int[] out, int start) {
        int o = start;
        CFrame cf = s.getCf();
        float sx = s.getScale().getX();
        float sy = s.getScale().getY();
        float sz = s.getScale().getZ();
        float ix = !((sx > 0.0f ? 1 : (sx == 0.0f ? 0 : -1)) == 0) ? 1.0f / sx : 0.0f;
        float iy = !((sy > 0.0f ? 1 : (sy == 0.0f ? 0 : -1)) == 0) ? 1.0f / sy : 0.0f;
        float iz = !(sz == 0.0f) ? 1.0f / sz : 0.0f;
        int col = b4(ub(s.getR()), ub(s.getG()), ub(s.getB()), s.getEmissive() ? 255 : 0);
        float[] m = s.getMesh();
        int st = s.getStride();
        float ox = s.getOffset().getX();
        float oy = s.getOffset().getY();
        float oz = s.getOffset().getZ();
        int k = 0;
        while (k + st <= m.length) {
            float px = (m[k] - ox) * sx;
            float py = (m[k + 1] - oy) * sy;
            float pz = (m[k + 2] - oz) * sz;
            out[o] = Float.floatToRawIntBits((cf.getR00() * px) + (cf.getR01() * py) + (cf.getR02() * pz) + cf.getX());
            out[o + 1] = Float.floatToRawIntBits((cf.getR10() * px) + (cf.getR11() * py) + (cf.getR12() * pz) + cf.getY());
            out[o + 2] = Float.floatToRawIntBits((cf.getR20() * px) + (cf.getR21() * py) + (cf.getR22() * pz) + cf.getZ());
            int o2 = o;
            out[o + 3] = Float.floatToRawIntBits(st >= 8 ? m[k + 6] : 0.0f);
            out[o2 + 4] = Float.floatToRawIntBits(st >= 8 ? m[k + 7] : 0.0f);
            float nx0 = m[k + 3] * ix;
            float ny0 = m[k + 4] * iy;
            float nz0 = m[k + 5] * iz;
            float nx = (cf.getR00() * nx0) + (cf.getR01() * ny0) + (cf.getR02() * nz0);
            float ny = (cf.getR10() * nx0) + (cf.getR11() * ny0) + (cf.getR12() * nz0);
            float nz = (cf.getR20() * nx0) + (cf.getR21() * ny0) + (cf.getR22() * nz0);
            float nl = RangesKt.coerceAtLeast((float) Math.sqrt((nx * nx) + (ny * ny) + (nz * nz)), 1.0E-9f);
            out[o2 + 5] = b4(sb(nx / nl), sb(ny / nl), sb(nz / nl), 0);
            out[o2 + 6] = col;
            o = o2 + 7;
            k += st;
        }
        return o;
    }
}
