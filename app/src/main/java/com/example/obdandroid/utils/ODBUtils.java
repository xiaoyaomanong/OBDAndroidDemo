package com.example.obdandroid.utils;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.obdandroid.config.ObdConfig;
import com.example.obdandroid.config.TAG;
import com.example.obdandroid.service.ObdCommandJob;
import com.example.obdandroid.ui.activity.MainActivity;
import com.example.obdandroid.ui.fragment.HomeFragment;
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

import static com.example.obdandroid.config.Constant.DEVICE_ADDRESS;
import static com.example.obdandroid.config.Constant.DEVICE_NAME;
import static com.example.obdandroid.config.Constant.IMPERIAL_UNITS_KEY;
import static com.example.obdandroid.config.Constant.MESSAGE_DEVICE_NAME;
import static com.example.obdandroid.config.Constant.MESSAGE_UPDATE_OBD;
import static com.example.obdandroid.config.Constant.OBD_DATA;
import static com.example.obdandroid.config.Constant.PROTOCOLS_LIST_KEY;
import static com.example.obdandroid.config.TAG.TAG_Activity;

/**
 * 作者：Jealous
 * 日期：2021/1/6
 * 描述：
 */
public class ODBUtils {

    //类初始化时，不初始化这个对象(延时加载，真正用的时候再创建)
    private static ODBUtils instance;
    private static SPUtil spUtil;
    private BluetoothSocket bluetoothSocket;
    protected BlockingQueue<ObdCommandJob> jobsQueue = new LinkedBlockingQueue<>();
    protected Long queueCounter = 0L;
    protected Activity ctx;
    private Handler mHandler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            executeQueue();
        }
    };

    //构造器私有化
    private ODBUtils(Context context, Activity activity, Handler handler) {
        this.ctx = activity;
        this.mHandler = handler;
        spUtil = new SPUtil(context);
    }

    //方法同步，调用效率低
    public static synchronized ODBUtils getInstance(Context context, Activity activity, Handler handler) {
        if (instance == null) {
            instance = new ODBUtils(context, activity, handler);
        }
        return instance;
    }

    /**
     * 启动并配置到OBD接口的连接。
     *
     * @param socket 蓝牙
     */
    public void startObdConnection(BluetoothSocket socket) {
        this.bluetoothSocket = socket;
        for (ObdCommand Command : ObdConfig.getCommands(spUtil.getString(PROTOCOLS_LIST_KEY, "AUTO"))) {
            Command.useImperialUnits(spUtil.getBoolean(IMPERIAL_UNITS_KEY, false));
            if (spUtil.getBoolean(Command.getName(), true))
                queueJob(new ObdCommandJob(Command));
        }
        queueCounter = 0L;
        startSendOBD();
    }

    public void startSendOBD() {
        new Thread(runnable).start();
    }

    /**
     * 该方法将作业添加到队列，同时将其ID设置为
     * 内部队列计数器。
     *
     * @param job the job to queue.
     */
    public void queueJob(ObdCommandJob job) {
        queueCounter++;
        Log.d(TAG_Activity, "Adding job[" + queueCounter + "] to queue..");
        // 这是执行英制单位选项的好地方
        job.getCommand().useImperialUnits(spUtil.getBoolean(IMPERIAL_UNITS_KEY, false));
        job.setId(queueCounter);
        try {
            jobsQueue.put(job);
            Log.d(TAG_Activity, "作业成功排队。");
        } catch (InterruptedException e) {
            job.setState(ObdCommandJob.ObdCommandJobState.QUEUE_ERROR);
            Log.e(TAG_Activity, "无法将作业排队");
        }
    }

    /**
     * 运行队列，直到服务停止
     */
    protected void executeQueue() {
        Log.d(TAG_Activity, "Executing queue..");
        while (!Thread.currentThread().isInterrupted()) {
            ObdCommandJob job = null;
            try {
                job = jobsQueue.take();
                //日志作业
                Log.d(TAG_Activity, "Taking job[" + job.getId() + "] from queue..");

                if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NEW)) {
                    Log.d(TAG_Activity, "作业状态为新。 运行..");
                    job.setState(ObdCommandJob.ObdCommandJobState.RUNNING);
                    if (bluetoothSocket.isConnected()) {
                        job.getCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                    } else {
                        job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                        Log.e(TAG_Activity, "Can't run command on a closed socket.");
                    }
                } else
                    // 记录不是新工作
                    Log.e(TAG_Activity, "作业状态不是新的，因此它不应该在队列中。 错误提示！");
            } catch (InterruptedException i) {
                Thread.currentThread().interrupt();
            } catch (UnsupportedCommandException u) {
                if (job != null) {
                    job.setState(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED);
                }
                Log.d(TAG_Activity, "命令不受支持. -> " + u.getMessage());
            } catch (IOException io) {
                if (job != null) {
                    if (io.getMessage().contains("Broken pipe"))
                        job.setState(ObdCommandJob.ObdCommandJobState.BROKEN_PIPE);
                    else
                        job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                }
                Log.e(TAG_Activity, "IO错误. -> " + io.getMessage());
            } catch (Exception e) {
                if (job != null) {
                    job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                }
                Log.e(TAG_Activity, "运行命令失败. -> " + e.getMessage());
            }

            if (job != null) {
                final ObdCommandJob job2 = job;
                ctx.runOnUiThread(() -> {
                    Message msg = mHandler.obtainMessage(MESSAGE_UPDATE_OBD);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(OBD_DATA, job2);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                });
            }
        }
    }
}