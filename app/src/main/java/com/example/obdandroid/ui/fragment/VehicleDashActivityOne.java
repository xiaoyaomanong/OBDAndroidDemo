package com.example.obdandroid.ui.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;
import com.sohrab.obd.reader.trip.TripRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/3/10 0010
 * 描述：
 */
public class VehicleDashActivityOne extends BaseActivity {
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
    private Thread mSpeedCommand = new Thread(new MySpeedCommand());
    private Thread mRPMCommand = new Thread(new MyRPMCommand());
    private Thread mOilTempCommand = new Thread(new MyOilTempCommand());
    private Thread mEngineCoolantTemperatureCommand = new Thread(new MyEngineCoolantTemperatureCommand());
    private List<ObdCommand> commands = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dash_one;
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
        ObdPreferences.get(getApplicationContext()).setServiceRunning(true);
        commands = setCommands();
        setRPM();
        setSpeed();
        setEngineCoolantTemp();
        setEngineOilTemp();
        startCommand();
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

    private void startCommand() {
        LogE("是否连接:" + MainApplication.getBluetoothSocket().isConnected());
        boolean isConnected = MainApplication.getBluetoothSocket().isConnected();
        if (isConnected) {
            startThread();
        } else {
            showToast("OBD连接已断开");
        }
    }

    /**
     * 开启线程
     */
    private void startThread() {
        mSpeedCommand.start();
        mRPMCommand.start();
        mOilTempCommand.start();
        mEngineCoolantTemperatureCommand.start();
    }


    /**
     * 中止线程
     */
    private void stopThread() {
        if (mSpeedCommand != null) {
            mSpeedCommand.interrupt();
            mSpeedCommand = null;
        }
        if (mRPMCommand != null) {
            mRPMCommand.interrupt();
            mRPMCommand = null;
        }
        if (mOilTempCommand != null) {
            mOilTempCommand.interrupt();
            mOilTempCommand = null;
        }
        if (mEngineCoolantTemperatureCommand != null) {
            mEngineCoolantTemperatureCommand.interrupt();
            mEngineCoolantTemperatureCommand = null;
        }
    }


    /**
     * 读取速度
     */
    private synchronized void executeSpeedCommand() {
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                command.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
                LogE("结果是:: " + command.getFormattedResult() + " :: name is :: " + command.getName());
                tripRecord.updateTrip(command.getName(), command);
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
       /* try {
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
        }*/
    }

    private List<ObdCommand> setCommands() {
        List<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.add(new ObdResetCommand());
        obdCommands.add(new SpeedCommand());
        return obdCommands;
    }

    /**
     * 读取转速
     */
    private synchronized void executeRPMCommand() {
        try {
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
    private synchronized void executeOilTempCommand() {
        try {
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
    private synchronized void executeEngineCoolantTemperatureCommand() {
        try {
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
    private synchronized void setRPM() {
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
            while (ObdPreferences.get(getApplicationContext()).getServiceRunning()) {
                executeSpeedCommand();
            }
        }
    }

    class MyRPMCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunning()) {
                executeRPMCommand();
            }
        }
    }

    class MyOilTempCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunning()) {
                executeOilTempCommand();
            }
        }
    }

    class MyEngineCoolantTemperatureCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunning()) {
                executeEngineCoolantTemperatureCommand();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ObdPreferences.get(getApplicationContext()).setServiceRunning(false);
        stopThread();
    }
}