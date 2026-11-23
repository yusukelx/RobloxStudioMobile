package com.goydevv.studrblx;

import android.app.ActivityManager;
import androidx.emoji2.bundled.BundledEmojiCompatConfig;
import androidx.emoji2.text.EmojiCompat;
import android.app.Application;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Process;
import com.goydevv.studrblx.core.OutputLog;
import com.goydevv.studrblx.core.Settings;
import com.goydevv.studrblx.core.Studio;
import com.goydevv.studrblx.ui.Ui;
import com.goydevv.studrblx.ui.RichText;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;

public final class StudioApp extends Application {
    public static final String LAST_CRASH_FILE = "last_crash.txt";
    public static final String SESSION_LOG_FILE = "session_log.txt";
    private static final String CRASH_PREFS = "crash_detector_prefs";
    private static final String KEY_LAST_EXIT_TS = "last_seen_exit_ts";
    private static final String KEY_LAST_JAVA_CRASH_TS = "last_java_crash_ts";

    private static final Object LOG_LOCK = new Object();
    private static final ArrayDeque<String> BREADCRUMBS = new ArrayDeque<>(120);
    private static volatile Context appContext;
    private static ScheduledExecutorService logFlusher;

    private boolean placeOpened;
    private boolean started;

    private final Lazy studio = LazyKt.lazy(new Function0() {
        @Override
        public final Object invoke() {
            return StudioApp.studio_delegate$lambda$0(StudioApp.this);
        }
    });

    static final Studio studio_delegate$lambda$0(StudioApp this$0) {
        return new Studio(this$0);
    }

    public final Studio getStudio() {
        return (Studio) this.studio.getValue();
    }

    public final boolean getStarted() {
        return this.started;
    }

    public final void setStarted(boolean z) {
        this.started = z;
    }

    public final boolean getPlaceOpened() {
        return this.placeOpened;
    }

    public final void setPlaceOpened(boolean z) {
        this.placeOpened = z;
    }

    public static void logBreadcrumb(String msg) {
        if (msg == null) return;
        String ts = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(new Date());
        synchronized (LOG_LOCK) {
            if (BREADCRUMBS.size() >= 120) {
                BREADCRUMBS.removeFirst();
            }
            BREADCRUMBS.addLast("[" + ts + "] " + msg);
        }
    }

    private static void flushSessionLog(Context ctx) {
        if (ctx == null) return;
        try {
            StringBuilder sb = new StringBuilder(4096);
            sb.append("=== StudioRblx Session Log ===\n");
            sb.append("Updated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date())).append('\n');
            synchronized (LOG_LOCK) {
                if (!BREADCRUMBS.isEmpty()) {
                    sb.append("--- Breadcrumbs ---\n");
                    for (String line : BREADCRUMBS) {
                        sb.append(line).append('\n');
                    }
                }
            }
            try {
                List<OutputLog.Entry> entries = OutputLog.INSTANCE.snapshot();
                if (entries != null && !entries.isEmpty()) {
                    sb.append("--- Studio Output ---\n");
                    int start = Math.max(0, entries.size() - 80);
                    for (int idx = start; idx < entries.size(); idx++) {
                        OutputLog.Entry en = entries.get(idx);
                        sb.append('[').append(en.getLevel()).append("] ").append(en.getText()).append('\n');
                    }
                }
            } catch (Throwable ignored) {
            }
            File out = new File(ctx.getFilesDir(), SESSION_LOG_FILE);
            try (FileOutputStream fos = new FileOutputStream(out)) {
                fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                fos.flush();
            }
        } catch (Throwable ignored) {
        }
    }

    private static String readSavedSessionLog(Context ctx) {
        try {
            File f = new File(ctx.getFilesDir(), SESSION_LOG_FILE);
            if (f.isFile() && f.length() > 0) {
                return new String(FilesKt.readBytes(f), StandardCharsets.UTF_8);
            }
        } catch (Throwable ignored) {
        }
        return "";
    }

    private static String readLogcatTail(int lines) {
        StringBuilder sb = new StringBuilder();
        try {
            java.lang.Process proc = Runtime.getRuntime().exec(new String[]{"logcat", "-d", "-t", String.valueOf(lines), "*:W"});
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            br.close();
        } catch (Throwable ignored) {
        }
        return sb.toString();
    }

    public static String[] checkPreviousCrashReport(Context ctx) {
        if (ctx == null) return null;
        try {
            // 1. Check pending Java crash report saved before process death
            File pending = new File(ctx.getFilesDir(), LAST_CRASH_FILE);
            if (pending.isFile() && pending.length() > 0) {
                String report = new String(FilesKt.readBytes(pending), StandardCharsets.UTF_8);
                return new String[]{report, pending.getAbsolutePath()};
            }
        } catch (Throwable ignored) {
        }

        // 2. On Android 11+ (API 30+), inspect ActivityManager historical exit reasons
        //    to catch native crashes (SIGSEGV/GLES), ANRs, OOM kills, or crashes where
        //    UncaughtExceptionHandler was bypassed by the OS.
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
                if (am != null) {
                    List<ApplicationExitInfo> exits = am.getHistoricalProcessExitReasons(ctx.getPackageName(), 0, 5);
                    SharedPreferences sp = ctx.getSharedPreferences(CRASH_PREFS, Context.MODE_PRIVATE);
                    long lastSeenTs = sp.getLong(KEY_LAST_EXIT_TS, 0L);
                    long lastJavaTs = sp.getLong(KEY_LAST_JAVA_CRASH_TS, 0L);
                    if (exits != null && !exits.isEmpty()) {
                        for (ApplicationExitInfo info : exits) {
                            if (info == null) continue;
                            String pName = info.getProcessName();
                            if (pName != null && pName.endsWith(":crash")) {
                                continue;
                            }
                            long ts = info.getTimestamp();
                            if (ts <= lastSeenTs) {
                                break;
                            }
                            int reason = info.getReason();
                            if (reason == ApplicationExitInfo.REASON_LOW_MEMORY) {
                                sp.edit().putLong(KEY_LAST_EXIT_TS, ts).apply();
                                continue;
                            }
                            boolean isCrash = reason == ApplicationExitInfo.REASON_CRASH
                                    || reason == ApplicationExitInfo.REASON_CRASH_NATIVE
                                    || reason == ApplicationExitInfo.REASON_ANR
                                    || reason == ApplicationExitInfo.REASON_EXCESSIVE_RESOURCE_USAGE
                                    || reason == ApplicationExitInfo.REASON_SIGNALED;
                            if (!isCrash) {
                                continue;
                            }
                            sp.edit().putLong(KEY_LAST_EXIT_TS, ts).apply();
                            // Skip if this exact Java crash (or our own Process.killProcess SIGKILL) was already shown immediately by CrashActivity
                            if ((reason == ApplicationExitInfo.REASON_CRASH || reason == ApplicationExitInfo.REASON_SIGNALED) && Math.abs(ts - lastJavaTs) < 10000L) {
                                continue;
                            }
                            String report = buildHistoricalExitReport(ctx, info);
                            String savedPath = saveExternalCrashCopy(ctx, report);
                            return new String[]{report, savedPath};
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    private static String reasonName(int reason) {
        switch (reason) {
            case 4: return "REASON_CRASH (Java Exception)";
            case 5: return "REASON_CRASH_NATIVE (Native Signal/GLES)";
            case 6: return "REASON_ANR (Application Not Responding)";
            case 3: return "REASON_LOW_MEMORY (OOM Kill)";
            case 9: return "REASON_EXCESSIVE_RESOURCE_USAGE";
            case 2: return "REASON_SIGNALED";
            default: return "REASON_" + reason;
        }
    }

    private static String buildHistoricalExitReport(Context ctx, ApplicationExitInfo info) {
        StringBuilder sb = new StringBuilder(4096);
        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(info.getTimestamp()));
        sb.append("=== StudioRblx Previous Crash Detected ===\n");
        sb.append("Crash Time: ").append(ts).append('\n');
        sb.append("Package: ").append(ctx.getPackageName()).append('\n');
        sb.append("Device: ").append(Build.MANUFACTURER).append(' ').append(Build.MODEL)
          .append(" (Android ").append(Build.VERSION.RELEASE).append(" / SDK ").append(Build.VERSION.SDK_INT).append(")\n");
        sb.append("Exit Reason: ").append(reasonName(info.getReason())).append('\n');
        sb.append("Status / Signal: ").append(info.getStatus()).append('\n');
        if (info.getDescription() != null && !info.getDescription().isEmpty()) {
            sb.append("Description: ").append(info.getDescription()).append('\n');
        }
        sb.append('\n');

        try (InputStream is = info.getTraceInputStream()) {
            if (is != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[4096];
                int n;
                int total = 0;
                while ((n = is.read(buf)) > 0 && total < 131072) {
                    bos.write(buf, 0, n);
                    total += n;
                }
                String trace = new String(bos.toByteArray(), StandardCharsets.UTF_8);
                if (!trace.isEmpty()) {
                    sb.append("--- OS Crash / Native Trace ---\n");
                    sb.append(trace).append("\n\n");
                }
            }
        } catch (Throwable ignored) {
        }

        String sessionLog = readSavedSessionLog(ctx);
        if (!sessionLog.isEmpty()) {
            sb.append(sessionLog).append('\n');
        }

        String logcat = readLogcatTail(150);
        if (!logcat.isEmpty()) {
            sb.append("--- Logcat Tail ---\n");
            sb.append(logcat);
        }
        return sb.toString();
    }

    private static String saveExternalCrashCopy(Context ctx, String report) {
        String savedPath = "";
        try {
            File extBase = ctx.getExternalFilesDir(null);
            if (extBase != null) {
                File dir = new File(extBase, "crash_logs");
                dir.mkdirs();
                String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                File out = new File(dir, "StudioRblx_crash_" + stamp + ".txt");
                try (FileOutputStream fos = new FileOutputStream(out)) {
                    fos.write(report.getBytes(StandardCharsets.UTF_8));
                    fos.flush();
                    try { fos.getFD().sync(); } catch (Throwable ignoredSync) {}
                }
                savedPath = out.getAbsolutePath();
            }
        } catch (Throwable ignored) {
        }
        return savedPath;
    }

    private String buildFullCrashReport(Thread t, Throwable e) {
        StringBuilder sb = new StringBuilder(4096);
        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
        sb.append("=== StudioRblx Crash Report ===\n");
        sb.append("Time: ").append(ts).append('\n');
        sb.append("Package: ").append(getPackageName()).append('\n');
        sb.append("Device: ").append(Build.MANUFACTURER).append(' ').append(Build.MODEL)
          .append(" (Android ").append(Build.VERSION.RELEASE).append(" / SDK ").append(Build.VERSION.SDK_INT).append(")\n");
        sb.append("Thread: ").append(t != null ? t.getName() : "unknown").append("\n\n");

        sb.append("--- Stack Trace ---\n");
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        sb.append(sw.toString()).append('\n');

        synchronized (LOG_LOCK) {
            if (!BREADCRUMBS.isEmpty()) {
                sb.append("--- Session Breadcrumbs ---\n");
                for (String b : BREADCRUMBS) {
                    sb.append(b).append('\n');
                }
                sb.append('\n');
            }
        }

        try {
            List<OutputLog.Entry> entries = OutputLog.INSTANCE.snapshot();
            if (entries != null && !entries.isEmpty()) {
                sb.append("--- Recent Studio Output ---\n");
                int start = Math.max(0, entries.size() - 80);
                for (int idx = start; idx < entries.size(); idx++) {
                    OutputLog.Entry en = entries.get(idx);
                    sb.append('[').append(en.getLevel()).append("] ").append(en.getText()).append('\n');
                }
                sb.append('\n');
            }
        } catch (Throwable ignored) {
        }

        String logcat = readLogcatTail(150);
        if (!logcat.isEmpty()) {
            sb.append("--- Logcat Tail ---\n");
            sb.append(logcat);
        }
        return sb.toString();
    }

    private String saveCrashToFiles(String report) {
        String savedPath = "";
        byte[] bytes = report.getBytes(StandardCharsets.UTF_8);
        try {
            File internal = new File(getFilesDir(), LAST_CRASH_FILE);
            try (FileOutputStream fos = new FileOutputStream(internal)) {
                fos.write(bytes);
                fos.flush();
                try { fos.getFD().sync(); } catch (Throwable ignoredSync) {}
            }
            savedPath = internal.getAbsolutePath();
        } catch (Throwable ignored) {
        }
        String extPath = saveExternalCrashCopy(this, report);
        if (!extPath.isEmpty()) {
            savedPath = extPath;
        }
        return savedPath;
    }

    private boolean isCrashProcess() {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                String pn = Application.getProcessName();
                return pn != null && pn.endsWith(":crash");
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        if (isCrashProcess()) {
            return;
        }
        logBreadcrumb("App process started");

        final Thread.UncaughtExceptionHandler prev = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                try {
                    logBreadcrumb("Uncaught exception on " + (t != null ? t.getName() : "unknown") + ": " + e);
                    flushSessionLog(StudioApp.this);
                    // Save raw stack trace FIRST so even if logcat/OutputLog fails or OS kills process, reopen ALWAYS shows crash screen
                    StringWriter sw0 = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw0));
                    String quickReport = "=== StudioRblx Crash ===\nThread: " + (t != null ? t.getName() : "unknown") + "\n\n" + sw0.toString();
                    String savedPath = saveCrashToFiles(quickReport);
                    String report = quickReport;
                    try {
                        report = buildFullCrashReport(t, e);
                        savedPath = saveCrashToFiles(report);
                    } catch (Throwable ignored2) {
                    }
                    try {
                        getSharedPreferences(CRASH_PREFS, Context.MODE_PRIVATE)
                                .edit()
                                .putLong(KEY_LAST_JAVA_CRASH_TS, System.currentTimeMillis())
                                .commit();
                    } catch (Throwable ignoredPrefs) {
                    }
                    Intent i = new Intent(StudioApp.this, CrashActivity.class);
                    i.putExtra(CrashActivity.EXTRA_TRACE, report);
                    i.putExtra(CrashActivity.EXTRA_SAVED_PATH, savedPath);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    try { Thread.sleep(500L); } catch (InterruptedException ignored) {}
                    Process.killProcess(Process.myPid());
                    System.exit(10);
                } catch (Throwable ignored) {
                    if (prev != null) prev.uncaughtException(t, e);
                }
            }
        });

        try {
            logFlusher = Executors.newSingleThreadScheduledExecutor();
            logFlusher.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    flushSessionLog(StudioApp.this);
                }
            }, 3L, 4L, TimeUnit.SECONDS);
        } catch (Throwable ignored) {
        }

        try {
            EmojiCompat.init(new BundledEmojiCompatConfig(this).setReplaceAll(true)).registerInitCallback(new EmojiCompat.InitCallback() {
                @Override
                public void onInitialized() {
                    RichText.INSTANCE.clearCache();
                }
            });
        } catch (Throwable ignored) {
        }

        Settings.INSTANCE.init(this);
        Ui.INSTANCE.applyTheme(Settings.INSTANCE.getTheme());
    }
}
