package com.goydevv.studrblx.format;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RbxBinary.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class Lz4 {
    public static final Lz4 INSTANCE = new Lz4();

    private Lz4() {
    }

    public final byte[] decompress(byte[] src, int off, int len, int dstLen) throws RbxFormatException {
        int s;
        int s2;
        Intrinsics.checkNotNullParameter(src, "src");
        if (off < 0 || len < 0 || dstLen < 0 || off + len > src.length) {
            throw new RbxFormatException("Invalid LZ4 chunk bounds");
        }
        byte[] dst = new byte[dstLen];
        int token = off;
        int end = off + len;
        int d = 0;
        while (token < end) {
            int b = token + 1;
            int token2 = src[token] & 255;
            int lit = token2 >>> 4;
            if (lit == 15) {
                while (true) {
                    if (b >= end) {
                        throw new RbxFormatException("Truncated LZ4 literal length");
                    }
                    s2 = b + 1;
                    int b2 = src[b] & 255;
                    lit += b2;
                    if (b2 != 255) {
                        break;
                    }
                    b = s2;
                }
                b = s2;
            }
            if (lit < 0 || b + lit > end || d + lit > dstLen) {
                throw new RbxFormatException("Corrupt LZ4 literal copy");
            }
            System.arraycopy(src, b, dst, d, lit);
            int s3 = b + lit;
            d += lit;
            if (s3 >= end) {
                break;
            }
            if (s3 + 2 > end) {
                throw new RbxFormatException("Truncated LZ4 match offset");
            }
            int offset = (src[s3] & 255) | ((src[s3 + 1] & 255) << 8);
            if (offset == 0) {
                throw new RbxFormatException("Invalid LZ4 match offset");
            }
            int b3 = s3 + 2;
            int ml = token2 & 15;
            if (ml == 15) {
                while (true) {
                    if (b3 >= end) {
                        throw new RbxFormatException("Truncated LZ4 match length");
                    }
                    s = b3 + 1;
                    int b4 = src[b3] & 255;
                    ml += b4;
                    if (b4 != 255) {
                        break;
                    }
                    b3 = s;
                }
                b3 = s;
            }
            int ml2 = ml + 4;
            int m = d - offset;
            if (m < 0 || d + ml2 > dstLen) {
                throw new RbxFormatException("Corrupt LZ4 data");
            }
            int i = 0;
            while (i < ml2) {
                dst[d] = dst[m];
                i++;
                d++;
                m++;
            }
            token = b3;
        }
        return dst;
    }
}
