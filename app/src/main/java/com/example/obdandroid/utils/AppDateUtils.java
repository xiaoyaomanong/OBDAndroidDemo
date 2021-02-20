package com.example.obdandroid.utils;

import android.annotation.SuppressLint;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 作者：Jealous
 * 日期：2021/1/6 0006
 * 描述：
 */
public class AppDateUtils {
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_ONE = "yyyy-MM-dd";
    public static final String FORMAT_T = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_Z = "EEE MMM dd HH:mm:ss Z yyyy";


    /* HH:mm:ss*/
    public static String getTodayDateTimeHms() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        return format.format(new Date());
    }

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
            if (date != null) {
                date1 = df1.parse(date.toString());
            }
            df2 = new SimpleDateFormat(FORMAT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return df2.format(date1);
    }

    public static String dealDateFormatYMD(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        try {
            DateFormat df = new SimpleDateFormat(FORMAT_T);
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat(FORMAT_Z, Locale.UK);
            if (date != null) {
                date1 = df1.parse(date.toString());
            }
            df2 = new SimpleDateFormat(FORMAT_ONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return df2.format(date1);
    }

    /* HH:mm:ss*/
    public static String getTodayDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * @param date 指定时间
     * @param day  天数
     * @return 指定时间添加几天获取时间
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static String addDate(String date, long day) throws ParseException {
        long time = timeFormat(date).getTime(); // 得到指定日期的毫秒数
        day = (day - 1) * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += day; // 相加得到新的毫秒数
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(time)); // 将毫秒数转换成日期
    }

    /**
     * @param day yyyy-MM-dd 格式时间串
     * @return 将yyyy-MM-dd 格式字符串转成Date
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static Date timeFormat(String day) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//定义一个formate
        return simpleDateFormat.parse(day);
    }

    public static int isAM_PM() {
        ////结果为“0”是上午 结果为“1”是下午
        GregorianCalendar ca = new GregorianCalendar();
        return ca.get(GregorianCalendar.AM_PM);
    }


    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isDateTwoBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = false;
        } else if (dt1.getTime() <= dt2.getTime()) {
            isBigger = true;
        }
        return isBigger;
    }

}