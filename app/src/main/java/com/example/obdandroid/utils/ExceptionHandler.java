package com.example.obdandroid.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 作者：Jealous
 * 日期：2018/1/3 0003 09:49
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionHandler handler = new ExceptionHandler();
    private Thread.UncaughtExceptionHandler defaultHandler;
    private File saveSpacePath;
    private File localErrorSave;
    private Context context;
    private StringBuilder sb = new StringBuilder();
    private OnCallBackError callBackError;

    private ExceptionHandler() {
    }

    public static ExceptionHandler getInstance() {
        return handler;
    }

    public void initConfig(Context context, OnCallBackError callBackError) {
        this.context = context;
        this.callBackError = callBackError;
        saveSpacePath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/ODBCar/ErrorLog/");
        localErrorSave = new File(saveSpacePath, "ODBError.txt");
        if (!saveSpacePath.exists()) {
            saveSpacePath.mkdirs();
        }
        if (!localErrorSave.exists()) {
            try {
                localErrorSave.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread t, Throwable e) {
        writeErrorToLocal(t, e);
    }


    private void writeErrorToLocal(Thread t, Throwable e) {
        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(localErrorSave, true));
            PackageManager packageManager = context.getPackageManager();
            String line = "\n----------------------------------------------------------------------------------------\n";
            sb.append(line);
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            sb.append(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())) + "<---->" +
                    "包名::" + packageInfo.packageName + "<---->版本名::" + packageInfo.versionName + "<---->版本号::" + packageInfo.versionCode + "\n");
         /*   sb.append("手机制造商::" + Build.MANUFACTURER + "\n");
            sb.append("手机型号::" + Build.MODEL + "\n");
            sb.append("CPU架构::" + Build.CPU_ABI + "\n");*/
            sb.append(e.toString() + "\n");
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement traceElement : trace)
                sb.append("\n\tat " + traceElement);
            sb.append("\n");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Throwable[] suppressed = e.getSuppressed();
                for (Throwable se : suppressed)
                    sb.append("\tat " + se.getMessage());
            }
            callBackError.Error(sb.toString());
            fos.write(sb.toString());
            fos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            defaultHandler.uncaughtException(t, e1);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
            defaultHandler.uncaughtException(t, e1);
        }
    }

    public interface OnCallBackError {
        void Error(String error);
    }
}
