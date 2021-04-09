package com.example.obdandroid.ui.entity;

/**
 * 作者：Jealous
 * 日期：2021/4/9 0009
 * 描述：
 */
public class AlipayOrderEntity {

    /**
     * success : true
     * code : SUCCESS
     * message : 操作成功
     * data : {"order_no":"45b92acf92434ead806a91b52f59bb3c","orderStr":"alipay_sdk=alipay-sdk-java-4.9.79.ALL&amp;app_id=2021002125608234&amp;biz_content=%7B%22body%22%3A%2210%E6%AC%A119.9%22%2C%22out_trade_no%22%3A%2245b92acf92434ead806a91b52f59bb3c%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%2210%E6%AC%A119.9%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&amp;charset=UTF-8&amp;format=json&amp;method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Fxzbpkw.natappfree.cc%2Fapi%2Falipay&amp;sign=VvBHiA0KDiaMjs%2BD2EW%2FkdfzjQCupVxMOS5IEUIFocmHdyWW%2BdQ%2F16TEgN6fNoB6liokCzV6maYraJrc%2F0VnYvLne4qhNAguQM3RhIaDv60Lpq1ldTA0EoCvCViEH9y%2B4ePywyLZv4U%2Bup0t0vbTV2NFrt0VhIN2DKxiSFdL4h5pKFALzadCD935QfpdeiymVMo4d6vgPdzt6hR2SlkJEwVY59YXfwPVrQmpl9tJJHnIl7RCrwDVPdsrnO3WnNayOBkeohU4Rox97JLCZkE38gvn7I%2BTLdFj1h%2FPxgFERuANdzMuj5Gm49%2FqnsM39SbOmbVg9uQhLwjkWCxk99PFoQ%3D%3D&amp;sign_type=RSA2×tamp=2021-04-09+16%3A29%3A04&amp;version=1.0","total_fee":0.01}
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
         * order_no : 45b92acf92434ead806a91b52f59bb3c
         * orderStr : alipay_sdk=alipay-sdk-java-4.9.79.ALL&amp;app_id=2021002125608234&amp;biz_content=%7B%22body%22%3A%2210%E6%AC%A119.9%22%2C%22out_trade_no%22%3A%2245b92acf92434ead806a91b52f59bb3c%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%2210%E6%AC%A119.9%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&amp;charset=UTF-8&amp;format=json&amp;method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Fxzbpkw.natappfree.cc%2Fapi%2Falipay&amp;sign=VvBHiA0KDiaMjs%2BD2EW%2FkdfzjQCupVxMOS5IEUIFocmHdyWW%2BdQ%2F16TEgN6fNoB6liokCzV6maYraJrc%2F0VnYvLne4qhNAguQM3RhIaDv60Lpq1ldTA0EoCvCViEH9y%2B4ePywyLZv4U%2Bup0t0vbTV2NFrt0VhIN2DKxiSFdL4h5pKFALzadCD935QfpdeiymVMo4d6vgPdzt6hR2SlkJEwVY59YXfwPVrQmpl9tJJHnIl7RCrwDVPdsrnO3WnNayOBkeohU4Rox97JLCZkE38gvn7I%2BTLdFj1h%2FPxgFERuANdzMuj5Gm49%2FqnsM39SbOmbVg9uQhLwjkWCxk99PFoQ%3D%3D&amp;sign_type=RSA2×tamp=2021-04-09+16%3A29%3A04&amp;version=1.0
         * total_fee : 0.01
         */

        private String order_no;
        private String orderStr;
        private double total_fee;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getOrderStr() {
            return orderStr;
        }

        public void setOrderStr(String orderStr) {
            this.orderStr = orderStr;
        }

        public double getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(double total_fee) {
            this.total_fee = total_fee;
        }
    }
}