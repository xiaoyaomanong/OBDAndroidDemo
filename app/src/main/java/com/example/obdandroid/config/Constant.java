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
    public static final String IS_CHECK = "isCheckPwd";
    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String VEHICLE_ID = "vehicleId";
    public static final String WX_PAY_TYPE = "1";
    public static final String ALI_PAY_TYPE = "2";
    public static final String OBD_ACTION = "com.android.ObdCar";
    public static final String PAY_ACTION = "com.obd.pay";
    public static final String RECORD_ACTION = "com.android.Record";
    public static final String ALI_RESULT_9000 = "9000";
    public static final String ALI_RESULT_8000 = "8000";
    public static final String ALI_RESULT_5000 = "5000";
    public static final String ALI_RESULT_6002 = "6002";
    public static final String ALI_RESULT_6004 = "6004";
    public static final String ALI_RESULT_6001 = "6001";
    public static final String ALI_RESULT_4000 = "4000";
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

    /**
     * 从蓝牙聊天服务处理程序发送的消息类型
     */
    public static final int REQUEST_ENABLE_BT = 3;
    public static final String CONNECT_BT_KEY = "connect_bt_preference";

}
