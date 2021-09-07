package com.example.obdandroid.ui.view.popwindow;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.obdandroid.ui.view.popwindow.PopSettings.DEBUGMODE;


/**
 * 作者：Jealous
 * 日期：2021/8/23 0023
 * 描述：
 */
public abstract class BasePop {

    protected static List<BasePop> dialogList = new ArrayList<>();         //对话框队列

    public boolean isDialogShown = false;

    public void log(Object o) {
        if (DEBUGMODE) Log.i("DialogSDK >>>", o.toString());
    }

    public abstract void showDialog();

    public abstract void doDismiss();

    public static void unloadAllDialog(){
        try{
            for (BasePop baseDialog:dialogList){
                baseDialog.doDismiss();
            }
            dialogList = new ArrayList<>();
        }catch (Exception e){
            if (DEBUGMODE)e.printStackTrace();
        }
    }
}
