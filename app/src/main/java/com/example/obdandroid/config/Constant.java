package com.example.obdandroid.config;

import java.util.UUID;

/**
 * 文件描述：项目中使用到的一些常量
 * <p/>
 * Created by Mjj on 2016/8/12 0012.
 */
public class Constant {
    /*================================基本参数设置=========================================*/
    public static final String ACT_FLAG = "actFlag";
    public static final String INT_FLAG = "intFlag";
    public static final String SP_NAME = "shift";
    public static final String ISLOGIN = "islogin";
    /**
     * 蓝牙UUID
     */
    public static UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //启用蓝牙
    public static final int BLUE_TOOTH_OPEN = 1000;
    //禁用蓝牙
    public static final int BLUE_TOOTH_CLOSE = BLUE_TOOTH_OPEN + 1;
    //搜索蓝牙
    public static final int BLUE_TOOTH_SEARTH = BLUE_TOOTH_CLOSE + 1;
    //被搜索蓝牙
    public static final int BLUE_TOOTH_MY_SEARTH = BLUE_TOOTH_SEARTH + 1;
    //关闭蓝牙连接
    public static final int BLUE_TOOTH_CLEAR = BLUE_TOOTH_MY_SEARTH + 1;

    public static final String PROTOCOLS_LIST_KEY = "obd_protocols_preference";

}
