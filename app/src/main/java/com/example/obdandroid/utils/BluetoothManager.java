package com.example.obdandroid.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class BluetoothManager {

    private static final String TAG = BluetoothManager.class.getName();
    private static ConnBluetoothSocketListener listeners;
    private static ConnectThread connectThread;
    /*
     * http://developer.android.com/reference/android/bluetooth/BluetoothDevice.html
     * #createRfcommSocketToServiceRecord(java.util.UUID)
     * 提示：如果您要连接到蓝牙串行板，请尝试使用众所周知的SPP UUID 00001101-0000-1000-8000-00805F9B34FB。
     * 但是如果你正在连接到Android对等设备，然后请生成您自己的唯一UUID。”
     */
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * 实例化远程设备的BluetoothSocket并将其连接。
     * <p/>
     * See http://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3/18786701#18786701
     *
     * @param dev 要连接的远程设备
     * @return The BluetoothSocket
     * @throws IOException
     */
    public static BluetoothSocket connect(BluetoothDevice dev, ConnBluetoothSocketListener listener) throws IOException {
        BluetoothSocket sock = null;
        BluetoothSocket sockFallback = null;
        listeners = listener;
        android.util.Log.d(TAG, "正在启动蓝牙连接..");
        try {
            sock = dev.createRfcommSocketToServiceRecord(MY_UUID);
            //启动连接线程
            connectThread = new ConnectThread(sock, true, dev);
            connectThread.start();
            /*  sock.connect();*/
            listeners.connectMsg(1, "蓝牙连接成功");//连接成功
        } catch (Exception e1) {
            android.util.Log.e(TAG, "建立蓝牙连接时出错", e1);
            listeners.connectMsg(2, "建立蓝牙连接时出错");//建立蓝牙连接时出错
            assert sock != null;
            Class<?> clazz = sock.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
            try {
                Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                Object[] params = new Object[]{1};
                sockFallback = (BluetoothSocket) m.invoke(sock.getRemoteDevice(), params);
                sockFallback.connect();
                sock = sockFallback;
            } catch (Exception e2) {
                android.util.Log.e(TAG, "建立蓝牙连接时无法回退", e2);
                listeners.connectMsg(3, "建立蓝牙连接时无法回退");//连接成功
                throw new IOException(e2.getMessage());
            }
        }
        return sock;
    }

    public interface ConnBluetoothSocketListener {
        void connectMsg(int code, String msg);
    }

    /**
     * 连接线程
     */
    private static class ConnectThread extends Thread {

        private BluetoothSocket socket;
        private boolean activeConnect;
        private BluetoothDevice device;
        InputStream inputStream;
        OutputStream outputStream;

        private ConnectThread(BluetoothSocket socket, boolean connect, BluetoothDevice device) {
            this.socket = socket;
            this.activeConnect = connect;
            this.device = device;
        }

        @Override
        public void run() {
            //如果是自动连接 则调用连接方法
            if (activeConnect) {
                try {
                    socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //这里建立蓝牙连接 socket.connect() 这句话必须单开一个子线程
                //至于原因 暂时不知道为什么
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            socket.connect();
                            android.util.Log.e(TAG, "建立蓝牙连接成功");
                           /* text_state.post(new Runnable() {
                                @Override
                                public void run() {
                                    text_state.setText(getResources().getString(R.string.connect_success));
                                }
                            });*/
                            inputStream = socket.getInputStream();
                            outputStream = socket.getOutputStream();
                            byte[] buffer = new byte[2048];
                            int bytes;
                            while (true) {
                                //读取数据
                                bytes = inputStream.read(buffer);


                                if (bytes > 0) {
                                    final byte[] data = new byte[bytes];
                                    System.arraycopy(buffer, 0, data, 0, bytes);
                                   /* text_msg.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            text_msg.setText(getResources().getString(R.string.get_msg) + new String(data));
                                        }
                                    });*/
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

        }


    }
}