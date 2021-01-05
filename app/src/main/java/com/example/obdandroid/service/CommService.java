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
 *
 */

package com.example.obdandroid.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.obdandroid.R;
import com.example.obdandroid.listener.SocketCallBack;

import java.util.logging.Logger;

import static com.example.obdandroid.config.Constant.DEVICE_ADDRESS;
import static com.example.obdandroid.config.Constant.DEVICE_NAME;
import static com.example.obdandroid.config.Constant.MESSAGE_DEVICE_NAME;
import static com.example.obdandroid.config.Constant.MESSAGE_STATE_CHANGE;
import static com.example.obdandroid.config.Constant.MESSAGE_TOAST;
import static com.example.obdandroid.config.Constant.TOAST;

/**
 * 抽象通信服务
 */
public abstract class CommService {

    /**
     * 通讯媒体
     */
    public enum MEDIUM {
        BLUETOOTH    //<蓝牙装置
    }

    /**
     * 媒体类型选择
     */
    public static MEDIUM medium = MEDIUM.BLUETOOTH;

    /**
     * 指示当前连接状态的常量
     */
    public enum STATE {
        NONE,           ///< 我们什么也没做
        LISTEN,         ///< 监听传入的连接
        CONNECTING,     ///< 启动传出连接
        CONNECTED,      ///< 连接到远程设备
        OFFLINE         ///< 我们离线
    }

    private static final String TAG = "CommService";

    static final Logger log = Logger.getLogger(TAG);

    Context mContext;
    private Handler mHandler = null;
    STATE mState;

    /**
     * 构造函数。 准备一个新的通信会话。
     */
    CommService() {
        super();
        mState = STATE.NONE;
    }

    /**
     * 构造函数。 准备一个新的通信会话。
     *
     * @param handler 将消息发送回UI活动的处理程序
     */
    private CommService(Handler handler) {
        this();
        mHandler = handler;
    }

    /**
     * 构造函数。 准备一个新的通信会话。
     *
     * @param context UI活动上下文
     * @param handler 将消息发送回UI活动的处理程序
     */
    CommService(Context context, Handler handler) {
        this(handler);
        mContext = context;
    }

    /**
     * 设置聊天连接的当前状态
     *
     * @param state 定义当前连接状态的整数
     */
    synchronized void setState(STATE state) {
        mState = state;
        // 将新状态赋予处理程序，以便UI活动可以更新
        mHandler.obtainMessage(MESSAGE_STATE_CHANGE, state).sendToTarget();
    }

    /**
     * 启动聊天服务。 专门启动AcceptThread以开始会话
     * 在侦听（服务器）模式下。 由活动onResume（）调用
     */
    protected abstract void start();

    /**
     * 停止所有线程
     */
    public abstract void stop();

    /**
     * 以非同步方式写入输出设备
     *
     * @param out The bytes to write
     */
    public abstract void write(byte[] out);

    /**
     * 开始连接到指定设备
     *
     * @param device 要连接的设备
     * @param secure 套接字安全性类型-安全（true），不安全（false）
     */
    public abstract void connect(Object device, boolean secure, SocketCallBack callBack);


    /**
     * 指示连接尝试失败，并通知UI活动。
     */
    void connectionFailed() {
        stop();
        // 离线设置新状态
        setState(STATE.OFFLINE);
        // 将失败消息发送回活动
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, mContext.getString(R.string.unabletoconnect));
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * 指示连接丢失，并通知UI活动。
     */
    void connectionLost() {
        stop();
        // 离线设置新状态
        setState(STATE.OFFLINE);
        //将失败消息发送回活动
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, mContext.getString(R.string.connectionlost));
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * 指示已建立连接，并通知UI活动。
     */
    void connectionEstablished(String deviceName, String deviceAddress) {
        // 将建立连接的设备的名称发送回UI活动
        Message msg = mHandler.obtainMessage(MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_NAME, deviceName);
        bundle.putString(DEVICE_ADDRESS, deviceAddress);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // 将状态设置为已连接
        setState(STATE.CONNECTED);
    }
}
