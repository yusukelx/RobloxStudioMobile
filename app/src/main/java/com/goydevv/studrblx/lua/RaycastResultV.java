package com.goydevv.studrblx.lua;

import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LuaTypes.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class RaycastResultV {
    private final RayHitInfo hit;
    private final LuaRuntime rt;

    public RaycastResultV(RayHitInfo hit) {
        this(hit, null);
    }

    public RaycastResultV(RayHitInfo hit, LuaRuntime rt) {
        Intrinsics.checkNotNullParameter(hit, "hit");
        this.hit = hit;
        this.rt = rt;
    }

    public final LuaRuntime getRt() {
        return this.rt;
    }

    public final RayHitInfo getHit() {
        return this.hit;
    }
}
