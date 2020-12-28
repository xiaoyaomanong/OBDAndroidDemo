package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2020/12/28 0028
 * 描述：
 */
public class BluetoothEntity {
    private String BluetoothDeviceName;
    private String State;

    public String getBluetoothDeviceName() {
        return BluetoothDeviceName;
    }

    public void setBluetoothDeviceName(String bluetoothDeviceName) {
        BluetoothDeviceName = bluetoothDeviceName;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}