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
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelLevelCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.trip.TripRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/3/10 0010
 * 描述：
 */
public class VehicleDashActivityTwo extends BaseActivity {
    private PhilText tvmIdlingFuelConsumption;
    private PhilText tvmInsFuelConsumption;
    private PhilText tvFuelLevel;
    private PhilText tvDrivingFuelConsumption;
    private CustomerDashboardViewLight dashInsFuelConsumption;
    private CustomerDashboardViewLight dashIdlingFuelConsumption;
    private CustomerDashboardViewLight dashFuelLevel;
    private CustomerDashboardViewLight dashDrivingFuelConsumption;
    private TripRecord tripRecord;
    private Thread mFuelLevelCommand = new Thread(new MyFuelLevelCommand());
    private Thread mIntakeManifoldPressureCommand = new Thread(new MyIntakeManifoldPressureCommand());
    private Thread mMassAirFlowCommand = new Thread(new MyMassAirFlowCommand());
    private List<ObdCommand> commands = new ArrayList<>();


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
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        tvmIdlingFuelConsumption = getView(R.id.tvmIdlingFuelConsumption);
        tvmInsFuelConsumption = getView(R.id.tvmInsFuelConsumption);
        tvFuelLevel = getView(R.id.tvFuelLevel);
        tvDrivingFuelConsumption = getView(R.id.tvDrivingFuelConsumption);
        dashInsFuelConsumption = getView(R.id.dashInsFuelConsumption);
        dashIdlingFuelConsumption = getView(R.id.dashIdlingFuelConsumption);
        dashFuelLevel = getView(R.id.dashFuelLevel);
        dashDrivingFuelConsumption = getView(R.id.dashDrivingFuelConsumption);
        TripRecord.getTriRecode(context).clear();
        tripRecord = TripRecord.getTriRecode(context);
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(true);
        commands = setCommands();
        setFuelLevel();
        setInsFuelConsumption();
        setDrivingFuelConsumption();
        setIdlingFuelConsumption();
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
        mFuelLevelCommand.start();
        mIntakeManifoldPressureCommand.start();
        mMassAirFlowCommand.start();
    }

    /**
     * 中止线程
     */
    private void stopThread() {
        if (mFuelLevelCommand != null) {
            mFuelLevelCommand.interrupt();
            mFuelLevelCommand = null;
        }
        if (mIntakeManifoldPressureCommand != null) {
            mIntakeManifoldPressureCommand.interrupt();
            mIntakeManifoldPressureCommand = null;
        }
        if (mMassAirFlowCommand != null) {
            mMassAirFlowCommand.interrupt();
            mMassAirFlowCommand = null;
        }
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
     * 读取进气歧管压力
     */
    private synchronized void executeIntakeManifoldPressureCommand() {
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                command.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
                LogE("结果是:: " + command.getFormattedResult() + " :: name is :: " + command.getName());
                tripRecord.updateTrip(command.getName(), command);
                setView(tripRecord);
            } catch (Exception e) {
                e.printStackTrace();
                LogE("执行命令异常  :: " + e.getMessage());
            }
        }
    }

    private List<ObdCommand> setCommands() {
        List<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.add(new ObdResetCommand());
        obdCommands.add(new AirIntakeTemperatureCommand());
        obdCommands.add(new IntakeManifoldPressureCommand());
        return obdCommands;
    }

    /**
     * 读取燃油油位
     */
    private synchronized void executeFuelLevelCommand() {
        try {
            FuelLevelCommand fuelLevelCommand = new FuelLevelCommand();//"01 2F"
            fuelLevelCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + fuelLevelCommand.getFormattedResult() + " :: name is :: " + fuelLevelCommand.getName());
            tripRecord.updateTrip(fuelLevelCommand.getName(), fuelLevelCommand);
            tvFuelLevel.setText(tripRecord.getmFuelLevel());
            dashFuelLevel.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmFuelLevel()) ? "0" : tripRecord.getmFuelLevel().replace("%", "")));
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }


    /**
     * 读取空气质量流量
     */
    private synchronized void executeMassAirFlowCommand() {
        try {
            MassAirFlowCommand massAirFlowCommand = new MassAirFlowCommand();//"01 10"//空气流量感测器（MAF）空气流率
            massAirFlowCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + massAirFlowCommand.getFormattedResult() + " :: name is :: " + massAirFlowCommand.getName());
            tripRecord.updateTrip(massAirFlowCommand.getName(), massAirFlowCommand);
            float DrivingFuelConsumption = BigDecimal.valueOf(tripRecord.getmDrivingFuelConsumption())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            dashDrivingFuelConsumption.setVelocity(DrivingFuelConsumption);
            tvDrivingFuelConsumption.setText(String.valueOf(DrivingFuelConsumption));
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * @param tripRecord OBD数据
     */
    private void setView(TripRecord tripRecord) {
        if (tripRecord != null) {
            float InsFuelConsumption = BigDecimal.valueOf(tripRecord.getmInsFuelConsumption())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            tvmInsFuelConsumption.setText(String.valueOf(InsFuelConsumption));
            dashInsFuelConsumption.setVelocity(InsFuelConsumption);
            dashIdlingFuelConsumption.setVelocity(tripRecord.getmIdlingFuelConsumption());
            tvmIdlingFuelConsumption.setText(String.valueOf(tripRecord.getmIdlingFuelConsumption()));
        }
    }


    class MyIntakeManifoldPressureCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeIntakeManifoldPressureCommand();
            }
        }
    }

    class MyFuelLevelCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeFuelLevelCommand();
            }
        }
    }

    class MyMassAirFlowCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeMassAirFlowCommand();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopThread();
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
    }
}