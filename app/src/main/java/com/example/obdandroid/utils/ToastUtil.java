package com.example.obdandroid.utils;

import android.widget.Toast;

import com.example.obdandroid.MainApplication;

/**
 * ToastUtils
 */
public class ToastUtil {

    private ToastUtil() {
        throw new AssertionError();
    }

    //长
    public static void show(String message) {
        Toast.makeText(MainApplication.context, message, Toast.LENGTH_LONG).show();
    }

    //短
    public static void shortShow(String message) {
        Toast.makeText(MainApplication.context, message, Toast.LENGTH_SHORT).show();
    }
}
