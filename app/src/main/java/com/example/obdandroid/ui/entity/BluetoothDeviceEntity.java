package com.example.obdandroid.ui.entity;

import java.io.Serializable;

/**
 * 作者：Jealous
 * 日期：2021/1/12 0012
 * 描述：
 */
public class BluetoothDeviceEntity implements Serializable {
    private String blue_name;
    private String blue_address;

    public String getBlue_name() {
        return blue_name;
    }

    public void setBlue_name(String blue_name) {
        this.blue_name = blue_name;
    }

    public String getBlue_address() {
        return blue_address;
    }

    public void setBlue_address(String blue_address) {
        this.blue_address = blue_address;
    }
}