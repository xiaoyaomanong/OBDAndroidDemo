package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.CheckRecord;
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
import com.sohrab.obd.reader.utils.LogUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    private CheckRecord tripRecord;
    private final Thread mSpeedCommand = new Thread(new MySpeedCommand());
    public MyHandler mHandler;
    private boolean isConnected;

    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100://速度
                    //setView((CheckRecord) msg.obj);
                    setSpeedView((CheckRecord) msg.obj);
                    break;
                case 101://转速
                    CheckRecord record= (CheckRecord) msg.obj;
                    String rpm = TextUtils.isEmpty(record.getEngineRpm()) ? "0" : record.getEngineRpm();
                    double maxRpm = (double) record.getEngineRpmMax() / 1000;
                    dashRPM.setVelocity((Float.parseFloat(rpm) / 1000));
                    tvmRPM.setText(String.valueOf(Float.parseFloat(rpm) / 1000));
                    tvMaxRPM.setText(String.valueOf(maxRpm));
                    break;
                case 102://发动机油温
                    CheckRecord record1= (CheckRecord) msg.obj;
                    dashEngineOilTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(record1.getmEngineOilTemp()) ? "0" : record1.getmEngineOilTemp().replace("℃", "")));
                    break;
                case 103://冷却液温度
                    CheckRecord record2= (CheckRecord) msg.obj;
                    dashEngineCoolantTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(record2.getmEngineCoolantTemp()) ? "0" : record2.getmEngineCoolantTemp().replace("℃", "")));
                    break;
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dash_one;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void initView() {
        super.initView();
        Context context = this;
        keepScreenLongLight(this, true);
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
        mHandler = new MyHandler();
        CheckRecord.getTriRecode(context, getToken()).clear();
        tripRecord = CheckRecord.getTriRecode(context, getToken());
        ObdPreferences.get(getApplicationContext()).setServiceRunning(true);
        if (MainApplication.getBluetoothSocket() != null) {
            isConnected = MainApplication.getBluetoothSocket().isConnected();
        }
        setRPM();
        setSpeed();
        setEngineCoolantTemp();
        setEngineOilTemp();
        startCommand();
        //newCachedThreadPool();
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
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


    private void newCachedThreadPool() {
        //创建可缓存的线程池，如果线程池的容量超过了任务数，自动回收空闲线程，任务增加时可以自动添加新线程，线程池的容量不限制
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //提交 4个任务
        cachedThreadPool.submit(() -> {//读取车速
            while (isConnected) {
                //executeCommandData(MainApplication.getBluetoothSocket(), getCommands());
                SpeedCommand command = new SpeedCommand(ModeTrim.MODE_01.buildObdCommand());
                executeCommandData(MainApplication.getBluetoothSocket(), command,100);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        cachedThreadPool.submit(() -> {//读取转速
            while (isConnected) {
                RPMCommand command = new RPMCommand(ModeTrim.MODE_01.buildObdCommand());
                executeCommandData(MainApplication.getBluetoothSocket(), command,101);
            }
        });
        cachedThreadPool.submit(() -> {//发动机油温
            while (isConnected) {
                OilTempCommand command = new OilTempCommand(ModeTrim.MODE_01.buildObdCommand());
                executeCommandData(MainApplication.getBluetoothSocket(), command,102);
            }
        });
        cachedThreadPool.submit(() -> {//发动机冷媒温度
            while (isConnected) {
                EngineCoolantTemperatureCommand command = new EngineCoolantTemperatureCommand(ModeTrim.MODE_01.buildObdCommand());
                executeCommandData(MainApplication.getBluetoothSocket(), command,103);
            }
        });
    }

    private void startCommand() {
        if (isConnected) {
            mSpeedCommand.start();
        } else {
            showToast(getString(R.string.device_not_conn));
        }
    }

    /**
     * @return 命令集合
     */
    private List<ObdCommand> getCommands() {
        List<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.clear();
        obdCommands.add(new ObdResetCommand());
        obdCommands.add(new RPMCommand(ModeTrim.MODE_01.buildObdCommand()));
        obdCommands.add(new OilTempCommand(ModeTrim.MODE_01.buildObdCommand()));
        obdCommands.add(new EngineCoolantTemperatureCommand(ModeTrim.MODE_01.buildObdCommand()));
        obdCommands.add(new SpeedCommand(ModeTrim.MODE_01.buildObdCommand()));
        return obdCommands;
    }

    /**
     * 读取速度
     */
    private void executeCommandData(BluetoothSocket socket, ObdCommand command,int what) {
        try {
            command.run(socket.getInputStream(), socket.getOutputStream());
          //  LogE("数据:"+command.getCalculatedResult());
            tripRecord.updateTrip(command.getName(), command);
            if (command.getName().equals("Vehicle Speed")&&what==100){
                LogE("速度:"+((SpeedCommand) command).getMetricSpeed());
            }
            Message msg = mHandler.obtainMessage();
            msg.what = what;
            msg.obj = tripRecord;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取速度
     */
    private void executeCommandData(BluetoothSocket socket, List<ObdCommand> commands) {
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                command.run(socket.getInputStream(), socket.getOutputStream());
                tripRecord.updateTrip(command.getName(), command);
                Message msg = mHandler.obtainMessage();
                msg.what = 100;
                msg.obj = tripRecord;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                LogE("执行命令异常  :: " + e.getMessage());
            }
        }
    }


    /**
     * @param tripRecord 速度
     *                   显示速度
     */
    private void setSpeedView(CheckRecord tripRecord) {
        tvmSpeed.setText(String.valueOf(tripRecord.getSpeed()));
        dashSpeed.setVelocity(tripRecord.getSpeed());
        tvAverageSpeed.setText(String.valueOf(tripRecord.getAverageSpeed()));
        tvMaxSpeed.setText(String.valueOf(tripRecord.getSpeedMax()));
        float DrivingDuration = BigDecimal.valueOf(tripRecord.getDrivingDuration())
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
        tvDrivingDuration.setText(String.valueOf(DrivingDuration));
    }


    private void setView(CheckRecord tripRecord) {
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
        dashRPM.setVelocity((Float.parseFloat(rpm) / 1000));
        tvmRPM.setText(String.valueOf(Float.parseFloat(rpm) / 1000));
        tvMaxRPM.setText(String.valueOf(maxRpm));
        dashEngineOilTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineOilTemp()) ? "0" : tripRecord.getmEngineOilTemp().replace("℃", "")));
        dashEngineCoolantTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineCoolantTemp()) ? "0" : tripRecord.getmEngineCoolantTemp().replace("℃", "")));
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
                executeCommandData(MainApplication.getBluetoothSocket(), getCommands());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keepScreenLongLight(this, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ObdPreferences.get(getApplicationContext()).setServiceRunning(false);
            finish();
        }
        return true;
    }
}