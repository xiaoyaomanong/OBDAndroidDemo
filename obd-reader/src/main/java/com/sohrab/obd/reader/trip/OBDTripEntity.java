package com.sohrab.obd.reader.trip;

import java.io.Serializable;

/**
 * 作者：Jealous
 * 日期：2021/1/19 0019
 * 描述：
 */
public class OBDTripEntity implements Serializable {

    /**
     * Name : 车速
     * value : 6km/h
     */

    private String name;
    private String value;

    public OBDTripEntity(String name, String value) {
        this.name = name;
        this.value = value;
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
}