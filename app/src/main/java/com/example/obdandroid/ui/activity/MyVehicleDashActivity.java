package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.obdCommand.control.ModuleVoltageCommand;
import com.sohrab.obd.reader.obdCommand.control.OdometerCommand;
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.fuel.AirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelLevelCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AmbientAirTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;
import com.sohrab.obd.reader.trip.CheckRecord;
import com.sohrab.obd.reader.trip.OBDTripEntity;
import com.sohrab.obd.reader.trip.TripRecord;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 作者：Jealous
 * 日期：2021/1/26 0026
 * 描述：
 */
public class MyVehicleDashActivity extends BaseActivity {
    private TextView tvmControlModuleVoltage;
    private TextView tvmFuelLevel;
    private TextView tvmMassAirFlow;
    private TextView tvmAmbientAirTemp;
    private PhilText tvmInsFuelConsumption;
    private PhilText tvmOdometer;
    private PhilText tvDrivingDuration;
    private PhilText tvMaxSpeed;
    private PhilText tvAverageSpeed;
    private CustomerDashboardViewLight dashSpeed;//车速
    private CustomerDashboardViewLight dashRPM;//转速
    private CustomerDashboardViewLight dashInsFuelConsumption;//瞬时油耗
    private CustomerDashboardViewLight dashEngineCoolantTemp;//冷却液温度
    private CustomerDashboardViewLight dashFuelLevel;//燃油油位
    private CustomerDashboardViewLight dashDrivingFuelConsumption;//行驶油耗
    private CustomerDashboardViewLight dashIdlingFuelConsumption;//怠速油耗
    private CustomerDashboardViewLight dashEngineOilTemp;//机油温度
    private final Thread mSpeedCommand = new Thread(new MySpeedCommand());
      private final Thread mAmbientAirTemperatureCommand = new Thread(new MyAmbientAirTemperatureCommand());
      private final Thread mEngineCoolantTemperatureCommand = new Thread(new MyEngineCoolantTemperatureCommand());
      private final Thread mFuelLevelCommand = new Thread(new MyFuelLevelCommand());
      private final Thread mIntakeManifoldPressureCommand = new Thread(new MyIntakeManifoldPressureCommand());
      private final Thread mMassAirFlowCommand = new Thread(new MyMassAirFlowCommand());
      private final Thread mOdometerCommand = new Thread(new MyOdometerCommand());
      private final Thread mOilTempCommand = new Thread(new MyOilTempCommand());
      private final Thread mRModuleVoltageCommand = new Thread(new MyRModuleVoltageCommand());
    private final Thread mRPMCommand = new Thread(new MyRPMCommand());
    private TripRecord tripRecord;

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
        Context context = this;
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
        tvmControlModuleVoltage = findViewById(R.id.tvmControlModuleVoltage);
        tvmFuelLevel = findViewById(R.id.tvmFuelLevel);
        tvmMassAirFlow = findViewById(R.id.tvmMassAirFlow);
        tvmAmbientAirTemp = findViewById(R.id.tvmAmbientAirTemp);
        tvmInsFuelConsumption = findViewById(R.id.tvmInsFuelConsumption);
        tvmOdometer = findViewById(R.id.tvmOdometer);
        dashSpeed = findViewById(R.id.dashSpeed);
        dashRPM = findViewById(R.id.dashRPM);
        tvMaxSpeed = findViewById(R.id.tvMaxSpeed);
        tvAverageSpeed = findViewById(R.id.tvAverageSpeed);
        tvDrivingDuration = findViewById(R.id.tvDrivingDuration);
        dashInsFuelConsumption = findViewById(R.id.dashInsFuelConsumption);
        dashEngineCoolantTemp = findViewById(R.id.dashEngineCoolantTemp);
        dashFuelLevel = findViewById(R.id.dashFuelLevel);
        dashDrivingFuelConsumption = findViewById(R.id.dashDrivingFuelConsumption);
        dashIdlingFuelConsumption = findViewById(R.id.dashIdlingFuelConsumption);
        dashEngineOilTemp = findViewById(R.id.dashEngineOilTemp);
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(true);
        TripRecord.getTriRecode(context).clear();
        tripRecord = TripRecord.getTriRecode(context);
        setRPM();
        setSpeed();
        setInsFuelConsumption();
        setEngineCoolantTemp();
        setFuelLevel();
        setDrivingFuelConsumption();
        setIdlingFuelConsumption();
        setEngineOilTemp();
        LogE("是否连接:" + MainApplication.getBluetoothSocket().isConnected());
        boolean isConnected = MainApplication.getBluetoothSocket().isConnected();
        if (isConnected) {
            mAmbientAirTemperatureCommand.start();
            mEngineCoolantTemperatureCommand.start();
            mFuelLevelCommand.start();
            mIntakeManifoldPressureCommand.start();
            mMassAirFlowCommand.start();
            mOdometerCommand.start();
            mOilTempCommand.start();
            mRModuleVoltageCommand.start();
            mRPMCommand.start();
            mSpeedCommand.start();
        } else {
            showToast("OBD连接已断开");
        }
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
     * 读取速度
     */
    private synchronized void executeSpeedCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            SpeedCommand speedCommand = new SpeedCommand();//"01 0D"
            speedCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + speedCommand.getFormattedResult() + " :: name is :: " + speedCommand.getName());
            tripRecord.updateTrip(speedCommand.getName(), speedCommand);
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
    private synchronized void executeRPMCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            RPMCommand rpmCommand = new RPMCommand();//"01 0C"
            rpmCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + rpmCommand.getFormattedResult() + " :: name is :: " + rpmCommand.getName());
            tripRecord.updateTrip(rpmCommand.getName(), rpmCommand);
            String rpm = TextUtils.isEmpty(tripRecord.getEngineRpm()) ? "0" : tripRecord.getEngineRpm();
            dashRPM.setVelocity(Float.parseFloat(rpm) / 1000);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取空气质量流量
     */
    private synchronized void executeMassAirFlowCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            MassAirFlowCommand massAirFlowCommand = new MassAirFlowCommand();//"01 10"//空气流量感测器（MAF）空气流率
            massAirFlowCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + massAirFlowCommand.getFormattedResult() + " :: name is :: " + massAirFlowCommand.getName());
            tripRecord.updateTrip(massAirFlowCommand.getName(), massAirFlowCommand);
            tvmMassAirFlow.setText(String.valueOf(tripRecord.getmMassAirFlow()));
            float DrivingFuelConsumption = BigDecimal.valueOf(tripRecord.getmDrivingFuelConsumption())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            dashDrivingFuelConsumption.setVelocity(DrivingFuelConsumption);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取进气歧管压力
     */
    private synchronized void executeIntakeManifoldPressureCommand() {
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
     * 读取环境空气温度
     */
    private synchronized void executeAmbientAirTemperatureCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            AmbientAirTemperatureCommand ambientAirTemperatureCommand = new AmbientAirTemperatureCommand();//"01 46"
            ambientAirTemperatureCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + ambientAirTemperatureCommand.getFormattedResult() + " :: name is :: " + ambientAirTemperatureCommand.getName());
            tripRecord.updateTrip(ambientAirTemperatureCommand.getName(), ambientAirTemperatureCommand);
            tvmAmbientAirTemp.setText(tripRecord.getmAmbientAirTemp());
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取发动机冷却液温度
     */
    private synchronized void executeEngineCoolantTemperatureCommand() {
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
     * 读取燃油油位
     */
    private synchronized void executeFuelLevelCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            FuelLevelCommand fuelLevelCommand = new FuelLevelCommand();//"01 2F"
            fuelLevelCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + fuelLevelCommand.getFormattedResult() + " :: name is :: " + fuelLevelCommand.getName());
            tripRecord.updateTrip(fuelLevelCommand.getName(), fuelLevelCommand);
            tvmFuelLevel.setText(tripRecord.getmFuelLevel());
            dashFuelLevel.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmFuelLevel()) ? "0" : tripRecord.getmFuelLevel().replace("%", "")));
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取里程
     */
    private synchronized void executeOdometerCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            OdometerCommand odometerCommand = new OdometerCommand();//"01 A6"
            odometerCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + odometerCommand.getFormattedResult() + " :: name is :: " + odometerCommand.getName());
            tripRecord.updateTrip(odometerCommand.getName(), odometerCommand);
            tvmOdometer.setText(TextUtils.isEmpty(tripRecord.getmOdometer()) ? "0" : tripRecord.getmOdometer());
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取控制模组电压
     */
    private synchronized void executeModuleVoltageCommand() {
        try {
            new ObdResetCommand().run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            ModuleVoltageCommand moduleVoltageCommand = new ModuleVoltageCommand();//"01 42"
            moduleVoltageCommand.run(MainApplication.getBluetoothSocket().getInputStream(), MainApplication.getBluetoothSocket().getOutputStream());
            LogE("结果是:: " + moduleVoltageCommand.getFormattedResult() + " :: name is :: " + moduleVoltageCommand.getName());
            tripRecord.updateTrip(moduleVoltageCommand.getName(), moduleVoltageCommand);
            tvmControlModuleVoltage.setText(tripRecord.getmControlModuleVoltage());
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
        }
    }

    /**
     * 读取发动机油温
     */
    private synchronized void executeOilTempCommand() {
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


    class MySpeedCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeSpeedCommand();
            }
        }
    }

    class MyRPMCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeRPMCommand();
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

    class MyIntakeManifoldPressureCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeIntakeManifoldPressureCommand();

            }
        }
    }

    class MyAmbientAirTemperatureCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeAmbientAirTemperatureCommand();

            }
        }
    }

    class MyEngineCoolantTemperatureCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeEngineCoolantTemperatureCommand();

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

    class MyOdometerCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeOdometerCommand();

            }
        }
    }

    class MyRModuleVoltageCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeModuleVoltageCommand();
            }
        }
    }

    class MyOilTempCommand implements Runnable {

        @Override
        public void run() {
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeOilTempCommand();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
    }

    @Override
    //安卓重写返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
            finish();
        }
        return true;
    }
}