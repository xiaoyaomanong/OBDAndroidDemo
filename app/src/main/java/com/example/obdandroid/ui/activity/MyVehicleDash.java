package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.trip.TripRecord;

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
    private PhilText tvDrivingDuration;
    private CustomerDashboardViewLight dashSpeed;//车速
    private CustomerDashboardViewLight dashRPM;//转速
    private CustomerDashboardViewLight dashInsFuelConsumption;//瞬时油耗
    private CustomerDashboardViewLight dashEngineCoolantTemp;//冷却液温度
    private CustomerDashboardViewLight dashFuelLevel;//燃油油位
    private CustomerDashboardViewLight dashDrivingFuelConsumption;//行驶油耗
    private CustomerDashboardViewLight dashIdlingFuelConsumption;//怠速油耗
    private CustomerDashboardViewLight dashEngineOilTemp;//机油温度

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
        TripRecord mTripRecord = (TripRecord) getIntent().getSerializableExtra("data");
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
        dashFuelLevel = findViewById(R.id.dashFuelLevel);
        dashDrivingFuelConsumption = findViewById(R.id.dashDrivingFuelConsumption);
        dashIdlingFuelConsumption = findViewById(R.id.dashIdlingFuelConsumption);
        dashEngineOilTemp = findViewById(R.id.dashEngineOilTemp);
        setRPM();
        setSpeed();
        setInsFuelConsumption();
        setEngineCoolantTemp();
        setFuelLevel();
        setDrivingFuelConsumption();
        setIdlingFuelConsumption();
        setEngineOilTemp();
        setView(mTripRecord);
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
     * @param tripRecord OBD数据
     */
    @SuppressLint("SetTextI18n")
    private void setView(TripRecord tripRecord) {
        if (tripRecord != null) {
            tvmControlModuleVoltage.setText(tripRecord.getmControlModuleVoltage());
            tvmFuelLevel.setText(tripRecord.getmFuelLevel() + "%");
            tvmMassAirFlow.setText(String.valueOf(tripRecord.getmMassAirFlow()));
            tvmAirFuelRatio.setText(tripRecord.getmAirFuelRatio());
            tvmAmbientAirTemp.setText(tripRecord.getmAmbientAirTemp());
            tvmInsFuelConsumption.setText(String.valueOf(tripRecord.getmInsFuelConsumption()));
            tvmOdometer.setText(tripRecord.getmOdometer());
            tvDrivingDuration.setText(String.valueOf(tripRecord.getDrivingDuration()));
            dashRPM.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getEngineRpm()) ? "0" : tripRecord.getEngineRpm()));
            dashSpeed.setVelocity(tripRecord.getSpeed());
            dashInsFuelConsumption.setVelocity(tripRecord.getmInsFuelConsumption());
            dashEngineCoolantTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineCoolantTemp().replace("C", "")) ? "0" : tripRecord.getmEngineCoolantTemp().replace("C", "")));
            dashFuelLevel.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmFuelLevel()) ? "" : tripRecord.getmFuelLevel()));
            dashDrivingFuelConsumption.setVelocity(tripRecord.getmDrivingFuelConsumption());
            dashIdlingFuelConsumption.setVelocity( tripRecord.getmIdlingFuelConsumption());
            dashEngineOilTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineOilTemp().replace("C", "")) ? "" : tripRecord.getmEngineOilTemp().replace("C", "")));
        }
    }

    /**
     * 设置转速仪表
     */
    private void setRPM() {
        dashRPM.setmSection(8);
        dashRPM.setmHeaderText("x1000");
        dashRPM.setmMax(8);
        dashRPM.setmMin(0);
    }

    /**
     * 设置速度仪表
     */
    private void setSpeed() {
        dashSpeed.setmSection(8);
        dashSpeed.setmHeaderText("km/h");
        dashSpeed.setmMax(240);
        dashSpeed.setmMin(0);

    }

    /**
     * 设置燃油油位仪表
     */
    private void setFuelLevel() {
        dashFuelLevel.setmSection(10);
        dashFuelLevel.setmHeaderText(" % ");
        dashFuelLevel.setmMax(100);
        dashFuelLevel.setmMin(0);

    }

    /**
     * 设置瞬时油耗仪表
     */
    private void setInsFuelConsumption() {
        dashInsFuelConsumption.setmSection(10);
        dashInsFuelConsumption.setmHeaderText("L/100km");
        dashInsFuelConsumption.setmMax(30);
        dashInsFuelConsumption.setmMin(0);
    }

    /**
     * 设置行驶油耗仪表
     */
    private void setDrivingFuelConsumption() {
        dashDrivingFuelConsumption.setmSection(10);
        dashDrivingFuelConsumption.setmHeaderText(" L ");
        dashDrivingFuelConsumption.setmMax(50);
        dashDrivingFuelConsumption.setmMin(0);
    }

    /**
     * 设置怠速油耗仪表
     */
    private void setIdlingFuelConsumption() {
        dashIdlingFuelConsumption.setmSection(10);
        dashIdlingFuelConsumption.setmHeaderText(" L ");
        dashIdlingFuelConsumption.setmMax(30);
        dashIdlingFuelConsumption.setmMin(0);
    }

    /**
     * 设置机油温度仪表
     */
    private void setEngineOilTemp() {
        dashEngineOilTemp.setmSection(10);
        dashEngineOilTemp.setmHeaderText(" °C ");
        dashEngineOilTemp.setmMax(260);
        dashEngineOilTemp.setmMin(-40);
    }

    /**
     * 设置冷却液温度仪表
     */
    private void setEngineCoolantTemp() {
        dashEngineCoolantTemp.setmSection(10);
        dashEngineCoolantTemp.setmHeaderText(" °C ");
        dashEngineCoolantTemp.setmMax(260);
        dashEngineCoolantTemp.setmMin(-40);
    }
}