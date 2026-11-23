package com.goydevv.studrblx.util;

import android.os.Process;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class BackgroundPool {

    private BackgroundPool() {
    }

    public static ExecutorService create(int threads) {
        return Executors.newFixedThreadPool(threads, r -> new Thread(() -> {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            r.run();
        }));
    }
}
