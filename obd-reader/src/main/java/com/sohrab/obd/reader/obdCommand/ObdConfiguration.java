package com.sohrab.obd.reader.obdCommand;

import com.sohrab.obd.reader.enums.AbsThrottlePosTrim;
import com.sohrab.obd.reader.enums.CatalystTrim;
import com.sohrab.obd.reader.enums.FuelTrim;
import com.sohrab.obd.reader.enums.ModeTrim;
import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.enums.OxygenSensorTrim;
import com.sohrab.obd.reader.obdCommand.control.AbsoluteEvapSystemVaporPressureCommand;
import com.sohrab.obd.reader.obdCommand.control.AbsoluteThrottlePositionCommand;
import com.sohrab.obd.reader.obdCommand.control.AcceleratorPedalPositionCommand;
import com.sohrab.obd.reader.obdCommand.control.ActualEnginePercentTorqueCommand;
import com.sohrab.obd.reader.obdCommand.control.CatalystTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.control.DPFTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.control.DistanceMILOnCommand;
import com.sohrab.obd.reader.obdCommand.control.DistanceSinceCCCommand;
import com.sohrab.obd.reader.obdCommand.control.DriverDemandEnginePercentTorqueCommand;
import com.sohrab.obd.reader.obdCommand.control.DtcNumberCommand;
import com.sohrab.obd.reader.obdCommand.control.EGRCommand;
import com.sohrab.obd.reader.obdCommand.control.EGRErrorCommand;
import com.sohrab.obd.reader.obdCommand.control.EngineFrictionPercentTorqueCommand;
import com.sohrab.obd.reader.obdCommand.control.EngineReferenceTorqueCommand;
import com.sohrab.obd.reader.obdCommand.control.EthanolFuelRateCommand;
import com.sohrab.obd.reader.obdCommand.control.EvaporativePurgeCommand;
import com.sohrab.obd.reader.obdCommand.control.FuelRailAbsPressureCommand;
import com.sohrab.obd.reader.obdCommand.control.HybridBatteryPackRemainingLifeCommand;
import com.sohrab.obd.reader.obdCommand.control.IgnitionMonitorCommand;
import com.sohrab.obd.reader.obdCommand.control.MaxAirFlowMassRateCommand;
import com.sohrab.obd.reader.obdCommand.control.ModuleVoltageCommand;
import com.sohrab.obd.reader.obdCommand.control.MeterCommand;
import com.sohrab.obd.reader.obdCommand.control.OxygenSensorTrimCommand;
import com.sohrab.obd.reader.obdCommand.control.PendingTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.PermanentTroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.RelativeAcceleratorPedalPosCommand;
import com.sohrab.obd.reader.obdCommand.control.RelativeThrottlePositionCommand;
import com.sohrab.obd.reader.obdCommand.control.SystemVaporPressureCommand;
import com.sohrab.obd.reader.obdCommand.control.TimeRunMILONCommand;
import com.sohrab.obd.reader.obdCommand.control.TimeSinceTroubleCodesClearedCommand;
import com.sohrab.obd.reader.obdCommand.control.TimingAdvanceCommand;
import com.sohrab.obd.reader.obdCommand.control.TroubleCodesCommand;
import com.sohrab.obd.reader.obdCommand.control.VinCommand;
import com.sohrab.obd.reader.obdCommand.control.WarmUpSinceCodesClearedCommand;
import com.sohrab.obd.reader.obdCommand.engine.AbsoluteLoadCommand;
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
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioEightCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioFiveCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioFourCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioOneCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioSevenCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioSixCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioThreeCommand;
import com.sohrab.obd.reader.obdCommand.fuel.WidebandAirFuelRatioTwoCommand;
import com.sohrab.obd.reader.obdCommand.pressure.BarometricPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.EvapSystemVaporPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelRailPressureCommand;
import com.sohrab.obd.reader.obdCommand.pressure.FuelRailPressureManifoldVacuumCommand;
import com.sohrab.obd.reader.obdCommand.pressure.IntakeManifoldPressureCommand;
import com.sohrab.obd.reader.obdCommand.protocol.DescribeProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.DescribeProtocolNumberCommand;
import com.sohrab.obd.reader.obdCommand.protocol.EchoOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.LineFeedOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SelectProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SpacesOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.TimeoutCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AirIntakeTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.AmbientAirTemperatureCommand;
import com.sohrab.obd.reader.obdCommand.temperature.EngineCoolantTemperatureCommand;

import java.util.ArrayList;

/**
 * Created by "Sohrab" on 1/9/2018.
 */

public class ObdConfiguration {

    private static ArrayList<ObdCommand> mObdCommands;

    public static ArrayList<ObdCommand> getObdCommands(ModeTrim modeTrim) {
        if (mObdCommands == null)
            getDefaultObdCommand(modeTrim);
        return mObdCommands;
    }

    private static void getDefaultObdCommand(ModeTrim modeTrim) {
        mObdCommands = new ArrayList<>();

        mObdCommands.add(new ObdResetCommand());//重置OBD连接
        mObdCommands.add(new EchoOffCommand());//关闭回声
        mObdCommands.add(new LineFeedOffCommand());//关闭换行
        mObdCommands.add(new SpacesOffCommand());//关闭空格
        mObdCommands.add(new TimeoutCommand(62));//这将设置OBD接口启动的时间值（毫秒）将等待ECU的响应。如果超过，则响应为“无数据”。
        mObdCommands.add(new SelectProtocolCommand(ObdProtocols.AUTO));//选择要使用的协议
        mObdCommands.add(new IgnitionMonitorCommand());//"AT IGN" 点火
        mObdCommands.add(new DescribeProtocolCommand());//"AT DP"
        mObdCommands.add(new DescribeProtocolNumberCommand());//"AT DPN"

        // mObdCommands.add(new FreezeFrameCommand(ModeTrim.MODE_02));//"02 02" 冻结的DTC
        //mObdCommands.add(new ModifiedTroubleCodesObdCommand());//"03" 故障代码
        mObdCommands.add(new DtcNumberCommand(modeTrim.buildObdCommand()));//"01 01" DTC情况指示
        mObdCommands.add(new FuelSystemStatusCommand(modeTrim.buildObdCommand()));//"01 03"燃油系统状态
        mObdCommands.add(new LoadCommand(modeTrim.buildObdCommand()));//"01 04" 发动机负荷
        mObdCommands.add(new EngineCoolantTemperatureCommand(modeTrim.buildObdCommand()));//"01 05"//发动机冷媒温度
        mObdCommands.add(new FuelTrimCommand(modeTrim.buildObdCommand(), FuelTrim.LONG_TERM_BANK_1));//燃油调节命令 长期燃油调节库1
        mObdCommands.add(new FuelTrimCommand(modeTrim.buildObdCommand(), FuelTrim.LONG_TERM_BANK_2));//燃油调节命令  长期燃油调节库2
        mObdCommands.add(new FuelTrimCommand(modeTrim.buildObdCommand(), FuelTrim.SHORT_TERM_BANK_1));//燃油调节命令 短期燃油调节库1
        mObdCommands.add(new FuelTrimCommand(modeTrim.buildObdCommand(), FuelTrim.SHORT_TERM_BANK_2));//燃油调节命令  短期燃油调节库2
        mObdCommands.add(new FuelPressureCommand(modeTrim.buildObdCommand()));//"01 0A" 油压
        mObdCommands.add(new IntakeManifoldPressureCommand(modeTrim.buildObdCommand()));//"01 0B" 邮箱压力绝对值
        mObdCommands.add(new RPMCommand(modeTrim.buildObdCommand()));//"01 0C" 发动机转速
        mObdCommands.add(new SpeedCommand(modeTrim.buildObdCommand()));//"01 0D" 车辆速度
        mObdCommands.add(new TimingAdvanceCommand(modeTrim.buildObdCommand()));//"01 0E" 点火提前值
        mObdCommands.add(new AirIntakeTemperatureCommand(modeTrim.buildObdCommand()));//"01 0F" 邮箱空气温度
        mObdCommands.add(new MassAirFlowCommand(modeTrim.buildObdCommand()));//"01 10" MAF空气流量速率
        mObdCommands.add(new ThrottlePositionCommand(modeTrim.buildObdCommand()));//"01 11"节气门位置
        mObdCommands.add(new RuntimeCommand(modeTrim.buildObdCommand()));//"01 1F" 发动机启动后的运行时间


        mObdCommands.add(new DistanceMILOnCommand(modeTrim.buildObdCommand()));//"01 21" 故障指示灯（MIL）亮时行驶的距离
        mObdCommands.add(new FuelRailPressureManifoldVacuumCommand(modeTrim.buildObdCommand()));//"01 22" 油轨压力（相对进气歧管真空度）
        mObdCommands.add(new FuelRailPressureCommand(modeTrim.buildObdCommand()));//"01 23" 油轨压力（柴油或汽油直喷）
        mObdCommands.add(new EGRCommand(modeTrim.buildObdCommand()));//"01 2C" 废气循环命令
        mObdCommands.add(new EGRErrorCommand(modeTrim.buildObdCommand()));//"01 2D" 废气循环错误
        mObdCommands.add(new EvaporativePurgeCommand(modeTrim.buildObdCommand()));//"01 2E" 蒸发净化
        mObdCommands.add(new FuelLevelCommand(modeTrim.buildObdCommand()));//"01 2F" 油量液位情况
        mObdCommands.add(new WarmUpSinceCodesClearedCommand(modeTrim.buildObdCommand()));//"01 30" 代码清除后的预热
        mObdCommands.add(new DistanceSinceCCCommand(modeTrim.buildObdCommand()));//"01 31" 故障码清除后行驶里程
        mObdCommands.add(new SystemVaporPressureCommand(modeTrim.buildObdCommand()));//"01 32" 系统蒸汽压力
        mObdCommands.add(new BarometricPressureCommand(modeTrim.buildObdCommand()));//"01 33" 绝对大气压
        mObdCommands.add(new WidebandAirFuelRatioOneCommand(modeTrim.buildObdCommand()));//"01 34" 氧气侦测器1 燃油-空气当量比 电流
        mObdCommands.add(new WidebandAirFuelRatioTwoCommand(modeTrim.buildObdCommand()));//"01 35" 氧气侦测器2 燃油-空气当量比 电流
        mObdCommands.add(new WidebandAirFuelRatioThreeCommand(modeTrim.buildObdCommand()));//"01 36" 氧气侦测器3 燃油-空气当量比 电流
        mObdCommands.add(new WidebandAirFuelRatioFourCommand(modeTrim.buildObdCommand()));//"01 37" 氧气侦测器4 燃油-空气当量比 电流
        mObdCommands.add(new WidebandAirFuelRatioFiveCommand(modeTrim.buildObdCommand()));//"01 38" 氧气侦测器5 燃油-空气当量比 电流
        mObdCommands.add(new WidebandAirFuelRatioSixCommand(modeTrim.buildObdCommand()));//"01 39" 氧气侦测器6 燃油-空气当量比 电流
        mObdCommands.add(new WidebandAirFuelRatioSevenCommand(modeTrim.buildObdCommand()));//"01 3A" 氧气侦测器7 燃油-空气当量比 电流
        mObdCommands.add(new WidebandAirFuelRatioEightCommand(modeTrim.buildObdCommand()));//"01 3B" 氧气侦测器8 燃油-空气当量比 电流
        mObdCommands.add(new CatalystTemperatureCommand(modeTrim.buildObdCommand(), CatalystTrim.Catalyst_Temperature_Bank_1_Sensor_1));//"01 3C" 催化剂温度:Bank1,感测器1
        mObdCommands.add(new CatalystTemperatureCommand(modeTrim.buildObdCommand(), CatalystTrim.Catalyst_Temperature_Bank_2_Sensor_1));//"01 3C" 催化剂温度:Bank2,感测器1
        mObdCommands.add(new CatalystTemperatureCommand(modeTrim.buildObdCommand(), CatalystTrim.Catalyst_Temperature_Bank_1_Sensor_2));//"01 3C" 催化剂温度:Bank1,感测器2
        mObdCommands.add(new CatalystTemperatureCommand(modeTrim.buildObdCommand(), CatalystTrim.Catalyst_Temperature_Bank_2_Sensor_2));//"01 3C" 催化剂温度:Bank2,感测器2


        mObdCommands.add(new ModuleVoltageCommand(modeTrim.buildObdCommand()));//"01 42" 模块控制组电压
        mObdCommands.add(new AbsoluteLoadCommand(modeTrim.buildObdCommand()));//"01 43" 绝对载荷
        mObdCommands.add(new AirFuelRatioCommand(modeTrim.buildObdCommand()));//"01 44" 燃油-空气命令等效比
        mObdCommands.add(new RelativeThrottlePositionCommand(modeTrim.buildObdCommand()));//"01 45" 相对节气门位置
        mObdCommands.add(new AmbientAirTemperatureCommand(modeTrim.buildObdCommand()));//"01 46" 环境空气温度
        mObdCommands.add(new AbsoluteThrottlePositionCommand(modeTrim.buildObdCommand(), AbsThrottlePosTrim.ABS_THROTTLE_POS_B));//"01 47" 绝对油门位置B
        mObdCommands.add(new AbsoluteThrottlePositionCommand(modeTrim.buildObdCommand(), AbsThrottlePosTrim.ABS_THROTTLE_POS_C));//"01 48" 绝对油门位置C
        mObdCommands.add(new AcceleratorPedalPositionCommand(modeTrim.buildObdCommand(), AbsThrottlePosTrim.ACC_PEDAL_POS_D));//"01 49" 加速踏板位置D
        mObdCommands.add(new AcceleratorPedalPositionCommand(modeTrim.buildObdCommand(), AbsThrottlePosTrim.ACC_PEDAL_POS_E));//"01 4A" 加速踏板位置E
        mObdCommands.add(new AcceleratorPedalPositionCommand(modeTrim.buildObdCommand(), AbsThrottlePosTrim.ACC_PEDAL_POS_F));//"01 4B" 加速踏板位置F
        mObdCommands.add(new AcceleratorPedalPositionCommand(modeTrim.buildObdCommand(), AbsThrottlePosTrim.THROTTLE_ACTUATOR));//"01 4C" 油门执行器控制值
        mObdCommands.add(new TimeRunMILONCommand(modeTrim.buildObdCommand()));//"01 4D"MIL灯亮的行驶时间
        mObdCommands.add(new TimeSinceTroubleCodesClearedCommand(modeTrim.buildObdCommand()));//"01 4E" 故障代码清除后的时间
        mObdCommands.add(new MaxAirFlowMassRateCommand(modeTrim.buildObdCommand()));//"01 50" 质量空气流量计的最大空气流率
        mObdCommands.add(new FindFuelTypeCommand(modeTrim.buildObdCommand()));//"01 51"燃料种类
        mObdCommands.add(new EthanolFuelRateCommand(modeTrim.buildObdCommand()));//"01 52"乙醇燃料百分比
        mObdCommands.add(new AbsoluteEvapSystemVaporPressureCommand(modeTrim.buildObdCommand()));//"01 53"蒸发系统绝对蒸气压力
        mObdCommands.add(new EvapSystemVaporPressureCommand(modeTrim.buildObdCommand()));//"01 54"蒸发系统（相对）蒸气压力
        mObdCommands.add(new OxygenSensorTrimCommand(modeTrim.buildObdCommand(), OxygenSensorTrim.SHORT_A_BANK1_B_BANK3));//"01 55"第二侧氧气侦测器短期修正，A：bank 1, B：bank 3
        mObdCommands.add(new OxygenSensorTrimCommand(modeTrim.buildObdCommand(), OxygenSensorTrim.LONG_A_BANK1_B_BANK3));//"01 56" 第二侧氧气侦测器长期修正，A：bank 1, B：bank 3
        mObdCommands.add(new OxygenSensorTrimCommand(modeTrim.buildObdCommand(), OxygenSensorTrim.SHORT_A_BANK2_B_BANK4));//"01 57" 第二侧氧气侦测器短期修正，A：bank 2, B：bank 4
        mObdCommands.add(new OxygenSensorTrimCommand(modeTrim.buildObdCommand(), OxygenSensorTrim.LONG_A_BANK2_B_BANK4));//"01 58" 第二侧氧气侦测器长期修正，A：bank 2, B：bank 4
        mObdCommands.add(new FuelRailAbsPressureCommand(modeTrim.buildObdCommand()));//"01 59" 高压共轨绝对压力
        mObdCommands.add(new RelativeAcceleratorPedalPosCommand(modeTrim.buildObdCommand()));//"01 5A" 加速踏板相对位置
        mObdCommands.add(new HybridBatteryPackRemainingLifeCommand(modeTrim.buildObdCommand()));//"01 5B" 油电混合电池组剩下寿命
        mObdCommands.add(new OilTempCommand(modeTrim.buildObdCommand()));//"01 5C"发动机油温
        mObdCommands.add(new ConsumptionRateCommand(modeTrim.buildObdCommand()));//"01 5E" 燃油效率
        mObdCommands.add(new DriverDemandEnginePercentTorqueCommand(modeTrim.buildObdCommand()));//"01 61" 驾驶的发动机命令-力矩百分比
        mObdCommands.add(new ActualEnginePercentTorqueCommand(modeTrim.buildObdCommand()));//"01 62" 实际发动机-力矩百分比
        mObdCommands.add(new EngineReferenceTorqueCommand(modeTrim.buildObdCommand()));//"01 63" 发动机参考力矩
        mObdCommands.add(new DPFTemperatureCommand(modeTrim.buildObdCommand()));//"01 7C" 柴油粒子过滤器（DPF）温度
        mObdCommands.add(new EngineFrictionPercentTorqueCommand(modeTrim.buildObdCommand()));//"01 8E" 发动机摩擦力-力矩百分比
        mObdCommands.add(new ModifiedOdometerCommand(modeTrim.buildObdCommand()));//"01 A6" 里程
        mObdCommands.add(new VinCommand(ModeTrim.MODE_09));//"09 02" 车辆识别码

        mObdCommands.add(new ModifiedTroubleCodesObdCommand());//"03" 故障代码
        mObdCommands.add(new ModifiedPermanentTroubleCodesCommand());//"0A" 永久故障码
        mObdCommands.add(new ModifiedPendingTroubleCodesCommand());//"07" 未解决故障码
    }

    public static class ModifiedTroubleCodesObdCommand extends TroubleCodesCommand {
        @Override
        public String getResult() {
            //输出中删除不必要的响应，因为这会导致错误的错误代码
            return rawData.replace("SEARCHING...", "").replace("NODATA", "");
        }
    }

    public static class ModifiedPermanentTroubleCodesCommand extends PermanentTroubleCodesCommand {
        @Override
        public String getResult() {
            //输出中删除不必要的响应，因为这会导致错误的错误代码
            return rawData.replace("SEARCHING...", "").replace("NODATA", "");
        }
    }

    public static class ModifiedPendingTroubleCodesCommand extends PendingTroubleCodesCommand {
        @Override
        public String getResult() {
            //输出中删除不必要的响应，因为这会导致错误的错误代码
            return rawData.replace("SEARCHING...", "").replace("NODATA", "");
        }
    }

    public static class ModifiedOdometerCommand extends MeterCommand {
        public ModifiedOdometerCommand(String mode) {
            super(mode);
        }

        @Override
        public String getResult() {
            //输出中删除不必要的响应，因为这会导致错误的错误代码
            return rawData.replace("SEARCHING...", "").replace("NODATA", "");
        }
    }
}