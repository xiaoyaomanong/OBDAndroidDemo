package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.view.CircleImageView;
import com.example.obdandroid.utils.JumpUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

/**
 * 作者：Jealous
 * 日期：2021/2/22 0022
 * 描述：
 */
public class AboutActivity extends BaseActivity {
    private Context context;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bbout;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        CircleImageView imageHeader = findViewById(R.id.imageHeader);
        TextView tvAppVersion = findViewById(R.id.tvAppVersion);
        LinearLayout layoutService = findViewById(R.id.layoutService);
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
        Glide.with(context).load(R.drawable.logo_green).into(imageHeader);
        tvAppVersion.setText(packageName(context));
        layoutService.setOnClickListener(v -> JumpUtil.startAct(context, AgreementActivity.class));
    }

    /**
     * @param context 上下文对象
     * @return 获取版本名称
     */
    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }
}