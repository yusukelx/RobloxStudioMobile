package com.goydevv.studrblx.lua;

import com.goydevv.studrblx.model.Instance;
import java.util.Map;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import org.luaj.vm2.LuaValue;

/* JADX INFO: compiled from: LuaRuntime.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Tween {
    private float elapsed;
    private final Map<String, Object> goals;
    private LuaValue guiCallback;
    private final TweenInfoV info;
    private final Instance inst;
    private int repeatsDone;
    private boolean reversing;
    private Map<String, Object> start;
    private int state;

    public Tween(Instance inst, TweenInfoV info, Map<String, ? extends Object> goals) {
        Intrinsics.checkNotNullParameter(inst, "inst");
        Intrinsics.checkNotNullParameter(info, "info");
        Intrinsics.checkNotNullParameter(goals, "goals");
        this.inst = inst;
        this.info = info;
        this.goals = (Map<String, Object>) goals;
        this.start = MapsKt.emptyMap();
    }

    public final Map<String, Object> getGoals() {
        return this.goals;
    }

    public final TweenInfoV getInfo() {
        return this.info;
    }

    public final Instance getInst() {
        return this.inst;
    }

    public final int getState() {
        return this.state;
    }

    public final void setState(int i) {
        this.state = i;
    }

    public final float getElapsed() {
        return this.elapsed;
    }

    public final void setElapsed(float f) {
        this.elapsed = f;
    }

    public final Map<String, Object> getStart() {
        return this.start;
    }

    public final void setStart(Map<String, ? extends Object> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.start = (Map<String, Object>) map;
    }

    public final int getRepeatsDone() {
        return this.repeatsDone;
    }

    public final void setRepeatsDone(int i) {
        this.repeatsDone = i;
    }

    public final boolean getReversing() {
        return this.reversing;
    }

    public final void setReversing(boolean z) {
        this.reversing = z;
    }

    public final LuaValue getGuiCallback() {
        return this.guiCallback;
    }

    public final void setGuiCallback(LuaValue cb) {
        this.guiCallback = cb;
    }
}
