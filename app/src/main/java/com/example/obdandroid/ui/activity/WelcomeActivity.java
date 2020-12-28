package com.example.obdandroid.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFullScreenActivity;
import com.example.obdandroid.config.Constant;
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

public class WelcomeActivity extends BaseFullScreenActivity implements OnNotchCallBack {

    private static final int ANIM_TIME = 2000;

    private static final float SCALE_END = 1.15F;

    private static final int[] Imgs = {
            R.drawable.welcomimg1, R.drawable.welcomimg3,
            R.drawable.welcomimg4, R.drawable.welcomimg5,
            R.drawable.welcomimg6, R.drawable.welcomimg7,
            R.drawable.welcomimg8, R.drawable.welcomimg9,
            R.drawable.welcomimg11, R.drawable.welcomimg12};
    private ImageView ivEntry;
    private ImageView imgBack;
    private Context context;
    private SPUtil spUtil;
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
            Intent intent = new Intent(context, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
        } else {
            // 如果不是第一次启动app，则正常显示启动屏
            //避免按home键后再次打开程序跳转登录界面，用在setContentView方法前
            if (!isTaskRoot()) {
                final Intent intent = getIntent();
                final String intentAction = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                    finish();
                    return;
                }
            }
            startMainActivity();
        }
    }

    private void startMainActivity() {
        setContentView(getContentViewId());
        ivEntry = findViewById(R.id.iv_entry);
        imgBack = findViewById(R.id.img_back);
        spUtil = new SPUtil(context);
        ISLOGIN = spUtil.getBoolean(Constant.ISLOGIN, false);
        NotchTools.getFullScreenTools().fullScreenUseStatusForActivityOnCreate(this, this);
        Random random = new Random(SystemClock.elapsedRealtime());//SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
        ivEntry.setImageResource(Imgs[random.nextInt(Imgs.length)]);
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
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    WelcomeActivity.this.finish();
                } else {
                    JumpUtil.startAct(context, LoginActivity.class);
                }
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
