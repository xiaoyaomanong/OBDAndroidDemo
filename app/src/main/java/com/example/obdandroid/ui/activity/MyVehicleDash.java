package com.example.obdandroid.ui.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.entity.SocketEntity;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.example.obdandroid.utils.BluetoothManager;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
import com.sohrab.obd.reader.obdCommand.control.ModuleVoltageCommand;
import com.sohrab.obd.reader.obdCommand.control.OdometerCommand;
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.engine.RuntimeCommand;
import com.sohrab.obd.reader.obdCommand.fuel.AirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelLevelCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AmbientAirTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;
import com.sohrab.obd.reader.trip.TripRecord;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 作者：Jealous
 * 日期：2021/1/26 0026
 * 描述：
 */
public class MyVehicleDash extends BaseActivity {
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
    private BluetoothSocket bluetoothSocket;
    private boolean isConnected;
    private Context context;
    private final Thread Speedthread = new Thread(() -> {
        while (!Thread.interrupted()) {
            executeSpeedCommand();
            executeRPMCommand();
            //executeRuntimeCommand();
            executeMassAirFlowCommand();
            executeIntakeManifoldPressureCommand();
            executeAmbientAirTemperatureCommand();
            executeEngineCoolantTemperatureCommand();
            executeFuelLevelCommand();
            executeAirFuelRatioCommand();
            executeOdometerCommand();
            executeModuleVoltageCommand();
            executeOilTempCommand();
        }
    });


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
        SocketEntity socketEntity = (SocketEntity) getIntent().getSerializableExtra("data");
        TitleBar titleBarSet = findViewById(R.id.titleBarSet);
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
        bluetoothSocket = socketEntity.getSocket();
        setRPM();
        setSpeed();
        setInsFuelConsumption();
        setEngineCoolantTemp();
        setFuelLevel();
        setDrivingFuelConsumption();
        setIdlingFuelConsumption();
        setEngineOilTemp();
        connectBtDevice(socketEntity.getAddress());
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                if (isConnected) {
                    closeSocket();
                    stopThread();
                    setResult(100, new Intent());
                }
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
     * 开启线程
     */
    private void startThread() {
        Speedthread.start();
    }

    /**
     * 中断线程
     */
    private void stopThread() {
        Speedthread.interrupt();
    }

    /**
     * @param address 蓝牙设备MAC地址
     *                启动与所选蓝牙设备的连接
     */
    private void connectBtDevice(String address) {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        try {
            bluetoothSocket = BluetoothManager.connect(device, (code, msg) -> {
                LogE("连接状态：" + msg);
                isConnected = code == 0;
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogE("建立连接时出错。 -> " + e.getMessage());
            LogE("此处在处理程序上收到的消息");
        }
        if (isConnected) {
            startThread();
        }
    }

    /**
     * 读取速度
     */
    private void executeSpeedCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            SpeedCommand speedCommand = new SpeedCommand();//"01 0D"
            speedCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + speedCommand.getFormattedResult() + " :: name is :: " + speedCommand.getName());
            tripRecord.updateTrip(speedCommand.getName(), speedCommand);
            LogE("速度:"+tripRecord.getSpeed());
            dashSpeed.setVelocity(tripRecord.getSpeed());
            float DrivingDuration = BigDecimal.valueOf(tripRecord.getDrivingDuration())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            tvDrivingDuration.setText(String.valueOf(DrivingDuration));
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取转速
     */
    private void executeRPMCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            RPMCommand rpmCommand = new RPMCommand();//"01 0C"
            rpmCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + rpmCommand.getFormattedResult() + " :: name is :: " + rpmCommand.getName());
            tripRecord.updateTrip(rpmCommand.getName(), rpmCommand);
            String rpm = TextUtils.isEmpty(tripRecord.getEngineRpm()) ? "0" : tripRecord.getEngineRpm();
            dashRPM.setVelocity(Float.parseFloat(rpm) / 1000);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取空气质量流量
     */
    private void executeMassAirFlowCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            MassAirFlowCommand massAirFlowCommand = new MassAirFlowCommand();//"01 10"//空气流量感测器（MAF）空气流率
            massAirFlowCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + massAirFlowCommand.getFormattedResult() + " :: name is :: " + massAirFlowCommand.getName());
            tripRecord.updateTrip(massAirFlowCommand.getName(), massAirFlowCommand);
            tvmMassAirFlow.setText(String.valueOf(tripRecord.getmMassAirFlow()));
            float DrivingFuelConsumption = BigDecimal.valueOf(tripRecord.getmDrivingFuelConsumption())
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            dashDrivingFuelConsumption.setVelocity(DrivingFuelConsumption);
            //setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取进气歧管压力
     */
    private void executeIntakeManifoldPressureCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            AirIntakeTemperatureCommand airIntakeTemperatureCommand=new   AirIntakeTemperatureCommand();
            airIntakeTemperatureCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + airIntakeTemperatureCommand.getFormattedResult() + " :: name is :: " + airIntakeTemperatureCommand.getName());
            tripRecord.updateTrip(airIntakeTemperatureCommand.getName(), airIntakeTemperatureCommand);
            IntakeManifoldPressureCommand intakeManifoldPressureCommand = new IntakeManifoldPressureCommand();//"01 0B"
            intakeManifoldPressureCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + intakeManifoldPressureCommand.getFormattedResult() + " :: name is :: " + intakeManifoldPressureCommand.getName());
            tripRecord.updateTrip(intakeManifoldPressureCommand.getName(), intakeManifoldPressureCommand);
            setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取环境空气温度
     */
    private void executeAmbientAirTemperatureCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            AmbientAirTemperatureCommand ambientAirTemperatureCommand = new AmbientAirTemperatureCommand();//"01 46"
            ambientAirTemperatureCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + ambientAirTemperatureCommand.getFormattedResult() + " :: name is :: " + ambientAirTemperatureCommand.getName());
            tripRecord.updateTrip(ambientAirTemperatureCommand.getName(), ambientAirTemperatureCommand);
            tvmAmbientAirTemp.setText(tripRecord.getmAmbientAirTemp());
            //setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取发动机冷却液温度
     */
    private void executeEngineCoolantTemperatureCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            EngineCoolantTemperatureCommand engineCoolantTemperatureCommand = new EngineCoolantTemperatureCommand();//"01 05"//发动机冷媒温度
            engineCoolantTemperatureCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + engineCoolantTemperatureCommand.getFormattedResult() + " :: name is :: " + engineCoolantTemperatureCommand.getName());
            tripRecord.updateTrip(engineCoolantTemperatureCommand.getName(), engineCoolantTemperatureCommand);
            dashEngineCoolantTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineCoolantTemp()) ? "0" : tripRecord.getmEngineCoolantTemp().replace("C", "")));
           // setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取燃油油位
     */
    private void executeFuelLevelCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            FuelLevelCommand fuelLevelCommand = new FuelLevelCommand();//"01 2F"
            fuelLevelCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + fuelLevelCommand.getFormattedResult() + " :: name is :: " + fuelLevelCommand.getName());
            tripRecord.updateTrip(fuelLevelCommand.getName(), fuelLevelCommand);
            tvmFuelLevel.setText(tripRecord.getmFuelLevel());
            dashFuelLevel.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmFuelLevel()) ? "0" : tripRecord.getmFuelLevel().replace("%", "")));
           // setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取宽带空燃比
     */
    private void executeAirFuelRatioCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            AirFuelRatioCommand airFuelRatioCommand = new AirFuelRatioCommand();//"01 44"
            airFuelRatioCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + airFuelRatioCommand.getFormattedResult() + " :: name is :: " + airFuelRatioCommand.getName());
            tripRecord.updateTrip(airFuelRatioCommand.getName(), airFuelRatioCommand);
            tvmAirFuelRatio.setText(tripRecord.getmAirFuelRatio());
            //setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取里程
     */
    private void executeOdometerCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            OdometerCommand odometerCommand = new OdometerCommand();//"01 A6"
            odometerCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + odometerCommand.getFormattedResult() + " :: name is :: " + odometerCommand.getName());
            tripRecord.updateTrip(odometerCommand.getName(), odometerCommand);
            tvmOdometer.setText(TextUtils.isEmpty(tripRecord.getmOdometer()) ? "0" : tripRecord.getmOdometer());
            //setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取控制模组电压
     */
    private void executeModuleVoltageCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            ModuleVoltageCommand moduleVoltageCommand = new ModuleVoltageCommand();//"01 42"
            moduleVoltageCommand.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + moduleVoltageCommand.getFormattedResult() + " :: name is :: " + moduleVoltageCommand.getName());
            tripRecord.updateTrip(moduleVoltageCommand.getName(), moduleVoltageCommand);
            tvmControlModuleVoltage.setText(tripRecord.getmControlModuleVoltage());
            //setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取发动机油温
     */
    private void executeOilTempCommand() {
        TripRecord tripRecord = TripRecord.getTriRecode(context);
        try {
            OilTempCommand oilTempCommand1 = new OilTempCommand();//"01 5C"
            oilTempCommand1.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            LogE("结果是:: " + oilTempCommand1.getFormattedResult() + " :: name is :: " + oilTempCommand1.getName());
            tripRecord.updateTrip(oilTempCommand1.getName(), oilTempCommand1);
            dashEngineOilTemp.setVelocity(Float.parseFloat(TextUtils.isEmpty(tripRecord.getmEngineOilTemp()) ? "0" : tripRecord.getmEngineOilTemp().replace("C", "")));
           // setView(tripRecord);
        } catch (Exception e) {
            LogE("执行命令异常  :: " + e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer"))) {
                LogE("命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * @param tripRecord OBD数据
     */
    @SuppressLint("SetTextI18n")
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

    private void closeSocket() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeSocket();
        stopThread();
    }
}