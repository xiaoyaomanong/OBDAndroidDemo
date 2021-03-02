package com.sohrab.obd.reader.obdCommand;

import android.content.Context;
import android.widget.Toast;

import com.sohrab.obd.reader.enums.FuelTrim;
import com.sohrab.obd.reader.obdCommand.control.CommandedEGRCommand;
import com.sohrab.obd.reader.obdCommand.control.DistanceMILOnCommand;
import com.sohrab.obd.reader.obdCommand.control.DistanceSinceCCCommand;
import com.sohrab.obd.reader.obdCommand.control.DtcNumberCommand;
import com.sohrab.obd.reader.obdCommand.control.EquivalentRatioCommand;
import com.sohrab.obd.reader.obdCommand.control.IgnitionMonitorCommand;
import com.sohrab.obd.reader.obdCommand.control.ModuleVoltageCommand;
import com.sohrab.obd.reader.obdCommand.control.OdometerCommand;
import com.sohrab.obd.reader.obdCommand.control.PendingTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.PermanentTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.TimingAdvanceCommand;
import com.sohrab.obd.reader.obdCommand.control.TroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.VinCommand;
import com.sohrab.obd.reader.obdCommand.engine.AbsoluteLoadCommand;
import com.sohrab.obd.reader.obdCommand.engine.EngineFuelRateCommand;
import com.sohrab.obd.reader.obdCommand.engine.LoadCommand;
import com.sohrab.obd.reader.obdCommand.engine.MassAirFlowCommand;
import com.sohrab.obd.reader.obdCommand.engine.OilTempCommand;
import com.sohrab.obd.reader.obdCommand.engine.RPMCommand;
import com.sohrab.obd.reader.obdCommand.engine.RuntimeCommand;
import com.sohrab.obd.reader.obdCommand.engine.ThrottlePositionCommand;
import com.sohrab.obd.reader.obdCommand.fuel.AirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.fuel.ConsumptionRateCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FindFuelTypeCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelLevelCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelSystemStatusCommand;
import com.sohrab.obd.reader.obdCommand.fuel.FuelTrimCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioCommand;
import com.sohrab.obd.reader.obdCommand.pressure.BarometricPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelRailPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelRailPressureManifoldVacuumCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.DescribeProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.DescribeProtocolNumberCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AmbientAirTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;

import java.util.ArrayList;

/**
 * Created by "Sohrab" on 1/9/2018.
 */

public class ObdConfiguration {

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
        mObdCommands.add(new BarometricPressureCommand());//"01 33"
        mObdCommands.add(new FuelPressureCommand());//"01 0A"
        mObdCommands.add(new FuelRailPressureCommand());//"01 23"
        mObdCommands.add(new FuelRailPressureManifoldVacuumCommand());//"01 22"高压共轨压力（相对进气歧管真空）

        //Tempereture
        mObdCommands.add(new AirIntakeTemperatureCommand());//"01 0F"
        mObdCommands.add(new AmbientAirTemperatureCommand());//"01 46"
        mObdCommands.add(new EngineCoolantTemperatureCommand());//"01 05"//发动机冷媒温度
        mObdCommands.add(new OilTempCommand());//"01 5C"

        //Fuel
        mObdCommands.add(new FindFuelTypeCommand());//"01 51"
        mObdCommands.add(new ConsumptionRateCommand());//"01 5E"
        mObdCommands.add(new FuelLevelCommand());//"01 2F"
        mObdCommands.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1));//燃油调节命令 长期燃油调节库1
        mObdCommands.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_2));//燃油调节命令  长期燃油调节库2
        mObdCommands.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_1));//燃油调节命令 短期燃油调节库1
        mObdCommands.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_2));//燃油调节命令  短期燃油调节库2
        mObdCommands.add(new AirFuelRatioCommand());//"01 44"
        mObdCommands.add(new WidebandAirFuelRatioCommand());//"01 34"
        mObdCommands.add(new FuelSystemStatusCommand());//"01 03"
        mObdCommands.add(new OdometerCommand());//"01 A6"

        //control
        mObdCommands.add(new DistanceMILOnCommand());//"01 21"故障指示灯（MIL）亮时行驶的距离
        mObdCommands.add(new DistanceSinceCCCommand());//"01 31"
        mObdCommands.add(new DtcNumberCommand());//"01 01"
        mObdCommands.add(new EquivalentRatioCommand());//"01 44"
        mObdCommands.add(new IgnitionMonitorCommand());//"AT IGN"
        mObdCommands.add(new ModuleVoltageCommand());//"01 42"
        mObdCommands.add(new TimingAdvanceCommand());//"01 0E"
        mObdCommands.add(new VinCommand());//"09 02"
        mObdCommands.add(new CommandedEGRCommand());//"01 2C"
        //Trouble codes
        mObdCommands.add(new TroubleCodesCommand());//"03" 故障代码
        mObdCommands.add(new PermanentTroubleCodesCommand());//"0A" 永久故障码
        mObdCommands.add(new PendingTroubleCodesCommand());//"07" 未解决故障码
        //engine
        mObdCommands.add(new AbsoluteLoadCommand());//"01 43"
        mObdCommands.add(new LoadCommand());//"01 04"
        mObdCommands.add(new OilTempCommand());//"01 5C"
        mObdCommands.add(new ThrottlePositionCommand());//"01 11"
        //mObdCommands.add(new EngineFuelRateCommand());//"01 5E"

        //protocol
        mObdCommands.add(new DescribeProtocolCommand());//"AT DP"
        mObdCommands.add(new DescribeProtocolNumberCommand());//"AT DPN"
    }
}
