package com.example.obdandroid.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.config.Constant;
import com.sohrab.obd.reader.enums.ObdProtocols;
import com.sohrab.obd.reader.obdCommand.protocol.EchoOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.LineFeedOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.ObdResetCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SelectProtocolCommand;
import com.sohrab.obd.reader.obdCommand.protocol.SpacesOffCommand;
import com.sohrab.obd.reader.obdCommand.protocol.TimeoutCommand;
import com.sohrab.obd.reader.utils.LogUtils;

import java.io.IOException;

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
     * 实例化远程设备的BluetoothSocket并将其连接。
     * <p/>
     * See http://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3/18786701#18786701
     *
     * @param device 要连接的远程设备
     * @return The BluetoothSocket
     * @throws IOException
     */
    private void connect(BluetoothDevice device, ConnBluetoothSocketListener listener) {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(Constant.SPP_UUID);
            if (mSocket != null)
                //全局只有一个bluetooth，所以我们可以将这个socket对象保存在appliaction中
                MainApplication.setBluetoothSocket(mSocket);
            //通过反射得到bltSocket对象，与uuid进行连接得到的结果一样，但这里不提倡用反射的方法
            //mBluetoothSocket = (BluetoothSocket) btDev.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(btDev, 1);
            Log.d("blueTooth", "开始连接...");
            //如果当前socket处于非连接状态则调用连接
            if (!mSocket.isConnected()) {
                //你应当确保在调用connect()时设备没有执行搜索设备的操作。
                // 如果搜索设备也在同时进行，那么将会显著地降低连接速率，并很大程度上会连接失败。
                try {
                    Thread.sleep(200);
                    mSocket.connect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            try {
                // 此线程是必需的，因为在Headunit中命令.run方法无限块，因此，线程的最长寿命为15秒，这样就可以处理块了。
                new ObdResetCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                new LineFeedOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                new SpacesOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
                new TimeoutCommand(125).run(mSocket.getInputStream(), mSocket.getOutputStream());
                new SelectProtocolCommand(ObdProtocols.AUTO).run(mSocket.getInputStream(), mSocket.getOutputStream());
                new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
            } catch (Exception e) {
                LogUtils.i("在新线程中重置命令异常:: " + e.getMessage());
            }
            listener.connectMsg(0, "连接成功");
        } else {
            listener.connectMsg(1, "连接失败");
        }
        // return mSocket;
    }

    /**
     * 尝试配对和连接
     *
     * @param btDev
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