package com.example.obdandroid.ui.activity;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.github.pires.obd.commands.protocol.HeadersOffCommand;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.protocol.EchoOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.LineFeedOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SelectProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SpacesOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.TimeoutCommand;
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
public class VehicleDashOneActivity extends BaseActivity {
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
                stopThread();
                ObdPreferences.get(getApplicationContext()).setServiceRunning(false);
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
    }


    /**
     * 中止线程
     */
    private void stopThread() {
        if (mSpeedCommand != null) {
            mSpeedCommand.interrupt();
            mSpeedCommand = null;
        }
    }


    /**
     * 读取速度
     */
    private synchronized void executeSpeedCommand(BluetoothSocket socket) {
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                command.run(socket.getInputStream(), socket.getOutputStream());
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
                String rpm = TextUtils.isEmpty(tripRecord.getEngineRpm()) ? "0" : tripRecord.getEngineRpm();
                double maxRpm = (double) tripRecord.getEngineRpmMax() / 1000;
                dashRPM.setVelocity(Float.parseFloat(rpm) / 1000);
                tvmRPM.setText(String.valueOf(Float.parseFloat(rpm) / 1000));
                tvMaxRPM.setText(String.valueOf(maxRpm));
                dashEngineOilTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineOilTemp()) ? "0" : tripRecord.getmEngineOilTemp().replace("℃", "")));
                dashEngineCoolantTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineCoolantTemp()) ? "0" : tripRecord.getmEngineCoolantTemp().replace("℃", "")));
            } catch (Exception e) {
                LogE("执行命令异常  :: " + e.getMessage());
            }
        }
    }

    private List<ObdCommand> setCommands() {
        List<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.add(new ObdResetCommand());
        obdCommands.add(new EchoOffCommand());
        obdCommands.add(new LineFeedOffCommand());
        obdCommands.add(new SpacesOffCommand());
        obdCommands.add(new TimeoutCommand(62));

        obdCommands.add(new SelectProtocolCommand(ObdProtocols.AUTO));
        obdCommands.add(new SpeedCommand(ModeTrim.MODE_01.buildObdCommand()));
        obdCommands.add(new RPMCommand(ModeTrim.MODE_01.buildObdCommand()));
        obdCommands.add(new OilTempCommand(ModeTrim.MODE_01.buildObdCommand()));
        obdCommands.add(new EngineCoolantTemperatureCommand(ModeTrim.MODE_01.buildObdCommand()));
        return obdCommands;
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
                executeSpeedCommand(MainApplication.getBluetoothSocket());
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ObdPreferences.get(getApplicationContext()).setServiceRunning(false);
            stopThread();
            finish();
        }
        return true;
    }
}