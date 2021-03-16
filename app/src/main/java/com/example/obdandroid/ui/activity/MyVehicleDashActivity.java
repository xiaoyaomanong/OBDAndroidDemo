package com.example.obdandroid.ui.activity;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.fragment.VehicleDashActivityOne;
import com.example.obdandroid.ui.fragment.VehicleDashActivityTwo;
import com.example.obdandroid.utils.JumpUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

/**
 * 作者：Jealous
 * 日期：2021/1/26 0026
 * 描述：
 */
public class MyVehicleDashActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dash;
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
        LinearLayout layoutDashOne = findViewById(R.id.layoutDashOne);
        LinearLayout layoutDashTwo = findViewById(R.id.layoutDashTwo);
        layoutDashOne.setOnClickListener(v -> JumpUtil.startAct(context, VehicleDashActivityOne.class));
        layoutDashTwo.setOnClickListener(v -> JumpUtil.startAct(context, VehicleDashActivityTwo.class));
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
}