package com.taranita.myblog.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "MY_BLOG_PREF";

    private String KEY_EMAIL = "pref_email";

    public SharedPrefManager() {
    }

    public void saveEmail(Context context, String data){
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        preferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(KEY_EMAIL, data);
        editor.apply();
    }

    public String getEmail(Context context){
        String data;
        SharedPreferences preferences;
        preferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        data = preferences.getString(KEY_EMAIL, "");

        return data;
    }
}
