package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/1/12 0012
 * 描述：
 */
public class SMSVerificationCodeEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 发送成功
     * data : {"taskID":"10729367"}
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
         * taskID : 10729367
         */

        private String taskID;

        public String getTaskID() {
            return taskID;
        }

        public void setTaskID(String taskID) {
            this.taskID = taskID;
        }
    }
}