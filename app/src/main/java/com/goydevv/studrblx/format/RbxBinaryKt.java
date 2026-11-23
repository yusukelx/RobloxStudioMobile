package com.goydevv.studrblx.format;

import com.goydevv.studrblx.model.BinaryString;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.Vector3;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: RbxBinary.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class RbxBinaryKt {
    private static final Vector3[] NORMALS = {Vector3.INSTANCE.getX(), Vector3.INSTANCE.getY(), Vector3.INSTANCE.getZ(), Vector3.INSTANCE.getX().unaryMinus(), Vector3.INSTANCE.getY().unaryMinus(), Vector3.INSTANCE.getZ().unaryMinus()};

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object bytesToStringValue(byte[] b) {
        try {
            CharsetDecoder dec = Charsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
            String string = dec.decode(ByteBuffer.wrap(b)).toString();
            Intrinsics.checkNotNull(string);
            return string;
        } catch (CharacterCodingException e) {
            return new BinaryString(b);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CFrame rotationFromId(int id) {
        int v = id - 1;
        if (v < 0 || v >= 36) {
            return CFrame.INSTANCE.getIDENTITY();
        }
        Vector3 r = NORMALS[v / 6];
        Vector3 u = NORMALS[v % 6];
        return CFrame.INSTANCE.fromColumns(Vector3.INSTANCE.getZERO(), r, u, r.cross(u));
    }
}
