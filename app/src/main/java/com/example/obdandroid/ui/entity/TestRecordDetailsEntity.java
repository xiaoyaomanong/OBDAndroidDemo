package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/3/25 0025
 * 描述：
 */
public class TestRecordDetailsEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"testRecordId":"1374918713771560962","vehicleId":"1368781680497922050","detectionTime":"2021-03-25 10:59:01","testData":"{\"engineRpm\":\"900RPM\",\"speed\":\"78km/h\"}","appUserId":"1364419027981832194"}
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
         * testRecordId : 1374918713771560962
         * vehicleId : 1368781680497922050
         * detectionTime : 2021-03-25 10:59:01
         * testData : {"engineRpm":"900RPM","speed":"78km/h"}
         * appUserId : 1364419027981832194
         */

        private String testRecordId;
        private String vehicleId;
        private String detectionTime;
        private String testData;
        private String appUserId;

        public String getTestRecordId() {
            return testRecordId;
        }

        public void setTestRecordId(String testRecordId) {
            this.testRecordId = testRecordId;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
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

        public String getAppUserId() {
            return appUserId;
        }

        public void setAppUserId(String appUserId) {
            this.appUserId = appUserId;
        }
    }
}