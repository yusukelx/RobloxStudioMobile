package com.goydevv.studrblx.ui;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Recents;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.engine.Avatar;
import com.goydevv.studrblx.format.RbxFile;
import com.goydevv.studrblx.model.Color3;
import com.goydevv.studrblx.net.Accounts;
import com.goydevv.studrblx.net.AuthRequiredException;
import com.goydevv.studrblx.net.CloudPlace;
import com.goydevv.studrblx.net.Http;
import com.goydevv.studrblx.net.HttpException;
import com.goydevv.studrblx.net.ImageLoader;
import com.goydevv.studrblx.net.PublishException;
import com.goydevv.studrblx.net.RobloxApi;
import com.goydevv.studrblx.net.RobloxCloud;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: AccountUi.kt */
/* JADX INFO: loaded from: classes5.dex */
public final class AccountUi {
    private static final SimpleDateFormat dateIn;
    private static final SimpleDateFormat dateOut;
    public static final AccountUi INSTANCE = new AccountUi();
    private static final ExecutorService net = Executors.newSingleThreadExecutor();
    private static final Handler main = new Handler(Looper.getMainLooper());

    private AccountUi() {
    }

    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateIn = simpleDateFormat;
        dateOut = new SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.getDefault());
    }

    public final <T> void background(Activity act, String message, Function0<? extends T> work, Function1<? super T, Unit> done) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(message, "message");
        Intrinsics.checkNotNullParameter(work, "work");
        Intrinsics.checkNotNullParameter(done, "done");
        background(act, message, work, done, null);
    }

    public final <T> void background(final Activity act, String message, final Function0<? extends T> work, final Function1<? super T, Unit> done, final Function1<? super Throwable, Boolean> failed) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(message, "message");
        Intrinsics.checkNotNullParameter(work, "work");
        Intrinsics.checkNotNullParameter(done, "done");
        LinearLayout linearLayout = new LinearLayout(act);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        linearLayout.setPadding(Ui.INSTANCE.dp(act, 20.0f), Ui.INSTANCE.dp(act, 18.0f), Ui.INSTANCE.dp(act, 20.0f), Ui.INSTANCE.dp(act, 18.0f));
        ProgressBar progressBar = new ProgressBar(act);
        progressBar.setIndeterminate(true);
        linearLayout.addView(progressBar, Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(act, 32.0f), Ui.INSTANCE.dp(act, 32.0f), 0.0f, 4, null));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, act, message, 14.0f, 0, false, 24, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setPadding(Ui.INSTANCE.dp(act, 14.0f), 0, 0, 0);
        linearLayout.addView(textViewText$default);
        final Ref.BooleanRef cancelled = new Ref.BooleanRef();
        final AlertDialog dlg = Dialogs.INSTANCE.themed(act).setView(linearLayout).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda84
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                cancelled.element = true;
            }
        }).create();
        dlg.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda85
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                cancelled.element = true;
            }
        });
        if (!alive(act)) return;
        dlg.show();
        net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda86
            @Override // java.lang.Runnable
            public final void run() {
                AccountUi.background$lambda$7(work, act, dlg, cancelled, done, failed);
            }
        });
    }

    static final void background$lambda$7(Function0 $work, final Activity $act, final AlertDialog $dlg, final Ref.BooleanRef $cancelled, final Function1 $done, final Function1 $failed) {
        Object objM457constructorimpl;
        try {
            objM457constructorimpl = $work.invoke();
        } catch (Throwable e) {
            objM457constructorimpl = ResultKt.createFailure(e);
        }
        final Object result = objM457constructorimpl;
        main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda74
            @Override // java.lang.Runnable
            public final void run() {
                AccountUi.background$lambda$7$lambda$6($act, $dlg, $cancelled, result, $done, $failed);
            }
        });
    }

    static boolean alive(Activity a) {
        return a != null && !a.isFinishing() && !a.isDestroyed();
    }

    static void safeDismiss(android.content.DialogInterface d) {
        if (d == null) return;
        try {
            if (d instanceof Dialog) {
                Dialog dd = (Dialog) d;
                if (!dd.isShowing()) return;
                android.view.Window w = dd.getWindow();
                if (w == null || !w.getDecorView().isAttachedToWindow()) return;
            }
            d.dismiss();
        } catch (Throwable ignored) {
        }
    }

    static final void background$lambda$7$lambda$6(Activity $act, AlertDialog $dlg, Ref.BooleanRef $cancelled, Object $result, Function1 $done, Function1 $failed) {
        if (!alive($act)) {
            safeDismiss($dlg);
            return;
        }
        try {
            safeDismiss($dlg);
        } catch (Exception e) {
        }
        if ($cancelled.element) {
            return;
        }
        Throwable thM460exceptionOrNullimpl = (($result) instanceof Result.Failure ? ((Result.Failure) ($result)).exception : null);
        if (thM460exceptionOrNullimpl != null) {
            boolean z = false;
            if ($failed != null && ((Boolean) $failed.invoke(thM460exceptionOrNullimpl)).booleanValue()) {
                z = true;
            }
            if (!z) {
                INSTANCE.showError($act, thM460exceptionOrNullimpl);
                return;
            }
            return;
        }
        $done.invoke($result);
    }

    private final void showError(final Activity act, Throwable e) {
        if (e instanceof AuthRequiredException) {
            Dialogs.INSTANCE.themed(act).setTitle("Sign in needed").setMessage(e.getMessage()).setPositiveButton("Sign in", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda12
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    AccountUi.signIn$default(AccountUi.INSTANCE, act, null, 2, null);
                }
            }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
            return;
        }
        OutputLog.INSTANCE.error("Roblox: " + e.getMessage());
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(act).setTitle("Something went wrong");
        String message = e.getMessage();
        if (message == null) {
            message = e.toString();
        }
        title.setMessage(message).setPositiveButton("OK", (DialogInterface.OnClickListener) null).show();
    }

    private final void toast(Activity act, String s) {
        Toast.makeText(act, s, 0).show();
    }

    public final boolean signedIn(Activity act) {
        Intrinsics.checkNotNullParameter(act, "act");
        return RobloxApi.INSTANCE.cookie(act).length() > 0;
    }

    public final String prettyDate(String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        try {
            SimpleDateFormat simpleDateFormat = dateOut;
            Date date = dateIn.parse(StringsKt.take(s, 19));
            Intrinsics.checkNotNull(date);
            String str = simpleDateFormat.format(date);
            Intrinsics.checkNotNull(str);
            return str;
        } catch (Exception e) {
            return s;
        }
    }

    public final void loadHeadshot(final ImageView img, final long userId) {
        Intrinsics.checkNotNullParameter(img, "img");
        Icons icons = Icons.INSTANCE;
        Context context = img.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        img.setImageDrawable(Icons.general$default(icons, context, "Account", null, 4, null));
        if (userId == 0) {
            return;
        }
        net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                AccountUi.loadHeadshot$lambda$13(userId, img);
            }
        });
    }

    static final void loadHeadshot$lambda$13(final long $userId, final ImageView $img) {
        String url = RobloxCloud.INSTANCE.headshot($userId);
        if (url == null) {
            return;
        }
        final String key = "url:" + url;
        $img.setTag(Long.valueOf($userId));
        loadHeadshot$lambda$13$apply(key, $img, $userId);
        ImageLoader.INSTANCE.getListeners().add(new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.loadHeadshot$lambda$13$lambda$12(key, $img, $userId, (String) obj);
            }
        });
    }

    private static final void loadHeadshot$lambda$13$apply(String key, final ImageView $img, final long $userId) {
        final Bitmap key2 = ImageLoader.INSTANCE.getKey(key);
        if (key2 != null) {
            main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    AccountUi.loadHeadshot$lambda$13$apply$lambda$11$lambda$10($img, $userId, key2);
                }
            });
        }
    }

    static final void loadHeadshot$lambda$13$apply$lambda$11$lambda$10(ImageView $img, long $userId, Bitmap $b) {
        if (Intrinsics.areEqual($img.getTag(), Long.valueOf($userId))) {
            $img.setImageBitmap($b);
        }
    }

    static final Unit loadHeadshot$lambda$13$lambda$12(String $key, ImageView $img, long $userId, String k) {
        Intrinsics.checkNotNullParameter(k, "k");
        if (Intrinsics.areEqual(k, $key)) {
            loadHeadshot$lambda$13$apply($key, $img, $userId);
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Type inference failed for: r6v3, types: [T, android.app.AlertDialog] */
    public final void show(final Activity act, final Studio studio, final MainActions actions) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(1);
        box.setPadding(Ui.INSTANCE.dp(act, 16.0f), Ui.INSTANCE.dp(act, 8.0f), Ui.INSTANCE.dp(act, 16.0f), Ui.INSTANCE.dp(act, 4.0f));
        final Ref.ObjectRef dlg = new Ref.ObjectRef();
        Accounts.Account active = Accounts.INSTANCE.active(act);
        box.addView(show$accountRow(act, active, true, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda58
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return Unit.INSTANCE;
            }
        }));
        Iterable list = Accounts.INSTANCE.list(act);
        Collection arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            Accounts.Account account = (Accounts.Account) next;
            if (active != null && account.getUserId() == active.getUserId()) {
                z = true;
            }
            if (!z) {
                arrayList.add(next);
            }
        }
        for (Object _aObj : (List) arrayList) {
            final Accounts.Account a = (Accounts.Account) _aObj;
            View viewShow$accountRow = show$accountRow(act, a, false, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda59
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AccountUi.show$lambda$26(dlg, act, a);
                }
            });
            LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default.topMargin = Ui.INSTANCE.dp(act, 4.0f);
            Unit unit = Unit.INSTANCE;
            box.addView(viewShow$accountRow, layoutParamsLp$default);
        }
        if (active != null) {
            View viewShow$accountRow2 = show$accountRow(act, null, false, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda60
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AccountUi.show$lambda$28(dlg, act);
                }
            });
            LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
            layoutParamsLp$default2.topMargin = Ui.INSTANCE.dp(act, 4.0f);
            Unit unit2 = Unit.INSTANCE;
            box.addView(viewShow$accountRow2, layoutParamsLp$default2);
        }
        View viewDivider$default = Ui.divider$default(Ui.INSTANCE, act, false, 2, null);
        LinearLayout.LayoutParams layoutParamsLp$default3 = Ui.lp$default(Ui.INSTANCE, -1, Ui.INSTANCE.dp(act, 1.0f), 0.0f, 4, null);
        layoutParamsLp$default3.topMargin = Ui.INSTANCE.dp(act, 10.0f);
        layoutParamsLp$default3.bottomMargin = Ui.INSTANCE.dp(act, 4.0f);
        Unit unit3 = Unit.INSTANCE;
        box.addView(viewDivider$default, layoutParamsLp$default3);
        show$action(box, act, dlg, active == null ? "Sign in with Roblox…" : "Add another account…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda61
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.show$lambda$34(act);
            }
        });
        show$action(box, act, dlg, "Playtest avatar: " + Avatar.INSTANCE.describe(act) + "…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda62
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.show$lambda$35(act, studio);
            }
        });
        if (active != null) {
            show$action(box, act, dlg, "My Games…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda63
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AccountUi.show$lambda$36(act, studio, actions);
                }
            });
            show$action(box, act, dlg, "Sign out of @" + active.getName(), new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda64
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AccountUi.show$lambda$37(act);
                }
            });
        }
        show$action(box, act, dlg, "Open Cloud API key (for publishing)…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda65
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.show$lambda$38(act);
            }
        });
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, act, "Sign-ins are stored only in this app's private storage on this device.", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setPadding(0, Ui.INSTANCE.dp(act, 10.0f), 0, 0);
        box.addView(textViewText$default);
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(act).setTitle("Roblox Account");
        ScrollView scrollView = new ScrollView(act);
        scrollView.addView(box);
        Unit unit4 = Unit.INSTANCE;
        dlg.element = title.setView(scrollView).setPositiveButton("Close", (DialogInterface.OnClickListener) null).show();
        if (active != null) {
            if (active.getName().length() == 0) {
                net.execute(new Runnable() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda66
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            RobloxCloud.INSTANCE.currentUser(act);
                        } catch (Exception _e) {
                            if (_e instanceof RuntimeException) throw (RuntimeException) _e;
                            throw new RuntimeException(_e);
                        }
                    }
                });
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:22:0x00fd  */
    private static final View show$accountRow(Activity $act, Accounts.Account a, boolean current, final Function0<Unit> function0) {
        String str;
        String str2;
        String displayName;
        LinearLayout row = new LinearLayout($act);
        row.setOrientation(0);
        row.setGravity(16);
        row.setPadding(Ui.INSTANCE.dp($act, 6.0f), Ui.INSTANCE.dp($act, 6.0f), Ui.INSTANCE.dp($act, 6.0f), Ui.INSTANCE.dp($act, 6.0f));
        Ui ui = Ui.INSTANCE;
        Activity activity = $act;
        Ui ui2 = Ui.INSTANCE;
        row.setBackground(ui.rippleBg(activity, current ? ui2.blend(Ui.INSTANCE.getPANEL(), Ui.INSTANCE.getACCENT(), 0.18f) : ui2.getPANEL(), 6.0f));
        row.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda104
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                function0.invoke();
            }
        });
        ImageView img = new ImageView($act);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp($act, 40.0f), Ui.INSTANCE.dp($act, 40.0f), 0.0f, 4, null);
        layoutParamsLp$default.rightMargin = Ui.INSTANCE.dp($act, 12.0f);
        Unit unit = Unit.INSTANCE;
        row.addView(img, layoutParamsLp$default);
        if (a != null) {
            INSTANCE.loadHeadshot(img, a.getUserId());
        } else {
            img.setImageDrawable(Icons.general$default(Icons.INSTANCE, $act, "Account", null, 4, null));
        }
        LinearLayout texts = new LinearLayout($act);
        boolean z = true;
        texts.setOrientation(1);
        Ui ui3 = Ui.INSTANCE;
        Activity activity2 = $act;
        if (a == null || (displayName = a.getDisplayName()) == null) {
            str = "Guest";
        } else {
            String name = displayName;
            if (name.length() != 0) {
                z = false;
            }
            if (z) {
                name = a.getName();
            }
            str = name;
            if (str == null) {
                str = "Guest";
            }
        }
        texts.addView(ui3.text(activity2, str, 15.0f, Ui.INSTANCE.getTEXT(), true));
        Ui ui4 = Ui.INSTANCE;
        Activity activity3 = $act;
        if (a != null) {
            str2 = "@" + a.getName() + (current ? "  ·  signed in" : "");
        } else {
            str2 = current ? "Not signed in" : "Use without an account";
        }
        texts.addView(Ui.text$default(ui4, activity3, str2, 12.0f, Ui.INSTANCE.getDIM(), false, 16, null));
        row.addView(texts, Ui.INSTANCE.lp(0, -2, 1.0f));
        if (current) {
            row.addView(Ui.INSTANCE.text($act, "✓", 16.0f, Ui.INSTANCE.getACCENT(), true));
        }
        return row;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$lambda$26(Ref.ObjectRef $dlg, final Activity $act, final Accounts.Account $a) {
        AlertDialog alertDialog = (AlertDialog) $dlg.element;
        if (alertDialog != null) {
            safeDismiss(alertDialog);
        }
        Accounts.INSTANCE.switchTo($act, $a);
        Avatar.INSTANCE.refresh($act);
        INSTANCE.toast($act, "Switched to @" + $a.getName());
        INSTANCE.background($act, "Checking @" + $a.getName() + "…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda91
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                try { return RobloxCloud.INSTANCE.currentUser($act); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda92
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.show$lambda$26$lambda$23((RobloxCloud.User) obj);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda93
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(AccountUi.show$lambda$26$lambda$25($act, $a, (Throwable) obj));
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$26$lambda$23(RobloxCloud.User it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final boolean show$lambda$26$lambda$25(final Activity $act, Accounts.Account $a, Throwable e) {
        Intrinsics.checkNotNullParameter(e, "e");
        if (!(e instanceof AuthRequiredException)) {
            return true;
        }
        Dialogs.INSTANCE.themed($act).setTitle("Sign in again").setMessage("The saved sign-in for @" + $a.getName() + " has expired.").setPositiveButton("Sign in", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda89
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AccountUi.signIn$default(AccountUi.INSTANCE, $act, null, 2, null);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$lambda$28(Ref.ObjectRef $dlg, Activity $act) {
        AlertDialog alertDialog = (AlertDialog) $dlg.element;
        if (alertDialog != null) {
            safeDismiss(alertDialog);
        }
        Accounts.INSTANCE.useGuest($act);
        Avatar.INSTANCE.refresh($act);
        INSTANCE.toast($act, "Using Roblox Studio as a guest");
        return Unit.INSTANCE;
    }

    private static final void show$action(LinearLayout box, Activity $act, final Ref.ObjectRef<AlertDialog> objectRef, String label, final Function0<Unit> function0) {
        TextView textViewButton$default = Ui.button$default(Ui.INSTANCE, $act, label, 0, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda34
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.show$action$lambda$30(objectRef, function0, (View) obj);
            }
        }, 4, null);
        textViewButton$default.setGravity(8388627);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default.topMargin = Ui.INSTANCE.dp($act, 6.0f);
        Unit unit = Unit.INSTANCE;
        box.addView(textViewButton$default, layoutParamsLp$default);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final Unit show$action$lambda$30(Ref.ObjectRef $dlg, Function0 $fn, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        AlertDialog alertDialog = (AlertDialog) $dlg.element;
        if (alertDialog != null) {
            safeDismiss(alertDialog);
        }
        $fn.invoke();
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$34(Activity $act) {
        signIn$default(INSTANCE, $act, null, 2, null);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$35(Activity $act, Studio $studio) {
        INSTANCE.avatarDialog($act, $studio);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$36(Activity $act, Studio $studio, MainActions $actions) {
        myGames$default(INSTANCE, $act, $studio, $actions, null, 8, null);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$37(Activity $act) {
        INSTANCE.signOut($act);
        return Unit.INSTANCE;
    }

    static final Unit show$lambda$38(Activity $act) {
        advanced$default(INSTANCE, $act, null, 2, null);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void advanced$default(AccountUi accountUi, Activity activity, Function0 function0, int i, Object obj) {
        if ((i & 2) != 0) {
            function0 = null;
        }
        accountUi.advanced(activity, function0);
    }

    public final void advanced(final Activity act, final Function0<Unit> then) {
        Intrinsics.checkNotNullParameter(act, "act");
        LinearLayout box = new LinearLayout(act);
        box.setOrientation(1);
        box.setPadding(Ui.INSTANCE.dp(act, 18.0f), Ui.INSTANCE.dp(act, 8.0f), Ui.INSTANCE.dp(act, 18.0f), 0);
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, act, "Roblox now only accepts place uploads (Publish / Save to Roblox) through Open Cloud. Create a key once:\n\n1. Open create.roblox.com → Open Cloud → API Keys → Create API Key.\n2. Under Access Permissions add \"universe-places\" (Write) and pick your experiences (or allow all). Add \"legacy-asset\" read access too if you want key-based downloads.\n3. Add IP address 0.0.0.0/0 (or leave restrictions off), save, and copy the key here.", 12.0f, 0, false, 24, null);
        textViewText$default.setSingleLine(false);
        box.addView(textViewText$default);
        TextView textViewButton$default = Ui.button$default(Ui.INSTANCE, act, "Open the API Keys page", 0, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.advanced$lambda$44(act, (View) obj);
            }
        }, 4, null);
        LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default.topMargin = Ui.INSTANCE.dp(act, 8.0f);
        Unit unit = Unit.INSTANCE;
        box.addView(textViewButton$default, layoutParamsLp$default);
        final EditText editTextInput$default = Ui.input$default(Ui.INSTANCE, act, RobloxApi.INSTANCE.apiKey(act), "Open Cloud API key", false, false, 24, null);
        editTextInput$default.setInputType(129);
        LinearLayout.LayoutParams layoutParamsLp$default2 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default2.topMargin = Ui.INSTANCE.dp(act, 8.0f);
        Unit unit2 = Unit.INSTANCE;
        box.addView(editTextInput$default, layoutParamsLp$default2);
        TextView textViewText$default2 = Ui.text$default(Ui.INSTANCE, act, "Or paste a .ROBLOSECURITY cookie instead of signing in. Anyone with it can use your account, so only do this on your own device.", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default2.setSingleLine(false);
        textViewText$default2.setPadding(0, Ui.INSTANCE.dp(act, 12.0f), 0, 0);
        box.addView(textViewText$default2);
        final EditText editTextInput$default2 = Ui.input$default(Ui.INSTANCE, act, "", signedIn(act) ? "(signed in — leave empty to keep)" : ".ROBLOSECURITY cookie", false, false, 24, null);
        editTextInput$default2.setInputType(129);
        LinearLayout.LayoutParams layoutParamsLp$default3 = Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null);
        layoutParamsLp$default3.topMargin = Ui.INSTANCE.dp(act, 8.0f);
        Unit unit3 = Unit.INSTANCE;
        box.addView(editTextInput$default2, layoutParamsLp$default3);
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(act).setTitle("Open Cloud API key");
        ScrollView scrollView = new ScrollView(act);
        scrollView.addView(box);
        title.setView(scrollView).setPositiveButton("Save", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AccountUi.advanced$lambda$54(act, editTextInput$default, editTextInput$default2, then, dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    static final Unit advanced$lambda$44(Activity $act, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        try {
            $act.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://create.roblox.com/dashboard/credentials")));
        } catch (Exception e) {
        }
        return Unit.INSTANCE;
    }

    static final void advanced$lambda$54(final Activity $act, EditText $key, EditText $cookie, final Function0 $then, DialogInterface dialogInterface, int i) {
        RobloxApi.INSTANCE.setApiKey($act, $key.getText().toString());
        String c = StringsKt.trim((CharSequence) $cookie.getText().toString()).toString();
        if (c.length() > 0) {
            RobloxApi.INSTANCE.setCookie($act, c);
            INSTANCE.background($act, "Checking your account…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda41
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    try { return RobloxCloud.INSTANCE.currentUser($act); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
                }
            }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda42
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return AccountUi.advanced$lambda$54$lambda$53($act, $then, (RobloxCloud.User) obj);
                }
            });
        } else if ($then != null) {
            $then.invoke();
        }
    }

    static final Unit advanced$lambda$54$lambda$53(Activity $act, Function0 $then, RobloxCloud.User u) {
        Intrinsics.checkNotNullParameter(u, "u");
        INSTANCE.toast($act, "Signed in as @" + u.getName());
        Avatar.INSTANCE.refresh($act);
        if ($then != null) {
            $then.invoke();
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void signIn$default(AccountUi accountUi, Activity activity, Function0 function0, int i, Object obj) {
        if ((i & 2) != 0) {
            function0 = null;
        }
        accountUi.signIn(activity, function0);
    }

    public final void signIn(final Activity act, final Function0<Unit> then) {
        Intrinsics.checkNotNullParameter(act, "act");
        final Dialog dlg = new Dialog(act, Ui.INSTANCE.getLIGHT() ? android.R.style.Theme_Material_Light_NoActionBar_Fullscreen : android.R.style.Theme_Material_NoActionBar_Fullscreen);
        LinearLayout linearLayout = new LinearLayout(act);
        linearLayout.setOrientation(1);
        linearLayout.setBackgroundColor(Ui.INSTANCE.getPANEL());
        LinearLayout linearLayout2 = new LinearLayout(act);
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        linearLayout2.setBackgroundColor(Ui.INSTANCE.getTOPBAR());
        linearLayout2.setPadding(Ui.INSTANCE.dp(act, 12.0f), Ui.INSTANCE.dp(act, 6.0f), Ui.INSTANCE.dp(act, 6.0f), Ui.INSTANCE.dp(act, 6.0f));
        linearLayout2.addView(Ui.INSTANCE.text(act, "Sign in to Roblox", 15.0f, Ui.INSTANCE.getTEXT(), true), Ui.INSTANCE.lp(0, -2, 1.0f));
        linearLayout2.addView(Ui.button$default(Ui.INSTANCE, act, "Cancel", 0, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda47
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.signIn$lambda$57(dlg, (View) obj);
            }
        }, 4, null));
        linearLayout.addView(linearLayout2, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        final ProgressBar progressBar = new ProgressBar(act, null, R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(true);
        linearLayout.addView(progressBar, Ui.lp$default(Ui.INSTANCE, -1, Ui.INSTANCE.dp(act, 4.0f), 0.0f, 4, null));
        final WebView web = new WebView(act);
        linearLayout.addView(web, Ui.INSTANCE.lp(-1, 0, 1.0f));
        dlg.setContentView(linearLayout);
        final CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.setAcceptThirdPartyCookies(web, true);
        cm.removeAllCookies(null);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        final Ref.BooleanRef done = new Ref.BooleanRef();
        final String previous = RobloxApi.INSTANCE.cookie(act);
        Runnable runnable = new Runnable() { // from class: com.goydevv.studrblx.ui.AccountUi$signIn$poll$1
            @Override // java.lang.Runnable
            public void run() {
                AccountUi.signIn$check(done, cm, previous, act, dlg, then);
                if (done.element || !dlg.isShowing()) {
                    return;
                }
                AccountUi.main.postDelayed(this, 800L);
            }
        };
        web.setWebViewClient(new WebViewClient() { // from class: com.goydevv.studrblx.ui.AccountUi.signIn.2
            /* JADX WARN: Code duplicated, block: B:7:0x0022  */
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                String host;
                Intrinsics.checkNotNullParameter(view, "view");
                Intrinsics.checkNotNullParameter(req, "req");
                Uri u = req.getUrl();
                String host2 = u.getHost();
                if (host2 != null) {
                    host = host2.toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue(host, "toLowerCase(...)");
                    if (host == null) {
                        host = "";
                    }
                } else {
                    host = "";
                }
                boolean ok = Intrinsics.areEqual(u.getScheme(), "https") && (Intrinsics.areEqual(host, "roblox.com") || StringsKt.endsWith(host, ".roblox.com", false));
                return !ok;
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(8);
                AccountUi.signIn$check(done, cm, previous, act, dlg, then);
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(0);
            }
        });
        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda48
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AccountUi.signIn$lambda$61(done, web, dialogInterface);
            }
        });
        dlg.show();
        web.loadUrl("https://www.roblox.com/login");
        main.postDelayed(runnable, 800L);
    }

    static final Unit signIn$lambda$57(Dialog $dlg, View it) {
        Intrinsics.checkNotNullParameter(it, "it");
        safeDismiss($dlg);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void signIn$check(Ref.BooleanRef done, CookieManager cm, String previous, final Activity $act, Dialog dlg, final Function0<Unit> function0) {
        String all;
        MatchResult m;
        if (done.element || (all = cm.getCookie("https://www.roblox.com")) == null || (m = (new Regex("(?:^|;\\s*)\\.ROBLOSECURITY=([^;]+)")).find(all, 0)) == null || Intrinsics.areEqual(m.getGroupValues().get(1), previous)) {
            return;
        }
        done.element = true;
        RobloxApi.INSTANCE.setCookie($act, m.getGroupValues().get(1));
        RobloxCloud.INSTANCE.resetSession();
        cm.removeAllCookies(null);
        cm.flush();
        safeDismiss(dlg);
        if (!alive($act)) {
            final Context appCtx = $act.getApplicationContext();
            net.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        RobloxCloud.User u = RobloxCloud.INSTANCE.currentUser(appCtx);
                        OutputLog.INSTANCE.info("Signed in to Roblox as @" + u.getName());
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                Avatar.INSTANCE.refresh(appCtx);
                            }
                        });
                    } catch (Throwable t) {
                        OutputLog.INSTANCE.warn("Sign-in finished but the account could not be loaded: " + t.getMessage());
                    }
                }
            });
            return;
        }
        INSTANCE.background($act, "Signing in…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda56
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                try { return RobloxCloud.INSTANCE.currentUser($act); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda57
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.signIn$check$lambda$60($act, function0, (RobloxCloud.User) obj);
            }
        });
    }

    static final Unit signIn$check$lambda$60(Activity $act, Function0 $then, RobloxCloud.User u) {
        Intrinsics.checkNotNullParameter(u, "u");
        INSTANCE.toast($act, "Signed in as @" + u.getName());
        OutputLog.INSTANCE.info("Signed in to Roblox as @" + u.getName());
        Avatar.INSTANCE.refresh($act);
        if ($then != null) {
            $then.invoke();
        }
        return Unit.INSTANCE;
    }

    static final void signIn$lambda$61(Ref.BooleanRef $done, WebView $web, DialogInterface it) {
        $done.element = true;
        $web.stopLoading();
        $web.destroy();
    }

    public final void signOut(final Activity act) {
        Intrinsics.checkNotNullParameter(act, "act");
        String strUserName = RobloxApi.INSTANCE.userName(act);
        if (strUserName.length() == 0) {
            strUserName = "your account";
        }
        String name = strUserName;
        Dialogs.INSTANCE.confirm(act, "Sign out", "Sign out of @" + name + "? This also ends that session on Roblox.", "Sign out", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda101
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.signOut$lambda$65(act);
            }
        });
    }

    static final Unit signOut$lambda$65(final Activity $act) {
        final long id = RobloxApi.INSTANCE.userId($act);
        INSTANCE.background($act, "Signing out…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda72
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.signOut$lambda$65$lambda$63($act);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda73
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.signOut$lambda$65$lambda$64($act, id, (Unit) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit signOut$lambda$65$lambda$63(Activity $act) {
        RobloxCloud.INSTANCE.logout($act);
        return Unit.INSTANCE;
    }

    static final Unit signOut$lambda$65$lambda$64(Activity $act, long $id, Unit it) {
        String str;
        Intrinsics.checkNotNullParameter(it, "it");
        Accounts.INSTANCE.remove($act, $id);
        Accounts.Account next = (Accounts.Account) CollectionsKt.firstOrNull((List) Accounts.INSTANCE.list($act));
        Accounts accounts = Accounts.INSTANCE;
        Activity activity = $act;
        if (next != null) {
            accounts.switchTo(activity, next);
        } else {
            accounts.useGuest(activity);
        }
        CookieManager.getInstance().removeAllCookies(null);
        Avatar.INSTANCE.refresh($act);
        AccountUi accountUi = INSTANCE;
        if (next != null) {
            str = "Signed out. Now using @" + next.getName();
        } else {
            str = "Signed out";
        }
        accountUi.toast($act, str);
        return Unit.INSTANCE;
    }

    private final void requireSignIn(Activity act, Function0<Unit> then) {
        if (signedIn(act)) {
            then.invoke();
        } else {
            signIn(act, then);
        }
    }

    public static /* synthetic */ void avatarDialog$default(AccountUi accountUi, Activity activity, Studio studio, int i, Object obj) {
        if ((i & 2) != 0) {
            studio = null;
        }
        accountUi.avatarDialog(activity, studio);
    }

    public final void avatarDialog(Activity act, Studio studio) {
        Intrinsics.checkNotNullParameter(act, "act");
        AvatarUi.INSTANCE.playtestAvatar(act, studio);
    }

    public final void customAvatar(final Activity act) {
        final Activity act2 = act;
        Intrinsics.checkNotNullParameter(act2, "act");
        JSONObject o = Avatar.INSTANCE.custom(act2);
        JSONObject jSONObjectOptJSONObject = o.optJSONObject("colors");
        if (jSONObjectOptJSONObject == null) {
            jSONObjectOptJSONObject = new JSONObject();
        }
        JSONObject colors = jSONObjectOptJSONObject;
        LinearLayout linearLayout = new LinearLayout(act2);
        linearLayout.setOrientation(1);
        int i = 0;
        linearLayout.setPadding(Ui.INSTANCE.dp(act2, 16.0f), Ui.INSTANCE.dp(act2, 6.0f), Ui.INSTANCE.dp(act2, 16.0f), 0);
        linearLayout.addView(Ui.INSTANCE.text(act2, "Body colors", 13.0f, Ui.INSTANCE.getTEXT(), true));
        final List<Pair> keys = CollectionsKt.listOf(new Pair[]{TuplesKt.to("head", "Head"), TuplesKt.to("torso", "Torso"), TuplesKt.to("leftArm", "Left Arm"), TuplesKt.to("rightArm", "Right Arm"), TuplesKt.to("leftLeg", "Left Leg"), TuplesKt.to("rightLeg", "Right Leg")});
        final HashMap chosen = new HashMap();
        for (Pair pair : keys) {
            final String k = (String) pair.component1();
            final String label = (String) pair.component2();
            Color3.Companion companion = Color3.INSTANCE;
            String strOptString = colors.optString(k);
            Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
            Color3 c = companion.fromHex(strOptString);
            if (c == null) {
                Color3 color3 = Avatar.INSTANCE.getCLASSIC_COLORS().get(label);
                Intrinsics.checkNotNull(color3);
                c = color3;
            }
            chosen.put(k, c);
            LinearLayout linearLayout2 = new LinearLayout(act2);
            linearLayout2.setOrientation(i);
            linearLayout2.setGravity(16);
            linearLayout2.setPadding(0, Ui.INSTANCE.dp(act2, 3.0f), 0, Ui.INSTANCE.dp(act2, 3.0f));
            linearLayout2.addView(Ui.text$default(Ui.INSTANCE, act2, label, 13.0f, 0, false, 24, null), Ui.INSTANCE.lp(0, -2, 1.0f));
            final View sw = new View(act2);
            customAvatar$paint(sw, chosen, k, act2);
            sw.setOnClickListener(new View.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda69
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AccountUi.customAvatar$lambda$70(act2, label, chosen, k, sw, view);
                }
            });
            linearLayout2.addView(sw, Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(act, 44.0f), Ui.INSTANCE.dp(act, 26.0f), 0.0f, 4, null));
            linearLayout.addView(linearLayout2);
            i = 0;
        }
        final EditText shirt = customAvatar$field(linearLayout, act, o, "Shirt", "shirt", "Shirt ID or catalog link");
        final EditText pants = customAvatar$field(linearLayout, act, o, "Pants", "pants", "Pants ID or catalog link");
        final EditText tshirt = customAvatar$field(linearLayout, act, o, "T-Shirt", "tshirt", "T-Shirt ID or link");
        final EditText face = customAvatar$field(linearLayout, act, o, "Face", "face", "Face ID or link");
        final EditText acc = customAvatar$field(linearLayout, act, o, "Accessories (hats, hair…)", "accessories", "IDs or links, separated by commas");
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, act, "Clothing and accessories are downloaded from Roblox, so they need a signed-in account.", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setPadding(0, Ui.INSTANCE.dp(act, 8.0f), 0, 0);
        linearLayout.addView(textViewText$default);
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(act).setTitle("Create an avatar");
        ScrollView scrollView = new ScrollView(act);
        scrollView.addView(linearLayout);
        title.setView(scrollView).setPositiveButton("Wear it", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda70
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                try {
                    AccountUi.customAvatar$lambda$74(chosen, shirt, pants, tshirt, face, acc, act, dialogInterface, i2);
                } catch (Exception _e) {
                    if (_e instanceof RuntimeException) throw (RuntimeException) _e;
                    throw new RuntimeException(_e);
                }
            }
        }).setNeutralButton("Start from a user…", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda71
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                Activity activity = act;
                Dialogs.INSTANCE.prompt(activity, "Copy colors from which username?", "", new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda46
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return AccountUi.customAvatar$lambda$81$lambda$80(activity, keys, (String) obj);
                    }
                });
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    private static final void customAvatar$paint(View sw, HashMap<String, Color3> map, String k, Activity $act) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        Color3 color3 = map.get(k);
        Intrinsics.checkNotNull(color3);
        gradientDrawable.setColor(color3.toArgb());
        gradientDrawable.setCornerRadius(Ui.INSTANCE.dp($act, 4.0f));
        gradientDrawable.setStroke(1, -7829368);
        sw.setBackground(gradientDrawable);
    }

    static final void customAvatar$lambda$70(final Activity $act, String $label, final HashMap $chosen, final String $k, final View $sw, View it) {
        Object obj = $chosen.get($k);
        Intrinsics.checkNotNull(obj);
        Dialogs.INSTANCE.colorPicker($act, $label, (Color3) obj, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return AccountUi.customAvatar$lambda$70$lambda$69($chosen, $k, $sw, $act, obj2);
            }
        });
    }

    static final Unit customAvatar$lambda$70$lambda$69(HashMap $chosen, String $k, View $sw, Activity $act, Object v) {
        Intrinsics.checkNotNull(v, "null cannot be cast to non-null type com.goydevv.studrblx.model.Color3");
        $chosen.put($k, (Color3) v);
        customAvatar$paint($sw, $chosen, $k, $act);
        return Unit.INSTANCE;
    }

    private static final EditText customAvatar$field(LinearLayout box, Activity $act, JSONObject o, String label, String key, String hint) {
        TextView textViewText = Ui.INSTANCE.text($act, label, 13.0f, Ui.INSTANCE.getTEXT(), true);
        textViewText.setPadding(0, Ui.INSTANCE.dp($act, 10.0f), 0, Ui.INSTANCE.dp($act, 3.0f));
        box.addView(textViewText);
        String strOptString = o.optString(key);
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        EditText e = Ui.input$default(Ui.INSTANCE, $act, strOptString, hint, false, false, 24, null);
        box.addView(e, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        return e;
    }

    static final void customAvatar$lambda$74(HashMap $chosen, EditText $shirt, EditText $pants, EditText $tshirt, EditText $face, EditText $acc, Activity $act, DialogInterface dialogInterface, int i) {
        try {
        JSONObject c = new JSONObject();
        for (Object _eObj : $chosen.entrySet()) {
            Map.Entry entry = (Map.Entry) _eObj;
            String k = (String) entry.getKey();
            Color3 v = (Color3) entry.getValue();
            c.put(k, v.toHex());
        }
        JSONObject out = new JSONObject().put("colors", c).put("shirt", StringsKt.trim((CharSequence) $shirt.getText().toString()).toString()).put("pants", StringsKt.trim((CharSequence) $pants.getText().toString()).toString()).put("tshirt", StringsKt.trim((CharSequence) $tshirt.getText().toString()).toString()).put("face", StringsKt.trim((CharSequence) $face.getText().toString()).toString()).put("accessories", StringsKt.trim((CharSequence) $acc.getText().toString()).toString());
        Intrinsics.checkNotNull(out);
        Avatar.INSTANCE.setCustom($act, out);
        INSTANCE.toast($act, "Custom avatar saved");
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit customAvatar$lambda$81$lambda$80(final Activity $act, final List $keys, final String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        INSTANCE.background($act, "Loading @" + name + "…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.customAvatar$lambda$81$lambda$80$lambda$75(name);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.customAvatar$lambda$81$lambda$80$lambda$79($keys, $act, (JSONObject) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final JSONObject customAvatar$lambda$81$lambda$80$lambda$75(String $name) {
        try {
        RobloxCloud.User u = RobloxCloud.INSTANCE.userByName($name);
        if (u == null) {
            throw new Exception("No Roblox user named \"" + $name + "\".");
        }
        return Http.json$default(Http.INSTANCE, "https://avatar.roblox.com/v2/avatar/users/" + u.getId() + "/avatar", null, 2, null);
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit customAvatar$lambda$81$lambda$80$lambda$79(List $keys, final Activity $act, JSONObject j) {
        try {
        String str;
        Intrinsics.checkNotNullParameter(j, "j");
        JSONObject bc = j.optJSONObject("bodyColor3s");
        JSONObject c = new JSONObject();
        if (bc != null) {
            Iterator it = $keys.iterator();
            while (it.hasNext()) {
                String k = (String) ((Pair) it.next()).component1();
                String strOptString = bc.optString(k + "Color3");
                Intrinsics.checkNotNull(strOptString);
                if (!(strOptString.length() > 0)) {
                    strOptString = null;
                }
                if (strOptString != null) {
                    c.put(k, strOptString);
                }
            }
        }
        JSONArray assets = j.optJSONArray("assets");
        final JSONObject next = new JSONObject().put("colors", c);
        ArrayList accs = new ArrayList();
        if (assets != null) {
            int length = assets.length();
            for (int i = 0; i < length; i++) {
                JSONObject a = assets.getJSONObject(i);
                JSONObject jSONObjectOptJSONObject = a.optJSONObject("assetType");
                int t = jSONObjectOptJSONObject != null ? jSONObjectOptJSONObject.optInt("id") : 0;
                String id = String.valueOf(a.optLong("id"));
                switch (t) {
                    case 2:
                        str = "tshirt";
                        break;
                    case 8:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                        accs.add(id);
                        continue;
                    case 11:
                        str = "shirt";
                        break;
                    case 12:
                        str = "pants";
                        break;
                    case 18:
                        str = "face";
                        break;
                    default:
                        continue;
                }
                next.put(str, id);
            }
        }
        next.put("accessories", CollectionsKt.joinToString(accs, ", ", "", "", -1, "...", null));
        main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda102
            @Override // java.lang.Runnable
            public final void run() {
                AccountUi.customAvatar$lambda$81$lambda$80$lambda$79$lambda$78($act, next);
            }
        });
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final void customAvatar$lambda$81$lambda$80$lambda$79$lambda$78(Activity $act, JSONObject $next) {
        $act.getSharedPreferences("avatar", 0).edit().putString("custom", $next.toString()).apply();
        INSTANCE.customAvatar($act);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: AccountUi.kt */
    static final class Row {
        private final Drawable iconFallback;
        private final String iconUrl;
        private final Function0<Unit> onClick;
        private final String sub;
        private final String title;

        public Row(String title, String sub, String iconUrl, Drawable iconFallback, Function0<Unit> onClick) {
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(sub, "sub");
            Intrinsics.checkNotNullParameter(onClick, "onClick");
            this.title = title;
            this.sub = sub;
            this.iconUrl = iconUrl;
            this.iconFallback = iconFallback;
            this.onClick = onClick;
        }

        public final Drawable getIconFallback() {
            return this.iconFallback;
        }

        public final String getIconUrl() {
            return this.iconUrl;
        }

        public final Function0<Unit> getOnClick() {
            return this.onClick;
        }

        public final String getSub() {
            return this.sub;
        }

        public final String getTitle() {
            return this.title;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ AlertDialog listDialog$default(AccountUi accountUi, Activity activity, String str, List list, String str2, Function1 function1, int i, Object obj) {
        if ((i & 16) != 0) {
            function1 = null;
        }
        return accountUi.listDialog(activity, str, list, str2, function1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0, types: [com.goydevv.studrblx.ui.AccountUi$listDialog$adapter$1] */
    /* JADX WARN: Type inference failed for: r9v6, types: [T, android.app.AlertDialog, java.lang.Object] */
    private final AlertDialog listDialog(final Activity act, String title, final List<Row> rows, String empty, Function1<? super AlertDialog.Builder, Unit> extra) {
        View content;
        AlertDialog alertDialog;
        AlertDialog alertDialog2;
        ListView lv = new ListView(act);
        final ArrayList listeners = new ArrayList();
        final BaseAdapter r5 = new BaseAdapter() { // from class: com.goydevv.studrblx.ui.AccountUi$listDialog$adapter$1
            @Override // android.widget.Adapter
            public int getCount() {
                return rows.size();
            }

            @Override // android.widget.Adapter
            public AccountUi.Row getItem(int p) {
                return rows.get(p);
            }

            @Override // android.widget.Adapter
            public long getItemId(int p) {
                return p;
            }

            @Override // android.widget.Adapter
            public View getView(int p, View convert, ViewGroup parent) {
                Intrinsics.checkNotNullParameter(parent, "parent");
                AccountUi.Row r = rows.get(p);
                LinearLayout v = convert instanceof LinearLayout ? (LinearLayout) convert : null;
                if (v == null) {
                    v = new LinearLayout(act);
                    Activity activity = act;
                    v.setOrientation(0);
                    v.setGravity(16);
                    Activity activity2 = activity;
                    v.setPadding(Ui.INSTANCE.dp(activity2, 12.0f), Ui.INSTANCE.dp(activity2, 8.0f), Ui.INSTANCE.dp(activity2, 12.0f), Ui.INSTANCE.dp(activity2, 8.0f));
                    ImageView imageView = new ImageView(activity2);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    LinearLayout.LayoutParams layoutParamsLp$default = Ui.lp$default(Ui.INSTANCE, Ui.INSTANCE.dp(activity2, 44.0f), Ui.INSTANCE.dp(activity2, 44.0f), 0.0f, 4, null);
                    layoutParamsLp$default.rightMargin = Ui.INSTANCE.dp(activity2, 12.0f);
                    Unit unit = Unit.INSTANCE;
                    v.addView(imageView, layoutParamsLp$default);
                    LinearLayout linearLayout = new LinearLayout(activity2);
                    linearLayout.setOrientation(1);
                    linearLayout.addView(Ui.INSTANCE.text(activity2, "", 14.0f, Ui.INSTANCE.getTEXT(), true));
                    linearLayout.addView(Ui.text$default(Ui.INSTANCE, activity2, "", 12.0f, Ui.INSTANCE.getDIM(), false, 16, null));
                    v.addView(linearLayout, Ui.INSTANCE.lp(0, -2, 1.0f));
                }
                View childAt = v.getChildAt(0);
                Intrinsics.checkNotNull(childAt, "null cannot be cast to non-null type android.widget.ImageView");
                ImageView img = (ImageView) childAt;
                View childAt2 = v.getChildAt(1);
                Intrinsics.checkNotNull(childAt2, "null cannot be cast to non-null type android.widget.LinearLayout");
                LinearLayout texts = (LinearLayout) childAt2;
                View childAt3 = texts.getChildAt(0);
                Intrinsics.checkNotNull(childAt3, "null cannot be cast to non-null type android.widget.TextView");
                ((TextView) childAt3).setText(r.getTitle());
                View childAt4 = texts.getChildAt(1);
                Intrinsics.checkNotNull(childAt4, "null cannot be cast to non-null type android.widget.TextView");
                TextView textView = (TextView) childAt4;
                textView.setText(r.getSub());
                textView.setVisibility(r.getSub().length() == 0 ? 8 : 0);
                String iconUrl = r.getIconUrl();
                Bitmap bmp = iconUrl != null ? ImageLoader.INSTANCE.getKey("url:" + iconUrl) : null;
                if (bmp != null) {
                    img.setImageBitmap(bmp);
                } else {
                    img.setImageDrawable(r.getIconFallback());
                }
                return v;
            }
        };
        lv.setAdapter((ListAdapter) r5);
        Function1<String, Unit> function1 = new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.listDialog$lambda$83(r5, (String) obj);
            }
        };
        listeners.add(function1);
        ImageLoader.INSTANCE.getListeners().add(function1);
        final Ref.ObjectRef dlg = new Ref.ObjectRef();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda17
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                AccountUi.listDialog$lambda$84(dlg, rows, adapterView, view, i, j);
            }
        });
        if (rows.isEmpty()) {
            TextView textViewText$default = Ui.text$default(Ui.INSTANCE, act, empty, 14.0f, Ui.INSTANCE.getDIM(), false, 16, null);
            textViewText$default.setSingleLine(false);
            textViewText$default.setPadding(Ui.INSTANCE.dp(act, 20.0f), Ui.INSTANCE.dp(act, 16.0f), Ui.INSTANCE.dp(act, 20.0f), Ui.INSTANCE.dp(act, 16.0f));
            content = textViewText$default;
        } else {
            content = lv;
        }
        AlertDialog.Builder b = Dialogs.INSTANCE.themed(act).setTitle(title).setView(content).setNegativeButton("Close", (DialogInterface.OnClickListener) null);
        if (extra != null) {
            Intrinsics.checkNotNull(b);
            extra.invoke(b);
        }
        AlertDialog Create = b.create();
        Intrinsics.checkNotNullExpressionValue(Create, "create(...)");
        dlg.element = Create;
        if (dlg.element == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dlg");
            alertDialog = null;
        } else {
            alertDialog = (AlertDialog) dlg.element;
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda18
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                AccountUi.listDialog$lambda$87(listeners, dialogInterface);
            }
        });
        if (dlg.element == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dlg");
            alertDialog2 = null;
        } else {
            alertDialog2 = (AlertDialog) dlg.element;
        }
        alertDialog2.show();
        if (dlg.element != null) {
            return (AlertDialog) dlg.element;
        }
        Intrinsics.throwUninitializedPropertyAccessException("dlg");
        return null;
    }

    static final Unit listDialog$lambda$83(final BaseAdapter $adapter, String k) {
        Intrinsics.checkNotNullParameter(k, "k");
        if (StringsKt.startsWith(k, "url:", false)) {
            main.post(new Runnable() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    $adapter.notifyDataSetChanged();
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final void listDialog$lambda$84(Ref.ObjectRef $dlg, List $rows, AdapterView adapterView, View view, int pos, long j) {
        AlertDialog alertDialog;
        if ($dlg.element == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dlg");
            alertDialog = null;
        } else {
            alertDialog = (AlertDialog) $dlg.element;
        }
        safeDismiss(alertDialog);
        ((Row) $rows.get(pos)).getOnClick().invoke();
    }

    static final void listDialog$lambda$87(ArrayList $listeners, DialogInterface it) {
        Iterator it2 = $listeners.iterator();
        while (it2.hasNext()) {
            ImageLoader.INSTANCE.getListeners().remove((Function1) it2.next());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void myGames$default(AccountUi accountUi, Activity activity, Studio studio, MainActions mainActions, Function2 function2, int i, Object obj) {
        if ((i & 8) != 0) {
            function2 = null;
        }
        accountUi.myGames(activity, studio, mainActions, function2);
    }

    public final void myGames(final Activity act, final Studio studio, final MainActions actions, final Function2<? super RobloxCloud.Game, ? super RobloxCloud.Place, Unit> pick) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        requireSignIn(act, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda40
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.myGames$lambda$96(act, pick, studio, actions);
            }
        });
    }

    static final Unit myGames$lambda$96(final Activity $act, final Function2 $pick, final Studio $studio, final MainActions $actions) {
        INSTANCE.background($act, "Loading your games…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda109
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.myGames$lambda$96$lambda$89($act);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda110
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.myGames$lambda$96$lambda$95($act, $pick, $studio, $actions, (Pair) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final Pair myGames$lambda$96$lambda$89(Activity $act) {
        try {
        RobloxCloud.User u = RobloxCloud.INSTANCE.currentUser($act);
        Iterable games = RobloxCloud.games$default(RobloxCloud.INSTANCE, $act, u, false, 4, null);
        RobloxCloud robloxCloud = RobloxCloud.INSTANCE;
        Iterable iterable = games;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(Long.valueOf(((RobloxCloud.Game) it.next()).getUniverseId()));
        }
        return TuplesKt.to(games, robloxCloud.gameIcons((List) arrayList));
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit myGames$lambda$96$lambda$95(final Activity $act, final Function2 $pick, final Studio $studio, final MainActions $actions, Pair pair) {
        Intrinsics.checkNotNullParameter(pair, "<destruct>");
        Iterable games = (List) pair.component1();
        Map icons = (Map) pair.component2();
        Iterable<RobloxCloud.Game> iterable = games;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (final RobloxCloud.Game game : iterable) {
            arrayList.add(new Row(game.getName(), game.getOwner() + (game.getUpdated().length() > 0 ? "  ·  updated " + INSTANCE.prettyDate(game.getUpdated()) : ""), (String) icons.get(Long.valueOf(game.getUniverseId())), Icons.general$default(Icons.INSTANCE, $act, "Place", null, 4, null), new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda19
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AccountUi.myGames$lambda$96$lambda$95$lambda$91$lambda$90($act, $studio, $actions, game, $pick);
                }
            }));
        }
        List rows = (List) arrayList;
        INSTANCE.listDialog($act, $pick != null ? "Publish to which game?" : StartScreen.MY_GAMES, rows, $pick != null ? "No games yet. Tap “Create new game…” to make one from the place you're editing." : "No games found on this account. Use File → Publish to Roblox As → Create new game, or create one on create.roblox.com.", new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.myGames$lambda$96$lambda$95$lambda$94($pick, $act, $studio, $actions, (AlertDialog.Builder) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit myGames$lambda$96$lambda$95$lambda$91$lambda$90(Activity $act, Studio $studio, MainActions $actions, RobloxCloud.Game $g, Function2 $pick) {
        INSTANCE.places($act, $studio, $actions, $g, $pick);
        return Unit.INSTANCE;
    }

    static final Unit myGames$lambda$96$lambda$95$lambda$94(Function2 $pick, final Activity $act, final Studio $studio, final MainActions $actions, AlertDialog.Builder b) {
        Intrinsics.checkNotNullParameter(b, "b");
        if ($pick == null) {
            b.setNeutralButton("Open by place ID…", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda14
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    AccountUi.INSTANCE.openById($act, $studio, $actions);
                }
            });
        } else {
            b.setNeutralButton("Create new game…", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda15
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    AccountUi.INSTANCE.createNewGame($act, $studio, $actions);
                }
            });
        }
        return Unit.INSTANCE;
    }

    public final void places(final Activity act, final Studio studio, final MainActions actions, final RobloxCloud.Game g, final Function2<? super RobloxCloud.Game, ? super RobloxCloud.Place, Unit> pick) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Intrinsics.checkNotNullParameter(g, "g");
        background(act, "Loading places…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda24
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return RobloxCloud.INSTANCE.places(act, g);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda25
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.places$lambda$100(pick, g, act, studio, actions, (List) obj);
            }
        });
    }

    static final Unit places$lambda$100(Function2 $pick, final RobloxCloud.Game $g, final Activity $act, final Studio $studio, final MainActions $actions, List list) {
        final Function2 function2 = $pick;
        Intrinsics.checkNotNullParameter(list, "list");
        if (list.size() == 1) {
            if (function2 != null) {
                function2.invoke($g, list.get(0));
            } else {
                INSTANCE.placeActions($act, $studio, $actions, $g, (RobloxCloud.Place) list.get(0));
            }
            return Unit.INSTANCE;
        }
        AccountUi accountUi = INSTANCE;
        String name = $g.getName();
        List<RobloxCloud.Place> list2 = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
        for (final RobloxCloud.Place place : list2) {
            arrayList.add(new Row(place.getName(), "Place " + place.getId() + (place.getId() == $g.getRootPlaceId() ? "  ·  start place" : ""), null, Icons.general$default(Icons.INSTANCE, $act, "Place", null, 4, null), new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda53
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AccountUi.places$lambda$100$lambda$99$lambda$98(function2, $g, place, $act, $studio, $actions);
                }
            }));

        }
        listDialog$default(accountUi, $act, name, (List) arrayList, "This experience has no places.", null, 16, null);
        return Unit.INSTANCE;
    }

    static final Unit places$lambda$100$lambda$99$lambda$98(Function2 $pick, RobloxCloud.Game $g, RobloxCloud.Place $p, Activity $act, Studio $studio, MainActions $actions) {
        if ($pick != null) {
            $pick.invoke($g, $p);
        } else {
            INSTANCE.placeActions($act, $studio, $actions, $g, $p);
        }
        return Unit.INSTANCE;
    }

    private final String safeFile(String name) {
        String string = StringsKt.trim((CharSequence) new Regex("[^A-Za-z0-9._ -]").replace(name, "_")).toString();
        if (StringsKt.isBlank(string)) {
            string = "Place";
        }
        return StringsKt.take(string, 60);
    }

    static final Unit placeActions$lambda$102(Activity $act, MainActions $actions, RobloxCloud.Game $g, RobloxCloud.Place $p) {
        INSTANCE.openPlace($act, $actions, $g, $p, null);
        return Unit.INSTANCE;
    }

    public final void placeActions(final Activity act, final Studio studio, final MainActions actions, final RobloxCloud.Game g, final RobloxCloud.Place p) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Intrinsics.checkNotNullParameter(g, "g");
        Intrinsics.checkNotNullParameter(p, "p");
        final List items = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Open (edit this place)", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda96
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.placeActions$lambda$102(act, actions, g, p);
            }
        }), TuplesKt.to("Download as .rbxl…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda97
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.placeActions$lambda$103(act, actions, p);
            }
        }), TuplesKt.to("Version history…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda98
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.placeActions$lambda$104(act, studio, actions, g, p);
            }
        }), TuplesKt.to("Copy place ID", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda99
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.placeActions$lambda$105(act, p);
            }
        })});
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(act).setTitle(p.getName());
        List list = items;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getFirst());
        }
        title.setItems((CharSequence[]) ((List) arrayList).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda100
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ((Function0) ((Pair) items.get(i)).getSecond()).invoke();
            }
        }).setNegativeButton("Close", (DialogInterface.OnClickListener) null).show();
    }

    static final Unit placeActions$lambda$103(Activity $act, MainActions $actions, RobloxCloud.Place $p) {
        INSTANCE.downloadPlace($act, $actions, $p, null);
        return Unit.INSTANCE;
    }

    static final Unit placeActions$lambda$104(Activity $act, Studio $studio, MainActions $actions, RobloxCloud.Game $g, RobloxCloud.Place $p) {
        INSTANCE.versionHistory($act, $studio, $actions, $g, $p);
        return Unit.INSTANCE;
    }

    static final Unit placeActions$lambda$105(Activity $act, RobloxCloud.Place $p) {
        Object systemService = $act.getSystemService("clipboard");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.content.ClipboardManager");
        ((ClipboardManager) systemService).setPrimaryClip(ClipData.newPlainText("Place ID", String.valueOf($p.getId())));
        INSTANCE.toast($act, "Copied " + $p.getId());
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:7:0x003a  */
    public final void openPlace(final Activity act, final MainActions actions, final RobloxCloud.Game g, final RobloxCloud.Place p, final Integer version) {
        String str;
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Intrinsics.checkNotNullParameter(p, "p");
        String name = p.getName();
        if (version != null) {
            str = " (version " + version.intValue() + ")";
            if (str == null) {
                str = "";
            }
        } else {
            str = "";
        }
        background(act, "Downloading " + name + str + "…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda87
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.openPlace$lambda$110(act, p, version, g);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda88
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.openPlace$lambda$113(p, version, g, actions, act, (Pair) obj);
            }
        });
    }

    static final Pair openPlace$lambda$110(Activity $act, RobloxCloud.Place $p, Integer $version, RobloxCloud.Game $g) {
        try {
        long universe;
        byte[] bytes = RobloxCloud.INSTANCE.downloadPlace($act, $p.getId(), $version);
        if (!RbxFile.INSTANCE.isPlace(RbxFile.INSTANCE.read(bytes))) {
            throw new Exception("That asset isn't a place file.");
        }
        if ($g != null) {
            universe = $g.getUniverseId();
        } else {
            Long lValueOf = Long.valueOf($p.getUniverseId());
            if (!(lValueOf.longValue() != 0)) {
                lValueOf = null;
            }
            universe = lValueOf != null ? lValueOf.longValue() : RobloxCloud.INSTANCE.universeOf($p.getId());
        }
        return TuplesKt.to(bytes, Long.valueOf(universe));
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    /* JADX WARN: Code duplicated, block: B:24:0x0088  */
    /* JADX WARN: Code duplicated, block: B:7:0x0043  */
    /* JADX WARN: Multi-variable type inference failed */
    static final Unit openPlace$lambda$113(RobloxCloud.Place $p, Integer $version, RobloxCloud.Game $g, MainActions mainActions, Activity $act, Pair pair) {
        try {
        String str;
        Object name;
        Intrinsics.checkNotNullParameter(pair, "<destruct>");
        byte[] bytes = (byte[]) pair.component1();
        long universe = ((Number) pair.component2()).longValue();
        String strSafeFile = INSTANCE.safeFile($p.getName());
        if ($version != null) {
            str = "_v" + $version.intValue();
            if (str == null) {
                str = "";
            }
        } else {
            str = "";
        }
        String name2 = strSafeFile + str + ".rbxl";
        Object name3 = null;
        if ($version == null && universe != 0) {
            long id = $p.getId();
            if ($g == null || (name = $g.getName()) == null) {
                name3 = $p.getName();
            } else {
                name3 = ($p.getId() > $g.getRootPlaceId() ? 1 : ($p.getId() == $g.getRootPlaceId() ? 0 : -1)) == 0 ? name : null;
                if (name3 == null) {
                    name3 = $p.getName();
                }
            }
            name3 = new CloudPlace(universe, id, (String) name3);
        }
        CloudPlace cloudPlace = (CloudPlace) name3;
        mainActions.openPlaceBytes(name2, bytes, cloudPlace);
        if (cloudPlace != null) {
            Recents.INSTANCE.add($act, new Recents.Entry(cloudPlace.getName(), $p.getId(), universe, null, 0L, RobloxApi.INSTANCE.userId($act), 24, null));
        }
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    private final void downloadPlace(final Activity act, final MainActions actions, final RobloxCloud.Place p, final Integer version) {
        background(act, "Downloading " + p.getName() + "…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda67
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return RobloxCloud.INSTANCE.downloadPlace(act, p.getId(), version);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda68
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.downloadPlace$lambda$117(actions, p, version, (byte[]) obj);
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0030  */
    static final Unit downloadPlace$lambda$117(MainActions $actions, RobloxCloud.Place $p, Integer $version, final byte[] bytes) {
        String str;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        String strSafeFile = INSTANCE.safeFile($p.getName());
        if ($version != null) {
            str = "_v" + $version.intValue();
            if (str == null) {
                str = "";
            }
        } else {
            str = "";
        }
        $actions.saveBytesAs(strSafeFile + str + ".rbxl", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda90
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.downloadPlace$lambda$117$lambda$116(bytes);
            }
        });
        return Unit.INSTANCE;
    }

    static final byte[] downloadPlace$lambda$117$lambda$116(byte[] $bytes) {
        return $bytes;
    }

    private final void versionHistory(final Activity act, Studio studio, final MainActions actions, final RobloxCloud.Game g, final RobloxCloud.Place p) {
        background(act, "Loading version history…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda49
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                try { return RobloxCloud.INSTANCE.versions(act, p.getId()); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda50
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.versionHistory$lambda$128(act, p, actions, g, (List) obj);
            }
        });
    }

    static final Unit versionHistory$lambda$128(final Activity $act, final RobloxCloud.Place $p, final MainActions $actions, final RobloxCloud.Game $g, List versions) {
        Intrinsics.checkNotNullParameter(versions, "versions");
        List<RobloxCloud.Version> list = versions;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        for (final RobloxCloud.Version version : list) {
            arrayList.add(new Row("Version " + version.getNumber() + (version.getPublished() ? "  (published)" : ""), INSTANCE.prettyDate(version.getCreated()), null, Icons.general$default(Icons.INSTANCE, $act, "History", null, 4, null), new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda29
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AccountUi.versionHistory$lambda$128$lambda$124$lambda$123($act, $p, version, $actions, $g);
                }
            }));
        }
        List rows = (List) arrayList;
        INSTANCE.listDialog($act, $p.getName() + " — versions", rows, "No versions found (you may not have edit access to this place).", new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda30
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.versionHistory$lambda$128$lambda$127($act, $actions, $p, (AlertDialog.Builder) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit versionHistory$lambda$128$lambda$124$lambda$123(final Activity $act, final RobloxCloud.Place $p, final RobloxCloud.Version $v, final MainActions $actions, final RobloxCloud.Game $g) {
        final List items = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Open this version", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda26
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.versionHistory$lambda$128$lambda$124$lambda$123$lambda$119($act, $actions, $g, $p, $v);
            }
        }), TuplesKt.to("Download this version (.rbxl)…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda27
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.versionHistory$lambda$128$lambda$124$lambda$123$lambda$120($act, $actions, $p, $v);
            }
        })});
        AlertDialog.Builder title = Dialogs.INSTANCE.themed($act).setTitle($p.getName() + " — version " + $v.getNumber());
        List list = items;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getFirst());
        }
        title.setItems((CharSequence[]) ((List) arrayList).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda28
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ((Function0) ((Pair) items.get(i)).getSecond()).invoke();
            }
        }).setNegativeButton("Close", (DialogInterface.OnClickListener) null).show();
        return Unit.INSTANCE;
    }

    static final Unit versionHistory$lambda$128$lambda$124$lambda$123$lambda$119(Activity $act, MainActions $actions, RobloxCloud.Game $g, RobloxCloud.Place $p, RobloxCloud.Version $v) {
        INSTANCE.openPlace($act, $actions, $g, $p, Integer.valueOf($v.getNumber()));
        return Unit.INSTANCE;
    }

    static final Unit versionHistory$lambda$128$lambda$124$lambda$123$lambda$120(Activity $act, MainActions $actions, RobloxCloud.Place $p, RobloxCloud.Version $v) {
        INSTANCE.downloadPlace($act, $actions, $p, Integer.valueOf($v.getNumber()));
        return Unit.INSTANCE;
    }

    static final Unit versionHistory$lambda$128$lambda$127(final Activity $act, final MainActions $actions, final RobloxCloud.Place $p, AlertDialog.Builder b) {
        Intrinsics.checkNotNullParameter(b, "b");
        b.setNeutralButton("Version number…", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda75
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                Activity activity = $act;
                Dialogs.INSTANCE.prompt(activity, "Download which version number?", "", new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda94
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return AccountUi.versionHistory$lambda$128$lambda$127$lambda$126$lambda$125(activity, $actions, $p, (String) obj);
                    }
                });
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit versionHistory$lambda$128$lambda$127$lambda$126$lambda$125(Activity $act, MainActions $actions, RobloxCloud.Place $p, String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        Integer intOrNull = StringsKt.toIntOrNull(StringsKt.trim((CharSequence) s).toString());
        if (intOrNull == null) {
            return Unit.INSTANCE;
        }
        int n = intOrNull.intValue();
        INSTANCE.downloadPlace($act, $actions, $p, Integer.valueOf(n));
        return Unit.INSTANCE;
    }

    public final void openById(final Activity act, Studio studio, final MainActions actions) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Dialogs.INSTANCE.prompt(act, "Place ID or link", "", new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda31
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.openById$lambda$134(act, actions, (String) obj);
            }
        });
    }

    static final Unit openById$lambda$134(final Activity $act, final MainActions $actions, String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        Long assetId = RobloxApi.INSTANCE.parseAssetId(s);
        if (assetId == null) {
            INSTANCE.toast($act, "That doesn't look like a place ID");
            return Unit.INSTANCE;
        }
        long id = assetId.longValue();
        final RobloxCloud.Place p = new RobloxCloud.Place(id, "Place " + id, 0L);
        final List items = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Open (save goes back to Roblox)", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda37
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.openById$lambda$134$lambda$130($act, $actions, p);
            }
        }), TuplesKt.to("Download as .rbxl…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda38
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.openById$lambda$134$lambda$131($act, $actions, p);
            }
        })});
        AlertDialog.Builder title = Dialogs.INSTANCE.themed($act).setTitle("Place " + id);
        List list = items;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getFirst());
        }
        title.setItems((CharSequence[]) ((List) arrayList).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda39
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ((Function0) ((Pair) items.get(i)).getSecond()).invoke();
            }
        }).show();
        return Unit.INSTANCE;
    }

    static final Unit openById$lambda$134$lambda$130(Activity $act, MainActions $actions, RobloxCloud.Place $p) {
        INSTANCE.openPlace($act, $actions, null, $p, null);
        return Unit.INSTANCE;
    }

    static final Unit openById$lambda$134$lambda$131(Activity $act, MainActions $actions, RobloxCloud.Place $p) {
        INSTANCE.downloadPlace($act, $actions, $p, null);
        return Unit.INSTANCE;
    }

    public final void publish(final Activity act, final Studio studio, final MainActions actions) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        final CloudPlace c = studio.getCloud();
        if (c == null) {
            publishAs(act, studio, actions);
            return;
        }
        final ArrayList items = new ArrayList();
        items.add(TuplesKt.to("Publish (players get this version)", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda76
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.publish$lambda$136(act, studio, c);
            }
        }));
        items.add(TuplesKt.to("Save to Roblox (new version, not published)", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda77
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.publish$lambda$137(act, studio, c);
            }
        }));
        items.add(TuplesKt.to("Publish to a different place…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda78
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.publish$lambda$138(act, studio, actions);
            }
        }));
        AlertDialog.Builder title = Dialogs.INSTANCE.themed(act).setTitle("“" + c.getName() + "” (place " + c.getPlaceId() + ")");
        ArrayList arrayList = items;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList, 10));
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add((String) ((Pair) it.next()).getFirst());
        }
        title.setItems((CharSequence[]) ((List) arrayList2).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda79
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ((Function0) ((Pair) items.get(i)).getSecond()).invoke();
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    static final Unit publish$lambda$136(Activity $act, Studio $studio, CloudPlace $c) {
        upload$default(INSTANCE, $act, $studio, $c, true, null, 16, null);
        return Unit.INSTANCE;
    }

    static final Unit publish$lambda$137(Activity $act, Studio $studio, CloudPlace $c) {
        upload$default(INSTANCE, $act, $studio, $c, false, null, 16, null);
        return Unit.INSTANCE;
    }

    static final Unit publish$lambda$138(Activity $act, Studio $studio, MainActions $actions) {
        INSTANCE.publishAs($act, $studio, $actions);
        return Unit.INSTANCE;
    }

    public final void publishAs(final Activity act, final Studio studio, final MainActions actions) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        requireSignIn(act, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda95
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.publishAs$lambda$147(act, studio, actions);
            }
        });
    }

    static final Unit publishAs$lambda$147(final Activity $act, final Studio $studio, final MainActions $actions) {
        final List items = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Create new game…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda80
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.publishAs$lambda$147$lambda$141($act, $studio, $actions);
            }
        }), TuplesKt.to("Update an existing game…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda81
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.publishAs$lambda$147$lambda$144($act, $studio, $actions);
            }
        })});
        AlertDialog.Builder title = Dialogs.INSTANCE.themed($act).setTitle("Publish to Roblox As");
        List list = items;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getFirst());
        }
        title.setItems((CharSequence[]) ((List) arrayList).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda82
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ((Function0) ((Pair) items.get(i)).getSecond()).invoke();
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
        return Unit.INSTANCE;
    }

    static final Unit publishAs$lambda$147$lambda$141(Activity $act, Studio $studio, MainActions $actions) {
        INSTANCE.createNewGame($act, $studio, $actions);
        return Unit.INSTANCE;
    }

    static final Unit publishAs$lambda$147$lambda$144(final Activity $act, final Studio $studio, MainActions $actions) {
        INSTANCE.myGames($act, $studio, $actions, new Function2() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda103
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return AccountUi.publishAs$lambda$147$lambda$144$lambda$143($act, $studio, (RobloxCloud.Game) obj, (RobloxCloud.Place) obj2);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit publishAs$lambda$147$lambda$144$lambda$143(final Activity $act, final Studio $studio, final RobloxCloud.Game g, final RobloxCloud.Place p) {
        Intrinsics.checkNotNullParameter(g, "g");
        Intrinsics.checkNotNullParameter(p, "p");
        Dialogs.INSTANCE.confirm($act, "Overwrite " + p.getName() + "?", "This replaces “" + p.getName() + "” in “" + g.getName() + "” with the place you're editing and publishes it.", "Publish", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.publishAs$lambda$147$lambda$144$lambda$143$lambda$142($act, $studio, g, p);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit publishAs$lambda$147$lambda$144$lambda$143$lambda$142(Activity $act, Studio $studio, RobloxCloud.Game $g, RobloxCloud.Place $p) {
        upload$default(INSTANCE, $act, $studio, new CloudPlace($g.getUniverseId(), $p.getId(), $p.getName()), true, null, 16, null);
        return Unit.INSTANCE;
    }

    public final void createNewGame(final Activity act, final Studio studio, MainActions actions) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        requireSignIn(act, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda51
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.createNewGame$lambda$161(act, studio);
            }
        });
    }

    static final Unit createNewGame$lambda$161(final Activity $act, final Studio $studio) {
        INSTANCE.background($act, "Loading your groups…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda35
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.createNewGame$lambda$161$lambda$148($act);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda36
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.createNewGame$lambda$161$lambda$160($act, $studio, (List) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final List createNewGame$lambda$161$lambda$148(Activity $act) {
        try {
        RobloxCloud.User u = RobloxCloud.INSTANCE.cachedUser($act);
        if (u == null) {
            u = RobloxCloud.INSTANCE.currentUser($act);
        }
        try {
            return RobloxCloud.INSTANCE.groups($act, u);
        } catch (AuthRequiredException e) {
            throw e;
        } catch (Exception e2) {
            return CollectionsKt.emptyList();
        }
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final Unit createNewGame$lambda$161$lambda$160(final Activity $act, final Studio $studio, List groups) {
        String name;
        Intrinsics.checkNotNullParameter(groups, "groups");
        LinearLayout linearLayout = new LinearLayout($act);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(Ui.INSTANCE.dp($act, 18.0f), Ui.INSTANCE.dp($act, 8.0f), Ui.INSTANCE.dp($act, 18.0f), 0);
        createNewGame$lambda$161$lambda$160$label(linearLayout, $act, "Name");
        Ui ui = Ui.INSTANCE;
        Activity activity = $act;
        CloudPlace cloud = $studio.getCloud();
        if (cloud == null || (name = cloud.getName()) == null) {
            String strSubstringBeforeLast$default = com.goydevv.studrblx.util.KtCompat.substringBeforeLast($studio.getFileName(), '.');
            if (StringsKt.isBlank(strSubstringBeforeLast$default)) {
                strSubstringBeforeLast$default = "My Game";
            }
            name = strSubstringBeforeLast$default;
        }
        final EditText name2 = Ui.input$default(ui, activity, name, "Game name", false, false, 24, null);
        linearLayout.addView(name2, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        createNewGame$lambda$161$lambda$160$label(linearLayout, $act, "Description");
        final EditText desc = Ui.input$default(Ui.INSTANCE, $act, "", "Optional", false, false, 24, null);
        linearLayout.addView(desc, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        createNewGame$lambda$161$lambda$160$label(linearLayout, $act, "Creator");
        List listListOf = CollectionsKt.listOf(TuplesKt.to("Me (@" + RobloxApi.INSTANCE.userName($act) + ")", 0L));
        List<RobloxCloud.Group> list = groups;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        for (RobloxCloud.Group group : list) {
            arrayList.add(TuplesKt.to(group.getName(), Long.valueOf(group.getId())));
        }
        final List owners = CollectionsKt.plus((Collection) listListOf, arrayList);
        final Spinner spinner = new Spinner($act);
        Activity activity2 = $act;
        List list2 = owners;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
        Iterator it = list2.iterator();
        while (it.hasNext()) {
            arrayList2.add((String) ((Pair) it.next()).getFirst());
        }
        spinner.setAdapter((SpinnerAdapter) new ArrayAdapter(activity2, R.layout.simple_spinner_dropdown_item, (List) arrayList2));
        linearLayout.addView(spinner, Ui.lp$default(Ui.INSTANCE, -1, -2, 0.0f, 4, null));
        TextView textViewText$default = Ui.text$default(Ui.INSTANCE, $act, "The new game is private until you make it public on create.roblox.com. Publishing the place into it uses your Open Cloud API key if you've added one (it needs access to the new game).", 11.0f, Ui.INSTANCE.getDIM(), false, 16, null);
        textViewText$default.setSingleLine(false);
        textViewText$default.setPadding(0, Ui.INSTANCE.dp($act, 10.0f), 0, 0);
        linearLayout.addView(textViewText$default);
        AlertDialog.Builder title = Dialogs.INSTANCE.themed($act).setTitle("Create new game");
        ScrollView scrollView = new ScrollView($act);
        scrollView.addView(linearLayout);
        title.setView(scrollView).setPositiveButton("Create", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda13
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                AccountUi.createNewGame$lambda$161$lambda$160$lambda$159(name2, owners, spinner, $act, desc, $studio, dialogInterface, i);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
        return Unit.INSTANCE;
    }

    private static final void createNewGame$lambda$161$lambda$160$label(LinearLayout box, Activity $act, String t) {
        TextView textViewText = Ui.INSTANCE.text($act, t, 12.0f, Ui.INSTANCE.getDIM(), true);
        textViewText.setPadding(0, Ui.INSTANCE.dp($act, 8.0f), 0, Ui.INSTANCE.dp($act, 3.0f));
        box.addView(textViewText);
    }

    static final void createNewGame$lambda$161$lambda$160$lambda$159(EditText $name, List $owners, Spinner $spinner, final Activity $act, final EditText $desc, final Studio $studio, DialogInterface dialogInterface, int i) {
        String string = StringsKt.trim((CharSequence) $name.getText().toString()).toString();
        if (string.length() == 0) {
            string = "My Game";
        }
        final String n = StringsKt.take(string, 50);
        Pair pair = (Pair) CollectionsKt.getOrNull($owners, $spinner.getSelectedItemPosition());
        final long owner = pair != null ? ((Number) pair.getSecond()).longValue() : 0L;
        INSTANCE.background($act, "Creating “" + n + "” on Roblox…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda44
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                try { return RobloxCloud.INSTANCE.createGame($act, n, owner, StringsKt.trim((CharSequence) $desc.getText().toString()).toString()); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda45
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.createNewGame$lambda$161$lambda$160$lambda$159$lambda$158($act, $studio, (Pair) obj);
            }
        });
    }

    static final Unit createNewGame$lambda$161$lambda$160$lambda$159$lambda$158(Activity $act, Studio $studio, Pair pair) {
        Intrinsics.checkNotNullParameter(pair, "<destruct>");
        RobloxCloud.Game g = (RobloxCloud.Game) pair.component1();
        RobloxCloud.Place p = (RobloxCloud.Place) pair.component2();
        OutputLog.INSTANCE.info("Created game “" + g.getName() + "” (place " + p.getId() + ") on Roblox");
        StartCache.INSTANCE.forget($act);
        upload$default(INSTANCE, $act, $studio, new CloudPlace(g.getUniverseId(), p.getId(), g.getName()), true, null, 16, null);
        return Unit.INSTANCE;
    }

    public final void currentPlaceHistory(final Activity act, final Studio studio, final MainActions actions) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        final CloudPlace c = studio.getCloud();
        if (c == null) {
            myGames$default(this, act, studio, actions, null, 8, null);
        } else {
            requireSignIn(act, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda52
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AccountUi.currentPlaceHistory$lambda$168(c, act, actions, studio);
                }
            });
        }
    }

    static final Unit currentPlaceHistory$lambda$168(CloudPlace $c, final Activity $act, final MainActions $actions, final Studio $studio) {
        final RobloxCloud.Game g = new RobloxCloud.Game($c.getUniverseId(), $c.getName(), $c.getPlaceId(), "", "", 0L, 32, null);
        final RobloxCloud.Place p = new RobloxCloud.Place($c.getPlaceId(), $c.getName(), $c.getUniverseId());
        final List items = CollectionsKt.listOf(new Pair[]{TuplesKt.to("Download this place (latest saved version)…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda105
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.currentPlaceHistory$lambda$168$lambda$163($act, $actions, p);
            }
        }), TuplesKt.to("Version history…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda106
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.currentPlaceHistory$lambda$168$lambda$164($act, $studio, $actions, g, p);
            }
        }), TuplesKt.to("Other games…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda107
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.currentPlaceHistory$lambda$168$lambda$165($act, $studio, $actions);
            }
        })});
        AlertDialog.Builder title = Dialogs.INSTANCE.themed($act).setTitle("“" + $c.getName() + "” (place " + $c.getPlaceId() + ")");
        List list = items;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((String) ((Pair) it.next()).getFirst());
        }
        title.setItems((CharSequence[]) ((List) arrayList).toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda108
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ((Function0) ((Pair) items.get(i)).getSecond()).invoke();
            }
        }).setNegativeButton("Close", (DialogInterface.OnClickListener) null).show();
        return Unit.INSTANCE;
    }

    static final Unit currentPlaceHistory$lambda$168$lambda$163(Activity $act, MainActions $actions, RobloxCloud.Place $p) {
        INSTANCE.downloadPlace($act, $actions, $p, null);
        return Unit.INSTANCE;
    }

    static final Unit currentPlaceHistory$lambda$168$lambda$164(Activity $act, Studio $studio, MainActions $actions, RobloxCloud.Game $g, RobloxCloud.Place $p) {
        INSTANCE.versionHistory($act, $studio, $actions, $g, $p);
        return Unit.INSTANCE;
    }

    static final Unit currentPlaceHistory$lambda$168$lambda$165(Activity $act, Studio $studio, MainActions $actions) {
        myGames$default(INSTANCE, $act, $studio, $actions, null, 8, null);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void saveToRoblox$default(AccountUi accountUi, Activity activity, Studio studio, Function0 function0, int i, Object obj) {
        if ((i & 4) != 0) {
            function0 = null;
        }
        accountUi.saveToRoblox(activity, studio, function0);
    }

    public final void saveToRoblox(Activity act, Studio studio, Function0<Unit> onSaved) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        CloudPlace c = studio.getCloud();
        if (c == null) {
            return;
        }
        upload(act, studio, c, false, onSaved);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void upload$default(AccountUi accountUi, Activity activity, Studio studio, CloudPlace cloudPlace, boolean z, Function0 function0, int i, Object obj) {
        if ((i & 16) != 0) {
            function0 = null;
        }
        accountUi.upload(activity, studio, cloudPlace, z, function0);
    }

    public final void upload(final Activity act, final Studio studio, final CloudPlace c, final boolean publish, final Function0<Unit> onDone) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(c, "c");
        requireSignInOrKey(act, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda83
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.upload$lambda$176(act, publish, studio, c, onDone);
            }
        });
    }

    static final Unit upload$lambda$176(final Activity $act, final boolean $publish, final Studio $studio, final CloudPlace $c, final Function0 $onDone) {
        INSTANCE.background($act, $publish ? "Publishing to Roblox…" : "Saving to Roblox…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.upload$lambda$176$lambda$169($studio, $act, $c, $publish);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.upload$lambda$176$lambda$170($studio, $c, $publish, $act, $onDone, (Pair) obj);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(AccountUi.upload$lambda$176$lambda$175($act, $publish, $studio, $c, $onDone, (Throwable) obj));
            }
        });
        return Unit.INSTANCE;
    }

    static final Pair upload$lambda$176$lambda$169(Studio $studio, Activity $act, CloudPlace $c, boolean $publish) {
        byte[] bytes = $studio.serializePlace();
        try { return TuplesKt.to(RobloxCloud.INSTANCE.publish($act, $c.getUniverseId(), $c.getPlaceId(), bytes, $publish), Integer.valueOf(bytes.length)); } catch (Throwable _ktEx) { throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx); }
    }

    static final Unit upload$lambda$176$lambda$170(Studio $studio, CloudPlace $c, boolean $publish, Activity $act, Function0 $onDone, Pair pair) {
        try {
        Intrinsics.checkNotNullParameter(pair, "<destruct>");
        String result = (String) pair.component1();
        int size = ((Number) pair.component2()).intValue();
        $studio.setCloud($c);
        $studio.markSavedToCloud();
        OutputLog.INSTANCE.info(($publish ? "Published" : "Saved") + " " + $c.getName() + " to Roblox (" + result + ", " + (size / 1024) + " KB)");
        INSTANCE.toast($act, $publish ? "Published to Roblox" : "Saved to Roblox");
        Recents.INSTANCE.add($act, new Recents.Entry($c.getName(), $c.getPlaceId(), $c.getUniverseId(), null, 0L, RobloxApi.INSTANCE.userId($act), 24, null));
        if ($onDone != null) {
            $onDone.invoke();
        }
        return Unit.INSTANCE;
    }
        catch (Throwable _ktEx) {
            throw com.goydevv.studrblx.util.KtCompat.sneakyThrow(_ktEx);
        }
    }

    static final boolean upload$lambda$176$lambda$175(final Activity $act, final boolean $publish, final Studio $studio, final CloudPlace $c, final Function0 $onDone, Throwable e) {
        Intrinsics.checkNotNullParameter(e, "e");
        if ((e instanceof PublishException) && ((PublishException) e).getNeedsApiKey()) {
            OutputLog.INSTANCE.warn("Roblox: " + e.getMessage());
            Dialogs.INSTANCE.themed($act).setTitle($publish ? "Publishing needs an API key" : "Saving to Roblox needs an API key").setMessage(e.getMessage()).setPositiveButton("Set up API key", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda21
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    Activity activity = $act;
                    AccountUi.INSTANCE.advanced(activity, new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda54
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return AccountUi.upload$lambda$176$lambda$175$lambda$172$lambda$171(activity, $studio, $c, $publish, $onDone);
                        }
                    });
                }
            }).setNeutralButton("Save a copy to this device", new DialogInterface.OnClickListener() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda22
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    AccountUi.upload$lambda$176$lambda$175$lambda$174($act, $c, $studio, dialogInterface, i);
                }
            }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
            return true;
        }
        return false;
    }

    static final Unit upload$lambda$176$lambda$175$lambda$172$lambda$171(Activity $act, Studio $studio, CloudPlace $c, boolean $publish, Function0 $onDone) {
        INSTANCE.upload($act, $studio, $c, $publish, $onDone);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final void upload$lambda$176$lambda$175$lambda$174(Activity activity, CloudPlace $c, final Studio $studio, DialogInterface dialogInterface, int i) {
        MainActions mainActions = activity instanceof MainActions ? (MainActions) activity : null;
        if (mainActions != null) {
            mainActions.saveBytesAs(INSTANCE.safeFile($c.getName()) + ".rbxl", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda43
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return $studio.serializePlace();
                }
            });
        }
    }

    private final void requireSignInOrKey(Activity act, Function0<Unit> then) {
        if (!signedIn(act)) {
            if (!(RobloxApi.INSTANCE.apiKey(act).length() > 0)) {
                signIn(act, then);
                return;
            }
        }
        then.invoke();
    }

    public static /* synthetic */ void installPluginFromRoblox$default(AccountUi accountUi, Activity activity, Studio studio, MainActions mainActions, String str, int i, Object obj) {
        if ((i & 8) != 0) {
            str = "";
        }
        accountUi.installPluginFromRoblox(activity, studio, mainActions, str);
    }

    public final void installPluginFromRoblox(final Activity act, final Studio studio, final MainActions actions, String initial) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Intrinsics.checkNotNullParameter(initial, "initial");
        Dialogs.INSTANCE.prompt(act, "Plugin asset ID or link", initial, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda23
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.installPluginFromRoblox$lambda$178(act, studio, actions, (String) obj);
            }
        });
    }

    static final Unit installPluginFromRoblox$lambda$178(Activity $act, Studio $studio, MainActions $actions, String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        Long assetId = RobloxApi.INSTANCE.parseAssetId(s);
        if (assetId == null) {
            INSTANCE.toast($act, "Paste a plugin ID or a link containing one");
            return Unit.INSTANCE;
        }
        long id = assetId.longValue();
        INSTANCE.installPluginId($act, $studio, $actions, id, "Plugin_" + id);
        return Unit.INSTANCE;
    }

    public final void installPluginId(final Activity act, final Studio studio, final MainActions actions, final long id, final String name) {
        Intrinsics.checkNotNullParameter(act, "act");
        Intrinsics.checkNotNullParameter(studio, "studio");
        Intrinsics.checkNotNullParameter(actions, "actions");
        Intrinsics.checkNotNullParameter(name, "name");
        background(act, "Downloading plugin…", new Function0() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AccountUi.installPluginId$lambda$179(act, id, studio, name);
            }
        }, new Function1() { // from class: com.goydevv.studrblx.ui.AccountUi$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AccountUi.installPluginId$lambda$180(studio, actions, act, (File) obj);
            }
        });
    }

    static final File installPluginId$lambda$179(Activity $act, long $id, Studio $studio, String $name) {
        byte[] bytes = RobloxApi.downloadAsset$default(RobloxApi.INSTANCE, $act, $id, null, 4, null);
        return $studio.getPlugins().install($name, bytes);
    }

    static final Unit installPluginId$lambda$180(Studio $studio, MainActions $actions, Activity $act, File f) {
        Intrinsics.checkNotNullParameter(f, "f");
        $studio.getPlugins().load(f);
        $studio.notifyPluginButtons();
        $actions.pluginsChanged();
        INSTANCE.toast($act, "Installed " + FilesKt.getNameWithoutExtension(f));
        return Unit.INSTANCE;
    }
}
