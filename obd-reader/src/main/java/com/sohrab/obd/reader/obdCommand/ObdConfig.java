package com.sohrab.obd.reader.obdCommand;

import android.content.Context;
import android.widget.Toast;

import com.sohrab.obd.reader.obdCommand.control.ModuleVoltageCommand;
import com.sohrab.obd.reader.obdCommand.control.OdometerCommand;
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.engine.RuntimeCommand;
import com.sohrab.obd.reader.obdCommand.fuel.AirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelLevelCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AmbientAirTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;

import java.util.ArrayList;

/**
 * 作者：Jealous
 * 日期：2021/2/24 0024
 * 描述：
 */
public class ObdConfig {
    private static ArrayList<ObdCommand> mObdCommands;

    public static ArrayList<ObdCommand> getmObdCommands() {
        if (mObdCommands == null)
            getDefaultObdCommand();
        return mObdCommands;
    }

    public static void setmObdCommands(Context context, ArrayList<ObdCommand> obdCommands) {
        if (mObdCommands == null) {
            mObdCommands = obdCommands;
            return;
        }
        Toast.makeText(context, "无法在ObdReaderService启动后添加命令!", Toast.LENGTH_SHORT).show();
    }

    private static void getDefaultObdCommand() {
        mObdCommands = new ArrayList<>();
        mObdCommands.add(new SpeedCommand());//"01 0D"
        mObdCommands.add(new RPMCommand());//"01 0C"
        mObdCommands.add(new RuntimeCommand());//"01 1F" 引擎运行时间
        mObdCommands.add(new MassAirFlowCommand());//"01 10"//空气流量感测器（MAF）空气流率

        //Pressure
        mObdCommands.add(new IntakeManifoldPressureCommand());//"01 0B"

        //Tempereture
        mObdCommands.add(new AmbientAirTemperatureCommand());//"01 46"
        mObdCommands.add(new EngineCoolantTemperatureCommand());//"01 05"//发动机冷媒温度
        mObdCommands.add(new OilTempCommand());//"01 5C"

        //Fuel
        mObdCommands.add(new FuelLevelCommand());//"01 2F"
        mObdCommands.add(new AirFuelRatioCommand());//"01 44"
        mObdCommands.add(new OdometerCommand());//"01 A6"

        //control
        mObdCommands.add(new ModuleVoltageCommand());//"01 42"

        //engine
        mObdCommands.add(new OilTempCommand());//"01 5C"
    }
}