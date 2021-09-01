package com.example.obdandroid.ui.entity;

import java.io.Serializable;

/**
 * 作者：Jealous
 * 日期：2021/2/7 0007
 * 描述：
 */
public class MessageCheckEntity implements Serializable {
    private String createTime;//创建时间
    private String content; //检测内容
    private String details; //检测记录id
    private String VehicleName;//车辆品牌名称
    private int platformType;// 1:android   2:IOS

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public String getVehicleName() {
        return VehicleName;
    }

    public void setVehicleName(String vehicleName) {
        VehicleName = vehicleName;
    }
}