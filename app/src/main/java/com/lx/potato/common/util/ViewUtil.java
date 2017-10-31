package com.lx.potato.common.util;

import android.app.Activity;
import android.view.View;

/**
 * Created by lixiang on 2017/10/31.
 */
public final class ViewUtil {
    private ViewUtil() {}

    public static <T extends View> T findView(Activity activity, int id) {
        if (activity != null) {
            return (T) activity.findViewById(id);
        }
        return null;
    }

    public static <T extends View> T findView(View view, int id) {
        if (view != null) {
            return (T) view.findViewById(id);
        }
        return null;
    }
}
