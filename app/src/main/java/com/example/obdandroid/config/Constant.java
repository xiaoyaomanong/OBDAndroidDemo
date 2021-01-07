package com.example.obdandroid.config;

import java.util.UUID;

import okhttp3.MediaType;

/**
 * 文件描述：项目中使用到的一些常量
 * <p/>
 * Created by Mjj on 2016/8/12 0012.
 */
public class Constant {
    /*================================基本参数设置=========================================*/
    public static final String ACT_FLAG = "actFlag";
    public static final String INT_FLAG = "intFlag";
    public static final String SP_NAME = "OBD_shift";
    public static final String IS_LOGIN = "isLogin";
    public static final String PLATFORM = "Android";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String TOKEN = "token";
    public static final String USER_ID = "userId";
    public static final String EXPIRE_TIME = "expireTime";
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


    /**
     * 从蓝牙聊天服务处理程序发送的消息类型
     */
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int REQUEST_ENABLE_BT = 3;
    public static final int MESSAGE_UPDATE_VIEW = 7;
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    /**
     * 显示更新之间的间隔时间，以表示数据更改
     */
    public static final int DISPLAY_UPDATE_TIME = 250;
    /**
     * 首选项的键名
     */
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String DEVICE_SOCKET = "device_socket";
    public static final String TOAST = "toast";

    public static final String OBD_UPDATE_PERIOD_KEY = "obd_update_period_preference";
    public static final String GPS_UPDATE_PERIOD_KEY = "gps_update_period_preference";
    public static final String GPS_DISTANCE_PERIOD_KEY = "gps_distance_period_preference";
    public static final String IMPERIAL_UNITS_KEY = "imperial_units_preference";
    public static final String ENABLE_GPS_KEY = "enable_gps_preference";
    public static final String CONNECT_BT_KEY = "connect_bt_preference";
    public static final String BT_NAME_KEY = "bt_name_preference";
    public static final String BT_ADDRESS_KEY = "bt_address_preference";

}
