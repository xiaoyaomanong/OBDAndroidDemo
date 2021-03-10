package com.example.obdandroid.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseFragment;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelLevelCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.trip.TripRecord;

import java.math.BigDecimal;

/**
 * 作者：Jealous
 * 日期：2021/3/10 0010
 * 描述：
 */
public class VehicleDashFragmentTwo extends BaseFragment {
    private PhilText tvmIdlingFuelConsumption;
    private PhilText tvmInsFuelConsumption;
    private PhilText tvFuelLevel;
    private PhilText tvDrivingFuelConsumption;
    private CustomerDashboardViewLight dashInsFuelConsumption;
    private CustomerDashboardViewLight dashIdlingFuelConsumption;
    private CustomerDashboardViewLight dashFuelLevel;
    private CustomerDashboardViewLight dashDrivingFuelConsumption;
    private Context context;
    private TripRecord tripRecord;
    private final Thread mFuelLevelCommand = new Thread(new MyFuelLevelCommand());
    private final Thread mIntakeManifoldPressureCommand = new Thread(new MyIntakeManifoldPressureCommand());
    private final Thread mMassAirFlowCommand = new Thread(new MyMassAirFlowCommand());
    private int flag=1;
    private boolean show;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dash_two;
    }
    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        show=isVisibleToUser;

    }
    @Override
    public void initView(View view, Bundle savedInstanceState) {
        context = getHoldingActivity();
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
        setFuelLevel();
        setInsFuelConsumption();
        setDrivingFuelConsumption();
        setIdlingFuelConsumption();
        startCommand();
    }

    private void startCommand() {
        LogE("是否连接:" + MainApplication.getBluetoothSocket().isConnected());
        // boolean isConnected = MainApplication.getBluetoothSocket().isConnected();
        boolean isConnected = true;
        if (isConnected) {
            mFuelLevelCommand.start();
         /*   mIntakeManifoldPressureCommand.start();
            mMassAirFlowCommand.start();*/
        } else {
            showToast("OBD连接已断开");
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
    private void executeIntakeManifoldPressureCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            AirIntakeTemperatureCommand airIntakeTemperatureCommand = new AirIntakeTemperatureCommand();
            airIntakeTemperatureCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + airIntakeTemperatureCommand.getFormattedResult() + " :: name is :: " + airIntakeTemperatureCommand.getName());
            tripRecord.updateTrip(airIntakeTemperatureCommand.getName(), airIntakeTemperatureCommand);
            IntakeManifoldPressureCommand intakeManifoldPressureCommand = new IntakeManifoldPressureCommand();//"01 0B"
            intakeManifoldPressureCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + intakeManifoldPressureCommand.getFormattedResult() + " :: name is :: " + intakeManifoldPressureCommand.getName());
            tripRecord.updateTrip(intakeManifoldPressureCommand.getName(), intakeManifoldPressureCommand);
            setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取燃油油位
     */
    private void executeFuelLevelCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
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
    private void executeMassAirFlowCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
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
            while (ObdPreferences.get(getContext()).getServiceRunning()) {
                executeIntakeManifoldPressureCommand();
            }
        }
    }

    class MyFuelLevelCommand implements Runnable {

        @Override
        public void run() {
            while (show) {
                LogE("2222");
                flag++;
                tvFuelLevel.setText(flag+"");
                // executeFuelLevelCommand();
            }
        }
    }

    class MyMassAirFlowCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getContext()).getServiceRunning()) {
                executeMassAirFlowCommand();
            }
        }
    }
}