package com.goydevv.studrblx.format;

import com.goydevv.studrblx.model.Axes;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.ColorKey;
import com.goydevv.studrblx.model.ColorSequence;
import com.goydevv.studrblx.model.ContentValue;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Faces;
import com.goydevv.studrblx.model.FontValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.NumberKey;
import com.goydevv.studrblx.model.NumberRange;
import com.goydevv.studrblx.model.NumberSequence;
import com.goydevv.studrblx.model.OptionalCFrame;
import com.goydevv.studrblx.model.PhysicalProps;
import com.goydevv.studrblx.model.RawPropValue;
import com.goydevv.studrblx.model.Ray;
import com.goydevv.studrblx.model.Rect;
import com.goydevv.studrblx.model.SecurityCaps;
import com.goydevv.studrblx.model.SharedStr;
import com.goydevv.studrblx.model.UDim;
import com.goydevv.studrblx.model.UDim2;
import com.goydevv.studrblx.model.UniqueId;
import com.goydevv.studrblx.model.Vector2;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.model.Vector3int16;
import com.github.luben.zstd.Zstd;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import kotlin.Triple;
import kotlin.collections.ArraysKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: RbxBinary.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class RbxBinaryReader {
    private static final Set<String> BINARY_PROPS = SetsKt.setOf(new String[]{
            "AttributesSerialize", "PhysicsData", "PhysicalConfigData", "MeshData",
            "MeshData2", "SmoothGrid", "MaterialColors", "ModelMeshData",
            "ChildData", "PhysicsGrid"
    });
    private final HashMap<Integer, ClassInfo> classes;
    private final byte[] data;
    private final HashMap<Integer, Instance> instances;
    private final LinkedHashMap<String, String> meta;
    private final ArrayList<Triple<Instance, String, Integer>> pendingRefs;
    private final ArrayList<byte[]> sharedStrings;
    private final ArrayList<String> warnings;

    /* JADX INFO: compiled from: RbxBinary.kt */
    private static final class Chunk {
        private final byte[] data;
        private final String name;

        public Chunk(String name, byte[] data) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(data, "data");
            this.name = name;
            this.data = data;
        }

        public final byte[] getData() {
            return this.data;
        }

        public final String getName() {
            return this.name;
        }
    }

    public RbxBinaryReader(byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        this.data = data;
        this.warnings = new ArrayList<>();
        this.instances = new HashMap<>();
        this.classes = new HashMap<>();
        this.sharedStrings = new ArrayList<>();
        this.pendingRefs = new ArrayList<>();
        this.meta = new LinkedHashMap<>();
    }

    /* JADX INFO: compiled from: RbxBinary.kt */
    private static final class ClassInfo {
        private final String name;
        private final int[] refs;

        public ClassInfo(String name, int[] refs) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(refs, "refs");
            this.name = name;
            this.refs = refs;
        }

        public final String getName() {
            return this.name;
        }

        public final int[] getRefs() {
            return this.refs;
        }
    }

    public final RbxDocument read() throws RbxFormatException {
        boolean z;
        char c;
        boolean z2;
        String strTrimEnd;
        byte[] bArrDecompress;
        ByteReader byteReader = new ByteReader(this.data, 0, 0, 6, null);
        byteReader.setPos(14);
        int iU16 = byteReader.u16();
        if (iU16 != 0) {
            this.warnings.add("Unknown file version " + iU16);
        }
        byteReader.i32();
        byteReader.i32();
        byteReader.bytes(8);
        ArrayList arrayList = new ArrayList();
        do {
            z = true;
            c = 4;
            z2 = false;
            if (byteReader.remaining() < 16) {
                break;
            }
            strTrimEnd = StringsKt.trimEnd(new String(byteReader.bytes(4), Charsets.ISO_8859_1), (char) 0);
            int compLen = byteReader.i32();
            int uncompLen = byteReader.i32();
            byteReader.i32();
            if (compLen < 0 || uncompLen < 0) {
                throw new RbxFormatException("Negative chunk length in " + strTrimEnd);
            }
            if (compLen == 0) {
                bArrDecompress = byteReader.bytes(uncompLen);
            } else {
                int pos = byteReader.getPos();
                byteReader.bytes(compLen);
                bArrDecompress = decompress(this.data, pos, compLen, uncompLen);
            }
            arrayList.add(new Chunk(strTrimEnd, bArrDecompress));
        } while (!Intrinsics.areEqual(strTrimEnd, "END"));
        Iterator it = arrayList.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Object next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Chunk chunk = (Chunk) next;
            if (Intrinsics.areEqual(chunk.getName(), "META")) {
                readMeta(chunk.getData());
            }
        }
        Iterator it2 = arrayList.iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        while (it2.hasNext()) {
            Object next2 = it2.next();
            Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
            Chunk chunk2 = (Chunk) next2;
            if (Intrinsics.areEqual(chunk2.getName(), "SSTR")) {
                readSstr(chunk2.getData());
            }
        }
        Iterator it3 = arrayList.iterator();
        Intrinsics.checkNotNullExpressionValue(it3, "iterator(...)");
        while (it3.hasNext()) {
            Object next3 = it3.next();
            Intrinsics.checkNotNullExpressionValue(next3, "next(...)");
            Chunk chunk3 = (Chunk) next3;
            if (Intrinsics.areEqual(chunk3.getName(), "INST")) {
                readInst(chunk3.getData());
            }
        }
        Iterator it4 = arrayList.iterator();
        Intrinsics.checkNotNullExpressionValue(it4, "iterator(...)");
        while (it4.hasNext()) {
            Object next4 = it4.next();
            Intrinsics.checkNotNullExpressionValue(next4, "next(...)");
            Chunk chunk4 = (Chunk) next4;
            if (Intrinsics.areEqual(chunk4.getName(), "PROP")) {
                try {
                    readProp(chunk4.getData());
                } catch (Exception e) {
                    this.warnings.add("Skipped a property: " + e.getMessage());
                }
            }
        }
        boolean z3 = z;
        char c2 = c;
        boolean z4 = z2;
        Iterator<Triple<Instance, String, Integer>> it5 = this.pendingRefs.iterator();
        Intrinsics.checkNotNullExpressionValue(it5, "iterator(...)");
        while (it5.hasNext()) {
            Triple<Instance, String, Integer> next5 = it5.next();
            Intrinsics.checkNotNullExpressionValue(next5, "next(...)");
            Triple<Instance, String, Integer> triple = next5;
            Instance instanceComponent1 = triple.component1();
            String strComponent2 = triple.component2();
            int iIntValue = triple.component3().intValue();
            Instance resolved = iIntValue < 0 ? null : this.instances.get(Integer.valueOf(iIntValue));
            if (iIntValue >= 0 && resolved == null) {
                this.warnings.add("Unresolved binary ref " + iIntValue + " for " + instanceComponent1.getClassName() + "." + strComponent2);
            }
            instanceComponent1.getProps().put(strComponent2, resolved);
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator it6 = arrayList.iterator();
        Intrinsics.checkNotNullExpressionValue(it6, "iterator(...)");
        while (it6.hasNext()) {
            Object next6 = it6.next();
            Intrinsics.checkNotNullExpressionValue(next6, "next(...)");
            Chunk chunk5 = (Chunk) next6;
            if (Intrinsics.areEqual(chunk5.getName(), "PRNT")) {
                readPrnt(chunk5.getData(), arrayList2);
            }
        }
        Iterator it7 = arrayList.iterator();
        Intrinsics.checkNotNullExpressionValue(it7, "iterator(...)");
        while (it7.hasNext()) {
            Object next7 = it7.next();
            Intrinsics.checkNotNullExpressionValue(next7, "next(...)");
            Chunk chunk6 = (Chunk) next7;
            String[] strArr = new String[7];
            strArr[z4 ? 1 : 0] = "META";
            strArr[z3 ? 1 : 0] = "SSTR";
            strArr[2] = "INST";
            strArr[3] = "PROP";
            strArr[c2] = "PRNT";
            strArr[5] = "END";
            strArr[6] = "SIGN";
            if (!SetsKt.setOf(strArr).contains(chunk6.getName())) {
                this.warnings.add("Ignored unknown chunk " + chunk6.getName());
            }
        }
        return new RbxDocument(arrayList2, this.meta, this.warnings);
    }

    private final byte[] decompress(byte[] src, int off, int len, int out) throws RbxFormatException {
        if (off < 0 || len < 0 || out < 0 || off + len > src.length) {
            throw new RbxFormatException("Invalid compressed chunk bounds");
        }
        boolean isZstd = len >= 4 && src[off] == 40 && src[off + 1] == -75 && src[off + 2] == 47 && src[off + 3] == -3;
        if (isZstd) {
            try {
                byte[] bArrDecompress = Zstd.decompress(ArraysKt.copyOfRange(src, off, off + len), out);
                Intrinsics.checkNotNull(bArrDecompress);
                return bArrDecompress;
            } catch (Throwable e) {
                throw new RbxFormatException("Could not decompress ZSTD chunk: " + e.getMessage());
            }
        }
        return Lz4.INSTANCE.decompress(src, off, len, out);
    }

    private final void readMeta(byte[] d) throws RbxFormatException {
        ByteReader r = new ByteReader(d, 0, 0, 6, null);
        int n = r.i32();
        for (int i = 0; i < n; i++) {
            this.meta.put(r.string(), r.string());
        }
    }

    private final void readSstr(byte[] d) throws RbxFormatException {
        ByteReader r = new ByteReader(d, 0, 0, 6, null);
        r.i32();
        int n = r.i32();
        for (int i = 0; i < n; i++) {
            r.bytes(16);
            this.sharedStrings.add(r.stringBytes());
        }
    }

    private final void readInst(byte[] d) throws RbxFormatException {
        ByteReader r = new ByteReader(d, 0, 0, 6, null);
        int classId = r.i32();
        String cls = r.string();
        int format = r.u8();
        int n = r.i32();
        int[] refs = r.refArray(n);
        byte[] markers = format == 1 ? r.bytes(n) : null;
        this.classes.put(Integer.valueOf(classId), new ClassInfo(cls, refs));
        for (int i = 0; i < refs.length; i++) {
            int ref = refs[i];
            Instance inst = new Instance(cls);
            inst.setService(markers != null && markers[i] != 0);
            inst.setLoadedRef(ref);
            this.instances.put(Integer.valueOf(ref), inst);
        }
    }

    private final void readPrnt(byte[] d, List<Instance> roots) throws RbxFormatException {
        ByteReader r = new ByteReader(d, 0, 0, 6, null);
        r.u8();
        int n = r.i32();
        int[] kids = r.refArray(n);
        int[] parents = r.refArray(n);
        for (int i = 0; i < n; i++) {
            Instance c = this.instances.get(Integer.valueOf(kids[i]));
            if (c != null) {
                Instance p = parents[i] < 0 ? null : this.instances.get(Integer.valueOf(parents[i]));
                if (p == null) {
                    roots.add(c);
                } else {
                    c.attachRaw(p);
                }
            }
        }
    }

    private final void readProp(byte[] d) throws RbxFormatException {
        RbxBinaryReader rbxBinaryReader = this;
        ByteReader r = new ByteReader(d, 0, 0, 6, null);
        int classId = r.i32();
        String name = r.string();
        if (r.remaining() == 0) {
            return;
        }
        int type = r.u8();
        ClassInfo ci = rbxBinaryReader.classes.get(Integer.valueOf(classId));
        if (ci == null) {
            return;
        }
        int[] refs = ci.getRefs();
        Collection arrayList = new ArrayList(refs.length);
        int length = refs.length;
        int i = 0;
        while (i < length) {
            int[] iArr = refs;
            Instance instance = rbxBinaryReader.instances.get(Integer.valueOf(refs[i]));
            Intrinsics.checkNotNull(instance);
            arrayList.add(instance);
            i++;
            rbxBinaryReader = this;
            refs = iArr;
        }
        List insts = (List) arrayList;
        int n = insts.size();
        int payloadStart = r.getPos();
        try {
            List<Object> values = readValues(r, type, n, insts, name);
            for (int i2 = 0; i2 < n; i2++) {
                if (type != 19) {
                    ((Instance) insts.get(i2)).getProps().put(name, values.get(i2));
                }
                ((Instance) insts.get(i2)).getPropTypes().put(name, Integer.valueOf(type));
            }
        } catch (Exception e) {
            byte[] payload = ArraysKt.copyOfRange(d, payloadStart, d.length);
            this.warnings.add("Preserved " + ci.getName() + "." + name + " (unsupported type 0x" + Integer.toHexString(type) + ")");
            for (int i3 = 0; i3 < n; i3++) {
                ((Instance) insts.get(i3)).getProps().put(name, new RawPropValue(type, payload, i3, n));
                ((Instance) insts.get(i3)).getPropTypes().put(name, Integer.valueOf(type));
            }
        }
    }

    private final List<CFrame> readCFrames(ByteReader r, int n) throws RbxFormatException {
        ArrayList rots = new ArrayList(n);
        for (int i = 0; i < n; i++) {
            int iU8 = r.u8();
            if (iU8 != 0) {
                rots.add(RbxBinaryKt.rotationFromId(iU8));
            } else {
                float[] fArr = new float[9];
                for (int i2 = 0; i2 < 9; i2++) {
                    fArr[i2] = r.f32();
                }
                rots.add(new CFrame(0.0f, 0.0f, 0.0f, fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5], fArr[6], fArr[7], fArr[8]));
            }
        }
        float[] xs = r.f32Array(n);
        float[] ys = r.f32Array(n);
        float[] zs = r.f32Array(n);
        ArrayList arrayList = new ArrayList(n);
        for (int i3 = 0; i3 < n; i3++) {
            int i4 = i3;
            arrayList.add(((CFrame) rots.get(i4)).withPosition(new Vector3(xs[i4], ys[i4], zs[i4])));
        }
        return arrayList;
    }

    private final List<Object> readValues(ByteReader r, int type, int n, List<? extends Instance> insts, String name) throws RbxFormatException {
        PhysicalProps physicalProps;
        int oi;
        ContentValue contentValue;
        switch (type) {
            case 1:
                boolean forceBinary = BINARY_PROPS.contains(name);
                ArrayList arrayList = new ArrayList(n);
                for (int i = 0; i < n; i++) {
                    byte[] raw = r.stringBytes();
                    arrayList.add(forceBinary ? new BinaryString(raw) : RbxBinaryKt.bytesToStringValue(raw));
                }
                return arrayList;
            case 2:
                ArrayList arrayList2 = new ArrayList(n);
                for (int i2 = 0; i2 < n; i2++) {
                    arrayList2.add(Boolean.valueOf(r.u8() != 0));
                }
                return arrayList2;
            case 3:
                return (List<Object>) (List<?>) ArraysKt.toList(r.i32Array(n));
            case 4:
                return (List<Object>) (List<?>) ArraysKt.toList(r.f32Array(n));
            case 5:
                ArrayList arrayList3 = new ArrayList(n);
                for (int i3 = 0; i3 < n; i3++) {
                    arrayList3.add(Double.valueOf(r.f64()));
                }
                return arrayList3;
            case 6:
                float[] s = r.f32Array(n);
                int[] o = r.i32Array(n);
                ArrayList arrayList4 = new ArrayList(n);
                for (int i4 = 0; i4 < n; i4++) {
                    int i5 = i4;
                    arrayList4.add(new UDim(s[i5], o[i5]));
                }
                return arrayList4;
            case 7:
                float[] xs = r.f32Array(n);
                float[] ys = r.f32Array(n);
                int[] xo = r.i32Array(n);
                int[] yo = r.i32Array(n);
                ArrayList arrayList5 = new ArrayList(n);
                int i6 = 0;
                while (i6 < n) {
                    int i7 = i6;
                    arrayList5.add(new UDim2(xs[i7], xo[i7], ys[i7], yo[i7]));
                    i6++;
                }
                return arrayList5;
            case 8:
                ArrayList arrayList6 = new ArrayList(n);
                for (int i8 = 0; i8 < n; i8++) {
                    arrayList6.add(new Ray(new Vector3(r.f32(), r.f32(), r.f32()), new Vector3(r.f32(), r.f32(), r.f32())));
                }
                return arrayList6;
            case 9:
                ArrayList arrayList7 = new ArrayList(n);
                for (int i9 = 0; i9 < n; i9++) {
                    arrayList7.add(new Faces(r.u8()));
                }
                return arrayList7;
            case 10:
                ArrayList arrayList8 = new ArrayList(n);
                for (int i10 = 0; i10 < n; i10++) {
                    arrayList8.add(new Axes(r.u8()));
                }
                return arrayList8;
            case 11:
                int[] iArrU32Array = r.u32Array(n);
                Collection arrayList9 = new ArrayList(iArrU32Array.length);
                for (int i11 : iArrU32Array) {
                    arrayList9.add(new BrickColorV(i11));
                }
                return (List) arrayList9;
            case 12:
                float[] a = r.f32Array(n);
                float[] b = r.f32Array(n);
                float[] c = r.f32Array(n);
                ArrayList arrayList10 = new ArrayList(n);
                for (int i12 = 0; i12 < n; i12++) {
                    int i13 = i12;
                    arrayList10.add(new Color3(a[i13], b[i13], c[i13]));
                }
                return arrayList10;
            case 13:
                float[] a2 = r.f32Array(n);
                float[] b2 = r.f32Array(n);
                ArrayList arrayList11 = new ArrayList(n);
                for (int i14 = 0; i14 < n; i14++) {
                    int i15 = i14;
                    arrayList11.add(new Vector2(a2[i15], b2[i15]));
                }
                return arrayList11;
            case 14:
                float[] a3 = r.f32Array(n);
                float[] b3 = r.f32Array(n);
                float[] c2 = r.f32Array(n);
                ArrayList arrayList12 = new ArrayList(n);
                for (int i16 = 0; i16 < n; i16++) {
                    int i17 = i16;
                    arrayList12.add(new Vector3(a3[i17], b3[i17], c2[i17]));
                }
                return arrayList12;
            case 15:
            case 17:
            case 29:
            default:
                throw new RbxFormatException("unsupported type 0x" + Integer.toHexString(type) + " for '" + name + "'");
            case 16:
                return (List<Object>) (List<?>) readCFrames(r, n);
            case 18:
                int[] iArrU32Array2 = r.u32Array(n);
                Collection arrayList13 = new ArrayList(iArrU32Array2.length);
                for (int i18 : iArrU32Array2) {
                    arrayList13.add(new EnumValue(i18));
                }
                return (List) arrayList13;
            case 19:
                int[] refs = r.refArray(n);
                for (int i19 = 0; i19 < n; i19++) {
                    this.pendingRefs.add(new Triple<>(insts.get(i19), name, Integer.valueOf(refs[i19])));
                }
                ArrayList arrayList14 = new ArrayList(n);
                for (int i20 = 0; i20 < n; i20++) {
                    arrayList14.add(null);
                }
                return arrayList14;
            case 20:
                ArrayList arrayList15 = new ArrayList(n);
                for (int i21 = 0; i21 < n; i21++) {
                    arrayList15.add(new Vector3int16(readValues$lambda$19$s16(r), readValues$lambda$19$s16(r), readValues$lambda$19$s16(r)));
                }
                return arrayList15;
            case 21:
                ArrayList arrayList16 = new ArrayList(n);
                int i22 = 0;
                while (i22 < n) {
                    int iI32 = r.i32();
                    ArrayList arrayList17 = new ArrayList(iI32);
                    int i23 = 0;
                    while (i23 < iI32) {
                        arrayList17.add(new NumberKey(r.f32(), r.f32(), r.f32()));
                        i23++;
                    }
                    arrayList16.add(new NumberSequence(arrayList17));
                    i22++;
                }
                return arrayList16;
            case 22:
                int i24 = 0;
                ArrayList arrayList18 = new ArrayList(n);
                int i25 = 0;
                while (i25 < n) {
                    int i26 = i25;
                    int iI33 = r.i32();
                    ArrayList arrayList19 = new ArrayList(iI33);
                    int i27 = i24;
                    while (i27 < iI33) {
                        arrayList19.add(new ColorKey(r.f32(), new Color3(r.f32(), r.f32(), r.f32()), r.f32()));
                        i27++;
                    }
                    arrayList18.add(new ColorSequence(arrayList19));
                    i25++;
                    i24 = 0;
                }
                return arrayList18;
            case 23:
                ArrayList arrayList20 = new ArrayList(n);
                for (int i28 = 0; i28 < n; i28++) {
                    arrayList20.add(new NumberRange(r.f32(), r.f32()));
                }
                return arrayList20;
            case 24:
                float[] a4 = r.f32Array(n);
                float[] b4 = r.f32Array(n);
                float[] c3 = r.f32Array(n);
                float[] e = r.f32Array(n);
                ArrayList arrayList21 = new ArrayList(n);
                for (int i29 = 0; i29 < n; i29++) {
                    int i30 = i29;
                    arrayList21.add(new Rect(a4[i30], b4[i30], c3[i30], e[i30]));
                }
                return arrayList21;
            case 25:
                ArrayList arrayList22 = new ArrayList(n);
                for (int i31 = 0; i31 < n; i31++) {
                    int iU8 = r.u8();
                    if ((iU8 & 1) != 0) {
                        physicalProps = new PhysicalProps(iU8, r.f32(), r.f32(), r.f32(), r.f32(), r.f32(), 0.0f, 64, null);
                        if ((iU8 & 2) != 0) {
                            physicalProps = PhysicalProps.copy$default(physicalProps, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, r.f32(), 63, null);
                        }
                    } else {
                        physicalProps = new PhysicalProps(iU8, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 126, null);
                    }
                    arrayList22.add(physicalProps);
                }
                return arrayList22;
            case 26:
                byte[] a5 = r.bytes(n);
                byte[] b5 = r.bytes(n);
                byte[] c4 = r.bytes(n);
                ArrayList arrayList23 = new ArrayList(n);
                for (int i32 = 0; i32 < n; i32++) {
                    int i33 = i32;
                    arrayList23.add(Color3.INSTANCE.fromRGB(a5[i33] & 255, b5[i33] & 255, c4[i33] & 255));
                }
                return arrayList23;
            case 27:
                return (List<Object>) (List<?>) ArraysKt.toList(r.i64Array(n));
            case 28:
                int[] iArrU32Array3 = r.u32Array(n);
                Collection arrayList24 = new ArrayList(iArrU32Array3.length);
                int length = iArrU32Array3.length;
                int i34 = 0;
                while (i34 < length) {
                    int i35 = iArrU32Array3[i34];
                    ArrayList<byte[]> arrayList25 = this.sharedStrings;
                    arrayList24.add(new SharedStr(i35 >= 0 && i35 < arrayList25.size() ? arrayList25.get(i35) : new byte[0]));
                    i34++;
                }
                return (List) arrayList24;
            case 30:
                r.u8();
                List<CFrame> cFrames = readCFrames(r, n);
                r.u8();
                ArrayList arrayList26 = new ArrayList(n);
                for (int i36 = 0; i36 < n; i36++) {
                    arrayList26.add(Boolean.valueOf(r.u8() != 0));
                }
                ArrayList has = arrayList26;
                ArrayList arrayList27 = new ArrayList(n);
                for (int i37 = 0; i37 < n; i37++) {
                    int i38 = i37;
                    arrayList27.add(new OptionalCFrame(((Boolean) has.get(i38)).booleanValue() ? cFrames.get(i38) : null));
                }
                return arrayList27;
            case 31:
                byte[] d = r.interleaved(n, 16);
                ArrayList arrayList28 = new ArrayList(n);
                for (int i39 = 0; i39 < n; i39++) {
                    int i40 = i39;
                    arrayList28.add(new UniqueId(ArraysKt.copyOfRange(d, i40 * 16, (i40 * 16) + 16)));
                }
                return arrayList28;
            case 32:
                ArrayList arrayList29 = new ArrayList(n);
                for (int i41 = 0; i41 < n; i41++) {
                    arrayList29.add(new FontValue(r.string(), r.u16(), r.u8(), r.string()));
                }
                return arrayList29;
            case 33:
                int i42 = 8;
                byte[] d2 = r.interleaved(n, 8);
                ArrayList arrayList30 = new ArrayList(n);
                int i43 = 0;
                while (i43 < n) {
                    int i44 = i43;
                    long j = 0;
                    int i45 = 0;
                    while (i45 < i42) {
                        j = (j << i42) | (((long) d2[(i44 * 8) + i45]) & 255);
                        i45++;
                        i42 = 8;
                    }
                    arrayList30.add(new SecurityCaps(j));
                    i43++;
                    i42 = 8;
                }
                return arrayList30;
            case 34:
                int[] types = r.i32Array(n);
                int iI34 = r.i32();
                ArrayList arrayList31 = new ArrayList(iI34);
                for (int i46 = 0; i46 < iI34; i46++) {
                    arrayList31.add(r.string());
                }
                ArrayList uris = arrayList31;
                int objCount = r.i32();
                int[] objs = r.refArray(objCount);
                int ext = r.i32();
                if (ext != 0) {
                    r.refArray(ext);
                }
                int ui = 0;
                int oi2 = 0;
                ArrayList arrayList32 = new ArrayList(n);
                int i47 = 0;
                while (i47 < n) {
                    Object obj = "";
                    switch (types[i47]) {
                        case 1:
                            int ui2 = ui + 1;
                            if (ui >= 0 && ui < uris.size()) {
                                obj = uris.get(ui);
                            }
                            ui = ui2;
                            oi = oi2;
                            contentValue = new ContentValue(1, (String) obj, null, 4, null);
                            break;
                        case 2:
                            oi = oi2 + 1;
                            int i48 = oi2 >= 0 && oi2 < objs.length ? objs[oi2] : -1;
                            Instance refInst = i48 < 0 ? null : this.instances.get(Integer.valueOf(i48));
                            if (i48 >= 0 && refInst == null) {
                                this.warnings.add("Unresolved binary Content ref " + i48 + " for " + insts.get(i47).getClassName() + "." + name);
                            }
                            contentValue = new ContentValue(2, "", refInst);
                            break;
                        default:
                            oi = oi2;
                            contentValue = new ContentValue(0, null, null, 6, null);
                            break;
                    }
                    arrayList32.add(contentValue);
                    i47++;
                    oi2 = oi;
                }
                return arrayList32;
        }
    }

    private static final int readValues$lambda$19$s16(ByteReader $r) {
        try {
            int v = $r.u16();
            return v >= 32768 ? v - 65536 : v;
        } catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }
}
