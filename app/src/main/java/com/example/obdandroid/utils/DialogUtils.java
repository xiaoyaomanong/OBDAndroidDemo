package com.example.obdandroid.utils;

import android.content.Context;

import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.WaitDialog;

import static com.kongzue.dialog.v2.DialogSettings.STYLE_IOS;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;

/**
 * 作者：Jealous
 * 日期：2017/10/19 0019 09:39
 */

public class DialogUtils {
    private Context mContext;

    public DialogUtils(Context context) {
        this.mContext = context;
        DialogSettings.dialog_theme = THEME_LIGHT;
        DialogSettings.tip_theme = THEME_LIGHT;
        DialogSettings.style = STYLE_IOS;
    }

    public void showProgressDialog() {
        WaitDialog.setCanCancelGlobal(true);
        WaitDialog.show(mContext, "请稍候...").setOnBackPressListener(alertDialog -> {
            alertDialog.dismiss();
        });
    }
    public void showProgressDialog(String msg) {
        WaitDialog.setCanCancelGlobal(true);
        WaitDialog.show(mContext, msg).setOnBackPressListener(alertDialog -> {
            alertDialog.dismiss();
        });
    }

    public void dismiss() {
        WaitDialog.dismiss();
    }
}
