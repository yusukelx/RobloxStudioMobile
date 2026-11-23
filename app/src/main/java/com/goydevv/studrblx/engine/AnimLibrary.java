package com.goydevv.studrblx.engine;

import com.goydevv.studrblx.util.BackgroundPool;
import com.goydevv.studrblx.format.RbxDocument;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.net.AssetFetcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: KeyframeAnim.kt */
/* JADX INFO: loaded from: classes7.dex */
public final class AnimLibrary {
    public static final AnimLibrary INSTANCE = new AnimLibrary();
    private static final ConcurrentHashMap<Long, AnimClip> clips = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> failed = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap.KeySetView<Long, Boolean> pending = ConcurrentHashMap.newKeySet();
    private static final ExecutorService pool = BackgroundPool.create(3);

    private AnimLibrary() {
    }

    public final AnimClip get(final long id) {
        AnimClip animClip = clips.get(Long.valueOf(id));
        if (animClip != null) {
            return animClip;
        }
        if (failed.contains(Long.valueOf(id)) || !pending.add(Long.valueOf(id))) {
            return null;
        }
        pool.execute(new Runnable() { // from class: com.goydevv.studrblx.engine.AnimLibrary$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnimLibrary.get$lambda$4(id);
            }
        });
        return null;
    }

    static final void get$lambda$4(long $id) {
        Object next;
        try {
            try {
                byte[] bytes = AssetFetcher.INSTANCE.load($id);
                RbxDocument doc = RbxFile.INSTANCE.read(bytes);
                Iterable roots = doc.getRoots();
                Collection arrayList = new ArrayList();
                Iterator it = roots.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Instance instance = (Instance) it.next();
                    CollectionsKt.addAll(arrayList, CollectionsKt.plus((Collection) CollectionsKt.listOf(instance), (Iterable) Instance.descendants$default(instance, null, 1, null)));
                }
                Iterator it2 = ((List) arrayList).iterator();
                do {
                    if (!it2.hasNext()) {
                        next = null;
                        break;
                    }
                    next = it2.next();
                } while (!Intrinsics.areEqual(((Instance) next).getClassName(), "KeyframeSequence"));
                Instance seq = (Instance) next;
                AnimClip clip = seq != null ? AnimClip.INSTANCE.from(seq) : null;
                if (clip != null) {
                    clips.put(Long.valueOf($id), clip);
                } else {
                    failed.add(Long.valueOf($id));
                }
            } catch (Exception e) {
                failed.add(Long.valueOf($id));
            }
        } finally {
            pending.remove(Long.valueOf($id));
        }
    }

    public final void trimMemory(int level) {
        if (level >= 40) {
            clips.clear();
            failed.clear();
        }
    }

    public final boolean failed(long id) {
        return failed.contains(Long.valueOf(id));
    }

    public final AnimClip fromInstance(Instance seq) {
        Intrinsics.checkNotNullParameter(seq, "seq");
        return AnimClip.INSTANCE.from(seq);
    }
}
