package com.example.obdandroid.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.util.Log;

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
     * 实例化远程设备的BluetoothSocket并将其连接。
     * <p/>
     * See http://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3/18786701#18786701
     *
     * @param device 要连接的远程设备
     * @return The BluetoothSocket
     * @throws IOException
     */
    public static BluetoothSocket connect(BluetoothDevice device, ConnBluetoothSocketListener listener) throws IOException {
        try {
            mSocket = (BluetoothSocket) device.getClass().getMethod(methodName, new Class[]{int.class}).invoke(device, 1);
        } catch (Exception e) {
            Log.e(TAG.TAG_Activity, "createInsecureRfcommSocket failed");
        }
        try {
            Thread.sleep(200);
            mSocket.connect();
        } catch (Exception e) {
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
        return mSocket;
    }

    public interface ConnBluetoothSocketListener {
        void connectMsg(int code, String msg);
    }
}