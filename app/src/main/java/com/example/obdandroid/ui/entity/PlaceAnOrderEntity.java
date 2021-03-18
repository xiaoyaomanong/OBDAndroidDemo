package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/3/18 0018
 * 描述：
 */
public class PlaceAnOrderEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"order_no":"36577d094b044494b6e4ae9986460671","nonce_str":"XzWkpT1B4nZDlDOp","total_fee":"1990","sign":"9A83AB74D2D759D2181A5935C7365602","mch_id":"1606785775","prepay_id":"wx181427421062497cb9ff245b4536950000","timestamp":1616048861}
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
         * order_no : 36577d094b044494b6e4ae9986460671
         * nonce_str : XzWkpT1B4nZDlDOp
         * total_fee : 1990
         * sign : 9A83AB74D2D759D2181A5935C7365602
         * mch_id : 1606785775
         * prepay_id : wx181427421062497cb9ff245b4536950000
         * timestamp : 1616048861
         */

        private String order_no;
        private String nonce_str;
        private String total_fee;
        private String sign;
        private String mch_id;
        private String prepay_id;
        private int timestamp;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }
}