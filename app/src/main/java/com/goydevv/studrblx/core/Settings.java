package com.goydevv.studrblx.core;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: Settings.kt */
/* JADX INFO: loaded from: classes4.dex */
public final class Settings {
    public static final Settings INSTANCE = new Settings();
    private static final CopyOnWriteArrayList<Function0<Unit>> listeners = new CopyOnWriteArrayList<>();
    private static SharedPreferences prefs;

    private Settings() {
    }

    public final CopyOnWriteArrayList<Function0<Unit>> getListeners() {
        return listeners;
    }

    public final void init(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        if (prefs == null) {
            prefs = ctx.getApplicationContext().getSharedPreferences("studio_settings", 0);
        }
    }

    private final SharedPreferences p() {
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences != null) {
            return sharedPreferences;
        }
        throw new IllegalStateException("Settings.init not called");
    }

    private final float f(String k, float d) {
        SharedPreferences sharedPreferences = prefs;
        return sharedPreferences != null ? sharedPreferences.getFloat(k, d) : d;
    }

    private final boolean b(String k, boolean d) {
        SharedPreferences sharedPreferences = prefs;
        return sharedPreferences != null ? sharedPreferences.getBoolean(k, d) : d;
    }

    private final String s(String k, String d) {
        String string;
        SharedPreferences sharedPreferences = prefs;
        return (sharedPreferences == null || (string = sharedPreferences.getString(k, d)) == null) ? d : string;
    }

    private final int i(String k, int d) {
        SharedPreferences sharedPreferences = prefs;
        return sharedPreferences != null ? sharedPreferences.getInt(k, d) : d;
    }

    public final void changed() {
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            ((Function0) it.next()).invoke();
        }
    }

    public final String getThemeName() {
        return s("theme", "High Contrast");
    }

    public final void setThemeName(String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        p().edit().putString("theme", v).apply();
    }

    public final List<Theme> customThemes() {
        String raw;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null || (raw = sharedPreferences.getString("customThemes", null)) == null) {
            return CollectionsKt.emptyList();
        }
        try {
            JSONArray jSONArray = new JSONArray(raw);
            Iterable iterableUntil = RangesKt.until(0, jSONArray.length());
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableUntil, 10));
            Iterator<Integer> it = iterableUntil.iterator();
            while (it.hasNext()) {
                int iNextInt = ((IntIterator) it).nextInt();
                Theme.Companion companion = Theme.INSTANCE;
                JSONObject jSONObject = jSONArray.getJSONObject(iNextInt);
                Intrinsics.checkNotNullExpressionValue(jSONObject, "getJSONObject(...)");
                arrayList.add(companion.fromJson(jSONObject));
            }
            return (List) arrayList;
        } catch (Exception e) {
            return CollectionsKt.emptyList();
        }
    }

    public final void saveCustomThemes(List<Theme> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        JSONArray a = new JSONArray();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            a.put(((Theme) it.next()).toJson());
        }
        p().edit().putString("customThemes", a.toString()).apply();
    }

    public final List<Theme> allThemes() {
        return CollectionsKt.plus((Collection) Theme.INSTANCE.getBuiltIn(), (Iterable) customThemes());
    }

    public final Theme getTheme() {
        Object next;
        Iterator it = allThemes().iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!Intrinsics.areEqual(((Theme) next).getName(), INSTANCE.getThemeName()));
        Theme theme = (Theme) next;
        return theme == null ? Theme.INSTANCE.getHIGH_CONTRAST() : theme;
    }

    public final float getFlySpeed() {
        return f("flySpeed", 1.0f);
    }

    public final void setFlySpeed(float v) {
        p().edit().putFloat("flySpeed", v).apply();
    }

    public final float getCameraSensitivity() {
        return f("camSens", 1.0f);
    }

    public final void setCameraSensitivity(float v) {
        p().edit().putFloat("camSens", v).apply();
    }

    public final boolean getCameraSmoothing() {
        return b("camSmooth", true);
    }

    public final void setCameraSmoothing(boolean v) {
        p().edit().putBoolean("camSmooth", v).apply();
    }

    public final float getPlayCameraSensitivity() {
        return f("playSens", 1.0f);
    }

    public final void setPlayCameraSensitivity(float v) {
        p().edit().putFloat("playSens", v).apply();
    }

    public final boolean getShiftLockSwitch() {
        return b("shiftLockSwitch", true);
    }

    public final void setShiftLockSwitch(boolean v) {
        p().edit().putBoolean("shiftLockSwitch", v).apply();
    }

    public final String getCameraMode() {
        return s("cameraMode", "Follow");
    }

    public final void setCameraMode(String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        p().edit().putString("cameraMode", v).apply();
    }

    public final String getMovementMode() {
        return s("movementMode", "Dynamic Thumbstick");
    }

    public final void setMovementMode(String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        p().edit().putString("movementMode", v).apply();
    }

    public final int getGraphicsQuality() {
        return i("gfx", 10);
    }

    public final void setGraphicsQuality(int v) {
        p().edit().putInt("gfx", v).apply();
    }

    public final int getVolume() {
        return i("volume", 10);
    }

    public final void setVolume(int v) {
        p().edit().putInt("volume", v).apply();
    }

    public final boolean getHideWindowsInPlaytest() {
        return b("hidePlay", false);
    }

    public final void setHideWindowsInPlaytest(boolean v) {
        p().edit().putBoolean("hidePlay", v).apply();
    }

    public final float getEditorFontSize() {
        return f("editorFont", 13.0f);
    }

    public final void setEditorFontSize(float v) {
        p().edit().putFloat("editorFont", v).apply();
    }

    public final boolean getAutocomplete() {
        return b("autocomplete", true);
    }

    public final void setAutocomplete(boolean v) {
        p().edit().putBoolean("autocomplete", v).apply();
    }

    public final boolean getAutoIndent() {
        return b("autoIndent", true);
    }

    public final void setAutoIndent(boolean v) {
        p().edit().putBoolean("autoIndent", v).apply();
    }

    public final boolean getAutoCloseBrackets() {
        return b("autoClose", true);
    }

    public final void setAutoCloseBrackets(boolean v) {
        p().edit().putBoolean("autoClose", v).apply();
    }

    public final boolean getModernMaterials() {
        return b("modernMaterials", true);
    }

    public final void setModernMaterials(boolean v) {
        p().edit().putBoolean("modernMaterials", v).apply();
        changed();
    }

    public final boolean getShowFps() {
        return b("showFps", true);
    }

    public final void setShowFps(boolean v) {
        p().edit().putBoolean("showFps", v).apply();
    }

    public final String getRenderDistance() {
        return s("renderDist", "Automatic");
    }

    public final void setRenderDistance(String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        p().edit().putString("renderDist", v).apply();
    }

    public final boolean getGuiSmartGuides() {
        return b("guiGuides", true);
    }

    public final void setGuiSmartGuides(boolean v) {
        p().edit().putBoolean("guiGuides", v).apply();
    }

    public final int getGuiGrid() {
        return i("guiGrid", 0);
    }

    public final void setGuiGrid(int v) {
        p().edit().putInt("guiGrid", v).apply();
    }

    public final boolean getConfirmDelete() {
        return b("confirmDel", false);
    }

    public final void setConfirmDelete(boolean v) {
        p().edit().putBoolean("confirmDel", v).apply();
    }

    public final float getMoveSnap() {
        return f("moveSnap", 1.0f);
    }

    public final void setMoveSnap(float v) {
        SharedPreferences.Editor editorEdit;
        SharedPreferences.Editor editorPutFloat;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null || (editorPutFloat = editorEdit.putFloat("moveSnap", v)) == null) {
            return;
        }
        editorPutFloat.apply();
    }

    public final float getRotSnap() {
        return f("rotSnap", 15.0f);
    }

    public final void setRotSnap(float v) {
        SharedPreferences.Editor editorEdit;
        SharedPreferences.Editor editorPutFloat;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null || (editorPutFloat = editorEdit.putFloat("rotSnap", v)) == null) {
            return;
        }
        editorPutFloat.apply();
    }

    public final boolean getMoveSnapOn() {
        return b("moveSnapOn", true);
    }

    public final void setMoveSnapOn(boolean v) {
        SharedPreferences.Editor editorEdit;
        SharedPreferences.Editor editorPutBoolean;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null || (editorPutBoolean = editorEdit.putBoolean("moveSnapOn", v)) == null) {
            return;
        }
        editorPutBoolean.apply();
    }

    public final boolean getRotSnapOn() {
        return b("rotSnapOn", true);
    }

    public final void setRotSnapOn(boolean v) {
        SharedPreferences.Editor editorEdit;
        SharedPreferences.Editor editorPutBoolean;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null || (editorPutBoolean = editorEdit.putBoolean("rotSnapOn", v)) == null) {
            return;
        }
        editorPutBoolean.apply();
    }

    public final boolean getSelectInsideModels() {
        return b("selInside", false);
    }

    public final void setSelectInsideModels(boolean v) {
        SharedPreferences.Editor editorEdit;
        SharedPreferences.Editor editorPutBoolean;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null || (editorPutBoolean = editorEdit.putBoolean("selInside", v)) == null) {
            return;
        }
        editorPutBoolean.apply();
    }

    public final boolean getDragCollisions() {
        return b("dragCollisions", false);
    }

    public final void setDragCollisions(boolean v) {
        SharedPreferences.Editor editorEdit;
        SharedPreferences.Editor editorPutBoolean;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null || (editorPutBoolean = editorEdit.putBoolean("dragCollisions", v)) == null) {
            return;
        }
        editorPutBoolean.apply();
    }

    public final float getPluginUiScale() {
        return f("pluginScale", 0.0f);
    }

    public final int getEditorGraphicsQuality() {
        return i("editorGfx", 8);
    }

    public final void setEditorGraphicsQuality(int v) {
        p().edit().putInt("editorGfx", RangesKt.coerceIn(v, 1, 10)).apply();
        changed();
    }

    public final int getRenderScale() {
        return i("renderScale", 100);
    }

    public final void setRenderScale(int v) {
        p().edit().putInt("renderScale", RangesKt.coerceIn(v, 50, 100)).apply();
        changed();
    }

    public final String getLodLevel() {
        return s("lodLevel", "Automatic");
    }

    public final void setLodLevel(String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        p().edit().putString("lodLevel", v).apply();
        changed();
    }

    public final String getShadowQuality() {
        return s("shadowQuality", "Medium");
    }

    public final void setShadowQuality(String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        p().edit().putString("shadowQuality", v).apply();
        changed();
    }

    public final boolean getPostProcessing() {
        return b("postFx", true);
    }

    public final void setPostProcessing(boolean v) {
        p().edit().putBoolean("postFx", v).apply();
        changed();
    }

    public final void setPluginUiScale(float v) {
        SharedPreferences.Editor editorEdit;
        SharedPreferences.Editor editorPutFloat;
        SharedPreferences sharedPreferences = prefs;
        if (sharedPreferences == null || (editorEdit = sharedPreferences.edit()) == null || (editorPutFloat = editorEdit.putFloat("pluginScale", v)) == null) {
            return;
        }
        editorPutFloat.apply();
    }
}
