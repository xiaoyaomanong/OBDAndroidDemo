package com.example.obdandroid.config;


import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.obdCommand.ObdCommand;
import com.sohrab.obd.reader.obdCommand.SpeedCommand;
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
 * TODO put description
 */
public final class ObdConfig {

    public static ArrayList<ObdCommand> getCommands(String protocol) {
        ArrayList<ObdCommand> cmds = new ArrayList<>();
        //协议
        cmds.add(new ObdResetCommand());//重置OBD连接。。
        //cmds.add(new ObdWarmstartCommand());//热启动OBD连接。。。
        cmds.add(new SpacesOffCommand());//关闭空间。
        cmds.add(new EchoOffCommand());//关掉Echo
       // cmds.add(new HeadersOffCommand());//关闭headers
        cmds.add(new LineFeedOffCommand());//关闭换行
        cmds.add(new SelectProtocolCommand(ObdProtocols.valueOf(protocol)));//选择要使用的协议
       //cmds.add(new AdaptiveTimingCommand(1));//自适应定时命令
        //cmds.add(new AdaptiveTimingCommand(2));//自适应定时命令
        //cmds.add(new AvailablePidsCommand_01_20());//检索范围为01到20的可用PID。
        //cmds.add(new AvailablePidsCommand_21_40());//检索范围为21到40的可用PID。
        //cmds.add(new AvailablePidsCommand_41_60());//检索范围为41到60的可用PID。
        //cmds.add(new DescribeProtocolCommand());//描述当前的协议。如果选择了协议，并且自动选项同时选中的AT DP将在单词“ AUTO”前显示协议说明。 注意描述显示实际的协议名称，而不是数字由协议设置命令使用。
        //cmds.add(new DescribeProtocolNumberCommand());//用数字描述协议。它返回一个代表当前的数字obd协议。 如果自动搜索功能也启用后，数字将以字母开头'A'。 该数字与设置obdProtocol并测试obdProtocol命令。
        //cmds.add(new ResetTroubleCodesCommand());//重置故障代码。。。。
        cmds.add(new TimeoutCommand(62));//这将设置OBD接口的时间值（以毫秒为单位）将等待ECU的响应。 如果超过，则响应为“无数据”。
        // 操纵装置
       /* cmds.add(new DistanceMILOnCommand());//距离MIL命令(发动机电控系统故障报警灯)
        cmds.add(new DistanceSinceCCCommand());//自清除代码以来经过的距离。
        cmds.add(new DtcNumberCommand());//DTC号码
        cmds.add(new EquivalentRatioCommand()); //当量比
        cmds.add(new IgnitionMonitorCommand());//点火监控器
        cmds.add(new ModuleVoltageCommand());//模块电压
        cmds.add(new PendingTroubleCodesCommand());//待处理的故障代码
        cmds.add(new PermanentTroubleCodesCommand());//永久故障代码
        cmds.add(new TimingAdvanceCommand());//定时提前
        cmds.add(new TroubleCodesCommand());//故障代码
        cmds.add(new VinCommand());//车辆识别码*/

        // 引擎
      /*  cmds.add(new AbsoluteLoadCommand());//绝对负载
        cmds.add(new LoadCommand());//CD加载命令
        cmds.add(new MassAirFlowCommand());//空气质量流量
        cmds.add(new OilTempCommand());//显示当前的发动机机油温度。
        cmds.add(new RPMCommand());//显示当前的发动机每分钟转数（RPM）。
        cmds.add(new RuntimeCommand());//发动机运行时间。
        cmds.add(new ThrottlePositionCommand());//读取节气门位置的百分比。*/
        // 油箱
      /*  cmds.add(new AirFuelRatioCommand());//空气燃油混合比
        cmds.add(new ConsumptionRateCommand());//每小时油耗率。
        cmds.add(new FindFuelTypeCommand());//该命令旨在确定车辆燃油类型。
        cmds.add(new FuelLevelCommand());//获取燃油量百分比
        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1));//燃油调节命令 长期燃油调节库1
        cmds.add(new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_2));//燃油调节命令  长期燃油调节库2
        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_1));//燃油调节命令 短期燃油调节库1
        cmds.add(new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_2));//燃油调节命令  短期燃油调节库2
        cmds.add(new WidebandAirFuelRatioCommand());//宽带空燃比命令*/
        // 压力
       /* cmds.add(new BarometricPressureCommand());//气压
        cmds.add(new FuelPressureCommand());//燃油压力
        cmds.add(new FuelRailPressureCommand());//燃油导轨压力
        cmds.add(new IntakeManifoldPressureCommand());//进气歧管压力*/
        // 温度
        cmds.add(new AirIntakeTemperatureCommand());//进气温度
        cmds.add(new AmbientAirTemperatureCommand());//环境温度
        cmds.add(new EngineCoolantTemperatureCommand());//发动机冷却液温度
        // Misc
        cmds.add(new SpeedCommand());//当前速度
        return cmds;
    }
}
