package com.example.obdandroid.utils;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.obdandroid.config.TAG;
import com.example.obdandroid.service.ObdCommandJob;
import com.example.obdandroid.ui.activity.MainActivity;
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

    protected Long queueCounter = 0L;
    protected BlockingQueue<ObdCommandJob> jobsQueue = new LinkedBlockingQueue<>();
    /**
     * 启动并配置到OBD接口的连接。
     * @throws IOException 异常
     */
    private void startObdConnection(BluetoothSocket socket,String protocol) throws IOException {
        Log.d(TAG.TAG_Activity, "正在启动OBD连接。");
        // 让我们配置连接。
        Log.d(TAG.TAG_Activity, "为连接配置排队作业。");
        queueJob(new ObdCommandJob(new ObdResetCommand()));

        //下面是在发送命令之前给适配器足够的时间来重置，否则可以忽略第一个启动命令。
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        queueJob(new ObdCommandJob(new EchoOffCommand()));

        /*
         * 将根据测试结果第二次发送。
         *
         * TODO 这可以通过仅发出而无需排队作业来完成
         * command.run(), command.getResult() and validate the result.
         */
        queueJob(new ObdCommandJob(new EchoOffCommand()));
        queueJob(new ObdCommandJob(new LineFeedOffCommand()));
        queueJob(new ObdCommandJob(new TimeoutCommand(62)));
        // 从首选项获取协议
       // final String protocol = spUtil.getString(PROTOCOLS_LIST_KEY, "AUTO");
        queueJob(new ObdCommandJob(new SelectProtocolCommand(ObdProtocols.valueOf(protocol))));
        //返回伪数据的作业
        queueJob(new ObdCommandJob(new AmbientAirTemperatureCommand()));
        queueCounter = 0L;
        Log.d(TAG.TAG_Activity, "初始化作业已排队。");
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
    protected void executeQueue(BluetoothSocket bluetoothSocket) {
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
               /* ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // HomeFragment.stateUpdate(job2);
                    }
                });*/
            }
        }
    }

}