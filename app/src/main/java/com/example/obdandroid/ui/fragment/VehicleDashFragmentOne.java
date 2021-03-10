package com.example.obdandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;
import com.sohrab.obd.reader.trip.TripRecord;

import java.math.BigDecimal;

/**
 * 作者：Jealous
 * 日期：2021/3/10 0010
 * 描述：
 */
public class VehicleDashFragmentOne extends BaseFragment {
    private PhilText tvmSpeed;
    private PhilText tvmRPM;
    private PhilText tvMaxRPM;
    private PhilText tvDrivingDuration;
    private PhilText tvMaxSpeed;
    private PhilText tvAverageSpeed;
    private CustomerDashboardViewLight dashSpeed;
    private CustomerDashboardViewLight dashRPM;
    private CustomerDashboardViewLight dashEngineOilTemp;
    private CustomerDashboardViewLight dashEngineCoolantTemp;
    private TripRecord tripRecord;
    private final Thread mSpeedCommand = new Thread(new MySpeedCommand());
    private final Thread mRPMCommand = new Thread(new MyRPMCommand());
    private final Thread mOilTempCommand = new Thread(new MyOilTempCommand());
    private final Thread mEngineCoolantTemperatureCommand = new Thread(new MyEngineCoolantTemperatureCommand());
    private int flag=1;
    private boolean show;
    private Handler mHandler = new Handler ();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dash_one;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        show=isVisibleToUser;
        LogE("isVisibleToUser:"+isVisibleToUser);
        if (isVisibleToUser){
            //通过handler启动线程
            mHandler.post(mEngineCoolantTemperatureCommand);
        }else {
            mHandler.removeCallbacks(mEngineCoolantTemperatureCommand);
        }
    }


    @Override
    public void initView(View view, Bundle savedInstanceState) {
        Context context = getHoldingActivity();
        tvmSpeed = getView(R.id.tvmSpeed);
        tvmRPM = getView(R.id.tvmRPM);
        tvMaxRPM = getView(R.id.tvMaxRPM);
        tvDrivingDuration = getView(R.id.tvDrivingDuration);
        tvMaxSpeed = getView(R.id.tvMaxSpeed);
        tvAverageSpeed = getView(R.id.tvAverageSpeed);
        dashSpeed = getView(R.id.dashSpeed);
        dashRPM = getView(R.id.dashRPM);
        dashEngineOilTemp = getView(R.id.dashEngineOilTemp);
        dashEngineCoolantTemp = getView(R.id.dashEngineCoolantTemp);
        TripRecord.getTriRecode(context).clear();
        tripRecord = TripRecord.getTriRecode(context);
        setRPM();
        setSpeed();
        setEngineCoolantTemp();
        setEngineOilTemp();
        startCommand();
    }

    private void startCommand() {
        LogE("是否连接:" + MainApplication.getBluetoothSocket().isConnected());
        // boolean isConnected = MainApplication.getBluetoothSocket().isConnected();
        boolean isConnected = true;
        if (isConnected) {
            mEngineCoolantTemperatureCommand.start();
          /*  mOilTempCommand.start();
            mRPMCommand.start();
            mSpeedCommand.start();*/
        } else {
            showToast("OBD连接已断开");
        }
    }


    /**
     * 读取速度
     */
    private void executeSpeedCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            SpeedCommand speedCommand = new SpeedCommand();//"01 0D"
            speedCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + speedCommand.getFormattedResult() + " :: name is :: " + speedCommand.getName());
            tripRecord.updateTrip(speedCommand.getName(), speedCommand);
            tvmSpeed.setText(String.valueOf(tripRecord.getSpeed()));
            dashSpeed.setVelocity(tripRecord.getSpeed());
            tvAverageSpeed.setText(String.valueOf(tripRecord.getAverageSpeed()));
            tvMaxSpeed.setText(String.valueOf(tripRecord.getSpeedMax()));
            float DrivingDuration = BigDecimal.valueOf(tripRecord.getDrivingDuration())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            tvDrivingDuration.setText(String.valueOf(DrivingDuration));
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取转速
     */
    private void executeRPMCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            RPMCommand rpmCommand = new RPMCommand();//"01 0C"
            rpmCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + rpmCommand.getFormattedResult() + " :: name is :: " + rpmCommand.getName());
            tripRecord.updateTrip(rpmCommand.getName(), rpmCommand);
            String rpm = TextUtils.isEmpty(tripRecord.getEngineRpm()) ? "0" : tripRecord.getEngineRpm();
            double maxRpm = (double) tripRecord.getEngineRpmMax() / 1000;
            dashRPM.setVelocity(Float.parseFloat(rpm) / 1000);
            tvmRPM.setText(String.valueOf(Float.parseFloat(rpm) / 1000));
            tvMaxRPM.setText(String.valueOf(maxRpm));
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取发动机油温
     */
    private void executeOilTempCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            OilTempCommand oilTempCommand1 = new OilTempCommand();//"01 5C"
            oilTempCommand1.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + oilTempCommand1.getFormattedResult() + " :: name is :: " + oilTempCommand1.getName());
            tripRecord.updateTrip(oilTempCommand1.getName(), oilTempCommand1);
            dashEngineOilTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineOilTemp()) ? "0" : tripRecord.getmEngineOilTemp().replace("C", "")));
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取发动机冷却液温度
     */
    private void executeEngineCoolantTemperatureCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            EngineCoolantTemperatureCommand engineCoolantTemperatureCommand = new EngineCoolantTemperatureCommand();//"01 05"//发动机冷媒温度
            engineCoolantTemperatureCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + engineCoolantTemperatureCommand.getFormattedResult() + " :: name is :: " + engineCoolantTemperatureCommand.getName());
            tripRecord.updateTrip(engineCoolantTemperatureCommand.getName(), engineCoolantTemperatureCommand);
            dashEngineCoolantTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineCoolantTemp()) ? "0" : tripRecord.getmEngineCoolantTemp().replace("C", "")));
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
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
        dashSpeed.setmSection(9);
        dashSpeed.setmHeaderText("km/h");
        dashSpeed.setmMax(270);
        dashSpeed.setmMin(0);
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

    class MySpeedCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getContext()).getServiceRunningStatus()) {
                executeSpeedCommand();
            }
        }
    }

    class MyRPMCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getContext()).getServiceRunningStatus()) {
                executeRPMCommand();
            }
        }
    }

    class MyOilTempCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getContext()).getServiceRunningStatus()) {
                executeOilTempCommand();
            }
        }
    }

    class MyEngineCoolantTemperatureCommand implements Runnable {

        @Override
        public void run() {
            while (show) {
                // executeEngineCoolantTemperatureCommand();
                flag++;
                tvMaxRPM.setText(flag+"");
                LogE("1111");
            }

            //mHandler.postDelayed(mEngineCoolantTemperatureCommand, 0);
        }
    }
}