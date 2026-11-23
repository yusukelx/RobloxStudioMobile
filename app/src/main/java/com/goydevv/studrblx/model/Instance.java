package com.goydevv.studrblx.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Instance.kt */
/* JADX INFO: loaded from: classes4.dex */
public class Instance {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static volatile long changeCount;
    private float absH;
    private float absW;
    private float absX;
    private float absY;
    private boolean archivable;
    private final LinkedHashMap<String, Object> attributes;
    private final ArrayList<Instance> children;
    private String className;
    private boolean destroyed;
    private boolean isService;
    private int loadedRef;
    private Instance parent;
    private final HashMap<String, Integer> propTypes;
    private final LinkedHashMap<String, Object> props;
    public Object renderRec;
    public int version;

    public Instance(String className) {
        Intrinsics.checkNotNullParameter(className, "className");
        this.className = className;
        this.props = new LinkedHashMap<>();
        this.propTypes = new HashMap<>();
        this.children = new ArrayList<>();
        this.archivable = true;
        this.loadedRef = -1;
        this.attributes = new LinkedHashMap<>();
    }

    public final String getClassName() {
        return this.className;
    }

    public final void setClassName(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.className = str;
    }

    public final LinkedHashMap<String, Object> getProps() {
        return this.props;
    }

    public final HashMap<String, Integer> getPropTypes() {
        return this.propTypes;
    }

    public final Instance getParent() {
        return this.parent;
    }

    public final void setParent$app(Instance instance) {
        this.parent = instance;
    }

    public final ArrayList<Instance> getChildren() {
        return this.children;
    }

    /* JADX INFO: renamed from: isService, reason: from getter */
    public final boolean getIsService() {
        return this.isService;
    }

    public final void setService(boolean z) {
        this.isService = z;
    }

    public final boolean getArchivable() {
        return this.archivable;
    }

    public final void setArchivable(boolean z) {
        this.archivable = z;
    }

    public final boolean getDestroyed() {
        return this.destroyed;
    }

    public final void setDestroyed(boolean z) {
        this.destroyed = z;
    }

    public final int getLoadedRef() {
        return this.loadedRef;
    }

    public final void setLoadedRef(int i) {
        this.loadedRef = i;
    }

    public final LinkedHashMap<String, Object> getAttributes() {
        return this.attributes;
    }

    public final float getAbsH() {
        return this.absH;
    }

    public final float getAbsW() {
        return this.absW;
    }

    public final float getAbsX() {
        return this.absX;
    }

    public final float getAbsY() {
        return this.absY;
    }

    public final void setAbsH(float f) {
        this.absH = f;
    }

    public final void setAbsW(float f) {
        this.absW = f;
    }

    public final void setAbsX(float f) {
        this.absX = f;
    }

    public final void setAbsY(float f) {
        this.absY = f;
    }

    public final String getName() {
        Object obj = this.props.get("Name");
        String str = obj instanceof String ? (String) obj : null;
        return str == null ? this.className : str;
    }

    public final void setName(String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        set$default(this, "Name", v, false, 4, null);
    }

    public final Object get(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        return this.props.get(name);
    }

    public static /* synthetic */ void set$default(Instance instance, String str, Object obj, boolean z, int i, Object obj2) {
        if (obj2 != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: set");
        }
        if ((i & 4) != 0) {
            z = true;
        }
        instance.set(str, obj, z);
    }

    public final void set(String name, Object value, boolean notify) {
        DataModel dataModelRoot;
        Instance instance;
        Intrinsics.checkNotNullParameter(name, "name");
        Object old = this.props.get(name);
        if (this.props.containsKey(name) && Intrinsics.areEqual(old, value)) {
            return;
        }
        this.props.put(name, value);
        this.version++;
        if (affectsParentShape() && (instance = this.parent) != null) {
            instance.version++;
        }
        long j = changeCount;
        Companion companion = INSTANCE;
        changeCount = j + 1;
        if (!this.propTypes.containsKey(name)) {
            this.propTypes.put(name, Integer.valueOf(PropType.INSTANCE.infer(value)));
        }
        if (!notify) {
            return;
        }
        dataModelRoot = root();
        if (dataModelRoot == null) {
            if (!detachedListeners.isEmpty()) {
                for (TreeListener l : detachedListeners) {
                    l.propChanged(this, name);
                }
            }
            return;
        }
        dataModelRoot.firePropChanged(this, name);
    }

    public static final java.util.concurrent.CopyOnWriteArrayList<TreeListener> detachedListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final boolean affectsParentShape() {
        String str = this.className;
        switch (str.hashCode()) {
            case -1418764294:
                if (str.equals("BlockMesh")) {
                    return true;
                }
                break;
            case -671103575:
                if (str.equals("FileMesh")) {
                    return true;
                }
                break;
            case 795242022:
                if (str.equals("SpecialMesh")) {
                    return true;
                }
                break;
            case 978161873:
                if (str.equals("SurfaceAppearance")) {
                    return true;
                }
                break;
            case 1108929283:
                if (str.equals("CylinderMesh")) {
                    return true;
                }
                break;
            case 1242205543:
                if (str.equals("BevelMesh")) {
                    return true;
                }
                break;
        }
        Instance instance = this.parent;
        return (instance != null && instance.isA("PartOperation")) && isA("BasePart");
    }

    public final DataModel root() {
        for (Instance p = this; p != null; p = p.parent) {
            if (p instanceof DataModel) {
                return (DataModel) p;
            }
        }
        return null;
    }

    public final boolean isDescendantOf(Instance other) {
        Intrinsics.checkNotNullParameter(other, "other");
        for (Instance p = this.parent; p != null; p = p.parent) {
            if (p == other) {
                return true;
            }
        }
        return false;
    }

    public static /* synthetic */ void setParent$default(Instance instance, Instance instance2, int i, int i2, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setParent");
        }
        if ((i2 & 2) != 0) {
            i = -1;
        }
        instance.setParent(instance2, i);
    }

    public final void setParent(Instance newParent, int index) {
        if (newParent == this.parent) {
            return;
        }
        if (newParent != null && (newParent == this || newParent.isDescendantOf(this))) {
            throw new IllegalArgumentException("Attempt to set parent of " + getName() + " to " + newParent.getName() + " would result in circular reference");
        }
        Instance old = this.parent;
        this.version++;
        long j = changeCount;
        Companion companion = INSTANCE;
        changeCount = j + 1;
        boolean shape = affectsParentShape();
        if (old != null) {
            if (shape) {
                old.version++;
            }
            old.children.remove(this);
            this.parent = null;
            DataModel dataModelRoot = old.root();
            if (dataModelRoot != null) {
                dataModelRoot.fireChildRemoved(old, this);
            }
        }
        if (newParent != null) {
            if (shape) {
                newParent.version++;
            }
            if (index >= 0 && index <= newParent.children.size()) {
                newParent.children.add(index, this);
            } else {
                newParent.children.add(this);
            }
            this.parent = newParent;
            DataModel dataModelRoot2 = newParent.root();
            if (dataModelRoot2 != null) {
                dataModelRoot2.fireChildAdded(newParent, this);
            }
        }
    }

    public final void attachRaw(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        this.parent = p;
        p.children.add(this);
    }

    public final void destroy() {
        if (this.destroyed) {
            return;
        }
        markDestroyed();
        setParent$default(this, null, 0, 2, null);
    }

    private final void markDestroyed() {
        this.destroyed = true;
        for (Instance c : CollectionsKt.toList(this.children)) {
            c.markDestroyed();
        }
    }

    public final boolean isA(String cls) {
        Intrinsics.checkNotNullParameter(cls, "cls");
        return ClassDb.INSTANCE.isA(this.className, cls);
    }

    public static /* synthetic */ Instance findFirstChild$default(Instance instance, String str, boolean z, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: findFirstChild");
        }
        if ((i & 2) != 0) {
            z = false;
        }
        return instance.findFirstChild(str, z);
    }

    public final Instance findFirstChild(String n, boolean recursive) {
        Intrinsics.checkNotNullParameter(n, "n");
        Iterator<Instance> it = this.children.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance c = it.next();
            if (Intrinsics.areEqual(c.getName(), n)) {
                return c;
            }
        }
        if (recursive) {
            Iterator<Instance> it2 = this.children.iterator();
            Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
            while (it2.hasNext()) {
                Instance instanceFindFirstChild = it2.next().findFirstChild(n, true);
                if (instanceFindFirstChild != null) {
                    return instanceFindFirstChild;
                }
            }
            return null;
        }
        return null;
    }

    public final Instance findFirstChildOfClass(String cls) {
        Object next;
        Intrinsics.checkNotNullParameter(cls, "cls");
        Iterator it = this.children.iterator();
        while (it.hasNext()) {
            next = it.next();
            if (Intrinsics.areEqual(((Instance) next).className, cls)) {
                return (Instance) next;
            }
        }
        next = null;
        return (Instance) next;
    }

    public static /* synthetic */ Instance findFirstChildWhichIsA$default(Instance instance, String str, boolean z, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: findFirstChildWhichIsA");
        }
        if ((i & 2) != 0) {
            z = false;
        }
        return instance.findFirstChildWhichIsA(str, z);
    }

    public final Instance findFirstChildWhichIsA(String cls, boolean recursive) {
        Intrinsics.checkNotNullParameter(cls, "cls");
        Iterator<Instance> it = this.children.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance c = it.next();
            if (c.isA(cls)) {
                return c;
            }
        }
        if (recursive) {
            Iterator<Instance> it2 = this.children.iterator();
            Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
            while (it2.hasNext()) {
                Instance instanceFindFirstChildWhichIsA = it2.next().findFirstChildWhichIsA(cls, true);
                if (instanceFindFirstChildWhichIsA != null) {
                    return instanceFindFirstChildWhichIsA;
                }
            }
            return null;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ List<Instance> descendants$default(Instance instance, List list, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: descendants");
        }
        if ((i & 1) != 0) {
            list = new ArrayList();
        }
        return instance.descendants(list);
    }

    public final List<Instance> descendants(List<Instance> out) {
        Intrinsics.checkNotNullParameter(out, "out");
        Iterator<Instance> it = this.children.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Instance c = it.next();
            out.add(c);
            c.descendants(out);
        }
        return out;
    }

    public final String fullName() {
        ArrayList parts = new ArrayList();
        for (Instance p = this; p != null && !(p instanceof DataModel); p = p.parent) {
            parts.add(p.getName());
        }
        return CollectionsKt.joinToString(CollectionsKt.reversed(parts), ".", "", "", -1, "...", null);
    }

    public final Instance cloneTree() {
        IdentityHashMap<Instance, Instance> identityHashMap = new IdentityHashMap<>();
        Instance r = INSTANCE.copyInto$app(this, identityHashMap, new Instance(this.className));
        INSTANCE.remapRefs$app(identityHashMap);
        return r;
    }

    public String toString() {
        return getName();
    }

    /* JADX INFO: compiled from: Instance.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Instance copyInto$app(Instance src, IdentityHashMap<Instance, Instance> map, Instance dst) {
            Intrinsics.checkNotNullParameter(src, "src");
            Intrinsics.checkNotNullParameter(map, "map");
            Intrinsics.checkNotNullParameter(dst, "dst");
            dst.getProps().putAll(src.getProps());
            dst.getPropTypes().putAll(src.getPropTypes());
            dst.setService(src.getIsService());
            dst.getAttributes().putAll(src.getAttributes());
            map.put(src, dst);
            Iterator<Instance> it = src.getChildren().iterator();
            Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
            while (it.hasNext()) {
                Instance ch = it.next();
                if (ch.getArchivable()) {
                    Instance cc = copyInto$app(ch, map, new Instance(ch.getClassName()));
                    cc.attachRaw(dst);
                }
            }
            return dst;
        }

        public final void remapRefs$app(IdentityHashMap<Instance, Instance> map) {
            Instance instance;
            Intrinsics.checkNotNullParameter(map, "map");
            for (Instance c : map.values()) {
                for (Map.Entry<String, Object> entry : c.getProps().entrySet()) {
                    Object v = entry.getValue();
                    if (v instanceof Instance) {
                        Instance instance2 = map.get(v);
                        if (instance2 != null) {
                            entry.setValue(instance2);
                        }
                    } else if ((v instanceof ContentValue) && ((ContentValue) v).getObj() != null && (instance = map.get(((ContentValue) v).getObj())) != null) {
                        entry.setValue(ContentValue.copy$default((ContentValue) v, 0, null, instance, 3, null));
                    }
                }
            }
        }
    }
}
