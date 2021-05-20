package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        startDialog();
    }

    private void startDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_intimate);
            window.setGravity(Gravity.CENTER);
            TextView tvContent = window.findViewById(R.id.tv_content);
            TextView tvCancel = window.findViewById(R.id.tv_cancel);
            TextView tvAgree = window.findViewById(R.id.tv_agree);
            String str = "    感谢您对本公司的支持!本公司非常重视您的个人信息和隐私保护。" +
                    "为了更好地保障您的个人权益，在您使用我们的产品前，" +
                    "请务必审慎阅读《隐私政策》和《用户协议》内的所有条款，" +
                    "尤其是:\n" +
                    " 1.我们对您的个人信息的收集/保存/使用/对外提供/保护等规则条款，以及您的用户权利等条款;\n" +
                    " 2. 约定我们的限制责任、免责条款;\n" +
                    " 3.其他以颜色或加粗进行标识的重要条款。\n" +
                    "如您对以上协议有任何疑问，" +
                    "可通过人工客服:0931-4896234或发邮件至liangyingn@163.com与我们联系。您点击“同意并继续”的行为即表示您已阅读完毕并同意以上协议的全部内容。" +
                    "如您同意以上协议内容，请点击“同意并继续”，开始使用我们的产品和服务!";

            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(str);
            final int start = str.indexOf("《");//第一个出现的位置
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    JumpUtil.startAct(context, UserAgreementActivity.class);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.red));
                    ds.setUnderlineText(false);
                }
            }, start, start + 6, 0);

            int end = str.lastIndexOf("《");
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    JumpUtil.startAct(context, ServiceAgreementActivity.class);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.red));
                    ds.setUnderlineText(false);
                }
            }, end, end + 6, 0);

            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            tvContent.setText(ssb, TextView.BufferType.SPANNABLE);


            tvCancel.setOnClickListener(v -> {
                SharedPreferencesUtil.putBoolean(context, SharedPreferencesUtil.FIRST_OPEN, true);
                alertDialog.cancel();
                finish();
            });

            tvAgree.setOnClickListener(v -> {
                SharedPreferencesUtil.putBoolean(context, SharedPreferencesUtil.FIRST_OPEN, false);
                // checkPermission();
                alertDialog.cancel();
            });
        }

    }

    /**
     * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
     * 如果进入按钮和跳过按钮有一个不存在的话就传 0
     * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
     * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
     */
    private void setListener() {
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, () -> {
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
