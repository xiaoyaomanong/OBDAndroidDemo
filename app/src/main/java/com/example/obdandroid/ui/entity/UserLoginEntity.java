package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/1/6 0006
 * 描述：
 */
public class UserLoginEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"userId":1346701207940632600,"token":"e1d2420ff46340038a2de6fade4574f2","expireTime":"2021-01-06T18:13:00.857+0000","updateTime":"2021-01-06T06:13:00.857+0000"}
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
         * userId : 1346701207940632600
         * token : e1d2420ff46340038a2de6fade4574f2
         * expireTime : 2021-01-06T18:13:00.857+0000
         * updateTime : 2021-01-06T06:13:00.857+0000
         */

        private String userId;
        private String token;
        private String expireTime;
        private String updateTime;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}