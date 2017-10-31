package com.lx.potato.common.util;

import android.util.Log;

public final class LogUtil {
    private static final boolean LOG_ENABLED = true;
    private static final String TAG_MSG = "CommonMsg";
    private static final String TAG_API_SERVICE = "ApiService";

    public static boolean isLogEnabled() {
        return LOG_ENABLED;
    }

    public static void logApiRequest(String msg) {
        if (LOG_ENABLED)
            Log.i(TAG_API_SERVICE, msg);
    }

    public static void i(String tag, String msg) {
        if (LOG_ENABLED)
            Log.i(tag, msg);
    }

    public static void msg(String msg) {
        if (LOG_ENABLED)
            Log.d(TAG_MSG, msg);
    }

    public static void d(String tag, String msg) {
        if (LOG_ENABLED)
            Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_ENABLED)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (LOG_ENABLED) {
            Log.e(tag, msg);
        }
    }

    public static void logException(Exception e) {
        if (LOG_ENABLED && e != null) {
            e.printStackTrace();
        }
    }
}
