package com.example.obdandroid.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.obdandroid.base.BaseActivity;
import com.example.obdandroid.ui.activity.MainActivity;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import roboguice.service.RoboService;

import static com.example.obdandroid.config.Constant.DEVICE_ADDRESS;
import static com.example.obdandroid.config.Constant.DEVICE_NAME;
import static com.example.obdandroid.config.Constant.MESSAGE_DEVICE_NAME;


public abstract class AbstractGatewayService extends Service {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = BaseActivity.class.getName();
    private final IBinder binder = new AbstractGatewayServiceBinder();
    @Inject
    protected NotificationManager notificationManager;
    protected Context ctx;
    protected boolean isRunning = false;
    protected Long queueCounter = 0L;
    protected BlockingQueue<ObdCommandJob> jobsQueue = new LinkedBlockingQueue<>();
    // 在另一个线程中运行executeQueue，以减轻UI线程
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                executeQueue();
            } catch (InterruptedException e) {
                t.interrupt();
            }
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "创建服务. .");
        t.start();
        Log.d(TAG, "服务创建。");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "销毁服务...");
        notificationManager.cancel(NOTIFICATION_ID);
        t.interrupt();
        Log.d(TAG, "服务遭到破坏。");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean queueEmpty() {
        return jobsQueue.isEmpty();
    }

    /**
     * 该方法将作业添加到队列，同时将其ID设置为
     * 内部队列计数器。
     *
     * @param job the job to queue.
     */
    public void queueJob(ObdCommandJob job) {
        queueCounter++;
        Log.d(TAG, "Adding job[" + queueCounter + "] to queue..");

        job.setId(queueCounter);
        try {
            jobsQueue.put(job);
            Log.d(TAG, "作业成功排队。");
        } catch (InterruptedException e) {
            job.setState(ObdCommandJob.ObdCommandJobState.QUEUE_ERROR);
            Log.e(TAG, "无法将作业排队");
        }
    }

   /* *//**
     * 在此服务运行时显示通知。
     *//*
    protected void showNotification(String contentTitle, String contentText, int icon, boolean ongoing, boolean notify, boolean vibrate) {
        final PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, MainActivity.class), 0);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx);
        notificationBuilder.setContentTitle(contentTitle)
                .setContentText(contentText).setSmallIcon(icon)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis());
        // 可以取消吗？
        if (ongoing) {
            notificationBuilder.setOngoing(true);
        } else {
            notificationBuilder.setAutoCancel(true);
        }
        if (vibrate) {
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        if (notify) {
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.getNotification());
        }
    }*/

    public void setContext(Context c) {
        ctx = c;
    }

    abstract protected void executeQueue() throws InterruptedException;

    abstract public void startService(String remoteDevice, Handler handler, BluetoothSocket socket) throws IOException;

    abstract public void stopService();

    public class AbstractGatewayServiceBinder extends Binder {
        public AbstractGatewayService getService() {
            return AbstractGatewayService.this;
        }
    }
}
