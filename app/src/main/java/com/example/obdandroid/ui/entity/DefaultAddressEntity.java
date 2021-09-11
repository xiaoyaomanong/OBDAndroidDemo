package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/9/10 0010
 * 描述：
 */
public class DefaultAddressEntity {

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
        private long id;
        private long appUserId;
        private String telephone;
        private String contacts;
        private String address;
        private String createTiem;
        private boolean isDefault;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getAppUserId() {
            return appUserId;
        }

        public void setAppUserId(long appUserId) {
            this.appUserId = appUserId;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreateTiem() {
            return createTiem;
        }

        public void setCreateTiem(String createTiem) {
            this.createTiem = createTiem;
        }

        public boolean isIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }
    }
}