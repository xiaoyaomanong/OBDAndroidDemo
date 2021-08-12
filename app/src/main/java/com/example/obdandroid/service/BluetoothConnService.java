package com.example.obdandroid.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;

import com.example.obdandroid.utils.BltManagerUtils;

/**
 * 作者：Jealous
 * 日期：2021/8/12 0012
 * 描述：
 */
public class BluetoothConnService extends Service {
    private final Intent intent = new Intent("com.example.obd.RECEIVER");
    public static final String ACTION = "com.example.obd.MSG_ACTION";

    /**
     * 连接蓝牙
     */
    public void startConn(String address) {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
      /*  new Thread(() ->
                BltManagerUtils.getInstance().createBond(device, (code, msg) -> {
                    intent.putExtra("code", code);
                    sendBroadcast(intent);
                })).start();*/
        BltManagerUtils.getInstance().createBond(device, (code, msg) -> {
            intent.putExtra("code", code);
            sendBroadcast(intent);
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startConn(intent.getStringExtra("address"));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}