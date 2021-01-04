package com.example.obdandroid.service;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.obdandroid.R;
import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.activity.MainActivity;
import com.example.obdandroid.utils.SPUtil;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ObdResetCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.exceptions.UnsupportedCommandException;

import java.io.File;
import java.io.IOException;

import static com.example.obdandroid.config.Constant.IMPERIAL_UNITS_KEY;
import static com.example.obdandroid.config.Constant.PROTOCOLS_LIST_KEY;

/**
 * 该服务主要负责建立和维护应用程序运行所在的设备与其他设备之间的永久连接OBD蓝牙接口。
 * <p/>
 * 其次，它将用作ObdCommandJobs的存储库时间应用程序状态机。
 */
public class ObdGatewayService extends AbstractGatewayService {

    private static final String TAG = BaseActivity.class.getName();
    private Handler mHandler = null;
    private BluetoothSocket bluetoothSocket;
    private SPUtil spUtil;

    public void startService(String remoteDevice, Handler handler, BluetoothSocket socket) throws IOException {
        Log.d(TAG, "正在启动服务..");
        mHandler = handler;
        spUtil = new SPUtil(getApplicationContext());
        // 获取远程蓝牙设备
        if (remoteDevice == null || "".equals(remoteDevice)) {
            Toast.makeText(ctx, getString(R.string.text_bluetooth_nodevice), Toast.LENGTH_LONG).show();
            stopService();
            throw new IOException();
        } else {
            showNotification(getString(R.string.notification_action), getString(R.string.service_starting), R.drawable.logo, true, true, false);
            try {
                startObdConnection(socket);
            } catch (Exception e) {
                Log.e(TAG, "建立连接时出错。 -> " + e.getMessage());
                //万一发生故障，请停止此服务。
                stopService();
                throw new IOException();
            }
            showNotification(getString(R.string.notification_action), getString(R.string.service_started), R.drawable.logo, true, true, false);
        }
    }

    /**
     * 启动并配置到OBD接口的连接。
     * <p/>
     * See http://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3/18786701#18786701
     *
     * @throws IOException 异常
     */
    private void startObdConnection(BluetoothSocket socket) throws IOException {
        Log.d(TAG, "正在启动OBD连接。");
        isRunning = true;
        bluetoothSocket = socket;
        // 让我们配置连接。
        Log.d(TAG, "为连接配置排队作业。");
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
        final String protocol = spUtil.getString(PROTOCOLS_LIST_KEY, "AUTO");
        queueJob(new ObdCommandJob(new SelectProtocolCommand(ObdProtocols.valueOf(protocol))));
        //返回伪数据的作业
        queueJob(new ObdCommandJob(new AmbientAirTemperatureCommand()));
        queueCounter = 0L;
        Log.d(TAG, "初始化作业已排队。");
    }

    /**
     * 该方法将作业添加到队列，同时将其ID设置为内部队列计数器。
     *
     * @param job 要排队的工作。
     */
    @Override
    public void queueJob(ObdCommandJob job) {
        // 这是执行英制单位选项的好地方
        job.getCommand().useImperialUnits(spUtil.getBoolean(IMPERIAL_UNITS_KEY, false));
        //现在我们可以通过
        super.queueJob(job);
    }

    /**
     * 运行队列，直到服务停止
     */
    protected void executeQueue() {
        Log.d(TAG, "Executing queue..");
        while (!Thread.currentThread().isInterrupted()) {
            ObdCommandJob job = null;
            try {
                job = jobsQueue.take();
                //日志作业
                Log.d(TAG, "Taking job[" + job.getId() + "] from queue..");

                if (job.getState().equals(ObdCommandJob.ObdCommandJobState.NEW)) {
                    Log.d(TAG, "作业状态为新。 运行..");
                    job.setState(ObdCommandJob.ObdCommandJobState.RUNNING);
                    if (bluetoothSocket.isConnected()) {
                        job.getCommand().run(bluetoothSocket.getInputStream(), bluetoothSocket.getOutputStream());
                    } else {
                        job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                        Log.e(TAG, "无法在封闭的套接字上运行命令.");
                    }
                } else
                    // 记录不是新工作
                    Log.e(TAG, "作业状态不是新的，因此它不应该在队列中。 错误提示！");
            } catch (InterruptedException i) {
                Thread.currentThread().interrupt();
            } catch (UnsupportedCommandException u) {
                if (job != null) {
                    job.setState(ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED);
                }
                Log.d(TAG, "命令不受支持. -> " + u.getMessage());
            } catch (IOException io) {
                if (job != null) {
                    if (io.getMessage().contains("Broken pipe"))
                        job.setState(ObdCommandJob.ObdCommandJobState.BROKEN_PIPE);
                    else
                        job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                }
                Log.e(TAG, "IO错误. -> " + io.getMessage());
            } catch (Exception e) {
                if (job != null) {
                    job.setState(ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR);
                }
                Log.e(TAG, "运行命令失败. -> " + e.getMessage());
            }

            if (job != null) {
                final ObdCommandJob job2 = job;
                ((MainActivity) ctx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //((MainActivity) ctx).stateUpdate(job2);
                    }
                });
            }
        }
    }

    /**
     * 停止OBD连接和队列处理。
     */
    public void stopService() {
        Log.d(TAG, "正在停止服务");
        notificationManager.cancel(NOTIFICATION_ID);
        jobsQueue.clear();
        isRunning = false;

        if (bluetoothSocket != null)
            // 关闭socket
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        // 杀死服务
        stopSelf();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public static void saveLogcatToFile(Context context, String devemail) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{devemail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "OBD2 Reader Debug Logs");
        StringBuilder sb = new StringBuilder();
        sb.append("\nManufacturer: ").append(Build.MANUFACTURER);
        sb.append("\nModel: ").append(Build.MODEL);
        sb.append("\nRelease: ").append(Build.VERSION.RELEASE);
        emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        String fileName = "OBDReader_logcat_" + System.currentTimeMillis() + ".txt";
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + File.separator + "OBD2Logs");
        if (dir.mkdirs()) {
            File outputFile = new File(dir, fileName);
            Uri uri = Uri.fromFile(outputFile);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

            Log.d("savingFile", "Going to save logcat to " + outputFile);
            //emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(emailIntent, "Pick an Email provider").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            try {
                @SuppressWarnings("unused")
                Process process = Runtime.getRuntime().exec("logcat -f " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
