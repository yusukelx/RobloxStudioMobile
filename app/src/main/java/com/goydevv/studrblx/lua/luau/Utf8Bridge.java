package com.goydevv.studrblx.lua.luau;

import java.util.Arrays;

final class Utf8Bridge {

    private Utf8Bridge() {
    }

    private static boolean cont(byte[] b, int i) {
        return (b[i] & 0xC0) == 0x80;
    }

    private static boolean isFourByte(byte[] b, int i) {
        if (i + 3 >= b.length || (b[i] & 0xF8) != 0xF0 || !cont(b, i + 1) || !cont(b, i + 2) || !cont(b, i + 3)) return false;
        int cp = ((b[i] & 7) << 18) | ((b[i + 1] & 63) << 12) | ((b[i + 2] & 63) << 6) | (b[i + 3] & 63);
        return cp >= 0x10000 && cp <= 0x10FFFF;
    }

    private static boolean isSurrogatePair(byte[] b, int i) {
        return i + 5 < b.length && (b[i] & 0xFF) == 0xED && (b[i + 1] & 0xF0) == 0xA0 && cont(b, i + 2) && (b[i + 3] & 0xFF) == 0xED && (b[i + 4] & 0xF0) == 0xB0 && cont(b, i + 5);
    }

    private static int put3(byte[] out, int o, int c) {
        out[o] = (byte) (0xE0 | (c >> 12));
        out[o + 1] = (byte) (0x80 | ((c >> 6) & 63));
        out[o + 2] = (byte) (0x80 | (c & 63));
        return o + 3;
    }

    static byte[] toLuaJ(byte[] b) {
        int extra = 0;
        for (int i = 0; i < b.length; i++) {
            if (isFourByte(b, i)) {
                extra += 2;
                i += 3;
            }
        }
        if (extra == 0) return b;
        byte[] out = new byte[b.length + extra];
        int o = 0;
        for (int i = 0; i < b.length; ) {
            if (isFourByte(b, i)) {
                int cp = (((b[i] & 7) << 18) | ((b[i + 1] & 63) << 12) | ((b[i + 2] & 63) << 6) | (b[i + 3] & 63)) - 0x10000;
                o = put3(out, o, 0xD800 | (cp >> 10));
                o = put3(out, o, 0xDC00 | (cp & 0x3FF));
                i += 4;
            } else {
                out[o++] = b[i++];
            }
        }
        return out;
    }

    static byte[] fromLuaJ(byte[] b) {
        int pairs = 0;
        for (int i = 0; i < b.length; i++) {
            if (isSurrogatePair(b, i)) {
                pairs++;
                i += 5;
            }
        }
        if (pairs == 0) return b;
        byte[] out = new byte[b.length - pairs * 2];
        int o = 0;
        for (int i = 0; i < b.length; ) {
            if (isSurrogatePair(b, i)) {
                int hi = 0xD000 | ((b[i + 1] & 63) << 6) | (b[i + 2] & 63);
                int lo = 0xD000 | ((b[i + 4] & 63) << 6) | (b[i + 5] & 63);
                int cp = 0x10000 + ((hi - 0xD800) << 10) + (lo - 0xDC00);
                out[o++] = (byte) (0xF0 | (cp >> 18));
                out[o++] = (byte) (0x80 | ((cp >> 12) & 63));
                out[o++] = (byte) (0x80 | ((cp >> 6) & 63));
                out[o++] = (byte) (0x80 | (cp & 63));
                i += 6;
            } else {
                out[o++] = b[i++];
            }
        }
        return out;
    }
}
