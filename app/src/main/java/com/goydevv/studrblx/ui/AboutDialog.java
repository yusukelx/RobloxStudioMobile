package com.goydevv.studrblx.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AboutDialog.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class AboutDialog {
    public static final String DOWNLOAD_URL = "https://www.mediafire.com/folder/33xwlczg9fxs1/mobile+studio";
    public static final AboutDialog INSTANCE = new AboutDialog();
    public static final String YOUTUBE_URL = "https://www.youtube.com/@fiowwwys/featured";

    private AboutDialog() {
    }

    public final Pair<String, Long> version(Context ctx) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            long code = Build.VERSION.SDK_INT >= 28 ? pi.getLongVersionCode() : pi.versionCode;
            String str = pi.versionName;
            if (str == null) {
                str = "?";
            }
            return TuplesKt.to(str, Long.valueOf(code));
        } catch (Exception e) {
            return TuplesKt.to("?", 0L);
        }
    }

    public final void open(Activity act, String url) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(url, "url");
        try {
            act.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        } catch (Exception e) {
            Toast.makeText(act, "No browser found to open " + url, 1).show();
        }
    }

    private static final int show$dp(Activity $act, float v) {
        return Ui.INSTANCE.dp($act, v);
    }

    public final void show(Activity act) {
        Intrinsics.checkNotNullParameter(act, "act");
        Pair<String, Long> pairVersion = version(act);
        String ver = pairVersion.component1();
        long code = pairVersion.component2().longValue();
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(1);
        box.setPadding(show$dp(act, 20.0f), show$dp(act, 18.0f), show$dp(act, 20.0f), show$dp(act, 6.0f));
        LinearLayout linearLayout = new LinearLayout(act);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        ImageView imageView = new ImageView(act);
        imageView.setImageResource(com.goydevv.studrblx.R.mipmap.ic_launcher);
        imageView.setPadding(0, 0, 0, 0);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Ui.INSTANCE.blend(Ui.INSTANCE.getPANEL(), Ui.INSTANCE.getACCENT(), 0.22f));
        gradientDrawable.setCornerRadius(show$dp(act, 12.0f));
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, show$dp(act, 52.0f), show$dp(act, 52.0f), 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = show$dp(act, 14.0f);
        Unit unit = Unit.INSTANCE;
        linearLayout.addView(imageView, layoutParamsLp$default);
        LinearLayout linearLayout2 = new LinearLayout(act);
        linearLayout2.setOrientation(1);
        linearLayout2.addView(Ui.INSTANCE.text(act, "Roblox Studio", 20.0f, Ui.INSTANCE.getTEXT(), true));
        linearLayout2.addView(Ui.text$default(Ui.INSTANCE, act, "Version " + ver + "  (build " + code + ")", 13.0f, Ui.INSTANCE.getDIM(), false, 16, null));
        linearLayout.addView(linearLayout2, Ui.INSTANCE.lp(0, -2, 1.0f));
        box.addView(linearLayout);
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, act, "A Roblox-Studio-style editor for Android: open, build, script, playtest and publish your places on the go.", 13.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setPadding(0, show$dp(act, 14.0f), 0, show$dp(act, 4.0f));
        box.addView(textViewText$default);
        show$section(box, act, "Credits");
        show$linkButton(act, box, "Video", "Made by fiowwwys", "YouTube · youtube.com/@fiowwwys", YOUTUBE_URL);
        show$section(box, act, "Updates");
        show$linkButton(act, box, "Download", "Get the latest version", "Download from MediaFire", DOWNLOAD_URL);
        TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, act, "You have version " + ver + ". Check the download folder for newer builds.", 11.5f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default2.setSingleLine(false);
        textViewText$default2.setPadding(show$dp(act, 2.0f), show$dp(act, 2.0f), 0, 0);
        box.addView(textViewText$default2);
        show$section(box, act, "Tips");
        TextView textViewText$default3 = Ui.text$default(Ui.INSTANCE, act, "• Windows: drag a panel's header to dock it left, right or under the viewport, or let it float. Double-tap a header to float/dock it.\n• Controls: left stick to fly/walk, drag to look, pinch to zoom/move, tap to select. Long-press Explorer items to drag them into the Toolbox or another parent.\n• File → Playtest Avatar picks who you play as (your avatar, R6/R15, or any model you save).", 12.0f, Ui.INSTANCE.getTEXT(), false, 16, null);
        textViewText$default3.setSingleLine(false);
        box.addView(textViewText$default3);
        show$section(box, act, "Acknowledgements");
        TextView textViewText$default4 = Ui.text$default(Ui.INSTANCE, act, "Icons are drawn by this app. Fonts: Montserrat, Source Sans 3, Fredoka One and other SIL Open Font License fonts. Toolbox content comes from the Roblox Creator Store. Not affiliated with Roblox Corporation.", 11.5f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default4.setSingleLine(false);
        textViewText$default4.setPadding(0, 0, 0, show$dp(act, 6.0f));
        box.addView(textViewText$default4);
        AlertDialog.Builder builderThemed = Dialogs.INSTANCE.themed(act);
        ScrollView scrollView = new ScrollView(act);
        scrollView.addView(box);
        builderThemed.setView(scrollView).setPositiveButton("Close", (DialogInterface.OnClickListener) null).show();
    }

    private static final void show$section(LinearLayout box, Activity $act, String title) {
        String upperCase = title.toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        TextView textViewText = Ui.INSTANCE.text($act, upperCase, 11.0f, Ui.INSTANCE.getDIM(), true);
        textViewText.setLetterSpacing(0.08f);
        textViewText.setPadding(0, show$dp($act, 16.0f), 0, show$dp($act, 6.0f));
        box.addView(textViewText);
    }

    private static final void show$linkButton(final Activity $act, LinearLayout box, String icon, String label, String sub, final String url) {
        LinearLayout row = new LinearLayout($act);
        row.setOrientation(0);
        row.setGravity(16);
        row.setPadding(show$dp($act, 10.0f), show$dp($act, 9.0f), show$dp($act, 12.0f), show$dp($act, 9.0f));
        row.setBackground(Ui.INSTANCE.rippleBg($act, Ui.INSTANCE.getCARD(), 10.0f));
        row.setClickable(true);
        row.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.AboutDialog$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AboutDialog.INSTANCE.open($act, url);
            }
        });
        ImageView imageView = new ImageView($act);
        imageView.setImageDrawable(Icons.general$default(Icons.INSTANCE, $act, icon, null, 4, null));
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, show$dp($act, 26.0f), show$dp($act, 26.0f), 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = show$dp($act, 12.0f);
        Unit unit = Unit.INSTANCE;
        row.addView(imageView, layoutParamsLp$default);
        LinearLayout t = new LinearLayout($act);
        t.setOrientation(1);
        t.addView(Ui.INSTANCE.text($act, label, 14.0f, Ui.INSTANCE.getTEXT(), true));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, $act, sub, 11.5f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setEllipsize(TextUtils.TruncateAt.END);
        t.addView(textViewText$default);
        row.addView(t, Ui.INSTANCE.lp(0, -2, 1.0f));
        row.addView(Ui.text$default(Ui.INSTANCE, $act, "›", 20.0f, Ui.INSTANCE.getDIM(), false, 16, null));
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default2.bottomMargin = show$dp($act, 6.0f);
        Unit unit2 = Unit.INSTANCE;
        box.addView(row, layoutParamsLp$default2);
    }
}
