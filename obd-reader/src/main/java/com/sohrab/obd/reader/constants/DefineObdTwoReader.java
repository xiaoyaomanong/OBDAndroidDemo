package com.sohrab.obd.reader.constants;

/**
 * 作者：Jealous
 * 日期：2021/1/21
 * 描述：
 */
public interface DefineObdTwoReader {
    String ACTION_OBD_CONNECTION_STATUS_CAR = "ACTION_OBD_CONNECTION_STATUS_CAR";
    /**
     * Real-time data
     */
    String ACTION_READ_OBD_REAL_TIME_DATA_CAR = "com.lzj.obd.reader.ACTION_READ_OBD_REAL_TIME_DATA";
    // intent key used to send data
    String INTENT_OBD_EXTRA_DATA_CAR = "com.lzj.obd.reader.INTENT_OBD_EXTRA_DATA";
}