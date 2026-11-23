package com.goydevv.studrblx.format;

import android.util.Base64;
import android.util.Xml;
import com.goydevv.studrblx.model.Axes;
import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.BrickColorV;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.ClassDb;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kotlin.Pair;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: compiled from: RbxXmlReader.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class RbxXmlReader {
    public static final RbxXmlReader INSTANCE = new RbxXmlReader();

    /* JADX INFO: compiled from: RbxXmlReader.kt */
    private static final class XNode {
        private final Map<String, String> attrs;
        private final ArrayList<XNode> children;
        private final String name;
        private final StringBuilder text;

        public XNode(String name, Map<String, String> attrs) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(attrs, "attrs");
            this.name = name;
            this.attrs = attrs;
            this.children = new ArrayList<>();
            this.text = new StringBuilder();
        }

        public final Map<String, String> getAttrs() {
            return this.attrs;
        }

        public final String getName() {
            return this.name;
        }

        public final ArrayList<XNode> getChildren() {
            return this.children;
        }

        public final StringBuilder getText() {
            return this.text;
        }

        public final XNode child(String n) {
            Intrinsics.checkNotNullParameter(n, "n");
            for (XNode next : this.children) {
                if (StringsKt.equals(next.name, n, true)) {
                    return next;
                }
            }
            return null;
        }

        public final String t(String n) {
            StringBuilder sb;
            String string;
            String string2;
            Intrinsics.checkNotNullParameter(n, "n");
            XNode xNodeChild = child(n);
            return (xNodeChild == null || (sb = xNodeChild.text) == null || (string = sb.toString()) == null || (string2 = StringsKt.trim((CharSequence) string).toString()) == null) ? "" : string2;
        }

        public final float f(String n) {
            Intrinsics.checkNotNullParameter(n, "n");
            return RbxXmlReader.INSTANCE.parseSpecial(t(n));
        }
    }

    private RbxXmlReader() {
    }

    public final RbxDocument read(byte[] bytes) throws XmlPullParserException, RbxFormatException, IOException {
        Object next;
        Iterable children;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        int offset = (bytes.length >= 3 && (bytes[0] & 0xFF) == 0xEF && (bytes[1] & 0xFF) == 0xBB && (bytes[2] & 0xFF) == 0xBF) ? 3 : 0;
        XmlPullParser p = Xml.newPullParser();
        p.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
        p.setInput(new ByteArrayInputStream(bytes, offset, bytes.length - offset), "UTF-8");
        Intrinsics.checkNotNull(p);
        XNode root = parseTree(p);
        if (root == null) {
            throw new RbxFormatException("Empty XML");
        }
        ArrayList<String> warnings = new ArrayList<>();
        HashMap<String, Instance> refs = new HashMap<>();
        ArrayList<Triple<Instance, String, String>> pending = new ArrayList<>();
        ArrayList<Triple<Instance, String, String>> pendingContentRefs = new ArrayList<>();
        HashMap<String, byte[]> shared = new HashMap<>();
        ArrayList<Triple<Instance, String, String>> pendingShared = new ArrayList<>();
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        Iterator it = root.getChildren().iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!Intrinsics.areEqual(((XNode) next).getName(), "SharedStrings"));
        XNode xNode = (XNode) next;
        if (xNode != null && (children = xNode.getChildren()) != null) {
            Iterable<XNode> iterable = children;
            for (XNode xNode2 : iterable) {
                String str3 = xNode2.getAttrs().get("md5");
                if (str3 == null) {
                    str3 = "";
                }
                String string = xNode2.getText().toString();
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                shared.put(str3, INSTANCE.decodeB64(string));
            }
        }
        ArrayList<Instance> roots = new ArrayList<>();
        for (XNode c : root.getChildren()) {
            String name = c.getName();
            if (Intrinsics.areEqual(name, "Item")) {
                read$build(roots, refs, pending, pendingContentRefs, pendingShared, warnings, c, null);
            } else if (Intrinsics.areEqual(name, "Meta")) {
                String str5 = c.getAttrs().get("name");
                if (str5 == null) {
                    str5 = "";
                }
                meta.put(str5, c.getText().toString());
            }
        }
        for (Triple<Instance, String, String> triple : pending) {
            Instance i = triple.component1();
            String n = triple.component2();
            String r = triple.component3();
            Instance instance;
            if (Intrinsics.areEqual(r, "null") || r.length() == 0) {
                instance = null;
            } else {
                instance = refs.get(r);
                if (instance == null) {
                    warnings.add("Unresolved XML ref " + r + " for " + i.getClassName() + "." + n);
                }
            }
            i.getProps().put(n, instance);
        }
        for (Triple<Instance, String, String> triple : pendingContentRefs) {
            Instance i = triple.component1();
            String n = triple.component2();
            String r = triple.component3();
            Instance instance;
            if (Intrinsics.areEqual(r, "null") || r.length() == 0) {
                instance = null;
            } else {
                instance = refs.get(r);
                if (instance == null) {
                    warnings.add("Unresolved XML Content ref " + r + " for " + i.getClassName() + "." + n);
                }
            }
            i.getProps().put(n, new ContentValue(2, "", instance));
        }
        for (Triple<Instance, String, String> triple2 : pendingShared) {
            Instance i2 = triple2.component1();
            String n2 = triple2.component2();
            String k = triple2.component3();
            byte[] bArr = shared.get(k);
            if (bArr == null) {
                bArr = new byte[0];
            }
            i2.getProps().put(n2, new SharedStr(bArr));
        }
        return new RbxDocument(roots, meta, warnings);
    }

    private static final void read$build(
            ArrayList<Instance> roots,
            HashMap<String, Instance> refs,
            ArrayList<Triple<Instance, String, String>> pending,
            ArrayList<Triple<Instance, String, String>> pendingContentRefs,
            ArrayList<Triple<Instance, String, String>> pendingShared,
            ArrayList<String> warnings,
            XNode node,
            Instance parent) {
        String cls = node.getAttrs().get("class");
        if (cls == null) {
            cls = "Folder";
        }
        Instance inst = new Instance(cls);
        if (ClassDb.INSTANCE.isServiceName(inst.getClassName()) && parent == null) {
            inst.setService(true);
        }
        String referent = node.getAttrs().get("referent");
        if (referent != null) {
            refs.put(referent, inst);
        }
        XNode propsNode = node.child("Properties");
        if (propsNode != null) {
            for (XNode xNode : propsNode.getChildren()) {
                String propName = xNode.getAttrs().get("name");
                if (propName == null) {
                    continue;
                }
                try {
                    String tagName = xNode.getName();
                    if (Intrinsics.areEqual(tagName, "Ref")) {
                        String refText = xNode.getText().toString();
                        Intrinsics.checkNotNullExpressionValue(refText, "toString(...)");
                        pending.add(new Triple<>(inst, propName, StringsKt.trim((CharSequence) refText).toString()));
                        inst.getPropTypes().put(propName, 19);
                    } else if (Intrinsics.areEqual(tagName, "SharedString")) {
                        String sharedText = xNode.getText().toString();
                        Intrinsics.checkNotNullExpressionValue(sharedText, "toString(...)");
                        pendingShared.add(new Triple<>(inst, propName, StringsKt.trim((CharSequence) sharedText).toString()));
                        inst.getPropTypes().put(propName, 28);
                    } else {
                        XNode contentObjNode = null;
                        if (Intrinsics.areEqual(tagName, "Content")) {
                            contentObjNode = xNode.child("Object");
                            if (contentObjNode == null) {
                                contentObjNode = xNode.child("Ref");
                            }
                        }
                        if (contentObjNode != null) {
                            String objRef = contentObjNode.getText().toString();
                            Intrinsics.checkNotNullExpressionValue(objRef, "toString(...)");
                            pendingContentRefs.add(new Triple<>(inst, propName, StringsKt.trim((CharSequence) objRef).toString()));
                            inst.getPropTypes().put(propName, 34);
                        } else {
                            Pair<Integer, Object> pairConvert = INSTANCE.convert(xNode);
                            int typeId = pairConvert.component1().intValue();
                            Object val = pairConvert.component2();
                            if (typeId == -1) {
                                warnings.add("Unknown XML property type " + tagName + " (" + propName + ")");
                            } else {
                                inst.getProps().put(propName, val);
                                inst.getPropTypes().put(propName, Integer.valueOf(typeId));
                            }
                        }
                    }
                } catch (Exception e) {
                    warnings.add("Bad value for " + propName + ": " + e.getMessage());
                }
            }
        }
        if (parent == null) {
            roots.add(inst);
        } else {
            inst.attachRaw(parent);
        }
        for (XNode childNode : node.getChildren()) {
            if (Intrinsics.areEqual(childNode.getName(), "Item")) {
                read$build(roots, refs, pending, pendingContentRefs, pendingShared, warnings, childNode, inst);
            }
        }
    }

    private final byte[] decodeB64(String s) {
        try {
            byte[] bArrDecode = Base64.decode(new Regex("\\s").replace(s, ""), 0);
            Intrinsics.checkNotNull(bArrDecode);
            return bArrDecode;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private final byte[] decodeHex16(String hex) {
        byte[] out = new byte[16];
        if (hex == null) {
            return out;
        }
        StringBuilder clean = new StringBuilder(32);
        for (int i = 0; i < hex.length(); i++) {
            char ch = hex.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
                clean.append(ch);
            }
        }
        int pairs = Math.min(16, clean.length() / 2);
        for (int i = 0; i < pairs; i++) {
            int hi = Character.digit(clean.charAt(i * 2), 16);
            int lo = Character.digit(clean.charAt((i * 2) + 1), 16);
            if (hi >= 0 && lo >= 0) {
                out[i] = (byte) ((hi << 4) | lo);
            }
        }
        return out;
    }

    private final XNode parseTree(XmlPullParser p) throws XmlPullParserException, IOException {
        ArrayList stack = new ArrayList();
        XNode root = null;
        int ev = p.getEventType();
        while (ev != 1) {
            switch (ev) {
                case 2:
                    HashMap attrs = new HashMap();
                    int attributeCount = p.getAttributeCount();
                    for (int i = 0; i < attributeCount; i++) {
                        attrs.put(p.getAttributeName(i), p.getAttributeValue(i));
                    }
                    String name = p.getName();
                    Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                    XNode n = new XNode(name, attrs);
                    if (stack.isEmpty()) {
                        root = n;
                    } else {
                        ((XNode) CollectionsKt.last((List) stack)).getChildren().add(n);
                    }
                    stack.add(n);
                    break;
                case 3:
                    if (!stack.isEmpty()) {
                        stack.remove(stack.size() - 1);
                    }
                    break;
                case 4:
                case 5:
                    if (!stack.isEmpty()) {
                        ((XNode) CollectionsKt.last((List) stack)).getText().append(p.getText());
                    }
                    break;
            }
            ev = p.nextToken();
        }
        return root;
    }

    private final CFrame cframe(XNode n) {
        return new CFrame(n.f("X"), n.f("Y"), n.f("Z"), n.f("R00"), n.f("R01"), n.f("R02"), n.f("R10"), n.f("R11"), n.f("R12"), n.f("R20"), n.f("R21"), n.f("R22"));
    }

    private final Pair<Integer, Object> content(XNode n) {
        XNode uriNode = n.child("uri");
        if (uriNode != null) {
            String uri = uriNode.getText().toString().trim();
            return TuplesKt.to(34, new ContentValue(1, uri, null));
        }
        XNode binNode = n.child("binary");
        if (binNode != null) {
            return TuplesKt.to(1, new BinaryString(decodeB64(binNode.getText().toString().trim())));
        }
        XNode urlNode = n.child("url");
        if (urlNode != null) {
            return TuplesKt.to(1, urlNode.getText().toString().trim());
        }
        if (n.child("null") != null) {
            return TuplesKt.to(1, "");
        }
        String raw = n.getText().toString().trim();
        return TuplesKt.to(1, raw);
    }

    private final String contentText(XNode n) {
        if (n == null) {
            return "";
        }
        XNode uriNode = n.child("uri");
        if (uriNode != null) {
            return uriNode.getText().toString().trim();
        }
        XNode urlNode = n.child("url");
        if (urlNode != null) {
            return urlNode.getText().toString().trim();
        }
        if (n.child("null") != null) {
            return "";
        }
        return n.getText().toString().trim();
    }

    private final Pair<Integer, Object> convert(XNode n) {
        if (n == null) {
            return TuplesKt.to(-1, null);
        }
        String tag = n.getName();
        String rawTxt = n.getText() != null ? n.getText().toString() : "";
        String txt = rawTxt.trim();
        switch (tag) {
            case "string":
            case "ProtectedString":
                return TuplesKt.to(1, rawTxt);
            case "BinaryString":
                return TuplesKt.to(1, new BinaryString(decodeB64(txt)));
            case "SharedString":
                return TuplesKt.to(28, new SharedStr(decodeB64(txt)));
            case "Content":
            case "ContentId":
                return content(n);
            case "bool":
                return TuplesKt.to(2, Boolean.valueOf("true".equalsIgnoreCase(txt) || "1".equals(txt)));
            case "int":
                try {
                    return TuplesKt.to(3, Integer.valueOf(Integer.parseInt(txt)));
                } catch (Exception e) {
                    return TuplesKt.to(3, 0);
                }
            case "float":
                return TuplesKt.to(4, Float.valueOf(parseSpecial(txt)));
            case "double":
                try {
                    return TuplesKt.to(5, Double.valueOf(Double.parseDouble(txt)));
                } catch (Exception e) {
                    return TuplesKt.to(5, Double.valueOf(parseSpecial(txt)));
                }
            case "UDim": {
                float sc = 0.0f;
                int off = 0;
                for (XNode c : n.getChildren()) {
                    String ct = c.getText() != null ? c.getText().toString().trim() : "";
                    if ("S".equals(c.getName())) {
                        sc = parseSpecial(ct);
                    } else if ("O".equals(c.getName())) {
                        try {
                            off = Integer.parseInt(ct);
                        } catch (Exception ignored) {
                        }
                    }
                }
                return TuplesKt.to(6, new UDim(sc, off));
            }
            case "UDim2": {
                float xs = 0.0f;
                float ys = 0.0f;
                int xo = 0;
                int yo = 0;
                for (XNode c : n.getChildren()) {
                    String ct = c.getText() != null ? c.getText().toString().trim() : "";
                    if ("XS".equals(c.getName())) {
                        xs = parseSpecial(ct);
                    } else if ("YS".equals(c.getName())) {
                        ys = parseSpecial(ct);
                    } else if ("XO".equals(c.getName())) {
                        try {
                            xo = Integer.parseInt(ct);
                        } catch (Exception ignored) {
                        }
                    } else if ("YO".equals(c.getName())) {
                        try {
                            yo = Integer.parseInt(ct);
                        } catch (Exception ignored) {
                        }
                    }
                }
                return TuplesKt.to(7, new UDim2(xs, xo, ys, yo));
            }
            case "Ray": {
                XNode orig = n.child("origin");
                XNode dir = n.child("direction");
                Vector3 o = orig != null ? new Vector3(orig.f("X"), orig.f("Y"), orig.f("Z")) : Vector3.INSTANCE.getZERO();
                Vector3 d = dir != null ? new Vector3(dir.f("X"), dir.f("Y"), dir.f("Z")) : Vector3.INSTANCE.getZERO();
                return TuplesKt.to(8, new Ray(o, d));
            }
            case "Faces": {
                int mask = 0;
                for (XNode c : n.getChildren()) {
                    if ("faces".equals(c.getName())) {
                        String ct = c.getText() != null ? c.getText().toString().trim() : "";
                        try {
                            mask = Integer.parseInt(ct);
                        } catch (Exception ignored) {
                        }
                    }
                }
                return TuplesKt.to(9, new Faces(mask));
            }
            case "Axes": {
                int mask = 0;
                for (XNode c : n.getChildren()) {
                    if ("axes".equals(c.getName())) {
                        String ct = c.getText() != null ? c.getText().toString().trim() : "";
                        try {
                            mask = Integer.parseInt(ct);
                        } catch (Exception ignored) {
                        }
                    }
                }
                return TuplesKt.to(10, new Axes(mask));
            }
            case "BrickColor": {
                int bc = 194;
                try {
                    bc = Integer.parseInt(txt);
                } catch (Exception ignored) {
                }
                return TuplesKt.to(11, new BrickColorV(bc));
            }
            case "Color3": {
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                for (XNode c : n.getChildren()) {
                    String ct = c.getText() != null ? c.getText().toString().trim() : "";
                    if ("R".equals(c.getName())) {
                        r = parseSpecial(ct);
                    } else if ("G".equals(c.getName())) {
                        g = parseSpecial(ct);
                    } else if ("B".equals(c.getName())) {
                        b = parseSpecial(ct);
                    }
                }
                return TuplesKt.to(12, new Color3(r, g, b));
            }
            case "Color3uint8": {
                try {
                    long packed = Long.parseLong(txt);
                    float r = ((packed >> 16) & 255) / 255.0f;
                    float g = ((packed >> 8) & 255) / 255.0f;
                    float b = (packed & 255) / 255.0f;
                    return TuplesKt.to(26, new Color3(r, g, b));
                } catch (Exception e) {
                    return TuplesKt.to(26, new Color3(0.5f, 0.5f, 0.5f));
                }
            }
            case "Vector2": {
                float x = 0.0f;
                float y = 0.0f;
                for (XNode c : n.getChildren()) {
                    String ct = c.getText() != null ? c.getText().toString().trim() : "";
                    if ("X".equals(c.getName())) {
                        x = parseSpecial(ct);
                    } else if ("Y".equals(c.getName())) {
                        y = parseSpecial(ct);
                    }
                }
                return TuplesKt.to(13, new Vector2(x, y));
            }
            case "Vector3": {
                float x = 0.0f;
                float y = 0.0f;
                float z = 0.0f;
                for (XNode c : n.getChildren()) {
                    String ct = c.getText() != null ? c.getText().toString().trim() : "";
                    if ("X".equals(c.getName())) {
                        x = parseSpecial(ct);
                    } else if ("Y".equals(c.getName())) {
                        y = parseSpecial(ct);
                    } else if ("Z".equals(c.getName())) {
                        z = parseSpecial(ct);
                    }
                }
                return TuplesKt.to(14, new Vector3(x, y, z));
            }
            case "CoordinateFrame":
            case "CFrame":
                return TuplesKt.to(16, cframe(n));
            case "token":
                try {
                    return TuplesKt.to(18, new EnumValue(Integer.parseInt(txt)));
                } catch (Exception e) {
                    return TuplesKt.to(18, new EnumValue(0));
                }
            case "Ref":
                return TuplesKt.to(19, txt);
            case "Vector3int16": {
                int x = 0;
                int y = 0;
                int z = 0;
                try {
                    x = Integer.parseInt(n.t("X"));
                } catch (Exception ignored) {
                }
                try {
                    y = Integer.parseInt(n.t("Y"));
                } catch (Exception ignored) {
                }
                try {
                    z = Integer.parseInt(n.t("Z"));
                } catch (Exception ignored) {
                }
                return TuplesKt.to(20, new Vector3int16(x, y, z));
            }
            case "NumberSequence": {
                ArrayList<NumberKey> keys = new ArrayList<>();
                if (!txt.isEmpty()) {
                    String[] parts = txt.split("\\s+");
                    for (int i = 0; i + 2 < parts.length; i += 3) {
                        keys.add(new NumberKey(parseSpecial(parts[i]), parseSpecial(parts[i + 1]), parseSpecial(parts[i + 2])));
                    }
                }
                return TuplesKt.to(21, new NumberSequence(keys));
            }
            case "ColorSequence": {
                ArrayList<ColorKey> keys = new ArrayList<>();
                if (!txt.isEmpty()) {
                    String[] parts = txt.split("\\s+");
                    for (int i = 0; i + 4 < parts.length; i += 5) {
                        float t = parseSpecial(parts[i]);
                        float r = parseSpecial(parts[i + 1]);
                        float g = parseSpecial(parts[i + 2]);
                        float b = parseSpecial(parts[i + 3]);
                        float env = parseSpecial(parts[i + 4]);
                        keys.add(new ColorKey(t, new Color3(r, g, b), env));
                    }
                }
                return TuplesKt.to(22, new ColorSequence(keys));
            }
            case "NumberRange": {
                String[] parts = txt.split("\\s+");
                float mn = parts.length > 0 ? parseSpecial(parts[0]) : 0.0f;
                float mx = parts.length > 1 ? parseSpecial(parts[1]) : mn;
                return TuplesKt.to(23, new NumberRange(mn, mx));
            }
            case "Rect2D":
            case "Rect": {
                XNode mnNode = n.child("min");
                XNode mxNode = n.child("max");
                float x0 = mnNode != null ? mnNode.f("X") : 0.0f;
                float y0 = mnNode != null ? mnNode.f("Y") : 0.0f;
                float x1 = mxNode != null ? mxNode.f("X") : 0.0f;
                float y1 = mxNode != null ? mxNode.f("Y") : 0.0f;
                return TuplesKt.to(24, new Rect(x0, y0, x1, y1));
            }
            case "PhysicalProperties": {
                String cp = n.t("CustomPhysics");
                boolean custom = "true".equalsIgnoreCase(cp) || "1".equals(cp);
                if (!custom) {
                    return TuplesKt.to(25, new PhysicalProps(0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 126, null));
                }
                float density = n.child("Density") != null ? n.f("Density") : 0.7f;
                float friction = n.child("Friction") != null ? n.f("Friction") : 0.3f;
                float elasticity = n.child("Elasticity") != null ? n.f("Elasticity") : 0.5f;
                float frictionW = n.child("FrictionWeight") != null ? n.f("FrictionWeight") : 1.0f;
                float elasticityW = n.child("ElasticityWeight") != null ? n.f("ElasticityWeight") : 1.0f;
                XNode acousticNode = n.child("AcousticAbsorption");
                if (acousticNode != null) {
                    return TuplesKt.to(25, new PhysicalProps(3, density, friction, elasticity, frictionW, elasticityW, parseSpecial(acousticNode.getText().toString().trim())));
                }
                return TuplesKt.to(25, new PhysicalProps(1, density, friction, elasticity, frictionW, elasticityW, 1.0f));
            }
            case "int64":
                try {
                    return TuplesKt.to(27, Long.valueOf(Long.parseLong(txt)));
                } catch (Exception e) {
                    return TuplesKt.to(27, 0L);
                }
            case "OptionalCoordinateFrame": {
                for (XNode c : n.getChildren()) {
                    if ("CFrame".equals(c.getName()) || "CoordinateFrame".equals(c.getName())) {
                        return TuplesKt.to(30, new OptionalCFrame(cframe(c)));
                    }
                }
                return TuplesKt.to(30, new OptionalCFrame(null));
            }
            case "UniqueId":
                return TuplesKt.to(31, new UniqueId(decodeHex16(txt)));
            case "Font": {
                String family = contentText(n.child("Family"));
                int weight = 400;
                String wStr = n.t("Weight");
                if (!wStr.isEmpty()) {
                    try {
                        weight = Integer.parseInt(wStr);
                    } catch (Exception ignored) {
                    }
                }
                int style = 0;
                String sStr = n.t("Style");
                if ("Italic".equalsIgnoreCase(sStr)) {
                    style = 1;
                } else if (!sStr.isEmpty() && !"Normal".equalsIgnoreCase(sStr)) {
                    try {
                        style = Integer.parseInt(sStr);
                    } catch (Exception ignored) {
                    }
                }
                String cachedFaceId = contentText(n.child("CachedFaceId"));
                return TuplesKt.to(32, new FontValue(family, weight, style, cachedFaceId));
            }
            case "SecurityCapabilities": {
                long caps = 0L;
                if (!txt.isEmpty()) {
                    try {
                        caps = Long.parseLong(txt);
                    } catch (Exception e) {
                        try {
                            caps = Long.parseUnsignedLong(txt);
                        } catch (Exception ignored) {
                        }
                    }
                }
                return TuplesKt.to(33, new SecurityCaps(caps));
            }
            default:
                return TuplesKt.to(-1, null);
        }
    }

    private final float parseSpecial(String t) {
        if (t == null) {
            return 0.0f;
        }
        String s = t.trim();
        if (s.isEmpty()) {
            return 0.0f;
        }
        String upperCase = s.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        switch (upperCase) {
            case "INF":
            case "+INF":
            case "INFINITY":
            case "+INFINITY":
                return Float.POSITIVE_INFINITY;
            case "-INF":
            case "-INFINITY":
                return Float.NEGATIVE_INFINITY;
            case "NAN":
            case "-NAN":
            case "+NAN":
                return Float.NaN;
            default:
                try {
                    return Float.parseFloat(s);
                } catch (NumberFormatException e) {
                    return 0.0f;
                }
        }
    }
}
