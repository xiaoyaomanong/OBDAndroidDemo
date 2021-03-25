package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/3/25 0025
 * 描述：
 */
public class AddTestRecordEntity {


    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"id":1374915046158897154,"vehicleId":1368781680497922050,"detectionTime":"2021-03-25T02:44:26.346+0000","testData":"{\"engineRpm\":\"900RPM\",\"speed\":\"78km/h\"}","appUserId":1364419027981832194,"platformType":1}
     */

    private boolean success;
    private String code;
    private String message;
    private DataEntity data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * id : 1374915046158897154
         * vehicleId : 1368781680497922050
         * detectionTime : 2021-03-25T02:44:26.346+0000
         * testData : {"engineRpm":"900RPM","speed":"78km/h"}
         * appUserId : 1364419027981832194
         * platformType : 1
         */

        private long id;
        private long vehicleId;
        private String detectionTime;
        private String testData;
        private long appUserId;
        private int platformType;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(long vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getDetectionTime() {
            return detectionTime;
        }

        public void setDetectionTime(String detectionTime) {
            this.detectionTime = detectionTime;
        }

        public String getTestData() {
            return testData;
        }

        public void setTestData(String testData) {
            this.testData = testData;
        }

        public long getAppUserId() {
            return appUserId;
        }

        public void setAppUserId(long appUserId) {
            this.appUserId = appUserId;
        }

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }
    }
}