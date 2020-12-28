package com.example.obdandroid.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2018/11/2 0002 10:20
 */

public class ActivityManager {

    private List<Activity> activityList = new ArrayList<>();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {

        return ActivityManagerHolder.Instantce;
    }

    /**
     * 静态内部类获取单例
     */
    static class ActivityManagerHolder {
        public static ActivityManager Instantce = new ActivityManager();

    }

    /**
     * 添加activity
     * @param activity
     */
    public void addActivity(Activity activity){
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }

    }

    /**
     * 移除activity
     * @param activity
     */
    public void removeActivity(Activity activity){
        if (activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }

    /**
     * 关闭所有的activity，退出应用
     */
    public void finishActivitys(){
        if (activityList != null&&!activityList.isEmpty()) {
            for (Activity activity1 : activityList) {
                activity1.finish();
            }
            activityList.clear();
        }
    }
}
