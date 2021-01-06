package com.example.obdandroid.utils;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.example.obdandroid.config.ObdConfig;
import com.example.obdandroid.config.TAG;
import com.example.obdandroid.service.ObdCommandJob;
import com.example.obdandroid.ui.activity.MainActivity;
import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ObdResetCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.exceptions.UnsupportedCommandException;

import java.io.IOException;
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
    private SPUtil spUtil;

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

    private Long queueCounter = 0L;
    private BlockingQueue<ObdCommandJob> jobsQueue = new LinkedBlockingQueue<>();

    /**
     * 启动并配置到OBD接口的连接。
     *
     * @param socket   蓝牙
     * @param protocol OBD协议
     * @param callBack 回调
     */
    public void startObdConnection(BluetoothSocket socket,  CommandCallBack callBack) {
        Log.d(TAG.TAG_Activity, "正在启动OBD连接。");
        // 让我们配置连接。
        Log.d(TAG.TAG_Activity, "为连接配置排队作业。");
        new Thread(new Runnable() {
            @Override
            public void run() {

                queueCommands();
            }
        }).start();
        queueCounter = 0L;
        Log.d(TAG.TAG_Activity, "初始化作业已排队。");
        executeQueue(socket, callBack);
    }

    /**
     * 队列命令
     */
    private void queueCommands() {
        for (ObdCommand Command : ObdConfig.getCommands(spUtil.getString(PROTOCOLS_LIST_KEY, "AUTO"))) {
            if (spUtil.getBoolean(Command.getName(), true))
                queueJob(new ObdCommandJob(Command));
        }
    }

    /**
     * 该方法将作业添加到队列，同时将其ID设置为
     * 内部队列计数器。
     *
     * @param job the job to queue.
     */
    public void queueJob(ObdCommandJob job) {
        queueCounter++;
        Log.d(TAG.TAG_Activity, "Adding job[" + queueCounter + "] to queue..");
        job.setId(queueCounter);
        try {
            jobsQueue.put(job);
            Log.d(TAG.TAG_Activity, "作业成功排队。");
        } catch (InterruptedException e) {
            job.setState(ObdCommandJob.ObdCommandJobState.QUEUE_ERROR);
            Log.e(TAG.TAG_Activity, "无法将作业排队");
        }
    }


    /**
     * 运行队列，直到服务停止
     */
    protected void executeQueue(BluetoothSocket bluetoothSocket, CommandCallBack callBack) {
        Log.d(TAG.TAG_Activity, "Executing queue..");
        while (!Thread.currentThread().isInterrupted()) {
            ObdCommandJob job = null;
            try {
                job = jobsQueue.take();
                //日志作业
                Log.d(TAG.TAG_Activity, "Taking job[" + job.getId() + "] from queue..");

                if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NEW)) {
                    Log.d(TAG.TAG_Activity, "作业状态为新。 运行..");
                    job.setState(ObdCommandJob.ObdCommandJobState.RUNNING);
                    if (bluetoothSocket.isConnected()) {
                        job.getCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                    } else {
                        job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                        Log.e(TAG.TAG_Activity, "无法在封闭的套接字上运行命令.");
                    }
                } else
                    // 记录不是新工作
                    Log.e(TAG.TAG_Activity, "作业状态不是新的，因此它不应该在队列中。 错误提示！");
            } catch (InterruptedException i) {
                Thread.currentThread().interrupt();
            } catch (UnsupportedCommandException u) {
                if (job != null) {
                    job.setState(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED);
                }
                Log.d(TAG.TAG_Activity, "命令不受支持. -> " + u.getMessage());
            } catch (IOException io) {
                if (job != null) {
                    if (io.getMessage().contains("Broken pipe"))
                        job.setState(ObdCommandJob.ObdCommandJobState.BROKEN_PIPE);
                    else
                        job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                }
                Log.e(TAG.TAG_Activity, "IO错误. -> " + io.getMessage());
            } catch (Exception e) {
                if (job != null) {
                    job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                }
                Log.e(TAG.TAG_Activity, "运行命令失败. -> " + e.getMessage());
            }

            if (job != null) {
                final ObdCommandJob job2 = job;
                callBack.upDateState(job2);
            }
        }
    }

    public interface CommandCallBack {
        void upDateState(ObdCommandJob job);
    }
}