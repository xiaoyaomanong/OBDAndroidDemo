package com.example.obdandroid.ui.entity;

import android.bluetooth.BluetoothDevice;

/**
 * 作者：Jealous
 * 日期：2020/12/28 0028
 * 描述：
 */
public class BluRxBean {

    public int id;
    public BluetoothDevice bluetoothDevice;
    private String state;

    public BluRxBean(int id, BluetoothDevice bluetoothDevice) {
        this.id = id;
        this.bluetoothDevice = bluetoothDevice;
    }

    public BluRxBean(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}