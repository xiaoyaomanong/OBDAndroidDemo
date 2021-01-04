package com.example.obdandroid.listener;

import android.bluetooth.BluetoothSocket;

/**
 * 作者：Jealous
 * 日期：2021/1/4 0004
 * 描述：
 */
public interface SocketCallBack {
    void connectMsg(String msg, BluetoothSocket socket);
}