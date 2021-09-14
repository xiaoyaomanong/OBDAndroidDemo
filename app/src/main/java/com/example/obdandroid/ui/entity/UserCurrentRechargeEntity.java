package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/5/8 0008
 * 描述：
 */
public class UserCurrentRechargeEntity {


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
        private String rechargeSetMeaName;
        private String rechargeTime;
        private String rechargeStatusName;
        private double rechargetAmount;
        private String paymentChannelsName;
        private String contacts;
        private String telephone;
        private String address;
        private String expressName;
        private String courierNumber;
        private int commodityType;//商品类型1 实物 2 虚拟
        private long pid;
        private String rechargeRecordVo;

        public String getRechargeSetMeaName() {
            return rechargeSetMeaName;
        }

        public void setRechargeSetMeaName(String rechargeSetMeaName) {
            this.rechargeSetMeaName = rechargeSetMeaName;
        }

        public String getRechargeTime() {
            return rechargeTime;
        }

        public void setRechargeTime(String rechargeTime) {
            this.rechargeTime = rechargeTime;
        }

        public String getRechargeStatusName() {
            return rechargeStatusName;
        }

        public void setRechargeStatusName(String rechargeStatusName) {
            this.rechargeStatusName = rechargeStatusName;
        }

        public double getRechargetAmount() {
            return rechargetAmount;
        }

        public void setRechargetAmount(double rechargetAmount) {
            this.rechargetAmount = rechargetAmount;
        }

        public String getPaymentChannelsName() {
            return paymentChannelsName;
        }

        public void setPaymentChannelsName(String paymentChannelsName) {
            this.paymentChannelsName = paymentChannelsName;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getExpressName() {
            return expressName;
        }

        public void setExpressName(String expressName) {
            this.expressName = expressName;
        }

        public String getCourierNumber() {
            return courierNumber;
        }

        public void setCourierNumber(String courierNumber) {
            this.courierNumber = courierNumber;
        }

        public int getCommodityType() {
            return commodityType;
        }

        public void setCommodityType(int commodityType) {
            this.commodityType = commodityType;
        }

        public long getPid() {
            return pid;
        }

        public void setPid(long pid) {
            this.pid = pid;
        }

        public String getRechargeRecordVo() {
            return rechargeRecordVo;
        }

        public void setRechargeRecordVo(String rechargeRecordVo) {
            this.rechargeRecordVo = rechargeRecordVo;
        }
    }
}