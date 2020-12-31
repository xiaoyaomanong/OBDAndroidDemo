/*
 * (C) Copyright 2015 by fr3ts0n <erwin.scheuch-heilig@gmx.at>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */

package com.example.obdandroid.service;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.ParcelUuid;


import com.example.obdandroid.utils.StreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Level;

/**
 * 该课程负责设置和管理蓝牙的所有工作
 * 与其他设备的连接。 它有一个监听inc的线程
 * 连接，用于与设备连接的线程以及用于
 * 连接时执行数据传输。
 */
@SuppressLint("NewApi")
public class BtCommService extends CommService {

    private BtConnectThread mBtConnectThread;
    private BtWorkerThread mBtWorkerThread;
    /**
     * 通信流处理程序
     */
    private final StreamHandler ser = new StreamHandler();


    /**
     * 构造函数。 准备一个新的蓝牙通信会话。
     *
     * @param context UI活动上下文
     * @param handler 将消息发送回UI活动的处理程序
     */
    public BtCommService(Context context, Handler handler) {
        super(context, handler);

        // 始终取消发现，因为它会减慢连接速度
        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        mAdapter.cancelDiscovery();
    }

    /**
     * 启动聊天服务。 专门启动AcceptThread以开始会话
     * 在侦听（服务器）模式下。 由活动onResume（）调用
     */
    @Override
    public synchronized void start() {
        log.log(Level.FINE, "start");
        // 取消任何试图建立连接的线程
        if (mBtConnectThread != null) {
            mBtConnectThread.cancel();
            mBtConnectThread = null;
        }
        // 取消当前正在运行连接的任何线程
        if (mBtWorkerThread != null) {
            mBtWorkerThread.cancel();
            mBtWorkerThread = null;
        }
        setState(STATE.LISTEN);
    }

    /**
     *开始连接到指定设备
     *
     * @param device 要连接的设备
     * @param secure 套接字安全性类型-安全（true），不安全（false）
     */
    @Override
    public synchronized void connect(Object device, boolean secure) {
        log.log(Level.FINE, "connect to: " + device);
        //取消任何试图建立连接的线程
        if (mState == STATE.CONNECTING) {
            if (mBtConnectThread != null) {
                mBtConnectThread.cancel();
                mBtConnectThread = null;
            }
        }

        // 取消当前正在运行连接的任何线程
        if (mBtWorkerThread != null) {
            mBtWorkerThread.cancel();
            mBtWorkerThread = null;
        }
        setState(STATE.CONNECTING);
        //启动线程以与给定的设备连接
        mBtConnectThread = new BtConnectThread((BluetoothDevice) device, secure);
        mBtConnectThread.start();
    }

    /**
     * 启动BtWorkerThread以开始管理蓝牙连接
     *
     * @param socket 建立连接的BluetoothSocket
     * @param device 已连接的BluetoothDevice
     */
    private synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        log.log(Level.FINE, "connected, Socket Type:" + socketType);

        // 取消完成连接的线程
        if (mBtConnectThread != null) {
            mBtConnectThread.cancel();
            mBtConnectThread = null;
        }

        //取消当前正在运行连接的任何线程
        if (mBtWorkerThread != null) {
            mBtWorkerThread.cancel();
            mBtWorkerThread = null;
        }
        // 启动线程以管理连接并执行传输
        mBtWorkerThread = new BtWorkerThread(socket, socketType);
        mBtWorkerThread.start();

        //我们已连接->已建立信号连接
        connectionEstablished(device.getName(),device.getAddress());
    }

    /**
     * 停止所有线程
     */
    @Override
    public synchronized void stop() {
        log.log(Level.FINE, "stop");
        if (mBtConnectThread != null) {
            mBtConnectThread.cancel();
            mBtConnectThread = null;
        }

        if (mBtWorkerThread != null) {
            mBtWorkerThread.cancel();
            mBtWorkerThread = null;
        }

        setState(STATE.OFFLINE);
    }

    /**
     * 以非同步方式写入BtWorkerThread
     *
     * @param out 要写入的字节
     * @see BtWorkerThread#write(byte[])
     */
    @Override
    public synchronized void write(byte[] out) {
        // 执行不同步的写入
        mBtWorkerThread.write(out);
    }

    /**
     * 该线程在尝试与
     * 设备。 它直通； 连接成功或失败。
     */
    private class BtConnectThread extends Thread {
        private final BluetoothDevice mmDevice;
        private BluetoothSocket mmSocket;
        private final String mSocketType;

        BtConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // 修改以与SPP设备一起使用
            final UUID SPP_UUID = UUID
                    .fromString("00001101-0000-1000-8000-00805F9B34FB");

            // 获取用于与给定BluetoothDevice连接的BluetoothSocket
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
                }
            } catch (IOException e) {
                log.log(Level.SEVERE, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;

            logSocketUuids(mmSocket, "BT socket");
        }

        /**
         * 记录指定的BluetoothSocket支持的UUID
         *
         * @param socket 套接字记录
         * @param msg   附加UUID的消息
         */
        private void logSocketUuids(BluetoothSocket socket, String msg) {
            if (log.isLoggable(Level.INFO)) {
                StringBuilder message = new StringBuilder(msg);
                // dump supported UUID's
                message.append(" - UUIDs:");
                ParcelUuid[] uuids = socket.getRemoteDevice().getUuids();
                if (uuids != null) {
                    for (ParcelUuid uuid : uuids) {
                        message.append(uuid.getUuid().toString()).append(",");
                    }
                } else {
                    message.append("NONE (Invalid BT implementation)");
                }
                log.log(Level.INFO, message.toString());
            }
        }

        public void run() {
            log.log(Level.INFO, "BEGIN mBtConnectThread SocketType:" + mSocketType);
            // 连接到BluetoothSocket
            try {
                log.log(Level.FINE, "Connect BT socket");
                // 这是一个阻塞的调用，只会在连接成功或出现异常时返回
                mmSocket.connect();
            } catch (IOException e) {
                log.log(Level.FINE, e.getMessage());
                cancel();
                log.log(Level.INFO, "后备尝试创建RfComm套接字");
                BluetoothSocket sockFallback;
                Class<?> clazz = mmSocket.getRemoteDevice().getClass();
                Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
                try {
                    Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                    Object[] params = new Object[]{1};
                    sockFallback = (BluetoothSocket) m.invoke(mmSocket.getRemoteDevice(), params);
                    mmSocket = sockFallback;
                    logSocketUuids(mmSocket, "Fallback socket");
                    // co连接后备插座
                    mmSocket.connect();
                } catch (Exception e2) {
                    log.log(Level.SEVERE, e2.getMessage());
                    connectionFailed();
                    return;
                }
            }

            // 重置BtConnectThread，因为我们完成了
            synchronized (BtCommService.this) {
                mBtConnectThread = null;
            }

            // 启动连接的线程
            connected(mmSocket, mmDevice, mSocketType);
        }

        synchronized void cancel() {
            try {
                log.log(Level.INFO, "Closing BT socket");
                mmSocket.close();
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    /**
     * 该线程在与远程设备连接期间运行。 它处理所有
     * 传入和传出传输。
     */
    private class BtWorkerThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        BtWorkerThread(BluetoothSocket socket, String socketType) {
            log.log(Level.FINE, "create BtWorkerThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // 获取BluetoothSocket输入和输出流
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                log.log(Level.SEVERE, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            // 设置流
            ser.setStreams(mmInStream, mmOutStream);
        }

        /**
         * 运行主通讯回路
         */
        public void run() {
            log.log(Level.INFO, "BEGIN mBtWorkerThread");
            try {
                // 运行通讯线程
                ser.run();
            } catch (Exception ex) {
                // 故意忽略
                log.log(Level.SEVERE, "Comm thread aborted", ex);
            }
            connectionLost();
        }

        /**
         * 写入连接的OutStream。
         *
         * @param buffer The bytes to write
         */
        synchronized void write(byte[] buffer) {
            ser.writeTelegram(new String(buffer).toCharArray());
        }

        synchronized void cancel() {
            try {
                log.log(Level.INFO, "Closing BT socket");
                mmSocket.close();
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        }

    }
}
