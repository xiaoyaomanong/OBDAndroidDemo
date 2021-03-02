package com.example.obdandroid.ui.wechatPay;

/**
 * 作者：Jealous
 * 日期：2020/6/5 0005 09:37
 */
public class WechatPayEntity {


    /**
     * success : true
     * msg :
     * data : {"appid":"wx05aa8fd6cd87ce7f","partnerid":"1598107781","prepayid":"wx1016403838175148c7a49f431027423100","package":"Sign=WXPay","noncestr":"MWRWSGCUPQWGLEAGSOBBSYMSWM","timestamp":"1591778439","sign":"A0BF174126A15B910D35CC0BB32E4244"}
     */

    private boolean success;
    private String msg;
    private DataEntity data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * appid : wx05aa8fd6cd87ce7f
         * partnerid : 1598107781
         * prepayid : wx1016403838175148c7a49f431027423100
         * package : Sign=WXPay
         * noncestr : MWRWSGCUPQWGLEAGSOBBSYMSWM
         * timestamp : 1591778439
         * sign : A0BF174126A15B910D35CC0BB32E4244
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        private String packagevalue;
        private String orderNo;
        private String noncestr;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackagevalue() {
            return packagevalue;
        }

        public void setPackagevalue(String packagevalue) {
            this.packagevalue = packagevalue;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
