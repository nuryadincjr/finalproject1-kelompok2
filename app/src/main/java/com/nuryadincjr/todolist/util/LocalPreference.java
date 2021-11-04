package com.nuryadincjr.todolist.util;

import android.content.*;

import com.nuryadincjr.todolist.pojo.Constaint;

public class LocalPreference {
    private static LocalPreference instance;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public LocalPreference(Context context) {
        preferences = context.getSharedPreferences(Constaint.TITLE_VIW_ONLY, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static LocalPreference getInstance(Context context) {
        if(instance == null) {
            instance = new LocalPreference(context);
        }
        return instance;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}