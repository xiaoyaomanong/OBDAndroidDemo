package com.sohrab.obd.reader.trip;

/**
 * 作者：Jealous
 * 日期：2021/1/19 0019
 * 描述：
 */
public class OBDTripEntity {

    /**
     * Name : 车速
     * value : 6km/h
     */

    private String name;
    private String value;
    private int resId;

    public OBDTripEntity(String name, String value,int resId) {
        this.name = name;
        this.value = value;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}