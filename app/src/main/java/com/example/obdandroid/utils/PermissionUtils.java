package com.example.obdandroid.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

/**
 * 作者：Jealous
 * 日期：2021/9/8 0008
 * 描述：
 */
public class PermissionUtils {
    private static boolean isPermissionRequested;

    /**
     * Android6.0之后需要动态申请权限
     */
    public static void requestPermission(Activity activity, String[] permissions) {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {
            isPermissionRequested = true;
            ArrayList<String> permissionsList = new ArrayList<>();

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(perm)) {
                    permissionsList.add(perm);
                }
            }

            if (!permissionsList.isEmpty()) {
                String[] strings = new String[permissionsList.size()];
                activity.requestPermissions(permissionsList.toArray(strings), 0);
            }
        }
    }
}