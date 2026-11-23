package com.goydevv.studrblx.model;

import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Enums.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class PartData {
    public static final PartData INSTANCE = new PartData();

    private PartData() {
    }

    public final CFrame cframe(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        Object obj = p.get("CFrame");
        if (obj instanceof CFrame) {
            return (CFrame) obj;
        }
        Object posObj = p.get("Position");
        Vector3 pos = posObj instanceof Vector3 ? (Vector3) posObj : null;
        Object rotObj = p.get("Orientation");
        if (!(rotObj instanceof Vector3)) {
            rotObj = p.get("Rotation");
        }
        Vector3 rotDeg = rotObj instanceof Vector3 ? (Vector3) rotObj : null;
        if (pos != null || rotDeg != null) {
            Vector3 pVec = pos != null ? pos : Vector3.INSTANCE.getZERO();
            if (rotDeg != null) {
                float rx = (float) Math.toRadians(rotDeg.getX());
                float ry = (float) Math.toRadians(rotDeg.getY());
                float rz = (float) Math.toRadians(rotDeg.getZ());
                return CFrame.INSTANCE.fromOrientation(rx, ry, rz).withPosition(pVec);
            }
            return CFrame.INSTANCE.at(pVec);
        }
        return CFrame.INSTANCE.getIDENTITY();
    }

    public final Vector3 size(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        Object obj = p.get("size");
        if (obj == null) {
            obj = p.get("Size");
        }
        Vector3 vector3 = obj instanceof Vector3 ? (Vector3) obj : null;
        return vector3 == null ? new Vector3(4.0f, 1.0f, 2.0f) : vector3;
    }

    public final Color3 color(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        Object obj = p.get("Color3uint8");
        Color3 color3 = obj instanceof Color3 ? (Color3) obj : null;
        if (color3 != null) {
            return color3;
        }
        Object obj2 = p.get("Color");
        Color3 color4 = obj2 instanceof Color3 ? (Color3) obj2 : null;
        if (color4 != null) {
            return color4;
        }
        Object obj3 = p.get("BrickColor");
        BrickColorV brickColorV = obj3 instanceof BrickColorV ? (BrickColorV) obj3 : null;
        if (brickColorV != null) {
            return brickColorV.getColor();
        }
        return Color3.INSTANCE.fromRGB(163, 162, 165);
    }

    public final float transparency(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        Object obj = p.get("Transparency");
        Number number = obj instanceof Number ? (Number) obj : null;
        if (number != null) {
            return number.floatValue();
        }
        return 0.0f;
    }

    public final int material(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        Object obj = p.get("Material");
        EnumValue enumValue = obj instanceof EnumValue ? (EnumValue) obj : null;
        if (enumValue != null) {
            return enumValue.getValue();
        }
        return 256;
    }

    public final String content(Instance p, String... keys) {
        String string;
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(keys, "keys");
        int length = keys.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                return null;
            }
            String k = keys[i];
            Object obj = p.get(k);
            if (obj != null && (string = obj.toString()) != null) {
                String str = StringsKt.isBlank(string) ? null : string;
                if (str != null) {
                    return str;
                }
            }
            i++;
        }
    }

    public final String meshContent(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        return content(p, "MeshId", "MeshID", "MeshContent");
    }

    public final String textureContent(Instance p) {
        Intrinsics.checkNotNullParameter(p, "p");
        return content(p, "TextureID", "TextureId", "TextureContent");
    }

    public final int shape(Instance p) {
        Object next;
        Intrinsics.checkNotNullParameter(p, "p");
        String className = p.getClassName();
        if (Intrinsics.areEqual(className, "WedgePart")) {
            return 3;
        }
        if (Intrinsics.areEqual(className, "CornerWedgePart")) {
            return 4;
        }
        Object obj = p.get("shape");
        if (obj == null) {
            obj = p.get("Shape");
        }
        EnumValue s = obj instanceof EnumValue ? (EnumValue) obj : null;
        if (s != null) {
            return s.getValue();
        }
        Iterator it = p.getChildren().iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!Intrinsics.areEqual(((Instance) next).getClassName(), "SpecialMesh"));
        Instance mesh = (Instance) next;
        if (mesh != null) {
            Object obj2 = mesh.get("MeshType");
            EnumValue enumValue = obj2 instanceof EnumValue ? (EnumValue) obj2 : null;
            Integer numValueOf = enumValue != null ? Integer.valueOf(enumValue.getValue()) : null;
            if (numValueOf == null || numValueOf.intValue() != 3) {
                return (numValueOf == null || numValueOf.intValue() != 2) ? 1 : 3;
            }
            return 0;
        }
        return 1;
    }

    public final void setCFrame(Instance p, CFrame cf) {
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(cf, "cf");
        Instance.set$default(p, "CFrame", cf, false, 4, null);
    }

    public final void setSize(Instance p, Vector3 s) {
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(s, "s");
        if (!p.getProps().containsKey("Size") || p.getProps().containsKey("size")) {
            Instance.set$default(p, "size", s, false, 4, null);
        } else {
            Instance.set$default(p, "Size", s, false, 4, null);
        }
    }

    public final void setColor(Instance p, Color3 c) {
        Intrinsics.checkNotNullParameter(p, "p");
        Intrinsics.checkNotNullParameter(c, "c");
        if (p.getProps().containsKey("Color3uint8")) {
            Instance.set$default(p, "Color3uint8", c, false, 4, null);
        } else if (p.getProps().containsKey("Color")) {
            Instance.set$default(p, "Color", c, false, 4, null);
        } else {
            Instance.set$default(p, "Color3uint8", c, false, 4, null);
            p.getPropTypes().put("Color3uint8", 26);
        }
        if (p.getProps().containsKey("BrickColor")) {
            Instance.set$default(p, "BrickColor", new BrickColorV(BrickColors.INSTANCE.closest(c)), false, 4, null);
        }
    }
}
