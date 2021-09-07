package com.example.obdandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xialo on 2016/7/25.
 */
public class SharedPreferencesUtil {

    private static final String spFileName = "welcomePage";
    public static final String FIRST_OPEN = "first_open";

    public static Boolean getBoolean(Context context, String strKey, Boolean strDefault) {
        SharedPreferences setPreferences = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        return setPreferences.getBoolean(strKey, strDefault);
    }

    public static void putBoolean(Context context, String strKey, Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.apply();
    }


}
