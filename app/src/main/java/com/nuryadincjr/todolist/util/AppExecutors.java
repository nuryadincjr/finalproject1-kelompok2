package com.nuryadincjr.todolist.util;

import android.os.*;

import com.nuryadincjr.todolist.pojo.Constaint;

import java.util.concurrent.*;

public class AppExecutors {
    private static final Object LOCK = new Object();
    private static AppExecutors instance;
    private final Executor diskID;
    private final Executor mainThread;
    private final Executor networkThread;

    public AppExecutors(Executor diskID, Executor mainThread, Executor networkThread) {
        this.diskID = diskID;
        this.mainThread = mainThread;
        this.networkThread = networkThread;
    }

    public static AppExecutors getInstance() {
        if(instance == null) {
            synchronized (LOCK) {
                instance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(Constaint.NUMBER_OF_THREADS),
                        new MainThreadExecutor());
            }
        }
        return instance;
    }

    public Executor diskID() {
        return diskID;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkID() {
        return networkThread;
    }

    public static class MainThreadExecutor implements Executor {
        private final Handler manThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            manThreadHandler.post(runnable);
        }
    }
}