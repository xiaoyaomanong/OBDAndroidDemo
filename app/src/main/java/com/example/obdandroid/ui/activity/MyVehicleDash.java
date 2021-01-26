package com.example.obdandroid.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

/**
 * 作者：Jealous
 * 日期：2021/1/26 0026
 * 描述：
 */
public class MyVehicleDash extends BaseActivity {
    private TitleBar titleBarSet;
    private TextView tvmControlModuleVoltage;
    private TextView tvmFuelLevel;
    private TextView tvmMassAirFlow;
    private TextView tvmAirFuelRatio;
    private TextView tvmAmbientAirTemp;
    private PhilText tvmInsFuelConsumption;
    private PhilText tvmOdometer;
    private CustomerDashboardViewLight dashSpeed;
    private CustomerDashboardViewLight dashRPM;
    private PhilText tvDrivingDuration;
    private CustomerDashboardViewLight dashInsFuelConsumption;
    private CustomerDashboardViewLight dashEngineCoolantTemp;

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
        titleBarSet = findViewById(R.id.titleBarSet);
        tvmControlModuleVoltage = findViewById(R.id.tvmControlModuleVoltage);
        tvmFuelLevel = findViewById(R.id.tvmFuelLevel);
        tvmMassAirFlow = findViewById(R.id.tvmMassAirFlow);
        tvmAirFuelRatio = findViewById(R.id.tvmAirFuelRatio);
        tvmAmbientAirTemp = findViewById(R.id.tvmAmbientAirTemp);
        tvmInsFuelConsumption = findViewById(R.id.tvmInsFuelConsumption);
        tvmOdometer = findViewById(R.id.tvmOdometer);
        dashSpeed = findViewById(R.id.dashSpeed);
        dashRPM = findViewById(R.id.dashRPM);
        tvDrivingDuration = findViewById(R.id.tvDrivingDuration);
        dashInsFuelConsumption = findViewById(R.id.dashInsFuelConsumption);
        dashEngineCoolantTemp = findViewById(R.id.dashEngineCoolantTemp);
        setRPM();
        setSpeed();
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

    /**
     * 设置转速仪表
     */
    private void setRPM() {
        dashRPM.setmHeaderText("x1000");
        dashRPM.setmMax(8);
        dashRPM.setmMin(0);
        dashRPM.setmSection(8);
        dashRPM.setVelocity(3);
    }

    /**
     * 设置速度仪表
     */
    private void setSpeed() {
        dashSpeed.setmHeaderText("km/h");
        dashSpeed.setmMin(240);
        dashSpeed.setmMin(0);
        dashSpeed.setmSection(8);
        dashSpeed.setVelocity(120);
    }
}