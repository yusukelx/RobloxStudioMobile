package com.goydevv.studrblx.audio;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.PlaySession;
import com.goydevv.studrblx.model.CFrame;
import com.goydevv.studrblx.model.EnumValue;
import com.goydevv.studrblx.model.Instance;
import com.goydevv.studrblx.model.PartData;
import com.goydevv.studrblx.model.Vector3;
import com.goydevv.studrblx.net.AssetFetcher;
import com.goydevv.studrblx.render.FlyCamera;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: AudioEngine.kt */
/* JADX INFO: loaded from: classes3.dex */
public final class AudioEngine {
    public static final AudioEngine INSTANCE = new AudioEngine();
    private static final int MAX_VOICES = 28;
    private static final Handler h;
    private static final HashSet<Long> missing;
    private static Function1<? super Instance, Unit> onEnded;
    private static Function1<? super Instance, Unit> onLoaded;
    private static volatile Preview preview;
    private static final CopyOnWriteArrayList<Function0<Unit>> previewListeners;
    private static MediaPlayer previewPlayer;
    private static final HandlerThread thread;
    private static final IdentityHashMap<Instance, Voice> voices;

    /* JADX INFO: compiled from: AudioEngine.kt */
    public enum PreviewState {
        LOADING,
        PLAYING,
        PAUSED,
        STOPPED,
        ERROR;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<PreviewState> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: AudioEngine.kt */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PreviewState.values().length];
            try {
                iArr[PreviewState.PLAYING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PreviewState.PAUSED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PreviewState.STOPPED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PreviewState.LOADING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[PreviewState.ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private AudioEngine() {
    }

    static {
        HandlerThread handlerThread = new HandlerThread("audio");
        handlerThread.start();
        thread = handlerThread;
        h = new Handler(thread.getLooper());
        voices = new IdentityHashMap<>();
        missing = new HashSet<>();
        previewListeners = new CopyOnWriteArrayList<>();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: AudioEngine.kt */
    static final class Voice {
        private volatile boolean ended;
        private volatile boolean failed;
        private final long id;
        private float lastL;
        private Boolean lastLoop;
        private double lastPushedPos;
        private float lastR;
        private float lastSpeed;
        private volatile double length;
        private volatile boolean loadedNotified;
        private MediaPlayer mp;
        private volatile double position;
        private boolean prepared;
        private double seek;
        private int seq;
        private final Instance sound;
        private boolean wantPlay;

        public Voice(Instance sound, long id) {
            Intrinsics.checkNotNullParameter(sound, "sound");
            this.sound = sound;
            this.id = id;
            this.lastL = -1.0f;
            this.lastR = -1.0f;
            this.lastSpeed = -1.0f;
            this.seek = -1.0d;
            Object obj = this.sound.getAttributes().get("__playSeq");
            Integer num = obj instanceof Integer ? (Integer) obj : null;
            this.seq = num != null ? num.intValue() : 0;
        }

        public final long getId() {
            return this.id;
        }

        public final Instance getSound() {
            return this.sound;
        }

        public final MediaPlayer getMp() {
            return this.mp;
        }

        public final void setMp(MediaPlayer mediaPlayer) {
            this.mp = mediaPlayer;
        }

        public final boolean getPrepared() {
            return this.prepared;
        }

        public final void setPrepared(boolean z) {
            this.prepared = z;
        }

        public final float getLastL() {
            return this.lastL;
        }

        public final Boolean getLastLoop() {
            return this.lastLoop;
        }

        public final float getLastR() {
            return this.lastR;
        }

        public final float getLastSpeed() {
            return this.lastSpeed;
        }

        public final boolean getWantPlay() {
            return this.wantPlay;
        }

        public final void setLastL(float f) {
            this.lastL = f;
        }

        public final void setLastLoop(Boolean bool) {
            this.lastLoop = bool;
        }

        public final void setLastR(float f) {
            this.lastR = f;
        }

        public final void setLastSpeed(float f) {
            this.lastSpeed = f;
        }

        public final void setWantPlay(boolean z) {
            this.wantPlay = z;
        }

        public final double getPosition() {
            return this.position;
        }

        public final void setPosition(double d) {
            this.position = d;
        }

        public final double getLength() {
            return this.length;
        }

        public final void setLength(double d) {
            this.length = d;
        }

        public final boolean getEnded() {
            return this.ended;
        }

        public final void setEnded(boolean z) {
            this.ended = z;
        }

        public final boolean getLoadedNotified() {
            return this.loadedNotified;
        }

        public final void setLoadedNotified(boolean z) {
            this.loadedNotified = z;
        }

        public final boolean getFailed() {
            return this.failed;
        }

        public final void setFailed(boolean z) {
            this.failed = z;
        }

        public final double getLastPushedPos() {
            return this.lastPushedPos;
        }

        public final void setLastPushedPos(double d) {
            this.lastPushedPos = d;
        }

        public final double getSeek() {
            return this.seek;
        }

        public final void setSeek(double d) {
            this.seek = d;
        }

        public final int getSeq() {
            return this.seq;
        }

        public final void setSeq(int i) {
            this.seq = i;
        }
    }

    public final Function1<Instance, Unit> getOnLoaded() {
        return (Function1<Instance, Unit>) onLoaded;
    }

    public final void setOnLoaded(Function1<? super Instance, Unit> function1) {
        onLoaded = function1;
    }

    public final Function1<Instance, Unit> getOnEnded() {
        return (Function1<Instance, Unit>) onEnded;
    }

    public final void setOnEnded(Function1<? super Instance, Unit> function1) {
        onEnded = function1;
    }

    private final Long soundId(Instance s) {
        return AssetFetcher.INSTANCE.idOf(s.get("SoundId"));
    }

    private final float num(Instance i, String k, float d) {
        Object obj = i != null ? i.get(k) : null;
        Number number = obj instanceof Number ? (Number) obj : null;
        return number != null ? number.floatValue() : d;
    }

    /* JADX WARN: Code duplicated, block: B:101:0x0274  */
    /* JADX WARN: Code duplicated, block: B:103:0x02b4  */
    /* JADX WARN: Code duplicated, block: B:104:0x02b7  */
    /* JADX WARN: Code duplicated, block: B:106:0x02ba  */
    /* JADX WARN: Code duplicated, block: B:107:0x02bf  */
    /* JADX WARN: Code duplicated, block: B:110:0x02c5  */
    /* JADX WARN: Code duplicated, block: B:111:0x02c8  */
    /* JADX WARN: Code duplicated, block: B:113:0x02cc  */
    /* JADX WARN: Code duplicated, block: B:114:0x02ce  */
    /* JADX WARN: Code duplicated, block: B:119:0x02d7  */
    /* JADX WARN: Code duplicated, block: B:122:0x02f0  */
    /* JADX WARN: Code duplicated, block: B:123:0x031e  */
    /* JADX WARN: Code duplicated, block: B:124:0x0328  */
    /* JADX WARN: Code duplicated, block: B:127:0x0370  */
    /* JADX WARN: Code duplicated, block: B:128:0x0374  */
    /* JADX WARN: Code duplicated, block: B:130:0x0377  */
    /* JADX WARN: Code duplicated, block: B:131:0x037c  */
    /* JADX WARN: Code duplicated, block: B:134:0x0383  */
    /* JADX WARN: Code duplicated, block: B:136:0x038c  */
    /* JADX WARN: Code duplicated, block: B:137:0x0399  */
    /* JADX WARN: Code duplicated, block: B:138:0x039c  */
    /* JADX WARN: Code duplicated, block: B:153:0x03e6  */
    /* JADX WARN: Code duplicated, block: B:160:0x0445  */
    /* JADX WARN: Code duplicated, block: B:163:0x045f  */
    /* JADX WARN: Code duplicated, block: B:164:0x0473  */
    /* JADX WARN: Code duplicated, block: B:169:0x049a  */
    /* JADX WARN: Code duplicated, block: B:208:0x04a9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:209:0x047a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:72:0x01ef  */
    /* JADX WARN: Code duplicated, block: B:73:0x0206  */
    /* JADX WARN: Code duplicated, block: B:75:0x020c  */
    /* JADX WARN: Code duplicated, block: B:76:0x0211  */
    /* JADX WARN: Code duplicated, block: B:97:0x0266  */
    public final void update(Studio studio) {
        Voice v = null;
        float f;
        boolean z = false;
        Instance parent;
        String className;
        Vector3 emitter;
        Instance parent2;
        Instance parent3;
        Vector3 zero;
        float left;
        float left2;
        float mn;
        float l = 0.0f;
        float r = 0.0f;
        float speed = 1.0f;
        boolean loop = false;
        Voice vv = null;
        Object obj;
        Integer num;
        int seq;
        Voice vv2;
        boolean z2;
        Function1<? super Instance, Unit> function1;
        Function1<? super Instance, Unit> function2;
        float d;
        float mn2;
        float mx;
        Object obj2;
        EnumValue enumValue;
        int mode;
        float gain;
        Vector3 dir;
        List<Instance> list;
        Intrinsics.checkNotNullParameter(studio, "studio");
        PlaySession play = studio.getPlay();
        if (play == null) {
            if (voices.isEmpty()) {
                return;
            }
            stopAll();
            return;
        }
        List<Instance> listAudibleSounds = studio.audibleSounds();
        Set alive = Collections.newSetFromMap(new IdentityHashMap());
        FlyCamera cam = studio.getCamera();
        float master = Settings.INSTANCE.getVolume() / 10.0f;
        for (Instance s : listAudibleSounds) {
            if (s.getParent() != null) {
                alive.add(s);
                boolean playing = Intrinsics.areEqual(s.get("Playing"), (Object) true) || Intrinsics.areEqual(s.get("IsPlaying"), (Object) true);
                Voice v2 = voices.get(s);
                Long id = soundId(s);
                if (v2 != null) {
                    long id2 = v2.getId();
                    if (id == null || id2 != id.longValue()) {
                        release(v2);
                        voices.remove(s);
                        v2 = null;
                    }
                }
                if (!playing) {
                    if (v2 != null && v2.getWantPlay()) {
                        v2.setWantPlay(false);
                        final boolean stopped = num(s, "TimePosition", 0.0f) == 0.0f;
                        final Voice vv3 = v2;
                        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda4
                            @Override // java.lang.Runnable
                            public final void run() {
                                AudioEngine.update$lambda$2(vv3, stopped);
                            }
                        });
                    }
                } else if (id != null) {
                    if (v2 != null) {
                        v = v2;
                    } else if (voices.size() < 28) {
                        boolean bytes = AssetFetcher.INSTANCE.isCached(id.longValue());
                        if (!bytes) {
                            PlaySession play2 = play;
                            AssetFetcher.INSTANCE.request(id.longValue());
                            if (missing.contains(id)) {
                                list = listAudibleSounds;
                            } else {
                                list = listAudibleSounds;
                                if (AssetFetcher.INSTANCE.lastError(id.longValue()) != null) {
                                    missing.add(id);
                                    OutputLog.INSTANCE.warn("Couldn't load sound " + s.fullName() + " (rbxassetid://" + id + "): " + AssetFetcher.INSTANCE.lastError(id.longValue()));
                                }
                            }
                            listAudibleSounds = list;
                            play = play2;
                        } else {
                            Voice v3 = new Voice(s, id.longValue());
                            voices.put(s, v3);
                            double start = num(s, "TimePosition", 0.0f);
                            v3.setSeek(start);
                            create(v3);
                            v = v3;
                        }
                    }
                    if (v.getFailed()) {
                    } else {
                        float vol = RangesKt.coerceIn(num(s, "Volume", 0.5f), 0.0f, 10.0f);
                        Object obj3 = s.get("SoundGroup");
                        Instance instance = obj3 instanceof Instance ? (Instance) obj3 : null;
                        if (instance != null) {
                            vol *= RangesKt.coerceIn(INSTANCE.num(instance, "Volume", 1.0f), 0.0f, 10.0f);
                        }
                        float vol2 = vol * master;
                        float right = 1.0f;
                        Instance parent4 = s.getParent();
                        if (parent4 != null) {
                            f = 1.0f;
                            z = parent4.isA("BasePart");
                            if (z) {
                                PartData partData = PartData.INSTANCE;
                                Instance parent5 = s.getParent();
                                Intrinsics.checkNotNull(parent5);
                                emitter = partData.cframe(parent5).getPosition();
                            } else {
                                parent = s.getParent();
                                if (parent != null) {
                                    className = parent.getClassName();
                                } else {
                                    className = null;
                                }
                                if (Intrinsics.areEqual(className, "Attachment") || (parent2 = s.getParent()) == null || (parent3 = parent2.getParent()) == null) {
                                    emitter = null;
                                } else {
                                    if (!parent3.isA("BasePart")) {
                                        parent3 = null;
                                    }
                                    if (parent3 != null) {
                                        CFrame cframe = PartData.INSTANCE.cframe(parent3);
                                        Instance parent6 = s.getParent();
                                        Intrinsics.checkNotNull(parent6);
                                        Object obj4 = parent6.get("CFrame");
                                        CFrame cFrame = obj4 instanceof CFrame ? (CFrame) obj4 : null;
                                        if (cFrame == null || (zero = cFrame.getPosition()) == null) {
                                            zero = Vector3.INSTANCE.getZERO();
                                        }
                                        emitter = cframe.times(zero);
                                    } else {
                                        emitter = null;
                                    }
                                }
                            }
                            if (emitter != null) {
                                left = 1.0f;
                                left2 = f;
                                mn = 0.0f;
                            } else {
                                d = emitter.minus(cam.getPos()).getMagnitude();
                                mn2 = RangesKt.coerceAtLeast(num(s, "RollOffMinDistance", num(s, "xmlRead_MinDistance_3", 10.0f)), 0.1f);
                                mx = RangesKt.coerceAtLeast(num(s, "RollOffMaxDistance", num(s, "xmlRead_MaxDistance_3", 10000.0f)), mn2);
                                obj2 = s.get("RollOffMode");
                                if (obj2 instanceof EnumValue) {
                                    enumValue = (EnumValue) obj2;
                                } else {
                                    enumValue = null;
                                }
                                if (enumValue != null) {
                                    mode = enumValue.getValue();
                                } else {
                                    mode = 0;
                                }
                                if (d <= mn2) {
                                    gain = f;
                                } else if (d >= mx) {
                                    gain = 0.0f;
                                } else if (mode != 1 || mode == 3) {
                                    gain = f - ((d - mn2) / (mx - mn2));
                                } else {
                                    gain = mn2 / d;
                                }
                                vol2 *= gain;
                                dir = emitter.minus(cam.getPos());
                                if (d > 1056964608) {
                                    left = 1.0f;
                                    left2 = f;
                                    mn = 0.0f;
                                } else {
                                    left2 = f;
                                    float pan = RangesKt.coerceIn(dir.getUnit().dot(cam.getRight()), -1.0f, left2) * 0.7f;
                                    mn = 0.0f;
                                    float left3 = left2 - Math.max(0.0f, pan);
                                    right = Math.min(0.0f, pan) + left2;
                                    left = left3;
                                }
                            }
                            l = RangesKt.coerceIn(vol2 * left, mn, left2);
                            r = RangesKt.coerceIn(vol2 * right, mn, left2);
                            speed = RangesKt.coerceIn(num(s, "PlaybackSpeed", num(s, "Pitch", left2)), 0.05f, 8.0f);
                            loop = Intrinsics.areEqual(s.get("Looped"), (Object) true);
                            vv = v;
                            obj = s.getAttributes().get("__playSeq");
                            if (obj instanceof Integer) {
                                num = (Integer) obj;
                            } else {
                                num = null;
                            }
                            if (num != null) {
                                seq = num.intValue();
                            } else {
                                seq = 0;
                            }
                            if (seq != vv.getSeq()) {
                                vv.setSeq(seq);
                                if (vv.getWantPlay()) {
                                    final Voice _vv = vv;
                                h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda5
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            AudioEngine.update$lambda$6(_vv);
                                        }
                                    });
                                }
                            }
                            if (vv.getWantPlay() || Math.abs(l - vv.getLastL()) > 0.01f || Math.abs(r - vv.getLastR()) > 0.01f) {
                                final boolean restart = !vv.getWantPlay();
                                vv.setWantPlay(true);
                                vv.setLastL(l);
                                vv.setLastR(r);
                                vv.setLastSpeed(speed);
                                vv.setLastLoop(Boolean.valueOf(loop));
                                final Voice _vvApply = vv; final float _lApply = l; final float _rApply = r; final float _speedApply = speed; final boolean _loopApply = loop;
                            Handler handler = h;
                                Runnable runnable = new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda6
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        AudioEngine.INSTANCE.apply(_vvApply, _lApply, _rApply, _speedApply, _loopApply, restart);
                                    }
                                };
                                vv2 = vv;
                                handler.post(runnable);
                            } else if ((speed == vv.getLastSpeed()) && Intrinsics.areEqual(Boolean.valueOf(loop), vv.getLastLoop())) {
                                vv2 = vv;
                            } else {
                                final boolean restart2 = !vv.getWantPlay();
                                vv.setWantPlay(true);
                                vv.setLastL(l);
                                vv.setLastR(r);
                                vv.setLastSpeed(speed);
                                vv.setLastLoop(Boolean.valueOf(loop));
                                final Voice _vvApply = vv; final float _lApply = l; final float _rApply = r; final float _speedApply = speed; final boolean _loopApply = loop;
                            Handler handler2 = h;
                                Runnable runnable2 = new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda6
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        AudioEngine.INSTANCE.apply(_vvApply, _lApply, _rApply, _speedApply, _loopApply, restart2);
                                    }
                                };
                                vv2 = vv;
                                handler2.post(runnable2);
                            }
                            if (vv2.getLength() > 0.0d && !vv2.getLoadedNotified()) {
                                vv2.setLoadedNotified(true);
                                s.set("TimeLength", Double.valueOf(vv2.getLength()), false);
                                s.set("IsLoaded", true, false);
                                function2 = onLoaded;
                                if (function2 != null) {
                                    function2.invoke(s);
                                }
                            }
                            if (Math.abs(vv2.getPosition() - vv2.getLastPushedPos()) > 0.2d) {
                                vv2.setLastPushedPos(vv2.getPosition());
                                z2 = false;
                                s.set("TimePosition", Double.valueOf(vv2.getPosition()), false);
                            } else {
                                z2 = false;
                            }
                            if (vv2.getEnded()) {
                                vv2.setEnded(z2);
                                vv2.setWantPlay(z2);
                                s.set("TimePosition", Double.valueOf(0.0d), z2);
                                Instance.set$default(s, "Playing", Boolean.valueOf(z2), false, 4, null);
                                function1 = onEnded;
                                if (function1 != null) {
                                    function1.invoke(s);
                                }
                            } else {
                            }
                        } else {
                            f = 1.0f;
                        }
                        if (z) {
                            PartData partData2 = PartData.INSTANCE;
                            Instance parent7 = s.getParent();
                            Intrinsics.checkNotNull(parent7);
                            emitter = partData2.cframe(parent7).getPosition();
                        } else {
                            parent = s.getParent();
                            if (parent != null) {
                                className = parent.getClassName();
                            } else {
                                className = null;
                            }
                            if (Intrinsics.areEqual(className, "Attachment")) {
                                emitter = null;
                            } else {
                                emitter = null;
                            }
                        }
                        if (emitter != null) {
                            left = 1.0f;
                            left2 = f;
                            mn = 0.0f;
                        } else {
                            d = emitter.minus(cam.getPos()).getMagnitude();
                            mn2 = RangesKt.coerceAtLeast(num(s, "RollOffMinDistance", num(s, "xmlRead_MinDistance_3", 10.0f)), 0.1f);
                            mx = RangesKt.coerceAtLeast(num(s, "RollOffMaxDistance", num(s, "xmlRead_MaxDistance_3", 10000.0f)), mn2);
                            obj2 = s.get("RollOffMode");
                            if (obj2 instanceof EnumValue) {
                                enumValue = (EnumValue) obj2;
                            } else {
                                enumValue = null;
                            }
                            if (enumValue != null) {
                                mode = enumValue.getValue();
                            } else {
                                mode = 0;
                            }
                            if (d <= mn2) {
                                gain = f;
                            } else if (d >= mx) {
                                gain = 0.0f;
                            } else if (mode != 1) {
                                gain = f - ((d - mn2) / (mx - mn2));
                            } else {
                                gain = f - ((d - mn2) / (mx - mn2));
                            }
                            vol2 *= gain;
                            dir = emitter.minus(cam.getPos());
                            if (d > 1056964608) {
                                left = 1.0f;
                                left2 = f;
                                mn = 0.0f;
                            } else {
                                left2 = f;
                                float pan2 = RangesKt.coerceIn(dir.getUnit().dot(cam.getRight()), -1.0f, left2) * 0.7f;
                                mn = 0.0f;
                                float left4 = left2 - Math.max(0.0f, pan2);
                                right = Math.min(0.0f, pan2) + left2;
                                left = left4;
                            }
                        }
                        l = RangesKt.coerceIn(vol2 * left, mn, left2);
                        r = RangesKt.coerceIn(vol2 * right, mn, left2);
                        speed = RangesKt.coerceIn(num(s, "PlaybackSpeed", num(s, "Pitch", left2)), 0.05f, 8.0f);
                        loop = Intrinsics.areEqual(s.get("Looped"), (Object) true);
                        vv = v;
                        obj = s.getAttributes().get("__playSeq");
                        if (obj instanceof Integer) {
                            num = (Integer) obj;
                        } else {
                            num = null;
                        }
                        if (num != null) {
                            seq = num.intValue();
                        } else {
                            seq = 0;
                        }
                        if (seq != vv.getSeq()) {
                            vv.setSeq(seq);
                            if (vv.getWantPlay()) {
                                final Voice _vv = vv;
                                h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda5
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        AudioEngine.update$lambda$6(_vv);
                                    }
                                });
                            }
                        }
                        if (vv.getWantPlay()) {
                            final boolean restart3 = !vv.getWantPlay();
                            vv.setWantPlay(true);
                            vv.setLastL(l);
                            vv.setLastR(r);
                            vv.setLastSpeed(speed);
                            vv.setLastLoop(Boolean.valueOf(loop));
                            final Voice _vvApply = vv; final float _lApply = l; final float _rApply = r; final float _speedApply = speed; final boolean _loopApply = loop;
                            Handler handler3 = h;
                            Runnable runnable3 = new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda6
                                @Override // java.lang.Runnable
                                public final void run() {
                                    AudioEngine.INSTANCE.apply(_vvApply, _lApply, _rApply, _speedApply, _loopApply, restart3);
                                }
                            };
                            vv2 = vv;
                            handler3.post(runnable3);
                        } else {
                            final boolean restart4 = !vv.getWantPlay();
                            vv.setWantPlay(true);
                            vv.setLastL(l);
                            vv.setLastR(r);
                            vv.setLastSpeed(speed);
                            vv.setLastLoop(Boolean.valueOf(loop));
                            final Voice _vvApply = vv; final float _lApply = l; final float _rApply = r; final float _speedApply = speed; final boolean _loopApply = loop;
                            Handler handler4 = h;
                            Runnable runnable4 = new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda6
                                @Override // java.lang.Runnable
                                public final void run() {
                                    AudioEngine.INSTANCE.apply(_vvApply, _lApply, _rApply, _speedApply, _loopApply, restart4);
                                }
                            };
                            vv2 = vv;
                            handler4.post(runnable4);
                        }
                        if (vv2.getLength() > 0.0d) {
                            vv2.setLoadedNotified(true);
                            s.set("TimeLength", Double.valueOf(vv2.getLength()), false);
                            s.set("IsLoaded", true, false);
                            function2 = onLoaded;
                            if (function2 != null) {
                                function2.invoke(s);
                            }
                        }
                        if (Math.abs(vv2.getPosition() - vv2.getLastPushedPos()) > 0.2d) {
                            vv2.setLastPushedPos(vv2.getPosition());
                            z2 = false;
                            s.set("TimePosition", Double.valueOf(vv2.getPosition()), false);
                        } else {
                            z2 = false;
                        }
                        if (vv2.getEnded()) {
                            vv2.setEnded(z2);
                            vv2.setWantPlay(z2);
                            s.set("TimePosition", Double.valueOf(0.0d), z2);
                            Instance.set$default(s, "Playing", Boolean.valueOf(z2), false, 4, null);
                            function1 = onEnded;
                            if (function1 != null) {
                                function1.invoke(s);
                            }
                        } else {
                        }
                    }
                }
            }
        }
        boolean z3 = false;
        if (voices.size() <= alive.size()) {
            Iterable iterableKeySet = voices.keySet();
            Intrinsics.checkNotNullExpressionValue(iterableKeySet, "<get-keys>(...)");
            Iterable iterable = iterableKeySet;
            if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                Iterator it = iterable.iterator();
                while (it.hasNext()) {
                    if (!alive.contains((Instance) it.next())) {
                        z3 = true;
                        break;
                    }
                }
            }
            if (!z3) {
                return;
            }
        }
        Iterator<Map.Entry<Instance, Voice>> it2 = voices.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Instance, Voice> next = it2.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Map.Entry<Instance, Voice> entry = next;
            if (!alive.contains(entry.getKey())) {
                Voice value = entry.getValue();
                Intrinsics.checkNotNullExpressionValue(value, "<get-value>(...)");
                release(value);
                it2.remove();
            }
        }
    }

    static final void update$lambda$2(Voice $vv, boolean $stopped) {
        try {
            MediaPlayer mp = $vv.getMp();
            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.pause();
                }
                if ($stopped && $vv.getPrepared()) {
                    mp.seekTo(0);
                }
            }
        } catch (Exception e) {
        }
    }

    static final void update$lambda$6(Voice $vv) {
        MediaPlayer mp;
        try {
            if (!$vv.getPrepared() || (mp = $vv.getMp()) == null) {
                return;
            }
            mp.seekTo(0);
        } catch (Exception e) {
        }
    }

    private final void create(final Voice v) {
        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.create$lambda$12(v);
            }
        });
    }

    static final void create$lambda$12(final Voice $v) {
        try {
            String strCacheFile = AssetFetcher.INSTANCE.cacheFile($v.getId());
            if (strCacheFile == null) {
                return;
            }
            File f = new File(strCacheFile);
            String bad = INSTANCE.checkAudioFile(f);
            if (bad != null) {
                $v.setFailed(true);
                OutputLog.INSTANCE.warn("Sound rbxassetid://" + $v.getId() + " can't be played: " + bad);
                return;
            }
            MediaPlayer mp = new MediaPlayer();
            mp.setAudioAttributes(new AudioAttributes.Builder().setUsage(14).setContentType(4).build());
            mp.setDataSource(f.getPath());
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda13
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    AudioEngine.create$lambda$12$lambda$9($v, mediaPlayer);
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda14
                @Override // android.media.MediaPlayer.OnCompletionListener
                public final void onCompletion(MediaPlayer mediaPlayer) {
                    AudioEngine.create$lambda$12$lambda$10($v, mediaPlayer);
                }
            });
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda15
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    return AudioEngine.create$lambda$12$lambda$11($v, mediaPlayer, i, i2);
                }
            });
            $v.setMp(mp);
            mp.prepareAsync();
        } catch (Exception e) {
            $v.setFailed(true);
            OutputLog.INSTANCE.warn("Sound rbxassetid://" + $v.getId() + ": " + e.getMessage());
        }
    }

    static final void create$lambda$12$lambda$9(Voice $v, MediaPlayer it) {
        try {
            create$lambda$12$lambda$9$prepared($v, it);
        } catch (Throwable e) {
            $v.setFailed(true);
            OutputLog.INSTANCE.warn("Sound rbxassetid://" + $v.getId() + ": " + e.getMessage());
        }
    }

    private static void create$lambda$12$lambda$9$prepared(Voice $v, MediaPlayer it) {
        if ($v.getMp() != it) {
            it.release();
            return;
        }
        $v.setPrepared(true);
        $v.setLength(((double) it.getDuration()) / 1000.0d);
        if ($v.getSeek() > 0.0d) {
            try {
                it.seekTo((int) ($v.getSeek() * 1000.0d));
            } catch (Exception e) {
            }
        }
        if ($v.getWantPlay()) {
            it.setVolume(RangesKt.coerceAtLeast($v.getLastL(), 0.0f), RangesKt.coerceAtLeast($v.getLastR(), 0.0f));
            it.setLooping(Intrinsics.areEqual((Object) $v.getLastLoop(), (Object) true));
            AudioEngine audioEngine = INSTANCE;
            Intrinsics.checkNotNull(it);
            audioEngine.setSpeed(it, $v.getLastSpeed());
            it.start();
        }
        INSTANCE.tick($v);
    }

    static final void create$lambda$12$lambda$10(Voice $v, MediaPlayer it) {
        if (it.isLooping()) {
            return;
        }
        $v.setEnded(true);
        $v.setPosition(0.0d);
    }

    static final boolean create$lambda$12$lambda$11(Voice $v, MediaPlayer mediaPlayer, int what, int extra) {
        $v.setFailed(true);
        OutputLog.INSTANCE.warn("Sound rbxassetid://" + $v.getId() + " can't be played on this device (" + what + "/" + extra + ")");
        return true;
    }

    public final String checkAudioFile(File f) {
        if (f == null || !f.isFile()) {
            return "file is missing";
        }
        long len = f.length();
        if (len < 16) {
            return "file is empty or truncated";
        }
        byte[] b = new byte[16];
        try (java.io.FileInputStream in = new java.io.FileInputStream(f)) {
            int n = 0;
            while (n < b.length) {
                int r = in.read(b, n, b.length - n);
                if (r < 0) {
                    break;
                }
                n += r;
            }
            if (n < b.length) {
                return "file is truncated";
            }
        } catch (Exception e) {
            return "file can't be read";
        }
        if (b[0] == 'O' && b[1] == 'g' && b[2] == 'g' && b[3] == 'S') {
            return null;
        }
        if (b[0] == 'I' && b[1] == 'D' && b[2] == '3') {
            return null;
        }
        if ((b[0] & 0xFF) == 0xFF && (b[1] & 0xE0) == 0xE0) {
            return null;
        }
        if (b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F' && b[8] == 'W' && b[9] == 'A' && b[10] == 'V' && b[11] == 'E') {
            return null;
        }
        if (b[0] == 'f' && b[1] == 'L' && b[2] == 'a' && b[3] == 'C') {
            return null;
        }
        if (b[4] == 'f' && b[5] == 't' && b[6] == 'y' && b[7] == 'p') {
            return null;
        }
        if (b[0] == '<' || b[0] == '{') {
            return "server returned a text response instead of audio";
        }
        return "unsupported audio format";
    }

    private final void setSpeed(MediaPlayer mp, float speed) {
        if (speed <= 0.0f || Math.abs(speed - 1.0f) < 0.001f) {
            return;
        }
        try {
            mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed).setPitch(speed));
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void apply(Voice v, float l, float r, float speed, boolean loop, boolean restart) {
        MediaPlayer mp = v.getMp();
        if (mp != null && v.getPrepared()) {
            try {
                mp.setVolume(l, r);
                mp.setLooping(loop);
                setSpeed(mp, speed);
                if (restart || !mp.isPlaying()) {
                    mp.start();
                    tick(v);
                }
            } catch (Exception e) {
            }
        }
    }

    private final void tick(final Voice v) {
        h.postDelayed(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine.tick.1
            @Override // java.lang.Runnable
            public void run() {
                MediaPlayer mp = v.getMp();
                if (mp == null) {
                    return;
                }
                try {
                    if (v.getPrepared()) {
                        v.setPosition(((double) mp.getCurrentPosition()) / 1000.0d);
                    }
                    if (v.getWantPlay()) {
                        AudioEngine.h.postDelayed(this, 200L);
                    }
                } catch (Exception e) {
                }
            }
        }, 200L);
    }

    private final void release(final Voice v) {
        v.setWantPlay(false);
        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.release$lambda$13(v);
            }
        });
    }

    static final void release$lambda$13(Voice $v) {
        try {
            MediaPlayer mp = $v.getMp();
            if (mp != null) {
                mp.release();
            }
        } catch (Exception e) {
        }
        $v.setMp(null);
    }

    public final void stopAll() {
        for (Voice v : voices.values()) {
            Intrinsics.checkNotNull(v);
            release(v);
        }
        voices.clear();
        missing.clear();
    }

    /* JADX INFO: compiled from: AudioEngine.kt */
    public static final class Preview {
        private volatile String error;
        private final long id;
        private volatile float length;
        private volatile float position;
        private volatile PreviewState state = PreviewState.LOADING;

        public Preview(long id) {
            this.id = id;
        }

        public final long getId() {
            return this.id;
        }

        public final PreviewState getState() {
            return this.state;
        }

        public final void setState(PreviewState previewState) {
            Intrinsics.checkNotNullParameter(previewState, "<set-?>");
            this.state = previewState;
        }

        public final float getPosition() {
            return this.position;
        }

        public final void setPosition(float f) {
            this.position = f;
        }

        public final float getLength() {
            return this.length;
        }

        public final void setLength(float f) {
            this.length = f;
        }

        public final String getError() {
            return this.error;
        }

        public final void setError(String str) {
            this.error = str;
        }
    }

    public final Preview getPreview() {
        return preview;
    }

    public final CopyOnWriteArrayList<Function0<Unit>> getPreviewListeners() {
        return previewListeners;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyPreview() {
        Iterator it = previewListeners.iterator();
        while (it.hasNext()) {
            ((Function0) it.next()).invoke();
        }
    }

    public static /* synthetic */ void togglePreview$default(AudioEngine audioEngine, long j, float f, int i, Object obj) {
        if ((i & 2) != 0) {
            f = 0.5f;
        }
        audioEngine.togglePreview(j, f);
    }

    public final void togglePreview(final long id, final float volume) {
        Preview cur = preview;
        if (cur != null && cur.getId() == id) {
            switch (WhenMappings.$EnumSwitchMapping$0[cur.getState().ordinal()]) {
                case 1:
                    pausePreview();
                    return;
                case 2:
                    resumePreview();
                    return;
                case 3:
                    if (cur.getLength() > 0.0f) {
                        resumePreview();
                        return;
                    }
                    break;
                case 4:
                    stopPreview();
                    return;
                case 5:
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
        }
        stopPreview();
        final Preview p = new Preview(id);
        preview = p;
        notifyPreview();
        new Thread(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.togglePreview$lambda$19(id, p, volume);
            }
        }).start();
    }

    static final void togglePreview$lambda$19(final long $id, final Preview $p, final float $volume) {
        try {
            AssetFetcher.INSTANCE.load($id);
            h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    AudioEngine.togglePreview$lambda$19$lambda$18($p, $id, $volume);
                }
            });
        } catch (Exception e) {
            $p.setState(PreviewState.ERROR);
            String message = e.getMessage();
            if (message == null) {
                message = "Couldn't download the audio";
            }
            $p.setError(message);
            INSTANCE.notifyPreview();
        }
    }

    static final void togglePreview$lambda$19$lambda$18(final Preview $p, long $id, final float $volume) {
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p) {
            return;
        }
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setAudioAttributes(new AudioAttributes.Builder().setUsage(1).setContentType(2).build());
            String strCacheFile = AssetFetcher.INSTANCE.cacheFile($id);
            if (strCacheFile == null) {
                mp.release();
                throw new IllegalStateException("Audio file is not cached");
            }
            String bad = INSTANCE.checkAudioFile(new File(strCacheFile));
            if (bad != null) {
                mp.release();
                throw new IllegalStateException(bad);
            }
            mp.setDataSource(strCacheFile);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda1
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    AudioEngine.togglePreview$lambda$19$lambda$18$lambda$15($p, $volume, mediaPlayer);
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda2
                @Override // android.media.MediaPlayer.OnCompletionListener
                public final void onCompletion(MediaPlayer mediaPlayer) {
                    AudioEngine.togglePreview$lambda$19$lambda$18$lambda$16($p, mediaPlayer);
                }
            });
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda3
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    return AudioEngine.togglePreview$lambda$19$lambda$18$lambda$17($p, mediaPlayer, i, i2);
                }
            });
            AudioEngine audioEngine2 = INSTANCE;
            previewPlayer = mp;
            mp.prepareAsync();
        } catch (Exception e) {
            $p.setState(PreviewState.ERROR);
            $p.setError(e.getMessage());
            INSTANCE.notifyPreview();
        }
    }

    static final void togglePreview$lambda$19$lambda$18$lambda$15(Preview $p, float $volume, MediaPlayer it) {
        try {
            togglePreview$prepared($p, $volume, it);
        } catch (Throwable e) {
            $p.setState(PreviewState.ERROR);
            $p.setError("This audio can't be played on this device");
            INSTANCE.notifyPreview();
        }
    }

    private static void togglePreview$prepared(Preview $p, float $volume, MediaPlayer it) {
        if (preview != $p || previewPlayer != it) {
            it.release();
            return;
        }
        $p.setLength(it.getDuration() / 1000.0f);
        float vol = RangesKt.coerceIn(2.0f * $volume, 0.05f, 1.0f);
        it.setVolume(vol, vol);
        it.start();
        $p.setState(PreviewState.PLAYING);
        INSTANCE.notifyPreview();
        AudioEngine audioEngine2 = INSTANCE;
        Intrinsics.checkNotNull(it);
        audioEngine2.tick($p, it);
    }

    static final void togglePreview$lambda$19$lambda$18$lambda$16(Preview $p, MediaPlayer it) {
        $p.setState(PreviewState.STOPPED);
        $p.setPosition(0.0f);
        INSTANCE.notifyPreview();
    }

    static final boolean togglePreview$lambda$19$lambda$18$lambda$17(Preview $p, MediaPlayer mediaPlayer, int i, int i2) {
        $p.setState(PreviewState.ERROR);
        $p.setError("This audio can't be played on this device");
        INSTANCE.notifyPreview();
        return true;
    }

    private final void tick(final Preview p, final MediaPlayer mp) {
        h.postDelayed(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine.tick.2
            @Override // java.lang.Runnable
            public void run() {
                if (AudioEngine.INSTANCE.getPreview() != p || AudioEngine.previewPlayer != mp || p.getState() != PreviewState.PLAYING) {
                    return;
                }
                try {
                    p.setPosition(mp.getCurrentPosition() / 1000.0f);
                    AudioEngine.INSTANCE.notifyPreview();
                    AudioEngine.h.postDelayed(this, 150L);
                } catch (Exception e) {
                }
            }
        }, 150L);
    }

    public final void pausePreview() {
        final Preview p = preview;
        if (p == null) {
            return;
        }
        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.pausePreview$lambda$20(p);
            }
        });
    }

    static final void pausePreview$lambda$20(Preview $p) {
        MediaPlayer mp = previewPlayer;
        if (mp == null) {
            return;
        }
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p || $p.getState() != PreviewState.PLAYING) {
            return;
        }
        try {
            mp.pause();
            $p.setPosition(mp.getCurrentPosition() / 1000.0f);
        } catch (Exception e) {
        }
        $p.setState(PreviewState.PAUSED);
        INSTANCE.notifyPreview();
    }

    public final void resumePreview() {
        final Preview p = preview;
        if (p == null) {
            return;
        }
        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.resumePreview$lambda$21(p);
            }
        });
    }

    static final void resumePreview$lambda$21(Preview $p) {
        MediaPlayer mp = previewPlayer;
        if (mp == null) {
            return;
        }
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p) {
            return;
        }
        try {
            if ($p.getState() == PreviewState.STOPPED) {
                mp.seekTo(0);
                $p.setPosition(0.0f);
            }
            mp.start();
            $p.setState(PreviewState.PLAYING);
            INSTANCE.notifyPreview();
            INSTANCE.tick($p, mp);
        } catch (Exception e) {
        }
    }

    public final void seekPreview(long id, final float seconds) {
        final Preview p = preview;
        if (p != null) {
            if (p.getId() != id) {
                return;
            }
            h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AudioEngine.seekPreview$lambda$23(p, seconds);
                }
            });
        }
    }

    static final void seekPreview$lambda$23(Preview $p, float $seconds) {
        MediaPlayer mp = previewPlayer;
        if (mp == null) {
            return;
        }
        AudioEngine audioEngine = INSTANCE;
        if (preview != $p || $p.getLength() <= 0.0f) {
            return;
        }
        float t = RangesKt.coerceIn($seconds, 0.0f, $p.getLength());
        try {
            mp.seekTo((int) (1000.0f * t));
            $p.setPosition(t);
            if ($p.getState() == PreviewState.STOPPED) {
                $p.setState(PreviewState.PAUSED);
            }
            INSTANCE.notifyPreview();
        } catch (Exception e) {
        }
    }

    public final void stopPreview() {
        Preview p = preview;
        preview = null;
        if (p != null) {
            p.setState(PreviewState.STOPPED);
        }
        h.post(new Runnable() { // from class: com.goydevv.studrblx.audio.AudioEngine$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                AudioEngine.stopPreview$lambda$24();
            }
        });
        notifyPreview();
    }

    static final void stopPreview$lambda$24() {
        try {
            MediaPlayer mediaPlayer = previewPlayer;
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        } catch (Exception e) {
        }
        AudioEngine audioEngine = INSTANCE;
        previewPlayer = null;
    }
}
