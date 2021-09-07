package com.example.obdandroid.ui.view.popwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 作者：Jealous
 * 日期：2021/8/23 0023
 * 描述：
 */
public class CustomPop extends BasePop {

    private CustomPopWindow alertDialog;
    private View rootView;
    private View anchor;
    private Context context;
    private BindView bindView;

    private CustomPop() {
    }

    public static CustomPop show(Context context, int layoutResId, View anchor) {
        View customView = LayoutInflater.from(context).inflate(layoutResId, null);
        CustomPop customDialog = build(context, customView, anchor, null);
        customDialog.showDialog();
        return customDialog;
    }

    public static CustomPop show(Context context, int layoutResId, View anchor, BindView bindView) {
        View customView = LayoutInflater.from(context).inflate(layoutResId, null);
        CustomPop customDialog = build(context, customView, anchor, bindView);
        customDialog.showDialog();
        return customDialog;
    }

    public static CustomPop show(Context context, View rootView, View anchor, BindView bindView) {
        CustomPop customDialog = build(context, rootView, anchor, bindView);
        customDialog.showDialog();
        return customDialog;
    }

    public static CustomPop build(Context context, View rootView, View anchor, BindView bindView) {
        synchronized (CustomPop.class) {
            CustomPop customDialog = new CustomPop();
            customDialog.alertDialog = null;
            customDialog.context = context;
            customDialog.bindView = bindView;
            customDialog.anchor = anchor;
            customDialog.rootView = rootView;
            customDialog.log("装载自定义对话框");
            return customDialog;
        }
    }

    public static CustomPop build(Context context, int layoutResId, View anchor, BindView bindView) {
        View customView = LayoutInflater.from(context).inflate(layoutResId, null);
        return build(context, customView, anchor, bindView);
    }


    @Override
    public void showDialog() {
        log("启动自定义对话框");
        alertDialog = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(rootView)
                .create()
                .showAsDropDown(anchor, 0, 20, Gravity.CENTER);
        if (bindView != null) bindView.onBind(this, rootView);
    }

    @Override
    public void doDismiss() {
        if (alertDialog != null) alertDialog.onDismiss();
    }

    public interface BindView {
        void onBind(CustomPop dialog, View rootView);
    }
}
