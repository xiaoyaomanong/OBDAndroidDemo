package com.example.obdandroid.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.config.TAG;
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
        private static final BltManagerUtils bltManager = new BltManagerUtils();
    }

    public static BltManagerUtils getInstance() {
        return BltManagerUtils.BluetoothManagers.bltManager;
    }


    /**
     * @param device   蓝牙设备
     * @param listener 回调接口
     *                 蓝牙连接
     */
    private void connect(BluetoothDevice device, ConnBluetoothSocketListener listener) {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(Constant.SPP_UUID);
            if (mSocket != null && !mSocket.isConnected()) {
                mSocket.connect();
            }
            MainApplication.setBluetoothSocket(mSocket);
        } catch (IOException connException) {
            try {
                Log.e(TAG.TAG_Fragemnt, "恢复时：无法连接", connException);
                mSocket.close();
            } catch (IOException exception) {
                Log.e(TAG.TAG_Fragemnt, "恢复时：连接失败时无法关闭套接字", exception);
            }
        }
        if (mSocket.isConnected()) {
            listener.connectMsg(0, "连接成功");
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
            if (btDev.createBond()) {
                connect(btDev, listener);
            } else {
                listener.connectMsg(2, "配对失败");
            }
        } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
            //如果这个设备已经配对完成，则尝试连接
            connect(btDev, listener);
        }
    }

    public interface ConnBluetoothSocketListener {
        void connectMsg(int code, String msg);
    }
}