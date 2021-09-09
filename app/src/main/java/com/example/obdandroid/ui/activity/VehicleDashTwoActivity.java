package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.CheckRecord;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.example.obdandroid.utils.StringUtil;
import com.github.pires.obd.commands.protocol.HeadersOffCommand;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.engine.ThrottlePositionCommand;
import com.sohrab.obd.reader.obdCommand.fuel.ConsumptionRateCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelRailPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.EchoOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.LineFeedOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SelectProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SpacesOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.TimeoutCommand;
import com.sohrab.obd.reader.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：Jealous
 * 日期：2021/3/10 0010
 * 描述：
 */
public class VehicleDashTwoActivity extends BaseActivity {
    private PhilText tvFuelRate;
    private PhilText tvIntakeAirTemp;
    private PhilText tvFuelRailPressure;
    private CustomerDashboardViewLight dashThrottlePos;
    private CheckRecord tripRecord;
    private final Thread CommandThread = new Thread(new MyCommand());
    private PhilText tvFuelPressure;
    private CustomerDashboardViewLight dashFuelPressure;
    private CustomerDashboardViewLight dashFuelRate;
    private CustomerDashboardViewLight dashFuelRailPressure;
    public MyHandler mHandler;
    private boolean isConnected;


    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                setView((CheckRecord) msg.obj);
            }
        }
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dash_two;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void initView() {
        super.initView();
        Context context = this;
        keepScreenLongLight(this, true);
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        tvFuelPressure = findViewById(R.id.tvFuelPressure);
        dashFuelPressure = findViewById(R.id.dashFuelPressure);
        tvFuelRate = getView(R.id.tvFuelRate);
        tvIntakeAirTemp = getView(R.id.tvIntakeAirTemp);
        tvFuelRailPressure = getView(R.id.tvFuelRailPressure);
        dashThrottlePos = getView(R.id.dashThrottlePos);
        dashFuelRate = findViewById(R.id.dashFuelRate);
        dashFuelRailPressure = findViewById(R.id.dashFuelRailPressure);
        mHandler = new MyHandler();
        CheckRecord.getTriRecode(context, getToken()).clear();
        tripRecord = CheckRecord.getTriRecode(context, getToken());
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(true);
        if (MainApplication.getBluetoothSocket() != null) {
            isConnected = MainApplication.getBluetoothSocket().isConnected();
        }
        setFuelLevel();
        setInsFuelConsumption();
        setDrivingFuelConsumption();
        setIdlingFuelConsumption();
        // startCommand();
        newCachedThreadPool();
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
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
     * 线程池
     */
    private void newCachedThreadPool() {
        //创建可缓存的线程池，如果线程池的容量超过了任务数，自动回收空闲线程，任务增加时可以自动添加新线程，线程池的容量不限制
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //提交 4个任务

        cachedThreadPool.submit(() -> {
            while (isConnected) {
                executeObdCommand(MainApplication.getBluetoothSocket(), getCommands());
            }
        });
    }


    private void startCommand() {
        if (isConnected) {
            CommandThread.start();
        } else {
            showToast(getString(R.string.device_not_conn));
        }
    }

    /**
     * 设置燃油压力仪表
     */
    private void setFuelLevel() {
        dashFuelPressure.setmSection(8);
        dashFuelPressure.setmHeaderText("kPa");
        dashFuelPressure.setmMax(800);
        dashFuelPressure.setmMin(0);
    }

    /**
     * 设置燃油效率仪表
     */
    private void setInsFuelConsumption() {
        dashFuelRate.setmSection(10);
        dashFuelRate.setmHeaderText("100L/h");
        dashFuelRate.setmMax(3300);
        dashFuelRate.setmMin(0);
    }

    /**
     * 设置油门位置仪表
     */
    private void setDrivingFuelConsumption() {
        dashThrottlePos.setmSection(10);
        dashThrottlePos.setmHeaderText(" % ");
        dashThrottlePos.setmMax(100);
        dashThrottlePos.setmMin(0);
    }

    /**
     * 设置油轨压力（柴油或汽油直喷）仪表
     */
    private void setIdlingFuelConsumption() {
        dashFuelRailPressure.setmSection(10);
        dashFuelRailPressure.setmHeaderText("1000kPa");
        dashFuelRailPressure.setmMax(660);
        dashFuelRailPressure.setmMin(0);
    }

    /**
     * @return 命令集合
     */
    private List<ObdCommand> getCommands() {
        List<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.clear();
        obdCommands.add(new FuelPressureCommand(ModeTrim.MODE_01.build()));//油压
        obdCommands.add(new ThrottlePositionCommand(ModeTrim.MODE_01.build()));//节气门位置
        obdCommands.add(new ConsumptionRateCommand(ModeTrim.MODE_01.build()));//燃油效率
        obdCommands.add(new FuelRailPressureCommand(ModeTrim.MODE_01.build()));//油轨压力（柴油或汽油直喷）
        return obdCommands;
    }

    /**
     * 发送命令
     */
    private void executeObdCommand(BluetoothSocket socket, ObdCommand command) {
        try {
            command.run(socket.getInputStream(), socket.getOutputStream());
            tripRecord.updateTrip(command.getName(), command);
            Message msg = mHandler.obtainMessage();
            msg.what = 100;
            msg.obj = tripRecord;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 发送命令
     */
    private void executeObdCommand(BluetoothSocket socket, List<ObdCommand> commands) {
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
                e.printStackTrace();
                LogE("执行命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * @param tripRecord OBD数据
     */
    private void setView(CheckRecord tripRecord) {
        if (tripRecord != null) {
            if (!StringUtil.isNull(tripRecord.getmFuelRailPressure())) {
                String FuelRailPressure = tripRecord.getmFuelRailPressure().replace("kPa", "");
                dashFuelRailPressure.setVelocity(Float.parseFloat(TextUtils.isEmpty(FuelRailPressure) ? "0" : FuelRailPressure) / 1000);
                tvFuelRailPressure.setText(String.valueOf(Float.parseFloat(TextUtils.isEmpty(FuelRailPressure) ? "0" : FuelRailPressure) / 1000));
            }

            if (!StringUtil.isNull(tripRecord.getmFuelConsumptionRate())) {
                String fuelRate = tripRecord.getmFuelConsumptionRate().replace("L/h", "");
                dashFuelRate.setVelocity(Float.parseFloat(TextUtils.isEmpty(fuelRate) ? "0" : fuelRate));
                tvFuelRate.setText(TextUtils.isEmpty(fuelRate) ? "0" : fuelRate);
            }

            if (!StringUtil.isNull(tripRecord.getmFuelPressure())) {
                String pressure = tripRecord.getmFuelPressure().replace("kPa", "");
                tvFuelPressure.setText(TextUtils.isEmpty(pressure) ? "0" : pressure);
                dashFuelPressure.setVelocity(Float.parseFloat(TextUtils.isEmpty(pressure) ? "0" : pressure));
            }
            if (!StringUtil.isNull(tripRecord.getmThrottlePos())) {
                String ThrottlePos = tripRecord.getmThrottlePos().replace("%", "");
                dashThrottlePos.setVelocity(Float.parseFloat(TextUtils.isEmpty(ThrottlePos) ? "0" : ThrottlePos));
                tvIntakeAirTemp.setText(TextUtils.isEmpty(ThrottlePos) ? "0" : ThrottlePos);
            }
        }
    }


    class MyCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeObdCommand(MainApplication.getBluetoothSocket(), getCommands());
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
            ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
            finish();
        }
        return true;
    }
}