package com.goydevv.studrblx.render;

import com.goydevv.studrblx.util.BackgroundPool;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.net.AssetFetcher;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: RbxMesh.kt */
/* JADX INFO: loaded from: classes8.dex */
public final class MeshLoader {
    private static volatile int epoch;
    private static volatile boolean waiting;
    public static final MeshLoader INSTANCE = new MeshLoader();
    private static final ConcurrentHashMap<Long, MeshData> ready = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> bad = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> pending = ConcurrentHashMap.newKeySet();
    private static final ExecutorService pool = BackgroundPool.create(2);
    private static final CopyOnWriteArrayList<Function1<Long, Unit>> listeners = new CopyOnWriteArrayList<>();

    private MeshLoader() {
    }

    public final CopyOnWriteArrayList<Function1<Long, Unit>> getListeners() {
        return listeners;
    }

    public final int getEpoch() {
        return epoch;
    }

    public final MeshData get(final long id) {
        MeshData meshData = ready.get(Long.valueOf(id));
        if (meshData != null) {
            return meshData;
        }
        if (bad.contains(Long.valueOf(id)) || !pending.add(Long.valueOf(id))) {
            return null;
        }
        final byte[] bytes = AssetFetcher.INSTANCE.request(id);
        if (bytes == null) {
            pending.remove(Long.valueOf(id));
            if (!waiting) {
                waiting = true;
                AssetFetcher.INSTANCE.getListeners().add(new Function1() { // from class: com.goydevv.studrblx.render.MeshLoader$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return MeshLoader.get$lambda$1(((Long) obj).longValue());
                    }
                });
            }
            return null;
        }
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.render.MeshLoader$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MeshLoader.get$lambda$3(id, bytes);
            }
        });
        return null;
    }

    static final Unit get$lambda$1(long done) {
        if (!ready.containsKey(Long.valueOf(done)) && !bad.contains(Long.valueOf(done))) {
            MeshLoader meshLoader = INSTANCE;
            int i = epoch;
            MeshLoader meshLoader2 = INSTANCE;
            epoch = i + 1;
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0077 A[Catch: all -> 0x00de, TryCatch #2 {all -> 0x00de, blocks: (B:2:0x0001, B:3:0x0009, B:9:0x001a, B:6:0x0011, B:16:0x0055, B:18:0x0057, B:20:0x0064, B:23:0x006c, B:25:0x0077, B:27:0x007f, B:29:0x0085, B:28:0x0082), top: B:46:0x0001, inners: #1 }] */
    /* JADX WARN: Code duplicated, block: B:27:0x007f A[Catch: all -> 0x00de, TryCatch #2 {all -> 0x00de, blocks: (B:2:0x0001, B:3:0x0009, B:9:0x001a, B:6:0x0011, B:16:0x0055, B:18:0x0057, B:20:0x0064, B:23:0x006c, B:25:0x0077, B:27:0x007f, B:29:0x0085, B:28:0x0082), top: B:46:0x0001, inners: #1 }] */
    /* JADX WARN: Code duplicated, block: B:28:0x0082 A[Catch: all -> 0x00de, TryCatch #2 {all -> 0x00de, blocks: (B:2:0x0001, B:3:0x0009, B:9:0x001a, B:6:0x0011, B:16:0x0055, B:18:0x0057, B:20:0x0064, B:23:0x006c, B:25:0x0077, B:27:0x007f, B:29:0x0085, B:28:0x0082), top: B:46:0x0001, inners: #1 }] */
    static final void get$lambda$3(long $id, byte[] $bytes) {
        String message;
        Object exc;
        MeshData meshData;
        try {
            try {
                ConcurrentHashMap<Long, MeshData> concurrentHashMap = ready;
                Long lValueOf = Long.valueOf($id);
                try {
                    meshData = RbxMesh.INSTANCE.parse($bytes);
                } catch (Exception e) {
                    MeshData meshDataMeshInsideModel = INSTANCE.meshInsideModel($id, $bytes);
                    if (meshDataMeshInsideModel == null) {
                        throw e;
                    }
                    meshData = meshDataMeshInsideModel;
                }
                concurrentHashMap.put(lValueOf, meshData);
                pending.remove(Long.valueOf($id));
                MeshLoader meshLoader = INSTANCE;
                int i = epoch;
                MeshLoader meshLoader2 = INSTANCE;
                epoch = i + 1;
                MeshLoader meshLoader3 = INSTANCE;
                Iterator it = listeners.iterator();
                while (it.hasNext()) {
                    ((Function1) it.next()).invoke(Long.valueOf($id));
                }
            } catch (Exception e2) {
                bad.add(Long.valueOf($id));
                if (e2 instanceof RbxMesh.UnsupportedMesh) {
                    String message2 = e2.getMessage();
                    if (message2 == null) {
                        message2 = "";
                    }
                    if (!StringsKt.startsWith(message2, "not a", false)) {
                        OutputLog outputLog = OutputLog.INSTANCE;
                        message = e2.getMessage();
                        if (message != null) {
                            exc = message;
                        } else {
                            exc = e2;
                        }
                        outputLog.warn("Mesh " + $id + ": " + exc);
                    }
                } else {
                    OutputLog outputLog2 = OutputLog.INSTANCE;
                    message = e2.getMessage();
                    if (message != null) {
                        exc = message;
                    } else {
                        exc = e2;
                    }
                    outputLog2.warn("Mesh " + $id + ": " + exc);
                }
                pending.remove(Long.valueOf($id));
                MeshLoader meshLoader4 = INSTANCE;
                int i2 = epoch;
                MeshLoader meshLoader5 = INSTANCE;
                epoch = i2 + 1;
                MeshLoader meshLoader6 = INSTANCE;
                Iterator it2 = listeners.iterator();
                while (it2.hasNext()) {
                    ((Function1) it2.next()).invoke(Long.valueOf($id));
                }
            }
        } catch (Throwable th) {
            pending.remove(Long.valueOf($id));
            MeshLoader meshLoader7 = INSTANCE;
            int i3 = epoch;
            MeshLoader meshLoader8 = INSTANCE;
            epoch = i3 + 1;
            MeshLoader meshLoader9 = INSTANCE;
            Iterator it3 = listeners.iterator();
            while (it3.hasNext()) {
                ((Function1) it3.next()).invoke(Long.valueOf($id));
            }
            throw th;
        }
    }

    public final boolean isBad(long id) {
        return bad.contains(Long.valueOf(id));
    }

    private final MeshData meshInsideModel(long id, byte[] bytes) {
        try {
            RbxDocument doc = RbxFile.INSTANCE.read(bytes);
            for (Instance r : doc.getRoots()) {
                for (Instance i : (List<Instance>) CollectionsKt.plus((Collection) CollectionsKt.listOf(r), (Iterable) Instance.descendants$default(r, null, 1, null))) {
                    Long lIdOf = AssetFetcher.INSTANCE.idOf(PartData.INSTANCE.meshContent(i));
                    if (lIdOf != null) {
                        long inner = lIdOf.longValue();
                        if (inner != id) {
                            try {
                                return RbxMesh.INSTANCE.parse(AssetFetcher.INSTANCE.load(inner));
                            } catch (Exception e) {
                                return null;
                            }
                        }
                    }
                }
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }
}
