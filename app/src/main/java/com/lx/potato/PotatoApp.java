package com.lx.potato;

import android.app.Application;

/**
 * Created by lixiang on 2017/10/31.
 */
public final class PotatoApp extends Application {
    private static PotatoApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static PotatoApp get() {
        return sInstance;
    }
}
