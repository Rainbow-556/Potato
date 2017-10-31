package com.lx.potato.model.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.lx.potato.PotatoApp;

/**
 * Created by lixiang on 2017/10/28.
 */
public final class SharedPrefHelper {
    private static final Application APP = PotatoApp.get();
    public static final String SHARED_FILE_DEFAULT = "sp_default";
    public static final String SHARED_FILE_CACHE = "sp_cache";

    private static SharedPreferences getSharedPreferences(String name) {
        return APP.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void put(String key, Object value) {
        put(SHARED_FILE_DEFAULT, key, value);
    }

    public void put(String fileName, String key, Object value) {
        SharedPreferences.Editor editor = getSharedPreferences(fileName).edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.apply();
    }

    public Object get(String key, Object defaultValue) {
        return get(SHARED_FILE_DEFAULT, key, defaultValue);
    }

    public Object get(String fileName, String key, Object defaultValue) {
        SharedPreferences sp = getSharedPreferences(fileName);
        if (defaultValue instanceof String) {
            return sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(key, (Long) defaultValue);
        }
        return defaultValue;
    }

    public void remove(String key) {
        remove(SHARED_FILE_DEFAULT, key);
    }

    /**
     * 移除指定share文件的key
     *
     * @param name
     * @param key
     */
    public void remove(String name, String key) {
        getSharedPreferences(name).edit().remove(key).apply();
    }

    public boolean contains(String key) {
        return contains(SHARED_FILE_DEFAULT, key);
    }

    /**
     * 判断是否存在指定key
     *
     * @param name 文件名
     * @param key
     */
    public boolean contains(String name, String key) {
        return getSharedPreferences(name).contains(key);
    }

    /**
     * 移除指定share文件的所有内容
     *
     * @param name
     */
    public void clear(String name) {
        getSharedPreferences(name).edit().clear().apply();
    }
}
