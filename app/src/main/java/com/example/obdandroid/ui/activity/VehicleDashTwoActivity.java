package com.example.obdandroid.ui.activity;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.config.CheckRecord;
import com.example.obdandroid.ui.view.PhilText;
import com.example.obdandroid.ui.view.dashView.CustomerDashboardViewLight;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.fuel.ConsumptionRateCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelRailPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.EchoOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.LineFeedOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SpacesOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.TimeoutCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Jealous
 * 日期：2021/3/10 0010
 * 描述：
 */
public class VehicleDashTwoActivity extends BaseActivity {
    private PhilText tvFuelRate;
    private PhilText tvIntakeAirTemp;
    private PhilText tvFuelRailPressure;
    private CustomerDashboardViewLight dashIntakeAirTemp;
    private CheckRecord tripRecord;
    private Thread CommandThread = new Thread(new ObdsCommand());
    private PhilText tvFuelPressure;
    private CustomerDashboardViewLight dashFuelPressure;
    private CustomerDashboardViewLight dashFuelRate;
    private CustomerDashboardViewLight dashFuelRailPressure;


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
        tvFuelPressure = findViewById(R.id.tvFuelPressure);
        dashFuelPressure = findViewById(R.id.dashFuelPressure);
        tvFuelRate = getView(R.id.tvFuelRate);
        tvIntakeAirTemp = getView(R.id.tvIntakeAirTemp);
        tvFuelRailPressure = getView(R.id.tvFuelRailPressure);
        dashIntakeAirTemp = getView(R.id.dashIntakeAirTemp);
        dashFuelRate = findViewById(R.id.dashFuelRate);
        dashFuelRailPressure = findViewById(R.id.dashFuelRailPressure);
        CheckRecord.getTriRecode(context,getToken()).clear();
        tripRecord = CheckRecord.getTriRecode(context,getToken());
        ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(true);
        setFuelLevel();
        setInsFuelConsumption();
        setDrivingFuelConsumption();
        setIdlingFuelConsumption();
        startCommand();
        titleBarSet.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                stopThread();
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
        CommandThread.start();
    }

    /**
     * 中止线程
     */
    private void stopThread() {
        if (CommandThread != null) {
            CommandThread.interrupt();
            CommandThread = null;
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
     * 设置进气温度仪表
     */
    private void setDrivingFuelConsumption() {
        dashIntakeAirTemp.setmSection(10);
        dashIntakeAirTemp.setmHeaderText(" °C ");
        dashIntakeAirTemp.setmMax(260);
        dashIntakeAirTemp.setmMin(-40);
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
     * 读取进气歧管压力
     */
    private synchronized void executeObdCommand(BluetoothSocket socket, List<ObdCommand> commands) {
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                command.run(socket.getInputStream(), socket.getOutputStream());
                LogE("结果是:: " + command.getFormattedResult() + " :: name is :: " + command.getName());
                tripRecord.updateTrip(command.getName(), command);
                setView(tripRecord);
            } catch (Exception e) {
                e.printStackTrace();
                LogE("执行命令异常  :: " + e.getMessage());
            }
        }
    }

    /**
     * 读取进气歧管压力
     */
    private synchronized void executeCommand(BluetoothSocket socket, List<ObdCommand> commands) {
        for (int i = 0; i < commands.size(); i++) {
            ObdCommand command = commands.get(i);
            try {
                command.run(socket.getInputStream(), socket.getOutputStream());
                LogE("结果是:: " + command.getFormattedResult() + " :: name is :: " + command.getName());
            } catch (Exception e) {
                e.printStackTrace();
                LogE("执行命令异常  :: " + e.getMessage());
            }
        }
    }

    private List<ObdCommand> getCommands() {
        List<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.clear();
        obdCommands.add(new FuelPressureCommand(ModeTrim.MODE_01.buildObdCommand()));//油压
        obdCommands.add(new AirIntakeTemperatureCommand(ModeTrim.MODE_01.buildObdCommand()));//邮箱空气温度
        obdCommands.add(new ConsumptionRateCommand(ModeTrim.MODE_01.buildObdCommand()));//燃油效率
        obdCommands.add(new FuelRailPressureCommand(ModeTrim.MODE_01.buildObdCommand()));//油轨压力（柴油或汽油直喷）
        return obdCommands;
    }

    private List<ObdCommand> getElpCommands() {
        List<ObdCommand> obdCommands = new ArrayList<>();
        obdCommands.clear();
        obdCommands.add(new ObdResetCommand());
        obdCommands.add(new EchoOffCommand());
        obdCommands.add(new LineFeedOffCommand());
        obdCommands.add(new SpacesOffCommand());
        obdCommands.add(new TimeoutCommand(62));
        return obdCommands;
    }

    /**
     * @param tripRecord OBD数据
     */
    private void setView(CheckRecord tripRecord) {
        if (tripRecord != null) {
            String FuelRailPressure = tripRecord.getmFuelRailPressure().replace("kPa", "");
            dashFuelRailPressure.setVelocity(Float.parseFloat(TextUtils.isEmpty(FuelRailPressure) ? "0" : FuelRailPressure) / 1000);
            tvFuelRailPressure.setText(String.valueOf(Float.parseFloat(TextUtils.isEmpty(FuelRailPressure) ? "0" : FuelRailPressure) / 1000));

            String fuelRate = tripRecord.getmFuelConsumptionRate().replace("L/h", "");
            dashFuelRate.setVelocity(Float.parseFloat(TextUtils.isEmpty(fuelRate) ? "0" : fuelRate));
            tvFuelRate.setText(TextUtils.isEmpty(fuelRate) ? "0" : fuelRate);

            String pressure = tripRecord.getmFuelPressure().replace("kPa", "");
            tvFuelPressure.setText(TextUtils.isEmpty(pressure) ? "0" : pressure);
            dashFuelPressure.setVelocity(Float.parseFloat(TextUtils.isEmpty(pressure) ? "0" : pressure));

            dashIntakeAirTemp.setVelocity(tripRecord.getmIntakeAirTemp());
            tvIntakeAirTemp.setText(String.valueOf(tripRecord.getmIntakeAirTemp()));
        }
    }


    class ObdsCommand implements Runnable {

        @Override
        public void run() {
            executeCommand(MainApplication.getBluetoothSocket(), getElpCommands());
            while (ObdPreferences.get(getApplicationContext()).getServiceRunningStatus()) {
                executeObdCommand(MainApplication.getBluetoothSocket(), getCommands());
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            stopThread();
            ObdPreferences.get(getApplicationContext()).setServiceRunningStatus(false);
            finish();
        }
        return true;
    }
}