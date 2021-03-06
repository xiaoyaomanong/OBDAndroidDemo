package com.example.obdandroid.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.example.obdandroid.MainApplication;
import com.example.obdandroid.config.Constant;
import com.example.obdandroid.config.TAG;
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
public class BluetoothManager {

    private static String methodName = "createInsecureRfcommSocket";
    private static BluetoothSocket mSocket;
    /**
     * 蓝牙适配器
     * BluetoothAdapter是Android系统中所有蓝牙操作都需要的，
     * 它对应本地Android设备的蓝牙模块，
     * 在整个系统中BluetoothAdapter是单例的。
     * 当你获取到它的实例之后，就能进行相关的蓝牙操作了。
     */
    private BluetoothAdapter mBluetoothAdapter;
    /**
     * 设置成单例模式
     */
    private BluetoothManager() {
    }

    private static class BluetoothManagers {
        private static BluetoothManager bltManager = new BluetoothManager();
    }

    public static BluetoothManager getInstance() {
        return BluetoothManager.BluetoothManagers.bltManager;
    }

    public static BluetoothSocket getmSocket() {
        return mSocket;
    }

    /**
     * 在使用蓝牙BLE之前，需要确认Android设备是否支持BLE feature(required为false时)，
     * 另外要需要确认蓝牙是否打开。如果发现不支持BLE，则不能使用BLE相关的功能。
     * 如果支持BLE，但是蓝牙没打开，则需要打开蓝牙。
     * api 18以上
     *
     * @param context
     */
   /* public void initBltManager(Context context) {
        if (bluetoothManager != null) return;
        //首先获取BluetoothManager
        bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        //获取BluetoothAdapter
        if (bluetoothManager != null)
            mBluetoothAdapter = bluetoothManager.getAdapter();
    }*/

    /**
     * 实例化远程设备的BluetoothSocket并将其连接。
     * <p/>
     * See http://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3/18786701#18786701
     *
     * @param device 要连接的远程设备
     * @return The BluetoothSocket
     * @throws IOException
     */
    private void connect(BluetoothDevice device, ConnBluetoothSocketListener listener) throws IOException {
        mSocket = device.createRfcommSocketToServiceRecord(Constant.SPP_UUID);
        if (mSocket != null)
            //全局只有一个bluetooth，所以我们可以将这个socket对象保存在appliaction中
            MainApplication.setBluetoothSocket(mSocket);
        //通过反射得到bltSocket对象，与uuid进行连接得到的结果一样，但这里不提倡用反射的方法
        //mBluetoothSocket = (BluetoothSocket) btDev.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(btDev, 1);
        Log.d("blueTooth", "开始连接...");
        //在建立之前调用
      /*  if (getmSocket().isDiscovering())
            //停止搜索
            getmSocket().cancelDiscovery();*/
        //如果当前socket处于非连接状态则调用连接
        if (!getmSocket().isConnected()) {
            //你应当确保在调用connect()时设备没有执行搜索设备的操作。
            // 如果搜索设备也在同时进行，那么将会显著地降低连接速率，并很大程度上会连接失败。
            getmSocket().connect();
        }
        try {
            Thread.sleep(500);
            mSocket.connect();
        } catch (Exception e) {
            Log.e("blueTooth", "...链接失败");
            try {
                getmSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        if (getmSocket().isConnected()) {
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


    public interface ConnBluetoothSocketListener {
        void connectMsg(int code, String msg);
    }
}