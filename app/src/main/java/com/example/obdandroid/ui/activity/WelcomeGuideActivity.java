package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFullScreenActivity;
import com.example.obdandroid.utils.JumpUtil;
import com.example.obdandroid.utils.SharedPreferencesUtil;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;
import notchtools.geek.com.notchtools.NotchTools;
import notchtools.geek.com.notchtools.core.NotchProperty;
import notchtools.geek.com.notchtools.core.OnNotchCallBack;

/**
 * Created by xialo on 2016/7/25.
 */
public class WelcomeGuideActivity extends BaseFullScreenActivity implements OnNotchCallBack {
    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;
    private Context context;
    private ImageView imgBack;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_guide;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(getContentViewId());
        context = this;
        mBackgroundBanner = findViewById(R.id.banner_guide_backgrounds);
        mForegroundBanner = findViewById(R.id.banner_guide_foregrounds);
        imgBack = findViewById(R.id.img_back);
        setListener();
        processLogic();
        NotchTools.getFullScreenTools().fullScreenUseStatusForActivityOnCreate(this, this);
    }

    /**
     * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
     * 如果进入按钮和跳过按钮有一个不存在的话就传 0
     * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
     * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
     */
    private void setListener() {
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, () -> {
            SharedPreferencesUtil.putBoolean(context, SharedPreferencesUtil.FIRST_OPEN, false);
            JumpUtil.startAct(context, LoginActivity.class);
            finish();
        });
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        mBackgroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.icon_welcome,
                R.drawable.icon_welcome_one);

        mForegroundBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.icon_welcome,
                R.drawable.icon_welcome_one);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
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
}
