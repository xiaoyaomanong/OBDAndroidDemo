package com.sohrab.obd.reader.constants;

/**
 * Created by EMP203 on 5/17/2017.
 * <p>
 * provides constant used in Application
 */

public interface DefineObdReader {

    String ACTION_OBD_CONNECTION_STATUS = "ACTION_OBD_CONNECTION_STATUS";
    String ACTION_OBD_CONNECTION_STATUS_CAR = "ACTION_OBD_CONNECTION_STATUS_CAR";
    /**
     * Real-time data
     */
    String ACTION_READ_OBD_REAL_TIME_DATA = "com.sohrab.obd.reader.ACTION_READ_OBD_REAL_TIME_DATA";
    String ACTION_READ_OBD_REAL_TIME_DATA_CAR = "com.lzj.obd.reader.ACTION_READ_OBD_REAL_TIME_DATA";
    // intent key used to send data
     String INTENT_OBD_EXTRA_DATA = "com.sohrab.obd.reader.INTENT_OBD_EXTRA_DATA";
     String INTENT_OBD_EXTRA_DATA_CAR = "com.lzj.obd.reader.INTENT_OBD_EXTRA_DATA";

}
