package com.example.obdandroid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.obdandroid.R;

/**
 * 作者：Jealous
 * 日期：2021/1/12 0012
 * 描述：
 */
public class CountDownTimerUtils extends CountDownTimer {
    private final TextView mTextView;
    private final Context context;

    public CountDownTimerUtils(TextView textView, Context context, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "s");  //设置倒计时时间
        mTextView.setBackgroundResource(R.drawable.btn_shape_gre_bg);//设置按钮为灰色，这时是不能点击的
        mTextView.setTextColor(context.getResources().getColor(R.color.gray));
    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取");
        mTextView.setClickable(true);//重新获得点击
        mTextView.setBackgroundResource(R.drawable.btn_shape_white_bg);  //还原背景色
        mTextView.setTextColor(context.getResources().getColor(R.color.white));
    }
}