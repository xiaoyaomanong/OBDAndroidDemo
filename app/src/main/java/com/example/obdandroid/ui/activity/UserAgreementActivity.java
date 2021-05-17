package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.APIConfig;
import com.example.obdandroid.utils.DialogUtils;
import com.example.obdandroid.utils.StringUtil;
import com.hacknife.immersive.Immersive;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

/**
 * 作者：Jealous
 * 日期：2019/9/29 0029 09:04
 */
public class UserAgreementActivity extends BaseActivity {
    private DialogUtils dialogUtils;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        Context context = this;
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        WebView wvAgreement = findViewById(R.id.wv_agreement);
        titleBarSet.setTitle("用户协议");
        dialogUtils = new DialogUtils(context);
        WebSettings webSettings = wvAgreement.getSettings();
        webSettings.setSupportMultipleWindows(true);
        webSettings.setSavePassword(false);
        webSettings.setAllowFileAccess(false);
        wvAgreement.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvAgreement.setWebViewClient(new MyWebViewClient());
        dialogUtils.showProgressDialog();
        wvAgreement.loadUrl(APIConfig.UserAgreement_URL);
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
    }

    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.cancel();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogE(url);
            if (!StringUtil.isNull(url)) {
                dialogUtils.dismiss();
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }

}
