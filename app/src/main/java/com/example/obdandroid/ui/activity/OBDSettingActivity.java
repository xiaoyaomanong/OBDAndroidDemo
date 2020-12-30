package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.utils.JumpUtil;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

/**
 * 作者：Jealous
 * 日期：2020/12/30 0030
 * 描述：
 */
public class OBDSettingActivity extends BaseActivity implements View.OnClickListener {
    private TitleBar titleBarSet;
    private LinearLayout layoutBluetooth;
    private CheckBox cbGpsCategory;
    private LinearLayout layoutGpsUpdatePeriod;
    private LinearLayout layoutGpsDistancePeriod;
    private LinearLayout layoutObdProtocol;
    private CheckBox cbImperialUnits;
    private LinearLayout layoutCommands;
    private Context context;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_obd_setting;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        titleBarSet = findViewById(R.id.titleBarSet);
        layoutBluetooth = findViewById(R.id.layoutBluetooth);
        cbGpsCategory = findViewById(R.id.cb_gps_category);
        layoutGpsUpdatePeriod = findViewById(R.id.layout_gps_update_period);
        layoutGpsDistancePeriod = findViewById(R.id.layout_gps_distance_period);
        layoutObdProtocol = findViewById(R.id.layout_obd_protocol);
        cbImperialUnits = findViewById(R.id.cb_imperial_units);
        layoutCommands = findViewById(R.id.layout_commands);
        titleBarSet.setTitle("OBD设置");
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
        layoutBluetooth.setOnClickListener(this);
        layoutCommands.setOnClickListener(this);
        layoutGpsDistancePeriod.setOnClickListener(this);
        layoutGpsUpdatePeriod.setOnClickListener(this);
        layoutObdProtocol.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutBluetooth:

                break;
            case R.id.layout_commands:
                JumpUtil.startAct(context, ObdCommandActivity.class);
                break;
            case R.id.layout_gps_distance_period:

                break;
            case R.id.layout_gps_update_period:

                break;
            case R.id.layout_obd_protocol:
                JumpUtil.startAct(context, OBDProtocolActivity.class);
                break;
        }
    }
}