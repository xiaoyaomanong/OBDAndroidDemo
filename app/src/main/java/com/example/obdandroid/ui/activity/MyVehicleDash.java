package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.trip.TripRecord;

import java.math.BigDecimal;

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
    private TestReceiver testReceiver;
    private Context context;

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
        context = this;
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
        initReceiver();
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
     * 发送数据
     */
    private void initReceiver() {
        //获取实例
        LocalBroadcastManager lm = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("com.android.ObdData");
        testReceiver = new TestReceiver();
        //绑定
        lm.registerReceiver(testReceiver, intentFilter);
    }

    private class TestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            TripRecord tripRecord = (TripRecord) intent.getSerializableExtra("data");
            LogE("时速:"+tripRecord.getSpeed());
            setView(tripRecord);
        }
    }

    /**
     * @param tripRecord OBD数据
     */
    @SuppressLint("SetTextI18n")
    private void setView(TripRecord tripRecord) {
        if (tripRecord != null) {
            tvmControlModuleVoltage.setText(tripRecord.getmControlModuleVoltage());
            tvmFuelLevel.setText(tripRecord.getmFuelLevel());
            tvmMassAirFlow.setText(String.valueOf(tripRecord.getmMassAirFlow()));
            tvmAirFuelRatio.setText(tripRecord.getmAirFuelRatio());
            tvmAmbientAirTemp.setText(tripRecord.getmAmbientAirTemp());
            float InsFuelConsumption = BigDecimal.valueOf(tripRecord.getmInsFuelConsumption())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            tvmInsFuelConsumption.setText(String.valueOf(InsFuelConsumption));
            tvmOdometer.setText(TextUtils.isEmpty(tripRecord.getmOdometer()) ? "0" : tripRecord.getmOdometer());
            float DrivingDuration = BigDecimal.valueOf(tripRecord.getDrivingDuration())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            tvDrivingDuration.setText(String.valueOf(DrivingDuration));
            String rpm = TextUtils.isEmpty(tripRecord.getEngineRpm()) ? "0" : tripRecord.getEngineRpm();
            dashRPM.setVelocity(Float.parseFloat(rpm) / 1000);
            dashSpeed.setVelocity(tripRecord.getSpeed());
            float InsFuelConsumptionTwo = BigDecimal.valueOf(tripRecord.getmInsFuelConsumption())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            dashInsFuelConsumption.setVelocity(InsFuelConsumptionTwo);
            dashEngineCoolantTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineCoolantTemp()) ? "0" : tripRecord.getmEngineCoolantTemp().replace("C", "")));
            dashFuelLevel.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmFuelLevel()) ? "0" : tripRecord.getmFuelLevel().replace("%", "")));
            float DrivingFuelConsumption = BigDecimal.valueOf(tripRecord.getmDrivingFuelConsumption())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            dashDrivingFuelConsumption.setVelocity(DrivingFuelConsumption);
            dashIdlingFuelConsumption.setVelocity(tripRecord.getmIdlingFuelConsumption());
            dashEngineOilTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineOilTemp()) ? "0" : tripRecord.getmEngineOilTemp().replace("C", "")));
        }
    }

    /**
     * 设置转速仪表
     */
    private void setRPM() {
        dashRPM.setmSection(10);
        dashRPM.setmHeaderText("x1000");
        dashRPM.setmMax(30);
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
        dashInsFuelConsumption.setmSection(8);
        dashInsFuelConsumption.setmHeaderText("L/100km");
        dashInsFuelConsumption.setmMax(240);
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
        dashIdlingFuelConsumption.setmMax(100);
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