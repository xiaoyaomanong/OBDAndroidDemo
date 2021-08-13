package com.example.obdandroid.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.config.Constant;
import com.github.pires.obd.commands.protocol.HeadersOffCommand;
import com.github.pires.obd.commands.protocol.ObdWarmstartCommand;
import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.obdCommand.protocol.EchoOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.LineFeedOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SelectProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SpacesOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.TimeoutCommand;
import com.sohrab.obd.reader.utils.LogUtils;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 作者：Jealous
 * 日期：2021/3/3 0003
 * 描述：
 */
public class BltManagerUtils {

    private BluetoothSocket mSocket;

    /**
     * 设置成单例模式
     */
    private BltManagerUtils() {
    }

    private static class BluetoothManagers {
        private static BltManagerUtils bltManager = new BltManagerUtils();
    }

    public static BltManagerUtils getInstance() {
        return BltManagerUtils.BluetoothManagers.bltManager;
    }

    public BluetoothSocket getmSocket() {
        return mSocket;
    }


    /**
     * @param device   蓝牙设备
     * @param listener 回调接口
     *                 蓝牙连接
     */
    private void connect(BluetoothDevice device, ConnBluetoothSocketListener listener) {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(Constant.SPP_UUID);
            if (mSocket != null)
                MainApplication.setBluetoothSocket(mSocket);
            if (!mSocket.isConnected()) {
                mSocket.connect();
            }
        } catch (Exception e) {
            Log.e("blueTooth", "...链接失败");
            try {
                getmSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        if (mSocket.isConnected()) {
            listener.connectMsg(0, "连接成功");
            try {
                new ObdResetCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
               // new ObdWarmstartCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());//热启动OBD连接。。。
                new LineFeedOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                new SpacesOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                new HeadersOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                new TimeoutCommand(62).run(mSocket.getInputStream(), mSocket.getOutputStream());
                new SelectProtocolCommand(ObdProtocols.AUTO).run(mSocket.getInputStream(), mSocket.getOutputStream());
                //listener.connectMsg(0, "连接成功");
            } catch (Exception e) {
                LogUtils.i("在新线程中重置命令异常:: " + e.getMessage());
            }
        } else {
            listener.connectMsg(1, "连接失败");
        }
    }

    /**
     * 尝试配对和连接
     *
     * @param btDev    蓝牙设备
     * @param listener 回调接口
     */
    public void createBond(BluetoothDevice btDev, ConnBluetoothSocketListener listener) {
        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
            //如果这个设备取消了配对，则尝试配对
            btDev.createBond();
        } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
            //如果这个设备已经配对完成，则尝试连接
            connect(btDev, listener);
        }
    }


    public interface ConnBluetoothSocketListener {
        void connectMsg(int code, String msg);
    }
}