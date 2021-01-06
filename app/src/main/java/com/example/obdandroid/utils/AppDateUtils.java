package com.example.obdandroid.utils;

import android.annotation.SuppressLint;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：Jealous
 * 日期：2021/1/6 0006
 * 描述：
 */
public class AppDateUtils {
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_T = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_Z = "EEE MMM dd HH:mm:ss Z yyyy";

    /**
     * 处理时间格式 2019-11-28T06:52:09.724+0000 为 yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String dealDateFormat(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        try {
            DateFormat df = new SimpleDateFormat(FORMAT_T);
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat(FORMAT_Z, Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat(FORMAT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return df2.format(date1);
    }
}