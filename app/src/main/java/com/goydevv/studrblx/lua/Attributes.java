package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.format.RbxBinaryKt;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.ColorKey;
import com.goydevv.studrblx.model.ColorSequence;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.FontValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.NumberKey;
import com.goydevv.studrblx.model.NumberRange;
import com.goydevv.studrblx.model.NumberSequence;
import com.goydevv.studrblx.model.Rect;
import com.goydevv.studrblx.model.UDim;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: InstanceApi.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Attributes {
    public static final Attributes INSTANCE = new Attributes();
    private static final Set<Instance> loaded = Collections.newSetFromMap(new WeakHashMap());

    private Attributes() {
    }

    public final void load(Instance i) {
        byte[] blob;
        Object objLoad$str;
        Intrinsics.checkNotNullParameter(i, "i");
        if (loaded.add(i)) {
            Object v = i.get("AttributesSerialize");
            if (v instanceof BinaryString) {
                blob = ((BinaryString) v).getBytes();
            } else {
                if (!(v instanceof String)) {
                    return;
                }
                blob = ((String) v).getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue(blob, "getBytes(...)");
            }
            if (blob.length < 4) {
                return;
            }
            try {
                ByteBuffer bb = ByteBuffer.wrap(blob).order(ByteOrder.LITTLE_ENDIAN);
                int count = bb.getInt();
                for (int i2 = 0; i2 < count; i2++) {
                    String strLoad$str = load$str(bb);
                    int type = bb.get() & 255;
                    switch (type) {
                        case 2:
                            objLoad$str = load$str(bb);
                            break;
                        case 3:
                            objLoad$str = Boolean.valueOf(bb.get() != 0);
                            break;
                        case 5:
                            objLoad$str = Float.valueOf(bb.getFloat());
                            break;
                        case 6:
                            objLoad$str = Double.valueOf(bb.getDouble());
                            break;
                        case 9:
                            objLoad$str = new UDim(bb.getFloat(), bb.getInt());
                            break;
                        case 10:
                            objLoad$str = new UDim2(bb.getFloat(), bb.getInt(), bb.getFloat(), bb.getInt());
                            break;
                        case 14:
                            objLoad$str = new BrickColorV(bb.getInt());
                            break;
                        case 15:
                            objLoad$str = new Color3(bb.getFloat(), bb.getFloat(), bb.getFloat());
                            break;
                        case 16:
                            objLoad$str = new Vector2(bb.getFloat(), bb.getFloat());
                            break;
                        case 17:
                            objLoad$str = new Vector3(bb.getFloat(), bb.getFloat(), bb.getFloat());
                            break;
                        case 18: {
                            float px = bb.getFloat();
                            float py = bb.getFloat();
                            float pz = bb.getFloat();
                            int orientId = bb.get() & 255;
                            if (orientId == 0) {
                                objLoad$str = new CFrame(px, py, pz, bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat());
                            } else {
                                objLoad$str = RbxBinaryKt.rotationFromId(orientId).withPosition(new Vector3(px, py, pz));
                            }
                            break;
                        }
                        case 23: {
                            load$str(bb);
                            objLoad$str = new EnumValue(bb.getInt());
                            break;
                        }
                        case 27:
                            objLoad$str = new NumberRange(bb.getFloat(), bb.getFloat());
                            break;
                        case 28:
                            objLoad$str = new Rect(bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat());
                            break;
                        case 29: {
                            int nKeys = bb.getInt();
                            if (nKeys < 0 || nKeys * 12L > bb.remaining()) {
                                throw new IllegalArgumentException("Invalid NumberSequence key count: " + nKeys);
                            }
                            ArrayList<NumberKey> keys = new ArrayList<>(nKeys);
                            for (int k = 0; k < nKeys; k++) {
                                float env = bb.getFloat();
                                float time = bb.getFloat();
                                float val = bb.getFloat();
                                keys.add(new NumberKey(time, val, env));
                            }
                            objLoad$str = new NumberSequence(keys);
                            break;
                        }
                        case 30: {
                            int cKeys = bb.getInt();
                            if (cKeys < 0 || cKeys * 20L > bb.remaining()) {
                                throw new IllegalArgumentException("Invalid ColorSequence key count: " + cKeys);
                            }
                            ArrayList<ColorKey> keys = new ArrayList<>(cKeys);
                            for (int k = 0; k < cKeys; k++) {
                                float env = bb.getFloat();
                                float time = bb.getFloat();
                                float r = bb.getFloat();
                                float g = bb.getFloat();
                                float b = bb.getFloat();
                                keys.add(new ColorKey(time, new Color3(r, g, b), env));
                            }
                            objLoad$str = new ColorSequence(keys);
                            break;
                        }
                        case 33: {
                            int weight = bb.getShort() & 0xFFFF;
                            int style = bb.get() & 255;
                            String family = load$str(bb);
                            String cachedFaceId = load$str(bb);
                            objLoad$str = new FontValue(family, weight, style, cachedFaceId);
                            break;
                        }
                        default:
                            OutputLog.INSTANCE.warn("Unsupported attribute type " + type + " on " + i.fullName() + " (" + strLoad$str + ")");
                            return;
                    }
                    if (!i.getAttributes().containsKey(strLoad$str)) {
                        i.getAttributes().put(strLoad$str, objLoad$str);
                    }
                }
            } catch (Exception e) {
                OutputLog.INSTANCE.warn("Could not read attributes of " + i.fullName());
            }
        }
    }

    private static final String load$str(ByteBuffer bb) {
        int n = bb.getInt();
        if (n < 0 || n > bb.remaining()) {
            throw new IllegalArgumentException("Invalid attribute string length: " + n);
        }
        byte[] b = new byte[n];
        bb.get(b);
        return new String(b, Charsets.UTF_8);
    }
}
