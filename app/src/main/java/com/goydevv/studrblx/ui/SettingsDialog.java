package com.goydevv.studrblx.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Theme;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.model.DataTypesKt;
import com.goydevv.studrblx.net.WebPermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: SettingsDialog.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class SettingsDialog {
    public static final SettingsDialog INSTANCE = new SettingsDialog();
    private static final List<Float> flySpeeds = CollectionsKt.listOf(new Float[]{Float.valueOf(0.1f), Float.valueOf(0.25f), Float.valueOf(0.5f), Float.valueOf(1.0f), Float.valueOf(2.0f), Float.valueOf(4.0f), Float.valueOf(8.0f), Float.valueOf(16.0f), Float.valueOf(32.0f), Float.valueOf(64.0f)});

    private SettingsDialog() {
    }

    public final List<Float> getFlySpeeds() {
        return flySpeeds;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v76, types: [T, android.app.AlertDialog] */
    /* JADX WARN: Type inference failed for: r1v60 */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r26v2 */
    /* JADX WARN: Type inference failed for: r26v3 */
    /* JADX WARN: Type inference failed for: r26v6 */
    /* JADX WARN: Type inference failed for: r7v31 */
    /* JADX WARN: Type inference failed for: r7v32 */
    /* JADX WARN: Type inference failed for: r7v33 */
    public final void show(final Context ctx, final MainActions actions, final Function0<Unit> onImportIcons, final Function0<Unit> onRemoveIcons) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Intrinsics.checkNotNullParameter(onImportIcons, "onImportIcons");
        final Ref.ObjectRef objectRef = new Ref.ObjectRef();
        final Ref.BooleanRef booleanRef = new Ref.BooleanRef();

        // Build 6 Roblox Studio category pages
        final String[] tabNames = new String[]{"Studio", "Rendering", "Camera", "Script Editor", "Playtest & Audio", "Viewport & UI"};
        final LinearLayout[] pages = new LinearLayout[tabNames.length];
        for (int pIdx = 0; pIdx < tabNames.length; pIdx++) {
            LinearLayout pg = new LinearLayout(ctx);
            pg.setOrientation(1);
            pg.setPadding(Ui.INSTANCE.dp(ctx, 14.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 14.0f), Ui.INSTANCE.dp(ctx, 14.0f));
            pg.setVisibility(pIdx == 0 ? View.VISIBLE : View.GONE);
            pages[pIdx] = pg;
        }

        // --- TAB 0: STUDIO (Appearance, Themes, Icons, Privacy) ---
        LinearLayout pgStudio = pages[0];
        section(ctx, pgStudio, "Appearance & Themes");
        List<Theme> listAllThemes = Settings.INSTANCE.allThemes();
        LinearLayout themeRow = new LinearLayout(ctx);
        themeRow.setOrientation(0);
        HorizontalScrollView hsv = new HorizontalScrollView(ctx);
        hsv.setHorizontalScrollBarEnabled(false);
        hsv.addView(themeRow);
        for (final Theme theme : listAllThemes) {
            themeRow.addView(themeChip(ctx, theme, Intrinsics.areEqual(theme.getName(), Settings.INSTANCE.getThemeName()), new Function0() {
                @Override
                public final Object invoke() {
                    return SettingsDialog.show$lambda$3(theme, booleanRef, objectRef);
                }
            }));
        }
        pgStudio.addView(hsv, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        LinearLayout themeBtns = new LinearLayout(ctx);
        themeBtns.setOrientation(0);
        themeBtns.setPadding(0, Ui.INSTANCE.dp(ctx, 6.0f), 0, 0);
        TextView newThemeBtn = Ui.button$default(Ui.INSTANCE, ctx, "New custom theme…", 0, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$5(objectRef, ctx, actions, (View) obj);
            }
        }, 4, null);
        LinearLayout.LayoutParams lpNew = Ui.INSTANCE.lp(0, -2, 1.0f);
        lpNew.rightMargin = Ui.INSTANCE.dp(ctx, 6.0f);
        themeBtns.addView(newThemeBtn, lpNew);
        boolean isCustom = false;
        for (Theme ct : Settings.INSTANCE.customThemes()) {
            if (Intrinsics.areEqual(ct.getName(), Settings.INSTANCE.getThemeName())) {
                isCustom = true;
                break;
            }
        }
        if (isCustom) {
            themeBtns.addView(Ui.button$default(Ui.INSTANCE, ctx, "Edit current…", 0, new Function1() {
                @Override
                public final Object invoke(Object obj) {
                    return SettingsDialog.show$lambda$8(objectRef, ctx, actions, (View) obj);
                }
            }, 4, null), Ui.INSTANCE.lp(0, -2, 1.0f));
        }
        pgStudio.addView(themeBtns);
        toggle(ctx, pgStudio, "Confirm before deleting", "Ask before deleting parts or scripts in Explorer.", Settings.INSTANCE.getConfirmDelete(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setConfirmDelete(((Boolean) obj).booleanValue());
                return Unit.INSTANCE;
            }
        });
        section(ctx, pgStudio, "Icons");
        TextView iconInfo = Ui.text$default(Ui.INSTANCE, ctx, Icons.INSTANCE.getInstalled() ? "Using your imported icon pack." : "Using the built-in icons.", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        iconInfo.setSingleLine(false);
        pgStudio.addView(iconInfo);
        LinearLayout iconBtns = new LinearLayout(ctx);
        iconBtns.setOrientation(0);
        iconBtns.setPadding(0, Ui.INSTANCE.dp(ctx, 6.0f), 0, 0);
        TextView impBtn = Ui.button$default(Ui.INSTANCE, ctx, "Import icon pack…", 0, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$39(objectRef, onImportIcons, (View) obj);
            }
        }, 4, null);
        LinearLayout.LayoutParams lpImp = Ui.INSTANCE.lp(0, -2, 1.0f);
        lpImp.rightMargin = Ui.INSTANCE.dp(ctx, 6.0f);
        iconBtns.addView(impBtn, lpImp);
        if (onRemoveIcons != null && Icons.INSTANCE.getInstalled()) {
            iconBtns.addView(Ui.button$default(Ui.INSTANCE, ctx, "Use built-in", 0, new Function1() {
                @Override
                public final Object invoke(Object obj) {
                    return SettingsDialog.show$lambda$41(objectRef, onRemoveIcons, (View) obj);
                }
            }, 4, null), Ui.INSTANCE.lp(0, -2, 1.0f));
        }
        pgStudio.addView(iconBtns);
        section(ctx, pgStudio, "Privacy & Web Permissions");
        TextView privText = Ui.text$default(Ui.INSTANCE, ctx, "Your Roblox sign-in and API key stay on this device and are only sent to Roblox servers over HTTPS.", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        privText.setSingleLine(false);
        pgStudio.addView(privText);
        Map<String, Boolean> mapRemembered = WebPermissions.INSTANCE.remembered(ctx);
        TextView webList = Ui.text$default(Ui.INSTANCE, ctx, mapRemembered.isEmpty() ? "No websites allowed or blocked yet." : CollectionsKt.joinToString(CollectionsKt.sortedWith(mapRemembered.entrySet(), new Comparator() {
            @Override
            public final int compare(Object t, Object t2) {
                return ComparisonsKt.compareValues((String) ((Map.Entry) t).getKey(), (String) ((Map.Entry) t2).getKey());
            }
        }), "\n", "", "", -1, "...", new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$33((Map.Entry) obj);
            }
        }), 12.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
        webList.setSingleLine(false);
        webList.setPadding(0, Ui.INSTANCE.dp(ctx, 6.0f), 0, 0);
        pgStudio.addView(webList);
        if (!mapRemembered.isEmpty()) {
            TextView forgetBtn = Ui.button$default(Ui.INSTANCE, ctx, "Forget website choices", 0, new Function1() {
                @Override
                public final Object invoke(Object obj) {
                    return SettingsDialog.show$lambda$35(ctx, objectRef, (View) obj);
                }
            }, 4, null);
            LinearLayout.LayoutParams lpF = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            lpF.topMargin = Ui.INSTANCE.dp(ctx, 6.0f);
            pgStudio.addView(forgetBtn, lpF);
        }

        // --- TAB 1: RENDERING (Editor Graphics, 3D Resolution Scale %, LOD, Shadows, PostFX) ---
        LinearLayout pgRender = pages[1];
        section(ctx, pgRender, "Editor Graphics Quality");
        slider(ctx, pgRender, "Editor Graphics Level (1–10)", 1.0f, 10.0f, (float) Settings.INSTANCE.getEditorGraphicsQuality(), 1.0f, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setEditorGraphicsQuality(Math.round(((Float) obj).floatValue()));
                return Unit.INSTANCE;
            }
        });
        TextView gfxNote = Ui.text$default(Ui.INSTANCE, ctx, "Applies only to the Studio Editor. Playtest graphics level is saved separately under Playtest & Audio.", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        gfxNote.setSingleLine(false);
        pgRender.addView(gfxNote);
        final List<Integer> resVals = CollectionsKt.listOf(new Integer[]{50, 60, 75, 85, 100});
        List<String> resLabels = CollectionsKt.listOf(new String[]{"50% (Fastest)", "60%", "75% (Balanced)", "85%", "100% (Native)"});
        choice(ctx, pgRender, "3D Viewport Resolution %", resLabels, "Scales internal 3D render resolution while keeping 2D UI crisp. Lowering to 75% or 60% boosts FPS on older GPUs.", RangesKt.coerceAtLeast(resVals.indexOf(Integer.valueOf(Settings.INSTANCE.getRenderScale())), 0), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setRenderScale(resVals.get(((Integer) obj).intValue()).intValue());
                return Unit.INSTANCE;
            }
        });
        final List<String> lodOpts = CollectionsKt.listOf(new String[]{"Automatic", "Low", "Medium", "High", "Ultra"});
        choice(ctx, pgRender, "Mesh & Part LOD Level", lodOpts, "Controls Level-of-Detail culling distance for small parts and complex meshes.", RangesKt.coerceAtLeast(lodOpts.indexOf(Settings.INSTANCE.getLodLevel()), 0), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setLodLevel(lodOpts.get(((Integer) obj).intValue()));
                return Unit.INSTANCE;
            }
        });
        final List<String> listListOf = CollectionsKt.listOf(new String[]{"Automatic", "Low", "Medium", "High", "Ultra"});
        choice(ctx, pgRender, "Render Distance", listListOf, "How far away chunks stay loaded on large maps.", RangesKt.coerceAtLeast(listListOf.indexOf(Settings.INSTANCE.getRenderDistance()), 0), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$20(listListOf, ((Integer) obj).intValue());
            }
        });
        final List<String> shOpts = CollectionsKt.listOf(new String[]{"Off", "Low", "Medium", "High"});
        choice(ctx, pgRender, "Editor Shadow Quality", shOpts, "Shadow quality for the Editor viewport.", RangesKt.coerceAtLeast(shOpts.indexOf(Settings.INSTANCE.getShadowQuality()), 0), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setShadowQuality(shOpts.get(((Integer) obj).intValue()));
                return Unit.INSTANCE;
            }
        });
        toggle(ctx, pgRender, "Post-Processing (Bloom & Blur)", "Enables multi-pass Gaussian BloomEffect and BlurEffect.", Settings.INSTANCE.getPostProcessing(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setPostProcessing(((Boolean) obj).booleanValue());
                return Unit.INSTANCE;
            }
        });
        choice(ctx, pgRender, "Materials", CollectionsKt.listOf(new String[]{"2022 (Modern)", "Pre-2022"}), "Default Roblox material texture set.", !Settings.INSTANCE.getModernMaterials() ? 1 : 0, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$19(((Integer) obj).intValue());
            }
        });
        toggle(ctx, pgRender, "Show FPS Counter", null, Settings.INSTANCE.getShowFps(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$18(((Boolean) obj).booleanValue());
            }
        });

        // --- TAB 2: CAMERA ---
        LinearLayout pgCam = pages[2];
        section(ctx, pgCam, "Editor Camera");
        int flyIdx = -1;
        for (int i2 = 0; i2 < flySpeeds.size(); i2++) {
            if (flySpeeds.get(i2).floatValue() == Settings.INSTANCE.getFlySpeed()) {
                flyIdx = i2;
                break;
            }
        }
        ArrayList<String> flyLabels = new ArrayList<>();
        for (Float fl : flySpeeds) {
            flyLabels.add("x" + DataTypesKt.fmtNum(fl.floatValue()));
        }
        choice(ctx, pgCam, "Fly speed", flyLabels, flyIdx < 0 ? "x" + DataTypesKt.fmtNum(Settings.INSTANCE.getFlySpeed()) : null, flyIdx, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$11(((Integer) obj).intValue());
            }
        });
        slider$default(this, ctx, pgCam, "Look sensitivity", 0.2f, 3.0f, Settings.INSTANCE.getCameraSensitivity(), 0.0f, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$12(((Float) obj).floatValue());
            }
        }, 64, null);
        toggle(ctx, pgCam, "Smooth camera movement", "Eases editor fly camera in and out.", Settings.INSTANCE.getCameraSmoothing(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$13(((Boolean) obj).booleanValue());
            }
        });

        // --- TAB 3: SCRIPT EDITOR ---
        LinearLayout pgScript = pages[3];
        section(ctx, pgScript, "Script Editor");
        slider(ctx, pgScript, "Font size", 9.0f, 24.0f, Settings.INSTANCE.getEditorFontSize(), 1.0f, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$14(((Float) obj).floatValue());
            }
        });
        toggle(ctx, pgScript, "Autocomplete", "Suggests keywords, globals, services, members, and names from your place.", Settings.INSTANCE.getAutocomplete(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$15(((Boolean) obj).booleanValue());
            }
        });
        toggle(ctx, pgScript, "Auto indent", null, Settings.INSTANCE.getAutoIndent(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$16(((Boolean) obj).booleanValue());
            }
        });
        toggle(ctx, pgScript, "Auto-close brackets and quotes", null, Settings.INSTANCE.getAutoCloseBrackets(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$17(((Boolean) obj).booleanValue());
            }
        });

        // --- TAB 4: PLAYTEST & AUDIO ---
        LinearLayout pgPlay = pages[4];
        section(ctx, pgPlay, "Playtest Graphics & Audio");
        slider(ctx, pgPlay, "Playtest Graphics Level (1–10)", 1.0f, 10.0f, (float) Settings.INSTANCE.getGraphicsQuality(), 1.0f, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setGraphicsQuality(Math.round(((Float) obj).floatValue()));
                return Unit.INSTANCE;
            }
        });
        slider(ctx, pgPlay, "Master Volume (0–10)", 0.0f, 10.0f, (float) Settings.INSTANCE.getVolume(), 1.0f, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setVolume(Math.round(((Float) obj).floatValue()));
                return Unit.INSTANCE;
            }
        });
        section(ctx, pgPlay, "Playtest Controls");
        toggle(ctx, pgPlay, "Hide windows while playtesting", "Gives the game the full screen; panels return when you stop.", Settings.INSTANCE.getHideWindowsInPlaytest(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$26(((Boolean) obj).booleanValue());
            }
        });
        toggle(ctx, pgPlay, "Shift Lock Switch", "Shows the shift lock toggle button on mobile.", Settings.INSTANCE.getShiftLockSwitch(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$27(((Boolean) obj).booleanValue());
            }
        });
        choice(ctx, pgPlay, "Camera mode", CollectionsKt.listOf(new String[]{"Classic", "Follow"}), null, !Intrinsics.areEqual(Settings.INSTANCE.getCameraMode(), "Classic") ? 1 : 0, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$28(((Integer) obj).intValue());
            }
        });
        choice(ctx, pgPlay, "Movement mode", CollectionsKt.listOf(new String[]{"Dynamic Thumbstick", "Thumbstick"}), null, Intrinsics.areEqual(Settings.INSTANCE.getMovementMode(), "Thumbstick") ? 1 : 0, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$29(((Integer) obj).intValue());
            }
        });
        slider$default(this, ctx, pgPlay, "Playtest camera sensitivity", 0.2f, 4.0f, Settings.INSTANCE.getPlayCameraSensitivity(), 0.0f, new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$30(((Float) obj).floatValue());
            }
        }, 64, null);

        // --- TAB 5: VIEWPORT & UI ---
        LinearLayout pgUi = pages[5];
        section(ctx, pgUi, "Viewport & GUI Editing");
        toggle(ctx, pgUi, "Smart guides when dragging GUIs", "Snaps GUI objects to edges and centers.", Settings.INSTANCE.getGuiSmartGuides(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$21(((Boolean) obj).booleanValue());
            }
        });
        final List listListOf2 = CollectionsKt.listOf(new Integer[]{0, 1, 2, 4, 5, 8, 10, 16});
        ArrayList<String> gridLabels = new ArrayList<>();
        for (Object o : listListOf2) {
            int gv = ((Number) o).intValue();
            gridLabels.add(gv == 0 ? "Off" : gv + " px");
        }
        choice(ctx, pgUi, "GUI drag grid", gridLabels, null, RangesKt.coerceAtLeast(listListOf2.indexOf(Integer.valueOf(Settings.INSTANCE.getGuiGrid())), 0), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$23(listListOf2, ((Integer) obj).intValue());
            }
        });
        final List<Float> scaleList = CollectionsKt.listOf(new Float[]{0.0f, 0.5f, 0.6f, 0.75f, 0.85f, 1.0f});
        ArrayList<String> scaleLabels = new ArrayList<>();
        for (Float fv : scaleList) {
            scaleLabels.add(fv.floatValue() == 0.0f ? "Auto (fit screen)" : ((int) (fv.floatValue() * 100.0f)) + "%");
        }
        choice(ctx, pgUi, "Plugin UI size", scaleLabels, null, RangesKt.coerceAtLeast(scaleList.indexOf(Float.valueOf(Settings.INSTANCE.getPluginUiScale())), 0), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                return SettingsDialog.show$lambda$25(scaleList, ((Integer) obj).intValue());
            }
        });
        toggle(ctx, pgUi, "Select parts inside Models", "Clicking in viewport selects child parts directly instead of the parent Model.", Settings.INSTANCE.getSelectInsideModels(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setSelectInsideModels(((Boolean) obj).booleanValue());
                return Unit.INSTANCE;
            }
        });
        toggle(ctx, pgUi, "Drag collisions", "Prevent moved parts from overlapping solid geometry.", Settings.INSTANCE.getDragCollisions(), new Function1() {
            @Override
            public final Object invoke(Object obj) {
                Settings.INSTANCE.setDragCollisions(((Boolean) obj).booleanValue());
                return Unit.INSTANCE;
            }
        });

        // Assemble Roblox Studio-style split-pane dialog (Left Tab Sidebar + Right Content ScrollView)
        LinearLayout splitRoot = new LinearLayout(ctx);
        splitRoot.setOrientation(0);
        splitRoot.setMinimumHeight(Ui.INSTANCE.dp(ctx, 360.0f));

        LinearLayout sidebar = new LinearLayout(ctx);
        sidebar.setOrientation(1);
        sidebar.setBackgroundColor(Ui.INSTANCE.getHEADER());
        sidebar.setPadding(0, Ui.INSTANCE.dp(ctx, 6.0f), 0, Ui.INSTANCE.dp(ctx, 6.0f));
        final TextView[] tabBtns = new TextView[tabNames.length];
        final ScrollView contentScroll = new ScrollView(ctx);
        LinearLayout contentHost = new LinearLayout(ctx);
        contentHost.setOrientation(1);
        for (int pIdx = 0; pIdx < pages.length; pIdx++) {
            contentHost.addView(pages[pIdx], Ui.INSTANCE.lp(-1, -2, 0.0f));
        }
        contentScroll.addView(contentHost, new android.widget.FrameLayout.LayoutParams(-1, -2));

        for (int tIdx = 0; tIdx < tabNames.length; tIdx++) {
            final int idx = tIdx;
            TextView tb = new TextView(ctx);
            tb.setText(tabNames[tIdx]);
            tb.setTextSize(12.0f);
            tb.setSingleLine(false);
            tb.setPadding(Ui.INSTANCE.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 10.0f), Ui.INSTANCE.dp(ctx, 8.0f), Ui.INSTANCE.dp(ctx, 10.0f));
            tb.setTextColor(tIdx == 0 ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.getTEXT());
            tb.setBackgroundColor(tIdx == 0 ? Ui.INSTANCE.getPANEL() : 0);
            tb.setTypeface(tIdx == 0 ? android.graphics.Typeface.DEFAULT_BOLD : android.graphics.Typeface.DEFAULT);
            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int k = 0; k < pages.length; k++) {
                        boolean sel = (k == idx);
                        pages[k].setVisibility(sel ? View.VISIBLE : View.GONE);
                        tabBtns[k].setTextColor(sel ? Ui.INSTANCE.getACCENT() : Ui.INSTANCE.getTEXT());
                        tabBtns[k].setBackgroundColor(sel ? Ui.INSTANCE.getPANEL() : 0);
                        tabBtns[k].setTypeface(sel ? android.graphics.Typeface.DEFAULT_BOLD : android.graphics.Typeface.DEFAULT);
                    }
                    contentScroll.scrollTo(0, 0);
                }
            });
            tabBtns[tIdx] = tb;
            sidebar.addView(tb, Ui.INSTANCE.lp(-1, -2, 0.0f));
        }
        ScrollView sideScroll = new ScrollView(ctx);
        sideScroll.setBackgroundColor(Ui.INSTANCE.getHEADER());
        sideScroll.addView(sidebar, new android.widget.FrameLayout.LayoutParams(-1, -2));
        splitRoot.addView(sideScroll, Ui.INSTANCE.lp(Ui.INSTANCE.dp(ctx, 114.0f), -1, 0.0f));

        View vDiv = new View(ctx);
        vDiv.setBackgroundColor(Ui.INSTANCE.getBORDER());
        splitRoot.addView(vDiv, Ui.INSTANCE.lp(Ui.INSTANCE.dp(ctx, 1.0f), -1, 0.0f));
        splitRoot.addView(contentScroll, Ui.INSTANCE.lp(0, -1, 1.0f));

        AlertDialog.Builder title = Dialogs.INSTANCE.themed(ctx).setTitle("Studio Settings");
        objectRef.element = title.setView(splitRoot).setPositiveButton("Done", (DialogInterface.OnClickListener) null).create();
        ((AlertDialog) objectRef.element).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public final void onDismiss(DialogInterface dialogInterface) {
                SettingsDialog.show$lambda$43(booleanRef, actions, dialogInterface);
            }
        });
        ((AlertDialog) objectRef.element).show();
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$lambda$3(Theme $t, Ref.BooleanRef $themeChanged, Ref.ObjectRef $dialog) {
        Settings.INSTANCE.setThemeName($t.getName());
        $themeChanged.element = true;
        AlertDialog alertDialog = (AlertDialog) $dialog.element;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$lambda$5(Ref.ObjectRef $dialog, Context $ctx, MainActions $actions, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AlertDialog alertDialog = (AlertDialog) $dialog.element;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        INSTANCE.editTheme($ctx, Theme.copy$default(Settings.INSTANCE.getTheme(), INSTANCE.uniqueName("My Theme"), false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8388606, null), true, $actions);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$lambda$8(Ref.ObjectRef $dialog, Context $ctx, MainActions $actions, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AlertDialog alertDialog = (AlertDialog) $dialog.element;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        INSTANCE.editTheme($ctx, Settings.INSTANCE.getTheme(), false, $actions);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$11(int i) {
        Settings settings = Settings.INSTANCE;
        SettingsDialog settingsDialog = INSTANCE;
        settings.setFlySpeed(flySpeeds.get(i).floatValue());
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$12(float it) {
        Settings.INSTANCE.setCameraSensitivity(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$13(boolean it) {
        Settings.INSTANCE.setCameraSmoothing(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$14(float it) {
        Settings.INSTANCE.setEditorFontSize(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$15(boolean it) {
        Settings.INSTANCE.setAutocomplete(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$16(boolean it) {
        Settings.INSTANCE.setAutoIndent(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$17(boolean it) {
        Settings.INSTANCE.setAutoCloseBrackets(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$18(boolean it) {
        Settings.INSTANCE.setShowFps(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$19(int it) {
        Settings.INSTANCE.setModernMaterials(it == 0);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$20(List $dists, int it) {
        Settings.INSTANCE.setRenderDistance((String) $dists.get(it));
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$21(boolean it) {
        Settings.INSTANCE.setGuiSmartGuides(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$23(List $grids, int it) {
        Settings.INSTANCE.setGuiGrid(((Number) $grids.get(it)).intValue());
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$25(List $scales, int it) {
        Settings.INSTANCE.setPluginUiScale(((Number) $scales.get(it)).floatValue());
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$26(boolean it) {
        Settings.INSTANCE.setHideWindowsInPlaytest(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$27(boolean it) {
        Settings.INSTANCE.setShiftLockSwitch(it);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$28(int it) {
        Settings.INSTANCE.setCameraMode(it == 0 ? "Classic" : "Follow");
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$29(int it) {
        Settings.INSTANCE.setMovementMode(it == 1 ? "Thumbstick" : "Dynamic Thumbstick");
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$30(float it) {
        Settings.INSTANCE.setPlayCameraSensitivity(it);
        return Unit.INSTANCE;
    }

    static final CharSequence show$lambda$33(Map.Entry entry) {
        Intrinsics.checkNotNullParameter(entry, "<destruct>");
        String h = (String) entry.getKey();
        boolean ok = ((Boolean) entry.getValue()).booleanValue();
        return (ok ? "Allowed: " : "Blocked: ") + h;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$lambda$35(Context $ctx, Ref.ObjectRef $dialog, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        WebPermissions.INSTANCE.forgetAll($ctx);
        AlertDialog alertDialog = (AlertDialog) $dialog.element;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$lambda$39(Ref.ObjectRef $dialog, Function0 $onImportIcons, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AlertDialog alertDialog = (AlertDialog) $dialog.element;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        $onImportIcons.invoke();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$lambda$41(Ref.ObjectRef $dialog, Function0 $onRemoveIcons, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AlertDialog alertDialog = (AlertDialog) $dialog.element;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        $onRemoveIcons.invoke();
        return Unit.INSTANCE;
    }

    static final void show$lambda$43(Ref.BooleanRef $themeChanged, MainActions $actions, DialogInterface it) {
        Settings.INSTANCE.changed();
        if ($themeChanged.element) {
            $actions.applyThemeChange();
        }
    }

    private final String uniqueName(String base) {
        Iterable iterableAllThemes = Settings.INSTANCE.allThemes();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableAllThemes, 10));
        Iterator it = iterableAllThemes.iterator();
        while (it.hasNext()) {
            arrayList.add(((Theme) it.next()).getName());
        }
        Set names = CollectionsKt.toSet((List) arrayList);
        if (!names.contains(base)) {
            return base;
        }
        int i = 2;
        while (names.contains(base + " " + i)) {
            i++;
        }
        return base + " " + i;
    }

    private final void section(Context ctx, LinearLayout box, String title) {
        Ui ui = Ui.INSTANCE;
        String upperCase = title.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        TextView textViewText = ui.text(ctx, upperCase, 11.0f, Ui.INSTANCE.getLIGHT() ? Ui.INSTANCE.getACCENT() : -10834689, true);
        textViewText.setPadding(0, Ui.INSTANCE.dp(ctx, 14.0f), 0, Ui.INSTANCE.dp(ctx, 4.0f));
        box.addView(textViewText);
    }

    private final View themeChip(final Context ctx, final Theme t, boolean selected, final Function0<Unit> onClick) {
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        linearLayout.setPadding(Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 6.0f), Ui.INSTANCE.dp(ctx, 4.0f));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0);
        gradientDrawable.setCornerRadius(Ui.INSTANCE.dp(ctx, 6.0f));
        gradientDrawable.setStroke(Ui.INSTANCE.dp(ctx, 2.0f), selected ? Ui.INSTANCE.getACCENT() : 0);
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda38
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                onClick.invoke();
            }
        });
        linearLayout.addView(new View(ctx) { // from class: com.goydevv.studrblx.ui.SettingsDialog$themeChip$preview$1
            @Override // android.view.View
            protected void onDraw(Canvas c) {
                Intrinsics.checkNotNullParameter(c, "c");
                Paint p = new Paint(1);
                float w = getWidth();
                float h = getHeight();
                float r = getResources().getDisplayMetrics().density * 4.0f;
                p.setColor(t.getBg());
                c.drawRoundRect(0.0f, 0.0f, w, h, r, r, p);
                p.setColor(t.getTopbar());
                c.drawRect(0.0f, 0.0f, w, h * 0.18f, p);
                p.setColor(t.getPanel());
                c.drawRect(0.68f * w, h * 0.18f, w, h, p);
                p.setColor(t.getSelect());
                c.drawRect(w * 0.7f, 0.36f * h, 0.98f * w, 0.48f * h, p);
                p.setColor(t.getText());
                c.drawRect(w * 0.72f, 0.58f * h, 0.94f * w, 0.62f * h, p);
                c.drawRect(w * 0.72f, h * 0.7f, 0.88f * w, 0.74f * h, p);
                p.setColor(t.getAccent());
                c.drawRect(0.06f * w, 0.05f * h, 0.2f * w, 0.13f * h, p);
                p.setStyle(Paint.Style.STROKE);
                p.setColor(t.getBorder());
                p.setStrokeWidth(1.0f);
                c.drawRoundRect(0.5f, 0.5f, w - 0.5f, h - 0.5f, r, r, p);
            }
        }, new LinearLayout.LayoutParams(Ui.INSTANCE.dp(ctx, 84.0f), Ui.INSTANCE.dp(ctx, 54.0f)));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, t.getName(), 11.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default.setGravity(17);
        textViewText$default.setPadding(0, Ui.INSTANCE.dp(ctx, 3.0f), 0, 0);
        linearLayout.addView(textViewText$default);
        return linearLayout;
    }

    private final LinearLayout row(Context ctx, LinearLayout box, String label, String hint) {
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(0, Ui.INSTANCE.dp(ctx, 5.0f), 0, Ui.INSTANCE.dp(ctx, 5.0f));
        LinearLayout linearLayout2 = new LinearLayout(ctx);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, ctx, label, 14.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default.setSingleLine(false);
        linearLayout2.addView(textViewText$default, Ui.INSTANCE.lp(0, -2, 1.0f));
        linearLayout.addView(linearLayout2);
        if (hint != null) {
            TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, ctx, hint, 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default2.setSingleLine(false);
            linearLayout.addView(textViewText$default2);
        }
        box.addView(linearLayout);
        return linearLayout2;
    }

    private final void toggle(Context ctx, LinearLayout box, String label, String hint, boolean value, final Function1<? super Boolean, Unit> onSet) {
        LinearLayout r = row(ctx, box, label, hint);
        Switch r1 = new Switch(ctx);
        r1.setChecked(value);
        r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda8
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SettingsDialog.toggle$lambda$55$lambda$54(onSet, compoundButton, z);
            }
        });
        r.addView(r1);
    }

    static final void toggle$lambda$55$lambda$54(Function1 $onSet, CompoundButton compoundButton, boolean b) {
        Intrinsics.checkNotNullParameter(compoundButton, "<unused var>");
        $onSet.invoke(Boolean.valueOf(b));
    }

    static /* synthetic */ void slider$default(SettingsDialog settingsDialog, Context context, LinearLayout linearLayout, String str, float f, float f2, float f3, float f4, Function1 function1, int i, Object obj) {
        settingsDialog.slider(context, linearLayout, str, f, f2, f3, (i & 64) != 0 ? 0.1f : f4, function1);
    }

    private final void slider(Context ctx, LinearLayout box, String label, final float min, float max, float value, final float step, final Function1<? super Float, Unit> onSet) {
        LinearLayout r = row(ctx, box, label, null);
        final TextView valueText = Ui.text$default(Ui.INSTANCE, ctx, DataTypesKt.fmtNum(value), 13.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        r.addView(valueText);
        int steps = (int) ((max - min) / step);
        SeekBar seekBar = new SeekBar(ctx);
        seekBar.setMax(steps);
        seekBar.setProgress(RangesKt.coerceIn((int) ((value - min) / step), 0, steps));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$slider$sb$1$1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar s, int p, boolean fromUser) {
                float v = min + (p * step);
                valueText.setText(DataTypesKt.fmtNum(v));
                if (fromUser) {
                    onSet.invoke(Float.valueOf(v));
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar s) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar s) {
            }
        });
        box.addView(seekBar, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
    }

    static /* synthetic */ void choice$default(SettingsDialog settingsDialog, Context context, LinearLayout linearLayout, String str, List list, String str2, int i, Function1 function1, int i2, Object obj) {
        if ((i2 & 32) != 0) {
            i = -1;
        }
        settingsDialog.choice(context, linearLayout, str, list, str2, i, function1);
    }

    private final void choice(final Context ctx, LinearLayout box, final String label, List<String> items, String customLabel, int current, final Function1<? super Integer, Unit> onSet) {
        List<String> list;
        String str;
        LinearLayout r = row(ctx, box, label, null);
        final Ref.IntRef idx = new Ref.IntRef();
        idx.element = current;
        Ui ui = Ui.INSTANCE;
        if (customLabel == null) {
            list = items;
            str = (String) CollectionsKt.getOrNull(list, idx.element);
            if (str == null && (str = (String) CollectionsKt.firstOrNull((List) list)) == null) {
                str = "";
            }
        } else {
            list = items;
            str = customLabel;
        }
        final TextView value = Ui.button$default(ui, ctx, str, 0, new Function1() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda35
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return SettingsDialog.choice$lambda$57((View) obj);
            }
        }, 4, null);
        final List<String> list2 = list;
        value.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda36
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Context context = ctx;
                String str2 = label;
                List list3 = list2;
                Ref.IntRef intRef = idx;
                Dialogs.INSTANCE.themed(context).setTitle(str2).setSingleChoiceItems((CharSequence[]) list3.toArray(new String[0]), intRef.element, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda6
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        SettingsDialog.choice$lambda$59$lambda$58(intRef, value, list3, onSet, dialogInterface, i);
                    }
                }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
            }
        });
        r.addView(value);
    }

    static final Unit choice$lambda$57(View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final void choice$lambda$59$lambda$58(Ref.IntRef $idx, TextView $value, List $items, Function1 $onSet, DialogInterface d, int which) {
        $idx.element = which;
        $value.setText((CharSequence) $items.get(which));
        $onSet.invoke(Integer.valueOf(which));
        d.dismiss();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void editTheme(Context ctx, Theme start, boolean isNew, final MainActions actions) {
        Context ctx2 = ctx;
        Intrinsics.checkNotNullParameter(ctx2, "ctx");
        Intrinsics.checkNotNullParameter(start, "start");
        Intrinsics.checkNotNullParameter(actions, "actions");
        final Ref.ObjectRef t = new Ref.ObjectRef();
        t.element = start;
        final String oldName = start.getName();
        LinearLayout linearLayout = new LinearLayout(ctx2);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(Ui.INSTANCE.dp(ctx2, 18.0f), Ui.INSTANCE.dp(ctx2, 6.0f), Ui.INSTANCE.dp(ctx2, 18.0f), Ui.INSTANCE.dp(ctx2, 8.0f));
        final EditText name = Ui.input$default(Ui.INSTANCE, ctx2, ((Theme) t.element).getName(), "Theme name", false, false, 24, null);
        linearLayout.addView(name);
        LinearLayout linearLayout2 = new LinearLayout(ctx2);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        linearLayout2.setPadding(0, Ui.INSTANCE.dp(ctx2, 6.0f), 0, Ui.INSTANCE.dp(ctx2, 6.0f));
        linearLayout2.addView(Ui.text$default(Ui.INSTANCE, ctx2, "Light theme (dark text, light dialogs)", 13.0f, 0, false, 24, null), Ui.INSTANCE.lp(0, -2, 1.0f));
        Switch r0 = new Switch(ctx2);
        r0.setChecked(((Theme) t.element).getLight());
        r0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SettingsDialog.editTheme$lambda$63$lambda$62(t, compoundButton, z);
            }
        });
        linearLayout2.addView(r0);
        linearLayout.addView(linearLayout2);
        LinearLayout linearLayout3 = new LinearLayout(ctx2);
        linearLayout3.setOrientation(0);
        final LinearLayout swatches = new LinearLayout(ctx2);
        swatches.setOrientation(1);
        for (final Theme base : Theme.INSTANCE.getBuiltIn()) {
            final Context context = ctx2;
            ctx2 = ctx;
            TextView textViewButton$default = Ui.button$default(Ui.INSTANCE, ctx2, "From " + base.getName(), 0, new Function1() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return SettingsDialog.editTheme$lambda$73(t, base, name, swatches, context, (View) obj);
                }
            }, 4, null);
            textViewButton$default.setTextSize(11.0f);
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -2, -2, 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = Ui.INSTANCE.dp(ctx2, 4.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout3.addView(textViewButton$default, layoutParamsLp$default);
        }
        LinearLayout swatches2 = swatches;
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(ctx2);
        horizontalScrollView.addView(linearLayout3);
        linearLayout.addView(horizontalScrollView);
        linearLayout.addView(swatches2);
        editTheme$refresh(swatches2, t, ctx2);
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(ctx2).setTitle(isNew ? "New Theme" : "Edit Theme");
        ScrollView scrollView = new ScrollView(ctx2);
        scrollView.addView(linearLayout);
        final Context context2 = ctx2;
        AlertDialog.Builder b = title.setView(scrollView).setPositiveButton("Save & use", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                SettingsDialog.editTheme$lambda$81(name, context2, t, actions, oldName, dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null);
        if (!isNew) {
            b.setNeutralButton("Delete", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    SettingsDialog.editTheme$lambda$83(oldName, actions, dialogInterface, i);
                }
            });
        }
        b.show();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [T, com.goydevv.studrblx.core.Theme] */
    static final void editTheme$lambda$63$lambda$62(Ref.ObjectRef $t, CompoundButton compoundButton, boolean b) {
        Intrinsics.checkNotNullParameter(compoundButton, "<unused var>");
        $t.element = Theme.copy$default((Theme) $t.element, null, b, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8388605, null);
    }

    private static final void editTheme$refresh(final LinearLayout swatches, Ref.ObjectRef<Theme> objectRef, Context $ctx) {
        final Context context = $ctx;
        swatches.removeAllViews();
        Ref.ObjectRef<Theme> objectRef2 = objectRef;
        for (Pair<String, Integer> pair : objectRef2.element.colors()) {
            final String slot = pair.component1();
            final int color = pair.component2().intValue();
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            linearLayout.setPadding(0, Ui.INSTANCE.dp(context, 4.0f), 0, Ui.INSTANCE.dp(context, 4.0f));
            linearLayout.setBackground(Ui.rippleBg$default(Ui.INSTANCE, context, 0, 0.0f, 4, null));
            View view = new View(context);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(color);
            gradientDrawable.setStroke(Ui.INSTANCE.dp(context, 1.0f), -7829368);
            gradientDrawable.setCornerRadius(Ui.INSTANCE.dp(context, 3.0f));
            view.setBackground(gradientDrawable);
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(context, 26.0f), Ui.INSTANCE.dp(context, 20.0f), 0.0f, 4, null);
            layoutParamsLp$default.rightMargin = Ui.INSTANCE.dp(context, 10.0f);
            Unit unit = Unit.INSTANCE;
            linearLayout.addView(view, layoutParamsLp$default);
            linearLayout.addView(Ui.text$default(Ui.INSTANCE, context, slot, 13.0f, 0, false, 24, null), Ui.INSTANCE.lp(0, -2, 1.0f));
            Ui ui = Ui.INSTANCE;
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String str = String.format("#%06X", Arrays.copyOf(new Object[]{Integer.valueOf(16777215 & color)}, 1));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            linearLayout.addView(Ui.text$default(ui, context, str, 12.0f, Ui.INSTANCE.getDIM(), false, 16, null));
            final Ref.ObjectRef<Theme> objectRef3 = objectRef2;
            linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda37
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    Context context2 = context;
                    String str2 = slot;
                    int i = color;
                    Dialogs.INSTANCE.colorPicker(context2, str2, new Color3(((i >> 16) & 255) / 255.0f, ((i >> 8) & 255) / 255.0f, (i & 255) / 255.0f), new Function1() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda7
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return SettingsDialog.editTheme$refresh$lambda$71$lambda$70(objectRef, str2, linearLayout, context2, obj);
                        }
                    });
                }
            });
            swatches.addView(linearLayout);
            objectRef2 = objectRef;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [T, com.goydevv.studrblx.core.Theme] */
    static final Unit editTheme$refresh$lambda$71$lambda$70(Ref.ObjectRef $t, String $slot, LinearLayout $swatches, Context $ctx, Object c) {
        Theme theme = (Theme) $t.element;
        Intrinsics.checkNotNull(c, "null cannot be cast to non-null type com.goydevv.studrblx.model.Color3");
        $t.element = theme.withColor($slot, ((Color3) c).toArgb() | ViewCompat.MEASURED_STATE_MASK);
        editTheme$refresh($swatches, $t, $ctx);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v5, types: [T, com.goydevv.studrblx.core.Theme] */
    static final Unit editTheme$lambda$73(Ref.ObjectRef $t, Theme $base, EditText $name, LinearLayout $swatches, Context $ctx, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String string = $name.getText().toString();
        if (StringsKt.isBlank(string)) {
            string = ((Theme) $t.element).getName();
        }
        $t.element = Theme.copy$default($base, string, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8388606, null);
        editTheme$refresh($swatches, $t, $ctx);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final void editTheme$lambda$81(EditText $name, Context $ctx, Ref.ObjectRef $t, MainActions $actions, String $oldName, DialogInterface dialogInterface, int i) {
        boolean z;
        String string = StringsKt.trim((CharSequence) $name.getText().toString()).toString();
        if (StringsKt.isBlank(string)) {
            string = "Custom";
        }
        String n = string;
        Iterable builtIn = Theme.INSTANCE.getBuiltIn();
        if (!(builtIn instanceof Collection) || !((Collection) builtIn).isEmpty()) {
            Iterator it = builtIn.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (Intrinsics.areEqual(((Theme) it.next()).getName(), n)) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
        } else {
            z = false;
        }
        if (z) {
            Toast.makeText($ctx, "\"" + n + "\" is a built-in theme name; pick another", 0).show();
            return;
        }
        Iterable iterableCustomThemes = Settings.INSTANCE.customThemes();
        Collection arrayList = new ArrayList();
        for (Object obj : iterableCustomThemes) {
            Theme theme = (Theme) obj;
            if ((Intrinsics.areEqual(theme.getName(), $oldName) || Intrinsics.areEqual(theme.getName(), n)) ? false : true) {
                arrayList.add(obj);
            }
        }
        List<Theme> mutableList = CollectionsKt.toMutableList(arrayList);
        mutableList.add(Theme.copy$default((Theme) $t.element, n, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8388606, null));
        Settings.INSTANCE.saveCustomThemes(mutableList);
        Settings.INSTANCE.setThemeName(n);
        $actions.applyThemeChange();
    }

    static final void editTheme$lambda$83(String $oldName, MainActions $actions, DialogInterface dialogInterface, int i) {
        Settings settings = Settings.INSTANCE;
        Iterable iterableCustomThemes = Settings.INSTANCE.customThemes();
        Collection arrayList = new ArrayList();
        for (Object obj : iterableCustomThemes) {
            if (!Intrinsics.areEqual(((Theme) obj).getName(), $oldName)) {
                arrayList.add(obj);
            }
        }
        settings.saveCustomThemes((List) arrayList);
        if (Intrinsics.areEqual(Settings.INSTANCE.getThemeName(), $oldName)) {
            Settings.INSTANCE.setThemeName("Dark");
        }
        $actions.applyThemeChange();
    }

    public final void pickFlySpeed(final Context ctx, final Function0<Unit> onSet) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(onSet, "onSet");
        Iterable iterable = flySpeeds;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add("x" + DataTypesKt.fmtNum(((Number) it.next()).floatValue()));
        }
        Collection labels = CollectionsKt.plus((Collection<? extends String>) arrayList, "Custom…");
        int cur = 0;
        Iterator<Float> it2 = flySpeeds.iterator();
        while (it2.hasNext()) {
            if (((Number) it2.next()).floatValue() == Settings.INSTANCE.getFlySpeed()) {
                Dialogs.INSTANCE.themed(ctx).setTitle("Fly speed").setSingleChoiceItems((CharSequence[]) labels.toArray(new String[0]), cur, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        SettingsDialog.pickFlySpeed$lambda$88(onSet, ctx, dialogInterface, i);
                    }
                }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
            }
            cur++;
        }
        cur = -1;
        Dialogs.INSTANCE.themed(ctx).setTitle("Fly speed").setSingleChoiceItems((CharSequence[]) labels.toArray(new String[0]), cur, new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                SettingsDialog.pickFlySpeed$lambda$88(onSet, ctx, dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    static final void pickFlySpeed$lambda$88(final Function0 $onSet, Context $ctx, DialogInterface d, int i) {
        d.dismiss();
        SettingsDialog settingsDialog = INSTANCE;
        if (i >= flySpeeds.size()) {
            Dialogs.INSTANCE.prompt($ctx, "Fly speed multiplier (0.05 – 200)", DataTypesKt.fmtNum(Settings.INSTANCE.getFlySpeed()), new Function1() { // from class: com.goydevv.studrblx.ui.SettingsDialog$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return SettingsDialog.pickFlySpeed$lambda$88$lambda$87($onSet, (String) obj);
                }
            });
            return;
        }
        Settings settings = Settings.INSTANCE;
        SettingsDialog settingsDialog2 = INSTANCE;
        settings.setFlySpeed(flySpeeds.get(i).floatValue());
        $onSet.invoke();
    }

    static final Unit pickFlySpeed$lambda$88$lambda$87(Function0 $onSet, String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        Float floatOrNull = StringsKt.toFloatOrNull(StringsKt.trim((CharSequence) s).toString());
        if (floatOrNull != null) {
            Settings.INSTANCE.setFlySpeed(RangesKt.coerceIn(floatOrNull.floatValue(), 0.05f, 200.0f));
            $onSet.invoke();
        }
        return Unit.INSTANCE;
    }

    private final TextView label(Context ctx, String s) {
        return Ui.text$default(Ui.INSTANCE, ctx, s, 13.0f, 0, false, 24, null);
    }
}
