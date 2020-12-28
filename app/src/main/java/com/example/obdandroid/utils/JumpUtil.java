package com.example.obdandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.obdandroid.R;
import com.example.obdandroid.config.Constant;


/**
 * Activity跳转工具类
 */
public class JumpUtil {

    /**
     * 跳转并传递数据
     *
     * @param context 上下文对象
     * @param classs
     * @param pamar
     */
    public static void startActToData(Context context, Class classs, String pamar, int flag) {
        Intent intent = new Intent(context, classs);
        intent.putExtra(Constant.ACT_FLAG, pamar);
        intent.putExtra(Constant.INT_FLAG, flag);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
    }

    /**
     * 跳转
     *
     * @param context
     * @param classs
     */
    public static void startAct(Context context, Class classs) {
        Intent intent = new Intent(context, classs);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
    }

    /**
     * 跳转
     *
     * @param context
     * @param classs
     */
    public static void startActBundle(Context context, Class classs, Bundle bundle) {
        Intent intent = new Intent(context, classs);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);//下往上推出效果
    }


    public static void startBaseActivityForResult(Activity activity, Class classs,
                                                  Bundle extras, int requestCode) {
        Intent intent = new Intent(activity.getApplicationContext(), classs);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivityForResult(intent, requestCode);
    }

}
