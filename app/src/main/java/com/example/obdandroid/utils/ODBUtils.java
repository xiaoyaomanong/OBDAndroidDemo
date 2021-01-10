package com.example.obdandroid.utils;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.obdandroid.config.ObdConfig;
import com.example.obdandroid.config.TAG;
import com.example.obdandroid.service.ObdCommandJob;
import com.example.obdandroid.ui.activity.MainActivity;
import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.control.DistanceMILOnCommand;
import com.github.pires.obd.commands.control.DistanceSinceCCCommand;
import com.github.pires.obd.commands.control.DtcNumberCommand;
import com.github.pires.obd.commands.control.EquivalentRatioCommand;
import com.github.pires.obd.commands.control.IgnitionMonitorCommand;
import com.github.pires.obd.commands.control.ModuleVoltageCommand;
import com.github.pires.obd.commands.control.PendingTroubleCodesCommand;
import com.github.pires.obd.commands.control.PermanentTroubleCodesCommand;
import com.github.pires.obd.commands.control.TimingAdvanceCommand;
import com.github.pires.obd.commands.control.TroubleCodesCommand;
import com.github.pires.obd.commands.control.VinCommand;
import com.github.pires.obd.commands.engine.AbsoluteLoadCommand;
import com.github.pires.obd.commands.engine.LoadCommand;
import com.github.pires.obd.commands.engine.MassAirFlowCommand;
import com.github.pires.obd.commands.engine.OilTempCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.RuntimeCommand;
import com.github.pires.obd.commands.engine.ThrottlePositionCommand;
import com.github.pires.obd.commands.fuel.AirFuelRatioCommand;
import com.github.pires.obd.commands.fuel.ConsumptionRateCommand;
import com.github.pires.obd.commands.fuel.FindFuelTypeCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.fuel.FuelTrimCommand;
import com.github.pires.obd.commands.fuel.WidebandAirFuelRatioCommand;
import com.github.pires.obd.commands.pressure.BarometricPressureCommand;
import com.github.pires.obd.commands.pressure.FuelPressureCommand;
import com.github.pires.obd.commands.pressure.FuelRailPressureCommand;
import com.github.pires.obd.commands.pressure.IntakeManifoldPressureCommand;
import com.github.pires.obd.commands.protocol.AdaptiveTimingCommand;
import com.github.pires.obd.commands.protocol.AvailablePidsCommand_01_20;
import com.github.pires.obd.commands.protocol.AvailablePidsCommand_21_40;
import com.github.pires.obd.commands.protocol.AvailablePidsCommand_41_60;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.HeadersOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ObdResetCommand;
import com.github.pires.obd.commands.protocol.ObdWarmstartCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.SpacesOffCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.AirIntakeTemperatureCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.FuelTrim;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.exceptions.UnsupportedCommandException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.example.obdandroid.config.Constant.IMPERIAL_UNITS_KEY;
import static com.example.obdandroid.config.Constant.PROTOCOLS_LIST_KEY;

/**
 * 作者：Jealous
 * 日期：2021/1/6
 * 描述：
 */
public class ODBUtils {

    //类初始化时，不初始化这个对象(延时加载，真正用的时候再创建)
    private static ODBUtils instance;
    private static SPUtil spUtil;

    //构造器私有化
    private ODBUtils(Context context) {
        spUtil = new SPUtil(context);
    }

    //方法同步，调用效率低
    public static synchronized ODBUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ODBUtils(context);
        }
        return instance;
    }

    /**
     * 启动并配置到OBD接口的连接。
     *
     * @param socket   蓝牙
     * @param callBack 回调
     */
    public void startObdConnection(BluetoothSocket socket, CommandCallBack callBack) {
        for (ObdCommand Command : ObdConfig.getCommands(spUtil.getString(PROTOCOLS_LIST_KEY, "AUTO"))) {
            Command.useImperialUnits(spUtil.getBoolean(IMPERIAL_UNITS_KEY, false));
            if (socket.isConnected()) {
                new Thread(() -> executeQueue(Command,socket,callBack)).start();
            }
        }
    }


    private static void executeQueue(ObdCommand Command, BluetoothSocket bluetoothSocket, CommandCallBack callBack) {
        try {
            Log.e(TAG.TAG_Fragemnt, "命令:" + Command.getName());
            Command.run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
            Log.e(TAG.TAG_Fragemnt, "命令:" + Command.getName());
            callBack.upDateState(Command);
            Log.e(TAG.TAG_Fragemnt, "命令:" + Command.getName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG.TAG_Fragemnt, "执行错误");
        }
    }

    public interface CommandCallBack {
        void upDateState(ObdCommand Command);
    }

}