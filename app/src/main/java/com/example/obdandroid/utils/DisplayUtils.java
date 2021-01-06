package com.example.obdandroid.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Author： Jealous_lzj
 * Date： 2017/8/21 0021.
 * 获取屏幕大小
 */

public class DisplayUtils {
    private static Context contexts;
    private static WindowManager wm;
    private DisplayUtils() {}
    private static DisplayUtils single=null;
    //静态工厂方法
    public static DisplayUtils getInstance(Context context) {
        contexts=context;
        wm = (WindowManager) contexts.getSystemService(Context.WINDOW_SERVICE);
        if (single == null) {
            single = new DisplayUtils();
        }
        return single;
    }

    public static  int getscreenWidth(){
        return wm.getDefaultDisplay().getWidth();
    }
    public static int getscreenHeight(){
        return wm.getDefaultDisplay().getHeight();
    }
}
