package com.example.obdandroid.ui.entity;

import android.bluetooth.BluetoothSocket;

import java.io.Serializable;

/**
 * 作者：Jealous
 * 日期：2021/3/3 0003
 * 描述：
 */
public class SocketEntity implements Serializable {
    private String name;
    private String address;
    private BluetoothSocket socket;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
    }
}