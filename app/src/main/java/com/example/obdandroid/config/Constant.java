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
    public static final String RECEIVER_ACTION = "com.example.obd.RECEIVER";
    public static final String ALI_RESULT_9000 = "9000";
    public static final String ALI_RESULT_8000 = "8000";
    public static final String ALI_RESULT_5000 = "5000";
    public static final String ALI_RESULT_6002 = "6002";
    public static final String ALI_RESULT_6004 = "6004";
    public static final String ALI_RESULT_6001 = "6001";
    public static final String ALI_RESULT_4000 = "4000";
    public static final String LOCATION_LATITUDE_SP_KEY = "latitude";
    public static final String LOCATION_lONGITUDE_SP_KEY = "longitude";
    public static final String GET_NEARBY_ADDRESSES_URL = "http://api.map.baidu.com/geocoder/v2/?ak=6eea93095ae93db2c77be9ac910ff311";
    /*================================发布版=========================================*/
    public static final String RELEASE_MD5 = "26:42:9A:58:4A:53:F3:8F:96:67:97:85:96:4C:DE:3F";
    public static final String RELEASE_SHA1 = "5A:9D:21:20:77:50:17:DD:BE:77:4A:9A:10:23:55:93:6D:EE:83:D3";
    public static final String RELEASE_SHA256 = "00:50:AE:22:89:B2:27:1E:DD:4F:C7:4A:EB:EE:59:CC:D2:8B:EA:7C:AF:C8:13:62:BD:A7:C6:B7:CF:B2:77:1D";

    /*================================开发版=========================================*/
    public static final String DEBUG_MD5 = "78:85:AF:D3:A3:A8:D6:74:35:AD:DF:B8:3A:EC:7F:43";
    public static final String DEBUG_SHA1 = "14:90:2D:D8:3E:20:BC:48:CB:EA:01:88:C0:31:65:5B:03:8D:3B:14";
    public static final String DEBUG_SHA256 = "B3:80:32:2A:FE:B6:31:2A:9C:DB:22:04:AC:81:69:77:83:96:7F:8B:FE:F1:F0:13:0F:C8:C1:62:A8:08:04:4D";

    public static final String MAP_AK = "jIzklXC45hyF9ytXChbzOr6ARLE0BCjK";//百度地图
    public static final String RELEASE_MCODE = "5A:9D:21:20:77:50:17:DD:BE:77:4A:9A:10:23:55:93:6D:EE:83:D3;com.example.obdandroid";//百度地图
    public static final String DEBUG_MCODE = "14:90:2D:D8:3E:20:BC:48:CB:EA:01:88:C0:31:65:5B:03:8D:3B:14;com.example.obdandroid";//百度地图

    public static final String MAP_SERVER_AK = "Wtyt52u9KTRM7PKbumEksKFoBFSYEBVp";//百度地图
    public static final String OUT_PUT = "json";//百度地图



    public static final int BT_TOAST = 102;
    public static final String TOAST = "toast";
    public static final int CONNECTED_DEVICE_NAME =103;
    public static final String DEVICE_NAME = "device_name";
    public static final int REC_DATA = 101;





    /**
     * 蓝牙UUID
     */
    public static UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //启用蓝牙
    public static final int BLUE_TOOTH_OPEN = 1000;

    /**
     * 从蓝牙聊天服务处理程序发送的消息类型
     */
    public static final int REQUEST_ENABLE_BT = 3;

}
