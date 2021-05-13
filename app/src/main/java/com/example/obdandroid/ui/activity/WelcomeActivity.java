package com.example.obdandroid.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFullScreenActivity;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.utils.AppDateUtils;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SPUtil;
import com.example.obdandroid.utils.SharedPreferencesUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import notchtools.geek.com.notchtools.NotchTools;
import notchtools.geek.com.notchtools.core.NotchProperty;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.example.obdandroid.config.Constant.EXPIRE_TIME;

public class WelcomeActivity extends BaseFullScreenActivity implements OnNotchCallBack {
    private static final int ANIM_TIME = 2000;
    private static final float SCALE_END = 1.15F;
    private ImageView ivEntry;
    private ImageView imgBack;
    private Context context;
    private boolean ISLOGIN = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        setTheme(R.style.AppThemes);//恢复原有的样式
        context = this;
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(context, SharedPreferencesUtil.FIRST_OPEN, true);
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            JumpUtil.startAct(context,WelcomeGuideActivity.class);
            finish();
            return;
        }
        startMainActivity();
    }
    private void startMainActivity() {
        setContentView(getContentViewId());
        ivEntry = findViewById(R.id.iv_entry);
        imgBack = findViewById(R.id.img_back);
        SPUtil spUtil = new SPUtil(context);
        ISLOGIN = spUtil.getBoolean(Constant.IS_LOGIN, false);
        NotchTools.getFullScreenTools().fullScreenUseStatusForActivityOnCreate(this, this);
        ivEntry.setImageResource(R.drawable.icon_welcome);
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> startAnim());
    }

    private void startAnim() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(ivEntry, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(ivEntry, "scaleY", 1f, SCALE_END);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                if (ISLOGIN) {
                    JumpUtil.startAct(context, MainActivity.class);
                } else {
                    JumpUtil.startAct(context, LoginActivity.class);
                }
                finish();
            }
        });
    }

    @Override
    public void onNotchPropertyCallback(NotchProperty notchProperty) {
        int marginTop = notchProperty.getMarginTop();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBack.getLayoutParams();
        layoutParams.topMargin += marginTop;
        imgBack.setLayoutParams(layoutParams);
    }

    /**
     * onWindowFocusChanged最好也进行全屏适配，防止失去焦点又重回焦点时的flag不正确。
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            NotchTools.getFullScreenTools().fullScreenUseStatusForOnWindowFocusChanged(this);
        }
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
